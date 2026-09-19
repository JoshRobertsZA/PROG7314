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
        // declares read-only property `title`, initialised to `message.notification?.title ?: message.data[…`
        val title = message.notification?.title ?: message.data["title"] ?: getString(com.waypoint.app.R.string.app_name)
        // declares read-only property `body`, initialised to `message.notification?.body ?: message.data["…`
        val body  = message.notification?.body  ?: message.data["body"]  ?: return

        // calls `d` on `Log` with arguments `(TAG, "Push received: title=$title data=${mes…)`
        Log.d(TAG, "Push received: title=$title data=${message.data}")
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
