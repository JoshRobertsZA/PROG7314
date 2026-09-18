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
)

data class ViewLodgingItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
)

data class ViewCarRentalItem(
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
)

data class ViewPlaceItem(
    val id: String,
    val name: String,
    val category: String,
    val note: String?,
)

data class ViewItineraryUiState(
    val isLoading: Boolean = true,
    val days: List<ViewDayItem> = emptyList(),
    val activeDayIndex: Int = 0,
    val flightsForActiveDay: List<ViewFlightItem> = emptyList(),
    val lodging: ViewLodgingItem? = null,
    val carRental: ViewCarRentalItem? = null,
    val placesForActiveDay: Map<String, List<ViewPlaceItem>> = emptyMap(),
)
