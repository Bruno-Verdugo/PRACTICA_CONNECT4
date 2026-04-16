package com.example.practica

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var gameTitle by mutableStateOf("CONNECT 4")
        private set

    var title by mutableStateOf("PRINCIPAL")
        private set

}