package com.example.everise.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.everise.app.EveriseApp
import com.example.everise.ui.screens.auth.LoginScreen
import com.example.everise.ui.screens.auth.RegisterScreen
import com.example.everise.ui.screens.main.MainScaffold
import com.example.everise.ui.screens.onboarding.OnboardingScreen
import com.example.everise.ui.screens.splash.SplashScreen
import kotlinx.coroutines.launch

@Composable
fun EveriseNavGraph() {
    val navController = rememberNavController()
    val app = LocalContext.current.applicationContext as EveriseApp
    val authRepo = app.container.authRepository
    val scope = rememberCoroutineScope()

    var displayName by remember { mutableStateOf("there") }
    var authError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun goMain(name: String) {
        displayName = name
        navController.navigate(Routes.MAIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    navController.navigate(Routes.ONBOARDING) {
                        popUpTo(Routes.SPLASH) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onGetStarted = {
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onLogin = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.LOGIN) {
            LoginScreen(
                isLoading = isLoading,
                errorMessage = authError,
                onLogin = { email, password ->
                    scope.launch {
                        isLoading = true
                        authError = null
                        val result = authRepo.login(email, password)
                        isLoading = false
                        result.onSuccess { goMain(it.displayName) }
                            .onFailure {
                                // Offline / server down → still allow local use with email name
                                authError = it.message ?: "Could not reach server. Continuing offline."
                                // Optional: fall back to local session
                                // goMain(email.substringBefore("@"))
                            }
                    }
                },
                onNavigateToRegister = {
                    authError = null
                    navController.navigate(Routes.REGISTER)
                },
                onContinueAsGuest = {
                    scope.launch {
                        val session = authRepo.continueAsGuest()
                        goMain(session.displayName)
                    }
                },
            )
        }
        composable(Routes.REGISTER) {
            RegisterScreen(
                isLoading = isLoading,
                errorMessage = authError,
                onRegister = { name, email, password ->
                    scope.launch {
                        isLoading = true
                        authError = null
                        val result = authRepo.register(email, password, name)
                        isLoading = false
                        result.onSuccess { goMain(it.displayName) }
                            .onFailure {
                                authError = it.message ?: "Registration failed"
                            }
                    }
                },
                onNavigateToLogin = {
                    authError = null
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.REGISTER) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.MAIN) {
            MainScaffold(
                userName = displayName,
                onLogout = {
                    scope.launch {
                        authRepo.logout()
                        navController.navigate(Routes.LOGIN) {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                },
            )
        }
    }
}
