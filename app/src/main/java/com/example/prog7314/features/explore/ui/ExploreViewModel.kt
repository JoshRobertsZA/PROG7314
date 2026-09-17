package com.example.prog7314.features.explore.ui

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7314.core.cache.ExplorePlace
import com.example.prog7314.core.cache.PlacesCache
import com.example.prog7314.core.secrets.RemoteSecrets
import com.example.prog7314.features.explore.data.LocationIQRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val PREFS_NAME = "explore_prefs"
private const val KEY_CITY   = "selected_city"
private const val DEFAULT_CITY = "Cape Town"

/**
 * ViewModel for the Explore screen.
 *
 * City selection is persisted in SharedPreferences so it survives process
 * death. Places data is cache-first via LocationIQRepository (calendar-day TTL).
 * Filtering is purely client-side against the cached [PlacesCache.places] list.
 */
class ExploreViewModel(application: Application) : AndroidViewModel(application) {

    private val prefs = application.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(
        ExploreUiState(
            selectedCity = prefs.getString(KEY_CITY, DEFAULT_CITY) ?: DEFAULT_CITY,
        ),
    )
    val uiState: StateFlow<ExploreUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            loadPlaces(_uiState.value.selectedCity)
        }
    }

    // ── Public surface ─────────────────────────────────────────────────────────

    fun selectCity(city: String) {
        prefs.edit().putString(KEY_CITY, city).apply()
        _uiState.update { it.copy(selectedCity = city) }
        loadPlaces(city)
    }

    fun setFilter(filter: ExploreFilter) {
        val current = _uiState.value.placesState
        val cache = (current as? PlacesState.Success)?.cache ?: return
        _uiState.update {
            it.copy(
                activeFilter  = filter,
                visiblePlaces = applyFilter(cache, filter),
            )
        }
    }

    // ── Private helpers ────────────────────────────────────────────────────────

    private fun loadPlaces(city: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(placesState = PlacesState.Loading, visiblePlaces = emptyList()) }
            val result = LocationIQRepository.getPlaces(city)
            _uiState.update { state ->
                if (result != null) {
                    state.copy(
                        placesState   = PlacesState.Success(result),
                        visiblePlaces = applyFilter(result, state.activeFilter),
                    )
                } else {
                    state.copy(placesState = PlacesState.Error)
                }
            }
        }
    }

    /**
     * Maps [ExploreFilter] values to LocationIQ "type" and "class" strings.
     * LocationIQ uses OpenStreetMap taxonomy: class = "amenity"/"tourism"/etc.,
     * type = "restaurant"/"cafe"/"hotel"/etc.
     */
    private fun applyFilter(cache: PlacesCache, filter: ExploreFilter): List<ExplorePlace> {
        return when (filter) {
            ExploreFilter.ALL           -> cache.places
            ExploreFilter.RESTAURANTS   -> cache.places.filter { it.type == "restaurant" }
            ExploreFilter.CAFES         -> cache.places.filter { it.type == "cafe" }
            ExploreFilter.ATTRACTIONS   -> cache.places.filter {
                it.category in setOf("tourism", "leisure") ||
                it.type in setOf("museum", "attraction", "viewpoint", "gallery", "theme_park")
            }
            ExploreFilter.ENTERTAINMENT -> cache.places.filter {
                it.type in setOf("cinema", "theatre", "nightclub", "bar", "pub", "casino",
                                 "arts_centre", "stadium", "sports_centre")
            }
            ExploreFilter.HOTELS        -> cache.places.filter {
                it.type in setOf("hotel", "hostel", "motel", "guest_house", "apartment") ||
                it.category == "tourism"
            }
        }
    }
}
