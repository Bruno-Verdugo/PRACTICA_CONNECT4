package com.example.practica.viewmodel

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.practica.iu.GameApplication

object AppViewModelProvider {
    val Factory = viewModelFactory {

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameApplication)
            ResultsViewModel(application.container.gameRepository)
        }

        initializer {
            val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as GameApplication)
            GameHistoryViewModel(application.container.gameRepository)
        }
    }
}