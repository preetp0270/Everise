package com.example.everise.data.repository

import com.example.everise.core.datastore.SessionStore
import com.example.everise.core.network.AuthApi
import com.example.everise.core.network.LoginRequest
import com.example.everise.core.network.RefreshRequest
import com.example.everise.core.network.RegisterRequest
import com.example.everise.domain.model.UserSession
import com.example.everise.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.util.UUID

class AuthRepositoryImpl(
    private val authApi: AuthApi,
    private val sessionStore: SessionStore,
) : AuthRepository {

    override val session: Flow<UserSession?> = sessionStore.sessionFlow

    override suspend fun register(
        email: String,
        password: String,
        displayName: String,
    ): Result<UserSession> = runCatching {
        val res = authApi.register(RegisterRequest(email, password, displayName))
        if (!res.success || res.data == null) {
            error(res.error?.message ?: "Registration failed")
        }
        val payload = res.data
        val session = UserSession(
            userId = payload.user.id,
            email = payload.user.email,
            displayName = payload.user.displayName ?: displayName,
            accessToken = payload.accessToken,
            refreshToken = payload.refreshToken,
        )
        sessionStore.save(session)
        session
    }

    override suspend fun login(email: String, password: String): Result<UserSession> = runCatching {
        val res = authApi.login(LoginRequest(email, password))
        if (!res.success || res.data == null) {
            error(res.error?.message ?: "Login failed")
        }
        val payload = res.data
        val session = UserSession(
            userId = payload.user.id,
            email = payload.user.email,
            displayName = payload.user.displayName ?: email.substringBefore("@"),
            accessToken = payload.accessToken,
            refreshToken = payload.refreshToken,
        )
        sessionStore.save(session)
        session
    }

    override suspend fun logout() {
        val current = sessionStore.sessionFlow.first()
        if (current != null && !current.isGuest && current.refreshToken.isNotBlank()) {
            runCatching {
                authApi.logout(mapOf("refreshToken" to current.refreshToken))
            }
        }
        sessionStore.clear()
    }

    override suspend fun continueAsGuest(): UserSession {
        val session = UserSession(
            userId = "guest-${UUID.randomUUID()}",
            email = "",
            displayName = "Guest",
            accessToken = "",
            refreshToken = "",
            isGuest = true,
        )
        sessionStore.save(session)
        return session
    }

    override suspend fun refreshIfNeeded(): Boolean {
        val current = sessionStore.sessionFlow.first() ?: return false
        if (current.isGuest || current.refreshToken.isBlank()) return false
        return runCatching {
            val res = authApi.refresh(RefreshRequest(current.refreshToken))
            if (!res.success || res.data == null) return false
            sessionStore.updateTokens(res.data.accessToken, res.data.refreshToken)
            true
        }.getOrDefault(false)
    }
}
