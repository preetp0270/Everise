package com.example.everise

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.everise.app.EveriseApp
import com.example.everise.core.notifications.ReminderScheduler
import com.example.everise.ui.navigation.EveriseNavGraph
import com.example.everise.ui.theme.EveriseTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val notificationPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            val app = application as EveriseApp
            CoroutineScope(Dispatchers.IO).launch {
                app.container.preferencesStore.setNotificationsEnabled(granted)
            }
            if (granted) {
                ReminderScheduler.ensureChannel(this)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermissionIfNeeded()
        setContent {
            EveriseTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    EveriseNavGraph()
                }
            }
        }
    }

    /**
     * First-install style prompt (Android 13+): system dialog asks to allow notifications,
     * same pattern as other apps. Only asked once unless user never answered.
     */
    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            ReminderScheduler.ensureChannel(this)
            return
        }
        val granted = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS,
        ) == PackageManager.PERMISSION_GRANTED
        if (granted) {
            ReminderScheduler.ensureChannel(this)
            return
        }
        val app = application as EveriseApp
        CoroutineScope(Dispatchers.Main).launch {
            val prefs = app.container.preferencesStore.prefs.first()
            // Ask once on first open; Settings can re-request later
            if (!prefs.notificationsPrompted) {
                app.container.preferencesStore.setNotificationsPrompted(true)
                notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }
}
