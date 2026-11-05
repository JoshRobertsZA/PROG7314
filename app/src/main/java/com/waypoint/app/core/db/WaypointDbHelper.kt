package com.waypoint.app.core.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * SQLite database for everything stored on the phone: accounts, trips, the itinerary tables,
 * notification history, the reminder log and the sync outbox. A single instance is shared (see
 * [getInstance]); migrations run in [onUpgrade].
 */
class WaypointDbHelper private constructor(context: Context) :
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    override fun onConfigure(db: SQLiteDatabase) {
        super.onConfigure(db)
        db.setForeignKeyConstraintsEnabled(true)
    }

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
        db.execSQL(CREATE_REMINDER_LOG)
        applyV9Migration(db)
        applyV10Migration(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LAT REAL")
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LNG REAL")
        }
        if (oldVersion < 4) {
            db.execSQL("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $COL_IPLACE_PHOTO_URL TEXT")
        }
        if (oldVersion < 3) {
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
            db.execSQL(CREATE_NOTIFICATIONS)
            db.execSQL(CREATE_NOTIFICATIONS_ACCOUNT_IDX)
        }
        if (oldVersion < 6) {
            db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_DEPARTURE TEXT")
            db.execSQL(CREATE_REMINDER_LOG)
        }
        if (oldVersion < 7) {
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_PHOTO TEXT")
        }
        if (oldVersion < 8) {
            db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_DOC_NAME TEXT")
            db.execSQL("ALTER TABLE $TABLE_ITIN_LODGING ADD COLUMN $COL_ILODGE_DOC_NAME TEXT")
            db.execSQL("ALTER TABLE $TABLE_ITIN_CAR ADD COLUMN $COL_ICAR_DOC_NAME TEXT")
        }
        if (oldVersion < 9) {
            applyV9Migration(db)
        }
        if (oldVersion < 10) {
            applyV10Migration(db)
        }
    }

    // Shared by onCreate (fresh installs, right after the base CREATE_*
    // calls) and onUpgrade's `oldVersion < 9` branch, so the two paths can
    // never apply these statements a different number of times and collide
    // on a duplicate column/table - unlike the older migrations above,
    // which bake added columns directly into their CREATE_* constant and
    // rely on onUpgrade's historical if-blocks never re-adding the same
    // column for a device jumping multiple versions at once.
    private fun applyV9Migration(db: SQLiteDatabase) {
        db.execSQL(CREATE_SYNC_OUTBOX)
        db.execSQL(CREATE_SYNC_OUTBOX_ENTITY_IDX)
        db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DELETED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_DAYS ADD COLUMN $COL_IDAY_UPDATED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_DAYS ADD COLUMN $COL_IDAY_DELETED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_UPDATED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_DELETED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_LODGING ADD COLUMN $COL_ILODGE_UPDATED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_LODGING ADD COLUMN $COL_ILODGE_DELETED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_CAR ADD COLUMN $COL_ICAR_UPDATED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_CAR ADD COLUMN $COL_ICAR_DELETED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $COL_IPLACE_UPDATED INTEGER NOT NULL DEFAULT 0")
        db.execSQL("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $COL_IPLACE_DELETED INTEGER NOT NULL DEFAULT 0")
    }

    // A brand-new table, so unlike applyV9Migration this never needs an
    // ALTER TABLE - it's safe to run unconditionally from onCreate too.
    private fun applyV10Migration(db: SQLiteDatabase) {
        db.execSQL(CREATE_PLACES_CACHE)
    }

    // Removes everything stored on this device for one account, used after the
    // account is deleted. Trips take their itinerary rows with them (ON DELETE
    // CASCADE). The outbox has no account column, so all of it is cleared: it only
    // ever holds changes made while signed in as the account that was just deleted.
    fun deleteAccountData(accountId: String) {
        val wdb = writableDatabase
        wdb.beginTransaction()
        try {
            wdb.delete(TABLE_TRIPS, "$COL_TRIP_ACCOUNT_ID = ?", arrayOf(accountId))
            wdb.delete(TABLE_NOTIFICATIONS, "$COL_NOTIF_ACCOUNT_ID = ?", arrayOf(accountId))
            wdb.delete(TABLE_PLACES_CACHE, "$COL_PCACHE_ACCOUNT_ID = ?", arrayOf(accountId))
            wdb.delete(TABLE_SYNC_OUTBOX, null, null)
            wdb.delete(TABLE_ACCOUNTS, "$COL_ACC_ID = ?", arrayOf(accountId))
            wdb.setTransactionSuccessful()
        } finally {
            wdb.endTransaction()
        }
    }

    companion object {
        private const val DB_NAME    = "waypoint.db"
        private const val DB_VERSION = 10

        const val TABLE_ACCOUNTS       = "accounts"
        const val COL_ACC_ID           = "id"
        const val COL_ACC_EMAIL        = "email"
        const val COL_ACC_DISPLAY_NAME = "display_name"
        const val COL_ACC_PHOTO_URL    = "photo_url"
        const val COL_ACC_LAST_LOGIN   = "last_login_ms"

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
        const val COL_TRIP_DEST_PHOTO  = "dest_photo_url"
        // Tombstone for sync: 0/1. Trips already has updated_at_ms (v1), so
        // only the delete flag is new here - see applyV9Migration.
        const val COL_TRIP_DELETED     = "deleted"

        const val TABLE_ITIN_DAYS      = "itinerary_days"
        const val COL_IDAY_ID          = "id"
        const val COL_IDAY_TRIP_ID     = "trip_id"
        const val COL_IDAY_DATE        = "date"
        const val COL_IDAY_UPDATED     = "updated_at_ms"
        const val COL_IDAY_DELETED     = "deleted"

        const val TABLE_ITIN_FLIGHTS   = "itinerary_flights"
        const val COL_IFLIGHT_ID            = "id"
        const val COL_IFLIGHT_DAY_ID        = "day_id"
        const val COL_IFLIGHT_FLIGHT_NUMBER = "flight_number"
        const val COL_IFLIGHT_PDF_URI       = "pdf_uri"
        const val COL_IFLIGHT_CREATED       = "created_at_ms"
        const val COL_IFLIGHT_DEPARTURE     = "departure_time"
        const val COL_IFLIGHT_DOC_NAME      = "doc_name"
        const val COL_IFLIGHT_UPDATED       = "updated_at_ms"
        const val COL_IFLIGHT_DELETED       = "deleted"

        const val TABLE_ITIN_LODGING   = "itinerary_lodging"
        const val COL_ILODGE_ID        = "id"
        const val COL_ILODGE_TRIP_ID   = "trip_id"
        const val COL_ILODGE_FROM_DATE = "from_date"
        const val COL_ILODGE_TO_DATE   = "to_date"
        const val COL_ILODGE_PDF_URI   = "pdf_uri"
        const val COL_ILODGE_CREATED   = "created_at_ms"
        const val COL_ILODGE_DOC_NAME  = "doc_name"
        const val COL_ILODGE_UPDATED   = "updated_at_ms"
        const val COL_ILODGE_DELETED   = "deleted"

        const val TABLE_ITIN_CAR       = "itinerary_car_rental"
        const val COL_ICAR_ID          = "id"
        const val COL_ICAR_TRIP_ID     = "trip_id"
        const val COL_ICAR_FROM_DATE   = "from_date"
        const val COL_ICAR_TO_DATE     = "to_date"
        const val COL_ICAR_PDF_URI     = "pdf_uri"
        const val COL_ICAR_CREATED     = "created_at_ms"
        const val COL_ICAR_DOC_NAME    = "doc_name"
        const val COL_ICAR_UPDATED     = "updated_at_ms"
        const val COL_ICAR_DELETED     = "deleted"

        const val TABLE_ITIN_PLACES       = "itinerary_places"
        const val COL_IPLACE_ID           = "id"
        const val COL_IPLACE_DAY_ID       = "day_id"
        const val COL_IPLACE_NAME         = "place_name"
        const val COL_IPLACE_CATEGORY     = "place_category"
        const val COL_IPLACE_LAT          = "place_lat"
        const val COL_IPLACE_LNG          = "place_lng"
        const val COL_IPLACE_NOTE         = "note"
        const val COL_IPLACE_CREATED      = "created_at_ms"
        const val COL_IPLACE_PHOTO_URL    = "photo_url"
        const val COL_IPLACE_UPDATED      = "updated_at_ms"
        const val COL_IPLACE_DELETED      = "deleted"

        const val TABLE_NOTIFICATIONS  = "notifications"
        const val COL_NOTIF_ID         = "id"
        const val COL_NOTIF_ACCOUNT_ID = "account_id"
        const val COL_NOTIF_TITLE      = "title"
        const val COL_NOTIF_BODY       = "body"
        const val COL_NOTIF_CREATED    = "created_at_ms"

        const val TABLE_REMINDER_LOG   = "reminder_log"
        const val COL_RLOG_KEY         = "key"
        const val COL_RLOG_SENT        = "sent_at_ms"

        // Durable queue of local changes waiting to reach the API. Each row is
        // one pending CREATE/UPDATE/DELETE for one entity (trip or itinerary
        // item); OutboxRepository owns enqueue/coalesce/retry logic on top of
        // this table.
        const val TABLE_SYNC_OUTBOX          = "sync_outbox"
        const val COL_OUTBOX_ID              = "id"
        const val COL_OUTBOX_ENTITY_TYPE     = "entity_type"
        const val COL_OUTBOX_ENTITY_ID       = "entity_id"
        const val COL_OUTBOX_OPERATION       = "operation"
        const val COL_OUTBOX_PAYLOAD_JSON    = "payload_json"
        const val COL_OUTBOX_CREATED         = "created_at_ms"
        const val COL_OUTBOX_ATTEMPTS        = "attempts"
        const val COL_OUTBOX_LAST_ERROR      = "last_error"

        // Per-user, per-query cache of nearby-places results (PR 12). Unlike
        // weather/currency, a places result is tied to one user's exact GPS
        // fix or search term, so it is keyed by account on top of the same
        // city/coord slug LocationIQRepository already uses for the shared
        // cache (see cache_key below and LocationIQRepository.placesPath).
        const val TABLE_PLACES_CACHE       = "places_cache"
        const val COL_PCACHE_ACCOUNT_ID    = "account_id"
        const val COL_PCACHE_KEY           = "cache_key"
        const val COL_PCACHE_JSON          = "places_json"


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
                $COL_TRIP_DEST_PHOTO  TEXT,
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
                $COL_IFLIGHT_DEPARTURE     TEXT,
                $COL_IFLIGHT_DOC_NAME      TEXT,
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
                $COL_ILODGE_DOC_NAME  TEXT,
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
                $COL_ICAR_DOC_NAME  TEXT,
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

        private val CREATE_REMINDER_LOG = """
            CREATE TABLE $TABLE_REMINDER_LOG (
                $COL_RLOG_KEY  TEXT PRIMARY KEY,
                $COL_RLOG_SENT INTEGER NOT NULL
            )
        """.trimIndent()

        // No FOREIGN KEY on entity_id: the referenced row (a trip, day,
        // flight...) can legitimately be gone by the time this entry is sent
        // (e.g. a DELETE queued right after a CREATE that hasn't synced yet),
        // and a constraint would block inserting that DELETE entry.
        private val CREATE_SYNC_OUTBOX = """
            CREATE TABLE $TABLE_SYNC_OUTBOX (
                $COL_OUTBOX_ID           TEXT PRIMARY KEY,
                $COL_OUTBOX_ENTITY_TYPE  TEXT NOT NULL,
                $COL_OUTBOX_ENTITY_ID    TEXT NOT NULL,
                $COL_OUTBOX_OPERATION    TEXT NOT NULL,
                $COL_OUTBOX_PAYLOAD_JSON TEXT,
                $COL_OUTBOX_CREATED      INTEGER NOT NULL,
                $COL_OUTBOX_ATTEMPTS     INTEGER NOT NULL DEFAULT 0,
                $COL_OUTBOX_LAST_ERROR   TEXT
            )
        """.trimIndent()

        // Lets OutboxRepository find an entity's existing pending entry (for
        // coalescing) without scanning the whole table.
        private const val CREATE_SYNC_OUTBOX_ENTITY_IDX =
            "CREATE INDEX idx_sync_outbox_entity ON $TABLE_SYNC_OUTBOX($COL_OUTBOX_ENTITY_TYPE, $COL_OUTBOX_ENTITY_ID)"

        private val CREATE_PLACES_CACHE = """
            CREATE TABLE $TABLE_PLACES_CACHE (
                $COL_PCACHE_ACCOUNT_ID TEXT NOT NULL,
                $COL_PCACHE_KEY        TEXT NOT NULL,
                $COL_PCACHE_JSON       TEXT NOT NULL,
                PRIMARY KEY ($COL_PCACHE_ACCOUNT_ID, $COL_PCACHE_KEY)
            )
        """.trimIndent()

        @Volatile private var instance: WaypointDbHelper? = null

        fun getInstance(context: Context): WaypointDbHelper =
            instance ?: synchronized(this) {
                instance ?: WaypointDbHelper(context).also { instance = it }
            }
    }
}
