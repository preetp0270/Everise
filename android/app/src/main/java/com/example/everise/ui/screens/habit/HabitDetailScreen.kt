package com.example.everise.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.core.notifications.ReminderScheduler
import com.example.everise.domain.model.LogStatus
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HabitDetailScreen(
    habitId: String,
    habitRepository: HabitRepository,
    onEdit: (String) -> Unit,
    onDeleted: () -> Unit,
    onBack: () -> Unit,
) {
    val habit by habitRepository.observeHabit(habitId).collectAsStateWithLifecycle(initialValue = null)
    val logs by habitRepository.observeLogs(habitId).collectAsStateWithLifecycle(initialValue = emptyList())
    var showDelete by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(habit?.name ?: "Habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { onEdit(habitId) }) {
                        Icon(Icons.Outlined.Edit, contentDescription = "Edit")
                    }
                    IconButton(onClick = { showDelete = true }) {
                        Icon(Icons.Outlined.Delete, contentDescription = "Delete")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            val h = habit
            if (h == null) {
                Text("Loading…", color = MaterialTheme.colorScheme.onSurfaceVariant)
                return@Column
            }

            Text("Category", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(h.category.replace('_', ' '), style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(12.dp))

            if (h.description.isNotBlank()) {
                Text("Description", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(h.description, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(12.dp))
            }

            if (h.implementationIntention.isNotBlank()) {
                Text("Implementation intention", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Text(h.implementationIntention, style = MaterialTheme.typography.bodyLarge)
                Spacer(Modifier.height(12.dp))
            }

            Text("Schedule", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(h.scheduleType.name.lowercase().replaceFirstChar { it.uppercase() }, style = MaterialTheme.typography.bodyLarge)
            Spacer(Modifier.height(12.dp))

            Text(
                "Reminder: " + if (h.reminderEnabled) {
                    h.reminderTimes.joinToString().ifBlank { "On" }
                } else {
                    "Off"
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(12.dp))

            val completed = logs.count { it.status == LogStatus.COMPLETED }
            Text("Completions logged: $completed", style = MaterialTheme.typography.titleSmall)
            Spacer(Modifier.height(24.dp))

            Button(
                onClick = {
                    scope.launch {
                        habitRepository.setTodayStatus(habitId, LogStatus.COMPLETED)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text("Mark complete today")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    scope.launch { habitRepository.undoToday(habitId) }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text("Undo today")
            }
            Spacer(Modifier.height(12.dp))
            OutlinedButton(
                onClick = {
                    scope.launch {
                        habitRepository.archiveHabit(habitId)
                        ReminderScheduler.cancelHabit(context, habitId)
                        onDeleted()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(999.dp),
            ) {
                Text("Archive habit")
            }
        }
    }

    if (showDelete) {
        AlertDialog(
            onDismissRequest = { showDelete = false },
            title = { Text("Delete habit?") },
            text = { Text("This removes the habit from this device. This cannot be undone.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        showDelete = false
                        scope.launch {
                            ReminderScheduler.cancelHabit(context, habitId)
                            habitRepository.deleteHabit(habitId)
                            onDeleted()
                        }
                    },
                ) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDelete = false }) { Text("Cancel") }
            },
        )
    }
}
