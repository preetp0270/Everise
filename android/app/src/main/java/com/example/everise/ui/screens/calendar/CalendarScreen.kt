package com.example.everise.ui.screens.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.outlined.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.everise.core.common.todayIsoDate
import com.example.everise.domain.repository.HabitRepository
import com.example.everise.ui.theme.SuccessLight
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun CalendarScreen(habitRepository: HabitRepository) {
    var month by remember { mutableStateOf(YearMonth.now()) }
    val today = todayIsoDate()
    val firstDay = month.atDay(1)
    val daysInMonth = month.lengthOfMonth()
    val startOffset = (firstDay.dayOfWeek.value % 7) // Mon=1..Sun=7 → 0 if Sun-start optional; use Mon-start: value-1
    val monStartOffset = firstDay.dayOfWeek.value - 1

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(onClick = { month = month.minusMonths(1) }) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowLeft, contentDescription = "Previous")
            }
            Text(
                text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground,
            )
            IconButton(onClick = { month = month.plusMonths(1) }) {
                Icon(Icons.AutoMirrored.Outlined.KeyboardArrowRight, contentDescription = "Next")
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun").forEach { d ->
                Text(
                    text = d,
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
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
                        val iso = date.toString()
                        val isToday = iso == today
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                                .padding(2.dp)
                                .clip(CircleShape)
                                .then(
                                    if (isToday) Modifier.background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f))
                                    else Modifier
                                )
                                .clickable { },
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = day.toString(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = if (isToday) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                        day++
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Select a day to review scheduled habits. Completion markers sync from your logs in later builds.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}
