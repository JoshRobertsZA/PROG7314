package com.waypoint.app.features.home.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.location.LocationProvider
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.core.settings.AppSettings
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.waypoint.app.features.home.data.CurrencyRepository
import com.waypoint.app.features.home.data.WeatherRepository
import com.waypoint.app.features.alltrips.ui.TripStatus
import com.waypoint.app.features.alltrips.ui.toRow
import com.waypoint.app.features.newtrip.data.TripRepository
import com.waypoint.app.core.db.SessionManager
import java.time.LocalDate
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PREFS_NAME  = "home_prefs"
private const val KEY_CITY     = "selected_city"
private const val KEY_CURRENCY = "selected_currency"
private const val DEFAULT_CITY = "Cape Town"
private const val DEFAULT_CURR = "USD"
private const val KEY_CURRENCY_TO = "selected_currency_to"

/**
 * Loads Home's weather, exchange rate, featured trip and nearby places. Remembers the chosen city and
 * currencies, and follows the home currency set in Settings.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        HomeUiState(
            selectedCity         = prefs.getString(KEY_CITY,     DEFAULT_CITY) ?: DEFAULT_CITY,
            selectedFromCurrency = prefs.getString(KEY_CURRENCY, DEFAULT_CURR) ?: DEFAULT_CURR,
            // Until the user picks one on Home, convert into the home currency chosen in Settings.
            selectedToCurrency   = prefs.getString(KEY_CURRENCY_TO, null) ?: AppSettings.current.defaultCurrency,
        ),
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    private var nearbyFetched = false

    init {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            val s = _uiState.value
            loadWeather(s.selectedCity)
            loadCurrency(s.selectedFromCurrency, s.selectedToCurrency)
            loadFeaturedTrip()
        }
        startLocationUpdates()
        // Changing the home currency in Settings retargets the converter straight away.
        viewModelScope.launch {
            AppSettings.state.map { it.defaultCurrency }.distinctUntilChanged().drop(1).collect { code ->
                Log.d("HomeViewModel", "Home currency changed in Settings to $code")
                selectToCurrency(code)
            }
        }
    }


    fun selectCity(city: String) {
        prefs.edit().putString(KEY_CITY, city).apply()
        _uiState.update { it.copy(selectedCity = city) }
        viewModelScope.launch { loadWeather(city) }
    }

    fun selectFromCurrency(code: String) {
        prefs.edit().putString(KEY_CURRENCY, code).apply()
        _uiState.update { it.copy(selectedFromCurrency = code) }
        viewModelScope.launch { loadCurrency(code, _uiState.value.selectedToCurrency) }
    }

    fun selectToCurrency(code: String) {
        prefs.edit().putString(KEY_CURRENCY_TO, code).apply()
        _uiState.update { it.copy(selectedToCurrency = code) }
        viewModelScope.launch { loadCurrency(_uiState.value.selectedFromCurrency, code) }
    }

    fun swapCurrencies() {
        val s = _uiState.value
        prefs.edit()
            .putString(KEY_CURRENCY, s.selectedToCurrency)
            .putString(KEY_CURRENCY_TO, s.selectedFromCurrency)
            .apply()
        _uiState.update {
            it.copy(
                selectedFromCurrency = s.selectedToCurrency,
                selectedToCurrency = s.selectedFromCurrency,
            )
        }
        viewModelScope.launch { loadCurrency(s.selectedToCurrency, s.selectedFromCurrency) }
    }

    fun refreshWeather()  = viewModelScope.launch { loadWeather(_uiState.value.selectedCity) }
    fun refreshCurrency() = viewModelScope.launch {
        loadCurrency(_uiState.value.selectedFromCurrency, _uiState.value.selectedToCurrency)
    }

    suspend fun refresh() = coroutineScope {
        val s = _uiState.value
        launch { loadWeather(s.selectedCity) }
        launch { loadCurrency(s.selectedFromCurrency, s.selectedToCurrency) }
        s.location?.let { loc -> launch { loadNearbyPlaces(loc.lat, loc.lng) } }
    }


    private suspend fun loadWeather(city: String) {
        _uiState.update { it.copy(weather = WeatherState.Loading) }
        val result = WeatherRepository.getWeather(city)
        _uiState.update {
            it.copy(
                weather = if (result != null) WeatherState.Success(result)
                          else WeatherState.Error,
            )
        }
    }

    private suspend fun loadCurrency(fromCode: String, toCode: String) {
        _uiState.update { it.copy(currency = CurrencyState.Loading) }
        val result = CurrencyRepository.getRate(fromCode, toCode)
        _uiState.update {
            it.copy(
                currency = if (result != null) CurrencyState.Success(result)
                           else CurrencyState.Error,
            )
        }
    }

    private fun startLocationUpdates() {
        viewModelScope.launch {
            LocationProvider.locationFlow(getApplication())
                .catch {  }
                .collect { loc ->
                    _uiState.update { it.copy(location = loc) }
                    if (!nearbyFetched) {
                        nearbyFetched = true
                        loadNearbyPlaces(loc.lat, loc.lng)
                    }
                }
        }
    }

    private suspend fun loadNearbyPlaces(lat: Double, lon: Double) {
        _uiState.update { it.copy(nearbyPlaces = NearbyState.Loading) }
        val result = LocationIQRepository.getPlacesByCoords(getApplication(), lat, lon)
        _uiState.update {
            it.copy(
                nearbyPlaces = if (result != null && result.places.isNotEmpty())
                    NearbyState.Success(result.places.take(3))
                else NearbyState.Error,
            )
        }
    }

    private fun loadFeaturedTrip() {
        viewModelScope.launch {
            val today     = LocalDate.now()
            val accountId = SessionManager.accountId
            val tripRepo  = TripRepository(getApplication())
            val entities  = tripRepo.getTripsForAccount(accountId)
            val rows      = entities.mapIndexed { i, e -> e.toRow(i, today) }
            val featured  = rows.firstOrNull { it.status == TripStatus.ONGOING }
                ?: rows.filter { it.status == TripStatus.UPCOMING }
                       .minByOrNull { it.dates }
            _uiState.update { it.copy(upcomingTrip = featured) }
        }
    }
}
