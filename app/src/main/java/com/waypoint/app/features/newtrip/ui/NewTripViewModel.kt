package com.waypoint.app.features.newtrip.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.features.explore.data.LocationIQRepository
import com.waypoint.app.features.home.data.WikipediaCitySearch
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth
import com.waypoint.app.core.common.TripValidation


/** Validates the new trip's name and dates and saves it. */
class NewTripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TripRepository(application)

    private val _uiState = MutableStateFlow(NewTripUiState())
    val uiState: StateFlow<NewTripUiState> = _uiState.asStateFlow()

    private val _tripSaved = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val tripSaved: SharedFlow<String> = _tripSaved.asSharedFlow()


    fun onTripNameChanged(name: String) {
        // Cap the name at the max length so it can't go over the limit
        _uiState.update { it.copy(tripName = name.take(TripValidation.TRIP_NAME_MAX)) }
    }


    fun onNextMonth() {
        _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) }
    }

    fun onPrevMonth() {
        val current = YearMonth.now()
        _uiState.update { state ->
            if (state.displayMonth > current)
                state.copy(displayMonth = state.displayMonth.minusMonths(1))
            else state
        }
    }


    fun onShowYearPicker() {
        _uiState.update { it.copy(showYearPicker = true) }
    }

    fun onDismissYearPicker() {
        _uiState.update { it.copy(showYearPicker = false) }
    }

    fun onYearMonthPicked(yearMonth: YearMonth) {
        _uiState.update { it.copy(displayMonth = yearMonth, showYearPicker = false) }
    }


    fun onDayTapped(tapped: LocalDate) {
        _uiState.update { state ->
            val start = state.startDate
            val end   = state.endDate

            when {
                start == null -> state.copy(startDate = tapped, endDate = null)

                end == null -> {
                    if (tapped == start) {
                        state.copy(startDate = null, endDate = null)
                    } else if (tapped < start) {
                        state.copy(startDate = tapped, endDate = start)
                    } else {
                        state.copy(endDate = tapped)
                    }
                }

                tapped == start || tapped == end ->
                    state.copy(startDate = tapped, endDate = null)

                tapped < start -> state.copy(startDate = tapped)
                tapped > end   -> state.copy(endDate = tapped)
                else           -> state.copy(startDate = tapped)
            }
        }
    }


    fun onShowDestSearch() {
        _uiState.update { it.copy(showDestSearch = true) }
    }

    fun onDismissDestSearch() {
        _uiState.update { it.copy(showDestSearch = false) }
    }

    fun onDestinationSelected(name: String) {
        _uiState.update { it.copy(showDestSearch = false, destinationName = name, isGeocodingDest = true) }
        viewModelScope.launch {
            val coords = LocationIQRepository.geocodeCity(name)
            val photo  = WikipediaCitySearch.thumbnailUrl(name)
            _uiState.update {
                it.copy(
                    isGeocodingDest = false,
                    destLat = coords?.first,
                    destLng = coords?.second,
                    destPhotoUrl = photo,
                )
            }
        }
    }


    fun saveTrip() {
        val state = _uiState.value
        if (!state.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val id = repository.insertTrip(
                accountId   = SessionManager.accountId,
                name        = state.tripName.trim(),
                startDate   = state.startDate!!.toString(),
                endDate     = state.endDate!!.toString(),
                destination = state.destinationName.ifBlank { null },
                destLat     = state.destLat,
                destLng     = state.destLng,
                destPhotoUrl = state.destPhotoUrl,
            )
            _uiState.update { it.copy(isSaving = false) }
            _tripSaved.emit(id)
            // repository.insertTrip() above already enqueues the create in
            // the sync outbox - no fire-and-forget network call needed here
            // (and making one too would double-send once the outbox worker
            // also sends it).
        }
    }
}
