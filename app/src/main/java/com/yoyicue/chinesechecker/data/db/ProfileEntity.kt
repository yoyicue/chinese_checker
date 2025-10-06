package com.yoyicue.chinesechecker.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "profiles")
data class ProfileEntity(
    @PrimaryKey val id: Int = 1,
    val nickname: String = "玩家",
    val avatarUri: String? = null
)

