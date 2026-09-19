// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.ContentValues` for use in this file
import android.content.ContentValues
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.database.sqlite.SQLiteDatabase` for use in this file
import android.database.sqlite.SQLiteDatabase
// imports `com.waypoint.app.core.db.WaypointDbHelper` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_KEY` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_KEY
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_SENT` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_RLOG_SENT
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_REMINDER_LOG` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_REMINDER_LOG
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext

// declares class `ReminderLogRepository` with a primary constructor taking 1 parameter (`context`) and opens its body
class ReminderLogRepository(context: Context) {

    // declares private read-only property `db`, initialised with the result of calling `WaypointDbHelper.getInstance(…)`
    private val db = WaypointDbHelper.getInstance(context)

    // declares suspend function `wasSent` taking 1 parameter (`key`), returning `Boolean`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun wasSent(key: String): Boolean = withContext(Dispatchers.IO) {
        // calls `query` on `db.readableDatabase` with an argument list that continues on the following lines
        db.readableDatabase.query(
            // continues the statement started above: `TABLE_REMINDER_LOG, arrayOf(COL_RLOG_KEY), "$COL_RLOG_KEY =…`
            TABLE_REMINDER_LOG, arrayOf(COL_RLOG_KEY), "$COL_RLOG_KEY = ?", arrayOf(key),
            // continues the statement started above: `null, null, null, "1",`
            null, null, null, "1",
        // continues the statement started above: `).use { it.moveToFirst() }`
        ).use { it.moveToFirst() }
    // closes the block
    }

    // declares suspend function `markSent` taking 1 parameter (`key`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun markSent(key: String) = withContext(Dispatchers.IO) {
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(COL_RLOG_KEY, key)`
            put(COL_RLOG_KEY, key)
            // calls `put` with arguments `(COL_RLOG_SENT, System.currentTimeMillis())`
            put(COL_RLOG_SENT, System.currentTimeMillis())
        // closes the lambda assigned to `cv`
        }
        // calls `insertWithOnConflict` on `db.writableDatabase` with arguments `(TABLE_REMINDER_LOG, null, cv, SQLiteDatabase…)`
        db.writableDatabase.insertWithOnConflict(TABLE_REMINDER_LOG, null, cv, SQLiteDatabase.CONFLICT_IGNORE)
    // closes the block
    }
// closes the class `ReminderLogRepository`
}
