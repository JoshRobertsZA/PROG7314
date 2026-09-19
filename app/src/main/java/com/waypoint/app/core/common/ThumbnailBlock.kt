package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.SubcomposeAsyncImage
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointPlaceAccent1

fun categoryPhotoUrl(type: String): String = when (type.lowercase()) {
    "park" -> "https://images.unsplash.com/photo-1519331379826-f10be5486c6f?q=80&w=300&auto=format&fit=crop"
    "cafe", "coffee" -> "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?q=80&w=300&auto=format&fit=crop"
    "restaurant", "food" -> "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=300&auto=format&fit=crop"
    "hotel", "lodging" -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=300&auto=format&fit=crop"
    "pub", "bar" -> "https://images.unsplash.com/photo-1514933651103-005eec06c04b?q=80&w=300&auto=format&fit=crop"
    "cinema", "theater" -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=300&auto=format&fit=crop"
    else -> "https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=300&auto=format&fit=crop"
}

@Composable
fun PlaceThumbnail(
    type: String,
    modifier: Modifier = Modifier,
    accentColor: Color = WaypointPlaceAccent1,
    size: Dp = 48.dp,
    cornerRadius: Dp = RadiusThumbnail,
    label: String = "",
) {
    val photoUrl = categoryPhotoUrl(type)
    SubcomposeAsyncImage(
        model = photoUrl,
        contentDescription = type,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(cornerRadius)),
        error = {
            ThumbnailBlock(
                accentColor = accentColor,
                size = size,
                cornerRadius = cornerRadius,
                label = label,
            )
        },
        loading = {
            ThumbnailBlock(
                accentColor = accentColor,
                size = size,
                cornerRadius = cornerRadius,
                label = label,
            )
        }
    )
}

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
