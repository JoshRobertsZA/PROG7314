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
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `AirLabsRepository` and opens its body
object AirLabsRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "AirLabsRepo"
    private const val TAG = "AirLabsRepo"

    // declares suspend function `lookupDepartureTime` taking 1 parameter (`flightNumber`), returning `String?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun lookupDepartureTime(flightNumber: String): String? = withContext(Dispatchers.IO) {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        // `if` statement: executes `{ Log.w(TAG, "No AirLabs key"); return@withC…` when `key.isBlank()` is true
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        // declares read-only property `iata`, initialised with the result of calling `flightNumber.replace(…)`
        val iata = flightNumber.replace(" ", "").uppercase()
        // `if` statement: executes `return@withContext null` when `iata.isBlank()` is true
        if (iata.isBlank()) return@withContext null

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

            // declares read-only property `depTime`, initialised with the result of calling `list.getJSONObject(…)`
            val depTime = list.getJSONObject(0).optString("dep_time", "")
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
// closes the object `AirLabsRepository`
}
