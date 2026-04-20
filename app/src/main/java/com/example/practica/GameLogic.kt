package com.example.practica

import kotlin.math.abs
import kotlin.math.max

data class Direction(val changeInRow: Int, val changeInColumn: Int) {

    fun invert(): Direction {
        return Direction(-changeInRow, -changeInColumn)
    }

    companion object {
        val DOWN = Direction(1, 0)
        val RIGHT = Direction(0, 1)
        val MAIN_DIAGONAL = Direction(1, 1)
        val CONTRA_DIAGONAL = Direction(1, -1)

        val ALL = arrayOf(RIGHT, DOWN, MAIN_DIAGONAL, CONTRA_DIAGONAL)
    }
}

data class Position(val row: Int, val column: Int) {

    fun move(direction: Direction): Position {
        return Position(row + direction.changeInRow, column + direction.changeInColumn)
    }

    companion object {
        fun pathLength(pos1: Position, pos2: Position): Int {
            val rowDiff = abs(pos1.row - pos2.row)
            val colDiff = abs(pos1.column - pos2.column)
            return max(rowDiff, colDiff)
        }
    }
}

enum class Player {
    NONE, HUMAN, SYSTEM
}

class Board(val size: Int) {

    val columns = size
    val rows = size

    val grid = Array(rows) { Array(columns) { Player.NONE } }

    fun firstEmptyRow(column: Int): Int {
        for (r in rows - 1 downTo 0) {
            if (grid[r][column] == Player.NONE) {
                return r
            }
        }
        return -1
    }

    fun occupyCell(column: Int, player: Player): Position? {
        val row = firstEmptyRow(column)
        if (row != -1) {
            grid[row][column] = player
            return Position(row, column)
        }
        return null
    }

    fun hasValidMoves(): Boolean {
        for (c in 0 until columns) {
            if (firstEmptyRow(c) != -1) return true
        }
        return false
    }

    fun maxConnected(position: Position): Int {
        val player = grid[position.row][position.column]
        if (player == Player.NONE) return 0

        var maxConsecutive = 0

        for (dir in Direction.ALL) {
            val end1 = furthestInDirection(position, dir, player)
            val end2 = furthestInDirection(position, dir.invert(), player)
            val count = Position.pathLength(end1, end2) + 1

            if (count > maxConsecutive) {
                maxConsecutive = count
            }
        }
        return maxConsecutive
    }

    private fun furthestInDirection(startPos: Position, dir: Direction, player: Player): Position {
        var currentPos = startPos
        var nextPos = currentPos.move(dir)
        while (isValid(nextPos) && grid[nextPos.row][nextPos.column] == player) {
            currentPos = nextPos
            nextPos = currentPos.move(dir)
        }

        return currentPos
    }

    private fun isValid(pos: Position): Boolean {
        return pos.row in 0 until rows && pos.column in 0 until columns
    }
}