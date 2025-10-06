package com.yoyicue.chinesechecker.game

import kotlin.random.Random

interface Bot {
    fun chooseMove(board: Board, player: Board.PlayerId): Board.Move?
}

enum class AiDifficulty { Weak, Greedy, Smart }

object BotFactory {
    fun create(level: AiDifficulty): Bot = when (level) {
        AiDifficulty.Weak -> WeakBot()
        AiDifficulty.Greedy -> GreedyBot()
        AiDifficulty.Smart -> SmartBot()
    }
}

class WeakBot(private val rnd: Random = Random.Default) : Bot {
    override fun chooseMove(board: Board, player: Board.PlayerId): Board.Move? {
        val moves = board.legalMovesFor(player)
        if (moves.isEmpty()) return null

        val startCamp = board.startCampCells(player)
        val baseline = progressMeasure(board, player)

        data class Entry(val move: Board.Move, val gain: Int, val isJump: Boolean, val freesHome: Boolean)
        val scored = moves.map { move ->
            val after = board.copy().apply(move)
            val gain = baseline - progressMeasure(after, player)
            val freesHome = move.from in startCamp && move.to !in startCamp
            Entry(move, gain, move.isJump, freesHome)
        }

        val jumpPref = scored.filter { it.isJump }
        val pool = if (jumpPref.isNotEmpty()) jumpPref else scored

        val weights = pool.map { entry ->
            val base = when {
                entry.gain > 0 -> 3.0 + entry.gain
                entry.gain == 0 -> 1.15
                else -> 0.45 / (1 - entry.gain)
            }
            val jumpBonus = if (entry.isJump) 1.4 else 1.0
            val homeBonus = if (entry.freesHome) 1.6 else 1.0
            (base * jumpBonus * homeBonus).coerceAtLeast(0.05)
        }
        val sumW = weights.sum().takeIf { it > 0 } ?: return pool.random(rnd).move
        val threshold = rnd.nextDouble(sumW)
        var acc = 0.0
        for ((index, entry) in pool.withIndex()) {
            acc += weights[index]
            if (threshold <= acc) return entry.move
        }
        return pool.last().move
    }
}

class GreedyBot : Bot {
    override fun chooseMove(board: Board, player: Board.PlayerId): Board.Move? {
        val moves = board.legalMovesFor(player)
        if (moves.isEmpty()) return null
        return moves.minBy { evalAfter(board.copy(), it, player) }
    }

    private fun evalAfter(b: Board, move: Board.Move, me: Board.PlayerId): Int {
        b.apply(move)
        return progressMeasure(b, me)
    }
}

/**
 * Minimax with alpha-beta pruning and simple move ordering.
 * - Iterative deepening within a small time budget.
 * - For games with >2 players, uses a Paranoid approximation (others minimize my score).
 */
