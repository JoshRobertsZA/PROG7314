package com.waypoint.app.features.newtrip.ui

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointDayMuted
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale
import androidx.compose.ui.res.stringResource
import com.waypoint.app.R

/** Month calendar for choosing a trip's start and end dates. */
@Composable
fun CalendarGrid(
    uiState: NewTripUiState,
    onDayTapped: (LocalDate) -> Unit,
    onPrevMonth: () -> Unit,
    onNextMonth: () -> Unit,
    onHeaderTap: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val today        = LocalDate.now()
    val month        = uiState.displayMonth
    val daysInMonth  = month.lengthOfMonth()
    val startOffset  = month.atDay(1).dayOfWeek.value % 7
    val isPrevLocked = month <= YearMonth.now()

    val cells: List<LocalDate?> = buildList {
        repeat(startOffset) { add(null) }
        for (d in 1..daysInMonth) add(month.atDay(d))
        while (size % 7 != 0) add(null)
    }

    Column(modifier = modifier) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "‹",
                color = if (isPrevLocked) WaypointDayMuted else WaypointTerracotta,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(36.dp)
                    .alpha(if (isPrevLocked) 0.35f else 1f)
                    .then(
                        if (!isPrevLocked) Modifier.clickable(onClick = onPrevMonth)
                        else Modifier
                    ),
            )

            Text(
                text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable(onClick = onHeaderTap),
            )

            Text(
                text = "›",
                color = WaypointTerracotta,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .size(36.dp)
                    .clickable(onClick = onNextMonth),
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            // Weekday labels come from strings.xml so they follow the selected language
            // Order is Sunday first to match startOffset (dayOfWeek % 7)
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

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
        ) {
            cells.chunked(7).forEachIndexed { rowIdx, row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (rowIdx == 0) 0.dp else 6.dp),
                ) {
                    row.forEach { date ->
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center,
                        ) {
                            if (date != null) {
                                CalendarDayCell(
                                    date      = date,
                                    today     = today,
                                    startDate = uiState.startDate,
                                    endDate   = uiState.endDate,
                                    onTapped  = { if (date >= today) onDayTapped(date) },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate,
    today: LocalDate,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onTapped: () -> Unit,
) {
    val isPast = date < today
    val isToday = date == today

    val inRange = startDate != null && endDate != null &&
                  date >= startDate && date <= endDate
    val isSingleStart = startDate != null && endDate == null && date == startDate
    val isSelected = inRange || isSingleStart

    val bgColor = when {
        isSelected -> WaypointTerracotta
        else       -> Color.Transparent
    }
    val textColor = when {
        isSelected -> White
        isPast     -> WaypointDayMuted
        else       -> WaypointTextPrimary
    }

    Box(
        modifier = Modifier
            .size(40.dp)
            .background(bgColor, RoundedCornerShape(RadiusThumbnail))
            .then(
                if (isToday && !isSelected)
                    Modifier.border(1.5.dp, WaypointTerracotta, RoundedCornerShape(RadiusThumbnail))
                else Modifier
            )
            .alpha(if (isPast) 0.4f else 1f)
            .then(if (!isPast) Modifier.clickable(onClick = onTapped) else Modifier),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = date.dayOfMonth.toString(),
            color = textColor,
            fontSize = 13.sp,
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
        )
    }
}
