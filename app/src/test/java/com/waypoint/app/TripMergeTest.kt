package com.waypoint.app

import com.waypoint.app.features.newtrip.data.shouldApplyCloudTrip
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

// Covers the last-write-wins decision used by
// TripRepository.pullAndMergeTrips(). shouldApplyCloudTrip() is a
// top-level function, not a TripRepository member, specifically so it's
// callable here without constructing TripRepository (its constructor
// needs a real Context) - this project's unit tests run on the plain JVM
// with no Robolectric.
class TripMergeTest {

    @Test
    fun cloudNewerThanLocal_appliesCloud() {
        assertTrue(shouldApplyCloudTrip(localUpdatedAtMs = 1_000L, cloudUpdatedAtMs = 2_000L))
    }

    @Test
    fun localNewerThanCloud_keepsLocal() {
        // The unsent-local-change case this is meant to protect:
        // a device that edited a trip but hasn't synced yet must not have
        // its own change overwritten by what the cloud still shows.
        assertFalse(shouldApplyCloudTrip(localUpdatedAtMs = 2_000L, cloudUpdatedAtMs = 1_000L))
    }

    @Test
    fun equalTimestamps_appliesCloud() {
        // Ties go to the cloud rather than being treated as "local wins" -
        // there's no local change to protect if nothing has changed.
        assertTrue(shouldApplyCloudTrip(localUpdatedAtMs = 1_000L, cloudUpdatedAtMs = 1_000L))
    }

    @Test
    fun noLocalRowYet_appliesCloud() {
        // First-ever pull of a trip this device has never seen before.
        assertTrue(shouldApplyCloudTrip(localUpdatedAtMs = null, cloudUpdatedAtMs = 1_000L))
    }
}
