package com.yoyicue.chinesechecker.ui.game

import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Hex
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class PathfindingTest {

    @After
    fun resetLongJumpToggle() {
        Board.setLongJumps(false)
    }

    @Test
    fun bfsReturnsDirectStepWhenAdjacent() {
        val board = Board.standard(2)
        val start = board.startCampCells(Board.PlayerId.A).first()
        val neighbor = Hex.DIRECTIONS.map { start.add(it) }.first { it in board.positions }

        val occupant = mapOf(start to Board.PlayerId.A)
        val customBoard = Board.restore(playerCount = 2, occupant = occupant, currentPlayer = Board.PlayerId.A)

        val path = bfsShortestJumpPath(customBoard, start, neighbor)
        assertEquals(listOf(start, neighbor), path)
    }

    @Test
    fun bfsFindsJumpAcrossBlockingPiece() {
        val base = Board.standard(2)
        val goalCells = base.goalCampCells(Board.PlayerId.A)
        val target = goalCells.first()
        val dir = Hex.DIRECTIONS.firstOrNull { vector ->
            val one = target.add(Hex(-vector.x, -vector.y, -vector.z))
            val two = target.add(Hex(-vector.x * 2, -vector.y * 2, -vector.z * 2))
            one in base.positions && two in base.positions
        } ?: error("No suitable direction for jump test")

        val mid = target.add(Hex(-dir.x, -dir.y, -dir.z))
        val start = target.add(Hex(-dir.x * 2, -dir.y * 2, -dir.z * 2))

        val occupant = mutableMapOf(
            start to Board.PlayerId.A,
            mid to Board.PlayerId.B
        )
        val board = Board.restore(playerCount = 2, occupant = occupant, currentPlayer = Board.PlayerId.A)

        val path = bfsShortestJumpPath(board, start, target)
        assertEquals(listOf(start, target), path)
        assertTrue("Expected jump path of length 2", path!!.size == 2)
    }

    @Test
    fun bfsReturnsNullWhenLandingBlocked() {
        val base = Board.standard(2)
        val start = base.startCampCells(Board.PlayerId.A).first()
        val neighbor = Hex.DIRECTIONS.map { start.add(it) }.first { it in base.positions }
        val blocked = neighbor

        val occupant = mapOf(
            start to Board.PlayerId.A,
            blocked to Board.PlayerId.B
        )
        val board = Board.restore(playerCount = 2, occupant = occupant, currentPlayer = Board.PlayerId.A)

        val path = bfsShortestJumpPath(board, start, neighbor)
        assertNull("Landing occupied by opponent should prevent path", path)
    }
}
