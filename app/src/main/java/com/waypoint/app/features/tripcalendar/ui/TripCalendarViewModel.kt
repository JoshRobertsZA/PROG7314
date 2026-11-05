package com.waypoint.app.features.tripcalendar.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.waypoint.app.features.home.data.WikipediaCitySearch
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import com.waypoint.app.core.common.TripValidation

/** Loads a trip, lets the user change its details and choose which days to plan, and deletes it. */
class TripCalendarViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(app) {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val repo = TripRepository(app)
    private val itineraryRepo = ItineraryRepository(app)

    private val _uiState = MutableStateFlow(TripCalendarUiState())
    val uiState: StateFlow<TripCalendarUiState> = _uiState

    sealed interface ItineraryNavTarget {
        data class EditItinerary(val tripId: String, val selectedDates: String) : ItineraryNavTarget
        data class ViewItinerary(val tripId: String) : ItineraryNavTarget
    }

    private val _navTarget = MutableStateFlow<ItineraryNavTarget?>(null)
    val navTarget: StateFlow<ItineraryNavTarget?> = _navTarget

    private val shortFmt = DateTimeFormatter.ofPattern("MMM d")
    private val longFmt  = DateTimeFormatter.ofPattern("MMM d, yyyy")

    init { loadTrip() }

    fun loadTrip() {
        viewModelScope.launch {
            val trips = repo.getTripsForAccount(SessionManager.accountId)
            val trip  = trips.firstOrNull { it.id == tripId }
            if (trip == null) { _uiState.update { it.copy(isLoading = false) }; return@launch }

            val start = runCatching { LocalDate.parse(trip.startDate) }.getOrNull()
            val end   = runCatching { LocalDate.parse(trip.endDate) }.getOrNull()
            val nights = if (start != null && end != null)
                ChronoUnit.DAYS.between(start, end).toInt() else 0
            val rangeLabel = if (start != null && end != null)
                "${start.format(shortFmt)} - ${end.format(longFmt)}" else ""
            val dayCount = if (start != null && end != null) (ChronoUnit.DAYS.between(start, end) + 1).toInt() else 0

            val storedDays = itineraryRepo.getSelectedDaysWithIds(trip.id)
            val dayIds   = storedDays.map { d -> d.id }
            val selectedDayDates = storedDays.map { d -> d.date }.toSet()
            val flights  = itineraryRepo.getFlightsForDays(dayIds).size
            val stays    = itineraryRepo.getLodgingsForTrip(trip.id).size
            val rentals  = itineraryRepo.getCarRentalsForTrip(trip.id).size

            _uiState.update {
                it.copy(
                    isLoading      = false,
                    tripId         = trip.id,
                    tripName       = trip.name,
                    destination    = trip.destination,
                    destLat        = trip.destLat,
                    destLng        = trip.destLng,
                    destPhotoUrl   = trip.destPhotoUrl,
                    nightCount     = nights,
                    flightCount    = flights,
                    stayCount      = stays,
                    rentalCount    = rentals,
                    startDate      = start,
                    endDate        = end,
                    displayMonth   = start?.let { d -> YearMonth.from(d) } ?: YearMonth.now(),
                    dateRangeLabel = rangeLabel,
                    dayCount       = dayCount,
                    selectedDays   = selectedDayDates,
                )
            }
        }
    }


