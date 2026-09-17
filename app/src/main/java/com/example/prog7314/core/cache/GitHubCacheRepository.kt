package com.example.prog7314.core.cache

import android.util.Base64
import android.util.Log
import com.example.prog7314.BuildConfig
import com.example.prog7314.core.network.HttpClient
import com.example.prog7314.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

/**
 * Reads and writes simple JSON cache blobs to the apiplayground-cache GitHub
 * repo under the cache/ folder.
 *
 * Read uses the read-only bootstrap token (assembled from BuildConfig fields —
 * already available at launch via RemoteSecrets). Write requires
 * GITHUB_WRITE_TOKEN fetched from secrets/keys.json at runtime; the repo
 * owner must add that key to keys.json before cache writes will work.
 *
 * Cache file locations:
 *   cache/weather_{city_slug}.json    2-hour weather data per city
 *   cache/currency_{fromCode}_ZAR.json  daily exchange rate per currency pair
 */
object GitHubCacheRepository {

    private const val TAG = "GitHubCacheRepo"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    fun weatherPath(citySlug: String) = "cache/weather_${citySlug}.json"
    fun currencyPath(fromCode: String) = "cache/currency_${fromCode}_ZAR.json"

    // The read-only bootstrap token — same assembly as RemoteSecrets.kt.
    private val readToken: String
        get() = buildString {
            append(BuildConfig.regionSeed)
            append(BuildConfig.cacheEpoch)
            append(BuildConfig.deviceClassTag)
            append(BuildConfig.syncNonce)
            append(BuildConfig.featureGateId)
            append(BuildConfig.telemetryPrefix)
            append(BuildConfig.sessionSlot)
            append(BuildConfig.buildFingerprint)
        }

    // Write token loaded at runtime from keys.json via RemoteSecrets.
    private val writeToken: String
        get() = RemoteSecrets.get("GITHUB_WRITE_TOKEN", "")

    private fun contentsUrl(path: String) =
        "https://api.github.com/repos/${BuildConfig.GITHUB_OWNER}/${BuildConfig.GITHUB_REPO}/contents/$path"

    // ── Public API ─────────────────────────────────────────────────────────────

    /**
     * Reads a JSON blob from the cache repo. Returns null when the file does
     * not exist yet (404) or any network error occurs.
     */
    suspend fun readJson(path: String): JSONObject? = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url(contentsUrl(path))
                .header("Authorization", "Bearer $readToken")
                .header("Accept", "application/vnd.github+json")
                .get()
                .build()

            val response = HttpClient.instance.newCall(request).execute()
            if (response.code == 404) return@withContext null
            if (!response.isSuccessful) {
                Log.w(TAG, "readJson $path -> HTTP ${response.code}")
                return@withContext null
            }

            val meta = JSONObject(response.body?.string() ?: return@withContext null)
            val encoded = meta.getString("content").replace("\n", "")
            JSONObject(String(Base64.decode(encoded, Base64.DEFAULT)))
        } catch (e: Exception) {
            Log.w(TAG, "readJson failed for $path: ${e.message}")
            null
        }
    }

    /**
     * Writes or updates a JSON blob in the cache repo. Fetches the current
     * file SHA first so GitHub treats the request as an update rather than a
     * conflict. Returns true on success. Logs a warning and returns false if
     * no write token is available.
     */
    suspend fun writeJson(path: String, json: JSONObject, commitMessage: String): Boolean =
        withContext(Dispatchers.IO) {
            val token = writeToken
            if (token.isBlank()) {
                Log.w(TAG, "GITHUB_WRITE_TOKEN not available — skipping cache write for $path")
                return@withContext false
            }
            try {
                val sha = currentSha(path, token)
                val contentBase64 = Base64.encodeToString(
                    json.toString(2).toByteArray(Charsets.UTF_8),
                    Base64.NO_WRAP,
                )
                val putBody = JSONObject().apply {
                    put("message", commitMessage)
                    put("content", contentBase64)
                    if (sha != null) put("sha", sha)
                }

                val request = Request.Builder()
                    .url(contentsUrl(path))
                    .header("Authorization", "Bearer $token")
                    .header("Accept", "application/vnd.github+json")
                    .put(putBody.toString().toRequestBody(JSON_MEDIA_TYPE))
                    .build()

                val response = HttpClient.instance.newCall(request).execute()
                if (!response.isSuccessful) {
                    Log.w(TAG, "writeJson $path -> HTTP ${response.code}: ${response.body?.string()}")
                }
                response.isSuccessful
            } catch (e: Exception) {
                Log.w(TAG, "writeJson failed for $path: ${e.message}")
                false
            }
        }

    // ── Helpers ────────────────────────────────────────────────────────────────

    /** Returns the current file SHA needed by the GitHub PUT endpoint, or null
     *  if the file does not yet exist (first write). */
    private fun currentSha(path: String, token: String): String? {
        return try {
            val request = Request.Builder()
                .url(contentsUrl(path))
                .header("Authorization", "Bearer $token")
                .header("Accept", "application/vnd.github+json")
                .get()
                .build()
            val response = HttpClient.instance.newCall(request).execute()
            if (!response.isSuccessful) return null
            JSONObject(response.body?.string() ?: return null).optString("sha").ifBlank { null }
        } catch (e: Exception) {
            null
        }
    }
}
