package com.waypoint.app

import com.waypoint.app.core.db.TripEntity
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TripEntityTest {

    @Test
    fun tripEntity_creationAndProperties() {
        val now = System.currentTimeMillis()
        val trip = TripEntity(
            id = "trip-123",
            accountId = "google-sub-456",
            name = "Cape Town Getaway",
            startDate = "2026-03-01",
            endDate = "2026-03-07",
            destination = "Cape Town",
            destLat = -33.9249,
            destLng = 18.4241,
            createdAtMs = now,
            updatedAtMs = now
        )

        assertEquals("trip-123", trip.id)
        assertEquals("google-sub-456", trip.accountId)
        assertEquals("Cape Town Getaway", trip.name)
        assertEquals("2026-03-01", trip.startDate)
        assertEquals("2026-03-07", trip.endDate)
        assertEquals("Cape Town", trip.destination)
        assertEquals(-33.9249, trip.destLat!!, 0.0001)
        assertEquals(18.4241, trip.destLng!!, 0.0001)
    }

    @Test
    fun tripEntity_dateComparison_isoFormatSortsChronologically() {
        val date1 = "2026-01-15"
        val date2 = "2026-03-01"

        assertTrue(date1 < date2)
    }

    @Test
    fun tripEntity_nullableDestination_handlesNulls() {
        val trip = TripEntity(
            id = "trip-999",
            accountId = "acc-1",
            name = "Road Trip",
            startDate = "2026-04-10",
            endDate = "2026-04-15",
            destination = null,
            destLat = null,
            destLng = null,
            createdAtMs = 1000L,
            updatedAtMs = 1000L
        )

        assertNull(trip.destination)
        assertNull(trip.destLat)
        assertNull(trip.destLng)
    }
}
