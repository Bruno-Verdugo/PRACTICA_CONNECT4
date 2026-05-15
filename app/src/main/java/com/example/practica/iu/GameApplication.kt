package com.example.practica.iu

import android.app.Application
import com.example.practica.data.AppContainer
import com.example.practica.data.AppDataContainer

class GameApplication : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}