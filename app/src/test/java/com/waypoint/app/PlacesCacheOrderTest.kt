package com.waypoint.app

import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.cache.PlacesCacheSerializer
import com.waypoint.app.features.explore.data.chooseStalePlacesFallback
import com.waypoint.app.features.explore.data.isUsablePlacesCache
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset

class PlacesCacheOrderTest {

    private val place = ExplorePlace("1", "Local Bistro", "restaurant", "amenity", -33.9, 18.4, "123 Main St", 150)

    private fun freshCache(places: List<ExplorePlace> = listOf(place)) = PlacesCache(
        city = "cape_town",
        fetchedDateUtc = LocalDate.now(ZoneOffset.UTC).toString(),
        places = places,
    )

    private fun staleCache(places: List<ExplorePlace> = listOf(place)) = PlacesCache(
        city = "cape_town",
        fetchedDateUtc = "2020-01-01",
        places = places,
    )

    @Test
    fun isUsablePlacesCache_null_isNotUsable() {
        assertFalse(isUsablePlacesCache(null))
    }

    @Test
    fun isUsablePlacesCache_fresh_isUsable() {
        assertTrue(isUsablePlacesCache(freshCache()))
    }

    @Test
    fun isUsablePlacesCache_stale_isNotUsable() {
        assertFalse(isUsablePlacesCache(staleCache()))
    }

    @Test
    fun isUsablePlacesCache_freshButEmpty_isNotUsable() {
        assertFalse(isUsablePlacesCache(freshCache(emptyList())))
    }

    @Test
    fun chooseStalePlacesFallback_prefersLocalOverShared() {
        val local = staleCache()
        val shared = staleCache(listOf(place.copy(id = "2")))
        assertEquals(local, chooseStalePlacesFallback(local, shared))
    }

    @Test
    fun chooseStalePlacesFallback_fallsBackToSharedWhenNoLocal() {
        val shared = staleCache()
        assertEquals(shared, chooseStalePlacesFallback(null, shared))
    }

    @Test
    fun chooseStalePlacesFallback_nullWhenBothMissing() {
        assertNull(chooseStalePlacesFallback(null, null))
    }

    @Test
    fun serializer_roundTripsPlacesCache() {
        val original = freshCache(listOf(place, place.copy(id = "2", name = "Central Park")))
        val json = PlacesCacheSerializer.toJson(original)
        val parsed = PlacesCacheSerializer.fromJson(json)
        assertEquals(original, parsed)
    }

    @Test
    fun serializer_fromJson_returnsNullForMalformedJson() {
        val malformed = JSONObject().apply { put("city", "cape_town") }
        assertNull(PlacesCacheSerializer.fromJson(malformed))
    }
}
