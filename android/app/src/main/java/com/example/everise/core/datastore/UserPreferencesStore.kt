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
    val theme: String = "dark", // system | light | dark
    val oledPureBlack: Boolean = true,
    val notificationsEnabled: Boolean = true,
    val quietHoursEnabled: Boolean = false,
    val weekStartsOn: String = "monday", // monday | sunday
    val soundHaptics: Boolean = true,
    val smartHabitStacking: Boolean = true,
    val streakFreeze: Boolean = true,
    val biometricLock: Boolean = false,
    val dailyReviewTime: String = "9:30 PM",
    val quietHoursRange: String = "10:00 PM – 7:00 AM",
    val onboardingDone: Boolean = false,
    val notificationsPrompted: Boolean = false,
)

class UserPreferencesStore(private val context: Context) {
    private val themeKey = stringPreferencesKey("theme")
    private val oledKey = booleanPreferencesKey("oled_pure_black")
    private val notifKey = booleanPreferencesKey("notifications_enabled")
    private val quietKey = booleanPreferencesKey("quiet_hours")
    private val weekKey = stringPreferencesKey("week_starts_on")
    private val soundHapticsKey = booleanPreferencesKey("sound_haptics")
    private val smartStackingKey = booleanPreferencesKey("smart_habit_stacking")
    private val streakFreezeKey = booleanPreferencesKey("streak_freeze")
    private val biometricKey = booleanPreferencesKey("biometric_lock")
    private val reviewTimeKey = stringPreferencesKey("daily_review_time")
    private val quietRangeKey = stringPreferencesKey("quiet_hours_range")
    private val onboardingKey = booleanPreferencesKey("onboarding_done")
    private val notifPromptedKey = booleanPreferencesKey("notifications_prompted")

    val prefs: Flow<UserPrefs> = context.prefsDataStore.data.map { p ->
        UserPrefs(
            theme = p[themeKey] ?: "dark",
            oledPureBlack = p[oledKey] ?: true,
            notificationsEnabled = p[notifKey] ?: true,
            quietHoursEnabled = p[quietKey] ?: false,
            weekStartsOn = p[weekKey] ?: "monday",
            soundHaptics = p[soundHapticsKey] ?: true,
            smartHabitStacking = p[smartStackingKey] ?: true,
            streakFreeze = p[streakFreezeKey] ?: true,
            biometricLock = p[biometricKey] ?: false,
            dailyReviewTime = p[reviewTimeKey] ?: "9:30 PM",
            quietHoursRange = p[quietRangeKey] ?: "10:00 PM – 7:00 AM",
            onboardingDone = p[onboardingKey] ?: false,
            notificationsPrompted = p[notifPromptedKey] ?: false,
        )
    }

    suspend fun setTheme(theme: String) {
        context.prefsDataStore.edit { it[themeKey] = theme }
    }

    suspend fun setOledPureBlack(enabled: Boolean) {
        context.prefsDataStore.edit { it[oledKey] = enabled }
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

    suspend fun setSoundHaptics(enabled: Boolean) {
        context.prefsDataStore.edit { it[soundHapticsKey] = enabled }
    }

    suspend fun setSmartHabitStacking(enabled: Boolean) {
        context.prefsDataStore.edit { it[smartStackingKey] = enabled }
    }

    suspend fun setStreakFreeze(enabled: Boolean) {
        context.prefsDataStore.edit { it[streakFreezeKey] = enabled }
    }

    suspend fun setBiometricLock(enabled: Boolean) {
        context.prefsDataStore.edit { it[biometricKey] = enabled }
    }

    suspend fun setDailyReviewTime(time: String) {
        context.prefsDataStore.edit { it[reviewTimeKey] = time }
    }

    suspend fun setQuietHoursRange(range: String) {
        context.prefsDataStore.edit { it[quietRangeKey] = range }
    }

    suspend fun setOnboardingDone(done: Boolean = true) {
        context.prefsDataStore.edit { it[onboardingKey] = done }
    }

    suspend fun setNotificationsPrompted(done: Boolean = true) {
        context.prefsDataStore.edit { it[notifPromptedKey] = done }
    }

    suspend fun isOnboardingDone(): Boolean {
        return prefs.first().onboardingDone
    }
}
