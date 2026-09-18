package com.waypoint.app.features.tripcalendar.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember
import kotlinx.coroutines.launch
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.common.AppButtonFilled
import com.waypoint.app.core.common.AppButtonOutline
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusCard
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointDayMuted
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White
import com.waypoint.app.features.home.ui.CitySearchDialog
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private enum class DayState { BLANK, MUTED, RANGE, SELECTED }
private data class Day(val number: Int?, val state: DayState, val date: LocalDate? = null)

private fun buildCalendarGrid(
    month: YearMonth,
    start: LocalDate?,
    end: LocalDate?,
    selected: Set<LocalDate>,
): List<List<Day>> {
    val firstDow = month.atDay(1).dayOfWeek.value % 7  // Sun=0..Sat=6
    val cells = mutableListOf<Day>()
    repeat(firstDow) { cells.add(Day(null, DayState.BLANK)) }
    for (d in 1..month.lengthOfMonth()) {
        val date  = month.atDay(d)
        val inRange = start != null && end != null && !date.isBefore(start) && !date.isAfter(end)
        val state = when {
            inRange && date in selected -> DayState.SELECTED
            inRange                     -> DayState.RANGE
            else                        -> DayState.MUTED
        }
        cells.add(Day(d, state, date))
    }
    while (cells.size % 7 != 0) cells.add(Day(null, DayState.BLANK))
    return cells.chunked(7)
}

