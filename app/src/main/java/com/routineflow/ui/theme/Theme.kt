package com.routineflow.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val RoutineFlowLightColors = lightColorScheme(
    primary = Color(0xFF0066FF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE5EFFC),
    onPrimaryContainer = Color(0xFF00388A),
    secondary = Color(0xFF6366F1),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEEF2FF),
    onSecondaryContainer = Color(0xFF312E81),
    tertiary = Color(0xFFEC4899),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFCE7F3),
    onTertiaryContainer = Color(0xFF831843),
    background = Color(0xFFF4F6FB),
    onBackground = Color(0xFF0F172A),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF0F172A),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569),
    outline = Color(0xFFCBD5E1),
    outlineVariant = Color(0xFFE2E8F0),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8FAFC),
    surfaceContainer = Color(0xFFF1F5F9),
    surfaceContainerHigh = Color(0xFFE2E8F0),
    surfaceContainerHighest = Color(0xFFCBD5E1)
)

private val RoutineFlowDarkColors = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF818CF8),
    onSecondary = Color(0xFF3730A3),
    secondaryContainer = Color(0xFF4338CA),
    onSecondaryContainer = Color(0xFFEEF2FF),
    tertiary = Color(0xFFF472B6),
    onTertiary = Color(0xFF831843),
    tertiaryContainer = Color(0xFF9D174D),
    onTertiaryContainer = Color(0xFFFCE7F3),
    background = Color(0xFF090D16),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF131926),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E2638),
    onSurfaceVariant = Color(0xFF94A3B8),
    outline = Color(0xFF475569),
    outlineVariant = Color(0xFF1E293B),
    surfaceContainerLowest = Color(0xFF060910),
    surfaceContainerLow = Color(0xFF0D121F),
    surfaceContainer = Color(0xFF131926),
    surfaceContainerHigh = Color(0xFF1E2638),
    surfaceContainerHighest = Color(0xFF283248)
)

private val RoutineFlowShapes = Shapes(
    extraSmall = RoundedCornerShape(10.dp),
    small = RoundedCornerShape(14.dp),
    medium = RoundedCornerShape(20.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp)
)

@Composable
fun RoutineFlowTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) RoutineFlowDarkColors else RoutineFlowLightColors,
        typography = AppTypography,
        shapes = RoutineFlowShapes,
        content = content
    )
}
