package com.waypoint.app.features.home.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.location.LocationProvider
import com.waypoint.app.core.secrets.RemoteSecrets
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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PREFS_NAME  = "home_prefs"
private const val KEY_CITY     = "selected_city"
private const val KEY_CURRENCY = "selected_currency"
private const val DEFAULT_CITY = "Cape Town"
private const val DEFAULT_CURR = "USD"

/**
 * ViewModel for the Home screen.
 *
 * Selections (city + FROM currency) are persisted in SharedPreferences so
 * they survive process death and are restored on the next launch.
 * Data (weather / rate) is cache-first via the GitHub cache repo:
 *   - Weather: re-fetched only if the cached entry is older than 2 hours.
 *   - Currency: re-fetched only if the cache is from a previous calendar day.
 * Nearby places are fetched once on the first GPS fix, showing the 3 closest POIs.
 */
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        HomeUiState(
            selectedCity         = prefs.getString(KEY_CITY,     DEFAULT_CITY) ?: DEFAULT_CITY,
            selectedFromCurrency = prefs.getString(KEY_CURRENCY, DEFAULT_CURR) ?: DEFAULT_CURR,
        ),
    )
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    /** Guards against re-fetching nearby places on every location update. */
    private var nearbyFetched = false

    init {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            val s = _uiState.value
            loadWeather(s.selectedCity)
            loadCurrency(s.selectedFromCurrency)
            loadFeaturedTrip()
        }
        startLocationUpdates()
    }

    // ── Public surface ─────────────────────────────────────────────────────────

    fun selectCity(city: String) {
        prefs.edit().putString(KEY_CITY, city).apply()
        _uiState.update { it.copy(selectedCity = city) }
        viewModelScope.launch { loadWeather(city) }
    }

    fun selectFromCurrency(code: String) {
        prefs.edit().putString(KEY_CURRENCY, code).apply()
        _uiState.update { it.copy(selectedFromCurrency = code) }
        viewModelScope.launch { loadCurrency(code) }
    }

    fun refreshWeather()  = viewModelScope.launch { loadWeather(_uiState.value.selectedCity) }
    fun refreshCurrency() = viewModelScope.launch { loadCurrency(_uiState.value.selectedFromCurrency) }

    /**
     * Pull-to-refresh entry point (HomeScreen's PullToRefreshBox). Re-fetches
     * weather, currency, and (if a GPS fix already exists) nearby places
     * concurrently, and suspends until all three finish so the caller can
     * drive a refresh spinner off it. A successful fetch here is also the de
     * facto "are we back online" check - no separate connectivity probe is
     * needed since rememberIsOnline() already reflects real connectivity live.
     */
    suspend fun refresh() = coroutineScope {
        val s = _uiState.value
        launch { loadWeather(s.selectedCity) }
        launch { loadCurrency(s.selectedFromCurrency) }
        s.location?.let { loc -> launch { loadNearbyPlaces(loc.lat, loc.lng) } }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

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

    private suspend fun loadCurrency(fromCode: String) {
        _uiState.update { it.copy(currency = CurrencyState.Loading) }
        val result = CurrencyRepository.getRate(fromCode)
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
                .catch { /* permission not granted or provider unavailable */ }
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
        val result = LocationIQRepository.getPlacesByCoords(lat, lon)
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
            // Prefer an ongoing trip; fall back to the soonest upcoming one
            val featured  = rows.firstOrNull { it.status == TripStatus.ONGOING }
                ?: rows.filter { it.status == TripStatus.UPCOMING }
                       .minByOrNull { it.dates }
            _uiState.update { it.copy(upcomingTrip = featured) }
        }
    }
}
