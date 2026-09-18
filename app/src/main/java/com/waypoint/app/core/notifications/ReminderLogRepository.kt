package com.waypoint.app.core.notifications

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_KEY
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_SENT
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_REMINDER_LOG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Remembers which scheduled reminders have already been sent, keyed by a
 * string like "countdown:<tripId>:30" or "digest:<tripId>:2026-09-20".
 * TripReminderWorker runs hourly, so without this every rule would re-fire
 * on each run inside its window.
 */
class ReminderLogRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)

    suspend fun wasSent(key: String): Boolean = withContext(Dispatchers.IO) {
        db.readableDatabase.query(
            TABLE_REMINDER_LOG, arrayOf(COL_RLOG_KEY), "$COL_RLOG_KEY = ?", arrayOf(key),
            null, null, null, "1",
        ).use { it.moveToFirst() }
    }

    suspend fun markSent(key: String) = withContext(Dispatchers.IO) {
        val cv = ContentValues().apply {
            put(COL_RLOG_KEY, key)
            put(COL_RLOG_SENT, System.currentTimeMillis())
        }
        db.writableDatabase.insertWithOnConflict(TABLE_REMINDER_LOG, null, cv, SQLiteDatabase.CONFLICT_IGNORE)
    }
}
