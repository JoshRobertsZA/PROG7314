package com.waypoint.app.features.settings.ui

/**
 * State for the Profile tab's stat cards. Both counts are scoped to the
 * signed-in account ([com.waypoint.app.core.db.SessionManager.accountId])
 * so two Google accounts on the same device never see each other's trips.
 */
data class SettingsUiState(
    val isLoading: Boolean = true,
    /** Trips whose start date falls in the current calendar year. */
    val plannedThisYear: Int = 0,
    /** Every trip this account has ever created - past, present and future. */
    val totalTrips: Int = 0,
)
