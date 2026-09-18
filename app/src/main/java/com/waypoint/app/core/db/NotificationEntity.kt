package com.waypoint.app.core.db

/**
 * One notification the app has posted, kept so the Profile bell can show a
 * history. Scoped by [accountId] like trips, so switching Google accounts on
 * the same device never leaks another user's alerts.
 */
data class NotificationEntity(
    val id: String,          // UUID
    val accountId: String,
    val title: String,
    val body: String,
    val createdAtMs: Long,
)
