package com.example.everise.ui.screens.habit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.everise.core.common.todayIsoDate
import com.example.everise.core.notifications.ReminderScheduler
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.ScheduleType
import com.example.everise.domain.model.TargetType
import com.example.everise.domain.repository.HabitRepository
import kotlinx.coroutines.launch
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateHabitScreen(
    habitRepository: HabitRepository,
    onDone: () -> Unit,
    onBack: () -> Unit,
    editHabitId: String? = null,
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("personal_development") }
    var intention by remember { mutableStateOf("") }
    var reminderEnabled by remember { mutableStateOf(false) }
    var reminderHour by remember { mutableStateOf(9) }
    var reminderMinute by remember { mutableStateOf(0) }
    var showTimePicker by remember { mutableStateOf(false) }
    var existing by remember { mutableStateOf<Habit?>(null) }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isEdit = editHabitId != null
    val reminderTimeLabel = "%02d:%02d".format(reminderHour, reminderMinute)

    LaunchedEffect(editHabitId) {
        if (editHabitId != null) {
            val h = habitRepository.getHabit(editHabitId)
            if (h != null) {
                existing = h
                name = h.name
                description = h.description
                category = h.category
                intention = h.implementationIntention
                reminderEnabled = h.reminderEnabled
                val parts = (h.reminderTimes.firstOrNull() ?: "09:00").split(":")
                reminderHour = parts.getOrNull(0)?.toIntOrNull() ?: 9
                reminderMinute = parts.getOrNull(1)?.toIntOrNull() ?: 0
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (isEdit) "Edit habit" else "New habit") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back")
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
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(Modifier.height(12.dp))
            OutlinedTextField(
                value = intention,
                onValueChange = { intention = it },
                label = { Text("Implementation intention") },
                placeholder = { Text("After breakfast, I will…") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Daily reminder", modifier = Modifier.weight(1f), style = MaterialTheme.typography.titleSmall)
                Switch(checked = reminderEnabled, onCheckedChange = { reminderEnabled = it })
            }
            if (reminderEnabled) {
                Spacer(Modifier.height(8.dp))
                OutlinedButton(
                    onClick = { showTimePicker = true },
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Icon(Icons.Outlined.Schedule, contentDescription = null)
                    Spacer(Modifier.padding(horizontal = 6.dp))
                    Text("Remind at $reminderTimeLabel")
                }
                Text(
                    "Notification will fire at this time every day (device local time).",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 6.dp),
                )
            }
            Spacer(Modifier.height(28.dp))
            Button(
                onClick = {
                    if (name.isBlank() || saving) return@Button
                    saving = true
                    scope.launch {
                        val base = existing
                        val habit = (base ?: Habit(
                            id = UUID.randomUUID().toString(),
                            name = name.trim(),
                            startDate = todayIsoDate(),
                        )).copy(
                            name = name.trim(),
                            description = description.trim(),
                            category = category.trim().ifBlank { "personal_development" },
                            implementationIntention = intention.trim(),
                            reminderEnabled = reminderEnabled,
                            reminderTimes = if (reminderEnabled) listOf(reminderTimeLabel) else emptyList(),
                            scheduleType = ScheduleType.DAILY,
                            targetType = TargetType.CHECKBOX,
                            updatedAt = System.currentTimeMillis(),
                        )
                        if (isEdit) {
                            habitRepository.updateHabit(habit)
                        } else {
                            habitRepository.createHabit(habit)
                        }
                        ReminderScheduler.scheduleHabit(context, habit)
                        saving = false
                        onDone()
                    }
                },
                enabled = name.isNotBlank() && !saving,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(
                    if (saving) "Saving…" else if (isEdit) "Save changes" else "Save habit",
                    style = MaterialTheme.typography.labelLarge,
                )
            }
            Spacer(Modifier.height(32.dp))
        }
    }

    if (showTimePicker) {
        val timeState = rememberTimePickerState(
            initialHour = reminderHour,
            initialMinute = reminderMinute,
            is24Hour = true,
        )
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            title = { Text("Reminder time") },
            text = {
                TimePicker(state = timeState)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        reminderHour = timeState.hour
                        reminderMinute = timeState.minute
                        showTimePicker = false
                    },
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}
