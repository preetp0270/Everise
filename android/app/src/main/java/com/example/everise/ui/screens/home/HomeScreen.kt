package com.example.everise.ui.screens.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.ui.components.HabitCard
import com.example.everise.ui.theme.SuccessLight
import java.time.LocalTime

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userName: String = "there",
    onAddHabit: () -> Unit = {},
    onHabitClick: (String) -> Unit = {},
) {
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    HomeScreenContent(
        message = message,
        onDismissMessage = { viewModel.clearMessage() },
        onSyncNow = { viewModel.syncNow() },

        userName = userName,
        habits = habits,
        progress = progress,
        onToggle = { id, done -> viewModel.toggleComplete(id, done) },
        onAddHabit = onAddHabit,
        onHabitClick = onHabitClick,
    )
}

@Composable
fun HomeScreenContent(
    userName: String,
    habits: List<HabitWithTodayStatus>,
    progress: DailyProgress,
    onToggle: (String, Boolean) -> Unit,
    onAddHabit: () -> Unit,
    onHabitClick: (String) -> Unit,
    message: String? = null,
    onDismissMessage: () -> Unit = {},
    onSyncNow: () -> Unit = {},
) {
    val greeting = when (LocalTime.now().hour) {
        in 5..11 -> "Good morning"
        in 12..16 -> "Good afternoon"
        in 17..20 -> "Good evening"
        else -> "Good night"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "$greeting, $userName",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
                Text(
                    text = "Today's habits",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(24.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "${progress.percent}%",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary,
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "Daily progress",
                            style = MaterialTheme.typography.titleSmall,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Text(
                            text = "${progress.completed} of ${progress.total} habits completed",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Your habits",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                )
            }

            if (habits.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(SuccessLight.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center,
                        ) {
                            Icon(Icons.Outlined.Check, contentDescription = null, tint = SuccessLight)
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No habits yet",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Create your first habit and start compounding small wins.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            } else {
                items(habits, key = { it.habit.id }) { item ->
                    HabitCard(
                        item = item,
                        onToggle = { onToggle(item.habit.id, item.isCompletedToday) },
                        onClick = { onHabitClick(item.habit.id) },
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(88.dp)) }
        }

        if (message != null) {
            Snackbar(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 88.dp, start = 16.dp, end = 16.dp),
                action = {
                    TextButton(onClick = {
                        onSyncNow()
                        onDismissMessage()
                    }) { Text("Upload") }
                },
            ) { Text(message) }
        }

        FloatingActionButton(
            onClick = onAddHabit,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = CircleShape,
        ) {
            Icon(Icons.Outlined.Add, contentDescription = "Add habit")
        }
    }
}
