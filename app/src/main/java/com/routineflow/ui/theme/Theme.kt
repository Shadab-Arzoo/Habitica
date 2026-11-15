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

// 1. EMERALD FOREST (🌲)
private val EmeraldLightColors = lightColorScheme(
    primary = Color(0xFF059669),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFD1FAE5),
    onPrimaryContainer = Color(0xFF064E3B),
    secondary = Color(0xFF10B981),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFA7F3D0),
    onSecondaryContainer = Color(0xFF065F46),
    background = Color(0xFFF0FDF4),
    onBackground = Color(0xFF064E3B),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF065F46),
    surfaceVariant = Color(0xFFE6F4EA),
    onSurfaceVariant = Color(0xFF047857),
    surfaceContainerHigh = Color(0xFFDCFCE7)
)

private val EmeraldDarkColors = darkColorScheme(
    primary = Color(0xFF34D399),
    onPrimary = Color(0xFF064E3B),
    primaryContainer = Color(0xFF047857),
    onPrimaryContainer = Color(0xFFD1FAE5),
    secondary = Color(0xFF10B981),
    onSecondary = Color(0xFF065F46),
    secondaryContainer = Color(0xFF064E3B),
    onSecondaryContainer = Color(0xFFA7F3D0),
    background = Color(0xFF061510),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0C221A),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF143026),
    onSurfaceVariant = Color(0xFF6EE7B7),
    surfaceContainerHigh = Color(0xFF143026)
)

// 2. SAKURA BLOSSOM (🌸)
private val SakuraLightColors = lightColorScheme(
    primary = Color(0xFFDB2777),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFCE7F3),
    onPrimaryContainer = Color(0xFF831843),
    secondary = Color(0xFFEC4899),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFBCFE8),
    onSecondaryContainer = Color(0xFF9D174D),
    background = Color(0xFFFDF2F8),
    onBackground = Color(0xFF831843),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF831843),
    surfaceVariant = Color(0xFFFCE7F3),
    onSurfaceVariant = Color(0xFFBE185D),
    surfaceContainerHigh = Color(0xFFFCE7F3)
)

private val SakuraDarkColors = darkColorScheme(
    primary = Color(0xFFF472B6),
    onPrimary = Color(0xFF831843),
    primaryContainer = Color(0xFFBE185D),
    onPrimaryContainer = Color(0xFFFCE7F3),
    secondary = Color(0xFFEC4899),
    onSecondary = Color(0xFF831843),
    secondaryContainer = Color(0xFF500724),
    onSecondaryContainer = Color(0xFFFBCFE8),
    background = Color(0xFF180812),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF27101E),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF38182B),
    onSurfaceVariant = Color(0xFFF472B6),
    surfaceContainerHigh = Color(0xFF38182B)
)

// 3. MIDNIGHT OCEAN (🌊)
private val OceanLightColors = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF075985),
    secondary = Color(0xFF0EA5E9),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFBAE6FD),
    onSecondaryContainer = Color(0xFF0369A1),
    background = Color(0xFFF0F9FF),
    onBackground = Color(0xFF075985),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF075985),
    surfaceVariant = Color(0xFFE0F2FE),
    onSurfaceVariant = Color(0xFF0284C7),
    surfaceContainerHigh = Color(0xFFE0F2FE)
)

private val OceanDarkColors = darkColorScheme(
    primary = Color(0xFF38BDF8),
    onPrimary = Color(0xFF075985),
    primaryContainer = Color(0xFF0369A1),
    onPrimaryContainer = Color(0xFFE0F2FE),
    secondary = Color(0xFF0284C7),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFF0C4A6E),
    onSecondaryContainer = Color(0xFFBAE6FD),
    background = Color(0xFF06131E),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF0B1E2F),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF142C43),
    onSurfaceVariant = Color(0xFF7DD3FC),
    surfaceContainerHigh = Color(0xFF142C43)
)

// 4. SUNSET AMBER (🌅)
private val SunsetLightColors = lightColorScheme(
    primary = Color(0xFFD97706),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFFEF3C7),
    onPrimaryContainer = Color(0xFF78350F),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color.White,
    secondaryContainer = Color(0xFFFDE68A),
    onSecondaryContainer = Color(0xFFB45309),
    background = Color(0xFFFFFBEB),
    onBackground = Color(0xFF78350F),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF78350F),
    surfaceVariant = Color(0xFFFEF3C7),
    onSurfaceVariant = Color(0xFFD97706),
    surfaceContainerHigh = Color(0xFFFEF3C7)
)

private val SunsetDarkColors = darkColorScheme(
    primary = Color(0xFFFBBF24),
    onPrimary = Color(0xFF78350F),
    primaryContainer = Color(0xFFB45309),
    onPrimaryContainer = Color(0xFFFEF3C7),
    secondary = Color(0xFFF59E0B),
    onSecondary = Color(0xFF78350F),
    secondaryContainer = Color(0xFF451A03),
    onSecondaryContainer = Color(0xFFFDE68A),
    background = Color(0xFF1A1206),
    onBackground = Color(0xFFF8FAFC),
    surface = Color(0xFF2B1D09),
    onSurface = Color(0xFFF8FAFC),
    surfaceVariant = Color(0xFF3F2B0E),
    onSurfaceVariant = Color(0xFFFCD34D),
    surfaceContainerHigh = Color(0xFF3F2B0E)
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
        "Sakura" -> if (darkTheme) SakuraDarkColors else SakuraLightColors
        "Ocean" -> if (darkTheme) OceanDarkColors else OceanLightColors
        "Sunset" -> if (darkTheme) SunsetDarkColors else SunsetLightColors
        else -> if (darkTheme) EmeraldDarkColors else EmeraldLightColors
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        shapes = RoutineFlowShapes,
        content = content
    )
}
