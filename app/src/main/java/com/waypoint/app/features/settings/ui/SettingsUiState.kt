package com.waypoint.app.features.settings.ui

import com.waypoint.app.core.network.UsageSnapshot

/** State of the Profile tab. */
data class SettingsUiState(
    val isLoading: Boolean = true,
    val plannedThisYear: Int = 0,
    val totalTrips: Int = 0,
    /** Local changes still waiting in the outbox to reach the API. */
    val pendingChangeCount: Int = 0,
    val apiUsage: List<UsageSnapshot> = emptyList(),
    /** True while SyncWorker is running. */
    val isSyncing: Boolean = false,
    /** When the last two-way sync finished; null if this phone has never synced. */
    val lastSyncAtMs: Long? = null,
)
