// declares that this file belongs to the package `com.waypoint.app.features.home.data`
package com.waypoint.app.features.home.data

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.cache.CurrencyCache` for use in this file
import com.waypoint.app.core.cache.CurrencyCache
// imports `com.waypoint.app.core.cache.CurrencyRatesCache` for use in this file
import com.waypoint.app.core.cache.CurrencyRatesCache
// imports `com.waypoint.app.core.cache.GitHubCacheRepository` for use in this file
import com.waypoint.app.core.cache.GitHubCacheRepository
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `CurrencyRepository` and opens its body
object CurrencyRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "CurrencyRepo"
    private const val TAG = "CurrencyRepo"

    // declares suspend function `getRate` taking 2 parameters (`fromCode`, `toCode`), returning `CurrencyCache?` and opens its body
    suspend fun getRate(fromCode: String, toCode: String = "ZAR"): CurrencyCache? {
        // `if` statement: the block below runs when `fromCode == toCode` is true
        if (fromCode == toCode) {
            // returns a rate of 1.0 without calling the API, because converting a currency to itself needs no lookup
            return CurrencyCache(fromCode, toCode, 1.0, System.currentTimeMillis())
        // closes the if block
        }

        // declares read-only property `rates`, initialised with the result of calling `getRates(…)`, returning null from the function when no rates are available
        val rates = getRates(fromCode) ?: return null
        // declares read-only property `rate`, initialised with the result of calling `rates.rateFor(…)`, returning null from the function when the target currency is missing
        val rate = rates.rateFor(toCode) ?: return null

        // returns a `CurrencyCache` for this pair, built from the cached rates table
        return CurrencyCache(
            // continues the statement started above: `fromCode = fromCode,`
            fromCode    = fromCode,
            // continues the statement started above: `toCode = toCode,`
            toCode      = toCode,
            // continues the statement started above: `rate = rate,`
            rate        = rate,
            // continues the statement started above: `fetchedAtMs = rates.fetchedAtMs,`
            fetchedAtMs = rates.fetchedAtMs,
        // closes the multi-line argument list started above
        )
    // closes the function `getRate`
    }

    // declares private suspend function `getRates` taking 1 parameter (`baseCode`), returning `CurrencyRatesCache?` and opens its body
    private suspend fun getRates(baseCode: String): CurrencyRatesCache? {
        // declares read-only property `path`, initialised with the result of calling `GitHubCacheRepository.currencyRatesPath(…)`
        val path = GitHubCacheRepository.currencyRatesPath(baseCode)

        // declares read-only property `cached`, initialised with the result of calling `readFromCache(…)`
        val cached = readFromCache(path)
        // `if` statement: the block below runs when `cached != null && !cached.isStale()` is true
        if (cached != null && !cached.isStale()) {
            // calls `d` on `Log` with arguments `(TAG, "Cache hit: $baseCode")`
            Log.d(TAG, "Cache hit: $baseCode")
            // returns `cached` from the current function
            return cached
        // closes the if block
        }

        // declares read-only property `fresh`, initialised with the result of calling `fetchFromApi(…)`
        val fresh = fetchFromApi(baseCode)
        // `if` statement: the block below runs when `fresh != null` is true
        if (fresh != null) {
            // calls `writeToCache` with arguments `(path, fresh)`
            writeToCache(path, fresh)
            // returns `fresh` from the current function
            return fresh
        // closes the if block
        }

        // `if` statement: executes `Log.d(TAG, "Returning stale cache for $baseC…` when `cached != null` is true
        if (cached != null) Log.d(TAG, "Returning stale cache for $baseCode after API failure")
        // returns `cached` from the current function
        return cached
    // closes the function `getRates`
    }

    // declares private function `parseRates` taking 1 parameter (`ratesJson`), returning `Map<String, Double>`; its body is the expression `ratesJson.keys().asSequence().associateWith { … }`
    private fun parseRates(ratesJson: JSONObject): Map<String, Double> =
        // continues the statement started above: `ratesJson.keys().asSequence().associateWith { ratesJson.getDouble(it) }`
        ratesJson.keys().asSequence().associateWith { ratesJson.getDouble(it) }

    // declares private suspend function `readFromCache` taking 1 parameter (`path`), returning `CurrencyRatesCache?` and opens its body
    private suspend fun readFromCache(path: String): CurrencyRatesCache? {
        // declares read-only property `json`, initialised with the result of calling `GitHubCacheRepository.readJson(…)`
        val json = GitHubCacheRepository.readJson(path) ?: return null
        // returns `try {` from the current function
        return try {
            // calls `CurrencyRatesCache` with an argument list that continues on the following lines
            CurrencyRatesCache(
                // continues the statement started above: `baseCode = json.getString("baseCode"),`
                baseCode    = json.getString("baseCode"),
                // continues the statement started above: `rates = parseRates(json.getJSONObject("rates")),`
                rates       = parseRates(json.getJSONObject("rates")),
                // continues the statement started above: `fetchedAtMs = json.getLong("fetchedAtMs"),`
                fetchedAtMs = json.getLong("fetchedAtMs"),
            // closes the multi-line argument list started above
            )
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Cache parse error for $path: ${e.messa…)`
            Log.w(TAG, "Cache parse error for $path: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the function `readFromCache`
    }

    // declares private suspend function `fetchFromApi` taking 1 parameter (`baseCode`), returning `CurrencyRatesCache?`; its body is the expression `withContext(Dispatchers.IO) {`
    private suspend fun fetchFromApi(baseCode: String): CurrencyRatesCache? = withContext(Dispatchers.IO) {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("EXCHANGERATE_API_KEY", BuildConfig.EXCHANGERATE_API_KEY)
        // `if` statement: executes `{ Log.w(TAG, "No ExchangeRate key"); return@…` when `key.isBlank()` is true
        if (key.isBlank()) { Log.w(TAG, "No ExchangeRate key"); return@withContext null }

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `url`, initialised to the string literal "https://v6.exchangerate-api.com/v6/$ke…
            val url = "https://v6.exchangerate-api.com/v6/$key/latest/$baseCode"
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("Accept", "application/json")`
                .header("Accept", "application/json")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // `if` statement: the block below runs when `!resp.isSuccessful` is true
            if (!resp.isSuccessful) {
                // calls `w` on `Log` with arguments `(TAG, "ExchangeRate-API HTTP ${resp.code} for…)`
                Log.w(TAG, "ExchangeRate-API HTTP ${resp.code} for $baseCode")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }
            // declares read-only property `body`, initialised with the result of calling `JSONObject(…)`
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            // `if` statement: the block below runs when `body.optString("result") != "success"` is true
            if (body.optString("result") != "success") {
                // calls `w` on `Log` with arguments `(TAG, "ExchangeRate-API error: ${body.optStri…)`
                Log.w(TAG, "ExchangeRate-API error: ${body.optString("error-type")}")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }
            // calls `CurrencyRatesCache` with an argument list that continues on the following lines
            CurrencyRatesCache(
                // continues the statement started above: `baseCode = baseCode,`
                baseCode    = baseCode,
                // continues the statement started above: `rates = parseRates(body.getJSONObject("conversion_rates")),`
                rates       = parseRates(body.getJSONObject("conversion_rates")),
                // continues the statement started above: `fetchedAtMs = System.currentTimeMillis(),`
                fetchedAtMs = System.currentTimeMillis(),
            // closes the multi-line argument list started above
            )
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "API fetch error for $baseCode: ${e.mes…)`
            Log.w(TAG, "API fetch error for $baseCode: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

    // declares private suspend function `writeToCache` taking 2 parameters (`path`, `data`) and opens its body
    private suspend fun writeToCache(path: String, data: CurrencyRatesCache) {
        // declares read-only property `ratesJson`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
        val ratesJson = JSONObject().apply {
            // expression: `data.rates.forEach { (code, value) -> put(code, value) }`
            data.rates.forEach { (code, value) -> put(code, value) }
        // closes the lambda assigned to `ratesJson`
        }
        // declares read-only property `json`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
        val json = JSONObject().apply {
            // calls `put` with arguments `("baseCode", data.baseCode)`
            put("baseCode",    data.baseCode)
            // calls `put` with arguments `("fetchedAtMs", data.fetchedAtMs)`
            put("fetchedAtMs", data.fetchedAtMs)
            // calls `put` with arguments `("rates", ratesJson)`
            put("rates",       ratesJson)
        // closes the lambda assigned to `json`
        }
        // calls `writeJson` on `GitHubCacheRepository` with arguments `(path, json)`
        GitHubCacheRepository.writeJson(path, json)
    // closes the function `writeToCache`
    }
// closes the object `CurrencyRepository`
}
