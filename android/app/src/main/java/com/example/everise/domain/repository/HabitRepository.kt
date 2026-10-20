package com.example.everise.domain.repository

import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.HabitLog
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.domain.model.LogStatus
import kotlinx.coroutines.flow.Flow

interface HabitRepository {
    fun observeTodayHabits(): Flow<List<HabitWithTodayStatus>>
    fun observeHabit(id: String): Flow<Habit?>
    fun observeLogs(habitId: String): Flow<List<HabitLog>>
    fun observeDailyProgress(): Flow<DailyProgress>
    suspend fun getHabit(id: String): Habit?
    suspend fun createHabit(habit: Habit): Habit
    suspend fun updateHabit(habit: Habit): Habit
    suspend fun archiveHabit(id: String)
    suspend fun deleteHabit(id: String)
    suspend fun setTodayStatus(habitId: String, status: LogStatus, value: Int? = null, note: String = "")
    suspend fun undoToday(habitId: String)
}
