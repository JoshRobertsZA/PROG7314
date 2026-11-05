package com.waypoint.app.core.db

/** A notification shown to the user, kept for the in-app notification history. */
data class NotificationEntity(
    val id: String,
    val accountId: String,
    val title: String,
    val body: String,
    val createdAtMs: Long,
)
