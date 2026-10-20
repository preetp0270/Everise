package com.example.everise.di

import android.content.Context
import com.example.everise.core.database.EveriseDatabase
import com.example.everise.core.datastore.SessionStore
import com.example.everise.core.network.ApiClient
import com.example.everise.core.network.AuthApi
import com.example.everise.core.network.HabitApi
import com.example.everise.data.repository.AuthRepositoryImpl
import com.example.everise.data.repository.HabitRepositoryImpl
import com.example.everise.domain.repository.AuthRepository
import com.example.everise.domain.repository.HabitRepository

/**
 * Manual DI container — keeps Phase structure simple without Hilt yet.
 */
class AppContainer(context: Context) {
    private val appContext = context.applicationContext
    private val db = EveriseDatabase.get(appContext)

    val sessionStore = SessionStore(appContext)
    val apiClient = ApiClient(sessionStore)
    val authApi: AuthApi get() = apiClient.authApi
    val habitApi: HabitApi get() = apiClient.habitApi

    val habitRepository: HabitRepository = HabitRepositoryImpl(
        habitDao = db.habitDao(),
        logDao = db.habitLogDao(),
        syncQueueDao = db.syncQueueDao(),
    )

    val authRepository: AuthRepository = AuthRepositoryImpl(
        authApi = authApi,
        sessionStore = sessionStore,
    )

    val syncQueueDao = db.syncQueueDao()
}
