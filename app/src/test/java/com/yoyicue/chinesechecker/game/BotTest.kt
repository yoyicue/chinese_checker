package com.yoyicue.chinesechecker.game

import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test

class BotTest {

    @After
    fun resetLongJumpToggle() {
        Board.setLongJumps(false)
    }

    @Test
    fun weakBotReturnsOnlyAvailableMove() {
        val base = Board.standard(2)
        val origin = Hex.origin()
        require(origin in base.positions) { "Origin must be a valid board position" }
        val freeDir = Hex.DIRECTIONS.first { origin.add(it) in base.positions }
        val occupant = buildMap {
            put(origin, Board.PlayerId.A)
            for (dir in Hex.DIRECTIONS) {
                if (dir == freeDir) continue
                val block = origin.add(dir)
                if (block in base.positions) put(block, Board.PlayerId.B)
                val landing = origin.add(Hex(dir.x * 2, dir.y * 2, dir.z * 2))
                if (landing in base.positions) put(landing, Board.PlayerId.B)
            }
        }
        val board = Board.restore(playerCount = 2, occupant = occupant, currentPlayer = Board.PlayerId.A)

        val legalMoves = board.legalMovesFor(Board.PlayerId.A)
        assertEquals("Expected only one legal move from tip of the camp", 1, legalMoves.size)

        val bot = WeakBot(Random(123))
        val move = bot.chooseMove(board, Board.PlayerId.A)
        assertNotNull("Weak bot should return the sole legal move", move)
        assertEquals("Weak bot should follow the only legal destination", legalMoves.first().to, move!!.to)
    }

    @Test
    fun greedyBotPrefersFinishingMove() {
        val base = Board.standard(2)
        val goalCells = base.goalCampCells(Board.PlayerId.A)
        require(goalCells.isNotEmpty()) { "Goal camp should contain cells" }
        val target = goalCells.first()
        val outwardDir = neighborDirection(base, target) ?: error("No outward direction from goal")
        val start = target.add(Hex(-outwardDir.x, -outwardDir.y, -outwardDir.z))

        require(start in base.positions) { "Start cell should be inside the board" }

        val occupant = goalCells.filter { it != target }.associateWith { Board.PlayerId.A }.toMutableMap()
        occupant[start] = Board.PlayerId.A

        val board = Board.restore(
            playerCount = 2,
            occupant = occupant,
            currentPlayer = Board.PlayerId.A
        )

        val bot = GreedyBot()
        val move = bot.chooseMove(board, Board.PlayerId.A)
        assertNotNull("Greedy bot should find a winning move", move)
        assertEquals("Greedy bot should move into the final goal cell", target, move!!.to)
    }

    @Test
    fun smartBotFindsWinningJump() {
        val base = Board.standard(2)
        val goalCells = base.goalCampCells(Board.PlayerId.A)
        require(goalCells.isNotEmpty())
        val target = goalCells.first()
        val outwardDir = neighborDirection(base, target) ?: error("No outward direction from goal")
        val mid = target.add(Hex(-outwardDir.x, -outwardDir.y, -outwardDir.z))
        val start = target.add(Hex(-outwardDir.x * 2, -outwardDir.y * 2, -outwardDir.z * 2))

        require(mid in base.positions && start in base.positions) { "Jump path must stay on the board" }

        val occupant = goalCells.filter { it != target }.associateWith { Board.PlayerId.A }.toMutableMap()
        occupant[start] = Board.PlayerId.A
        occupant[mid] = Board.PlayerId.B // obstacle to jump over

        val board = Board.restore(
            playerCount = 2,
            occupant = occupant,
            currentPlayer = Board.PlayerId.A
        )

        val bot = SmartBot(timeBudgetMs = 150, maxDepth = 4, ttCapacity = 1_024)
        val move = bot.chooseMove(board, Board.PlayerId.A)
        assertNotNull("Smart bot should compute a jump into the final goal cell", move)
        assertEquals("Smart bot should land on the winning target", target, move!!.to)
        assertTrue("Move should be a jump over the blocking piece", move.isJump)
    }

    @Test
    fun boardPassRotatesAcrossPlayers() {
        val board = Board.standard(4)
        assertEquals(Board.PlayerId.A, board.currentPlayer)
        board.pass()
        assertEquals(Board.PlayerId.B, board.currentPlayer)
        board.pass()
        assertEquals(Board.PlayerId.C, board.currentPlayer)
        board.pass()
        assertEquals(Board.PlayerId.D, board.currentPlayer)
        board.pass()
        assertEquals(Board.PlayerId.A, board.currentPlayer)
    }

    private fun neighborCells(board: Board, cell: Hex): List<Hex> =
        Hex.DIRECTIONS.map { cell.add(it) }.filter { it in board.positions }

    private fun neighborDirection(board: Board, cell: Hex): Hex? =
        Hex.DIRECTIONS.firstOrNull { dir ->
            val oneStep = cell.add(Hex(-dir.x, -dir.y, -dir.z))
            val twoStep = cell.add(Hex(-dir.x * 2, -dir.y * 2, -dir.z * 2))
            oneStep in board.positions && twoStep in board.positions
        }
}
