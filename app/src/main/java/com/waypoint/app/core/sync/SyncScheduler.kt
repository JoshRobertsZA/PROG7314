package com.waypoint.app.core.sync

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

// Not called from anywhere yet - the actual trigger points (login,
// reconnect, pull-to-refresh) are a separate commit. This just defines how
// a sync run is requested once something does call enqueue().
object SyncScheduler {

    private const val WORK_NAME = "outbox_sync"

    fun enqueue(context: Context) {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(constraints)
            // WorkManager's minimum allowed backoff is 10 seconds; this
            // doubles on each retry (LINEAR would just add 10s each time,
            // which floods a genuinely-down server for longer before
            // backing off meaningfully).
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .build()

        // KEEP, not REPLACE: if a sync is already queued or running,
        // another enqueue() call (e.g. two screens both triggering a sync)
        // shouldn't cancel its progress and restart from the front of the
        // outbox.
        WorkManager.getInstance(context.applicationContext)
            .enqueueUniqueWork(WORK_NAME, ExistingWorkPolicy.KEEP, request)
    }
}
