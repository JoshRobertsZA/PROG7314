package com.example.prog7314.features.edititinerary.ui

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
)

/**
 * The single lodging document for the whole selection.
 * Null while no PDF has been uploaded.
 */
data class LodgingItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
)

/**
 * The single car-rental document for the whole selection.
 * Null while no PDF has been uploaded.
 */
data class CarRentalItem(
    val id: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
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
    val lodging: LodgingItem? = null,
    val carRental: CarRentalItem? = null,
    /**
     * Non-null while the system PDF picker should be showing.
     * Cleared after the picker result arrives (success or cancel).
     */
    val pendingUploadType: ItineraryUploadType? = null,
)
