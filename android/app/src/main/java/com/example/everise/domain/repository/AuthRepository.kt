package com.example.everise.domain.repository

import com.example.everise.domain.model.UserSession
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    val session: Flow<UserSession?>
    suspend fun getSession(): UserSession?
    /** True if a real account is stored on this device (skip login). */
    suspend fun isLoggedIn(): Boolean
    suspend fun register(email: String, password: String, displayName: String): Result<UserSession>
    suspend fun login(email: String, password: String): Result<UserSession>
    suspend fun logout()
    suspend fun refreshIfNeeded(): Boolean
}
