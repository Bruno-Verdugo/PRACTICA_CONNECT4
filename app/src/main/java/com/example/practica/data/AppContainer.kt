package com.example.practica.data

import android.content.Context
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

interface AppContainer {
    val gameRepository: GameRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    private val applicationScope = CoroutineScope(SupervisorJob())

    override val gameRepository: GameRepository by lazy {
        GameRepository(GameRoomDatabase.getDatabase(context, applicationScope).gameRecordDao())
    }
}