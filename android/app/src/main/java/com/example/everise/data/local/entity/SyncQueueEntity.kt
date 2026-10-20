package com.example.everise.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sync_queue")
data class SyncQueueEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val entityType: String, // habit | habit_log
    val entityId: String,
    val operation: String, // create | update | delete | complete
    val payloadJson: String,
    val createdAt: Long = System.currentTimeMillis(),
    val attempts: Int = 0,
    val lastError: String? = null,
)
