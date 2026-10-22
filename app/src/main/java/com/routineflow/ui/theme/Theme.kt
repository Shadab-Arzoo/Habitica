package com.routineflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AppleLightColorScheme = lightColorScheme(
    primary = Color(0xFF007AFF),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFDCEBFF),
    onPrimaryContainer = Color(0xFF003C7A),
    secondary = Color(0xFF5E5CE6),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFE4E3FF),
    onSecondaryContainer = Color(0xFF29276B),
    tertiary = Color(0xFFFF375F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFFFE1E7),
    onTertiaryContainer = Color(0xFF7A001C),
    background = Color(0xFFF2F2F7),
    onBackground = Color(0xFF000000),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF000000),
    surfaceVariant = Color(0xFFE5E5EA),
    onSurfaceVariant = Color(0xFF3C3C43),
    outline = Color(0xFFC7C7CC),
    outlineVariant = Color(0xFFD1D1D6)
)

private val AppleDarkColorScheme = darkColorScheme(
    primary = Color(0xFF0A84FF),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFF004A92),
    onPrimaryContainer = Color(0xFFDCEBFF),
    secondary = Color(0xFF7D7AFF),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFF414080),
    onSecondaryContainer = Color(0xFFE4E3FF),
    tertiary = Color(0xFFFF375F),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFF8C0025),
    onTertiaryContainer = Color(0xFFFFE1E7),
    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),
    surface = Color(0xFF1C1C1E),
    onSurface = Color(0xFFFFFFFF),
    surfaceVariant = Color(0xFF2C2C2E),
    onSurfaceVariant = Color(0xFFEBEBF5),
    outline = Color(0xFF636366),
    outlineVariant = Color(0xFF48484A)
)

@Composable
fun RoutineFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) AppleDarkColorScheme else AppleLightColorScheme,
        typography = AppTypography,
        content = content
    )
}
