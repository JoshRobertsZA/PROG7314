package com.waypoint.app.features.settings.ui

import androidx.appcompat.app.AppCompatDelegate
import android.os.Build
import coil.compose.AsyncImage
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import com.waypoint.app.core.theme.White
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.ui.window.DialogProperties
import androidx.core.os.LocaleListCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import android.text.format.DateUtils
import androidx.compose.material3.CircularProgressIndicator
import com.waypoint.app.core.connectivity.rememberIsOnline
import com.waypoint.app.core.settings.AppSettings
import android.widget.Toast
import com.waypoint.app.core.auth.BiometricAuthenticator
import com.waypoint.app.core.auth.biometricFailureMessage
import com.waypoint.app.core.auth.classifyBiometricError
import com.waypoint.app.core.auth.BiometricAvailability
import com.waypoint.app.core.auth.BiometricLockPreferences
import com.waypoint.app.core.common.TabHeader
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.notifications.PushNotifier
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.core.network.GuardedApi
import com.waypoint.app.core.network.UsageSnapshot
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.ThemePreferences
import androidx.compose.foundation.isSystemInDarkTheme
import com.waypoint.app.core.theme.RadiusRow
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointLogoutBorder
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.WaypointTripBadgeText

/**
 * The Profile tab: account details, trip stats, API budget meters, every setting (notifications,
 * theme, language, home currency, biometric unlock), sync status and sign-out.
 */
