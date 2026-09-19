// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.features.edititinerary.data.CarRentalEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.CarRentalEntity
// imports `com.waypoint.app.features.edititinerary.data.FlightEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.FlightEntity
// imports `com.waypoint.app.features.edititinerary.data.ItineraryDayEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryDayEntity
// imports `com.waypoint.app.features.edititinerary.data.LodgingEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.LodgingEntity
// imports `com.waypoint.app.features.edititinerary.data.PlaceEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.PlaceEntity
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Assert.assertNull` for use in this file
import org.junit.Assert.assertNull
// imports `org.junit.Test` for use in this file
import org.junit.Test
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// declares class `ItineraryEntitiesTest` and opens its body
class ItineraryEntitiesTest {

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `itineraryDayEntity_initialization` taking no parameters and opens its body
    fun itineraryDayEntity_initialization() {
        // declares read-only property `date`, initialised with the result of calling `LocalDate.of(…)`
        val date = LocalDate.of(2026, 3, 1)
        // declares read-only property `day`, initialised with the result of calling `ItineraryDayEntity(…)`
        val day = ItineraryDayEntity(
            // continues the statement started above: `id = "day-1",`
            id = "day-1",
            // continues the statement started above: `tripId = "trip-123",`
            tripId = "trip-123",
            // continues the statement started above: `date = date`
            date = date
        // closes the multi-line argument list started above
        )

        // calls `assertEquals` with arguments `("day-1", day.id)`
        assertEquals("day-1", day.id)
        // calls `assertEquals` with arguments `("trip-123", day.tripId)`
        assertEquals("trip-123", day.tripId)
        // calls `assertEquals` with arguments `(date, day.date)`
        assertEquals(date, day.date)
    // closes the function `itineraryDayEntity_initialization`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `flightEntity_initialization` taking no parameters and opens its body
    fun flightEntity_initialization() {
        // declares read-only property `flight`, initialised with the result of calling `FlightEntity(…)`
        val flight = FlightEntity(
            // continues the statement started above: `id = "f-1",`
            id = "f-1",
            // continues the statement started above: `dayId = "day-1",`
            dayId = "day-1",
            // continues the statement started above: `flightNumber = "SA 321",`
            flightNumber = "SA 321",
            // continues the statement started above: `pdfUri = "content://media/external/file/10",`
            pdfUri = "content://media/external/file/10",
            // continues the statement started above: `createdAtMs = 1000L`
            createdAtMs = 1000L
        // closes the multi-line argument list started above
        )

        // calls `assertEquals` with arguments `("SA 321", flight.flightNumber)`
        assertEquals("SA 321", flight.flightNumber)
        // calls `assertEquals` with arguments `("content://media/external/file/10", flight.p…)`
        assertEquals("content://media/external/file/10", flight.pdfUri)
    // closes the function `flightEntity_initialization`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `lodgingAndCarRental_dateRangeVerification` taking no parameters and opens its body
    fun lodgingAndCarRental_dateRangeVerification() {
        // declares read-only property `from`, initialised with the result of calling `LocalDate.of(…)`
        val from = LocalDate.of(2026, 3, 1)
        // declares read-only property `to`, initialised with the result of calling `LocalDate.of(…)`
        val to = LocalDate.of(2026, 3, 7)

        // declares read-only property `lodging`, initialised with the result of calling `LodgingEntity(…)`
        val lodging = LodgingEntity(
            // continues the statement started above: `id = "l-1",`
            id = "l-1",
            // continues the statement started above: `tripId = "trip-123",`
            tripId = "trip-123",
            // continues the statement started above: `fromDate = from,`
            fromDate = from,
            // continues the statement started above: `toDate = to,`
            toDate = to,
            // continues the statement started above: `pdfUri = "content://lodging",`
            pdfUri = "content://lodging",
            // continues the statement started above: `createdAtMs = 2000L`
            createdAtMs = 2000L
        // closes the multi-line argument list started above
        )

        // declares read-only property `car`, initialised with the result of calling `CarRentalEntity(…)`
        val car = CarRentalEntity(
            // continues the statement started above: `id = "c-1",`
            id = "c-1",
            // continues the statement started above: `tripId = "trip-123",`
            tripId = "trip-123",
            // continues the statement started above: `fromDate = from,`
            fromDate = from,
            // continues the statement started above: `toDate = to,`
            toDate = to,
            // continues the statement started above: `pdfUri = "content://car",`
            pdfUri = "content://car",
            // continues the statement started above: `createdAtMs = 2000L`
            createdAtMs = 2000L
        // closes the multi-line argument list started above
        )

        // calls `assertEquals` with arguments `(from, lodging.fromDate)`
        assertEquals(from, lodging.fromDate)
        // calls `assertEquals` with arguments `(to, lodging.toDate)`
        assertEquals(to, lodging.toDate)
        // calls `assertEquals` with arguments `(from, car.fromDate)`
        assertEquals(from, car.fromDate)
        // calls `assertEquals` with arguments `(to, car.toDate)`
        assertEquals(to, car.toDate)
    // closes the function `lodgingAndCarRental_dateRangeVerification`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `placeEntity_initializationWithOptionalFields` taking no parameters and opens its body
    fun placeEntity_initializationWithOptionalFields() {
        // declares read-only property `place`, initialised with the result of calling `PlaceEntity(…)`
        val place = PlaceEntity(
            // continues the statement started above: `id = "p-1",`
            id = "p-1",
            // continues the statement started above: `dayId = "day-1",`
            dayId = "day-1",
            // continues the statement started above: `name = "Table Mountain Aerial Cableway",`
            name = "Table Mountain Aerial Cableway",
            // continues the statement started above: `category = "PARKS",`
            category = "PARKS",
            // continues the statement started above: `lat = -33.9573,`
            lat = -33.9573,
            // continues the statement started above: `lng = 18.4031,`
            lng = 18.4031,
            // continues the statement started above: `note = "Book tickets online in advance",`
            note = "Book tickets online in advance",
            // continues the statement started above: `photoUrl = null,`
            photoUrl = null,
            // continues the statement started above: `createdAtMs = 3000L`
            createdAtMs = 3000L
        // closes the multi-line argument list started above
        )

        // calls `assertEquals` with arguments `("Table Mountain Aerial Cableway", place.name)`
        assertEquals("Table Mountain Aerial Cableway", place.name)
        // calls `assertEquals` with arguments `("PARKS", place.category)`
        assertEquals("PARKS", place.category)
        // calls `assertEquals` with arguments `("Book tickets online in advance", place.note)`
        assertEquals("Book tickets online in advance", place.note)
        // calls `assertNull` with arguments `(place.photoUrl)`
        assertNull(place.photoUrl)
    // closes the function `placeEntity_initializationWithOptionalFields`
    }
// closes the class `ItineraryEntitiesTest`
}
