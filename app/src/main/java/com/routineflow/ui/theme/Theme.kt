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
    primary = Color(0xFF2F6BFF),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE2EAFF),
    onPrimaryContainer = Color(0xFF12357A),
    secondary = Color(0xFF6E63D9),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFEAE7FF),
    onSecondaryContainer = Color(0xFF332B72),
    tertiary = Color(0xFFEE6B8A),
    onTertiary = Color.White,
    tertiaryContainer = Color(0xFFFFE5EC),
    onTertiaryContainer = Color(0xFF7A2941),
    background = Color(0xFFF6F7FB),
    onBackground = Color(0xFF171A24),
    surface = Color(0xFFFCFCFE),
    onSurface = Color(0xFF171A24),
    surfaceVariant = Color(0xFFECEEF5),
    onSurfaceVariant = Color(0xFF5E6473),
    outline = Color(0xFFBFC5D2),
    outlineVariant = Color(0xFFDDE1EA),
    surfaceContainerLowest = Color.White,
    surfaceContainerLow = Color(0xFFF8F9FC),
    surfaceContainer = Color(0xFFF0F2F8),
    surfaceContainerHigh = Color(0xFFE9ECF4),
    surfaceContainerHighest = Color(0xFFE1E5EF)
)

private val RoutineFlowDarkColors = darkColorScheme(
    primary = Color(0xFF8EAEFF),
    onPrimary = Color(0xFF102B67),
    primaryContainer = Color(0xFF294889),
    onPrimaryContainer = Color(0xFFDCE6FF),
    secondary = Color(0xFFB5AAFF),
    onSecondary = Color(0xFF30286E),
    secondaryContainer = Color(0xFF474080),
    onSecondaryContainer = Color(0xFFEAE7FF),
    tertiary = Color(0xFFFFA9BC),
    onTertiary = Color(0xFF5A1D31),
    tertiaryContainer = Color(0xFF7A2941),
    onTertiaryContainer = Color(0xFFFFD9E2),
    background = Color(0xFF0C1018),
    onBackground = Color(0xFFF1F3FA),
    surface = Color(0xFF151A24),
    onSurface = Color(0xFFF1F3FA),
    surfaceVariant = Color(0xFF222936),
    onSurfaceVariant = Color(0xFFBEC5D5),
    outline = Color(0xFF4C5668),
    outlineVariant = Color(0xFF303846),
    surfaceContainerLowest = Color(0xFF090C12),
    surfaceContainerLow = Color(0xFF111620),
    surfaceContainer = Color(0xFF1A202B),
    surfaceContainerHigh = Color(0xFF242B38),
    surfaceContainerHighest = Color(0xFF2D3543)
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
