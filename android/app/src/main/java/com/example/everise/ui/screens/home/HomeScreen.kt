package com.example.everise.ui.screens.home

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.domain.model.DailyProgress
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.ui.components.HabitCard
import com.example.everise.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    userName: String,
    onAddHabit: () -> Unit,
    onHabitClick: (String) -> Unit,
) {
    val habits by viewModel.habits.collectAsStateWithLifecycle()
    val progress by viewModel.progress.collectAsStateWithLifecycle()
    val message by viewModel.message.collectAsStateWithLifecycle()

    HomeScreenContent(
        userName = userName,
        habits = habits,
        progress = progress,
        onToggle = { id, done -> viewModel.toggleComplete(id, done) },
        onAddHabit = onAddHabit,
        onHabitClick = onHabitClick,
        message = message,
        onDismissMessage = { viewModel.clearMessage() },
        onSyncNow = { viewModel.syncNow() },
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
    val today = LocalDate.now()
    val dateLabel = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM d", Locale.getDefault()))
    val hour = java.time.LocalTime.now().hour
    val greeting = when {
        hour < 12 -> "Good morning"
        hour < 17 -> "Good afternoon"
        else -> "Good evening"
    }
    val pct = if (progress.total == 0) 0 else ((progress.completed * 100f) / progress.total).toInt()

    var selectedFilter by remember { mutableStateOf("All") }

    val morningHabits = habits.filter {
        val rem = it.habit.reminderTimes.firstOrNull() ?: ""
        rem.contains("AM", ignoreCase = true) || it.habit.category.contains("morning", ignoreCase = true) || it.habit.name.contains("morning", ignoreCase = true)
    }
    val eveningHabits = habits.filter {
        val rem = it.habit.reminderTimes.firstOrNull() ?: ""
        rem.contains("PM", ignoreCase = true) || it.habit.category.contains("evening", ignoreCase = true) || it.habit.name.contains("evening", ignoreCase = true)
    }

    val displayedHabits = when (selectedFilter) {
        "Morning" -> if (morningHabits.isNotEmpty()) morningHabits else habits.take(habits.size / 2 + 1)
        "Evening" -> if (eveningHabits.isNotEmpty()) eveningHabits else habits.takeLast(habits.size / 2)
        else -> habits
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(EveriseBackgroundLight),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // Header: Date, Contextual Greeting, Theme/Sun button
            item {
                Spacer(Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top,
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = dateLabel,
                            style = MaterialTheme.typography.labelMedium,
                            color = EveriseSecondaryTextLight,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "$greeting, ${userName.ifBlank { "Elena" }}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = EverisePrimaryTextLight,
                        )
                    }
                    Surface(
                        shape = CircleShape,
                        color = EverisePaleBlueSurface,
                        modifier = Modifier.size(42.dp),
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            Icon(
                                Icons.Outlined.WbSunny,
                                contentDescription = "Theme",
                                tint = EveriseBrandBlue,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                }

                Spacer(Modifier.height(12.dp))

                // Motivational banner chip
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = EveriseWarmCreamSurface,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.Spa,
                            contentDescription = null,
                            tint = Color(0xFF8C6D3B),
                            modifier = Modifier.size(18.dp),
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Small steps still move you forward.",
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF5C4724),
                        )
                    }
                }
            }

            // Daily Progress Card with Donut & Weekday Row
            item {
                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = EveriseWhiteSurface,
                    shadowElevation = 1.dp,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            ProgressRing(percent = pct, modifier = Modifier.size(62.dp))
                            Spacer(Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${progress.completed} of ${progress.total} completed",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = EverisePrimaryTextLight,
                                    )
                                    if (progress.completed > 0) {
                                        Spacer(Modifier.width(6.dp))
                                        Icon(
                                            Icons.Outlined.TrendingUp,
                                            contentDescription = null,
                                            tint = EveriseSageGreen,
                                            modifier = Modifier.size(18.dp),
                                        )
                                    }
                                }
                                Spacer(Modifier.height(2.dp))
                                Text(
                                    text = if (progress.total == 0) "Add a habit to begin."
                                    else if (pct >= 100) "Beautiful consistency today."
                                    else "You're building momentum.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EveriseSecondaryTextLight,
                                )
                            }
                            // Day badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = EverisePaleBlueSurface,
                            ) {
                                Text(
                                    text = "Day 18",
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = EveriseDeepBlue,
                                )
                            }
                        }

                        Spacer(Modifier.height(18.dp))
                        WeekDotsRow(todayCompleted = progress.completed)
                    }
                }
            }

            // Section Header & Filters
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Today's habits",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = EverisePrimaryTextLight,
                        modifier = Modifier.weight(1f),
                    )
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EverisePaleBlueSurface,
                    ) {
                        Text(
                            text = "${progress.completed}/${progress.total}",
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EveriseDeepBlue,
                        )
                    }
                    Spacer(Modifier.width(8.dp))
                    Icon(
                        Icons.Outlined.FilterList,
                        contentDescription = "Filter",
                        tint = EveriseSecondaryTextLight,
                        modifier = Modifier.size(20.dp),
                    )
                }

                Spacer(Modifier.height(10.dp))

                // Filter Chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChipPill(
                        text = "All (${habits.size})",
                        selected = selectedFilter == "All",
                        onClick = { selectedFilter = "All" },
                    )
                    FilterChipPill(
                        text = "Morning (${if (morningHabits.isNotEmpty()) morningHabits.size else 3})",
                        selected = selectedFilter == "Morning",
                        onClick = { selectedFilter = "Morning" },
                    )
                    FilterChipPill(
                        text = "Evening (${if (eveningHabits.isNotEmpty()) eveningHabits.size else 3})",
                        selected = selectedFilter == "Evening",
                        onClick = { selectedFilter = "Evening" },
                    )
                }
            }

            // Habits List or Empty State
            if (displayedHabits.isEmpty()) {
                item {
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = EveriseWhiteSurface,
                        shadowElevation = 1.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(onClick = onAddHabit),
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            Icon(
                                Icons.Outlined.Spa,
                                contentDescription = null,
                                tint = EveriseBrandBlue,
                                modifier = Modifier.size(36.dp),
                            )
                            Spacer(Modifier.height(10.dp))
                            Text(
                                "No habits scheduled yet",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EverisePrimaryTextLight,
                            )
                            Text(
                                "Create your first small routine to build momentum.",
                                style = MaterialTheme.typography.bodySmall,
                                color = EveriseSecondaryTextLight,
                            )
                            Spacer(Modifier.height(14.dp))
                            Text(
                                "Tap + to add",
                                color = EveriseBrandBlue,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.SemiBold,
                            )
                        }
                    }
                }
            } else {
                items(displayedHabits, key = { it.habit.id }) { item ->
                    HabitCard(
                        item = item,
                        onToggle = { onToggle(item.habit.id, item.isCompletedToday) },
                        onClick = { onHabitClick(item.habit.id) },
                    )
                }
            }

            // Habit Stacking Principle Card
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = EveriseWarmCreamSurface,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color(0xFFEADFC4)),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Outlined.Lightbulb,
                                    contentDescription = null,
                                    tint = Color(0xFF6B4E1F),
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                            Text(
                                text = "HABIT STACKING PRINCIPLE",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF8C6D3B),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                        Spacer(Modifier.height(10.dp))
                        Text(
                            text = "“After I close my evening journal, I will set out tomorrow's book.”",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF382911),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Anchor new routines to existing sensory cues.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF8C6D3B),
                        )
                    }
                }
                Spacer(Modifier.height(84.dp))
            }
        }

        // Offline / Sync banner if needed
        if (message != null) {
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp, start = 16.dp, end = 16.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = EverisePrimaryTextLight,
                shadowElevation = 4.dp,
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = message,
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.weight(1f),
                    )
                    TextButton(onClick = {
                        onSyncNow()
                        onDismissMessage()
                    }) {
                        Text("Upload", color = EveriseBrandBlue, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun FilterChipPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        color = if (selected) EverisePrimaryTextLight else EverisePaleBlueSurface.copy(alpha = 0.8f),
    ) {
        Text(
            text = text,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) Color.White else EveriseSecondaryTextLight,
        )
    }
}

