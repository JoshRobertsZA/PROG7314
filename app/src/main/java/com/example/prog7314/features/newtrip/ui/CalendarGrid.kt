package com.example.prog7314.features.newtrip.ui

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
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointDayMuted
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * A single-month calendar grid driven entirely by [uiState].
 *
 * Past days are grayed out and non-tappable. The selected range (start
 * through end) is filled orange. Today gets an orange outline ring when
 * not part of a selection. The month header is tappable to open the
 * year/month overlay; the prev arrow is disabled on the current month.
 */
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
    // Sunday-anchored offset: ISO DayOfWeek has Mon=1..Sun=7, so Sun%7==0
    val startOffset  = month.atDay(1).dayOfWeek.value % 7
    val isPrevLocked = month <= YearMonth.now()

    // Build cells: null = blank padding slot, non-null = real date
    val cells: List<LocalDate?> = buildList {
        repeat(startOffset) { add(null) }
        for (d in 1..daysInMonth) add(month.atDay(d))
        while (size % 7 != 0) add(null)
    }

    Column(modifier = modifier) {

        // Month navigation header
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

            // Tapping the month/year label opens the year picker overlay
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

        // Weekday labels (Sun … Sat)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 12.dp),
        ) {
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                Text(
                    text = label,
                    color = WaypointTextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.weight(1f),
                )
            }
        }

        // Day grid
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
