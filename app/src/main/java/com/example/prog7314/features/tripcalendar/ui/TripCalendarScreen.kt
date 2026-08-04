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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import com.example.prog7314.core.theme.WaypointTripBadgeText
import com.example.prog7314.core.theme.WaypointTripRange
import com.example.prog7314.core.theme.White

private enum class DayState { BLANK, MUTED, RANGE, SELECTED }
private data class Day(val number: Int?, val state: DayState)

/**
 * Trip calendar screen. Minimal skeleton whose only job is to display the
 * screen and let the user navigate back - the day grid, month nav, and
 * itinerary buttons are still static/mock content, not wired up yet.
 *
 * TODO: wire up month nav (prev/next month), day selection,
 * tvEditTripName, and the itinerary buttons once those have somewhere to
 * go / data to act on.
 */
@Composable
fun TripCalendarScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // August 2026 hardcoded to match the mock design. Blue cells mark the
    // booked trip range (Aug 2-9); terracotta cells mark the sub-range
    // currently selected for the itinerary view (Aug 3-5).
    val rows: List<List<Day>> = listOf(
        listOf(null, null, null, null, null, null, 1).map {
            if (it == null) Day(null, DayState.BLANK) else Day(it, DayState.MUTED)
        },
        listOf(2 to DayState.RANGE, 3 to DayState.SELECTED, 4 to DayState.SELECTED, 5 to DayState.SELECTED, 6 to DayState.RANGE, 7 to DayState.RANGE, 8 to DayState.RANGE)
            .map { Day(it.first, it.second) },
        listOf(9 to DayState.RANGE, 10 to DayState.MUTED, 11 to DayState.MUTED, 12 to DayState.MUTED, 13 to DayState.MUTED, 14 to DayState.MUTED, 15 to DayState.MUTED)
            .map { Day(it.first, it.second) },
        (16..22).map { Day(it, DayState.MUTED) },
        (23..29).map { Day(it, DayState.MUTED) },
        listOf(30 to DayState.MUTED, 31 to DayState.MUTED).map { Day(it.first, it.second) } +
            listOf(Day(null, DayState.BLANK), Day(null, DayState.BLANK), Day(null, DayState.BLANK), Day(null, DayState.BLANK), Day(null, DayState.BLANK)),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 24.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // TopBar: back button, trip name + edit glyph (centered)
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.calendar_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Row(modifier = Modifier.align(Alignment.Center), verticalAlignment = Alignment.CenterVertically) {
                Text("Cape Town Getaway", color = WaypointTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                // TODO: not wired to an edit-trip-name flow yet
                Text(
                    text = stringResource(R.string.calendar_edit_trip_glyph),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clickable(onClick = {}),
                )
            }
        }

        Text(
            text = "Aug 2 – Aug 9, 2026 · 8 days",
            color = WaypointTextMuted,
            fontSize = 12.sp,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.fillMaxWidth().padding(top = 18.dp),
        )

        // MonthNav. TODO: prev/next don't change the displayed month yet
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
                modifier = Modifier.size(32.dp).clickable(onClick = {}),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
            Text("August 2026", color = WaypointTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
            Text(
                text = stringResource(R.string.calendar_next_month_glyph),
                color = WaypointTerracotta,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.size(32.dp).clickable(onClick = {}),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            )
        }

        // WeekdayRow
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
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // CalendarGrid
        Column(modifier = Modifier.fillMaxWidth().padding(top = 18.dp)) {
            rows.forEachIndexed { index, row ->
                Row(modifier = Modifier.fillMaxWidth().padding(top = if (index == 0) 0.dp else 8.dp)) {
                    row.forEach { day ->
                        Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                            if (day.number != null) {
                                CalendarDayCell(day)
                            }
                        }
                    }
                }
            }
        }

        // SelectedSummary chip
        Box(
            modifier = Modifier
                .padding(top = 18.dp)
                .background(com.example.prog7314.core.theme.WaypointCard, RoundedCornerShape(com.example.prog7314.core.theme.RadiusChip))
                .padding(horizontal = 14.dp, vertical = 8.dp),
        ) {
            Text(
                text = "3 days selected · Aug 3 – Aug 5",
                color = WaypointTripBadgeText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }

        // CalendarActions. TODO: not wired to an itinerary screen yet
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
        DayState.SELECTED -> WaypointTerracotta to White
        else -> Color.Transparent to WaypointDayMuted
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
            fontWeight = if (day.state == DayState.RANGE || day.state == DayState.SELECTED) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