class SmartBot(
    private val timeBudgetMs: Long = 450,
    private val maxDepth: Int = 6,
    private val ttCapacity: Int = 50_000
) : Bot {
    private enum class Bound { EXACT, LOWER, UPPER }
    private data class TTEntry(val key: Long, val depth: Int, val score: Int, val bound: Bound, val best: Board.Move?)

    override fun chooseMove(board: Board, player: Board.PlayerId): Board.Move? {
        val moves = board.legalMovesFor(player)
        if (moves.isEmpty()) return null
        if (moves.size == 1) return moves.first()

        // Zobrist init per search
        val posList = board.positions.toList()
        val indexByHex = posList.withIndex().associate { it.value to it.index }
        val rnd = Random(0xC0FFEE)
        val zob = Array(posList.size) { LongArray(Board.PlayerId.values().size) { rnd.nextLong() } }
        val sideZ = LongArray(Board.PlayerId.values().size) { rnd.nextLong() }
        fun hash(b: Board): Long {
            var h = 1469598103934665603L // FNV offset
            for ((hex, p) in b.occupant) {
                val idx = indexByHex[hex] ?: continue
                h = h xor zob[idx][p.ordinal]
            }
            h = h xor sideZ[b.currentPlayer.ordinal]
            return h
        }

        val tt = java.util.LinkedHashMap<Long, TTEntry>(ttCapacity, 0.75f, true)
        fun ttPut(e: TTEntry) {
            tt[e.key] = e
            if (tt.size > ttCapacity) {
                val it = tt.entries.iterator()
                if (it.hasNext()) { it.next(); it.remove() }
            }
        }

        // Killer moves and history heuristics
        val killers1 = Array<Board.Move?>(maxDepth + 16) { null }
        val killers2 = Array<Board.Move?>(maxDepth + 16) { null }
        val history = HashMap<Pair<Int, Int>, Int>(8192)
        fun fromToKey(m: Board.Move): Pair<Int, Int>? {
            val fi = indexByHex[m.from] ?: return null
            val ti = indexByHex[m.to] ?: return null
            return fi to ti
        }

        val deadline = System.nanoTime() + timeBudgetMs * 1_000_000

        // Base ordering by greedy improvement
        data class RootChild(val move: Board.Move, val boardAfter: Board, val priority: Long)
        val startCamp = board.startCampCells(player)
        fun baseOrder(ms: List<Board.Move>): List<RootChild> = ms.map { m ->
            val b = board.copy().apply(m)
            val scoreValue = score(b, player).toLong()
            val jumpBonus = if (m.isJump) 10L else 0L
            val homeBreak = if (m.from in startCamp && m.to !in startCamp) 1L else 0L
            val pri = scoreValue * ROOT_ORDER_SCORE_SCALE + jumpBonus + homeBreak
            RootChild(m, b, pri)
        }.sortedByDescending { it.priority }
        val rootChildren = baseOrder(moves)

        var bestMove: Board.Move? = null
        var depth = 1

        fun cancelled() = Thread.interrupted() || System.nanoTime() >= deadline

        while (depth <= maxDepth && !cancelled()) {
            var localBest: Board.Move? = bestMove
            var localBestScore = Int.MIN_VALUE
            var alphaRoot = Int.MIN_VALUE + 1
            val betaRoot = Int.MAX_VALUE - 1
            for (child in rootChildren) {
                if (cancelled()) break
                val b2 = child.boardAfter
                val v = alphaBeta(
                    b2, player, depth - 1,
                    alphaRoot, betaRoot, deadline,
                    indexByHex, zob, sideZ, tt, ::ttPut,
                    killers1, killers2, history, ply = 0
                )
                if (v > localBestScore) {
                    localBestScore = v
                    localBest = child.move
                }
                if (v > alphaRoot) alphaRoot = v
            }
            if (localBest != null) {
                bestMove = localBest
            }
            depth += 1
        }
        return bestMove ?: rootChildren.first().move
    }

    private fun alphaBeta(
        board: Board,
        me: Board.PlayerId,
        depth: Int,
        alphaInit: Int,
        betaInit: Int,
        deadlineNs: Long,
        indexByHex: Map<Hex, Int>,
        zob: Array<LongArray>,
        sideZ: LongArray,
        tt: java.util.LinkedHashMap<Long, TTEntry>,
        ttPut: (TTEntry) -> Unit,
        killers1: Array<Board.Move?>,
        killers2: Array<Board.Move?>,
        history: MutableMap<Pair<Int, Int>, Int>,
        ply: Int
    ): Int {
        fun hash(b: Board): Long {
            var h = 1469598103934665603L
            for ((hex, p) in b.occupant) {
                val idx = indexByHex[hex] ?: continue
                h = h xor zob[idx][p.ordinal]
            }
            h = h xor sideZ[b.currentPlayer.ordinal]
            return h
        }

        // Terminal
        val w = board.winner()
        if (w != null) return if (w == me) 1_000_000 else -1_000_000
        if (depth == 0 || System.nanoTime() >= deadlineNs || Thread.interrupted()) {
            val current = board.currentPlayer
            val moves0 = board.legalMovesFor(current)
            if (moves0.isEmpty()) return score(board, me)

            val jumpMoves = moves0.filter { it.isJump }
            val considerSlides = jumpMoves.isEmpty() || jumpMoves.size <= QUIESCENCE_SLIDE_TRIGGER

            val candidates = mutableListOf<Pair<Board.Move, Int>>()

            fun evaluate(move: Board.Move): Pair<Board.Move, Int> {
                val b2 = board.copy().apply(move)
                val sc = score(b2, me)
                return move to sc
            }

            if (jumpMoves.isNotEmpty()) {
                val orderedJumps = jumpMoves
                    .map(::evaluate)
                    .sortedBy { if (current == me) -it.second else it.second }
                candidates += orderedJumps.take(QUIESCENCE_JUMP_CAP)
            }

            if (considerSlides) {
                val slides = moves0.asSequence().filterNot { it.isJump }
                    .map(::evaluate)
                    .sortedBy { if (current == me) -it.second else it.second }
                    .take(QUIESCENCE_SLIDE_CAP)
                candidates += slides
            }

            if (candidates.isEmpty()) {
                return score(board, me)
            }

            val fallback = score(board, me)
            val values = candidates.map { it.second }
            return if (current == me) values.maxOrNull() ?: fallback else values.minOrNull() ?: fallback
        }

        var alpha = alphaInit
        var beta = betaInit
        val current = board.currentPlayer
        val key = hash(board)

        // Probe TT
        val ttEntry = tt[key]
        if (ttEntry != null && ttEntry.depth >= depth) {
            when (ttEntry.bound) {
                Bound.EXACT -> return ttEntry.score
                Bound.LOWER -> if (ttEntry.score >= beta) return ttEntry.score
                Bound.UPPER -> if (ttEntry.score <= alpha) return ttEntry.score
            }
        }

        // Generate moves
        val moves = board.legalMovesFor(current)
        if (moves.isEmpty()) {
            val b2 = board.copy().pass()
            return alphaBeta(b2, me, depth - 1, alpha, beta, deadlineNs, indexByHex, zob, sideZ, tt, ttPut, killers1, killers2, history, ply + 1)
        }

        // Build ordering: TT best, killers, history, then greedy
        val ttBest = ttEntry?.best
        data class ChildNode(val move: Board.Move, val boardAfter: Board, val priority: Int)
        val ordered = moves.map { m ->
            var pri = 0
            if (ttBest != null && m == ttBest) pri += 1_000_000
            if (killers1.getOrNull(ply) == m) pri += 500_000
            if (killers2.getOrNull(ply) == m) pri += 400_000
            if (m.isJump) pri += 300_000
            val keyFT = fromToKey(m, indexByHex)
            if (keyFT != null) pri += (history[keyFT] ?: 0)
            val b2 = board.copy().apply(m)
            val s = score(b2, me)
            ChildNode(m, b2, pri + if (current == me) s else -s)
        }.sortedByDescending { it.priority }

        val maximizing = (current == me)
        var bestMove: Board.Move? = null
        var value = if (maximizing) Int.MIN_VALUE else Int.MAX_VALUE
        val origAlpha = alpha
        val origBeta = beta

        var searchedAny = false
        for (child in ordered) {
            if (System.nanoTime() >= deadlineNs || Thread.interrupted()) break
            val childScore = alphaBeta(child.boardAfter, me, depth - 1, alpha, beta, deadlineNs, indexByHex, zob, sideZ, tt, ttPut, killers1, killers2, history, ply + 1)
            searchedAny = true
            if (maximizing) {
                if (childScore > value) { value = childScore; bestMove = child.move }
                if (value > alpha) alpha = value
                if (alpha >= beta) {
                    // beta cut -> update killers/history
                    if (killers1[ply] != child.move) { killers2[ply] = killers1[ply]; killers1[ply] = child.move }
                    val ft = fromToKey(child.move, indexByHex)
                    if (ft != null) history[ft] = (history[ft] ?: 0) + depth * depth
                    break
                }
            } else {
                if (childScore < value) { value = childScore; bestMove = child.move }
                if (value < beta) beta = value
                if (alpha >= beta) {
                    if (killers1[ply] != child.move) { killers2[ply] = killers1[ply]; killers1[ply] = child.move }
                    val ft = fromToKey(child.move, indexByHex)
                    if (ft != null) history[ft] = (history[ft] ?: 0) + depth * depth
                    break
                }
            }
        }

        // Store TT
        if (!searchedAny) return score(board, me)
        val bound = when {
            value <= origAlpha -> Bound.UPPER
            value >= origBeta -> Bound.LOWER
            else -> Bound.EXACT
        }
        ttPut(TTEntry(key, depth, value, bound, bestMove))
        return value
    }

    private fun fromToKey(m: Board.Move, indexByHex: Map<Hex, Int>): Pair<Int, Int>? {
        val fi = indexByHex[m.from] ?: return null
        val ti = indexByHex[m.to] ?: return null
        return fi to ti
    }

    private fun score(b: Board, me: Board.PlayerId): Int {
        val others = b.activePlayers.filter { it != me }
        if (others.isEmpty()) return -effectiveDistance(b, me)

        val myEff = effectiveDistance(b, me)
        val relative = if (others.size == 1) {
            effectiveDistance(b, others[0]) - myEff
        } else {
            val closest = others.minOf { effectiveDistance(b, it) }
            closest - myEff
        }

        val enteredDiff = enteredCount(b, me) - others.sumOf { enteredCount(b, it) }
        val bonus = enteredDiff * enteredWeight(myEff)
        return relative + bonus
    }
}

