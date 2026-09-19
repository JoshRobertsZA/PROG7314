// declares that this file belongs to the package `com.waypoint.app.features.placepicker.data`
package com.waypoint.app.features.placepicker.data

// imports `android.graphics.Bitmap` for use in this file
import android.graphics.Bitmap
// imports `android.graphics.BitmapFactory` for use in this file
import android.graphics.BitmapFactory
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
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject
// imports `java.net.URLEncoder` for use in this file
import java.net.URLEncoder

// declares object `WikipediaPlaceRepository` and opens its body
object WikipediaPlaceRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "WikipediaPlaceRepo"
    private const val TAG = "WikipediaPlaceRepo"

    // expression: `data class WikipediaSummary(`
    data class WikipediaSummary(
        // continues the statement started above: `val extract : String,`
        val extract      : String,
        // continues the statement started above: `val thumbnailUrl : String?,`
        val thumbnailUrl : String?,
    // closes the multi-line argument list started above
    )

    // declares suspend function `getSummary` taking 1 parameter (`placeName`), returning `WikipediaSummary?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun getSummary(placeName: String): WikipediaSummary? = withContext(Dispatchers.IO) {
        // `if` statement: executes `return@withContext null` when `placeName.isBlank()` is true
        if (placeName.isBlank()) return@withContext null

        // declares read-only property `resolvedTitle`, initialised with the result of calling `resolveTitle(…)` and opens a lambda / block
        val resolvedTitle = resolveTitle(placeName) ?: run {
            // calls `w` on `Log` with arguments `(TAG, "No Wikipedia article found for '$place…)`
            Log.w(TAG, "No Wikipedia article found for '$placeName'")
            // expression: `return@withContext null`
            return@withContext null
        // closes the lambda assigned to `resolvedTitle`
        }
        // lambda `Log.d(TAG, "Resolved '$placeN… -> '$resolvedTitle'")`
        Log.d(TAG, "Resolved '$placeName' -> '$resolvedTitle'")

        // calls `fetchSummary` with arguments `(resolvedTitle)`
        fetchSummary(resolvedTitle)
    // closes the block
    }


    // declares private function `resolveTitle` taking 1 parameter (`query`), returning `String?` and opens its body
    private fun resolveTitle(query: String): String? {
        // returns `try {` from the current function
        return try {
            // declares read-only property `encoded`, initialised with the result of calling `URLEncoder.encode(…)`
            val encoded = URLEncoder.encode(query.trim(), "UTF-8")
            // declares read-only property `url`, initialised to the string literal "https://en.wikipedia.org/w/api.php" +
            val url = "https://en.wikipedia.org/w/api.php" +
                // continues the statement started above: `"?action=opensearch&search=$encoded&limit=1&namespace=0&for…`
                "?action=opensearch&search=$encoded&limit=1&namespace=0&format=json"
            // calls `d` on `Log` with arguments `(TAG, "OpenSearch: $url")`
            Log.d(TAG, "OpenSearch: $url")
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("User-Agent", "WaypointApp/1.0 (Android…)`
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
            HttpClient.instance.newCall(req).execute().use { resp ->
                // continues the statement started above: `if (!resp.isSuccessful) return null`
                if (!resp.isSuccessful) return null
                // declares read-only property `body`, initialised to `resp.body?.string() ?: return null`
                val body = resp.body?.string() ?: return null
                // declares read-only property `root`, initialised with the result of calling `JSONArray(…)`
                val root   = JSONArray(body)
                // declares read-only property `titles`, initialised with the result of calling `root.optJSONArray(…)`
                val titles = root.optJSONArray(1) ?: return null
                // `if` statement: executes `return null` when `titles.length() == 0` is true
                if (titles.length() == 0) return null
                // calls `getString` on `titles` with arguments `(0)`, then chains `.ifBlank { null }`
                titles.getString(0).ifBlank { null }
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "OpenSearch failed for '$query': ${e.me…)`
            Log.w(TAG, "OpenSearch failed for '$query': ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the function `resolveTitle`
    }

    // declares private function `fetchSummary` taking 1 parameter (`title`), returning `WikipediaSummary?` and opens its body
    private fun fetchSummary(title: String): WikipediaSummary? {
        // returns `try {` from the current function
        return try {
            // declares read-only property `encoded`, initialised with the result of calling `URLEncoder.encode(…)`
            val encoded = URLEncoder.encode(title.trim(), "UTF-8")
                // chained call `.replace` on the previous result with arguments `("+", "_")`
                .replace("+", "_")
            // declares read-only property `url`, initialised to the string literal "https://en.wikipedia.org/api/rest_v1/p…
            val url = "https://en.wikipedia.org/api/rest_v1/page/summary/$encoded"
            // calls `d` on `Log` with arguments `(TAG, "Summary fetch: $url")`
            Log.d(TAG, "Summary fetch: $url")
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("User-Agent", "WaypointApp/1.0 (Android…)`
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
            HttpClient.instance.newCall(req).execute().use { resp ->
                // continues the statement started above: `if (!resp.isSuccessful) {`
                if (!resp.isSuccessful) {
                    // calls `w` on `Log` with arguments `(TAG, "Summary HTTP ${resp.code} for '$title'")`
                    Log.w(TAG, "Summary HTTP ${resp.code} for '$title'")
                    // returns `null` from the current function
                    return null
                // closes the block
                }
                // declares read-only property `body`, initialised to `resp.body?.string() ?: return null`
                val body = resp.body?.string() ?: return null
                // declares read-only property `obj`, initialised with the result of calling `JSONObject(…)`
                val obj  = JSONObject(body)
                // declares read-only property `extract`, initialised with the result of calling `obj.optString(…)`
                val extract = obj.optString("extract").ifBlank { null } ?: return null
                // declares read-only property `thumb`, initialised with the result of calling `obj.optJSONObject(…)`
                val thumb   = obj.optJSONObject("thumbnail")
                    // expression: `?.optString("source")?.ifBlank { null }`
                    ?.optString("source")?.ifBlank { null }
                // calls `d` on `Log` with arguments `(TAG, "Thumbnail for '$title': $thumb")`
                Log.d(TAG, "Thumbnail for '$title': $thumb")
                // calls `WikipediaSummary` with arguments `(extract = extract, thumbnailUrl = thumb)`
                WikipediaSummary(extract = extract, thumbnailUrl = thumb)
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Summary fetch failed for '$title': ${e…)`
            Log.w(TAG, "Summary fetch failed for '$title': ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the function `fetchSummary`
    }
    // declares suspend function `downloadBitmap` taking 1 parameter (`url`), returning `Bitmap?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun downloadBitmap(url: String): Bitmap? = withContext(Dispatchers.IO) {
        // `if` statement: executes `return@withContext null` when `url.isBlank()` is true
        if (url.isBlank()) return@withContext null
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("User-Agent", "WaypointApp/1.0 (Android…)`
                .header("User-Agent", "WaypointApp/1.0 (Android; prog7314@iie.ac.za)")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
            HttpClient.instance.newCall(req).execute().use { resp ->
                // continues the statement started above: `if (!resp.isSuccessful) {`
                if (!resp.isSuccessful) {
                    // calls `w` on `Log` with arguments `(TAG, "Bitmap download HTTP \${resp.code} for…)`
                    Log.w(TAG, "Bitmap download HTTP \${resp.code} for \$url")
                    // expression: `return@withContext null`
                    return@withContext null
                // closes the block
                }
                // declares read-only property `bytes`, initialised to `resp.body?.bytes() ?: return@withContext null`
                val bytes = resp.body?.bytes() ?: return@withContext null
                // calls `decodeByteArray` on `BitmapFactory` with arguments `(bytes, 0, bytes.size)`
                BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Bitmap download failed: \${e.message}")`
            Log.w(TAG, "Bitmap download failed: \${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

// closes the object `WikipediaPlaceRepository`
}
