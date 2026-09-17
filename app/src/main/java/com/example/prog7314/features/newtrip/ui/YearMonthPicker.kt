package com.example.prog7314.features.newtrip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointDayMuted
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White
import java.time.Month
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

/**
 * Full-screen dialog overlay that lets the user jump to any future month.
 *
 * Shows 12 months in a 3-column grid for the [browseYear] currently in
 * view. Year navigation arrows move between years. Past months and years
 * before the current one are grayed and non-tappable. The [currentDisplay]
 * month is highlighted with an orange border.
 *
 * Calls [onMonthPicked] with the selected [YearMonth] and dismisses itself.
 * Calls [onDismiss] when the user taps outside or the back button.
 */
@Composable
fun YearMonthPicker(
    currentDisplay: YearMonth,
    onMonthPicked: (YearMonth) -> Unit,
    onDismiss: () -> Unit,
) {
    val today = YearMonth.now()
    var browseYear by remember { mutableStateOf(currentDisplay.year) }
    val isPrevYearLocked = browseYear <= today.year

    Dialog(onDismissRequest = onDismiss) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(WaypointCream, RoundedCornerShape(RadiusButton))
                .padding(20.dp),
        ) {
            // Year navigation header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "‹",
                    color = if (isPrevYearLocked) WaypointDayMuted else WaypointTerracotta,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .alpha(if (isPrevYearLocked) 0.35f else 1f)
                        .then(
                            if (!isPrevYearLocked) Modifier.clickable { browseYear-- }
                            else Modifier
                        ),
                )

                Text(
                    text = browseYear.toString(),
                    color = WaypointTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "›",
                    color = WaypointTerracotta,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .size(36.dp)
                        .clickable { browseYear++ },
                )
            }

            // 3-column month grid
            val months = Month.values()
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .verticalScroll(rememberScrollState()),
            ) {
                months.toList().chunked(3).forEach { rowMonths ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        rowMonths.forEach { month ->
                            val ym       = YearMonth.of(browseYear, month)
                            val isPast   = ym < today
                            val isCurrent = ym == currentDisplay

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(44.dp)
                                    .background(
                                        if (isCurrent) WaypointTerracotta else WaypointCard,
                                        RoundedCornerShape(RadiusThumbnail),
                                    )
                                    .then(
                                        if (!isCurrent && ym == today)
                                            Modifier.border(
                                                1.5.dp,
                                                WaypointTerracotta,
                                                RoundedCornerShape(RadiusThumbnail),
                                            )
                                        else Modifier
                                    )
                                    .alpha(if (isPast) 0.35f else 1f)
                                    .then(
                                        if (!isPast) Modifier.clickable { onMonthPicked(ym) }
                                        else Modifier
                                    ),
                                contentAlignment = Alignment.Center,
                            ) {
                                Text(
                                    text = month.getDisplayName(TextStyle.SHORT, Locale.getDefault()),
                                    color = if (isCurrent) White else WaypointTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }

            // Dismiss hint
            Text(
                text = "Tap a month to jump to it",
                color = WaypointTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .alpha(0.7f),
            )
        }
    }
}
