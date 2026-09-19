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
// imports `androidx.compose.foundation.layout.PaddingValues` for use in this file
import androidx.compose.foundation.layout.PaddingValues
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
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
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.text.style.TextOverflow` for use in this file
import androidx.compose.ui.text.style.TextOverflow
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun AppButtonFilled(`
fun AppButtonFilled(
    // continues the statement started above: `text: String,`
    text: String,
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `enabled: Boolean = true,`
    enabled: Boolean = true,
    // continues the statement started above: `fillColor: Color = WaypointTerracotta,`
    fillColor: Color = WaypointTerracotta,
    // continues the statement started above: `textColor: Color = White,`
    textColor: Color = White,
    // continues the statement started above: `cornerRadius: Dp = RadiusButton,`
    cornerRadius: Dp = RadiusButton,
    // continues the statement started above: `contentPadding: PaddingValues = PaddingValues(vertical = 15…`
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.then(if (enabled) Modifier.clickable(onClick = onClick) el…`
            .then(if (enabled) Modifier.clickable(onClick = onClick) else Modifier)
            // continues the statement started above: `.background(`
            .background(
                // continues the statement started above: `color = if (enabled) fillColor else fillColor.copy(alpha = …`
                color = if (enabled) fillColor else fillColor.copy(alpha = 0.4f),
                // continues the statement started above: `shape = RoundedCornerShape(cornerRadius),`
                shape = RoundedCornerShape(cornerRadius),
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.padding(contentPadding),`
            .padding(contentPadding),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(text = text, color = textColor, fontWeight =…)`
        Text(text = text, color = textColor, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun AppButtonOutline(`
fun AppButtonOutline(
    // continues the statement started above: `text: String,`
    text: String,
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `fillColor: Color = WaypointCard,`
    fillColor: Color = WaypointCard,
    // continues the statement started above: `borderColor: Color = WaypointTerracotta,`
    borderColor: Color = WaypointTerracotta,
    // continues the statement started above: `borderWidth: Dp = 1.5.dp,`
    borderWidth: Dp = 1.5.dp,
    // continues the statement started above: `textColor: Color = WaypointTerracotta,`
    textColor: Color = WaypointTerracotta,
    // continues the statement started above: `cornerRadius: Dp = RadiusButton,`
    cornerRadius: Dp = RadiusButton,
    // continues the statement started above: `contentPadding: PaddingValues = PaddingValues(vertical = 15…`
    contentPadding: PaddingValues = PaddingValues(vertical = 15.dp),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `shape`, initialised with the result of calling `RoundedCornerShape(…)`
    val shape = RoundedCornerShape(cornerRadius)
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.clickable(onClick = onClick)`
            .clickable(onClick = onClick)
            // continues the statement started above: `.background(color = fillColor, shape = shape)`
            .background(color = fillColor, shape = shape)
            // continues the statement started above: `.border(width = borderWidth, color = borderColor, shape = s…`
            .border(width = borderWidth, color = borderColor, shape = shape)
            // continues the statement started above: `.padding(contentPadding),`
            .padding(contentPadding),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(text = text, color = textColor, fontWeight =…)`
        Text(text = text, color = textColor, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, maxLines = 2, overflow = TextOverflow.Ellipsis)
    // closes the block
    }
// closes the block
}
