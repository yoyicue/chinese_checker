package com.yoyicue.chinesechecker.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoyicue.chinesechecker.data.AppSettings
import com.yoyicue.chinesechecker.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val repository: SettingsRepository) : ViewModel() {
    val settings: StateFlow<AppSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.Eagerly, AppSettings())

    private fun update(transform: (AppSettings) -> AppSettings) {
        viewModelScope.launch { repository.update(transform) }
    }

    fun setSoundsVolume(volume: Float) = update { it.copy(soundsVolume = volume) }
    fun setHaptics(enabled: Boolean) = update { it.copy(haptics = enabled) }
    fun setThemeDark(enabled: Boolean) = update { it.copy(themeDark = enabled) }
    fun setFastGame(enabled: Boolean) = update { it.copy(fastGame = enabled) }
    fun setLongJumps(enabled: Boolean) = update { it.copy(longJumps = enabled) }
    fun setTurnSeconds(seconds: Int) = update { it.copy(turnSeconds = seconds) }
    fun setTimeoutAction(action: Int) = update { it.copy(timeoutAction = action) }
    fun setDebugOverlay(enabled: Boolean) = update { it.copy(debugOverlay = enabled) }
    fun setAiDifficulty(code: Int) = update { it.copy(aiDifficulty = code) }
    fun setAiLongJumps(enabled: Boolean) = update { it.copy(aiLongJumps = enabled) }
    fun setLanguage(tag: String) = update { it.copy(languageTag = tag) }
}
