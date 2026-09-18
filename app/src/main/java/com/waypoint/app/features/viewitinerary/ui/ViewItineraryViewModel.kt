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

            val lodging = repo.getLodgingForTrip(tripId)?.let { l ->
                ViewLodgingItem(fromDate = l.fromDate, toDate = l.toDate, pdfUri = l.pdfUri)
            }
            val car = repo.getCarRentalForTrip(tripId)?.let { c ->
                ViewCarRentalItem(fromDate = c.fromDate, toDate = c.toDate, pdfUri = c.pdfUri)
            }

            val activeIndex = 0
            val flights     = if (days.isNotEmpty()) loadFlightsFor(days[activeIndex].dayId) else emptyList()
            val places      = if (days.isNotEmpty()) loadPlacesFor(days[activeIndex].dayId)  else emptyMap()

            _uiState.update {
                it.copy(
                    isLoading           = false,
                    days                = days,
                    activeDayIndex      = activeIndex,
                    flightsForActiveDay = flights,
                    lodging             = lodging,
                    carRental           = car,
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

    private suspend fun loadFlightsFor(dayId: String): List<ViewFlightItem> =
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            ViewFlightItem(id = f.id, flightNumber = f.flightNumber, pdfUri = f.pdfUri)
        }

    private suspend fun loadPlacesFor(dayId: String): Map<String, List<ViewPlaceItem>> {
        val all = repo.getPlacesForDay(dayId).map { p ->
            ViewPlaceItem(id = p.id, name = p.name, category = p.category, note = p.note, lat = p.lat, lng = p.lng)
        }
        return itinCategories.associateWith { cat -> all.filter { it.category == cat } }
    }
}
