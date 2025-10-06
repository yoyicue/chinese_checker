package com.yoyicue.chinesechecker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_results")
data class GameResultEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long,
    val playerCount: Int,
    val winnerPlayer: String?, // Board.PlayerId name, or null for draw/aborted
    val winnerController: String?, // Human or AI
    val winnerDifficulty: String? // Weak/Greedy/Smart when AI
)

