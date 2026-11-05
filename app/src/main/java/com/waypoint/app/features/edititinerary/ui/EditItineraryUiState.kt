package com.waypoint.app.features.edititinerary.ui

import java.time.LocalDate

/** Which kind of booking PDF the file picker is choosing. */
enum class ItineraryUploadType {
    FLIGHT,
    LODGING,
    CAR_RENTAL,
}

/** An itinerary day in the day scroller. */
data class DayItem(
    val dayId: String,
    val date: LocalDate,
) {
    /** e.g. "5 Nov", with the month name in the app's current language. */
    val label: String
        get() = date.format(java.time.format.DateTimeFormatter.ofPattern("d MMM", java.util.Locale.getDefault()))
}

/** A flight card on the edit screen. */
data class FlightItem(
    val id: String,
    val dayId: String,
    val flightNumber: String,
    val pdfUri: String,
    val departureTime: String? = null,
    val docName: String? = null,
)

/** A lodging card on the edit screen. */
data class LodgingItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

/** A car-rental card on the edit screen. */
data class CarRentalItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)


/** A place card on the edit screen. */
data class PlaceItem(
    val id: String,
    val dayId: String,
    val name: String,
    val category: String,
    val note: String?,
    val photoUrl: String? = null,
)

/** State of the edit screen; lodging and car rentals are filtered to the selected day. */
data class EditItineraryUiState(
    val isLoading: Boolean = true,
    val days: List<DayItem> = emptyList(),
    val activeDayIndex: Int = 0,
    val flightsForActiveDay: List<FlightItem> = emptyList(),
    val lodgings: List<LodgingItem> = emptyList(),
    val carRentals: List<CarRentalItem> = emptyList(),
    val pendingUploadType: ItineraryUploadType? = null,
    val placesForActiveDay: Map<String, List<PlaceItem>> = emptyMap(),
) {
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    val lodgingForActiveDay: List<LodgingItem>
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    val carRentalsForActiveDay: List<CarRentalItem>
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
}
