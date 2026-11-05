package com.waypoint.app.features.viewitinerary.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.R
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.waypoint.app.core.network.AirLabsRepository
import com.waypoint.app.core.network.GuardedApi
import com.waypoint.app.core.network.UsageGuardian
import android.util.Log

/**
 * Loads a trip's itinerary for viewing and looks up live flight status on AirLabs (within the usage
 * budget).
 */
class ViewItineraryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val repo = ItineraryRepository(application)

    companion object { private const val TAG = "ViewItineraryVM" }

    private val flightStatusCache = HashMap<String, Pair<Long, AirLabsRepository.FlightStatusResult>>()
    private val CACHE_TTL_MS = 10 * 60 * 1_000L

    private val _uiState = MutableStateFlow(ViewItineraryUiState())
    val uiState: StateFlow<ViewItineraryUiState> = _uiState.asStateFlow()

    private val itinCategories = listOf("RESTAURANTS", "HOTELS", "PARKS", "PUBS", "CINEMAS")

    init { loadAll() }

    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val dayEntities = repo.getSelectedDaysWithIds(tripId)
            val days        = dayEntities.map { e -> ViewDayItem(dayId = e.id, date = e.date) }

            val lodgings = repo.getLodgingsForTrip(tripId).map { l -> ViewLodgingItem(l.fromDate, l.toDate, l.pdfUri, l.docName) }
            val cars     = repo.getCarRentalsForTrip(tripId).map { c -> ViewCarRentalItem(c.fromDate, c.toDate, c.pdfUri, c.docName) }

            val activeIndex = 0
            val flights     = if (days.isNotEmpty()) loadFlightsFor(days[activeIndex].dayId) else emptyList()
            val places      = if (days.isNotEmpty()) loadPlacesFor(days[activeIndex].dayId)  else emptyMap()

            _uiState.update {
                it.copy(
                    isLoading           = false,
                    days                = days,
                    activeDayIndex      = activeIndex,
                    flightsForActiveDay = flights,
                    lodgings            = lodgings,
                    carRentals          = cars,
                    placesForActiveDay  = places,
                )
            }
        }
    }

    fun onDaySelected(index: Int) {
        val days = _uiState.value.days
        if (index < 0 || index >= days.size) return
        viewModelScope.launch {
            val dayId   = days[index].dayId
            val flights = loadFlightsFor(dayId)
            val places  = loadPlacesFor(dayId)
            _uiState.update {
                it.copy(
                    activeDayIndex      = index,
                    flightsForActiveDay = flights,
                    placesForActiveDay  = places,
                )
            }
        }
    }

    fun onPlaceSelected(place: ViewPlaceItem) {
        _uiState.update { it.copy(selectedPlace = place) }
    }

    fun onPlaceDismissed() {
        _uiState.update { it.copy(selectedPlace = null) }
    }

    fun onFlightStatusTap(flightNumber: String) {
        viewModelScope.launch {
            val iata = flightNumber.replace(" ", "").uppercase()
            val date = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.date?.toString()
            val cacheKey = AirLabsRepository.cacheKey(iata, date)
            val cached = flightStatusCache[cacheKey]
            if (cached != null && (System.currentTimeMillis() - cached.first) < CACHE_TTL_MS) {
                Log.d(TAG, "Flight status cache hit for $iata")
                _uiState.update { it.copy(flightStatus = FlightStatusState.Success(cached.second)) }
                return@launch
            }
            _uiState.update { it.copy(flightStatus = FlightStatusState.Loading) }
            val result = AirLabsRepository.fetchFlightStatus(iata, date)
            if (result != null) {
                flightStatusCache[cacheKey] = Pair(System.currentTimeMillis(), result)
                _uiState.update { it.copy(flightStatus = FlightStatusState.Success(result)) }
                val dayId = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.dayId
                if (dayId != null) {
                    _uiState.update { it.copy(flightsForActiveDay = loadFlightsFor(dayId)) }
                }
            } else {
                val budgetUsedUp = !UsageGuardian.isAllowed(GuardedApi.AIRLABS)
                if (budgetUsedUp) {
                    val lastKnown = flightStatusCache[cacheKey]?.second?.takeIf { it.status != "not_found" }
                    _uiState.update {
                        it.copy(
                            flightStatus = if (lastKnown != null) FlightStatusState.Success(lastKnown)
                                           else FlightStatusState.Error(getApplication<Application>().getString(R.string.flight_status_limit_reached)),
                        )
                    }
                    return@launch
                }
                val missResult = AirLabsRepository.FlightStatusResult(iata, "not_found", "", "", "", "", "", "", 0, 0)
                flightStatusCache[cacheKey] = Pair(System.currentTimeMillis() - CACHE_TTL_MS + 2 * 60 * 1_000L, missResult)
                _uiState.update { it.copy(flightStatus = FlightStatusState.Error("No flight found for $iata")) }
            }
        }
    }

    fun onFlightStatusDismissed() {
        _uiState.update { it.copy(flightStatus = FlightStatusState.Idle) }
    }

    private suspend fun loadFlightsFor(dayId: String): List<ViewFlightItem> =
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            val iata = f.flightNumber?.replace(" ", "")?.uppercase()
            val dayDate = _uiState.value.days.firstOrNull { it.dayId == dayId }?.date?.toString()
            val cached = if (iata != null) flightStatusCache[AirLabsRepository.cacheKey(iata, dayDate)] else null
            val isAirborne = cached != null && cached.second.status.lowercase() in listOf("active", "landed")
            ViewFlightItem(
                id            = f.id,
                flightNumber  = f.flightNumber,
                pdfUri        = f.pdfUri,
                departureTime = f.departureTime?.takeIf { it != AirLabsRepository.AIRLABS_MISS },
                arrivalTime   = if (isAirborne) cached!!.second.arrTime.substringAfter(" ", cached.second.arrTime).takeIf { it.isNotBlank() } else null,
                docName       = f.docName,
            )
        }

    private suspend fun loadPlacesFor(dayId: String): Map<String, List<ViewPlaceItem>> {
        val all = repo.getPlacesForDay(dayId).map { p ->
            ViewPlaceItem(id = p.id, name = p.name, category = p.category, note = p.note, lat = p.lat, lng = p.lng, photoUrl = p.photoUrl)
        }
        return itinCategories.associateWith { cat -> all.filter { it.category == cat } }
    }
}
