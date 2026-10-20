package com.example.everise.ui.screens.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.everise.app.EveriseApp
import com.example.everise.ui.navigation.Routes
import com.example.everise.ui.screens.calendar.CalendarScreen
import com.example.everise.ui.screens.habit.CreateHabitScreen
import com.example.everise.ui.screens.home.HomeScreen
import com.example.everise.ui.screens.home.HomeViewModel
import com.example.everise.ui.screens.insights.InsightsScreen
import com.example.everise.ui.screens.profile.ProfileScreen

private data class BottomDest(val route: String, val label: String, val icon: ImageVector)

private val bottomDestinations = listOf(
    BottomDest(Routes.HOME, "Home", Icons.Outlined.Home),
    BottomDest(Routes.CALENDAR, "Calendar", Icons.Outlined.CalendarMonth),
    BottomDest(Routes.ADD_HABIT, "Add", Icons.Outlined.AddCircleOutline),
    BottomDest(Routes.INSIGHTS, "Insights", Icons.Outlined.BarChart),
    BottomDest(Routes.PROFILE, "Profile", Icons.Outlined.Person),
)

@Composable
fun MainScaffold(
    userName: String = "there",
    onLogout: () -> Unit = {},
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val app = LocalContext.current.applicationContext as EveriseApp
    val habitRepo = app.container.habitRepository

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp,
            ) {
                bottomDestinations.forEach { dest ->
                    val selected = currentDestination?.hierarchy?.any { it.route == dest.route } == true
                    NavigationBarItem(
                        selected = selected,
                        onClick = {
                            navController.navigate(dest.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        },
                        icon = { Icon(dest.icon, contentDescription = dest.label) },
                        label = {
                            Text(dest.label, style = MaterialTheme.typography.labelSmall)
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f),
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                        ),
                    )
                }
            }
        },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(Routes.HOME) {
                val vm: HomeViewModel = viewModel(factory = HomeViewModel.Factory(habitRepo))
                HomeScreen(
                    viewModel = vm,
                    userName = userName,
                    onAddHabit = { navController.navigate(Routes.ADD_HABIT) },
                    onHabitClick = { /* detail later */ },
                )
            }
            composable(Routes.CALENDAR) {
                CalendarScreen(habitRepository = habitRepo)
            }
            composable(Routes.ADD_HABIT) {
                CreateHabitScreen(
                    habitRepository = habitRepo,
                    onDone = {
                        navController.navigate(Routes.HOME) {
                            popUpTo(Routes.HOME) { inclusive = false }
                            launchSingleTop = true
                        }
                    },
                    onBack = { navController.popBackStack() },
                )
            }
            composable(Routes.INSIGHTS) {
                InsightsScreen(habitRepository = habitRepo)
            }
            composable(Routes.PROFILE) {
                ProfileScreen(userName = userName, onLogout = onLogout)
            }
        }
    }
}
