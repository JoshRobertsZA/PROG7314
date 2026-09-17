package com.example.prog7314.features.newtrip.data

import android.content.ContentValues
import android.content.Context
import com.example.prog7314.core.db.AccountEntity
import com.example.prog7314.core.db.TripEntity
import com.example.prog7314.core.db.WaypointDbHelper
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_ACC_DISPLAY_NAME
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_ACC_EMAIL
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_ACC_ID
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_ACC_LAST_LOGIN
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_ACC_PHOTO_URL
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_ACCOUNT_ID
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_CREATED
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_DESTINATION
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_END
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_ID
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_NAME
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_START
import com.example.prog7314.core.db.WaypointDbHelper.Companion.COL_TRIP_UPDATED
import com.example.prog7314.core.db.WaypointDbHelper.Companion.TABLE_ACCOUNTS
import com.example.prog7314.core.db.WaypointDbHelper.Companion.TABLE_TRIPS
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

/**
 * All SQLite reads and writes for trips and accounts.
 * Every public function dispatches to [Dispatchers.IO] so callers can use
 * it safely from a coroutine launched on Main.
 */
class TripRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)

    // ── Accounts ──────────────────────────────────────────────────────────────

    /** Insert or replace an account row (called after Google SSO sign-in). */
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

    // ── Trips ─────────────────────────────────────────────────────────────────

    /**
     * Insert a new trip for [accountId].
     * Returns the generated UUID that was used as the row id.
     */
    suspend fun insertTrip(
        accountId: String,
        name: String,
        startDate: String,
        endDate: String,
        destination: String? = null,
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
            put(COL_TRIP_CREATED,     now)
            put(COL_TRIP_UPDATED,     now)
        }
        db.writableDatabase.insertOrThrow(TABLE_TRIPS, null, cv)
        id
    }

    /**
     * All trips for one Google account, newest first.
     */
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
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_CREATED)),
                        updatedAtMs = it.getLong(it.getColumnIndexOrThrow(COL_TRIP_UPDATED)),
                    )
                }
            }
            trips
        }

    /**
     * Delete a single trip by id (only if it belongs to [accountId]).
     */
    suspend fun deleteTrip(id: String, accountId: String) = withContext(Dispatchers.IO) {
        db.writableDatabase.delete(
            TABLE_TRIPS,
            "$COL_TRIP_ID = ? AND $COL_TRIP_ACCOUNT_ID = ?",
            arrayOf(id, accountId),
        )
    }
}
