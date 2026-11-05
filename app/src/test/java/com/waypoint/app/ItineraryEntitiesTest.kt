package com.waypoint.app

import com.waypoint.app.features.edititinerary.data.CarRentalEntity
import com.waypoint.app.features.edititinerary.data.FlightEntity
import com.waypoint.app.features.edititinerary.data.ItineraryDayEntity
import com.waypoint.app.features.edititinerary.data.LodgingEntity
import com.waypoint.app.features.edititinerary.data.PlaceEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.time.LocalDate

class ItineraryEntitiesTest {

    @Test
    fun itineraryDayEntity_initialization() {
        val date = LocalDate.of(2026, 3, 1)
        val day = ItineraryDayEntity(
            id = "day-1",
            tripId = "trip-123",
            date = date
        )

        assertEquals("day-1", day.id)
        assertEquals("trip-123", day.tripId)
        assertEquals(date, day.date)
    }

    @Test
    fun flightEntity_initialization() {
        val flight = FlightEntity(
            id = "f-1",
            dayId = "day-1",
            flightNumber = "SA 321",
            pdfUri = "content://media/external/file/10",
            createdAtMs = 1000L
        )

        assertEquals("SA 321", flight.flightNumber)
        assertEquals("content://media/external/file/10", flight.pdfUri)
    }

    @Test
    fun lodgingAndCarRental_dateRangeVerification() {
        val from = LocalDate.of(2026, 3, 1)
        val to = LocalDate.of(2026, 3, 7)

        val lodging = LodgingEntity(
            id = "l-1",
            tripId = "trip-123",
            fromDate = from,
            toDate = to,
            pdfUri = "content://lodging",
            createdAtMs = 2000L
        )

        val car = CarRentalEntity(
            id = "c-1",
            tripId = "trip-123",
            fromDate = from,
            toDate = to,
            pdfUri = "content://car",
            createdAtMs = 2000L
        )

        assertEquals(from, lodging.fromDate)
        assertEquals(to, lodging.toDate)
        assertEquals(from, car.fromDate)
        assertEquals(to, car.toDate)
    }

    @Test
    fun placeEntity_initializationWithOptionalFields() {
        val place = PlaceEntity(
            id = "p-1",
            dayId = "day-1",
            name = "Table Mountain Aerial Cableway",
            category = "PARKS",
            lat = -33.9573,
            lng = 18.4031,
            note = "Book tickets online in advance",
            photoUrl = null,
            createdAtMs = 3000L
        )

        assertEquals("Table Mountain Aerial Cableway", place.name)
        assertEquals("PARKS", place.category)
        assertEquals("Book tickets online in advance", place.note)
        assertNull(place.photoUrl)
    }
}
