package com.waypoint.app.core.sync

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.notifications.PushTokenManager
import com.waypoint.app.core.settings.AppSettings
import com.waypoint.app.core.settings.SettingsSync
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import com.waypoint.app.features.newtrip.data.TripRepository

/** 4xx responses that retrying can never fix. 401/408/429 can succeed later. */
internal fun isPermanentFailure(httpStatus: Int): Boolean =
    httpStatus in 400..499 && httpStatus != 401 && httpStatus != 408 && httpStatus != 429

/**
 * Two-way sync. Uploads the outbox (parents before children), then pulls the
 * cloud copy so changes made on another device arrive here. Conflicts are
 * resolved by the most recent updatedAt (see TripRepository.pullAndMergeTrips).
 */
class SyncWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val outbox = OutboxRepository(applicationContext)

    override suspend fun doWork(): Result {
        val user = AuthRepository.currentUser ?: return Result.success()
        val idToken = AuthRepository.getIdToken() ?: return Result.success()

        val pushedAll = pushOutbox(idToken)
        if (!pushedAll) return Result.retry()

        return try {
            pullCloudChanges(idToken, user.uid)
            SettingsSync.pull(applicationContext, idToken)
            AppSettings.recordSync(System.currentTimeMillis())
            Log.i(TAG, "Sync complete for ${user.uid}")
            Result.success()
        } catch (e: Exception) {
            Log.w(TAG, "Pull failed, will retry: ${e.message}")
            Result.retry()
        }
    }

    /** Returns false when at least one entry failed in a way worth retrying. */
    private suspend fun pushOutbox(idToken: String): Boolean {
        var allSent = true
        for (entry in outbox.peekAll()) {
            val result = WaypointApiRepository.sendOutboxPayload(
                idToken,
                methodFor(entry.operation),
                pathFor(entry),
                entry.payloadJson,
                PushTokenManager.currentToken,
            )
            when {
                result.successful -> outbox.ack(entry.id)
                isPermanentFailure(result.httpStatus) -> {
                    Log.w(TAG, "Permanent failure (HTTP ${result.httpStatus}) for ${entry.entityType}/${entry.entityId}, dropping from outbox")
                    outbox.ack(entry.id)
                }
                else -> {
                    outbox.markFailed(entry.id, "HTTP ${result.httpStatus}")
                    allSent = false
                }
            }
        }
        return allSent
    }

    private suspend fun pullCloudChanges(idToken: String, accountId: String) {
        val trips = TripRepository(applicationContext)
        val itinerary = ItineraryRepository(applicationContext)
        trips.pullAndMergeTrips(idToken, accountId)
        for (trip in trips.getTripsForAccount(accountId)) {
            itinerary.pullAndMergeForTrip(idToken, trip.id)
        }
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
        return if (entry.operation == OutboxOperation.CREATE) base else "$base/${entry.entityId}"
    }

    private companion object {
        const val TAG = "SyncWorker"
    }
}
