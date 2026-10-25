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
                Color.White.copy(alpha = if (renderMode == GlassRenderMode.Full) 0.35f else 0.20f),
                tint.copy(alpha = (tint.alpha * 0.90f).coerceIn(0.22f, 0.88f)),
                tint.copy(alpha = (tint.alpha * 0.70f).coerceIn(0.18f, 0.74f))
            )
        )
    } else {
        Brush.verticalGradient(listOf(tint, tint))
    }

    // Specular border gradient for glossy light reflection along the edges
    val glassBorder = Brush.linearGradient(
        colors = listOf(
            Color.White.copy(alpha = if (renderMode == GlassRenderMode.Full) 0.75f else 0.40f),
            onSurfaceColor.copy(alpha = 0.15f),
            primaryColor.copy(alpha = 0.35f)
        )
    )

    Box(
        modifier = modifier
            .shadow(
                elevation = if (highlighted && renderMode == GlassRenderMode.Full) {
                    elevation.coerceAtMost(8.dp)
                } else if (highlighted) {
                    (elevation / 2).coerceAtMost(4.dp)
                } else {
                    0.dp
                },
                shape = shape,
                ambientColor = primaryColor.copy(alpha = 0.14f),
                spotColor = Color.Black.copy(alpha = 0.10f)
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
