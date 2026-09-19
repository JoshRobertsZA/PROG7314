// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.google.firebase.messaging.FirebaseMessaging` for use in this file
import com.google.firebase.messaging.FirebaseMessaging
// imports `kotlinx.coroutines.tasks.await` for use in this file
import kotlinx.coroutines.tasks.await

// declares object `PushTokenManager` and opens its body
object PushTokenManager {

    // declares mutable property `currentToken` of type `String?`, initialised to null
    @Volatile var currentToken: String? = null
        // expression: `private set`
        private set

    // declares suspend function `fetchToken` taking no parameters, returning `String?`; its body is the expression `try {`
    suspend fun fetchToken(): String? = try {
        // calls `getInstance` on `FirebaseMessaging` with arguments `()`, then chains `.token`, `.await()`, `.also { onTokenRefreshed(it) }`
        FirebaseMessaging.getInstance().token.await().also { onTokenRefreshed(it) }
    // `catch` block: handles a thrown `Exception` bound to `e`
    } catch (e: Exception) {
        // calls `w` on `Log` with arguments `(TAG, "Could not fetch FCM token", e)`
        Log.w(TAG, "Could not fetch FCM token", e)
        // expression: `null`
        null
    // closes the catch block
    }

    // declares function `onTokenRefreshed` taking 1 parameter (`token`) and opens its body
    fun onTokenRefreshed(token: String) {
        // assigns `currentToken` the value `token`
        currentToken = token
        // calls `i` on `Log` with arguments `(TAG, "FCM token: $token")`
        Log.i(TAG, "FCM token: $token")
    // closes the function `onTokenRefreshed`
    }

    // declares private const read-only property `TAG`, initialised to the string literal "WaypointFCM"
    private const val TAG = "WaypointFCM"
// closes the object `PushTokenManager`
}
