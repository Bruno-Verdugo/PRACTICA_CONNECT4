package com.example.practica.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "game_records_table")
data class GameRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "alias")
    val alias: String,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "columns")
    val columns: Int,

    @ColumnInfo(name = "difficulty")
    val difficulty: String,

    @ColumnInfo(name = "time_left")
    val timeLeft: Int,

    @ColumnInfo(name = "result")
    val result: String
) : Serializable