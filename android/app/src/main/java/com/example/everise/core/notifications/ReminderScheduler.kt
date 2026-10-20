package com.example.everise.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object ReminderScheduler {
    const val CHANNEL_ID = "habit_reminders"

    fun ensureChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val mgr = context.getSystemService(NotificationManager::class.java)
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Habit reminders",
                NotificationManager.IMPORTANCE_DEFAULT,
            ).apply {
                description = "Gentle reminders for your daily habits"
            }
            mgr?.createNotificationChannel(channel)
        }
    }

    fun rescheduleAll(context: Context) {
        ensureChannel(context)
        // Load habits with reminderEnabled from Room and schedule via WorkManager / AlarmManager.
        // Implemented as extension point for Phase 8 completion on device.
    }
}
