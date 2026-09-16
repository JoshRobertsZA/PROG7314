package com.example.prog7314.features.settings.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.prog7314.R
import com.example.prog7314.core.common.TabHeader
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointLogoutBorder
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.features.currencyexchange.ui.CurrencyExchangeModal

/**
 * Profile screen - the Profile tab root (Figma node 281:20, "Core
 * Navigation" section). Rendered as a tab inside MainNavShell, which owns
 * the shared BottomNavigationBar - this screen does not render its own
 * nav and has no back arrow, matching every other tab root.
 *
 * Replaces the previous Settings skeleton, which was built against the
 * wrong Figma node (83:2) and was missing the avatar/name/email header,
 * trip-count stat cards, and the currency/biometric rows entirely.
 *
 * TODO: replace the mock avatar/name/email/trip-counts with real
 * Firebase Auth + trip data, and wire the notifications/biometric
 * toggles to real persistence. Log out is not yet wired to a real
 * sign-out flow. Currency now opens its real modal
 * (CurrencyExchangeModal), though selecting a value there still doesn't
 * persist anything yet. Language is still a TODO - see the Language
 * Modal work happening on its own branch.
 */
@Composable
fun SettingsScreen(modifier: Modifier = Modifier) {
    var showCurrencyModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            // Header: brand name + bell (no avatar here - the big avatar
            // below is this screen's own subject).
            TabHeader(showAvatar = false)

            // Avatar + name + email, centered.
            Image(
                painter = painterResource(R.drawable.img_mock_avatar),
                contentDescription = stringResource(R.string.tab_header_avatar_cd),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
            )
            Text(
                text = "Alex Carter",
                color = WaypointTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            )
            Text(
                text = "alex.carter@email.com",
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            )

            // StatsRow: trips planned / trips created, mock counts.
            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                StatCard(value = "4 trips", label = stringResource(R.string.profile_stat_planned_label), modifier = Modifier.weight(1f))
                StatCard(value = "4 trips", label = stringResource(R.string.profile_stat_created_label), modifier = Modifier.weight(1f).padding(start = 10.dp))
            }

            Text(
                text = stringResource(R.string.profile_preferences_header),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp),
            )

            var notificationsEnabled by remember { mutableStateOf(true) }
            PreferenceRow(
                title = stringResource(R.string.profile_notifications_title),
                subtitle = stringResource(R.string.profile_notifications_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(checked = notificationsEnabled, onCheckedChange = { notificationsEnabled = it })
            }

            // TODO: not wired to a Language Modal yet - see the Language
            // Modal work happening on its own branch.
            PreferenceRow(
                title = stringResource(R.string.profile_language_title),
                subtitle = stringResource(R.string.profile_language_subtitle),
                onClick = {},
                modifier = Modifier.padding(top = 12.dp),
            ) {
                ChevronValue(value = "English")
            }

            var biometricEnabled by remember { mutableStateOf(true) }
            PreferenceRow(
                title = stringResource(R.string.profile_biometric_title),
                subtitle = stringResource(R.string.profile_biometric_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(checked = biometricEnabled, onCheckedChange = { biometricEnabled = it })
            }

            PreferenceRow(
                title = stringResource(R.string.profile_currency_title),
                subtitle = stringResource(R.string.profile_currency_subtitle),
                onClick = { showCurrencyModal = true },
                modifier = Modifier.padding(top = 12.dp),
            ) {
                ChevronValue(value = "ZAR")
            }

            // TODO: not wired to a real sign-out flow yet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    .border(1.dp, WaypointLogoutBorder, RoundedCornerShape(RadiusRow))
                    .clickable(onClick = {})
                    .padding(vertical = 12.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.profile_logout),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }

    if (showCurrencyModal) {
        Dialog(onDismissRequest = { showCurrencyModal = false }) {
            CurrencyExchangeModal(onSaveClick = { showCurrencyModal = false })
        }
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(WaypointCard, RoundedCornerShape(RadiusButton))
            .padding(vertical = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(value, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(label, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
    }
}

@Composable
private fun PreferenceRow(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
    trailing: @Composable () -> Unit,
) {
    var rowModifier = modifier
        .fillMaxWidth()
        .background(WaypointCard, RoundedCornerShape(RadiusRow))
    if (onClick != null) {
        rowModifier = rowModifier.clickable(onClick = onClick)
    }
    Row(
        modifier = rowModifier.padding(start = 27.dp, top = 12.dp, end = 18.dp, bottom = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = WaypointTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            Text(subtitle, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        }
        trailing()
    }
}

@Composable
private fun PreferenceToggle(checked: Boolean, onCheckedChange: (Boolean) -> Unit, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(width = 40.dp, height = 24.dp)
            .clickable { onCheckedChange(!checked) }
            .background(if (checked) WaypointTerracotta else WaypointBorderSoft, RoundedCornerShape(12.dp)),
    ) {
        Box(
            modifier = Modifier
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                .padding(horizontal = 3.dp)
                .size(18.dp)
                .background(WaypointCard, CircleShape),
        )
    }
}

@Composable
private fun ChevronValue(value: String, modifier: Modifier = Modifier) {
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        Text(value, color = WaypointTextMuted, fontSize = 13.sp)
        Text(
            text = stringResource(R.string.profile_chevron_glyph),
            color = WaypointTextMuted,
            fontSize = 16.sp,
            modifier = Modifier.padding(start = 4.dp),
        )
    }
}
