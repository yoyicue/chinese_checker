package com.yoyicue.chinesechecker.game

import kotlin.math.abs

/** Cube coordinates for hex grid (x + y + z = 0). */
data class Hex(val x: Int, val y: Int, val z: Int) {
    init {
        require(x + y + z == 0) { "x + y + z must be 0" }
    }

    fun add(o: Hex) = Hex(x + o.x, y + o.y, z + o.z)

    fun rotateCw(times: Int = 1): Hex {
        var h = this
        repeat((times % 6 + 6) % 6) {
            h = Hex(-h.z, -h.x, -h.y) // 60° CW
        }
        return h
    }

    fun distanceTo(o: Hex): Int = (abs(x - o.x) + abs(y - o.y) + abs(z - o.z)) / 2

    companion object {
        val DIRECTIONS = listOf(
            Hex(+1, -1, 0), Hex(+1, 0, -1), Hex(0, +1, -1),
            Hex(-1, +1, 0), Hex(-1, 0, +1), Hex(0, -1, +1)
        )

        fun origin() = Hex(0, 0, 0)
    }
}

