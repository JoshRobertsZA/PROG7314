// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.ContentValues` for use in this file
import android.content.ContentValues
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `com.waypoint.app.core.db.NotificationEntity` for use in this file
import com.waypoint.app.core.db.NotificationEntity
// imports `com.waypoint.app.core.db.WaypointDbHelper` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ACCOUNT_ID` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ACCOUNT_ID
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_BODY` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_BODY
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_CREATED` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_CREATED
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ID` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_ID
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_TITLE` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_NOTIF_TITLE
// imports `com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_NOTIFICATIONS` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_NOTIFICATIONS
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `java.util.UUID` for use in this file
import java.util.UUID

// declares class `NotificationRepository` with a primary constructor taking 1 parameter (`context`) and opens its body
class NotificationRepository(context: Context) {

    // declares private read-only property `db`, initialised with the result of calling `WaypointDbHelper.getInstance(…)`
    private val db = WaypointDbHelper.getInstance(context)

    // declares suspend function `record` taking 3 parameters (`accountId`, `title`, `body`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun record(accountId: String, title: String, body: String) = withContext(Dispatchers.IO) {
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(COL_NOTIF_ID, UUID.randomUUID().toString())`
            put(COL_NOTIF_ID,         UUID.randomUUID().toString())
            // calls `put` with arguments `(COL_NOTIF_ACCOUNT_ID, accountId)`
            put(COL_NOTIF_ACCOUNT_ID, accountId)
            // calls `put` with arguments `(COL_NOTIF_TITLE, title)`
            put(COL_NOTIF_TITLE,      title)
            // calls `put` with arguments `(COL_NOTIF_BODY, body)`
            put(COL_NOTIF_BODY,       body)
            // calls `put` with arguments `(COL_NOTIF_CREATED, System.currentTimeMillis())`
            put(COL_NOTIF_CREATED,    System.currentTimeMillis())
        // closes the lambda assigned to `cv`
        }
        // calls `insertOrThrow` on `db.writableDatabase` with arguments `(TABLE_NOTIFICATIONS, null, cv)`
        db.writableDatabase.insertOrThrow(TABLE_NOTIFICATIONS, null, cv)
    // closes the block
    }

    // declares suspend function `getForAccount` taking 1 parameter (`accountId`), returning `List<NotificationEntity>`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun getForAccount(accountId: String): List<NotificationEntity> = withContext(Dispatchers.IO) {
        // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
        val cursor = db.readableDatabase.query(
            // continues the statement started above: `TABLE_NOTIFICATIONS, null,`
            TABLE_NOTIFICATIONS, null,
            // continues the statement started above: `"$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId),`
            "$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId),
            // continues the statement started above: `null, null, "$COL_NOTIF_CREATED DESC",`
            null, null, "$COL_NOTIF_CREATED DESC",
        // closes the multi-line argument list started above
        )
        // declares read-only property `out`, initialised with the result of calling `mutableListOf(…)`
        val out = mutableListOf<NotificationEntity>()
        // opens a block after `cursor.use`
        cursor.use {
            // `while` loop: repeats the block below as long as `it.moveToNext()` is true
            while (it.moveToNext()) {
                // adds to `out` the value `NotificationEntity(`
                out += NotificationEntity(
                    // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_ID)),`
                    id          = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_ID)),
                    // continues the statement started above: `accountId = it.getString(it.getColumnIndexOrThrow(COL_NOTIF…`
                    accountId   = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_ACCOUNT_ID)),
                    // continues the statement started above: `title = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_TIT…`
                    title       = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_TITLE)),
                    // continues the statement started above: `body = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_BODY…`
                    body        = it.getString(it.getColumnIndexOrThrow(COL_NOTIF_BODY)),
                    // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_NOTIF…`
                    createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_NOTIF_CREATED)),
                // closes the multi-line argument list started above
                )
            // closes the while loop
            }
        // closes the block
        }
        // expression: `out`
        out
    // closes the block
    }

    // declares suspend function `clearForAccount` taking 1 parameter (`accountId`); its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun clearForAccount(accountId: String) = withContext(Dispatchers.IO) {
        // calls `delete` on `db.writableDatabase` with arguments `(TABLE_NOTIFICATIONS, "$COL_NOTIF_ACCOUNT_ID …)`
        db.writableDatabase.delete(TABLE_NOTIFICATIONS, "$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId))
    // closes the block
    }
// closes the class `NotificationRepository`
}
