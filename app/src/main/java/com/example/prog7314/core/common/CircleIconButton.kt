package com.example.prog7314.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointTerracotta

/**
 * Replaces the back-button family (bg_all_trips_back_button,
 * bg_calendar_back_button, bg_edit_itinerary_back_button,
 * bg_itinerary_back_button, bg_new_trip_close_button, bg_nearby_back_button,
 * bg_settings_back_button - all pixel-identical: card fill, 1.2dp
 * terracotta stroke, oval) plus bg_waypoint_logo_circle (solid terracotta,
 * no stroke) and bg_google_g_badge (white fill, 1dp waypoint_border
 * stroke) by making fill/border/size parameters instead of separate files.
 *
 * Pass [borderColor] = null for the no-stroke logo-circle case.
 */
@Composable
fun CircleIconButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    size: Dp = 36.dp,
    fillColor: Color = WaypointCard,
    borderColor: Color? = WaypointTerracotta,
    borderWidth: Dp = 1.2.dp,
    content: @Composable () -> Unit,
) {
    var box: Modifier = modifier
        .size(size)
        .clickable(onClick = onClick)
        .background(color = fillColor, shape = CircleShape)
    if (borderColor != null) {
        box = box.border(width = borderWidth, color = borderColor, shape = CircleShape)
    }
    Box(modifier = box, contentAlignment = Alignment.Center) {
        content()
    }
}
