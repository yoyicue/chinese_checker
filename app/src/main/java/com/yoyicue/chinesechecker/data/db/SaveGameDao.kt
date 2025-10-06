package com.yoyicue.chinesechecker.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface SaveGameDao {
    @Query("SELECT * FROM save_game WHERE id = 1 LIMIT 1")
    suspend fun get(): SaveGameEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(entity: SaveGameEntity)

    @Query("DELETE FROM save_game WHERE id = 1")
    suspend fun clear()
}

