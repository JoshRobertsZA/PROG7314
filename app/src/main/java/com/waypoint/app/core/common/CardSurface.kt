// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard

// expression: `fun Modifier.cardSurface(`
fun Modifier.cardSurface(
    // continues the statement started above: `cornerRadius: Dp = RadiusButton,`
    cornerRadius: Dp = RadiusButton,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
    // continues the statement started above: `borderColor: Color = WaypointBorderSoft,`
    borderColor: Color = WaypointBorderSoft,
// continues the statement started above: `): Modifier {`
): Modifier {
    // declares read-only property `shape`, initialised with the result of calling `RoundedCornerShape(…)`
    val shape = RoundedCornerShape(cornerRadius)
    // returns `this` from the current function
    return this
        // chained call `.background` on the previous result with arguments `(color = fillColor, shape = shape)`
        .background(color = fillColor, shape = shape)
        // chained call `.border` on the previous result with arguments `(width = 1.dp, color = borderColor, shap…)`
        .border(width = 1.dp, color = borderColor, shape = shape)
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun CardSurface(`
fun CardSurface(
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `cornerRadius: Dp = RadiusButton,`
    cornerRadius: Dp = RadiusButton,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
    // continues the statement started above: `borderColor: Color = WaypointBorderSoft,`
    borderColor: Color = WaypointBorderSoft,
    // continues the statement started above: `content: @Composable () -> Unit,`
    content: @Composable () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with arguments `(modifier = modifier.cardSurface(cornerRadius…)` and opens a trailing lambda / block
    Box(modifier = modifier.cardSurface(cornerRadius, fillColor, borderColor)) {
        // calls `content` with arguments `()`
        content()
    // closes the lambda passed to `Box`
    }
// closes the block
}
