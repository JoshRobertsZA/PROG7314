// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

// imports `androidx.appcompat.app.AppCompatDelegate` for use in this file
import androidx.appcompat.app.AppCompatDelegate
// imports `android.os.Build` for use in this file
import android.os.Build
// imports `coil.compose.AsyncImage` for use in this file
import coil.compose.AsyncImage
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
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
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `androidx.compose.foundation.layout.statusBars` for use in this file
import androidx.compose.foundation.layout.statusBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.Text` for use in this file
// imports `androidx.compose.material3.LinearProgressIndicator` for use in this file
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.pluralStringResource` for use in this file
import androidx.compose.ui.res.pluralStringResource
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
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `androidx.compose.ui.window.DialogProperties` for use in this file
import androidx.compose.ui.window.DialogProperties
// imports `androidx.core.os.LocaleListCompat` for use in this file
import androidx.core.os.LocaleListCompat
// imports `androidx.fragment.app.FragmentActivity` for use in this file
import androidx.fragment.app.FragmentActivity
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
import android.text.format.DateUtils
import androidx.compose.material3.CircularProgressIndicator
import com.waypoint.app.core.connectivity.rememberIsOnline
import com.waypoint.app.core.settings.AppSettings
// imports `com.waypoint.app.core.auth.BiometricAuthenticator` for use in this file
import android.widget.Toast
import com.waypoint.app.core.auth.BiometricAuthenticator
import com.waypoint.app.core.auth.biometricFailureMessage
import com.waypoint.app.core.auth.classifyBiometricError
// imports `com.waypoint.app.core.auth.BiometricAvailability` for use in this file
import com.waypoint.app.core.auth.BiometricAvailability
// imports `com.waypoint.app.core.auth.BiometricLockPreferences` for use in this file
import com.waypoint.app.core.auth.BiometricLockPreferences
// imports `com.waypoint.app.core.common.TabHeader` for use in this file
import com.waypoint.app.core.common.TabHeader
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.notifications.PushNotifier
// imports `com.waypoint.app.core.locale.AppLanguage` for use in this file
import com.waypoint.app.core.locale.AppLanguage
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
// imports `com.waypoint.app.core.network.GuardedApi` for use in this file
import com.waypoint.app.core.network.GuardedApi
// imports `com.waypoint.app.core.network.UsageSnapshot` for use in this file
import com.waypoint.app.core.network.UsageSnapshot
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.ThemePreferences` for use in this file
import com.waypoint.app.core.theme.ThemePreferences
// imports `androidx.compose.foundation.isSystemInDarkTheme` for use in this file
import androidx.compose.foundation.isSystemInDarkTheme
// imports `com.waypoint.app.core.theme.RadiusRow` for use in this file
import com.waypoint.app.core.theme.RadiusRow
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointLogoutBorder` for use in this file
import com.waypoint.app.core.theme.WaypointLogoutBorder
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.WaypointTripBadgeText` for use in this file
import com.waypoint.app.core.theme.WaypointTripBadgeText

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun SettingsScreen(`
fun SettingsScreen(
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `onLogoutClick: () -> Unit = {},`
    onLogoutClick: () -> Unit = {},
    // continues the statement started above: `onNotificationsClick: () -> Unit = {},`
    onNotificationsClick: () -> Unit = {},
    onRequestNotificationPermission: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    // continues the statement started above: `viewModel: SettingsViewModel = viewModel(),`
    viewModel: SettingsViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()
    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.loadTripCounts(); viewModel.loadApiUsage() }

    // declares mutable property `selectedLanguage`, delegated to `remember { mutableStateOf(AppLanguage.c…`
    var selectedLanguage by remember { mutableStateOf(AppLanguage.current()) }
    // declares mutable property `showLanguageModal`, delegated to `remember { mutableStateOf(false) }`
    var showLanguageModal by remember { mutableStateOf(false) }
    // declares mutable property `showLogoutModal`, delegated to `remember { mutableStateOf(false) }`
    var showLogoutModal by remember { mutableStateOf(false) }
    var showCurrencyModal by remember { mutableStateOf(false) }
    var pendingCurrency by remember { mutableStateOf(AppSettings.current.defaultCurrency) }

    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.statusBars)`
            .windowInsetsPadding(WindowInsets.statusBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp),`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.weight(1f)`
                .weight(1f)
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                .verticalScroll(rememberScrollState()),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `TabHeader` with arguments `(showAvatar = false, onBellClick = onNotifica…)`
            TabHeader(showAvatar = false, onBellClick = onNotificationsClick)

            // calls `AsyncImage` with an argument list that continues on the following lines
            AsyncImage(
                // continues the statement started above: `model = SessionManager.photoUrl.ifBlank { null },`
                model = SessionManager.photoUrl.ifBlank { null },
                // continues the statement started above: `contentDescription = stringResource(R.string.tab_header_ava…`
                contentDescription = stringResource(R.string.tab_header_avatar_cd),
                // continues the statement started above: `contentScale = ContentScale.Crop,`
                contentScale = ContentScale.Crop,
                // continues the statement started above: `placeholder = painterResource(R.drawable.ic_avatar_default),`
                placeholder = painterResource(R.drawable.ic_avatar_default),
                // continues the statement started above: `error = painterResource(R.drawable.ic_avatar_default),`
                error = painterResource(R.drawable.ic_avatar_default),
                // continues the statement started above: `fallback = painterResource(R.drawable.ic_avatar_default),`
                fallback = painterResource(R.drawable.ic_avatar_default),
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 8.dp)`
                    .padding(top = 8.dp)
                    // continues the statement started above: `.size(72.dp)`
                    .size(72.dp)
                    // continues the statement started above: `.align(Alignment.CenterHorizontally)`
                    .align(Alignment.CenterHorizontally)
                    // continues the statement started above: `.clip(CircleShape),`
                    .clip(CircleShape),
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = SessionManager.displayName,`
                text = SessionManager.displayName,
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 18.sp,`
                fontSize = 18.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),`
                modifier = Modifier.padding(top = 12.dp).fillMaxWidth(),
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = SessionManager.email.ifBlank { "alex.carter@email.co…`
                text = SessionManager.email,
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),`
                modifier = Modifier.padding(top = 4.dp).fillMaxWidth(),
            // closes the multi-line argument list started above
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

            // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                // calls `StatCard` with an argument list that continues on the following lines
                StatCard(
                    // continues the statement started above: `value = pluralStringResource(R.plurals.profile_stat_trips, …`
                    value = pluralStringResource(R.plurals.profile_stat_trips, uiState.plannedThisYear, uiState.plannedThisYear),
                    // continues the statement started above: `label = stringResource(R.string.profile_stat_planned_label),`
                    label = stringResource(R.string.profile_stat_planned_label),
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
                // calls `StatCard` with an argument list that continues on the following lines
                StatCard(
                    // continues the statement started above: `value = pluralStringResource(R.plurals.profile_stat_trips, …`
                    value = pluralStringResource(R.plurals.profile_stat_trips, uiState.totalTrips, uiState.totalTrips),
                    // continues the statement started above: `label = stringResource(R.string.profile_stat_created_label),`
                    label = stringResource(R.string.profile_stat_created_label),
                    // continues the statement started above: `modifier = Modifier.weight(1f).padding(start = 10.dp),`
                    modifier = Modifier.weight(1f).padding(start = 10.dp),
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `Row`
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

            // `if` statement: the section only shows when usage could be read, so it stays hidden when CounterAPI is not configured
            if (uiState.apiUsage.isNotEmpty()) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    text = stringResource(R.string.api_budget_header),
                    color = WaypointTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 20.dp),
                // closes the multi-line argument list started above
                )
                // opens one white card that holds all the meters, matching the preference rows
                Column(
                    // continues the statement started above: the card is full width with a gap below the heading
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp)
                        .background(WaypointCard, RoundedCornerShape(RadiusRow))
                        .padding(start = 27.dp, top = 4.dp, end = 18.dp, bottom = 14.dp),
                // closes the multi-line argument list started above
                ) {
                    // draws one meter per guarded API
                    uiState.apiUsage.forEach { snapshot -> ApiBudgetMeter(snapshot) }
                // closes the card
                }
            // closes the if block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.profile_preferences_header),`
                text = stringResource(R.string.profile_preferences_header),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
                // continues the statement started above: `modifier = Modifier.padding(top = 20.dp),`
                modifier = Modifier.padding(top = 20.dp),
            // closes the multi-line argument list started above
            )

            // declares read-only property `context`, initialised to `LocalContext.current`
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

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 16.dp, bottom = 16.dp)`
                    .padding(top = 16.dp, bottom = 16.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusRow))`
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    // continues the statement started above: `.border(1.dp, WaypointLogoutBorder, RoundedCornerShape(Radi…`
                    .border(1.dp, WaypointLogoutBorder, RoundedCornerShape(RadiusRow))
                    // continues the statement started above: `.clickable { showLogoutModal = true }`
                    .clickable { showLogoutModal = true }
                    // continues the statement started above: `.padding(vertical = 12.dp),`
                    .padding(vertical = 12.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.profile_logout),`
                    text = stringResource(R.string.profile_logout),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }

        // `if` statement: the block below runs when `showLanguageModal` is true
        if (showLanguageModal) {
            // calls `Dialog` with an argument list that continues on the following lines
            Dialog(
                // continues the statement started above: `onDismissRequest = { showLanguageModal = false },`
                onDismissRequest = { showLanguageModal = false },
                // continues the statement started above: `properties = DialogProperties(usePlatformDefaultWidth = fal…`
                properties = DialogProperties(usePlatformDefaultWidth = false),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.padding(horizontal = 16.dp),`
                        .padding(horizontal = 16.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `LanguageModal` with an argument list that continues on the following lines
                    LanguageModal(
                        // continues the statement started above: `selectedLanguage = selectedLanguage,`
                        selectedLanguage = selectedLanguage,
                        // continues the statement started above: `onLanguageSelected = { selectedLanguage = it },`
                        onLanguageSelected = { selectedLanguage = it },
                        // continues the statement started above: `onSaveClick = {`
                        onSaveClick = {
                            // calls `setApplicationLocales` on `AppCompatDelegate` with an argument list that continues on the following lines
                            AppCompatDelegate.setApplicationLocales(
                                // continues the statement started above: `LocaleListCompat.forLanguageTags(selectedLanguage.localeTag…`
                                LocaleListCompat.forLanguageTags(selectedLanguage.localeTag),
                            // closes the multi-line argument list started above
                            )
                            AppSettings.markLanguageChanged()
                            // assigns `showLanguageModal` the value `false`
                            showLanguageModal = false
                        // closes the block
                        },
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }
        // closes the if block
        }

        // `if` statement: the block below runs when `showLogoutModal` is true
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
            // calls `LogoutConfirmationModal` with an argument list that continues on the following lines
            LogoutConfirmationModal(
                // continues the statement started above: `onConfirmLogout = {`
                onConfirmLogout = {
                    // assigns `showLogoutModal` the value `false`
                    showLogoutModal = false
                    // calls `onLogoutClick` with arguments `()`
                    onLogoutClick()
                // closes the block
                },
                // continues the statement started above: `onDismiss = { showLogoutModal = false },`
                onDismiss = { showLogoutModal = false },
            // closes the multi-line argument list started above
            )
        // closes the if block
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun LogoutConfirmationModal(`
private fun LogoutConfirmationModal(
    // continues the statement started above: `onConfirmLogout: () -> Unit,`
    onConfirmLogout: () -> Unit,
    // continues the statement started above: `onDismiss: () -> Unit,`
    onDismiss: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Dialog` with an argument list that continues on the following lines
    Dialog(
        // continues the statement started above: `onDismissRequest = onDismiss,`
        onDismissRequest = onDismiss,
        // continues the statement started above: `properties = DialogProperties(usePlatformDefaultWidth = fal…`
        properties = DialogProperties(usePlatformDefaultWidth = false),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.padding(horizontal = 20.dp),`
                .padding(horizontal = 20.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = modifier`
                modifier = modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(22.dp))`
                    .background(WaypointCard, RoundedCornerShape(22.dp))
                    // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(22.dp))`
                    .border(1.dp, WaypointBorderSoft, RoundedCornerShape(22.dp))
                    // continues the statement started above: `.padding(22.dp),`
                    .padding(22.dp),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
                // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(16.dp),`
                verticalArrangement = Arrangement.spacedBy(16.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.size(56.dp)`
                        .size(56.dp)
                        // continues the statement started above: `.clip(CircleShape)`
                        .clip(CircleShape)
                        // continues the statement started above: `.background(WaypointTerracotta.copy(alpha = 0.12f)),`
                        .background(WaypointTerracotta.copy(alpha = 0.12f)),
                    // continues the statement started above: `contentAlignment = Alignment.Center,`
                    contentAlignment = Alignment.Center,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with arguments `("👋", fontSize = 28.sp)`
                    Text("👋", fontSize = 28.sp)
                // closes the block
                }

                // calls `Column` with an argument list that continues on the following lines
                Column(
                    // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                    horizontalAlignment = Alignment.CenterHorizontally,
                    // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(6.dp),`
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // Logout dialog title from strings.xml so it follows the selected language
                        text = stringResource(R.string.settings_logout_dialog_title),
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color = WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 19.sp,`
                        fontSize = 19.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                        // continues the statement started above: `textAlign = TextAlign.Center,`
                        textAlign = TextAlign.Center,
                    // closes the multi-line argument list started above
                    )

                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // Logout dialog body from strings.xml
                        text = stringResource(R.string.settings_logout_dialog_body),
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `textAlign = TextAlign.Center,`
                        textAlign = TextAlign.Center,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }

                // calls `Spacer` with arguments `(modifier = Modifier.height(4.dp))`
                Spacer(modifier = Modifier.height(4.dp))

                // calls `Column` with an argument list that continues on the following lines
                Column(
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                    // continues the statement started above: `verticalArrangement = Arrangement.spacedBy(10.dp),`
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.height(48.dp)`
                            .height(48.dp)
                            // continues the statement started above: `.clip(RoundedCornerShape(RadiusButton))`
                            .clip(RoundedCornerShape(RadiusButton))
                            // continues the statement started above: `.background(WaypointTerracotta)`
                            .background(WaypointTerracotta)
                            // continues the statement started above: `.clickable(onClick = onConfirmLogout),`
                            .clickable(onClick = onConfirmLogout),
                        // continues the statement started above: `contentAlignment = Alignment.Center,`
                        contentAlignment = Alignment.Center,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.profile_logout),`
                            text = stringResource(R.string.profile_logout),
                            // continues the statement started above: `color = White,`
                            color = White,
                            // continues the statement started above: `fontSize = 16.sp,`
                            fontSize = 16.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }

                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.height(48.dp)`
                            .height(48.dp)
                            // continues the statement started above: `.clip(RoundedCornerShape(RadiusButton))`
                            .clip(RoundedCornerShape(RadiusButton))
                            // continues the statement started above: `.background(WaypointCream)`
                            .background(WaypointCream)
                            // continues the statement started above: `.border(1.dp, WaypointBorderSoft, RoundedCornerShape(Radius…`
                            .border(1.dp, WaypointBorderSoft, RoundedCornerShape(RadiusButton))
                            // continues the statement started above: `.clickable(onClick = onDismiss),`
                            .clickable(onClick = onDismiss),
                        // continues the statement started above: `contentAlignment = Alignment.Center,`
                        contentAlignment = Alignment.Center,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // Reuses the shared Cancel string
                            text = stringResource(R.string.common_cancel),
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color = WaypointTextPrimary,
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
        // closes the block
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `StatCard` taking 3 parameters (`value`, `label`, `modifier`) and opens its body
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
            .background(WaypointCard, RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.padding(vertical = 10.dp),`
            .padding(vertical = 10.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(value, color = WaypointTextPrimary, fontSize…)`
        Text(value, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        // calls `Text` with arguments `(label, color = WaypointTextMuted, fontSize =…)`
        Text(label, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
    // closes the block
    }
// closes the function `StatCard`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun PreferenceRow(`
private fun PreferenceRow(
    // continues the statement started above: `title: String,`
    title: String,
    // continues the statement started above: `subtitle: String,`
    subtitle: String,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `onClick: (() -> Unit)? = null,`
    onClick: (() -> Unit)? = null,
    // continues the statement started above: `trailing: @Composable () -> Unit,`
    trailing: @Composable () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `rowModifier`, initialised to `modifier`
    var rowModifier = modifier
        // chained call `.fillMaxWidth` on the previous result
        .fillMaxWidth()
        // chained call `.background` on the previous result with arguments `(WaypointCard, RoundedCornerShape(Radius…)`
        .background(WaypointCard, RoundedCornerShape(RadiusRow))
    // `if` statement: the block below runs when `onClick != null` is true
    if (onClick != null) {
        // assigns `rowModifier` the value `rowModifier.clickable(onClick = onClick)`
        rowModifier = rowModifier.clickable(onClick = onClick)
    // closes the if block
    }
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = rowModifier.padding(start = 27.dp, top = 12.dp, …`
        modifier = rowModifier.padding(start = 27.dp, top = 12.dp, end = 18.dp, bottom = 12.dp),
        // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
        verticalAlignment = Alignment.CenterVertically,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with arguments `(modifier = Modifier.weight(1f))` and opens a trailing lambda / block
        Column(modifier = Modifier.weight(1f)) {
            // calls `Text` with arguments `(title, color = WaypointTextPrimary, fontSize…)`
            Text(title, color = WaypointTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
            // calls `Text` with arguments `(subtitle, color = WaypointTextMuted, fontSiz…)`
            Text(subtitle, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
        // closes the lambda passed to `Column`
        }
        // calls `trailing` with arguments `()`
        trailing()
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun PreferenceToggle(`
private fun PreferenceToggle(
    // continues the statement started above: `checked: Boolean,`
    checked: Boolean,
    // continues the statement started above: `onCheckedChange: (Boolean) -> Unit,`
    onCheckedChange: (Boolean) -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `enabled: Boolean = true,`
    enabled: Boolean = true,
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.size(width = 40.dp, height = 24.dp)`
            .size(width = 40.dp, height = 24.dp)
            // continues the statement started above: `.clickable(enabled = enabled) { onCheckedChange(!checked) }`
            .clickable(enabled = enabled) { onCheckedChange(!checked) }
            // continues the statement started above: `.background(`
            .background(
                // continues the statement started above: `when {`
                when {
                    // lambda `!enabled -> WaypointBorderSoft.copy(alpha…`
                    !enabled -> WaypointBorderSoft.copy(alpha = 0.5f)
                    // lambda `checked -> WaypointTerracotta`
                    checked -> WaypointTerracotta
                    // `else` branch of the `when`: evaluates `WaypointBorderSoft`
                    else -> WaypointBorderSoft
                // closes the block
                },
                // continues the statement started above: `RoundedCornerShape(12.dp),`
                RoundedCornerShape(12.dp),
            // closes the multi-line argument list started above
            ),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.align(if (checked) Alignment.CenterEnd else Alignment.Cent…`
                .align(if (checked) Alignment.CenterEnd else Alignment.CenterStart)
                // continues the statement started above: `.padding(horizontal = 3.dp)`
                .padding(horizontal = 3.dp)
                // continues the statement started above: `.size(18.dp)`
                .size(18.dp)
                // continues the statement started above: `.background(WaypointCard, CircleShape),`
                .background(WaypointCard, CircleShape),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ChevronValue` taking 2 parameters (`value`, `modifier`) and opens its body
private fun ChevronValue(value: String, modifier: Modifier = Modifier) {
    // calls `Row` with arguments `(modifier = modifier, verticalAlignment = Ali…)` and opens a trailing lambda / block
    Row(modifier = modifier, verticalAlignment = Alignment.CenterVertically) {
        // calls `Text` with arguments `(value, color = WaypointTextMuted, fontSize =…)`
        Text(value, color = WaypointTextMuted, fontSize = 13.sp)
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.profile_chevron_glyph),`
            text = stringResource(R.string.profile_chevron_glyph),
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 16.sp,`
            fontSize = 16.sp,
            // continues the statement started above: `modifier = Modifier.padding(start = 4.dp),`
            modifier = Modifier.padding(start = 4.dp),
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Row`
    }
// closes the function `ChevronValue`
}
// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ApiBudgetMeter` taking one parameter (`snapshot`); shows a label, a progress bar and "used / limit" for one API
private fun ApiBudgetMeter(snapshot: UsageSnapshot) {
    // declares read-only property `label`, the display name of the API
    val label = stringResource(if (snapshot.api == GuardedApi.LOCATIONIQ) R.string.api_budget_locationiq else R.string.api_budget_airlabs)
    // declares read-only property `period`, whether the limit is per day or per month
    val period = stringResource(if (snapshot.api.dailyLimit != null) R.string.api_budget_today else R.string.api_budget_this_month)
    // declares read-only property `barColor`, terracotta once the budget is 90% used and a calmer colour before that
    val barColor = if (snapshot.fraction >= 0.9f) WaypointTerracotta else WaypointTripBadgeText
    // opens a vertical stack for the meter
    Column(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
        // row holding the label on the left and the numbers on the right
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            // the API name
            Text(label, color = WaypointTextPrimary, fontSize = 13.sp)
            // the numbers, for example "1,204 / 5,000 today"
            Text("${snapshot.used} / ${snapshot.limit} $period", color = WaypointTextMuted, fontSize = 12.sp)
        // closes the row
        }
        // the progress bar showing how much of the limit is used
        LinearProgressIndicator(
            progress = { snapshot.fraction },
            color = barColor,
            trackColor = WaypointBorderSoft,
            modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        // closes the multi-line argument list started above
        )
    // closes the column
    }
// closes the function `ApiBudgetMeter`
}
