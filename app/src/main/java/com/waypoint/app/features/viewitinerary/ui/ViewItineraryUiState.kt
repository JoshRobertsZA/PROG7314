package com.waypoint.app.features.viewitinerary.ui

import java.time.LocalDate

data class ViewDayItem(
    val dayId: String,
    val date: LocalDate,
)

data class ViewFlightItem(
    val id: String,
    val flightNumber: String?,
    val pdfUri: String,
    /** "HH:mm" local, null if never set or resolved. */
    val departureTime: String? = null,
    val docName: String? = null,
)

data class ViewLodgingItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

data class ViewCarRentalItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

data class ViewPlaceItem(
    val id: String,
    val name: String,
    val category: String,
    val note: String?,
    val lat: Double? = null,
    val lng: Double? = null,
    val photoUrl: String? = null,
)

data class ViewItineraryUiState(
    val isLoading: Boolean = true,
    val days: List<ViewDayItem> = emptyList(),
    val activeDayIndex: Int = 0,
    val flightsForActiveDay: List<ViewFlightItem> = emptyList(),
    val lodgings: List<ViewLodgingItem> = emptyList(),
    val carRentals: List<ViewCarRentalItem> = emptyList(),
    val placesForActiveDay: Map<String, List<ViewPlaceItem>> = emptyMap(),
    val selectedPlace: ViewPlaceItem? = null,
) {
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    val lodgingForActiveDay: List<ViewLodgingItem>
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    val carRentalsForActiveDay: List<ViewCarRentalItem>
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
}

