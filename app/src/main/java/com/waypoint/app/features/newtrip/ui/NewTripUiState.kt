package com.waypoint.app.features.newtrip.ui

import java.time.LocalDate
import java.time.YearMonth
import com.waypoint.app.core.common.TripValidation

/** State of the new-trip sheet. */
data class NewTripUiState(
    val tripName: String = "",
    val displayMonth: YearMonth = YearMonth.now(),
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val showYearPicker: Boolean = false,
    val destinationName: String = "",
    val destLat: Double? = null,
    val destLng: Double? = null,
    val destPhotoUrl: String? = null,
    val showDestSearch: Boolean = false,
    val isGeocodingDest: Boolean = false,
    val isSaving: Boolean = false,
) {

    // Trip can only be saved with a valid name, dates in the right order and a destination
    val canSave: Boolean
        get() = TripValidation.isTripNameValid(tripName) &&
                TripValidation.isDateOrderValid(startDate, endDate) &&
                destinationName.isNotBlank()

    val selectedDayCount: Int
        get() = if (startDate != null && endDate != null)
            (endDate.toEpochDay() - startDate.toEpochDay() + 1).toInt()
        else 0
}
