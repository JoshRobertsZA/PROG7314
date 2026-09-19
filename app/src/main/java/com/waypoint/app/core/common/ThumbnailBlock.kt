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
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent1` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent1

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
