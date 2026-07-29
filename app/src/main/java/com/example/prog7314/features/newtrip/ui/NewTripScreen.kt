package com.example.prog7314.features.newtrip.ui

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
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusChip
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripBadgeText
import com.example.prog7314.core.theme.White

private enum class DayState { BLANK, PAST, TODAY, UNSELECTED, SELECTED }
private data class Day(val number: Int?, val state: DayState)

/**
 * New trip screen. Minimal skeleton whose only job is to display the
 * screen and let the user close it - the trip name field, month nav, day
 * grid, and save button are still static/mock content, not wired up yet.
 *
 * TODO: wire up the trip name field, prev/next month (change the
 * displayed month and regenerate the day grid from the real device date),
 * tapping a day (update the selected range + summary chip), and the save
 * button (create the trip and navigate to the edit-itinerary screen),
 * once the backend (trip storage) is wired up on its own branch.
 */
@Composable
fun NewTripScreen(
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // July 2026 hardcoded to match the mock design. Jul 1, 2026 is a
    // Wednesday, so row 0 leads with three blank slots. Days 1-24 are in
    // the past (muted, not selectable). Day 25 is "today" (outlined).
    // Days 26-27 are selectable future days with no selection yet. Days
    // 28-31 are the currently selected range.
    val rows: List<List<Day>> = listOf(
        listOf(Day(null, DayState.BLANK), Day(null, DayState.BLANK), Day(null, DayState.BLANK)) +
            (1..4).map { Day(it, DayState.PAST) },
        (5..11).map { Day(it, DayState.PAST) },
        (12..18).map { Day(it, DayState.PAST) },
        (19..24).map { Day(it, DayState.PAST) } + Day(25, DayState.TODAY),
        listOf(Day(26, DayState.UNSELECTED), Day(27, DayState.UNSELECTED), Day(28, DayState.SELECTED), Day(29, DayState.SELECTED), Day(30, DayState.SELECTED), Day(31, DayState.SELECTED), Day(null, DayState.BLANK)),
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 28.dp),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        ) {
            // HandleWrap: decorative drag-handle bar, purely visual
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(width = 40.dp, height = 4.dp)
                    .background(WaypointBorderSoft, RoundedCornerShape(com.example.prog7314.core.theme.RadiusHandle)),
            )

            // TopBar: centered title, close button on the right
            Box(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                Text(
                    text = stringResource(R.string.new_trip_title),
                    color = WaypointTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                )
                CircleIconButton(onClick = onCloseClick, modifier = Modifier.align(Alignment.CenterEnd)) {
                    Text(
                        text = stringResource(R.string.new_trip_close_glyph),
                        color = WaypointTerracotta,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Trip name
            Text(
                text = stringResource(R.string.new_trip_name_label),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
            )
            var tripName by remember { mutableStateOf("") }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            ) {
                if (tripName.isEmpty()) {
                    Text(
                        text = stringResource(R.string.new_trip_name_hint),
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                    )
                }
                BasicTextField(
                    value = tripName,
                    onValueChange = { tripName = it },
                    textStyle = androidx.compose.ui.text.TextStyle(color = WaypointTextPrimary, fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // DatesLabel
            Text(
                text = stringResource(R.string.new_trip_dates_label),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 16.dp),
            )
            Text(
                text = stringResource(R.string.new_trip_dates_subtitle),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                modifier = Modifier.padding(top = 2.dp).alpha(0.8f),
            )

            // MonthNav. TODO: prev/next don't change the displayed month yet
            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.new_trip_prev_month_glyph),
                    color = WaypointTerracotta,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.size(32.dp).clickable(onClick = {}),
                )
                Text("July 2026", color = WaypointTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = stringResource(R.string.new_trip_next_month_glyph),
                    color = WaypointTerracotta,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.size(32.dp).clickable(onClick = {}),
                )
            }

            // WeekdayRow
            Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                listOf(
                    R.string.new_trip_wd_sun, R.string.new_trip_wd_mon, R.string.new_trip_wd_tue,
                    R.string.new_trip_wd_wed, R.string.new_trip_wd_thu, R.string.new_trip_wd_fri, R.string.new_trip_wd_sat,
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

            // CalendarGrid
            Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                rows.forEachIndexed { index, row ->
                    Row(modifier = Modifier.fillMaxWidth().padding(top = if (index == 0) 0.dp else 8.dp)) {
                        row.forEach { day ->
                            Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                                if (day.number != null) {
                                    NewTripDayCell(day)
                                }
                            }
                        }
                    }
                }
            }

            // SelectedSummary chip
            Box(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusChip))
                    .padding(horizontal = 14.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "4 days selected · Jul 28 – Jul 31, 2026",
                    color = WaypointTripBadgeText,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // SaveButton. TODO: not wired to save/navigate yet
            AppButtonFilled(
                text = stringResource(R.string.new_trip_save_button),
                onClick = {},
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp),
            )

            // Footer hint
            Text(
                text = stringResource(R.string.new_trip_footer_hint),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 4.dp)
                    .alpha(0.85f),
            )
        }
    }
}

@Composable
private fun NewTripDayCell(day: Day) {
    val modifier = Modifier.size(40.dp)
    when (day.state) {
        DayState.PAST -> Box(modifier = modifier.alpha(0.6f), contentAlignment = Alignment.Center) {
            Text(day.number.toString(), color = com.example.prog7314.core.theme.WaypointDayMuted, fontSize = 13.sp)
        }
        DayState.TODAY -> Box(
            modifier = modifier
                .background(Color.Transparent, RoundedCornerShape(RadiusThumbnail))
                .border(1.5.dp, WaypointTerracotta, RoundedCornerShape(RadiusThumbnail)),
            contentAlignment = Alignment.Center,
        ) {
            Text(day.number.toString(), color = WaypointTripBadgeText, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        DayState.UNSELECTED -> Box(
            modifier = modifier.background(WaypointCard, RoundedCornerShape(RadiusThumbnail)),
            contentAlignment = Alignment.Center,
        ) {
            Text(day.number.toString(), color = WaypointTextPrimary, fontSize = 13.sp)
        }
        DayState.SELECTED -> Box(
            modifier = modifier.background(WaypointTerracotta, RoundedCornerShape(RadiusThumbnail)),
            contentAlignment = Alignment.Center,
        ) {
            Text(day.number.toString(), color = White, fontSize = 13.sp, fontWeight = FontWeight.Bold)
        }
        DayState.BLANK -> Unit
    }
}
