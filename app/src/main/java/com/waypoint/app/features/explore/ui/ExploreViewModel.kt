// declares that this file belongs to the package `com.waypoint.app.features.explore.ui`
package com.waypoint.app.features.explore.ui

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.app.Application` for use in this file
import android.app.Application
// imports `android.content.pm.PackageManager` for use in this file
import android.content.pm.PackageManager
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `androidx.core.content.ContextCompat` for use in this file
import androidx.core.content.ContextCompat
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.cache.PlacesCache` for use in this file
import com.waypoint.app.core.cache.PlacesCache
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.features.explore.data.LocationIQRepository` for use in this file
import com.waypoint.app.features.explore.data.LocationIQRepository
// imports `com.google.android.gms.location.LocationServices` for use in this file
import com.google.android.gms.location.LocationServices
// imports `com.google.android.gms.location.Priority` for use in this file
import com.google.android.gms.location.Priority
// imports `com.google.android.gms.tasks.CancellationTokenSource` for use in this file
import com.google.android.gms.tasks.CancellationTokenSource
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.asStateFlow` for use in this file
import kotlinx.coroutines.flow.asStateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch
// imports `kotlinx.coroutines.tasks.await` for use in this file
import kotlinx.coroutines.tasks.await

// declares private const read-only property `TAG`, initialised to the string literal "ExploreVM"
private const val TAG = "ExploreVM"

// declares internal function `applyExploreFilter` taking 2 parameters (`cache`, `filter`), returning `List<ExplorePlace>` and opens its body
internal fun applyExploreFilter(cache: PlacesCache, filter: ExploreFilter): List<ExplorePlace> {
    // returns `when (filter) {` from the current function
    return when (filter) {
        // lambda `ExploreFilter.ALL -> cache.places`
        ExploreFilter.ALL         -> cache.places
        // lambda `ExploreFilter.RESTAURANTS -> cache.places.filter { it.type…`
        ExploreFilter.RESTAURANTS -> cache.places.filter { it.type == "restaurant" }
        // lambda `ExploreFilter.CAFES -> cache.places.filter { it.type…`
        ExploreFilter.CAFES       -> cache.places.filter { it.type == "cafe" }
        // lambda `ExploreFilter.HOTELS -> cache.places.filter { it.type…`
        ExploreFilter.HOTELS      -> cache.places.filter { it.type == "hotel" }
        // lambda `ExploreFilter.PARKS -> cache.places.filter { it.type…`
        ExploreFilter.PARKS       -> cache.places.filter { it.type == "park" }
        // lambda `ExploreFilter.PUBS -> cache.places.filter { it.type…`
        ExploreFilter.PUBS        -> cache.places.filter { it.type == "pub" }
        // lambda `ExploreFilter.CINEMAS -> cache.places.filter { it.type…`
        ExploreFilter.CINEMAS     -> cache.places.filter { it.type == "cinema" }
    // closes the block
    }
// closes the function `applyExploreFilter`
}

