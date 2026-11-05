package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import com.waypoint.app.core.theme.RadiusChip
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta

/** Small pill label, e.g. a trip's "In 5 days" badge or a section's "+ Add" chip. */
@Composable
fun StatusBadge(
    text: String,
    modifier: Modifier = Modifier,
    fillColor: Color? = WaypointCard,
    borderColor: Color? = null,
    textColor: Color = WaypointTerracotta,
    cornerRadius: Dp = RadiusChip,
    bold: Boolean = true,
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
) {
    val shape = RoundedCornerShape(cornerRadius)
    var box: Modifier = modifier
    if (fillColor != null) {
        box = box.background(color = fillColor, shape = shape)
    }
    if (borderColor != null) {
        box = box.border(width = 1.dp, color = borderColor, shape = shape)
    }
    Box(modifier = box.padding(contentPadding), contentAlignment = Alignment.Center) {
        Text(
            text = text,
            color = textColor,
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
