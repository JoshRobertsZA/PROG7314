package com.waypoint.app.core.notifications

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

/**
 * Enqueues [TripReminderWorker] to run roughly hourly. WorkManager doesn't
 * promise exact timing, so instead of one alarm per reminder the worker
 * polls hourly and each rule checks whether it's inside its window (7 am
 * or 7 pm hour) and hasn't already fired (see ReminderLogRepository).
 *
 * KEEP means re-calling this on every app start is harmless.
 */
object TripReminderScheduler {

    private const val WORK_NAME = "trip_reminders"

    fun ensureScheduled(context: Context) {
        val request = PeriodicWorkRequestBuilder<TripReminderWorker>(1, TimeUnit.HOURS)
            .build()
        WorkManager.getInstance(context.applicationContext)
            .enqueueUniquePeriodicWork(WORK_NAME, ExistingPeriodicWorkPolicy.KEEP, request)
    }
}
