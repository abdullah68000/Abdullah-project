package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = FarmDarkGreen,
    onPrimary = Color.White,
    primaryContainer = FarmLightGreen,
    onPrimaryContainer = FarmDarkGreen,
    secondary = FarmFreshGreen,
    onSecondary = Color.White,
    secondaryContainer = FarmLightGreen,
    onSecondaryContainer = FarmDarkGreenHeader,
    tertiary = FarmNaturalBrown,
    onTertiary = Color.White,
    tertiaryContainer = FarmBrownSurface,
    onTertiaryContainer = FarmNaturalBrown,
    background = FarmBackground,
    onBackground = FarmTextPrimary,
    surface = FarmSurfaceCard,
    onSurface = FarmTextPrimary,
    surfaceVariant = Color(0xFFEFF5F0),
    onSurfaceVariant = FarmTextSecondary,
    outline = FarmBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFF67B279),
    onPrimary = Color(0xFF042111),
    primaryContainer = Color(0xFF1B4D33),
    onPrimaryContainer = Color(0xFFBBE5C4),
    secondary = Color(0xFF81C784),
    onSecondary = Color(0xFF0C3813),
    secondaryContainer = Color(0xFF204826),
    onSecondaryContainer = Color(0xFFA7E8AC),
    tertiary = Color(0xFFD7CCC8),
    onTertiary = Color(0xFF3E2723),
    background = Color(0xFF101913),
    onBackground = Color(0xFFE2ECE4),
    surface = Color(0xFF16221A),
    onSurface = Color(0xFFE2ECE4),
    surfaceVariant = Color(0xFF223126),
    onSurfaceVariant = Color(0xFFB5C5B9),
    outline = Color(0xFF3B4F41)
)

@Composable
fun RehmatDairyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    // We intentionally prioritize the bespoke Rehmat Dairy Farm brand palette
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

// Backward compatibility alias
@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    RehmatDairyTheme(darkTheme = darkTheme, content = content)
}
