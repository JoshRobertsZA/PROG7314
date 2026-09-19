// declares that this file belongs to the package `com.waypoint.app.features.tripcalendar.ui`
package com.waypoint.app.features.tripcalendar.ui

// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `java.time.format.DateTimeFormatter` for use in this file
import java.time.format.DateTimeFormatter

// expression: `data class TripCalendarUiState(`
data class TripCalendarUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val tripId: String = "",`
    val tripId: String = "",
    // continues the statement started above: `val tripName: String = "",`
    val tripName: String = "",
    // continues the statement started above: `val destination: String? = null,`
    val destination: String? = null,
    // continues the statement started above: `val destLat: Double? = null,`
    val destLat: Double? = null,
    // continues the statement started above: `val destLng: Double? = null,`
    val destLng: Double? = null,
    // continues the statement started above: `val destPhotoUrl: String? = null,`
    val destPhotoUrl: String? = null,
    // continues the statement started above: `val nightCount: Int = 0,`
    val nightCount: Int = 0,
    // continues the statement started above: `val flightCount: Int = 0,`
    val flightCount: Int = 0,
    // continues the statement started above: `val stayCount: Int = 0,`
    val stayCount: Int = 0,
    // continues the statement started above: `val rentalCount: Int = 0,`
    val rentalCount: Int = 0,
    // continues the statement started above: `val startDate: LocalDate? = null,`
    val startDate: LocalDate? = null,
    // continues the statement started above: `val endDate: LocalDate? = null,`
    val endDate: LocalDate? = null,
    // continues the statement started above: `val displayMonth: YearMonth = YearMonth.now(),`
    val displayMonth: YearMonth = YearMonth.now(),
    // continues the statement started above: `val dateRangeLabel: String = "",`
    val dateRangeLabel: String = "",
    // continues the statement started above: `val dayCount: Int = 0,`
    val dayCount: Int = 0,
    // continues the statement started above: `val showNameDialog: Boolean = false,`
    val showNameDialog: Boolean = false,
    // continues the statement started above: `val nameInput: String = "",`
    val nameInput: String = "",
    // continues the statement started above: `val showDeleteConfirm: Boolean = false,`
    val showDeleteConfirm: Boolean = false,
    // continues the statement started above: `val showEditDates: Boolean = false,`
    val showEditDates: Boolean = false,
    // continues the statement started above: `val editStart: LocalDate? = null,`
    val editStart: LocalDate? = null,
    // continues the statement started above: `val editEnd: LocalDate? = null,`
    val editEnd: LocalDate? = null,
    // continues the statement started above: `val editMonth: YearMonth = YearMonth.now(),`
    val editMonth: YearMonth = YearMonth.now(),
    // continues the statement started above: `val deleted: Boolean = false,`
    val deleted: Boolean = false,
    // continues the statement started above: `val showDestSearch: Boolean = false,`
    val showDestSearch: Boolean = false,
    // continues the statement started above: `val isGeocodingDest: Boolean = false,`
    val isGeocodingDest: Boolean = false,
    // continues the statement started above: `val selectedDays: Set<LocalDate> = emptySet(),`
    val selectedDays: Set<LocalDate> = emptySet(),
    // continues the statement started above: `val showNoDaysError: Boolean = false,`
    val showNoDaysError: Boolean = false,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `selectionLabel` of type `String`
    val selectionLabel: String
        // custom getter: opens the block that computes the property value
        get() {
            // `if` statement: executes `return ""` when `selectedDays.isEmpty()` is true
            if (selectedDays.isEmpty()) return ""
            // declares read-only property `sorted`, initialised with the result of calling `selectedDays.sorted(…)`
            val sorted = selectedDays.sorted()
            // declares read-only property `fmt`, initialised with the result of calling `DateTimeFormatter.ofPattern(…)`
            val fmt    = DateTimeFormatter.ofPattern("MMM d")
            // declares read-only property `n`, initialised to `sorted.size`
            val n      = sorted.size
            // returns `if (n == 1) "1 day selected · ${sorted.first().format(fmt)}"` from the current function
            return if (n == 1) "1 day selected · ${sorted.first().format(fmt)}"
            // expression: `else "$n days selected · ${sorted.first().format(fmt)} - ${sorte…`
            else "$n days selected · ${sorted.first().format(fmt)} - ${sorted.last().format(fmt)}"
        // closes the getter
        }
// closes the block
}
