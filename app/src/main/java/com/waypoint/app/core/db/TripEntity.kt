package com.waypoint.app.core.db

/** A trip as stored in the local `trips` table and synced to `/trips` on the API. */
data class TripEntity(
    val id: String,
    val accountId: String,
    val name: String,
    val startDate: String,
    val endDate: String,
    val destination: String?,
    val destLat: Double?,
    val destLng: Double?,
    val destPhotoUrl: String? = null,
    val createdAtMs: Long,
    val updatedAtMs: Long,
)
