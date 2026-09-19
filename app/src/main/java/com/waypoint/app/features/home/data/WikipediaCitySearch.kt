// declares that this file belongs to the package `com.waypoint.app.features.home.data`
package com.waypoint.app.features.home.data

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONArray` for use in this file
import org.json.JSONArray
// imports `java.net.URLEncoder` for use in this file
import java.net.URLEncoder

// declares object `WikipediaCitySearch` and opens its body
object WikipediaCitySearch {

    // declares private const read-only property `TAG`, initialised to the string literal "WikipediaCitySearch"
    private const val TAG = "WikipediaCitySearch"

    // declares suspend function `thumbnailUrl` taking 1 parameter (`title`), returning `String?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun thumbnailUrl(title: String): String? = withContext(Dispatchers.IO) {
        // `if` statement: executes `return@withContext null` when `title.isBlank()` is true
        if (title.isBlank()) return@withContext null
        // declares read-only property `encoded`, initialised with the result of calling `URLEncoder.encode(…)`
        val encoded = URLEncoder.encode(title.trim().replace(' ', '_'), "UTF-8")
        // declares read-only property `url`, initialised to the string literal "https://en.wikipedia.org/api/rest_v1/p…
        val url = "https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `request`, initialised with the result of calling `Request.Builder(…)`
            val request = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("User-Agent", "WaypointApp/1.0 (Android…)`
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                // chained call `.get` on the previous result
                .get()
                // chained call `.build` on the previous result
                .build()
            // declares read-only property `body`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val body = HttpClient.instance.newCall(request).execute().use { it.body?.string() }
            // `if` statement: executes `return@withContext null` when `body.isNullOrBlank()` is true
            if (body.isNullOrBlank()) return@withContext null
            // calls `JSONObject` on `org.json` with arguments `(body)`, then chains `.optJSONObject("thumbnail")`, `.?.optString("source")`, `.?.takeIf { it.isNotBlank() }`
            org.json.JSONObject(body).optJSONObject("thumbnail")?.optString("source")?.takeIf { it.isNotBlank() }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Thumbnail lookup failed for '$title'",…)`
            Log.w(TAG, "Thumbnail lookup failed for '$title'", e)
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

    // declares suspend function `search` taking 1 parameter (`query`), returning `List<String>`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun search(query: String): List<String> = withContext(Dispatchers.IO) {
        // `if` statement: executes `return@withContext emptyList()` when `query.isBlank()` is true
        if (query.isBlank()) return@withContext emptyList()
        // declares read-only property `encoded`, initialised with the result of calling `URLEncoder.encode(…)`
        val encoded = URLEncoder.encode(query.trim(), "UTF-8")
        // declares read-only property `url`, initialised to the string literal "https://en.wikipedia.org/w/api.php" +
        val url = "https://en.wikipedia.org/w/api.php" +
            // continues the statement started above: `"?action=opensearch&search=$encoded&limit=8&namespace=0&for…`
            "?action=opensearch&search=$encoded&limit=8&namespace=0&format=json"
        // calls `d` on `Log` with arguments `(TAG, "Fetching: $url")`
        Log.d(TAG, "Fetching: $url")
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `request`, initialised with the result of calling `Request.Builder(…)`
            val request = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("User-Agent", "WaypointApp/1.0 (Android…)`
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                // chained call `.get` on the previous result
                .get()
                // chained call `.build` on the previous result
                .build()
            // declares read-only property `response`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val response = HttpClient.instance.newCall(request).execute()
            // declares read-only property `body`, initialised to `response.use { it.body?.string() }`
            val body = response.use { it.body?.string() }
            // calls `d` on `Log` with arguments `(TAG, "Response code: ${response.code}, body …)`
            Log.d(TAG, "Response code: ${response.code}, body length: ${body?.length}")
            // `if` statement: executes `return@withContext emptyList()` when `body.isNullOrBlank()` is true
            if (body.isNullOrBlank()) return@withContext emptyList()
            // declares read-only property `root`, initialised with the result of calling `JSONArray(…)`
            val root = JSONArray(body)
            // declares read-only property `titles`, initialised with the result of calling `root.getJSONArray(…)`
            val titles = root.getJSONArray(1)
            // declares read-only property `results`, initialised with the result of calling `List(…)`
            val results = List(titles.length()) { titles.getString(it) }
            // calls `d` on `Log` with arguments `(TAG, "Results: $results")`
            Log.d(TAG, "Results: $results")
            // expression: `results`
            results
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `e` on `Log` with arguments `(TAG, "Search failed for query='$query'", e)`
            Log.e(TAG, "Search failed for query='$query'", e)
            // calls `emptyList` with arguments `()`
            emptyList()
        // closes the catch block
        }
    // closes the block
    }
// closes the object `WikipediaCitySearch`
}
