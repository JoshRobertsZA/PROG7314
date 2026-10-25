// declares that this file belongs to the package `com.waypoint.app.features.newtrip.data`
package com.waypoint.app.features.newtrip.data

// imports `android.content.ContentValues` for use in this file
import android.content.ContentValues
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `com.waypoint.app.core.db.AccountEntity` for use in this file
import com.waypoint.app.core.db.AccountEntity
// imports `com.waypoint.app.core.db.TripEntity` for use in this file
import com.waypoint.app.core.db.TripEntity
// imports `com.waypoint.app.core.db.WaypointDbHelper` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_DISPLAY_NAME` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_DISPLAY_NAME
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_EMAIL` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_EMAIL
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_ID` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_ID
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_LAST_LOGIN` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_LAST_LOGIN
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_PHOTO_URL` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_ACC_PHOTO_URL
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ACCOUNT_ID` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ACCOUNT_ID
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_CREATED` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_CREATED
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DESTINATION` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DESTINATION
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LAT` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LAT
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LNG` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_LNG
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_PHOTO` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_DEST_PHOTO
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_END` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_END
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ID` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_ID
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_NAME` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_NAME
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_START` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_START
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_UPDATED` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_TRIP_UPDATED
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_ACCOUNTS` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_ACCOUNTS
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_TRIPS` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_TRIPS
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `java.util.UUID` for use in this file
import java.util.UUID
// imports `com.waypoint.app.core.network.WaypointApiRepository` for use in this file
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.sync.OutboxEntityType
import com.waypoint.app.core.sync.OutboxOperation
import com.waypoint.app.core.sync.OutboxRepository
import org.json.JSONObject

// declares class `TripRepository` with a primary constructor taking 1 parameter (`context`) and opens its body
class TripRepository(context: Context) {

    // declares private read-only property `db`, initialised with the result of calling `WaypointDbHelper.getInstance(…)`
    private val db = WaypointDbHelper.getInstance(context)
    private val outbox = OutboxRepository(context)


    // declares suspend function `upsertAccount` taking 1 parameter (`account`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun upsertAccount(account: AccountEntity) = withContext(Dispatchers.IO) {
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(COL_ACC_ID, account.id)`
            put(COL_ACC_ID,           account.id)
            // calls `put` with arguments `(COL_ACC_EMAIL, account.email)`
            put(COL_ACC_EMAIL,        account.email)
            // calls `put` with arguments `(COL_ACC_DISPLAY_NAME, account.displayName)`
            put(COL_ACC_DISPLAY_NAME, account.displayName)
            // calls `put` with arguments `(COL_ACC_PHOTO_URL, account.photoUrl)`
            put(COL_ACC_PHOTO_URL,    account.photoUrl)
            // calls `put` with arguments `(COL_ACC_LAST_LOGIN, account.lastLoginMs)`
            put(COL_ACC_LAST_LOGIN,   account.lastLoginMs)
        // closes the lambda assigned to `cv`
        }
        // calls `insertWithOnConflict` on `db.writableDatabase` with an argument list that continues on the following lines
        db.writableDatabase.insertWithOnConflict(
            // continues the statement started above: `TABLE_ACCOUNTS, null, cv, android.database.sqlite.SQLiteDat…`
            TABLE_ACCOUNTS, null, cv, android.database.sqlite.SQLiteDatabase.CONFLICT_REPLACE
        // closes the multi-line argument list started above
        )
    // closes the block
    }


    // expression: `suspend fun insertTrip(`
    suspend fun insertTrip(
        // continues the statement started above: `accountId: String,`
        accountId: String,
        // continues the statement started above: `name: String,`
        name: String,
        // continues the statement started above: `startDate: String,`
        startDate: String,
        // continues the statement started above: `endDate: String,`
        endDate: String,
        // continues the statement started above: `destination: String? = null,`
        destination: String? = null,
        // continues the statement started above: `destLat: Double? = null,`
        destLat: Double? = null,
        // continues the statement started above: `destLng: Double? = null,`
        destLng: Double? = null,
        // continues the statement started above: `destPhotoUrl: String? = null,`
        destPhotoUrl: String? = null,
    // continues the statement started above: `): String = withContext(Dispatchers.IO) {`
    ): String = withContext(Dispatchers.IO) {
        // declares read-only property `id`, initialised with the result of calling `UUID.randomUUID(…)`
        val id  = UUID.randomUUID().toString()
        // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
        val now = System.currentTimeMillis()
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv  = ContentValues().apply {
            // calls `put` with arguments `(COL_TRIP_ID, id)`
            put(COL_TRIP_ID,          id)
            // calls `put` with arguments `(COL_TRIP_ACCOUNT_ID, accountId)`
            put(COL_TRIP_ACCOUNT_ID,  accountId)
            // calls `put` with arguments `(COL_TRIP_NAME, name)`
            put(COL_TRIP_NAME,        name)
            // calls `put` with arguments `(COL_TRIP_START, startDate)`
            put(COL_TRIP_START,       startDate)
            // calls `put` with arguments `(COL_TRIP_END, endDate)`
            put(COL_TRIP_END,         endDate)
            // calls `put` with arguments `(COL_TRIP_DESTINATION, destination)`
            put(COL_TRIP_DESTINATION, destination)
            // `if` statement: executes `put(COL_TRIP_DEST_LAT, destLat)` when `destLat != null` is true
            if (destLat != null) put(COL_TRIP_DEST_LAT, destLat)
            // `if` statement: executes `put(COL_TRIP_DEST_LNG, destLng)` when `destLng != null` is true
            if (destLng != null) put(COL_TRIP_DEST_LNG, destLng)
            // calls `put` with arguments `(COL_TRIP_DEST_PHOTO, destPhotoUrl)`
            put(COL_TRIP_DEST_PHOTO,  destPhotoUrl)
            // calls `put` with arguments `(COL_TRIP_CREATED, now)`
            put(COL_TRIP_CREATED,     now)
            // calls `put` with arguments `(COL_TRIP_UPDATED, now)`
            put(COL_TRIP_UPDATED,     now)
        // closes the lambda assigned to `cv`
        }
        // calls `insertOrThrow` on `db.writableDatabase` with arguments `(TABLE_TRIPS, null, cv)`
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

        // expression: `id`
        id
    // closes the block
    }

    // declares suspend function `getTripsForAccount` taking 1 parameter (`accountId`), returning `List<TripEntity>`; its body is the expression ``
    suspend fun getTripsForAccount(accountId: String): List<TripEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.rawQuery(…)`
            val cursor = db.readableDatabase.rawQuery(
                // continues the statement started above: `"""`
                """
                SELECT * FROM $TABLE_TRIPS
                WHERE $COL_TRIP_ACCOUNT_ID = ?
                ORDER BY $COL_TRIP_CREATED DESC
                """.trimIndent(),
                // continues the statement started above: `arrayOf(accountId),`
                arrayOf(accountId),
            // closes the multi-line argument list started above
            )
            // declares read-only property `trips`, initialised with the result of calling `mutableListOf(…)`
            val trips = mutableListOf<TripEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // adds to `trips` the value `TripEntity(`
                    trips += TripEntity(
                        // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),`
                        id          = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),
                        // continues the statement started above: `accountId = it.getString(it.getColumnIndexOrThrow(COL_TRIP_…`
                        accountId   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ACCOUNT_ID)),
                        // continues the statement started above: `name = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)…`
                        name        = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)),
                        // continues the statement started above: `startDate = it.getString(it.getColumnIndexOrThrow(COL_TRIP_…`
                        startDate   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_START)),
                        // continues the statement started above: `endDate = it.getString(it.getColumnIndexOrThrow(COL_TRIP_EN…`
                        endDate     = it.getString(it.getColumnIndexOrThrow(COL_TRIP_END)),
                        // continues the statement started above: `destination = it.getString(it.getColumnIndexOrThrow(COL_TRI…`
                        destination = it.getString(it.getColumnIndexOrThrow(COL_TRIP_DESTINATION)),
                        // continues the statement started above: `destLat = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx ->…`
                        destLat     = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                        // continues the statement started above: `destLng = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx ->…`
                        destLng     = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                        // continues the statement started above: `destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let {…`
                        destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getString(idx) else null },
                        // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_…`
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_CREATED)),
                        // continues the statement started above: `updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_…`
                        updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_UPDATED)),
                    // closes the multi-line argument list started above
                    )
                // closes the while loop
                }
            // closes the block
            }
            // expression: `trips`
            trips
        // closes the block
        }


    // expression: `suspend fun updateTripDetails(`
    suspend fun updateTripDetails(
        // continues the statement started above: `id: String,`
        id: String,
        // continues the statement started above: `name: String,`
        name: String,
        // continues the statement started above: `destination: String? = null,`
        destination: String? = null,
        // continues the statement started above: `destLat: Double? = null,`
        destLat: Double? = null,
        // continues the statement started above: `destLng: Double? = null,`
        destLng: Double? = null,
        // continues the statement started above: `destPhotoUrl: String? = null,`
        destPhotoUrl: String? = null,
    // continues the statement started above: `) = withContext(Dispatchers.IO) {`
    ) = withContext(Dispatchers.IO) {
        // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
        val now = System.currentTimeMillis()
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv  = ContentValues().apply {
            // calls `put` with arguments `(COL_TRIP_NAME, name)`
            put(COL_TRIP_NAME,        name)
            // calls `put` with arguments `(COL_TRIP_DESTINATION, destination)`
            put(COL_TRIP_DESTINATION, destination)
            // `if` statement: executes `put(COL_TRIP_DEST_LAT, destLat) else putNull…` when `destLat != null` is true
            if (destLat != null) put(COL_TRIP_DEST_LAT, destLat) else putNull(COL_TRIP_DEST_LAT)
            // `if` statement: executes `put(COL_TRIP_DEST_LNG, destLng) else putNull…` when `destLng != null` is true
            if (destLng != null) put(COL_TRIP_DEST_LNG, destLng) else putNull(COL_TRIP_DEST_LNG)
            // calls `put` with arguments `(COL_TRIP_DEST_PHOTO, destPhotoUrl)`
            put(COL_TRIP_DEST_PHOTO,  destPhotoUrl)
            // calls `put` with arguments `(COL_TRIP_UPDATED, now)`
            put(COL_TRIP_UPDATED,     now)
        // closes the lambda assigned to `cv`
        }
        // calls `update` on `db.writableDatabase` with an argument list that continues on the following lines
        db.writableDatabase.update(
            // continues the statement started above: `TABLE_TRIPS, cv,`
            TABLE_TRIPS, cv,
            // continues the statement started above: `"$COL_TRIP_ID = ?",`
            "$COL_TRIP_ID = ?",
            // continues the statement started above: `arrayOf(id),`
            arrayOf(id),
        // closes the multi-line argument list started above
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
    // closes the block
    }

    // declares suspend function `updateTripDates` taking 3 parameters (`id`, `startDate`, `endDate`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun updateTripDates(id: String, startDate: String, endDate: String) = withContext(Dispatchers.IO) {
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(COL_TRIP_START, startDate)`
            put(COL_TRIP_START,   startDate)
            // calls `put` with arguments `(COL_TRIP_END, endDate)`
            put(COL_TRIP_END,     endDate)
            // calls `put` with arguments `(COL_TRIP_UPDATED, System.currentTimeMillis())`
            put(COL_TRIP_UPDATED, System.currentTimeMillis())
        // closes the lambda assigned to `cv`
        }
        // calls `update` on `db.writableDatabase` with arguments `(TABLE_TRIPS, cv, "$COL_TRIP_ID = ?", arrayOf…)`
        db.writableDatabase.update(TABLE_TRIPS, cv, "$COL_TRIP_ID = ?", arrayOf(id))

        val payload = JSONObject().apply {
            put("startDate", startDate)
            put("endDate", endDate)
        }.toString()
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.UPDATE, payload)
    // closes the block
    }

    // declares suspend function `getTripById` taking 1 parameter (`id`), returning `TripEntity?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun getTripById(id: String): TripEntity? = withContext(Dispatchers.IO) {
        // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.rawQuery(…)`
        val cursor = db.readableDatabase.rawQuery(
            // continues the statement started above: `"SELECT * FROM $TABLE_TRIPS WHERE $COL_TRIP_ID = ? LIMIT 1",`
            "SELECT * FROM $TABLE_TRIPS WHERE $COL_TRIP_ID = ? LIMIT 1",
            // continues the statement started above: `arrayOf(id),`
            arrayOf(id),
        // closes the multi-line argument list started above
        )
        // opens a block after `cursor.use`
        cursor.use {
            // `if` statement: executes `return@withContext null` when `!it.moveToFirst()` is true
            if (!it.moveToFirst()) return@withContext null
            // calls `TripEntity` with an argument list that continues on the following lines
            TripEntity(
                // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),`
                id          = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ID)),
                // continues the statement started above: `accountId = it.getString(it.getColumnIndexOrThrow(COL_TRIP_…`
                accountId   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_ACCOUNT_ID)),
                // continues the statement started above: `name = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)…`
                name        = it.getString(it.getColumnIndexOrThrow(COL_TRIP_NAME)),
                // continues the statement started above: `startDate = it.getString(it.getColumnIndexOrThrow(COL_TRIP_…`
                startDate   = it.getString(it.getColumnIndexOrThrow(COL_TRIP_START)),
                // continues the statement started above: `endDate = it.getString(it.getColumnIndexOrThrow(COL_TRIP_EN…`
                endDate     = it.getString(it.getColumnIndexOrThrow(COL_TRIP_END)),
                // continues the statement started above: `destination = it.getString(it.getColumnIndexOrThrow(COL_TRI…`
                destination = it.getString(it.getColumnIndexOrThrow(COL_TRIP_DESTINATION)),
                // continues the statement started above: `destLat = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx ->…`
                destLat     = it.getColumnIndex(COL_TRIP_DEST_LAT).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                // continues the statement started above: `destLng = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx ->…`
                destLng     = it.getColumnIndex(COL_TRIP_DEST_LNG).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getDouble(idx) else null },
                // continues the statement started above: `destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let {…`
                destPhotoUrl = it.getColumnIndex(COL_TRIP_DEST_PHOTO).let { idx -> if (idx >= 0 && !it.isNull(idx)) it.getString(idx) else null },
                // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_…`
                createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_CREATED)),
                // continues the statement started above: `updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_…`
                updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_UPDATED)),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the block
    }

    // declares suspend function `deleteTrip` taking 2 parameters (`id`, `accountId`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun deleteTrip(id: String, accountId: String) = withContext(Dispatchers.IO) {
        // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
        db.writableDatabase.delete(
            // continues the statement started above: `TABLE_TRIPS,`
            TABLE_TRIPS,
            // continues the statement started above: `"$COL_TRIP_ID = ? AND $COL_TRIP_ACCOUNT_ID = ?",`
            "$COL_TRIP_ID = ? AND $COL_TRIP_ACCOUNT_ID = ?",
            // continues the statement started above: `arrayOf(id, accountId),`
            arrayOf(id, accountId),
        // closes the multi-line argument list started above
        )

        // Still a hard delete locally, same as before this commit - the
        // deleted tombstone column added earlier isn't consumed here. It
        // exists for PR 2b's pull/merge logic (not written yet) to tell
        // "deleted elsewhere" apart from "not synced yet"; a device's own
        // delete of its own row doesn't need to keep that row around.
        outbox.enqueue(OutboxEntityType.TRIP, id, OutboxOperation.DELETE, null)
    // closes the block
    }

    // declares suspend function `syncTripCreate` taking 2 parameters (`idToken`, `tripId`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun syncTripCreate(idToken: String, tripId: String) = withContext(Dispatchers.IO) {
        // declares read-only property `trip`, initialised with the result of calling `getTripById(…)`
        val trip = getTripById(tripId) ?: return@withContext
        // calls `createTrip` on `WaypointApiRepository` with arguments `(idToken, trip)`
        WaypointApiRepository.createTrip(idToken, trip)
    // closes the block
    }

    // declares suspend function `syncTripUpdate` taking 2 parameters (`idToken`, `tripId`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun syncTripUpdate(idToken: String, tripId: String) = withContext(Dispatchers.IO) {
        // declares read-only property `trip`, initialised with the result of calling `getTripById(…)`
        val trip = getTripById(tripId) ?: return@withContext
        // calls `updateTrip` on `WaypointApiRepository` with arguments `(idToken, trip)`
        WaypointApiRepository.updateTrip(idToken, trip)
    // closes the block
    }

    // declares suspend function `syncTripDelete` taking 2 parameters (`idToken`, `tripId`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun syncTripDelete(idToken: String, tripId: String) = withContext(Dispatchers.IO) {
        // calls `deleteTrip` on `WaypointApiRepository` with arguments `(idToken, tripId)`
        WaypointApiRepository.deleteTrip(idToken, tripId)
    // closes the block
    }

    // declares suspend function `pullAndMergeTrips` taking 2 parameters (`idToken`, `accountId`); its body is the expression `withContext(Dispatchers.IO) {`
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

            val localUpdatedAt = localUpdatedAtMs(wdb, t.id)
            if (localUpdatedAt != null && localUpdatedAt > t.updatedAtMs) continue

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

// closes the class `TripRepository`
}
