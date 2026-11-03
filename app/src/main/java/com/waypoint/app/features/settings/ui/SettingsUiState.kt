// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

// imports `com.waypoint.app.core.network.UsageSnapshot` for use in this file
import com.waypoint.app.core.network.UsageSnapshot

// expression: `data class SettingsUiState(`
data class SettingsUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val plannedThisYear: Int = 0,`
    val plannedThisYear: Int = 0,
    // continues the statement started above: `val totalTrips: Int = 0,`
    val totalTrips: Int = 0,
    val pendingChangeCount: Int = 0,
    // usage of each guarded API; stays empty when CounterAPI is not configured or cannot be read
    val apiUsage: List<UsageSnapshot> = emptyList(),
// closes the multi-line argument list started above
)
