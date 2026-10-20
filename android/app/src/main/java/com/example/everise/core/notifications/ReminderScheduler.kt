package com.example.everise.core.notifications

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.example.everise.domain.model.Habit
import java.util.Calendar

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

    fun scheduleHabit(context: Context, habit: Habit) {
        ensureChannel(context)
        cancelHabit(context, habit.id)
        if (!habit.reminderEnabled) return
        val times = habit.reminderTimes.ifEmpty { listOf("09:00") }
        times.forEachIndexed { index, time ->
            val parts = time.split(":")
            if (parts.size < 2) return@forEachIndexed
            val hour = parts[0].toIntOrNull() ?: return@forEachIndexed
            val minute = parts[1].toIntOrNull() ?: return@forEachIndexed
            scheduleDaily(context, habit.id, habit.name, hour, minute, index)
        }
    }

    fun cancelHabit(context: Context, habitId: String) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        // Cancel up to a few reminder slots per habit
        repeat(5) { index ->
            val pi = pendingIntent(context, habitId, index, null) ?: return@repeat
            am.cancel(pi)
            pi.cancel()
        }
    }

    fun rescheduleAll(context: Context) {
        ensureChannel(context)
        // Caller should load habits and invoke scheduleHabit for each.
    }

    private fun scheduleDaily(
        context: Context,
        habitId: String,
        name: String,
        hour: Int,
        minute: Int,
        index: Int,
    ) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val cal = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, ReminderReceiver::class.java).apply {
            putExtra(ReminderReceiver.EXTRA_TITLE, name)
            putExtra(ReminderReceiver.EXTRA_BODY, "Time for: $name")
            putExtra(ReminderReceiver.EXTRA_HABIT_ID, habitId)
        }
        val pi = pendingIntent(context, habitId, index, intent) ?: return

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                am.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            } else {
                am.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
            }
        } catch (_: SecurityException) {
            am.set(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        }
    }

    private fun pendingIntent(
        context: Context,
        habitId: String,
        index: Int,
        intent: Intent?,
    ): PendingIntent? {
        val i = intent ?: Intent(context, ReminderReceiver::class.java)
        val req = (habitId.hashCode() * 31 + index) and 0x7fffffff
        return PendingIntent.getBroadcast(
            context,
            req,
            i,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )
    }
}
