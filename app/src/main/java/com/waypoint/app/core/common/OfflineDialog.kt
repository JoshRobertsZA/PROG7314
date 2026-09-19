// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.Canvas` for use in this file
import androidx.compose.foundation.Canvas
// imports `androidx.compose.foundation.Image` for use in this file
import androidx.compose.foundation.Image
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.shadow` for use in this file
import androidx.compose.ui.draw.shadow
// imports `androidx.compose.ui.graphics.Path` for use in this file
import androidx.compose.ui.graphics.Path
// imports `androidx.compose.ui.platform.LocalDensity` for use in this file
import androidx.compose.ui.platform.LocalDensity
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.IntOffset` for use in this file
import androidx.compose.ui.unit.IntOffset
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `androidx.compose.ui.window.DialogProperties` for use in this file
import androidx.compose.ui.window.DialogProperties
// imports `androidx.compose.ui.window.Popup` for use in this file
import androidx.compose.ui.window.Popup
// imports `androidx.compose.ui.window.PopupProperties` for use in this file
import androidx.compose.ui.window.PopupProperties
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusChip` for use in this file
import com.waypoint.app.core.theme.RadiusChip
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointModalScrim` for use in this file
import com.waypoint.app.core.theme.WaypointModalScrim
// imports `com.waypoint.app.core.theme.WaypointOfflineTitle` for use in this file
import com.waypoint.app.core.theme.WaypointOfflineTitle
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `OfflineDialog` taking 1 parameter (`onDismissRequest`) and opens its body
fun OfflineDialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Dialog` with an argument list that continues on the following lines
    Dialog(
        // continues the statement started above: `onDismissRequest = onDismissRequest,`
        onDismissRequest = onDismissRequest,
        // continues the statement started above: `properties = DialogProperties(usePlatformDefaultWidth = fal…`
        properties = DialogProperties(usePlatformDefaultWidth = false),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = modifier`
            modifier = modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.background(WaypointModalScrim)`
                .background(WaypointModalScrim)
                // continues the statement started above: `.padding(horizontal = 35.dp),`
                .padding(horizontal = 35.dp),
            // continues the statement started above: `contentAlignment = Alignment.Center,`
            contentAlignment = Alignment.Center,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.shadow(elevation = 8.dp, shape = RoundedCornerShape(Radius…`
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusChip))
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusChip))`
                    .background(WaypointCard, RoundedCornerShape(RadiusChip))
                    // continues the statement started above: `.padding(horizontal = 24.dp, vertical = 28.dp),`
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
                // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(16.dp),`
                verticalArrangement = Arrangement.spacedBy(16.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.size(56.dp)`
                        .size(56.dp)
                        // continues the statement started above: `.background(WaypointTerracotta, CircleShape),`
                        .background(WaypointTerracotta, CircleShape),
                    // continues the statement started above: `contentAlignment = Alignment.Center,`
                    contentAlignment = Alignment.Center,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with arguments `(text = "!", color = White, fontSize = 26.sp,…)`
                    Text(text = "!", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                // closes the block
                }

                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.offline_dialog_title),`
                    text = stringResource(R.string.offline_dialog_title),
                    // continues the statement started above: `color = WaypointOfflineTitle,`
                    color = WaypointOfflineTitle,
                    // continues the statement started above: `fontSize = 18.sp,`
                    fontSize = 18.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `textAlign = TextAlign.Center,`
                    textAlign = TextAlign.Center,
                // closes the multi-line argument list started above
                )

                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.offline_dialog_body),`
                    text = stringResource(R.string.offline_dialog_body),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 14.sp,`
                    fontSize = 14.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Medium,`
                    fontWeight = FontWeight.Medium,
                    // continues the statement started above: `textAlign = TextAlign.Center,`
                    textAlign = TextAlign.Center,
                // closes the multi-line argument list started above
                )

                // calls `OkayButton` with arguments `(onClick = onDismissRequest)`
                OkayButton(onClick = onDismissRequest)
            // closes the block
            }
        // closes the block
        }
    // closes the block
    }
// closes the function `OfflineDialog`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `StillOfflineBubble` taking 1 parameter (`onOkayClick`) and opens its body
fun StillOfflineBubble(onOkayClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.width(260.dp)`
            .width(260.dp)
            // continues the statement started above: `.shadow(elevation = 8.dp, shape = RoundedCornerShape(Radius…`
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusChip))
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusChip))`
            .background(WaypointCard, RoundedCornerShape(RadiusChip))
            // continues the statement started above: `.padding(horizontal = 22.dp, vertical = 24.dp),`
            .padding(horizontal = 22.dp, vertical = 24.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
        // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(16.dp),`
        verticalArrangement = Arrangement.spacedBy(16.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.offline_bubble_body),`
            text = stringResource(R.string.offline_bubble_body),
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 14.sp,`
            fontSize = 14.sp,
            // continues the statement started above: `fontWeight = FontWeight.Medium,`
            fontWeight = FontWeight.Medium,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )
        // calls `OkayButton` with arguments `(onClick = onOkayClick)`
        OkayButton(onClick = onOkayClick)
    // closes the block
    }
