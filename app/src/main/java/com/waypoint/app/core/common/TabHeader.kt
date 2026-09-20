// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.material3.Icon` for use in this file
import androidx.compose.material3.Icon
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
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
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `coil.compose.AsyncImage` for use in this file
import coil.compose.AsyncImage
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.features.notifications.ui.NotificationHistoryModal` for use in this file
import com.waypoint.app.features.notifications.ui.NotificationHistoryModal
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.core.connectivity.rememberIsOnline` for use in this file
import com.waypoint.app.core.connectivity.rememberIsOnline
// imports `com.waypoint.app.features.notifications.ui.NotificationHistoryViewModel` for use in this file
import com.waypoint.app.features.notifications.ui.NotificationHistoryViewModel

// annotation `@Composable` applied to the declaration that follows
@Composable
fun TabHeader(
    onBellClick: (() -> Unit)? = null,
    showAvatar: Boolean = true,
    onAvatarClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val notifVm: NotificationHistoryViewModel = viewModel()
    val notifState by notifVm.uiState.collectAsState()
    LaunchedEffect(Unit) { notifVm.load() }
    val isOnline by rememberIsOnline()

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.brand_name),
            color = WaypointTerracotta,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Top),
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            BellWithBadge(onClick = { onBellClick?.invoke() }, hasUnread = notifState.hasUnread)
            // `if` statement: the block below runs when `showAvatar` is true
            if (showAvatar) {
                // `if` statement: the block below runs when `isOnline` is true
                if (isOnline) {
                    // calls `AsyncImage` with an argument list that continues on the following lines
                    AsyncImage(
                        // continues the statement started above: `model = SessionManager.photoUrl.ifBlank { null },`
                        model = SessionManager.photoUrl.ifBlank { null },
                        // continues the statement started above: `contentDescription = stringResource(R.string.tab_header_ava…`
                        contentDescription = stringResource(R.string.tab_header_avatar_cd),
                        // continues the statement started above: `contentScale = ContentScale.Crop,`
                        contentScale = ContentScale.Crop,
                        // continues the statement started above: `placeholder = painterResource(R.drawable.img_mock_avatar),`
                        placeholder = painterResource(R.drawable.img_mock_avatar),
                        // continues the statement started above: `error = painterResource(R.drawable.img_mock_avatar),`
                        error = painterResource(R.drawable.img_mock_avatar),
                        // continues the statement started above: `fallback = painterResource(R.drawable.img_mock_avatar),`
                        fallback = painterResource(R.drawable.img_mock_avatar),
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.padding(start = 14.dp)`
                            .padding(start = 14.dp)
                            // continues the statement started above: `.size(40.dp)`
                            .size(40.dp)
                            // continues the statement started above: `.clip(CircleShape)`
                            .clip(CircleShape)
                            // continues the statement started above: `.clickable(onClick = onAvatarClick),`
                            .clickable(onClick = onAvatarClick),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `OfflineHeaderIndicator` with an argument list that continues on the following lines
                    OfflineHeaderIndicator(
                        // continues the statement started above: `isOnline = false,`
                        isOnline = false,
                        // continues the statement started above: `modifier = Modifier.padding(start = 14.dp),`
                        modifier = Modifier.padding(start = 14.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the else branch
                }
            // closes the if block
            }
        // closes the lambda passed to `Row`
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `BellWithBadge` taking 1 parameter (`onClick`) and opens its body
fun BellWithBadge(onClick: () -> Unit, hasUnread: Boolean = false, modifier: Modifier = Modifier) {
    // calls `Box` with arguments `(modifier = modifier)` and opens a trailing lambda / block
    Box(modifier = modifier) {
        // calls `Icon` with an argument list that continues on the following lines
        Icon(
            // continues the statement started above: `painter = painterResource(R.drawable.ic_bell),`
            painter = painterResource(R.drawable.ic_bell),
            // continues the statement started above: `contentDescription = stringResource(R.string.tab_header_bel…`
            contentDescription = stringResource(R.string.tab_header_bell_cd),
            // continues the statement started above: `tint = WaypointTextPrimary,`
            tint = WaypointTextPrimary,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.size(24.dp)`
                .size(24.dp)
                // continues the statement started above: `.clickable(onClick = onClick),`
                .clickable(onClick = onClick),
        // closes the multi-line argument list started above
        )
        // `if` statement: the block below runs when `hasUnread` is true
        if (hasUnread) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.align(Alignment.TopEnd)`
                    .align(Alignment.TopEnd)
                    // continues the statement started above: `.size(9.dp)`
                    .size(9.dp)
                    // continues the statement started above: `.background(WaypointCard, CircleShape),`
                    .background(WaypointCard, CircleShape),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.align(Alignment.Center)`
                        .align(Alignment.Center)
                        // continues the statement started above: `.size(6.dp)`
                        .size(6.dp)
                        // continues the statement started above: `.background(WaypointTerracotta, CircleShape),`
                        .background(WaypointTerracotta, CircleShape),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the if block
        }
    // closes the lambda passed to `Box`
    }
// closes the function `BellWithBadge`
}
