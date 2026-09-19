// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.PaddingValues` for use in this file
import androidx.compose.foundation.layout.PaddingValues
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
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
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `com.waypoint.app.core.theme.RadiusChip` for use in this file
import com.waypoint.app.core.theme.RadiusChip
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun StatusBadge(`
fun StatusBadge(
    // continues the statement started above: `text: String,`
    text: String,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `fillColor: Color? = WaypointCard,`
    fillColor: Color? = WaypointCard,
    // continues the statement started above: `borderColor: Color? = null,`
    borderColor: Color? = null,
    // continues the statement started above: `textColor: Color = WaypointTerracotta,`
    textColor: Color = WaypointTerracotta,
    // continues the statement started above: `cornerRadius: Dp = RadiusChip,`
    cornerRadius: Dp = RadiusChip,
    // continues the statement started above: `bold: Boolean = true,`
    bold: Boolean = true,
    // continues the statement started above: `contentPadding: PaddingValues = PaddingValues(horizontal = …`
    contentPadding: PaddingValues = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `shape`, initialised with the result of calling `RoundedCornerShape(…)`
    val shape = RoundedCornerShape(cornerRadius)
    // declares mutable property `box` of type `Modifier`, initialised to `modifier`
    var box: Modifier = modifier
    // `if` statement: the block below runs when `fillColor != null` is true
    if (fillColor != null) {
        // assigns `box` the value `box.background(color = fillColor, shape = shape)`
        box = box.background(color = fillColor, shape = shape)
    // closes the if block
    }
    // `if` statement: the block below runs when `borderColor != null` is true
    if (borderColor != null) {
        // assigns `box` the value `box.border(width = 1.dp, color = borderColor, sha…`
        box = box.border(width = 1.dp, color = borderColor, shape = shape)
    // closes the if block
    }
    // calls `Box` with arguments `(modifier = box.padding(contentPadding), cont…)` and opens a trailing lambda / block
    Box(modifier = box.padding(contentPadding), contentAlignment = Alignment.Center) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = text,`
            text = text,
            // continues the statement started above: `color = textColor,`
            color = textColor,
            // continues the statement started above: `fontWeight = if (bold) FontWeight.Bold else FontWeight.Norm…`
            fontWeight = if (bold) FontWeight.Bold else FontWeight.Normal,
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the block
}
