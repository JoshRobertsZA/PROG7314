package com.waypoint.app.core.network

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject

/**
 * Flight lookups on AirLabs: status, route, terminal and gate by flight number and travel date, and
 * the departure time used by the trip reminders. Results are cached and calls go through
 * [UsageGuardian].
 */
object AirLabsRepository {

    private const val TAG = "AirLabsRepo"
    const val AIRLABS_MISS = "AIRLABS_MISS"

    fun cacheKey(flightNumber: String, date: String?): String {
        val iata = flightNumber.replace(" ", "").uppercase()
        // returns the bare flight number when there is no date, otherwise the flight number and the date joined by a pipe
        return if (date.isNullOrBlank()) iata else "$iata|${date.trim()}"
    }

    fun pickScheduleEntry(list: JSONArray, date: String?): JSONObject? {
        if (list.length() == 0) return null
        if (date.isNullOrBlank()) return list.getJSONObject(0)
        for (i in 0 until list.length()) {
            val entry = list.getJSONObject(i)
            if (entry.optString("dep_time", "").startsWith(date.trim())) return entry
        }
        return null
    }

    suspend fun lookupDepartureTime(flightNumber: String, date: String? = null): String? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        val iata = flightNumber.replace(" ", "").uppercase()
        if (iata.isBlank()) return@withContext null

        if (!UsageGuardian.isAllowed(GuardedApi.AIRLABS)) {
            Log.w(TAG, "AirLabs budget used up -- skipping call for $iata")
            return@withContext null
        }
        UsageGuardian.recordUse(GuardedApi.AIRLABS)

        try {
            val url = "https://airlabs.co/api/v9/schedules?flight_iata=$iata&api_key=$key"
            val req = Request.Builder().url(url).header("Accept", "application/json").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "AirLabs HTTP ${resp.code} for $iata"); return@withContext null }

            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            val list = body.optJSONArray("response") ?: return@withContext null
            if (list.length() == 0) return@withContext null

            val entry = pickScheduleEntry(list, date) ?: list.getJSONObject(0)
            val depTime = entry.optString("dep_time", "")
            depTime.substringAfter(' ', "").takeIf { it.matches(Regex("\\d{2}:\\d{2}")) }
        } catch (e: Exception) {
            Log.w(TAG, "AirLabs lookup failed for $iata: ${e.message}")
            null
        }
    }
    data class FlightStatusResult(
        val flightIata: String,
        val status: String,
        val depIata: String,
        val arrIata: String,
        val depTime: String,
        val arrTime: String,
        val depTerminal: String,
        val depGate: String,
        val depDelayedMin: Int,
        val durationMin: Int,
    )

    suspend fun fetchFlightStatus(flightNumber: String, date: String? = null): FlightStatusResult? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        val iata = flightNumber.replace(" ", "").uppercase()
        if (iata.isBlank()) return@withContext null

        if (!UsageGuardian.isAllowed(GuardedApi.AIRLABS)) {
            Log.w(TAG, "AirLabs budget used up -- skipping call for $iata")
            return@withContext null
        }
        UsageGuardian.recordUse(GuardedApi.AIRLABS)

        try {
            val url = "https://airlabs.co/api/v9/schedules?flight_iata=$iata&api_key=$key"
            val req = Request.Builder().url(url).header("Accept", "application/json").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "AirLabs HTTP ${'$'}{resp.code} for $iata"); return@withContext null }

            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            val list = body.optJSONArray("response") ?: return@withContext null
            if (list.length() == 0) return@withContext null

            val flight = pickScheduleEntry(list, date) ?: return@withContext null
            FlightStatusResult(
                flightIata   = flight.optString("flight_iata", iata),
                status       = flight.optString("status", "unknown"),
                depIata      = flight.optString("dep_iata", ""),
                arrIata      = flight.optString("arr_iata", ""),
                depTime      = flight.optString("dep_time", "").substringAfter(' ', ""),
                arrTime      = flight.optString("arr_time", "").substringAfter(' ', ""),
                depTerminal  = flight.optString("dep_terminal", "").let { if (it == "null") "" else it },
                depGate      = flight.optString("dep_gate",     "").let { if (it == "null") "" else it },
                depDelayedMin = flight.optInt("dep_delayed", 0),
                durationMin  = flight.optInt("duration", 0),
            )
        } catch (e: Exception) {
            Log.w(TAG, "AirLabs fetchFlightStatus failed for $iata: ${'$'}{e.message}")
            null
        }
    }

}
