// declares that this file belongs to the package `com.waypoint.app.core.db`
package com.waypoint.app.core.db

// declares object `SessionManager` and opens its body
object SessionManager {
    // declares const read-only property `PLACEHOLDER_ID`, initialised to the string literal "local_user"
    const val PLACEHOLDER_ID = "local_user"

    // declares mutable property `accountId` of type `String`, initialised to `PLACEHOLDER_ID`
    var accountId: String = PLACEHOLDER_ID
    // declares mutable property `email` of type `String`, initialised to the string literal ""
    var email: String = ""
    // declares mutable property `displayName` of type `String`, initialised to the string literal ""
    var displayName: String = ""
    // declares mutable property `photoUrl` of type `String`, initialised to the string literal ""
    var photoUrl: String = ""

    // declares read-only property `isSignedIn` of type `Boolean`
    val isSignedIn: Boolean
        // custom getter: returns `accountId != PLACEHOLDER_ID`
        get() = accountId != PLACEHOLDER_ID

    // declares function `signIn` taking 4 parameters (`id`, `email`, `displayName`, `photoUrl`) and opens its body
    fun signIn(id: String, email: String, displayName: String, photoUrl: String) {
        // assigns `this.accountId` the value `id`
        this.accountId   = id
        // assigns `this.email` the value `email`
        this.email       = email
        // assigns `this.displayName` the value `displayName`
        this.displayName = displayName
        // assigns `this.photoUrl` the value `photoUrl`
        this.photoUrl    = photoUrl
    // closes the function `signIn`
    }

    // declares function `signOut` taking no parameters and opens its body
    fun signOut() {
        // assigns `accountId` the value `PLACEHOLDER_ID`
        accountId   = PLACEHOLDER_ID
        // assigns `email` the value `""`
        email       = ""
        // assigns `displayName` the value `""`
        displayName = ""
        // assigns `photoUrl` the value `""`
        photoUrl    = ""
    // closes the function `signOut`
    }
// closes the object `SessionManager`
}
