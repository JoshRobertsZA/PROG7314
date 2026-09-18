package com.waypoint.app.features.home.data

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.cache.CurrencyCache
import com.waypoint.app.core.cache.GitHubCacheRepository
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

/**
 * Cache-first exchange rate data source. Target currency is always ZAR.
 *
 * Flow: read GitHub cache -> if same calendar day, return it; else call
 * ExchangeRate-API, write result back to cache, return fresh data. Stale
 * cache is surfaced as a fallback if the API call fails.
 */
object CurrencyRepository {

    private const val TAG = "CurrencyRepo"

    suspend fun getRate(fromCode: String): CurrencyCache? {
        val path = GitHubCacheRepository.currencyPath(fromCode)

        val cached = readFromCache(path)
        if (cached != null && !cached.isStale()) {
            Log.d(TAG, "Cache hit: $fromCode/ZAR")
            return cached
        }

        val fresh = fetchFromApi(fromCode)
        if (fresh != null) {
            writeToCache(path, fresh)
            return fresh
        }

        if (cached != null) Log.d(TAG, "Returning stale cache for $fromCode/ZAR after API failure")
        return cached
    }

    private suspend fun readFromCache(path: String): CurrencyCache? {
        val json = GitHubCacheRepository.readJson(path) ?: return null
        return try {
            CurrencyCache(
                fromCode    = json.getString("fromCode"),
                rate        = json.getDouble("rate"),
                fetchedAtMs = json.getLong("fetchedAtMs"),
            )
        } catch (e: Exception) {
            Log.w(TAG, "Cache parse error for $path: ${e.message}")
            null
        }
    }

    private suspend fun fetchFromApi(fromCode: String): CurrencyCache? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("EXCHANGERATE_API_KEY", BuildConfig.EXCHANGERATE_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No ExchangeRate key"); return@withContext null }

        try {
            val url = "https://v6.exchangerate-api.com/v6/$key/pair/$fromCode/ZAR"
            val req = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) {
                Log.w(TAG, "ExchangeRate-API HTTP ${resp.code} for $fromCode")
                return@withContext null
            }
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            if (body.optString("result") != "success") {
                Log.w(TAG, "ExchangeRate-API error: ${body.optString("error-type")}")
                return@withContext null
            }
            CurrencyCache(
                fromCode    = fromCode,
                rate        = body.getDouble("conversion_rate"),
                fetchedAtMs = System.currentTimeMillis(),
            )
        } catch (e: Exception) {
            Log.w(TAG, "API fetch error for $fromCode: ${e.message}")
            null
        }
    }

    private suspend fun writeToCache(path: String, data: CurrencyCache) {
        val json = JSONObject().apply {
            put("fromCode",    data.fromCode)
            put("rate",        data.rate)
            put("fetchedAtMs", data.fetchedAtMs)
        }
        GitHubCacheRepository.writeJson(
            path, json, "chore(cache): update exchange rate ${data.fromCode}/ZAR",
        )
    }
}
