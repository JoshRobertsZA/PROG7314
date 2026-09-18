package com.waypoint.app.features.home.ui

import com.waypoint.app.core.cache.CurrencyCache
import com.waypoint.app.core.cache.DeviceLocation
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.WeatherCache
import com.waypoint.app.features.alltrips.ui.TripRow

/**
 * Top-level state for the Home screen.
 *
 * selectedCity         — last city the user picked (default: Cape Town)
 * selectedFromCurrency — currency to convert FROM; target is always ZAR
 * location             — live device position, null until first GPS fix
 * nearbyPlaces         — 3 closest POIs fetched from LocationIQ on first GPS fix
 */
data class HomeUiState(
    val weather: WeatherState = WeatherState.Idle,
    val currency: CurrencyState = CurrencyState.Idle,
    val location: DeviceLocation? = null,
    val selectedCity: String = "Cape Town",
    val selectedFromCurrency: String = "USD",
    val nearbyPlaces: NearbyState = NearbyState.Idle,
    val upcomingTrip: TripRow? = null,
)

sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherCache) : WeatherState()
    object Error : WeatherState()
}

sealed class CurrencyState {
    object Idle : CurrencyState()
    object Loading : CurrencyState()
    data class Success(val data: CurrencyCache) : CurrencyState()
    object Error : CurrencyState()
}

sealed class NearbyState {
    object Idle : NearbyState()
    object Loading : NearbyState()
    data class Success(val places: List<ExplorePlace>) : NearbyState()
    object Error : NearbyState()
}
