package com.example.practica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class ConfigurationViewModel(application: Application) : AndroidViewModel(application) {

    private val dataStoreManager = DataStoreManager(application)

    var alias by mutableStateOf("")
        private set

    var columns by mutableIntStateOf(7)
        private set

    var time by mutableStateOf(false)
        private set

    var difficulty by mutableStateOf("Fàcil")
        private set

    init {
        viewModelScope.launch {
            dataStoreManager.getUserSettings().collect { settings ->
                alias = settings.alias
                columns = settings.columns
                time = settings.time
                difficulty = settings.difficulty
            }
        }
    }

    fun onAliasChange(newAlias: String) {
        alias = newAlias
        viewModelScope.launch { dataStoreManager.saveAlias(newAlias) }
    }

    fun onColumnsChange(newColumns: Int) {
        columns = newColumns
        viewModelScope.launch { dataStoreManager.saveColumns(newColumns) }
    }

    fun onTimeChange(active: Boolean) {
        time = active
        viewModelScope.launch { dataStoreManager.saveTime(active) }
    }

    fun onDifficultyChange(newDiff: String) {
        difficulty = newDiff
        viewModelScope.launch { dataStoreManager.saveDifficulty(newDiff) }
    }
}