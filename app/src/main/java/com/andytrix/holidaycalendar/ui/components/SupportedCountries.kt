package com.andytrix.holidaycalendar.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.andytrix.holidaycalendar.R
import com.andytrix.holidaycalendar.data.model.Country

@Composable
fun supportedCountries(): List<Country> {
    return listOf(
        Country("FI", stringResource(R.string.country_fi)),
        Country("SE", stringResource(R.string.country_se)),
        Country("NO", stringResource(R.string.country_no)),
        Country("DK", stringResource(R.string.country_dk)),
        Country("EE", stringResource(R.string.country_ee)),
        Country("DE", stringResource(R.string.country_de)),
        Country("FR", stringResource(R.string.country_fr)),
        Country("GB", stringResource(R.string.country_gb)),
        Country("US", stringResource(R.string.country_us))
    )
}
