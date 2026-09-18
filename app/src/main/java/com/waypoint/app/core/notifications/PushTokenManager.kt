package com.waypoint.app.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

/**
 * Owns the device's FCM registration token.
 *
 * For now the token is only logged (tag "WaypointFCM") so it can be copied
 * from Logcat into Firebase Console -> Messaging -> "Send test message".
 * Once we decide what the app sends, this is where the token gets written
 * to the signed-in account so a backend can target this device.
 */
object PushTokenManager {

    @Volatile var currentToken: String? = null
        private set

    /** Fetches (or returns the cached) token. Null if Play Services is unavailable. */
    suspend fun fetchToken(): String? = try {
        FirebaseMessaging.getInstance().token.await().also { onTokenRefreshed(it) }
    } catch (e: Exception) {
        Log.w(TAG, "Could not fetch FCM token", e)
        null
    }

    fun onTokenRefreshed(token: String) {
        currentToken = token
        Log.i(TAG, "FCM token: $token")
    }

    private const val TAG = "WaypointFCM"
}
