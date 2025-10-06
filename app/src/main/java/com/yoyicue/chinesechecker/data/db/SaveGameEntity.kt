package com.yoyicue.chinesechecker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "save_game")
data class SaveGameEntity(
    @PrimaryKey val id: Int = 1,
    val updatedAt: Long,
    val playerCount: Int,
    val currentPlayer: String,
    val occupantJson: String,
    val lastMoveJson: String?,
    val lastOwner: String?,
    val playersJson: String?
)

