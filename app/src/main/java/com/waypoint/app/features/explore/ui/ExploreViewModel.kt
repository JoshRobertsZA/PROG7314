package com.waypoint.app.features.explore.ui

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

private const val TAG = "ExploreVM"

/** The places in [cache] that match [filter]. */
internal fun applyExploreFilter(cache: PlacesCache, filter: ExploreFilter): List<ExplorePlace> {
    return when (filter) {
        ExploreFilter.ALL         -> cache.places
        ExploreFilter.RESTAURANTS -> cache.places.filter { it.type == "restaurant" }
        ExploreFilter.CAFES       -> cache.places.filter { it.type == "cafe" }
        ExploreFilter.HOTELS      -> cache.places.filter { it.type == "hotel" }
        ExploreFilter.PARKS       -> cache.places.filter { it.type == "park" }
        ExploreFilter.PUBS        -> cache.places.filter { it.type == "pub" }
        ExploreFilter.CINEMAS     -> cache.places.filter { it.type == "cinema" }
    }
}

/** Finds the user's location (or uses a searched city) and loads nearby places for the Explore tab. */
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val fusedLocation =
        LocationServices.getFusedLocationProviderClient(application)

    private val _uiState = MutableStateFlow(ExploreUiState())
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            loadPlacesFromGps()
        }
    }


    fun retry() {
        viewModelScope.launch { loadPlacesFromGps() }
    }

    fun searchCity(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(placesState = PlacesState.Loading, locationLabel = city, visiblePlaces = emptyList()) }
            val result = LocationIQRepository.getPlaces(getApplication(), city)
            _uiState.update { state ->
                if (result != null) {
                    state.copy(
                        locationLabel = city,
                        placesState   = PlacesState.Success(result),
                        visiblePlaces = applyExploreFilter(result, state.activeFilter),
                    )
                } else {
                    state.copy(placesState = PlacesState.Error)
                }
            }
        }
    }

    fun setFilter(filter: ExploreFilter) {
        val current = _uiState.value.placesState
        val cache = (current as? PlacesState.Success)?.cache ?: return
        _uiState.update {
            it.copy(
                activeFilter  = filter,
                visiblePlaces = applyExploreFilter(cache, filter),
            )
        }
    }


    private suspend fun loadPlacesFromGps() {
        val ctx = getApplication<Application>()

        val hasPermission = ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_FINE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED ||
        ContextCompat.checkSelfPermission(
            ctx, Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED

        if (!hasPermission) {
            _uiState.update {
                it.copy(
                    locationLabel = "Location permission required",
                    placesState   = PlacesState.Error,
                )
            }
            return
        }

        _uiState.update { it.copy(placesState = PlacesState.Loading, visiblePlaces = emptyList()) }

        try {
            val cts = CancellationTokenSource()
            val location = fusedLocation
                .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, cts.token)
                .await()
                ?: fusedLocation.lastLocation.await()

            if (location == null) {
                Log.w(TAG, "Could not get device location")
                _uiState.update {
                    it.copy(
                        locationLabel = "Could not get location",
                        placesState   = PlacesState.Error,
                    )
                }
                return
            }

            val lat = location.latitude
            val lon = location.longitude

            val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
            val label = if (key.isNotBlank()) {
                LocationIQRepository.reverseGeocode(lat, lon, key) ?: "%.4f, %.4f".format(lat, lon)
            } else {
                "%.4f, %.4f".format(lat, lon)
            }

            _uiState.update { it.copy(locationLabel = label) }

            val result = LocationIQRepository.getPlacesByCoords(ctx, lat, lon)
            _uiState.update { state ->
                if (result != null) {
                    state.copy(
                        placesState   = PlacesState.Success(result),
                        visiblePlaces = applyExploreFilter(result, state.activeFilter),
                    )
                } else {
                    state.copy(placesState = PlacesState.Error)
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "GPS/places fetch failed: ${e.message}")
            _uiState.update { it.copy(placesState = PlacesState.Error) }
        }
    }
}
