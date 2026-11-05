package com.waypoint.app.core.notifications

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

/**
 * Receives FCM pushes from the API and turns them into notifications, and re-registers the device when
 * its token changes.
 */
class WaypointMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(message: RemoteMessage) {
        // The server sends a data-only payload (type + whatever extras that
        // type needs, e.g. tripId) rather than a notification.title/body -
        // see PR 4's localisation note. Visible text is built here, from
        // string resources, so it respects whatever language the user has
        // chosen on this device rather than whatever the server happened to
        // send.
        val type = message.data["type"]
        val (title, body) = when (type) {
            "trip_created" -> getString(com.waypoint.app.R.string.notification_trip_created_title) to
                getString(com.waypoint.app.R.string.notification_trip_created_body)
            "test" -> getString(com.waypoint.app.R.string.notification_test_title) to
                getString(com.waypoint.app.R.string.notification_test_body)
            // Fallback for any push that isn't one of the above (kept so an
            // unrecognised or future type doesn't silently disappear).
            else -> {
                val fallbackTitle = message.notification?.title ?: message.data["title"] ?: getString(com.waypoint.app.R.string.app_name)
                val fallbackBody = message.notification?.body ?: message.data["body"] ?: return
                fallbackTitle to fallbackBody
            }
        }

        Log.d(TAG, "Push received: type=$type data=${message.data}")
        PushNotifier.show(applicationContext, NotificationType.TRIP_UPDATES, title, body, message.data)
    }

    override fun onNewToken(token: String) {
        Log.i(TAG, "FCM token refreshed")
        PushTokenManager.onTokenRefreshed(token)
        // If the user is already signed in, tell the server about the new
        // token right away rather than waiting for the next login.
        DeviceRegistrar.registerIfSignedIn()
    }

    private companion object {
        const val TAG = "WaypointFCM"
    }
}
