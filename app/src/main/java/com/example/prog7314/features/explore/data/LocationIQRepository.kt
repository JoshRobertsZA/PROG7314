package com.example.prog7314.features.explore.data

import android.util.Log
import com.example.prog7314.BuildConfig
import com.example.prog7314.core.cache.ExplorePlace
import com.example.prog7314.core.cache.GitHubCacheRepository
import com.example.prog7314.core.cache.PlacesCache
import com.example.prog7314.core.network.HttpClient
import com.example.prog7314.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.time.LocalDate
import java.time.ZoneOffset

/**
 * Cache-first nearby-places data source using LocationIQ.
 *
 * Flow:
 *   1. Check GitHub cache (cache/places_{citySlug}.json)
 *   2. If fresh (same UTC day) AND non-empty, return cached places
 *   3. Otherwise: geocode city -> lat/lon, fetch nearby places,
 *      write to cache, increment CounterAPI, return fresh places
 *   4. If network fails and stale cache exists, return stale as fallback
 */
object LocationIQRepository {

    private const val TAG = "LocationIQRepo"
    private const val NEARBY_RADIUS_METRES = 15000

    fun citySlug(city: String): String =
        city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")

    fun placesPath(citySlug: String) = "cache/places_${citySlug}.json"

    // ── Public API ──────────────────────────────────────────────────────────

    suspend fun getPlaces(city: String): PlacesCache? {
        val slug = citySlug(city)
        val path = placesPath(slug)

        val cached = readFromCache(path)
        // Treat an empty cache as invalid so a previous failed/empty fetch
        // does not permanently block the city from loading real places.
        if (cached != null && !cached.isStale() && cached.places.isNotEmpty()) {
            Log.d(TAG, "Cache hit: $city (${cached.places.size} places)")
            return cached
        }

        val fresh = fetchFromApi(city, slug)
        if (fresh != null) {
            writeToCache(path, fresh)
            incrementCounter()
            return fresh
        }

        if (cached != null) {
            Log.d(TAG, "Returning stale cache for $city after API failure")
        }
        return cached
    }

    /**
     * Entry point when lat/lon are already known (e.g. from device GPS).
     * Skips geocoding, uses rounded coords as cache slug.
     */
    suspend fun getPlacesByCoords(lat: Double, lon: Double): PlacesCache? {
        val slug = coordSlug(lat, lon)
        val path = placesPath(slug)

        val cached = readFromCache(path)
        if (cached != null && !cached.isStale() && cached.places.isNotEmpty()) {
            Log.d(TAG, "Cache hit: $slug (${cached.places.size} places)")
            return cached
        }

        val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
        if (key.isBlank()) return null

        val places = withContext(Dispatchers.IO) { fetchNearby(lat, lon, key) }
        if (places != null) {
            val fresh = PlacesCache(
                city           = slug,
                fetchedDateUtc = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString(),
                places         = places,
            )
            writeToCache(path, fresh)
            incrementCounter()
            return fresh
        }

        return cached
    }

