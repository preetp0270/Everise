package com.example.everise.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habit_logs",
    indices = [
        Index(value = ["habitId", "occurrenceDate"], unique = true),
        Index(value = ["idempotencyKey"], unique = true),
        Index("occurrenceDate"),
    ],
)
data class HabitLogEntity(
    @PrimaryKey val id: String,
    val habitId: String,
    val occurrenceDate: String,
    val status: String,
    val value: Int?,
    val note: String,
    val idempotencyKey: String,
    val completedAt: Long?,
    val remoteId: String?,
    val pendingSync: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)
