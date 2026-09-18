package com.waypoint.app.features.tripcalendar.ui

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

data class TripCalendarUiState(
    val isLoading: Boolean = true,
    val tripId: String = "",
    val tripName: String = "",
    val destination: String? = null,
    val destLat: Double? = null,
    val destLng: Double? = null,
    val nightCount: Int = 0,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val displayMonth: YearMonth = YearMonth.now(),
    val dateRangeLabel: String = "",
    val dayCountLabel: String = "",
    // Edit name dialog
    val showNameDialog: Boolean = false,
    val nameInput: String = "",
    // Destination search
    val showDestSearch: Boolean = false,
    val isGeocodingDest: Boolean = false,
    // Day selection
    val selectedDays: Set<LocalDate> = emptySet(),
    val showNoDaysError: Boolean = false,
) {
    /** "3 days selected · Jul 3 - Jul 5", or blank when nothing is selected. */
    val selectionLabel: String
        get() {
            if (selectedDays.isEmpty()) return ""
            val sorted = selectedDays.sorted()
            val fmt    = DateTimeFormatter.ofPattern("MMM d")
            val n      = sorted.size
            return if (n == 1) "1 day selected · ${sorted.first().format(fmt)}"
            else "$n days selected · ${sorted.first().format(fmt)} - ${sorted.last().format(fmt)}"
        }
}
