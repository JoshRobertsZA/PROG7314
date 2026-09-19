// declares that this file belongs to the package `com.waypoint.app.features.home.ui`
package com.waypoint.app.features.home.ui

// imports `com.waypoint.app.core.cache.CurrencyCache` for use in this file
import com.waypoint.app.core.cache.CurrencyCache
// imports `com.waypoint.app.core.cache.DeviceLocation` for use in this file
import com.waypoint.app.core.cache.DeviceLocation
// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.cache.WeatherCache` for use in this file
import com.waypoint.app.core.cache.WeatherCache
// imports `com.waypoint.app.features.alltrips.ui.TripRow` for use in this file
import com.waypoint.app.features.alltrips.ui.TripRow

// expression: `data class HomeUiState(`
data class HomeUiState(
    // continues the statement started above: `val weather: WeatherState = WeatherState.Idle,`
    val weather: WeatherState = WeatherState.Idle,
    // continues the statement started above: `val currency: CurrencyState = CurrencyState.Idle,`
    val currency: CurrencyState = CurrencyState.Idle,
    // continues the statement started above: `val location: DeviceLocation? = null,`
    val location: DeviceLocation? = null,
    // continues the statement started above: `val selectedCity: String = "Cape Town",`
    val selectedCity: String = "Cape Town",
    // continues the statement started above: `val selectedFromCurrency: String = "USD",`
    val selectedFromCurrency: String = "USD",
    // continues the statement started above: `val nearbyPlaces: NearbyState = NearbyState.Idle,`
    val nearbyPlaces: NearbyState = NearbyState.Idle,
    // continues the statement started above: `val upcomingTrip: TripRow? = null,`
    val upcomingTrip: TripRow? = null,
// closes the multi-line argument list started above
)

// declares sealed class `WeatherState` and opens its body
sealed class WeatherState {
    // statement: `object Idle : WeatherState()`
    object Idle : WeatherState()
    // statement: `object Loading : WeatherState()`
    object Loading : WeatherState()
    // declares data class `Success` with a primary constructor taking 1 parameter (`data`), inheriting from `WeatherState()`
    data class Success(val data: WeatherCache) : WeatherState()
    // statement: `object Error : WeatherState()`
    object Error : WeatherState()
// closes the class `WeatherState`
}

// declares sealed class `CurrencyState` and opens its body
sealed class CurrencyState {
    // statement: `object Idle : CurrencyState()`
    object Idle : CurrencyState()
    // statement: `object Loading : CurrencyState()`
    object Loading : CurrencyState()
    // declares data class `Success` with a primary constructor taking 1 parameter (`data`), inheriting from `CurrencyState()`
    data class Success(val data: CurrencyCache) : CurrencyState()
    // statement: `object Error : CurrencyState()`
    object Error : CurrencyState()
// closes the class `CurrencyState`
}

// declares sealed class `NearbyState` and opens its body
sealed class NearbyState {
    // statement: `object Idle : NearbyState()`
    object Idle : NearbyState()
    // statement: `object Loading : NearbyState()`
    object Loading : NearbyState()
    // declares data class `Success` with a primary constructor taking 1 parameter (`places`), inheriting from `NearbyState()`
    data class Success(val places: List<ExplorePlace>) : NearbyState()
    // statement: `object Error : NearbyState()`
    object Error : NearbyState()
// closes the class `NearbyState`
}
