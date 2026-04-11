package com.example.practica

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class MainViewModel : ViewModel() {
    var appTitle by mutableStateOf("CONNECT 4")
        private set

    var screenSubtitle by mutableStateOf("PRINCIPAL")
        private set

}