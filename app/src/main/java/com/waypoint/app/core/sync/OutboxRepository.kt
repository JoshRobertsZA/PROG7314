package com.waypoint.app.core.sync

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_ATTEMPTS
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_CREATED
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_ENTITY_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_ENTITY_TYPE
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_LAST_ERROR
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_OPERATION
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_OUTBOX_PAYLOAD_JSON
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_SYNC_OUTBOX
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.util.UUID

enum class OutboxEntityType {
    TRIP, DAY, FLIGHT, LODGING, CAR, PLACE;

    // Parent-before-child ordering for peekAll(): a day can't be created on
    // the server before its trip exists there, and a flight/lodging/car/
    // place can't exist before its day or trip does.
    val syncDepth: Int
        get() = when (this) {
            TRIP -> 0
            DAY -> 1
            FLIGHT, LODGING, CAR, PLACE -> 2
        }
}

enum class OutboxOperation { CREATE, UPDATE, DELETE }

data class OutboxEntry(
    val id: String,
    val entityType: OutboxEntityType,
    val entityId: String,
    val operation: OutboxOperation,
    val payloadJson: String?,
    val createdAtMs: Long,
    val attempts: Int,
    val lastError: String?,
)

// Durable queue of local changes waiting to reach the API. One row per
// entity with a pending change - enqueue() coalesces a new operation with
// whatever is already queued for that entity instead of appending a
// second row, so an edit-edit-edit sequence sends one network call, not
// three, and a create-then-delete before the create ever syncs sends
// nothing to the server at all.
class OutboxRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)

    suspend fun enqueue(
        entityType: OutboxEntityType,
        entityId: String,
        operation: OutboxOperation,
        payloadJson: String?,
    ) = withContext(Dispatchers.IO) {
        val wdb = db.writableDatabase
        wdb.beginTransaction()
        try {
            val existing = findPending(wdb, entityType, entityId)
            val resolved = coalesce(existing, operation, payloadJson)

            if (existing != null) {
                wdb.delete(TABLE_SYNC_OUTBOX, "$COL_OUTBOX_ID = ?", arrayOf(existing.id))
            }
            if (resolved != null) {
                val cv = ContentValues().apply {
                    put(COL_OUTBOX_ID, UUID.randomUUID().toString())
                    put(COL_OUTBOX_ENTITY_TYPE, entityType.name)
                    put(COL_OUTBOX_ENTITY_ID, entityId)
                    put(COL_OUTBOX_OPERATION, resolved.first.name)
                    put(COL_OUTBOX_PAYLOAD_JSON, resolved.second)
                    // keep the original queue position so an edited entry
                    // doesn't jump the line ahead of older unrelated ones
                    put(COL_OUTBOX_CREATED, existing?.createdAtMs ?: System.currentTimeMillis())
                    put(COL_OUTBOX_ATTEMPTS, 0)
                }
                wdb.insertOrThrow(TABLE_SYNC_OUTBOX, null, cv)
            }
            wdb.setTransactionSuccessful()
        } finally {
            wdb.endTransaction()
        }
    }

    /** All pending entries, parent entity types first, then oldest first within a type. */
    suspend fun peekAll(): List<OutboxEntry> = withContext(Dispatchers.IO) {
        val entries = mutableListOf<OutboxEntry>()
        db.readableDatabase.query(
            TABLE_SYNC_OUTBOX, null, null, null, null, null,
            "$COL_OUTBOX_CREATED ASC",
        ).use { cursor ->
            while (cursor.moveToNext()) entries += cursor.toOutboxEntry()
        }
        entries.sortedBy { it.entityType.syncDepth }
    }

    suspend fun ack(entryId: String) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete(TABLE_SYNC_OUTBOX, "$COL_OUTBOX_ID = ?", arrayOf(entryId))
    }

    // Used by pull/merge (ItineraryRepository, TripRepository): an entity
    // with an unsent local change must never be overwritten or deleted by
    // incoming cloud data, since the cloud doesn't know about that change
    // yet - the local change is, by definition, the more recent one.
    suspend fun pendingEntityIds(entityType: OutboxEntityType): Set<String> = withContext(Dispatchers.IO) {
        val ids = mutableSetOf<String>()
        db.readableDatabase.query(
            TABLE_SYNC_OUTBOX, arrayOf(COL_OUTBOX_ENTITY_ID),
            "$COL_OUTBOX_ENTITY_TYPE = ?", arrayOf(entityType.name), null, null, null,
        ).use { cursor -> while (cursor.moveToNext()) ids += cursor.getString(0) }
        ids
    }

    suspend fun markFailed(entryId: String, error: String) = withContext(Dispatchers.IO) {
        val wdb = db.writableDatabase
        wdb.beginTransaction()
        try {
            val attempts = currentAttempts(wdb, entryId)
            val cv = ContentValues().apply {
                put(COL_OUTBOX_ATTEMPTS, attempts + 1)
                put(COL_OUTBOX_LAST_ERROR, error)
            }
            wdb.update(TABLE_SYNC_OUTBOX, cv, "$COL_OUTBOX_ID = ?", arrayOf(entryId))
            wdb.setTransactionSuccessful()
        } finally {
            wdb.endTransaction()
        }
    }

    // Coalescing rules (PR 2a commit 3 in the project plan):
    //   CREATE + UPDATE -> CREATE, with the update's fields merged into the
    //                       create's payload (the cloud only ever sees one
    //                       create call, carrying the latest field values)
    //   CREATE + DELETE -> cancelled entirely: the entity never reached the
    //                       cloud, so there is nothing there to delete
    //   UPDATE + UPDATE -> UPDATE, merged the same way as CREATE + UPDATE
    //                       (newer field values win, older fields not
    //                       touched by the newer call are kept - two
    //                       partial updates to different fields of the
    //                       same entity must not clobber each other)
    //   UPDATE + DELETE -> DELETE (the update is moot once the row is gone)
    //   nothing queued  -> the new operation, unchanged
    // A DELETE followed by another operation on the same id isn't a real
    // case today (ids are fresh UUIDs per create, never reused for a new
    // entity), so it has no rule here; the fallback is "newest operation
    // wins," logged as unexpected rather than silently assumed correct.
    private fun coalesce(
        existing: OutboxEntry?,
        newOp: OutboxOperation,
        newPayload: String?,
    ): Pair<OutboxOperation, String?>? {
        if (existing == null) return newOp to newPayload

        return when (existing.operation to newOp) {
            OutboxOperation.CREATE to OutboxOperation.UPDATE ->
                OutboxOperation.CREATE to mergeJson(existing.payloadJson, newPayload)
            OutboxOperation.CREATE to OutboxOperation.DELETE ->
                null
            OutboxOperation.UPDATE to OutboxOperation.UPDATE ->
                OutboxOperation.UPDATE to mergeJson(existing.payloadJson, newPayload)
            OutboxOperation.UPDATE to OutboxOperation.DELETE ->
                OutboxOperation.DELETE to null
            else -> {
                Log.w(
                    TAG,
                    "Unexpected outbox transition ${existing.operation} -> $newOp " +
                        "for ${existing.entityType}/${existing.entityId}; keeping the newest operation",
                )
                newOp to newPayload
            }
        }
    }

    private fun mergeJson(base: String?, updates: String?): String? {
        if (updates == null) return base
        if (base == null) return updates
        val merged = JSONObject(base)
        val updateObj = JSONObject(updates)
        updateObj.keys().forEach { key -> merged.put(key, updateObj.get(key)) }
        return merged.toString()
    }

    private fun findPending(wdb: SQLiteDatabase, entityType: OutboxEntityType, entityId: String): OutboxEntry? {
        wdb.query(
            TABLE_SYNC_OUTBOX,
            null,
            "$COL_OUTBOX_ENTITY_TYPE = ? AND $COL_OUTBOX_ENTITY_ID = ?",
            arrayOf(entityType.name, entityId),
            null, null, null,
        ).use { cursor ->
            if (!cursor.moveToFirst()) return null
            return cursor.toOutboxEntry()
        }
    }

    private fun currentAttempts(wdb: SQLiteDatabase, entryId: String): Int {
        wdb.query(
            TABLE_SYNC_OUTBOX, arrayOf(COL_OUTBOX_ATTEMPTS),
            "$COL_OUTBOX_ID = ?", arrayOf(entryId), null, null, null,
        ).use { cursor ->
            return if (cursor.moveToFirst()) cursor.getInt(0) else 0
        }
    }

    private fun Cursor.toOutboxEntry(): OutboxEntry {
        val payloadIdx = getColumnIndex(COL_OUTBOX_PAYLOAD_JSON)
        val lastErrorIdx = getColumnIndex(COL_OUTBOX_LAST_ERROR)
        return OutboxEntry(
            id = getString(getColumnIndexOrThrow(COL_OUTBOX_ID)),
            entityType = OutboxEntityType.valueOf(getString(getColumnIndexOrThrow(COL_OUTBOX_ENTITY_TYPE))),
            entityId = getString(getColumnIndexOrThrow(COL_OUTBOX_ENTITY_ID)),
            operation = OutboxOperation.valueOf(getString(getColumnIndexOrThrow(COL_OUTBOX_OPERATION))),
            payloadJson = if (payloadIdx >= 0 && !isNull(payloadIdx)) getString(payloadIdx) else null,
            createdAtMs = getLong(getColumnIndexOrThrow(COL_OUTBOX_CREATED)),
            attempts = getInt(getColumnIndexOrThrow(COL_OUTBOX_ATTEMPTS)),
            lastError = if (lastErrorIdx >= 0 && !isNull(lastErrorIdx)) getString(lastErrorIdx) else null,
        )
    }

    private companion object {
        const val TAG = "OutboxRepository"
    }
}
