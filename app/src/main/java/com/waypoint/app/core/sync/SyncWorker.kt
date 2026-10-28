package com.waypoint.app.core.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.notifications.PushTokenManager

// Drains the local sync outbox: sends each pending entry to the API in the
// order OutboxRepository.peekAll() returns (parents before children, then
// oldest first), acking on success. WorkManager's own NetworkType.CONNECTED
// constraint and exponential backoff (set up in SyncScheduler, not here -
// this class only defines what a single run does) handle not running at
// all without a network and retrying the whole worker later.
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val outbox = OutboxRepository(applicationContext)

    override suspend fun doWork(): Result {
        // Not signed in - nothing to sync, and nothing useful a retry would
        // change, so this is success (not failure/retry), matching
        // TripReminderWorker's same pattern for the same situation.
        val idToken = AuthRepository.getIdToken() ?: return Result.success()

        val entries = outbox.peekAll()
        if (entries.isEmpty()) return Result.success()

        var anyRetryableFailure = false
        for (entry in entries) {
            val result = WaypointApiRepository.sendOutboxPayload(
                idToken,
                methodFor(entry.operation),
                pathFor(entry),
                entry.payloadJson,
                PushTokenManager.currentToken,
            )
            when {
                result.successful -> outbox.ack(entry.id)
                result.httpStatus in 400..499 -> {
                    // A 4xx means the request itself is wrong (bad id,
                    // validation failure, ownership conflict...) - retrying
                    // the exact same payload will never succeed, so this
                    // drops the entry rather than retrying it forever. Logged
                    // clearly since this is effectively silent data loss for
                    // that one change.
                    Log.w(
                        TAG,
                        "Permanent failure (HTTP ${result.httpStatus}) for " +
                            "${entry.entityType}/${entry.entityId}, dropping from outbox",
                    )
                    outbox.ack(entry.id)
                }
                else -> {
                    // Network failure (-1) or 5xx - both are plausibly
                    // transient, so keep the entry and let this worker run
                    // retry it.
                    outbox.markFailed(entry.id, "HTTP ${result.httpStatus}")
                    anyRetryableFailure = true
                }
            }
        }

        return if (anyRetryableFailure) Result.retry() else Result.success()
    }

    private fun methodFor(operation: OutboxOperation): String = when (operation) {
        OutboxOperation.CREATE -> "POST"
        OutboxOperation.UPDATE -> "PUT"
        OutboxOperation.DELETE -> "DELETE"
    }

    private fun pathFor(entry: OutboxEntry): String {
        val base = when (entry.entityType) {
            OutboxEntityType.TRIP -> "/trips"
            OutboxEntityType.DAY -> "/itinerary/days"
            OutboxEntityType.FLIGHT -> "/itinerary/flights"
            OutboxEntityType.LODGING -> "/itinerary/lodging"
            OutboxEntityType.CAR -> "/itinerary/cars"
            OutboxEntityType.PLACE -> "/itinerary/places"
        }
        // A create posts to the collection; update/delete target the
        // specific id.
        return if (entry.operation == OutboxOperation.CREATE) base else "$base/${entry.entityId}"
    }

    private companion object {
        const val TAG = "SyncWorker"
    }
}
