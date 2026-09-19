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
// imports `com.waypoint.app.features.explore.data.LocationIQRepository` for use in this file
import com.waypoint.app.features.explore.data.LocationIQRepository
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
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

// declares private const read-only property `TAG`, initialised to the string literal "PlacePickerVM"
private const val TAG = "PlacePickerVM"

// expression: `class PlacePickerViewModel(`
class PlacePickerViewModel(
    // continues the statement started above: `application: Application,`
    application: Application,
    // continues the statement started above: `savedStateHandle: SavedStateHandle,`
    savedStateHandle: SavedStateHandle,
// continues the statement started above: `) : AndroidViewModel(application) {`
) : AndroidViewModel(application) {

    // declares private read-only property `tripId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val tripId   : String = checkNotNull(savedStateHandle["tripId"])
    // declares read-only property `dayId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    val dayId            : String = checkNotNull(savedStateHandle["dayId"])
    // declares private read-only property `category` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val category : String = checkNotNull(savedStateHandle["category"])

    // declares private read-only property `tripRepo`, initialised with the result of calling `TripRepository(…)`
    private val tripRepo = TripRepository(application)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(PlacePickerUiState())
    // declares read-only property `uiState` of type `StateFlow<PlacePickerUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<PlacePickerUiState> = _uiState.asStateFlow()

    // initialiser block: runs when an instance of the class is constructed
    init {
        // expression: `_uiState.update { it.copy(categoryLabel = categoryLabel(category…`
        _uiState.update { it.copy(categoryLabel = categoryLabel(category)) }
        // expression: `viewModelScope.launch { loadPlaces() }`
        viewModelScope.launch { loadPlaces() }
    // closes the init block
    }


    // declares private suspend function `loadPlaces` taking no parameters and opens its body
    private suspend fun loadPlaces() {
        // expression: `_uiState.update { it.copy(loadState = PickerLoadState.Loading) }`
        _uiState.update { it.copy(loadState = PickerLoadState.Loading) }

        // declares read-only property `trip`, initialised with the result of calling `tripRepo.getTripById(…)`
        val trip = tripRepo.getTripById(tripId)
        // `if` statement: the block below runs when `trip == null || (trip.destLat == null && trip.destLng == null && trip…` is true
        if (trip == null || (trip.destLat == null && trip.destLng == null && trip.destination.isNullOrBlank())) {
            // calls `w` on `Log` with arguments `(TAG, "Trip $tripId has no destination — cann…)`
            Log.w(TAG, "Trip $tripId has no destination — cannot load places")
            // expression: `_uiState.update { it.copy(loadState = PickerLoadState.Error("Thi…`
            _uiState.update { it.copy(loadState = PickerLoadState.Error("This trip has no destination set. Edit your trip to add one.")) }
            // returns from the current function with no value
            return
        // closes the if block
        }

        // declares read-only property `cache`, initialised to `when` and opens a lambda / block
        val cache = when {
            // lambda with parameters `trip.destLat != null && trip.destLng !=…`: opens its body
            trip.destLat != null && trip.destLng != null -> {
                // calls `d` on `Log` with arguments `(TAG, "Loading places by coords for trip $tri…)`
                Log.d(TAG, "Loading places by coords for trip $tripId")
                // calls `getPlacesByCoords` on `LocationIQRepository` with arguments `(trip.destLat, trip.destLng)`
                LocationIQRepository.getPlacesByCoords(trip.destLat, trip.destLng)
            // closes the lambda body
            }
            // `else` branch of the `when`: opens a block
            else -> {
                // calls `d` on `Log` with arguments `(TAG, "Loading places by city name '${trip.de…)`
                Log.d(TAG, "Loading places by city name '${trip.destination}' for trip $tripId")
                // calls `getPlaces` on `LocationIQRepository` with arguments `(trip.destination!!)`
                LocationIQRepository.getPlaces(trip.destination!!)
            // closes the when else-branch
            }
        // closes the lambda assigned to `cache`
        }

        // `if` statement: the block below runs when `cache == null` is true
        if (cache == null) {
            // expression: `_uiState.update { it.copy(loadState = PickerLoadState.Error("Cou…`
            _uiState.update { it.copy(loadState = PickerLoadState.Error("Could not load places. Check your connection and try again.")) }
            // returns from the current function with no value
            return
        // closes the if block
        }

        // declares read-only property `typeKey`, initialised with the result of calling `categoryToType(…)`
        val typeKey  = categoryToType(category)
        // declares read-only property `filtered`, initialised to `cache.places.filter { it.type == typeKey }`
        val filtered = cache.places.filter { it.type == typeKey }
        // calls `d` on `Log` with arguments `(TAG, "Filtered ${filtered.size} places for $…)`
        Log.d(TAG, "Filtered ${filtered.size} places for $category (type=$typeKey)")

        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(
                // continues the statement started above: `loadState = if (filtered.isEmpty()) PickerLoadState.Empty`
                loadState = if (filtered.isEmpty()) PickerLoadState.Empty
                            // continues the statement started above: `else PickerLoadState.Success(filtered),`
                            else PickerLoadState.Success(filtered),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `loadPlaces`
    }


    // declares the companion object holding members shared by all instances of the enclosing class
    companion object {
        // declares function `categoryToType` taking 1 parameter (`category`), returning `String`; its body is the expression `when (category) {`
        fun categoryToType(category: String): String = when (category) {
            // lambda `"HOTELS" -> "hotel"`
            "HOTELS"       -> "hotel"
            // lambda `"RESTAURANTS" -> "restaurant"`
            "RESTAURANTS"  -> "restaurant"
            // lambda `"PARKS" -> "park"`
            "PARKS"        -> "park"
            // lambda `"PUBS" -> "pub"`
            "PUBS"    -> "pub"
            // lambda `"CINEMAS" -> "cinema"`
            "CINEMAS" -> "cinema"
            // `else` branch of the `when`: evaluates `category.lowercase()`
            else      -> category.lowercase()
        // closes the block
        }

        // declares function `categoryLabel` taking 1 parameter (`category`), returning `String`; its body is the expression `when (category) {`
        fun categoryLabel(category: String): String = when (category) {
            // lambda `"HOTELS" -> "Hotels"`
            "HOTELS"       -> "Hotels"
            // lambda `"RESTAURANTS" -> "Restaurants & Cafes"`
            "RESTAURANTS"  -> "Restaurants & Cafes"
            // lambda `"PARKS" -> "Parks"`
            "PARKS"        -> "Parks"
            // lambda `"PUBS" -> "Pubs"`
            "PUBS"    -> "Pubs"
            // lambda `"CINEMAS" -> "Cinemas"`
            "CINEMAS" -> "Cinemas"
            // `else` branch of the `when`: evaluates `category.lowercase().replaceFirstChar { it.uppercase() }`
            else      -> category.lowercase().replaceFirstChar { it.uppercase() }
        // closes the block
        }
    // closes the companion object
    }
// closes the block
}
