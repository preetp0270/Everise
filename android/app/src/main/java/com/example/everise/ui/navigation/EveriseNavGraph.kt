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
import com.example.everise.core.common.isOnline
import com.example.everise.core.sync.SyncWorker
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
    val prefs = app.container.preferencesStore
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var displayName by remember { mutableStateOf("there") }
    var authError by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    fun goMain(name: String) {
        displayName = name.ifBlank { "there" }
        // Push any offline changes when entering the app online
        if (isOnline(context)) {
            SyncWorker.enqueue(context)
        }
        navController.navigate(Routes.MAIN) {
            popUpTo(0) { inclusive = true }
        }
    }

    NavHost(navController = navController, startDestination = Routes.SPLASH) {
        composable(Routes.SPLASH) {
            SplashScreen(
                onFinished = {
                    scope.launch {
                        // 1) Already logged in on this device → home (no login again)
                        if (authRepo.isLoggedIn()) {
                            val session = authRepo.getSession()
                            goMain(session?.displayName ?: "there")
                            return@launch
                        }
                        // 2) First-time vs returning visitor without session
                        val onboardingDone = prefs.isOnboardingDone()
                        if (onboardingDone) {
                            navController.navigate(Routes.LOGIN) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        } else {
                            navController.navigate(Routes.ONBOARDING) {
                                popUpTo(Routes.SPLASH) { inclusive = true }
                            }
                        }
                    }
                },
            )
        }
        composable(Routes.ONBOARDING) {
            OnboardingScreen(
                onGetStarted = {
                    scope.launch { prefs.setOnboardingDone(true) }
                    navController.navigate(Routes.REGISTER) {
                        popUpTo(Routes.ONBOARDING) { inclusive = true }
                    }
                },
                onLogin = {
                    scope.launch { prefs.setOnboardingDone(true) }
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
                        if (!isOnline(context)) {
                            isLoading = false
                            authError = "Turn on internet to sign in. After login, habits work offline."
                            return@launch
                        }
                        val result = authRepo.login(email, password)
                        isLoading = false
                        result.onSuccess {
                            prefs.setOnboardingDone(true)
                            goMain(it.displayName)
                        }.onFailure {
                            authError = it.message ?: "Login failed. Check email, password, and network."
                        }
                    }
                },
                onNavigateToRegister = {
                    authError = null
                    navController.navigate(Routes.REGISTER)
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
                        if (!isOnline(context)) {
                            isLoading = false
                            authError = "Turn on internet to create an account."
                            return@launch
                        }
                        val result = authRepo.register(email, password, name)
                        isLoading = false
                        result.onSuccess {
                            prefs.setOnboardingDone(true)
                            goMain(it.displayName)
                        }.onFailure {
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
