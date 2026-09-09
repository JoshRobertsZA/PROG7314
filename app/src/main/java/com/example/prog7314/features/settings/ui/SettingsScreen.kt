package com.example.prog7314.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.example.prog7314.R
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.locale.AppLanguage
import com.example.prog7314.core.theme.RadiusTogglePill
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary

/**
 * Settings screen. Minimal skeleton whose only job is to display the
 * screen and let the user go back - the notifications toggle and logout
 * row are still static/mock content, not wired up yet. The language row
 * now opens LanguageModal (see LanguageModal.kt); selection is held in
 * local state here but not persisted anywhere.
 * preference), the logout row (sign the user out and navigate to login),
 * the selected language (persist it and apply the app's actual locale),
 * and the top bell button (once there's a notifications feed/screen to
 * open), once settings persistence is wired up on its own branch.
 */
@Composable
fun SettingsScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var selectedLanguage by remember { mutableStateOf(AppLanguage.current()) }
    var showLanguageModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 28.dp, top = 64.dp, end = 28.dp, bottom = 36.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // TopBar: back button, centered title, bell shortcut
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.settings_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = stringResource(R.string.settings_title),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
            // TODO: not wired to a notifications feed/screen yet
            Icon(
                painter = painterResource(R.drawable.ic_bell),
                contentDescription = stringResource(R.string.settings_bell_cd),
                tint = WaypointTextPrimary,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(24.dp)
                    .clickable(onClick = {}),
            )
        }

        // Notifications row
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_bell), null, tint = WaypointTextPrimary, modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.settings_row_notifications),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
            )
            // Toggle: off state (thumb at start). Hand-built from a
            // track + thumb shape rather than a real Switch widget,
            // matching the source XML.
            Box(
                modifier = Modifier
                    .size(width = 51.dp, height = 25.dp)
                    .clickable(onClick = {}),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(WaypointBorderSoft, RoundedCornerShape(RadiusTogglePill)),
                )
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 2.dp)
                        .size(21.dp)
                        .background(WaypointCard, CircleShape),
                )
            }
        }

        // Logout row
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .clickable(onClick = {}),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_logout), null, tint = WaypointTextPrimary, modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.settings_row_logout),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
            )
        }

        // Language row: shows the currently selected language and opens
        // LanguageModal on tap.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
                .clickable(onClick = { showLanguageModal = true }),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(painterResource(R.drawable.ic_globe), null, tint = WaypointTextPrimary, modifier = Modifier.size(24.dp))
            Text(
                text = stringResource(R.string.settings_row_language),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                modifier = Modifier.weight(1f).padding(start = 16.dp),
            )
            Text(
                text = selectedLanguage.displayName,
                color = WaypointTextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(end = 6.dp),
            )
            Text(
                text = stringResource(R.string.settings_language_expand_glyph),
                color = WaypointTextMuted,
                fontSize = 14.sp,
                modifier = Modifier.alpha(0.6f),
            )
        }
    }

    if (showLanguageModal) {
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
            onDismissRequest = { showLanguageModal = false },
        )
    }
}