package com.yoyicue.chinesechecker.data.db

import android.database.sqlite.SQLiteException
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [ProfileEntity::class, GameResultEntity::class, SaveGameEntity::class],
    version = 2,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun profileDao(): ProfileDao
    abstract fun gameResultDao(): GameResultDao
    abstract fun saveGameDao(): SaveGameDao

    companion object {
        /**
         * Historical v1 only stored the single save slot. v2 adds optional profile/stat tables and
         * keeps save metadata for per-seat config. A forward migration keeps existing data intact.
         */
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS `profiles` (
                            `id` INTEGER NOT NULL,
                            `nickname` TEXT NOT NULL,
                            `avatarUri` TEXT,
                            PRIMARY KEY(`id`)
                        )
                    """.trimIndent()
                )
                db.execSQL(
                    """
                        CREATE TABLE IF NOT EXISTS `game_results` (
                            `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                            `timestamp` INTEGER NOT NULL,
                            `playerCount` INTEGER NOT NULL,
                            `winnerPlayer` TEXT,
                            `winnerController` TEXT,
                            `winnerDifficulty` TEXT
                        )
                    """.trimIndent()
                )
                try {
                    db.execSQL("ALTER TABLE `save_game` ADD COLUMN `playersJson` TEXT")
                } catch (sqlite: SQLiteException) {
                    // Ignore if the column already exists (duplicate column name).
                    val message = sqlite.message?.lowercase()
                    if (message == null || !message.contains("duplicate column")) {
                        throw sqlite
                    }
                }
            }
        }

        val ALL_MIGRATIONS: Array<Migration> = arrayOf(MIGRATION_1_2)
    }
}
