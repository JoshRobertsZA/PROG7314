package com.example.prog7314.features.notifications.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.theme.RadiusChip
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointNotifRowBorder
import com.example.prog7314.core.theme.WaypointOfflineTitle
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary

/**
 * Notifications screen. Frontend skeleton only.
 */
@Composable
fun NotificationsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    notifications: List<NotificationItem> = mockNotifications,
    onMarkAllReadClick: () -> Unit = {},
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 22.dp),
    ) {

        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.notifications_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = stringResource(R.string.notifications_title),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(if (notifications.isEmpty()) Alignment.CenterStart else Alignment.Center)
                    .padding(start = if (notifications.isEmpty()) 48.dp else 0.dp),
            )
            if (notifications.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.notifications_mark_all_read),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clickable(onClick = onMarkAllReadClick),
                )
            }
        }

        if (notifications.isEmpty()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                EmptyState()
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(top = 24.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                notifications.forEach { item ->
                    NotificationRow(item)
                }
            }
        }
    }
}

data class NotificationItem(
    val title: String,
    val body: String,
    val timestamp: String,
)

private val mockNotifications = listOf(
    NotificationItem("Flight reminder", "CPT → JNB departs in 3 hours", "9:40 AM"),
    NotificationItem("Weather alert", "Rain expected in Cape Town tomorrow", "Aug 4"),
    NotificationItem("Trip reminder", "Cape Town Getaway starts in 8 days", "2h ago"),
    NotificationItem("Price drop", "Flights to Zanzibar are now cheaper", "Yesterday"),
    NotificationItem("Booking confirmed", "Test Valley Boutique Hotel confirmed", "2 days ago"),
    NotificationItem("Car rental confirmed", "Avis Compact Car booked for Aug 3–5", "2 days ago"),
    NotificationItem("Exchange rate alert", "ZAR strengthened against USD this week", "3 days ago"),
    NotificationItem("Trip completed", "How was Lisbon Long Weekend? Leave a note", "1 week ago"),
    NotificationItem("New feature", "Offline mode is now available in Settings", "1 week ago"),
)

@Composable
private fun NotificationRow(item: NotificationItem, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(72.dp)
            .background(WaypointCard, RoundedCornerShape(RadiusChip))
            .border(1.dp, WaypointNotifRowBorder, RoundedCornerShape(RadiusChip))
            .padding(horizontal = 16.dp, vertical = 13.dp),
    ) {
        Text(
            text = item.timestamp,
            color = WaypointTextMuted,
            fontSize = 10.sp,
            modifier = Modifier.align(Alignment.TopEnd),
        )
        Column(modifier = Modifier.align(Alignment.CenterStart)) {
            Text(
                text = item.title,
                color = WaypointTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = item.body,
                color = WaypointTextMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(WaypointCard, CircleShape),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = "✓",
                color = WaypointTerracotta,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
            )
        }
        Text(
            text = stringResource(R.string.notifications_empty_title),
            color = WaypointOfflineTitle,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
        Text(
            text = stringResource(R.string.notifications_empty_subtitle),
            color = WaypointTextMuted,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
    }
}
