package com.example.everise.core.network

data class ApiResponse<T>(
    val success: Boolean,
    val data: T? = null,
    val error: ApiErrorBody? = null,
)

data class ApiErrorBody(
    val code: String? = null,
    val message: String? = null,
)

data class AuthPayload(
    val user: RemoteUser,
    val accessToken: String,
    val refreshToken: String,
    val expiresAt: String? = null,
)

data class RemoteUser(
    val id: String,
    val email: String,
    val displayName: String? = null,
)

data class RegisterRequest(
    val email: String,
    val password: String,
    val displayName: String = "",
    val timezone: String = "UTC",
)

data class LoginRequest(
    val email: String,
    val password: String,
    val deviceInfo: String = "android",
)

data class RefreshRequest(
    val refreshToken: String,
    val deviceInfo: String = "android",
)

data class RemoteHabit(
    val id: String? = null,
    val name: String,
    val description: String? = "",
    val icon: String? = "spa",
    val color: String? = "#5D9FC6",
    val category: String? = "personal_development",
    val schedule: RemoteSchedule? = null,
    val target: RemoteTarget? = null,
    val reminder: RemoteReminder? = null,
    val startDate: String,
    val endDate: String? = null,
    val isArchived: Boolean? = false,
    val isPinned: Boolean? = false,
    val priority: Int? = 0,
    val order: Int? = 0,
    val isNegative: Boolean? = false,
    val implementationIntention: String? = "",
    val minVersion: String? = "",
)

data class RemoteSchedule(
    val type: String = "daily",
    val daysOfWeek: List<Int> = emptyList(),
    val interval: Int = 1,
)

data class RemoteTarget(
    val type: String = "checkbox",
    val value: Int = 1,
    val unit: String = "",
)

data class RemoteReminder(
    val enabled: Boolean = false,
    val times: List<String> = emptyList(),
    val days: List<Int> = emptyList(),
)

data class RemoteHabitLog(
    val id: String? = null,
    val habitId: String,
    val occurrenceDate: String,
    val status: String,
    val value: Int? = null,
    val note: String? = "",
    val idempotencyKey: String,
)

data class CreateHabitLogRequest(
    val habitId: String,
    val occurrenceDate: String,
    val status: String,
    val value: Int? = null,
    val note: String? = "",
    val idempotencyKey: String,
)
