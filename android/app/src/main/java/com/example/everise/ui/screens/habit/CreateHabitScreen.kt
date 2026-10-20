package com.example.everise.ui.screens.habit

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.everise.core.common.todayIsoDate
import com.example.everise.core.notifications.ReminderScheduler
import com.example.everise.domain.model.Habit
import com.example.everise.domain.model.ScheduleType
import com.example.everise.domain.model.TargetType
import com.example.everise.domain.repository.HabitRepository
import com.example.everise.ui.theme.*
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
    var name by remember { mutableStateOf("Evening Reflection Journal") }
    var description by remember { mutableStateOf("Short reflection on daily wins and tomorrow's focus") }
    var selectedCategory by remember { mutableStateOf("Mindfulness") }
    var scheduleMode by remember { mutableStateOf("Weekdays") }
    var activeDays by remember { mutableStateOf(setOf(1, 2, 3, 4, 5, 6)) } // Mon-Sat
    var targetType by remember { mutableStateOf("Duration") }
    var targetValue by remember { mutableStateOf(10) }
    var targetUnit by remember { mutableStateOf("Minutes") }
    var anchorAfter by remember { mutableStateOf("finish dinner & close my laptop") }
    var anchorWill by remember { mutableStateOf("journal mindfully for 10 minutes") }
    var reminderEnabled by remember { mutableStateOf(true) }
    var reminderTime by remember { mutableStateOf("09:30 PM") }

    var existing by remember { mutableStateOf<Habit?>(null) }
    var saving by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val isEdit = editHabitId != null

    LaunchedEffect(editHabitId) {
        if (editHabitId != null) {
            val h = habitRepository.getHabit(editHabitId)
            if (h != null) {
                existing = h
                name = h.name
                description = h.description
                selectedCategory = h.category
                reminderEnabled = h.reminderEnabled
                targetValue = h.targetValue
                targetUnit = h.targetUnit
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Outlined.ArrowBack, contentDescription = "Back", tint = EverisePrimaryTextLight)
                        }
                        Surface(
                            shape = CircleShape,
                            color = EverisePaleBlueSurface,
                            modifier = Modifier.size(28.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Outlined.TrendingUp, contentDescription = null, tint = EveriseDeepBlue, modifier = Modifier.size(16.dp))
                            }
                        }
                        Text(
                            if (isEdit) "Edit Habit" else "Create Habit",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = EverisePrimaryTextLight,
                        )
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 16.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(EverisePaleBlueSurface),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = EveriseDeepBlue,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = EveriseBackgroundLight),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(EveriseBackgroundLight)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            // Header Intro: Leaf icon + Craft Your Daily Flow
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(EverisePaleSageSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        Icons.Outlined.Spa,
                        contentDescription = null,
                        tint = EveriseSageGreen,
                        modifier = Modifier.size(22.dp),
                    )
                }
                Column {
                    Text(
                        "Craft Your Daily Flow",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = EverisePrimaryTextLight,
                    )
                    Text(
                        "Make it small. Make it repeatable.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EveriseSecondaryTextLight,
                    )
                }
            }

            // 1. Identity & Focus Card with "Core" badge
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.EditNote, contentDescription = null, tint = EveriseDeepBlue)
                            Text("Identity & Focus", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EverisePrimaryTextLight)
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EverisePaleBlueSurface,
                        ) {
                            Text("Core", modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = EveriseDeepBlue)
                        }
                    }

                    // Habit Name
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Habit Name *", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = EverisePrimaryTextLight)
                            Text("Max 40", style = MaterialTheme.typography.bodySmall, color = EveriseSecondaryTextLight)
                        }
                        OutlinedTextField(
                            value = name,
                            onValueChange = { if (it.length <= 40) name = it },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = EverisePaleBlueSurface.copy(alpha = 0.5f),
                                unfocusedContainerColor = EverisePaleBlueSurface.copy(alpha = 0.5f),
                                focusedBorderColor = EveriseBrandBlue,
                                unfocusedBorderColor = Color.Transparent,
                            ),
                        )
                    }

                    // Description
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Intention & Context (Optional)", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = EverisePrimaryTextLight)
                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            maxLines = 2,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = EverisePaleBlueSurface.copy(alpha = 0.5f),
                                unfocusedContainerColor = EverisePaleBlueSurface.copy(alpha = 0.5f),
                                focusedBorderColor = EveriseBrandBlue,
                                unfocusedBorderColor = Color.Transparent,
                            ),
                        )
                    }

                    // Life Domain
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Life Domain", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = EverisePrimaryTextLight)
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .horizontalScroll(rememberScrollState()),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            listOf(
                                "Health" to Icons.Outlined.FavoriteBorder,
                                "Learning" to Icons.Outlined.MenuBook,
                                "Fitness" to Icons.Outlined.FitnessCenter,
                                "Mindfulness" to Icons.Outlined.SelfImprovement,
                                "Productivity" to Icons.Outlined.Psychology,
                            ).forEach { (cat, icon) ->
                                val selected = selectedCategory == cat
                                Surface(
                                    onClick = { selectedCategory = cat },
                                    shape = RoundedCornerShape(20.dp),
                                    color = if (selected) EveriseDeepBlue else EverisePaleBlueSurface,
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    ) {
                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            tint = if (selected) Color.White else EveriseDeepBlue,
                                            modifier = Modifier.size(16.dp),
                                        )
                                        Text(
                                            cat,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selected) Color.White else EverisePrimaryTextLight,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 2. Schedule & Cadence Card with "6 days / wk" badge
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.CalendarToday, contentDescription = null, tint = EveriseDeepBlue)
                            Text("Schedule & Cadence", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EverisePrimaryTextLight)
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EverisePaleSageSurface,
                        ) {
                            Text("6 days / wk", modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF285C38))
                        }
                    }

                    // Segmented control: Daily, Weekdays, Target, Custom
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf("Daily", "Weekdays", "Target", "Custom").forEach { mode ->
                                val selected = scheduleMode == mode
                                Surface(
                                    onClick = { scheduleMode = mode },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) EveriseWhiteSurface else Color.Transparent,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Box(modifier = Modifier.padding(vertical = 7.dp), contentAlignment = Alignment.Center) {
                                        Text(
                                            mode,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selected) EverisePrimaryTextLight else EveriseSecondaryTextLight,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Active Routine Days
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Active Routine Days", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold, color = EverisePrimaryTextLight)
                            Text("Gentle rest on Sun", style = MaterialTheme.typography.bodySmall, color = EveriseDeepBlue)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            listOf("M" to 1, "T" to 2, "W" to 3, "T" to 4, "F" to 5, "S" to 6, "S" to 7).forEach { (label, dayNum) ->
                                val active = activeDays.contains(dayNum)
                                Surface(
                                    modifier = Modifier
                                        .size(38.dp)
                                        .clickable {
                                            activeDays = if (active) activeDays - dayNum else activeDays + dayNum
                                        },
                                    shape = CircleShape,
                                    color = if (active) Color(0xFF3B6B4C) else EverisePaleBlueSurface,
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            label,
                                            style = MaterialTheme.typography.labelMedium,
                                            color = if (active) Color.White else EverisePrimaryTextLight,
                                            fontWeight = FontWeight.Bold,
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // 3. Measurable Target Card with "Precision" badge
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Flag, contentDescription = null, tint = EveriseDeepBlue)
                            Text("Measurable Target", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EverisePrimaryTextLight)
                        }
                        Text("Precision", style = MaterialTheme.typography.labelSmall, color = EveriseSecondaryTextLight)
                    }

                    // Switcher: Check, Duration, Count
                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(modifier = Modifier.padding(3.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                            listOf(
                                "Check" to Icons.Outlined.Check,
                                "Duration" to Icons.Outlined.Timer,
                                "Count" to Icons.Outlined.Tag,
                            ).forEach { (type, icon) ->
                                val selected = targetType == type
                                Surface(
                                    onClick = { targetType = type },
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) EveriseWhiteSurface else Color.Transparent,
                                    modifier = Modifier.weight(1f),
                                ) {
                                    Row(
                                        modifier = Modifier.padding(vertical = 7.dp),
                                        horizontalArrangement = Arrangement.Center,
                                        verticalAlignment = Alignment.CenterVertically,
                                    ) {
                                        Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = if (selected) EveriseDeepBlue else EveriseSecondaryTextLight)
                                        Spacer(Modifier.width(4.dp))
                                        Text(
                                            type,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (selected) EverisePrimaryTextLight else EveriseSecondaryTextLight,
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Stepper row
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Column {
                                Text("Daily Session", style = MaterialTheme.typography.bodySmall, color = EveriseSecondaryTextLight)
                                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        targetValue.toString(),
                                        style = MaterialTheme.typography.headlineLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = EveriseDeepBlue,
                                    )
                                    Text(
                                        targetUnit,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = EverisePrimaryTextLight,
                                    )
                                }
                            }
                            Row(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(EveriseWhiteSurface)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = { if (targetValue > 1) targetValue-- }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Outlined.Remove, contentDescription = "Decrease", tint = EverisePrimaryTextLight)
                                }
                                Box(modifier = Modifier.width(1.dp).height(18.dp).background(EveriseBorderLight))
                                IconButton(onClick = { targetValue++ }, modifier = Modifier.size(32.dp)) {
                                    Icon(Icons.Outlined.Add, contentDescription = "Increase", tint = EveriseDeepBlue)
                                }
                            }
                        }
                    }
                }
            }

            // 4. Habit Stacking Anchor Card (Warm Cream Surface)
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWarmCreamSurface,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Hub, contentDescription = null, tint = Color(0xFF6B4E1F))
                            Text("Habit Stacking Anchor", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = Color(0xFF382911))
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = Color(0xFFEADFC4),
                        ) {
                            Text("Momentum", modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp), style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold, color = Color(0xFF6B4E1F))
                        }
                    }
                    Text(
                        "Tie this new action to a concrete anchor cue already cemented in your day:",
                        style = MaterialTheme.typography.bodySmall,
                        color = Color(0xFF6B4E1F),
                    )

                    Surface(
                        shape = RoundedCornerShape(12.dp),
                        color = EveriseWhiteSurface,
                    ) {
                        Column(
                            modifier = Modifier.padding(14.dp),
                            verticalArrangement = Arrangement.spacedBy(10.dp),
                        ) {
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("AFTER", style = MaterialTheme.typography.labelSmall, color = EveriseDeepBlue, fontWeight = FontWeight.Bold)
                                OutlinedTextField(
                                    value = anchorAfter,
                                    onValueChange = { anchorAfter = it },
                                    modifier = Modifier.weight(1f),
                                    singleLine = true,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EveriseBrandBlue,
                                        unfocusedBorderColor = EveriseBorderLight,
                                    ),
                                )
                            }
                            HorizontalDivider(color = EveriseBorderLight)
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("I WILL", style = MaterialTheme.typography.labelSmall, color = Color(0xFF3B6B4C), fontWeight = FontWeight.Bold)
                                Text(
                                    anchorWill.ifBlank { name },
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = EverisePrimaryTextLight,
                                )
                            }
                        }
                    }
                }
            }

            // 5. Calm Notification Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Outlined.Notifications, contentDescription = null, tint = EveriseDeepBlue)
                            Text("Calm Notification", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EverisePrimaryTextLight)
                        }
                        Switch(
                            checked = reminderEnabled,
                            onCheckedChange = { reminderEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = EveriseBrandBlue,
                            ),
                        )
                    }

                    if (reminderEnabled) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 14.dp, vertical = 10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(Icons.Outlined.Schedule, contentDescription = null, tint = EveriseDeepBlue, modifier = Modifier.size(20.dp))
                                    Column {
                                        Text("Scheduled Time", style = MaterialTheme.typography.bodySmall, color = EveriseSecondaryTextLight)
                                        Text(reminderTime, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, color = EverisePrimaryTextLight)
                                    }
                                }
                                Surface(
                                    onClick = { /* Adjust time dialog */ },
                                    shape = RoundedCornerShape(10.dp),
                                    color = EveriseWhiteSurface,
                                ) {
                                    Text(
                                        "Adjust ▾",
                                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = EveriseDeepBlue,
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(Modifier.height(4.dp))

            // Save Habit Routine Primary Button
            Button(
                onClick = {
                    if (name.isBlank()) {
                        Toast.makeText(context, "Please enter a habit name", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    saving = true
                    scope.launch {
                        val habitId = existing?.id ?: UUID.randomUUID().toString()
                        val newHabit = Habit(
                            id = habitId,
                            name = name,
                            description = description,
                            category = selectedCategory.lowercase(),
                            scheduleType = ScheduleType.DAILY,
                            targetType = when (targetType) {
                                "Duration" -> TargetType.DURATION
                                "Count" -> TargetType.QUANTITY
                                else -> TargetType.CHECKBOX
                            },
                            targetValue = targetValue,
                            targetUnit = targetUnit,
                            reminderEnabled = reminderEnabled,
                            reminderTimes = listOf(reminderTime),
                            startDate = existing?.startDate ?: todayIsoDate(),
                            implementationIntention = "$anchorAfter -> $anchorWill",
                        )
                        if (isEdit) {
                            habitRepository.updateHabit(newHabit)
                        } else {
                            habitRepository.createHabit(newHabit)
                        }
                        if (reminderEnabled) {
                            ReminderScheduler.scheduleHabit(context, newHabit)
                        }
                        saving = false
                        Toast.makeText(context, "Habit saved successfully", Toast.LENGTH_SHORT).show()
                        onDone()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = EveriseBrandBlue),
            ) {
                if (saving) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White)
                } else {
                    Icon(Icons.Outlined.CheckCircle, contentDescription = null, tint = Color.White)
                    Spacer(Modifier.width(8.dp))
                    Text(
                        if (isEdit) "Update Habit Routine" else "Save Habit Routine",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )
                }
            }

            // Save as Private Draft Text Button
            TextButton(
                onClick = {
                    Toast.makeText(context, "Draft saved locally", Toast.LENGTH_SHORT).show()
                    onDone()
                },
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    "Save as Private Draft",
                    style = MaterialTheme.typography.bodyMedium,
                    color = EveriseSecondaryTextLight,
                )
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}