@Composable
private fun ProgressRing(percent: Int, modifier: Modifier = Modifier) {
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val stroke = 6.dp.toPx()
            val diameter = size.minDimension - stroke
            val topLeft = Offset((size.width - diameter) / 2f, (size.height - diameter) / 2f)
            // Background track
            drawArc(
                color = EverisePaleBlueSurface,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
            // Progress arc
            drawArc(
                color = EveriseBrandBlue,
                startAngle = -90f,
                sweepAngle = 360f * (percent.coerceIn(0, 100) / 100f),
                useCenter = false,
                topLeft = topLeft,
                size = Size(diameter, diameter),
                style = Stroke(width = stroke, cap = StrokeCap.Round),
            )
        }
        Text(
            text = "$percent%",
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = EverisePrimaryTextLight,
        )
    }
}

@Composable
private fun WeekDotsRow(todayCompleted: Int = 4) {
    val days = listOf("M", "T", "W", "T", "F", "S", "S")
    val todayIndex = (LocalDate.now().dayOfWeek.value - 1).coerceIn(0, 6)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        days.forEachIndexed { index, label ->
            val isToday = index == todayIndex
            val isPast = index < todayIndex

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelSmall,
                    color = EveriseSecondaryTextLight,
                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Normal,
                )
                Surface(
                    shape = CircleShape,
                    color = when {
                        isToday -> EveriseDeepBlue
                        isPast -> Color(0xFF3B6B4C)
                        else -> EverisePaleBlueSurface
                    },
                    modifier = Modifier.size(32.dp),
                ) {
                    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                        when {
                            isPast -> {
                                Icon(
                                    Icons.Outlined.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(15.dp),
                                )
                            }
                            isToday -> {
                                Text(
                                    text = todayCompleted.toString(),
                                    style = MaterialTheme.typography.labelSmall,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold,
                                )
                            }
                            else -> {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(EveriseBrandBlue.copy(alpha = 0.4f)),
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
