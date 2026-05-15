package com.example.practica.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewModelScope
import com.example.practica.data.GameRecord
import com.example.practica.data.GameRepository
import kotlinx.coroutines.launch

class ResultsViewModel(private val repository: GameRepository) : ViewModel() {
    var dateText by mutableStateOf("")
    var logText by mutableStateOf("")
    var emailText by mutableStateOf("mvs33@alumnes.udl.cat")

    private var initialized = false

    fun initData(alias: String, columns: Int, timeLeft: Int, result: String, difficulty: String) {
        if (initialized) return

        val dateFormat = SimpleDateFormat("MMM dd, yyyy h:mm:ss a", Locale.getDefault())
        dateText = dateFormat.format(Date())

        val timeControlled = timeLeft > 0 || result == "TEMPS ESGOTAT"
        val timeSpent = if (timeControlled) 120 - timeLeft else 0

        val commonLog = "Alias: $alias\nMida graella: $columns, Dificultat: $difficulty, Temps Total: $timeSpent segons.\n"
        val specificLog = when (result) {
            "HAS GUANYAT" -> {
                var log = "Has guanyat!"
                if (timeControlled) log += "\nHan sobrat $timeLeft segons!"
                log
            }
            "GUANYA LA MÀQUINA" -> "Ha guanyat la màquina!"
            "EMPAT" -> "Heu empatat!"
            "TEMPS ESGOTAT" -> "Has esgotat el temps!"
            else -> ""
        }
        logText = commonLog + specificLog

        val record = GameRecord(
            alias = alias,
            date = dateText,
            columns = columns,
            difficulty = difficulty,
            timeLeft = timeLeft,
            result = result
        )

        viewModelScope.launch {
            repository.insertRecord(record)
        }

        initialized = true
    }
}