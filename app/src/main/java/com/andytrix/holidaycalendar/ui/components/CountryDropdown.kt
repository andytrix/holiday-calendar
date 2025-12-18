package com.andytrix.holidaycalendar.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import com.andytrix.holidaycalendar.data.model.Country

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CountryDropdown(
    selected: Country,
    countries: List<Country>,
    onSelected: (Country) -> Unit,
    label: String
) {
    var expanded by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    // Key fix: clear focus on the next frame after the menu closes
    LaunchedEffect(expanded) {
        if (!expanded) {
            withFrameNanos { }
            focusManager.clearFocus()
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = "${selected.name} (${selected.code})",
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                focusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 1.00f),
                unfocusedBorderColor = MaterialTheme.colorScheme.outline.copy(alpha = 1.00f),
                focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
            ),
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(
                    type = androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable
                )
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            countries.forEach { country ->
                DropdownMenuItem(
                    text = { Text("${country.name} (${country.code})") },
                    onClick = {
                        onSelected(country)
                        expanded = false
                    }
                )
            }
        }
    }
}