    /** Reverse geocodes lat/lon to a human-readable suburb/city string, or null on failure. */
    suspend fun reverseGeocode(lat: Double, lon: Double, key: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://us1.locationiq.com/v1/reverse" +
                          "?key=$key&lat=$lat&lon=$lon&format=json"
                val req = Request.Builder().url(url).get().build()
                HttpClient.instance.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) return@withContext null
                    val obj  = JSONObject(resp.body?.string() ?: return@withContext null)
                    val addr = obj.optJSONObject("address") ?: return@withContext null
                    val suburb = addr.optString("suburb").ifBlank { null }
                    val city   = addr.optString("city").ifBlank {
                        addr.optString("town").ifBlank {
                            addr.optString("county").ifBlank { null }
                        }
                    }
                    listOfNotNull(suburb, city).joinToString(", ").ifBlank { null }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Reverse geocode failed: ${e.message}")
                null
            }
        }

    /** Cache slug from GPS coordinates rounded to ~1 km grid. */
    private fun coordSlug(lat: Double, lon: Double) =
        "%.2f_%.2f".format(lat, lon).replace('-', 'n').replace('.', 'd')

    // ── Cache ───────────────────────────────────────────────────────────────

    private suspend fun readFromCache(path: String): PlacesCache? {
        val json = GitHubCacheRepository.readJson(path) ?: return null
        return try {
            val placesArray = json.getJSONArray("places")
            val places = (0 until placesArray.length()).map { i ->
                val p = placesArray.getJSONObject(i)
                ExplorePlace(
                    id             = p.getString("id"),
                    name           = p.getString("name"),
                    type           = p.getString("type"),
                    category       = p.getString("category"),
                    lat            = p.getDouble("lat"),
                    lon            = p.getDouble("lon"),
                    displayAddress = p.getString("displayAddress"),
                    distanceMetres = p.getInt("distanceMetres"),
                )
            }
            PlacesCache(
                city            = json.getString("city"),
                fetchedDateUtc  = json.getString("fetchedDateUtc"),
                places          = places,
            )
        } catch (e: Exception) {
            Log.w(TAG, "Cache parse error for $path: ${e.message}")
            null
        }
    }

    private suspend fun writeToCache(path: String, data: PlacesCache) {
        val placesArray = JSONArray().apply {
            data.places.forEach { p ->
                put(JSONObject().apply {
                    put("id",             p.id)
                    put("name",           p.name)
                    put("type",           p.type)
                    put("category",       p.category)
                    put("lat",            p.lat)
                    put("lon",            p.lon)
                    put("displayAddress", p.displayAddress)
                    put("distanceMetres", p.distanceMetres)
                })
            }
        }
        val json = JSONObject().apply {
            put("city",           data.city)
            put("fetchedDateUtc", data.fetchedDateUtc)
            put("places",         placesArray)
        }
        GitHubCacheRepository.writeJson(
            path, json, "chore(cache): update places for ${data.city}",
        )
    }

    // ── API fetch ───────────────────────────────────────────────────────────

    private suspend fun fetchFromApi(city: String, slug: String): PlacesCache? =
        withContext(Dispatchers.IO) {
            val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
            if (key.isBlank()) {
                Log.w(TAG, "No LocationIQ key available")
                return@withContext null
            }

            val coords = geocodeCityWithKey(city, key) ?: return@withContext null
            val (lat, lon) = coords

            val places = withContext(Dispatchers.IO) { fetchNearby(lat, lon, key) }
            if (places == null) return@withContext null

            PlacesCache(
                city           = slug,
                fetchedDateUtc = LocalDate.now(ZoneOffset.UTC).toString(),
                places         = places,
            )
        }

    /**
     * Geocodes [city] using the LocationIQ search API.
     * Returns (lat, lon) or null on failure.
     * Fetches the API key from [RemoteSecrets] internally.
     */
    suspend fun geocodeCity(city: String): Pair<Double, Double>? {
        val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
        if (key.isBlank()) return null
        return withContext(Dispatchers.IO) { geocodeCityWithKey(city, key) }
    }

    /** Returns (lat, lon) for the city, or null on failure. */
    private fun geocodeCityWithKey(city: String, key: String): Pair<Double, Double>? {
        return try {
            val encoded = URLEncoder.encode(city.trim(), "UTF-8")
            val url = "https://us1.locationiq.com/v1/search" +
                      "?key=$key&q=$encoded&format=json&limit=1"
            val req = Request.Builder().url(url).get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    Log.w(TAG, "Geocode HTTP ${resp.code} for $city")
                    return null
                }
                val arr = JSONArray(resp.body?.string() ?: return null)
                if (arr.length() == 0) return null
                val first = arr.getJSONObject(0)
                Pair(first.getString("lat").toDouble(), first.getString("lon").toDouble())
            }
        } catch (e: Exception) {
            Log.w(TAG, "Geocode failed for $city: ${e.message}")
            null
        }
    }

    /**
     * Fetches nearby places from LocationIQ /v1/nearby.
     *
     * Only these four tag values are confirmed valid on LocationIQ's free tier.
     * Other OSM types (hotel, museum, cinema, etc.) return HTTP 404.
     * Using only four tags also keeps us well under the per-minute rate limit.
     */
    private fun fetchNearby(lat: Double, lon: Double, key: String): List<ExplorePlace>? {
        // Valid tag values from the LocationIQ tag list.
        // bar/attraction are NOT in the list and return HTTP 404.
        // A 400ms delay between calls prevents HTTP 429 rate limiting.
        val tags = listOf("restaurant", "cafe", "hotel", "pub", "cinema", "park")

        val seen    = mutableSetOf<String>()
        val results = mutableListOf<ExplorePlace>()
        var anySucceeded = false

        for ((index, tag) in tags.withIndex()) {
            if (index > 0) Thread.sleep(400)
            try {
                val url = "https://us1.locationiq.com/v1/nearby" +
                          "?key=$key" +
                          "&lat=$lat" +
                          "&lon=$lon" +
                          "&tag=$tag" +
                          "&radius=$NEARBY_RADIUS_METRES" +
                          "&format=json"
                val req = Request.Builder().url(url).get().build()
                HttpClient.instance.newCall(req).execute().use { resp ->
                    if (!resp.isSuccessful) {
                        Log.w(TAG, "Nearby[$tag] HTTP ${resp.code}")
                        return@use
                    }
                    anySucceeded = true
                    val body = resp.body?.string() ?: return@use
                    val arr  = JSONArray(body)
                    Log.d(TAG, "Nearby[$tag] returned ${arr.length()} results")
                    for (i in 0 until arr.length()) {
                        try {
                            val p       = arr.getJSONObject(i)
                            val rawName = p.optString("name").ifBlank { p.optString("display_name", "") }
                            if (rawName.isBlank()) continue
                            val id = p.optString("place_id", "${tag}_$i")
                            if (!seen.add(id)) continue
                            results.add(
                                ExplorePlace(
                                    id             = id,
                                    name           = rawName.lines().first().trim(),
                                    type           = p.optString("type", tag),
                                    category       = p.optString("class", ""),
                                    lat            = p.getString("lat").toDouble(),
                                    lon            = p.getString("lon").toDouble(),
                                    displayAddress = p.optString("display_name", ""),
                                    distanceMetres = p.optInt("distance", 0),
                                )
                            )
                        } catch (_: Exception) { }
                    }
                }
            } catch (e: Exception) {
                Log.w(TAG, "Nearby[$tag] failed: ${e.message}")
            }
        }

        if (!anySucceeded && results.isEmpty()) return null
        return results.sortedBy { it.distanceMetres }
    }

    // ── CounterAPI v2 ───────────────────────────────────────────────────────

    private suspend fun incrementCounter() = withContext(Dispatchers.IO) {
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        val counter = RemoteSecrets.get("COUNTERAPI_PLACES_SLUG", BuildConfig.COUNTERAPI_PLACES_SLUG)

        if (token.isBlank() || workspace.isBlank() || counter.isBlank()) {
            Log.d(TAG, "CounterAPI not configured -- skipping increment")
            return@withContext
        }

        try {
            val url = "https://api.counterapi.dev/v2/$workspace/$counter/up"
            val req = Request.Builder()
                .url(url)
                .header("Authorization", "Bearer $token")
                .get()
                .build()
            val resp = HttpClient.instance.newCall(req).execute()
            Log.d(TAG, "CounterAPI increment -> HTTP ${resp.code}")
        } catch (e: Exception) {
            Log.w(TAG, "CounterAPI increment failed: ${e.message}")
        }
    }
}
