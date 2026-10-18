package com.routineflow.ui.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun GlassSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = 16.dp,
    tint: Color = Color.Unspecified,
    highlighted: Boolean = true,
    elevation: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    ElevatedCard(
        modifier = modifier,
        shape = RoundedCornerShape(cornerRadius),
        colors = if (tint != Color.Unspecified) {
            CardDefaults.elevatedCardColors(containerColor = tint)
        } else {
            CardDefaults.elevatedCardColors()
        },
        elevation = CardDefaults.elevatedCardElevation(
            defaultElevation = if (highlighted) elevation else 0.dp
        )
    ) {
        content()
    }
}
