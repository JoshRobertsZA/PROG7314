package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.waypoint.app.core.theme.RadiusRow
import com.waypoint.app.core.theme.WaypointCard

/** Rounded background used for list rows. */
fun Modifier.rowSurface(
    cornerRadius: Dp = RadiusRow,
    fillColor: Color = WaypointCard,
): Modifier = this.background(color = fillColor, shape = RoundedCornerShape(cornerRadius))

/** A [Box] drawn with [rowSurface]. */
@Composable
fun RowSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = RadiusRow,
    fillColor: Color = WaypointCard,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.rowSurface(cornerRadius, fillColor)) {
        content()
    }
}
