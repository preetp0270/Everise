package com.example.everise.data.local.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "habits",
    indices = [Index("isArchived"), Index("order"), Index("remoteId")],
)
data class HabitEntity(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val icon: String,
    val color: String,
    val category: String,
    val scheduleType: String,
    val daysOfWeek: String, // comma-separated
    val targetType: String,
    val targetValue: Int,
    val targetUnit: String,
    val reminderEnabled: Boolean,
    val reminderTimes: String,
    val startDate: String,
    val endDate: String?,
    val isArchived: Boolean,
    val isPinned: Boolean,
    val priority: Int,
    val order: Int,
    val implementationIntention: String,
    val minVersion: String,
    val isNegative: Boolean,
    val syncVersion: Long,
    val remoteId: String?,
    val pendingSync: Boolean = false,
    val createdAt: Long,
    val updatedAt: Long,
)
