package com.waypoint.app.features.edititinerary.data

import java.time.LocalDate

/** A single selected day in the itinerary. */
data class ItineraryDayEntity(
    val id: String,
    val tripId: String,
    val date: LocalDate,
)

/** A flight document attached to one specific day. */
data class FlightEntity(
    val id: String,
    val dayId: String,
    /** e.g. "SA 123" - may be blank until the user types it. */
    val flightNumber: String?,
    /** content:// URI string (persisted permission already taken). */
    val pdfUri: String,
    val createdAtMs: Long,
    /** Local departure time "HH:mm"; null until the user sets it or AirLabs resolves it. */
    val departureTime: String? = null,
    /** Display name of the PDF as shown by the picker, e.g. "SA123-boarding.pdf". */
    val docName: String? = null,
)

/**
 * A lodging document covering [fromDate]..[toDate] - the days that were
 * selected when it was uploaded. A trip can hold several; screens show
 * only the ones whose range includes the day being viewed.
 */
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

/** A car-rental document; same date-range semantics as [LodgingEntity]. */
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

/** A place (hotel, park, pub, or cinema) pinned to a specific itinerary day. */
data class PlaceEntity(
    val id: String,
    val dayId: String,
    val name: String,
    /** One of: HOTELS, PARKS, PUBS, CINEMAS - matches ExploreFilter enum name. */
    val category: String,
    val lat: Double?,
    val lng: Double?,
    val note: String?,
    val photoUrl: String? = null,
    val createdAtMs: Long,
)
