package com.waypoint.app.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Single SQLiteOpenHelper for the whole app.
 * Obtain via [WaypointDbHelper.getInstance].
 *
 * Version history:
 *   1 — initial schema: accounts + trips tables
 *   2 — added dest_lat / dest_lng columns to trips
 *   3 — added itinerary tables: itinerary_days, itinerary_flights,
 *         itinerary_lodging, itinerary_car_rental, itinerary_places
 *   4 — added photo_url column to itinerary_places
 *   5 — added notifications table (per-account push history)
 */
class WaypointDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_ACCOUNTS)
        db.execSQL(CREATE_TRIPS)
        db.execSQL(CREATE_TRIPS_ACCOUNT_IDX)
        db.execSQL(CREATE_ITINERARY_DAYS)
        db.execSQL(CREATE_ITINERARY_DAYS_TRIP_IDX)
        db.execSQL(CREATE_ITINERARY_FLIGHTS)
        db.execSQL(CREATE_ITINERARY_FLIGHTS_DAY_IDX)
        db.execSQL(CREATE_ITINERARY_LODGING)
        db.execSQL(CREATE_ITINERARY_LODGING_TRIP_IDX)
        db.execSQL(CREATE_ITINERARY_CAR_RENTAL)
        db.execSQL(CREATE_ITINERARY_CAR_TRIP_IDX)
        db.execSQL(CREATE_ITINERARY_PLACES)
        db.execSQL(CREATE_ITINERARY_PLACES_DAY_IDX)
        db.execSQL(CREATE_NOTIFICATIONS)
        db.execSQL(CREATE_NOTIFICATIONS_ACCOUNT_IDX)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            // v2: destination lat/lng columns on trips.
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LAT REAL")
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LNG REAL")
        }
        if (oldVersion < 4) {
            // v4: photo_url column on itinerary_places.
            db.execSQL("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $COL_IPLACE_PHOTO_URL TEXT")
        }
        if (oldVersion < 3) {
            // v3: itinerary tables.
            db.execSQL(CREATE_ITINERARY_DAYS)
            db.execSQL(CREATE_ITINERARY_DAYS_TRIP_IDX)
            db.execSQL(CREATE_ITINERARY_FLIGHTS)
            db.execSQL(CREATE_ITINERARY_FLIGHTS_DAY_IDX)
            db.execSQL(CREATE_ITINERARY_LODGING)
            db.execSQL(CREATE_ITINERARY_LODGING_TRIP_IDX)
            db.execSQL(CREATE_ITINERARY_CAR_RENTAL)
            db.execSQL(CREATE_ITINERARY_CAR_TRIP_IDX)
            db.execSQL(CREATE_ITINERARY_PLACES)
            db.execSQL(CREATE_ITINERARY_PLACES_DAY_IDX)
        }
        if (oldVersion < 5) {
            // v5: notification history.
            db.execSQL(CREATE_NOTIFICATIONS)
            db.execSQL(CREATE_NOTIFICATIONS_ACCOUNT_IDX)
        }
    }

    companion object {
        private const val DB_NAME    = "waypoint.db"
        private const val DB_VERSION = 5

        // ── accounts ─────────────────────────────────────────────────────────
        const val TABLE_ACCOUNTS       = "accounts"
        const val COL_ACC_ID           = "id"
        const val COL_ACC_EMAIL        = "email"
        const val COL_ACC_DISPLAY_NAME = "display_name"
        const val COL_ACC_PHOTO_URL    = "photo_url"
        const val COL_ACC_LAST_LOGIN   = "last_login_ms"

        // ── trips ─────────────────────────────────────────────────────────────
        const val TABLE_TRIPS          = "trips"
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

        // ── itinerary_days ────────────────────────────────────────────────────
        const val TABLE_ITIN_DAYS      = "itinerary_days"
        const val COL_IDAY_ID          = "id"
        const val COL_IDAY_TRIP_ID     = "trip_id"
        const val COL_IDAY_DATE        = "date"          // yyyy-MM-dd

        // ── itinerary_flights ─────────────────────────────────────────────────
        const val TABLE_ITIN_FLIGHTS   = "itinerary_flights"
        const val COL_IFLIGHT_ID            = "id"
        const val COL_IFLIGHT_DAY_ID        = "day_id"
        const val COL_IFLIGHT_FLIGHT_NUMBER = "flight_number"  // nullable
        const val COL_IFLIGHT_PDF_URI       = "pdf_uri"
        const val COL_IFLIGHT_CREATED       = "created_at_ms"

        // ── itinerary_lodging ─────────────────────────────────────────────────
        const val TABLE_ITIN_LODGING   = "itinerary_lodging"
        const val COL_ILODGE_ID        = "id"
        const val COL_ILODGE_TRIP_ID   = "trip_id"
        const val COL_ILODGE_FROM_DATE = "from_date"    // yyyy-MM-dd
        const val COL_ILODGE_TO_DATE   = "to_date"      // yyyy-MM-dd
        const val COL_ILODGE_PDF_URI   = "pdf_uri"
        const val COL_ILODGE_CREATED   = "created_at_ms"

        // ── itinerary_car_rental ──────────────────────────────────────────────
        const val TABLE_ITIN_CAR       = "itinerary_car_rental"
        const val COL_ICAR_ID          = "id"
        const val COL_ICAR_TRIP_ID     = "trip_id"
        const val COL_ICAR_FROM_DATE   = "from_date"    // yyyy-MM-dd
        const val COL_ICAR_TO_DATE     = "to_date"      // yyyy-MM-dd
        const val COL_ICAR_PDF_URI     = "pdf_uri"
        const val COL_ICAR_CREATED     = "created_at_ms"

        // ── itinerary_places ──────────────────────────────────────────────────
        // place_category maps to ExploreFilter values: HOTELS, PARKS, PUBS, CINEMAS
        const val TABLE_ITIN_PLACES       = "itinerary_places"
        const val COL_IPLACE_ID           = "id"
        const val COL_IPLACE_DAY_ID       = "day_id"
        const val COL_IPLACE_NAME         = "place_name"
        const val COL_IPLACE_CATEGORY     = "place_category"
        const val COL_IPLACE_LAT          = "place_lat"   // nullable
        const val COL_IPLACE_LNG          = "place_lng"   // nullable
        const val COL_IPLACE_NOTE         = "note"        // nullable
        const val COL_IPLACE_CREATED      = "created_at_ms"
        const val COL_IPLACE_PHOTO_URL    = "photo_url"  // nullable

        // ── notifications ─────────────────────────────────────────────────────
        const val TABLE_NOTIFICATIONS  = "notifications"
        const val COL_NOTIF_ID         = "id"
        const val COL_NOTIF_ACCOUNT_ID = "account_id"
        const val COL_NOTIF_TITLE      = "title"
        const val COL_NOTIF_BODY       = "body"
        const val COL_NOTIF_CREATED    = "created_at_ms"

        // ── CREATE statements ─────────────────────────────────────────────────

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

        private const val CREATE_TRIPS_ACCOUNT_IDX =
            "CREATE INDEX idx_trips_account_id ON $TABLE_TRIPS($COL_TRIP_ACCOUNT_ID)"

        private val CREATE_ITINERARY_DAYS = """
            CREATE TABLE $TABLE_ITIN_DAYS (
                $COL_IDAY_ID      TEXT PRIMARY KEY,
                $COL_IDAY_TRIP_ID TEXT NOT NULL,
                $COL_IDAY_DATE    TEXT NOT NULL,
                FOREIGN KEY ($COL_IDAY_TRIP_ID) REFERENCES $TABLE_TRIPS($COL_TRIP_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        private const val CREATE_ITINERARY_DAYS_TRIP_IDX =
            "CREATE INDEX idx_itin_days_trip ON $TABLE_ITIN_DAYS($COL_IDAY_TRIP_ID)"

        private val CREATE_ITINERARY_FLIGHTS = """
            CREATE TABLE $TABLE_ITIN_FLIGHTS (
                $COL_IFLIGHT_ID            TEXT PRIMARY KEY,
                $COL_IFLIGHT_DAY_ID        TEXT NOT NULL,
                $COL_IFLIGHT_FLIGHT_NUMBER TEXT,
                $COL_IFLIGHT_PDF_URI       TEXT NOT NULL,
                $COL_IFLIGHT_CREATED       INTEGER NOT NULL,
                FOREIGN KEY ($COL_IFLIGHT_DAY_ID) REFERENCES $TABLE_ITIN_DAYS($COL_IDAY_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        private const val CREATE_ITINERARY_FLIGHTS_DAY_IDX =
            "CREATE INDEX idx_itin_flights_day ON $TABLE_ITIN_FLIGHTS($COL_IFLIGHT_DAY_ID)"

        private val CREATE_ITINERARY_LODGING = """
            CREATE TABLE $TABLE_ITIN_LODGING (
                $COL_ILODGE_ID        TEXT PRIMARY KEY,
                $COL_ILODGE_TRIP_ID   TEXT NOT NULL,
                $COL_ILODGE_FROM_DATE TEXT NOT NULL,
                $COL_ILODGE_TO_DATE   TEXT NOT NULL,
                $COL_ILODGE_PDF_URI   TEXT NOT NULL,
                $COL_ILODGE_CREATED   INTEGER NOT NULL,
                FOREIGN KEY ($COL_ILODGE_TRIP_ID) REFERENCES $TABLE_TRIPS($COL_TRIP_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        private const val CREATE_ITINERARY_LODGING_TRIP_IDX =
            "CREATE INDEX idx_itin_lodging_trip ON $TABLE_ITIN_LODGING($COL_ILODGE_TRIP_ID)"

        private val CREATE_ITINERARY_CAR_RENTAL = """
            CREATE TABLE $TABLE_ITIN_CAR (
                $COL_ICAR_ID        TEXT PRIMARY KEY,
                $COL_ICAR_TRIP_ID   TEXT NOT NULL,
                $COL_ICAR_FROM_DATE TEXT NOT NULL,
                $COL_ICAR_TO_DATE   TEXT NOT NULL,
                $COL_ICAR_PDF_URI   TEXT NOT NULL,
                $COL_ICAR_CREATED   INTEGER NOT NULL,
                FOREIGN KEY ($COL_ICAR_TRIP_ID) REFERENCES $TABLE_TRIPS($COL_TRIP_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        private const val CREATE_ITINERARY_CAR_TRIP_IDX =
            "CREATE INDEX idx_itin_car_trip ON $TABLE_ITIN_CAR($COL_ICAR_TRIP_ID)"

        private val CREATE_ITINERARY_PLACES = """
            CREATE TABLE $TABLE_ITIN_PLACES (
                $COL_IPLACE_ID       TEXT PRIMARY KEY,
                $COL_IPLACE_DAY_ID   TEXT NOT NULL,
                $COL_IPLACE_NAME     TEXT NOT NULL,
                $COL_IPLACE_CATEGORY TEXT NOT NULL,
                $COL_IPLACE_LAT      REAL,
                $COL_IPLACE_LNG      REAL,
                $COL_IPLACE_NOTE     TEXT,
                $COL_IPLACE_PHOTO_URL TEXT,
                $COL_IPLACE_CREATED  INTEGER NOT NULL,
                FOREIGN KEY ($COL_IPLACE_DAY_ID) REFERENCES $TABLE_ITIN_DAYS($COL_IDAY_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        private const val CREATE_ITINERARY_PLACES_DAY_IDX =
            "CREATE INDEX idx_itin_places_day ON $TABLE_ITIN_PLACES($COL_IPLACE_DAY_ID)"

        private val CREATE_NOTIFICATIONS = """
            CREATE TABLE $TABLE_NOTIFICATIONS (
                $COL_NOTIF_ID         TEXT PRIMARY KEY,
                $COL_NOTIF_ACCOUNT_ID TEXT NOT NULL,
                $COL_NOTIF_TITLE      TEXT NOT NULL,
                $COL_NOTIF_BODY       TEXT NOT NULL,
                $COL_NOTIF_CREATED    INTEGER NOT NULL
            )
        """.trimIndent()

        private const val CREATE_NOTIFICATIONS_ACCOUNT_IDX =
            "CREATE INDEX idx_notifications_account ON $TABLE_NOTIFICATIONS($COL_NOTIF_ACCOUNT_ID)"

        @Volatile private var instance: WaypointDbHelper? = null

        fun getInstance(context: Context): WaypointDbHelper =
            instance ?: synchronized(this) {
                instance ?: WaypointDbHelper(context).also { instance = it }
            }
    }
}
