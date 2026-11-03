package com.waypoint.app

import com.waypoint.app.core.cache.GitHubCacheRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GitHubCacheRepositoryPathTest {

    @Test
    fun weatherPath_prefixesCitySlug() {
        assertEquals("weather_cape_town", GitHubCacheRepository.weatherPath("cape_town"))
    }

    @Test
    fun currencyRatesPath_prefixesBaseCode() {
        assertEquals("currency_USD", GitHubCacheRepository.currencyRatesPath("USD"))
    }

    @Test
    fun weatherPath_differentCities_differ() {
        assertNotEquals(
            GitHubCacheRepository.weatherPath("cape_town"),
            GitHubCacheRepository.weatherPath("durban"),
        )
    }
}