private val CENTER_HEX: Hex = Hex.origin()
private const val QUIESCENCE_JUMP_CAP = 12
private const val QUIESCENCE_SLIDE_CAP = 4
private const val QUIESCENCE_SLIDE_TRIGGER = 6
private const val ROOT_ORDER_SCORE_SCALE = 1_000L

private fun distanceSumToGoal(board: Board, player: Board.PlayerId): Int {
    val target = board.goalCampCells(player)
    val pieces = board.allPieces(player)
    val remaining = target.toMutableSet()
    var sum = 0
    for (piece in pieces) {
        val best = remaining.minByOrNull { it.distanceTo(piece) }
        if (best != null) {
            sum += best.distanceTo(piece)
            remaining.remove(best)
        }
    }
    return sum
}

private fun homePenalty(board: Board, player: Board.PlayerId): Int {
    val start = board.startCampCells(player)
    if (start.isEmpty()) return 0
    val pieces = board.allPieces(player)
    val inside = pieces.filter { it in start }
    if (inside.isEmpty()) return 0
    val outsideCount = pieces.size - inside.size
    val base = inside.size * 60
    val pressure = inside.size * outsideCount * 15
    val distance = inside.sumOf { minOf(it.distanceTo(CENTER_HEX), 6) } * 12
    return base + pressure + distance
}

private fun effectiveDistance(board: Board, player: Board.PlayerId): Int {
    return distanceSumToGoal(board, player) + homePenalty(board, player)
}

private fun progressMeasure(board: Board, player: Board.PlayerId): Int = effectiveDistance(board, player)

private fun enteredCount(board: Board, player: Board.PlayerId): Int {
    val goal = board.goalCampCells(player)
    return board.allPieces(player).count { it in goal }
}

private fun enteredWeight(myEff: Int): Int = when {
    myEff >= 600 -> 60
    myEff >= 300 -> 100
    else -> 40
}
