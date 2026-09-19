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
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
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
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointRadioBorderUnselected` for use in this file
import com.waypoint.app.core.theme.WaypointRadioBorderUnselected
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary

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
        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
            // expression: `AppLanguage.entries.forEach { language ->`
            AppLanguage.entries.forEach { language ->
                // continues the statement started above: `LanguageOptionRow(`
                LanguageOptionRow(
                    // continues the statement started above: `name = language.displayName,`
                    name = language.displayName,
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
// declares private function `LanguageOptionRow` taking 3 parameters (`name`, `selected`, `onClick`) and opens its body
private fun LanguageOptionRow(name: String, selected: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.background(if (selected) WaypointCream else WaypointCard, …`
            .background(if (selected) WaypointCream else WaypointCard, RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.border(`
            .border(
                // continues the statement started above: `width = 1.5.dp,`
                width = 1.5.dp,
                // continues the statement started above: `color = if (selected) WaypointTerracotta else WaypointRadio…`
                color = if (selected) WaypointTerracotta else WaypointRadioBorderUnselected,
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
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = name,`
            text = name,
            // continues the statement started above: `color = WaypointTextPrimary,`
            color = WaypointTextPrimary,
            // continues the statement started above: `fontSize = 14.sp,`
            fontSize = 14.sp,
            // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
            fontWeight = FontWeight.SemiBold,
        // closes the multi-line argument list started above
        )
        // calls `RadioIndicator` with arguments `(selected = selected)`
        RadioIndicator(selected = selected)
    // closes the block
    }
// closes the function `LanguageOptionRow`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `RadioIndicator` taking 2 parameters (`selected`, `modifier`) and opens its body
private fun RadioIndicator(selected: Boolean, modifier: Modifier = Modifier) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.size(20.dp)`
            .size(20.dp)
            // continues the statement started above: `.border(`
            .border(
                // continues the statement started above: `width = 1.5.dp,`
                width = 1.5.dp,
                // continues the statement started above: `color = if (selected) WaypointTerracotta else WaypointRadio…`
                color = if (selected) WaypointTerracotta else WaypointRadioBorderUnselected,
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
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.size(12.dp)`
                    .size(12.dp)
                    // continues the statement started above: `.background(WaypointTerracotta, CircleShape),`
                    .background(WaypointTerracotta, CircleShape),
            // closes the multi-line argument list started above
            )
        // closes the if block
        }
    // closes the block
    }
// closes the function `RadioIndicator`
}
