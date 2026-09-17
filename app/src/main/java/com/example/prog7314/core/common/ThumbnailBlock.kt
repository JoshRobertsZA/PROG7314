package com.example.prog7314.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointPlaceAccent1

/**
 * Flat-color rounded thumbnail block used across all trip/place card lists.
 * Pass [label] (e.g. an emoji) to display a centred icon inside the block.
 * Callers that omit [label] get the original plain-color behaviour.
 */
@Composable
fun ThumbnailBlock(
    modifier: Modifier = Modifier,
    accentColor: Color = WaypointPlaceAccent1,
    size: Dp = 48.dp,
    cornerRadius: Dp = RadiusThumbnail,
    label: String = "",
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(size)
            .background(color = accentColor, shape = RoundedCornerShape(cornerRadius)),
    ) {
        if (label.isNotEmpty()) {
            Text(
                text = label,
                fontSize = (size.value * 0.42f).sp,
            )
        }
    }
}
