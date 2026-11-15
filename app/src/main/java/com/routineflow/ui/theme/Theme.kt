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

private val EmeraldDarkColors = darkColorScheme(
    primary = Color(0xFF10B981),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF047857),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF34D399),
    onSecondary = Color(0xFF065F46),
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    background = Color(0xFF090D16),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF131926),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1E2638),
    onSurfaceVariant = Color(0xFF94A3B8),
    surfaceContainerHigh = Color(0xFF1E2638)
)

private val SakuraDarkColors = darkColorScheme(
    primary = Color(0xFFEC4899),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFBE185D),
    onPrimaryContainer = Color(0xFFFCE7F3),
    secondary = Color(0xFFF472B6),
    onSecondary = Color(0xFF831843),
    secondaryContainer = Color(0xFF500724),
    onSecondaryContainer = Color(0xFFFBCFE8),
    background = Color(0xFF110A14),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1A101E),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF281830),
    onSurfaceVariant = Color(0xFFA898B0),
    surfaceContainerHigh = Color(0xFF281830)
)

private val OceanDarkColors = darkColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF38BDF8),
    onSecondary = Color(0xFF075985),
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    background = Color(0xFF070F18),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0F1826),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF1A2638),
    onSurfaceVariant = Color(0xFF8FA3B8),
    surfaceContainerHigh = Color(0xFF1A2638)
)

private val SunsetDarkColors = darkColorScheme(
    primary = Color(0xFFF59E0B),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFB45309),
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = Color(0xFFFBBF24),
    onSecondary = Color(0xFF78350F),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    background = Color(0xFF140F08),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF1E170F),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF302418),
    onSurfaceVariant = Color(0xFFB0A090),
    surfaceContainerHigh = Color(0xFF302418)
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
    palette: String = "Emerald",
    content: @Composable () -> Unit
) {
    val colorScheme = when (palette) {
        "Sakura" -> SakuraDarkColors
        "Ocean" -> OceanDarkColors
        "Sunset" -> SunsetDarkColors
        else -> EmeraldDarkColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = RoutineFlowShapes,
        content = content
    )
}
