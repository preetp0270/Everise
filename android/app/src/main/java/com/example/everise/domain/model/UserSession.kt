package com.example.everise.domain.model

data class UserSession(
    val userId: String,
    val email: String,
    val displayName: String,
    val accessToken: String,
    val refreshToken: String,
    val isGuest: Boolean = false,
)
