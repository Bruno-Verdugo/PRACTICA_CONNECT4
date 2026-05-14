package com.example.practica.viewmodel

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings_preferences")

class DataStoreManager(val context: Context) {

    private object PreferencesKeys {
        val ALIAS_KEY = stringPreferencesKey("alias_player")
        val COLUMNS_KEY = intPreferencesKey("board_columns")
        val TIME_KEY = booleanPreferencesKey("time_control")
        val DIFFICULTY_KEY = stringPreferencesKey("game_difficulty")
    }

    data class UserSettings(
        val alias: String,
        val columns: Int,
        val time: Boolean,
        val difficulty: String
    )

    fun getUserSettings(): Flow<UserSettings> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                exception.printStackTrace()
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            UserSettings(
                alias = preferences[PreferencesKeys.ALIAS_KEY] ?: "Jugador",
                columns = preferences[PreferencesKeys.COLUMNS_KEY] ?: 7,
                time = preferences[PreferencesKeys.TIME_KEY] ?: false,
                difficulty = preferences[PreferencesKeys.DIFFICULTY_KEY] ?: "Fàcil"
            )
        }

    suspend fun saveAlias(alias: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.ALIAS_KEY] = alias
        }
    }

    suspend fun saveColumns(columns: Int) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.COLUMNS_KEY] = columns
        }
    }

    suspend fun saveTime(time: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.TIME_KEY] = time
        }
    }

    suspend fun saveDifficulty(difficulty: String) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.DIFFICULTY_KEY] = difficulty
        }
    }
}