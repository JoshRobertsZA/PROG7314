package com.waypoint.app.features.notifications.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusDeco
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

/**
 * Notification history card, opened from the bell on the Profile tab.
 * Hosted in a plain androidx.compose.ui.window.Dialog by the caller (same
 * pattern as LanguageModal) so dismissal is tap-outside / back.
 *
 * Same card shell as core/common/SelectionModal, but with a "Clear" action
 * instead of "Save" - SelectionModal hard-codes the Save label.
 */
@Composable
fun NotificationHistoryModal(
    modifier: Modifier = Modifier,
    viewModel: NotificationHistoryViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.load() }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(WaypointCard, RoundedCornerShape(RadiusDeco))
            .padding(horizontal = 22.dp, vertical = 24.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp),
    ) {
        Text(
            text = stringResource(R.string.notifications_title),
            color = WaypointTextPrimary,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
        )

        if (!uiState.isLoading && uiState.items.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp), contentAlignment = Alignment.Center) {
                NotificationsEmptyState()
            }
        } else {
            Column(
                modifier = Modifier
                    .heightIn(max = 360.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                uiState.items.forEach { NotificationRow(it) }
            }
        }

        if (uiState.items.isNotEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                    .clickable(onClick = viewModel::clearAll),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.notifications_clear_all),
                    color = White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}
