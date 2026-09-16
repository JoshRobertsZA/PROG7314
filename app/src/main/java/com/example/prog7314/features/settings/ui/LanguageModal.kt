package com.example.prog7314.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.prog7314.R
import com.example.prog7314.core.locale.AppLanguage
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusDeco
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointModalScrim
import com.example.prog7314.core.theme.WaypointRadioBorderUnselected
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White

/**
 * "Change language" modal, launched from the Language row on the Profile
 * tab (SettingsScreen). onSaveClick is expected to call
 * AppCompatDelegate.setApplicationLocales(...) with the selected
 * language's locale tag (see SettingsScreen's usage) - that's what
 * actually switches the app's locale, and AppCompat persists the choice
 * across restarts on its own.
 * Uses a plain Compose Dialog rather than a custom full-screen Box so it
 * gets platform back-button/scrim-tap dismissal for free.
 * usePlatformDefaultWidth = false lets the card use its own width instead
 * of the system dialog's default max width. The card is vertically
 * centered here rather than matching Figma's exact (non-centered) canvas
 * offset, which is standard modal placement, not a meaningful design
 * detail to preserve.
 */
@Composable
fun LanguageModal(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onSaveClick: () -> Unit,
    onDismissRequest: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(usePlatformDefaultWidth = false),
    ) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(WaypointModalScrim)
                .padding(horizontal = 35.dp),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(WaypointCard, RoundedCornerShape(RadiusDeco))
                    .padding(horizontal = 22.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
            ) {
                Text(
                    text = stringResource(R.string.language_modal_title),
                    color = WaypointTextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    AppLanguage.entries.forEach { language ->
                        LanguageOptionRow(
                            name = language.displayName,
                            selected = language == selectedLanguage,
                            onClick = { onLanguageSelected(language) },
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                        .clickable(onClick = onSaveClick),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.language_modal_save),
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

@Composable
private fun LanguageOptionRow(name: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(if (selected) WaypointCream else WaypointCard, RoundedCornerShape(RadiusButton))
            .border(
                width = 1.5.dp,
                color = if (selected) WaypointTerracotta else WaypointRadioBorderUnselected,
                shape = RoundedCornerShape(RadiusButton),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = name,
            color = WaypointTextPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
        )
        RadioIndicator(selected = selected)
    }
}

@Composable
private fun RadioIndicator(selected: Boolean, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(20.dp)
            .border(
                width = 1.5.dp,
                color = if (selected) WaypointTerracotta else WaypointRadioBorderUnselected,
                shape = CircleShape,
            ),
        contentAlignment = Alignment.Center,
    ) {
        if (selected) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(WaypointTerracotta, CircleShape),
            )
        }
    }
}