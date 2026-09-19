// declares that this file belongs to the package `com.waypoint.app.features.placepicker.ui`
package com.waypoint.app.features.placepicker.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.SavedStateHandle` for use in this file
import androidx.lifecycle.SavedStateHandle
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.features.edititinerary.data.ItineraryRepository` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
// imports `com.waypoint.app.features.explore.data.LocationIQRepository` for use in this file
import com.waypoint.app.features.explore.data.LocationIQRepository
// imports `com.waypoint.app.features.home.data.WeatherRepository` for use in this file
import com.waypoint.app.features.home.data.WeatherRepository
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository` for use in this file
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository
// imports `kotlinx.coroutines.async` for use in this file
import kotlinx.coroutines.async
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

// declares private const read-only property `TAG`, initialised to the string literal "PlaceDetailPickerVM"
private const val TAG = "PlaceDetailPickerVM"

// expression: `class PlaceDetailPickerViewModel(`
class PlaceDetailPickerViewModel(
    // continues the statement started above: `application: Application,`
    application: Application,
    // continues the statement started above: `savedStateHandle: SavedStateHandle,`
    savedStateHandle: SavedStateHandle,
// continues the statement started above: `) : AndroidViewModel(application) {`
) : AndroidViewModel(application) {

    // declares private read-only property `tripId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val tripId   : String = checkNotNull(savedStateHandle["tripId"])
    // declares private read-only property `dayId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val dayId    : String = checkNotNull(savedStateHandle["dayId"])
    // declares private read-only property `category` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val category : String = checkNotNull(savedStateHandle["category"])
    // declares private read-only property `placeId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val placeId  : String = checkNotNull(savedStateHandle["placeId"])

    // declares private read-only property `tripRepo`, initialised with the result of calling `TripRepository(…)`
    private val tripRepo      = TripRepository(application)
    // declares private read-only property `itineraryRepo`, initialised with the result of calling `ItineraryRepository(…)`
    private val itineraryRepo = ItineraryRepository(application)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(PlaceDetailPickerUiState())
    // declares read-only property `uiState` of type `StateFlow<PlaceDetailPickerUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<PlaceDetailPickerUiState> = _uiState.asStateFlow()

    // initialiser block: runs when an instance of the class is constructed
    init {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `ensureLoaded` on `RemoteSecrets` with arguments `()`
            RemoteSecrets.ensureLoaded()
            // calls `loadAll` with arguments `()`
            loadAll()
        // closes the block
        }
    // closes the init block
    }


    // declares private suspend function `loadAll` taking no parameters and opens its body
    private suspend fun loadAll() {
        // declares read-only property `trip`, initialised with the result of calling `tripRepo.getTripById(…)`
        val trip = tripRepo.getTripById(tripId)
        // declares read-only property `cache`, initialised to `when` and opens a lambda / block
        val cache = when {
            // expression: `trip?.destLat != null && trip.destLng != null ->`
            trip?.destLat != null && trip.destLng != null ->
                // continues the statement started above: `LocationIQRepository.getPlacesByCoords(trip.destLat, trip.d…`
                LocationIQRepository.getPlacesByCoords(trip.destLat, trip.destLng)
            // expression: `trip?.destination != null ->`
            trip?.destination != null ->
                // continues the statement started above: `LocationIQRepository.getPlaces(trip.destination)`
                LocationIQRepository.getPlaces(trip.destination)
            // `else` branch of the `when`: evaluates `null`
            else -> null
        // closes the lambda assigned to `cache`
        }

        // declares read-only property `place`, initialised to `cache?.places?.firstOrNull { it.id == placeI…`
        val place = cache?.places?.firstOrNull { it.id == placeId }
        // `if` statement: the block below runs when `place == null` is true
        if (place == null) {
            // calls `w` on `Log` with arguments `(TAG, "Place $placeId not found in cache for …)`
            Log.w(TAG, "Place $placeId not found in cache for trip $tripId")
            // returns from the current function with no value
            return
        // closes the if block
        }

        // declares read-only property `distKm`, initialised with the result of calling `if(…)`
        val distKm = if (place.distanceMetres > 0)
            // expression: `"${"%.1f".format(place.distanceMetres / 1000.0)} km away"`
            "${"%.1f".format(place.distanceMetres / 1000.0)} km away"
        // expression: `else ""`
        else ""

        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `placeName = place.name,`
                placeName  = place.name,
                // continues the statement started above: `placeType = place.type,`
                placeType  = place.type,
                // continues the statement started above: `placeEmoji = placeTypeEmoji(place.type),`
                placeEmoji = placeTypeEmoji(place.type),
                // continues the statement started above: `lat = place.lat,`
                lat        = place.lat,
                // continues the statement started above: `lon = place.lon,`
                lon        = place.lon,
                // continues the statement started above: `distanceKm = distKm,`
                distanceKm = distKm,
            // closes the multi-line argument list started above
            )
        // closes the block
        }

        // declares read-only property `wikiDeferred`, initialised to `viewModelScope.async` and opens a lambda / block
        val wikiDeferred    = viewModelScope.async {
            // calls `getSummary` on `WikipediaPlaceRepository` with arguments `(place.name)`
            WikipediaPlaceRepository.getSummary(place.name)
        // closes the lambda assigned to `wikiDeferred`
        }
        // declares read-only property `weatherDeferred`, initialised to `viewModelScope.async` and opens a lambda / block
        val weatherDeferred = viewModelScope.async {
            // calls `getWeatherByCoords` on `WeatherRepository` with arguments `(place.lat, place.lon)`
            WeatherRepository.getWeatherByCoords(place.lat, place.lon)
        // closes the lambda assigned to `weatherDeferred`
        }

        // declares read-only property `wiki`, initialised with the result of calling `wikiDeferred.await(…)`
        val wiki = wikiDeferred.await()
        // declares read-only property `bitmap`, initialised to `wiki?.thumbnailUrl?.let { WikipediaPlaceRepo…`
        val bitmap = wiki?.thumbnailUrl?.let { WikipediaPlaceRepository.downloadBitmap(it) }
        // expression: `_uiState.update { it.copy(`
        _uiState.update { it.copy(
            // continues the statement started above: `wikiSummary = wiki,`
            wikiSummary       = wiki,
            // continues the statement started above: `photoBitmap = bitmap,`
            photoBitmap       = bitmap,
            // continues the statement started above: `isLoadingWiki = false,`
            isLoadingWiki     = false,
            // continues the statement started above: `weather = weatherDeferred.await(),`
            weather           = weatherDeferred.await(),
            // continues the statement started above: `isLoadingWeather = false,`
            isLoadingWeather  = false,
        // continues the statement started above: `) }`
        ) }
    // closes the function `loadAll`
    }


    // declares function `onAddToItinerary` taking no parameters and opens its body
    fun onAddToItinerary() {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // `if` statement: executes `return` when `state.isAdding || state.isAdded || state.placeName.isBlank()` is true
        if (state.isAdding || state.isAdded || state.placeName.isBlank()) return
        // expression: `_uiState.update { it.copy(isAdding = true) }`
        _uiState.update { it.copy(isAdding = true) }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `insertPlace` on `itineraryRepo` with an argument list that continues on the following lines
            itineraryRepo.insertPlace(
                // continues the statement started above: `dayId = dayId,`
                dayId     = dayId,
                // continues the statement started above: `placeName = state.placeName,`
                placeName = state.placeName,
                // continues the statement started above: `category = category,`
                category  = category,
                // continues the statement started above: `lat = state.lat,`
                lat       = state.lat,
                // continues the statement started above: `lng = state.lon,`
                lng       = state.lon,
                // continues the statement started above: `photoUrl = state.wikiSummary?.thumbnailUrl,`
                photoUrl  = state.wikiSummary?.thumbnailUrl,
            // closes the multi-line argument list started above
            )
            // expression: `_uiState.update { it.copy(isAdding = false, isAdded = true) }`
            _uiState.update { it.copy(isAdding = false, isAdded = true) }
        // closes the block
        }
    // closes the function `onAddToItinerary`
    }


    // declares private function `placeTypeEmoji` taking 1 parameter (`type`), returning `String`; its body is the expression `when (type) {`
    private fun placeTypeEmoji(type: String): String = when (type) {
        // lambda `"hotel" -> "🏨"`
        "hotel"  -> "🏨"
        // lambda `"pub" -> "🍺"`
        "pub"    -> "🍺"
        // lambda `"cinema" -> "🎬"`
        "cinema" -> "🎬"
        // lambda `"park" -> "🌳"`
        "park"   -> "🌳"
        // `else` branch of the `when`: evaluates `"📍"`
        else     -> "📍"
    // closes the block
    }
// closes the block
}
