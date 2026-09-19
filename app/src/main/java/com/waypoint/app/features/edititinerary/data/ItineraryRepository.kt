// declares that this file belongs to the package `com.waypoint.app.features.edititinerary.data`
package com.waypoint.app.features.edititinerary.data

// imports `android.content.ContentValues` for use in this file
import android.content.ContentValues
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `com.waypoint.app.core.db.WaypointDbHelper` for use in this file
import com.waypoint.app.core.db.WaypointDbHelper
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.util.UUID` for use in this file
import java.util.UUID

// declares class `ItineraryRepository` with a primary constructor taking 1 parameter (`context`) and opens its body
class ItineraryRepository(context: Context) {

    // declares private read-only property `db`, initialised with the result of calling `WaypointDbHelper.getInstance(…)`
    private val db = WaypointDbHelper.getInstance(context)


    // expression: `suspend fun replaceSelectedDays(`
    suspend fun replaceSelectedDays(
        // continues the statement started above: `tripId: String,`
        tripId: String,
        // continues the statement started above: `days: Set<LocalDate>,`
        days: Set<LocalDate>,
    // continues the statement started above: `) = withContext(Dispatchers.IO) {`
    ) = withContext(Dispatchers.IO) {
        // declares read-only property `write`, initialised to `db.writableDatabase`
        val write = db.writableDatabase
        // calls `beginTransaction` on `write` with arguments `()`
        write.beginTransaction()
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `existing` of type `Map<LocalDate, String>`, initialised to `run` and opens a lambda / block
            val existing: Map<LocalDate, String> = run {
                // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
                val cursor = db.readableDatabase.query(
                    // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_DAYS,`
                    WaypointDbHelper.TABLE_ITIN_DAYS,
                    // continues the statement started above: `arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_…`
                    arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_IDAY_DATE),
                    // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",`
                    "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                    // continues the statement started above: `arrayOf(tripId),`
                    arrayOf(tripId),
                    // continues the statement started above: `null, null, null,`
                    null, null, null,
                // closes the multi-line argument list started above
                )
                // declares read-only property `map`, initialised with the result of calling `mutableMapOf(…)`
                val map = mutableMapOf<LocalDate, String>()
                // expression: `cursor.use { c ->`
                cursor.use { c ->
                    // continues the statement started above: `while (c.moveToNext()) {`
                    while (c.moveToNext()) {
                        // opens a block after `runCatching`
                        runCatching {
                            // assigns `map[LocalDate.parse(c.getString(1))]` the value `c.getString(0)`
                            map[LocalDate.parse(c.getString(1))] = c.getString(0)
                        // closes the block
                        }
                    // closes the block
                    }
                // closes the block
                }
                // expression: `map`
                map
            // closes the lambda assigned to `existing`

            }

            // declares read-only property `datesToAdd`, initialised to `days - existing.keys`
            val datesToAdd = days - existing.keys
            // `for` loop: iterates over `datesToAdd`, binding each element to `date`
            for (date in datesToAdd) {
                // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
                val cv = ContentValues().apply {
                    // calls `put` with arguments `(WaypointDbHelper.COL_IDAY_ID, UUID.randomUUI…)`
                    put(WaypointDbHelper.COL_IDAY_ID,      UUID.randomUUID().toString())
                    // calls `put` with arguments `(WaypointDbHelper.COL_IDAY_TRIP_ID, tripId)`
                    put(WaypointDbHelper.COL_IDAY_TRIP_ID, tripId)
                    // calls `put` with arguments `(WaypointDbHelper.COL_IDAY_DATE, date.toStrin…)`
                    put(WaypointDbHelper.COL_IDAY_DATE,    date.toString())
                // closes the lambda assigned to `cv`
                }
                // calls `insert` on `write` with arguments `(WaypointDbHelper.TABLE_ITIN_DAYS, null, cv)`
                write.insert(WaypointDbHelper.TABLE_ITIN_DAYS, null, cv)
            // closes the for loop
            }

            // calls `setTransactionSuccessful` on `write` with arguments `()`
            write.setTransactionSuccessful()
        // `finally` block: always runs after the `try`/`catch`, whether or not an exception occurred
        } finally {
            // calls `endTransaction` on `write` with arguments `()`
            write.endTransaction()
        // closes the finally block
        }
    // closes the block
    }

    // declares suspend function `removeDaysOutside` taking 3 parameters (`tripId`, `start`, `end`); its body is the expression ``
    suspend fun removeDaysOutside(tripId: String, start: LocalDate, end: LocalDate) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.delete(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_DAYS,`
                WaypointDbHelper.TABLE_ITIN_DAYS,
                // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_TRIP_ID} = ? AND (${WaypointDb…`
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ? AND (${WaypointDbHelper.COL_IDAY_DATE} < ? OR ${WaypointDbHelper.COL_IDAY_DATE} > ?)",
                // continues the statement started above: `arrayOf(tripId, start.toString(), end.toString()),`
                arrayOf(tripId, start.toString(), end.toString()),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

    // declares suspend function `getSelectedDays` taking 1 parameter (`tripId`), returning `List<LocalDate>`; its body is the expression ``
    suspend fun getSelectedDays(tripId: String): List<LocalDate> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `read`, initialised to `db.readableDatabase`
            val read = db.readableDatabase
            // declares read-only property `cursor`, initialised with the result of calling `read.query(…)`
            val cursor = read.query(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_DAYS,`
                WaypointDbHelper.TABLE_ITIN_DAYS,
                // continues the statement started above: `arrayOf(WaypointDbHelper.COL_IDAY_DATE),`
                arrayOf(WaypointDbHelper.COL_IDAY_DATE),
                // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",`
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                // continues the statement started above: `arrayOf(tripId),`
                arrayOf(tripId),
                // continues the statement started above: `null, null,`
                null, null,
                // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_DATE} ASC",`
                "${WaypointDbHelper.COL_IDAY_DATE} ASC",
            // closes the multi-line argument list started above
            )
            // declares read-only property `result`, initialised with the result of calling `mutableListOf(…)`
            val result = mutableListOf<LocalDate>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // opens a block after `runCatching`
                    runCatching {
                        // calls `add` on `result` with arguments `(LocalDate.parse(it.getString(0)))`
                        result.add(LocalDate.parse(it.getString(0)))
                    // closes the block
                    }
                // closes the while loop
                }
            // closes the block
            }
            // expression: `result`
            result
        // closes the block
        }


    // declares suspend function `getSelectedDaysWithIds` taking 1 parameter (`tripId`), returning `List<ItineraryDayEntity>`; its body is the expression ``
    suspend fun getSelectedDaysWithIds(tripId: String): List<ItineraryDayEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
            val cursor = db.readableDatabase.query(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_DAYS,`
                WaypointDbHelper.TABLE_ITIN_DAYS,
                // continues the statement started above: `arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_…`
                arrayOf(WaypointDbHelper.COL_IDAY_ID, WaypointDbHelper.COL_IDAY_DATE),
                // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",`
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                // continues the statement started above: `arrayOf(tripId),`
                arrayOf(tripId),
                // continues the statement started above: `null, null,`
                null, null,
                // continues the statement started above: `"${WaypointDbHelper.COL_IDAY_DATE} ASC",`
                "${WaypointDbHelper.COL_IDAY_DATE} ASC",
            // closes the multi-line argument list started above
            )
            // declares read-only property `result`, initialised with the result of calling `mutableListOf(…)`
            val result = mutableListOf<ItineraryDayEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // opens a block after `runCatching`
                    runCatching {
                        // calls `add` on `result` with an argument list that continues on the following lines
                        result.add(
                            // continues the statement started above: `ItineraryDayEntity(`
                            ItineraryDayEntity(
                                // continues the statement started above: `id = it.getString(0),`
                                id     = it.getString(0),
                                // continues the statement started above: `tripId = tripId,`
                                tripId = tripId,
                                // continues the statement started above: `date = LocalDate.parse(it.getString(1)),`
                                date   = LocalDate.parse(it.getString(1)),
                            // closes the multi-line argument list started above
                            )
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the while loop
                }
            // closes the block
            }
            // expression: `result`
            result
        // closes the block
        }


    // declares suspend function `insertFlight` taking 3 parameters (`dayId`, `pdfUri`, `docName`), returning `String`; its body is the expression ``
    suspend fun insertFlight(dayId: String, pdfUri: String, docName: String? = null): String =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `id`, initialised with the result of calling `UUID.randomUUID(…)`
            val id = UUID.randomUUID().toString()
            // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
            val cv = ContentValues().apply {
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_ID, id)`
                put(WaypointDbHelper.COL_IFLIGHT_ID,      id)
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_DAY_ID, dayId)`
                put(WaypointDbHelper.COL_IFLIGHT_DAY_ID,  dayId)
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_PDF_URI, pdfUri)`
                put(WaypointDbHelper.COL_IFLIGHT_PDF_URI, pdfUri)
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_DOC_NAME, docNa…)`
                put(WaypointDbHelper.COL_IFLIGHT_DOC_NAME, docName)
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_CREATED, System…)`
                put(WaypointDbHelper.COL_IFLIGHT_CREATED, System.currentTimeMillis())
            // closes the lambda assigned to `cv`
            }
            // calls `insert` on `db.writableDatabase` with arguments `(WaypointDbHelper.TABLE_ITIN_FLIGHTS, null, cv)`
            db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_FLIGHTS, null, cv)
            // expression: `id`
            id
        // closes the block
        }

    // declares suspend function `updateFlightNumber` taking 2 parameters (`flightId`, `number`); its body is the expression ``
    suspend fun updateFlightNumber(flightId: String, number: String) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
            val cv = ContentValues().apply {
                // calls `put` with arguments `(WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER, …)`
                put(WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER, number)
            // closes the lambda assigned to `cv`
            }
            // calls `update` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.update(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_FLIGHTS,`
                WaypointDbHelper.TABLE_ITIN_FLIGHTS,
                // continues the statement started above: `cv,`
                cv,
                // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_ID} = ?",`
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?",
                // continues the statement started above: `arrayOf(flightId),`
                arrayOf(flightId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

    // declares suspend function `updateFlightDepartureTime` taking 2 parameters (`flightId`, `time`); its body is the expression ``
    suspend fun updateFlightDepartureTime(flightId: String, time: String?) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
            val cv = ContentValues().apply {
                // `if` statement: executes `putNull(WaypointDbHelper.COL_IFLIGHT_DEPARTU…` when `time == null` is true
                if (time == null) putNull(WaypointDbHelper.COL_IFLIGHT_DEPARTURE)
                // statement: `else put(WaypointDbHelper.COL_IFLIGHT_DEPARTURE, time)`
                else put(WaypointDbHelper.COL_IFLIGHT_DEPARTURE, time)
            // closes the lambda assigned to `cv`
            }
            // calls `update` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.update(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_FLIGHTS, cv,`
                WaypointDbHelper.TABLE_ITIN_FLIGHTS, cv,
                // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_ID} = ?", arrayOf(flightId),`
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?", arrayOf(flightId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }

    // declares suspend function `getFlightsForDays` taking 1 parameter (`dayIds`), returning `List<FlightEntity>`; its body is the expression ``
    suspend fun getFlightsForDays(dayIds: List<String>): List<FlightEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // `if` statement: executes `return@withContext emptyList()` when `dayIds.isEmpty()` is true
            if (dayIds.isEmpty()) return@withContext emptyList()
            // declares read-only property `placeholders`, initialised with the result of calling `dayIds.joinToString(…)`
            val placeholders = dayIds.joinToString(",") { "?" }
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.rawQuery(…)`
            val cursor = db.readableDatabase.rawQuery(
                // continues the statement started above: `"SELECT ${WaypointDbHelper.COL_IFLIGHT_ID}, " +`
                "SELECT ${WaypointDbHelper.COL_IFLIGHT_ID}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_DAY_ID}, " +`
                    "${WaypointDbHelper.COL_IFLIGHT_DAY_ID}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER}, " +`
                    "${WaypointDbHelper.COL_IFLIGHT_FLIGHT_NUMBER}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_PDF_URI}, " +`
                    "${WaypointDbHelper.COL_IFLIGHT_PDF_URI}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_CREATED}, " +`
                    "${WaypointDbHelper.COL_IFLIGHT_CREATED}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_DEPARTURE}, " +`
                    "${WaypointDbHelper.COL_IFLIGHT_DEPARTURE}, " +
                    // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_DOC_NAME} " +`
                    "${WaypointDbHelper.COL_IFLIGHT_DOC_NAME} " +
                    // continues the statement started above: `"FROM ${WaypointDbHelper.TABLE_ITIN_FLIGHTS} " +`
                    "FROM ${WaypointDbHelper.TABLE_ITIN_FLIGHTS} " +
                    // continues the statement started above: `"WHERE ${WaypointDbHelper.COL_IFLIGHT_DAY_ID} IN ($placehol…`
                    "WHERE ${WaypointDbHelper.COL_IFLIGHT_DAY_ID} IN ($placeholders) " +
                    // continues the statement started above: `"ORDER BY ${WaypointDbHelper.COL_IFLIGHT_CREATED} ASC",`
                    "ORDER BY ${WaypointDbHelper.COL_IFLIGHT_CREATED} ASC",
                // continues the statement started above: `dayIds.toTypedArray(),`
                dayIds.toTypedArray(),
            // closes the multi-line argument list started above
            )
            // declares read-only property `result`, initialised with the result of calling `mutableListOf(…)`
            val result = mutableListOf<FlightEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // calls `add` on `result` with an argument list that continues on the following lines
                    result.add(
                        // continues the statement started above: `FlightEntity(`
                        FlightEntity(
                            // continues the statement started above: `id = it.getString(0),`
                            id           = it.getString(0),
                            // continues the statement started above: `dayId = it.getString(1),`
                            dayId        = it.getString(1),
                            // continues the statement started above: `flightNumber = it.getString(2),`
                            flightNumber = it.getString(2),
                            // continues the statement started above: `pdfUri = it.getString(3),`
                            pdfUri       = it.getString(3),
                            // continues the statement started above: `createdAtMs = it.getLong(4),`
                            createdAtMs  = it.getLong(4),
                            // continues the statement started above: `departureTime = if (it.isNull(5)) null else it.getString(5),`
                            departureTime = if (it.isNull(5)) null else it.getString(5),
                            // continues the statement started above: `docName = if (it.isNull(6)) null else it.getString(6),`
                            docName      = if (it.isNull(6)) null else it.getString(6),
                        // closes the multi-line argument list started above
                        )
                    // closes the multi-line argument list started above
                    )
                // closes the while loop
                }
            // closes the block
            }
            // expression: `result`
            result
        // closes the block
        }

    // declares suspend function `deleteFlight` taking 1 parameter (`flightId`); its body is the expression ``
    suspend fun deleteFlight(flightId: String) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.delete(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_FLIGHTS,`
                WaypointDbHelper.TABLE_ITIN_FLIGHTS,
                // continues the statement started above: `"${WaypointDbHelper.COL_IFLIGHT_ID} = ?",`
                "${WaypointDbHelper.COL_IFLIGHT_ID} = ?",
                // continues the statement started above: `arrayOf(flightId),`
                arrayOf(flightId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }


    // expression: `suspend fun insertLodging(`
    suspend fun insertLodging(
        // continues the statement started above: `tripId: String,`
        tripId: String,
        // continues the statement started above: `fromDate: LocalDate,`
        fromDate: LocalDate,
        // continues the statement started above: `toDate: LocalDate,`
        toDate: LocalDate,
        // continues the statement started above: `pdfUri: String,`
        pdfUri: String,
        // continues the statement started above: `docName: String? = null,`
        docName: String? = null,
    // continues the statement started above: `): String = withContext(Dispatchers.IO) {`
    ): String = withContext(Dispatchers.IO) {
        // declares read-only property `id`, initialised with the result of calling `UUID.randomUUID(…)`
        val id = UUID.randomUUID().toString()
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_ID, id)`
            put(WaypointDbHelper.COL_ILODGE_ID,        id)
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_TRIP_ID, tripId)`
            put(WaypointDbHelper.COL_ILODGE_TRIP_ID,   tripId)
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_FROM_DATE, fromD…)`
            put(WaypointDbHelper.COL_ILODGE_FROM_DATE, fromDate.toString())
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_TO_DATE, toDate.…)`
            put(WaypointDbHelper.COL_ILODGE_TO_DATE,   toDate.toString())
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_PDF_URI, pdfUri)`
            put(WaypointDbHelper.COL_ILODGE_PDF_URI,   pdfUri)
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_CREATED, System.…)`
            put(WaypointDbHelper.COL_ILODGE_CREATED,   System.currentTimeMillis())
            // calls `put` with arguments `(WaypointDbHelper.COL_ILODGE_DOC_NAME, docName)`
            put(WaypointDbHelper.COL_ILODGE_DOC_NAME,  docName)
        // closes the lambda assigned to `cv`
        }
        // calls `insert` on `db.writableDatabase` with arguments `(WaypointDbHelper.TABLE_ITIN_LODGING, null, cv)`
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_LODGING, null, cv)
        // expression: `id`
        id
    // closes the block
    }

    // declares suspend function `getLodgingsForTrip` taking 1 parameter (`tripId`), returning `List<LodgingEntity>`; its body is the expression ``
    suspend fun getLodgingsForTrip(tripId: String): List<LodgingEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
            val cursor = db.readableDatabase.query(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_LODGING, null,`
                WaypointDbHelper.TABLE_ITIN_LODGING, null,
                // continues the statement started above: `"${WaypointDbHelper.COL_ILODGE_TRIP_ID} = ?", arrayOf(tripI…`
                "${WaypointDbHelper.COL_ILODGE_TRIP_ID} = ?", arrayOf(tripId),
                // continues the statement started above: `null, null, "${WaypointDbHelper.COL_ILODGE_CREATED} ASC",`
                null, null, "${WaypointDbHelper.COL_ILODGE_CREATED} ASC",
            // closes the multi-line argument list started above
            )
            // declares read-only property `out`, initialised with the result of calling `mutableListOf(…)`
            val out = mutableListOf<LodgingEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // declares read-only property `nameIdx`, initialised with the result of calling `it.getColumnIndex(…)`
                    val nameIdx = it.getColumnIndex(WaypointDbHelper.COL_ILODGE_DOC_NAME)
                    // adds to `out` the value `LodgingEntity(`
                    out += LodgingEntity(
                        // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper…`
                        id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_ID)),
                        // continues the statement started above: `tripId = tripId,`
                        tripId      = tripId,
                        // continues the statement started above: `fromDate = LocalDate.parse(it.getString(it.getColumnIndexOr…`
                        fromDate    = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_FROM_DATE))),
                        // continues the statement started above: `toDate = LocalDate.parse(it.getString(it.getColumnIndexOrTh…`
                        toDate      = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_TO_DATE))),
                        // continues the statement started above: `pdfUri = it.getString(it.getColumnIndexOrThrow(WaypointDbHe…`
                        pdfUri      = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_PDF_URI)),
                        // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointD…`
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ILODGE_CREATED)),
                        // continues the statement started above: `docName = if (nameIdx < 0 || it.isNull(nameIdx)) null else …`
                        docName     = if (nameIdx < 0 || it.isNull(nameIdx)) null else it.getString(nameIdx),
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

    // declares suspend function `deleteLodging` taking 1 parameter (`lodgingId`); its body is the expression ``
    suspend fun deleteLodging(lodgingId: String) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.delete(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_LODGING,`
                WaypointDbHelper.TABLE_ITIN_LODGING,
                // continues the statement started above: `"${WaypointDbHelper.COL_ILODGE_ID} = ?",`
                "${WaypointDbHelper.COL_ILODGE_ID} = ?",
                // continues the statement started above: `arrayOf(lodgingId),`
                arrayOf(lodgingId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }


    // expression: `suspend fun insertCarRental(`
    suspend fun insertCarRental(
        // continues the statement started above: `tripId: String,`
        tripId: String,
        // continues the statement started above: `fromDate: LocalDate,`
        fromDate: LocalDate,
        // continues the statement started above: `toDate: LocalDate,`
        toDate: LocalDate,
        // continues the statement started above: `pdfUri: String,`
        pdfUri: String,
        // continues the statement started above: `docName: String? = null,`
        docName: String? = null,
    // continues the statement started above: `): String = withContext(Dispatchers.IO) {`
    ): String = withContext(Dispatchers.IO) {
        // declares read-only property `id`, initialised with the result of calling `UUID.randomUUID(…)`
        val id = UUID.randomUUID().toString()
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_ID, id)`
            put(WaypointDbHelper.COL_ICAR_ID,        id)
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_TRIP_ID, tripId)`
            put(WaypointDbHelper.COL_ICAR_TRIP_ID,   tripId)
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_FROM_DATE, fromDat…)`
            put(WaypointDbHelper.COL_ICAR_FROM_DATE, fromDate.toString())
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_TO_DATE, toDate.to…)`
            put(WaypointDbHelper.COL_ICAR_TO_DATE,   toDate.toString())
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_PDF_URI, pdfUri)`
            put(WaypointDbHelper.COL_ICAR_PDF_URI,   pdfUri)
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_CREATED, System.cu…)`
            put(WaypointDbHelper.COL_ICAR_CREATED,   System.currentTimeMillis())
            // calls `put` with arguments `(WaypointDbHelper.COL_ICAR_DOC_NAME, docName)`
            put(WaypointDbHelper.COL_ICAR_DOC_NAME,  docName)
        // closes the lambda assigned to `cv`
        }
        // calls `insert` on `db.writableDatabase` with arguments `(WaypointDbHelper.TABLE_ITIN_CAR, null, cv)`
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_CAR, null, cv)
        // expression: `id`
        id
    // closes the block
    }

    // declares suspend function `getCarRentalsForTrip` taking 1 parameter (`tripId`), returning `List<CarRentalEntity>`; its body is the expression ``
    suspend fun getCarRentalsForTrip(tripId: String): List<CarRentalEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
            val cursor = db.readableDatabase.query(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_CAR, null,`
                WaypointDbHelper.TABLE_ITIN_CAR, null,
                // continues the statement started above: `"${WaypointDbHelper.COL_ICAR_TRIP_ID} = ?", arrayOf(tripId),`
                "${WaypointDbHelper.COL_ICAR_TRIP_ID} = ?", arrayOf(tripId),
                // continues the statement started above: `null, null, "${WaypointDbHelper.COL_ICAR_CREATED} ASC",`
                null, null, "${WaypointDbHelper.COL_ICAR_CREATED} ASC",
            // closes the multi-line argument list started above
            )
            // declares read-only property `out`, initialised with the result of calling `mutableListOf(…)`
            val out = mutableListOf<CarRentalEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // declares read-only property `nameIdx`, initialised with the result of calling `it.getColumnIndex(…)`
                    val nameIdx = it.getColumnIndex(WaypointDbHelper.COL_ICAR_DOC_NAME)
                    // adds to `out` the value `CarRentalEntity(`
                    out += CarRentalEntity(
                        // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper…`
                        id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_ID)),
                        // continues the statement started above: `tripId = tripId,`
                        tripId      = tripId,
                        // continues the statement started above: `fromDate = LocalDate.parse(it.getString(it.getColumnIndexOr…`
                        fromDate    = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_FROM_DATE))),
                        // continues the statement started above: `toDate = LocalDate.parse(it.getString(it.getColumnIndexOrTh…`
                        toDate      = LocalDate.parse(it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_TO_DATE))),
                        // continues the statement started above: `pdfUri = it.getString(it.getColumnIndexOrThrow(WaypointDbHe…`
                        pdfUri      = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_PDF_URI)),
                        // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointD…`
                        createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_ICAR_CREATED)),
                        // continues the statement started above: `docName = if (nameIdx < 0 || it.isNull(nameIdx)) null else …`
                        docName     = if (nameIdx < 0 || it.isNull(nameIdx)) null else it.getString(nameIdx),
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

    // declares suspend function `deleteCarRental` taking 1 parameter (`carRentalId`); its body is the expression ``
    suspend fun deleteCarRental(carRentalId: String) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.delete(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_CAR,`
                WaypointDbHelper.TABLE_ITIN_CAR,
                // continues the statement started above: `"${WaypointDbHelper.COL_ICAR_ID} = ?",`
                "${WaypointDbHelper.COL_ICAR_ID} = ?",
                // continues the statement started above: `arrayOf(carRentalId),`
                arrayOf(carRentalId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }


    // expression: `suspend fun insertPlace(`
    suspend fun insertPlace(
        // continues the statement started above: `dayId: String,`
        dayId: String,
        // continues the statement started above: `placeName: String,`
        placeName: String,
        // continues the statement started above: `category: String,`
        category: String,
        // continues the statement started above: `lat: Double? = null,`
        lat: Double? = null,
        // continues the statement started above: `lng: Double? = null,`
        lng: Double? = null,
        // continues the statement started above: `note: String? = null,`
        note: String? = null,
        // continues the statement started above: `photoUrl: String? = null,`
        photoUrl: String? = null,
    // continues the statement started above: `): String = withContext(Dispatchers.IO) {`
    ): String = withContext(Dispatchers.IO) {
        // declares read-only property `id`, initialised with the result of calling `UUID.randomUUID(…)`
        val id = UUID.randomUUID().toString()
        // declares read-only property `cv`, initialised with the result of calling `ContentValues(…)` and opens a lambda / block
        val cv = ContentValues().apply {
            // calls `put` with arguments `(WaypointDbHelper.COL_IPLACE_ID, id)`
            put(WaypointDbHelper.COL_IPLACE_ID,       id)
            // calls `put` with arguments `(WaypointDbHelper.COL_IPLACE_DAY_ID, dayId)`
            put(WaypointDbHelper.COL_IPLACE_DAY_ID,   dayId)
            // calls `put` with arguments `(WaypointDbHelper.COL_IPLACE_NAME, placeName)`
            put(WaypointDbHelper.COL_IPLACE_NAME,     placeName)
            // calls `put` with arguments `(WaypointDbHelper.COL_IPLACE_CATEGORY, catego…)`
            put(WaypointDbHelper.COL_IPLACE_CATEGORY, category)
            // `if` statement: executes `put(WaypointDbHelper.COL_IPLACE_LAT, lat)` when `lat != null` is true
            if (lat != null) put(WaypointDbHelper.COL_IPLACE_LAT, lat)
            // `if` statement: executes `put(WaypointDbHelper.COL_IPLACE_LNG, lng)` when `lng != null` is true
            if (lng != null) put(WaypointDbHelper.COL_IPLACE_LNG, lng)
            // `if` statement: executes `put(WaypointDbHelper.COL_IPLACE_NOTE, note)` when `note != null` is true
            if (note != null) put(WaypointDbHelper.COL_IPLACE_NOTE, note)
            // `if` statement: executes `put(WaypointDbHelper.COL_IPLACE_PHOTO_URL, p…` when `photoUrl != null` is true
            if (photoUrl != null) put(WaypointDbHelper.COL_IPLACE_PHOTO_URL, photoUrl)
            // calls `put` with arguments `(WaypointDbHelper.COL_IPLACE_CREATED, System.…)`
            put(WaypointDbHelper.COL_IPLACE_CREATED, System.currentTimeMillis())
        // closes the lambda assigned to `cv`
        }
        // calls `insert` on `db.writableDatabase` with arguments `(WaypointDbHelper.TABLE_ITIN_PLACES, null, cv)`
        db.writableDatabase.insert(WaypointDbHelper.TABLE_ITIN_PLACES, null, cv)
        // expression: `id`
        id
    // closes the block
    }

    // declares suspend function `getPlacesForDay` taking 1 parameter (`dayId`), returning `List<PlaceEntity>`; its body is the expression ``
    suspend fun getPlacesForDay(dayId: String): List<PlaceEntity> =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `cursor`, initialised with the result of calling `db.readableDatabase.query(…)`
            val cursor = db.readableDatabase.query(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_PLACES,`
                WaypointDbHelper.TABLE_ITIN_PLACES,
                // continues the statement started above: `null,`
                null,
                // continues the statement started above: `"${WaypointDbHelper.COL_IPLACE_DAY_ID} = ?",`
                "${WaypointDbHelper.COL_IPLACE_DAY_ID} = ?",
                // continues the statement started above: `arrayOf(dayId),`
                arrayOf(dayId),
                // continues the statement started above: `null, null,`
                null, null,
                // continues the statement started above: `"${WaypointDbHelper.COL_IPLACE_CREATED} ASC",`
                "${WaypointDbHelper.COL_IPLACE_CREATED} ASC",
            // closes the multi-line argument list started above
            )
            // declares read-only property `result`, initialised with the result of calling `mutableListOf(…)`
            val result = mutableListOf<PlaceEntity>()
            // opens a block after `cursor.use`
            cursor.use {
                // `while` loop: repeats the block below as long as `it.moveToNext()` is true
                while (it.moveToNext()) {
                    // declares read-only property `latIdx`, initialised with the result of calling `it.getColumnIndex(…)`
                    val latIdx   = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_LAT)
                    // declares read-only property `lngIdx`, initialised with the result of calling `it.getColumnIndex(…)`
                    val lngIdx   = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_LNG)
                    // declares read-only property `photoIdx`, initialised with the result of calling `it.getColumnIndex(…)`
                    val photoIdx = it.getColumnIndex(WaypointDbHelper.COL_IPLACE_PHOTO_URL)
                    // calls `add` on `result` with an argument list that continues on the following lines
                    result.add(
                        // continues the statement started above: `PlaceEntity(`
                        PlaceEntity(
                            // continues the statement started above: `id = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper…`
                            id          = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_ID)),
                            // continues the statement started above: `dayId = dayId,`
                            dayId       = dayId,
                            // continues the statement started above: `name = it.getString(it.getColumnIndexOrThrow(WaypointDbHelp…`
                            name        = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_NAME)),
                            // continues the statement started above: `category = it.getString(it.getColumnIndexOrThrow(WaypointDb…`
                            category    = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_CATEGORY)),
                            // continues the statement started above: `lat = if (latIdx >= 0 && !it.isNull(latIdx)) it.getDouble(l…`
                            lat         = if (latIdx >= 0 && !it.isNull(latIdx)) it.getDouble(latIdx) else null,
                            // continues the statement started above: `lng = if (lngIdx >= 0 && !it.isNull(lngIdx)) it.getDouble(l…`
                            lng         = if (lngIdx >= 0 && !it.isNull(lngIdx)) it.getDouble(lngIdx) else null,
                            // continues the statement started above: `note = it.getString(it.getColumnIndexOrThrow(WaypointDbHelp…`
                            note        = it.getString(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_NOTE)),
                            // continues the statement started above: `photoUrl = if (photoIdx >= 0 && !it.isNull(photoIdx)) it.ge…`
                            photoUrl    = if (photoIdx >= 0 && !it.isNull(photoIdx)) it.getString(photoIdx) else null,
                            // continues the statement started above: `createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointD…`
                            createdAtMs = it.getLong(it.getColumnIndexOrThrow(WaypointDbHelper.COL_IPLACE_CREATED)),
                        // closes the multi-line argument list started above
                        )
                    // closes the multi-line argument list started above
                    )
                // closes the while loop
                }
            // closes the block
            }
            // expression: `result`
            result
        // closes the block
        }

    // declares suspend function `deletePlace` taking 1 parameter (`placeId`); its body is the expression ``
    suspend fun deletePlace(placeId: String) =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // calls `delete` on `db.writableDatabase` with an argument list that continues on the following lines
            db.writableDatabase.delete(
                // continues the statement started above: `WaypointDbHelper.TABLE_ITIN_PLACES,`
                WaypointDbHelper.TABLE_ITIN_PLACES,
                // continues the statement started above: `"${WaypointDbHelper.COL_IPLACE_ID} = ?",`
                "${WaypointDbHelper.COL_IPLACE_ID} = ?",
                // continues the statement started above: `arrayOf(placeId),`
                arrayOf(placeId),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
// closes the class `ItineraryRepository`
}
