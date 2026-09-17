package com.example.prog7314.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Single SQLiteOpenHelper for the whole app.
 * Obtain via [WaypointDbHelper.getInstance].
 *
 * Version history:
 *   1 — initial schema: accounts + trips tables
 */
class WaypointDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ACCOUNTS)
        db.execSQL(CREATE_TRIPS)
        db.execSQL(CREATE_TRIPS_ACCOUNT_IDX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // Add destination lat/lng columns introduced in version 2.
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LAT REAL")
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LNG REAL")
        }
    }

    companion object {
        private const val DB_NAME    = "waypoint.db"
        private const val DB_VERSION = 2

        const val TABLE_ACCOUNTS = "accounts"
        const val COL_ACC_ID           = "id"
        const val COL_ACC_EMAIL        = "email"
        const val COL_ACC_DISPLAY_NAME = "display_name"
        const val COL_ACC_PHOTO_URL    = "photo_url"
        const val COL_ACC_LAST_LOGIN   = "last_login_ms"

        const val TABLE_TRIPS = "trips"
        const val COL_TRIP_ID          = "id"
        const val COL_TRIP_ACCOUNT_ID  = "account_id"
        const val COL_TRIP_NAME        = "name"
        const val COL_TRIP_START       = "start_date"
        const val COL_TRIP_END         = "end_date"
        const val COL_TRIP_DESTINATION = "destination"
        const val COL_TRIP_CREATED     = "created_at_ms"
        const val COL_TRIP_UPDATED     = "updated_at_ms"
        const val COL_TRIP_DEST_LAT    = "dest_lat"
        const val COL_TRIP_DEST_LNG    = "dest_lng"

        private val CREATE_ACCOUNTS = """
            CREATE TABLE $TABLE_ACCOUNTS (
                $COL_ACC_ID           TEXT PRIMARY KEY,
                $COL_ACC_EMAIL        TEXT NOT NULL,
                $COL_ACC_DISPLAY_NAME TEXT NOT NULL DEFAULT '',
                $COL_ACC_PHOTO_URL    TEXT NOT NULL DEFAULT '',
                $COL_ACC_LAST_LOGIN   INTEGER NOT NULL
            )
        """.trimIndent()

        private val CREATE_TRIPS = """
            CREATE TABLE $TABLE_TRIPS (
                $COL_TRIP_ID          TEXT PRIMARY KEY,
                $COL_TRIP_ACCOUNT_ID  TEXT NOT NULL,
                $COL_TRIP_NAME        TEXT NOT NULL,
                $COL_TRIP_START       TEXT NOT NULL,
                $COL_TRIP_END         TEXT NOT NULL,
                $COL_TRIP_DESTINATION TEXT,
                $COL_TRIP_DEST_LAT    REAL,
                $COL_TRIP_DEST_LNG    REAL,
                $COL_TRIP_CREATED     INTEGER NOT NULL,
                $COL_TRIP_UPDATED     INTEGER NOT NULL,
                FOREIGN KEY ($COL_TRIP_ACCOUNT_ID) REFERENCES $TABLE_ACCOUNTS($COL_ACC_ID)
            )
        """.trimIndent()

        // Index so "trips for this account" queries are fast
        private const val CREATE_TRIPS_ACCOUNT_IDX =
            "CREATE INDEX idx_trips_account_id ON $TABLE_TRIPS($COL_TRIP_ACCOUNT_ID)"

        @Volatile private var instance: WaypointDbHelper? = null

        fun getInstance(context: Context): WaypointDbHelper =
            instance ?: synchronized(this) {
                instance ?: WaypointDbHelper(context).also { instance = it }
            }
    }
}
