// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.cache.CurrencyCache` for use in this file
import com.waypoint.app.core.cache.CurrencyCache
// imports `com.waypoint.app.core.cache.PlacesCache` for use in this file
import com.waypoint.app.core.cache.PlacesCache
// imports `com.waypoint.app.core.cache.WeatherCache` for use in this file
import com.waypoint.app.core.cache.WeatherCache
// imports `org.junit.Assert.assertFalse` for use in this file
import org.junit.Assert.assertFalse
// imports `org.junit.Assert.assertTrue` for use in this file
import org.junit.Assert.assertTrue
// imports `org.junit.Test` for use in this file
import org.junit.Test
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.ZoneOffset` for use in this file
import java.time.ZoneOffset

// declares class `CacheModelsTest` and opens its body
class CacheModelsTest {

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `weatherCache_freshData_returnsNotStale` taking no parameters and opens its body
    fun weatherCache_freshData_returnsNotStale() {
        // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
        val now = System.currentTimeMillis()
        // declares read-only property `cache`, initialised with the result of calling `WeatherCache(…)`
        val cache = WeatherCache(
            // continues the statement started above: `city = "cape_town",`
            city = "cape_town",
            // continues the statement started above: `displayName = "Cape Town",`
            displayName = "Cape Town",
            // continues the statement started above: `tempC = 22.5,`
            tempC = 22.5,
            // continues the statement started above: `description = "Sunny",`
            description = "Sunny",
            // continues the statement started above: `fetchedAtMs = now`
            fetchedAtMs = now
        // closes the multi-line argument list started above
        )
        // calls `assertFalse` with arguments `("Recent weather cache should not be stale", …)`
        assertFalse("Recent weather cache should not be stale", cache.isStale())
    // closes the function `weatherCache_freshData_returnsNotStale`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `weatherCache_olderThanTwoHours_returnsStale` taking no parameters and opens its body
    fun weatherCache_olderThanTwoHours_returnsStale() {
        // declares read-only property `threeHoursAgo`, initialised with the result of calling `System.currentTimeMillis(…)`
        val threeHoursAgo = System.currentTimeMillis() - (3 * 60 * 60 * 1000)
        // declares read-only property `cache`, initialised with the result of calling `WeatherCache(…)`
        val cache = WeatherCache(
            // continues the statement started above: `city = "cape_town",`
            city = "cape_town",
            // continues the statement started above: `displayName = "Cape Town",`
            displayName = "Cape Town",
            // continues the statement started above: `tempC = 20.0,`
            tempC = 20.0,
            // continues the statement started above: `description = "Cloudy",`
            description = "Cloudy",
            // continues the statement started above: `fetchedAtMs = threeHoursAgo`
            fetchedAtMs = threeHoursAgo
        // closes the multi-line argument list started above
        )
        // calls `assertTrue` with arguments `("Weather cache older than 2 hours should be …)`
        assertTrue("Weather cache older than 2 hours should be stale", cache.isStale())
    // closes the function `weatherCache_olderThanTwoHours_returnsStale`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `currencyCache_sameDay_returnsNotStale` taking no parameters and opens its body
    fun currencyCache_sameDay_returnsNotStale() {
        // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
        val now = System.currentTimeMillis()
        // declares read-only property `cache`, initialised with the result of calling `CurrencyCache(…)`
        val cache = CurrencyCache(
            // continues the statement started above: `fromCode = "USD",`
            fromCode = "USD",
            // continues the statement started above: `rate = 18.5,`
            rate = 18.5,
            // continues the statement started above: `fetchedAtMs = now`
            fetchedAtMs = now
        // closes the multi-line argument list started above
        )
        // calls `assertFalse` with arguments `("Currency cache from today should not be sta…)`
        assertFalse("Currency cache from today should not be stale", cache.isStale())
    // closes the function `currencyCache_sameDay_returnsNotStale`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `placesCache_todayDateUtc_returnsNotStale` taking no parameters and opens its body
    fun placesCache_todayDateUtc_returnsNotStale() {
        // declares read-only property `todayUtc`, initialised with the result of calling `LocalDate.now(…)`
        val todayUtc = LocalDate.now(ZoneOffset.UTC).toString()
        // declares read-only property `cache`, initialised with the result of calling `PlacesCache(…)`
        val cache = PlacesCache(
            // continues the statement started above: `city = "cape_town",`
            city = "cape_town",
            // continues the statement started above: `fetchedDateUtc = todayUtc,`
            fetchedDateUtc = todayUtc,
            // continues the statement started above: `places = emptyList()`
            places = emptyList()
        // closes the multi-line argument list started above
        )
        // calls `assertFalse` with arguments `("Places cache with today's date should not b…)`
        assertFalse("Places cache with today's date should not be stale", cache.isStale())
    // closes the function `placesCache_todayDateUtc_returnsNotStale`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `placesCache_oldDateUtc_returnsStale` taking no parameters and opens its body
    fun placesCache_oldDateUtc_returnsStale() {
        // declares read-only property `yesterdayUtc`, initialised with the result of calling `LocalDate.now(…)`
        val yesterdayUtc = LocalDate.now(ZoneOffset.UTC).minusDays(1).toString()
        // declares read-only property `cache`, initialised with the result of calling `PlacesCache(…)`
        val cache = PlacesCache(
            // continues the statement started above: `city = "cape_town",`
            city = "cape_town",
            // continues the statement started above: `fetchedDateUtc = yesterdayUtc,`
            fetchedDateUtc = yesterdayUtc,
            // continues the statement started above: `places = emptyList()`
            places = emptyList()
        // closes the multi-line argument list started above
        )
        // calls `assertTrue` with arguments `("Places cache with old date should be stale"…)`
        assertTrue("Places cache with old date should be stale", cache.isStale())
    // closes the function `placesCache_oldDateUtc_returnsStale`
    }
// closes the class `CacheModelsTest`
}
