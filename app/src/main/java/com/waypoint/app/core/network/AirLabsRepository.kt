package com.waypoint.app.core.network

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

/**
 * Minimal AirLabs client: resolves a flight number to its scheduled local
 * departure time so TripReminderWorker can decide whether to remind the
 * evening before (early flight) or the morning of. The result is written
 * back to the flight row by the caller, so each flight is looked up once.
 */
object AirLabsRepository {

    private const val TAG = "AirLabsRepo"

    /**
     * Returns "HH:mm" local departure time for [flightNumber] (e.g. "SA 123",
     * spaces ignored), or null if the key is missing, the flight is unknown,
     * or the network call fails.
     */
    suspend fun lookupDepartureTime(flightNumber: String): String? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("AIRLABS_API_KEY", BuildConfig.AIRLABS_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No AirLabs key"); return@withContext null }

        val iata = flightNumber.replace(" ", "").uppercase()
        if (iata.isBlank()) return@withContext null

        try {
            val url = "https://airlabs.co/api/v9/schedules?flight_iata=$iata&api_key=$key"
            val req = Request.Builder().url(url).header("Accept", "application/json").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) { Log.w(TAG, "AirLabs HTTP ${resp.code} for $iata"); return@withContext null }

            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            val list = body.optJSONArray("response") ?: return@withContext null
            if (list.length() == 0) return@withContext null

            // dep_time is "yyyy-MM-dd HH:mm" in the departure airport's local time.
            val depTime = list.getJSONObject(0).optString("dep_time", "")
            depTime.substringAfter(' ', "").takeIf { it.matches(Regex("\\d{2}:\\d{2}")) }
        } catch (e: Exception) {
            Log.w(TAG, "AirLabs lookup failed for $iata: ${e.message}")
            null
        }
    }
}
