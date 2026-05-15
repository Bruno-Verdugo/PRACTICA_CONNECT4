package com.example.practica.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [GameRecord::class], version = 1, exportSchema = false)
abstract class GameRoomDatabase : RoomDatabase() {

    abstract fun gameRecordDao(): GameRecordDao

    private class GameDatabaseCallback(
        private val scope: CoroutineScope
    ) : Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.gameRecordDao())
                }
            }
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: GameRoomDatabase? = null

        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): GameRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GameRoomDatabase::class.java,
                    "game_records_database"
                )

                    .fallbackToDestructiveMigration()
                    .addCallback(GameDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }

        suspend fun populateDatabase(dao: GameRecordDao) {
            dao.deleteAll()
        }
    }
}