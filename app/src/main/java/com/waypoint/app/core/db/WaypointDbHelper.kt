// declares that this file belongs to the package `com.waypoint.app.core.db`
package com.waypoint.app.core.db

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.database.sqlite.SQLiteDatabase` for use in this file
import android.database.sqlite.SQLiteDatabase
// imports `android.database.sqlite.SQLiteOpenHelper` for use in this file
import android.database.sqlite.SQLiteOpenHelper

// expression: `class WaypointDbHelper private constructor(context: Context) :`
class WaypointDbHelper private constructor(context: Context) :
    // calls `SQLiteOpenHelper` with arguments `(context.applicationContext, DB_NAME, null, D…)` and opens a trailing lambda / block
    SQLiteOpenHelper(context.applicationContext, DB_NAME, null, DB_VERSION) {

    // declares override function `onConfigure` taking 1 parameter (`db`) and opens its body
    override fun onConfigure(db: SQLiteDatabase) {
        // calls `onConfigure` on `super` with arguments `(db)`
        super.onConfigure(db)
        // calls `setForeignKeyConstraintsEnabled` on `db` with arguments `(true)`
        db.setForeignKeyConstraintsEnabled(true)
    // closes the function `onConfigure`
    }

    // declares override function `onCreate` taking 1 parameter (`db`) and opens its body
    override fun onCreate(db: SQLiteDatabase) {
        // calls `execSQL` on `db` with arguments `(CREATE_ACCOUNTS)`
        db.execSQL(CREATE_ACCOUNTS)
        // calls `execSQL` on `db` with arguments `(CREATE_TRIPS)`
        db.execSQL(CREATE_TRIPS)
        // calls `execSQL` on `db` with arguments `(CREATE_TRIPS_ACCOUNT_IDX)`
        db.execSQL(CREATE_TRIPS_ACCOUNT_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_DAYS)`
        db.execSQL(CREATE_ITINERARY_DAYS)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_DAYS_TRIP_IDX)`
        db.execSQL(CREATE_ITINERARY_DAYS_TRIP_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_FLIGHTS)`
        db.execSQL(CREATE_ITINERARY_FLIGHTS)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_FLIGHTS_DAY_IDX)`
        db.execSQL(CREATE_ITINERARY_FLIGHTS_DAY_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_LODGING)`
        db.execSQL(CREATE_ITINERARY_LODGING)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_LODGING_TRIP_IDX)`
        db.execSQL(CREATE_ITINERARY_LODGING_TRIP_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_CAR_RENTAL)`
        db.execSQL(CREATE_ITINERARY_CAR_RENTAL)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_CAR_TRIP_IDX)`
        db.execSQL(CREATE_ITINERARY_CAR_TRIP_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_PLACES)`
        db.execSQL(CREATE_ITINERARY_PLACES)
        // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_PLACES_DAY_IDX)`
        db.execSQL(CREATE_ITINERARY_PLACES_DAY_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_NOTIFICATIONS)`
        db.execSQL(CREATE_NOTIFICATIONS)
        // calls `execSQL` on `db` with arguments `(CREATE_NOTIFICATIONS_ACCOUNT_IDX)`
        db.execSQL(CREATE_NOTIFICATIONS_ACCOUNT_IDX)
        // calls `execSQL` on `db` with arguments `(CREATE_REMINDER_LOG)`
        db.execSQL(CREATE_REMINDER_LOG)
    // closes the function `onCreate`
    }

    // declares override function `onUpgrade` taking 3 parameters (`db`, `oldVersion`, `newVersion`) and opens its body
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // `if` statement: the block below runs when `oldVersion < 2` is true
        if (oldVersion < 2) {
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TR…)`
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LAT REAL")
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TR…)`
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_LNG REAL")
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 4` is true
        if (oldVersion < 4) {
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $…)`
            db.execSQL("ALTER TABLE $TABLE_ITIN_PLACES ADD COLUMN $COL_IPLACE_PHOTO_URL TEXT")
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 3` is true
        if (oldVersion < 3) {
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_DAYS)`
            db.execSQL(CREATE_ITINERARY_DAYS)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_DAYS_TRIP_IDX)`
            db.execSQL(CREATE_ITINERARY_DAYS_TRIP_IDX)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_FLIGHTS)`
            db.execSQL(CREATE_ITINERARY_FLIGHTS)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_FLIGHTS_DAY_IDX)`
            db.execSQL(CREATE_ITINERARY_FLIGHTS_DAY_IDX)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_LODGING)`
            db.execSQL(CREATE_ITINERARY_LODGING)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_LODGING_TRIP_IDX)`
            db.execSQL(CREATE_ITINERARY_LODGING_TRIP_IDX)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_CAR_RENTAL)`
            db.execSQL(CREATE_ITINERARY_CAR_RENTAL)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_CAR_TRIP_IDX)`
            db.execSQL(CREATE_ITINERARY_CAR_TRIP_IDX)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_PLACES)`
            db.execSQL(CREATE_ITINERARY_PLACES)
            // calls `execSQL` on `db` with arguments `(CREATE_ITINERARY_PLACES_DAY_IDX)`
            db.execSQL(CREATE_ITINERARY_PLACES_DAY_IDX)
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 5` is true
        if (oldVersion < 5) {
            // calls `execSQL` on `db` with arguments `(CREATE_NOTIFICATIONS)`
            db.execSQL(CREATE_NOTIFICATIONS)
            // calls `execSQL` on `db` with arguments `(CREATE_NOTIFICATIONS_ACCOUNT_IDX)`
            db.execSQL(CREATE_NOTIFICATIONS_ACCOUNT_IDX)
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 6` is true
        if (oldVersion < 6) {
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN …)`
            db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_DEPARTURE TEXT")
            // calls `execSQL` on `db` with arguments `(CREATE_REMINDER_LOG)`
            db.execSQL(CREATE_REMINDER_LOG)
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 7` is true
        if (oldVersion < 7) {
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TR…)`
            db.execSQL("ALTER TABLE $TABLE_TRIPS ADD COLUMN $COL_TRIP_DEST_PHOTO TEXT")
        // closes the if block
        }
        // `if` statement: the block below runs when `oldVersion < 8` is true
        if (oldVersion < 8) {
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN …)`
            db.execSQL("ALTER TABLE $TABLE_ITIN_FLIGHTS ADD COLUMN $COL_IFLIGHT_DOC_NAME TEXT")
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_ITIN_LODGING ADD COLUMN …)`
            db.execSQL("ALTER TABLE $TABLE_ITIN_LODGING ADD COLUMN $COL_ILODGE_DOC_NAME TEXT")
            // calls `execSQL` on `db` with arguments `("ALTER TABLE $TABLE_ITIN_CAR ADD COLUMN $COL…)`
            db.execSQL("ALTER TABLE $TABLE_ITIN_CAR ADD COLUMN $COL_ICAR_DOC_NAME TEXT")
        // closes the if block
        }
    // closes the function `onUpgrade`
    }

    // declares the companion object holding members shared by all instances of the enclosing class
    companion object {
        // declares private const read-only property `DB_NAME`, initialised to the string literal "waypoint.db"
        private const val DB_NAME    = "waypoint.db"
        // declares private const read-only property `DB_VERSION`, initialised to the number 8
        private const val DB_VERSION = 8

        // declares const read-only property `TABLE_ACCOUNTS`, initialised to the string literal "accounts"
        const val TABLE_ACCOUNTS       = "accounts"
        // declares const read-only property `COL_ACC_ID`, initialised to the string literal "id"
        const val COL_ACC_ID           = "id"
        // declares const read-only property `COL_ACC_EMAIL`, initialised to the string literal "email"
        const val COL_ACC_EMAIL        = "email"
        // declares const read-only property `COL_ACC_DISPLAY_NAME`, initialised to the string literal "display_name"
        const val COL_ACC_DISPLAY_NAME = "display_name"
        // declares const read-only property `COL_ACC_PHOTO_URL`, initialised to the string literal "photo_url"
        const val COL_ACC_PHOTO_URL    = "photo_url"
        // declares const read-only property `COL_ACC_LAST_LOGIN`, initialised to the string literal "last_login_ms"
        const val COL_ACC_LAST_LOGIN   = "last_login_ms"

        // declares const read-only property `TABLE_TRIPS`, initialised to the string literal "trips"
        const val TABLE_TRIPS          = "trips"
        // declares const read-only property `COL_TRIP_ID`, initialised to the string literal "id"
        const val COL_TRIP_ID          = "id"
        // declares const read-only property `COL_TRIP_ACCOUNT_ID`, initialised to the string literal "account_id"
        const val COL_TRIP_ACCOUNT_ID  = "account_id"
        // declares const read-only property `COL_TRIP_NAME`, initialised to the string literal "name"
        const val COL_TRIP_NAME        = "name"
        // declares const read-only property `COL_TRIP_START`, initialised to the string literal "start_date"
        const val COL_TRIP_START       = "start_date"
        // declares const read-only property `COL_TRIP_END`, initialised to the string literal "end_date"
        const val COL_TRIP_END         = "end_date"
        // declares const read-only property `COL_TRIP_DESTINATION`, initialised to the string literal "destination"
        const val COL_TRIP_DESTINATION = "destination"
        // declares const read-only property `COL_TRIP_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_TRIP_CREATED     = "created_at_ms"
        // declares const read-only property `COL_TRIP_UPDATED`, initialised to the string literal "updated_at_ms"
        const val COL_TRIP_UPDATED     = "updated_at_ms"
        // declares const read-only property `COL_TRIP_DEST_LAT`, initialised to the string literal "dest_lat"
        const val COL_TRIP_DEST_LAT    = "dest_lat"
        // declares const read-only property `COL_TRIP_DEST_LNG`, initialised to the string literal "dest_lng"
        const val COL_TRIP_DEST_LNG    = "dest_lng"
        // declares const read-only property `COL_TRIP_DEST_PHOTO`, initialised to the string literal "dest_photo_url"
        const val COL_TRIP_DEST_PHOTO  = "dest_photo_url"

        // declares const read-only property `TABLE_ITIN_DAYS`, initialised to the string literal "itinerary_days"
        const val TABLE_ITIN_DAYS      = "itinerary_days"
        // declares const read-only property `COL_IDAY_ID`, initialised to the string literal "id"
        const val COL_IDAY_ID          = "id"
        // declares const read-only property `COL_IDAY_TRIP_ID`, initialised to the string literal "trip_id"
        const val COL_IDAY_TRIP_ID     = "trip_id"
        // declares const read-only property `COL_IDAY_DATE`, initialised to the string literal "date"
        const val COL_IDAY_DATE        = "date"

        // declares const read-only property `TABLE_ITIN_FLIGHTS`, initialised to the string literal "itinerary_flights"
        const val TABLE_ITIN_FLIGHTS   = "itinerary_flights"
        // declares const read-only property `COL_IFLIGHT_ID`, initialised to the string literal "id"
        const val COL_IFLIGHT_ID            = "id"
        // declares const read-only property `COL_IFLIGHT_DAY_ID`, initialised to the string literal "day_id"
        const val COL_IFLIGHT_DAY_ID        = "day_id"
        // declares const read-only property `COL_IFLIGHT_FLIGHT_NUMBER`, initialised to the string literal "flight_number"
        const val COL_IFLIGHT_FLIGHT_NUMBER = "flight_number"
        // declares const read-only property `COL_IFLIGHT_PDF_URI`, initialised to the string literal "pdf_uri"
        const val COL_IFLIGHT_PDF_URI       = "pdf_uri"
        // declares const read-only property `COL_IFLIGHT_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_IFLIGHT_CREATED       = "created_at_ms"
        // declares const read-only property `COL_IFLIGHT_DEPARTURE`, initialised to the string literal "departure_time"
        const val COL_IFLIGHT_DEPARTURE     = "departure_time"
        // declares const read-only property `COL_IFLIGHT_DOC_NAME`, initialised to the string literal "doc_name"
        const val COL_IFLIGHT_DOC_NAME      = "doc_name"

        // declares const read-only property `TABLE_ITIN_LODGING`, initialised to the string literal "itinerary_lodging"
        const val TABLE_ITIN_LODGING   = "itinerary_lodging"
        // declares const read-only property `COL_ILODGE_ID`, initialised to the string literal "id"
        const val COL_ILODGE_ID        = "id"
        // declares const read-only property `COL_ILODGE_TRIP_ID`, initialised to the string literal "trip_id"
        const val COL_ILODGE_TRIP_ID   = "trip_id"
        // declares const read-only property `COL_ILODGE_FROM_DATE`, initialised to the string literal "from_date"
        const val COL_ILODGE_FROM_DATE = "from_date"
        // declares const read-only property `COL_ILODGE_TO_DATE`, initialised to the string literal "to_date"
        const val COL_ILODGE_TO_DATE   = "to_date"
        // declares const read-only property `COL_ILODGE_PDF_URI`, initialised to the string literal "pdf_uri"
        const val COL_ILODGE_PDF_URI   = "pdf_uri"
        // declares const read-only property `COL_ILODGE_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_ILODGE_CREATED   = "created_at_ms"
        // declares const read-only property `COL_ILODGE_DOC_NAME`, initialised to the string literal "doc_name"
        const val COL_ILODGE_DOC_NAME  = "doc_name"

        // declares const read-only property `TABLE_ITIN_CAR`, initialised to the string literal "itinerary_car_rental"
        const val TABLE_ITIN_CAR       = "itinerary_car_rental"
        // declares const read-only property `COL_ICAR_ID`, initialised to the string literal "id"
        const val COL_ICAR_ID          = "id"
        // declares const read-only property `COL_ICAR_TRIP_ID`, initialised to the string literal "trip_id"
        const val COL_ICAR_TRIP_ID     = "trip_id"
        // declares const read-only property `COL_ICAR_FROM_DATE`, initialised to the string literal "from_date"
        const val COL_ICAR_FROM_DATE   = "from_date"
        // declares const read-only property `COL_ICAR_TO_DATE`, initialised to the string literal "to_date"
        const val COL_ICAR_TO_DATE     = "to_date"
        // declares const read-only property `COL_ICAR_PDF_URI`, initialised to the string literal "pdf_uri"
        const val COL_ICAR_PDF_URI     = "pdf_uri"
        // declares const read-only property `COL_ICAR_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_ICAR_CREATED     = "created_at_ms"
        // declares const read-only property `COL_ICAR_DOC_NAME`, initialised to the string literal "doc_name"
        const val COL_ICAR_DOC_NAME    = "doc_name"

        // declares const read-only property `TABLE_ITIN_PLACES`, initialised to the string literal "itinerary_places"
        const val TABLE_ITIN_PLACES       = "itinerary_places"
        // declares const read-only property `COL_IPLACE_ID`, initialised to the string literal "id"
        const val COL_IPLACE_ID           = "id"
        // declares const read-only property `COL_IPLACE_DAY_ID`, initialised to the string literal "day_id"
        const val COL_IPLACE_DAY_ID       = "day_id"
        // declares const read-only property `COL_IPLACE_NAME`, initialised to the string literal "place_name"
        const val COL_IPLACE_NAME         = "place_name"
        // declares const read-only property `COL_IPLACE_CATEGORY`, initialised to the string literal "place_category"
        const val COL_IPLACE_CATEGORY     = "place_category"
        // declares const read-only property `COL_IPLACE_LAT`, initialised to the string literal "place_lat"
        const val COL_IPLACE_LAT          = "place_lat"
        // declares const read-only property `COL_IPLACE_LNG`, initialised to the string literal "place_lng"
        const val COL_IPLACE_LNG          = "place_lng"
        // declares const read-only property `COL_IPLACE_NOTE`, initialised to the string literal "note"
        const val COL_IPLACE_NOTE         = "note"
        // declares const read-only property `COL_IPLACE_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_IPLACE_CREATED      = "created_at_ms"
        // declares const read-only property `COL_IPLACE_PHOTO_URL`, initialised to the string literal "photo_url"
        const val COL_IPLACE_PHOTO_URL    = "photo_url"

        // declares const read-only property `TABLE_NOTIFICATIONS`, initialised to the string literal "notifications"
        const val TABLE_NOTIFICATIONS  = "notifications"
        // declares const read-only property `COL_NOTIF_ID`, initialised to the string literal "id"
        const val COL_NOTIF_ID         = "id"
        // declares const read-only property `COL_NOTIF_ACCOUNT_ID`, initialised to the string literal "account_id"
        const val COL_NOTIF_ACCOUNT_ID = "account_id"
        // declares const read-only property `COL_NOTIF_TITLE`, initialised to the string literal "title"
        const val COL_NOTIF_TITLE      = "title"
        // declares const read-only property `COL_NOTIF_BODY`, initialised to the string literal "body"
        const val COL_NOTIF_BODY       = "body"
        // declares const read-only property `COL_NOTIF_CREATED`, initialised to the string literal "created_at_ms"
        const val COL_NOTIF_CREATED    = "created_at_ms"

        // declares const read-only property `TABLE_REMINDER_LOG`, initialised to the string literal "reminder_log"
        const val TABLE_REMINDER_LOG   = "reminder_log"
        // declares const read-only property `COL_RLOG_KEY`, initialised to the string literal "key"
        const val COL_RLOG_KEY         = "key"
        // declares const read-only property `COL_RLOG_SENT`, initialised to the string literal "sent_at_ms"
        const val COL_RLOG_SENT        = "sent_at_ms"


        // declares private read-only property `CREATE_ACCOUNTS`, initialised to a multi-line raw string (its lines follow)
        private val CREATE_ACCOUNTS = """
            CREATE TABLE $TABLE_ACCOUNTS (
                $COL_ACC_ID           TEXT PRIMARY KEY,
                $COL_ACC_EMAIL        TEXT NOT NULL,
                $COL_ACC_DISPLAY_NAME TEXT NOT NULL DEFAULT '',
                $COL_ACC_PHOTO_URL    TEXT NOT NULL DEFAULT '',
                $COL_ACC_LAST_LOGIN   INTEGER NOT NULL
            )
        """.trimIndent()

        // declares private read-only property `CREATE_TRIPS`, initialised to a multi-line raw string (its lines follow)
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

        // declares private const read-only property `CREATE_TRIPS_ACCOUNT_IDX`
        private const val CREATE_TRIPS_ACCOUNT_IDX =
            // continues the statement started above: `"CREATE INDEX idx_trips_account_id ON $TABLE_TRIPS($COL_TRI…`
            "CREATE INDEX idx_trips_account_id ON $TABLE_TRIPS($COL_TRIP_ACCOUNT_ID)"

        // declares private read-only property `CREATE_ITINERARY_DAYS`, initialised to a multi-line raw string (its lines follow)
        private val CREATE_ITINERARY_DAYS = """
            CREATE TABLE $TABLE_ITIN_DAYS (
                $COL_IDAY_ID      TEXT PRIMARY KEY,
                $COL_IDAY_TRIP_ID TEXT NOT NULL,
                $COL_IDAY_DATE    TEXT NOT NULL,
                FOREIGN KEY ($COL_IDAY_TRIP_ID) REFERENCES $TABLE_TRIPS($COL_TRIP_ID)
                    ON DELETE CASCADE
            )
        """.trimIndent()

        // declares private const read-only property `CREATE_ITINERARY_DAYS_TRIP_IDX`
        private const val CREATE_ITINERARY_DAYS_TRIP_IDX =
            // continues the statement started above: `"CREATE INDEX idx_itin_days_trip ON $TABLE_ITIN_DAYS($COL_I…`
            "CREATE INDEX idx_itin_days_trip ON $TABLE_ITIN_DAYS($COL_IDAY_TRIP_ID)"

        // declares private read-only property `CREATE_ITINERARY_FLIGHTS`, initialised to a multi-line raw string (its lines follow)
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

        // declares private const read-only property `CREATE_ITINERARY_FLIGHTS_DAY_IDX`
        private const val CREATE_ITINERARY_FLIGHTS_DAY_IDX =
            // continues the statement started above: `"CREATE INDEX idx_itin_flights_day ON $TABLE_ITIN_FLIGHTS($…`
            "CREATE INDEX idx_itin_flights_day ON $TABLE_ITIN_FLIGHTS($COL_IFLIGHT_DAY_ID)"

        // declares private read-only property `CREATE_ITINERARY_LODGING`, initialised to a multi-line raw string (its lines follow)
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

        // declares private const read-only property `CREATE_ITINERARY_LODGING_TRIP_IDX`
        private const val CREATE_ITINERARY_LODGING_TRIP_IDX =
            // continues the statement started above: `"CREATE INDEX idx_itin_lodging_trip ON $TABLE_ITIN_LODGING(…`
            "CREATE INDEX idx_itin_lodging_trip ON $TABLE_ITIN_LODGING($COL_ILODGE_TRIP_ID)"

        // declares private read-only property `CREATE_ITINERARY_CAR_RENTAL`, initialised to a multi-line raw string (its lines follow)
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

        // declares private const read-only property `CREATE_ITINERARY_CAR_TRIP_IDX`
        private const val CREATE_ITINERARY_CAR_TRIP_IDX =
            // continues the statement started above: `"CREATE INDEX idx_itin_car_trip ON $TABLE_ITIN_CAR($COL_ICA…`
            "CREATE INDEX idx_itin_car_trip ON $TABLE_ITIN_CAR($COL_ICAR_TRIP_ID)"

        // declares private read-only property `CREATE_ITINERARY_PLACES`, initialised to a multi-line raw string (its lines follow)
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

        // declares private const read-only property `CREATE_ITINERARY_PLACES_DAY_IDX`
        private const val CREATE_ITINERARY_PLACES_DAY_IDX =
            // continues the statement started above: `"CREATE INDEX idx_itin_places_day ON $TABLE_ITIN_PLACES($CO…`
            "CREATE INDEX idx_itin_places_day ON $TABLE_ITIN_PLACES($COL_IPLACE_DAY_ID)"

        // declares private read-only property `CREATE_NOTIFICATIONS`, initialised to a multi-line raw string (its lines follow)
        private val CREATE_NOTIFICATIONS = """
            CREATE TABLE $TABLE_NOTIFICATIONS (
                $COL_NOTIF_ID         TEXT PRIMARY KEY,
                $COL_NOTIF_ACCOUNT_ID TEXT NOT NULL,
                $COL_NOTIF_TITLE      TEXT NOT NULL,
                $COL_NOTIF_BODY       TEXT NOT NULL,
                $COL_NOTIF_CREATED    INTEGER NOT NULL
            )
        """.trimIndent()

        // declares private const read-only property `CREATE_NOTIFICATIONS_ACCOUNT_IDX`
        private const val CREATE_NOTIFICATIONS_ACCOUNT_IDX =
            // continues the statement started above: `"CREATE INDEX idx_notifications_account ON $TABLE_NOTIFICAT…`
            "CREATE INDEX idx_notifications_account ON $TABLE_NOTIFICATIONS($COL_NOTIF_ACCOUNT_ID)"

        // declares private read-only property `CREATE_REMINDER_LOG`, initialised to a multi-line raw string (its lines follow)
        private val CREATE_REMINDER_LOG = """
            CREATE TABLE $TABLE_REMINDER_LOG (
                $COL_RLOG_KEY  TEXT PRIMARY KEY,
                $COL_RLOG_SENT INTEGER NOT NULL
            )
        """.trimIndent()

        // declares private mutable property `instance` of type `WaypointDbHelper?`, initialised to null
        @Volatile private var instance: WaypointDbHelper? = null

        // declares function `getInstance` taking 1 parameter (`context`), returning `WaypointDbHelper`; its body is the expression ``
        fun getInstance(context: Context): WaypointDbHelper =
            // continues the statement started above: `instance ?: synchronized(this) {`
            instance ?: synchronized(this) {
                // expression: `instance ?: WaypointDbHelper(context).also { instance = it }`
                instance ?: WaypointDbHelper(context).also { instance = it }
            // closes the block
            }
    // closes the companion object
    }
// closes the lambda passed to `SQLiteOpenHelper`
}
