package com.yoyicue.chinesechecker.data

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val DATASTORE_NAME = "settings"

val Context.dataStore by preferencesDataStore(name = DATASTORE_NAME)

data class AppSettings(
    val musicVolume: Float = 0.0f,
    val soundsVolume: Float = 0.5f,
    val haptics: Boolean = true,
    val themeDark: Boolean = true,
    val fastGame: Boolean = false,
    val turnSeconds: Int = 15,
    val timeoutAction: Int = 0, // 0=Skip,1=AutoMove
    val longJumps: Boolean = false,
    val aiDifficulty: Int = 1, // 0=Weak,1=Greedy,2=Smart
    val aiLongJumps: Boolean = true,
    val debugOverlay: Boolean = false
)

class SettingsRepository(private val context: Context) {
    private object Keys {
        val musicVolume = floatPreferencesKey("music_volume")
        val soundsVolume = floatPreferencesKey("sounds_volume")
        val haptics = booleanPreferencesKey("haptics")
        val themeDark = booleanPreferencesKey("theme_dark")
        val fastGame = booleanPreferencesKey("fast_game")
        val turnSeconds = intPreferencesKey("turn_seconds")
        val timeoutAction = intPreferencesKey("timeout_action")
        val longJumps = booleanPreferencesKey("long_jumps")
        val aiDifficulty = intPreferencesKey("ai_difficulty")
        val aiLongJumps = booleanPreferencesKey("ai_long_jumps")
        val debugOverlay = booleanPreferencesKey("debug_overlay")
    }

    val settings: Flow<AppSettings> = context.dataStore.data.map { p -> p.toSettings() }

    suspend fun update(transform: (AppSettings) -> AppSettings) {
        context.dataStore.edit { prefs ->
            val current = prefs.toSettings()
            val next = transform(current)
            prefs[Keys.musicVolume] = next.musicVolume
            prefs[Keys.soundsVolume] = next.soundsVolume
            prefs[Keys.haptics] = next.haptics
            prefs[Keys.themeDark] = next.themeDark
            prefs[Keys.fastGame] = next.fastGame
            prefs[Keys.turnSeconds] = next.turnSeconds
            prefs[Keys.timeoutAction] = next.timeoutAction
            prefs[Keys.longJumps] = next.longJumps
            prefs[Keys.aiDifficulty] = next.aiDifficulty
            prefs[Keys.aiLongJumps] = next.aiLongJumps
            prefs[Keys.debugOverlay] = next.debugOverlay
        }
    }

    private fun Preferences.toSettings(): AppSettings = AppSettings(
        musicVolume = this[Keys.musicVolume] ?: 0.0f,
        soundsVolume = this[Keys.soundsVolume] ?: 0.5f,
        haptics = this[Keys.haptics] ?: true,
        themeDark = this[Keys.themeDark] ?: true,
        fastGame = this[Keys.fastGame] ?: false,
        turnSeconds = this[Keys.turnSeconds] ?: 15,
        timeoutAction = this[Keys.timeoutAction] ?: 0,
        longJumps = this[Keys.longJumps] ?: false,
        aiDifficulty = this[Keys.aiDifficulty] ?: 1,
        aiLongJumps = this[Keys.aiLongJumps] ?: true,
        debugOverlay = this[Keys.debugOverlay] ?: false
    )
}
