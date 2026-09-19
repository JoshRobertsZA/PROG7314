// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

// imports `androidx.appcompat.app.AppCompatDelegate` for use in this file
import androidx.appcompat.app.AppCompatDelegate
// imports `coil.compose.AsyncImage` for use in this file
import coil.compose.AsyncImage
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
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
import androidx.compose.ui.window.DialogProperties
// imports `androidx.core.os.LocaleListCompat` for use in this file
import androidx.core.os.LocaleListCompat
// imports `androidx.fragment.app.FragmentActivity` for use in this file
import androidx.fragment.app.FragmentActivity
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.auth.BiometricAuthenticator` for use in this file
import com.waypoint.app.core.auth.BiometricAuthenticator
// imports `com.waypoint.app.core.auth.BiometricAvailability` for use in this file
import com.waypoint.app.core.auth.BiometricAvailability
// imports `com.waypoint.app.core.auth.BiometricLockPreferences` for use in this file
import com.waypoint.app.core.auth.BiometricLockPreferences
// imports `com.waypoint.app.core.common.TabHeader` for use in this file
import com.waypoint.app.core.common.TabHeader
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.core.notifications.NotificationPreferences` for use in this file
import com.waypoint.app.core.notifications.NotificationPreferences
// imports `com.waypoint.app.core.locale.AppLanguage` for use in this file
import com.waypoint.app.core.locale.AppLanguage
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
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

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun SettingsScreen(`
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onLogoutClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    viewModel: SettingsViewModel = viewModel(),
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()
    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.loadTripCounts() }

    var selectedLanguage by remember { mutableStateOf(AppLanguage.current()) }
    var showLanguageModal by remember { mutableStateOf(false) }
    var showLogoutModal by remember { mutableStateOf(false) }

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
            TabHeader(showAvatar = false, onBellClick = onNotificationsClick)

            // calls `AsyncImage` with an argument list that continues on the following lines
            AsyncImage(
                // continues the statement started above: `model = SessionManager.photoUrl.ifBlank { null },`
                model = SessionManager.photoUrl.ifBlank { null },
                // continues the statement started above: `contentDescription = stringResource(R.string.tab_header_ava…`
                contentDescription = stringResource(R.string.tab_header_avatar_cd),
                // continues the statement started above: `contentScale = ContentScale.Crop,`
                contentScale = ContentScale.Crop,
                // continues the statement started above: `placeholder = painterResource(R.drawable.img_mock_avatar),`
                placeholder = painterResource(R.drawable.img_mock_avatar),
                // continues the statement started above: `error = painterResource(R.drawable.img_mock_avatar),`
                error = painterResource(R.drawable.img_mock_avatar),
                // continues the statement started above: `fallback = painterResource(R.drawable.img_mock_avatar),`
                fallback = painterResource(R.drawable.img_mock_avatar),
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
                // continues the statement started above: `text = SessionManager.displayName.ifBlank { "Alex Carter" },`
                text = SessionManager.displayName.ifBlank { "Alex Carter" },
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
                text = SessionManager.email.ifBlank { "alex.carter@email.com" },
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
            // declares mutable property `notificationsEnabled`, delegated to `remember { mutableStateOf(NotificationP…`
            var notificationsEnabled by remember { mutableStateOf(NotificationPreferences.isEnabled(context)) }
            // calls `PreferenceRow` with an argument list that continues on the following lines
            PreferenceRow(
                // continues the statement started above: `title = stringResource(R.string.profile_notifications_title…`
                title = stringResource(R.string.profile_notifications_title),
                // continues the statement started above: `subtitle = stringResource(R.string.profile_notifications_su…`
                subtitle = stringResource(R.string.profile_notifications_subtitle),
                // continues the statement started above: `modifier = Modifier.padding(top = 12.dp),`
                modifier = Modifier.padding(top = 12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `PreferenceToggle` with an argument list that continues on the following lines
                PreferenceToggle(
                    // continues the statement started above: `checked = notificationsEnabled,`
                    checked = notificationsEnabled,
                    // continues the statement started above: `onCheckedChange = {`
                    onCheckedChange = {
                        // assigns `notificationsEnabled` the value `it`
                        notificationsEnabled = it
                        // calls `setEnabled` on `NotificationPreferences` with arguments `(context, it)`
                        NotificationPreferences.setEnabled(context, it)
                    // closes the block
                    },
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `PreferenceRow` with an argument list that continues on the following lines
            PreferenceRow(
                // continues the statement started above: `title = stringResource(R.string.profile_language_title),`
                title = stringResource(R.string.profile_language_title),
                // continues the statement started above: `subtitle = stringResource(R.string.profile_language_subtitl…`
                subtitle = stringResource(R.string.profile_language_subtitle),
                // continues the statement started above: `onClick = { showLanguageModal = true },`
                onClick = { showLanguageModal = true },
                // continues the statement started above: `modifier = Modifier.padding(top = 12.dp),`
                modifier = Modifier.padding(top = 12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `ChevronValue` with arguments `(value = selectedLanguage.displayName)`
                ChevronValue(value = selectedLanguage.displayName)
            // closes the block
            }

            // declares read-only property `biometricAvailability`, initialised to `remember { BiometricAuthenticator.availabili…`
            val biometricAvailability = remember { BiometricAuthenticator.availability(context) }
            // declares mutable property `biometricEnabled`, delegated to `remember { mutableStateOf(BiometricLock…`
            var biometricEnabled by remember { mutableStateOf(BiometricLockPreferences.isEnabled(context)) }
            // declares read-only property `biometricSubtitle`, initialised with the result of calling `when(…)` and opens a lambda / block
            val biometricSubtitle = when (biometricAvailability) {
                // lambda `BiometricAvailability.AVAILAB… -> stringResource(R.string.profi…`
                BiometricAvailability.AVAILABLE -> stringResource(R.string.profile_biometric_subtitle)
                // lambda `BiometricAvailability.NONE_EN… -> stringResource(R.string.profi…`
                BiometricAvailability.NONE_ENROLLED -> stringResource(R.string.profile_biometric_unavailable_none_enrolled)
                // `else` branch of the `when`: evaluates `stringResource(R.string.profile_biometric_unavailable_no_hardware)`
                else -> stringResource(R.string.profile_biometric_unavailable_no_hardware)
            // closes the lambda assigned to `biometricSubtitle`
            }
            // declares read-only property `enableTitle`, initialised with the result of calling `stringResource(…)`
            val enableTitle = stringResource(R.string.profile_biometric_enable_title)
            // declares read-only property `enableSubtitle`, initialised with the result of calling `stringResource(…)`
            val enableSubtitle = stringResource(R.string.profile_biometric_enable_subtitle)
            // declares read-only property `cancelText`, initialised with the result of calling `stringResource(…)`
            val cancelText = stringResource(R.string.biometric_lock_cancel)
            // calls `PreferenceRow` with an argument list that continues on the following lines
            PreferenceRow(
                // continues the statement started above: `title = stringResource(R.string.profile_biometric_title),`
                title = stringResource(R.string.profile_biometric_title),
                // continues the statement started above: `subtitle = biometricSubtitle,`
                subtitle = biometricSubtitle,
                // continues the statement started above: `modifier = Modifier.padding(top = 12.dp),`
                modifier = Modifier.padding(top = 12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `PreferenceToggle` with an argument list that continues on the following lines
                PreferenceToggle(
                    // continues the statement started above: `checked = biometricEnabled,`
                    checked = biometricEnabled,
                    // continues the statement started above: `enabled = biometricAvailability == BiometricAvailability.AV…`
                    enabled = biometricAvailability == BiometricAvailability.AVAILABLE,
                    // continues the statement started above: `onCheckedChange = { turningOn ->`
                    onCheckedChange = { turningOn ->
                        // continues the statement started above: `if (!turningOn) {`
                        if (!turningOn) {
                            // assigns `biometricEnabled` the value `false`
                            biometricEnabled = false
                            // calls `setEnabled` on `BiometricLockPreferences` with arguments `(context, false)`
                            BiometricLockPreferences.setEnabled(context, false)
                            // expression: `return@PreferenceToggle`
                            return@PreferenceToggle
                        // closes the block
                        }
                        // calls `authenticate` on `BiometricAuthenticator` with an argument list that continues on the following lines
                        BiometricAuthenticator.authenticate(
                            // continues the statement started above: `activity = context as FragmentActivity,`
                            activity = context as FragmentActivity,
                            // continues the statement started above: `title = enableTitle,`
                            title = enableTitle,
                            // continues the statement started above: `subtitle = enableSubtitle,`
                            subtitle = enableSubtitle,
                            // continues the statement started above: `negativeButtonText = cancelText,`
                            negativeButtonText = cancelText,
                            // continues the statement started above: `onSuccess = {`
                            onSuccess = {
                                // assigns `biometricEnabled` the value `true`
                                biometricEnabled = true
                                // calls `setEnabled` on `BiometricLockPreferences` with arguments `(context, true)`
                                BiometricLockPreferences.setEnabled(context, true)
                            // closes the block
                            },
                            // continues the statement started above: `onError = { _, _ -> },`
                            onError = { _, _ ->  },
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    },
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
                    // continues the statement started above: `.padding(top = 16.dp, bottom = 16.dp)`
                    .padding(top = 16.dp, bottom = 16.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusRow))`
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    .border(1.dp, WaypointLogoutBorder, RoundedCornerShape(RadiusRow))
                    .clickable { showLogoutModal = true }
                    .padding(vertical = 12.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
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
                            showLanguageModal = false
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
                // Exit / Logout Icon Badge
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .clip(CircleShape)
                        .background(WaypointTerracotta.copy(alpha = 0.12f)),
                    contentAlignment = Alignment.Center,
                ) {
                    Text("👋", fontSize = 28.sp)
                }

                // Title & Description
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp),
                ) {
                    Text(
                        text = "Log out of Waypoint?",
                        color = WaypointTextPrimary,
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                    )

                    Text(
                        text = "Are you sure you want to log out? You can sign back in anytime to access your trips and saved itineraries.",
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Action Buttons
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    // Confirm Logout Button
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

                    // Cancel Button
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
                            text = "Cancel",
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