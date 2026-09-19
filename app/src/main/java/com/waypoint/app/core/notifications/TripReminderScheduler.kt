// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `androidx.work.ExistingPeriodicWorkPolicy` for use in this file
import androidx.work.ExistingPeriodicWorkPolicy
// imports `androidx.work.PeriodicWorkRequestBuilder` for use in this file
import androidx.work.PeriodicWorkRequestBuilder
// imports `androidx.work.WorkManager` for use in this file
import androidx.work.WorkManager
// imports `java.util.concurrent.TimeUnit` for use in this file
import java.util.concurrent.TimeUnit

// declares object `TripReminderScheduler` and opens its body
object TripReminderScheduler {

    // declares private const read-only property `WORK_NAME`, initialised to the string literal "trip_reminders"
    private const val WORK_NAME = "trip_reminders"

    // declares function `ensureScheduled` taking 1 parameter (`context`) and opens its body
    fun ensureScheduled(context: Context) {
        // declares read-only property `request`, initialised with the result of calling `PeriodicWorkRequestBuilder(…)`
        val request = PeriodicWorkRequestBuilder<TripReminderWorker>(1, TimeUnit.HOURS)
            // chained call `.build` on the previous result
            .build()
        // calls `getInstance` on `WorkManager` with arguments `(context.applicationContext)`
        WorkManager.getInstance(context.applicationContext)
            // chained call `.enqueueUniquePeriodicWork` on the previous result with arguments `(WORK_NAME, ExistingPeriodicWorkPolicy.K…)`
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    // closes the function `ensureScheduled`
    }
// closes the object `TripReminderScheduler`
}
