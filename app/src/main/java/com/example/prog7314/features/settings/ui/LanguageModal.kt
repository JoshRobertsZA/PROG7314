package com.example.prog7314.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import com.example.prog7314.R
import com.example.prog7314.core.common.SelectionModal
import com.example.prog7314.core.locale.AppLanguage
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointRadioBorderUnselected
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextPrimary

/**
 * "Change language" modal, launched from the Language row on the Profile
 * tab (SettingsScreen). onSaveClick is expected to call
 * AppCompatDelegate.setApplicationLocales(...) with the selected
 * language's locale tag (see SettingsScreen's usage) - that's what
 * actually switches the app's locale, and AppCompat persists the choice
 * across restarts on its own.
 * Renders only the card itself - hosted in a plain
 * androidx.compose.ui.window.Dialog by the caller (see SettingsScreen.kt),
 * same as CurrencyExchangeModal. That gives it Android's own full-screen
 * dim/scrim (which correctly extends behind the status/nav bars) instead
 * of hand-rolling one, which is what a previous version of this modal did
 * and which left the real screen background visible in thin strips above
 * and below the card.
 * The card itself is core/common/SelectionModal - the same shell the
 * Currency Change modal uses (Figma 392:12 vs this modal's 394:62 are
 * the same title+content+Save-button card) - only the content slot
 * (the language rows below) differs.
 */
@Composable
fun LanguageModal(
    selectedLanguage: AppLanguage,
    onLanguageSelected: (AppLanguage) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    SelectionModal(
        title = stringResource(R.string.language_modal_title),
        onSaveClick = onSaveClick,
        modifier = modifier,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            AppLanguage.entries.forEach { language ->
                LanguageOptionRow(
                    name = language.displayName,
                    selected = language == selectedLanguage,
                    onClick = { onLanguageSelected(language) },
                )
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
