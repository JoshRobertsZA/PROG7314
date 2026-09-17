package com.example.prog7314.features.newtrip.ui

import java.time.LocalDate
import java.time.YearMonth

/**
 * UI state for the New Trip screen.
 *
 * [displayMonth]   — the month currently shown in the calendar grid.
 * [startDate]      — first selected day (null = nothing picked yet).
 * [endDate]        — last selected day (null = only one day picked so far).
 * [showYearPicker] — true while the year/month overlay is open.
 * [isSaving]       — true while the repository insert is in flight.
 */
data class NewTripUiState(
    val tripName: String = "",
    val displayMonth: YearMonth = YearMonth.now(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val showYearPicker: Boolean = false,
    val isSaving: Boolean = false,
) {
    /** True only when both dates are set and the name is non-blank. */
    val canSave: Boolean
        get() = tripName.isNotBlank() && startDate != null && endDate != null

    /** Number of inclusive days in the selected range, or 0 if incomplete. */
    val selectedDayCount: Int
        get() = if (startDate != null && endDate != null)
            (endDate.toEpochDay() - startDate.toEpochDay() + 1).toInt()
        else 0
}