@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onRequestNotificationPermission: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    viewModel: SettingsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) { viewModel.loadTripCounts(); viewModel.loadApiUsage() }

    var selectedLanguage by remember { mutableStateOf(AppLanguage.current()) }
    var showLanguageModal by remember { mutableStateOf(false) }
    var showLogoutModal by remember { mutableStateOf(false) }
    var showCurrencyModal by remember { mutableStateOf(false) }
    var pendingCurrency by remember { mutableStateOf(AppSettings.current.defaultCurrency) }

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
            TabHeader(showAvatar = false, onBellClick = onNotificationsClick)

            AsyncImage(
                model = SessionManager.photoUrl.ifBlank { null },
                contentDescription = stringResource(R.string.tab_header_avatar_cd),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.ic_avatar_default),
                error = painterResource(R.drawable.ic_avatar_default),
                fallback = painterResource(R.drawable.ic_avatar_default),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .size(72.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape),
            )
            Text(
                text = SessionManager.displayName,
                color = WaypointTextPrimary,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            )
            Text(
                text = SessionManager.email,
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            )

            PreferenceRow(
                title = stringResource(R.string.profile_edit_row_title),
                subtitle = stringResource(R.string.profile_edit_row_subtitle),
                onClick = onEditProfileClick,
                modifier = Modifier.padding(top = 16.dp),
            ) {
                Text(
                    text = stringResource(R.string.profile_chevron_glyph),
                    color = WaypointTextMuted,
                    fontSize = 16.sp,
                )
            }

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

            // Only shown when there's actually something queued - most of
            // the time this is 0 and the row just isn't there, rather than
            // always showing "0 changes waiting."
            if (uiState.pendingChangeCount > 0) {
                Text(
                    text = pluralStringResource(
                        R.plurals.profile_pending_changes_count,
                        uiState.pendingChangeCount,
                        uiState.pendingChangeCount,
                    ),
                    color = WaypointTextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 8.dp).fillMaxWidth(),
                )
            }

            if (uiState.apiUsage.isNotEmpty()) {
                Text(
                    text = stringResource(R.string.api_budget_header),
                    color = WaypointTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 20.dp),
                )
                // opens one white card that holds all the meters, matching the preference rows
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(WaypointCard, RoundedCornerShape(RadiusRow))
                        .padding(start = 27.dp, top = 4.dp, end = 18.dp, bottom = 14.dp),
                ) {
                    // draws one meter per guarded API
                    uiState.apiUsage.forEach { snapshot -> ApiBudgetMeter(snapshot) }
                }
            }

            Text(
                text = stringResource(R.string.profile_preferences_header),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp),
            )

            val context = LocalContext.current
            val settings by AppSettings.state.collectAsState()
            val isOnline by rememberIsOnline()

            // Switching a notification type on is pointless without the Android 13+ permission, so ask for it then.
            fun requestPermissionIfTurningOn(turningOn: Boolean) {
                if (turningOn && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PushNotifier.hasPermission(context)) {
                    onRequestNotificationPermission()
                }
            }

            PreferenceRow(
                title = stringResource(R.string.settings_notify_trip_updates_title),
                subtitle = stringResource(R.string.settings_notify_trip_updates_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = settings.notifyTripUpdates,
                    onCheckedChange = {
                        AppSettings.setNotifyTripUpdates(it)
                        requestPermissionIfTurningOn(it)
                    },
                )
            }

            PreferenceRow(
                title = stringResource(R.string.settings_notify_reminders_title),
                subtitle = stringResource(R.string.settings_notify_reminders_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = settings.notifyReminders,
                    onCheckedChange = {
                        AppSettings.setNotifyReminders(it)
                        requestPermissionIfTurningOn(it)
                    },
                )
            }

            val darkModeOn = ThemePreferences.override ?: isSystemInDarkTheme()
            PreferenceRow(
                title = stringResource(R.string.profile_dark_mode_title),
                subtitle = stringResource(R.string.profile_dark_mode_subtitle),
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = darkModeOn,
                    onCheckedChange = { ThemePreferences.setDark(it) },
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

            PreferenceRow(
                title = stringResource(R.string.settings_home_currency_title),
                subtitle = stringResource(R.string.settings_home_currency_subtitle),
                onClick = {
                    pendingCurrency = settings.defaultCurrency
                    showCurrencyModal = true
                },
                modifier = Modifier.padding(top = 12.dp),
            ) {
                ChevronValue(value = settings.defaultCurrency)
            }

            val biometricAvailability = remember { BiometricAuthenticator.availability(context) }
            val biometricSubtitle = when (biometricAvailability) {
                BiometricAvailability.AVAILABLE -> stringResource(R.string.profile_biometric_subtitle)
                BiometricAvailability.NONE_ENROLLED -> stringResource(R.string.profile_biometric_unavailable_none_enrolled)
                else -> stringResource(R.string.profile_biometric_unavailable_no_hardware)
            }
            val enableTitle = stringResource(R.string.profile_biometric_enable_title)
            val enableSubtitle = stringResource(R.string.profile_biometric_enable_subtitle)
            PreferenceRow(
                title = stringResource(R.string.profile_biometric_title),
                subtitle = biometricSubtitle,
                modifier = Modifier.padding(top = 12.dp),
            ) {
                PreferenceToggle(
                    checked = settings.biometricLock,
                    enabled = biometricAvailability == BiometricAvailability.AVAILABLE,
                    onCheckedChange = { turningOn ->
                        if (!turningOn) {
                            BiometricLockPreferences.setEnabled(false)
                            return@PreferenceToggle
                        }
                        // Confirm the user can actually unlock before locking the app behind it.
                        BiometricAuthenticator.authenticate(
                            activity = context as FragmentActivity,
                            title = enableTitle,
                            subtitle = enableSubtitle,
                            onSuccess = { BiometricLockPreferences.setEnabled(true) },
                            onError = { code, _ ->
                                Toast.makeText(
                                    context,
                                    context.getString(biometricFailureMessage(classifyBiometricError(code))),
                                    Toast.LENGTH_SHORT,
                                ).show()
                            },
                        )
                    },
                )
            }

            Text(
                text = stringResource(R.string.settings_sync_header),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 20.dp),
            )
            val lastSyncAtMs = uiState.lastSyncAtMs
            val syncSubtitle = when {
                uiState.isSyncing -> stringResource(R.string.settings_sync_in_progress)
                !isOnline -> stringResource(R.string.settings_sync_offline)
                lastSyncAtMs == null -> stringResource(R.string.settings_sync_never)
                else -> stringResource(
                    R.string.settings_sync_last,
                    DateUtils.formatDateTime(
                        context,
                        lastSyncAtMs,
                        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_TIME or DateUtils.FORMAT_ABBREV_MONTH,
                    ),
                )
            }
            val canSync = isOnline && !uiState.isSyncing
            PreferenceRow(
                title = stringResource(R.string.settings_sync_now_title),
                subtitle = syncSubtitle,
                onClick = if (canSync) viewModel::syncNow else null,
                modifier = Modifier.padding(top = 12.dp),
            ) {
                if (uiState.isSyncing) {
                    CircularProgressIndicator(
                        color = WaypointTerracotta,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(20.dp),
                    )
                } else {
                    Text(
                        text = stringResource(R.string.settings_sync_now_action),
                        color = if (canSync) WaypointTerracotta else WaypointTextMuted,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    .border(1.dp, WaypointLogoutBorder, RoundedCornerShape(RadiusRow))
                    .clickable { showLogoutModal = true }
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

        if (showLanguageModal) {
            Dialog(
                onDismissRequest = { showLanguageModal = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                ) {
                    LanguageModal(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelected = { selectedLanguage = it },
                        onSaveClick = {
                            AppCompatDelegate.setApplicationLocales(
                                LocaleListCompat.forLanguageTags(selectedLanguage.localeTag),
                            )
                            AppSettings.markLanguageChanged()
                            showLanguageModal = false
                        },
                    )
                }
            }
        }

        if (showCurrencyModal) {
            Dialog(
                onDismissRequest = { showCurrencyModal = false },
                properties = DialogProperties(usePlatformDefaultWidth = false),
            ) {
                Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
                    CurrencyModal(
                        selectedCode = pendingCurrency,
                        onCurrencySelected = { pendingCurrency = it },
                        onSaveClick = {
                            AppSettings.setDefaultCurrency(pendingCurrency)
                            showCurrencyModal = false
                        },
                    )
                }
            }
        }

        if (showLogoutModal) {
            LogoutConfirmationModal(
                onConfirmLogout = {
                    showLogoutModal = false
                    onLogoutClick()
                },
                onDismiss = { showLogoutModal = false },
            )
        }
    }
}

