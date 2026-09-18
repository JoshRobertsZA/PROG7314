package com.waypoint.app.features.placepicker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PlacePickerVM"

class PlacePickerViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val tripId   : String = checkNotNull(savedStateHandle["tripId"])
    val dayId            : String = checkNotNull(savedStateHandle["dayId"])
    private val category : String = checkNotNull(savedStateHandle["category"])

    private val tripRepo = TripRepository(application)

    private val _uiState = MutableStateFlow(PlacePickerUiState())
    val uiState: StateFlow<PlacePickerUiState> = _uiState.asStateFlow()

    init {
        _uiState.update { it.copy(categoryLabel = categoryLabel(category)) }
        viewModelScope.launch { loadPlaces() }
    }

    // ── Load ──────────────────────────────────────────────────────────────────

    private suspend fun loadPlaces() {
        _uiState.update { it.copy(loadState = PickerLoadState.Loading) }

        val trip = tripRepo.getTripById(tripId)
        if (trip == null || (trip.destLat == null && trip.destLng == null && trip.destination.isNullOrBlank())) {
            Log.w(TAG, "Trip $tripId has no destination — cannot load places")
            _uiState.update { it.copy(loadState = PickerLoadState.Error("This trip has no destination set. Edit your trip to add one.")) }
            return
        }

        val cache = when {
            trip.destLat != null && trip.destLng != null -> {
                Log.d(TAG, "Loading places by coords for trip $tripId")
                LocationIQRepository.getPlacesByCoords(trip.destLat, trip.destLng)
            }
            else -> {
                Log.d(TAG, "Loading places by city name '${trip.destination}' for trip $tripId")
                LocationIQRepository.getPlaces(trip.destination!!)
            }
        }

        if (cache == null) {
            _uiState.update { it.copy(loadState = PickerLoadState.Error("Could not load places. Check your connection and try again.")) }
            return
        }

        val typeKey  = categoryToType(category)
        val filtered = cache.places.filter { it.type == typeKey }
        Log.d(TAG, "Filtered ${filtered.size} places for $category (type=$typeKey)")

        _uiState.update {
            it.copy(
                loadState = if (filtered.isEmpty()) PickerLoadState.Empty
                            else PickerLoadState.Success(filtered),
            )
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    companion object {
        /** Maps DB category key to the LocationIQ OSM type string. */
        fun categoryToType(category: String): String = when (category) {
            "HOTELS"       -> "hotel"
            "RESTAURANTS"  -> "restaurant"
            "PARKS"        -> "park"
            "PUBS"    -> "pub"
            "CINEMAS" -> "cinema"
            else      -> category.lowercase()
        }

        /** Human-readable header label for the picker screen. */
        fun categoryLabel(category: String): String = when (category) {
            "HOTELS"       -> "Hotels"
            "RESTAURANTS"  -> "Restaurants & Cafes"
            "PARKS"        -> "Parks"
            "PUBS"    -> "Pubs"
            "CINEMAS" -> "Cinemas"
            else      -> category.lowercase().replaceFirstChar { it.uppercase() }
        }
    }
}
