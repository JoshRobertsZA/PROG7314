package com.waypoint.app.features.edititinerary.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.network.AirLabsRepository
import com.waypoint.app.core.common.DocumentNames
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate
import com.waypoint.app.core.common.TripValidation

/**
 * Loads and edits one trip's itinerary, and looks up a flight's departure time on AirLabs as the user
 * types its number.
 */
class EditItineraryViewModel(
    application: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(application) {

    val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val selectedDates: Set<String> = (savedStateHandle.get<String>("selectedDates") ?: "")
        .split("|").filter { it.isNotBlank() }.toSet()

    private val repo = ItineraryRepository(application)
    private val depTimeLookupJobs = HashMap<String, Job>()

    private val _uiState = MutableStateFlow(EditItineraryUiState())
    val uiState: StateFlow<EditItineraryUiState> = _uiState.asStateFlow()

    val activeDayId: String?
        get() = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.dayId

    init {
        loadAll()
    }


    private fun loadAll() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val allDayEntities = repo.getSelectedDaysWithIds(tripId)
            val dayEntities = if (selectedDates.isEmpty()) allDayEntities
                              else allDayEntities.filter { it.date.toString() in selectedDates }
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
                departureTime = f.departureTime?.takeIf { it != AirLabsRepository.AIRLABS_MISS },
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
                photoUrl = p.photoUrl,
            )
        }
        return itinCategories.associateWith { cat ->
            all.filter { it.category == cat }
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


    fun onUploadFlightClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.FLIGHT) }

    fun onUploadLodgingClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.LODGING) }

    fun onUploadCarRentalClick() =
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.CAR_RENTAL) }

    fun onPickerDismissed() =
        _uiState.update { it.copy(pendingUploadType = null) }


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

    /** Returns false (and saves nothing) if the range is out of order or outside the itinerary. */
    fun onUpdateLodgingDates(lodgingId: String, from: LocalDate, to: LocalDate): Boolean {
        if (!TripValidation.isBookingRangeValid(from, to, _uiState.value.days.map { it.date })) return false
        viewModelScope.launch {
            repo.updateLodgingDates(lodgingId, from, to)
            _uiState.update { it.copy(lodgings = loadLodgings()) }
        }
        return true
    }

    /** Returns false (and saves nothing) if the range is out of order or outside the itinerary. */
    fun onUpdateCarRentalDates(carRentalId: String, from: LocalDate, to: LocalDate): Boolean {
        if (!TripValidation.isBookingRangeValid(from, to, _uiState.value.days.map { it.date })) return false
        viewModelScope.launch {
            repo.updateCarRentalDates(carRentalId, from, to)
            _uiState.update { it.copy(carRentals = loadCarRentals()) }
        }
        return true
    }

    /** Returns false (and saves nothing) if the note is too long. */
    fun onUpdatePlaceNote(placeId: String, note: String): Boolean {
        if (!TripValidation.isPlaceNoteValid(note)) return false
        val activeDayId = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.dayId ?: return false
        viewModelScope.launch {
            repo.updatePlaceNote(placeId, TripValidation.normalisePlaceNote(note))
            _uiState.update { it.copy(placesForActiveDay = loadPlacesFor(activeDayId)) }
        }
        return true
    }


    fun onFlightNumberChanged(flightId: String, number: String) {
        _uiState.update { state ->
            state.copy(
                flightsForActiveDay = state.flightsForActiveDay.map { f ->
                    if (f.id == flightId) f.copy(flightNumber = number) else f
                }
            )
        }
        // Only save once the number is empty or valid, so half-typed values aren't stored
        if (number.isBlank() || TripValidation.isFlightNumberValid(number)) {
            viewModelScope.launch { repo.updateFlightNumber(flightId, number.trim()) }
        }
        depTimeLookupJobs[flightId]?.cancel()
        val iata = TripValidation.normaliseFlightNumber(number)
        val flightDate = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.date?.toString()
        // Only look up the departure time for a valid flight number
        if (TripValidation.isFlightNumberValid(iata)) {
            depTimeLookupJobs[flightId] = viewModelScope.launch {
                delay(1_500)
                val time = AirLabsRepository.lookupDepartureTime(iata, flightDate)
                if (time != null) {
                    repo.updateFlightDepartureTime(flightId, time)
                    _uiState.update { state ->
                        state.copy(
                            flightsForActiveDay = state.flightsForActiveDay.map { f ->
                                if (f.id == flightId) f.copy(departureTime = time) else f
                            }
                        )
                    }
                }
            }
        }
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


    fun refreshPlacesForActiveDay() {
        val dayId = activeDayId ?: return
        viewModelScope.launch {
            val places = loadPlacesFor(dayId)
            _uiState.update { it.copy(placesForActiveDay = places) }
        }
    }
}
