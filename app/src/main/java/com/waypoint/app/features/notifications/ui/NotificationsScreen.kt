// declares that this file belongs to the package `com.waypoint.app.features.notifications.ui`
package com.waypoint.app.features.notifications.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.theme.RadiusChip` for use in this file
import com.waypoint.app.core.theme.RadiusChip
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointNotifRowBorder` for use in this file
import com.waypoint.app.core.theme.WaypointNotifRowBorder
// imports `com.waypoint.app.core.theme.WaypointOfflineTitle` for use in this file
import com.waypoint.app.core.theme.WaypointOfflineTitle
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun NotificationsScreen(`
fun NotificationsScreen(
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `notifications: List<NotificationItem> = mockNotifications,`
    notifications: List<NotificationItem> = mockNotifications,
    // continues the statement started above: `onMarkAllReadClick: () -> Unit = {},`
    onMarkAllReadClick: () -> Unit = {},
// ends the argument list started above and opens the block that follows
) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
            .windowInsetsPadding(WindowInsets.systemBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = …`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 22.dp),
    // ends the argument list started above and opens the block that follows
    ) {

        // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
        Box(modifier = Modifier.fillMaxWidth()) {
            // calls `CircleIconButton` with arguments `(onClick = onBackClick, modifier = Modifier.a…)` and opens a trailing lambda / block
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.notifications_back_glyph),`
                    text = stringResource(R.string.notifications_back_glyph),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `CircleIconButton`
            }
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.notifications_title),`
                text = stringResource(R.string.notifications_title),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 17.sp,`
                fontSize = 17.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.align(if (notifications.isEmpty()) Alig…`
                modifier = Modifier.align(if (notifications.isEmpty()) Alignment.CenterStart else Alignment.Center)
                    // continues the statement started above: `.padding(start = if (notifications.isEmpty()) 48.dp else 0.…`
                    .padding(start = if (notifications.isEmpty()) 48.dp else 0.dp),
            // closes the multi-line argument list started above
            )
            // `if` statement: the block below runs when `notifications.isNotEmpty()` is true
            if (notifications.isNotEmpty()) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.notifications_mark_all_read),`
                    text = stringResource(R.string.notifications_mark_all_read),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.align(Alignment.CenterEnd)`
                        .align(Alignment.CenterEnd)
                        // continues the statement started above: `.clickable(onClick = onMarkAllReadClick),`
                        .clickable(onClick = onMarkAllReadClick),
                // closes the multi-line argument list started above
                )
            // closes the if block
            }
        // closes the lambda passed to `Box`
        }

        // `if` statement: the block below runs when `notifications.isEmpty()` is true
        if (notifications.isEmpty()) {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
                modifier = Modifier.fillMaxSize(),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
                // continues the statement started above: `verticalArrangement = Arrangement.Center,`
                verticalArrangement = Arrangement.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `NotificationsEmptyState` with arguments `()`
                NotificationsEmptyState()
            // closes the block
            }
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 24.dp)`
                    .padding(top = 24.dp)
                    // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                    .verticalScroll(rememberScrollState()),
                // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(8.dp),`
                verticalArrangement = Arrangement.spacedBy(8.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // expression: `notifications.forEach { item ->`
                notifications.forEach { item ->
                    // continues the statement started above: `NotificationRow(item)`
                    NotificationRow(item)
                // closes the block
                }
            // closes the block
            }
        // closes the else branch
        }
    // closes the block
    }
// closes the block
}

// expression: `data class NotificationItem(`
data class NotificationItem(
    // continues the statement started above: `val title: String,`
    val title: String,
    // continues the statement started above: `val body: String,`
    val body: String,
    // continues the statement started above: `val timestamp: String,`
    val timestamp: String,
// closes the multi-line argument list started above
)

