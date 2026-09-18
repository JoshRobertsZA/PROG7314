package com.waypoint.app.features.edititinerary.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.common.DocumentNames
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class EditItineraryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    val tripId: String = checkNotNull(savedStateHandle["tripId"])

    private val repo = ItineraryRepository(application)

    private val _uiState = MutableStateFlow(EditItineraryUiState())
    val uiState: StateFlow<EditItineraryUiState> = _uiState.asStateFlow()

    /** The Room ID of the currently displayed day, or null when days haven't loaded yet. */
    val activeDayId: String?
        get() = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.dayId

    init {
        loadAll()
    }

    // ── Load ──────────────────────────────────────────────────────────────────

    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val dayEntities = repo.getSelectedDaysWithIds(tripId)
            val days        = dayEntities.map { e -> DayItem(dayId = e.id, date = e.date) }
            val lodgings    = loadLodgings()
            val cars        = loadCarRentals()

            val activeIndex = 0
            val flights     = if (days.isNotEmpty()) loadFlightsFor(days[activeIndex].dayId) else emptyList()

            val places = if (days.isNotEmpty()) loadPlacesFor(days[activeIndex].dayId) else emptyMap()

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

    private suspend fun loadLodgings(): List<LodgingItem> =
        repo.getLodgingsForTrip(tripId).map { l -> LodgingItem(l.id, l.fromDate, l.toDate, l.pdfUri, l.docName) }

    private suspend fun loadCarRentals(): List<CarRentalItem> =
        repo.getCarRentalsForTrip(tripId).map { c -> CarRentalItem(c.id, c.fromDate, c.toDate, c.pdfUri, c.docName) }

    private suspend fun loadFlightsFor(dayId: String): List<FlightItem> =
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            FlightItem(
                id           = f.id,
                dayId        = f.dayId,
                flightNumber = f.flightNumber.orEmpty(),
                pdfUri       = f.pdfUri,
                departureTime = f.departureTime,
                docName      = f.docName,
            )
        }

    private suspend fun loadPlacesFor(dayId: String): Map<String, List<PlaceItem>> {
        val itinCategories = listOf("RESTAURANTS", "HOTELS", "PARKS", "PUBS", "CINEMAS")
        val all = repo.getPlacesForDay(dayId).map { p ->
            PlaceItem(
                id       = p.id,
                dayId    = p.dayId,
                name     = p.name,
                category = p.category,
                note     = p.note,
            )
        }
        return itinCategories.associateWith { cat ->
            all.filter { it.category == cat }
        }
    }

    // ── Day scroller ──────────────────────────────────────────────────────────

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

    // ── PDF picker trigger ────────────────────────────────────────────────────

    fun onUploadFlightClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.FLIGHT) }

    fun onUploadLodgingClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.LODGING) }

    fun onUploadCarRentalClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.CAR_RENTAL) }

    fun onPickerDismissed() =
        _uiState.update { it.copy(pendingUploadType = null) }

    // ── PDF picked ────────────────────────────────────────────────────────────

    fun onFlightPdfPicked(pdfUri: String) {
        val activeDayId = _uiState.value.days
            .getOrNull(_uiState.value.activeDayIndex)
            ?.dayId ?: return
        viewModelScope.launch {
            repo.insertFlight(dayId = activeDayId, pdfUri = pdfUri, docName = DocumentNames.displayName(getApplication(), pdfUri))
            val flights = loadFlightsFor(activeDayId)
            _uiState.update {
                it.copy(
                    pendingUploadType   = null,
                    flightsForActiveDay = flights,
                )
            }
        }
    }

    fun onLodgingPdfPicked(pdfUri: String) {
        val days = _uiState.value.days
        if (days.isEmpty()) return
        val from = days.first().date
        val to   = days.last().date
        // Covers the currently selected days only; other days keep their own docs.
        viewModelScope.launch {
            repo.insertLodging(tripId, from, to, pdfUri, DocumentNames.displayName(getApplication(), pdfUri))
            _uiState.update { it.copy(pendingUploadType = null, lodgings = loadLodgings()) }
        }
    }

    fun onCarRentalPdfPicked(pdfUri: String) {
        val days = _uiState.value.days
        if (days.isEmpty()) return
        val from = days.first().date
        val to   = days.last().date
        viewModelScope.launch {
            repo.insertCarRental(tripId, from, to, pdfUri, DocumentNames.displayName(getApplication(), pdfUri))
            _uiState.update { it.copy(pendingUploadType = null, carRentals = loadCarRentals()) }
        }
    }

    // ── Delete ────────────────────────────────────────────────────────────────

    fun onDeleteFlight(flightId: String) {
        val activeDayId = _uiState.value.days
            .getOrNull(_uiState.value.activeDayIndex)
            ?.dayId ?: return
        viewModelScope.launch {
            repo.deleteFlight(flightId)
            val flights = loadFlightsFor(activeDayId)
            _uiState.update { it.copy(flightsForActiveDay = flights) }
        }
    }

    fun onDeleteLodging(lodgingId: String) {
        viewModelScope.launch {
            repo.deleteLodging(lodgingId)
            _uiState.update { it.copy(lodgings = loadLodgings()) }
        }
    }

    fun onDeleteCarRental(carRentalId: String) {
        viewModelScope.launch {
            repo.deleteCarRental(carRentalId)
            _uiState.update { it.copy(carRentals = loadCarRentals()) }
        }
    }

    // ── Flight number edit ────────────────────────────────────────────────────

    fun onFlightNumberChanged(flightId: String, number: String) {
        // Update DB in background; also update UI state immediately
        _uiState.update { state ->
            state.copy(
                flightsForActiveDay = state.flightsForActiveDay.map { f ->
                    if (f.id == flightId) f.copy(flightNumber = number) else f
                }
            )
        }
        viewModelScope.launch { repo.updateFlightNumber(flightId, number) }
    }

    fun onFlightDepartureTimeChanged(flightId: String, time: String?) {
        _uiState.update { state ->
            state.copy(
                flightsForActiveDay = state.flightsForActiveDay.map { f ->
                    if (f.id == flightId) f.copy(departureTime = time) else f
                }
            )
        }
        viewModelScope.launch { repo.updateFlightDepartureTime(flightId, time) }
    }

    // ── Places ────────────────────────────────────────────────────────────────

    fun onDeletePlace(placeId: String) {
        val activeDayId = _uiState.value.days
            .getOrNull(_uiState.value.activeDayIndex)
            ?.dayId ?: return
        viewModelScope.launch {
            repo.deletePlace(placeId)
            val places = loadPlacesFor(activeDayId)
            _uiState.update { it.copy(placesForActiveDay = places) }
        }
    }

    // ── Refresh (called when the screen re-enters composition after add-place flow) ──

    fun refreshPlacesForActiveDay() {
        val dayId = activeDayId ?: return
        viewModelScope.launch {
            val places = loadPlacesFor(dayId)
            _uiState.update { it.copy(placesForActiveDay = places) }
        }
    }
}
