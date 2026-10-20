package com.example.everise.ui.screens.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.everise.core.common.isOnline
import com.example.everise.core.sync.SyncWorker
import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val app: Application,
    private val habitRepository: HabitRepository,
) : AndroidViewModel(app) {

    val habits: StateFlow<List<HabitWithTodayStatus>> = habitRepository
        .observeTodayHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val progress: StateFlow<DailyProgress> = habitRepository
        .observeDailyProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DailyProgress(0, 0))

    private val _message = MutableStateFlow<String?>(null)
    val message: StateFlow<String?> = _message.asStateFlow()

    fun clearMessage() {
        _message.value = null
    }

    fun toggleComplete(habitId: String, currentlyCompleted: Boolean) {
        viewModelScope.launch {
            // Always save locally first (offline-first)
            if (currentlyCompleted) {
                habitRepository.undoToday(habitId)
            } else {
                habitRepository.setTodayStatus(habitId, LogStatus.COMPLETED)
            }
            if (isOnline(app)) {
                SyncWorker.enqueue(app)
                _message.value = "Saved — syncing to cloud…"
            } else {
                _message.value = "Saved offline. Connect to internet to upload."
            }
        }
    }

    fun skip(habitId: String) {
        viewModelScope.launch {
            habitRepository.setTodayStatus(habitId, LogStatus.SKIPPED)
            if (isOnline(app)) SyncWorker.enqueue(app)
            else _message.value = "Saved offline. Connect to internet to upload."
        }
    }

    fun syncNow() {
        viewModelScope.launch {
            if (!isOnline(app)) {
                _message.value = "Turn on internet to upload your habits."
                return@launch
            }
            SyncWorker.enqueue(app)
            _message.value = "Uploading pending changes…"
        }
    }

    class Factory(
        private val app: Application,
        private val repo: HabitRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(app, repo) as T
    }
}
