package com.example.everise.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

/**
 * Reschedule local habit reminders after device reboot.
 * Full AlarmManager / WorkManager scheduling lands with reminder preferences.
 */
class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action == Intent.ACTION_BOOT_COMPLETED) {
            Log.i("Everise", "Boot completed — reminder reschedule hook")
            ReminderScheduler.rescheduleAll(context)
        }
    }
}
