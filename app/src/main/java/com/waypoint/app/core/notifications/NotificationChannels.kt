// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.app.NotificationChannel` for use in this file
import android.app.NotificationChannel
// imports `android.app.NotificationManager` for use in this file
import android.app.NotificationManager
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.os.Build` for use in this file
import android.os.Build
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R

// declares object `NotificationChannels` and opens its body
object NotificationChannels {

    // declares function `defaultChannelId` taking 1 parameter (`context`), returning `String`; its body is the expression ``
    fun defaultChannelId(context: Context): String =
        // continues the statement started above: `context.getString(R.string.notification_channel_id)`
        context.getString(R.string.notification_channel_id)

    // declares function `ensureCreated` taking 1 parameter (`context`) and opens its body
    fun ensureCreated(context: Context) {
        // `if` statement: executes `return` when `Build.VERSION.SDK_INT < Build.VERSION_CODES.O` is true
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        // declares read-only property `channel`, initialised with the result of calling `NotificationChannel(…)`
        val channel = NotificationChannel(
            // continues the statement started above: `defaultChannelId(context),`
            defaultChannelId(context),
            // continues the statement started above: `context.getString(R.string.notification_channel_name),`
            context.getString(R.string.notification_channel_name),
            // continues the statement started above: `NotificationManager.IMPORTANCE_DEFAULT,`
            NotificationManager.IMPORTANCE_DEFAULT,
        // continues the statement started above: `).apply {`
        ).apply {
            // assigns `description` the value `context.getString(R.string.notification_channel_d…`
            description = context.getString(R.string.notification_channel_description)
        // closes the block
        }

        // declares read-only property `manager`, initialised with the result of calling `context.getSystemService(…)`
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        // calls `createNotificationChannel` on `manager` with arguments `(channel)`
        manager.createNotificationChannel(channel)
    // closes the function `ensureCreated`
    }
// closes the object `NotificationChannels`
}
