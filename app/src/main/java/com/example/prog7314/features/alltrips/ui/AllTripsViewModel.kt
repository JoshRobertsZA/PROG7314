package com.example.prog7314.features.alltrips.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.prog7314.core.db.SessionManager
import com.example.prog7314.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class AllTripsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TripRepository(app)

    private val _uiState = MutableStateFlow(AllTripsUiState(isLoading = true))
    val uiState: StateFlow<AllTripsUiState> = _uiState

    init {
        loadTrips()
    }

    fun loadTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val accountId = SessionManager.accountId
            val today     = LocalDate.now()
            val entities  = repo.getTripsForAccount(accountId)

            // Sort: Ongoing first, then Upcoming, then Completed; within each group newest start date first
            val rows = entities
                .mapIndexed { index, entity -> entity.toRow(index, today) }
                .sortedWith(
                    compareBy<TripRow> { it.status.ordinal }
                        .thenByDescending { it.dates }
                )

            _uiState.update { it.copy(trips = rows, isLoading = false) }
        }
    }

    fun onFilterSelected(filter: TripFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
