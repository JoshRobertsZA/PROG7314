package com.waypoint.app.features.newtrip.data

import android.content.ContentValues
import android.content.Context
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.TripEntity
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_DISPLAY_NAME
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_EMAIL
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_LAST_LOGIN
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_PHOTO_URL
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ACCOUNT_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_CREATED
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DESTINATION
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LAT
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LNG
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_PHOTO
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_END
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_NAME
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_START
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_UPDATED
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_ACCOUNTS
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_TRIPS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.sync.OutboxEntityType
import com.waypoint.app.core.sync.OutboxOperation
import com.waypoint.app.core.sync.OutboxRepository
import org.json.JSONObject

// Top-level (not a member function) so TripMergeTest can call it without
// constructing TripRepository, whose constructor needs a real Context -
// pure logic, no SQLite dependency, split out from pullAndMergeTrips
// specifically to be testable without Robolectric (not set up in this
// project's unit tests, which run on the plain JVM). null localUpdatedAtMs
// means "no local row yet" (first-ever pull), which always applies cloud.
internal fun shouldApplyCloudTrip(localUpdatedAtMs: Long?, cloudUpdatedAtMs: Long): Boolean {
    if (localUpdatedAtMs != null && localUpdatedAtMs > cloudUpdatedAtMs) return false
    return true
}

/**
 * Local storage for trips. Every create, update and delete is saved to SQLite and queued in the
 * outbox; [pullAndMergeTrips] brings in changes from the API.
 */
class TripRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)
    private val outbox = OutboxRepository(context)


    suspend fun upsertAccount(account: AccountEntity) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put(COL_ACC_ID,           account.id)
            put(COL_ACC_EMAIL,        account.email)
            put(COL_ACC_DISPLAY_NAME, account.displayName)
            put(COL_ACC_PHOTO_URL,    account.photoUrl)
            put(COL_ACC_LAST_LOGIN,   account.lastLoginMs)
        }
        db.writableDatabase.insertWithOnConflict(
            TABLE_ACCOUNTS, null, cv, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        )
    }


    suspend fun insertTrip(
        accountId: String,
        name: String,
        startDate: String,
        endDate: String,
        destination: String? = null,
        destLat: Double? = null,
        destLng: Double? = null,
        destPhotoUrl: String? = null,
    ): String = withContext(Dispatchers.IO) {
        val id  = UUID.randomUUID().toString()
        val now = System.currentTimeMillis()
        val cv  = ContentValues().apply {
            put(COL_TRIP_ID,          id)
            put(COL_TRIP_ACCOUNT_ID,  accountId)
            put(COL_TRIP_NAME,        name)
            put(COL_TRIP_START,       startDate)
            put(COL_TRIP_END,         endDate)
            put(COL_TRIP_DESTINATION, destination)
            if (destLat != null) put(COL_TRIP_DEST_LAT, destLat)
            if (destLng != null) put(COL_TRIP_DEST_LNG, destLng)
            put(COL_TRIP_DEST_PHOTO,  destPhotoUrl)
            put(COL_TRIP_CREATED,     now)
            put(COL_TRIP_UPDATED,     now)
        }
        db.writableDatabase.insertOrThrow(TABLE_TRIPS, null, cv)

        // Queue the create for the sync worker (PR 2b) to send later. The
        // payload shape matches WaypointApiRepository.createTrip's own
        // request body exactly, since that's what eventually gets POSTed.
        val payload = JSONObject().apply {
            put("id", id)
            put("name", name)
            put("startDate", startDate)
            put("endDate", endDate)
            if (destination != null) put("destination", destination)
            if (destLat != null) put("destLat", destLat)
            if (destLng != null) put("destLng", destLng)
            if (destPhotoUrl != null) put("destPhotoUrl", destPhotoUrl)
        }.toString()
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.CREATE, payload)

        id
    }

    suspend fun getTripsForAccount(accountId: String): List<TripEntity> =
        withContext(Dispatchers.IO) {
            val cursor = db.readableDatabase.rawQuery(
                """
                SELECT * FROM $TABLE_TRIPS
                WHERE $COL_TRIP_ACCOUNT_ID = ?
                ORDER BY $COL_TRIP_CREATED DESC
                """.trimIndent(),
                arrayOf(accountId),
            )
            val trips = mutableListOf<TripEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    trips += TripEntity(
                        id          = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),
                        accountId   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ACCOUNT_ID)),
                        name        = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)),
                        startDate   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_START)),
                        endDate     = it.getString(it.getColumnIndexOrThrow(COL_TRIP_END)),
                        destination = it.getString(it.getColumnIndexOrThrow(COL_TRIP_DESTINATION)),
                        destLat     = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                        destLng     = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                        destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getString(idx) else null },
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_CREATED)),
                        updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_UPDATED)),
                    )
                }
            }
            trips
        }


    suspend fun updateTripDetails(
        id: String,
        name: String,
        destination: String? = null,
        destLat: Double? = null,
        destLng: Double? = null,
        destPhotoUrl: String? = null,
    ) = withContext(Dispatchers.IO) {
        val now = System.currentTimeMillis()
        val cv  = ContentValues().apply {
            put(COL_TRIP_NAME,        name)
            put(COL_TRIP_DESTINATION, destination)
            if (destLat != null) put(COL_TRIP_DEST_LAT, destLat) else putNull(COL_TRIP_DEST_LAT)
            if (destLng != null) put(COL_TRIP_DEST_LNG, destLng) else putNull(COL_TRIP_DEST_LNG)
            put(COL_TRIP_DEST_PHOTO,  destPhotoUrl)
            put(COL_TRIP_UPDATED,     now)
        }
        db.writableDatabase.update(
            TABLE_TRIPS, cv,
            "$COL_TRIP_ID = ?",
            arrayOf(id),
        )

        // Only the fields this call actually changes go in the payload -
        // not the full trip - so a coalesced CREATE+UPDATE merge (see
        // OutboxRepository) only overwrites what really changed, and an
        // UPDATE sent on its own doesn't clobber startDate/endDate with
        // stale values this function never touched.
        val payload = JSONObject().apply {
            put("name", name)
            put("destination", destination ?: JSONObject.NULL)
            put("destLat", destLat ?: JSONObject.NULL)
            put("destLng", destLng ?: JSONObject.NULL)
            put("destPhotoUrl", destPhotoUrl ?: JSONObject.NULL)
        }.toString()
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.UPDATE, payload)
    }

    suspend fun updateTripDates(id: String, startDate: String, endDate: String) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put(COL_TRIP_START,   startDate)
            put(COL_TRIP_END,     endDate)
            put(COL_TRIP_UPDATED, System.currentTimeMillis())
        }
        db.writableDatabase.update(TABLE_TRIPS, cv, "$COL_TRIP_ID = ?", arrayOf(id))

        val payload = JSONObject().apply {
            put("startDate", startDate)
            put("endDate", endDate)
        }.toString()
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.UPDATE, payload)
    }

    suspend fun getTripById(id: String): TripEntity? = withContext(Dispatchers.IO) {
        val cursor = db.readableDatabase.rawQuery(
            "SELECT * FROM $TABLE_TRIPS WHERE $COL_TRIP_ID = ? LIMIT 1",
            arrayOf(id),
        )
        cursor.use {
            if (!it.moveToFirst()) return@withContext null
            TripEntity(
                id          = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),
                accountId   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ACCOUNT_ID)),
                name        = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)),
                startDate   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_START)),
                endDate     = it.getString(it.getColumnIndexOrThrow(COL_TRIP_END)),
                destination = it.getString(it.getColumnIndexOrThrow(COL_TRIP_DESTINATION)),
                destLat     = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                destLng     = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getString(idx) else null },
                createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_CREATED)),
                updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_UPDATED)),
            )
        }
    }

    suspend fun deleteTrip(id: String, accountId: String) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete(
            TABLE_TRIPS,
            "$COL_TRIP_ID = ? AND $COL_TRIP_ACCOUNT_ID = ?",
            arrayOf(id, accountId),
        )

        // Still a hard delete locally, same as before this commit - the
        // deleted tombstone column added earlier isn't consumed here. It
        // exists for PR 2b's pull/merge logic (not written yet) to tell
        // "deleted elsewhere" apart from "not synced yet"; a device's own
        // delete of its own row doesn't need to keep that row around.
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.DELETE, null)
    }

    // Was CONFLICT_IGNORE (local always wins, cloud edits/deletes from
    // other devices never applied - the audit's original finding). Now a
    // real merge: an entity with an unsent local change (still in the
    // outbox) is left alone entirely, since the cloud doesn't know about
    // that change yet. Otherwise, unlike the itinerary merge in the
    // previous commit, trips actually carry a server-side updatedAtMs
    // (trips.js stores it, TripPayload just never parsed it before this
    // commit), so this can do a genuine last-write-wins comparison instead
    // of "no pending change = cloud always wins." Caveat inherited from
    // that timestamp being client-supplied at write time: a device with a
    // badly wrong clock can still win a conflict it shouldn't.
    suspend fun pullAndMergeTrips(idToken: String, accountId: String) = withContext(Dispatchers.IO) {
        val cloudTrips = WaypointApiRepository.getTrips(idToken)?.filter { it.accountId == accountId } ?: return@withContext
        val pending = outbox.pendingEntityIds(OutboxEntityType.TRIP)
        val wdb = db.writableDatabase
        val cloudIds = cloudTrips.map { it.id }.toSet()

        for (t in cloudTrips) {
            if (t.id in pending) continue

            if (!shouldApplyCloudTrip(localUpdatedAtMs(wdb, t.id), t.updatedAtMs)) continue

            val cv = ContentValues().apply {
                put(COL_TRIP_ACCOUNT_ID,  t.accountId)
                put(COL_TRIP_NAME,        t.name)
                put(COL_TRIP_START,       t.startDate)
                put(COL_TRIP_END,         t.endDate)
                put(COL_TRIP_DESTINATION, t.destination)
                if (t.destLat != null) put(COL_TRIP_DEST_LAT, t.destLat) else putNull(COL_TRIP_DEST_LAT)
                if (t.destLng != null) put(COL_TRIP_DEST_LNG, t.destLng) else putNull(COL_TRIP_DEST_LNG)
                put(COL_TRIP_DEST_PHOTO,  t.destPhotoUrl)
                put(COL_TRIP_CREATED,     t.createdAtMs)
                put(COL_TRIP_UPDATED,     t.updatedAtMs)
            }
            // Try UPDATE first, INSERT only if no row matched - never
            // INSERT OR REPLACE: trips is the parent side of an ON DELETE
            // CASCADE FK (days/lodging/cars reference it), and REPLACE's
            // delete-then-reinsert would cascade-wipe this trip's entire
            // itinerary as a side effect of a plain field update.
            val updated = wdb.update(TABLE_TRIPS, cv, "$COL_TRIP_ID = ?", arrayOf(t.id))
            if (updated == 0) {
                cv.put(COL_TRIP_ID, t.id)
                wdb.insert(TABLE_TRIPS, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            TABLE_TRIPS, arrayOf(COL_TRIP_ID),
            "$COL_TRIP_ACCOUNT_ID = ?", arrayOf(accountId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(TABLE_TRIPS, "$COL_TRIP_ID = ?", arrayOf(id))
            }
        }
    }

    private fun localUpdatedAtMs(wdb: android.database.sqlite.SQLiteDatabase, tripId: String): Long? {
        wdb.query(
            TABLE_TRIPS, arrayOf(COL_TRIP_UPDATED),
            "$COL_TRIP_ID = ?", arrayOf(tripId), null, null, null,
        ).use { c -> return if (c.moveToFirst()) c.getLong(0) else null }
    }

}
