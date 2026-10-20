package com.example.everise.ui.screens.settings

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.core.datastore.UserPreferencesStore
import com.example.everise.core.notifications.ReminderScheduler
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    preferencesStore: UserPreferencesStore,
    onBack: () -> Unit,
) {
    val prefs by preferencesStore.prefs.collectAsStateWithLifecycle(
        initialValue = com.example.everise.core.datastore.UserPrefs(),
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { granted ->
        scope.launch {
            preferencesStore.setNotificationsEnabled(granted)
            if (granted) ReminderScheduler.ensureChannel(context)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
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
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            Text("Appearance", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth()) {
                listOf("system" to "System", "light" to "Light", "dark" to "Dark").forEach { (id, label) ->
                    FilterChip(
                        selected = prefs.theme == id,
                        onClick = { scope.launch { preferencesStore.setTheme(id) } },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            Text("Notifications", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Habit reminders", modifier = Modifier.weight(1f))
                Switch(
                    checked = prefs.notificationsEnabled,
                    onCheckedChange = { enabled ->
                        if (enabled && Build.VERSION.SDK_INT >= 33) {
                            val granted = ContextCompat.checkSelfPermission(
                                context,
                                Manifest.permission.POST_NOTIFICATIONS,
                            ) == PackageManager.PERMISSION_GRANTED
                            if (!granted) {
                                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                                return@Switch
                            }
                        }
                        scope.launch {
                            preferencesStore.setNotificationsEnabled(enabled)
                            if (enabled) ReminderScheduler.ensureChannel(context)
                        }
                    },
                )
            }
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text("Quiet hours (evening)", modifier = Modifier.weight(1f))
                Switch(
                    checked = prefs.quietHoursEnabled,
                    onCheckedChange = { scope.launch { preferencesStore.setQuietHours(it) } },
                )
            }
            Spacer(Modifier.height(24.dp))

            Text("Calendar", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Row {
                listOf("monday" to "Week starts Monday", "sunday" to "Week starts Sunday").forEach { (id, label) ->
                    FilterChip(
                        selected = prefs.weekStartsOn == id,
                        onClick = { scope.launch { preferencesStore.setWeekStartsOn(id) } },
                        label = { Text(label) },
                        modifier = Modifier.padding(end = 8.dp),
                    )
                }
            }
            Spacer(Modifier.height(24.dp))

            Text(
                "Sign out is on the Profile tab. Account deletion and data export will expand here.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
