package com.waypoint.app.core.common

import androidx.compose.foundation.Image
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextPrimary

/**
 * Shared tab-root header (brand name + bell-with-badge, optionally an
 * avatar) used by Home/Trips/Explore/Profile - each tab's own header in
 * Figma is this same layout, only Profile omits the avatar since the big
 * avatar below is that screen's own subject.
 *
 * While offline, the avatar slot is replaced by [OfflineHeaderIndicator]
 * (Figma "Offline Mode" section, node 332:424) - tapping it surfaces the
 * still-offline reminder bubble. [isOnline] defaults to true so existing
 * callers that don't pass it keep their current (online) appearance.
 */
@Composable
fun TabHeader(
    onBellClick: () -> Unit = {},
    showAvatar: Boolean = true,
    isOnline: Boolean = true,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.login_brand_name),
            color = WaypointTerracotta,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            BellWithBadge(onClick = onBellClick)
            if (showAvatar) {
                if (isOnline) {
                    Image(
                        painter = painterResource(R.drawable.img_mock_avatar),
                        contentDescription = stringResource(R.string.tab_header_avatar_cd),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .padding(start = 14.dp)
                            .size(40.dp)
                            .clip(CircleShape),
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

/** Bell icon with a small unread-notification dot, top-end of the icon. */
@Composable
fun BellWithBadge(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        // TODO: not wired to a notifications feed/screen yet
        Icon(
            painter = painterResource(R.drawable.ic_bell),
            contentDescription = stringResource(R.string.tab_header_bell_cd),
            tint = WaypointTextPrimary,
            modifier = Modifier
                .size(24.dp)
                .clickable(onClick = onClick),
        )
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
