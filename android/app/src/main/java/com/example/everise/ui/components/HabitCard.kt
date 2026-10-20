package com.example.everise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.domain.model.TargetType
import com.example.everise.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HabitCard(
    item: HabitWithTodayStatus,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    onStartTimer: (() -> Unit)? = null,
) {
    val done = item.isCompletedToday
    val habit = item.habit
    val iconInfo = resolveHabitIcon(habit.category, habit.name)
    val isDurationHabit = habit.targetType == TargetType.DURATION

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (done) EverisePaleSageSurface else EveriseWhiteSurface,
        shadowElevation = if (done) 0.dp else 1.dp,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // Left category / habit icon badge
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (done) EveriseWhiteSurface else EverisePaleBlueSurface),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        imageVector = iconInfo.first,
                        contentDescription = habit.category,
                        tint = if (done) EveriseDeepBlue else EveriseDeepBlue,
                        modifier = Modifier.size(22.dp),
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Title and contextual subtitle
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = habit.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = EverisePrimaryTextLight,
                        textDecoration = if (done) TextDecoration.LineThrough else TextDecoration.None,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    val subtitleText = when {
                        done -> {
                            val timeStr = item.todayLog?.completedAt?.let {
                                SimpleDateFormat("h:mm a", Locale.getDefault()).format(Date(it))
                            }
                            if (timeStr != null) "Completed at $timeStr"
                            else if (habit.targetValue > 1 && habit.targetUnit.isNotBlank()) {
                                "${habit.targetValue}/${habit.targetValue} ${habit.targetUnit} completed"
                            } else "Completed today"
                        }
                        isDurationHabit && habit.targetValue > 0 -> {
                            if (habit.description.isNotBlank()) habit.description
                            else "${habit.targetValue} ${habit.targetUnit.ifBlank { "min" }} focus block"
                        }
                        habit.reminderEnabled && habit.reminderTimes.isNotEmpty() -> {
                            "Scheduled for ${habit.reminderTimes.first()} · ${habit.category.replace('_', ' ')}"
                        }
                        habit.description.isNotBlank() -> habit.description
                        else -> habit.category.replace('_', ' ').replaceFirstChar { it.uppercase() }
                    }

                    Text(
                        text = subtitleText,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (done) EveriseSecondaryTextLight else EveriseSecondaryTextLight,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // 3-dot menu button
                IconButton(
                    onClick = onClick,
                    modifier = Modifier.size(32.dp),
                ) {
                    Icon(
                        Icons.Outlined.MoreVert,
                        contentDescription = "Details",
                        tint = EveriseSecondaryTextLight,
                        modifier = Modifier.size(18.dp),
                    )
                }

                Spacer(modifier = Modifier.width(4.dp))

                // Right action: Duration action pill OR Completion Circle Check
                if (!done && isDurationHabit) {
                    Surface(
                        onClick = { onStartTimer?.invoke() ?: onToggle() },
                        shape = RoundedCornerShape(20.dp),
                        color = EverisePaleBlueSurface,
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp),
                        ) {
                            Icon(
                                Icons.Outlined.PlayArrow,
                                contentDescription = "Start",
                                tint = EveriseDeepBlue,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = "Start",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = EveriseDeepBlue,
                            )
                        }
                    }
                } else {
                    // Check Circle button
                    Surface(
                        shape = CircleShape,
                        color = if (done) EveriseSageGreen else EverisePaleBlueSurface,
                        modifier = Modifier
                            .size(34.dp)
                            .clickable(onClick = onToggle),
                    ) {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                            if (done) {
                                Icon(
                                    Icons.Outlined.Check,
                                    contentDescription = "Completed",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp),
                                )
                            } else {
                                Box(
                                    modifier = Modifier
                                        .size(16.dp)
                                        .clip(CircleShape)
                                        .background(EveriseBrandBlue.copy(alpha = 0.25f)),
                                )
                            }
                        }
                    }
                }
            }

            // Optional thin progress line along bottom if in-progress duration
            if (!done && isDurationHabit) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .background(EverisePaleBlueSurface),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.35f)
                            .height(3.dp)
                            .background(EveriseDeepBlue),
                    )
                }
            }
        }
    }
}

/**
 * Maps habit category and name to suitable Material icon vector and accent color.
 */
fun resolveHabitIcon(category: String, name: String): Pair<ImageVector, Color> {
    val lower = (category + " " + name).lowercase()
    return when {
        lower.contains("water") || lower.contains("hydrat") || lower.contains("drink") ->
            Icons.Outlined.WaterDrop to EveriseBrandBlue
        lower.contains("read") || lower.contains("book") || lower.contains("learn") ->
            Icons.Outlined.MenuBook to EveriseDeepBlue
        lower.contains("walk") || lower.contains("step") || lower.contains("run") || lower.contains("fitness") ->
            Icons.Outlined.DirectionsWalk to EveriseSageGreen
        lower.contains("meditat") || lower.contains("breath") || lower.contains("mindful") ->
            Icons.Outlined.SelfImprovement to EveriseBrandBlue
        lower.contains("journal") || lower.contains("reflect") || lower.contains("write") ->
            Icons.Outlined.EditNote to EveriseDeepBlue
        lower.contains("deep work") || lower.contains("focus") || lower.contains("code") || lower.contains("work") ->
            Icons.Outlined.Psychology to EveriseBrandBlue
        else ->
            Icons.Outlined.Spa to EveriseSageGreen
    }
}
