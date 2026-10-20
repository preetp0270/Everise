package com.example.everise.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.everise.domain.model.HabitWithTodayStatus
import com.example.everise.ui.theme.SuccessLight

@Composable
fun HabitCard(
    item: HabitWithTodayStatus,
    onToggle: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val completed = item.isCompletedToday
    val borderColor by animateColorAsState(
        if (completed) SuccessLight else MaterialTheme.colorScheme.outline,
        label = "habitBorder",
    )
    val ringColor by animateColorAsState(
        if (completed) SuccessLight else MaterialTheme.colorScheme.outline,
        label = "habitRing",
    )

    Surface(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp))
            .border(1.dp, borderColor, RoundedCornerShape(24.dp))
            .clickable(onClick = onClick),
        color = if (completed) {
            MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)
        } else {
            MaterialTheme.colorScheme.surface
        },
        shape = RoundedCornerShape(24.dp),
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                modifier = Modifier
                    .size(44.dp)
                    .clip(CircleShape)
                    .clickable(onClick = onToggle),
                color = if (completed) SuccessLight else Color.Transparent,
                shape = CircleShape,
                border = androidx.compose.foundation.BorderStroke(2.5.dp, ringColor),
            ) {
                if (completed) {
                    Icon(
                        Icons.Outlined.Check,
                        contentDescription = "Completed",
                        tint = Color.White,
                        modifier = Modifier.padding(10.dp),
                    )
                }
            }
            Spacer(modifier = Modifier.width(14.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.habit.name,
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = item.habit.category.replace('_', ' ')
                        .replaceFirstChar { it.uppercase() },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}
