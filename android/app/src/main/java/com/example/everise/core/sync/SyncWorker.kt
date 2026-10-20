package com.example.everise.core.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.everise.app.EveriseApp

/**
 * Processes the local sync queue when connectivity is available.
 */
class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val app = applicationContext as? EveriseApp ?: return Result.failure()
        val queue = app.container.syncQueueDao
        val batch = queue.nextBatch(20)
        if (batch.isEmpty()) return Result.success()

        // Phase 6: push each item via HabitApi using session tokens.
        // For now mark processed so queue does not grow unbounded in offline-only use.
        Log.i("EveriseSync", "Processing ${batch.size} sync queue items")
        batch.forEach { item ->
            try {
                // Real API push would go here
                queue.remove(item.id)
            } catch (e: Exception) {
                queue.markFailed(item.id, e.message ?: "error")
            }
        }
        return Result.success()
    }

    companion object {
        private const val UNIQUE = "everise_sync"

        fun enqueue(context: Context) {
            val req = OneTimeWorkRequestBuilder<SyncWorker>().build()
            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE,
                ExistingWorkPolicy.KEEP,
                req,
            )
        }
    }
}
