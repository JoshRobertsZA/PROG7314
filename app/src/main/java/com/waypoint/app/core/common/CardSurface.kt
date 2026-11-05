package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard

/** Background and border used by every card in the app. */
fun Modifier.cardSurface(
    cornerRadius: Dp = RadiusButton,
    fillColor: Color = WaypointCard,
    borderColor: Color = WaypointBorderSoft,
): Modifier {
    val shape = RoundedCornerShape(cornerRadius)
    return this
        .background(color = fillColor, shape = shape)
        .border(width = 1.dp, color = borderColor, shape = shape)
}

/** A [Box] drawn with [cardSurface]. */
@Composable
fun CardSurface(
    modifier: Modifier = Modifier,
    cornerRadius: Dp = RadiusButton,
    fillColor: Color = WaypointCard,
    borderColor: Color = WaypointBorderSoft,
    content: @Composable () -> Unit,
) {
    Box(modifier = modifier.cardSurface(cornerRadius, fillColor, borderColor)) {
        content()
    }
}
