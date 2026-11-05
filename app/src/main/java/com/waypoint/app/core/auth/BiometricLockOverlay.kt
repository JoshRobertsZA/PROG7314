package com.waypoint.app.core.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

/**
 * Full-screen cover shown over the app until the user unlocks it. Opens the prompt as soon as it
 * appears, and offers a retry button with the reason the last attempt failed.
 */
@Composable
fun BiometricLockOverlay(
    onUnlockClick: () -> Unit,
    modifier: Modifier = Modifier,
    message: String? = null,
) {
    LaunchedEffect(Unit) { onUnlockClick() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            modifier = Modifier.padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .background(WaypointTerracotta, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "W",
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Text(
                text = stringResource(R.string.biometric_lock_title),
                color = WaypointTextPrimary,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.biometric_lock_subtitle),
                color = WaypointTextMuted,
                fontSize = 13.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp),
            )

            // explains why the last unlock attempt failed, when it did
            if (message != null) {
                Text(
                    text = message,
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 16.dp),
                )
            }

            Box(
                modifier = Modifier
                    .padding(top = 28.dp)
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(16.dp))
                    .clickable(onClick = onUnlockClick),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.biometric_lock_unlock_button),
                    color = White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
