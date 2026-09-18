package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusDeco
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

/**
 * Shared shell for modals that are a bold title + a feature-specific
 * content slot + an identical full-width Save button - currently used
 * by the Currency Change modal (Figma node 392:12). The Language modal
 * (Figma node 394:62) is the same card and should use this shell too
 * once it's built (in progress on its own branch). Meant to be hosted
 * in an androidx.compose.ui.window.Dialog by the caller; this
 * composable only renders the card itself.
 */
@Composable
fun SelectionModal(
    title: String,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WaypointCard, RoundedCornerShape(RadiusDeco))
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(text = title, color = WaypointTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
        content()
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                .clickable(onClick = onSaveClick),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.modal_save_button),
                color = White,
                fontSize = 17.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}
