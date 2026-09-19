// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
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
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
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
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `BiometricLockOverlay` taking 1 parameter (`onUnlockClick`) and opens its body
fun BiometricLockOverlay(onUnlockClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { onUnlockClick() }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream),`
            .background(WaypointCream),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier.padding(32.dp),`
            modifier = Modifier.padding(32.dp),
            // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
            horizontalAlignment = Alignment.CenterHorizontally,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.size(72.dp)`
                    .size(72.dp)
                    // continues the statement started above: `.background(WaypointTerracotta, CircleShape),`
                    .background(WaypointTerracotta, CircleShape),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = "W",`
                    text = "W",
                    // continues the statement started above: `color = White,`
                    color = White,
                    // continues the statement started above: `fontSize = 28.sp,`
                    fontSize = 28.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.biometric_lock_title),`
                text = stringResource(R.string.biometric_lock_title),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 20.sp,`
                fontSize = 20.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.padding(top = 24.dp),`
                modifier = Modifier.padding(top = 24.dp),
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.biometric_lock_subtitle),`
                text = stringResource(R.string.biometric_lock_subtitle),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 13.sp,`
                fontSize = 13.sp,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                modifier = Modifier.padding(top = 8.dp),
            // closes the multi-line argument list started above
            )

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 28.dp)`
                    .padding(top = 28.dp)
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.height(52.dp)`
                    .height(52.dp)
                    // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(16.dp))`
                    .background(WaypointTerracotta, RoundedCornerShape(16.dp))
                    // continues the statement started above: `.clickable(onClick = onUnlockClick),`
                    .clickable(onClick = onUnlockClick),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.biometric_lock_unlock_button…`
                    text = stringResource(R.string.biometric_lock_unlock_button),
                    // continues the statement started above: `color = White,`
                    color = White,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the block
    }
// closes the function `BiometricLockOverlay`
}
