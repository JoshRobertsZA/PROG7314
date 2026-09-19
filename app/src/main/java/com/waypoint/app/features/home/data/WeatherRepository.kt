// declares that this file belongs to the package `com.waypoint.app.features.home.data`
package com.waypoint.app.features.home.data

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.cache.GitHubCacheRepository` for use in this file
import com.waypoint.app.core.cache.GitHubCacheRepository
// imports `com.waypoint.app.core.cache.WeatherCache` for use in this file
import com.waypoint.app.core.cache.WeatherCache
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `java.util.Calendar` for use in this file
import java.util.Calendar
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject

// declares object `WeatherRepository` and opens its body
object WeatherRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "WeatherRepo"
    private const val TAG = "WeatherRepo"

    // declares function `citySlug` taking 1 parameter (`city`), returning `String`; its body is the expression ``
    fun citySlug(city: String): String =
        // continues the statement started above: `city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")`
        city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")

    // declares suspend function `getWeather` taking 1 parameter (`city`), returning `WeatherCache?` and opens its body
    suspend fun getWeather(city: String): WeatherCache? {
        // declares read-only property `slug`, initialised with the result of calling `citySlug(…)`
        val slug = citySlug(city)
        // declares read-only property `path`, initialised with the result of calling `GitHubCacheRepository.weatherPath(…)`
        val path = GitHubCacheRepository.weatherPath(slug)

        // declares read-only property `cached`, initialised with the result of calling `readFromCache(…)`
        val cached = readFromCache(path)
        // `if` statement: the block below runs when `cached != null && !cached.isStale()` is true
        if (cached != null && !cached.isStale()) {
            // calls `d` on `Log` with arguments `(TAG, "Cache hit: $city")`
            Log.d(TAG, "Cache hit: $city")
            // returns `cached` from the current function
            return cached
        // closes the if block
        }

        // declares read-only property `fresh`, initialised with the result of calling `fetchFromApi(…)`
        val fresh = fetchFromApi(city)
        // `if` statement: the block below runs when `fresh != null` is true
        if (fresh != null) {
            // calls `writeToCache` with arguments `(path, fresh)`
            writeToCache(path, fresh)
            // returns `fresh` from the current function
            return fresh
        // closes the if block
        }

        // `if` statement: executes `Log.d(TAG, "Returning stale cache for $city …` when `cached != null` is true
        if (cached != null) Log.d(TAG, "Returning stale cache for $city after API failure")
        // returns `cached` from the current function
        return cached
    // closes the function `getWeather`
    }

    // declares suspend function `getWeatherByCoords` taking 2 parameters (`lat`, `lon`), returning `WeatherCache?` and opens its body
    suspend fun getWeatherByCoords(lat: Double, lon: Double): WeatherCache? {
        // declares read-only property `slug`, initialised with the result of calling `coordSlug(…)`
        val slug = coordSlug(lat, lon)
        // declares read-only property `path`, initialised with the result of calling `GitHubCacheRepository.weatherPath(…)`
        val path = GitHubCacheRepository.weatherPath(slug)

        // declares read-only property `cached`, initialised with the result of calling `readFromCache(…)`
        val cached = readFromCache(path)
        // `if` statement: the block below runs when `cached != null && !isDailyStale(cached.fetchedAtMs)` is true
        if (cached != null && !isDailyStale(cached.fetchedAtMs)) {
            // calls `d` on `Log` with arguments `(TAG, "Cache hit (daily): $slug")`
            Log.d(TAG, "Cache hit (daily): $slug")
            // returns `cached` from the current function
            return cached
        // closes the if block
        }

        // declares read-only property `fresh`, initialised with the result of calling `fetchFromApiByCoords(…)`
        val fresh = fetchFromApiByCoords(lat, lon)
        // `if` statement: the block below runs when `fresh != null` is true
        if (fresh != null) {
            // calls `writeToCache` with arguments `(path, fresh.copy(city = slug))`
            writeToCache(path, fresh.copy(city = slug))
            // returns `fresh` from the current function
            return fresh
        // closes the if block
        }

        // `if` statement: executes `Log.d(TAG, "Returning stale coord cache for …` when `cached != null` is true
        if (cached != null) Log.d(TAG, "Returning stale coord cache for $slug after API failure")
        // returns `cached` from the current function
        return cached
    // closes the function `getWeatherByCoords`
    }

    // declares private function `coordSlug` taking 2 parameters (`lat`, `lon`); its body is the expression ``
    private fun coordSlug(lat: Double, lon: Double) =
        // continues the statement started above: `"%.2f_%.2f".format(lat, lon).replace('-', 'n').replace('.',…`
        "%.2f_%.2f".format(lat, lon).replace('-', 'n').replace('.', 'd')

    // declares private function `isDailyStale` taking 1 parameter (`fetchedAtMs`), returning `Boolean` and opens its body
    private fun isDailyStale(fetchedAtMs: Long): Boolean {
        // declares read-only property `now`, initialised with the result of calling `Calendar.getInstance(…)`
        val now     = Calendar.getInstance()
        // declares read-only property `fetched`, initialised with the result of calling `Calendar.getInstance(…)`
        val fetched = Calendar.getInstance().apply { timeInMillis = fetchedAtMs }
        // returns `now.get(Calendar.YEAR) != fetched.get(Calendar.YEAR) ||` from the current function
        return now.get(Calendar.YEAR)        != fetched.get(Calendar.YEAR) ||
               // continues the statement started above: `now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_O…`
               now.get(Calendar.DAY_OF_YEAR) != fetched.get(Calendar.DAY_OF_YEAR)
    // closes the function `isDailyStale`
    }

    // declares private suspend function `fetchFromApiByCoords` taking 2 parameters (`lat`, `lon`), returning `WeatherCache?`; its body is the expression ``
    private suspend fun fetchFromApiByCoords(lat: Double, lon: Double): WeatherCache? =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
            val key = RemoteSecrets.get("OPENWEATHER_API_KEY", BuildConfig.OPENWEATHER_API_KEY)
            // `if` statement: executes `{ Log.w(TAG, "No OpenWeatherMap key"); retur…` when `key.isBlank()` is true
            if (key.isBlank()) { Log.w(TAG, "No OpenWeatherMap key"); return@withContext null }
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // declares read-only property `url`, initialised to the string literal "https://api.openweathermap.org/data/2.…
                val url = "https://api.openweathermap.org/data/2.5/weather" +
                          // continues the statement started above: `"?lat=$lat&lon=$lon&units=metric&appid=$key"`
                          "?lat=$lat&lon=$lon&units=metric&appid=$key"
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
                    // calls `w` on `Log` with arguments `(TAG, "OpenWeatherMap coords HTTP \${resp.cod…)`
                    Log.w(TAG, "OpenWeatherMap coords HTTP \${resp.code}")
                    // expression: `return@withContext null`
                    return@withContext null
                // closes the if block
                }
                // declares read-only property `body`, initialised with the result of calling `JSONObject(…)`
                val body = JSONObject(resp.body?.string() ?: return@withContext null)
                // calls `WeatherCache` with an argument list that continues on the following lines
                WeatherCache(
                    // continues the statement started above: `city = coordSlug(lat, lon),`
                    city        = coordSlug(lat, lon),
                    // continues the statement started above: `displayName = body.getString("name"),`
                    displayName = body.getString("name"),
                    // continues the statement started above: `tempC = body.getJSONObject("main").getDouble("temp"),`
                    tempC       = body.getJSONObject("main").getDouble("temp"),
                    // continues the statement started above: `description = body.getJSONArray("weather")`
                    description = body.getJSONArray("weather")
                                      // continues the statement started above: `.getJSONObject(0)`
                                      .getJSONObject(0)
                                      // continues the statement started above: `.getString("description")`
                                      .getString("description")
                                      // continues the statement started above: `.replaceFirstChar { it.uppercase() },`
                                      .replaceFirstChar { it.uppercase() },
                    // continues the statement started above: `fetchedAtMs = System.currentTimeMillis(),`
                    fetchedAtMs = System.currentTimeMillis(),
                // closes the multi-line argument list started above
                )
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "API fetch error (coords): \${e.message…)`
                Log.w(TAG, "API fetch error (coords): \${e.message}")
                // expression: `null`
                null
            // closes the catch block
            }
        // closes the block
        }

    // declares private suspend function `readFromCache` taking 1 parameter (`path`), returning `WeatherCache?` and opens its body
    private suspend fun readFromCache(path: String): WeatherCache? {
        // declares read-only property `json`, initialised with the result of calling `GitHubCacheRepository.readJson(…)`
        val json = GitHubCacheRepository.readJson(path) ?: return null
        // returns `try {` from the current function
        return try {
            // calls `WeatherCache` with an argument list that continues on the following lines
            WeatherCache(
                // continues the statement started above: `city = json.getString("city"),`
                city        = json.getString("city"),
                // continues the statement started above: `displayName = json.getString("displayName"),`
                displayName = json.getString("displayName"),
                // continues the statement started above: `tempC = json.getDouble("tempC"),`
                tempC       = json.getDouble("tempC"),
                // continues the statement started above: `description = json.getString("description"),`
                description = json.getString("description"),
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

    // declares private suspend function `fetchFromApi` taking 1 parameter (`city`), returning `WeatherCache?`; its body is the expression `withContext(Dispatchers.IO) {`
    private suspend fun fetchFromApi(city: String): WeatherCache? = withContext(Dispatchers.IO) {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("OPENWEATHER_API_KEY", BuildConfig.OPENWEATHER_API_KEY)
        // `if` statement: executes `{ Log.w(TAG, "No OpenWeatherMap key"); retur…` when `key.isBlank()` is true
        if (key.isBlank()) { Log.w(TAG, "No OpenWeatherMap key"); return@withContext null }

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `encodedCity`, initialised with the result of calling `city.trim(…)`
            val encodedCity = city.trim().replace(" ", "+")
            // declares read-only property `url`, initialised to the string literal "https://api.openweathermap.org/data/2.…
            val url = "https://api.openweathermap.org/data/2.5/weather" +
                      // continues the statement started above: `"?q=$encodedCity&units=metric&appid=$key"`
                      "?q=$encodedCity&units=metric&appid=$key"
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
                // calls `w` on `Log` with arguments `(TAG, "OpenWeatherMap HTTP ${resp.code} for $…)`
                Log.w(TAG, "OpenWeatherMap HTTP ${resp.code} for $city")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }
            // declares read-only property `body`, initialised with the result of calling `JSONObject(…)`
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            // calls `WeatherCache` with an argument list that continues on the following lines
            WeatherCache(
                // continues the statement started above: `city = citySlug(city),`
                city        = citySlug(city),
                // continues the statement started above: `displayName = body.getString("name"),`
                displayName = body.getString("name"),
                // continues the statement started above: `tempC = body.getJSONObject("main").getDouble("temp"),`
                tempC       = body.getJSONObject("main").getDouble("temp"),
                // continues the statement started above: `description = body.getJSONArray("weather")`
                description = body.getJSONArray("weather")
                                  // continues the statement started above: `.getJSONObject(0)`
                                  .getJSONObject(0)
                                  // continues the statement started above: `.getString("description")`
                                  .getString("description")
                                  // continues the statement started above: `.replaceFirstChar { it.uppercase() },`
                                  .replaceFirstChar { it.uppercase() },
                // continues the statement started above: `fetchedAtMs = System.currentTimeMillis(),`
                fetchedAtMs = System.currentTimeMillis(),
            // closes the multi-line argument list started above
            )
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "API fetch error for $city: ${e.message…)`
            Log.w(TAG, "API fetch error for $city: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

    // declares private suspend function `writeToCache` taking 2 parameters (`path`, `data`) and opens its body
    private suspend fun writeToCache(path: String, data: WeatherCache) {
        // declares read-only property `json`, initialised with the result of calling `JSONObject(…)` and opens a lambda / block
        val json = JSONObject().apply {
            // calls `put` with arguments `("city", data.city)`
            put("city",        data.city)
            // calls `put` with arguments `("displayName", data.displayName)`
            put("displayName", data.displayName)
            // calls `put` with arguments `("tempC", data.tempC)`
            put("tempC",       data.tempC)
            // calls `put` with arguments `("description", data.description)`
            put("description", data.description)
            // calls `put` with arguments `("fetchedAtMs", data.fetchedAtMs)`
            put("fetchedAtMs", data.fetchedAtMs)
        // closes the lambda assigned to `json`
        }
        // calls `writeJson` on `GitHubCacheRepository` with an argument list that continues on the following lines
        GitHubCacheRepository.writeJson(
            // continues the statement started above: `path, json, "chore(cache): update weather for ${data.displa…`
            path, json, "chore(cache): update weather for ${data.displayName}",
        // closes the multi-line argument list started above
        )
    // closes the function `writeToCache`
    }
// closes the object `WeatherRepository`
}
