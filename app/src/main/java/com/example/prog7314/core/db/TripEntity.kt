package com.example.prog7314.core.db

/**
 * A single saved trip belonging to one Google account.
 *
 * [accountId] matches [AccountEntity.id] so trips are never shared between
 * two Google accounts on the same device.
 *
 * Dates are stored as ISO strings ("yyyy-MM-dd") so they sort correctly
 * with plain string comparisons and need no epoch conversion.
 */
data class TripEntity(
    val id: String,            // UUID, generated at insert time
    val accountId: String,     // Google sub of the owning account
    val name: String,
    val startDate: String,     // "yyyy-MM-dd"
    val endDate: String,       // "yyyy-MM-dd"
    val destination: String?,  // nullable; city name added in a later feature
    val createdAtMs: Long,
    val updatedAtMs: Long,
)
