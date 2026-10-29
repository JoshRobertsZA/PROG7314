// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

// Outcome of tapping "send test notification" - drives what the row's
// trailing text shows. Idle before/after a result has been shown for a
// while isn't distinguished from NeverSent; both just show nothing.
enum class TestNotificationState { IDLE, SENDING, SENT, FAILED }

// expression: `data class SettingsUiState(`
data class SettingsUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val plannedThisYear: Int = 0,`
    val plannedThisYear: Int = 0,
    // continues the statement started above: `val totalTrips: Int = 0,`
    val totalTrips: Int = 0,
    val pendingChangeCount: Int = 0,
    val testNotificationState: TestNotificationState = TestNotificationState.IDLE,
// closes the multi-line argument list started above
)
