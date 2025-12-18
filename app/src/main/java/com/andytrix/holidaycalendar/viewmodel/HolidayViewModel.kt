package com.andytrix.holidaycalendar.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.andytrix.holidaycalendar.R
import com.andytrix.holidaycalendar.data.PrefsDataStore
import com.andytrix.holidaycalendar.data.model.PublicHoliday
import com.andytrix.holidaycalendar.data.repository.HolidayRepository
import com.andytrix.holidaycalendar.ui.state.HolidayUiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

class HolidayViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = HolidayRepository()
    private val prefs = PrefsDataStore(application.applicationContext)

    var uiState: HolidayUiState by mutableStateOf(HolidayUiState.Idle)
        private set

    var selectedCountryCode by mutableStateOf("FI")
        private set

    var yearText by mutableStateOf("2025")
        private set

    var isDarkMode by mutableStateOf(false)
        private set

    private var lastQuery: Pair<Int, String>? = null
    private var lastResult: List<PublicHoliday> = emptyList()

    init {
        viewModelScope.launch {
            selectedCountryCode = prefs.countryCode.first()
            yearText = prefs.year.first()
            isDarkMode = prefs.darkMode.first()
        }
    }

    fun updateCountryCode(code: String) {
        selectedCountryCode = code
        viewModelScope.launch { prefs.setCountryCode(code) }
    }

    fun updateYearText(value: String) {
        yearText = value
        viewModelScope.launch { prefs.setYear(value) }
    }

    fun updateDarkMode(enabled: Boolean) {
        isDarkMode = enabled
        viewModelScope.launch { prefs.setDarkMode(enabled) }
    }

    fun fetchHolidays(yearInput: String, countryInput: String) {
        val year = yearInput.toIntOrNull()
        val countryCode = countryInput.trim().uppercase()

        if (year == null || year < 1900 || year > 2100) {
            uiState = HolidayUiState.Error(R.string.error_invalid_year)
            return
        }

        if (countryCode.length != 2) {
            uiState = HolidayUiState.Error(R.string.error_invalid_country)
            return
        }

        val query = year to countryCode

        uiState = HolidayUiState.Loading

        viewModelScope.launch {
            try {
                val result = withContext(Dispatchers.IO) {
                    repository.getPublicHolidays(year, countryCode)
                }

                lastQuery = query
                lastResult = result
                uiState = HolidayUiState.Success(result)
            } catch (_: HttpException) {
                uiState = HolidayUiState.Error(R.string.error_http)
            } catch (_: Exception) {
                uiState = HolidayUiState.Error(R.string.error_unknown)
            }
        }
    }
}
