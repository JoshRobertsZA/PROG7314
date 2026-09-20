// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

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
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.SelectionModal` for use in this file
import com.waypoint.app.core.common.SelectionModal
// imports `com.waypoint.app.core.locale.AppLanguage` for use in this file
import com.waypoint.app.core.locale.AppLanguage
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// declares private function `languageFlag` taking 1 parameter (`language`), returning `String`; its body is the expression `when (language) {`
private fun languageFlag(language: AppLanguage): String = when (language) {
    // lambda `AppLanguage.ENGLISH -> "🇬🇧"`
    AppLanguage.ENGLISH -> "🇬🇧"
    // lambda `AppLanguage.ZULU -> "🇿🇦"`
    AppLanguage.ZULU -> "🇿🇦"
    // lambda `AppLanguage.XHOSA -> "🇿🇦"`
    AppLanguage.XHOSA -> "🇿🇦"
// closes the block
}

// declares private function `languageSubLabel` taking 1 parameter (`language`), returning `String`; its body is the expression `when (language) {`
private fun languageSubLabel(language: AppLanguage): String = when (language) {
    // lambda `AppLanguage.ENGLISH -> "English (Default)"`
    AppLanguage.ENGLISH -> "English (Default)"
    // lambda `AppLanguage.ZULU -> "isiZulu (South Africa)"`
    AppLanguage.ZULU -> "isiZulu (South Africa)"
    // lambda `AppLanguage.XHOSA -> "isiXhosa (South Africa)"`
    AppLanguage.XHOSA -> "isiXhosa (South Africa)"
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun LanguageModal(`
fun LanguageModal(
    // continues the statement started above: `selectedLanguage: AppLanguage,`
    selectedLanguage: AppLanguage,
    // continues the statement started above: `onLanguageSelected: (AppLanguage) -> Unit,`
    onLanguageSelected: (AppLanguage) -> Unit,
    // continues the statement started above: `onSaveClick: () -> Unit,`
    onSaveClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `SelectionModal` with an argument list that continues on the following lines
    SelectionModal(
        // continues the statement started above: `title = stringResource(R.string.language_modal_title),`
        title = stringResource(R.string.language_modal_title),
        // continues the statement started above: `onSaveClick = onSaveClick,`
        onSaveClick = onSaveClick,
        // continues the statement started above: `modifier = modifier,`
        modifier = modifier,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with arguments `(verticalArrangement = Arrangement.spacedBy(1…)` and opens a trailing lambda / block
        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            // expression: `AppLanguage.entries.forEach { language ->`
            AppLanguage.entries.forEach { language ->
                // continues the statement started above: `LanguageOptionRow(`
                LanguageOptionRow(
                    // continues the statement started above: `language = language,`
                    language = language,
                    // continues the statement started above: `selected = language == selectedLanguage,`
                    selected = language == selectedLanguage,
                    // continues the statement started above: `onClick = { onLanguageSelected(language) },`
                    onClick = { onLanguageSelected(language) },
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the lambda passed to `Column`
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun LanguageOptionRow(`
private fun LanguageOptionRow(
    // continues the statement started above: `language: AppLanguage,`
    language: AppLanguage,
    // continues the statement started above: `selected: Boolean,`
    selected: Boolean,
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.clip(RoundedCornerShape(RadiusButton))`
            .clip(RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.background(`
            .background(
                // continues the statement started above: `if (selected) WaypointTerracotta.copy(alpha = 0.12f)`
                if (selected) WaypointTerracotta.copy(alpha = 0.12f)
                // continues the statement started above: `else WaypointCream`
                else WaypointCream
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.border(`
            .border(
                // continues the statement started above: `width = if (selected) 1.5.dp else 1.dp,`
                width = if (selected) 1.5.dp else 1.dp,
                // continues the statement started above: `color = if (selected) WaypointTerracotta else WaypointBorde…`
                color = if (selected) WaypointTerracotta else WaypointBorderSoft,
                // continues the statement started above: `shape = RoundedCornerShape(RadiusButton),`
                shape = RoundedCornerShape(RadiusButton),
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.clickable(onClick = onClick)`
            .clickable(onClick = onClick)
            // continues the statement started above: `.padding(horizontal = 16.dp, vertical = 14.dp),`
            .padding(horizontal = 16.dp, vertical = 14.dp),
        // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
        horizontalArrangement = Arrangement.SpaceBetween,
        // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
        verticalAlignment = Alignment.CenterVertically,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
            // continues the statement started above: `modifier = Modifier.weight(1f),`
            modifier = Modifier.weight(1f),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with arguments `(text = languageFlag(language), fontSize = 24…)`
            Text(text = languageFlag(language), fontSize = 24.sp)
            // calls `Spacer` with arguments `(modifier = Modifier.width(14.dp))`
            Spacer(modifier = Modifier.width(14.dp))
            // opens a block after `Column`
            Column {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = language.displayName,`
                    text = language.displayName,
                    // continues the statement started above: `color = if (selected) WaypointTerracotta else WaypointTextP…`
                    color = if (selected) WaypointTerracotta else WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = languageSubLabel(language),`
                    text = languageSubLabel(language),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 12.sp,`
                    fontSize = 12.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Medium,`
                    fontWeight = FontWeight.Medium,
                    // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                    modifier = Modifier.padding(top = 2.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }

        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.size(24.dp)`
                .size(24.dp)
                // continues the statement started above: `.clip(CircleShape)`
                .clip(CircleShape)
                // continues the statement started above: `.background(`
                .background(
                    // continues the statement started above: `if (selected) WaypointTerracotta else WaypointCream`
                    if (selected) WaypointTerracotta else WaypointCream
                // closes the multi-line argument list started above
                )
                // continues the statement started above: `.border(`
                .border(
                    // continues the statement started above: `width = if (selected) 0.dp else 1.5.dp,`
                    width = if (selected) 0.dp else 1.5.dp,
                    // continues the statement started above: `color = if (selected) WaypointTerracotta else WaypointBorde…`
                    color = if (selected) WaypointTerracotta else WaypointBorderSoft,
                    // continues the statement started above: `shape = CircleShape,`
                    shape = CircleShape,
                // closes the multi-line argument list started above
                ),
            // continues the statement started above: `contentAlignment = Alignment.Center,`
            contentAlignment = Alignment.Center,
        // ends the argument list started above and opens the block that follows
        ) {
            // `if` statement: the block below runs when `selected` is true
            if (selected) {
                // calls `Text` with arguments `(text = "✓", color = White, fontSize = 14.sp,…)`
                Text(text = "✓", color = White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
            // closes the if block
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}
