// declares that this file belongs to the package `com.waypoint.app.features.home.ui`
package com.waypoint.app.features.home.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.location.LocationProvider` for use in this file
import com.waypoint.app.core.location.LocationProvider
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.features.explore.data.LocationIQRepository` for use in this file
import com.waypoint.app.features.explore.data.LocationIQRepository
// imports `com.waypoint.app.features.home.data.CurrencyRepository` for use in this file
import com.waypoint.app.features.home.data.CurrencyRepository
// imports `com.waypoint.app.features.home.data.WeatherRepository` for use in this file
import com.waypoint.app.features.home.data.WeatherRepository
// imports `com.waypoint.app.features.alltrips.ui.TripStatus` for use in this file
import com.waypoint.app.features.alltrips.ui.TripStatus
// imports `com.waypoint.app.features.alltrips.ui.toRow` for use in this file
import com.waypoint.app.features.alltrips.ui.toRow
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `kotlinx.coroutines.coroutineScope` for use in this file
import kotlinx.coroutines.coroutineScope
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.asStateFlow` for use in this file
import kotlinx.coroutines.flow.asStateFlow
// imports `kotlinx.coroutines.flow.catch` for use in this file
import kotlinx.coroutines.flow.catch
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// declares private const read-only property `PREFS_NAME`, initialised to the string literal "home_prefs"
private const val PREFS_NAME  = "home_prefs"
// declares private const read-only property `KEY_CITY`, initialised to the string literal "selected_city"
private const val KEY_CITY     = "selected_city"
// declares private const read-only property `KEY_CURRENCY`, initialised to the string literal "selected_currency"
private const val KEY_CURRENCY = "selected_currency"
// declares private const read-only property `DEFAULT_CITY`, initialised to the string literal "Cape Town"
private const val DEFAULT_CITY = "Cape Town"
// declares private const read-only property `DEFAULT_CURR`, initialised to the string literal "USD"
private const val DEFAULT_CURR = "USD"

// declares class `HomeViewModel` with a primary constructor taking 1 parameter (`application`), inheriting from `AndroidViewModel(application)` and opens its body
class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // declares private read-only property `prefs`, initialised with the result of calling `application.getSharedPreferences(…)`
    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(
        // continues the statement started above: `HomeUiState(`
        HomeUiState(
            // continues the statement started above: `selectedCity = prefs.getString(KEY_CITY, DEFAULT_CITY) ?: D…`
            selectedCity         = prefs.getString(KEY_CITY,     DEFAULT_CITY) ?: DEFAULT_CITY,
            // continues the statement started above: `selectedFromCurrency = prefs.getString(KEY_CURRENCY, DEFAUL…`
            selectedFromCurrency = prefs.getString(KEY_CURRENCY, DEFAULT_CURR) ?: DEFAULT_CURR,
        // closes the multi-line argument list started above
        ),
    // closes the multi-line argument list started above
    )
    // declares read-only property `uiState` of type `StateFlow<HomeUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // declares private mutable property `nearbyFetched`, initialised to false
    private var nearbyFetched = false

    // initialiser block: runs when an instance of the class is constructed
    init {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `ensureLoaded` on `RemoteSecrets` with arguments `()`
            RemoteSecrets.ensureLoaded()
            // declares read-only property `s`, initialised to `_uiState.value`
            val s = _uiState.value
            // calls `loadWeather` with arguments `(s.selectedCity)`
            loadWeather(s.selectedCity)
            // calls `loadCurrency` with arguments `(s.selectedFromCurrency)`
            loadCurrency(s.selectedFromCurrency)
            // calls `loadFeaturedTrip` with arguments `()`
            loadFeaturedTrip()
        // closes the block
        }
        // calls `startLocationUpdates` with arguments `()`
        startLocationUpdates()
    // closes the init block
    }


    // declares function `selectCity` taking 1 parameter (`city`) and opens its body
    fun selectCity(city: String) {
        // calls `edit` on `prefs` with arguments `()`, then chains `.putString(KEY_CITY, city)`, `.apply()`
        prefs.edit().putString(KEY_CITY, city).apply()
        // expression: `_uiState.update { it.copy(selectedCity = city) }`
        _uiState.update { it.copy(selectedCity = city) }
        // expression: `viewModelScope.launch { loadWeather(city) }`
        viewModelScope.launch { loadWeather(city) }
    // closes the function `selectCity`
    }

    // declares function `selectFromCurrency` taking 1 parameter (`code`) and opens its body
    fun selectFromCurrency(code: String) {
        // calls `edit` on `prefs` with arguments `()`, then chains `.putString(KEY_CURRENCY, code)`, `.apply()`
        prefs.edit().putString(KEY_CURRENCY, code).apply()
        // expression: `_uiState.update { it.copy(selectedFromCurrency = code) }`
        _uiState.update { it.copy(selectedFromCurrency = code) }
        // expression: `viewModelScope.launch { loadCurrency(code) }`
        viewModelScope.launch { loadCurrency(code) }
    // closes the function `selectFromCurrency`
    }

    // declares function `refreshWeather` taking no parameters; its body is the expression `viewModelScope.launch { loadWeather(_uiState…`
    fun refreshWeather()  = viewModelScope.launch { loadWeather(_uiState.value.selectedCity) }
    // declares function `refreshCurrency` taking no parameters; its body is the expression `viewModelScope.launch { loadCurrency(_uiStat…`
    fun refreshCurrency() = viewModelScope.launch { loadCurrency(_uiState.value.selectedFromCurrency) }

    // declares suspend function `refresh` taking no parameters; its body is the expression `coroutineScope {`
    suspend fun refresh() = coroutineScope {
        // declares read-only property `s`, initialised to `_uiState.value`
        val s = _uiState.value
        // expression: `launch { loadWeather(s.selectedCity) }`
        launch { loadWeather(s.selectedCity) }
        // expression: `launch { loadCurrency(s.selectedFromCurrency) }`
        launch { loadCurrency(s.selectedFromCurrency) }
        // lambda `s.location?.let { loc -> launch { loadNearbyPlaces(loc…`
        s.location?.let { loc -> launch { loadNearbyPlaces(loc.lat, loc.lng) } }
    // closes the block
    }


    // declares private suspend function `loadWeather` taking 1 parameter (`city`) and opens its body
    private suspend fun loadWeather(city: String) {
        // expression: `_uiState.update { it.copy(weather = WeatherState.Loading) }`
        _uiState.update { it.copy(weather = WeatherState.Loading) }
        // declares read-only property `result`, initialised with the result of calling `WeatherRepository.getWeather(…)`
        val result = WeatherRepository.getWeather(city)
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `weather = if (result != null) WeatherState.Success(result)`
                weather = if (result != null) WeatherState.Success(result)
                          // continues the statement started above: `else WeatherState.Error,`
                          else WeatherState.Error,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `loadWeather`
    }

    // declares private suspend function `loadCurrency` taking 1 parameter (`fromCode`) and opens its body
    private suspend fun loadCurrency(fromCode: String) {
        // expression: `_uiState.update { it.copy(currency = CurrencyState.Loading) }`
        _uiState.update { it.copy(currency = CurrencyState.Loading) }
        // declares read-only property `result`, initialised with the result of calling `CurrencyRepository.getRate(…)`
        val result = CurrencyRepository.getRate(fromCode)
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `currency = if (result != null) CurrencyState.Success(result)`
                currency = if (result != null) CurrencyState.Success(result)
                           // continues the statement started above: `else CurrencyState.Error,`
                           else CurrencyState.Error,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `loadCurrency`
    }

    // declares private function `startLocationUpdates` taking no parameters and opens its body
    private fun startLocationUpdates() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `locationFlow` on `LocationProvider` with arguments `(getApplication())`
            LocationProvider.locationFlow(getApplication())
                // chained call `.catch` on the previous result with an inline lambda that evaluates ``
                .catch {  }
                // expression: `.collect { loc ->`
                .collect { loc ->
                    // continues the statement started above: `_uiState.update { it.copy(location = loc) }`
                    _uiState.update { it.copy(location = loc) }
                    // `if` statement: the block below runs when `!nearbyFetched` is true
                    if (!nearbyFetched) {
                        // assigns `nearbyFetched` the value `true`
                        nearbyFetched = true
                        // calls `loadNearbyPlaces` with arguments `(loc.lat, loc.lng)`
                        loadNearbyPlaces(loc.lat, loc.lng)
                    // closes the if block
                    }
                // closes the block
                }
        // closes the block
        }
    // closes the function `startLocationUpdates`
    }

    // declares private suspend function `loadNearbyPlaces` taking 2 parameters (`lat`, `lon`) and opens its body
    private suspend fun loadNearbyPlaces(lat: Double, lon: Double) {
        // expression: `_uiState.update { it.copy(nearbyPlaces = NearbyState.Loading) }`
        _uiState.update { it.copy(nearbyPlaces = NearbyState.Loading) }
        // declares read-only property `result`, initialised with the result of calling `LocationIQRepository.getPlacesByCoords(…)`
        val result = LocationIQRepository.getPlacesByCoords(lat, lon)
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `nearbyPlaces = if (result != null && result.places.isNotEmp…`
                nearbyPlaces = if (result != null && result.places.isNotEmpty())
                    // continues the statement started above: `NearbyState.Success(result.places.take(3))`
                    NearbyState.Success(result.places.take(3))
                // continues the statement started above: `else NearbyState.Error,`
                else NearbyState.Error,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `loadNearbyPlaces`
    }

    // declares private function `loadFeaturedTrip` taking no parameters and opens its body
    private fun loadFeaturedTrip() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `today`, initialised with the result of calling `LocalDate.now(…)`
            val today     = LocalDate.now()
            // declares read-only property `accountId`, initialised to `SessionManager.accountId`
            val accountId = SessionManager.accountId
            // declares read-only property `tripRepo`, initialised with the result of calling `TripRepository(…)`
            val tripRepo  = TripRepository(getApplication())
            // declares read-only property `entities`, initialised with the result of calling `tripRepo.getTripsForAccount(…)`
            val entities  = tripRepo.getTripsForAccount(accountId)
            // declares read-only property `rows`, initialised to a lambda / arrow function
            val rows      = entities.mapIndexed { i, e -> e.toRow(i, today) }
            // declares read-only property `featured`, initialised to `rows.firstOrNull { it.status == TripStatus.O…`
            val featured  = rows.firstOrNull { it.status == TripStatus.ONGOING }
                // expression: `?: rows.filter { it.status == TripStatus.UPCOMING }`
                ?: rows.filter { it.status == TripStatus.UPCOMING }
                       // chained call `.minByOrNull` on the previous result with an inline lambda that evaluates `it.dates`
                       .minByOrNull { it.dates }
            // expression: `_uiState.update { it.copy(upcomingTrip = featured) }`
            _uiState.update { it.copy(upcomingTrip = featured) }
        // closes the block
        }
    // closes the function `loadFeaturedTrip`
    }
// closes the class `HomeViewModel`
}
