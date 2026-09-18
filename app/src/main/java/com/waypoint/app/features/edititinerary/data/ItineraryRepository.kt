package com.waypoint.app.features.edititinerary.data

import android.content.ContentValues
import android.content.Context
import com.waypoint.app.core.db.WaypointDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID

/**
 * Data layer for all itinerary tables (days, flights, lodging,
 * car rental, places). Wraps raw SQLite via [WaypointDbHelper].
 */
class ItineraryRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)

    // ── Selected days ─────────────────────────────────────────────────────────

    /**
     * Replaces the full set of selected days for [tripId].
     * Deletes any previously saved days for this trip, then inserts
     * the new set. Runs on the IO dispatcher.
     */
    suspend fun replaceSelectedDays(
        tripId: String,
        days: Set<LocalDate>,
    ) = withContext(Dispatchers.IO) {
        val write = db.writableDatabase
        write.beginTransaction()
        try {
            // Load the existing date → id mapping so we can preserve IDs for
            // dates that are still selected. This keeps place/flight rows valid.
            val existing: Map<LocalDate, String> = run {
                val cursor = db.readableDatabase.query(
                    WaypointDbHelper.TABLE_ITIN_DAYS,
                    arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_IDAY_DATE),
                    "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                    arrayOf(tripId),
                    null, null, null,
                )
                val map = mutableMapOf<LocalDate, String>()
                cursor.use { c ->
                    while (c.moveToNext()) {
                        runCatching {
                            map[LocalDate.parse(c.getString(1))] = c.getString(0)
                        }
                    }
                }
                map
            }

            // Delete rows for dates no longer in the selection.
            val datesToRemove = existing.keys - days
            for (date in datesToRemove) {
                val id = existing[date] ?: continue
                write.delete(
                    WaypointDbHelper.TABLE_ITIN_DAYS,
                    "${WaypointDbHelper.COL_IDAY_ID} = ?",
                    arrayOf(id),
                )
            }

            // Insert rows only for dates that are genuinely new.
            val datesToAdd = days - existing.keys
            for (date in datesToAdd) {
                val cv = ContentValues().apply {
                    put(WaypointDbHelper.COL_IDAY_ID,      UUID.randomUUID().toString())
                    put(WaypointDbHelper.COL_IDAY_TRIP_ID, tripId)
                    put(WaypointDbHelper.COL_IDAY_DATE,    date.toString())
                }
                write.insert(WaypointDbHelper.TABLE_ITIN_DAYS, null, cv)
            }

            write.setTransactionSuccessful()
        } finally {
            write.endTransaction()
        }
    }

    /**
     * Returns the saved selected days for [tripId], sorted ascending.
     */
    /**
     * After a trip's dates change, drop selected days that no longer fall
     * inside the range. Flights/places on those days go with them (CASCADE).
     */
    suspend fun removeDaysOutside(tripId: String, start: LocalDate, end: LocalDate) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_DAYS,
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ? AND (${WaypointDbHelper.COL_IDAY_DATE} < ? OR ${WaypointDbHelper.COL_IDAY_DATE} > ?)",
                arrayOf(tripId, start.toString(), end.toString()),
            )
        }

    suspend fun getSelectedDays(tripId: String): List<LocalDate> =
        withContext(Dispatchers.IO) {
            val read = db.readableDatabase
            val cursor = read.query(
                WaypointDbHelper.TABLE_ITIN_DAYS,
                arrayOf(WaypointDbHelper.COL_IDAY_DATE),
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                arrayOf(tripId),
                null, null,
                "${WaypointDbHelper.COL_IDAY_DATE} ASC",
            )
            val result = mutableListOf<LocalDate>()
            cursor.use {
                while (it.moveToNext()) {
                    runCatching {
                        result.add(LocalDate.parse(it.getString(0)))
                    }
                }
            }
            result
        }

    // ── Selected days with IDs ────────────────────────────────────────────────

    /**
     * Returns selected days for [tripId] as full entities (id + date), sorted ascending.
     */
    suspend fun getSelectedDaysWithIds(tripId: String): List<ItineraryDayEntity> =
        withContext(Dispatchers.IO) {
            val cursor = db.readableDatabase.query(
                WaypointDbHelper.TABLE_ITIN_DAYS,
                arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_IDAY_DATE),
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                arrayOf(tripId),
                null, null,
                "${WaypointDbHelper.COL_IDAY_DATE} ASC",
            )
            val result = mutableListOf<ItineraryDayEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    runCatching {
                        result.add(
                            ItineraryDayEntity(
                                id     = it.getString(0),
                                tripId = tripId,
                                date   = LocalDate.parse(it.getString(1)),
                            )
                        )
                    }
                }
            }
            result
        }

    // ── Flights ───────────────────────────────────────────────────────────────

    /**
     * Inserts a new flight row for [dayId]. Returns the generated row id.
     */
    suspend fun insertFlight(dayId: String, pdfUri: String, docName: String? = null): String =
        withContext(Dispatchers.IO) {
            val id = UUID.randomUUID().toString()
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_IFLIGHT_ID,      id)
                put(WaypointDbHelper.COL_IFLIGHT_DAY_ID,  dayId)
                put(WaypointDbHelper.COL_IFLIGHT_PDF_URI, pdfUri)
                put(WaypointDbHelper.COL_IFLIGHT_DOC_NAME, docName)
                put(WaypointDbHelper.COL_IFLIGHT_CREATED, System.currentTimeMillis())
            }
            db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_FLIGHTS, null, cv)
            id
        }

    /**
     * Updates the flight number text for an existing flight row.
     */
    suspend fun updateFlightNumber(flightId: String, number: String) =
        withContext(Dispatchers.IO) {
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER, number)
            }
            db.writableDatabase.update(
                WaypointDbHelper.TABLE_ITIN_FLIGHTS,
                cv,
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?",
                arrayOf(flightId),
            )
        }

    /** Sets (or clears with null) the local departure time "HH:mm" for a flight. */
    suspend fun updateFlightDepartureTime(flightId: String, time: String?) =
        withContext(Dispatchers.IO) {
            val cv = ContentValues().apply {
                if (time == null) putNull(WaypointDbHelper.COL_IFLIGHT_DEPARTURE)
                else put(WaypointDbHelper.COL_IFLIGHT_DEPARTURE, time)
            }
            db.writableDatabase.update(
                WaypointDbHelper.TABLE_ITIN_FLIGHTS, cv,
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?", arrayOf(flightId),
            )
        }

    /**
     * Returns all flights for the given list of day IDs.
     */
    suspend fun getFlightsForDays(dayIds: List<String>): List<FlightEntity> =
        withContext(Dispatchers.IO) {
            if (dayIds.isEmpty()) return@withContext emptyList()
            val placeholders = dayIds.joinToString(",") { "?" }
            val cursor = db.readableDatabase.rawQuery(
                "SELECT ${WaypointDbHelper.COL_IFLIGHT_ID}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_DAY_ID}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_PDF_URI}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_CREATED}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_DEPARTURE}, " +
                    "${WaypointDbHelper.COL_IFLIGHT_DOC_NAME} " +
                    "FROM ${WaypointDbHelper.TABLE_ITIN_FLIGHTS} " +
                    "WHERE ${WaypointDbHelper.COL_IFLIGHT_DAY_ID} IN ($placeholders) " +
                    "ORDER BY ${WaypointDbHelper.COL_IFLIGHT_CREATED} ASC",
                dayIds.toTypedArray(),
            )
            val result = mutableListOf<FlightEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    result.add(
                        FlightEntity(
                            id           = it.getString(0),
                            dayId        = it.getString(1),
                            flightNumber = it.getString(2),
                            pdfUri       = it.getString(3),
                            createdAtMs  = it.getLong(4),
                            departureTime = if (it.isNull(5)) null else it.getString(5),
                            docName      = if (it.isNull(6)) null else it.getString(6),
                        )
                    )
                }
            }
            result
        }

    /** Deletes a single flight row by its id. */
    suspend fun deleteFlight(flightId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_FLIGHTS,
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?",
                arrayOf(flightId),
            )
        }

    // ── Lodging ───────────────────────────────────────────────────────────────

    /** Adds a lodging document covering [fromDate]..[toDate]. Multiple per trip are allowed. */
    suspend fun insertLodging(
        tripId: String,
        fromDate: LocalDate,
        toDate: LocalDate,
        pdfUri: String,
        docName: String? = null,
    ): String = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val cv = ContentValues().apply {
            put(WaypointDbHelper.COL_ILODGE_ID,        id)
            put(WaypointDbHelper.COL_ILODGE_TRIP_ID,   tripId)
            put(WaypointDbHelper.COL_ILODGE_FROM_DATE, fromDate.toString())
            put(WaypointDbHelper.COL_ILODGE_TO_DATE,   toDate.toString())
            put(WaypointDbHelper.COL_ILODGE_PDF_URI,   pdfUri)
            put(WaypointDbHelper.COL_ILODGE_CREATED,   System.currentTimeMillis())
            put(WaypointDbHelper.COL_ILODGE_DOC_NAME,  docName)
        }
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_LODGING, null, cv)
        id
    }

    /** Every lodging document for [tripId], oldest first. */
    suspend fun getLodgingsForTrip(tripId: String): List<LodgingEntity> =
        withContext(Dispatchers.IO) {
            val cursor = db.readableDatabase.query(
                WaypointDbHelper.TABLE_ITIN_LODGING, null,
                "${WaypointDbHelper.COL_ILODGE_TRIP_ID} = ?", arrayOf(tripId),
                null, null, "${WaypointDbHelper.COL_ILODGE_CREATED} ASC",
            )
            val out = mutableListOf<LodgingEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    val nameIdx = it.getColumnIndex(WaypointDbHelper.COL_ILODGE_DOC_NAME)
                    out += LodgingEntity(
                        id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_ID)),
                        tripId      = tripId,
                        fromDate    = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_FROM_DATE))),
                        toDate      = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_TO_DATE))),
                        pdfUri      = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_PDF_URI)),
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_CREATED)),
                        docName     = if (nameIdx < 0 || it.isNull(nameIdx)) null else it.getString(nameIdx),
                    )
                }
            }
            out
        }

    /** Removes one lodging document by id. */
    suspend fun deleteLodging(lodgingId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_LODGING,
                "${WaypointDbHelper.COL_ILODGE_ID} = ?",
                arrayOf(lodgingId),
            )
        }

    // ── Car Rental ────────────────────────────────────────────────────────────

    /** Adds a car-rental document covering [fromDate]..[toDate]. Multiple per trip are allowed. */
    suspend fun insertCarRental(
        tripId: String,
        fromDate: LocalDate,
        toDate: LocalDate,
        pdfUri: String,
        docName: String? = null,
    ): String = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val cv = ContentValues().apply {
            put(WaypointDbHelper.COL_ICAR_ID,        id)
            put(WaypointDbHelper.COL_ICAR_TRIP_ID,   tripId)
            put(WaypointDbHelper.COL_ICAR_FROM_DATE, fromDate.toString())
            put(WaypointDbHelper.COL_ICAR_TO_DATE,   toDate.toString())
            put(WaypointDbHelper.COL_ICAR_PDF_URI,   pdfUri)
            put(WaypointDbHelper.COL_ICAR_CREATED,   System.currentTimeMillis())
            put(WaypointDbHelper.COL_ICAR_DOC_NAME,  docName)
        }
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_CAR, null, cv)
        id
    }

    /** Every car-rental document for [tripId], oldest first. */
    suspend fun getCarRentalsForTrip(tripId: String): List<CarRentalEntity> =
        withContext(Dispatchers.IO) {
            val cursor = db.readableDatabase.query(
                WaypointDbHelper.TABLE_ITIN_CAR, null,
                "${WaypointDbHelper.COL_ICAR_TRIP_ID} = ?", arrayOf(tripId),
                null, null, "${WaypointDbHelper.COL_ICAR_CREATED} ASC",
            )
            val out = mutableListOf<CarRentalEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    val nameIdx = it.getColumnIndex(WaypointDbHelper.COL_ICAR_DOC_NAME)
                    out += CarRentalEntity(
                        id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_ID)),
                        tripId      = tripId,
                        fromDate    = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_FROM_DATE))),
                        toDate      = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_TO_DATE))),
                        pdfUri      = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_PDF_URI)),
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_CREATED)),
                        docName     = if (nameIdx < 0 || it.isNull(nameIdx)) null else it.getString(nameIdx),
                    )
                }
            }
            out
        }

    /** Removes one car-rental document by id. */
    suspend fun deleteCarRental(carRentalId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_CAR,
                "${WaypointDbHelper.COL_ICAR_ID} = ?",
                arrayOf(carRentalId),
            )
        }

    // ── Places ────────────────────────────────────────────────────────────────

    /**
     * Inserts a new place row for the given [dayId].
     * [category] must be one of HOTELS, PARKS, PUBS, CINEMAS
     * (matching [com.waypoint.app.features.explore.ui.ExploreFilter]).
     */
    suspend fun insertPlace(
        dayId: String,
        placeName: String,
        category: String,
        lat: Double? = null,
        lng: Double? = null,
        note: String? = null,
        photoUrl: String? = null,
    ): String = withContext(Dispatchers.IO) {
        val id = UUID.randomUUID().toString()
        val cv = ContentValues().apply {
            put(WaypointDbHelper.COL_IPLACE_ID,       id)
            put(WaypointDbHelper.COL_IPLACE_DAY_ID,   dayId)
            put(WaypointDbHelper.COL_IPLACE_NAME,     placeName)
            put(WaypointDbHelper.COL_IPLACE_CATEGORY, category)
            if (lat != null) put(WaypointDbHelper.COL_IPLACE_LAT, lat)
            if (lng != null) put(WaypointDbHelper.COL_IPLACE_LNG, lng)
            if (note != null) put(WaypointDbHelper.COL_IPLACE_NOTE, note)
            if (photoUrl != null) put(WaypointDbHelper.COL_IPLACE_PHOTO_URL, photoUrl)
            put(WaypointDbHelper.COL_IPLACE_CREATED, System.currentTimeMillis())
        }
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_PLACES, null, cv)
        id
    }

    /**
     * Returns all place rows for [dayId], sorted by creation time.
     * Each row is returned as a raw map of column name to value.
     */
    suspend fun getPlacesForDay(dayId: String): List<PlaceEntity> =
        withContext(Dispatchers.IO) {
            val cursor = db.readableDatabase.query(
                WaypointDbHelper.TABLE_ITIN_PLACES,
                null,
                "${WaypointDbHelper.COL_IPLACE_DAY_ID} = ?",
                arrayOf(dayId),
                null, null,
                "${WaypointDbHelper.COL_IPLACE_CREATED} ASC",
            )
            val result = mutableListOf<PlaceEntity>()
            cursor.use {
                while (it.moveToNext()) {
                    val latIdx   = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_LAT)
                    val lngIdx   = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_LNG)
                    val photoIdx = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_PHOTO_URL)
                    result.add(
                        PlaceEntity(
                            id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_ID)),
                            dayId       = dayId,
                            name        = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_NAME)),
                            category    = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_CATEGORY)),
                            lat         = if (latIdx >= 0 && !it.isNull(latIdx)) it.getDouble(latIdx) else null,
                            lng         = if (lngIdx >= 0 && !it.isNull(lngIdx)) it.getDouble(lngIdx) else null,
                            note        = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_NOTE)),
                            photoUrl    = if (photoIdx >= 0 && !it.isNull(photoIdx)) it.getString(photoIdx) else null,
                            createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_CREATED)),
                        )
                    )
                }
            }
            result
        }

    /** Deletes a single place row by its id. */
    suspend fun deletePlace(placeId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_PLACES,
                "${WaypointDbHelper.COL_IPLACE_ID} = ?",
                arrayOf(placeId),
            )
        }
}
