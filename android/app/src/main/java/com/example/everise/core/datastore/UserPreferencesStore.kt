package com.example.everise.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.prefsDataStore by preferencesDataStore("everise_prefs")

data class UserPrefs(
    val theme: String = "system", // system | light | dark
    val notificationsEnabled: Boolean = true,
    val quietHoursEnabled: Boolean = false,
    val weekStartsOn: String = "monday", // monday | sunday
    val onboardingDone: Boolean = false,
)

class UserPreferencesStore(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme")
    private val notifKey = booleanPreferencesKey("notifications_enabled")
    private val quietKey = booleanPreferencesKey("quiet_hours")
    private val weekKey = stringPreferencesKey("week_starts_on")
    private val onboardingKey = booleanPreferencesKey("onboarding_done")

    val prefs: Flow<UserPrefs> = context.prefsDataStore.data.map { p ->
        UserPrefs(
            theme = p[themeKey] ?: "system",
            notificationsEnabled = p[notifKey] ?: true,
            quietHoursEnabled = p[quietKey] ?: false,
            weekStartsOn = p[weekKey] ?: "monday",
            onboardingDone = p[onboardingKey] ?: false,
        )
    }

    suspend fun setTheme(theme: String) {
        context.prefsDataStore.edit { it[themeKey] = theme }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.prefsDataStore.edit { it[notifKey] = enabled }
    }

    suspend fun setQuietHours(enabled: Boolean) {
        context.prefsDataStore.edit { it[quietKey] = enabled }
    }

    suspend fun setWeekStartsOn(value: String) {
        context.prefsDataStore.edit { it[weekKey] = value }
    }

    suspend fun setOnboardingDone(done: Boolean = true) {
        context.prefsDataStore.edit { it[onboardingKey] = done }
    }

    suspend fun isOnboardingDone(): Boolean {
        return prefs.first().onboardingDone
    }
}
