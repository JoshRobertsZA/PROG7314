// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.google.firebase.messaging.FirebaseMessagingService` for use in this file
import com.google.firebase.messaging.FirebaseMessagingService
// imports `com.google.firebase.messaging.RemoteMessage` for use in this file
import com.google.firebase.messaging.RemoteMessage

// declares class `WaypointMessagingService`, inheriting from `FirebaseMessagingService()` and opens its body
class WaypointMessagingService : FirebaseMessagingService() {

    // declares override function `onMessageReceived` taking 1 parameter (`message`) and opens its body
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

        // calls `d` on `Log` with arguments `(TAG, "Push received: type=$type data=${messa…)`
        Log.d(TAG, "Push received: type=$type data=${message.data}")
        // calls `show` on `PushNotifier` with arguments `(applicationContext, title, body, message.data)`
        PushNotifier.show(applicationContext, title, body, message.data)
    // closes the function `onMessageReceived`
    }

    // declares override function `onNewToken` taking 1 parameter (`token`) and opens its body
    override fun onNewToken(token: String) {
        // calls `i` on `Log` with arguments `(TAG, "FCM token refreshed: $token")`
        Log.i(TAG, "FCM token refreshed: $token")
        // calls `onTokenRefreshed` on `PushTokenManager` with arguments `(token)`
        PushTokenManager.onTokenRefreshed(token)
        // If the user is already signed in, tell the server about the new
        // token right away rather than waiting for the next login.
        DeviceRegistrar.registerIfSignedIn()
    // closes the function `onNewToken`
    }

    // declares the companion object holding members shared by all instances of the enclosing class
    private companion object {
        // declares const read-only property `TAG`, initialised to the string literal "WaypointFCM"
        const val TAG = "WaypointFCM"
    // closes the companion object
    }
// closes the class `WaypointMessagingService`
}
