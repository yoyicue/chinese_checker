package com.yoyicue.chinesechecker.ui.game

import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex

// Compute shortest jump path via BFS from start to goal using current board occupancy.
// Supports both single-step and long jumps (mirror across any blocking piece with clear line).
fun bfsShortestJumpPath(board: Board, start: Hex, goal: Hex): List<Hex>? {
    if (start == goal) return listOf(start)
    // Single step move
    if (start.distanceTo(goal) == 1 && board.isEmpty(goal)) return listOf(start, goal)

    val queue: ArrayDeque<Hex> = ArrayDeque()
    val prev: MutableMap<Hex, Hex?> = mutableMapOf()
    queue.add(start)
    prev[start] = null

    while (queue.isNotEmpty()) {
        val cur = queue.removeFirst()
        for (n in jumpNeighbors(board, cur)) {
            if (n !in prev) {
                prev[n] = cur
                if (n == goal) {
                    return reconstruct(prev, goal)
                }
                queue.add(n)
            }
        }
    }
    return null
}

private fun reconstruct(prev: Map<Hex, Hex?>, end: Hex): List<Hex> {
    val path = ArrayList<Hex>()
    var cur: Hex? = end
    while (cur != null) {
        path.add(cur)
        cur = prev[cur]
    }
    path.reverse()
    return path
}

private fun jumpNeighbors(board: Board, from: Hex): List<Hex> {
    val res = ArrayList<Hex>()
    for (dir in Hex.DIRECTIONS) {
        val maxSteps = 8 // keep in sync with Board long jump capability
        steps@ for (k in 1..maxSteps) {
            val b = from.add(Hex(dir.x * k, dir.y * k, dir.z * k))
            val landing = from.add(Hex(dir.x * 2 * k, dir.y * 2 * k, dir.z * 2 * k))
            if (b !in board.positions || landing !in board.positions) break@steps
            // Cells strictly between from and landing except b must be empty
            for (i in 1 until 2 * k) {
                if (i == k) continue // b is occupied
                val cell = from.add(Hex(dir.x * i, dir.y * i, dir.z * i))
                if (cell !in board.positions || !board.isEmpty(cell)) continue@steps
            }
            if (!board.isEmpty(b) && board.isEmpty(landing)) {
                res.add(landing)
            }
        }
    }
    return res
}

