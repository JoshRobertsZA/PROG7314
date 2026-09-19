// declares that this file belongs to the package `com.waypoint.app.features.edititinerary.data`
package com.waypoint.app.features.edititinerary.data

// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// expression: `data class ItineraryDayEntity(`
data class ItineraryDayEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val tripId: String,`
    val tripId: String,
    // continues the statement started above: `val date: LocalDate,`
    val date: LocalDate,
// closes the multi-line argument list started above
)

// expression: `data class FlightEntity(`
data class FlightEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val flightNumber: String?,`
    val flightNumber: String?,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val createdAtMs: Long,`
    val createdAtMs: Long,
    // continues the statement started above: `val departureTime: String? = null,`
    val departureTime: String? = null,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class LodgingEntity(`
data class LodgingEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val tripId: String,`
    val tripId: String,
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val createdAtMs: Long,`
    val createdAtMs: Long,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// ends the argument list started above and opens the block that follows
) {
    // declares function `covers` taking 1 parameter (`date`); its body is the expression `!date.isBefore(fromDate) && !date.isAfter(to…`
    fun covers(date: LocalDate) = !date.isBefore(fromDate) && !date.isAfter(toDate)
// closes the block
}

// expression: `data class CarRentalEntity(`
data class CarRentalEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val tripId: String,`
    val tripId: String,
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val createdAtMs: Long,`
    val createdAtMs: Long,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// ends the argument list started above and opens the block that follows
) {
    // declares function `covers` taking 1 parameter (`date`); its body is the expression `!date.isBefore(fromDate) && !date.isAfter(to…`
    fun covers(date: LocalDate) = !date.isBefore(fromDate) && !date.isAfter(toDate)
// closes the block
}

// expression: `data class PlaceEntity(`
data class PlaceEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val category: String,`
    val category: String,
    // continues the statement started above: `val lat: Double?,`
    val lat: Double?,
    // continues the statement started above: `val lng: Double?,`
    val lng: Double?,
    // continues the statement started above: `val note: String?,`
    val note: String?,
    // continues the statement started above: `val photoUrl: String? = null,`
    val photoUrl: String? = null,
    // continues the statement started above: `val createdAtMs: Long,`
    val createdAtMs: Long,
// closes the multi-line argument list started above
)
