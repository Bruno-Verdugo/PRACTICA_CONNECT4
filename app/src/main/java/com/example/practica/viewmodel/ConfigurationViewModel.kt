package com.example.practica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    var alias by mutableStateOf("")
        private set

    var columns by mutableIntStateOf(7)
        private set

    var time by mutableStateOf(false)
        private set

    var difficulty by mutableStateOf("Fàcil")
        private set

    fun onAliasChange(newAlias: String) {
        alias = newAlias
    }

    fun onColumnsChange(newColumns: Int) {
        columns = newColumns
    }

    fun onTimeChange(active: Boolean) {
        time = active
    }

    fun onDifficultyChange(newDiff: String) {
        difficulty = newDiff
    }
}