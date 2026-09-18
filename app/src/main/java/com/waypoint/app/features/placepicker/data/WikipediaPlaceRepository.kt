package com.waypoint.app.features.placepicker.data

import android.util.Log
import com.waypoint.app.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Fetches a Wikipedia REST Summary for a named place.
 *
 * Endpoint: GET https://en.wikipedia.org/api/rest_v1/page/summary/{title}
 * No API key required. Returns [WikipediaSummary] or null on failure.
 */
object WikipediaPlaceRepository {

    private const val TAG = "WikipediaPlaceRepo"

    data class WikipediaSummary(
        val extract      : String,
        val thumbnailUrl : String?,
    )

    suspend fun getSummary(placeName: String): WikipediaSummary? = withContext(Dispatchers.IO) {
        if (placeName.isBlank()) return@withContext null
        try {
            val encoded = URLEncoder.encode(placeName.trim(), "UTF-8")
                .replace("+", "_")   // Wikipedia REST API uses underscores in the title path
            val url = "https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"
            Log.d(TAG, "Fetching: $url")
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                .get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    Log.w(TAG, "Wikipedia REST HTTP ${resp.code} for '$placeName'")
                    return@withContext null
                }
                val body = resp.body?.string() ?: return@withContext null
                val obj  = JSONObject(body)
                val extract = obj.optString("extract").ifBlank { null }
                    ?: return@withContext null
                val thumb = obj.optJSONObject("thumbnail")?.optString("source")?.ifBlank { null }
                WikipediaSummary(extract = extract, thumbnailUrl = thumb)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Summary fetch failed for '$placeName': ${e.message}")
            null
        }
    }
}
