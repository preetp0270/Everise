package com.example.everise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.everise.data.local.entity.SyncQueueEntity

@Dao
interface SyncQueueDao {
    @Insert
    suspend fun enqueue(item: SyncQueueEntity): Long

    @Query("SELECT * FROM sync_queue ORDER BY createdAt ASC LIMIT :limit")
    suspend fun nextBatch(limit: Int = 20): List<SyncQueueEntity>

    @Query("DELETE FROM sync_queue WHERE id = :id")
    suspend fun remove(id: Long)

    @Query("UPDATE sync_queue SET attempts = attempts + 1, lastError = :error WHERE id = :id")
    suspend fun markFailed(id: Long, error: String)

    @Query("SELECT COUNT(*) FROM sync_queue")
    suspend fun count(): Int
}
