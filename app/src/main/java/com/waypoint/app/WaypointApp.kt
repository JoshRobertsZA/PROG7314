// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `coil.ImageLoader` for use in this file
import coil.ImageLoader
// imports `coil.ImageLoaderFactory` for use in this file
import coil.ImageLoaderFactory
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `com.waypoint.app.core.notifications.NotificationChannels` for use in this file
import com.waypoint.app.core.notifications.NotificationChannels
// imports `com.waypoint.app.core.notifications.TripReminderScheduler` for use in this file
import com.waypoint.app.core.notifications.TripReminderScheduler

// declares class `WaypointApp`, inheriting from `Application(), ImageLoaderFactory` and opens its body
class WaypointApp : Application(), ImageLoaderFactory {

    // declares override function `newImageLoader` taking no parameters, returning `ImageLoader`; its body is the expression ``
    override fun newImageLoader(): ImageLoader =
        // continues the statement started above: `ImageLoader.Builder(this).okHttpClient { HttpClient.instanc…`
        ImageLoader.Builder(this).okHttpClient { HttpClient.instance }.build()

    // declares override function `onCreate` taking no parameters and opens its body
    override fun onCreate() {
        // calls `onCreate` on `super` with arguments `()`
        super.onCreate()
        // calls `ensureCreated` on `NotificationChannels` with arguments `(this)`
        NotificationChannels.ensureCreated(this)
        // calls `ensureScheduled` on `TripReminderScheduler` with arguments `(this)`
        TripReminderScheduler.ensureScheduled(this)
    // closes the function `onCreate`
    }
// closes the class `WaypointApp`
}
