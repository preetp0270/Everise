package com.example.everise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.everise.data.local.entity.HabitEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitDao {
    @Query("SELECT * FROM habits WHERE isArchived = 0 ORDER BY isPinned DESC, `order` ASC, createdAt DESC")
    fun observeActive(): Flow<List<HabitEntity>>

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): HabitEntity?

    @Query("SELECT * FROM habits WHERE id = :id LIMIT 1")
    fun observeById(id: String): Flow<HabitEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(habit: HabitEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(habits: List<HabitEntity>)

    @Update
    suspend fun update(habit: HabitEntity)

    @Query("UPDATE habits SET isArchived = 1, updatedAt = :now, pendingSync = 1 WHERE id = :id")
    suspend fun archive(id: String, now: Long = System.currentTimeMillis())

    @Query("UPDATE habits SET isArchived = 0, updatedAt = :now, pendingSync = 1 WHERE id = :id")
    suspend fun restore(id: String, now: Long = System.currentTimeMillis())

    @Query("DELETE FROM habits WHERE id = :id")
    suspend fun delete(id: String)

    @Query("SELECT * FROM habits WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<HabitEntity>

    @Query("SELECT COUNT(*) FROM habits WHERE isArchived = 0")
    suspend fun countActive(): Int
}
