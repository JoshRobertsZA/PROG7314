// declares that this file belongs to the package `com.waypoint.app.features.notifications.ui`
package com.waypoint.app.features.notifications.ui

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
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.heightIn` for use in this file
import androidx.compose.foundation.layout.heightIn
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
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
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusDeco` for use in this file
import com.waypoint.app.core.theme.RadiusDeco
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun NotificationHistoryModal(`
fun NotificationHistoryModal(
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: NotificationHistoryViewModel = viewModel(),`
    viewModel: NotificationHistoryViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()
    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.load() }

    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusDeco))`
            .background(WaypointCard, RoundedCornerShape(RadiusDeco))
            // continues the statement started above: `.padding(horizontal = 22.dp, vertical = 24.dp),`
            .padding(horizontal = 22.dp, vertical = 24.dp),
        // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(20.dp),`
        verticalArrangement = Arrangement.spacedBy(20.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.notifications_title),`
            text = stringResource(R.string.notifications_title),
            // continues the statement started above: `color = WaypointTextPrimary,`
            color = WaypointTextPrimary,
            // continues the statement started above: `fontSize = 22.sp,`
            fontSize = 22.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
        // closes the multi-line argument list started above
        )

        // `if` statement: the block below runs when `!uiState.isLoading && uiState.items.isEmpty()` is true
        if (!uiState.isLoading && uiState.items.isEmpty()) {
            // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth().padding(v…)` and opens a trailing lambda / block
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                // calls `NotificationsEmptyState` with arguments `()`
                NotificationsEmptyState()
            // closes the lambda passed to `Box`
            }
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.heightIn(max = 360.dp)`
                    .heightIn(max = 360.dp)
                    // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                    .verticalScroll(rememberScrollState()),
                // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(8.dp),`
                verticalArrangement = Arrangement.spacedBy(8.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // expression: `uiState.items.forEach { NotificationRow(it) }`
                uiState.items.forEach { NotificationRow(it) }
            // closes the block
            }
        // closes the else branch
        }

        // `if` statement: the block below runs when `uiState.items.isNotEmpty()` is true
        if (uiState.items.isNotEmpty()) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.height(48.dp)`
                    .height(48.dp)
                    // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusBu…`
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.clickable(onClick = viewModel::clearAll),`
                    .clickable(onClick = viewModel::clearAll),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.notifications_clear_all),`
                    text = stringResource(R.string.notifications_clear_all),
                    // continues the statement started above: `color = White,`
                    color = White,
                    // continues the statement started above: `fontSize = 17.sp,`
                    fontSize = 17.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the if block
        }
    // closes the block
    }
// closes the block
}
