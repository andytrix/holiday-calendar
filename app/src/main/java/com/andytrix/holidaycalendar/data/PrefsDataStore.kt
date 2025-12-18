package com.andytrix.holidaycalendar.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "holiday_prefs")

object PrefKeys {
    val COUNTRY_CODE = stringPreferencesKey("country_code")
    val YEAR = stringPreferencesKey("year")
    val DARK_MODE = booleanPreferencesKey("dark_mode")
}

class PrefsDataStore(private val context: Context) {

    val countryCode: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.COUNTRY_CODE] ?: "FI"
    }

    val year: Flow<String> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.YEAR] ?: "2025"
    }

    val darkMode: Flow<Boolean> = context.dataStore.data.map { prefs ->
        prefs[PrefKeys.DARK_MODE] ?: false
    }

    suspend fun setCountryCode(code: String) {
        context.dataStore.edit { prefs -> prefs[PrefKeys.COUNTRY_CODE] = code }
    }

    suspend fun setYear(year: String) {
        context.dataStore.edit { prefs -> prefs[PrefKeys.YEAR] = year }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs -> prefs[PrefKeys.DARK_MODE] = enabled }
    }
}