    fun onPrevMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.minusMonths(1)) } }
    fun onNextMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) } }


    fun onShowNameDialog() {
        _uiState.update { it.copy(showNameDialog = true, nameInput = it.tripName) }
    }

    fun onNameInputChanged(value: String) {
        // Same max length as when the trip is created
        _uiState.update { it.copy(nameInput = value.take(TripValidation.TRIP_NAME_MAX)) }
    }

    fun onDismissNameDialog() {
        _uiState.update { it.copy(showNameDialog = false) }
    }

    fun onConfirmNameEdit() {
        val state = _uiState.value
        val newName = state.nameInput.trim()
        // Don't save a blank or too-long name
        if (!TripValidation.isTripNameValid(newName)) return
        _uiState.update { it.copy(showNameDialog = false, tripName = newName) }
        viewModelScope.launch {
            repo.updateTripDetails(
                id          = tripId,
                name        = newName,
                destination = state.destination,
                destLat     = state.destLat,
                destLng     = state.destLng,
                destPhotoUrl = state.destPhotoUrl,
            )
            // repo.updateTripDetails() above already enqueues the update in
            // the sync outbox - no fire-and-forget network call needed here.
        }
    }


    fun onShowDestSearch() { _uiState.update { it.copy(showDestSearch = true) } }
    fun onDismissDestSearch() { _uiState.update { it.copy(showDestSearch = false) } }


    fun onDayToggled(date: LocalDate) {
        val state = _uiState.value
        val start = state.startDate ?: return
        val end   = state.endDate   ?: return
        if (date.isBefore(start) || date.isAfter(end)) return
        val current = state.selectedDays
        _uiState.update {
            it.copy(selectedDays = if (date in current) current - date else current + date)
        }
    }


    fun onEditItineraryClick() {
        val state = _uiState.value
        if (state.selectedDays.isEmpty()) {
            _uiState.update { it.copy(showNoDaysError = true) }
            return
        }
        viewModelScope.launch {
            itineraryRepo.replaceSelectedDays(state.tripId, state.selectedDays)
            val selectedDates = state.selectedDays.joinToString("|") { it.toString() }
            _navTarget.value = ItineraryNavTarget.EditItinerary(state.tripId, selectedDates)
        }
    }

    fun onViewItineraryClick() {
        val state = _uiState.value
        if (state.selectedDays.isEmpty()) {
            _uiState.update { it.copy(showNoDaysError = true) }
            return
        }
        viewModelScope.launch {
            itineraryRepo.replaceSelectedDays(state.tripId, state.selectedDays)
            _navTarget.value = ItineraryNavTarget.ViewItinerary(state.tripId)
        }
    }


    fun onDeleteClick()   { _uiState.update { it.copy(showDeleteConfirm = true) } }
    fun onDeleteDismiss() { _uiState.update { it.copy(showDeleteConfirm = false) } }

    fun onDeleteConfirm() {
        viewModelScope.launch {
            repo.deleteTrip(tripId, SessionManager.accountId)
            // repo.deleteTrip() above already enqueues the delete in the
            // sync outbox - no fire-and-forget network call needed here.
            _uiState.update { it.copy(showDeleteConfirm = false, deleted = true) }
        }
    }


    fun onEditDatesClick() {
        _uiState.update {
            it.copy(showEditDates = true, editStart = null, editEnd = null,
                    editMonth = it.startDate?.let { d -> YearMonth.from(d) } ?: YearMonth.now())
        }
    }
    fun onEditDatesDismiss() { _uiState.update { it.copy(showEditDates = false) } }
    fun onEditPrevMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.minusMonths(1)) } }
    fun onEditNextMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.plusMonths(1)) } }

    fun onEditDayTapped(date: LocalDate) {
        _uiState.update { s ->
            when {
                s.editStart == null || s.editEnd != null -> s.copy(editStart = date, editEnd = null)
                date < s.editStart                       -> s.copy(editStart = date, editEnd = s.editStart)
                else                                     -> s.copy(editEnd = date)
            }
        }
    }

    fun onEditDatesSave() {
        val s = _uiState.value
        val start = s.editStart ?: return
        val end   = s.editEnd ?: start
        viewModelScope.launch {
            repo.updateTripDates(tripId, start.toString(), end.toString())
            // repo.updateTripDates() above already enqueues the update in
            // the sync outbox - no fire-and-forget network call needed here.
            itineraryRepo.removeDaysOutside(tripId, start, end)
            _uiState.update { it.copy(showEditDates = false) }
            loadTrip()
        }
    }

    fun onNavConsumed() { _navTarget.value = null }

    fun onNoDaysErrorShown() { _uiState.update { it.copy(showNoDaysError = false) } }

    fun onDestinationSelected(name: String) {
        _uiState.update { it.copy(showDestSearch = false, destination = name, isGeocodingDest = true) }
        viewModelScope.launch {
            val coords = LocationIQRepository.geocodeCity(name)
            val photo  = WikipediaCitySearch.thumbnailUrl(name)
            val state  = _uiState.value
            _uiState.update {
                it.copy(
                    isGeocodingDest = false,
                    destLat         = coords?.first,
                    destLng         = coords?.second,
                    destPhotoUrl    = photo,
                )
            }
            repo.updateTripDetails(
                id          = tripId,
                name        = state.tripName,
                destination = name,
                destLat     = coords?.first,
                destLng     = coords?.second,
                destPhotoUrl = photo,
            )
            // repo.updateTripDetails() above already enqueues the update in
            // the sync outbox - no fire-and-forget network call needed here.
        }
    }
}
