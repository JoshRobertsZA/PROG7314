package com.waypoint.app

import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.features.explore.ui.ExploreFilter
import com.waypoint.app.features.explore.ui.applyExploreFilter
import org.junit.Assert.assertEquals
import org.junit.Test

class ExplorePlaceFilterTest {

    private val sampleCache = PlacesCache(
        city = "cape_town",
        fetchedDateUtc = "2026-03-01",
        places = listOf(
            ExplorePlace("1", "Local Bistro", "restaurant", "amenity", -33.9, 18.4, "123 Main St", 150),
            ExplorePlace("2", "Central Park", "park", "leisure", -33.9, 18.4, "Park Ave", 300),
            ExplorePlace("3", "Grand Hotel", "hotel", "tourism", -33.9, 18.4, "Beach Rd", 500),
            ExplorePlace("4", "Coffee Spot", "cafe", "amenity", -33.9, 18.4, "45 High St", 80),
        ),
    )

    @Test
    fun filterAll_returnsAllPlaces() {
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.ALL)
        assertEquals(4, filtered.size)
    }

    @Test
    fun filterRestaurants_returnsOnlyRestaurants() {
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.RESTAURANTS)
        assertEquals(1, filtered.size)
        assertEquals("Local Bistro", filtered.first().name)
    }

    @Test
    fun filterParks_returnsOnlyParks() {
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.PARKS)
        assertEquals(1, filtered.size)
        assertEquals("Central Park", filtered.first().name)
    }
}
