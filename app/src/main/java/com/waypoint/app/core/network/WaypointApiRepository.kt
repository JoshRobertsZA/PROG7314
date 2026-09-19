// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.db.TripEntity` for use in this file
import com.waypoint.app.core.db.TripEntity
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
    // closes the multi-line argument list started above
    )


    // declares private function `authBuilder` taking 1 parameter (`idToken`), returning `Request.Builder`; its body is the expression ``
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


    // declares suspend function `createTrip` taking 2 parameters (`idToken`, `trip`), returning `Boolean`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun createTrip(idToken: String, trip: TripEntity): Boolean = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `json`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
            val json = JSONObject().apply {
                // calls `put` with arguments `("id", trip.id)`
                put("id",        trip.id)
                // calls `put` with arguments `("name", trip.name)`
                put("name",      trip.name)
                // calls `put` with arguments `("startDate", trip.startDate)`
                put("startDate", trip.startDate)
                // calls `put` with arguments `("endDate", trip.endDate)`
                put("endDate",   trip.endDate)
                // `if` statement: executes `put("destination", trip.destination)` when `trip.destination != null` is true
                if (trip.destination  != null) put("destination",  trip.destination)
                // `if` statement: executes `put("destLat", trip.destLat)` when `trip.destLat != null` is true
                if (trip.destLat      != null) put("destLat",      trip.destLat)
                // `if` statement: executes `put("destLng", trip.destLng)` when `trip.destLng != null` is true
                if (trip.destLng      != null) put("destLng",      trip.destLng)
                // `if` statement: executes `put("destPhotoUrl", trip.destPhotoUrl)` when `trip.destPhotoUrl != null` is true
                if (trip.destPhotoUrl != null) put("destPhotoUrl", trip.destPhotoUrl)
            // statement: `}.toString().toRequestBody(JSON_TYPE)`
            }.toString().toRequestBody(JSON_TYPE)
            // declares read-only property `req`, initialised with the result of calling `authBuilder(…)`
            val req  = authBuilder(idToken).url("$BASE_URL/trips").post(json).build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // lambda `resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "POST /tr…`
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "POST /trips HTTP ${resp.code}") }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "createTrip failed: ${e.message}")`
            Log.w(TAG, "createTrip failed: ${e.message}")
            // expression: `false`
            false
        // closes the catch block
        }
    // closes the block
    }


    // declares suspend function `updateTrip` taking 2 parameters (`idToken`, `trip`), returning `Boolean`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun updateTrip(idToken: String, trip: TripEntity): Boolean = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `json`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
            val json = JSONObject().apply {
                // calls `put` with arguments `("name", trip.name)`
                put("name",      trip.name)
                // calls `put` with arguments `("startDate", trip.startDate)`
                put("startDate", trip.startDate)
                // calls `put` with arguments `("endDate", trip.endDate)`
                put("endDate",   trip.endDate)
                // calls `put` with arguments `("destination", trip.destination ?: JSONObjec…)`
                put("destination",  trip.destination  ?: JSONObject.NULL)
                // calls `put` with arguments `("destLat", trip.destLat ?: JSONObject.NULL)`
                put("destLat",      trip.destLat      ?: JSONObject.NULL)
                // calls `put` with arguments `("destLng", trip.destLng ?: JSONObject.NULL)`
                put("destLng",      trip.destLng      ?: JSONObject.NULL)
                // calls `put` with arguments `("destPhotoUrl", trip.destPhotoUrl ?: JSONObj…)`
                put("destPhotoUrl", trip.destPhotoUrl ?: JSONObject.NULL)
            // statement: `}.toString().toRequestBody(JSON_TYPE)`
            }.toString().toRequestBody(JSON_TYPE)
            // declares read-only property `req`, initialised with the result of calling `authBuilder(…)`
            val req  = authBuilder(idToken).url("$BASE_URL/trips/${trip.id}").put(json).build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // lambda `resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "PUT /tri…`
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "PUT /trips/${trip.id} HTTP ${resp.code}") }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "updateTrip failed: ${e.message}")`
            Log.w(TAG, "updateTrip failed: ${e.message}")
            // expression: `false`
            false
        // closes the catch block
        }
    // closes the block
    }


    // declares suspend function `deleteTrip` taking 2 parameters (`idToken`, `tripId`), returning `Boolean`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun deleteTrip(idToken: String, tripId: String): Boolean = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `req`, initialised with the result of calling `authBuilder(…)`
            val req  = authBuilder(idToken).url("$BASE_URL/trips/$tripId").delete().build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // lambda `resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "DELETE /…`
            resp.isSuccessful.also { ok -> if (!ok) Log.w(TAG, "DELETE /trips/$tripId HTTP ${resp.code}") }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "deleteTrip failed: ${e.message}")`
            Log.w(TAG, "deleteTrip failed: ${e.message}")
            // expression: `false`
            false
        // closes the catch block
        }
    // closes the block
    }


    // declares suspend function `incrementCounter` taking 2 parameters (`idToken`, `key`), returning `Int?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun incrementCounter(idToken: String, key: String): Int? = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `body`, initialised to the string literal "{}".toRequestBody(JSON_TYPE)
            val body = "{}".toRequestBody(JSON_TYPE)
            // declares read-only property `req`, initialised with the result of calling `authBuilder(…)`
            val req  = authBuilder(idToken).url("$BASE_URL/counter/$key/increment").post(body).build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // `if` statement: the block below runs when `!resp.isSuccessful` is true
            if (!resp.isSuccessful) {
                // calls `w` on `Log` with arguments `(TAG, "POST /counter/$key/increment HTTP ${re…)`
                Log.w(TAG, "POST /counter/$key/increment HTTP ${resp.code}")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }
            // calls `JSONObject` with arguments `(resp.body?.string() ?: return@withContext nu…)`, then chains `.optInt("count")`
            JSONObject(resp.body?.string() ?: return@withContext null).optInt("count")
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "incrementCounter failed: ${e.message}")`
            Log.w(TAG, "incrementCounter failed: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }
// closes the object `WaypointApiRepository`
}
