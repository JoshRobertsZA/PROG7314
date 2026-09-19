// declares that this file belongs to the package `com.waypoint.app.features.tripcalendar.ui`
package com.waypoint.app.features.tripcalendar.ui

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
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
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
// imports `androidx.compose.material3.AlertDialog` for use in this file
import androidx.compose.material3.AlertDialog
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.Scaffold` for use in this file
import androidx.compose.material3.Scaffold
// imports `androidx.compose.material3.Snackbar` for use in this file
import androidx.compose.material3.Snackbar
// imports `androidx.compose.material3.SnackbarHost` for use in this file
import androidx.compose.material3.SnackbarHost
// imports `androidx.compose.material3.SnackbarHostState` for use in this file
import androidx.compose.material3.SnackbarHostState
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.material3.TextButton` for use in this file
import androidx.compose.material3.TextButton
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch
// imports `androidx.compose.runtime.rememberCoroutineScope` for use in this file
import androidx.compose.runtime.rememberCoroutineScope
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
// imports `androidx.compose.ui.res.pluralStringResource` for use in this file
import androidx.compose.ui.res.pluralStringResource
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
// imports `com.waypoint.app.core.common.AppButtonFilled` for use in this file
import com.waypoint.app.core.common.AppButtonFilled
// imports `com.waypoint.app.core.common.AppButtonOutline` for use in this file
import com.waypoint.app.core.common.AppButtonOutline
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusCard` for use in this file
import com.waypoint.app.core.theme.RadiusCard
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointDayMuted` for use in this file
import com.waypoint.app.core.theme.WaypointDayMuted
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `com.waypoint.app.features.home.ui.CitySearchDialog` for use in this file
import com.waypoint.app.features.home.ui.CitySearchDialog
// imports `com.waypoint.app.features.newtrip.ui.CalendarGrid` for use in this file
import com.waypoint.app.features.newtrip.ui.CalendarGrid
// imports `com.waypoint.app.features.newtrip.ui.NewTripUiState` for use in this file
import com.waypoint.app.features.newtrip.ui.NewTripUiState
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `com.waypoint.app.core.theme.RadiusDeco` for use in this file
import com.waypoint.app.core.theme.RadiusDeco
// imports `com.waypoint.app.core.theme.WaypointLogoutBorder` for use in this file
import com.waypoint.app.core.theme.WaypointLogoutBorder
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `java.time.format.DateTimeFormatter` for use in this file
import java.time.format.DateTimeFormatter

// expression: `private enum class DayState { BLANK, MUTED, RANGE, SELECTED }`
private enum class DayState { BLANK, MUTED, RANGE, SELECTED }
// declares private data class `Day` with a primary constructor taking 3 parameters (`number`, `state`, `date`)
private data class Day(val number: Int?, val state: DayState, val date: LocalDate? = null)

