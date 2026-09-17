package com.example.prog7314.features.home.ui

import com.example.prog7314.core.cache.CurrencyCache
import com.example.prog7314.core.cache.DeviceLocation
import com.example.prog7314.core.cache.WeatherCache

/**
 * Top-level state for the Home screen.
 *
 * selectedCity         — last city the user picked (default: Cape Town)
 * selectedFromCurrency — currency to convert FROM; target is always ZAR
 * location             — live device position, null until first GPS fix
 */
data class HomeUiState(
    val weather: WeatherState = WeatherState.Idle,
    val currency: CurrencyState = CurrencyState.Idle,
    val location: DeviceLocation? = null,
    val selectedCity: String = "Cape Town",
    val selectedFromCurrency: String = "USD",
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
