package com.example.everise.app

import android.app.Application
import com.example.everise.di.AppContainer

class EveriseApp : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
