package com.waypoint.app.core.db

/**
 * In-memory details of the signed-in user (id, email, name, photo) used to scope local queries. Holds
 * [PLACEHOLDER_ID] while nobody is signed in.
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
