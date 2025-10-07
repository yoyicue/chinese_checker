package com.yoyicue.chinesechecker.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yoyicue.chinesechecker.data.ProfileRepository
import com.yoyicue.chinesechecker.data.ProfileUi
import com.yoyicue.chinesechecker.data.StatsRepository
import com.yoyicue.chinesechecker.data.StatsUi
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val profileRepository: ProfileRepository,
    private val statsRepository: StatsRepository
) : ViewModel() {

    val profile: StateFlow<ProfileUi> = profileRepository.profile()
        .stateIn(viewModelScope, SharingStarted.Eagerly, ProfileUi(nickname = "", avatarUri = null))

    val stats: StateFlow<StatsUi?> = statsRepository.stats()
        .stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun updateNickname(name: String) {
        val trimmed = name.trim()
        val value = if (trimmed.isEmpty()) "" else trimmed
        viewModelScope.launch { profileRepository.updateNickname(value) }
    }

    fun resetStats() {
        viewModelScope.launch { statsRepository.reset() }
    }
}
