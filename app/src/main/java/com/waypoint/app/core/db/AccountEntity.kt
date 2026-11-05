package com.waypoint.app.core.db

/** The signed-in user's profile as stored in the local `accounts` table. */
data class AccountEntity(
    val id: String,
    val email: String,
    val displayName: String,
    val photoUrl: String,
    val lastLoginMs: Long,
)
