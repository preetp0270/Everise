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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.everise.core.common.todayIsoDate
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
) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("personal_development") }
    var intention by remember { mutableStateOf("") }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("New habit") },
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
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Habit name") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Description (optional)") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = category,
                onValueChange = { category = it },
                label = { Text("Category") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(modifier = Modifier.height(12.dp))
            OutlinedTextField(
                value = intention,
                onValueChange = { intention = it },
                label = { Text("Implementation intention") },
                placeholder = { Text("After breakfast, I will…") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
            )
            Spacer(modifier = Modifier.height(28.dp))
            Button(
                onClick = {
                    if (name.isBlank() || saving) return@Button
                    saving = true
                    scope.launch {
                        val habit = Habit(
                            id = UUID.randomUUID().toString(),
                            name = name.trim(),
                            description = description.trim(),
                            category = category.trim().ifBlank { "personal_development" },
                            scheduleType = ScheduleType.DAILY,
                            targetType = TargetType.CHECKBOX,
                            startDate = todayIsoDate(),
                            implementationIntention = intention.trim(),
                        )
                        habitRepository.createHabit(habit)
                        saving = false
                        onDone()
                    }
                },
                enabled = name.isNotBlank() && !saving,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(999.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            ) {
                Text(if (saving) "Saving…" else "Save habit", style = MaterialTheme.typography.labelLarge)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