// expression: `private fun buildCalendarGrid(`
private fun buildCalendarGrid(
    // continues the statement started above: `month: YearMonth,`
    month: YearMonth,
    // continues the statement started above: `start: LocalDate?,`
    start: LocalDate?,
    // continues the statement started above: `end: LocalDate?,`
    end: LocalDate?,
    // continues the statement started above: `selected: Set<LocalDate>,`
    selected: Set<LocalDate>,
// continues the statement started above: `): List<List<Day>> {`
): List<List<Day>> {
    // declares read-only property `firstDow`, initialised with the result of calling `month.atDay(…)`
    val firstDow = month.atDay(1).dayOfWeek.value % 7
    // declares read-only property `cells`, initialised with the result of calling `mutableListOf(…)`
    val cells = mutableListOf<Day>()
    // calls `repeat` with arguments `(firstDow)`
    repeat(firstDow) { cells.add(Day(null, DayState.BLANK)) }
    // `for` loop: iterates over `1..month.lengthOfMonth()`, binding each element to `d`
    for (d in 1..month.lengthOfMonth()) {
        // declares read-only property `date`, initialised with the result of calling `month.atDay(…)`
        val date  = month.atDay(d)
        // declares read-only property `inRange`, initialised to `start != null && end != null && !date.isBefo…`
        val inRange = start != null && end != null && !date.isBefore(start) && !date.isAfter(end)
        // declares read-only property `state`, initialised to `when` and opens a lambda / block
        val state = when {
            // lambda `inRange && date in selected -> DayState.SELECTED`
            inRange && date in selected -> DayState.SELECTED
            // lambda `inRange -> DayState.RANGE`
            inRange                     -> DayState.RANGE
            // `else` branch of the `when`: evaluates `DayState.MUTED`
            else                        -> DayState.MUTED
        // closes the lambda assigned to `state`
        }
        // calls `add` on `cells` with arguments `(Day(d, state, date))`
        cells.add(Day(d, state, date))
    // closes the for loop
    }
    // `while` loop: repeats the block below as long as `cells.size % 7 != 0` is true; executes `cells.add(Day(null, DayState.BLANK))`
    while (cells.size % 7 != 0) cells.add(Day(null, DayState.BLANK))
    // returns `cells.chunked(7)` from the current function
    return cells.chunked(7)
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun TripCalendarScreen(`
fun TripCalendarScreen(
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `onEditItineraryClick: (tripId: String) -> Unit = {},`
    onEditItineraryClick: (tripId: String) -> Unit = {},
    // continues the statement started above: `onViewItineraryClick: (tripId: String) -> Unit = {},`
    onViewItineraryClick: (tripId: String) -> Unit = {},
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: TripCalendarViewModel = viewModel(),`
    viewModel: TripCalendarViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()
    // declares read-only property `navTarget`, delegated to `viewModel.navTarget.collectAsState()`
    val navTarget by viewModel.navTarget.collectAsState()
    // declares read-only property `snackbarHostState`, initialised to `remember { SnackbarHostState() }`
    val snackbarHostState = remember { SnackbarHostState() }
    // declares read-only property `scope`, initialised with the result of calling `rememberCoroutineScope(…)`
    val scope = rememberCoroutineScope()

    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.loadTrip() }

    // calls `LaunchedEffect` with arguments `(uiState.deleted)`
    LaunchedEffect(uiState.deleted) { if (uiState.deleted) onBackClick() }

    // `if` statement: the block below runs when `uiState.showDeleteConfirm` is true
    if (uiState.showDeleteConfirm) {
        // calls `AlertDialog` with an argument list that continues on the following lines
        AlertDialog(
            // continues the statement started above: `onDismissRequest = { viewModel.onDeleteDismiss() },`
            onDismissRequest = { viewModel.onDeleteDismiss() },
            // continues the statement started above: `title = { Text(stringResource(R.string.calendar_delete_conf…`
            title = { Text(stringResource(R.string.calendar_delete_confirm_title)) },
            // continues the statement started above: `text = { Text(stringResource(R.string.calendar_delete_confi…`
            text  = { Text(stringResource(R.string.calendar_delete_confirm_body, uiState.tripName)) },
            // continues the statement started above: `confirmButton = {`
            confirmButton = {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.calendar_delete_trip),`
                    text = stringResource(R.string.calendar_delete_trip),
                    // continues the statement started above: `color = WaypointTerracotta, fontWeight = FontWeight.Bold,`
                    color = WaypointTerracotta, fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier.clickable { viewModel.onDeleteConfirm()…`
                    modifier = Modifier.clickable { viewModel.onDeleteConfirm() }.padding(8.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            },
            // continues the statement started above: `dismissButton = {`
            dismissButton = {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.common_cancel),`
                    text = stringResource(R.string.common_cancel),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `modifier = Modifier.clickable { viewModel.onDeleteDismiss()…`
                    modifier = Modifier.clickable { viewModel.onDeleteDismiss() }.padding(8.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            },
            // continues the statement started above: `containerColor = WaypointCard,`
            containerColor = WaypointCard,
        // closes the multi-line argument list started above
        )
    // closes the if block
    }

    // `if` statement: the block below runs when `uiState.showEditDates` is true
    if (uiState.showEditDates) {
        // calls `Dialog` with arguments `(onDismissRequest = { viewModel.onEditDatesDi…)` and opens a trailing lambda / block
        Dialog(onDismissRequest = { viewModel.onEditDatesDismiss() }) {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusDeco))`
                    .background(WaypointCard, RoundedCornerShape(RadiusDeco))
                    // continues the statement started above: `.padding(horizontal = 18.dp, vertical = 20.dp),`
                    .padding(horizontal = 18.dp, vertical = 20.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.calendar_edit_dates_title),`
                    text = stringResource(R.string.calendar_edit_dates_title),
                    // continues the statement started above: `color = WaypointTextPrimary, fontSize = 20.sp, fontWeight =…`
                    color = WaypointTextPrimary, fontSize = 20.sp, fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(`
                    text = stringResource(
                        // continues the statement started above: `if (uiState.editStart == null) R.string.calendar_edit_dates…`
                        if (uiState.editStart == null) R.string.calendar_edit_dates_pick_start
                        // continues the statement started above: `else R.string.calendar_edit_dates_pick_end`
                        else R.string.calendar_edit_dates_pick_end
                    // closes the multi-line argument list started above
                    ),
                    // continues the statement started above: `color = WaypointTextMuted, fontSize = 12.sp,`
                    color = WaypointTextMuted, fontSize = 12.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),`
                    modifier = Modifier.padding(top = 4.dp, bottom = 12.dp),
                // closes the multi-line argument list started above
                )
                // calls `CalendarGrid` with an argument list that continues on the following lines
                CalendarGrid(
                    // continues the statement started above: `uiState = NewTripUiState(`
                    uiState = NewTripUiState(
                        // continues the statement started above: `displayMonth = uiState.editMonth,`
                        displayMonth = uiState.editMonth,
                        // continues the statement started above: `startDate = uiState.editStart,`
                        startDate = uiState.editStart,
                        // continues the statement started above: `endDate = uiState.editEnd,`
                        endDate = uiState.editEnd,
                    // closes the multi-line argument list started above
                    ),
                    // continues the statement started above: `onDayTapped = viewModel::onEditDayTapped,`
                    onDayTapped = viewModel::onEditDayTapped,
                    // continues the statement started above: `onPrevMonth = viewModel::onEditPrevMonth,`
                    onPrevMonth = viewModel::onEditPrevMonth,
                    // continues the statement started above: `onNextMonth = viewModel::onEditNextMonth,`
                    onNextMonth = viewModel::onEditNextMonth,
                    // continues the statement started above: `onHeaderTap = {},`
                    onHeaderTap = {},
                // closes the multi-line argument list started above
                )
                // calls `AppButtonFilled` with an argument list that continues on the following lines
                AppButtonFilled(
                    // continues the statement started above: `text = stringResource(R.string.modal_save_button),`
                    text = stringResource(R.string.modal_save_button),
                    // continues the statement started above: `onClick = { viewModel.onEditDatesSave() },`
                    onClick = { viewModel.onEditDatesSave() },
                    // continues the statement started above: `enabled = uiState.editStart != null,`
                    enabled = uiState.editStart != null,
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = 16.dp),`
                    modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the lambda passed to `Dialog`
        }
    // closes the if block
    }

    // calls `LaunchedEffect` with arguments `(navTarget)` and opens a trailing lambda / block
    LaunchedEffect(navTarget) {
        // `when` expression on the value of `val t = navTarget`: the first matching branch below runs
        when (val t = navTarget) {
            // `when` branch `is TripCalendarViewModel.ItineraryNavTa…`: opens a block
            is TripCalendarViewModel.ItineraryNavTarget.EditItinerary -> {
                // calls `onEditItineraryClick` with arguments `(t.tripId)`
                onEditItineraryClick(t.tripId)
                // calls `onNavConsumed` on `viewModel` with arguments `()`
                viewModel.onNavConsumed()
            // closes the when branch
            }
            // `when` branch `is TripCalendarViewModel.ItineraryNavTa…`: opens a block
            is TripCalendarViewModel.ItineraryNavTarget.ViewItinerary -> {
                // calls `onViewItineraryClick` with arguments `(t.tripId)`
                onViewItineraryClick(t.tripId)
                // calls `onNavConsumed` on `viewModel` with arguments `()`
                viewModel.onNavConsumed()
            // closes the when branch
            }
            // `when` branch: when the subject matches `null`, evaluates `Unit`
            null -> Unit
        // closes the when block
        }
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `LaunchedEffect` with arguments `(uiState.showNoDaysError)` and opens a trailing lambda / block
    LaunchedEffect(uiState.showNoDaysError) {
        // `if` statement: the block below runs when `uiState.showNoDaysError` is true
        if (uiState.showNoDaysError) {
            // opens a block after `scope.launch`
            scope.launch {
                // calls `showSnackbar` on `snackbarHostState` with arguments `("Select at least one day to continue")`
                snackbarHostState.showSnackbar("Select at least one day to continue")
            // closes the block
            }
            // calls `onNoDaysErrorShown` on `viewModel` with arguments `()`
            viewModel.onNoDaysErrorShown()
        // closes the if block
        }
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `Scaffold` with an argument list that continues on the following lines
    Scaffold(
        // continues the statement started above: `containerColor = WaypointCream,`
        containerColor = WaypointCream,
        // continues the statement started above: `contentWindowInsets = WindowInsets(0, 0, 0, 0),`
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        // continues the statement started above: `snackbarHost = {`
        snackbarHost = {
            // calls `SnackbarHost` with arguments `(snackbarHostState)`
            SnackbarHost(snackbarHostState) { data ->
                // continues the statement started above: `Snackbar(`
                Snackbar(
                    // continues the statement started above: `snackbarData = data,`
                    snackbarData = data,
                    // continues the statement started above: `containerColor = WaypointTextPrimary,`
                    containerColor = WaypointTextPrimary,
                    // continues the statement started above: `contentColor = WaypointCream,`
                    contentColor = WaypointCream,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        },
    // continues the statement started above: `) { _ ->`
    ) { _ ->

    // continues the statement started above: `if (uiState.showDestSearch) {`
    if (uiState.showDestSearch) {
        // calls `CitySearchDialog` with an argument list that continues on the following lines
        CitySearchDialog(
            // continues the statement started above: `onDismiss = viewModel::onDismissDestSearch,`
            onDismiss = viewModel::onDismissDestSearch,
            // continues the statement started above: `onCitySelected = viewModel::onDestinationSelected,`
            onCitySelected = viewModel::onDestinationSelected,
        // closes the multi-line argument list started above
        )
    // closes the block
    }

    // `if` statement: the block below runs when `uiState.showNameDialog` is true
    if (uiState.showNameDialog) {
        // calls `AlertDialog` with an argument list that continues on the following lines
        AlertDialog(
            // continues the statement started above: `onDismissRequest = viewModel::onDismissNameDialog,`
            onDismissRequest = viewModel::onDismissNameDialog,
            // continues the statement started above: `title = { Text(stringResource(R.string.calendar_rename_trip…`
            title = { Text(stringResource(R.string.calendar_rename_trip), color = WaypointTextPrimary, fontWeight = FontWeight.Bold) },
            // continues the statement started above: `text = {`
            text = {
                // calls `BasicTextField` with an argument list that continues on the following lines
                BasicTextField(
                    // continues the statement started above: `value = uiState.nameInput,`
                    value = uiState.nameInput,
                    // continues the statement started above: `onValueChange = viewModel::onNameInputChanged,`
                    onValueChange = viewModel::onNameInputChanged,
                    // continues the statement started above: `textStyle = TextStyle(color = WaypointTextPrimary, fontSize…`
                    textStyle = TextStyle(color = WaypointTextPrimary, fontSize = 14.sp),
                    // continues the statement started above: `singleLine = true,`
                    singleLine = true,
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            },
            // continues the statement started above: `confirmButton = {`
            confirmButton = {
                // calls `TextButton` with arguments `(onClick = viewModel::onConfirmNameEdit)` and opens a trailing lambda / block
                TextButton(onClick = viewModel::onConfirmNameEdit) {
                    // calls `Text` with arguments `(stringResource(R.string.modal_save_button), …)`
                    Text(stringResource(R.string.modal_save_button), color = WaypointTerracotta, fontWeight = FontWeight.Bold)
                // closes the lambda passed to `TextButton`
                }
            // closes the block
            },
            // continues the statement started above: `dismissButton = {`
            dismissButton = {
                // calls `TextButton` with arguments `(onClick = viewModel::onDismissNameDialog)` and opens a trailing lambda / block
                TextButton(onClick = viewModel::onDismissNameDialog) {
                    // calls `Text` with arguments `(stringResource(R.string.common_cancel), colo…)`
                    Text(stringResource(R.string.common_cancel), color = WaypointTextMuted)
                // closes the lambda passed to `TextButton`
                }
            // closes the block
            },
            // continues the statement started above: `containerColor = WaypointCream,`
            containerColor = WaypointCream,
        // closes the multi-line argument list started above
        )
    // closes the if block
    }

    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
            .windowInsetsPadding(WindowInsets.systemBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = …`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 24.dp)
            // continues the statement started above: `.verticalScroll(rememberScrollState()),`
            .verticalScroll(rememberScrollState()),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
        Box(modifier = Modifier.fillMaxWidth()) {
            // calls `CircleIconButton` with arguments `(onClick = onBackClick, modifier = Modifier.a…)` and opens a trailing lambda / block
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.calendar_back_glyph),`
                    text = stringResource(R.string.calendar_back_glyph),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `CircleIconButton`
            }
            // `if` statement: the block below runs when `!uiState.isLoading && uiState.tripName.isNotBlank()` is true
            if (!uiState.isLoading && uiState.tripName.isNotBlank()) {
                // calls `Row` with an argument list that continues on the following lines
                Row(
                    // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                    modifier = Modifier.align(Alignment.Center),
                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                    verticalAlignment = Alignment.CenterVertically,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = uiState.tripName,`
                        text = uiState.tripName,
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color = WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 16.sp,`
                        fontSize = 16.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    )
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.calendar_edit_trip_glyph),`
                        text = stringResource(R.string.calendar_edit_trip_glyph),
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color = WaypointTerracotta,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.padding(start = 6.dp)`
                            .padding(start = 6.dp)
                            // continues the statement started above: `.clickable { viewModel.onShowNameDialog() },`
                            .clickable { viewModel.onShowNameDialog() },
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the if block
            }
        // closes the lambda passed to `Box`
        }

        // `if` statement: the block below runs when `uiState.isLoading` is true
        if (uiState.isLoading) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = 60.dp),`
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // continues the statement started above: `) { CircularProgressIndicator(color = WaypointTerracotta) }`
            ) { CircularProgressIndicator(color = WaypointTerracotta) }
            // expression: `return@Column`
            return@Column
        // closes the if block
        }

        // `if` statement: the block below runs when `uiState.dateRangeLabel.isNotBlank()` is true
        if (uiState.dateRangeLabel.isNotBlank()) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "${uiState.dateRangeLabel} · ${pluralStringResource(…`
                text = "${uiState.dateRangeLabel} · ${pluralStringResource(R.plurals.day_count, uiState.dayCount, uiState.dayCount)}",
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = 6.dp),`
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            // closes the multi-line argument list started above
            )
        // closes the if block
        }

        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(R.string.calendar_overview),`
            text = stringResource(R.string.calendar_overview),
            // continues the statement started above: `color = WaypointTextPrimary,`
            color = WaypointTextPrimary,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
            // continues the statement started above: `modifier = Modifier.padding(top = 24.dp, bottom = 10.dp),`
            modifier = Modifier.padding(top = 24.dp, bottom = 10.dp),
        // closes the multi-line argument list started above
        )

        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth()) {
            // calls `OverviewCard` with an argument list that continues on the following lines
            OverviewCard(
                // continues the statement started above: `value = "${uiState.nightCount}",`
                value = "${uiState.nightCount}",
                // continues the statement started above: `label = stringResource(R.string.calendar_duration),`
                label = stringResource(R.string.calendar_duration),
                // continues the statement started above: `suffix = pluralStringResource(R.plurals.night_count_suffix,…`
                suffix = pluralStringResource(R.plurals.night_count_suffix, uiState.nightCount),
                // continues the statement started above: `modifier = Modifier.weight(1f),`
                modifier = Modifier.weight(1f),
            // closes the multi-line argument list started above
            )
            // calls `OverviewCard` with an argument list that continues on the following lines
            OverviewCard(
                // continues the statement started above: `value = uiState.destination ?: stringResource(R.string.cale…`
                value = uiState.destination ?: stringResource(R.string.calendar_add_destination),
                // continues the statement started above: `label = stringResource(R.string.calendar_destination),`
                label = stringResource(R.string.calendar_destination),
                // continues the statement started above: `isPlaceholder = uiState.destination == null,`
                isPlaceholder = uiState.destination == null,
                // continues the statement started above: `isLoading = uiState.isGeocodingDest,`
                isLoading = uiState.isGeocodingDest,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.weight(1f)`
                    .weight(1f)
                    // continues the statement started above: `.padding(start = 10.dp)`
                    .padding(start = 10.dp)
                    // continues the statement started above: `.clickable { viewModel.onShowDestSearch() },`
                    .clickable { viewModel.onShowDestSearch() },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Row`
        }

        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
            // calls `listOf` with an argument list that continues on the following lines
            listOf(
                // continues the statement started above: `stringResource(R.string.calendar_flight) to uiState.flightC…`
                stringResource(R.string.calendar_flight) to uiState.flightCount,
                // continues the statement started above: `stringResource(R.string.calendar_stay) to uiState.stayCount,`
                stringResource(R.string.calendar_stay)   to uiState.stayCount,
                // continues the statement started above: `stringResource(R.string.calendar_rental) to uiState.rentalC…`
                stringResource(R.string.calendar_rental) to uiState.rentalCount,
            // continues the statement started above: `).forEachIndexed { i, (label, count) ->`
            ).forEachIndexed { i, (label, count) ->
                // continues the statement started above: `OverviewCard(`
                OverviewCard(
                    // continues the statement started above: `value = "$count",`
                    value = "$count",
                    // continues the statement started above: `label = label,`
                    label = label,
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.weight(1f)`
                        .weight(1f)
                        // continues the statement started above: `.padding(start = if (i == 0) 0.dp else 10.dp),`
                        .padding(start = if (i == 0) 0.dp else 10.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the lambda passed to `Row`
        }

        // declares read-only property `monthFmt`, initialised with the result of calling `DateTimeFormatter.ofPattern(…)`
        val monthFmt = DateTimeFormatter.ofPattern("MMMM yyyy")
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = 24.dp),`
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
            horizontalArrangement = Arrangement.SpaceBetween,
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.calendar_prev_month_glyph),`
                text = stringResource(R.string.calendar_prev_month_glyph),
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `fontSize = 18.sp,`
                fontSize = 18.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.size(32.dp).clickable { viewModel.onPre…`
                modifier = Modifier.size(32.dp).clickable { viewModel.onPrevMonth() },
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = uiState.displayMonth.format(monthFmt),`
                text = uiState.displayMonth.format(monthFmt),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
            // closes the multi-line argument list started above
            )
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.calendar_next_month_glyph),`
                text = stringResource(R.string.calendar_next_month_glyph),
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `fontSize = 18.sp,`
                fontSize = 18.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.size(32.dp).clickable { viewModel.onNex…`
                modifier = Modifier.size(32.dp).clickable { viewModel.onNextMonth() },
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
            // closes the multi-line argument list started above
            )
        // closes the block
        }

        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            // calls `listOf` with an argument list that continues on the following lines
            listOf(
                // continues the statement started above: `R.string.calendar_wd_sun, R.string.calendar_wd_mon, R.strin…`
                R.string.calendar_wd_sun, R.string.calendar_wd_mon, R.string.calendar_wd_tue,
                // continues the statement started above: `R.string.calendar_wd_wed, R.string.calendar_wd_thu, R.strin…`
                R.string.calendar_wd_wed, R.string.calendar_wd_thu, R.string.calendar_wd_fri, R.string.calendar_wd_sat,
            // continues the statement started above: `).forEach { res ->`
            ).forEach { res ->
                // continues the statement started above: `Text(`
                Text(
                    // continues the statement started above: `text = stringResource(res),`
                    text = stringResource(res),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 11.sp,`
                    fontSize = 11.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `textAlign = TextAlign.Center,`
                    textAlign = TextAlign.Center,
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the lambda passed to `Row`
        }

        // declares read-only property `rows`, initialised with the result of calling `buildCalendarGrid(…)`
        val rows = buildCalendarGrid(uiState.displayMonth, uiState.startDate, uiState.endDate, uiState.selectedDays)
        // calls `Column` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Column(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            // expression: `rows.forEachIndexed { rowIndex, row ->`
            rows.forEachIndexed { rowIndex, row ->
                // continues the statement started above: `Row(`
                Row(
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth()`
                    modifier = Modifier.fillMaxWidth()
                        // continues the statement started above: `.padding(top = if (rowIndex == 0) 0.dp else 8.dp),`
                        .padding(top = if (rowIndex == 0) 0.dp else 8.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // expression: `row.forEach { day ->`
                    row.forEach { day ->
                        // continues the statement started above: `Box(modifier = Modifier.weight(1f), contentAlignment = Alig…`
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            // `if` statement: executes `CalendarDayCell(day, onClick = { day.date?.l…` when `day.number != null` is true
                            if (day.number != null) CalendarDayCell(day, onClick = { day.date?.let { viewModel.onDayToggled(it) } })
                        // closes the block
                        }
                    // closes the block
                    }
                // closes the block
                }
            // closes the block
            }
        // closes the lambda passed to `Column`
        }

        // `if` statement: the block below runs when `uiState.selectionLabel.isNotBlank()` is true
        if (uiState.selectionLabel.isNotBlank()) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 16.dp)`
                    .padding(top = 16.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 8.dp),`
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = uiState.selectionLabel,`
                    text = uiState.selectionLabel,
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 12.sp,`
                    fontSize = 12.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the if block
        }

        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 4.dp)) {
            // calls `AppButtonOutline` with an argument list that continues on the following lines
            AppButtonOutline(
                // continues the statement started above: `text = stringResource(R.string.calendar_view_itinerary),`
                text = stringResource(R.string.calendar_view_itinerary),
                // continues the statement started above: `onClick = { viewModel.onViewItineraryClick() },`
                onClick = { viewModel.onViewItineraryClick() },
                // continues the statement started above: `modifier = Modifier.weight(1f),`
                modifier = Modifier.weight(1f),
                // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            // closes the multi-line argument list started above
            )
            // calls `AppButtonFilled` with an argument list that continues on the following lines
            AppButtonFilled(
                // continues the statement started above: `text = stringResource(R.string.calendar_edit_itinerary),`
                text = stringResource(R.string.calendar_edit_itinerary),
                // continues the statement started above: `onClick = { viewModel.onEditItineraryClick() },`
                onClick = { viewModel.onEditItineraryClick() },
                // continues the statement started above: `modifier = Modifier.weight(1f).padding(start = 12.dp),`
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Row`
        }

        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth().padding(top = 12.dp, bottom = 4.dp)) {
            // calls `AppButtonOutline` with an argument list that continues on the following lines
            AppButtonOutline(
                // continues the statement started above: `text = stringResource(R.string.calendar_edit_dates),`
                text = stringResource(R.string.calendar_edit_dates),
                // continues the statement started above: `onClick = { viewModel.onEditDatesClick() },`
                onClick = { viewModel.onEditDatesClick() },
                // continues the statement started above: `modifier = Modifier.weight(1f),`
                modifier = Modifier.weight(1f),
                // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            // closes the multi-line argument list started above
            )
            // calls `AppButtonOutline` with an argument list that continues on the following lines
            AppButtonOutline(
                // continues the statement started above: `text = stringResource(R.string.calendar_delete_trip),`
                text = stringResource(R.string.calendar_delete_trip),
                // continues the statement started above: `onClick = { viewModel.onDeleteClick() },`
                onClick = { viewModel.onDeleteClick() },
                // continues the statement started above: `borderColor = WaypointLogoutBorder,`
                borderColor = WaypointLogoutBorder,
                // continues the statement started above: `modifier = Modifier.weight(1f).padding(start = 12.dp),`
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Row`
        }
    // closes the block
    }
    // closes the block
    }

// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun OverviewCard(`
private fun OverviewCard(
    // continues the statement started above: `value: String,`
    value: String,
    // continues the statement started above: `label: String,`
    label: String,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `suffix: String = "",`
    suffix: String = "",
    // continues the statement started above: `isPlaceholder: Boolean = false,`
    isPlaceholder: Boolean = false,
    // continues the statement started above: `isLoading: Boolean = false,`
    isLoading: Boolean = false,
// ends the argument list started above and opens the block that follows
) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusCard))`
            .background(WaypointCard, RoundedCornerShape(RadiusCard))
            // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
            .padding(horizontal = 14.dp, vertical = 12.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // `if` statement: the block below runs when `isLoading` is true
        if (isLoading) {
            // calls `CircularProgressIndicator` with an argument list that continues on the following lines
            CircularProgressIndicator(
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `modifier = Modifier.size(16.dp),`
                modifier = Modifier.size(16.dp),
                // continues the statement started above: `strokeWidth = 2.dp,`
                strokeWidth = 2.dp,
            // closes the multi-line argument list started above
            )
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // calls `Row` with arguments `(verticalAlignment = Alignment.Bottom)` and opens a trailing lambda / block
            Row(verticalAlignment = Alignment.Bottom) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = value,`
                    text = value,
                    // continues the statement started above: `color = if (isPlaceholder) WaypointTextMuted else WaypointT…`
                    color = if (isPlaceholder) WaypointTextMuted else WaypointTextPrimary,
                    // continues the statement started above: `fontSize = if (isPlaceholder) 12.sp else 18.sp,`
                    fontSize = if (isPlaceholder) 12.sp else 18.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // `if` statement: the block below runs when `suffix.isNotBlank()` is true
                if (suffix.isNotBlank()) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = " $suffix",`
                        text = " $suffix",
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 11.sp,`
                        fontSize = 11.sp,
                        // continues the statement started above: `modifier = Modifier.padding(bottom = 2.dp),`
                        modifier = Modifier.padding(bottom = 2.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the if block
                }
            // closes the lambda passed to `Row`
            }
        // closes the else branch
        }
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = label,`
            text = label,
            // continues the statement started above: `color = WaypointTextMuted,`
            color = WaypointTextMuted,
            // continues the statement started above: `fontSize = 11.sp,`
            fontSize = 11.sp,
            // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
            modifier = Modifier.padding(top = 2.dp),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `CalendarDayCell` taking 2 parameters (`day`, `onClick`) and opens its body
private fun CalendarDayCell(day: Day, onClick: () -> Unit) {
    // declares read-only property `shape`, initialised with the result of calling `RoundedCornerShape(…)`
    val shape = RoundedCornerShape(RadiusThumbnail)
    // declares read-only property `isInteractive`, initialised to `day.state == DayState.RANGE || day.state == …`
    val isInteractive = day.state == DayState.RANGE || day.state == DayState.SELECTED
    // declares mutable property `mod`, initialised with the result of calling `Modifier.size(…)`
    var mod = Modifier.size(40.dp)
    // `if` statement: executes `mod = mod.clickable { onClick() }` when `isInteractive` is true
    if (isInteractive) mod = mod.clickable { onClick() }
    // assigns `mod` the value `when (day.state) {`
    mod = when (day.state) {
        // lambda `DayState.SELECTED -> mod.background(WaypointTerrac…`
        DayState.SELECTED -> mod.background(WaypointTerracotta, shape)
        // lambda `DayState.RANGE -> mod.border(1.5.dp, WaypointCa…`
        DayState.RANGE    -> mod.border(1.5.dp, WaypointCard, shape)
        // `else` branch of the `when`: evaluates `mod`
        else              -> mod
    // closes the block
    }
    // calls `Box` with arguments `(modifier = mod, contentAlignment = Alignment…)` and opens a trailing lambda / block
    Box(modifier = mod, contentAlignment = Alignment.Center) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = day.number.toString(),`
            text = day.number.toString(),
            // continues the statement started above: `color = when (day.state) {`
            color = when (day.state) {
                // lambda `DayState.SELECTED -> White`
                DayState.SELECTED -> White
                // lambda `DayState.RANGE -> WaypointTextPrimary`
                DayState.RANGE    -> WaypointTextPrimary
                // `else` branch of the `when`: evaluates `WaypointDayMuted`
                else              -> WaypointDayMuted
            // closes the block
            },
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = if (day.state != DayState.MUTED) FontWeight.Bo…`
            fontWeight = if (day.state != DayState.MUTED) FontWeight.Bold else FontWeight.Normal,
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the function `CalendarDayCell`
}
