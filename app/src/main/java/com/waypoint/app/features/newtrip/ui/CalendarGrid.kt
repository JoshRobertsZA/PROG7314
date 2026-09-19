// declares that this file belongs to the package `com.waypoint.app.features.newtrip.ui`
package com.waypoint.app.features.newtrip.ui

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
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
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
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `java.time.format.TextStyle` for use in this file
import java.time.format.TextStyle
// imports `java.util.Locale` for use in this file
import java.util.Locale

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun CalendarGrid(`
fun CalendarGrid(
    // continues the statement started above: `uiState: NewTripUiState,`
    uiState: NewTripUiState,
    // continues the statement started above: `onDayTapped: (LocalDate) -> Unit,`
    onDayTapped: (LocalDate) -> Unit,
    // continues the statement started above: `onPrevMonth: () -> Unit,`
    onPrevMonth: () -> Unit,
    // continues the statement started above: `onNextMonth: () -> Unit,`
    onNextMonth: () -> Unit,
    // continues the statement started above: `onHeaderTap: () -> Unit,`
    onHeaderTap: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `today`, initialised with the result of calling `LocalDate.now(…)`
    val today        = LocalDate.now()
    // declares read-only property `month`, initialised to `uiState.displayMonth`
    val month        = uiState.displayMonth
    // declares read-only property `daysInMonth`, initialised with the result of calling `month.lengthOfMonth(…)`
    val daysInMonth  = month.lengthOfMonth()
    // declares read-only property `startOffset`, initialised with the result of calling `month.atDay(…)`
    val startOffset  = month.atDay(1).dayOfWeek.value % 7
    // declares read-only property `isPrevLocked`, initialised to `month <= YearMonth.now()`
    val isPrevLocked = month <= YearMonth.now()

    // declares read-only property `cells` of type `List<LocalDate?>`, initialised to `buildList` and opens a lambda / block
    val cells: List<LocalDate?> = buildList {
        // calls `repeat` with arguments `(startOffset)`
        repeat(startOffset) { add(null) }
        // `for` loop: iterates over `1..daysInMonth`, binding each element to `d`; executes `add(month.atDay(d))`
        for (d in 1..daysInMonth) add(month.atDay(d))
        // `while` loop: repeats the block below as long as `size % 7 != 0` is true; executes `add(null)`
        while (size % 7 != 0) add(null)
    // closes the lambda assigned to `cells`
    }

    // calls `Column` with arguments `(modifier = modifier)` and opens a trailing lambda / block
    Column(modifier = modifier) {

        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
            modifier = Modifier.fillMaxWidth(),
            // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
            horizontalArrangement = Arrangement.SpaceBetween,
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "‹",`
                text = "‹",
                // continues the statement started above: `color = if (isPrevLocked) WaypointDayMuted else WaypointTer…`
                color = if (isPrevLocked) WaypointDayMuted else WaypointTerracotta,
                // continues the statement started above: `fontSize = 22.sp,`
                fontSize = 22.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.size(36.dp)`
                    .size(36.dp)
                    // continues the statement started above: `.alpha(if (isPrevLocked) 0.35f else 1f)`
                    .alpha(if (isPrevLocked) 0.35f else 1f)
                    // continues the statement started above: `.then(`
                    .then(
                        // continues the statement started above: `if (!isPrevLocked) Modifier.clickable(onClick = onPrevMonth)`
                        if (!isPrevLocked) Modifier.clickable(onClick = onPrevMonth)
                        // continues the statement started above: `else Modifier`
                        else Modifier
                    // closes the multi-line argument list started above
                    ),
            // closes the multi-line argument list started above
            )

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "${month.month.getDisplayName(TextStyle.FULL, Locale…`
                text = "${month.month.getDisplayName(TextStyle.FULL, Locale.getDefault())} ${month.year}",
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.clickable(onClick = onHeaderTap),`
                modifier = Modifier.clickable(onClick = onHeaderTap),
            // closes the multi-line argument list started above
            )

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "›",`
                text = "›",
                // continues the statement started above: `color = WaypointTerracotta,`
                color = WaypointTerracotta,
                // continues the statement started above: `fontSize = 22.sp,`
                fontSize = 22.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.size(36.dp)`
                    .size(36.dp)
                    // continues the statement started above: `.clickable(onClick = onNextMonth),`
                    .clickable(onClick = onNextMonth),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.padding(top = 12.dp),`
                .padding(top = 12.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `listOf` with arguments `("S", "M", "T", "W", "T", "F", "S")`, then chains `.forEach { label ->`
            listOf("S", "M", "T", "W", "T", "F", "S").forEach { label ->
                // continues the statement started above: `Text(`
                Text(
                    // continues the statement started above: `text = label,`
                    text = label,
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
        // closes the block
        }

        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.padding(top = 8.dp),`
                .padding(top = 8.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `chunked` on `cells` with arguments `(7)`, then chains `.forEachIndexed { rowIdx, row …`
            cells.chunked(7).forEachIndexed { rowIdx, row ->
                // continues the statement started above: `Row(`
                Row(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.padding(top = if (rowIdx == 0) 0.dp else 6.dp),`
                        .padding(top = if (rowIdx == 0) 0.dp else 6.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // expression: `row.forEach { date ->`
                    row.forEach { date ->
                        // continues the statement started above: `Box(`
                        Box(
                            // continues the statement started above: `modifier = Modifier.weight(1f),`
                            modifier = Modifier.weight(1f),
                            // continues the statement started above: `contentAlignment = Alignment.Center,`
                            contentAlignment = Alignment.Center,
                        // ends the argument list started above and opens the block that follows
                        ) {
                            // `if` statement: the block below runs when `date != null` is true
                            if (date != null) {
                                // calls `CalendarDayCell` with an argument list that continues on the following lines
                                CalendarDayCell(
                                    // continues the statement started above: `date = date,`
                                    date      = date,
                                    // continues the statement started above: `today = today,`
                                    today     = today,
                                    // continues the statement started above: `startDate = uiState.startDate,`
                                    startDate = uiState.startDate,
                                    // continues the statement started above: `endDate = uiState.endDate,`
                                    endDate   = uiState.endDate,
                                    // continues the statement started above: `onTapped = { if (date >= today) onDayTapped(date) },`
                                    onTapped  = { if (date >= today) onDayTapped(date) },
                                // closes the multi-line argument list started above
                                )
                            // closes the if block
                            }
                        // closes the block
                        }
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
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun CalendarDayCell(`
private fun CalendarDayCell(
    // continues the statement started above: `date: LocalDate,`
    date: LocalDate,
    // continues the statement started above: `today: LocalDate,`
    today: LocalDate,
    // continues the statement started above: `startDate: LocalDate?,`
    startDate: LocalDate?,
    // continues the statement started above: `endDate: LocalDate?,`
    endDate: LocalDate?,
    // continues the statement started above: `onTapped: () -> Unit,`
    onTapped: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `isPast`, initialised to `date < today`
    val isPast = date < today
    // declares read-only property `isToday`, initialised to `date == today`
    val isToday = date == today

    // declares read-only property `inRange`, initialised to `startDate != null && endDate != null &&`
    val inRange = startDate != null && endDate != null &&
                  // continues the statement started above: `date >= startDate && date <= endDate`
                  date >= startDate && date <= endDate
    // declares read-only property `isSingleStart`, initialised to `startDate != null && endDate == null && date…`
    val isSingleStart = startDate != null && endDate == null && date == startDate
    // declares read-only property `isSelected`, initialised to `inRange || isSingleStart`
    val isSelected = inRange || isSingleStart

    // declares read-only property `bgColor`, initialised to `when` and opens a lambda / block
    val bgColor = when {
        // lambda `isSelected -> WaypointTerracotta`
        isSelected -> WaypointTerracotta
        // `else` branch of the `when`: evaluates `Color.Transparent`
        else       -> Color.Transparent
    // closes the lambda assigned to `bgColor`
    }
    // declares read-only property `textColor`, initialised to `when` and opens a lambda / block
    val textColor = when {
        // lambda `isSelected -> White`
        isSelected -> White
        // lambda `isPast -> WaypointDayMuted`
        isPast     -> WaypointDayMuted
        // `else` branch of the `when`: evaluates `WaypointTextPrimary`
        else       -> WaypointTextPrimary
    // closes the lambda assigned to `textColor`
    }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.size(40.dp)`
            .size(40.dp)
            // continues the statement started above: `.background(bgColor, RoundedCornerShape(RadiusThumbnail))`
            .background(bgColor, RoundedCornerShape(RadiusThumbnail))
            // continues the statement started above: `.then(`
            .then(
                // continues the statement started above: `if (isToday && !isSelected)`
                if (isToday && !isSelected)
                    // continues the statement started above: `Modifier.border(1.5.dp, WaypointTerracotta, RoundedCornerSh…`
                    Modifier.border(1.5.dp, WaypointTerracotta, RoundedCornerShape(RadiusThumbnail))
                // continues the statement started above: `else Modifier`
                else Modifier
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.alpha(if (isPast) 0.4f else 1f)`
            .alpha(if (isPast) 0.4f else 1f)
            // continues the statement started above: `.then(if (!isPast) Modifier.clickable(onClick = onTapped) e…`
            .then(if (!isPast) Modifier.clickable(onClick = onTapped) else Modifier),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = date.dayOfMonth.toString(),`
            text = date.dayOfMonth.toString(),
            // continues the statement started above: `color = textColor,`
            color = textColor,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = if (isSelected || isToday) FontWeight.Bold els…`
            fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}
