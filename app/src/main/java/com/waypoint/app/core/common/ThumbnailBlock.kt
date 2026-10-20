// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `coil.compose.SubcomposeAsyncImage` for use in this file
import coil.compose.SubcomposeAsyncImage
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent1` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent1

// declares function `categoryPhotoUrl` taking 1 parameter (`type`), returning `String`; its body is the expression `when (type.lowercase()) {`
fun categoryPhotoUrl(type: String): String = when (type.lowercase()) {
    // lambda `"park" -> "https://images.unsplash.com/…`
    "park" -> "https://images.unsplash.com/photo-1519331379826-f10be5486c6f?q=80&w=300&auto=format&fit=crop"
    // lambda `"cafe", "coffee" -> "https://images.unsplash.com/…`
    "cafe", "coffee" -> "https://images.unsplash.com/photo-1501339847302-ac426a4a7cbb?q=80&w=300&auto=format&fit=crop"
    // lambda `"restaurant", "food" -> "https://images.unsplash.com/…`
    "restaurant", "food" -> "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?q=80&w=300&auto=format&fit=crop"
    // lambda `"hotel", "lodging" -> "https://images.unsplash.com/…`
    "hotel", "lodging" -> "https://images.unsplash.com/photo-1566073771259-6a8506099945?q=80&w=300&auto=format&fit=crop"
    // lambda `"pub", "bar" -> "https://images.unsplash.com/…`
    "pub", "bar" -> "https://images.unsplash.com/photo-1514933651103-005eec06c04b?q=80&w=300&auto=format&fit=crop"
    // lambda `"cinema", "theater" -> "https://images.unsplash.com/…`
    "cinema", "theater" -> "https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?q=80&w=300&auto=format&fit=crop"
    // `else` branch of the `when`: evaluates `"https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=…`
    else -> "https://images.unsplash.com/photo-1488646953014-85cb44e25828?q=80&w=300&auto=format&fit=crop"
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun PlaceThumbnail(`
fun PlaceThumbnail(
    // continues the statement started above: `type: String,`
    type: String,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `accentColor: Color = WaypointPlaceAccent1,`
    accentColor: Color = WaypointPlaceAccent1,
    // continues the statement started above: `size: Dp = 48.dp,`
    size: Dp = 48.dp,
    // continues the statement started above: `cornerRadius: Dp = RadiusThumbnail,`
    cornerRadius: Dp = RadiusThumbnail,
    // continues the statement started above: `label: String = "",`
    label: String = "",
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `photoUrl`, initialised with the result of calling `categoryPhotoUrl(…)`
    val photoUrl = categoryPhotoUrl(type)
    // calls `SubcomposeAsyncImage` with an argument list that continues on the following lines
    SubcomposeAsyncImage(
        // continues the statement started above: `model = photoUrl,`
        model = photoUrl,
        // continues the statement started above: `contentDescription = type,`
        contentDescription = type,
        // continues the statement started above: `contentScale = ContentScale.Crop,`
        contentScale = ContentScale.Crop,
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.size(size)`
            .size(size)
            // continues the statement started above: `.clip(RoundedCornerShape(cornerRadius)),`
            .clip(RoundedCornerShape(cornerRadius)),
        // continues the statement started above: `error = {`
        error = {
            // calls `ThumbnailBlock` with an argument list that continues on the following lines
            ThumbnailBlock(
                // continues the statement started above: `accentColor = accentColor,`
                accentColor = accentColor,
                // continues the statement started above: `size = size,`
                size = size,
                // continues the statement started above: `cornerRadius = cornerRadius,`
                cornerRadius = cornerRadius,
                // continues the statement started above: `label = label,`
                label = label,
            // closes the multi-line argument list started above
            )
        // closes the block
        },
        // continues the statement started above: `loading = {`
        loading = {
            // calls `ThumbnailBlock` with an argument list that continues on the following lines
            ThumbnailBlock(
                // continues the statement started above: `accentColor = accentColor,`
                accentColor = accentColor,
                // continues the statement started above: `size = size,`
                size = size,
                // continues the statement started above: `cornerRadius = cornerRadius,`
                cornerRadius = cornerRadius,
                // continues the statement started above: `label = label,`
                label = label,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the multi-line argument list started above
    )
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun ThumbnailBlock(`
fun ThumbnailBlock(
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `accentColor: Color = WaypointPlaceAccent1,`
    accentColor: Color = WaypointPlaceAccent1,
    // continues the statement started above: `size: Dp = 48.dp,`
    size: Dp = 48.dp,
    // continues the statement started above: `cornerRadius: Dp = RadiusThumbnail,`
    cornerRadius: Dp = RadiusThumbnail,
    // continues the statement started above: `label: String = "",`
    label: String = "",
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.size(size)`
            .size(size)
            // continues the statement started above: `.background(color = accentColor, shape = RoundedCornerShape…`
            .background(color = accentColor, shape = RoundedCornerShape(cornerRadius)),
    // ends the argument list started above and opens the block that follows
    ) {
        // `if` statement: the block below runs when `label.isNotEmpty()` is true
        if (label.isNotEmpty()) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = label,`
                text = label,
                // continues the statement started above: `fontSize = (size.value * 0.42f).sp,`
                fontSize = (size.value * 0.42f).sp,
            // closes the multi-line argument list started above
            )
        // closes the if block
        }
    // closes the block
    }
// closes the block
}
