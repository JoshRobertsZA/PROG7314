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
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.time.LocalDate
import java.time.ZoneOffset
import java.util.concurrent.TimeUnit
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Cache-first nearby-places data source.
 *
 * Geocoding  -> LocationIQ /v1/search (free tier, reliable)
 * POI lookup -> Overpass API (OpenStreetMap, free, no key, no quota)
 * Caching    -> GitHub repo JSON blobs (calendar-day TTL)
 *
 * Flow:
 *   1. Check GitHub cache (cache/places_{citySlug}.json)
 *   2. If fresh (same UTC day) AND non-empty, return cached places
 *   3. Otherwise: geocode city -> lat/lon via LocationIQ,
 *      fetch all POIs via Overpass, write to cache, increment CounterAPI, return fresh
 *   4. If network fails and stale cache exists, return stale as fallback
 */
object LocationIQRepository {

    private const val TAG = "LocationIQRepo"
    private const val NEARBY_RADIUS_METRES = 15000

    /** Overpass needs a longer timeout than the default 10 s. */
    private val overpassClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(45, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .build()
    }

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

            val coords = geocodeCity(city, key) ?: return@withContext null
            val (lat, lon) = coords

            val places = fetchNearbyOverpass(lat, lon)
            if (places == null) return@withContext null

            PlacesCache(
                city           = slug,
                fetchedDateUtc = LocalDate.now(ZoneOffset.UTC).toString(),
                places         = places,
            )
        }

    /** Returns (lat, lon) for the city via LocationIQ, or null on failure. */
    private fun geocodeCity(city: String, key: String): Pair<Double, Double>? {
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
     * Fetches nearby places from the Overpass API (OpenStreetMap).
     *
     * Uses a single POST query covering all ExploreFilter categories:
     * - amenity: restaurant, cafe, bar, pub, cinema, theatre, nightclub, arts_centre
     * - tourism: hotel, hostel, museum, attraction, gallery, viewpoint, theme_park
     * - leisure: park, sports_centre, stadium
     *
     * No API key required. Overpass is free and returns all OSM node/way/relation data.
     * Distance is calculated client-side via haversine since Overpass does not return it.
     */
    private fun fetchNearbyOverpass(lat: Double, lon: Double): List<ExplorePlace>? {
        val radiusMetres = NEARBY_RADIUS_METRES

        // OverpassQL: union of node queries for each amenity/tourism/leisure value
        val query = """
            [out:json][timeout:40];
            (
              node["amenity"~"^(restaurant|cafe|bar|pub|cinema|theatre|nightclub|arts_centre)$"](around:$radiusMetres,$lat,$lon);
              node["tourism"~"^(hotel|hostel|museum|attraction|gallery|viewpoint|theme_park)$"](around:$radiusMetres,$lat,$lon);
              node["leisure"~"^(park|sports_centre|stadium)$"](around:$radiusMetres,$lat,$lon);
            );
            out body;
        """.trimIndent()

        return try {
            val body = FormBody.Builder()
                .add("data", query)
                .build()
            val req = Request.Builder()
                .url("https://overpass-api.de/api/interpreter")
                .post(body)
                .build()

            overpassClient.newCall(req).execute().use { resp ->
                if (!resp.isSuccessful) {
                    Log.w(TAG, "Overpass HTTP ${resp.code}")
                    return null
                }

                val responseText = resp.body?.string() ?: return null
                val root = JSONObject(responseText)
                val elements = root.optJSONArray("elements") ?: return null

                val seen    = mutableSetOf<String>()
                val results = mutableListOf<ExplorePlace>()

                for (i in 0 until elements.length()) {
                    try {
                        val el   = elements.getJSONObject(i)
                        val tags = el.optJSONObject("tags") ?: continue

                        val name = tags.optString("name").ifBlank { tags.optString("brand", "") }
                        if (name.isBlank()) continue

                        val elLat = el.optDouble("lat", Double.NaN)
                        val elLon = el.optDouble("lon", Double.NaN)
                        if (elLat.isNaN() || elLon.isNaN()) continue

                        val osmId = "${el.optString("type", "node")}/${el.optLong("id")}"
                        if (!seen.add(osmId)) continue

                        // Determine OSM type and category
                        val (osmType, osmCategory) = when {
                            tags.has("amenity") -> tags.getString("amenity") to "amenity"
                            tags.has("tourism") -> tags.getString("tourism") to "tourism"
                            tags.has("leisure") -> tags.getString("leisure") to "leisure"
                            else -> continue
                        }

                        val distMetres = haversineMetres(lat, lon, elLat, elLon).toInt()
                        val address    = buildDisplayAddress(tags)

                        results.add(
                            ExplorePlace(
                                id             = osmId,
                                name           = name,
                                type           = osmType,
                                category       = osmCategory,
                                lat            = elLat,
                                lon            = elLon,
                                displayAddress = address,
                                distanceMetres = distMetres,
                            )
                        )
                    } catch (_: Exception) { /* skip malformed element */ }
                }

                Log.d(TAG, "Overpass returned ${results.size} places near ($lat,$lon)")
                if (results.isEmpty()) null else results.sortedBy { it.distanceMetres }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Overpass fetch failed: ${e.message}")
            null
        }
    }

    /** Haversine great-circle distance in metres. */
    private fun haversineMetres(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val r = 6_371_000.0 // Earth radius in metres
        val phi1 = Math.toRadians(lat1)
        val phi2 = Math.toRadians(lat2)
        val dPhi = Math.toRadians(lat2 - lat1)
        val dLam = Math.toRadians(lon2 - lon1)
        val a = sin(dPhi / 2).pow(2) + cos(phi1) * cos(phi2) * sin(dLam / 2).pow(2)
        return r * 2 * atan2(sqrt(a), sqrt(1 - a))
    }

    /** Builds a human-readable address from OSM address tags. */
    private fun buildDisplayAddress(tags: JSONObject): String {
        val parts = listOfNotNull(
            tags.optString("addr:housenumber").ifBlank { null },
            tags.optString("addr:street").ifBlank { null },
            tags.optString("addr:suburb").ifBlank { null },
            tags.optString("addr:city").ifBlank { null },
            tags.optString("addr:country").ifBlank { null },
        )
        return parts.joinToString(", ")
    }

    // ── CounterAPI v2 ───────────────────────────────────────────────────────

    /**
     * Increments the places counter on CounterAPI v2.
     * Fails silently if the token or slugs are missing.
     */
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
