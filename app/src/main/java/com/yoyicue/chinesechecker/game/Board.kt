package com.yoyicue.chinesechecker.game

import kotlin.math.abs

/**
 * Chinese Checkers board with 121 positions: hex radius 4 core + 6 arms of length 4.
 * Coordinates: cube hex (x+y+z=0). A position is valid if max(|x|,|y|,|z|) <= 8 and min(|x|,|y|,|z|) <= 4.
 *
 * Supports 2/3/4/6 players by mapping players to the six camps (triangles) and
 * defining each player's goal as the opposite camp.
 */
class Board private constructor(
    val positions: Set<Hex>,
    val occupant: MutableMap<Hex, PlayerId>,
    val activePlayers: List<PlayerId>,
    val campCells: Map<Camp, Set<Hex>>, // Cells for each of the 6 camps
    val startCampOf: Map<PlayerId, Camp>,
    val goalCampOf: Map<PlayerId, Camp>
) {
    enum class PlayerId { A, B, C, D, E, F }

    enum class Camp { Top, Bottom, NE, SW, NW, SE; }

    data class Move(val path: List<Hex>) {
        val from: Hex get() = path.first()
        val to: Hex get() = path.last()
        val isJump: Boolean get() = path.size > 2 || (path.size == 2 && from.distanceTo(to) > 1)
    }

    fun copy(): Board {
        val b = Board(
            positions.toSet(),
            occupant.toMutableMap(),
            activePlayers.toList(),
            campCells.mapValues { it.value.toSet() },
            startCampOf.toMap(),
            goalCampOf.toMap()
        )
        b.currentPlayer = this.currentPlayer
        return b
    }

    fun at(h: Hex): PlayerId? = occupant[h]

    fun isEmpty(h: Hex) = h in positions && occupant[h] == null

    fun allPieces(p: PlayerId): List<Hex> = occupant.filterValues { it == p }.keys.toList()

    fun legalMovesFrom(h: Hex): List<Move> {
        val who = occupant[h] ?: return emptyList()
        if (who != currentPlayer) return emptyList()

        val stepTargets = Hex.DIRECTIONS.map { h.add(it) }
            .filter { it in positions && isEmpty(it) }
            .map { Move(listOf(h, it)) }

        val jumpMoves = allJumpsFrom(h)
        return stepTargets + jumpMoves
    }

    fun legalMovesFor(p: PlayerId): List<Move> = allPieces(p).flatMap { legalMovesFrom(it) }

    private fun allJumpsFrom(start: Hex): List<Move> {
        val results = mutableListOf<Move>()

        fun dfs(current: Hex, path: MutableList<Hex>) {
            for (dir in Hex.DIRECTIONS) {
                val maxSteps = if (ALLOW_LONG_JUMPS) 8 else 1
                steps@ for (k in 1..maxSteps) {
                    val b = current.add(Hex(dir.x * k, dir.y * k, dir.z * k))
                    val landing = current.add(Hex(dir.x * 2 * k, dir.y * 2 * k, dir.z * 2 * k))
                    if (b !in positions || landing !in positions) break@steps

                    // All cells strictly between start and landing except b must be empty
                    for (i in 1 until 2 * k) {
                        if (i == k) continue // this is b
                        val cell = current.add(Hex(dir.x * i, dir.y * i, dir.z * i))
                        if (cell !in positions || !isEmpty(cell)) continue@steps
                    }
                    if (!isEmpty(b) && isEmpty(landing)) {
                        if (landing !in path) {
                            path.add(landing)
                            results.add(Move(path.toList()))
                            dfs(landing, path)
                            path.removeAt(path.lastIndex)
                        }
                    }
                }
            }
        }

        dfs(start, mutableListOf(start))
        return results
    }

    fun apply(move: Move): Board {
        val who = occupant[move.from] ?: error("No piece at ${move.from}")
        require(who == currentPlayer)
        occupant.remove(move.from)
        occupant[move.to] = who

        // No captures in Chinese Checkers.
        val idx = activePlayers.indexOf(currentPlayer)
        val next = if (idx == -1) 0 else (idx + 1) % activePlayers.size
        currentPlayer = activePlayers[next]
        return this
    }

    // Skip current player's turn (used by timer timeout). Mutates and returns self.
    fun pass(): Board {
        val idx = activePlayers.indexOf(currentPlayer)
        val next = if (idx == -1) 0 else (idx + 1) % activePlayers.size
        currentPlayer = activePlayers[next]
        return this
    }

    

    fun winner(): PlayerId? {
        for (p in activePlayers) {
            val goal = goalCampOf[p]?.let { campCells[it] } ?: continue
            if (allPieces(p).toSet() == goal) return p
        }
        return null
    }

    // Player to move
    var currentPlayer: PlayerId = PlayerId.A
        private set

    fun goalCampCells(player: PlayerId): Set<Hex> = goalCampOf[player]?.let { campCells[it] } ?: emptySet()
    fun startCampCells(player: PlayerId): Set<Hex> = startCampOf[player]?.let { campCells[it] } ?: emptySet()

    companion object {
        // Default: reduce branching for AI; toggled via Settings
        @Volatile
        private var ALLOW_LONG_JUMPS: Boolean = false

        fun setLongJumps(enabled: Boolean) { ALLOW_LONG_JUMPS = enabled }

        private fun buildPositions(): Set<Hex> = buildSet {
            for (x in -8..8) for (y in -8..8) {
                val z = -x - y
                if (z < -8 || z > 8) continue
                val ax = abs(x); val ay = abs(y); val az = abs(z)
                val maxAbs = maxOf(ax, ay, az)
                if (maxAbs > 8) continue
                val countLe4 = (if (ax <= 4) 1 else 0) + (if (ay <= 4) 1 else 0) + (if (az <= 4) 1 else 0)
                if (countLe4 >= 2) add(Hex(x, y, z))
            }
        }

        private fun computeCampCells(positions: Set<Hex>): Map<Camp, Set<Hex>> {
            val top = positions.filter { it.z < -4 && abs(it.x) <= 4 && abs(it.y) <= 4 }.toSet()
            val bottom = positions.filter { it.z > 4 && abs(it.x) <= 4 && abs(it.y) <= 4 }.toSet()
            val ne = positions.filter { it.x > 4 && abs(it.y) <= 4 && abs(it.z) <= 4 }.toSet()
            val sw = positions.filter { it.x < -4 && abs(it.y) <= 4 && abs(it.z) <= 4 }.toSet()
            val nw = positions.filter { it.y > 4 && abs(it.x) <= 4 && abs(it.z) <= 4 }.toSet()
            val se = positions.filter { it.y < -4 && abs(it.x) <= 4 && abs(it.z) <= 4 }.toSet()
            return mapOf(
                Camp.Top to top,
                Camp.Bottom to bottom,
                Camp.NE to ne,
                Camp.SW to sw,
                Camp.NW to nw,
                Camp.SE to se
            )
        }

        private fun oppositeOf(camp: Camp): Camp = when (camp) {
            Camp.Top -> Camp.Bottom
            Camp.Bottom -> Camp.Top
            Camp.NE -> Camp.SW
            Camp.SW -> Camp.NE
            Camp.NW -> Camp.SE
            Camp.SE -> Camp.NW
        }

        private data class Assignment(
            val order: List<PlayerId>,
            val start: Map<PlayerId, Camp>
        )

        private fun defaultAssignment(playerCount: Int): Assignment {
            require(playerCount in setOf(2, 3, 4, 6)) { "playerCount must be 2,3,4,or 6" }
            return when (playerCount) {
                2 -> Assignment(
                    order = listOf(PlayerId.A, PlayerId.B),
                    start = mapOf(PlayerId.A to Camp.Top, PlayerId.B to Camp.Bottom)
                )
                3 -> Assignment(
                    // Alternate triangles (every other camp)
                    order = listOf(PlayerId.A, PlayerId.B, PlayerId.C),
                    start = mapOf(
                        PlayerId.A to Camp.Top,
                        PlayerId.B to Camp.SW,
                        PlayerId.C to Camp.SE
                    )
                )
                4 -> Assignment(
                    // Two opposing pairs
                    order = listOf(PlayerId.A, PlayerId.B, PlayerId.C, PlayerId.D),
                    start = mapOf(
                        PlayerId.A to Camp.Top,
                        PlayerId.B to Camp.Bottom,
                        PlayerId.C to Camp.NE,
                        PlayerId.D to Camp.SW
                    )
                )
                else -> Assignment(
                    // 6 players occupy all camps. Turn order clockwise from Top: Top -> NE -> SE -> Bottom -> SW -> NW
                    order = listOf(PlayerId.A, PlayerId.C, PlayerId.F, PlayerId.B, PlayerId.D, PlayerId.E),
                    start = mapOf(
                        PlayerId.A to Camp.Top,
                        PlayerId.B to Camp.Bottom,
                        PlayerId.C to Camp.NE,
                        PlayerId.D to Camp.SW,
                        PlayerId.E to Camp.NW,
                        PlayerId.F to Camp.SE
                    )
                )
            }
        }

        fun standard(): Board = standard(2)

        fun standard(playerCount: Int): Board {
            val pos = buildPositions()
            val camps = computeCampCells(pos)
            val asg = defaultAssignment(playerCount)
            val goal = asg.start.mapValues { (_, camp) -> oppositeOf(camp) }

            val occ = mutableMapOf<Hex, PlayerId>()
            for ((p, camp) in asg.start) {
                val cells = camps[camp] ?: continue
                for (h in cells) occ[h] = p
            }

            val b = Board(
                positions = pos,
                occupant = occ,
                activePlayers = asg.order,
                campCells = camps,
                startCampOf = asg.start,
                goalCampOf = goal
            )
            b.currentPlayer = asg.order.first()
            return b
        }

        fun restore(
            playerCount: Int,
            occupant: Map<Hex, PlayerId>,
            currentPlayer: PlayerId
        ): Board {
            val pos = buildPositions()
            val camps = computeCampCells(pos)
            val asg = defaultAssignment(playerCount)
            val goal = asg.start.mapValues { (_, camp) -> oppositeOf(camp) }
            val b = Board(
                positions = pos,
                occupant = occupant.toMutableMap(),
                activePlayers = asg.order,
                campCells = camps,
                startCampOf = asg.start,
                goalCampOf = goal
            )
            b.currentPlayer = currentPlayer
            return b
        }
    }
}
