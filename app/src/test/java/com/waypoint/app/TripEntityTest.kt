// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.db.TripEntity` for use in this file
import com.waypoint.app.core.db.TripEntity
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Assert.assertNull` for use in this file
import org.junit.Assert.assertNull
// imports `org.junit.Assert.assertTrue` for use in this file
import org.junit.Assert.assertTrue
// imports `org.junit.Test` for use in this file
import org.junit.Test

// declares class `TripEntityTest` and opens its body
class TripEntityTest {

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `tripEntity_creationAndProperties` taking no parameters and opens its body
    fun tripEntity_creationAndProperties() {
        // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
        val now = System.currentTimeMillis()
        // declares read-only property `trip`, initialised with the result of calling `TripEntity(…)`
        val trip = TripEntity(
            // continues the statement started above: `id = "trip-123",`
            id = "trip-123",
            // continues the statement started above: `accountId = "google-sub-456",`
            accountId = "google-sub-456",
            // continues the statement started above: `name = "Cape Town Getaway",`
            name = "Cape Town Getaway",
            // continues the statement started above: `startDate = "2026-03-01",`
            startDate = "2026-03-01",
            // continues the statement started above: `endDate = "2026-03-07",`
            endDate = "2026-03-07",
            // continues the statement started above: `destination = "Cape Town",`
            destination = "Cape Town",
            // continues the statement started above: `destLat = -33.9249,`
            destLat = -33.9249,
            // continues the statement started above: `destLng = 18.4241,`
            destLng = 18.4241,
            // continues the statement started above: `createdAtMs = now,`
            createdAtMs = now,
            // continues the statement started above: `updatedAtMs = now`
            updatedAtMs = now
        // closes the multi-line argument list started above
        )

        // calls `assertEquals` with arguments `("trip-123", trip.id)`
        assertEquals("trip-123", trip.id)
        // calls `assertEquals` with arguments `("google-sub-456", trip.accountId)`
        assertEquals("google-sub-456", trip.accountId)
        // calls `assertEquals` with arguments `("Cape Town Getaway", trip.name)`
        assertEquals("Cape Town Getaway", trip.name)
        // calls `assertEquals` with arguments `("2026-03-01", trip.startDate)`
        assertEquals("2026-03-01", trip.startDate)
        // calls `assertEquals` with arguments `("2026-03-07", trip.endDate)`
        assertEquals("2026-03-07", trip.endDate)
        // calls `assertEquals` with arguments `("Cape Town", trip.destination)`
        assertEquals("Cape Town", trip.destination)
        // calls `assertEquals` with arguments `(-33.9249, trip.destLat!!, 0.0001)`
        assertEquals(-33.9249, trip.destLat!!, 0.0001)
        // calls `assertEquals` with arguments `(18.4241, trip.destLng!!, 0.0001)`
        assertEquals(18.4241, trip.destLng!!, 0.0001)
    // closes the function `tripEntity_creationAndProperties`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `tripEntity_dateComparison_isoFormatSortsChronologically` taking no parameters and opens its body
    fun tripEntity_dateComparison_isoFormatSortsChronologically() {
        // declares read-only property `date1`, initialised to the string literal "2026-01-15"
        val date1 = "2026-01-15"
        // declares read-only property `date2`, initialised to the string literal "2026-03-01"
        val date2 = "2026-03-01"

        // calls `assertTrue` with arguments `(date1 < date2)`
        assertTrue(date1 < date2)
    // closes the function `tripEntity_dateComparison_isoFormatSortsChronologically`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `tripEntity_nullableDestination_handlesNulls` taking no parameters and opens its body
    fun tripEntity_nullableDestination_handlesNulls() {
        // declares read-only property `trip`, initialised with the result of calling `TripEntity(…)`
        val trip = TripEntity(
            // continues the statement started above: `id = "trip-999",`
            id = "trip-999",
            // continues the statement started above: `accountId = "acc-1",`
            accountId = "acc-1",
            // continues the statement started above: `name = "Road Trip",`
            name = "Road Trip",
            // continues the statement started above: `startDate = "2026-04-10",`
            startDate = "2026-04-10",
            // continues the statement started above: `endDate = "2026-04-15",`
            endDate = "2026-04-15",
            // continues the statement started above: `destination = null,`
            destination = null,
            // continues the statement started above: `destLat = null,`
            destLat = null,
            // continues the statement started above: `destLng = null,`
            destLng = null,
            // continues the statement started above: `createdAtMs = 1000L,`
            createdAtMs = 1000L,
            // continues the statement started above: `updatedAtMs = 1000L`
            updatedAtMs = 1000L
        // closes the multi-line argument list started above
        )

        // calls `assertNull` with arguments `(trip.destination)`
        assertNull(trip.destination)
        // calls `assertNull` with arguments `(trip.destLat)`
        assertNull(trip.destLat)
        // calls `assertNull` with arguments `(trip.destLng)`
        assertNull(trip.destLng)
    // closes the function `tripEntity_nullableDestination_handlesNulls`
    }
// closes the class `TripEntityTest`
}
