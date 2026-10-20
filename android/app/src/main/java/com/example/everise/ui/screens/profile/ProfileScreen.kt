package com.example.everise.ui.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ProfileScreen(
    userName: String,
    onOpenSettings: () -> Unit,
    onLogout: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
    ) {
        Text("Profile", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onBackground)
        Spacer(Modifier.height(8.dp))
        Text(userName, style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.onSurface)
        Spacer(Modifier.height(24.dp))
        OutlinedButton(
            onClick = onOpenSettings,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(999.dp),
        ) {
            Text("Settings")
        }
        Spacer(Modifier.height(12.dp))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            ),
        ) {
            Text("Sign out")
        }
    }
}
