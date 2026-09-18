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
)

/**
 * A lodging document that spans the entire trip selection.
 * [fromDate] = first selected day, [toDate] = last selected day.
 */
data class LodgingEntity(
    val id: String,
    val tripId: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val createdAtMs: Long,
)

/**
 * A car-rental document that spans the entire trip selection.
 * Same date-range semantics as [LodgingEntity].
 */
data class CarRentalEntity(
    val id: String,
    val tripId: String,
    val fromDate: LocalDate,
    val toDate: LocalDate,
    val pdfUri: String,
    val createdAtMs: Long,
)

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
    val createdAtMs: Long,
)
