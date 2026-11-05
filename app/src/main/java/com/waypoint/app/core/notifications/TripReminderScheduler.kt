package com.waypoint.app.core.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/** Runs [TripReminderWorker] hourly, and shortly after any trip or itinerary change. */
object TripReminderScheduler {

    private const val WORK_NAME = "trip_reminders"
    private const val CHECK_NOW_WORK_NAME = "trip_reminders_now"

    fun ensureScheduled(context: Context) {
        val request = PeriodicWorkRequestBuilder<TripReminderWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }

    /**
     * Re-checks reminders a few seconds after a change, so e.g. a trip created for tomorrow gets its
     * countdown straight away instead of at the next hourly run. REPLACE restarts the delay, so a burst
     * of edits produces one check.
     */
    fun checkSoon(context: Context) {
        val request = OneTimeWorkRequestBuilder<TripReminderWorker>()
            .setInitialDelay(5, TimeUnit.SECONDS)
            .build()
        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork(CHECK_NOW_WORK_NAME, ExistingWorkPolicy.REPLACE, request)
    }
}
