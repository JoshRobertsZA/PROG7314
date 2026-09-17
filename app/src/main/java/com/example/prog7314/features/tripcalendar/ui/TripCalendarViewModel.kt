package com.example.prog7314.features.tripcalendar.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
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

    private val shortFmt  = DateTimeFormatter.ofPattern("MMM d")
    private val longFmt   = DateTimeFormatter.ofPattern("MMM d, yyyy")

    init {
        loadTrip()
    }

    private fun loadTrip() {
        viewModelScope.launch {
            // TripRepository.getTripsForAccount returns all trips; we pick ours by id.
            // A dedicated getTrip(id) can replace this once the repository grows one.
            val trips = repo.getTripsForAccount(
                com.example.prog7314.core.db.SessionManager.accountId
            )
            val trip = trips.firstOrNull { it.id == tripId }
            if (trip == null) {
                _uiState.update { it.copy(isLoading = false) }
                return@launch
            }

            val start = runCatching { LocalDate.parse(trip.startDate) }.getOrNull()
            val end   = runCatching { LocalDate.parse(trip.endDate) }.getOrNull()

            val rangeLabel = if (start != null && end != null) {
                "${start.format(shortFmt)} - ${end.format(longFmt)}"
            } else ""

            val dayCount = if (start != null && end != null) {
                val n = ChronoUnit.DAYS.between(start, end) + 1
                "$n ${if (n == 1L) "day" else "days"}"
            } else ""

            _uiState.update {
                it.copy(
                    isLoading      = false,
                    tripName       = trip.name,
                    startDate      = start,
                    endDate        = end,
                    displayMonth   = start?.let { d -> YearMonth.from(d) } ?: YearMonth.now(),
                    dateRangeLabel = rangeLabel,
                    dayCountLabel  = dayCount,
                )
            }
        }
    }

    fun onPrevMonth() {
        _uiState.update { it.copy(displayMonth = it.displayMonth.minusMonths(1)) }
    }

    fun onNextMonth() {
        _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) }
    }
}
