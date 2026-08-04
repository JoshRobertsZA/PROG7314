package com.example.prog7314.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointPlaceAccent1

/**
 * Replaces bg_all_trips_thumb_1..4, bg_home_thumb_1..3,
 * bg_edit_itinerary_thumb_1..4, bg_itinerary_thumb_a..d,
 * bg_nearby_thumb_1..4 - every one of these is a flat-color rounded
 * rectangle that just cycles through waypoint_place_accent_1..4 (see
 * Color.kt), no imagery. Radius is 12dp everywhere except the all-trips
 * screen, which uses 14dp - hence the parameterized [cornerRadius].
 */
@Composable
fun ThumbnailBlock(
    modifier: Modifier = Modifier,
    accentColor: Color = WaypointPlaceAccent1,
    size: Dp = 48.dp,
    cornerRadius: Dp = RadiusThumbnail,
) {
    Box(
        modifier = modifier
            .size(size)
            .background(color = accentColor, shape = RoundedCornerShape(cornerRadius)),
    )
}
