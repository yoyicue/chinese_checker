package com.yoyicue.chinesechecker.ui.offline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoyicue.chinesechecker.data.AppContainer
import com.yoyicue.chinesechecker.data.GameRepository
import com.yoyicue.chinesechecker.data.SettingsRepository
import com.yoyicue.chinesechecker.game.AiDifficulty
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.ui.game.ControllerType
import com.yoyicue.chinesechecker.ui.game.GameConfig
import com.yoyicue.chinesechecker.ui.game.PlayerConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Color
import java.util.LinkedHashMap

class OfflineConfigViewModel(
    private val settingsRepository: SettingsRepository,
    private val gameRepository: GameRepository,
    private val appContainer: AppContainer
) : ViewModel() {

    private val _state = MutableStateFlow(
        OfflineConfigUiState(
            playerCount = DEFAULT_PLAYER_COUNT,
            seats = seatsForCount(DEFAULT_PLAYER_COUNT),
            setups = initialSetups(DEFAULT_DIFFICULTY)
        )
    )
    val state: StateFlow<OfflineConfigUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            settingsRepository.settings.collectLatest { settings ->
                val defaultDiff = when (settings.aiDifficulty) {
                    0 -> AiDifficulty.Weak
                    1 -> AiDifficulty.Greedy
                    2 -> AiDifficulty.Smart
                    else -> AiDifficulty.Greedy
                }
                _state.update { current ->
                    current.copy(
                        defaultDifficulty = defaultDiff,
                        setups = current.setups.mapValues { (_, setup) ->
                            if (setup.isAi && !setup.userDifficultyLocked) {
                                setup.copy(difficulty = defaultDiff)
                            } else setup
                        }
                    )
                }
            }
        }
    }

    fun setPlayerCount(count: Int) {
        val seats = seatsForCount(count)
        _state.update { current ->
            val updatedSetups = LinkedHashMap<Board.PlayerId, SeatSetup>()
            seats.forEach { (pid, _) ->
                val existing = current.setups[pid]
                val base = existing ?: SeatSetup(
                    isAi = false,
                    difficulty = current.defaultDifficulty,
                    color = DEFAULT_COLORS[pid] ?: Color.Gray,
                    userDifficultyLocked = false
                )
                updatedSetups[pid] = base
            }
            current.copy(playerCount = count, seats = seats, setups = updatedSetups)
        }
    }

    fun toggleAi(pid: Board.PlayerId, enabled: Boolean) {
        _state.update { current ->
            val setup = current.setups[pid] ?: return
            val updated = setup.copy(
                isAi = enabled,
                difficulty = if (enabled) setup.difficulty else current.defaultDifficulty,
                userDifficultyLocked = if (enabled) setup.userDifficultyLocked else false
            )
            current.copy(setups = current.setups + (pid to updated))
        }
    }

    fun setDifficulty(pid: Board.PlayerId, difficulty: AiDifficulty) {
        _state.update { current ->
            val setup = current.setups[pid] ?: return
            val updated = setup.copy(difficulty = difficulty, userDifficultyLocked = true)
            current.copy(setups = current.setups + (pid to updated))
        }
    }

    fun setColor(pid: Board.PlayerId, color: Color) {
        _state.update { current ->
            val setup = current.setups[pid] ?: return
            val updated = setup.copy(color = color)
            current.copy(setups = current.setups + (pid to updated))
        }
    }

    fun colorOptionsFor(pid: Board.PlayerId): List<Pair<Color, String>> {
        val state = _state.value
        val used = state.setups.filter { it.key != pid }.values.map { it.color }.toSet()
        return COLOR_PALETTE.filter { (color, _) -> color == state.setups[pid]?.color || color !in used }
    }

    fun currentSetup(pid: Board.PlayerId): SeatSetup? = _state.value.setups[pid]

    fun createGameConfig(): GameConfig {
        val state = _state.value
        val players = state.seats.map { (pid, _) ->
            val setup = state.setups[pid] ?: SeatSetup(false, state.defaultDifficulty, DEFAULT_COLORS[pid] ?: Color.Gray, false)
            PlayerConfig(
                playerId = pid,
                controller = if (setup.isAi) ControllerType.AI else ControllerType.Human,
                difficulty = if (setup.isAi) setup.difficulty else null,
                color = setup.color
            )
        }
        return GameConfig(
            playerCount = state.playerCount,
            players = players
        )
    }

    fun prepareGame(config: GameConfig) {
        appContainer.pendingRestore = null
        appContainer.lastGameConfig = config
        viewModelScope.launch(Dispatchers.IO) { gameRepository.clearSave() }
    }

    data class OfflineConfigUiState(
        val playerCount: Int,
        val seats: List<Pair<Board.PlayerId, String>>,
        val setups: Map<Board.PlayerId, SeatSetup>,
        val defaultDifficulty: AiDifficulty = DEFAULT_DIFFICULTY
    )

    data class SeatSetup(
        val isAi: Boolean,
        val difficulty: AiDifficulty,
        val color: Color,
        val userDifficultyLocked: Boolean
    )

    companion object {
        private const val DEFAULT_PLAYER_COUNT = 2
        private val DEFAULT_COLORS = mapOf(
            Board.PlayerId.A to Color(0xFFE53935),
            Board.PlayerId.B to Color(0xFF1E88E5),
            Board.PlayerId.C to Color(0xFF8E24AA),
            Board.PlayerId.D to Color(0xFF43A047),
            Board.PlayerId.E to Color(0xFFFDD835),
            Board.PlayerId.F to Color(0xFFFF7043)
        )
        val COLOR_PALETTE: List<Pair<Color, String>> = listOf(
            Color(0xFFE53935) to "红",
            Color(0xFF1E88E5) to "蓝",
            Color(0xFF8E24AA) to "紫",
            Color(0xFF43A047) to "绿",
            Color(0xFFFDD835) to "黄",
            Color(0xFFFF7043) to "橙",
            Color(0xFF26A69A) to "青"
        )
        private val DEFAULT_DIFFICULTY = AiDifficulty.Greedy

        private fun initialSetups(defaultDiff: AiDifficulty): Map<Board.PlayerId, SeatSetup> {
            return Board.PlayerId.values().associateWith { pid ->
                SeatSetup(
                    isAi = false,
                    difficulty = defaultDiff,
                    color = DEFAULT_COLORS[pid] ?: Color.Gray,
                    userDifficultyLocked = false
                )
            }
        }

        private fun seatsForCount(n: Int): List<Pair<Board.PlayerId, String>> = when (n) {
            2 -> listOf(
                Board.PlayerId.A to "上(Top)",
                Board.PlayerId.B to "下(Bottom)"
            )
            3 -> listOf(
                Board.PlayerId.A to "上(Top)",
                Board.PlayerId.B to "左下(SW)",
                Board.PlayerId.C to "右下(SE)"
            )
            4 -> listOf(
                Board.PlayerId.A to "上(Top)",
                Board.PlayerId.B to "下(Bottom)",
                Board.PlayerId.C to "右上(NE)",
                Board.PlayerId.D to "左下(SW)"
            )
            else -> listOf(
                Board.PlayerId.A to "上(Top)",
                Board.PlayerId.C to "右上(NE)",
                Board.PlayerId.F to "右下(SE)",
                Board.PlayerId.B to "下(Bottom)",
                Board.PlayerId.D to "左下(SW)",
                Board.PlayerId.E to "左上(NW)"
            )
        }
    }
}
