package com.example.everise.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val habitRepository: HabitRepository,
) : ViewModel() {

    val habits: StateFlow<List<HabitWithTodayStatus>> = habitRepository
        .observeTodayHabits()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val progress: StateFlow<DailyProgress> = habitRepository
        .observeDailyProgress()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DailyProgress(0, 0))

    fun toggleComplete(habitId: String, currentlyCompleted: Boolean) {
        viewModelScope.launch {
            if (currentlyCompleted) {
                habitRepository.undoToday(habitId)
            } else {
                habitRepository.setTodayStatus(habitId, LogStatus.COMPLETED)
            }
        }
    }

    fun skip(habitId: String) {
        viewModelScope.launch {
            habitRepository.setTodayStatus(habitId, LogStatus.SKIPPED)
        }
    }

    class Factory(private val repo: HabitRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T =
            HomeViewModel(repo) as T
    }
}
