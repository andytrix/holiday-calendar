package com.andytrix.holidaycalendar.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,

    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,

    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,

    background = Background,
    onBackground = OnBackground,

    surface = Surface,
    onSurface = OnSurface,
    surfaceVariant = SurfaceVariant,
    onSurfaceVariant = OnSurfaceVariant,

    outline = Outline
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFC4B5FD),
    onPrimary = Color(0xFF22114A),
    primaryContainer = Color(0xFF2F1A66),
    onPrimaryContainer = Color(0xFFF3EFFF),

    secondary = Color(0xFF5EEAD4),
    onSecondary = Color(0xFF05302B),
    secondaryContainer = Color(0xFF0B3F39),
    onSecondaryContainer = Color(0xFFD1FAF5),

    tertiary = Color(0xFFFDBA74),
    onTertiary = Color(0xFF3A1606),
    tertiaryContainer = Color(0xFF4A1F0A),
    onTertiaryContainer = Color(0xFFFFEDD5),

    background = Color(0xFF0B1020),
    onBackground = Color(0xFFE5E7EB),

    surface = Color(0xFF0F172A),
    onSurface = Color(0xFFE5E7EB),
    surfaceVariant = Color(0xFF141B33),
    onSurfaceVariant = Color(0xFFCBD5E1),

    outline = Color(0xFF3A3F66)
)

@Composable
fun HolidayPlannerTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