// declares private read-only property `mockNotifications`, initialised with the result of calling `listOf(…)`
private val mockNotifications = listOf(
    // continues the statement started above: `NotificationItem("Flight reminder", "CPT → JNB departs in 3…`
    NotificationItem("Flight reminder", "CPT → JNB departs in 3 hours", "9:40 AM"),
    // continues the statement started above: `NotificationItem("Weather alert", "Rain expected in Cape To…`
    NotificationItem("Weather alert", "Rain expected in Cape Town tomorrow", "Aug 4"),
    // continues the statement started above: `NotificationItem("Trip reminder", "Cape Town Getaway starts…`
    NotificationItem("Trip reminder", "Cape Town Getaway starts in 8 days", "2h ago"),
    // continues the statement started above: `NotificationItem("Price drop", "Flights to Zanzibar are now…`
    NotificationItem("Price drop", "Flights to Zanzibar are now cheaper", "Yesterday"),
    // continues the statement started above: `NotificationItem("Booking confirmed", "Test Valley Boutique…`
    NotificationItem("Booking confirmed", "Test Valley Boutique Hotel confirmed", "2 days ago"),
    // continues the statement started above: `NotificationItem("Car rental confirmed", "Avis Compact Car …`
    NotificationItem("Car rental confirmed", "Avis Compact Car booked for Aug 3–5", "2 days ago"),
    // continues the statement started above: `NotificationItem("Exchange rate alert", "ZAR strengthened a…`
    NotificationItem("Exchange rate alert", "ZAR strengthened against USD this week", "3 days ago"),
    // continues the statement started above: `NotificationItem("Trip completed", "How was Lisbon Long Wee…`
    NotificationItem("Trip completed", "How was Lisbon Long Weekend? Leave a note", "1 week ago"),
    // continues the statement started above: `NotificationItem("New feature", "Offline mode is now availa…`
    NotificationItem("New feature", "Offline mode is now available in Settings", "1 week ago"),
// closes the multi-line argument list started above
)

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares internal function `NotificationRow` taking 2 parameters (`item`, `modifier`) and opens its body
internal fun NotificationRow(item: NotificationItem, modifier: Modifier = Modifier) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.height(72.dp)`
            .height(72.dp)
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusChip))`
            .background(WaypointCard, RoundedCornerShape(RadiusChip))
            // continues the statement started above: `.border(1.dp, WaypointNotifRowBorder, RoundedCornerShape(Ra…`
            .border(1.dp, WaypointNotifRowBorder, RoundedCornerShape(RadiusChip))
            // continues the statement started above: `.padding(horizontal = 16.dp, vertical = 13.dp),`
            .padding(horizontal = 16.dp, vertical = 13.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = item.timestamp,`
            text = item.timestamp,
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 10.sp,`
            fontSize = 10.sp,
            // continues the statement started above: `modifier = Modifier.align(Alignment.TopEnd),`
            modifier = Modifier.align(Alignment.TopEnd),
        // closes the multi-line argument list started above
        )
        // calls `Column` with arguments `(modifier = Modifier.align(Alignment.CenterSt…)` and opens a trailing lambda / block
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = item.title,`
                text = item.title,
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 13.sp,`
                fontSize = 13.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = item.body,`
                text = item.body,
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 11.sp,`
                fontSize = 11.sp,
                // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
                modifier = Modifier.padding(top = 4.dp),
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Column`
        }
    // closes the block
    }
// closes the function `NotificationRow`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares internal function `NotificationsEmptyState` taking 1 parameter (`modifier`) and opens its body
internal fun NotificationsEmptyState(modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier,`
        modifier = modifier,
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
        // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(12.dp),`
        verticalArrangement = Arrangement.spacedBy(12.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.size(60.dp)`
                .size(60.dp)
                // continues the statement started above: `.background(WaypointCard, CircleShape),`
                .background(WaypointCard, CircleShape),
            // continues the statement started above: `contentAlignment = Alignment.Center,`
            contentAlignment = Alignment.Center,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "✓",`
                text = "✓",
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `fontSize = 22.sp,`
                fontSize = 22.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.notifications_empty_title),`
            text = stringResource(R.string.notifications_empty_title),
            // continues the statement started above: `color = WaypointOfflineTitle,`
            color = WaypointOfflineTitle,
            // continues the statement started above: `fontSize = 16.sp,`
            fontSize = 16.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.notifications_empty_subtitle…`
            text = stringResource(R.string.notifications_empty_subtitle),
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = FontWeight.Medium,`
            fontWeight = FontWeight.Medium,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `NotificationsEmptyState`
}
