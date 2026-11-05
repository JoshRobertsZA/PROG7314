package com.waypoint.app.features.edititinerary.data

import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.waypoint.app.core.db.WaypointDbHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.util.UUID
import com.waypoint.app.core.sync.OutboxEntityType
import com.waypoint.app.core.sync.OutboxOperation
import com.waypoint.app.core.sync.OutboxRepository
import com.waypoint.app.core.network.WaypointApiRepository
import org.json.JSONObject

/**
 * Local storage for itinerary days, flights, lodging, car rentals and places. Every write is saved to
 * SQLite and queued in the outbox; [pullAndMergeForTrip] brings in changes from other devices.
 */
class ItineraryRepository(context: Context) {

    private val db = WaypointDbHelper.getInstance(context)
    private val outbox = OutboxRepository(context)


    suspend fun replaceSelectedDays(
        tripId: String,
        days: Set<LocalDate>,
    ) = withContext(Dispatchers.IO) {
        val write = db.writableDatabase
        // Collected inside the transaction below, enqueued after it
        // commits (outbox.enqueue() opens its own transaction on this same
        // database, so it must run after this one has ended).
        val createdDays = mutableListOf<Pair<String, LocalDate>>()
        write.beginTransaction()
        try {
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

            val datesToAdd = days - existing.keys
            for (date in datesToAdd) {
                val newId = UUID.randomUUID().toString()
                val cv = ContentValues().apply {
                    put(WaypointDbHelper.COL_IDAY_ID,      newId)
                    put(WaypointDbHelper.COL_IDAY_TRIP_ID, tripId)
                    put(WaypointDbHelper.COL_IDAY_DATE,    date.toString())
                }
                write.insert(WaypointDbHelper.TABLE_ITIN_DAYS, null, cv)
                createdDays += newId to date
            }

            write.setTransactionSuccessful()
        } finally {
            write.endTransaction()
        }

        for ((dayId, date) in createdDays) {
            val payload = JSONObject().apply {
                put("id", dayId)
                put("tripId", tripId)
                put("date", date.toString())
            }.toString()
            outbox.enqueue(OutboxEntityType.DAY, dayId, OutboxOperation.CREATE, payload)
        }
    }

    suspend fun removeDaysOutside(tripId: String, start: LocalDate, end: LocalDate) =
        withContext(Dispatchers.IO) {
            val whereClause = "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ? AND " +
                "(${WaypointDbHelper.COL_IDAY_DATE} < ? OR ${WaypointDbHelper.COL_IDAY_DATE} > ?)"
            val whereArgs = arrayOf(tripId, start.toString(), end.toString())

            // Must read which days (and the flights/places under them) are
            // about to go before deleting: ON DELETE CASCADE (enabled in
            // WaypointDbHelper.onConfigure) removes those flights/places
            // locally as a side effect of the day delete below, without
            // ever going through deleteFlight()/deletePlace() - so their
            // own outbox DELETE entries have to be queued here, or the
            // server never finds out those records are gone.
            val removedDayIds = mutableListOf<String>()
            db.readableDatabase.query(
                WaypointDbHelper.TABLE_ITIN_DAYS, arrayOf(WaypointDbHelper.COL_IDAY_ID),
                whereClause, whereArgs, null, null, null,
            ).use { cursor -> while (cursor.moveToNext()) removedDayIds += cursor.getString(0) }

            if (removedDayIds.isEmpty()) return@withContext

            val removedFlightIds = mutableListOf<String>()
            val removedPlaceIds = mutableListOf<String>()
            for (dayId in removedDayIds) {
                db.readableDatabase.query(
                    WaypointDbHelper.TABLE_ITIN_FLIGHTS, arrayOf(WaypointDbHelper.COL_IFLIGHT_ID),
                    "${WaypointDbHelper.COL_IFLIGHT_DAY_ID} = ?", arrayOf(dayId), null, null, null,
                ).use { cursor -> while (cursor.moveToNext()) removedFlightIds += cursor.getString(0) }
                db.readableDatabase.query(
                    WaypointDbHelper.TABLE_ITIN_PLACES, arrayOf(WaypointDbHelper.COL_IPLACE_ID),
                    "${WaypointDbHelper.COL_IPLACE_DAY_ID} = ?", arrayOf(dayId), null, null, null,
                ).use { cursor -> while (cursor.moveToNext()) removedPlaceIds += cursor.getString(0) }
            }

            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_DAYS,
                whereClause,
                whereArgs,
            )

