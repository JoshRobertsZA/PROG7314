package com.waypoint.app

import com.waypoint.app.core.cache.CurrencyCache
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.cache.WeatherCache
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate
import java.time.ZoneOffset

class CacheModelsTest {

    @Test
    fun weatherCache_freshData_returnsNotStale() {
        val now = System.currentTimeMillis()
        val cache = WeatherCache(
            city = "cape_town",
            displayName = "Cape Town",
            tempC = 22.5,
            description = "Sunny",
            fetchedAtMs = now
        )
        assertFalse("Recent weather cache should not be stale", cache.isStale())
    }

    @Test
    fun weatherCache_olderThanTwoHours_returnsStale() {
        val threeHoursAgo = System.currentTimeMillis() - (3 * 60 * 60 * 1000)
        val cache = WeatherCache(
            city = "cape_town",
            displayName = "Cape Town",
            tempC = 20.0,
            description = "Cloudy",
            fetchedAtMs = threeHoursAgo
        )
        assertTrue("Weather cache older than 2 hours should be stale", cache.isStale())
    }

    @Test
    fun currencyCache_sameDay_returnsNotStale() {
        val now = System.currentTimeMillis()
        val cache = CurrencyCache(
            fromCode = "USD",
            rate = 18.5,
            fetchedAtMs = now
        )
        assertFalse("Currency cache from today should not be stale", cache.isStale())
    }

    @Test
    fun placesCache_todayDateUtc_returnsNotStale() {
        val todayUtc = LocalDate.now(ZoneOffset.UTC).toString()
        val cache = PlacesCache(
            city = "cape_town",
            fetchedDateUtc = todayUtc,
            places = emptyList()
        )
        assertFalse("Places cache with today's date should not be stale", cache.isStale())
    }

    @Test
    fun placesCache_oldDateUtc_returnsStale() {
        val yesterdayUtc = LocalDate.now(ZoneOffset.UTC).minusDays(1).toString()
        val cache = PlacesCache(
            city = "cape_town",
            fetchedDateUtc = yesterdayUtc,
            places = emptyList()
        )
        assertTrue("Places cache with old date should be stale", cache.isStale())
    }
}
