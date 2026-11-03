package com.waypoint.app

import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.cache.PlacesCacheSerializer
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class PlacesCacheSerializerTest {

    private fun sampleCache() = PlacesCache(
        city = "cape_town",
        fetchedDateUtc = "2026-11-03",
        places = listOf(
            ExplorePlace(
                id = "place-1",
                name = "Table Mountain",
                type = "attraction",
                category = "nature",
                lat = -33.9628,
                lon = 18.4098,
                displayAddress = "Table Mountain, Cape Town",
                distanceMetres = 1200,
            ),
        ),
    )

    @Test
    fun toJson_thenFromJson_roundTripsEqual() {
        val original = sampleCache()
        val restored = PlacesCacheSerializer.fromJson(PlacesCacheSerializer.toJson(original))
        assertEquals(original, restored)
    }

    @Test
    fun toJson_thenFromJson_emptyPlacesRoundTrips() {
        val original = PlacesCache(city = "durban", fetchedDateUtc = "2026-11-03", places = emptyList())
        val restored = PlacesCacheSerializer.fromJson(PlacesCacheSerializer.toJson(original))
        assertEquals(original, restored)
    }

    @Test
    fun fromJson_missingField_returnsNull() {
        val json = PlacesCacheSerializer.toJson(sampleCache())
        json.remove("city")
        assertNull(PlacesCacheSerializer.fromJson(json))
    }

    @Test
    fun fromJson_placeMissingField_returnsNull() {
        val json = PlacesCacheSerializer.toJson(sampleCache())
        val place = json.getJSONArray("places").getJSONObject(0)
        place.remove("lat")
        assertNull(PlacesCacheSerializer.fromJson(json))
    }

    @Test
    fun fromJson_malformedPlacesField_returnsNull() {
        val json = JSONObject()
            .put("city", "cape_town")
            .put("fetchedDateUtc", "2026-11-03")
            .put("places", "not an array")
        assertNull(PlacesCacheSerializer.fromJson(json))
    }
}
