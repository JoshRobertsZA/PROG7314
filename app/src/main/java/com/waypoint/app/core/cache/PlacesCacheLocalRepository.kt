package com.waypoint.app.core.cache

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_PCACHE_ACCOUNT_ID
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_PCACHE_JSON
import com.waypoint.app.core.db.WaypointDbHelper.Companion.COL_PCACHE_KEY
import com.waypoint.app.core.db.WaypointDbHelper.Companion.TABLE_PLACES_CACHE
import org.json.JSONObject

// Nearby-places results are tied to one user's exact GPS fix or search term,
// so unlike weather/currency they were never a good fit for the cache every
// user shares through the API. LocationIQRepository reads this before
// falling back to that shared cache, and no longer writes to the shared
// cache at all (PR 12 commit 4) - a fresh fetch only lands here.
class PlacesCacheLocalRepository(context: Context) {

    private val dbHelper = WaypointDbHelper.getInstance(context)

    fun get(accountId: String, cacheKey: String): PlacesCache? {
        dbHelper.readableDatabase.query(
            TABLE_PLACES_CACHE,
            arrayOf(COL_PCACHE_JSON),
            "$COL_PCACHE_ACCOUNT_ID = ? AND $COL_PCACHE_KEY = ?",
            arrayOf(accountId, cacheKey),
            null, null, null,
        ).use { cursor ->
            if (!cursor.moveToFirst()) return null
            return PlacesCacheSerializer.fromJson(JSONObject(cursor.getString(0)))
        }
    }

    fun put(accountId: String, cacheKey: String, data: PlacesCache) {
        val values = ContentValues().apply {
            put(COL_PCACHE_ACCOUNT_ID, accountId)
            put(COL_PCACHE_KEY, cacheKey)
            put(COL_PCACHE_JSON, PlacesCacheSerializer.toJson(data).toString())
        }
        dbHelper.writableDatabase.insertWithOnConflict(
            TABLE_PLACES_CACHE, null, values, SQLiteDatabase.CONFLICT_REPLACE,
        )
    }
}