@Composable
private fun LogoutConfirmationModal(
    onConfirmLogout: () -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
        ) {
            Column(
                modifier = modifier
                    .fillMaxWidth()
                    .background(WaypointCard, RoundedCornerShape(22.dp))
                    .border(1.dp, WaypointBorderSoft, RoundedCornerShape(22.dp))
                    .padding(22.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(WaypointTerracotta.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("👋", fontSize = 28.sp)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        // Logout dialog title from strings.xml so it follows the selected language
                        text = stringResource(R.string.settings_logout_dialog_title),
                        color = WaypointTextPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        // Logout dialog body from strings.xml
                        text = stringResource(R.string.settings_logout_dialog_body),
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(RadiusButton))
                            .background(WaypointTerracotta)
                            .clickable(onClick = onConfirmLogout),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(R.string.profile_logout),
                            color = White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .clip(RoundedCornerShape(RadiusButton))
                            .background(WaypointCream)
                            .border(1.dp, WaypointBorderSoft, RoundedCornerShape(RadiusButton))
                            .clickable(onClick = onDismiss),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            // Reuses the shared Cancel string
                            text = stringResource(R.string.common_cancel),
                            color = WaypointTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                        )
                    }
                }
            }
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
@Composable
private fun ApiBudgetMeter(snapshot: UsageSnapshot) {
    val label = stringResource(if (snapshot.api == GuardedApi.LOCATIONIQ) R.string.api_budget_locationiq else R.string.api_budget_airlabs)
    val period = stringResource(if (snapshot.api.dailyLimit != null) R.string.api_budget_today else R.string.api_budget_this_month)
    val barColor = if (snapshot.fraction >= 0.9f) WaypointTerracotta else WaypointTripBadgeText
    // opens a vertical stack for the meter
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        // row holding the label on the left and the numbers on the right
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // the API name
            Text(label, color = WaypointTextPrimary, fontSize = 13.sp)
            // the numbers, for example "1,204 / 5,000 today"
            Text("${snapshot.used} / ${snapshot.limit} $period", color = WaypointTextMuted, fontSize = 12.sp)
        }
        // the progress bar showing how much of the limit is used
        LinearProgressIndicator(
            progress = { snapshot.fraction },
            color = barColor,
            trackColor = WaypointBorderSoft,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        )
    }
}
