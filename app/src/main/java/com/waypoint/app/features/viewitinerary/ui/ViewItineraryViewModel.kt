package com.waypoint.app.features.viewitinerary.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewItineraryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val repo = ItineraryRepository(application)

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

    private suspend fun loadFlightsFor(dayId: String): List<ViewFlightItem> =
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            ViewFlightItem(id = f.id, flightNumber = f.flightNumber, pdfUri = f.pdfUri, departureTime = f.departureTime, docName = f.docName)
        }

    private suspend fun loadPlacesFor(dayId: String): Map<String, List<ViewPlaceItem>> {
        val all = repo.getPlacesForDay(dayId).map { p ->
            ViewPlaceItem(id = p.id, name = p.name, category = p.category, note = p.note, lat = p.lat, lng = p.lng, photoUrl = p.photoUrl)
        }
        return itinCategories.associateWith { cat -> all.filter { it.category == cat } }
    }
}
