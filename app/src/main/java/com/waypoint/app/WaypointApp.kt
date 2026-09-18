package com.waypoint.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.notifications.NotificationChannels
import com.waypoint.app.core.notifications.TripReminderScheduler

/**
 * Process-wide setup that must exist before any Activity or Service runs.
 * Registered via android:name on <application> in the manifest.
 *
 * Right now that's only the notification channel: on Android 8+ a
 * notification posted to an unregistered channel is silently dropped, and
 * FCM can wake WaypointMessagingService while no Activity exists, so the
 * channel can't be created lazily from MainActivity.
 */
class WaypointApp : Application(), ImageLoaderFactory {

    /**
     * Coil's default OkHttp client sends no User-Agent, which Wikimedia's
     * image CDN refuses - destination thumbnails from Wikipedia then load
     * as nothing. Route Coil through the shared client instead.
     */
    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this).okHttpClient { HttpClient.instance }.build()

    override fun onCreate() {
        super.onCreate()
        NotificationChannels.ensureCreated(this)
        TripReminderScheduler.ensureScheduled(this)
    }
}
