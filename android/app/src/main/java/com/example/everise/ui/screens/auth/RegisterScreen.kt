package com.example.everise.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.everise.ui.theme.EveriseTheme

@Composable
fun RegisterScreen(
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onRegister: (displayName: String, email: String, password: String) -> Unit,
    onNavigateToLogin: () -> Unit,
) {
    var displayName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp, vertical = 32.dp),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Create account",
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Start building habits that last.",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Spacer(modifier = Modifier.height(28.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { displayName = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Display name") },
            leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            shape = RoundedCornerShape(16.dp),
            colors = fieldColors(),
        )
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Outlined.Email, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next,
            ),
            shape = RoundedCornerShape(16.dp),
            colors = fieldColors(),
        )
        Spacer(modifier = Modifier.height(14.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Password") },
            leadingIcon = { Icon(Icons.Outlined.Lock, contentDescription = null) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        if (passwordVisible) Icons.Outlined.VisibilityOff
                        else Icons.Outlined.Visibility,
                        contentDescription = null,
                    )
                }
            },
            singleLine = true,
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            supportingText = {
                Text(
                    "At least 8 characters, with a letter and a number",
                    style = MaterialTheme.typography.bodySmall,
                )
            },
            shape = RoundedCornerShape(16.dp),
            colors = fieldColors(),
        )

        if (errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        Button(
            onClick = {
                onRegister(displayName.trim(), email.trim(), password)
            },
            enabled = !isLoading && email.isNotBlank() && password.length >= 8,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(999.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.height(24.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp,
                )
            } else {
                Text("Create account", style = MaterialTheme.typography.labelLarge)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = onNavigateToLogin,
            modifier = Modifier.align(Alignment.CenterHorizontally),
        ) {
            Text(
                text = "Already have an account? Sign in",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = MaterialTheme.colorScheme.primary,
    unfocusedBorderColor = MaterialTheme.colorScheme.outline,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    unfocusedContainerColor = MaterialTheme.colorScheme.surface,
)

@Preview(showBackground = true)
@Composable
private fun RegisterPreview() {
    EveriseTheme {
        RegisterScreen(
            onRegister = { _, _, _ -> },
            onNavigateToLogin = {},
        )
    }
}
