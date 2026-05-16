package com.example.practica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.practica.data.GameRecord
import com.example.practica.data.GameRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

class GameHistoryViewModel(repository: GameRepository) : ViewModel() {

    val selectedFilter = MutableStateFlow("Totes")

    val uiState: StateFlow<List<GameRecord>> = combine(repository.allRecords, selectedFilter) { records, filter ->
        if (filter == "Totes") {
            records
        } else {
            records.filter { it.result == filter }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onFilterChange(newFilter: String) {
        selectedFilter.value = newFilter
    }
}