package com.example.everise.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.everise.domain.model.UserSession
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("everise_session")

class SessionStore(private val context: Context) {
    private val keys = object {
        val userId = stringPreferencesKey("user_id")
        val email = stringPreferencesKey("email")
        val displayName = stringPreferencesKey("display_name")
        val access = stringPreferencesKey("access_token")
        val refresh = stringPreferencesKey("refresh_token")
        val guest = stringPreferencesKey("is_guest")
    }

    val sessionFlow: Flow<UserSession?> = context.dataStore.data.map { prefs ->
        val userId = prefs[keys.userId] ?: return@map null
        UserSession(
            userId = userId,
            email = prefs[keys.email].orEmpty(),
            displayName = prefs[keys.displayName].orEmpty(),
            accessToken = prefs[keys.access].orEmpty(),
            refreshToken = prefs[keys.refresh].orEmpty(),
            isGuest = prefs[keys.guest] == "1",
        )
    }

    /** True when a real (non-guest) account is stored on this device. */
    suspend fun hasLoggedInSession(): Boolean {
        val s = getSession() ?: return false
        if (s.isGuest) return false
        return s.userId.isNotBlank() && (s.accessToken.isNotBlank() || s.refreshToken.isNotBlank())
    }

    suspend fun getSession(): UserSession? = sessionFlow.first()

    suspend fun save(session: UserSession) {
        context.dataStore.edit { prefs ->
            prefs[keys.userId] = session.userId
            prefs[keys.email] = session.email
            prefs[keys.displayName] = session.displayName
            prefs[keys.access] = session.accessToken
            prefs[keys.refresh] = session.refreshToken
            prefs[keys.guest] = if (session.isGuest) "1" else "0"
        }
    }

    suspend fun clear() {
        context.dataStore.edit { it.clear() }
    }

    suspend fun getAccessToken(): String? = getSession()?.accessToken?.takeIf { it.isNotBlank() }

    suspend fun getBearer(): String? = getAccessToken()?.let { "Bearer $it" }

    suspend fun updateTokens(access: String, refresh: String) {
        context.dataStore.edit { prefs ->
            prefs[keys.access] = access
            prefs[keys.refresh] = refresh
        }
    }
}
