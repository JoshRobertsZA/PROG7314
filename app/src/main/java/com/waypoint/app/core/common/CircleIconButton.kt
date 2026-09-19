// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
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
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun CircleIconButton(`
fun CircleIconButton(
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `size: Dp = 36.dp,`
    size: Dp = 36.dp,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
    // continues the statement started above: `borderColor: Color? = WaypointTerracotta,`
    borderColor: Color? = WaypointTerracotta,
    // continues the statement started above: `borderWidth: Dp = 1.2.dp,`
    borderWidth: Dp = 1.2.dp,
    // continues the statement started above: `content: @Composable () -> Unit,`
    content: @Composable () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `box` of type `Modifier`, initialised to `modifier`
    var box: Modifier = modifier
        // chained call `.size` on the previous result with arguments `(size)`
        .size(size)
        // chained call `.clickable` on the previous result with arguments `(onClick = onClick)`
        .clickable(onClick = onClick)
        // chained call `.background` on the previous result with arguments `(color = fillColor, shape = CircleShape)`
        .background(color = fillColor, shape = CircleShape)
    // `if` statement: the block below runs when `borderColor != null` is true
    if (borderColor != null) {
        // assigns `box` the value `box.border(width = borderWidth, color = borderCol…`
        box = box.border(width = borderWidth, color = borderColor, shape = CircleShape)
    // closes the if block
    }
    // calls `Box` with arguments `(modifier = box, contentAlignment = Alignment…)` and opens a trailing lambda / block
    Box(modifier = box, contentAlignment = Alignment.Center) {
        // calls `content` with arguments `()`
        content()
    // closes the lambda passed to `Box`
    }
// closes the block
}
