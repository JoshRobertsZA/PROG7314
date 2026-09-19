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
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
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
// imports `java.time.Month` for use in this file
import java.time.Month
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `java.time.format.TextStyle` for use in this file
import java.time.format.TextStyle
// imports `java.util.Locale` for use in this file
import java.util.Locale

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun YearMonthPicker(`
fun YearMonthPicker(
    // continues the statement started above: `currentDisplay: YearMonth,`
    currentDisplay: YearMonth,
    // continues the statement started above: `onMonthPicked: (YearMonth) -> Unit,`
    onMonthPicked: (YearMonth) -> Unit,
    // continues the statement started above: `onDismiss: () -> Unit,`
    onDismiss: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `today`, initialised with the result of calling `YearMonth.now(…)`
    val today = YearMonth.now()
    // declares mutable property `browseYear`, delegated to `remember { mutableStateOf(currentDispla…`
    var browseYear by remember { mutableStateOf(currentDisplay.year) }
    // declares read-only property `isPrevYearLocked`, initialised to `browseYear <= today.year`
    val isPrevYearLocked = browseYear <= today.year

    // calls `Dialog` with arguments `(onDismissRequest = onDismiss)` and opens a trailing lambda / block
    Dialog(onDismissRequest = onDismiss) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.background(WaypointCream, RoundedCornerShape(RadiusButton))`
                .background(WaypointCream, RoundedCornerShape(RadiusButton))
                // continues the statement started above: `.padding(20.dp),`
                .padding(20.dp),
        // ends the argument list started above and opens the block that follows
        ) {
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
                    // continues the statement started above: `color = if (isPrevYearLocked) WaypointDayMuted else Waypoin…`
                    color = if (isPrevYearLocked) WaypointDayMuted else WaypointTerracotta,
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
                        // continues the statement started above: `.alpha(if (isPrevYearLocked) 0.35f else 1f)`
                        .alpha(if (isPrevYearLocked) 0.35f else 1f)
                        // continues the statement started above: `.then(`
                        .then(
                            // continues the statement started above: `if (!isPrevYearLocked) Modifier.clickable { browseYear-- }`
                            if (!isPrevYearLocked) Modifier.clickable { browseYear-- }
                            // continues the statement started above: `else Modifier`
                            else Modifier
                        // closes the multi-line argument list started above
                        ),
                // closes the multi-line argument list started above
                )

                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = browseYear.toString(),`
                    text = browseYear.toString(),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 17.sp,`
                    fontSize = 17.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
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
                        // continues the statement started above: `.clickable { browseYear++ },`
                        .clickable { browseYear++ },
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // declares read-only property `months`, initialised with the result of calling `Month.values(…)`
            val months = Month.values()
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 16.dp)`
                    .padding(top = 16.dp)
                    // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                    .verticalScroll(rememberScrollState()),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `toList` on `months` with arguments `()`, then chains `.chunked(3)`, `.forEach { rowMonths ->`
                months.toList().chunked(3).forEach { rowMonths ->
                    // continues the statement started above: `Row(`
                    Row(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(bottom = 10.dp),`
                            .padding(bottom = 10.dp),
                        // continues the statement started above: `horizontalArrangement = Arrangement.spacedBy(8.dp),`
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // expression: `rowMonths.forEach { month ->`
                        rowMonths.forEach { month ->
                            // continues the statement started above: `val ym = YearMonth.of(browseYear, month)`
                            val ym       = YearMonth.of(browseYear, month)
                            // declares read-only property `isPast`, initialised to `ym < today`
                            val isPast   = ym < today
                            // declares read-only property `isCurrent`, initialised to `ym == currentDisplay`
                            val isCurrent = ym == currentDisplay

                            // calls `Box` with an argument list that continues on the following lines
                            Box(
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.weight(1f)`
                                    .weight(1f)
                                    // continues the statement started above: `.height(44.dp)`
                                    .height(44.dp)
                                    // continues the statement started above: `.background(`
                                    .background(
                                        // continues the statement started above: `if (isCurrent) WaypointTerracotta else WaypointCard,`
                                        if (isCurrent) WaypointTerracotta else WaypointCard,
                                        // continues the statement started above: `RoundedCornerShape(RadiusThumbnail),`
                                        RoundedCornerShape(RadiusThumbnail),
                                    // closes the multi-line argument list started above
                                    )
                                    // continues the statement started above: `.then(`
                                    .then(
                                        // continues the statement started above: `if (!isCurrent && ym == today)`
                                        if (!isCurrent && ym == today)
                                            // continues the statement started above: `Modifier.border(`
                                            Modifier.border(
                                                // continues the statement started above: `1.5.dp,`
                                                1.5.dp,
                                                // continues the statement started above: `WaypointTerracotta,`
                                                WaypointTerracotta,
                                                // continues the statement started above: `RoundedCornerShape(RadiusThumbnail),`
                                                RoundedCornerShape(RadiusThumbnail),
                                            // closes the multi-line argument list started above
                                            )
                                        // continues the statement started above: `else Modifier`
                                        else Modifier
                                    // closes the multi-line argument list started above
                                    )
                                    // continues the statement started above: `.alpha(if (isPast) 0.35f else 1f)`
                                    .alpha(if (isPast) 0.35f else 1f)
                                    // continues the statement started above: `.then(`
                                    .then(
                                        // continues the statement started above: `if (!isPast) Modifier.clickable { onMonthPicked(ym) }`
                                        if (!isPast) Modifier.clickable { onMonthPicked(ym) }
                                        // continues the statement started above: `else Modifier`
                                        else Modifier
                                    // closes the multi-line argument list started above
                                    ),
                                // continues the statement started above: `contentAlignment = Alignment.Center,`
                                contentAlignment = Alignment.Center,
                            // ends the argument list started above and opens the block that follows
                            ) {
                                // calls `Text` with an argument list that continues on the following lines
                                Text(
                                    // continues the statement started above: `text = month.getDisplayName(TextStyle.SHORT, Locale.getDefa…`
                                    text = month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                    // continues the statement started above: `color = if (isCurrent) White else WaypointTextPrimary,`
                                    color = if (isCurrent) White else WaypointTextPrimary,
                                    // continues the statement started above: `fontSize = 12.sp,`
                                    fontSize = 12.sp,
                                    // continues the statement started above: `fontWeight = if (isCurrent) FontWeight.Bold else FontWeight…`
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    // continues the statement started above: `textAlign = TextAlign.Center,`
                                    textAlign = TextAlign.Center,
                                // closes the multi-line argument list started above
                                )
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

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.year_picker_hint),`
                text = stringResource(R.string.year_picker_hint),
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
                    // continues the statement started above: `.padding(top = 4.dp)`
                    .padding(top = 4.dp)
                    // continues the statement started above: `.alpha(0.7f),`
                    .alpha(0.7f),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the lambda passed to `Dialog`
    }
// closes the block
}
