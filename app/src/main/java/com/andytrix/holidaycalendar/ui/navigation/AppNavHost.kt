package com.andytrix.holidaycalendar.ui.navigation

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.andytrix.holidaycalendar.R
import com.andytrix.holidaycalendar.ui.components.AppTopBar
import com.andytrix.holidaycalendar.ui.screens.HomeScreen
import com.andytrix.holidaycalendar.ui.screens.InfoScreen
import com.andytrix.holidaycalendar.ui.screens.SettingsScreen

@Composable
fun AppNavHost(
    darkTheme: Boolean,
    onDarkThemeChange: (Boolean) -> Unit
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: Routes.HOME

    val title = when (currentRoute) {
        Routes.HOME -> stringResource(R.string.home_title)
        Routes.INFO -> stringResource(R.string.info_title)
        Routes.SETTINGS -> stringResource(R.string.settings_title)
        else -> stringResource(R.string.app_title)
    }

    val showBack = currentRoute != Routes.HOME
    val showInfo = currentRoute == Routes.HOME
    val showSettings = currentRoute != Routes.SETTINGS

    Scaffold(
        topBar = {
            AppTopBar(
                title = title,
                showBack = showBack,
                showInfo = showInfo,
                showSettings = showSettings,
                onBack = { navController.popBackStack() },
                onInfo = { navController.navigate(Routes.INFO) },
                onSettings = { navController.navigate(Routes.SETTINGS) }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Routes.HOME
        ) {
            composable(Routes.HOME) {
                HomeScreen(paddingValues = innerPadding)
            }
            composable(Routes.INFO) {
                InfoScreen(paddingValues = innerPadding)
            }
            composable(Routes.SETTINGS) {
                SettingsScreen(
                    paddingValues = innerPadding,
                    darkTheme = darkTheme,
                    onDarkThemeChange = onDarkThemeChange
                )
            }
        }
    }
}
