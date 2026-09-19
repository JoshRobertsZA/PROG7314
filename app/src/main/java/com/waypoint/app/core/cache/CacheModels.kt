// declares that this file belongs to the package `com.waypoint.app.core.cache`
package com.waypoint.app.core.cache

// imports `java.util.Calendar` for use in this file
import java.util.Calendar

// expression: `data class WeatherCache(`
data class WeatherCache(
    // continues the statement started above: `val city: String,`
    val city: String,
    // continues the statement started above: `val displayName: String,`
    val displayName: String,
    // continues the statement started above: `val tempC: Double,`
    val tempC: Double,
    // continues the statement started above: `val description: String,`
    val description: String,
    // continues the statement started above: `val fetchedAtMs: Long,`
    val fetchedAtMs: Long,
// ends the argument list started above and opens the block that follows
) {
    // declares function `isStale` taking no parameters, returning `Boolean`; its body is the expression ``
    fun isStale(): Boolean =
        // continues the statement started above: `System.currentTimeMillis() - fetchedAtMs > TWO_HOURS_MS`
        System.currentTimeMillis() - fetchedAtMs > TWO_HOURS_MS

    // declares the companion object holding members shared by all instances of the enclosing class
    companion object {
        // declares private const read-only property `TWO_HOURS_MS`, initialised to the number 2L * 60 * 60 * 1_000
        private const val TWO_HOURS_MS = 2L * 60 * 60 * 1_000
    // closes the companion object
    }
// closes the block
}

// expression: `data class CurrencyCache(`
data class CurrencyCache(
    // continues the statement started above: `val fromCode: String,`
    val fromCode: String,
    // continues the statement started above: `val rate: Double,`
    val rate: Double,
    // continues the statement started above: `val fetchedAtMs: Long,`
    val fetchedAtMs: Long,
// ends the argument list started above and opens the block that follows
) {
    // declares function `isStale` taking no parameters, returning `Boolean` and opens its body
    fun isStale(): Boolean {
        // declares read-only property `now`, initialised with the result of calling `Calendar.getInstance(…)`
        val now = Calendar.getInstance()
        // declares read-only property `fetched`, initialised with the result of calling `Calendar.getInstance(…)`
        val fetched = Calendar.getInstance().apply { timeInMillis = fetchedAtMs }
        // returns `now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||` from the current function
        return now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||
               // continues the statement started above: `now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_O…`
               now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_OF_YEAR)
    // closes the function `isStale`
    }
// closes the block
}

// expression: `data class DeviceLocation(`
data class DeviceLocation(
    // continues the statement started above: `val lat: Double,`
    val lat: Double,
    // continues the statement started above: `val lng: Double,`
    val lng: Double,
// closes the multi-line argument list started above
)


// expression: `data class ExplorePlace(`
data class ExplorePlace(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val type: String,`
    val type: String,
    // continues the statement started above: `val category: String,`
    val category: String,
    // continues the statement started above: `val lat: Double,`
    val lat: Double,
    // continues the statement started above: `val lon: Double,`
    val lon: Double,
    // continues the statement started above: `val displayAddress: String,`
    val displayAddress: String,
    // continues the statement started above: `val distanceMetres: Int,`
    val distanceMetres: Int,
// closes the multi-line argument list started above
)

// expression: `data class PlacesCache(`
data class PlacesCache(
    // continues the statement started above: `val city: String,`
    val city: String,
    // continues the statement started above: `val fetchedDateUtc: String,`
    val fetchedDateUtc: String,
    // continues the statement started above: `val places: List<ExplorePlace>,`
    val places: List<ExplorePlace>,
// ends the argument list started above and opens the block that follows
) {
    // declares function `isStale` taking no parameters, returning `Boolean` and opens its body
    fun isStale(): Boolean {
        // declares read-only property `today`, initialised with the result of calling `java.time.LocalDate.now(…)`
        val today = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString()
        // returns `fetchedDateUtc != today` from the current function
        return fetchedDateUtc != today
    // closes the function `isStale`
    }
// closes the block
}
