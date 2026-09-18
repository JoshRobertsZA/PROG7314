package com.waypoint.app.features.edititinerary.ui

import java.time.LocalDate

/** Which PDF upload the user triggered - used to fire the system picker. */
enum class ItineraryUploadType {
    FLIGHT,
    LODGING,
    CAR_RENTAL,
}

/**
 * One card in the horizontal day-scroller.
 * [dayId] is the DB primary key; [date] drives the label.
 */
data class DayItem(
    val dayId: String,
    val date: LocalDate,
) {
    val label: String
        get() = "${date.dayOfMonth} ${
            date.month.name
                .lowercase()
                .replaceFirstChar { it.uppercaseChar() }
                .take(3)
        }"
}

/**
 * A flight PDF card shown under the active day.
 * [flightNumber] starts empty and can be typed by the user.
 */
data class FlightItem(
    val id: String,
    val dayId: String,
    val flightNumber: String,
    val pdfUri: String,
    /** "HH:mm" local, or null while unset. Drives early-vs-same-day flight reminders. */
    val departureTime: String? = null,
    val docName: String? = null,
)

/** One lodging document; only listed on days inside its range. */
data class LodgingItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)

/** One car-rental document; only listed on days inside its range. */
data class CarRentalItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val docName: String? = null,
)


/**
 * A place (hotel, park, pub, cinema) saved for a specific itinerary day.
 */
data class PlaceItem(
    val id: String,
    val dayId: String,
    val name: String,
    /** One of: HOTELS, PARKS, PUBS, CINEMAS */
    val category: String,
    val note: String?,
)

data class EditItineraryUiState(
    val isLoading: Boolean = true,
    val days: List<DayItem> = emptyList(),
    /** Index into [days] for the currently selected/active clock card. */
    val activeDayIndex: Int = 0,
    /**
     * Flights that belong to the active day.
     * Flights from other days are loaded lazily when the user scrolls to them.
     */
    val flightsForActiveDay: List<FlightItem> = emptyList(),
    /** All documents on the trip; filter with [lodgingForActiveDay] / [carRentalsForActiveDay]. */
    val lodgings: List<LodgingItem> = emptyList(),
    val carRentals: List<CarRentalItem> = emptyList(),
    /**
     * Non-null while the system PDF picker should be showing.
     * Cleared after the picker result arrives (success or cancel).
     */
    val pendingUploadType: ItineraryUploadType? = null,
    /**
     * Places for the active day, grouped by category key
     * (HOTELS, PARKS, PUBS, CINEMAS).
     */
    val placesForActiveDay: Map<String, List<PlaceItem>> = emptyMap(),
) {
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    val lodgingForActiveDay: List<LodgingItem>
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    val carRentalsForActiveDay: List<CarRentalItem>
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
}
