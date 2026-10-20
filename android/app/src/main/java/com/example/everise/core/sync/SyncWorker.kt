package com.example.everise.core.sync

import android.content.Context
import android.util.Log
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.everise.app.EveriseApp
import com.example.everise.core.common.isOnline
import com.example.everise.core.network.CreateHabitLogRequest
import com.example.everise.core.network.RemoteHabit
import com.example.everise.core.network.RemoteReminder
import com.example.everise.core.network.RemoteSchedule
import com.example.everise.core.network.RemoteTarget
import com.example.everise.data.mapper.toDomain

/**
 * Uploads pending local changes when the device is online.
 * Habits remain fully usable offline; this only pushes the queue.
 */
class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        if (!isOnline(applicationContext)) {
            Log.i(TAG, "Offline — skip sync")
            return Result.retry()
        }

        val app = applicationContext as? EveriseApp ?: return Result.failure()
        val container = app.container
        val sessionStore = container.sessionStore
        val authRepo = container.authRepository

        if (!sessionStore.hasLoggedInSession()) {
            Log.i(TAG, "Not logged in — skip remote sync")
            return Result.success()
        }

        // Refresh token if needed before batch
        var bearer = sessionStore.getBearer()
        if (bearer == null) {
            authRepo.refreshIfNeeded()
            bearer = sessionStore.getBearer()
        }
        if (bearer == null) {
            Log.w(TAG, "No access token after refresh")
            return Result.retry()
        }

        val queue = container.syncQueueDao
        val habitDao = container.db.habitDao()
        val logDao = container.db.habitLogDao()
        val habitApi = container.habitApi

        val batch = queue.nextBatch(30)
        if (batch.isEmpty()) {
            Log.i(TAG, "Sync queue empty")
            return Result.success()
        }

        Log.i(TAG, "Uploading ${batch.size} pending items…")
        var failures = 0

        for (item in batch) {
            try {
                when (item.entityType to item.operation) {
                    "habit" to "create", "habit" to "update" -> {
                        val entity = habitDao.getById(item.entityId) ?: run {
                            queue.remove(item.id)
                            continue
                        }
                        val h = entity.toDomain()
                        val body = RemoteHabit(
                            name = h.name,
                            description = h.description,
                            icon = h.icon,
                            color = h.color,
                            category = h.category,
                            schedule = RemoteSchedule(type = h.scheduleType.name.lowercase()),
                            target = RemoteTarget(type = h.targetType.name.lowercase(), value = h.targetValue, unit = h.targetUnit),
                            reminder = RemoteReminder(enabled = h.reminderEnabled, times = h.reminderTimes),
                            startDate = h.startDate,
                            endDate = h.endDate,
                            isPinned = h.isPinned,
                            priority = h.priority,
                            order = h.order,
                            isNegative = h.isNegative,
                            implementationIntention = h.implementationIntention,
                            minVersion = h.minVersion,
                        )
                        val res = if (item.operation == "create" && h.remoteId == null) {
                            habitApi.create(bearer, body)
                        } else {
                            val remoteId = h.remoteId ?: h.id
                            habitApi.update(bearer, remoteId, body)
                        }
                        if (res.success) {
                            val remoteId = res.data?.id
                            if (remoteId != null) {
                                habitDao.upsert(entity.copy(remoteId = remoteId, pendingSync = false))
                            } else {
                                habitDao.upsert(entity.copy(pendingSync = false))
                            }
                            queue.remove(item.id)
                        } else {
                            queue.markFailed(item.id, res.error?.message ?: "habit sync failed")
                            failures++
                        }
                    }
                    "habit" to "archive" -> {
                        val entity = habitDao.getById(item.entityId)
                        val remoteId = entity?.remoteId ?: item.entityId
                        runCatching { habitApi.archive(bearer, remoteId) }
                        queue.remove(item.id)
                    }
                    "habit" to "delete" -> {
                        runCatching { habitApi.delete(bearer, item.entityId) }
                        queue.remove(item.id)
                    }
                    "habit_log" to "complete" -> {
                        val log = logDao.getForHabitAndDate(
                            // entityId is log id — find via getPending or parse
                            item.payloadJson.substringBefore(":").takeIf { false } ?: "",
                            "",
                        )
                        // Load by scanning pending logs
                        val pendingLogs = logDao.getPendingSync()
                        val match = pendingLogs.find { it.id == item.entityId }
                        if (match == null) {
                            queue.remove(item.id)
                            continue
                        }
                        val req = CreateHabitLogRequest(
                            habitId = match.habitId,
                            occurrenceDate = match.occurrenceDate,
                            status = match.status.lowercase(),
                            value = match.value,
                            note = match.note,
                            idempotencyKey = match.idempotencyKey,
                        )
                        // Prefer remote habit id if available
                        val habit = habitDao.getById(match.habitId)
                        val remoteHabitId = habit?.remoteId ?: match.habitId
                        val res = habitApi.createLog(
                            bearer,
                            req.copy(habitId = remoteHabitId),
                        )
                        if (res.success) {
                            logDao.upsert(match.copy(pendingSync = false, remoteId = res.data?.id))
                            queue.remove(item.id)
                        } else {
                            queue.markFailed(item.id, res.error?.message ?: "log sync failed")
                            failures++
                        }
                    }
                    "habit_log" to "undo" -> {
                        queue.remove(item.id)
                    }
                    else -> {
                        Log.w(TAG, "Unknown queue item ${item.entityType}/${item.operation}")
                        queue.remove(item.id)
                    }
                }
            } catch (e: Exception) {
                Log.e(TAG, "Item ${item.id} failed: ${e.message}")
                queue.markFailed(item.id, e.message ?: "error")
                failures++
                // Try refresh once on auth errors
                if (e.message?.contains("401") == true || e.message?.contains("Unauthorized") == true) {
                    authRepo.refreshIfNeeded()
                    bearer = sessionStore.getBearer() ?: bearer
                }
            }
        }

        return if (failures > 0) Result.retry() else Result.success()
    }

    companion object {
        private const val TAG = "EveriseSync"
        private const val UNIQUE = "everise_sync"

        fun enqueue(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
            val req = OneTimeWorkRequestBuilder<SyncWorker>()
                .setConstraints(constraints)
                .build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE,
                ExistingWorkPolicy.REPLACE,
                req,
            )
        }
    }
}
