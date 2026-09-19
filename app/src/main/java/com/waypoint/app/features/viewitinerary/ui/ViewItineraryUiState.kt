// declares that this file belongs to the package `com.waypoint.app.features.viewitinerary.ui`
package com.waypoint.app.features.viewitinerary.ui

// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// expression: `data class ViewDayItem(`
data class ViewDayItem(
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val date: LocalDate,`
    val date: LocalDate,
// closes the multi-line argument list started above
)

// expression: `data class ViewFlightItem(`
data class ViewFlightItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val flightNumber: String?,`
    val flightNumber: String?,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    val departureTime: String? = null,
    // arrival time populated from AirLabs status cache when flight is active or landed
    val arrivalTime: String? = null,
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class ViewLodgingItem(`
data class ViewLodgingItem(
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class ViewCarRentalItem(`
data class ViewCarRentalItem(
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class ViewPlaceItem(`
data class ViewPlaceItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val category: String,`
    val category: String,
    // continues the statement started above: `val note: String?,`
    val note: String?,
    // continues the statement started above: `val lat: Double? = null,`
    val lat: Double? = null,
    // continues the statement started above: `val lng: Double? = null,`
    val lng: Double? = null,
    // continues the statement started above: `val photoUrl: String? = null,`
    val photoUrl: String? = null,
// closes the multi-line argument list started above
)

// sealed class representing the loading/success/idle states for the flight status modal
sealed class FlightStatusState {
    // idle: modal is not visible
    object Idle : FlightStatusState()
    // loading: fetch is in progress, shows spinner
    object Loading : FlightStatusState()
    // success: fetch returned data, shows modal
    data class Success(val result: com.waypoint.app.core.network.AirLabsRepository.FlightStatusResult) : FlightStatusState()
    // error: fetch failed, shows error text
    data class Error(val message: String) : FlightStatusState()
}

// expression: `data class ViewItineraryUiState(`
data class ViewItineraryUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val days: List<ViewDayItem> = emptyList(),`
    val days: List<ViewDayItem> = emptyList(),
    // continues the statement started above: `val activeDayIndex: Int = 0,`
    val activeDayIndex: Int = 0,
    // continues the statement started above: `val flightsForActiveDay: List<ViewFlightItem> = emptyList(),`
    val flightsForActiveDay: List<ViewFlightItem> = emptyList(),
    // continues the statement started above: `val lodgings: List<ViewLodgingItem> = emptyList(),`
    val lodgings: List<ViewLodgingItem> = emptyList(),
    // continues the statement started above: `val carRentals: List<ViewCarRentalItem> = emptyList(),`
    val carRentals: List<ViewCarRentalItem> = emptyList(),
    // continues the statement started above: `val placesForActiveDay: Map<String, List<ViewPlaceItem>> = …`
    val placesForActiveDay: Map<String, List<ViewPlaceItem>> = emptyMap(),
    // continues the statement started above: `val selectedPlace: ViewPlaceItem? = null,`
    val selectedPlace: ViewPlaceItem? = null,
    // continues the statement started above: `val flightStatus: FlightStatusState = FlightStatusState.Idle,`
    val flightStatus: FlightStatusState = FlightStatusState.Idle,
// ends the argument list started above and opens the block that follows
) {
    // expression: `private val activeDate: LocalDate? get() = days.getOrNull(active…`
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    // declares read-only property `lodgingForActiveDay` of type `List<ViewLodgingItem>`
    val lodgingForActiveDay: List<ViewLodgingItem>
        // custom getter: returns `activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && …`
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    // declares read-only property `carRentalsForActiveDay` of type `List<ViewCarRentalItem>`
    val carRentalsForActiveDay: List<ViewCarRentalItem>
        // custom getter: returns `activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) &…`
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
// closes the block
}

