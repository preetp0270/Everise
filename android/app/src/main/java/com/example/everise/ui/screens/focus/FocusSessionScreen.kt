package com.example.everise.ui.screens.focus

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

/**
 * Focus timer with persisted target end time recommended for production.
 * This implementation uses a simple countdown suitable for foreground use.
 */
@Composable
fun FocusSessionScreen(
    initialMinutes: Int = 25,
    onFinished: () -> Unit = {},
) {
    var remaining by remember { mutableIntStateOf(initialMinutes * 60) }
    var running by remember { mutableStateOf(false) }

    LaunchedEffect(running, remaining) {
        if (running && remaining > 0) {
            delay(1000)
            remaining -= 1
        } else if (remaining == 0 && running) {
            running = false
            onFinished()
        }
    }

    val mins = remaining / 60
    val secs = remaining % 60

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Focus session", style = MaterialTheme.typography.headlineMedium)
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "%02d:%02d".format(mins, secs),
            style = MaterialTheme.typography.displayMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { running = !running },
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(999.dp),
        ) {
            Text(if (running) "Pause" else "Start")
        }
    }
}