// closes the function `StillOfflineBubble`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `OfflineHeaderIndicator` taking 3 parameters (`isOnline`, `modifier`, `iconSize`) and opens its body
fun OfflineHeaderIndicator(isOnline: Boolean, modifier: Modifier = Modifier, iconSize: Dp = 40.dp) {
    // declares mutable property `showBubble`, delegated to `remember { mutableStateOf(false) }`
    var showBubble by remember { mutableStateOf(false) }
    // calls `LaunchedEffect` with arguments `(isOnline)`
    LaunchedEffect(isOnline) { if (isOnline) showBubble = false }

    // `if` statement: executes `return` when `isOnline` is true
    if (isOnline) return

    // declares read-only property `density`, initialised to `LocalDensity.current`
    val density = LocalDensity.current

    // calls `Box` with arguments `(modifier = modifier)` and opens a trailing lambda / block
    Box(modifier = modifier) {
        // calls `Image` with an argument list that continues on the following lines
        Image(
            // continues the statement started above: `painter = painterResource(R.drawable.ic_no_wifi),`
            painter = painterResource(R.drawable.ic_no_wifi),
            // continues the statement started above: `contentDescription = stringResource(R.string.tab_header_off…`
            contentDescription = stringResource(R.string.tab_header_offline_cd),
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.size(iconSize)`
                .size(iconSize)
                // continues the statement started above: `.clickable { showBubble = !showBubble },`
                .clickable { showBubble = !showBubble },
        // closes the multi-line argument list started above
        )

        // `if` statement: the block below runs when `showBubble` is true
        if (showBubble) {
            // calls `Popup` with an argument list that continues on the following lines
            Popup(
                // continues the statement started above: `alignment = Alignment.TopEnd,`
                alignment = Alignment.TopEnd,
                // continues the statement started above: `offset = IntOffset(0, with(density) { (iconSize + 6.dp).rou…`
                offset = IntOffset(0, with(density) { (iconSize + 6.dp).roundToPx() }),
                // continues the statement started above: `onDismissRequest = { showBubble = false },`
                onDismissRequest = { showBubble = false },
                // continues the statement started above: `properties = PopupProperties(focusable = true),`
                properties = PopupProperties(focusable = true),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Column` with arguments `(horizontalAlignment = Alignment.End)` and opens a trailing lambda / block
                Column(horizontalAlignment = Alignment.End) {
                    // calls `BubbleTail` with arguments `(modifier = Modifier.padding(end = 8.dp))`
                    BubbleTail(modifier = Modifier.padding(end = 8.dp))
                    // calls `StillOfflineBubble` with arguments `(onOkayClick = { showBubble = false })`
                    StillOfflineBubble(onOkayClick = { showBubble = false })
                // closes the lambda passed to `Column`
                }
            // closes the block
            }
        // closes the if block
        }
    // closes the lambda passed to `Box`
    }
// closes the function `OfflineHeaderIndicator`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `BubbleTail` taking 1 parameter (`modifier`) and opens its body
private fun BubbleTail(modifier: Modifier = Modifier) {
    // calls `Canvas` with arguments `(modifier = modifier.size(width = 20.dp, heig…)` and opens a trailing lambda / block
    Canvas(modifier = modifier.size(width = 20.dp, height = 10.dp)) {
        // declares read-only property `path`, initialised with the result of calling `Path(…)` and opens a lambda / block
        val path = Path().apply {
            // calls `moveTo` with arguments `(size.width / 2f, 0f)`
            moveTo(size.width / 2f, 0f)
            // calls `lineTo` with arguments `(size.width, size.height)`
            lineTo(size.width, size.height)
            // calls `lineTo` with arguments `(0f, size.height)`
            lineTo(0f, size.height)
            // calls `close` with arguments `()`
            close()
        // closes the lambda assigned to `path`
        }
        // calls `drawPath` with arguments `(path, color = WaypointCard)`
        drawPath(path, color = WaypointCard)
    // closes the lambda passed to `Canvas`
    }
// closes the function `BubbleTail`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `OkayButton` taking 1 parameter (`onClick`) and opens its body
private fun OkayButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusBu…`
            .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.clickable(onClick = onClick)`
            .clickable(onClick = onClick)
            // continues the statement started above: `.padding(vertical = 14.dp),`
            .padding(vertical = 14.dp),
        // continues the statement started above: `horizontalArrangement = Arrangement.Center,`
        horizontalArrangement = Arrangement.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.offline_dialog_okay),`
            text = stringResource(R.string.offline_dialog_okay),
            // continues the statement started above: `color = White,`
            color = White,
            // continues the statement started above: `fontSize = 14.sp,`
            fontSize = 14.sp,
            // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
            fontWeight = FontWeight.SemiBold,
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `OkayButton`
}
