package com.yoyicue.chinesechecker.data

import com.yoyicue.chinesechecker.data.db.GameResultDao
import com.yoyicue.chinesechecker.data.db.GameResultEntity
import com.yoyicue.chinesechecker.game.Board
import com.yoyicue.chinesechecker.ui.game.ControllerType
import com.yoyicue.chinesechecker.ui.game.GameConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

data class StatsUi(
    val totalGames: Int,
    val winsByPlayer: Map<String, Int>,
    val winsByController: Map<String, Int>,
    val winsByDifficulty: Map<String, Int>,
    val lastPlayedAt: Long?
)

class StatsRepository(private val dao: GameResultDao) {
    suspend fun recordGameResult(winner: Board.PlayerId?, config: GameConfig, whenMillis: Long = System.currentTimeMillis()) {
        val winnerCfg = winner?.let { id -> config.players.find { it.playerId == id } }
        val entity = GameResultEntity(
            timestamp = whenMillis,
            playerCount = config.playerCount,
            winnerPlayer = winner?.name,
            winnerController = winnerCfg?.controller?.name,
            winnerDifficulty = winnerCfg?.difficulty?.name
        )
        dao.insert(entity)
    }

    fun stats(): Flow<StatsUi> = combine(
        dao.totalGames(),
        dao.winsByPlayer(),
        dao.winsByController(),
        dao.winsByDifficulty(),
        dao.lastPlayed()
    ) { total, byPlayer, byCtrl, byDiff, last ->
        StatsUi(
            totalGames = total,
            winsByPlayer = byPlayer.associate { it.label to it.count },
            winsByController = byCtrl.associate { it.label to it.count },
            winsByDifficulty = byDiff.associate { it.label to it.count },
            lastPlayedAt = last
        )
    }

    suspend fun reset() { dao.clearAll() }
}
