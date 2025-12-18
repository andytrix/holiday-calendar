package com.andytrix.holidaycalendar

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andytrix.holidaycalendar.ui.navigation.AppNavHost
import com.andytrix.holidaycalendar.ui.theme.HolidayPlannerTheme
import com.andytrix.holidaycalendar.viewmodel.HolidayViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val vm: HolidayViewModel = viewModel()

            HolidayPlannerTheme(darkTheme = vm.isDarkMode) {
                AppNavHost(
                    darkTheme = vm.isDarkMode,
                    onDarkThemeChange = vm::updateDarkMode
                )
            }
        }
    }
}
