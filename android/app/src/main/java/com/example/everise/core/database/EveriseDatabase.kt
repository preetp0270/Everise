package com.example.everise.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.everise.data.local.dao.HabitDao
import com.example.everise.data.local.dao.HabitLogDao
import com.example.everise.data.local.dao.SyncQueueDao
import com.example.everise.data.local.entity.HabitEntity
import com.example.everise.data.local.entity.HabitLogEntity
import com.example.everise.data.local.entity.SyncQueueEntity

@Database(
    entities = [HabitEntity::class, HabitLogEntity::class, SyncQueueEntity::class],
    version = 1,
    exportSchema = false,
)
abstract class EveriseDatabase : RoomDatabase() {
    abstract fun habitDao(): HabitDao
    abstract fun habitLogDao(): HabitLogDao
    abstract fun syncQueueDao(): SyncQueueDao

    companion object {
        @Volatile private var instance: EveriseDatabase? = null

        fun get(context: Context): EveriseDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    EveriseDatabase::class.java,
                    "everise.db",
                ).fallbackToDestructiveMigration().build().also { instance = it }
            }
    }
}
