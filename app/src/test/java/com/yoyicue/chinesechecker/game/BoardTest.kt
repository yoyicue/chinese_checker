package com.yoyicue.chinesechecker.game

import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BoardTest {

    @After
    fun tearDown() {
        // Ensure global long-jump toggle is reset for other tests.
        Board.setLongJumps(false)
    }

    @Test
    fun standardBoard_has121CellsAndExpectedPieceCount() {
        val board = Board.standard(2)

        assertEquals("Board should expose 121 playable hexes", 121, board.positions.size)
        assertEquals("Two-player setup should place 20 pieces", 20, board.occupant.size)
        assertEquals("Player A starts with 10 pieces", 10, board.allPieces(Board.PlayerId.A).size)
        assertEquals("Player B starts with 10 pieces", 10, board.allPieces(Board.PlayerId.B).size)

        val expectedOccupantCounts = mapOf(2 to 20, 3 to 30, 4 to 40, 6 to 60)
        for ((players, expectedPieces) in expectedOccupantCounts) {
            val multiBoard = Board.standard(players)
            assertEquals("Expected $expectedPieces pieces for $players players", expectedPieces, multiBoard.occupant.size)
        }
    }

    @Test
    fun winner_detectedWhenPlayerOccupiesGoalCamp() {
        val base = Board.standard(2)
        val goal = base.goalCampCells(Board.PlayerId.A)
        val occupiedGoal = goal.associateWith { Board.PlayerId.A }

        val winningBoard = Board.restore(
            playerCount = 2,
            occupant = occupiedGoal,
            currentPlayer = Board.PlayerId.A
        )

        val winner = winningBoard.winner()
        assertNotNull("Winner should be detected when goal camp is fully occupied", winner)
        assertEquals(Board.PlayerId.A, winner)
    }

    @Test
    fun longJumpToggle_controlsExtendedJumpAvailability() {
        val start = Hex(0, 0, 0)
        val blocker = Hex(2, -2, 0)
        val landing = Hex(4, -4, 0)
        val occupants = mapOf(
            start to Board.PlayerId.A,
            blocker to Board.PlayerId.B
        )

        Board.setLongJumps(false)
        val withoutLongJumps = Board.restore(
            playerCount = 2,
            occupant = occupants,
            currentPlayer = Board.PlayerId.A
        )
        val noLongJumpMoves = withoutLongJumps.legalMovesFrom(start)
        assertTrue("Long landing should be unavailable when long jumps are disabled", noLongJumpMoves.none { it.to == landing })

        Board.setLongJumps(true)
        val withLongJumps = Board.restore(
            playerCount = 2,
            occupant = occupants,
            currentPlayer = Board.PlayerId.A
        )
        val longJumpMoves = withLongJumps.legalMovesFrom(start)
        assertTrue(
            "Expected long jump landing to be offered when long jumps are enabled",
            longJumpMoves.any { it.to == landing && it.isJump }
        )
    }
}
