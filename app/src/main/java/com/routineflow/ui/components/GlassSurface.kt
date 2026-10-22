package com.routineflow.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

enum class GlassRenderMode {
    Full,
    Simplified
}

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 20.dp,
    tint: Color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
    highlighted: Boolean = true,
    elevation: Dp = 10.dp,
    renderMode: GlassRenderMode = GlassRenderMode.Full,
    content: @Composable () -> Unit
) {
    val shape = RoundedCornerShape(cornerRadius)
    val onSurfaceColor = MaterialTheme.colorScheme.onSurface
    val primaryColor = MaterialTheme.colorScheme.primary

    // Vertical gradient for glossy liquid glass sheen
    val glassFill = if (highlighted) {
        Brush.verticalGradient(
            colors = listOf(
                tint.copy(alpha = (tint.alpha + 0.14f).coerceAtMost(0.92f)),
                tint.copy(alpha = (tint.alpha - 0.08f).coerceAtLeast(0.40f)),
                tint
            )
        )
    } else {
        Brush.verticalGradient(listOf(tint, tint))
    }

    // Specular border gradient for glossy light reflection along the edges
    val glassBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = if (renderMode == GlassRenderMode.Full) 0.45f else 0.28f),
            onSurfaceColor.copy(alpha = 0.08f),
            primaryColor.copy(alpha = 0.18f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = if (highlighted && renderMode == GlassRenderMode.Full) {
                    elevation
                } else if (highlighted) {
                    (elevation / 2).coerceAtMost(4.dp)
                } else {
                    0.dp
                },
                shape = shape,
                ambientColor = primaryColor.copy(alpha = 0.20f),
                spotColor = Color.Black.copy(alpha = 0.12f)
            )
            .clip(shape)
            .background(glassFill)
            .border(
                width = 1.dp,
                brush = glassBorder,
                shape = shape
            )
    ) {
        content()
    }
}
