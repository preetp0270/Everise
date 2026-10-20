package com.example.everise.ui.screens.habit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.HabitLog
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.model.StreakInfo
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HabitDetailViewModel(
    private val habitId: String,
    private val habitRepository: HabitRepository,
) : ViewModel() {

    val habit: StateFlow<Habit?> = habitRepository
        .observeHabit(habitId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    val logs: StateFlow<List<HabitLog>> = habitRepository
        .observeLogs(habitId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val streakInfo: StateFlow<StreakInfo> = habitRepository
        .observeHabitStreak(habitId)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), StreakInfo())

    fun setTodayStatus(status: LogStatus, note: String = "") {
        viewModelScope.launch {
            habitRepository.setTodayStatus(habitId, status, note = note)
        }
    }

    fun undoToday() {
        viewModelScope.launch {
            habitRepository.undoToday(habitId)
        }
    }

    fun archiveHabit(onDone: () -> Unit) {
        viewModelScope.launch {
            habitRepository.archiveHabit(habitId)
            onDone()
        }
    }

    fun deleteHabit(onDone: () -> Unit) {
        viewModelScope.launch {
            habitRepository.deleteHabit(habitId)
            onDone()
        }
    }

    class Factory(
        private val habitId: String,
        private val repo: HabitRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HabitDetailViewModel(habitId, repo) as T
    }
}
