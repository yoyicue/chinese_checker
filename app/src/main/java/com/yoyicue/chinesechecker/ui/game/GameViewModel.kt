package com.yoyicue.chinesechecker.ui.game

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoyicue.chinesechecker.data.GameRepository
import com.yoyicue.chinesechecker.data.StatsRepository
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.game.Bot
import com.yoyicue.chinesechecker.game.BotFactory
import com.yoyicue.chinesechecker.game.WeakBot
import com.yoyicue.chinesechecker.game.GreedyBot
import com.yoyicue.chinesechecker.game.SmartBot
import com.yoyicue.chinesechecker.game.Hex
import com.yoyicue.chinesechecker.ui.game.bfsShortestJumpPath
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

data class GameUiState(
    val board: Board,
    val selected: Hex? = null,
    val winner: Board.PlayerId? = null,
    val lastMovePath: List<Hex>? = null,
    val lastMoveOwner: Board.PlayerId? = null,
    val animPath: List<Hex>? = null,
    val animOwner: Board.PlayerId? = null,
    val animating: Boolean = false,
    val timeLeftSec: Int? = null
)

enum class TimeoutAction { Skip, AutoMove }
data class ClockConfig(val enabled: Boolean, val turnSeconds: Int, val timeout: TimeoutAction)

class GameViewModel(
    initialConfig: GameConfig,
    private val clock: ClockConfig,
    private val aiLongJumps: Boolean,
    private val gameRepository: GameRepository,
    private val statsRepository: StatsRepository,
    initialRestore: GameRepository.Restored? = null
) : ViewModel() {
    companion object {
        private const val TAG = "GameViewModel"
    }
    private var activeConfig: GameConfig = initialRestore?.config ?: initialConfig
    private val _ui = MutableStateFlow(GameUiState(board = Board.standard(activeConfig.playerCount)))
    val ui: StateFlow<GameUiState> = _ui

    private val _events = MutableStateFlow<List<String>>(emptyList())
    val events: StateFlow<List<String>> = _events
    private val _sfx = MutableSharedFlow<SfxEvent>(extraBufferCapacity = 8)
    val sfx: SharedFlow<SfxEvent> = _sfx

    enum class SfxEvent { Select, Invalid, Move, Win }

    private var aiBots: Map<Board.PlayerId, Bot> = buildAiBots(activeConfig)
    private var winRecorded = false

    private fun rebuildAiBots(conf: GameConfig) { aiBots = buildAiBots(conf) }

    private fun buildAiBots(conf: GameConfig): Map<Board.PlayerId, Bot> {
        val (budgetMs, maxDepth) = smartParams()
        return conf.players
            .filter { it.controller == ControllerType.AI && it.difficulty != null }
            .associate { pc ->
                val bot = when (pc.difficulty!!) {
                    com.yoyicue.chinesechecker.game.AiDifficulty.Weak -> WeakBot()
                    com.yoyicue.chinesechecker.game.AiDifficulty.Greedy -> GreedyBot()
                    com.yoyicue.chinesechecker.game.AiDifficulty.Smart -> if (clock.enabled) SmartBot(timeBudgetMs = budgetMs, maxDepth = maxDepth) else SmartBot()
                }
                pc.playerId to bot
            }
    }

    private fun smartParams(): Pair<Long, Int> {
        if (!clock.enabled) return 350L to 5
        val t = clock.turnSeconds
        return when {
            t <= 10 -> 160L to 4
            t <= 15 -> 220L to 5
            t <= 20 -> 260L to 5
            else -> 320L to 6
        }
    }

    private data class HistoryEntry(
        val board: Board,
        val lastMovePath: List<Hex>?,
        val lastMoveOwner: Board.PlayerId?
    )
    private val past = ArrayDeque<HistoryEntry>()
    private val _canUndo = MutableStateFlow(false)
    val canUndo = _canUndo.asStateFlow()
    private var timerJob: Job? = null
    private var aiJob: Job? = null

    init {
        if (initialRestore != null) {
            activeConfig = initialRestore.config
            val restoredPieces = initialRestore.board.activePlayers.sumOf { pid -> initialRestore.board.allPieces(pid).size }
            rebuildAiBots(activeConfig)
            _ui.value = GameUiState(
                board = initialRestore.board.copy(),
                selected = null,
                winner = initialRestore.board.winner(),
                lastMovePath = initialRestore.lastMovePath,
                lastMoveOwner = initialRestore.lastMoveOwner
            )
            Log.d(TAG, "init restore: pieces=$restoredPieces current=${initialRestore.board.currentPlayer}")
            log("restored game: players=${activeConfig.playerCount}, pieces=$restoredPieces, ai=${aiBots.keys}, aiLongJumps=$aiLongJumps")
            maybeAiTurn()
            startTurnTimerIfNeeded()
        } else {
            val freshPieces = _ui.value.board.activePlayers.sumOf { pid -> _ui.value.board.allPieces(pid).size }
            Log.d(TAG, "init new game: pieces=$freshPieces current=${_ui.value.board.currentPlayer}")
            log("new game: players=${activeConfig.playerCount}, pieces=$freshPieces, ai=${aiBots.keys}, aiLongJumps=$aiLongJumps")
            maybeAiTurn()
            startTurnTimerIfNeeded()
        }
    }

    fun restore(board: Board, lastMovePath: List<Hex>?, lastMoveOwner: Board.PlayerId?, conf: GameConfig? = null) {
        cancelAi()
        if (conf != null) {
            activeConfig = conf
            rebuildAiBots(conf)
        }
        _ui.value = _ui.value.copy(
            board = board,
            selected = null,
            winner = board.winner(),
            lastMovePath = lastMovePath,
            lastMoveOwner = lastMoveOwner,
            animPath = null,
            animOwner = null,
            animating = false
        )
        winRecorded = _ui.value.winner != null
        past.clear(); updateHistoryFlags()
        log("restored game: current=${board.currentPlayer}")
        maybeAiTurn()
    }

    fun newGame() {
        cancelTimer(); cancelAi()
        past.clear(); updateHistoryFlags()
        winRecorded = false
        _ui.value = GameUiState(board = Board.standard(activeConfig.playerCount))
        log("new game started: players=${activeConfig.playerCount}")
        clearSaveAsync()
        maybeAiTurn(); startTurnTimerIfNeeded()
    }

    fun onTap(h: Hex) {
        val s = _ui.value
        if (s.winner != null || s.animating) return
        // Block if current is AI
        if (aiBots.containsKey(s.board.currentPlayer)) return
        val occ = s.board.at(h)
        log("tap ${h} occ=${occ ?: "empty"} current=${s.board.currentPlayer}")
        when {
            occ == s.board.currentPlayer -> {
                _ui.value = s.copy(selected = h)
                emitSfx(SfxEvent.Select)
            }
            s.selected != null -> {
                val moves = s.board.legalMovesFrom(s.selected)
                val cand = moves.filter { it.to == h }
                if (cand.isNotEmpty()) {
                    // Use UI BFS to derive a shortest animation path
                    val path = bfsShortestJumpPath(s.board, s.selected, h) ?: listOf(s.selected, h)
                    cancelTimer()
                    _ui.value = s.copy(
                        selected = null,
                        animPath = path,
                        animOwner = s.board.currentPlayer,
                        animating = true,
                        timeLeftSec = null
                    )
                } else if (occ == s.board.currentPlayer) {
                    _ui.value = s.copy(selected = h)
                } else {
                    log("invalid target ${h} from ${s.selected}")
                    _ui.value = s.copy(selected = null)
                    emitSfx(SfxEvent.Invalid)
                }
            }
        }
    }

    fun onAnimationFinished() {
        val s = _ui.value
        val path = s.animPath ?: return
        val owner = s.animOwner ?: s.board.currentPlayer
        try {
            // Push current to history
            past.addLast(HistoryEntry(s.board.copy(), s.lastMovePath, s.lastMoveOwner))
            updateHistoryFlags()

            val b2 = s.board.copy().apply(Board.Move(path))
            log("move $owner: ${path.joinToString(" -> ")}")
            val w = b2.winner()
            _ui.value = s.copy(
                board = b2,
                selected = null,
                winner = w,
                lastMovePath = path,
                lastMoveOwner = owner,
                animPath = null,
                animOwner = null,
                animating = false,
                timeLeftSec = null
            )
            if (w != null) {
                emitSfx(SfxEvent.Win)
                if (!winRecorded) {
                    winRecorded = true
                    recordWinAsync(w)
                }
            } else {
                winRecorded = false
                emitSfx(SfxEvent.Move)
                persistSnapshotAsync(_ui.value)
            }
            maybeAiTurn()
            startTurnTimerIfNeeded()
        } catch (t: Throwable) {
            log("error apply move: ${t.message}")
            _ui.value = s.copy(animPath = null, animOwner = null, animating = false)
        }
    }

    fun undo() {
        val s = _ui.value
        if (past.isEmpty() || s.animating) return
        val prev = past.removeLast()
        _ui.value = s.copy(
            board = prev.board.copy(),
            selected = null,
            winner = prev.board.winner(),
            lastMovePath = prev.lastMovePath,
            lastMoveOwner = prev.lastMoveOwner,
            animPath = null,
            animOwner = null,
            animating = false
        )
        winRecorded = _ui.value.winner != null
        persistSnapshotAsync(_ui.value)
        updateHistoryFlags()
        // After undo, allow AI to play if it's AI's turn, or restart human timer.
        maybeAiTurn(); startTurnTimerIfNeeded()
    }

    private fun updateHistoryFlags() {
        _canUndo.value = past.isNotEmpty()
    }

    private fun maybeAiTurn() {
        val s = _ui.value
        if (s.winner != null) return
        val side = s.board.currentPlayer
        val bot = aiBots[side] ?: return
        aiJob?.cancel()
        aiJob = viewModelScope.launch {
            val cur = _ui.value
            val move = withContext(Dispatchers.Default) {
                val candidate = bot.chooseMove(cur.board, side)
                adjustAiMove(cur.board, side, candidate)
            }
            if (move != null) {
                // Animate AI move using the provided path; apply on animation finish
                log("ai ${side} planning: ${move.path.joinToString(" -> ")}")
                cancelTimer()
                _ui.value = cur.copy(
                    selected = null,
                    animPath = move.path,
                    animOwner = side,
                    animating = true,
                    timeLeftSec = null
                )
            }
        }
    }

    private fun startTurnTimerIfNeeded() {
        val s = _ui.value
        if (!clock.enabled || s.winner != null || s.animating) return
        val curPlayer = s.board.currentPlayer
        // Only time human turns
        if (aiBots.containsKey(curPlayer)) return
        cancelTimer()
        val total = clock.turnSeconds.coerceIn(5, 120)
        _ui.value = s.copy(timeLeftSec = total)
        timerJob = viewModelScope.launch {
            var left = total
            while (left > 0 && _ui.value.board.currentPlayer == curPlayer && !_ui.value.animating && _ui.value.winner == null) {
                kotlinx.coroutines.delay(1000)
                left -= 1
                _ui.value = _ui.value.copy(timeLeftSec = left)
            }
            if (left <= 0 && _ui.value.board.currentPlayer == curPlayer && !_ui.value.animating && _ui.value.winner == null) {
                onTimeout(curPlayer)
            }
        }
    }

    private fun cancelTimer() { timerJob?.cancel(); timerJob = null }
    private fun cancelAi() { aiJob?.cancel(); aiJob = null }

    private fun persistSnapshotAsync(state: GameUiState = _ui.value) {
        if (state.animating) return
        viewModelScope.launch(Dispatchers.IO) {
            gameRepository.save(state.board, state.lastMovePath, state.lastMoveOwner, activeConfig)
        }
    }

    private fun clearSaveAsync() {
        viewModelScope.launch(Dispatchers.IO) { gameRepository.clearSave() }
    }

    private fun recordWinAsync(winner: Board.PlayerId) {
        viewModelScope.launch(Dispatchers.IO) {
            statsRepository.recordGameResult(winner, activeConfig)
            gameRepository.clearSave()
        }
    }

    suspend fun saveSnapshot() {
        val state = _ui.value
        withContext(Dispatchers.IO) {
            gameRepository.save(state.board, state.lastMovePath, state.lastMoveOwner, activeConfig)
        }
    }

    suspend fun clearSavedGame() {
        withContext(Dispatchers.IO) { gameRepository.clearSave() }
    }

    fun resetBoardDueToInvalidState() {
        newGame()
    }

    private suspend fun onTimeout(player: Board.PlayerId) {
        when (clock.timeout) {
            TimeoutAction.Skip -> {
                val s = _ui.value
                // Record history for undo
                past.addLast(HistoryEntry(s.board.copy(), s.lastMovePath, s.lastMoveOwner))
                updateHistoryFlags()
                val b2 = s.board.copy().pass()
                log("timeout: $player -> skip turn")
                _ui.value = s.copy(board = b2, selected = null, lastMovePath = null, lastMoveOwner = null, animPath = null, animOwner = null, animating = false, timeLeftSec = null)
                winRecorded = false
                persistSnapshotAsync(_ui.value)
                maybeAiTurn(); startTurnTimerIfNeeded()
            }
            TimeoutAction.AutoMove -> {
                val s = _ui.value
                // Use Greedy for auto move
                val greedy = com.yoyicue.chinesechecker.game.GreedyBot()
                val move = withContext(Dispatchers.Default) {
                    val candidate = greedy.chooseMove(s.board, player)
                    adjustAiMove(s.board, player, candidate)
                }
                if (move != null) {
                    log("timeout: $player -> auto move ${move.path.joinToString(" -> ")}")
                    _ui.value = s.copy(selected = null, animPath = move.path, animOwner = player, animating = true, timeLeftSec = null)
                } else {
                    // No legal move, skip
                    val b2 = s.board.copy().pass()
                    _ui.value = s.copy(board = b2, selected = null, animPath = null, animOwner = null, animating = false, timeLeftSec = null)
                    winRecorded = false
                    persistSnapshotAsync(_ui.value)
                    maybeAiTurn(); startTurnTimerIfNeeded()
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        cancelTimer(); cancelAi()
    }

    fun clearLog() { _events.value = emptyList() }

    private fun log(msg: String) {
        val next = (_events.value + msg)
        _events.value = if (next.size > 50) next.takeLast(50) else next
    }

    private fun emitSfx(ev: SfxEvent) { _sfx.tryEmit(ev) }

    private fun adjustAiMove(board: Board, player: Board.PlayerId, move: Board.Move?): Board.Move? {
        if (move == null) return null
        if (aiLongJumps) return move
        if (move.path.size <= 2) return move
        val alternatives = board.legalMovesFor(player).filter { it.path.size <= 2 }
        if (alternatives.isEmpty()) return move
        return alternatives.minByOrNull { evalMove(board, it, player) } ?: move
    }

    private fun evalMove(board: Board, move: Board.Move, me: Board.PlayerId): Int {
        val b2 = board.copy().apply(move)
        val target = b2.goalCampCells(me)
        val myPieces = b2.allPieces(me)
        val remaining = target.toMutableSet()
        var sum = 0
        for (p in myPieces) {
            val best = remaining.minByOrNull { it.distanceTo(p) }
            if (best != null) {
                sum += best.distanceTo(p)
                remaining.remove(best)
            }
        }
        return sum
    }
}
