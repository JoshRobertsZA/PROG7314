package com.example.prog7314.core.common

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.prog7314.R
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusChip
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointModalScrim
import com.example.prog7314.core.theme.WaypointOfflineTitle
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.White

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