            for (flightId in removedFlightIds) outbox.enqueue(OutboxEntityType.FLIGHT, flightId, OutboxOperation.DELETE, null)
            for (placeId in removedPlaceIds) outbox.enqueue(OutboxEntityType.PLACE, placeId, OutboxOperation.DELETE, null)
            for (dayId in removedDayIds) outbox.enqueue(OutboxEntityType.DAY, dayId, OutboxOperation.DELETE, null)
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

            // pdfUri is a device-local content:// URI - meaningless on any
            // other device, so it's never sent. docName (the filename the
            // user picked) is still useful cross-device metadata.
            val payload = JSONObject().apply {
                put("id", id)
                put("dayId", dayId)
                if (docName != null) put("docName", docName)
            }.toString()
            outbox.enqueue(OutboxEntityType.FLIGHT, id, OutboxOperation.CREATE, payload)

            id
        }

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

            val payload = JSONObject().apply { put("flightNumber", number) }.toString()
            outbox.enqueue(OutboxEntityType.FLIGHT, flightId, OutboxOperation.UPDATE, payload)
        }

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

            val payload = JSONObject().apply { put("departureTime", time ?: JSONObject.NULL) }.toString()
            outbox.enqueue(OutboxEntityType.FLIGHT, flightId, OutboxOperation.UPDATE, payload)
        }

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

    suspend fun deleteFlight(flightId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_FLIGHTS,
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?",
                arrayOf(flightId),
            )
            outbox.enqueue(OutboxEntityType.FLIGHT, flightId, OutboxOperation.DELETE, null)
        }


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

        // pdfUri is device-local, never synced - see insertFlight.
        val payload = JSONObject().apply {
            put("id", id)
            put("tripId", tripId)
            put("fromDate", fromDate.toString())
            put("toDate", toDate.toString())
            if (docName != null) put("docName", docName)
        }.toString()
        outbox.enqueue(OutboxEntityType.LODGING, id, OutboxOperation.CREATE, payload)

        id
    }

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

    suspend fun deleteLodging(lodgingId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_LODGING,
                "${WaypointDbHelper.COL_ILODGE_ID} = ?",
                arrayOf(lodgingId),
            )
            outbox.enqueue(OutboxEntityType.LODGING, lodgingId, OutboxOperation.DELETE, null)
        }


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

        // pdfUri is device-local, never synced - see insertFlight.
        val payload = JSONObject().apply {
            put("id", id)
            put("tripId", tripId)
            put("fromDate", fromDate.toString())
            put("toDate", toDate.toString())
            if (docName != null) put("docName", docName)
        }.toString()
        outbox.enqueue(OutboxEntityType.CAR, id, OutboxOperation.CREATE, payload)

        id
    }

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

    suspend fun deleteCarRental(carRentalId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_CAR,
                "${WaypointDbHelper.COL_ICAR_ID} = ?",
                arrayOf(carRentalId),
            )
            outbox.enqueue(OutboxEntityType.CAR, carRentalId, OutboxOperation.DELETE, null)
        }


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

        val payload = JSONObject().apply {
            put("id", id)
            put("dayId", dayId)
            put("name", placeName)
            put("category", category)
            if (lat != null) put("lat", lat)
            if (lng != null) put("lng", lng)
            if (note != null) put("note", note)
            if (photoUrl != null) put("photoUrl", photoUrl)
        }.toString()
        outbox.enqueue(OutboxEntityType.PLACE, id, OutboxOperation.CREATE, payload)

        id
    }

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

    suspend fun deletePlace(placeId: String) =
        withContext(Dispatchers.IO) {
            db.writableDatabase.delete(
                WaypointDbHelper.TABLE_ITIN_PLACES,
                "${WaypointDbHelper.COL_IPLACE_ID} = ?",
                arrayOf(placeId),
            )
            outbox.enqueue(OutboxEntityType.PLACE, placeId, OutboxOperation.DELETE, null)
        }

    /** Changes which nights a lodging booking covers. Saved locally first, then synced via the outbox. */
    suspend fun updateLodgingDates(lodgingId: String, fromDate: LocalDate, toDate: LocalDate) =
        updateDateRange(
            table = WaypointDbHelper.TABLE_ITIN_LODGING,
            idColumn = WaypointDbHelper.COL_ILODGE_ID,
            fromColumn = WaypointDbHelper.COL_ILODGE_FROM_DATE,
            toColumn = WaypointDbHelper.COL_ILODGE_TO_DATE,
            updatedColumn = WaypointDbHelper.COL_ILODGE_UPDATED,
            type = OutboxEntityType.LODGING,
            id = lodgingId,
            fromDate = fromDate,
            toDate = toDate,
        )

    /** Changes which days a car rental covers. Saved locally first, then synced via the outbox. */
    suspend fun updateCarRentalDates(carRentalId: String, fromDate: LocalDate, toDate: LocalDate) =
        updateDateRange(
            table = WaypointDbHelper.TABLE_ITIN_CAR,
            idColumn = WaypointDbHelper.COL_ICAR_ID,
            fromColumn = WaypointDbHelper.COL_ICAR_FROM_DATE,
            toColumn = WaypointDbHelper.COL_ICAR_TO_DATE,
            updatedColumn = WaypointDbHelper.COL_ICAR_UPDATED,
            type = OutboxEntityType.CAR,
            id = carRentalId,
            fromDate = fromDate,
            toDate = toDate,
        )

    private suspend fun updateDateRange(
        table: String,
        idColumn: String,
        fromColumn: String,
        toColumn: String,
        updatedColumn: String,
        type: OutboxEntityType,
        id: String,
        fromDate: LocalDate,
        toDate: LocalDate,
    ) = withContext(Dispatchers.IO) {
        require(!toDate.isBefore(fromDate)) { "toDate must not be before fromDate" }
        val cv = ContentValues().apply {
            put(fromColumn, fromDate.toString())
            put(toColumn, toDate.toString())
            put(updatedColumn, System.currentTimeMillis())
        }
        db.writableDatabase.update(table, cv, "$idColumn = ?", arrayOf(id))
        val payload = JSONObject().apply {
            put("fromDate", fromDate.toString())
            put("toDate", toDate.toString())
        }.toString()
        outbox.enqueue(type, id, OutboxOperation.UPDATE, payload)
        Log.d("ItineraryRepository", "Updated $type $id to $fromDate..$toDate")
    }

    /** Sets or clears a place's note. A blank note is stored as no note. */
    suspend fun updatePlaceNote(placeId: String, note: String?) =
        withContext(Dispatchers.IO) {
            val cv = ContentValues().apply {
                if (note != null) put(WaypointDbHelper.COL_IPLACE_NOTE, note) else putNull(WaypointDbHelper.COL_IPLACE_NOTE)
                put(WaypointDbHelper.COL_IPLACE_UPDATED, System.currentTimeMillis())
            }
            db.writableDatabase.update(
                WaypointDbHelper.TABLE_ITIN_PLACES,
                cv,
                "${WaypointDbHelper.COL_IPLACE_ID} = ?",
                arrayOf(placeId),
            )
            val payload = JSONObject().apply { put("note", note ?: JSONObject.NULL) }.toString()
            outbox.enqueue(OutboxEntityType.PLACE, placeId, OutboxOperation.UPDATE, payload)
        }


    // Pulls this trip's itinerary from the cloud and merges it into local
    // SQLite. "Last write wins, keeping original timestamps": an entity
    // with an unsent local change (still in the outbox) is left alone -
    // the cloud doesn't know about that change yet, so it can't be the
    // more recent write. Everything else is brought in line with the
    // cloud, including removing local rows the cloud no longer has (the
    // API has no per-field updatedAt to compare against, and deletes are
    // hard deletes server-side, so "missing from the cloud response" is
    // the only signal available that another device deleted it).
    //
    // Known limitation: if a day is removed here while a flight/place
    // under it has its own unsent pending change, SQLite's ON DELETE
    // CASCADE still removes that child locally. Its outbox entry is
    // untouched and gets attempted later by SyncWorker, which fails
    // cleanly (404, treated as permanent) rather than looping - a rare
    // concurrent-edit case, not handled more precisely than that.
    suspend fun pullAndMergeForTrip(idToken: String, tripId: String) = withContext(Dispatchers.IO) {
        val cloudDays = WaypointApiRepository.getDaysForTrip(idToken, tripId) ?: return@withContext
        val cloudLodging = WaypointApiRepository.getLodgingForTrip(idToken, tripId) ?: emptyList()
        val cloudCars = WaypointApiRepository.getCarsForTrip(idToken, tripId) ?: emptyList()

        mergeDays(tripId, cloudDays)
        mergeLodging(tripId, cloudLodging)
        mergeCars(tripId, cloudCars)

        for (day in cloudDays) {
            val cloudFlights = WaypointApiRepository.getFlightsForDay(idToken, day.id) ?: emptyList()
            val cloudPlaces = WaypointApiRepository.getPlacesForDay(idToken, day.id) ?: emptyList()
            mergeFlights(day.id, cloudFlights)
            mergePlaces(day.id, cloudPlaces)
        }
    }

    private suspend fun mergeDays(tripId: String, cloudDays: List<WaypointApiRepository.DayPayload>) {
        val pending = outbox.pendingEntityIds(OutboxEntityType.DAY)
        val wdb = db.writableDatabase
        val cloudIds = cloudDays.map { it.id }.toSet()

        for (d in cloudDays) {
            if (d.id in pending) continue
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_IDAY_TRIP_ID, d.tripId)
                put(WaypointDbHelper.COL_IDAY_DATE, d.date)
            }
            // Try UPDATE first, INSERT only if no row matched - never
            // INSERT OR REPLACE here, since itinerary_days is the parent
            // side of an ON DELETE CASCADE FK (flights/places reference
            // it); REPLACE deletes-then-reinserts on conflict, which would
            // cascade-wipe every flight/place under this day as a side
            // effect of what's meant to be a plain field update.
            val updated = wdb.update(WaypointDbHelper.TABLE_ITIN_DAYS, cv, "${WaypointDbHelper.COL_IDAY_ID} = ?", arrayOf(d.id))
            if (updated == 0) {
                cv.put(WaypointDbHelper.COL_IDAY_ID, d.id)
                wdb.insert(WaypointDbHelper.TABLE_ITIN_DAYS, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            WaypointDbHelper.TABLE_ITIN_DAYS, arrayOf(WaypointDbHelper.COL_IDAY_ID),
            "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?", arrayOf(tripId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(WaypointDbHelper.TABLE_ITIN_DAYS, "${WaypointDbHelper.COL_IDAY_ID} = ?", arrayOf(id))
            }
        }
    }

    private suspend fun mergeFlights(dayId: String, cloudFlights: List<WaypointApiRepository.FlightPayload>) {
        val pending = outbox.pendingEntityIds(OutboxEntityType.FLIGHT)
        val wdb = db.writableDatabase
        val cloudIds = cloudFlights.map { it.id }.toSet()

        for (f in cloudFlights) {
            if (f.id in pending) continue
            // pdfUri is deliberately absent here - it's device-local and
            // was never in the cloud payload to begin with (see
            // insertFlight), so this never overwrites whatever PDF this
            // device already has attached to the flight.
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_IFLIGHT_DAY_ID, f.dayId)
                put(WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER, f.flightNumber)
                put(WaypointDbHelper.COL_IFLIGHT_DEPARTURE, f.departureTime)
                put(WaypointDbHelper.COL_IFLIGHT_DOC_NAME, f.docName)
                put(WaypointDbHelper.COL_IFLIGHT_CREATED, f.createdAtMs)
            }
            val updated = wdb.update(WaypointDbHelper.TABLE_ITIN_FLIGHTS, cv, "${WaypointDbHelper.COL_IFLIGHT_ID} = ?", arrayOf(f.id))
            if (updated == 0) {
                // pdf_uri is NOT NULL with no column default - only the
                // INSERT path needs an explicit value, since this device
                // has never had one for an id it's only just learned about.
                cv.put(WaypointDbHelper.COL_IFLIGHT_ID, f.id)
                cv.put(WaypointDbHelper.COL_IFLIGHT_PDF_URI, "")
                wdb.insert(WaypointDbHelper.TABLE_ITIN_FLIGHTS, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            WaypointDbHelper.TABLE_ITIN_FLIGHTS, arrayOf(WaypointDbHelper.COL_IFLIGHT_ID),
            "${WaypointDbHelper.COL_IFLIGHT_DAY_ID} = ?", arrayOf(dayId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(WaypointDbHelper.TABLE_ITIN_FLIGHTS, "${WaypointDbHelper.COL_IFLIGHT_ID} = ?", arrayOf(id))
            }
        }
    }

    private suspend fun mergeLodging(tripId: String, cloudLodging: List<WaypointApiRepository.LodgingPayload>) {
        val pending = outbox.pendingEntityIds(OutboxEntityType.LODGING)
        val wdb = db.writableDatabase
        val cloudIds = cloudLodging.map { it.id }.toSet()

        for (l in cloudLodging) {
            if (l.id in pending) continue
            // pdfUri deliberately absent - see mergeFlights.
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_ILODGE_TRIP_ID, l.tripId)
                put(WaypointDbHelper.COL_ILODGE_FROM_DATE, l.fromDate)
                put(WaypointDbHelper.COL_ILODGE_TO_DATE, l.toDate)
                put(WaypointDbHelper.COL_ILODGE_DOC_NAME, l.docName)
                put(WaypointDbHelper.COL_ILODGE_CREATED, l.createdAtMs)
            }
            val updated = wdb.update(WaypointDbHelper.TABLE_ITIN_LODGING, cv, "${WaypointDbHelper.COL_ILODGE_ID} = ?", arrayOf(l.id))
            if (updated == 0) {
                cv.put(WaypointDbHelper.COL_ILODGE_ID, l.id)
                cv.put(WaypointDbHelper.COL_ILODGE_PDF_URI, "")
                wdb.insert(WaypointDbHelper.TABLE_ITIN_LODGING, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            WaypointDbHelper.TABLE_ITIN_LODGING, arrayOf(WaypointDbHelper.COL_ILODGE_ID),
            "${WaypointDbHelper.COL_ILODGE_TRIP_ID} = ?", arrayOf(tripId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(WaypointDbHelper.TABLE_ITIN_LODGING, "${WaypointDbHelper.COL_ILODGE_ID} = ?", arrayOf(id))
            }
        }
    }

    private suspend fun mergeCars(tripId: String, cloudCars: List<WaypointApiRepository.CarPayload>) {
        val pending = outbox.pendingEntityIds(OutboxEntityType.CAR)
        val wdb = db.writableDatabase
        val cloudIds = cloudCars.map { it.id }.toSet()

        for (c0 in cloudCars) {
            if (c0.id in pending) continue
            // pdfUri deliberately absent - see mergeFlights.
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_ICAR_TRIP_ID, c0.tripId)
                put(WaypointDbHelper.COL_ICAR_FROM_DATE, c0.fromDate)
                put(WaypointDbHelper.COL_ICAR_TO_DATE, c0.toDate)
                put(WaypointDbHelper.COL_ICAR_DOC_NAME, c0.docName)
                put(WaypointDbHelper.COL_ICAR_CREATED, c0.createdAtMs)
            }
            val updated = wdb.update(WaypointDbHelper.TABLE_ITIN_CAR, cv, "${WaypointDbHelper.COL_ICAR_ID} = ?", arrayOf(c0.id))
            if (updated == 0) {
                cv.put(WaypointDbHelper.COL_ICAR_ID, c0.id)
                cv.put(WaypointDbHelper.COL_ICAR_PDF_URI, "")
                wdb.insert(WaypointDbHelper.TABLE_ITIN_CAR, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            WaypointDbHelper.TABLE_ITIN_CAR, arrayOf(WaypointDbHelper.COL_ICAR_ID),
            "${WaypointDbHelper.COL_ICAR_TRIP_ID} = ?", arrayOf(tripId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(WaypointDbHelper.TABLE_ITIN_CAR, "${WaypointDbHelper.COL_ICAR_ID} = ?", arrayOf(id))
            }
        }
    }

    private suspend fun mergePlaces(dayId: String, cloudPlaces: List<WaypointApiRepository.PlacePayload>) {
        val pending = outbox.pendingEntityIds(OutboxEntityType.PLACE)
        val wdb = db.writableDatabase
        val cloudIds = cloudPlaces.map { it.id }.toSet()

        for (p in cloudPlaces) {
            if (p.id in pending) continue
            val cv = ContentValues().apply {
                put(WaypointDbHelper.COL_IPLACE_DAY_ID, p.dayId)
                put(WaypointDbHelper.COL_IPLACE_NAME, p.name)
                // place_category is NOT NULL locally but nullable from the
                // API (places.js makes it optional on create) - default to
                // empty rather than violate the constraint.
                put(WaypointDbHelper.COL_IPLACE_CATEGORY, p.category ?: "")
                put(WaypointDbHelper.COL_IPLACE_LAT, p.lat)
                put(WaypointDbHelper.COL_IPLACE_LNG, p.lng)
                put(WaypointDbHelper.COL_IPLACE_NOTE, p.note)
                put(WaypointDbHelper.COL_IPLACE_PHOTO_URL, p.photoUrl)
                put(WaypointDbHelper.COL_IPLACE_CREATED, p.createdAtMs)
            }
            val updated = wdb.update(WaypointDbHelper.TABLE_ITIN_PLACES, cv, "${WaypointDbHelper.COL_IPLACE_ID} = ?", arrayOf(p.id))
            if (updated == 0) {
                cv.put(WaypointDbHelper.COL_IPLACE_ID, p.id)
                wdb.insert(WaypointDbHelper.TABLE_ITIN_PLACES, null, cv)
            }
        }

        val localIds = mutableListOf<String>()
        wdb.query(
            WaypointDbHelper.TABLE_ITIN_PLACES, arrayOf(WaypointDbHelper.COL_IPLACE_ID),
            "${WaypointDbHelper.COL_IPLACE_DAY_ID} = ?", arrayOf(dayId), null, null, null,
        ).use { c -> while (c.moveToNext()) localIds += c.getString(0) }
        for (id in localIds) {
            if (id !in cloudIds && id !in pending) {
                wdb.delete(WaypointDbHelper.TABLE_ITIN_PLACES, "${WaypointDbHelper.COL_IPLACE_ID} = ?", arrayOf(id))
            }
        }
    }
}
