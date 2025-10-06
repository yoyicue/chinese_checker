package com.yoyicue.chinesechecker.data

import com.yoyicue.chinesechecker.data.db.ProfileDao
import com.yoyicue.chinesechecker.data.db.ProfileEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

data class ProfileUi(
    val nickname: String,
    val avatarUri: String?
)

class ProfileRepository(private val dao: ProfileDao) {
    fun profile(): Flow<ProfileUi> = dao.observeProfile().map { p ->
        val e = p ?: ProfileEntity()
        ProfileUi(nickname = e.nickname, avatarUri = e.avatarUri)
    }

    suspend fun updateNickname(nickname: String) {
        dao.upsert(ProfileEntity(nickname = nickname))
    }
}