// declares class `ExploreViewModel` with a primary constructor taking 1 parameter (`application`), inheriting from `AndroidViewModel(application)` and opens its body
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    // declares private read-only property `fusedLocation`
    private val fusedLocation =
        // continues the statement started above: `LocationServices.getFusedLocationProviderClient(application)`
        LocationServices.getFusedLocationProviderClient(application)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(ExploreUiState())
    // declares read-only property `uiState` of type `StateFlow<ExploreUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    // initialiser block: runs when an instance of the class is constructed
    init {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `ensureLoaded` on `RemoteSecrets` with arguments `()`
            RemoteSecrets.ensureLoaded()
            // calls `loadPlacesFromGps` with arguments `()`
            loadPlacesFromGps()
        // closes the block
        }
    // closes the init block
    }


    // declares function `retry` taking no parameters and opens its body
    fun retry() {
        // expression: `viewModelScope.launch { loadPlacesFromGps() }`
        viewModelScope.launch { loadPlacesFromGps() }
    // closes the function `retry`
    }

    // declares function `searchCity` taking 1 parameter (`city`) and opens its body
    fun searchCity(city: String) {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(placesState = PlacesState.Loading, loc…`
            _uiState.update { it.copy(placesState = PlacesState.Loading, locationLabel = city, visiblePlaces = emptyList()) }
            // declares read-only property `result`, initialised with the result of calling `LocationIQRepository.getPlaces(…)`
            val result = LocationIQRepository.getPlaces(city)
            // expression: `_uiState.update { state ->`
            _uiState.update { state ->
                // continues the statement started above: `if (result != null) {`
                if (result != null) {
                    // calls `copy` on `state` with an argument list that continues on the following lines
                    state.copy(
                        // continues the statement started above: `locationLabel = city,`
                        locationLabel = city,
                        // continues the statement started above: `placesState = PlacesState.Success(result),`
                        placesState   = PlacesState.Success(result),
                        // continues the statement started above: `visiblePlaces = applyExploreFilter(result, state.activeFilt…`
                        visiblePlaces = applyExploreFilter(result, state.activeFilter),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `copy` on `state` with arguments `(placesState = PlacesState.Error)`
                    state.copy(placesState = PlacesState.Error)
                // closes the else branch
                }
            // closes the block
            }
        // closes the block
        }
    // closes the function `searchCity`
    }

    // declares function `setFilter` taking 1 parameter (`filter`) and opens its body
    fun setFilter(filter: ExploreFilter) {
        // declares read-only property `current`, initialised to `_uiState.value.placesState`
        val current = _uiState.value.placesState
        // declares read-only property `cache`, initialised to `(current as? PlacesState.Success)?.cache ?: …`
        val cache = (current as? PlacesState.Success)?.cache ?: return
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `activeFilter = filter,`
                activeFilter  = filter,
                // continues the statement started above: `visiblePlaces = applyExploreFilter(cache, filter),`
                visiblePlaces = applyExploreFilter(cache, filter),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `setFilter`
    }


    // declares private suspend function `loadPlacesFromGps` taking no parameters and opens its body
    private suspend fun loadPlacesFromGps() {
        // declares read-only property `ctx`, initialised with the result of calling `getApplication(…)`
        val ctx = getApplication<Application>()

        // declares read-only property `hasPermission`, initialised with the result of calling `ContextCompat.checkSelfPermission(…)`
        val hasPermission = ContextCompat.checkSelfPermission(
            // continues the statement started above: `ctx, Manifest.permission.ACCESS_FINE_LOCATION,`
            ctx, Manifest.permission.ACCESS_FINE_LOCATION,
        // continues the statement started above: `) == PackageManager.PERMISSION_GRANTED ||`
        ) == PackageManager.PERMISSION_GRANTED ||
        // continues the statement started above: `ContextCompat.checkSelfPermission(`
        ContextCompat.checkSelfPermission(
            // continues the statement started above: `ctx, Manifest.permission.ACCESS_COARSE_LOCATION,`
            ctx, Manifest.permission.ACCESS_COARSE_LOCATION,
        // continues the statement started above: `) == PackageManager.PERMISSION_GRANTED`
        ) == PackageManager.PERMISSION_GRANTED

        // `if` statement: the block below runs when `!hasPermission` is true
        if (!hasPermission) {
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `locationLabel = "Location permission required",`
                    locationLabel = "Location permission required",
                    // continues the statement started above: `placesState = PlacesState.Error,`
                    placesState   = PlacesState.Error,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // returns from the current function with no value
            return
        // closes the if block
        }

        // expression: `_uiState.update { it.copy(placesState = PlacesState.Loading, vis…`
        _uiState.update { it.copy(placesState = PlacesState.Loading, visiblePlaces = emptyList()) }

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `cts`, initialised with the result of calling `CancellationTokenSource(…)`
            val cts = CancellationTokenSource()
            // declares read-only property `location`, initialised to `fusedLocation`
            val location = fusedLocation
                // chained call `.getCurrentLocation` on the previous result with arguments `(Priority.PRIORITY_BALANCED_POWER_ACCURA…)`
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                // chained call `.await` on the previous result
                .await()
                // statement: `?: fusedLocation.lastLocation.await()`
                ?: fusedLocation.lastLocation.await()

            // `if` statement: the block below runs when `location == null` is true
            if (location == null) {
                // calls `w` on `Log` with arguments `(TAG, "Could not get device location")`
                Log.w(TAG, "Could not get device location")
                // opens a block after `_uiState.update`
                _uiState.update {
                    // calls `copy` on `it` with an argument list that continues on the following lines
                    it.copy(
                        // continues the statement started above: `locationLabel = "Could not get location",`
                        locationLabel = "Could not get location",
                        // continues the statement started above: `placesState = PlacesState.Error,`
                        placesState   = PlacesState.Error,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
                // returns from the current function with no value
                return
            // closes the if block
            }

            // declares read-only property `lat`, initialised to `location.latitude`
            val lat = location.latitude
            // declares read-only property `lon`, initialised to `location.longitude`
            val lon = location.longitude

            // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
            val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
            // declares read-only property `label`, initialised with the result of calling `if(…)` and opens a lambda / block
            val label = if (key.isNotBlank()) {
                // calls `reverseGeocode` on `LocationIQRepository` with arguments `(lat, lon, key)`, then chains `.format(lat, lon)`
                LocationIQRepository.reverseGeocode(lat, lon, key) ?: "%.4f, %.4f".format(lat, lon)
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // statement: `"%.4f, %.4f".format(lat, lon)`
                "%.4f, %.4f".format(lat, lon)
            // closes the else branch
            }

            // expression: `_uiState.update { it.copy(locationLabel = label) }`
            _uiState.update { it.copy(locationLabel = label) }

            // declares read-only property `result`, initialised with the result of calling `LocationIQRepository.getPlacesByCoords(…)`
            val result = LocationIQRepository.getPlacesByCoords(lat, lon)
            // expression: `_uiState.update { state ->`
            _uiState.update { state ->
                // continues the statement started above: `if (result != null) {`
                if (result != null) {
                    // calls `copy` on `state` with an argument list that continues on the following lines
                    state.copy(
                        // continues the statement started above: `placesState = PlacesState.Success(result),`
                        placesState   = PlacesState.Success(result),
                        // continues the statement started above: `visiblePlaces = applyExploreFilter(result, state.activeFilt…`
                        visiblePlaces = applyExploreFilter(result, state.activeFilter),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `copy` on `state` with arguments `(placesState = PlacesState.Error)`
                    state.copy(placesState = PlacesState.Error)
                // closes the else branch
                }
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "GPS/places fetch failed: ${e.message}")`
            Log.w(TAG, "GPS/places fetch failed: ${e.message}")
            // expression: `_uiState.update { it.copy(placesState = PlacesState.Error) }`
            _uiState.update { it.copy(placesState = PlacesState.Error) }
        // closes the catch block
        }
    // closes the function `loadPlacesFromGps`
    }
// closes the class `ExploreViewModel`
}
