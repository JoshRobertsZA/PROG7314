package com.example.prog7314.core.cache

import java.util.Calendar

/**
 * Cached weather reading for a single city. Treated as stale after 2 hours.
 */
data class WeatherCache(
    val city: String,          // slug used as the cache key, e.g. "cape_town"
    val displayName: String,   // city name from the API, e.g. "Cape Town"
    val tempC: Double,
    val description: String,   // e.g. "Partly cloudy"
    val fetchedAtMs: Long,
) {
    fun isStale(): Boolean =
        System.currentTimeMillis() - fetchedAtMs > TWO_HOURS_MS

    companion object {
        private const val TWO_HOURS_MS = 2L * 60 * 60 * 1_000
    }
}

/**
 * Cached exchange rate. fromCode is user-selected (e.g. "USD", "EUR").
 * The target currency is always ZAR. Treated as stale once the calendar
 * day rolls over.
 */
data class CurrencyCache(
    val fromCode: String,
    val rate: Double,          // 1 fromCode = rate ZAR
    val fetchedAtMs: Long,
) {
    fun isStale(): Boolean {
        val now = Calendar.getInstance()
        val fetched = Calendar.getInstance().apply { timeInMillis = fetchedAtMs }
        return now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||
               now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_OF_YEAR)
    }
}

/**
 * The device's last-known GPS position. Not persisted to GitHub; held in
 * memory only and updated live as the device moves.
 */
data class DeviceLocation(
    val lat: Double,
    val lng: Double,
)

// ---------------------------------------------------------------------------
// Explore / Places
// ---------------------------------------------------------------------------

/**
 * A single point of interest returned by LocationIQ Nearby.
 */
data class ExplorePlace(
    val id: String,              // OSM place_id from LocationIQ
    val name: String,
    val type: String,            // LocationIQ "type" field  (e.g. "restaurant")
    val category: String,        // LocationIQ "class" field (e.g. "amenity")
    val lat: Double,
    val lon: Double,
    val displayAddress: String,
    val distanceMetres: Int,
)

/**
 * Cached nearby-places result for a city. Treated as stale once the UTC
 * calendar day rolls over (same policy as CurrencyCache).
 */
data class PlacesCache(
    val city: String,
    val fetchedDateUtc: String,  // "yyyy-MM-dd" UTC date at fetch time
    val places: List<ExplorePlace>,
) {
    fun isStale(): Boolean {
        val today = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString()
        return fetchedDateUtc != today
    }
}
