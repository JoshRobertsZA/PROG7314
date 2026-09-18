package com.waypoint.app.core.db

/**
 * Represents a Google SSO account that has signed in on this device.
 * [id] is the stable Google sub (subject) — used as the foreign key in [TripEntity].
 */
data class AccountEntity(
    val id: String,           // Google sub
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val lastLoginMs: Long,
)
