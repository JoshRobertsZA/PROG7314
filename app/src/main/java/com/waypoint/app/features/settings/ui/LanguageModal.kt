package com.waypoint.app.features.settings.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.common.SelectionModal
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

private fun languageFlag(language: AppLanguage): String = when (language) {
    AppLanguage.ENGLISH -> "🇬🇧"
    AppLanguage.ZULU -> "🇿🇦"
    AppLanguage.XHOSA -> "🇿🇦"
}

@Composable
private fun languageSubLabel(language: AppLanguage): String = when (language) {
    AppLanguage.ENGLISH -> stringResource(R.string.language_sub_english)
    AppLanguage.ZULU -> stringResource(R.string.language_sub_zulu)
    AppLanguage.XHOSA -> stringResource(R.string.language_sub_xhosa)
}

/** Language picker opened from Settings. */
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
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            AppLanguage.entries.forEach { language ->
                SelectionOptionRow(
                    flag = languageFlag(language),
                    title = language.displayName,
                    subtitle = languageSubLabel(language),
                    selected = language == selectedLanguage,
                    onClick = { onLanguageSelected(language) },
                )
            }
        }
    }
}

/** One radio-style option, shared by the language and home-currency pickers. */
@Composable
internal fun SelectionOptionRow(
    flag: String,
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(RadiusButton))
            .background(
                if (selected) WaypointTerracotta.copy(alpha = 0.12f)
                else WaypointCream
            )
            .border(
                width = if (selected) 1.5.dp else 1.dp,
                color = if (selected) WaypointTerracotta else WaypointBorderSoft,
                shape = RoundedCornerShape(RadiusButton),
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.weight(1f),
        ) {
            Text(text = flag, fontSize = 24.sp)
            Spacer(modifier = Modifier.width(14.dp))
            Column {
                Text(
                    text = title,
                    color = if (selected) WaypointTerracotta else WaypointTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = subtitle,
                    color = WaypointTextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        Box(
            modifier = Modifier
                .size(24.dp)
                .clip(CircleShape)
                .background(
                    if (selected) WaypointTerracotta else WaypointCream
                )
                .border(
                    width = if (selected) 0.dp else 1.5.dp,
                    color = if (selected) WaypointTerracotta else WaypointBorderSoft,
                    shape = CircleShape,
                ),
            contentAlignment = Alignment.Center,
        ) {
            if (selected) {
                Text(text = "✓", color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}
