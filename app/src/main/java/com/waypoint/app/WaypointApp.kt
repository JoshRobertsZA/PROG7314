package com.waypoint.app

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.network.UsageGuardian
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.notifications.NotificationChannels
import com.waypoint.app.core.notifications.TripReminderScheduler
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.core.settings.AppSettings
import com.waypoint.app.core.settings.SettingsSync
import com.waypoint.app.core.sync.SyncScheduler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/** Process-wide setup that must run before any screen is shown. */
class WaypointApp : Application(), ImageLoaderFactory {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun newImageLoader(): ImageLoader =
        ImageLoader.Builder(this).okHttpClient { HttpClient.instance }.build()

    override fun onCreate() {
        super.onCreate()
        AppSettings.init(this)
        SettingsSync.init()
        RemoteSecrets.init(this)
        UsageGuardian.init(this)
        NotificationChannels.ensureCreated(this)
        TripReminderScheduler.ensureScheduled(this)
        SyncScheduler.ensurePeriodic(this)
        appScope.launch { WaypointApiRepository.warmUp() }
    }
}
