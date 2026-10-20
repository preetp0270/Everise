package com.example.everise.app

import android.app.Application
import com.example.everise.core.common.isOnline
import com.example.everise.core.notifications.ReminderScheduler
import com.example.everise.core.sync.SyncWorker
import com.example.everise.di.AppContainer

class EveriseApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
        ReminderScheduler.ensureChannel(this)
        // Upload any offline work as soon as the process starts online
        if (isOnline(this)) {
            SyncWorker.enqueue(this)
        }
    }
}
