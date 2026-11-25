package com.homeofhope.app.data.local

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.authSessionDataStore by preferencesDataStore(
    name = "auth_session"
)

class AuthSessionStore(private val context: Context) {

    private object Keys {
        val token = stringPreferencesKey("api_token")
        val serverUserId = intPreferencesKey("server_user_id")
    }

    val token: Flow<String?> = context.authSessionDataStore.data.map {
        preferences -> preferences[Keys.token]
    }

    suspend fun save(token: String, serverUserId: Int) {
        context.authSessionDataStore.edit { preferences ->
            preferences[Keys.token] = token
            preferences[Keys.serverUserId] = serverUserId
        }
    }

    suspend fun currentToken(): String? = token.first()

    suspend fun clearIfToken(expectedToken: String?) {
        context.authSessionDataStore.edit { preferences ->
            if (preferences[Keys.token] == expectedToken) {
                preferences.remove(Keys.token)
                preferences.remove(Keys.serverUserId)
            }
        }
    }

    suspend fun clear() {
        context.authSessionDataStore.edit { preferences ->
            preferences.remove(Keys.token)
            preferences.remove(Keys.serverUserId)
        }
    }
}
