package com.example.everise.ui.screens.calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.domain.repository.HabitRepository
import com.example.everise.ui.components.resolveHabitIcon
import com.example.everise.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(habitRepository: HabitRepository) {
    var month by remember { mutableStateOf(YearMonth.now()) }
    val todayDate = LocalDate.now()
    var selectedDate by remember { mutableStateOf(todayDate) }

    val habits by habitRepository.observeTodayHabits().collectAsStateWithLifecycle(initialValue = emptyList())

    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val monStartOffset = (firstDay.dayOfWeek.value - 1 + 7) % 7

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = EverisePaleBlueSurface,
                            modifier = Modifier.size(32.dp),
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Outlined.TrendingUp,
                                    contentDescription = null,
                                    tint = EveriseDeepBlue,
                                    modifier = Modifier.size(18.dp),
                                )
                            }
                        }
                        Text(
                            "Calendar & History",
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = EveriseBackgroundLight,
                ),
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
            // Month Navigation & Today Chip
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = EverisePrimaryTextLight,
                    )
                    Row {
                        IconButton(onClick = { month = month.minusMonths(1) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "Prev", tint = EverisePrimaryTextLight)
                        }
                        IconButton(onClick = { month = month.plusMonths(1) }, modifier = Modifier.size(32.dp)) {
                            Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "Next", tint = EverisePrimaryTextLight)
                        }
                    }
                }

                Surface(
                    onClick = {
                        month = YearMonth.now()
                        selectedDate = todayDate
                    },
                    shape = RoundedCornerShape(20.dp),
                    color = EverisePaleBlueSurface,
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.Today,
                            contentDescription = null,
                            modifier = Modifier.size(15.dp),
                            tint = EveriseDeepBlue,
                        )
                        Text(
                            "Today",
                            style = MaterialTheme.typography.labelSmall,
                            color = EveriseDeepBlue,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            Text(
                "Gentle, compounding gains without pressure",
                style = MaterialTheme.typography.bodySmall,
                color = EveriseSecondaryTextLight,
            )

            // Month Grid Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // Weekday headers: M T W T F S S
                    Row(modifier = Modifier.fillMaxWidth()) {
                        listOf("M", "T", "W", "T", "F", "S", "S").forEach { d ->
                            Text(
                                text = d,
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = EveriseSecondaryTextLight,
                            )
                        }
                    }

                    // Calendar grid
                    val cells = monStartOffset + daysInMonth
                    val rows = (cells + 6) / 7
                    var day = 1
                    repeat(rows) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            repeat(7) { col ->
                                val index = it * 7 + col
                                if (index < monStartOffset || day > daysInMonth) {
                                    Box(modifier = Modifier.weight(1f).aspectRatio(1f))
                                } else {
                                    val date = month.atDay(day)
                                    val isSelected = date == selectedDate
                                    val isToday = date == todayDate
                                    val dotState = when {
                                        day % 3 == 0 -> 1 // Complete
                                        day % 2 == 0 -> 2 // Partial
                                        else -> 3 // Rest/Breathe
                                    }

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .aspectRatio(1f)
                                            .padding(2.dp)
                                            .clickable { selectedDate = date },
                                        shape = RoundedCornerShape(12.dp),
                                        color = if (isSelected) EverisePaleBlueSurface else Color.Transparent,
                                    ) {
                                        Column(
                                            modifier = Modifier.fillMaxSize(),
                                            horizontalAlignment = Alignment.CenterHorizontally,
                                            verticalArrangement = Arrangement.Center,
                                        ) {
                                            Text(
                                                text = day.toString(),
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                                color = if (isSelected) EveriseDeepBlue else EverisePrimaryTextLight,
                                            )
                                            Spacer(Modifier.height(3.dp))
                                            Box(
                                                modifier = Modifier
                                                    .size(5.dp)
                                                    .clip(CircleShape)
                                                    .background(
                                                        when (dotState) {
                                                            1 -> EveriseDeepBlue
                                                            2 -> EveriseSageGreen
                                                            else -> EverisePaleBlueSurface
                                                        },
                                                    ),
                                            )
                                        }
                                    }
                                    day++
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(4.dp))

                    // Legend row
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            LegendDot(color = EveriseDeepBlue, label = "Complete")
                            LegendDot(color = EveriseSageGreen, label = "Partial")
                            LegendDot(color = EveriseBrandBlue.copy(alpha = 0.5f), label = "Rest / Breathe")
                        }
                    }
                }
            }

            // 12-Week Momentum Card with Heatmap
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Outlined.AutoAwesome,
                                contentDescription = null,
                                tint = EveriseDeepBlue,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                "12-Week Momentum",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EverisePrimaryTextLight,
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EverisePaleSageSurface,
                        ) {
                            Text(
                                "84% Rate",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF285C38),
                            )
                        }
                    }

                    Spacer(Modifier.height(4.dp))
                    Text(
                        "Quiet rhythm across 84 days. Every tiny block supports your identity.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EveriseSecondaryTextLight,
                    )

                    Spacer(Modifier.height(14.dp))

                    // 12 columns x 7 rows Heatmap grid
                    HeatmapGrid()

                    Spacer(Modifier.height(14.dp))

                    // Bottom Streak Counters
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                Icons.Outlined.LocalFireDepartment,
                                contentDescription = null,
                                tint = EveriseDeepBlue,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = "18 days active streak",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = EverisePrimaryTextLight,
                            )
                        }
                        Text(
                            text = "31/37 intentional days",
                            style = MaterialTheme.typography.bodySmall,
                            color = EveriseSecondaryTextLight,
                        )
                    }
                }
            }

            // Selected Day Habit Details List
            val formattedSelected = selectedDate.format(DateTimeFormatter.ofPattern("EEEE, MMM d", Locale.getDefault()))
            val isCurrentDay = selectedDate == todayDate

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
                        Column {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = formattedSelected,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = EverisePrimaryTextLight,
                                )
                                if (isCurrentDay) {
                                    Surface(
                                        shape = RoundedCornerShape(10.dp),
                                        color = EverisePaleBlueSurface,
                                    ) {
                                        Text(
                                            text = "Today",
                                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = EveriseDeepBlue,
                                        )
                                    }
                                }
                            }
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = "4 of 6 habits completed · 67% completion",
                                style = MaterialTheme.typography.bodySmall,
                                color = EveriseSecondaryTextLight,
                            )
                        }

                        // Mini Donut
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(42.dp)) {
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val stroke = 4.dp.toPx()
                                drawArc(
                                    color = EverisePaleBlueSurface,
                                    startAngle = -90f,
                                    sweepAngle = 360f,
                                    useCenter = false,
                                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                                )
                                drawArc(
                                    color = EveriseDeepBlue,
                                    startAngle = -90f,
                                    sweepAngle = 360f * 0.67f,
                                    useCenter = false,
                                    style = Stroke(width = stroke, cap = StrokeCap.Round),
                                )
                            }
                            Text(
                                "67%",
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                color = EveriseDeepBlue,
                            )
                        }
                    }

                    Divider(color = EveriseBorderLight)

                    // Scheduled / Completed habit items
                    DayHabitItem(
                        name = "Drink 8 glasses of water",
                        subtitle = "Completed at 2:15 PM",
                        category = "health",
                        isDone = true,
                    )
                    DayHabitItem(
                        name = "Read 20 minutes",
                        subtitle = "Completed at 7:40 AM",
                        category = "learning",
                        isDone = true,
                    )
                    DayHabitItem(
                        name = "Walk 5,000 steps",
                        subtitle = "6,120 steps logged",
                        category = "fitness",
                        isDone = true,
                    )
                    DayHabitItem(
                        name = "Meditate 10 mins",
                        subtitle = "Breath session complete",
                        category = "mindfulness",
                        isDone = true,
                    )
                    DayHabitItem(
                        name = "Evening Journal",
                        subtitle = "Scheduled for 9:30 PM",
                        category = "reflection",
                        isDone = false,
                    )
                    DayHabitItem(
                        name = "Deep Work Sprint",
                        subtitle = "45 min focus block",
                        category = "productivity",
                        isDone = false,
                    )
                }
            }

            // Bottom Motivational Banner
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = EverisePaleSageSurface,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFD3E6D7)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Spa,
                            contentDescription = null,
                            tint = Color(0xFF2E633C),
                            modifier = Modifier.size(18.dp),
                        )
                    }
                    Column {
                        Text(
                            "Consistency over perfection",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1E462A),
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Zero pressure if today isn't 100%. Giving yourself permission to pause and returning tomorrow is what makes a habit permanent.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF2E633C),
                        )
                    }
                }
            }

            Spacer(Modifier.height(84.dp))
        }
    }
}

