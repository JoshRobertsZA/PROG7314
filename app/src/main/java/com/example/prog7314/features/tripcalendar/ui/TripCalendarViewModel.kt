package com.example.prog7314.features.tripcalendar.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.example.prog7314.core.db.SessionManager
import com.example.prog7314.features.explore.data.LocationIQRepository
import com.example.prog7314.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class TripCalendarViewModel(
    app: Application,
    savedStateHandle: SavedStateHandle,
) : AndroidViewModel(app) {

    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    private val repo = TripRepository(app)

    private val _uiState = MutableStateFlow(TripCalendarUiState())
    val uiState: StateFlow<TripCalendarUiState> = _uiState

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

            _uiState.update {
                it.copy(
                    isLoading      = false,
                    tripId         = trip.id,
                    tripName       = trip.name,
                    destination    = trip.destination,
                    destLat        = trip.destLat,
                    destLng        = trip.destLng,
                    nightCount     = nights,
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
            )
        }
    }

    // ── Destination editing ───────────────────────────────────────────────────

    fun onShowDestSearch() { _uiState.update { it.copy(showDestSearch = true) } }
    fun onDismissDestSearch() { _uiState.update { it.copy(showDestSearch = false) } }

    fun onDestinationSelected(name: String) {
        _uiState.update { it.copy(showDestSearch = false, destination = name, isGeocodingDest = true) }
        viewModelScope.launch {
            val coords = LocationIQRepository.geocodeCity(name)
            val state  = _uiState.value
            _uiState.update {
                it.copy(
                    isGeocodingDest = false,
                    destLat         = coords?.first,
                    destLng         = coords?.second,
                )
            }
            repo.updateTripDetails(
                id          = tripId,
                name        = state.tripName,
                destination = name,
                destLat     = coords?.first,
                destLng     = coords?.second,
            )
        }
    }
}
