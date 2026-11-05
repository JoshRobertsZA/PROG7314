package com.waypoint.app.features.tripcalendar.ui

import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

/** State of the trip overview. */
data class TripCalendarUiState(
    val isLoading: Boolean = true,
    val tripId: String = "",
    val tripName: String = "",
    val destination: String? = null,
    val destLat: Double? = null,
    val destLng: Double? = null,
    val destPhotoUrl: String? = null,
    val nightCount: Int = 0,
    val flightCount: Int = 0,
    val stayCount: Int = 0,
    val rentalCount: Int = 0,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val displayMonth: YearMonth = YearMonth.now(),
    val dateRangeLabel: String = "",
    val dayCount: Int = 0,
    val showNameDialog: Boolean = false,
    val nameInput: String = "",
    val showDeleteConfirm: Boolean = false,
    val showEditDates: Boolean = false,
    val editStart: LocalDate? = null,
    val editEnd: LocalDate? = null,
    val editMonth: YearMonth = YearMonth.now(),
    val deleted: Boolean = false,
    val showDestSearch: Boolean = false,
    val isGeocodingDest: Boolean = false,
    val selectedDays: Set<LocalDate> = emptySet(),
    val showNoDaysError: Boolean = false,
) {
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
