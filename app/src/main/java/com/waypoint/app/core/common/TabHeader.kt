package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.waypoint.app.R
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.features.notifications.ui.NotificationHistoryModal
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextPrimary
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.core.connectivity.rememberIsOnline
import com.waypoint.app.features.notifications.ui.NotificationHistoryViewModel

/**
 * Header used on the main tabs: the Waypoint wordmark, the offline indicator, the notification bell
 * and the user's avatar.
 */
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
            if (showAvatar) {
                if (isOnline) {
                    AsyncImage(
                        model = SessionManager.photoUrl.ifBlank { null },
                        contentDescription = stringResource(R.string.tab_header_avatar_cd),
                        contentScale = ContentScale.Crop,
                        placeholder = painterResource(R.drawable.ic_avatar_default),
                        error = painterResource(R.drawable.ic_avatar_default),
                        fallback = painterResource(R.drawable.ic_avatar_default),
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(40.dp)
                            .clip(CircleShape)
                            .clickable(onClick = onAvatarClick),
                    )
                } else {
                    OfflineHeaderIndicator(
                        isOnline = false,
                        modifier = Modifier.padding(start = 14.dp),
                    )
                }
            }
        }
    }
}

/** Notification bell with a dot when there are notifications in the history. */
@Composable
fun BellWithBadge(onClick: () -> Unit, hasUnread: Boolean = false, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = stringResource(R.string.tab_header_bell_cd),
            tint = WaypointTextPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick),
        )
        if (hasUnread) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .size(9.dp)
                    .background(WaypointCard, CircleShape),
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(6.dp)
                        .background(WaypointTerracotta, CircleShape),
                )
            }
        }
    }
}
