package com.example.guiamapp.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("auth")

class TokenStore(private val context: Context) {
    companion object {
        private val TOKEN = stringPreferencesKey("jwt")
        private val ROLE = stringPreferencesKey("role")
    }

    suspend fun save(token: String, role: String) {
        context.dataStore.edit {
            it[TOKEN] = token
            it[ROLE] = role
        }
    }

    suspend fun getToken(): String? =
        context.dataStore.data.map { it[TOKEN] }.first()

    suspend fun getRole(): String? =
        context.dataStore.data.map { it[ROLE] }.first()

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }
}