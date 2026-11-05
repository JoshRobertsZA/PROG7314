package com.waypoint.app.features.settings.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.GuardedApi
import com.waypoint.app.core.network.UsageGuardian
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.core.settings.AppSettings
import com.waypoint.app.core.sync.OutboxRepository
import com.waypoint.app.core.sync.SyncScheduler
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = TripRepository(app)
    private val outbox = OutboxRepository(app)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState.asStateFlow()

    init {
        loadTripCounts()
        loadApiUsage()
        viewModelScope.launch {
            SyncScheduler.isSyncing(app).collect { syncing -> _uiState.update { it.copy(isSyncing = syncing) } }
        }
        viewModelScope.launch {
            AppSettings.state.collect { s -> _uiState.update { it.copy(lastSyncAtMs = s.lastSyncAtMs) } }
        }
        viewModelScope.launch {
            // Keeps "N changes waiting" and the trip counts current as the sync drains the outbox.
            OutboxRepository.changes.collect { loadTripCounts() }
        }
    }

    fun loadTripCounts() {
        viewModelScope.launch {
            val trips = repo.getTripsForAccount(SessionManager.accountId)
            val currentYear = LocalDate.now().year.toString()
            val pending = outbox.count()
            _uiState.update {
                it.copy(
                    isLoading = false,
                    plannedThisYear = trips.count { trip -> trip.startDate.startsWith(currentYear) },
                    totalTrips = trips.size,
                    pendingChangeCount = pending,
                )
            }
        }
    }

    /** Reads how much of each guarded API's quota is used, for the budget meters. */
    fun loadApiUsage() {
        viewModelScope.launch {
            RemoteSecrets.ensureLoaded()
            val snapshots = GuardedApi.values().mapNotNull { UsageGuardian.readCounts(it) }
            _uiState.update { it.copy(apiUsage = snapshots) }
        }
    }

    fun syncNow() {
        Log.i("SettingsViewModel", "Sync now requested")
        SyncScheduler.enqueue(getApplication())
    }
}
