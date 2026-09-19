// declares that this file belongs to the package `com.waypoint.app.core.db`
package com.waypoint.app.core.db

// expression: `data class TripEntity(`
data class TripEntity(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val accountId: String,`
    val accountId: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val startDate: String,`
    val startDate: String,
    // continues the statement started above: `val endDate: String,`
    val endDate: String,
    // continues the statement started above: `val destination: String?,`
    val destination: String?,
    // continues the statement started above: `val destLat: Double?,`
    val destLat: Double?,
    // continues the statement started above: `val destLng: Double?,`
    val destLng: Double?,
    // continues the statement started above: `val destPhotoUrl: String? = null,`
    val destPhotoUrl: String? = null,
    // continues the statement started above: `val createdAtMs: Long,`
    val createdAtMs: Long,
    // continues the statement started above: `val updatedAtMs: Long,`
    val updatedAtMs: Long,
// closes the multi-line argument list started above
)
