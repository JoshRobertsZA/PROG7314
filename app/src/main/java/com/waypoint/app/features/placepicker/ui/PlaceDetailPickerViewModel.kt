package com.waypoint.app.features.placepicker.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.waypoint.app.features.home.data.WeatherRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "PlaceDetailPickerVM"

class PlaceDetailPickerViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val tripId   : String = checkNotNull(savedStateHandle["tripId"])
    private val dayId    : String = checkNotNull(savedStateHandle["dayId"])
    private val category : String = checkNotNull(savedStateHandle["category"])
    private val placeId  : String = checkNotNull(savedStateHandle["placeId"])

    private val tripRepo      = TripRepository(application)
    private val itineraryRepo = ItineraryRepository(application)

    private val _uiState = MutableStateFlow(PlaceDetailPickerUiState())
    val uiState: StateFlow<PlaceDetailPickerUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            loadAll()
        }
    }

    // ── Load ──────────────────────────────────────────────────────────────────

    private suspend fun loadAll() {
        // 1. Resolve the place from the trip's cached LocationIQ data
        val trip = tripRepo.getTripById(tripId)
        val cache = when {
            trip?.destLat != null && trip.destLng != null ->
                LocationIQRepository.getPlacesByCoords(trip.destLat, trip.destLng)
            trip?.destination != null ->
                LocationIQRepository.getPlaces(trip.destination)
            else -> null
        }

        val place = cache?.places?.firstOrNull { it.id == placeId }
        if (place == null) {
            Log.w(TAG, "Place $placeId not found in cache for trip $tripId")
            return
        }

        val distKm = if (place.distanceMetres > 0)
            "${"%.1f".format(place.distanceMetres / 1000.0)} km away"
        else ""

        _uiState.update {
            it.copy(
                placeName  = place.name,
                placeType  = place.type,
                placeEmoji = placeTypeEmoji(place.type),
                lat        = place.lat,
                lon        = place.lon,
                distanceKm = distKm,
            )
        }

        // 2. Fetch Wikipedia summary and weather in parallel
        val wikiDeferred    = viewModelScope.async {
            WikipediaPlaceRepository.getSummary(place.name)
        }
        val weatherDeferred = viewModelScope.async {
            WeatherRepository.getWeatherByCoords(place.lat, place.lon)
        }

        _uiState.update { it.copy(
            wikiSummary       = wikiDeferred.await(),
            isLoadingWiki     = false,
            weather           = weatherDeferred.await(),
            isLoadingWeather  = false,
        ) }
    }

    // ── Add to itinerary ──────────────────────────────────────────────────────

    fun onAddToItinerary() {
        val state = _uiState.value
        if (state.isAdding || state.isAdded || state.placeName.isBlank()) return
        _uiState.update { it.copy(isAdding = true) }
        viewModelScope.launch {
            itineraryRepo.insertPlace(
                dayId     = dayId,
                placeName = state.placeName,
                category  = category,
                lat       = state.lat,
                lng       = state.lon,
                photoUrl  = state.wikiSummary?.thumbnailUrl,
            )
            _uiState.update { it.copy(isAdding = false, isAdded = true) }
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private fun placeTypeEmoji(type: String): String = when (type) {
        "hotel"  -> "🏨"  // 🏨
        "pub"    -> "🍺"  // 🍺
        "cinema" -> "🎬"  // 🎬
        "park"   -> "🌳"  // 🌳
        else     -> "📍"  // 📍
    }
}
