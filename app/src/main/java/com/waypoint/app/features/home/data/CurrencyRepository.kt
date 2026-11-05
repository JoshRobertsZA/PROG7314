package com.waypoint.app.features.home.data

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.cache.CurrencyCache
import com.waypoint.app.core.cache.CurrencyRatesCache
import com.waypoint.app.core.cache.GitHubCacheRepository
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

/**
 * Live exchange rates from ExchangeRate-API, cached per base currency on the phone and in the shared
 * API cache.
 */
object CurrencyRepository {

    private const val TAG = "CurrencyRepo"

    suspend fun getRate(fromCode: String, toCode: String = "ZAR"): CurrencyCache? {
        if (fromCode == toCode) {
            // returns a rate of 1.0 without calling the API, because converting a currency to itself needs no lookup
            return CurrencyCache(fromCode, toCode, 1.0, System.currentTimeMillis())
        }

        val rates = getRates(fromCode) ?: return null
        val rate = rates.rateFor(toCode) ?: return null

        // returns a `CurrencyCache` for this pair, built from the cached rates table
        return CurrencyCache(
            fromCode    = fromCode,
            toCode      = toCode,
            rate        = rate,
            fetchedAtMs = rates.fetchedAtMs,
        )
    }

    private suspend fun getRates(baseCode: String): CurrencyRatesCache? {
        val path = GitHubCacheRepository.currencyRatesPath(baseCode)

        val cached = readFromCache(path)
        if (cached != null && !cached.isStale()) {
            Log.d(TAG, "Cache hit: $baseCode")
            return cached
        }

        val fresh = fetchFromApi(baseCode)
        if (fresh != null) {
            writeToCache(path, fresh)
            return fresh
        }

        if (cached != null) Log.d(TAG, "Returning stale cache for $baseCode after API failure")
        return cached
    }

    private fun parseRates(ratesJson: JSONObject): Map<String, Double> =
        ratesJson.keys().asSequence().associateWith { ratesJson.getDouble(it) }

    private suspend fun readFromCache(path: String): CurrencyRatesCache? {
        val json = GitHubCacheRepository.readJson(path) ?: return null
        return try {
            CurrencyRatesCache(
                baseCode    = json.getString("baseCode"),
                rates       = parseRates(json.getJSONObject("rates")),
                fetchedAtMs = json.getLong("fetchedAtMs"),
            )
        } catch (e: Exception) {
            Log.w(TAG, "Cache parse error for $path: ${e.message}")
            null
        }
    }

    private suspend fun fetchFromApi(baseCode: String): CurrencyRatesCache? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("EXCHANGERATE_API_KEY", BuildConfig.EXCHANGERATE_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No ExchangeRate key"); return@withContext null }

        try {
            val url = "https://v6.exchangerate-api.com/v6/$key/latest/$baseCode"
            val req = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) {
                Log.w(TAG, "ExchangeRate-API HTTP ${resp.code} for $baseCode")
                return@withContext null
            }
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            if (body.optString("result") != "success") {
                Log.w(TAG, "ExchangeRate-API error: ${body.optString("error-type")}")
                return@withContext null
            }
            CurrencyRatesCache(
                baseCode    = baseCode,
                rates       = parseRates(body.getJSONObject("conversion_rates")),
                fetchedAtMs = System.currentTimeMillis(),
            )
        } catch (e: Exception) {
            Log.w(TAG, "API fetch error for $baseCode: ${e.message}")
            null
        }
    }

    private suspend fun writeToCache(path: String, data: CurrencyRatesCache) {
        val ratesJson = JSONObject().apply {
            data.rates.forEach { (code, value) -> put(code, value) }
        }
        val json = JSONObject().apply {
            put("baseCode",    data.baseCode)
            put("fetchedAtMs", data.fetchedAtMs)
            put("rates",       ratesJson)
        }
        GitHubCacheRepository.writeJson(path, json)
    }
}
