// declares that this file belongs to the package `com.waypoint.app.features.viewitinerary.ui`
package com.waypoint.app.features.viewitinerary.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.SavedStateHandle` for use in this file
import androidx.lifecycle.SavedStateHandle
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.features.edititinerary.data.ItineraryRepository` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
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
// imports `com.waypoint.app.core.network.AirLabsRepository` for use in this file
import com.waypoint.app.core.network.AirLabsRepository
// imports `android.util.Log` for use in this file
import android.util.Log

// expression: `class ViewItineraryViewModel(`
class ViewItineraryViewModel(
    // continues the statement started above: `application: Application,`
    application: Application,
    // continues the statement started above: `savedStateHandle: SavedStateHandle,`
    savedStateHandle: SavedStateHandle,
// continues the statement started above: `) : AndroidViewModel(application) {`
) : AndroidViewModel(application) {

    // declares private read-only property `tripId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    // declares private read-only property `repo`, initialised with the result of calling `ItineraryRepository(…)`
    private val repo = ItineraryRepository(application)

    // companion object holding a log tag for this ViewModel
    companion object { private const val TAG = "ViewItineraryVM" }

    // in-memory cache: flight IATA -> (fetchedAtMillis, result); cleared when ViewModel is cleared
    private val flightStatusCache = HashMap<String, Pair<Long, AirLabsRepository.FlightStatusResult>>()
    // cache TTL: 10 minutes
    private val CACHE_TTL_MS = 10 * 60 * 1_000L

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(ViewItineraryUiState())
    // declares read-only property `uiState` of type `StateFlow<ViewItineraryUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<ViewItineraryUiState> = _uiState.asStateFlow()

    // declares private read-only property `itinCategories`, initialised with the result of calling `listOf(…)`
    private val itinCategories = listOf("RESTAURANTS", "HOTELS", "PARKS", "PUBS", "CINEMAS")

    // expression: `init { loadAll() }`
    init { loadAll() }

    // declares private function `loadAll` taking no parameters and opens its body
    private fun loadAll() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true) }`
            _uiState.update { it.copy(isLoading = true) }

            // declares read-only property `dayEntities`, initialised with the result of calling `repo.getSelectedDaysWithIds(…)`
            val dayEntities = repo.getSelectedDaysWithIds(tripId)
            // declares read-only property `days`, initialised to a lambda / arrow function
            val days        = dayEntities.map { e -> ViewDayItem(dayId = e.id, date = e.date) }

            // declares read-only property `lodgings`, initialised with the result of calling `repo.getLodgingsForTrip(…)`
            val lodgings = repo.getLodgingsForTrip(tripId).map { l -> ViewLodgingItem(l.fromDate, l.toDate, l.pdfUri, l.docName) }
            // declares read-only property `cars`, initialised with the result of calling `repo.getCarRentalsForTrip(…)`
            val cars     = repo.getCarRentalsForTrip(tripId).map { c -> ViewCarRentalItem(c.fromDate, c.toDate, c.pdfUri, c.docName) }

            // declares read-only property `activeIndex`, initialised to the number 0
            val activeIndex = 0
            // declares read-only property `flights`, initialised with the result of calling `if(…)`
            val flights     = if (days.isNotEmpty()) loadFlightsFor(days[activeIndex].dayId) else emptyList()
            // declares read-only property `places`, initialised with the result of calling `if(…)`
            val places      = if (days.isNotEmpty()) loadPlacesFor(days[activeIndex].dayId)  else emptyMap()

            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isLoading = false,`
                    isLoading           = false,
                    // continues the statement started above: `days = days,`
                    days                = days,
                    // continues the statement started above: `activeDayIndex = activeIndex,`
                    activeDayIndex      = activeIndex,
                    // continues the statement started above: `flightsForActiveDay = flights,`
                    flightsForActiveDay = flights,
                    // continues the statement started above: `lodgings = lodgings,`
                    lodgings            = lodgings,
                    // continues the statement started above: `carRentals = cars,`
                    carRentals          = cars,
                    // continues the statement started above: `placesForActiveDay = places,`
                    placesForActiveDay  = places,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `loadAll`
    }

    // declares function `onDaySelected` taking 1 parameter (`index`) and opens its body
    fun onDaySelected(index: Int) {
        // declares read-only property `days`, initialised to `_uiState.value.days`
        val days = _uiState.value.days
        // `if` statement: executes `return` when `index < 0 || index >= days.size` is true
        if (index < 0 || index >= days.size) return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `dayId`, initialised to `days[index].dayId`
            val dayId   = days[index].dayId
            // declares read-only property `flights`, initialised with the result of calling `loadFlightsFor(…)`
            val flights = loadFlightsFor(dayId)
            // declares read-only property `places`, initialised with the result of calling `loadPlacesFor(…)`
            val places  = loadPlacesFor(dayId)
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `activeDayIndex = index,`
                    activeDayIndex      = index,
                    // continues the statement started above: `flightsForActiveDay = flights,`
                    flightsForActiveDay = flights,
                    // continues the statement started above: `placesForActiveDay = places,`
                    placesForActiveDay  = places,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `onDaySelected`
    }

    // declares function `onPlaceSelected` taking 1 parameter (`place`) and opens its body
    fun onPlaceSelected(place: ViewPlaceItem) {
        // expression: `_uiState.update { it.copy(selectedPlace = place) }`
        _uiState.update { it.copy(selectedPlace = place) }
    // closes the function `onPlaceSelected`
    }

    // declares function `onPlaceDismissed` taking no parameters and opens its body
    fun onPlaceDismissed() {
        // expression: `_uiState.update { it.copy(selectedPlace = null) }`
        _uiState.update { it.copy(selectedPlace = null) }
    // closes the function `onPlaceDismissed`
    }

    // declares function `onFlightStatusTap` that triggers a live status fetch for the given flight number
    fun onFlightStatusTap(flightNumber: String) {
        viewModelScope.launch {
            val iata = flightNumber.replace(" ", "").uppercase()
            // check in-memory cache first
            val cached = flightStatusCache[iata]
            if (cached != null && (System.currentTimeMillis() - cached.first) < CACHE_TTL_MS) {
                Log.d(TAG, "Flight status cache hit for $iata")
                _uiState.update { it.copy(flightStatus = FlightStatusState.Success(cached.second)) }
                return@launch
            }
            // show spinner while fetching
            _uiState.update { it.copy(flightStatus = FlightStatusState.Loading) }
            val result = AirLabsRepository.fetchFlightStatus(iata)
            if (result != null) {
                flightStatusCache[iata] = Pair(System.currentTimeMillis(), result)
                _uiState.update { it.copy(flightStatus = FlightStatusState.Success(result)) }
            } else {
                // cache the miss for 2 minutes so rapid re-taps on an unknown flight don't burn quota
                val missResult = AirLabsRepository.FlightStatusResult(iata, "not_found", "", "", "", "", "", "", 0, 0)
                flightStatusCache[iata] = Pair(System.currentTimeMillis() - CACHE_TTL_MS + 2 * 60 * 1_000L, missResult)
                _uiState.update { it.copy(flightStatus = FlightStatusState.Error("No flight found for $iata")) }
            }
        }
    }

    // declares function `onFlightStatusDismissed` that hides the modal
    fun onFlightStatusDismissed() {
        _uiState.update { it.copy(flightStatus = FlightStatusState.Idle) }
    }

    // declares private suspend function `loadFlightsFor` taking 1 parameter (`dayId`), returning `List<ViewFlightItem>`; its body is the expression ``
    private suspend fun loadFlightsFor(dayId: String): List<ViewFlightItem> =
        // continues the statement started above: `repo.getFlightsForDays(listOf(dayId)).map { f ->`
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            // continues the statement started above: `ViewFlightItem(id = f.id, flightNumber = f.flightNumber, pd…`
            ViewFlightItem(id = f.id, flightNumber = f.flightNumber, pdfUri = f.pdfUri, departureTime = f.departureTime?.takeIf { it != AirLabsRepository.AIRLABS_MISS }, docName = f.docName)
        // closes the block
        }

    // declares private suspend function `loadPlacesFor` taking 1 parameter (`dayId`), returning `Map<String, List<ViewPlaceItem>>` and opens its body
    private suspend fun loadPlacesFor(dayId: String): Map<String, List<ViewPlaceItem>> {
        // declares read-only property `all`, initialised with the result of calling `repo.getPlacesForDay(…)`
        val all = repo.getPlacesForDay(dayId).map { p ->
            // continues the statement started above: `ViewPlaceItem(id = p.id, name = p.name, category = p.catego…`
            ViewPlaceItem(id = p.id, name = p.name, category = p.category, note = p.note, lat = p.lat, lng = p.lng, photoUrl = p.photoUrl)
        // closes the block
        }
        // lambda `return itinCategories.associa… -> all.filter { it.category == c…`
        return itinCategories.associateWith { cat -> all.filter { it.category == cat } }
    // closes the function `loadPlacesFor`
    }
// closes the block
}
