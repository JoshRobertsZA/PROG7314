package com.example.prog7314.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.White

/**
 * Filled variant. Replaces bg_home_button_filled, bg_calendar_button_filled
 * and bg_new_trip_save_button - all three are the same solid-terracotta,
 * 14dp-corner shape.
 */
@Composable
fun AppButtonFilled(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    fillColor: Color = WaypointTerracotta,
    textColor: Color = White,
    cornerRadius: Dp = RadiusButton,
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            .background(
                color = if (enabled) fillColor else fillColor.copy(alpha = 0.4f),
                shape = RoundedCornerShape(cornerRadius),
            )
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = textColor, fontWeight = FontWeight.Bold)
    }
}

/**
 * Outline variant. Replaces bg_home_button_outline, bg_calendar_button_outline
 * (card fill + 1.5dp terracotta stroke) and bg_google_button (white fill +
 * 1dp waypoint_border stroke) - same shape family, different colors/stroke
 * widths per screen, hence the parameters.
 */
@Composable
fun AppButtonOutline(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    fillColor: Color = WaypointCard,
    borderColor: Color = WaypointTerracotta,
    borderWidth: Dp = 1.5.dp,
    textColor: Color = WaypointTerracotta,
    cornerRadius: Dp = RadiusButton,
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
) {
    val shape = RoundedCornerShape(cornerRadius)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .background(color = fillColor, shape = shape)
            .border(width = borderWidth, color = borderColor, shape = shape)
            .padding(contentPadding),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = text, color = textColor, fontWeight = FontWeight.Bold)
    }
}
