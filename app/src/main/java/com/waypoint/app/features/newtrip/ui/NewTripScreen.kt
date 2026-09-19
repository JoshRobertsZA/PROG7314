// declares that this file belongs to the package `com.waypoint.app.features.newtrip.ui`
package com.waypoint.app.features.newtrip.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.text.BasicTextField` for use in this file
import androidx.compose.foundation.text.BasicTextField
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
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
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.TextStyle` for use in this file
import androidx.compose.ui.text.TextStyle
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.features.home.ui.CitySearchDialog` for use in this file
import com.waypoint.app.features.home.ui.CitySearchDialog
// imports `com.waypoint.app.core.common.AppButtonFilled` for use in this file
import com.waypoint.app.core.common.AppButtonFilled
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusChip` for use in this file
import com.waypoint.app.core.theme.RadiusChip
// imports `com.waypoint.app.core.theme.RadiusHandle` for use in this file
import com.waypoint.app.core.theme.RadiusHandle
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.WaypointTripBadgeText` for use in this file
import com.waypoint.app.core.theme.WaypointTripBadgeText
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `java.time.format.DateTimeFormatter` for use in this file
import java.time.format.DateTimeFormatter
// imports `java.util.Locale` for use in this file
import java.util.Locale

// declares private read-only property `SUMMARY_FMT`, initialised with the result of calling `DateTimeFormatter.ofPattern(…)`
private val SUMMARY_FMT = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun NewTripScreen(`
fun NewTripScreen(
    // continues the statement started above: `onCloseClick: () -> Unit,`
    onCloseClick: () -> Unit,
    // continues the statement started above: `onSaveSuccess: () -> Unit = onCloseClick,`
    onSaveSuccess: () -> Unit = onCloseClick,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: NewTripViewModel = viewModel(),`
    viewModel: NewTripViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()

    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // expression: `viewModel.tripSaved.collect { onSaveSuccess() }`
        viewModel.tripSaved.collect { onSaveSuccess() }
    // closes the lambda passed to `LaunchedEffect`
    }

    // `if` statement: the block below runs when `uiState.showDestSearch` is true
    if (uiState.showDestSearch) {
        // calls `CitySearchDialog` with an argument list that continues on the following lines
        CitySearchDialog(
            // continues the statement started above: `onCitySelected = viewModel::onDestinationSelected,`
            onCitySelected = viewModel::onDestinationSelected,
            // continues the statement started above: `onDismiss = viewModel::onDismissDestSearch,`
            onDismiss      = viewModel::onDismissDestSearch,
        // closes the multi-line argument list started above
        )
    // closes the if block
    }

    // `if` statement: the block below runs when `uiState.showYearPicker` is true
    if (uiState.showYearPicker) {
        // calls `YearMonthPicker` with an argument list that continues on the following lines
        YearMonthPicker(
            // continues the statement started above: `currentDisplay = uiState.displayMonth,`
            currentDisplay = uiState.displayMonth,
            // continues the statement started above: `onMonthPicked = viewModel::onYearMonthPicked,`
            onMonthPicked  = viewModel::onYearMonthPicked,
            // continues the statement started above: `onDismiss = viewModel::onDismissYearPicker,`
            onDismiss      = viewModel::onDismissYearPicker,
        // closes the multi-line argument list started above
        )
    // closes the if block
    }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars),`
            .windowInsetsPadding(WindowInsets.systemBars),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.verticalScroll(rememberScrollState())`
                .verticalScroll(rememberScrollState())
                // continues the statement started above: `.padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = …`
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 88.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.align(Alignment.CenterHorizontally)`
                    .align(Alignment.CenterHorizontally)
                    // continues the statement started above: `.padding(bottom = 4.dp)`
                    .padding(bottom = 4.dp)
                    // continues the statement started above: `.background(`
                    .background(
                        // continues the statement started above: `WaypointBorderSoft,`
                        WaypointBorderSoft,
                        // continues the statement started above: `RoundedCornerShape(RadiusHandle),`
                        RoundedCornerShape(RadiusHandle),
                    // closes the multi-line argument list started above
                    )
                    // continues the statement started above: `.padding(horizontal = 20.dp, vertical = 2.dp),`
                    .padding(horizontal = 20.dp, vertical = 2.dp),
            // closes the multi-line argument list started above
            )

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 12.dp),`
                    .padding(top = 12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.new_trip_title),`
                    text = stringResource(R.string.new_trip_title),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 17.sp,`
                    fontSize = 17.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                    modifier = Modifier.align(Alignment.Center),
                // closes the multi-line argument list started above
                )
                // calls `CircleIconButton` with an argument list that continues on the following lines
                CircleIconButton(
                    // continues the statement started above: `onClick = onCloseClick,`
                    onClick = onCloseClick,
                    // continues the statement started above: `modifier = Modifier.align(Alignment.CenterEnd),`
                    modifier = Modifier.align(Alignment.CenterEnd),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.new_trip_close_glyph),`
                        text = stringResource(R.string.new_trip_close_glyph),
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color = WaypointTerracotta,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.new_trip_name_label),`
                text = stringResource(R.string.new_trip_name_label),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.padding(top = 20.dp),`
                modifier = Modifier.padding(top = 20.dp),
            // closes the multi-line argument list started above
            )
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 8.dp)`
                    .padding(top = 8.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 13.dp),`
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // `if` statement: the block below runs when `uiState.tripName.isEmpty()` is true
                if (uiState.tripName.isEmpty()) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.new_trip_name_hint),`
                        text = stringResource(R.string.new_trip_name_hint),
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                    // closes the multi-line argument list started above
                    )
                // closes the if block
                }
                // calls `BasicTextField` with an argument list that continues on the following lines
                BasicTextField(
                    // continues the statement started above: `value = uiState.tripName,`
                    value = uiState.tripName,
                    // continues the statement started above: `onValueChange = viewModel::onTripNameChanged,`
                    onValueChange = viewModel::onTripNameChanged,
                    // continues the statement started above: `textStyle = TextStyle(color = WaypointTextPrimary, fontSize…`
                    textStyle = TextStyle(color = WaypointTextPrimary, fontSize = 13.sp),
                    // continues the statement started above: `singleLine = true,`
                    singleLine = true,
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                // closes the multi-line argument list started above
                )
            // closes the block
            }


            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.new_trip_destination_label),`
                text = stringResource(R.string.new_trip_destination_label),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.padding(top = 20.dp),`
                modifier = Modifier.padding(top = 20.dp),
            // closes the multi-line argument list started above
            )
            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 8.dp)`
                    .padding(top = 8.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.clickable(enabled = !uiState.isGeocodingDest) { viewModel.…`
                    .clickable(enabled = !uiState.isGeocodingDest) { viewModel.onShowDestSearch() }
                    // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 13.dp),`
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                // continues the statement started above: `verticalAlignment = androidx.compose.ui.Alignment.CenterVer…`
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = "📍",`
                    text = "📍",
                    // continues the statement started above: `fontSize = 14.sp,`
                    fontSize = 14.sp,
                    // continues the statement started above: `modifier = Modifier.padding(end = 8.dp),`
                    modifier = Modifier.padding(end = 8.dp),
                // closes the multi-line argument list started above
                )
                // `if` statement: the block below runs when `uiState.isGeocodingDest` is true
                if (uiState.isGeocodingDest) {
                    // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                    CircularProgressIndicator(
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color    = WaypointTerracotta,
                        // continues the statement started above: `modifier = Modifier.size(16.dp),`
                        modifier = Modifier.size(16.dp),
                        // continues the statement started above: `strokeWidth = 2.dp,`
                        strokeWidth = 2.dp,
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = uiState.destinationName.ifBlank { "e.g. Cape Town, S…`
                        text = uiState.destinationName.ifBlank { "e.g. Cape Town, South Africa" },
                        // continues the statement started above: `color = if (uiState.destinationName.isBlank()) WaypointText…`
                        color = if (uiState.destinationName.isBlank()) WaypointTextMuted else WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                    // closes the multi-line argument list started above
                    )
                // closes the else branch
                }
            // closes the block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.new_trip_dates_label),`
                text = stringResource(R.string.new_trip_dates_label),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.padding(top = 20.dp),`
                modifier = Modifier.padding(top = 20.dp),
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.new_trip_dates_subtitle),`
                text = stringResource(R.string.new_trip_dates_subtitle),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 10.sp,`
                fontSize = 10.sp,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 2.dp)`
                    .padding(top = 2.dp)
                    // continues the statement started above: `.alpha(0.8f),`
                    .alpha(0.8f),
            // closes the multi-line argument list started above
            )

            // calls `CalendarGrid` with an argument list that continues on the following lines
            CalendarGrid(
                // continues the statement started above: `uiState = uiState,`
                uiState      = uiState,
                // continues the statement started above: `onDayTapped = viewModel::onDayTapped,`
                onDayTapped  = viewModel::onDayTapped,
                // continues the statement started above: `onPrevMonth = viewModel::onPrevMonth,`
                onPrevMonth  = viewModel::onPrevMonth,
                // continues the statement started above: `onNextMonth = viewModel::onNextMonth,`
                onNextMonth  = viewModel::onNextMonth,
                // continues the statement started above: `onHeaderTap = viewModel::onShowYearPicker,`
                onHeaderTap  = viewModel::onShowYearPicker,
                // continues the statement started above: `modifier = Modifier.padding(top = 16.dp),`
                modifier     = Modifier.padding(top = 16.dp),
            // closes the multi-line argument list started above
            )

            // calls `AppButtonFilled` with an argument list that continues on the following lines
            AppButtonFilled(
                // continues the statement started above: `text = if (uiState.isSaving) "Saving…" else stringResource(…`
                text     = if (uiState.isSaving) "Saving…" else stringResource(R.string.new_trip_save_button),
                // continues the statement started above: `onClick = viewModel::saveTrip,`
                onClick  = viewModel::saveTrip,
                // continues the statement started above: `enabled = uiState.canSave && !uiState.isSaving,`
                enabled  = uiState.canSave && !uiState.isSaving,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 20.dp),`
                    .padding(top = 20.dp),
            // closes the multi-line argument list started above
            )

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.new_trip_footer_hint),`
                text = stringResource(R.string.new_trip_footer_hint),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 10.sp,`
                fontSize = 10.sp,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 12.dp)`
                    .padding(top = 12.dp)
                    // continues the statement started above: `.alpha(0.85f),`
                    .alpha(0.85f),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

        // calls `TripSummaryBar` with an argument list that continues on the following lines
        TripSummaryBar(
            // continues the statement started above: `uiState = uiState,`
            uiState  = uiState,
            // continues the statement started above: `modifier = Modifier.align(Alignment.BottomCenter),`
            modifier = Modifier.align(Alignment.BottomCenter),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun TripSummaryBar(`
private fun TripSummaryBar(
    // continues the statement started above: `uiState: NewTripUiState,`
    uiState: NewTripUiState,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `text`, initialised to `when` and opens a lambda / block
    val text = when {
        // assigns `uiState.startDate` the value `= null ->`
        uiState.startDate == null ->
            // continues the statement started above: `"Tap a day to set your start date"`
            "Tap a day to set your start date"
        // assigns `uiState.endDate` the value `= null ->`
        uiState.endDate == null ->
            // continues the statement started above: `"${uiState.startDate.format(SUMMARY_FMT)} selected — tap an…`
            "${uiState.startDate.format(SUMMARY_FMT)} selected — tap another day for end date"
        // `else` branch of the `when`: opens a block
        else -> {
            // declares read-only property `start`, initialised with the result of calling `uiState.startDate.format(…)`
            val start = uiState.startDate.format(SUMMARY_FMT)
            // declares read-only property `end`, initialised with the result of calling `uiState.endDate.format(…)`
            val end   = uiState.endDate.format(SUMMARY_FMT)
            // declares read-only property `days`, initialised to `uiState.selectedDayCount`
            val days  = uiState.selectedDayCount
            // statement: `"$start – $end · " + androidx.compose.ui.res.pluralStringResourc…`
            "$start – $end · " + androidx.compose.ui.res.pluralStringResource(R.plurals.day_count, days, days)
        // closes the when else-branch
        }
    // closes the lambda assigned to `text`
    }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.background(WaypointCard)`
            .background(WaypointCard)
            // continues the statement started above: `.padding(horizontal = 22.dp, vertical = 14.dp),`
            .padding(horizontal = 22.dp, vertical = 14.dp),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = text,`
            text = text,
            // continues the statement started above: `color = WaypointTripBadgeText,`
            color = WaypointTripBadgeText,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
            // continues the statement started above: `textAlign = TextAlign.Center,`
            textAlign = TextAlign.Center,
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}
