package com.example.everise.data.mapper

import com.example.everise.data.local.entity.HabitEntity
import com.example.everise.data.local.entity.HabitLogEntity
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.HabitLog
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.model.ScheduleType
import com.example.everise.domain.model.TargetType

fun HabitEntity.toDomain(): Habit = Habit(
    id = id,
    name = name,
    description = description,
    icon = icon,
    color = color,
    category = category,
    scheduleType = runCatching { ScheduleType.valueOf(scheduleType) }.getOrDefault(ScheduleType.DAILY),
    daysOfWeek = if (daysOfWeek.isBlank()) emptyList() else daysOfWeek.split(",").mapNotNull { it.toIntOrNull() },
    targetType = runCatching { TargetType.valueOf(targetType) }.getOrDefault(TargetType.CHECKBOX),
    targetValue = targetValue,
    targetUnit = targetUnit,
    reminderEnabled = reminderEnabled,
    reminderTimes = if (reminderTimes.isBlank()) emptyList() else reminderTimes.split(","),
    startDate = startDate,
    endDate = endDate,
    isArchived = isArchived,
    isPinned = isPinned,
    priority = priority,
    order = order,
    implementationIntention = implementationIntention,
    minVersion = minVersion,
    isNegative = isNegative,
    syncVersion = syncVersion,
    remoteId = remoteId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Habit.toEntity(pendingSync: Boolean = false): HabitEntity = HabitEntity(
    id = id,
    name = name,
    description = description,
    icon = icon,
    color = color,
    category = category,
    scheduleType = scheduleType.name,
    daysOfWeek = daysOfWeek.joinToString(","),
    targetType = targetType.name,
    targetValue = targetValue,
    targetUnit = targetUnit,
    reminderEnabled = reminderEnabled,
    reminderTimes = reminderTimes.joinToString(","),
    startDate = startDate,
    endDate = endDate,
    isArchived = isArchived,
    isPinned = isPinned,
    priority = priority,
    order = order,
    implementationIntention = implementationIntention,
    minVersion = minVersion,
    isNegative = isNegative,
    syncVersion = syncVersion,
    remoteId = remoteId,
    pendingSync = pendingSync,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun HabitLogEntity.toDomain(): HabitLog = HabitLog(
    id = id,
    habitId = habitId,
    occurrenceDate = occurrenceDate,
    status = runCatching { LogStatus.valueOf(status) }.getOrDefault(LogStatus.INCOMPLETE),
    value = value,
    note = note,
    idempotencyKey = idempotencyKey,
    completedAt = completedAt,
    remoteId = remoteId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun HabitLog.toEntity(pendingSync: Boolean = false): HabitLogEntity = HabitLogEntity(
    id = id,
    habitId = habitId,
    occurrenceDate = occurrenceDate,
    status = status.name,
    value = value,
    note = note,
    idempotencyKey = idempotencyKey,
    completedAt = completedAt,
    remoteId = remoteId,
    pendingSync = pendingSync,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
