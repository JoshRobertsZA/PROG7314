package com.waypoint.app.features.viewitinerary.ui

import java.time.LocalDate

/** A day in the itinerary view. */
data class ViewDayItem(
    val dayId: String,
    val date: LocalDate,
)

/** A flight in the itinerary view. */
data class ViewFlightItem(
    val id: String,
    val flightNumber: String?,
    val pdfUri: String,
    val departureTime: String? = null,
    val arrivalTime: String? = null,
    val docName: String? = null,
)

/** A lodging booking in the itinerary view. */
data class ViewLodgingItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

/** A car rental in the itinerary view. */
data class ViewCarRentalItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

/** A place in the itinerary view. */
data class ViewPlaceItem(
    val id: String,
    val name: String,
    val category: String,
    val note: String?,
    val lat: Double? = null,
    val lng: Double? = null,
    val photoUrl: String? = null,
)

/** Loading state of a flight-status lookup. */
sealed class FlightStatusState {
    object Idle : FlightStatusState()
    object Loading : FlightStatusState()
    data class Success(val result: com.waypoint.app.core.network.AirLabsRepository.FlightStatusResult) : FlightStatusState()
    data class Error(val message: String) : FlightStatusState()
}

/** State of the itinerary view. */
data class ViewItineraryUiState(
    val isLoading: Boolean = true,
    val days: List<ViewDayItem> = emptyList(),
    val activeDayIndex: Int = 0,
    val flightsForActiveDay: List<ViewFlightItem> = emptyList(),
    val lodgings: List<ViewLodgingItem> = emptyList(),
    val carRentals: List<ViewCarRentalItem> = emptyList(),
    val placesForActiveDay: Map<String, List<ViewPlaceItem>> = emptyMap(),
    val selectedPlace: ViewPlaceItem? = null,
    val flightStatus: FlightStatusState = FlightStatusState.Idle,
) {
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    val lodgingForActiveDay: List<ViewLodgingItem>
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    val carRentalsForActiveDay: List<ViewCarRentalItem>
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
}

