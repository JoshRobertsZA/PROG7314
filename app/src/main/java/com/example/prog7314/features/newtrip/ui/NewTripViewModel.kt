package com.example.prog7314.features.newtrip.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7314.core.db.SessionManager
import com.example.prog7314.features.newtrip.data.TripRepository
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

class NewTripViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = TripRepository(application)

    private val _uiState = MutableStateFlow(NewTripUiState())
    val uiState: StateFlow<NewTripUiState> = _uiState.asStateFlow()

    /**
     * Emits the saved trip's UUID when [saveTrip] succeeds.
     * The screen collects this and navigates away.
     */
    private val _tripSaved = MutableSharedFlow<String>(extraBufferCapacity = 1)
    val tripSaved: SharedFlow<String> = _tripSaved.asSharedFlow()

    // ── Trip name ─────────────────────────────────────────────────────────────

    fun onTripNameChanged(name: String) {
        _uiState.update { it.copy(tripName = name) }
    }

    // ── Month navigation ──────────────────────────────────────────────────────

    /** Move forward one month (always allowed). */
    fun onNextMonth() {
        _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) }
    }

    /**
     * Move back one month — blocked if already on the current month,
     * because we never show past months.
     */
    fun onPrevMonth() {
        val current = YearMonth.now()
        _uiState.update { state ->
            if (state.displayMonth > current)
                state.copy(displayMonth = state.displayMonth.minusMonths(1))
            else state
        }
    }

    // ── Year/month picker overlay ─────────────────────────────────────────────

    fun onShowYearPicker() {
        _uiState.update { it.copy(showYearPicker = true) }
    }

    fun onDismissYearPicker() {
        _uiState.update { it.copy(showYearPicker = false) }
    }

    /** Called when the user picks a month from the year-picker overlay. */
    fun onYearMonthPicked(yearMonth: YearMonth) {
        _uiState.update { it.copy(displayMonth = yearMonth, showYearPicker = false) }
    }

    // ── Day tap logic ─────────────────────────────────────────────────────────

    /**
     * Handle a tap on [tapped].
     *
     * Rules (matches the spec):
     *
     * - Nothing selected yet          → set startDate, clear endDate
     * - Only startDate set            → set endDate (swap dates if tapped < startDate)
     * - Full range, tap on a boundary → reset: startDate = tapped, endDate = null
     * - Full range, tap before start  → extend: startDate = tapped
     * - Full range, tap after end     → extend: endDate   = tapped
     * - Full range, tap inside        → move start forward: startDate = tapped
     */
    fun onDayTapped(tapped: LocalDate) {
        _uiState.update { state ->
            val start = state.startDate
            val end   = state.endDate

            when {
                // Nothing selected yet
                start == null -> state.copy(startDate = tapped, endDate = null)

                // One date — second tap completes the range
                end == null -> {
                    if (tapped == start) {
                        // Tap same day as start: reset
                        state.copy(startDate = null, endDate = null)
                    } else if (tapped < start) {
                        state.copy(startDate = tapped, endDate = start)
                    } else {
                        state.copy(endDate = tapped)
                    }
                }

                // Full range — boundary taps reset
                tapped == start || tapped == end ->
                    state.copy(startDate = tapped, endDate = null)

                // Full range — extend or move start
                tapped < start -> state.copy(startDate = tapped)
                tapped > end   -> state.copy(endDate = tapped)
                else           -> state.copy(startDate = tapped) // inside range: move start
            }
        }
    }

    // ── Save ──────────────────────────────────────────────────────────────────

    fun saveTrip() {
        val state = _uiState.value
        if (!state.canSave) return

        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true) }
            val id = repository.insertTrip(
                accountId   = SessionManager.accountId,
                name        = state.tripName.trim(),
                startDate   = state.startDate!!.toString(),   // "yyyy-MM-dd"
                endDate     = state.endDate!!.toString(),
            )
            _uiState.update { it.copy(isSaving = false) }
            _tripSaved.emit(id)
        }
    }
}
