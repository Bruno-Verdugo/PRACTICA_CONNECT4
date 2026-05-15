package com.example.practica.data

import kotlinx.coroutines.flow.Flow

class GameRepository(private val gameRecordDao: GameRecordDao) {

    val allRecords: Flow<List<GameRecord>> = gameRecordDao.getAllGameRecords()

    suspend fun insertRecord(record: GameRecord) {
        gameRecordDao.insert(record)
    }
}