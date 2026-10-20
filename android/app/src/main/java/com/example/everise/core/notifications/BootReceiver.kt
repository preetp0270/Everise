package com.example.everise.core.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.everise.core.database.EveriseDatabase
import com.example.everise.data.mapper.toDomain
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        if (intent?.action != Intent.ACTION_BOOT_COMPLETED) return
        Log.i("Everise", "Boot completed — rescheduling habit reminders")
        val pending = goAsync()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                ReminderScheduler.ensureChannel(context)
                val habits = EveriseDatabase.get(context).habitDao().listActive()
                habits.map { it.toDomain() }.forEach { habit ->
                    if (habit.reminderEnabled) {
                        ReminderScheduler.scheduleHabit(context, habit)
                    }
                }
            } catch (e: Exception) {
                Log.e("Everise", "Reschedule failed: ${e.message}")
            } finally {
                pending.finish()
            }
        }
    }
}
