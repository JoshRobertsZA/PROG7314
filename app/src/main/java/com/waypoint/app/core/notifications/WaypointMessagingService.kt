package com.waypoint.app.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Entry point for Firebase Cloud Messaging.
 *
 * FCM delivers two kinds of payload:
 *  - "notification" messages: when the app is in the background the SDK
 *    posts these itself using the manifest default icon/channel and this
 *    method is NOT called. In the foreground it IS called, so we post it
 *    ourselves here or the user would never see it.
 *  - "data" messages: always routed here, foreground or background.
 *
 * To cover both, we read title/body from the notification block first and
 * fall back to the data map, so a console test message and a server-sent
 * data message render identically.
 */
class WaypointMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        val title = message.notification?.title ?: message.data["title"] ?: getString(com.waypoint.app.R.string.app_name)
        val body  = message.notification?.body  ?: message.data["body"]  ?: return

        Log.d(TAG, "Push received: title=$title data=${message.data}")
        PushNotifier.show(applicationContext, title, body, message.data)
    }

    /**
     * Fired on first install and whenever FCM rotates the token. Logged so
     * the token can be pasted into the Firebase Console for test sends;
     * a later checkpoint will persist it against the signed-in account.
     */
    override fun onNewToken(token: String) {
        Log.i(TAG, "FCM token refreshed: $token")
        PushTokenManager.onTokenRefreshed(token)
    }

    private companion object {
        const val TAG = "WaypointFCM"
    }
}
