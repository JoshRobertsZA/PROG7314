package com.waypoint.app.core.db

/**
 * Holds the currently signed-in Google account for this app session.
 *
 * Set [accountId] (and the display fields) immediately after a successful
 * Google SSO sign-in.  Every feature that needs to scope data to one
 * account (trips, itinerary items, etc.) reads [accountId] from here.
 *
 * Defaults to [PLACEHOLDER_ID] so the New Trip flow works during
 * development before real auth is wired up.
 */
object SessionManager {
    const val PLACEHOLDER_ID = "local_user"

    var accountId: String = PLACEHOLDER_ID
    var email: String = ""
    var displayName: String = ""
    var photoUrl: String = ""

    val isSignedIn: Boolean
        get() = accountId != PLACEHOLDER_ID

    fun signIn(id: String, email: String, displayName: String, photoUrl: String) {
        this.accountId   = id
        this.email       = email
        this.displayName = displayName
        this.photoUrl    = photoUrl
    }

    fun signOut() {
        accountId   = PLACEHOLDER_ID
        email       = ""
        displayName = ""
        photoUrl    = ""
    }
}
