package com.waypoint.app.core.cache

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

// Was a direct read/write of files in a GitHub repo (a chunked BuildConfig
// token for reads, a writable PAT for writes - the PR 1 audit's other main
// finding, alongside RemoteSecrets). Now reads and writes go through this
// app's own authenticated API instead; see api/src/routes/cache.js for the
// Firestore-backed collection this hits.
object GitHubCacheRepository {

    private const val TAG = "GitHubCacheRepo"
    private val JSON_MEDIA_TYPE = "application/json; charset=utf-8".toMediaType()

    private val BASE_URL get() = BuildConfig.WAYPOINT_API_BASE_URL.trimEnd('/')

    // No "cache/" prefix or ".json" suffix any more - these are now path
    // params matched against the server's weather|currency|places allowlist
    // (api/src/routes/cache.js), not GitHub repo file paths.
    fun weatherPath(citySlug: String) = "weather_${citySlug}"
    fun currencyPath(fromCode: String) = "currency_${fromCode}_ZAR"

    suspend fun readJson(path: String): JSONObject? = withContext(Dispatchers.IO) {
        try {
            val idToken = AuthRepository.getIdToken() ?: return@withContext null
            val request = Request.Builder()
                .url("$BASE_URL/cache/$path")
                .header("Authorization", "Bearer $idToken")
                .get()
                .build()

            val response = HttpClient.instance.newCall(request).execute()
            if (response.code == 404) return@withContext null
            if (!response.isSuccessful) {
                Log.w(TAG, "readJson $path -> HTTP ${response.code}")
                return@withContext null
            }

            JSONObject(response.body?.string() ?: return@withContext null)
        } catch (e: Exception) {
            Log.w(TAG, "readJson failed for $path: ${e.message}")
            null
        }
    }

    suspend fun writeJson(path: String, json: JSONObject): Boolean = withContext(Dispatchers.IO) {
        try {
            val idToken = AuthRepository.getIdToken()
            if (idToken == null) {
                Log.w(TAG, "No signed-in user — skipping cache write for $path")
                return@withContext false
            }

            val request = Request.Builder()
                .url("$BASE_URL/cache/$path")
                .header("Authorization", "Bearer $idToken")
                .put(json.toString().toRequestBody(JSON_MEDIA_TYPE))
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
}
