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
        data class EditItinerary(val tripId: String) : ItineraryNavTarget
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
            val dayCount = if (start != null && end != null) {
                val n = ChronoUnit.DAYS.between(start, end) + 1
                "$n ${if (n == 1L) "day" else "days"}"
            } else ""

            // Overview counts: flights across every selected day, and every
            // uploaded lodging / car-rental document.
            val dayIds   = itineraryRepo.getSelectedDaysWithIds(trip.id).map { d -> d.id }
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
                    dayCountLabel  = dayCount,
                )
            }
        }
    }

    // ── Month navigation ──────────────────────────────────────────────────────

    fun onPrevMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.minusMonths(1)) } }
    fun onNextMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) } }

    // ── Trip name editing ─────────────────────────────────────────────────────

    fun onShowNameDialog() {
        _uiState.update { it.copy(showNameDialog = true, nameInput = it.tripName) }
    }

    fun onNameInputChanged(value: String) {
        _uiState.update { it.copy(nameInput = value) }
    }

    fun onDismissNameDialog() {
        _uiState.update { it.copy(showNameDialog = false) }
    }

    fun onConfirmNameEdit() {
        val state = _uiState.value
        val newName = state.nameInput.trim().ifBlank { return }
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
        }
    }

    // ── Destination editing ───────────────────────────────────────────────────

    fun onShowDestSearch() { _uiState.update { it.copy(showDestSearch = true) } }
    fun onDismissDestSearch() { _uiState.update { it.copy(showDestSearch = false) } }

    // ── Day selection ────────────────────────────────────────────────────────────

    fun onDayToggled(date: LocalDate) {
        val state = _uiState.value
        val start = state.startDate ?: return
        val end   = state.endDate   ?: return
        if (date.isBefore(start) || date.isAfter(end)) return  // only in-range days
        val current = state.selectedDays
        _uiState.update {
            it.copy(selectedDays = if (date in current) current - date else current + date)
        }
    }

    // ── Itinerary navigation ──────────────────────────────────────────────────

    /** Persists the current day selection, then signals navigation to Edit Itinerary.
     *  Shows an error snackbar and does nothing if no days are selected. */
    fun onEditItineraryClick() {
        val state = _uiState.value
        if (state.selectedDays.isEmpty()) {
            _uiState.update { it.copy(showNoDaysError = true) }
            return
        }
        viewModelScope.launch {
            itineraryRepo.replaceSelectedDays(state.tripId, state.selectedDays)
            _navTarget.value = ItineraryNavTarget.EditItinerary(state.tripId)
        }
    }

    /** Persists the current day selection, then signals navigation to View Itinerary.
     *  Shows an error snackbar and does nothing if no days are selected. */
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

    // ── Delete trip ───────────────────────────────────────────────────────────

    fun onDeleteClick()   { _uiState.update { it.copy(showDeleteConfirm = true) } }
    fun onDeleteDismiss() { _uiState.update { it.copy(showDeleteConfirm = false) } }

    fun onDeleteConfirm() {
        viewModelScope.launch {
            repo.deleteTrip(tripId, SessionManager.accountId)
            _uiState.update { it.copy(showDeleteConfirm = false, deleted = true) }
        }
    }

    // ── Edit dates ────────────────────────────────────────────────────────────

    fun onEditDatesClick() {
        _uiState.update {
            it.copy(showEditDates = true, editStart = null, editEnd = null,
                    editMonth = it.startDate?.let { d -> YearMonth.from(d) } ?: YearMonth.now())
        }
    }
    fun onEditDatesDismiss() { _uiState.update { it.copy(showEditDates = false) } }
    fun onEditPrevMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.minusMonths(1)) } }
    fun onEditNextMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.plusMonths(1)) } }

    /** First tap = start, second = end (swapped if earlier), third starts over. */
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
            itineraryRepo.removeDaysOutside(tripId, start, end)
            _uiState.update { it.copy(showEditDates = false) }
            loadTrip()
        }
    }

    /** Called by the screen after it has acted on [navTarget] to clear the event. */
    fun onNavConsumed() { _navTarget.value = null }

    /** Called by the screen after the "no days selected" snackbar has been shown. */
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
        }
    }
}
