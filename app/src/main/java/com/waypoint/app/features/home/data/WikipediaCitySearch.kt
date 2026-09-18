package com.waypoint.app.features.home.data

import android.util.Log
import com.waypoint.app.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import java.net.URLEncoder

/**
 * Queries the Wikipedia OpenSearch API to get city/place name suggestions.
 * Returns up to 8 matching article titles, or emptyList() on any failure.
 */
object WikipediaCitySearch {

    private const val TAG = "WikipediaCitySearch"

    suspend fun search(query: String): List<String> = withContext(Dispatchers.IO) {
        if (query.isBlank()) return@withContext emptyList()
        val encoded = URLEncoder.encode(query.trim(), "UTF-8")
        val url = "https://en.wikipedia.org/w/api.php" +
            "?action=opensearch&search=$encoded&limit=8&namespace=0&format=json"
        Log.d(TAG, "Fetching: $url")
        try {
            val request = Request.Builder()
                .url(url)
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                .get()
                .build()
            val response = HttpClient.instance.newCall(request).execute()
            val body = response.use { it.body?.string() }
            Log.d(TAG, "Response code: ${response.code}, body length: ${body?.length}")
            if (body.isNullOrBlank()) return@withContext emptyList()
            // OpenSearch response: [query, [titles], [descriptions], [urls]]
            val root = JSONArray(body)
            val titles = root.getJSONArray(1)
            val results = List(titles.length()) { titles.getString(it) }
            Log.d(TAG, "Results: $results")
            results
        } catch (e: Exception) {
            Log.e(TAG, "Search failed for query='$query'", e)
            emptyList()
        }
    }
}
