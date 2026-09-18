package com.waypoint.app.core.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusChip
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointModalScrim
import com.waypoint.app.core.theme.WaypointOfflineTitle
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.White

/**
 * "No internet connection" dialog, shown the first time connectivity
 * drops (see rememberIsOnline in core/connectivity). Frontend only -
 * onDismissRequest just closes it; it doesn't retry anything itself,
 * since dismissal happens automatically once rememberIsOnline flips back
 * to true.
 */
@Composable
fun OfflineDialog(onDismissRequest: () -> Unit, modifier: Modifier = Modifier) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(WaypointModalScrim)
                .padding(horizontal = 35.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusChip))
                    .background(WaypointCard, RoundedCornerShape(RadiusChip))
                    .padding(horizontal = 24.dp, vertical = 28.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(WaypointTerracotta, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = "!", color = White, fontSize = 26.sp, fontWeight = FontWeight.Bold)
                }

                Text(
                    text = stringResource(R.string.offline_dialog_title),
                    color = WaypointOfflineTitle,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )

                Text(
                    text = stringResource(R.string.offline_dialog_body),
                    color = WaypointTextMuted,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center,
                )

                OkayButton(onClick = onDismissRequest)
            }
        }
    }
}

@Composable
fun StillOfflineBubble(onOkayClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(260.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusChip))
            .background(WaypointCard, RoundedCornerShape(RadiusChip))
            .padding(horizontal = 22.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = stringResource(R.string.offline_bubble_body),
            color = WaypointTextMuted,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            textAlign = TextAlign.Center,
        )
        OkayButton(onClick = onOkayClick)
    }
}

/**
 * Persistent header icon shown in place of the header's avatar while
 * offline (Figma "NoWifiIcon", node 347:13/333:167) - appears once the
 * first-drop [OfflineDialog] has been dismissed and connectivity is
 * still down. Tapping it toggles the [StillOfflineBubble] popover
 * (Figma "ReconnectBubble", node 347:9), anchored below the icon with a
 * [BubbleTail] pointer. Renders nothing while online.
 */
@Composable
fun OfflineHeaderIndicator(isOnline: Boolean, modifier: Modifier = Modifier, iconSize: Dp = 40.dp) {
    var showBubble by remember { mutableStateOf(false) }
    LaunchedEffect(isOnline) { if (isOnline) showBubble = false }

    if (isOnline) return

    val density = LocalDensity.current

    Box(modifier = modifier) {
        Image(
            painter = painterResource(R.drawable.ic_no_wifi),
            contentDescription = stringResource(R.string.tab_header_offline_cd),
            modifier = Modifier
                .size(iconSize)
                .clickable { showBubble = !showBubble },
        )

        if (showBubble) {
            Popup(
                alignment = Alignment.TopEnd,
                offset = IntOffset(0, with(density) { (iconSize + 6.dp).roundToPx() }),
                onDismissRequest = { showBubble = false },
                properties = PopupProperties(focusable = true),
            ) {
                Column(horizontalAlignment = Alignment.End) {
                    BubbleTail(modifier = Modifier.padding(end = 8.dp))
                    StillOfflineBubble(onOkayClick = { showBubble = false })
                }
            }
        }
    }
}

/** Small upward-pointing triangle connecting [StillOfflineBubble] to the header icon it's anchored to. */
@Composable
private fun BubbleTail(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier.size(width = 20.dp, height = 10.dp)) {
        val path = Path().apply {
            moveTo(size.width / 2f, 0f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(path, color = WaypointCard)
    }
}

@Composable
private fun OkayButton(onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
            .clickable(onClick = onClick)
            .padding(vertical = 14.dp),
        horizontalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(R.string.offline_dialog_okay),
            color = White,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
