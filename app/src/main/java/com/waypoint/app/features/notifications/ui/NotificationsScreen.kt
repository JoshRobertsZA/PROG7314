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
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
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
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.Icon` for use in this file
import androidx.compose.material3.Icon
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
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
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
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
    // continues the statement started above: `viewModel: NotificationHistoryViewModel = viewModel(),`
    viewModel: NotificationHistoryViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()

    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // calls `load` on `viewModel` with arguments `()`
        viewModel.load()
    // closes the lambda passed to `LaunchedEffect`
    }

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
            // continues the statement started above: `.padding(horizontal = 22.dp, vertical = 20.dp),`
            .padding(horizontal = 22.dp, vertical = 20.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.padding(bottom = 22.dp),`
                .padding(bottom = 22.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `CircleIconButton` with an argument list that continues on the following lines
            CircleIconButton(
                // continues the statement started above: `onClick = onBackClick,`
                onClick = onBackClick,
                // continues the statement started above: `modifier = Modifier.align(Alignment.CenterStart),`
                modifier = Modifier.align(Alignment.CenterStart),
                // continues the statement started above: `size = 40.dp,`
                size = 40.dp,
                // continues the statement started above: `fillColor = WaypointTerracotta,`
                fillColor = WaypointTerracotta,
                // continues the statement started above: `borderColor = null,`
                borderColor = null,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Icon` with an argument list that continues on the following lines
                Icon(
                    // continues the statement started above: `painter = painterResource(R.drawable.ic_back_arrow),`
                    painter = painterResource(R.drawable.ic_back_arrow),
                    // continues the statement started above: `contentDescription = null,`
                    contentDescription = null,
                    // continues the statement started above: `tint = White,`
                    tint = White,
                    // continues the statement started above: `modifier = Modifier.size(16.dp),`
                    modifier = Modifier.size(16.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.notifications_title),`
                text = stringResource(R.string.notifications_title),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 19.sp,`
                fontSize = 19.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                modifier = Modifier.align(Alignment.Center),
            // closes the multi-line argument list started above
            )

            // `if` statement: the block below runs when `uiState.items.isNotEmpty()` is true
            if (uiState.items.isNotEmpty()) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = "Clear All",`
                    text = "Clear All",
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.align(Alignment.CenterEnd)`
                        .align(Alignment.CenterEnd)
                        // continues the statement started above: `.clip(RoundedCornerShape(8.dp))`
                        .clip(RoundedCornerShape(8.dp))
                        // continues the statement started above: `.clickable { viewModel.clearAll() }`
                        .clickable { viewModel.clearAll() }
                        // continues the statement started above: `.padding(horizontal = 8.dp, vertical = 4.dp),`
                        .padding(horizontal = 8.dp, vertical = 4.dp),
                // closes the multi-line argument list started above
                )
            // closes the if block
            }
        // closes the block
        }

        // `if` statement: the block below runs when `uiState.isLoading` is true
        if (uiState.isLoading) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
                modifier = Modifier.fillMaxSize(),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                CircularProgressIndicator(
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `strokeWidth = 2.dp,`
                    strokeWidth = 2.dp,
                    // continues the statement started above: `modifier = Modifier.size(28.dp),`
                    modifier = Modifier.size(28.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the previous branch and opens an `else if` branch that runs when `uiState.items.isEmpty()` is true
        } else if (uiState.items.isEmpty()) {
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
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                    .verticalScroll(rememberScrollState()),
                // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(12.dp),`
                verticalArrangement = Arrangement.spacedBy(12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // expression: `uiState.items.forEach { item ->`
                uiState.items.forEach { item ->
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

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares internal function `NotificationRow` taking 2 parameters (`item`, `modifier`) and opens its body
internal fun NotificationRow(item: NotificationItem, modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(20.dp))`
            .background(WaypointCard, RoundedCornerShape(20.dp))
            // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))`
            .border(1.dp, WaypointBorderSoft, RoundedCornerShape(20.dp))
            // continues the statement started above: `.padding(16.dp),`
            .padding(16.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
            modifier = Modifier.fillMaxWidth(),
            // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
            horizontalArrangement = Arrangement.SpaceBetween,
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = item.title,`
                text = item.title,
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 14.sp,`
                fontSize = 14.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.weight(1f, fill = false),`
                modifier = Modifier.weight(1f, fill = false),
            // closes the multi-line argument list started above
            )

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = item.timestamp,`
                text = item.timestamp,
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 11.sp,`
                fontSize = 11.sp,
                // continues the statement started above: `fontWeight = FontWeight.Medium,`
                fontWeight = FontWeight.Medium,
                // continues the statement started above: `modifier = Modifier.padding(start = 6.dp),`
                modifier = Modifier.padding(start = 6.dp),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = item.body,`
            text = item.body,
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 12.sp,`
            fontSize = 12.sp,
            // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
            modifier = Modifier.padding(top = 4.dp),
        // closes the multi-line argument list started above
        )
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
        // continues the statement started above: `modifier = modifier.padding(horizontal = 24.dp),`
        modifier = modifier.padding(horizontal = 24.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
        // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(8.dp),`
        verticalArrangement = Arrangement.spacedBy(8.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = "No Notifications Yet",`
            text = "No Notifications Yet",
            // continues the statement started above: `color = WaypointTextPrimary,`
            color = WaypointTextPrimary,
            // continues the statement started above: `fontSize = 19.sp,`
            fontSize = 19.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )

        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = "We'll notify you here about upcoming trip departure…`
            text = "We'll notify you here about upcoming trip departures, live flight updates, and destination weather reminders.",
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = FontWeight.Normal,`
            fontWeight = FontWeight.Normal,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `NotificationsEmptyState`
}
