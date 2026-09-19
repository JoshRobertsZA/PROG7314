// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.cache.PlacesCache` for use in this file
import com.waypoint.app.core.cache.PlacesCache
// imports `com.waypoint.app.features.explore.ui.ExploreFilter` for use in this file
import com.waypoint.app.features.explore.ui.ExploreFilter
// imports `com.waypoint.app.features.explore.ui.applyExploreFilter` for use in this file
import com.waypoint.app.features.explore.ui.applyExploreFilter
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Test` for use in this file
import org.junit.Test

// declares class `ExplorePlaceFilterTest` and opens its body
class ExplorePlaceFilterTest {

    // declares private read-only property `sampleCache`, initialised with the result of calling `PlacesCache(…)`
    private val sampleCache = PlacesCache(
        // continues the statement started above: `city = "cape_town",`
        city = "cape_town",
        // continues the statement started above: `fetchedDateUtc = "2026-03-01",`
        fetchedDateUtc = "2026-03-01",
        // continues the statement started above: `places = listOf(`
        places = listOf(
            // continues the statement started above: `ExplorePlace("1", "Local Bistro", "restaurant", "amenity", …`
            ExplorePlace("1", "Local Bistro", "restaurant", "amenity", -33.9, 18.4, "123 Main St", 150),
            // continues the statement started above: `ExplorePlace("2", "Central Park", "park", "leisure", -33.9,…`
            ExplorePlace("2", "Central Park", "park", "leisure", -33.9, 18.4, "Park Ave", 300),
            // continues the statement started above: `ExplorePlace("3", "Grand Hotel", "hotel", "tourism", -33.9,…`
            ExplorePlace("3", "Grand Hotel", "hotel", "tourism", -33.9, 18.4, "Beach Rd", 500),
            // continues the statement started above: `ExplorePlace("4", "Coffee Spot", "cafe", "amenity", -33.9, …`
            ExplorePlace("4", "Coffee Spot", "cafe", "amenity", -33.9, 18.4, "45 High St", 80),
        // closes the multi-line argument list started above
        ),
    // closes the multi-line argument list started above
    )

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `filterAll_returnsAllPlaces` taking no parameters and opens its body
    fun filterAll_returnsAllPlaces() {
        // declares read-only property `filtered`, initialised with the result of calling `applyExploreFilter(…)`
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.ALL)
        // calls `assertEquals` with arguments `(4, filtered.size)`
        assertEquals(4, filtered.size)
    // closes the function `filterAll_returnsAllPlaces`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `filterRestaurants_returnsOnlyRestaurants` taking no parameters and opens its body
    fun filterRestaurants_returnsOnlyRestaurants() {
        // declares read-only property `filtered`, initialised with the result of calling `applyExploreFilter(…)`
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.RESTAURANTS)
        // calls `assertEquals` with arguments `(1, filtered.size)`
        assertEquals(1, filtered.size)
        // calls `assertEquals` with arguments `("Local Bistro", filtered.first().name)`
        assertEquals("Local Bistro", filtered.first().name)
    // closes the function `filterRestaurants_returnsOnlyRestaurants`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `filterParks_returnsOnlyParks` taking no parameters and opens its body
    fun filterParks_returnsOnlyParks() {
        // declares read-only property `filtered`, initialised with the result of calling `applyExploreFilter(…)`
        val filtered = applyExploreFilter(sampleCache, ExploreFilter.PARKS)
        // calls `assertEquals` with arguments `(1, filtered.size)`
        assertEquals(1, filtered.size)
        // calls `assertEquals` with arguments `("Central Park", filtered.first().name)`
        assertEquals("Central Park", filtered.first().name)
    // closes the function `filterParks_returnsOnlyParks`
    }
// closes the class `ExplorePlaceFilterTest`
}
