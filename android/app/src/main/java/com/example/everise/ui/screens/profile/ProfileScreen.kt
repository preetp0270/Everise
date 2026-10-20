package com.example.everise.ui.screens.profile

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpCenter
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.automirrored.outlined.OpenInNew
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.everise.core.datastore.UserPreferencesStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String = "Elena Rostova",
    preferencesStore: UserPreferencesStore,
    onOpenSettings: () -> Unit = {},
    onSwitchAccount: () -> Unit = {},
    onLogout: () -> Unit = {},
    onDeleteAccount: () -> Unit = {},
) {
    val prefs by preferencesStore.prefs.collectAsStateWithLifecycle(
        initialValue = com.example.everise.core.datastore.UserPrefs(),
    )
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var showEditDialog by remember { mutableStateOf(false) }
    var currentName by remember { mutableStateOf(userName) }
    var showReviewTimeDialog by remember { mutableStateOf(false) }
    var showQuietHoursDialog by remember { mutableStateOf(false) }
    var showDevicesDialog by remember { mutableStateOf(false) }
    var showGuideDialog by remember { mutableStateOf(false) }
    var showFaqDialog by remember { mutableStateOf(false) }
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var syncStatusText by remember { mutableStateOf("Synced 2 mins ago") }
    var isSyncing by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = onOpenSettings) {
                        Icon(Icons.Outlined.Settings, contentDescription = "Settings")
                    }
                    Box(
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center,
                    ) {
                        Icon(
                            Icons.Outlined.Person,
                            contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(20.dp),
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.9f),
                ),
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Spacer(Modifier.height(4.dp))

            // Profile Hero Card (Elevated Night Canvas)
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.inverseSurface),
                elevation = CardDefaults.cardElevation(8.dp),
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // Identity Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .size(72.dp)
                                .clip(CircleShape)
                                .background(
                                    Brush.linearGradient(
                                        listOf(
                                            MaterialTheme.colorScheme.primaryContainer,
                                            MaterialTheme.colorScheme.secondaryContainer,
                                            MaterialTheme.colorScheme.tertiaryContainer,
                                        ),
                                    ),
                                )
                                .padding(2.dp),
                            contentAlignment = Alignment.Center,
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(
                                    Icons.Outlined.Person,
                                    contentDescription = null,
                                    modifier = Modifier.size(36.dp),
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.tertiary),
                                contentAlignment = Alignment.Center,
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer),
                                )
                            }
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(
                                    text = currentName,
                                    style = MaterialTheme.typography.titleMedium,
                                    color = MaterialTheme.colorScheme.inverseOnSurface,
                                    fontWeight = FontWeight.Bold,
                                )
                                OutlinedButton(
                                    onClick = { showEditDialog = true },
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                                    modifier = Modifier.height(32.dp),
                                    shape = RoundedCornerShape(16.dp),
                                ) {
                                    Icon(Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(14.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Edit", fontSize = 12.dp.value.sp)
                                }
                            }
                            Text(
                                text = "elena.rostova@example.com",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outlineVariant,
                            )
                            Spacer(Modifier.height(6.dp))
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.25f),
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Icon(
                                        Icons.Outlined.WorkspacePremium,
                                        contentDescription = null,
                                        modifier = Modifier.size(14.dp),
                                        tint = MaterialTheme.colorScheme.primaryFixedDim,
                                    )
                                    Text(
                                        text = "Pro Member • Compounding since Jan 2024",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primaryFixedDim,
                                    )
                                }
                            }
                        }
                    }

                    // Quick Metrics Ribbon (3-Column Bento Stat Row)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "14d",
                            label = "Streak",
                            valueColor = MaterialTheme.colorScheme.primaryFixedDim,
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "92%",
                            label = "Consistency",
                            valueColor = MaterialTheme.colorScheme.tertiaryFixed,
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            value = "6",
                            label = "Active Habits",
                            valueColor = MaterialTheme.colorScheme.secondaryFixed,
                        )
                    }
                }
            }

            // Appearance Section
            SectionHeader(icon = Icons.Outlined.Palette, title = "Appearance")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    // 3-Segment Theme Selector
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .background(MaterialTheme.colorScheme.surfaceContainerHighest)
                            .padding(4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                    ) {
                        ThemeTab(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.LightMode,
                            label = "Light",
                            selected = prefs.theme == "light",
                            onClick = { scope.launch { preferencesStore.setTheme("light") } },
                        )
                        ThemeTab(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.DarkMode,
                            label = "Dark",
                            selected = prefs.theme == "dark",
                            onClick = { scope.launch { preferencesStore.setTheme("dark") } },
                        )
                        ThemeTab(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Outlined.SettingsSystemDaydream,
                            label = "System",
                            selected = prefs.theme == "system",
                            onClick = { scope.launch { preferencesStore.setTheme("system") } },
                        )
                    }

                    // OLED Toggle Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f).padding(end = 8.dp)) {
                            Text("OLED Pure Black Mode", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                            Text("True black surfaces for battery conservation", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Switch(
                            checked = prefs.oledPureBlack,
                            onCheckedChange = { scope.launch { preferencesStore.setOledPureBlack(it) } },
                        )
                    }
                }
            }

            // Habits & Routine Preferences
            SectionHeader(icon = Icons.Outlined.Tune, title = "Habits & Routine")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column {
                    PreferenceRow(
                        icon = Icons.Outlined.CalendarToday,
                        title = "First Day of the Week",
                        trailingText = if (prefs.weekStartsOn == "monday") "Monday" else "Sunday",
                        onClick = {
                            val next = if (prefs.weekStartsOn == "monday") "sunday" else "monday"
                            scope.launch { preferencesStore.setWeekStartsOn(next) }
                            Toast.makeText(context, "Week starts on $next", Toast.LENGTH_SHORT).show()
                        },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.Outlined.Alarm,
                        title = "Daily Review Time",
                        trailingText = prefs.dailyReviewTime,
                        onClick = { showReviewTimeDialog = true },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceSwitchRow(
                        icon = Icons.Outlined.Vibration,
                        title = "Sound & Haptic Feedback",
                        subtitle = "Tactile click on habit completion",
                        checked = prefs.soundHaptics,
                        onCheckedChange = { scope.launch { preferencesStore.setSoundHaptics(it) } },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceSwitchRow(
                        icon = Icons.Outlined.AutoAwesome,
                        title = "Smart Habit Stacking",
                        subtitle = "AI-suggested cue pairings",
                        checked = prefs.smartHabitStacking,
                        onCheckedChange = { scope.launch { preferencesStore.setSmartHabitStacking(it) } },
                    )
                }
            }

            // Notifications & Reminders
            SectionHeader(icon = Icons.Outlined.Notifications, title = "Notifications")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column {
                    PreferenceSwitchRow(
                        icon = Icons.Outlined.NotificationsActive,
                        title = "Daily Habit Reminders",
                        checked = prefs.notificationsEnabled,
                        onCheckedChange = { scope.launch { preferencesStore.setNotificationsEnabled(it) } },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.Outlined.Bedtime,
                        title = "Quiet Hours (DND)",
                        subtitle = "No alerts while resting",
                        trailingText = prefs.quietHoursRange,
                        onClick = { showQuietHoursDialog = true },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceSwitchRow(
                        icon = Icons.Outlined.AcUnit,
                        title = "Streak Freeze & Gentle Alerts",
                        subtitle = "Never miss twice encouragement",
                        checked = prefs.streakFreeze,
                        onCheckedChange = { scope.launch { preferencesStore.setStreakFreeze(it) } },
                    )
                }
            }

            // Account, Privacy & Cloud Sync
            SectionHeader(icon = Icons.Outlined.CloudSync, title = "Account & Data")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column {
                    // Sync Status
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                                contentAlignment = Alignment.Center,
                            ) {
                                Icon(Icons.Outlined.CloudDone, contentDescription = null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Cloud Sync Status", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                                Row(
                                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(MaterialTheme.colorScheme.tertiary))
                                    Text(syncStatusText, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.tertiary)
                                }
                            }
                        }
                        IconButton(
                            onClick = {
                                isSyncing = true
                                syncStatusText = "Syncing..."
                                scope.launch {
                                    delay(1200L)
                                    isSyncing = false
                                    syncStatusText = "Synced just now"
                                    Toast.makeText(context, "Data successfully synced with cloud", Toast.LENGTH_SHORT).show()
                                }
                            },
                        ) {
                            Icon(
                                Icons.Outlined.Sync,
                                contentDescription = "Sync",
                                tint = if (isSyncing) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface,
                            )
                        }
                    }
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.Outlined.Download,
                        title = "Export Habit Data (CSV / JSON)",
                        onClick = {
                            Toast.makeText(context, "Exported backup to Downloads folder successfully", Toast.LENGTH_LONG).show()
                        },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceSwitchRow(
                        icon = Icons.Outlined.Fingerprint,
                        title = "Security & Biometric Lock",
                        subtitle = "Fingerprint / Face ID required",
                        checked = prefs.biometricLock,
                        onCheckedChange = { scope.launch { preferencesStore.setBiometricLock(it) } },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.Outlined.Devices,
                        title = "Active Devices",
                        subtitle = "Pixel 8 Pro (This device) • 2 devices active",
                        onClick = { showDevicesDialog = true },
                    )
                }
            }

            // About & Community Section
            SectionHeader(icon = Icons.AutoMirrored.Outlined.MenuBook, title = "About & Community")
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
            ) {
                Column {
                    PreferenceRow(
                        icon = Icons.Outlined.Psychology,
                        title = "Atomic Habits Methodology Guide",
                        trailingIcon = Icons.AutoMirrored.Outlined.OpenInNew,
                        onClick = { showGuideDialog = true },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.AutoMirrored.Outlined.HelpCenter,
                        title = "Help & FAQ",
                        onClick = { showFaqDialog = true },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    PreferenceRow(
                        icon = Icons.Outlined.Shield,
                        title = "Privacy Policy & Terms",
                        onClick = { showPrivacyDialog = true },
                    )
                    HorizontalDivider(color = MaterialTheme.colorScheme.surfaceContainerHighest)
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text("App Version", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceContainerHighest,
                        ) {
                            Text(
                                "v2.4.0 (Build 384)",
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.outline,
                            )
                        }
                    }
                }
            }

            // Session Controls & Danger Zone
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                Button(
                    onClick = onSwitchAccount,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.surfaceContainerHigh),
                ) {
                    Icon(Icons.Outlined.SwitchAccount, contentDescription = null, tint = MaterialTheme.colorScheme.onSurface)
                    Spacer(Modifier.width(8.dp))
                    Text("Switch Account", color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onLogout,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(26.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    ),
                ) {
                    Icon(Icons.AutoMirrored.Outlined.Logout, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Log Out", fontWeight = FontWeight.SemiBold)
                }

                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    TextButton(onClick = { showDeleteConfirmDialog = true }) {
                        Text(
                            "Delete Account & All Data",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.85f),
                        )
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }

    // Dialogs
    if (showEditDialog) {
        var nameInput by remember { mutableStateOf(currentName) }
        AlertDialog(
            onDismissRequest = { showEditDialog = false },
            title = { Text("Edit Profile Name") },
            text = {
                OutlinedTextField(
                    value = nameInput,
                    onValueChange = { nameInput = it },
                    singleLine = true,
                    label = { Text("Display Name") },
                )
            },
            confirmButton = {
                TextButton(onClick = {
                    currentName = nameInput
                    showEditDialog = false
                    Toast.makeText(context, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showEditDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showReviewTimeDialog) {
        var timeInput by remember { mutableStateOf(prefs.dailyReviewTime) }
        AlertDialog(
            onDismissRequest = { showReviewTimeDialog = false },
            title = { Text("Daily Review Time") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Set your preferred time for end-of-day reflection:")
                    OutlinedTextField(
                        value = timeInput,
                        onValueChange = { timeInput = it },
                        singleLine = true,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { preferencesStore.setDailyReviewTime(timeInput) }
                    showReviewTimeDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showReviewTimeDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showQuietHoursDialog) {
        var rangeInput by remember { mutableStateOf(prefs.quietHoursRange) }
        AlertDialog(
            onDismissRequest = { showQuietHoursDialog = false },
            title = { Text("Quiet Hours (DND)") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Set quiet hours range:")
                    OutlinedTextField(
                        value = rangeInput,
                        onValueChange = { rangeInput = it },
                        singleLine = true,
                    )
                }
            },
            confirmButton = {
                TextButton(onClick = {
                    scope.launch { preferencesStore.setQuietHoursRange(rangeInput) }
                    showQuietHoursDialog = false
                }) {
                    Text("Save")
                }
            },
            dismissButton = {
                TextButton(onClick = { showQuietHoursDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }

    if (showDevicesDialog) {
        AlertDialog(
            onDismissRequest = { showDevicesDialog = false },
            title = { Text("Active Devices") },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Pixel 8 Pro (This device) - Active now")
                    Text("• Pixel Tablet - Last active 2 hours ago")
                }
            },
            confirmButton = {
                TextButton(onClick = { showDevicesDialog = false }) {
                    Text("Close")
                }
            },
        )
    }

    if (showGuideDialog) {
        AlertDialog(
            onDismissRequest = { showGuideDialog = false },
            title = { Text("Atomic Habits Methodology") },
            text = {
                Text("1. Make it Obvious\n2. Make it Attractive\n3. Make it Easy\n4. Make it Satisfying\n\nCompound tiny 1% improvements every single day for remarkable long-term results.")
            },
            confirmButton = {
                TextButton(onClick = { showGuideDialog = false }) {
                    Text("Got it")
                }
            },
        )
    }

    if (showFaqDialog) {
        AlertDialog(
            onDismissRequest = { showFaqDialog = false },
            title = { Text("Help & FAQ") },
            text = {
                Text("Q: How do streaks work?\nA: Completing at least one target per habit each day maintains your streak.\n\nQ: Is cloud sync secure?\nA: Yes, end-to-end encrypted with zero-knowledge keys.")
            },
            confirmButton = {
                TextButton(onClick = { showFaqDialog = false }) {
                    Text("Close")
                }
            },
        )
    }

    if (showPrivacyDialog) {
        AlertDialog(
            onDismissRequest = { showPrivacyDialog = false },
            title = { Text("Privacy Policy & Terms") },
            text = {
                Text("Everise respects your personal data. All habit data is stored locally and securely synced. We never sell user data.")
            },
            confirmButton = {
                TextButton(onClick = { showPrivacyDialog = false }) {
                    Text("Agree & Close")
                }
            },
        )
    }

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Delete All Data?") },
            text = { Text("This will permanently delete all your habits, logs, and account data. This action cannot be undone.") },
            confirmButton = {
                TextButton(onClick = {
                    showDeleteConfirmDialog = false
                    onDeleteAccount()
                }) {
                    Text("Delete", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text("Cancel")
                }
            },
        )
    }
}

@Composable
private fun SectionHeader(icon: ImageVector, title: String) {
    Row(
        modifier = Modifier.padding(horizontal = 4.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primaryFixedDim, modifier = Modifier.size(18.dp))
        Text(
            text = title.uppercase(),
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onSurface,
            letterSpacing = 1.2.sp,
        )
    }
}

@Composable
private fun StatCard(modifier: Modifier = Modifier, value: String, label: String, valueColor: Color) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.15f),
    ) {
        Column(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = valueColor)
            Spacer(Modifier.height(2.dp))
            Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.outlineVariant)
        }
    }
}

@Composable
private fun ThemeTab(modifier: Modifier = Modifier, icon: ImageVector, label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        modifier = modifier.clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        color = if (selected) MaterialTheme.colorScheme.inverseSurface else Color.Transparent,
        shadowElevation = if (selected) 4.dp else 0.dp,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp),
                tint = if (selected) MaterialTheme.colorScheme.primaryFixedDim else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.primaryFixedDim else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

@Composable
private fun PreferenceRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    trailingText: String? = null,
    trailingIcon: ImageVector = Icons.Outlined.ChevronRight,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primaryFixedDim, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Row(
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (trailingText != null) {
                Text(trailingText, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
            }
            Icon(trailingIcon, contentDescription = null, tint = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun PreferenceSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String? = null,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f).padding(end = 8.dp),
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceContainerHighest),
                contentAlignment = Alignment.Center,
            ) {
                Icon(icon, contentDescription = null, tint = MaterialTheme.colorScheme.primaryFixedDim, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                if (subtitle != null) {
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
        )
    }
}
