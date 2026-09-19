// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
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
// imports `com.waypoint.app.core.theme.RadiusRow` for use in this file
import com.waypoint.app.core.theme.RadiusRow
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard

// expression: `fun Modifier.rowSurface(`
fun Modifier.rowSurface(
    // continues the statement started above: `cornerRadius: Dp = RadiusRow,`
    cornerRadius: Dp = RadiusRow,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
// continues the statement started above: `): Modifier = this.background(color = fillColor, shape = Ro…`
): Modifier = this.background(color = fillColor, shape = RoundedCornerShape(cornerRadius))

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun RowSurface(`
fun RowSurface(
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `cornerRadius: Dp = RadiusRow,`
    cornerRadius: Dp = RadiusRow,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
    // continues the statement started above: `content: @Composable () -> Unit,`
    content: @Composable () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with arguments `(modifier = modifier.rowSurface(cornerRadius,…)` and opens a trailing lambda / block
    Box(modifier = modifier.rowSurface(cornerRadius, fillColor)) {
        // calls `content` with arguments `()`
        content()
    // closes the lambda passed to `Box`
    }
// closes the block
}
