package com.waypoint.app.features.edititinerary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.waypoint.app.R
import com.waypoint.app.core.common.SelectionModal
import com.waypoint.app.core.common.TripValidation
import com.waypoint.app.core.theme.RadiusChip
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White
import java.time.LocalDate

/**
 * Lets the user change which itinerary days a lodging or car-rental booking covers.
 * [onSave] returns false when the range is rejected, and the error is shown instead of closing.
 */
@Composable
fun BookingDatesDialog(
    title: String,
    days: List<DayItem>,
    initialFrom: LocalDate,
    initialTo: LocalDate,
    onSave: (from: LocalDate, to: LocalDate) -> Boolean,
    onDismiss: () -> Unit,
) {
    val dates = days.map { it.date }
    var from by remember { mutableStateOf(initialFrom.coerceIn(dates)) }
    var to by remember { mutableStateOf(initialTo.coerceIn(dates)) }
    var showError by remember { mutableStateOf(false) }

    EditDialogFrame(onDismiss = onDismiss) {
        SelectionModal(
            title = title,
            onSaveClick = {
                if (onSave(from, to)) onDismiss() else showError = true
            },
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                DayChipRow(stringResource(R.string.edit_itinerary_from_label), days, from) { from = it; showError = false }
                DayChipRow(stringResource(R.string.edit_itinerary_to_label), days, to) { to = it; showError = false }
                if (showError) {
                    Text(
                        text = stringResource(R.string.edit_itinerary_dates_invalid),
                        color = WaypointTerracotta,
                        fontSize = 12.sp,
                    )
                }
            }
        }
    }
}

/** Edits the short note shown under a saved place. Clearing the text removes the note. */
@Composable
fun PlaceNoteDialog(
    placeName: String,
    initialNote: String?,
    onSave: (String) -> Boolean,
    onDismiss: () -> Unit,
) {
    var note by remember { mutableStateOf(initialNote.orEmpty()) }
    val tooLong = !TripValidation.isPlaceNoteValid(note)

    EditDialogFrame(onDismiss = onDismiss) {
        SelectionModal(
            title = placeName,
            onSaveClick = { if (onSave(note)) onDismiss() },
        ) {
            Column {
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    placeholder = { Text(stringResource(R.string.edit_itinerary_note_hint)) },
                    label = { Text(stringResource(R.string.edit_itinerary_note_label)) },
                    isError = tooLong,
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = WaypointTerracotta,
                        focusedLabelColor = WaypointTerracotta,
                        cursorColor = WaypointTerracotta,
                        focusedTextColor = WaypointTextPrimary,
                        unfocusedTextColor = WaypointTextPrimary,
                    ),
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    text = stringResource(R.string.edit_itinerary_note_counter, note.trim().length, TripValidation.PLACE_NOTE_MAX),
                    color = if (tooLong) WaypointTerracotta else WaypointTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

@Composable
private fun EditDialogFrame(onDismiss: () -> Unit, content: @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismiss, properties = DialogProperties(usePlatformDefaultWidth = false)) {
        Box(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)) { content() }
    }
}

@Composable
private fun DayChipRow(label: String, days: List<DayItem>, selected: LocalDate, onSelect: (LocalDate) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, color = WaypointTextMuted, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(days, key = { it.dayId }) { day ->
                val isSelected = day.date == selected
                Box(
                    modifier = Modifier
                        .background(if (isSelected) WaypointTerracotta else WaypointCream, RoundedCornerShape(RadiusChip))
                        .border(1.dp, if (isSelected) WaypointTerracotta else WaypointBorderSoft, RoundedCornerShape(RadiusChip))
                        .clickable { onSelect(day.date) }
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                ) {
                    Text(
                        text = day.label,
                        color = if (isSelected) White else WaypointTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }
    }
}

/** Snaps a stored date onto the nearest itinerary day, in case days were removed since it was saved. */
private fun LocalDate.coerceIn(dates: List<LocalDate>): LocalDate = when {
    dates.isEmpty() || this in dates -> this
    isBefore(dates.first()) -> dates.first()
    isAfter(dates.last()) -> dates.last()
    else -> dates.firstOrNull { !it.isBefore(this) } ?: dates.last()
}