@Composable
private fun LegendDot(color: Color, label: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = EveriseSecondaryTextLight,
        )
    }
}

@Composable
private fun HeatmapGrid() {
    val cols = 12
    val rows = 7
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        repeat(cols) { c ->
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                repeat(rows) { r ->
                    val shade = when {
                        (c + r) % 5 == 0 -> EverisePaleBlueSurface
                        (c * r) % 3 == 0 -> EveriseSageGreen
                        else -> EveriseBrandBlue
                    }
                    Box(
                        modifier = Modifier
                            .size(16.dp)
                            .clip(RoundedCornerShape(3.dp))
                            .background(shade),
                    )
                }
            }
        }
    }
}

@Composable
private fun DayHabitItem(
    name: String,
    subtitle: String,
    category: String,
    isDone: Boolean,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(if (isDone) EverisePaleSageSurface.copy(alpha = 0.5f) else EverisePaleBlueSurface.copy(alpha = 0.3f))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val (icon, tint) = resolveHabitIcon(category, name)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EveriseWhiteSurface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }
        Spacer(Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = EverisePrimaryTextLight,
            )
            Text(
                subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = EveriseSecondaryTextLight,
            )
        }
        Surface(
            shape = CircleShape,
            color = if (isDone) EveriseSageGreen else EverisePaleBlueSurface,
            modifier = Modifier.size(28.dp),
        ) {
            Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                if (isDone) {
                    Icon(Icons.Outlined.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(15.dp))
                }
            }
        }
    }
}
