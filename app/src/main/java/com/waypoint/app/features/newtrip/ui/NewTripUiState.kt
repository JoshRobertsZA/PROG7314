// declares that this file belongs to the package `com.waypoint.app.features.newtrip.ui`
package com.waypoint.app.features.newtrip.ui

// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `com.waypoint.app.core.common.TripValidation` for use in this file
import com.waypoint.app.core.common.TripValidation

// expression: `data class NewTripUiState(`
data class NewTripUiState(
    // continues the statement started above: `val tripName: String = "",`
    val tripName: String = "",
    // continues the statement started above: `val displayMonth: YearMonth = YearMonth.now(),`
    val displayMonth: YearMonth = YearMonth.now(),
    // continues the statement started above: `val startDate: LocalDate? = null,`
    val startDate: LocalDate? = null,
    // continues the statement started above: `val endDate: LocalDate? = null,`
    val endDate: LocalDate? = null,
    // continues the statement started above: `val showYearPicker: Boolean = false,`
    val showYearPicker: Boolean = false,
    // continues the statement started above: `val destinationName: String = "",`
    val destinationName: String = "",
    // continues the statement started above: `val destLat: Double? = null,`
    val destLat: Double? = null,
    // continues the statement started above: `val destLng: Double? = null,`
    val destLng: Double? = null,
    // continues the statement started above: `val destPhotoUrl: String? = null,`
    val destPhotoUrl: String? = null,
    // continues the statement started above: `val showDestSearch: Boolean = false,`
    val showDestSearch: Boolean = false,
    // continues the statement started above: `val isGeocodingDest: Boolean = false,`
    val isGeocodingDest: Boolean = false,
    // continues the statement started above: `val isSaving: Boolean = false,`
    val isSaving: Boolean = false,
// ends the argument list started above and opens the block that follows
) {

    // Trip can only be saved with a valid name, dates in the right order and a destination
    val canSave: Boolean
        get() = TripValidation.isTripNameValid(tripName) &&
                TripValidation.isDateOrderValid(startDate, endDate) &&
                destinationName.isNotBlank()

    // declares read-only property `selectedDayCount` of type `Int`
    val selectedDayCount: Int
        // custom getter: returns `if (startDate != null && endDate != null)`
        get() = if (startDate != null && endDate != null)
            // statement: `(endDate.toEpochDay() - startDate.toEpochDay() + 1).toInt()`
            (endDate.toEpochDay() - startDate.toEpochDay() + 1).toInt()
        // expression: `else 0`
        else 0
// closes the block
}
