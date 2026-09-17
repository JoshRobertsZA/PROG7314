package com.example.prog7314.features.tripcalendar.ui

import androidx.compose.foundation.background
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.AppButtonOutline
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointDayMuted
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripRange
import com.example.prog7314.core.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

private enum class DayState { BLANK, MUTED, RANGE }
private data class Day(val number: Int?, val state: DayState)

private fun buildCalendarGrid(
    month: YearMonth,
    start: LocalDate?,
    end: LocalDate?,
): List<List<Day>> {
    val firstDow = month.atDay(1).dayOfWeek.value % 7  // Sun=0, Mon=1, ..., Sat=6
    val cells = mutableListOf<Day>()
    repeat(firstDow) { cells.add(Day(null, DayState.BLANK)) }
    for (d in 1..month.lengthOfMonth()) {
        val date = month.atDay(d)
        val state = if (start != null && end != null &&
            !date.isBefore(start) && !date.isAfter(end)
        ) DayState.RANGE else DayState.MUTED
        cells.add(Day(d, state))
    }
    while (cells.size % 7 != 0) cells.add(Day(null, DayState.BLANK))
    return cells.chunked(7)
}

@Composable
fun TripCalendarScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: TripCalendarViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

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
                Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                    Text(uiState.tripName, color = WaypointTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text(
                        text = stringResource(R.string.calendar_edit_trip_glyph),
                        color = WaypointTerracotta,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(start = 6.dp).clickable(onClick = {}),
                    )
                }
            }
        }

        if (uiState.isLoading) {
            Box(modifier = Modifier.fillMaxWidth().padding(top = 60.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = WaypointTerracotta)
            }
            return@Column
        }

        // Date range label
        if (uiState.dateRangeLabel.isNotBlank()) {
            Text(
                text = "${uiState.dateRangeLabel} · ${uiState.dayCountLabel}",
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
            )
        }

        // Month navigation
        val monthFmt = DateTimeFormatter.ofPattern("MMMM yyyy")
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
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

        // Calendar grid
        val rows = buildCalendarGrid(uiState.displayMonth, uiState.startDate, uiState.endDate)
        Column(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            rows.forEachIndexed { rowIndex, row ->
                Row(modifier = Modifier.fillMaxWidth().padding(top = if (rowIndex == 0) 0.dp else 8.dp)) {
                    row.forEach { day ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            if (day.number != null) CalendarDayCell(day)
                        }
                    }
                }
            }
        }

        // Actions
        Row(modifier = Modifier.fillMaxWidth().padding(top = 18.dp, bottom = 4.dp)) {
            AppButtonOutline(
                text = stringResource(R.string.calendar_view_itinerary),
                onClick = {},
                modifier = Modifier.weight(1f),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            )
            AppButtonFilled(
                text = stringResource(R.string.calendar_edit_itinerary),
                onClick = {},
                modifier = Modifier.weight(1f).padding(start = 12.dp),
                contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 14.dp),
            )
        }
    }
}

@Composable
private fun CalendarDayCell(day: Day) {
    val (bg, textColor) = when (day.state) {
        DayState.RANGE -> WaypointTripRange to White
        else           -> Color.Transparent to WaypointDayMuted
    }
    Box(
        modifier = Modifier
            .size(40.dp)
            .background(bg, RoundedCornerShape(RadiusThumbnail)),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = day.number.toString(),
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (day.state == DayState.RANGE) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
