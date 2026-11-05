package com.waypoint.app.features.home.ui

import com.waypoint.app.core.cache.CurrencyCache
import com.waypoint.app.core.cache.DeviceLocation
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.WeatherCache
import com.waypoint.app.features.alltrips.ui.TripRow

/** State of the Home tab. */
data class HomeUiState(
    val weather: WeatherState = WeatherState.Idle,
    val currency: CurrencyState = CurrencyState.Idle,
    val location: DeviceLocation? = null,
    val selectedCity: String = "Cape Town",
    val selectedFromCurrency: String = "USD",
    val selectedToCurrency: String = "ZAR",
    val nearbyPlaces: NearbyState = NearbyState.Idle,
    val upcomingTrip: TripRow? = null,
)

/** Loading state of the weather card. */
sealed class WeatherState {
    object Idle : WeatherState()
    object Loading : WeatherState()
    data class Success(val data: WeatherCache) : WeatherState()
    object Error : WeatherState()
}

/** Loading state of the currency card. */
sealed class CurrencyState {
    object Idle : CurrencyState()
    object Loading : CurrencyState()
    data class Success(val data: CurrencyCache) : CurrencyState()
    object Error : CurrencyState()
}

/** Loading state of the nearby places list. */
sealed class NearbyState {
    object Idle : NearbyState()
    object Loading : NearbyState()
    data class Success(val places: List<ExplorePlace>) : NearbyState()
    object Error : NearbyState()
}
