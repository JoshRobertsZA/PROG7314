// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONArray` for use in this file
import org.json.JSONArray
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `AirLabsRepository` and opens its body
object AirLabsRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "AirLabsRepo"
    private const val TAG = "AirLabsRepo"
    // declares const read-only property `AIRLABS_MISS`, initialised to the string literal "AIRLABS_MISS"
    const val AIRLABS_MISS = "AIRLABS_MISS"

    // declares function `cacheKey` taking 2 parameters (`flightNumber`, `date`), returning `String`; the same flight on different days gets different keys, so a cached status is never shown for the wrong day
    fun cacheKey(flightNumber: String, date: String?): String {
        // declares read-only property `iata`, initialised with the result of calling `flightNumber.replace(…)`
        val iata = flightNumber.replace(" ", "").uppercase()
        // returns the bare flight number when there is no date, otherwise the flight number and the date joined by a pipe
        return if (date.isNullOrBlank()) iata else "$iata|${date.trim()}"
    // closes the function `cacheKey`
    }

    // declares function `pickScheduleEntry` taking 2 parameters (`list`, `date`), returning `JSONObject?`; with no date it returns the first entry, with a `yyyy-MM-dd` date it returns the entry whose `dep_time` starts with that date, or null when none match
    fun pickScheduleEntry(list: JSONArray, date: String?): JSONObject? {
        // `if` statement: executes `return null` when `list.length() == 0` is true
        if (list.length() == 0) return null
        // `if` statement: executes `return list.getJSONObject(0)` when `date.isNullOrBlank()` is true, keeping the old behaviour for callers without a date
        if (date.isNullOrBlank()) return list.getJSONObject(0)
        // `for` loop: `i` takes each value in `0 until list.length()` in turn
        for (i in 0 until list.length()) {
            // declares read-only property `entry`, initialised with the result of calling `list.getJSONObject(…)`
            val entry = list.getJSONObject(i)
            // `if` statement: executes `return entry` when `entry.optString("dep_time", "").startsWith(date.trim())` is true
            if (entry.optString("dep_time", "").startsWith(date.trim())) return entry
        // closes the for loop
        }
        // returns `null` from the current function because no entry matched the date
        return null
    // closes the function `pickScheduleEntry`
    }

    // declares suspend function `lookupDepartureTime` taking 2 parameters (`flightNumber`, `date`), returning `String?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun lookupDepartureTime(flightNumber: String, date: String? = null): String? = withContext(Dispatchers.IO) {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        // `if` statement: executes `{ Log.w(TAG, "No AirLabs key"); return@withC…` when `key.isBlank()` is true
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        // declares read-only property `iata`, initialised with the result of calling `flightNumber.replace(…)`
        val iata = flightNumber.replace(" ", "").uppercase()
        // `if` statement: executes `return@withContext null` when `iata.isBlank()` is true
        if (iata.isBlank()) return@withContext null

        // `if` statement: the block below runs when the guardian says the AirLabs monthly budget is used up
        if (!UsageGuardian.isAllowed(GuardedApi.AIRLABS)) {
            // calls `w` on `Log` with arguments `(TAG, "AirLabs budget used up -- skipping call for $iata")`
            Log.w(TAG, "AirLabs budget used up -- skipping call for $iata")
            // expression: `return@withContext null`
            return@withContext null
        // closes the if block
        }
        // calls `recordUse` on `UsageGuardian` with arguments `(GuardedApi.AIRLABS)`, counting this request before it is sent
        UsageGuardian.recordUse(GuardedApi.AIRLABS)

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `url`, initialised to the string literal "https://airlabs.co/api/v9/schedules?fl…
            val url = "https://airlabs.co/api/v9/schedules?flight_iata=$iata&api_key=$key"
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder().url(url).header("Accept", "application/json").get().build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // `if` statement: executes `{ Log.w(TAG, "AirLabs HTTP ${resp.code} for …` when `!resp.isSuccessful` is true
            if (!resp.isSuccessful) { Log.w(TAG, "AirLabs HTTP ${resp.code} for $iata"); return@withContext null }

            // declares read-only property `body`, initialised with the result of calling `JSONObject(…)`
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            // declares read-only property `list`, initialised with the result of calling `body.optJSONArray(…)`
            val list = body.optJSONArray("response") ?: return@withContext null
            // `if` statement: executes `return@withContext null` when `list.length() == 0` is true
            if (list.length() == 0) return@withContext null

            // declares read-only property `entry`, the entry for the requested date, falling back to the first entry because a flight's time of day repeats across days
            val entry = pickScheduleEntry(list, date) ?: list.getJSONObject(0)
            // declares read-only property `depTime`, initialised with the result of calling `entry.optString(…)`
            val depTime = entry.optString("dep_time", "")
            // calls `substringAfter` on `depTime` with arguments `(' ', "")`, then chains `.takeIf { it.matches(Regex("\\…`
            depTime.substringAfter(' ', "").takeIf { it.matches(Regex("\\d{2}:\\d{2}")) }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "AirLabs lookup failed for $iata: ${e.m…)`
            Log.w(TAG, "AirLabs lookup failed for $iata: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }
    // expression: `data class FlightStatusResult(`
    data class FlightStatusResult(
        // continues the statement started above: `val flightIata: String,`
        val flightIata: String,
        // continues the statement started above: `val status: String,`
        val status: String,
        // continues the statement started above: `val depIata: String,`
        val depIata: String,
        // continues the statement started above: `val arrIata: String,`
        val arrIata: String,
        // continues the statement started above: `val depTime: String,`
        val depTime: String,
        // continues the statement started above: `val arrTime: String,`
        val arrTime: String,
        // continues the statement started above: `val depTerminal: String,`
        val depTerminal: String,
        // continues the statement started above: `val depGate: String,`
        val depGate: String,
        // continues the statement started above: `val depDelayedMin: Int,`
        val depDelayedMin: Int,
        // continues the statement started above: `val durationMin: Int,`
        val durationMin: Int,
    // closes the multi-line argument list started above
    )

    // declares suspend function `fetchFlightStatus` taking 2 parameters (`flightNumber`, `date`), returning `FlightStatusResult?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun fetchFlightStatus(flightNumber: String, date: String? = null): FlightStatusResult? = withContext(Dispatchers.IO) {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        // `if` statement: executes `{ Log.w(TAG, "No AirLabs key"); return@withC…` when `key.isBlank()` is true
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        // declares read-only property `iata`, initialised with the result of calling `flightNumber.replace(…)`
        val iata = flightNumber.replace(" ", "").uppercase()
        // `if` statement: executes `return@withContext null` when `iata.isBlank()` is true
        if (iata.isBlank()) return@withContext null

        // `if` statement: the block below runs when the guardian says the AirLabs monthly budget is used up
        if (!UsageGuardian.isAllowed(GuardedApi.AIRLABS)) {
            // calls `w` on `Log` with arguments `(TAG, "AirLabs budget used up -- skipping call for $iata")`
            Log.w(TAG, "AirLabs budget used up -- skipping call for $iata")
            // expression: `return@withContext null`
            return@withContext null
        // closes the if block
        }
        // calls `recordUse` on `UsageGuardian` with arguments `(GuardedApi.AIRLABS)`, counting this request before it is sent
        UsageGuardian.recordUse(GuardedApi.AIRLABS)

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `url`, initialised to the string literal "https://airlabs.co/api/v9/schedules?fl…
            val url = "https://airlabs.co/api/v9/schedules?flight_iata=$iata&api_key=$key"
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder().url(url).header("Accept", "application/json").get().build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // `if` statement: executes `{ Log.w(TAG, "AirLabs HTTP ${'$'}{resp.code}…` when `!resp.isSuccessful` is true
            if (!resp.isSuccessful) { Log.w(TAG, "AirLabs HTTP ${'$'}{resp.code} for $iata"); return@withContext null }

            // declares read-only property `body`, initialised with the result of calling `JSONObject(…)`
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            // declares read-only property `list`, initialised with the result of calling `body.optJSONArray(…)`
            val list = body.optJSONArray("response") ?: return@withContext null
            // `if` statement: executes `return@withContext null` when `list.length() == 0` is true
            if (list.length() == 0) return@withContext null

            // declares read-only property `flight`, the entry for the requested date, returning null from the function when a date was given and no entry matches
            val flight = pickScheduleEntry(list, date) ?: return@withContext null
            // calls `FlightStatusResult` with an argument list that continues on the following lines
            FlightStatusResult(
                // continues the statement started above: `flightIata = flight.optString("flight_iata", iata),`
                flightIata   = flight.optString("flight_iata", iata),
                // continues the statement started above: `status = flight.optString("status", "unknown"),`
                status       = flight.optString("status", "unknown"),
                // continues the statement started above: `depIata = flight.optString("dep_iata", ""),`
                depIata      = flight.optString("dep_iata", ""),
                // continues the statement started above: `arrIata = flight.optString("arr_iata", ""),`
                arrIata      = flight.optString("arr_iata", ""),
                // continues the statement started above: `depTime = flight.optString("dep_time", "").substringAfter('…`
                depTime      = flight.optString("dep_time", "").substringAfter(' ', ""),
                // continues the statement started above: `arrTime = flight.optString("arr_time", "").substringAfter('…`
                arrTime      = flight.optString("arr_time", "").substringAfter(' ', ""),
                // continues the statement started above: `depTerminal = flight.optString("dep_terminal", "").let { if…`
                depTerminal  = flight.optString("dep_terminal", "").let { if (it == "null") "" else it },
                // continues the statement started above: `depGate = flight.optString("dep_gate", "").let { if (it == …`
                depGate      = flight.optString("dep_gate",     "").let { if (it == "null") "" else it },
                // continues the statement started above: `depDelayedMin = flight.optInt("dep_delayed", 0),`
                depDelayedMin = flight.optInt("dep_delayed", 0),
                // continues the statement started above: `durationMin = flight.optInt("duration", 0),`
                durationMin  = flight.optInt("duration", 0),
            // closes the multi-line argument list started above
            )
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "AirLabs fetchFlightStatus failed for $…)`
            Log.w(TAG, "AirLabs fetchFlightStatus failed for $iata: ${'$'}{e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

// closes the object `AirLabsRepository`
}
