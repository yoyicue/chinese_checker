package com.yoyicue.chinesechecker.game

import kotlin.comparisons.compareBy
import kotlin.math.abs
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

    private var cachedPositionsKey: Int? = null
    private var cachedIndexByHex: Map<Hex, Int>? = null
    private var cachedZobrist: Array<LongArray>? = null
    private var cachedSideZ: LongArray? = null

    private fun ensureHashSeeds(board: Board) {
        val key = System.identityHashCode(board.positions)
        if (cachedPositionsKey == key) return

        val posList = board.positions.toList().sortedWith(HEX_COMPARATOR)
        val indexMap = posList.withIndex().associate { it.value to it.index }
        val rnd = Random(0xC0FFEE)
        val zob = Array(posList.size) { LongArray(Board.PlayerId.values().size) { rnd.nextLong() } }
        val side = LongArray(Board.PlayerId.values().size) { rnd.nextLong() }

        cachedPositionsKey = key
        cachedIndexByHex = indexMap
        cachedZobrist = zob
        cachedSideZ = side
    }

    override fun chooseMove(board: Board, player: Board.PlayerId): Board.Move? {
        val moves = board.legalMovesFor(player)
        if (moves.isEmpty()) return null
        if (moves.size == 1) return moves.first()

        ensureHashSeeds(board)
        val indexByHex = cachedIndexByHex ?: error("Index cache missing")
        val zob = cachedZobrist ?: error("Zobrist cache missing")
        val sideZ = cachedSideZ ?: error("Side cache missing")
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
        val rootChildren = baseOrder(moves).toMutableList()

        var bestMove: Board.Move? = null
        val rootHash = hash(board)
        var depth = 1

        fun cancelled() = Thread.interrupted() || System.nanoTime() >= deadline

        while (depth <= maxDepth && !cancelled()) {
            val pvMove = bestMove ?: tt[rootHash]?.best
            if (pvMove != null) {
                rootChildren.sortWith(
                    compareByDescending<RootChild> { it.move == pvMove }
                        .thenByDescending { it.priority }
                )
            }
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
        if (w != null) return if (w == me) WIN_SCORE else -WIN_SCORE
        if (depth == 0 || System.nanoTime() >= deadlineNs || Thread.interrupted()) {
            return quiescence(board, me, alphaInit, betaInit, QUIESCENCE_MAX_DEPTH, deadlineNs)
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
        data class ChildNode(
            val move: Board.Move,
            val boardAfter: Board,
            val evalForOrder: Int,
            val isTtBest: Boolean,
            val killerRank: Int,
            val isJump: Boolean,
            val historyScore: Int
        )
        val ordered = moves.map { m ->
            val b2 = board.copy().apply(m)
            val eval = score(b2, me)
            val evalForOrder = if (current == me) eval else -eval
            val killerRank = when {
                killers1.getOrNull(ply) == m -> 2
                killers2.getOrNull(ply) == m -> 1
                else -> 0
            }
            val keyFT = fromToKey(m, indexByHex)
            val historyScore = keyFT?.let { history[it] ?: 0 } ?: 0
            ChildNode(
                move = m,
                boardAfter = b2,
                evalForOrder = evalForOrder,
                isTtBest = (ttBest != null && m == ttBest),
                killerRank = killerRank,
                isJump = m.isJump,
                historyScore = historyScore
            )
        }.sortedWith(
            compareByDescending<ChildNode> { it.evalForOrder }
                .thenByDescending { it.isTtBest }
                .thenByDescending { it.killerRank }
                .thenByDescending { it.isJump }
                .thenByDescending { it.historyScore }
        )

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

    private fun quiescence(
        board: Board,
        me: Board.PlayerId,
        alphaInit: Int,
        betaInit: Int,
        depth: Int,
        deadlineNs: Long
    ): Int {
        val winner = board.winner()
        if (winner != null) {
            return if (winner == me) WIN_SCORE else -WIN_SCORE
        }
        if (depth <= 0 || System.nanoTime() >= deadlineNs || Thread.interrupted()) {
            return score(board, me)
        }

        var alpha = alphaInit
        var beta = betaInit
        val maximizing = board.currentPlayer == me
        val standPat = score(board, me)

        if (maximizing) {
            if (standPat >= beta) return standPat
            if (standPat > alpha) alpha = standPat
        } else {
            if (standPat <= alpha) return standPat
            if (standPat < beta) beta = standPat
        }

        val moves = board.legalMovesFor(board.currentPlayer)
        if (moves.isEmpty()) return standPat

        val tactical = selectTacticalMoves(board, me, moves, maximizing)
        if (tactical.isEmpty()) return standPat

        var value = standPat
        for (candidate in tactical) {
            val childScore = quiescence(
                candidate.boardAfter,
                me,
                alpha,
                beta,
                depth - 1,
                deadlineNs
            )
            if (maximizing) {
                if (childScore > value) value = childScore
                if (value > alpha) alpha = value
                if (alpha >= beta) return value
            } else {
                if (childScore < value) value = childScore
                if (value < beta) beta = value
                if (alpha >= beta) return value
            }
        }
        return value
    }

    private data class TacticCandidate(
        val move: Board.Move,
        val boardAfter: Board,
        val evalForOrder: Int,
        val freesHome: Boolean,
        val centerBias: Int
    )

    private fun selectTacticalMoves(
        board: Board,
        me: Board.PlayerId,
        moves: List<Board.Move>,
        maximizing: Boolean
    ): List<TacticCandidate> {
        if (moves.isEmpty()) return emptyList()
        val currentPlayer = board.currentPlayer
        val startCamp = board.startCampCells(currentPlayer)

        fun heuristic(move: Board.Move): Int {
            var h = 0
            if (move.isJump) h += 16
            if (move.from in startCamp && move.to !in startCamp) h += 8
            h += (6 - move.to.distanceTo(CENTER_HEX)).coerceAtLeast(0)
            if (move.path.size > 2) h += move.path.size
            return h
        }

        val jumpPrefilter = moves.asSequence()
            .filter { it.isJump }
            .sortedByDescending { heuristic(it) }
            .take(QUIESCENCE_PREFILTER_CAP)
            .toList()

        val slidePrefilter = moves.asSequence()
            .filterNot { it.isJump }
            .sortedByDescending { heuristic(it) }
            .take(QUIESCENCE_PREFILTER_SLIDE_CAP)
            .toList()

        val prefiltered = (jumpPrefilter + slidePrefilter).ifEmpty { moves.take(QUIESCENCE_PREFILTER_CAP) }

        val comparator = compareByDescending<TacticCandidate> { it.evalForOrder }
            .thenByDescending { it.freesHome }
            .thenByDescending { it.centerBias }

        val candidates = prefiltered.map { move ->
            val b2 = board.copy().apply(move)
            val eval = score(b2, me)
            val evalForOrder = if (maximizing) eval else -eval
            val freesHome = move.from in startCamp && move.to !in startCamp
            val centerBias = -move.to.distanceTo(CENTER_HEX)
            TacticCandidate(move, b2, evalForOrder, freesHome, centerBias)
        }.sortedWith(comparator)

        val jumpCandidates = candidates.filter { it.move.isJump }.take(QUIESCENCE_JUMP_CAP)
        val slideCandidates = candidates.filterNot { it.move.isJump }.take(QUIESCENCE_SLIDE_CAP)

        return buildList { addAll(jumpCandidates); addAll(slideCandidates) }
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

        val myEntered = enteredCount(b, me)
        val oppEntered = others.maxOf { enteredCount(b, it) }
        val enteredDiff = myEntered - oppEntered
        val rawBonus = enteredDiff * enteredWeight(myEff)
        val capFromRelative = (abs(relative) * ENTERED_RELATIVE_CAP_PERCENT) / 100
        val cap = if (capFromRelative > 0) capFromRelative.coerceAtLeast(ENTERED_ABS_CAP_MIN) else ENTERED_ABS_CAP_MIN
        val limitedBonus = rawBonus.coerceIn(-cap, cap)
        return relative + limitedBonus
    }
}

private val CENTER_HEX: Hex = Hex.origin()
private const val WIN_SCORE = 1_000_000
private const val QUIESCENCE_JUMP_CAP = 12
private const val QUIESCENCE_SLIDE_CAP = 4
private const val QUIESCENCE_PREFILTER_CAP = 24
private const val QUIESCENCE_PREFILTER_SLIDE_CAP = 8
private const val QUIESCENCE_MAX_DEPTH = 2
private const val ROOT_ORDER_SCORE_SCALE = 1_000L
private const val ENTERED_RELATIVE_CAP_PERCENT = 60
private const val ENTERED_ABS_CAP_MIN = 120
private val HEX_COMPARATOR = compareBy<Hex>({ it.x }, { it.y }, { it.z })

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

private fun enteredWeight(myEff: Int): Int {
    val clamped = myEff.coerceIn(0, 900)
    val scaled = 110 - (70 * clamped) / 900
    return scaled.coerceIn(40, 110)
}
