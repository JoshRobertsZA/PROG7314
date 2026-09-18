package com.waypoint.app.core.notifications

import android.content.ContentValues
import android.content.Context
import com.waypoint.app.core.db.NotificationEntity
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ACCOUNT_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_BODY
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_CREATED
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_TITLE
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_NOTIFICATIONS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/** SQLite reads/writes for the per-account notification history. */
class NotificationRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)

    suspend fun record(accountId: String, title: String, body: String) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put(COL_NOTIF_ID,         UUID.randomUUID().toString())
            put(COL_NOTIF_ACCOUNT_ID, accountId)
            put(COL_NOTIF_TITLE,      title)
            put(COL_NOTIF_BODY,       body)
            put(COL_NOTIF_CREATED,    System.currentTimeMillis())
        }
        db.writableDatabase.insertOrThrow(TABLE_NOTIFICATIONS, null, cv)
    }

    /** Newest first. */
    suspend fun getForAccount(accountId: String): List<NotificationEntity> = withContext(Dispatchers.IO) {
        val cursor = db.readableDatabase.query(
            TABLE_NOTIFICATIONS, null,
            "$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId),
            null, null, "$COL_NOTIF_CREATED DESC",
        )
        val out = mutableListOf<NotificationEntity>()
        cursor.use {
            while (it.moveToNext()) {
                out += NotificationEntity(
                    id          = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_ID)),
                    accountId   = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_ACCOUNT_ID)),
                    title       = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_TITLE)),
                    body        = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_BODY)),
                    createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_NOTIF_CREATED)),
                )
            }
        }
        out
    }

    suspend fun clearForAccount(accountId: String) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete(TABLE_NOTIFICATIONS, "$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId))
    }
}
