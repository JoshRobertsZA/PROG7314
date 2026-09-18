package com.waypoint.app.features.settings.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Backs the trip-count stat cards on the Profile tab. Mirrors the
 * AllTripsViewModel pattern: one repository call, counts derived in memory.
 *
 * Trip start dates are stored as "yyyy-MM-dd" strings (see TripEntity), so
 * the year is the first four characters - no date parsing needed.
 */
class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TripRepository(app)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        loadTripCounts()
    }

    fun loadTripCounts() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            val trips       = repo.getTripsForAccount(SessionManager.accountId)
            val currentYear = LocalDate.now().year.toString()

            _uiState.update {
                it.copy(
                    isLoading       = false,
                    plannedThisYear = trips.count { trip -> trip.startDate.startsWith(currentYear) },
                    totalTrips      = trips.size,
                )
            }
        }
    }
}
