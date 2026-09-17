package com.example.prog7314.features.tripcalendar.ui

import java.time.LocalDate
import java.time.YearMonth

data class TripCalendarUiState(
    val isLoading: Boolean = true,
    val tripName: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    /** The month currently shown in the calendar grid. */
    val displayMonth: YearMonth = YearMonth.now(),
    /** "Aug 2 - Aug 9, 2026" */
    val dateRangeLabel: String = "",
    /** "8 days" */
    val dayCountLabel: String = "",
)
