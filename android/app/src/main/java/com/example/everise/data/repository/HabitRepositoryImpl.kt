package com.example.everise.data.repository

import android.content.Context
import com.example.everise.core.common.isOnline
import com.example.everise.core.common.todayIsoDate
import com.example.everise.core.sync.SyncWorker
import com.example.everise.data.local.dao.HabitDao
import com.example.everise.data.local.dao.HabitLogDao
import com.example.everise.data.local.dao.SyncQueueDao
import com.example.everise.data.local.entity.SyncQueueEntity
import com.example.everise.data.mapper.toDomain
import com.example.everise.data.mapper.toEntity
import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.HabitLog
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.util.UUID

class HabitRepositoryImpl(
    private val appContext: Context,
    private val habitDao: HabitDao,
    private val logDao: HabitLogDao,
    private val syncQueueDao: SyncQueueDao,
) : HabitRepository {

    private fun requestSync() {
        // Always enqueue; WorkManager runs when network is available
        SyncWorker.enqueue(appContext)
    }


    override fun observeTodayHabits(): Flow<List<HabitWithTodayStatus>> {
        val today = todayIsoDate()
        return combine(
            habitDao.observeActive(),
            logDao.observeForDate(today),
        ) { habits, logs ->
            val byHabit = logs.associateBy { it.habitId }
            habits.map { entity ->
                HabitWithTodayStatus(
                    habit = entity.toDomain(),
                    todayLog = byHabit[entity.id]?.toDomain(),
                )
            }
        }
    }

    override fun observeHabit(id: String): Flow<Habit?> =
        habitDao.observeById(id).map { it?.toDomain() }

    override fun observeLogs(habitId: String): Flow<List<HabitLog>> =
        logDao.observeForHabit(habitId).map { list -> list.map { it.toDomain() } }

    override fun observeDailyProgress(): Flow<DailyProgress> =
        observeTodayHabits().map { list ->
            DailyProgress(
                completed = list.count { it.isCompletedToday },
                total = list.size,
            )
        }

    override suspend fun getHabit(id: String): Habit? = habitDao.getById(id)?.toDomain()

    override suspend fun createHabit(habit: Habit): Habit {
        val entity = habit.toEntity(pendingSync = true)
        habitDao.upsert(entity)
        syncQueueDao.enqueue(
            SyncQueueEntity(
                entityType = "habit",
                entityId = habit.id,
                operation = "create",
                payloadJson = habit.id,
            )
        )
        requestSync()
        return habit
    }

    override suspend fun updateHabit(habit: Habit): Habit {
        val updated = habit.copy(updatedAt = System.currentTimeMillis(), syncVersion = habit.syncVersion + 1)
        habitDao.upsert(updated.toEntity(pendingSync = true))
        syncQueueDao.enqueue(
            SyncQueueEntity(
                entityType = "habit",
                entityId = updated.id,
                operation = "update",
                payloadJson = updated.id,
            )
        )
        requestSync()
        return updated
    }

    override suspend fun archiveHabit(id: String) {
        habitDao.archive(id)
        syncQueueDao.enqueue(
            SyncQueueEntity(entityType = "habit", entityId = id, operation = "archive", payloadJson = id)
        )
        requestSync()
    }

    override suspend fun deleteHabit(id: String) {
        habitDao.delete(id)
        syncQueueDao.enqueue(
            SyncQueueEntity(entityType = "habit", entityId = id, operation = "delete", payloadJson = id)
        )
        requestSync()
    }

    override suspend fun setTodayStatus(
        habitId: String,
        status: LogStatus,
        value: Int?,
        note: String,
    ) {
        val today = todayIsoDate()
        val existing = logDao.getForHabitAndDate(habitId, today)
        val now = System.currentTimeMillis()
        val log = HabitLog(
            id = existing?.id ?: UUID.randomUUID().toString(),
            habitId = habitId,
            occurrenceDate = today,
            status = status,
            value = value,
            note = note,
            idempotencyKey = existing?.idempotencyKey ?: "$habitId:$today:${UUID.randomUUID()}",
            completedAt = if (status == LogStatus.COMPLETED) now else null,
            remoteId = existing?.remoteId,
            createdAt = existing?.createdAt ?: now,
            updatedAt = now,
        )
        logDao.upsert(log.toEntity(pendingSync = true))
        syncQueueDao.enqueue(
            SyncQueueEntity(
                entityType = "habit_log",
                entityId = log.id,
                operation = "complete",
                payloadJson = log.id,
            )
        )
        requestSync()
    }

    override suspend fun undoToday(habitId: String) {
        val today = todayIsoDate()
        logDao.deleteForHabitAndDate(habitId, today)
        syncQueueDao.enqueue(
            SyncQueueEntity(
                entityType = "habit_log",
                entityId = "$habitId:$today",
                operation = "undo",
                payloadJson = "$habitId:$today",
            )
        )
        requestSync()
    }
}
