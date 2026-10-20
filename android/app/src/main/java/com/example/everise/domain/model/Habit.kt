package com.example.everise.domain.model

/**
 * Domain habit — pure Kotlin, no Android/Room deps.
 */
data class Habit(
    val id: String,
    val name: String,
    val description: String = "",
    val icon: String = "spa",
    val color: String = "#5D9FC6",
    val category: String = "personal_development",
    val scheduleType: ScheduleType = ScheduleType.DAILY,
    val daysOfWeek: List<Int> = emptyList(),
    val targetType: TargetType = TargetType.CHECKBOX,
    val targetValue: Int = 1,
    val targetUnit: String = "",
    val reminderEnabled: Boolean = false,
    val reminderTimes: List<String> = emptyList(),
    val startDate: String, // YYYY-MM-DD
    val endDate: String? = null,
    val isArchived: Boolean = false,
    val isPinned: Boolean = false,
    val priority: Int = 0,
    val order: Int = 0,
    val implementationIntention: String = "",
    val minVersion: String = "",
    val isNegative: Boolean = false,
    val syncVersion: Long = 1,
    val remoteId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class ScheduleType { DAILY, WEEKDAYS, WEEKLY, CUSTOM }

enum class TargetType { CHECKBOX, DURATION, QUANTITY, REPS }

enum class LogStatus { COMPLETED, SKIPPED, INCOMPLETE }

data class HabitLog(
    val id: String,
    val habitId: String,
    val occurrenceDate: String, // YYYY-MM-DD
    val status: LogStatus,
    val value: Int? = null,
    val note: String = "",
    val idempotencyKey: String,
    val completedAt: Long? = null,
    val remoteId: String? = null,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

data class HabitWithTodayStatus(
    val habit: Habit,
    val todayLog: HabitLog?,
) {
    val isCompletedToday: Boolean get() = todayLog?.status == LogStatus.COMPLETED
}

data class DailyProgress(
    val completed: Int,
    val total: Int,
) {
    val percent: Int get() = if (total == 0) 0 else ((completed * 100f) / total).toInt()
}
