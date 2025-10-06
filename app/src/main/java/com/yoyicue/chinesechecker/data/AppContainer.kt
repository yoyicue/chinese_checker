package com.yoyicue.chinesechecker.data

import android.content.Context
import androidx.room.Room
import com.yoyicue.chinesechecker.data.db.AppDatabase
import com.yoyicue.chinesechecker.ui.game.GameConfig

class AppContainer(context: Context) {
    val settingsRepository = SettingsRepository(context)
    // Room database and repositories
    private val db = Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
        .fallbackToDestructiveMigration()
        .build()
    val statsRepository = StatsRepository(db.gameResultDao())
    val profileRepository = ProfileRepository(db.profileDao())
    val gameRepository = GameRepository(context, db.saveGameDao())

    // Ephemeral config & pending restore payload for navigation between screens
    var lastGameConfig: GameConfig? = null
    var pendingRestore: GameRepository.Restored? = null
}
