package com.example.everise.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.everise.data.local.entity.HabitLogEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface HabitLogDao {
    @Query("SELECT * FROM habit_logs WHERE occurrenceDate = :date")
    fun observeForDate(date: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND occurrenceDate = :date LIMIT 1")
    suspend fun getForHabitAndDate(habitId: String, date: String): HabitLogEntity?

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId ORDER BY occurrenceDate DESC")
    fun observeForHabit(habitId: String): Flow<List<HabitLogEntity>>

    @Query("SELECT * FROM habit_logs WHERE habitId = :habitId AND occurrenceDate BETWEEN :start AND :end")
    suspend fun getInRange(habitId: String, start: String, end: String): List<HabitLogEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(log: HabitLogEntity)

    @Query("DELETE FROM habit_logs WHERE id = :id")
    suspend fun delete(id: String)

    @Query("DELETE FROM habit_logs WHERE habitId = :habitId AND occurrenceDate = :date")
    suspend fun deleteForHabitAndDate(habitId: String, date: String)

    @Query("SELECT * FROM habit_logs WHERE pendingSync = 1")
    suspend fun getPendingSync(): List<HabitLogEntity>

    @Query("SELECT COUNT(*) FROM habit_logs WHERE status = 'COMPLETED'")
    suspend fun countCompleted(): Int
}