@Composable
fun TripCalendarScreen(
    onBackClick: () -> Unit,
    onEditItineraryClick: (tripId: String) -> Unit = {},
    onViewItineraryClick: (tripId: String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: TripCalendarViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val navTarget by viewModel.navTarget.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(navTarget) {
        when (val t = navTarget) {
            is TripCalendarViewModel.ItineraryNavTarget.EditItinerary -> {
                onEditItineraryClick(t.tripId)
                viewModel.onNavConsumed()
            }
            is TripCalendarViewModel.ItineraryNavTarget.ViewItinerary -> {
                onViewItineraryClick(t.tripId)
                viewModel.onNavConsumed()
            }
            null -> Unit
        }
    }

    LaunchedEffect(uiState.showNoDaysError) {
        if (uiState.showNoDaysError) {
            scope.launch {
                snackbarHostState.showSnackbar("Select at least one day to continue")
            }
            viewModel.onNoDaysErrorShown()
        }
    }

    Scaffold(
        containerColor = WaypointCream,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = WaypointTextPrimary,
                    contentColor = WaypointCream,
                )
            }
        },
    ) { _ ->

    // Destination search overlay
    if (uiState.showDestSearch) {
        CitySearchDialog(
            onDismiss = viewModel::onDismissDestSearch,
            onCitySelected = viewModel::onDestinationSelected,
        )
    }

    // Rename dialog
    if (uiState.showNameDialog) {
        AlertDialog(
            onDismissRequest = viewModel::onDismissNameDialog,
            title = { Text("Rename trip", color = WaypointTextPrimary, fontWeight = FontWeight.Bold) },
            text = {
                BasicTextField(
                    value = uiState.nameInput,
                    onValueChange = viewModel::onNameInputChanged,
                    textStyle = TextStyle(color = WaypointTextPrimary, fontSize = 14.sp),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                )
            },
            confirmButton = {
                TextButton(onClick = viewModel::onConfirmNameEdit) {
                    Text("Save", color = WaypointTerracotta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = viewModel::onDismissNameDialog) {
                    Text("Cancel", color = WaypointTextMuted)
                }
            },
            containerColor = WaypointCream,
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // TopBar
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.calendar_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            if (!uiState.isLoading && uiState.tripName.isNotBlank()) {
                Row(
                    modifier = Modifier.align(Alignment.Center),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = uiState.tripName,
                        color = WaypointTextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = stringResource(R.string.calendar_edit_trip_glyph),
                        color = WaypointTerracotta,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .padding(start = 6.dp)
                            .clickable { viewModel.onShowNameDialog() },
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxWidth().padding(top = 60.dp),
                contentAlignment = Alignment.Center,
            ) { CircularProgressIndicator(color = WaypointTerracotta) }
            return@Column
        }

        // Date range subtitle
        if (uiState.dateRangeLabel.isNotBlank()) {
            Text(
                text = "${uiState.dateRangeLabel} · ${uiState.dayCountLabel}",
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
            )
        }

        // Trip overview section
        Text(
            text = "Trip overview",
            color = WaypointTextPrimary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 24.dp, bottom = 10.dp),
        )

        // Row 1: Duration + Destination
        Row(modifier = Modifier.fillMaxWidth()) {
            OverviewCard(
                value = "${uiState.nightCount}",
                label = "Duration",
                suffix = if (uiState.nightCount == 1) "night" else "nights",
                modifier = Modifier.weight(1f),
            )
            OverviewCard(
                value = uiState.destination ?: "Add destination",
                label = "Destination",
                isPlaceholder = uiState.destination == null,
                isLoading = uiState.isGeocodingDest,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 10.dp)
                    .clickable { viewModel.onShowDestSearch() },
            )
        }

        // Row 2: Flight, Stay, Rental placeholders
        Row(modifier = Modifier.fillMaxWidth().padding(top = 10.dp)) {
            listOf("Flight", "Stay", "Rental").forEachIndexed { i, label ->
                OverviewCard(
                    value = "0",
                    label = label,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = if (i == 0) 0.dp else 10.dp),
                )
            }
        }

        // Month navigation
        val monthFmt = DateTimeFormatter.ofPattern("MMMM yyyy")
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.calendar_prev_month_glyph),
                color = WaypointTerracotta,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.size(32.dp).clickable { viewModel.onPrevMonth() },
                textAlign = TextAlign.Center,
            )
            Text(
                text = uiState.displayMonth.format(monthFmt),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = stringResource(R.string.calendar_next_month_glyph),
                color = WaypointTerracotta,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.size(32.dp).clickable { viewModel.onNextMonth() },
                textAlign = TextAlign.Center,
            )
        }

        // Weekday headers
        Row(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            listOf(
                R.string.calendar_wd_sun, R.string.calendar_wd_mon, R.string.calendar_wd_tue,
                R.string.calendar_wd_wed, R.string.calendar_wd_thu, R.string.calendar_wd_fri, R.string.calendar_wd_sat,
            ).forEach { res ->
                Text(
                    text = stringResource(res),
                    color = WaypointTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Calendar grid (selected set empty for now - wired in commit 2)
        val rows = buildCalendarGrid(uiState.displayMonth, uiState.startDate, uiState.endDate, uiState.selectedDays)
        Column(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            rows.forEachIndexed { rowIndex, row ->
                Row(
                    modifier = Modifier.fillMaxWidth()
                        .padding(top = if (rowIndex == 0) 0.dp else 8.dp),
                ) {
                    row.forEach { day ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            if (day.number != null) CalendarDayCell(day, onClick = { day.date?.let { viewModel.onDayToggled(it) } })
                        }
                    }
                }
            }
        }

        // Selected days chip
        if (uiState.selectionLabel.isNotBlank()) {
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(
                    text = uiState.selectionLabel,
                    color = WaypointTextPrimary,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        // Actions
        Row(modifier = Modifier.fillMaxWidth().padding(top = 24.dp, bottom = 4.dp)) {
            AppButtonOutline(
                text = stringResource(R.string.calendar_view_itinerary),
                onClick = { viewModel.onViewItineraryClick() },
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            )
            AppButtonFilled(
                text = stringResource(R.string.calendar_edit_itinerary),
                onClick = { viewModel.onEditItineraryClick() },
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            )
        }
    }
    } // end Scaffold

} // end TripCalendarScreen

@Composable
private fun OverviewCard(
    value: String,
    label: String,
    modifier: Modifier = Modifier,
    suffix: String = "",
    isPlaceholder: Boolean = false,
    isLoading: Boolean = false,
) {
    Column(
        modifier = modifier
            .background(WaypointCard, RoundedCornerShape(RadiusCard))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = WaypointTerracotta,
                modifier = Modifier.size(16.dp),
                strokeWidth = 2.dp,
            )
        } else {
            Row(verticalAlignment = Alignment.Bottom) {
                Text(
                    text = value,
                    color = if (isPlaceholder) WaypointTextMuted else WaypointTextPrimary,
                    fontSize = if (isPlaceholder) 12.sp else 18.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (suffix.isNotBlank()) {
                    Text(
                        text = " $suffix",
                        color = WaypointTextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(bottom = 2.dp),
                    )
                }
            }
        }
        Text(
            text = label,
            color = WaypointTextMuted,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 2.dp),
        )
    }
}

@Composable
private fun CalendarDayCell(day: Day, onClick: () -> Unit) {
    val shape = RoundedCornerShape(RadiusThumbnail)
    val isInteractive = day.state == DayState.RANGE || day.state == DayState.SELECTED
    var mod = Modifier.size(40.dp)
    if (isInteractive) mod = mod.clickable { onClick() }
    mod = when (day.state) {
        DayState.SELECTED -> mod.background(WaypointTerracotta, shape)
        DayState.RANGE    -> mod.border(1.5.dp, WaypointCard, shape)
        else              -> mod
    }
    Box(modifier = mod, contentAlignment = Alignment.Center) {
        Text(
            text = day.number.toString(),
            color = when (day.state) {
                DayState.SELECTED -> White
                DayState.RANGE    -> WaypointTextPrimary
                else              -> WaypointDayMuted
            },
            fontSize = 13.sp,
            fontWeight = if (day.state != DayState.MUTED) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
