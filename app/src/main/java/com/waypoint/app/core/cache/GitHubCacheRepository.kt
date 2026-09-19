// declares that this file belongs to the package `com.waypoint.app.core.cache`
package com.waypoint.app.core.cache

// imports `android.util.Base64` for use in this file
import android.util.Base64
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
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
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `GitHubCacheRepository` and opens its body
object GitHubCacheRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "GitHubCacheRepo"
    private const val TAG = "GitHubCacheRepo"
    // declares private read-only property `JSON_MEDIA_TYPE`, initialised to the string literal "application/json; charset=utf-8".toMed…
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    // declares function `weatherPath` taking 1 parameter (`citySlug`); its body is the expression `"cache/weather_${citySlug}.json"`
    fun weatherPath(citySlug: String) = "cache/weather_${citySlug}.json"
    // declares function `currencyPath` taking 1 parameter (`fromCode`); its body is the expression `"cache/currency_${fromCode}_ZAR.json"`
    fun currencyPath(fromCode: String) = "cache/currency_${fromCode}_ZAR.json"

    // declares private read-only property `readToken` of type `String`
    private val readToken: String
        // custom getter: returns `buildString {`
        get() = buildString {
            // calls `append` with arguments `(BuildConfig.regionSeed)`
            append(BuildConfig.regionSeed)
            // calls `append` with arguments `(BuildConfig.cacheEpoch)`
            append(BuildConfig.cacheEpoch)
            // calls `append` with arguments `(BuildConfig.deviceClassTag)`
            append(BuildConfig.deviceClassTag)
            // calls `append` with arguments `(BuildConfig.syncNonce)`
            append(BuildConfig.syncNonce)
            // calls `append` with arguments `(BuildConfig.featureGateId)`
            append(BuildConfig.featureGateId)
            // calls `append` with arguments `(BuildConfig.telemetryPrefix)`
            append(BuildConfig.telemetryPrefix)
            // calls `append` with arguments `(BuildConfig.sessionSlot)`
            append(BuildConfig.sessionSlot)
            // calls `append` with arguments `(BuildConfig.buildFingerprint)`
            append(BuildConfig.buildFingerprint)
        // closes the block
        }

    // declares private read-only property `writeToken` of type `String`
    private val writeToken: String
        // custom getter: returns `RemoteSecrets.get("GITHUB_WRITE_TOKEN", "")`
        get() = RemoteSecrets.get("GITHUB_WRITE_TOKEN", "")

    // declares private function `contentsUrl` taking 1 parameter (`path`); its body is the expression ``
    private fun contentsUrl(path: String) =
        // continues the statement started above: `"https://api.github.com/repos/${BuildConfig.GITHUB_OWNER}/$…`
        "https://api.github.com/repos/${BuildConfig.GITHUB_OWNER}/${BuildConfig.GITHUB_REPO}/contents/$path"


    // declares suspend function `readJson` taking 1 parameter (`path`), returning `JSONObject?`; its body is the expression `withContext(Dispatchers.IO) {`
    suspend fun readJson(path: String): JSONObject? = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `request`, initialised with the result of calling `Request.Builder(…)`
            val request = Request.Builder()
                // chained call `.url` on the previous result with arguments `(contentsUrl(path))`
                .url(contentsUrl(path))
                // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $readToken")`
                .header("Authorization", "Bearer $readToken")
                // chained call `.header` on the previous result with arguments `("Accept", "application/vnd.github+json")`
                .header("Accept", "application/vnd.github+json")
                // chained call `.get` on the previous result
                .get()
                // chained call `.build` on the previous result
                .build()

            // declares read-only property `response`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val response = HttpClient.instance.newCall(request).execute()
            // `if` statement: executes `return@withContext null` when `response.code == 404` is true
            if (response.code == 404) return@withContext null
            // `if` statement: the block below runs when `!response.isSuccessful` is true
            if (!response.isSuccessful) {
                // lambda `Log.w(TAG, "readJson $path -> HTTP ${response.code}")`
                Log.w(TAG, "readJson $path -> HTTP ${response.code}")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }

            // declares read-only property `meta`, initialised with the result of calling `JSONObject(…)`
            val meta = JSONObject(response.body?.string() ?: return@withContext null)
            // declares read-only property `encoded`, initialised with the result of calling `meta.getString(…)`
            val encoded = meta.getString("content").replace("\n", "")
            // calls `JSONObject` with arguments `(String(Base64.decode(encoded, Base64.DEFAULT…)`
            JSONObject(String(Base64.decode(encoded, Base64.DEFAULT)))
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "readJson failed for $path: ${e.message…)`
            Log.w(TAG, "readJson failed for $path: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

    // declares suspend function `writeJson` taking 3 parameters (`path`, `json`, `commitMessage`), returning `Boolean`; its body is the expression ``
    suspend fun writeJson(path: String, json: JSONObject, commitMessage: String): Boolean =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `token`, initialised to `writeToken`
            val token = writeToken
            // `if` statement: the block below runs when `token.isBlank()` is true
            if (token.isBlank()) {
                // calls `w` on `Log` with arguments `(TAG, "GITHUB_WRITE_TOKEN not available — ski…)`
                Log.w(TAG, "GITHUB_WRITE_TOKEN not available — skipping cache write for $path")
                // expression: `return@withContext false`
                return@withContext false
            // closes the if block
            }
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // declares read-only property `sha`, initialised with the result of calling `currentSha(…)`
                val sha = currentSha(path, token)
                // declares read-only property `contentBase64`, initialised with the result of calling `Base64.encodeToString(…)`
                val contentBase64 = Base64.encodeToString(
                    // continues the statement started above: `json.toString(2).toByteArray(Charsets.UTF_8),`
                    json.toString(2).toByteArray(Charsets.UTF_8),
                    // continues the statement started above: `Base64.NO_WRAP,`
                    Base64.NO_WRAP,
                // closes the multi-line argument list started above
                )
                // declares read-only property `putBody`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
                val putBody = JSONObject().apply {
                    // calls `put` with arguments `("message", commitMessage)`
                    put("message", commitMessage)
                    // calls `put` with arguments `("content", contentBase64)`
                    put("content", contentBase64)
                    // `if` statement: executes `put("sha", sha)` when `sha != null` is true
                    if (sha != null) put("sha", sha)
                // closes the lambda assigned to `putBody`
                }

                // declares read-only property `request`, initialised with the result of calling `Request.Builder(…)`
                val request = Request.Builder()
                    // chained call `.url` on the previous result with arguments `(contentsUrl(path))`
                    .url(contentsUrl(path))
                    // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $token")`
                    .header("Authorization", "Bearer $token")
                    // chained call `.header` on the previous result with arguments `("Accept", "application/vnd.github+json")`
                    .header("Accept", "application/vnd.github+json")
                    // chained call `.put` on the previous result with arguments `(putBody.toString().toRequestBody(JSON_M…)`
                    .put(putBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                    // chained call `.build` on the previous result
                    .build()

                // declares read-only property `response`, initialised with the result of calling `HttpClient.instance.newCall(…)`
                val response = HttpClient.instance.newCall(request).execute()
                // `if` statement: the block below runs when `!response.isSuccessful` is true
                if (!response.isSuccessful) {
                    // lambda `Log.w(TAG, "writeJson $path -> HTTP ${response.code}: ${resp…`
                    Log.w(TAG, "writeJson $path -> HTTP ${response.code}: ${response.body?.string()}")
                // closes the if block
                }
                // expression: `response.isSuccessful`
                response.isSuccessful
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "writeJson failed for $path: ${e.messag…)`
                Log.w(TAG, "writeJson failed for $path: ${e.message}")
                // expression: `false`
                false
            // closes the catch block
            }
        // closes the block
        }


    // declares private function `currentSha` taking 2 parameters (`path`, `token`), returning `String?` and opens its body
    private fun currentSha(path: String, token: String): String? {
        // returns `try {` from the current function
        return try {
            // declares read-only property `request`, initialised with the result of calling `Request.Builder(…)`
            val request = Request.Builder()
                // chained call `.url` on the previous result with arguments `(contentsUrl(path))`
                .url(contentsUrl(path))
                // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $token")`
                .header("Authorization", "Bearer $token")
                // chained call `.header` on the previous result with arguments `("Accept", "application/vnd.github+json")`
                .header("Accept", "application/vnd.github+json")
                // chained call `.get` on the previous result
                .get()
                // chained call `.build` on the previous result
                .build()
            // declares read-only property `response`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val response = HttpClient.instance.newCall(request).execute()
            // `if` statement: executes `return null` when `!response.isSuccessful` is true
            if (!response.isSuccessful) return null
            // calls `JSONObject` with arguments `(response.body?.string() ?: return null)`, then chains `.optString("sha")`, `.ifBlank { null }`
            JSONObject(response.body?.string() ?: return null).optString("sha").ifBlank { null }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the function `currentSha`
    }
// closes the object `GitHubCacheRepository`
}
