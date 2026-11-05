package com.waypoint.app.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.coroutines.tasks.await

/**
 * Holds this device's FCM token so it can be registered with the API and sent with requests (so the
 * device doesn't notify itself).
 */
object PushTokenManager {

    @Volatile var currentToken: String? = null
        private set

    suspend fun fetchToken(): String? = try {
        FirebaseMessaging.getInstance().token.await().also { onTokenRefreshed(it) }
    } catch (e: Exception) {
        Log.w(TAG, "Could not fetch FCM token", e)
        null
    }

    fun onTokenRefreshed(token: String) {
        currentToken = token
        Log.i(TAG, "FCM token received")
    }

    private const val TAG = "WaypointFCM"
}
