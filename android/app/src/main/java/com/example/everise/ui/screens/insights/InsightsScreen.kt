package com.example.everise.ui.screens.insights

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.domain.repository.HabitRepository
import com.example.everise.ui.components.resolveHabitIcon
import com.example.everise.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(habitRepository: HabitRepository) {
    val progress by habitRepository.observeDailyProgress().collectAsStateWithLifecycle(
        initialValue = com.example.everise.domain.model.DailyProgress(4, 6),
    )
    val habits by habitRepository.observeTodayHabits().collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedPeriod by remember { mutableStateOf("This Week") }

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
                            "Insights & Analytics",
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
            // Period selector tabs: This Week, Month, Year, All Time
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = EverisePaleBlueSurface.copy(alpha = 0.5f),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    listOf("This Week", "Month", "Year", "All Time").forEach { p ->
                        val isSelected = selectedPeriod == p
                        Surface(
                            onClick = { selectedPeriod = p },
                            shape = RoundedCornerShape(20.dp),
                            color = if (isSelected) EverisePrimaryTextLight else Color.Transparent,
                            modifier = Modifier.weight(1f),
                        ) {
                            Box(
                                modifier = Modifier.padding(vertical = 8.dp),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = p,
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) Color.White else EveriseSecondaryTextLight,
                                )
                            }
                        }
                    }
                }
            }

            // 3 Stat Summary Cards
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                InsightStatCard(
                    title = "Consistency",
                    value = "86%",
                    badge = "+4%",
                    badgeIsPositive = true,
                    icon = Icons.Outlined.CheckCircle,
                    modifier = Modifier.weight(1f),
                )
                InsightStatCard(
                    title = "Streak",
                    value = "14d",
                    badge = "Best: 28d",
                    badgeIsPositive = false,
                    icon = Icons.Outlined.LocalFireDepartment,
                    modifier = Modifier.weight(1f),
                )
                InsightStatCard(
                    title = "Active",
                    value = "${habits.size.coerceAtLeast(6)}",
                    badge = "Daily habits",
                    badgeIsPositive = false,
                    icon = Icons.Outlined.Spa,
                    modifier = Modifier.weight(1f),
                )
            }

            // Weekly Rhythm Bar Chart Card
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
                        Column {
                            Text(
                                "Weekly Rhythm",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EverisePrimaryTextLight,
                            )
                            Text(
                                "Steady daily completion vs 80% baseline",
                                style = MaterialTheme.typography.bodySmall,
                                color = EveriseSecondaryTextLight,
                            )
                        }
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = EverisePaleSageSurface,
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(Color(0xFF285C38)))
                                Text(
                                    "Optimal Flow",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF285C38),
                                )
                            }
                        }
                    }

                    Spacer(Modifier.height(20.dp))

                    // Weekly Bar Chart
                    WeeklyBarChart()
                }
            }

            // Momentum Window Card
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color(0xFFF3F5F9),
                modifier = Modifier.fillMaxWidth(),
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.Top,
                ) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(EveriseWhiteSurface),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Lightbulb,
                            contentDescription = null,
                            tint = EveriseDeepBlue,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                    Column {
                        Text(
                            "MOMENTUM WINDOW · 7:30 – 9:00 AM",
                            style = MaterialTheme.typography.labelSmall,
                            fontWeight = FontWeight.Bold,
                            color = EverisePrimaryTextLight,
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            "Your completion velocity is highest in the early morning. Stacking deliberate new practices during this 90-minute window yields a 3x higher 30-day retention.",
                            style = MaterialTheme.typography.bodySmall,
                            color = EveriseSecondaryTextLight,
                            lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                        )
                    }
                }
            }

            // Energy Allocation Card
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
                        Text(
                            "Energy Allocation",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = EverisePrimaryTextLight,
                        )
                        Text(
                            "4 active themes",
                            style = MaterialTheme.typography.bodySmall,
                            color = EveriseSecondaryTextLight,
                        )
                    }

                    // Stacked horizontal progress bar
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(10.dp)
                            .clip(RoundedCornerShape(5.dp)),
                    ) {
                        Box(modifier = Modifier.weight(0.35f).fillMaxHeight().background(EveriseDeepBlue))
                        Spacer(Modifier.width(2.dp))
                        Box(modifier = Modifier.weight(0.30f).fillMaxHeight().background(Color(0xFF436650)))
                        Spacer(Modifier.width(2.dp))
                        Box(modifier = Modifier.weight(0.20f).fillMaxHeight().background(EveriseBrandBlue))
                        Spacer(Modifier.width(2.dp))
                        Box(modifier = Modifier.weight(0.15f).fillMaxHeight().background(Color(0xFFA59E92)))
                    }

                    // Allocation Legend
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        AllocationLegendItem(color = EveriseDeepBlue, label = "Mindfulness", percent = "35%")
                        AllocationLegendItem(color = Color(0xFF436650), label = "Health", percent = "30%")
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        AllocationLegendItem(color = EveriseBrandBlue, label = "Learning", percent = "20%")
                        AllocationLegendItem(color = Color(0xFFA59E92), label = "Productivity", percent = "15%")
                    }
                }
            }

            // Habit Stability Leaderboard Card
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
                        Column {
                            Text(
                                "Habit Stability",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = EverisePrimaryTextLight,
                            )
                            Text(
                                "Ranked by 30-day recurrence reliability",
                                style = MaterialTheme.typography.bodySmall,
                                color = EveriseSecondaryTextLight,
                            )
                        }
                        Icon(
                            Icons.Outlined.Sort,
                            contentDescription = "Sort",
                            tint = EveriseSecondaryTextLight,
                            modifier = Modifier.size(20.dp),
                        )
                    }

                    StabilityRow(
                        name = "Hydration (8 glasses)",
                        streak = "24-day streak",
                        percent = 96,
                        category = "health",
                    )
                    StabilityRow(
                        name = "Morning Meditation",
                        streak = "14-day streak",
                        percent = 92,
                        category = "mindfulness",
                    )
                    StabilityRow(
                        name = "Daily Reading",
                        streak = "9-day streak",
                        percent = 88,
                        category = "learning",
                    )
                    StabilityRow(
                        name = "Daily Walking",
                        streak = "12-day streak",
                        percent = 85,
                        category = "fitness",
                    )
                    StabilityRow(
                        name = "Deep Work",
                        streak = "Needs ease · Try the 2-minute start...",
                        percent = 70,
                        category = "productivity",
                    )
                }
            }

            // Mindful Compounding Quote Card
            Surface(
                shape = RoundedCornerShape(18.dp),
                color = EveriseWhiteSurface,
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    // Serene Header
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE5EDE8)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                Icons.Outlined.Spa,
                                contentDescription = null,
                                tint = Color(0xFF416B4D),
                                modifier = Modifier.size(32.dp),
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                "Serene Flow & Reflection",
                                style = MaterialTheme.typography.labelSmall,
                                color = Color(0xFF416B4D),
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Icon(
                            Icons.Outlined.FormatQuote,
                            contentDescription = null,
                            tint = EveriseDeepBlue,
                            modifier = Modifier.size(18.dp),
                        )
                        Text(
                            "Mindful Compounding",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold,
                            color = EveriseDeepBlue,
                        )
                    }

                    Spacer(Modifier.height(6.dp))

                    Text(
                        "“You do not rise to the level of your goals. You fall to the level of your systems.” Focus today on showing up with low friction, rather than seeking perfection.",
                        style = MaterialTheme.typography.bodySmall,
                        color = EverisePrimaryTextLight,
                        lineHeight = MaterialTheme.typography.bodySmall.lineHeight,
                    )
                }
            }

            Spacer(Modifier.height(84.dp))
        }
    }
}

