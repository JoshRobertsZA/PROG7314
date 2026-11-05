package com.waypoint.app.features.alltrips.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.sync.OutboxEntityType
import com.waypoint.app.core.sync.OutboxRepository
import com.waypoint.app.core.sync.SyncScheduler
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

/**
 * Loads the user's trips from SQLite, marks those waiting to sync, and supports pull-to-refresh from
 * the API.
 */
class AllTripsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TripRepository(app)
    private val outbox = OutboxRepository(app)

    private val _uiState = MutableStateFlow(AllTripsUiState(isLoading = true))
    val uiState: StateFlow<AllTripsUiState> = _uiState

    init {
        loadTrips()
        // Refresh the "waiting to sync" icons as changes are queued and uploaded.
        viewModelScope.launch { OutboxRepository.changes.collect { loadTrips() } }
    }

    fun loadTrips() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val accountId = SessionManager.accountId
            val today     = LocalDate.now()
            val entities  = repo.getTripsForAccount(accountId)
            val pendingIds = outbox.pendingEntityIds(OutboxEntityType.TRIP)

            val rows = entities
                .mapIndexed { index, entity -> entity.toRow(index, today).copy(isPendingSync = entity.id in pendingIds) }
                .sortedWith(
                    compareBy<TripRow> { it.status.ordinal }
                        .thenByDescending { it.dates }
                )

            _uiState.update { it.copy(trips = rows, isLoading = false) }
        }
    }

    // Pull-to-refresh: asks the cloud for anything new before re-reading
    // local SQLite, rather than just re-reading what's already there
    // (which is all loadTrips() does on its own). Also drains the outbox,
    // since a user who pulls to refresh almost certainly has a network
    // connection right now - as good a moment as any to push pending
    // changes too, not just pull new ones.
    fun refreshFromCloud() {
        viewModelScope.launch {
            val token = AuthRepository.getIdToken()
            val accountId = SessionManager.accountId
            if (token != null) repo.pullAndMergeTrips(token, accountId)
            SyncScheduler.enqueue(getApplication())
            loadTrips()
        }
    }

    fun onFilterSelected(filter: TripFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }
}
