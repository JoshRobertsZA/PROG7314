package com.example.prog7314.features.edititinerary.data

import android.content.ContentValues
import android.content.Context
import com.example.prog7314.core.db.WaypointDbHelper
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
            // Clear existing days for this trip
            write.delete(
                WaypointDbHelper.TABLE_ITIN_DAYS,
                "${WaypointDbHelper.COL_IDAY_TRIP_ID} = ?",
                arrayOf(tripId),
            )
            // Insert each selected day
            for (date in days) {
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
}
