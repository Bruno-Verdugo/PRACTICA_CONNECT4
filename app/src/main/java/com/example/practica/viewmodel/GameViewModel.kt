package com.example.practica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.practica.model.Board
import com.example.practica.model.Player
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class GameStatus {
    PLAYING, WON, DRAW, TIME_OUT
}

class GameViewModel : ViewModel() {

    lateinit var board: Board
        private set

    var currentTurn by mutableStateOf(Player.HUMAN)
        private set

    var status by mutableStateOf(GameStatus.PLAYING)
        private set

    var winner by mutableStateOf(Player.NONE)
        private set

    var boardUpdateTrigger by mutableIntStateOf(0)
        private set

    var isBoardCreated by mutableStateOf(false)
        private set

    var isTimeEnabled by mutableStateOf(false)
        private set

    var timeLeft by mutableIntStateOf(0)
        private set

    private var timerJob: Job? = null

    var columnFullTrigger by mutableIntStateOf(0)
        private set

    var finalResultText by mutableStateOf("")
        private set

    var difficulty by mutableStateOf("Fàcil")
        private set

    fun startGame(columns: Int, time: Boolean, diff: String) {
        board = Board(columns)
        currentTurn = Player.HUMAN
        status = GameStatus.PLAYING
        winner = Player.NONE
        boardUpdateTrigger = 0
        isBoardCreated = true
        isTimeEnabled = time
        finalResultText = ""
        difficulty = diff
        if (isTimeEnabled) {
            manageTime(45)
        }
    }

    fun drop(col: Int) {
        if (status != GameStatus.PLAYING) return

        val pos = board.occupyCell(col, currentTurn)

        if (pos != null) {
            boardUpdateTrigger++

            if (board.maxConnected(pos) >= 4) {
                status = GameStatus.WON
                winner = currentTurn
                timerJob?.cancel()
                prepareResult("HAS GUANYAT", "GUANYA LA MÀQUINA")
            } else if (!board.hasValidMoves()) {
                status = GameStatus.DRAW
                timerJob?.cancel()
                finalResultText = "EMPAT"
            } else {
                toggleTurn()
            }
        } else {
            if (currentTurn == Player.HUMAN) {
                columnFullTrigger++
            }
        }
    }

    private fun prepareResult(humanText: String, systemText: String) {
        finalResultText = if (winner == Player.HUMAN) humanText else systemText
    }

    private fun manageTime(seconds: Int) {
        timerJob?.cancel()
        timeLeft = seconds

        timerJob = viewModelScope.launch {
            while (timeLeft > 0 && status == GameStatus.PLAYING) {
                delay(1000L)
                timeLeft--

                if (timeLeft == 0 && status == GameStatus.PLAYING) {
                    status = GameStatus.TIME_OUT
                    finalResultText = "TEMPS ESGOTAT"
                }
            }
        }
    }

    private fun toggleTurn() {
        currentTurn = if (currentTurn == Player.HUMAN) Player.SYSTEM else Player.HUMAN

        if (currentTurn == Player.SYSTEM && status == GameStatus.PLAYING) {
            viewModelScope.launch {
                delay(800L)
                playOpponent()
            }
        }
    }

    private fun playOpponent() {
        val validColumns = mutableListOf<Int>()
        for (c in 0 until board.columns) {
            if (board.firstEmptyRow(c) != -1) {
                validColumns.add(c)
            }
        }
        if (validColumns.isEmpty()) return

        val chosenColumn = when (difficulty) {
            "Fàcil" -> validColumns.random()
            "Mitjana" -> playMedium(validColumns)
            "Difícil" -> playHard(validColumns)
            else -> validColumns.random()
        }
        drop(chosenColumn)
    }

    private fun playMedium(validColumns: List<Int>): Int {
        val winningMove = findWinningMove(Player.SYSTEM, validColumns)
        if (winningMove != -1) {
            return winningMove
        }

        val blockingMove = findWinningMove(Player.HUMAN, validColumns)
        if (blockingMove != -1) {
            return blockingMove
        }
        return validColumns.random()
    }

    private fun playHard(validColumns: List<Int>): Int {
        val winningMove = findWinningMove(Player.SYSTEM, validColumns)
        if (winningMove != -1) {
            return winningMove
        }

        val blockingMove = findWinningMove(Player.HUMAN, validColumns)
        if (blockingMove != -1) {
            return blockingMove
        }

        val safeColumns = validColumns.filter { col ->
            !givesOpponentWin(col, Player.HUMAN)
        }

        val columnsToConsider = if (safeColumns.isNotEmpty()) {
            safeColumns
        } else {
            validColumns
        }

        val threatMoves = columnsToConsider.filter { col ->
            createsThreat(col, Player.SYSTEM)
        }
        if (threatMoves.isNotEmpty()) {
            return threatMoves.random()
        }

        val centerCol = board.columns / 2
        return columnsToConsider.sortedBy { kotlin.math.abs(it - centerCol) }.first()
    }

    private fun findWinningMove(playerToCheck: Player, validColumns: List<Int>): Int {
        for (col in validColumns) {
            val row = board.firstEmptyRow(col)
            if (row != -1) {
                board.grid[row][col] = playerToCheck
                val pos = com.example.practica.model.Position(row, col)
                val connects = board.maxConnected(pos)
                board.grid[row][col] = Player.NONE

                if (connects >= 4) {
                    return col
                }
            }
        }
        return -1
    }

    private fun givesOpponentWin(col: Int, opponent: Player): Boolean {
        var isSuicide = false
        val row = board.firstEmptyRow(col)
        if (row != -1) {
            board.grid[row][col] = Player.SYSTEM
            val nextRow = row - 1
            if (nextRow >= 0) {
                board.grid[nextRow][col] = opponent
                val pos = com.example.practica.model.Position(nextRow, col)
                if (board.maxConnected(pos) >= 4) {
                    isSuicide = true
                }
                board.grid[nextRow][col] = Player.NONE
            }
            board.grid[row][col] = Player.NONE
        }
        return isSuicide
    }

    private fun createsThreat(col: Int, me: Player): Boolean {
        var createsWin = false
        val row = board.firstEmptyRow(col)
        if (row != -1) {
            board.grid[row][col] = me

            val tempValid = mutableListOf<Int>()
            for (c in 0 until board.columns) {
                if (board.firstEmptyRow(c) != -1) tempValid.add(c)
            }
            if (findWinningMove(me, tempValid) != -1) {
                createsWin = true
            }

            board.grid[row][col] = Player.NONE
        }
        return createsWin
    }

    val statusText: String
        get() = when (status) {
            GameStatus.PLAYING -> if (currentTurn == Player.HUMAN) "El teu torn (Vermell)" else "Torn de la màquina (Groc)"
            GameStatus.WON -> if (winner == Player.HUMAN) "HAS GUANYAT!" else "GUANYA LA MÀQUINA!"
            GameStatus.DRAW -> "EMPAT!"
            GameStatus.TIME_OUT -> "TEMPS ESGOTAT!"
        }

    val timeText: String
        get() = if (isTimeEnabled) {
            "${timeLeft} segons."
        } else {
            "No hi ha temps limit"
        }
}