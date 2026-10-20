package com.example.everise.ui.screens.insights

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.domain.repository.HabitRepository

@Composable
fun InsightsScreen(habitRepository: HabitRepository) {
    val progress by habitRepository.observeDailyProgress().collectAsStateWithLifecycle(
        initialValue = com.example.everise.domain.model.DailyProgress(0, 0),
    )
    val habits by habitRepository.observeTodayHabits().collectAsStateWithLifecycle(initialValue = emptyList())

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
    ) {
        Text(
            text = "Insights",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(16.dp))
        StatCard(
            title = "Today's completion",
            value = "${progress.percent}%",
            subtitle = "${progress.completed}/${progress.total} habits",
        )
        Spacer(modifier = Modifier.height(12.dp))
        StatCard(
            title = "Active habits",
            value = habits.size.toString(),
            subtitle = "Tracked locally — streaks expand with history",
        )
        Spacer(modifier = Modifier.height(12.dp))
        StatCard(
            title = "Consistency",
            value = if (progress.total == 0) "—" else if (progress.percent >= 80) "Strong" else "Building",
            subtitle = "Encourage consistency over perfection",
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Weekly heatmaps, category breakdowns, and personal records unlock as you log more days.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun StatCard(title: String, value: String, subtitle: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .background(MaterialTheme.colorScheme.surface)
            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
            .padding(20.dp),
    ) {
        Text(title, style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(modifier = Modifier.height(8.dp))
        Text(value, style = MaterialTheme.typography.headlineLarge, color = MaterialTheme.colorScheme.onSurface)
        Spacer(modifier = Modifier.height(4.dp))
        Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}
