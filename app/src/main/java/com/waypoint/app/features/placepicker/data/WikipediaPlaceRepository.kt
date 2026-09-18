package com.waypoint.app.features.placepicker.data

import android.util.Log
import com.waypoint.app.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder

/**
 * Fetches a Wikipedia REST Summary for a named place.
 *
 * Strategy:
 *  1. OpenSearch API to find the best-matching Wikipedia article title.
 *  2. REST summary API on that resolved title to get extract + thumbnail.
 *
 * No API key required.
 */
object WikipediaPlaceRepository {

    private const val TAG = "WikipediaPlaceRepo"

    data class WikipediaSummary(
        val extract      : String,
        val thumbnailUrl : String?,
    )

    suspend fun getSummary(placeName: String): WikipediaSummary? = withContext(Dispatchers.IO) {
        if (placeName.isBlank()) return@withContext null

        // Step 1 — resolve the best Wikipedia title via OpenSearch
        val resolvedTitle = resolveTitle(placeName) ?: run {
            Log.w(TAG, "No Wikipedia article found for '$placeName'")
            return@withContext null
        }
        Log.d(TAG, "Resolved '$placeName' -> '$resolvedTitle'")

        // Step 2 — fetch the REST summary for the resolved title
        fetchSummary(resolvedTitle)
    }

    // -------------------------------------------------------------------------

    private fun resolveTitle(query: String): String? {
        return try {
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            val url = "https://en.wikipedia.org/w/api.php" +
                "?action=opensearch&search=$encoded&limit=1&namespace=0&format=json"
            Log.d(TAG, "OpenSearch: $url")
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                .get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) return null
                val body = resp.body?.string() ?: return null
                // OpenSearch returns [query, [titles], [descriptions], [urls]]
                val root   = JSONArray(body)
                val titles = root.optJSONArray(1) ?: return null
                if (titles.length() == 0) return null
                titles.getString(0).ifBlank { null }
            }
        } catch (e: Exception) {
            Log.w(TAG, "OpenSearch failed for '$query': ${e.message}")
            null
        }
    }

    private fun fetchSummary(title: String): WikipediaSummary? {
        return try {
            val encoded = URLEncoder.encode(title.trim(), "UTF-8")
                .replace("+", "_")
            val url = "https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"
            Log.d(TAG, "Summary fetch: $url")
            val req = Request.Builder()
                .url(url)
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                .get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    Log.w(TAG, "Summary HTTP ${resp.code} for '$title'")
                    return null
                }
                val body = resp.body?.string() ?: return null
                val obj  = JSONObject(body)
                val extract = obj.optString("extract").ifBlank { null } ?: return null
                val thumb   = obj.optJSONObject("thumbnail")
                    ?.optString("source")?.ifBlank { null }
                Log.d(TAG, "Thumbnail for '$title': $thumb")
                WikipediaSummary(extract = extract, thumbnailUrl = thumb)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Summary fetch failed for '$title': ${e.message}")
            null
        }
    }
}
