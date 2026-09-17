package com.example.prog7314.features.newtrip.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusChip
import com.example.prog7314.core.theme.RadiusHandle
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripBadgeText
import com.example.prog7314.core.theme.White
import java.time.format.DateTimeFormatter
import java.util.Locale

private val SUMMARY_FMT = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

/**
 * New trip screen. Wires [NewTripViewModel] to the custom [CalendarGrid]
 * and [YearMonthPicker] composables.
 *
 * The persistent bottom summary bar shows the current selection at all
 * times. Save is enabled only once both a name and a full date range are
 * set. After a successful save the screen navigates to the All Trips screen via [onSaveSuccess].
 */
@Composable
fun NewTripScreen(
    onCloseClick: () -> Unit,
    onSaveSuccess: () -> Unit = onCloseClick,
    modifier: Modifier = Modifier,
    viewModel: NewTripViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    // Navigate back as soon as the trip is written to SQLite
    LaunchedEffect(Unit) {
        viewModel.tripSaved.collect { onSaveSuccess() }
    }

    // Year/month picker overlay — rendered above the main sheet
    if (uiState.showYearPicker) {
        YearMonthPicker(
            currentDisplay = uiState.displayMonth,
            onMonthPicked  = viewModel::onYearMonthPicked,
            onDismiss      = viewModel::onDismissYearPicker,
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        // Scrollable main content — bottom padding reserves space for the
        // sticky summary bar so it never hides the save button.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(start = 22.dp, top = 20.dp, end = 22.dp, bottom = 88.dp),
        ) {
            // Decorative drag handle
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 4.dp)
                    .background(
                        WaypointBorderSoft,
                        RoundedCornerShape(RadiusHandle),
                    )
                    .padding(horizontal = 20.dp, vertical = 2.dp),
            )

            // Top bar: title + close
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
            ) {
                Text(
                    text = stringResource(R.string.new_trip_title),
                    color = WaypointTextPrimary,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.align(Alignment.Center),
                )
                CircleIconButton(
                    onClick = onCloseClick,
                    modifier = Modifier.align(Alignment.CenterEnd),
                ) {
                    Text(
                        text = stringResource(R.string.new_trip_close_glyph),
                        color = WaypointTerracotta,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            // Trip name field
            Text(
                text = stringResource(R.string.new_trip_name_label),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            ) {
                if (uiState.tripName.isEmpty()) {
                    Text(
                        text = stringResource(R.string.new_trip_name_hint),
                        color = WaypointTextMuted,
                        fontSize = 13.sp,
                    )
                }
                BasicTextField(
                    value = uiState.tripName,
                    onValueChange = viewModel::onTripNameChanged,
                    textStyle = TextStyle(color = WaypointTextPrimary, fontSize = 13.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Dates section label
            Text(
                text = stringResource(R.string.new_trip_dates_label),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp),
            )
            Text(
                text = stringResource(R.string.new_trip_dates_subtitle),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(top = 2.dp)
                    .alpha(0.8f),
            )

            // Live calendar grid
            CalendarGrid(
                uiState      = uiState,
                onDayTapped  = viewModel::onDayTapped,
                onPrevMonth  = viewModel::onPrevMonth,
                onNextMonth  = viewModel::onNextMonth,
                onHeaderTap  = viewModel::onShowYearPicker,
                modifier     = Modifier.padding(top = 16.dp),
            )

            // Save button
            AppButtonFilled(
                text     = if (uiState.isSaving) "Saving…" else stringResource(R.string.new_trip_save_button),
                onClick  = viewModel::saveTrip,
                enabled  = uiState.canSave && !uiState.isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp),
            )

            // Footer hint
            Text(
                text = stringResource(R.string.new_trip_footer_hint),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .alpha(0.85f),
            )
        }

        // Persistent sticky summary bar pinned to the bottom
        TripSummaryBar(
            uiState  = uiState,
            modifier = Modifier.align(Alignment.BottomCenter),
        )
    }
}

@Composable
private fun TripSummaryBar(
    uiState: NewTripUiState,
    modifier: Modifier = Modifier,
) {
    val text = when {
        uiState.startDate == null ->
            "Tap a day to set your start date"
        uiState.endDate == null ->
            "${uiState.startDate.format(SUMMARY_FMT)} selected — tap another day for end date"
        else -> {
            val start = uiState.startDate.format(SUMMARY_FMT)
            val end   = uiState.endDate.format(SUMMARY_FMT)
            val days  = uiState.selectedDayCount
            "$start – $end · $days ${if (days == 1) "day" else "days"}"
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(WaypointCard)
            .padding(horizontal = 22.dp, vertical = 14.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            color = WaypointTripBadgeText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
        )
    }
}
