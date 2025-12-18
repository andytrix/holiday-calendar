package com.andytrix.holidaycalendar.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Update
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andytrix.holidaycalendar.R
import com.andytrix.holidaycalendar.ui.components.supportedCountries
import com.andytrix.holidaycalendar.ui.components.CountryDropdown
import com.andytrix.holidaycalendar.ui.components.ErrorView
import com.andytrix.holidaycalendar.ui.components.HolidayListItem
import com.andytrix.holidaycalendar.ui.components.LoadingView
import com.andytrix.holidaycalendar.ui.state.HolidayUiState
import com.andytrix.holidaycalendar.viewmodel.HolidayViewModel
import java.util.Calendar
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.material3.OutlinedTextFieldDefaults
import com.andytrix.holidaycalendar.ui.components.AppActionButton
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun HomeScreen(
    paddingValues: PaddingValues
) {
    val vm: HolidayViewModel = viewModel()
    val state = vm.uiState
    val focusManager = LocalFocusManager.current
    val countries = supportedCountries()
    val keyboardController = LocalSoftwareKeyboardController.current
    val yearFocusRequester = remember { FocusRequester() }

    val selectedCountry = countries.firstOrNull { it.code == vm.selectedCountryCode }
        ?: countries.first { it.code == "FI" }

    val year = vm.yearText

    var filterUpcoming by remember { mutableStateOf(true) }
    var filterGlobal by remember { mutableStateOf(false) }

    val sortedHolidays = (state as? HolidayUiState.Success)
        ?.holidays
        ?.sortedBy { it.date }
        .orEmpty()

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val todayIso = remember { getTodayIsoDate() }

    val filtered = sortedHolidays
        .let { list ->
            if (filterUpcoming && year.toIntOrNull() == currentYear)
            list.filter { it.date >= todayIso }
            else list
        }
        .let { list -> if (filterGlobal) list.filter { it.global == true } else list }

    val nextHoliday = filtered.firstOrNull { it.date >= todayIso }

    Surface(color = MaterialTheme.colorScheme.background) {
        LazyColumn(
            modifier = Modifier
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus()
                    keyboardController?.hide()
                }
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 24.dp)
        ) {
            item {
                HeroHeader()
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Filled.FilterAlt,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = stringResource(R.string.search_title),
                                style = MaterialTheme.typography.titleMedium
                            )
                        }

                        Text(
                            text = "${selectedCountry.name} (${selectedCountry.code}), $year",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        CountryDropdown(
                            selected = selectedCountry,
                            countries = countries,
                            onSelected = { vm.updateCountryCode(it.code) },
                            label = stringResource(R.string.select_country)
                        )

                        OutlinedTextField(
                            value = year,
                            onValueChange = { input ->
                                vm.updateYearText(input.filter { it.isDigit() }.take(4))
                            },
                            label = { Text(text = stringResource(R.string.select_year)) },
                            placeholder = { Text(text = stringResource(R.string.year_hint)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .focusRequester(yearFocusRequester),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 1.00f),
                                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 1.00f),
                                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                cursorColor = MaterialTheme.colorScheme.primary
                            )
                        )

                        AppActionButton(
                            text = stringResource(R.string.fetch_holidays),
                            icon = Icons.Filled.Refresh,
                            spinIconOnClick = true,
                            isLoading = state is HolidayUiState.Loading,
                            loadingText = stringResource(R.string.loading),
                            onClick = {
                                focusManager.clearFocus(force = true)
                                keyboardController?.hide()
                                vm.fetchHolidays(vm.yearText, vm.selectedCountryCode)
                            }
                        )

                        AnimatedVisibility(visible = state is HolidayUiState.Loading) {
                            Text(
                                text = stringResource(R.string.loading),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }

            item {
                ElevatedCard(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.next_holiday),
                            style = MaterialTheme.typography.titleMedium
                        )

                        when (state) {
                            HolidayUiState.Idle -> Text(text = stringResource(R.string.no_holidays_yet))
                            HolidayUiState.Loading -> LoadingView()
                            is HolidayUiState.Error -> ErrorView(
                                message = stringResource(state.messageResId),
                                onRetry = {
                                    when (state.messageResId) {
                                        R.string.error_invalid_year,
                                        R.string.error_invalid_country -> {
                                            // Input error: focus the year field and show keyboard
                                            focusManager.clearFocus(force = true)
                                            yearFocusRequester.requestFocus()
                                            keyboardController?.show()
                                        }
                                        else -> {
                                            // Network/server/unknown error: retry fetch
                                            focusManager.clearFocus(force = true)
                                            keyboardController?.hide()
                                            vm.fetchHolidays(vm.yearText, vm.selectedCountryCode)
                                        }
                                    }
                                }
                            )

                            is HolidayUiState.Success -> {
                                Text(
                                    text = nextHoliday?.name ?: stringResource(R.string.no_upcoming_holidays),
                                    style = MaterialTheme.typography.titleLarge
                                )

                                if (nextHoliday != null) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Surface(
                                            shape = MaterialTheme.shapes.medium,
                                            color = MaterialTheme.colorScheme.primaryContainer
                                        ) {
                                            Text(
                                                text = formatFinnishDate(nextHoliday.date),
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }

                                        Surface(
                                            shape = MaterialTheme.shapes.medium,
                                            color = MaterialTheme.colorScheme.secondaryContainer
                                        ) {
                                            Text(
                                                text = nextHoliday.countryCode ?: selectedCountry.code,
                                                style = MaterialTheme.typography.labelMedium,
                                                color = MaterialTheme.colorScheme.onSecondaryContainer,
                                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                            )
                                        }
                                    }

                                    Text(
                                        text = "${stringResource(R.string.local_name_label)}: ${nextHoliday.localName}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }
            }

            if (state is HolidayUiState.Success) {
                item {
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.holidays_list_title),
                                style = MaterialTheme.typography.titleMedium
                            )

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                FilterChip(
                                    selected = filterUpcoming,
                                    onClick = { filterUpcoming = !filterUpcoming },
                                    label = { Text(stringResource(R.string.filter_upcoming)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Update,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 2.dp)
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )

                                FilterChip(
                                    selected = filterGlobal,
                                    onClick = { filterGlobal = !filterGlobal },
                                    label = { Text(stringResource(R.string.filter_global)) },
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Filled.Public,
                                            contentDescription = null,
                                            modifier = Modifier.padding(end = 2.dp)
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                        selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            if (filtered.isEmpty()) {
                                Text(
                                    text = stringResource(R.string.no_results_filters),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            } else {
                                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                    filtered.forEach { holiday ->
                                        HolidayListItem(holiday = holiday)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HeroHeader() {
    val gradient = Brush.linearGradient(
        colors = listOf(
            MaterialTheme.colorScheme.primaryContainer,
            MaterialTheme.colorScheme.surfaceVariant
        )
    )

    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.elevatedCardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Surface(
                    shape = MaterialTheme.shapes.large,
                    color = MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarMonth,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(12.dp)
                    )
                }

                Column {
                    Text(
                        text = stringResource(R.string.public_holidays),
                        style = MaterialTheme.typography.headlineSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = stringResource(R.string.home_tagline),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                    )
                }
            }
        }
    }
}

private fun getTodayIsoDate(): String {
    val cal = Calendar.getInstance()
    val y = cal.get(Calendar.YEAR)
    val m = cal.get(Calendar.MONTH) + 1
    val d = cal.get(Calendar.DAY_OF_MONTH)
    val mm = if (m < 10) "0$m" else "$m"
    val dd = if (d < 10) "0$d" else "$d"
    return "$y-$mm-$dd"
}

private fun formatFinnishDate(isoDate: String): String {
    val parts = isoDate.split("-")
    return if (parts.size == 3) "${parts[2]}.${parts[1]}.${parts[0]}" else isoDate
}
