package com.waypoint.app.core.cache

import java.util.Calendar

/** Current weather for a city, with the time it was fetched so stale values can be refreshed. */
data class WeatherCache(
    val city: String,
    val displayName: String,
    val tempC: Double,
    val description: String,
    val fetchedAtMs: Long,
) {
    fun isStale(): Boolean =
        System.currentTimeMillis() - fetchedAtMs > TWO_HOURS_MS

    companion object {
        private const val TWO_HOURS_MS = 2L * 60 * 60 * 1_000
    }
}

/** A single exchange rate between two currencies. */
data class CurrencyCache(
    val fromCode: String,
    val toCode: String = "ZAR",
    val rate: Double,
    val fetchedAtMs: Long,
) {
    fun isStale(): Boolean {
        val now = Calendar.getInstance()
        val fetched = Calendar.getInstance().apply { timeInMillis = fetchedAtMs }
        return now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||
               now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_OF_YEAR)
    }
}

/** All rates for one base currency from ExchangeRate-API. */
data class CurrencyRatesCache(
    val baseCode: String,
    val rates: Map<String, Double>,
    val fetchedAtMs: Long,
) {
    fun isStale(): Boolean {
        val now = Calendar.getInstance()
        val fetched = Calendar.getInstance().apply { timeInMillis = fetchedAtMs }
        return now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||
               now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_OF_YEAR)
    }

    fun rateFor(code: String): Double? = if (code == baseCode) 1.0 else rates[code]
}

/** A latitude/longitude pair from the phone's location services. */
data class DeviceLocation(
    val lat: Double,
    val lng: Double,
)


/** A nearby place returned by LocationIQ. */
data class ExplorePlace(
    val id: String,
    val name: String,
    val type: String,
    val category: String,
    val lat: Double,
    val lon: Double,
    val displayAddress: String,
    val distanceMetres: Int,
)

/** Nearby places for a city, refreshed once per UTC day to save LocationIQ quota. */
data class PlacesCache(
    val city: String,
    val fetchedDateUtc: String,
    val places: List<ExplorePlace>,
) {
    fun isStale(): Boolean {
        val today = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString()
        return fetchedDateUtc != today
    }
}
