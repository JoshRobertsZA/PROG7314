package com.example.prog7314.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointCard

/**
 * Replaces bg_home_row, bg_edit_itinerary_row, bg_itinerary_row,
 * bg_nearby_place_row - all four are pixel-identical (cream fill, 16dp
 * corners, no border), so this needs no per-screen overrides beyond the
 * defaults, but still exposes them for consistency with the other
 * core/common components.
 */
fun Modifier.rowSurface(
    cornerRadius: Dp = RadiusRow,
    fillColor: Color = WaypointCard,
): Modifier = this.background(color = fillColor, shape = RoundedCornerShape(cornerRadius))

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
