package com.example.practica

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class ConfigurationViewModel : ViewModel() {

    var alias by mutableStateOf("p1")
        private set

    var columnes by mutableIntStateOf(7)
        private set

    var controlTemps by mutableStateOf(false)
        private set

    fun onAliasChange(nouAlias: String) {
        alias = nouAlias
    }

    fun onColumnesChange(novesColumnes: Int) {
        columnes = novesColumnes

    }

    fun onControlTempsChange(actiu: Boolean) {
        controlTemps = actiu
    }
}