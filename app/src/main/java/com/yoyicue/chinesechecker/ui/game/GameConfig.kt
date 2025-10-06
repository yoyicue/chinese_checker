package com.yoyicue.chinesechecker.ui.game

import androidx.compose.ui.graphics.Color
import com.yoyicue.chinesechecker.game.AiDifficulty
import com.yoyicue.chinesechecker.game.Board

enum class ControllerType { Human, AI }

data class PlayerConfig(
    val playerId: Board.PlayerId,
    val controller: ControllerType,
    val difficulty: AiDifficulty? = null,
    val color: Color
)

data class GameConfig(
    val playerCount: Int,
    val players: List<PlayerConfig>
)

