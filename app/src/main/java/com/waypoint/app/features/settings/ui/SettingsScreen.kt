package com.waypoint.app.features.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import coil.compose.AsyncImage
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.auth.BiometricAuthenticator
import com.waypoint.app.core.auth.BiometricAvailability
import com.waypoint.app.core.auth.BiometricLockPreferences
import com.waypoint.app.core.common.TabHeader
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.notifications.NotificationPreferences
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.features.notifications.ui.NotificationHistoryModal
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusRow
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointLogoutBorder
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary

/**
 * Profile screen - the Profile tab root (Figma node 281:20, "Core
 * Navigation" section). Rendered as a tab inside MainNavShell, which owns
 * the shared BottomNavigationBar - this screen does not render its own
 * nav and has no back arrow, matching every other tab root.
 *
 * Replaces the previous Settings skeleton, which was built against the
 * wrong Figma node (83:2) and was missing the avatar/name/email header,
 * trip-count stat cards, and the biometric row entirely.
 *
 * The language row opens LanguageModal (see LanguageModal.kt); the
 * selected language is held in local state here and applied via
 * AppCompatDelegate on save, but isn't persisted across restarts yet.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit = {},
    viewModel: SettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    // Re-count every time the tab is opened so a trip created elsewhere
    // in the app is reflected without restarting.
    LaunchedEffect(Unit) { viewModel.loadTripCounts() }

    var selectedLanguage by remember { mutableStateOf(AppLanguage.current()) }
    var showLanguageModal by remember { mutableStateOf(false) }
    var showNotificationHistory by remember { mutableStateOf(false) }

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
            TabHeader(showAvatar = false, onBellClick = { showNotificationHistory = true })

            // Avatar + name + email, centered.
            AsyncImage(
                model = SessionManager.photoUrl.ifBlank { null },
                contentDescription = stringResource(R.string.tab_header_avatar_cd),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.img_mock_avatar),
                error = painterResource(R.drawable.img_mock_avatar),
                fallback = painterResource(R.drawable.img_mock_avatar),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
            )
            Text(
                text = SessionManager.displayName.ifBlank { "Alex Carter" },
                color = WaypointTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            )
            Text(
                text = SessionManager.email.ifBlank { "alex.carter@email.com" },
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            )

            // StatsRow: trips starting this calendar year / every trip this
            // account has created, both scoped to SessionManager.accountId.
            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                StatCard(
                    value = pluralStringResource(R.plurals.profile_stat_trips, uiState.plannedThisYear, uiState.plannedThisYear),
                    label = stringResource(R.string.profile_stat_planned_label),
                    modifier = Modifier.weight(1f),
                )
                StatCard(
                    value = pluralStringResource(R.plurals.profile_stat_trips, uiState.totalTrips, uiState.totalTrips),
                    label = stringResource(R.string.profile_stat_created_label),
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                )
            }

            Text(
                text = stringResource(R.string.profile_preferences_header),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp),
            )

            // Backed by SharedPreferences so the choice survives app restarts;
            // PushNotifier.show() reads the same flag before posting anything.
            val context = LocalContext.current
            var notificationsEnabled by remember { mutableStateOf(NotificationPreferences.isEnabled(context)) }
            PreferenceRow(
                title = stringResource(R.string.profile_notifications_title),
                subtitle = stringResource(R.string.profile_notifications_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = notificationsEnabled,
                    onCheckedChange = {
                        notificationsEnabled = it
                        NotificationPreferences.setEnabled(context, it)
                    },
                )
            }

            PreferenceRow(
                title = stringResource(R.string.profile_language_title),
                subtitle = stringResource(R.string.profile_language_subtitle),
                onClick = { showLanguageModal = true },
                modifier = Modifier.padding(top = 12.dp),
            ) {
                ChevronValue(value = selectedLanguage.displayName)
            }

            // Checked once - device biometric enrollment doesn't change
            // while this screen is open, and re-checking on every
            // recomposition would be wasteful.
            val biometricAvailability = remember { BiometricAuthenticator.availability(context) }
            var biometricEnabled by remember { mutableStateOf(BiometricLockPreferences.isEnabled(context)) }
            val biometricSubtitle = when (biometricAvailability) {
                BiometricAvailability.AVAILABLE -> stringResource(R.string.profile_biometric_subtitle)
                BiometricAvailability.NONE_ENROLLED -> stringResource(R.string.profile_biometric_unavailable_none_enrolled)
                else -> stringResource(R.string.profile_biometric_unavailable_no_hardware)
            }
            val enableTitle = stringResource(R.string.profile_biometric_enable_title)
            val enableSubtitle = stringResource(R.string.profile_biometric_enable_subtitle)
            val cancelText = stringResource(R.string.biometric_lock_cancel)
            PreferenceRow(
                title = stringResource(R.string.profile_biometric_title),
                subtitle = biometricSubtitle,
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = biometricEnabled,
                    enabled = biometricAvailability == BiometricAvailability.AVAILABLE,
                    onCheckedChange = { turningOn ->
                        if (!turningOn) {
                            biometricEnabled = false
                            BiometricLockPreferences.setEnabled(context, false)
                            return@PreferenceToggle
                        }
                        // Confirm biometric actually works before persisting
                        // enabled=true - otherwise a stale/broken sensor
                        // could lock the user out of their own app.
                        BiometricAuthenticator.authenticate(
                            activity = context as FragmentActivity,
                            title = enableTitle,
                            subtitle = enableSubtitle,
                            negativeButtonText = cancelText,
                            onSuccess = {
                                biometricEnabled = true
                                BiometricLockPreferences.setEnabled(context, true)
                            },
                            onError = { _, _ -> /* leave the toggle off */ },
                        )
                    },
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    .border(1.dp, WaypointLogoutBorder, RoundedCornerShape(RadiusRow))
                    .clickable(onClick = onLogoutClick)
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

    if (showNotificationHistory) {
        Dialog(onDismissRequest = { showNotificationHistory = false }) {
            NotificationHistoryModal()
        }
    }

    if (showLanguageModal) {
        Dialog(onDismissRequest = { showLanguageModal = false }) {
            LanguageModal(
                selectedLanguage = selectedLanguage,
                onLanguageSelected = { selectedLanguage = it },
                onSaveClick = {
                    // This is what actually switches the app's locale.
                    // AppCompat recreates the activity to apply it immediately,
                    // and persists the choice across restarts on its own - no
                    // manual SharedPreferences/DataStore needed.
                    AppCompatDelegate.setApplicationLocales(
                        LocaleListCompat.forLanguageTags(selectedLanguage.localeTag),
                    )
                    showLanguageModal = false
                },
            )
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
private fun PreferenceToggle(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    Box(
        modifier = modifier
            .size(width = 40.dp, height = 24.dp)
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            .background(
                when {
                    !enabled -> WaypointBorderSoft.copy(alpha = 0.5f)
                    checked -> WaypointTerracotta
                    else -> WaypointBorderSoft
                },
                RoundedCornerShape(12.dp),
            ),
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