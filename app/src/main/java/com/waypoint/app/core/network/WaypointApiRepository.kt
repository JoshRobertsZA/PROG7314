// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.MediaType.Companion.toMediaType` for use in this file
import okhttp3.MediaType.Companion.toMediaType
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `okhttp3.RequestBody.Companion.toRequestBody` for use in this file
import okhttp3.RequestBody.Companion.toRequestBody
// imports `org.json.JSONArray` for use in this file
import org.json.JSONArray
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `WaypointApiRepository` and opens its body
object WaypointApiRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "WaypointApiRepo"
    private const val TAG = "WaypointApiRepo"
    // statement: `private val BASE_URL get() = BuildConfig.WAYPOINT_API_BASE_URL.t…`
    private val BASE_URL get() = BuildConfig.WAYPOINT_API_BASE_URL.trimEnd('/')
    // declares private read-only property `JSON_TYPE`, initialised to the string literal "application/json; charset=utf-8".toMed…
    private val JSON_TYPE = "application/json; charset=utf-8".toMediaType()


    // expression: `data class TripPayload(`
    data class TripPayload(
        // continues the statement started above: `val id: String,`
        val id: String,
        // continues the statement started above: `val name: String,`
        val name: String,
        // continues the statement started above: `val startDate: String,`
        val startDate: String,
        // continues the statement started above: `val endDate: String,`
        val endDate: String,
        // continues the statement started above: `val destination: String?,`
        val destination: String?,
        // continues the statement started above: `val destLat: Double?,`
        val destLat: Double?,
        // continues the statement started above: `val destLng: Double?,`
        val destLng: Double?,
        // continues the statement started above: `val destPhotoUrl: String?,`
        val destPhotoUrl: String?,
        // continues the statement started above: `val accountId: String,`
        val accountId: String,
        val createdAtMs: Long,
        val updatedAtMs: Long,
    // closes the multi-line argument list started above
    )

    data class DayPayload(val id: String, val tripId: String, val date: String)

    data class FlightPayload(
        val id: String,
        val dayId: String,
        val flightNumber: String?,
        val pdfUri: String,
        val departureTime: String?,
        val docName: String?,
        val createdAtMs: Long,
    )

    data class LodgingPayload(
        val id: String,
        val tripId: String,
        val fromDate: String?,
        val toDate: String?,
        val pdfUri: String,
        val docName: String?,
        val createdAtMs: Long,
    )

    data class CarPayload(
        val id: String,
        val tripId: String,
        val fromDate: String?,
        val toDate: String?,
        val pdfUri: String,
        val docName: String?,
        val createdAtMs: Long,
    )

    data class PlacePayload(
        val id: String,
        val dayId: String,
        val name: String,
        val category: String?,
        val lat: Double?,
        val lng: Double?,
        val note: String?,
        val photoUrl: String?,
        val createdAtMs: Long,
    )


    /**
     * Calls the public `/health` route. Cloud Run scales to zero when idle, so hitting
     * this at app start wakes the server while the splash screen is still showing.
     */
    suspend fun warmUp(): Boolean = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder().url("$BASE_URL/health").get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                Log.i(TAG, "GET /health -> HTTP ${resp.code}")
                resp.isSuccessful
            }
        } catch (e: Exception) {
            Log.w(TAG, "GET /health failed: ${e.message}")
            false
        }
    }

    private fun authBuilder(idToken: String): Request.Builder =
        // continues the statement started above: `Request.Builder().header("Authorization", "Bearer $idToken")`
        Request.Builder().header("Authorization", "Bearer $idToken")


    // declares suspend function `getTrips` taking 1 parameter (`idToken`), returning `List<TripPayload>?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun getTrips(idToken: String): List<TripPayload>? = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `req`, initialised with the result of calling `authBuilder(…)`
            val req  = authBuilder(idToken).url("$BASE_URL/trips").get().build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // `if` statement: the block below runs when `!resp.isSuccessful` is true
            if (!resp.isSuccessful) {
                // calls `w` on `Log` with arguments `(TAG, "GET /trips HTTP ${resp.code}")`
                Log.w(TAG, "GET /trips HTTP ${resp.code}")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }
            // declares read-only property `arr`, initialised with the result of calling `JSONArray(…)`
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            // expression: `(0 until arr.length()).map { i ->`
            (0 until arr.length()).map { i ->
                // continues the statement started above: `val o = arr.getJSONObject(i)`
                val o = arr.getJSONObject(i)
                // calls `TripPayload` with an argument list that continues on the following lines
                TripPayload(
                    // continues the statement started above: `id = o.getString("id"),`
                    id           = o.getString("id"),
                    // continues the statement started above: `name = o.getString("name"),`
                    name         = o.getString("name"),
                    // continues the statement started above: `startDate = o.getString("startDate"),`
                    startDate    = o.getString("startDate"),
                    // continues the statement started above: `endDate = o.getString("endDate"),`
                    endDate      = o.getString("endDate"),
                    // continues the statement started above: `destination = o.optString("destination").takeIf { it.isNotB…`
                    destination  = o.optString("destination").takeIf { it.isNotBlank() && it != "null" },
                    // continues the statement started above: `destLat = if (o.has("destLat") && !o.isNull("destLat")) o.g…`
                    destLat      = if (o.has("destLat")      && !o.isNull("destLat"))      o.getDouble("destLat")      else null,
                    // continues the statement started above: `destLng = if (o.has("destLng") && !o.isNull("destLng")) o.g…`
                    destLng      = if (o.has("destLng")      && !o.isNull("destLng"))      o.getDouble("destLng")      else null,
                    // continues the statement started above: `destPhotoUrl = o.optString("destPhotoUrl").takeIf { it.isNo…`
                    destPhotoUrl = o.optString("destPhotoUrl").takeIf { it.isNotBlank() && it != "null" },
                    // continues the statement started above: `accountId = o.getString("accountId"),`
                    accountId    = o.getString("accountId"),
                    createdAtMs  = o.optLong("createdAtMs"),
                    updatedAtMs  = o.optLong("updatedAtMs"),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "getTrips failed: ${e.message}")`
            Log.w(TAG, "getTrips failed: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }


    // --- Itinerary: pull (added here in PR 2b; PR 2a was write-only) ---

    suspend fun getDaysForTrip(idToken: String, tripId: String): List<DayPayload>? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/itinerary/days/$tripId").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "GET /itinerary/days/$tripId HTTP ${resp.code}"); return@withContext null }
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                DayPayload(id = o.getString("id"), tripId = o.getString("tripId"), date = o.getString("date"))
            }
        } catch (e: Exception) {
            Log.w(TAG, "getDaysForTrip failed: ${e.message}")
            null
        }
    }

    suspend fun getFlightsForDay(idToken: String, dayId: String): List<FlightPayload>? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/itinerary/flights/$dayId").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "GET /itinerary/flights/$dayId HTTP ${resp.code}"); return@withContext null }
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                FlightPayload(
                    id = o.getString("id"),
                    dayId = o.getString("dayId"),
                    flightNumber = o.optString("flightNumber").takeIf { it.isNotBlank() && it != "null" },
                    pdfUri = o.optString("pdfUri"),
                    departureTime = o.optString("departureTime").takeIf { it.isNotBlank() && it != "null" },
                    docName = o.optString("docName").takeIf { it.isNotBlank() && it != "null" },
                    createdAtMs = o.optLong("createdAtMs"),
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getFlightsForDay failed: ${e.message}")
            null
        }
    }

    suspend fun getLodgingForTrip(idToken: String, tripId: String): List<LodgingPayload>? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/itinerary/lodging/$tripId").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "GET /itinerary/lodging/$tripId HTTP ${resp.code}"); return@withContext null }
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                LodgingPayload(
                    id = o.getString("id"),
                    tripId = o.getString("tripId"),
                    fromDate = o.optString("fromDate").takeIf { it.isNotBlank() && it != "null" },
                    toDate = o.optString("toDate").takeIf { it.isNotBlank() && it != "null" },
                    pdfUri = o.optString("pdfUri"),
                    docName = o.optString("docName").takeIf { it.isNotBlank() && it != "null" },
                    createdAtMs = o.optLong("createdAtMs"),
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getLodgingForTrip failed: ${e.message}")
            null
        }
    }

    suspend fun getCarsForTrip(idToken: String, tripId: String): List<CarPayload>? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/itinerary/cars/$tripId").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "GET /itinerary/cars/$tripId HTTP ${resp.code}"); return@withContext null }
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                CarPayload(
                    id = o.getString("id"),
                    tripId = o.getString("tripId"),
                    fromDate = o.optString("fromDate").takeIf { it.isNotBlank() && it != "null" },
                    toDate = o.optString("toDate").takeIf { it.isNotBlank() && it != "null" },
                    pdfUri = o.optString("pdfUri"),
                    docName = o.optString("docName").takeIf { it.isNotBlank() && it != "null" },
                    createdAtMs = o.optLong("createdAtMs"),
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getCarsForTrip failed: ${e.message}")
            null
        }
    }

    suspend fun getPlacesForDay(idToken: String, dayId: String): List<PlacePayload>? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/itinerary/places/$dayId").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "GET /itinerary/places/$dayId HTTP ${resp.code}"); return@withContext null }
            val arr = JSONArray(resp.body?.string() ?: return@withContext null)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                PlacePayload(
                    id = o.getString("id"),
                    dayId = o.getString("dayId"),
                    name = o.getString("name"),
                    category = o.optString("category").takeIf { it.isNotBlank() && it != "null" },
                    lat = if (o.has("lat") && !o.isNull("lat")) o.getDouble("lat") else null,
                    lng = if (o.has("lng") && !o.isNull("lng")) o.getDouble("lng") else null,
                    note = o.optString("note").takeIf { it.isNotBlank() && it != "null" },
                    photoUrl = o.optString("photoUrl").takeIf { it.isNotBlank() && it != "null" },
                    createdAtMs = o.optLong("createdAtMs"),
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getPlacesForDay failed: ${e.message}")
            null
        }
    }


    // --- Itinerary: days (create/update/delete) ---

    // --- Itinerary: flights ---

    // --- Itinerary: lodging ---

    // --- Itinerary: cars ---

    // --- Itinerary: places ---

    // --- Push notifications (PR 4) ---

    suspend fun registerDevice(idToken: String, token: String, locale: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("token", token)
                put("locale", locale)
            }.toString().toRequestBody(JSON_TYPE)
            val req = authBuilder(idToken).url("$BASE_URL/devices").post(json).build()
            val resp = HttpClient.instance.newCall(req).execute()
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "POST /devices HTTP ${resp.code}") }
        } catch (e: Exception) {
            Log.w(TAG, "registerDevice failed: ${e.message}")
            false
        }
    }

    suspend fun unregisterDevice(idToken: String, token: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/devices/$token").delete().build()
            val resp = HttpClient.instance.newCall(req).execute()
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "DELETE /devices/$token HTTP ${resp.code}") }
        } catch (e: Exception) {
            Log.w(TAG, "unregisterDevice failed: ${e.message}")
            false
        }
    }

    suspend fun deleteAccount(idToken: String): Boolean = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/account").delete().build()
            val resp = HttpClient.instance.newCall(req).execute()
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "DELETE /account HTTP ${resp.code}") }
        } catch (e: Exception) {
            Log.w(TAG, "deleteAccount failed: ${e.message}")
            false
        }
    }

    /** The user's synced settings as stored by the API. `updatedAtMs == 0` means none saved yet. */
    data class SettingsPayload(
        val darkMode: Boolean?,
        val notifyTripUpdates: Boolean,
        val notifyReminders: Boolean,
        val defaultCurrency: String,
        val language: String?,
        val updatedAtMs: Long,
    )

    /** GET /account/settings. Returns null when the request failed. */
    suspend fun getSettings(idToken: String): SettingsPayload? = withContext(Dispatchers.IO) {
        try {
            val req = authBuilder(idToken).url("$BASE_URL/account/settings").get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    Log.w(TAG, "GET /account/settings HTTP ${resp.code}")
                    return@withContext null
                }
                val o = JSONObject(resp.body?.string().orEmpty())
                SettingsPayload(
                    darkMode = if (o.isNull("darkMode")) null else o.optBoolean("darkMode"),
                    notifyTripUpdates = o.optBoolean("notifyTripUpdates", true),
                    notifyReminders = o.optBoolean("notifyReminders", true),
                    defaultCurrency = o.optString("defaultCurrency", "ZAR"),
                    language = if (o.isNull("language")) null else o.optString("language"),
                    updatedAtMs = o.optLong("updatedAtMs", 0L),
                )
            }
        } catch (e: Exception) {
            Log.w(TAG, "getSettings failed: ${e.message}")
            null
        }
    }

    /** PUT /account/settings. The server keeps whichever copy has the newer updatedAtMs. */
    suspend fun putSettings(idToken: String, settings: SettingsPayload): Boolean = withContext(Dispatchers.IO) {
        try {
            val json = JSONObject().apply {
                put("darkMode", settings.darkMode ?: JSONObject.NULL)
                put("notifyTripUpdates", settings.notifyTripUpdates)
                put("notifyReminders", settings.notifyReminders)
                put("defaultCurrency", settings.defaultCurrency)
                put("language", settings.language ?: JSONObject.NULL)
                put("updatedAtMs", settings.updatedAtMs)
            }.toString().toRequestBody(JSON_TYPE)
            val req = authBuilder(idToken).url("$BASE_URL/account/settings").put(json).build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                Log.i(TAG, "PUT /account/settings -> HTTP ${resp.code}")
                resp.isSuccessful
            }
        } catch (e: Exception) {
            Log.w(TAG, "putSettings failed: ${e.message}")
            false
        }
    }



    // HTTP status of a sendOutboxPayload() call, or -1 for a network-level
    // failure (no response at all - timeout, DNS, connection refused...).
    // SyncWorker classifies this to decide retry vs give up: 2xx succeeds,
    // 4xx is a permanent failure (retrying a bad request forever
    // accomplishes nothing), 5xx/-1 is retryable.
    data class SendResult(val httpStatus: Int, val successful: Boolean)

    // Every create, update and delete goes through here. The sync worker drains
    // OutboxRepository, whose entries already carry a payload shaped to
    // match each endpoint exactly (built in TripRepository /
    // ItineraryRepository at enqueue time - see the coalescing rules in
    // OutboxRepository), including partial UPDATE payloads such as just
    // { "flightNumber": ... } or { "note": ... }.
    // deviceToken, when present, is sent as X-Device-Token so the server can
    // skip notifying the device that made the change (see POST /trips) -
    // optional because most outbox entries aren't trip creation and the
    // server ignores the header everywhere else.
    suspend fun sendOutboxPayload(idToken: String, method: String, path: String, bodyJson: String?, deviceToken: String? = null): SendResult =
        withContext(Dispatchers.IO) {
            try {
                val builder = authBuilder(idToken).url("$BASE_URL$path")
                if (deviceToken != null) builder.header("X-Device-Token", deviceToken)
                when (method) {
                    "POST" -> builder.post((bodyJson ?: "{}").toRequestBody(JSON_TYPE))
                    "PUT" -> builder.put((bodyJson ?: "{}").toRequestBody(JSON_TYPE))
                    "DELETE" -> builder.delete()
                    else -> error("Unsupported method for sendOutboxPayload: $method")
                }
                val resp = HttpClient.instance.newCall(builder.build()).execute()
                if (!resp.isSuccessful) Log.w(TAG, "$method $path HTTP ${resp.code}")
                SendResult(resp.code, resp.isSuccessful)
            } catch (e: Exception) {
                Log.w(TAG, "$method $path failed: ${e.message}")
                SendResult(-1, false)
            }
        }
// closes the object `WaypointApiRepository`
}
