package com.waypoint.app.features.home.data

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.cache.GitHubCacheRepository
import com.waypoint.app.core.cache.WeatherCache
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

/**
 * Cache-first weather data source.
 *
 * Flow: read GitHub cache -> if fresh, return it; else call OpenWeatherMap,
 * write result back to cache, return fresh data. If the API call fails and
 * a stale cache entry exists, the stale entry is returned as a fallback so
 * the UI always shows something.
 */
object WeatherRepository {

    private const val TAG = "WeatherRepo"

    /** Normalises a city name into a safe cache-file slug, e.g. "Cape Town" -> "cape_town". */
    fun citySlug(city: String): String =
        city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")

    suspend fun getWeather(city: String): WeatherCache? {
        val slug = citySlug(city)
        val path = GitHubCacheRepository.weatherPath(slug)

        val cached = readFromCache(path)
        if (cached != null && !cached.isStale()) {
            Log.d(TAG, "Cache hit: $city")
            return cached
        }

        val fresh = fetchFromApi(city)
        if (fresh != null) {
            writeToCache(path, fresh)
            return fresh
        }

        // Network failed: surface stale data rather than nothing
        if (cached != null) Log.d(TAG, "Returning stale cache for $city after API failure")
        return cached
    }

    private suspend fun readFromCache(path: String): WeatherCache? {
        val json = GitHubCacheRepository.readJson(path) ?: return null
        return try {
            WeatherCache(
                city        = json.getString("city"),
                displayName = json.getString("displayName"),
                tempC       = json.getDouble("tempC"),
                description = json.getString("description"),
                fetchedAtMs = json.getLong("fetchedAtMs"),
            )
        } catch (e: Exception) {
            Log.w(TAG, "Cache parse error for $path: ${e.message}")
            null
        }
    }

    private suspend fun fetchFromApi(city: String): WeatherCache? = withContext(Dispatchers.IO) {
        val key = RemoteSecrets.get("OPENWEATHER_API_KEY", BuildConfig.OPENWEATHER_API_KEY)
        if (key.isBlank()) { Log.w(TAG, "No OpenWeatherMap key"); return@withContext null }

        try {
            val encodedCity = city.trim().replace(" ", "+")
            val url = "https://api.openweathermap.org/data/2.5/weather" +
                      "?q=$encodedCity&units=metric&appid=$key"
            val req = Request.Builder()
                .url(url)
                .header("Accept", "application/json")
                .get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) {
                Log.w(TAG, "OpenWeatherMap HTTP ${resp.code} for $city")
                return@withContext null
            }
            val body = JSONObject(resp.body?.string() ?: return@withContext null)
            WeatherCache(
                city        = citySlug(city),
                displayName = body.getString("name"),
                tempC       = body.getJSONObject("main").getDouble("temp"),
                description = body.getJSONArray("weather")
                                  .getJSONObject(0)
                                  .getString("description")
                                  .replaceFirstChar { it.uppercase() },
                fetchedAtMs = System.currentTimeMillis(),
            )
        } catch (e: Exception) {
            Log.w(TAG, "API fetch error for $city: ${e.message}")
            null
        }
    }

    private suspend fun writeToCache(path: String, data: WeatherCache) {
        val json = JSONObject().apply {
            put("city",        data.city)
            put("displayName", data.displayName)
            put("tempC",       data.tempC)
            put("description", data.description)
            put("fetchedAtMs", data.fetchedAtMs)
        }
        GitHubCacheRepository.writeJson(
            path, json, "chore(cache): update weather for ${data.displayName}",
        )
    }
}
