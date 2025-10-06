package com.yoyicue.chinesechecker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ProfileEntity::class, GameResultEntity::class, SaveGameEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun gameResultDao(): GameResultDao
    abstract fun saveGameDao(): SaveGameDao
}
