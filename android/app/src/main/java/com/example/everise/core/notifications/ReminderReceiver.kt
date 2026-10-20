package com.example.everise.core.notifications

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.everise.MainActivity
import com.example.everise.R

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val title = intent?.getStringExtra(EXTRA_TITLE) ?: "Habit reminder"
        val body = intent?.getStringExtra(EXTRA_BODY) ?: "Time for your habit"
        val habitId = intent?.getStringExtra(EXTRA_HABIT_ID) ?: ""

        ReminderScheduler.ensureChannel(context)

        val open = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            putExtra("habitId", habitId)
        }
        val pending = PendingIntent.getActivity(
            context,
            habitId.hashCode(),
            open,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, ReminderScheduler.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pending)
            .setAutoCancel(true)
            .build()

        try {
            NotificationManagerCompat.from(context).notify(habitId.hashCode(), notification)
        } catch (_: SecurityException) {
            // POST_NOTIFICATIONS not granted
        }
    }

    companion object {
        const val EXTRA_TITLE = "title"
        const val EXTRA_BODY = "body"
        const val EXTRA_HABIT_ID = "habit_id"
    }
}
