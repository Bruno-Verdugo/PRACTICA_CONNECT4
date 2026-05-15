package com.example.practica.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameRecordDao {
    @Query("SELECT * FROM game_records_table ORDER BY id DESC")
    fun getAllGameRecords(): Flow<List<GameRecord>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(gameRecord: GameRecord)

    @Query("DELETE FROM game_records_table")
    suspend fun deleteAll()
}