@Composable
private fun InsightStatCard(
    title: String,
    value: String,
    badge: String,
    badgeIsPositive: Boolean,
    icon: ImageVector,
    modifier: Modifier = Modifier,
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = EveriseWhiteSurface,
        shadowElevation = 1.dp,
        modifier = modifier,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    title,
                    style = MaterialTheme.typography.labelSmall,
                    color = EveriseSecondaryTextLight,
                )
                Icon(
                    icon,
                    contentDescription = null,
                    tint = EveriseBrandBlue,
                    modifier = Modifier.size(14.dp),
                )
            }
            Text(
                value,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = EverisePrimaryTextLight,
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                if (badgeIsPositive) {
                    Icon(
                        Icons.Outlined.ArrowUpward,
                        contentDescription = null,
                        tint = EveriseSageGreen,
                        modifier = Modifier.size(10.dp),
                    )
                }
                Text(
                    badge,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (badgeIsPositive) EveriseSageGreen else EveriseSecondaryTextLight,
                )
            }
        }
    }
}

@Composable
private fun WeeklyBarChart() {
    val dayLabels = listOf("M", "T", "W", "T", "F", "S", "S")
    // Completion heights (0.0 to 1.0)
    val heights = listOf(0.75f, 0.60f, 0.75f, 0.45f, 0.65f, 0.85f, 0.95f)

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(130.dp),
        ) {
            // 80% Dashed Goal Line
            Canvas(modifier = Modifier.fillMaxSize()) {
                val goalY = size.height * (1f - 0.80f)
                drawLine(
                    color = Color(0xFFC0C7CE),
                    start = Offset(0f, goalY),
                    end = Offset(size.width, goalY),
                    strokeWidth = 1.dp.toPx(),
                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(8f, 6f), 0f),
                )
            }

            // Top right "80% GOAL" label
            Text(
                "80% GOAL",
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(y = (-14).dp),
                style = MaterialTheme.typography.labelSmall,
                color = EveriseSecondaryTextLight,
            )

            // Bars
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom,
            ) {
                heights.forEachIndexed { idx, h ->
                    val isPeak = idx == 6
                    Box(
                        modifier = Modifier
                            .width(22.dp)
                            .fillMaxHeight(h)
                            .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp))
                            .background(if (isPeak) EveriseDeepBlue else if (h < 0.5f) EverisePaleBlueSurface else EveriseBrandBlue),
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))

        // Weekday labels underneath
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            dayLabels.forEachIndexed { idx, d ->
                Text(
                    text = d,
                    modifier = Modifier.width(22.dp),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = if (idx == 6) FontWeight.Bold else FontWeight.Medium,
                    color = if (idx == 6) EveriseDeepBlue else EveriseSecondaryTextLight,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                )
            }
        }
    }
}

@Composable
private fun AllocationLegendItem(color: Color, label: String, percent: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp),
    ) {
        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = EverisePrimaryTextLight,
        )
        Text(
            text = percent,
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = EveriseSecondaryTextLight,
        )
    }
}

@Composable
private fun StabilityRow(
    name: String,
    streak: String,
    percent: Int,
    category: String,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        val (icon, tint) = resolveHabitIcon(category, name)
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(EverisePaleBlueSurface),
            contentAlignment = Alignment.Center,
        ) {
            Icon(icon, contentDescription = null, tint = tint, modifier = Modifier.size(18.dp))
        }

        Column(modifier = Modifier.weight(1f)) {
            Text(
                name,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = EverisePrimaryTextLight,
            )
            Text(
                streak,
                style = MaterialTheme.typography.bodySmall,
                color = EveriseSecondaryTextLight,
            )
        }

        Column(horizontalAlignment = Alignment.End) {
            Text(
                "$percent%",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = EveriseDeepBlue,
            )
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(70.dp)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(EverisePaleBlueSurface),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(percent / 100f)
                        .fillMaxHeight()
                        .background(Color(0xFF436650)),
                )
            }
        }
    }
}
