package com.waypoint.app.features.edititinerary.data

import java.time.LocalDate

/** A day of a trip that the user has chosen to plan. */
data class ItineraryDayEntity(
    val id: String,
    val tripId: String,
    val date: LocalDate,
)

/** A flight on an itinerary day, with its booking PDF and looked-up departure time. */
data class FlightEntity(
    val id: String,
    val dayId: String,
    val flightNumber: String?,
    val pdfUri: String,
    val createdAtMs: Long,
    val departureTime: String? = null,
    val docName: String? = null,
)

/** A lodging booking covering a range of days. */
data class LodgingEntity(
    val id: String,
    val tripId: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val createdAtMs: Long,
    val docName: String? = null,
) {
    fun covers(date: LocalDate) = !date.isBefore(fromDate) && !date.isAfter(toDate)
}

/** A car rental covering a range of days. */
data class CarRentalEntity(
    val id: String,
    val tripId: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val createdAtMs: Long,
    val docName: String? = null,
) {
    fun covers(date: LocalDate) = !date.isBefore(fromDate) && !date.isAfter(toDate)
}

/** A place to visit on an itinerary day, with an optional note. */
data class PlaceEntity(
    val id: String,
    val dayId: String,
    val name: String,
    val category: String,
    val lat: Double?,
    val lng: Double?,
    val note: String?,
    val photoUrl: String? = null,
    val createdAtMs: Long,
)
