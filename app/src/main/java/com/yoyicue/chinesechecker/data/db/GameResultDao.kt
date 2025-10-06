package com.yoyicue.chinesechecker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

// Column names must match query aliases below (label, count)
data class WinnerCount(val label: String, val count: Int)

@Dao
interface GameResultDao {
    @Insert
    suspend fun insert(entity: GameResultEntity)

    @Query("DELETE FROM game_results")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM game_results")
    fun totalGames(): Flow<Int>

    @Query("SELECT MAX(timestamp) FROM game_results")
    fun lastPlayed(): Flow<Long?>

    @Query("SELECT winnerPlayer AS label, COUNT(*) AS count FROM game_results WHERE winnerPlayer IS NOT NULL GROUP BY winnerPlayer")
    fun winsByPlayer(): Flow<List<WinnerCount>>

    @Query("SELECT winnerController AS label, COUNT(*) AS count FROM game_results WHERE winnerController IS NOT NULL GROUP BY winnerController")
    fun winsByController(): Flow<List<WinnerCount>>

    @Query("SELECT winnerDifficulty AS label, COUNT(*) AS count FROM game_results WHERE winnerDifficulty IS NOT NULL GROUP BY winnerDifficulty")
    fun winsByDifficulty(): Flow<List<WinnerCount>>
}
