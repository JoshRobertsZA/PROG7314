package com.waypoint.app.features.explore.data

import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.GitHubCacheRepository
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.cache.PlacesCacheLocalRepository
import com.waypoint.app.core.cache.PlacesCacheSerializer
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.GuardedApi
import com.waypoint.app.core.network.HttpClient
import com.waypoint.app.core.network.UsageGuardian
import com.waypoint.app.core.secrets.RemoteSecrets
import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONArray
import org.json.JSONObject
import java.net.URLEncoder
import java.time.LocalDate
import java.time.ZoneOffset

// Top-level, not a member of LocationIQRepository - same reason as
// OutboxRepository's coalesce(): pure decision logic with no Android
// dependency, so it can run in this project's plain-JVM unit tests (no
// Robolectric) even though the functions that call it need a real Context.
// Encodes the local -> shared -> live priority order PR 12 commits 2 and 3
// ask for: a cache entry only wins over a live fetch while it is both
// present and not stale/empty.
internal fun isUsablePlacesCache(cache: PlacesCache?): Boolean =
    cache != null && !cache.isStale() && cache.places.isNotEmpty()

// Last resort when neither cache was usable and the live fetch also failed:
// prefer whatever is in the account's own local cache (even stale) over the
// shared cache, since it's the one closer to what this user last saw.
internal fun chooseStalePlacesFallback(local: PlacesCache?, shared: PlacesCache?): PlacesCache? =
    local ?: shared

/**
 * Nearby places and reverse geocoding from LocationIQ, cached per city on the phone and in the shared
 * API cache to stay within the free quota.
 */
object LocationIQRepository {

    private const val TAG = "LocationIQRepo"
    private const val NEARBY_RADIUS_METRES = 15000
    private const val NEARBY_REQUESTS = 6
    private const val REFRESH_REQUESTS = NEARBY_REQUESTS + 1

    fun citySlug(city: String): String =
        city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")

    fun placesPath(citySlug: String) = "places_${citySlug}"


    // Local (per-user, per-query) cache first, then the cross-user shared
    // cache, then a live LocationIQ fetch (PR 12 commits 2-4). A fresh fetch
    // is only ever written back to the local cache: one user's exact GPS fix
    // or search term is a poor fit for the cache every user shares through
    // the API (unlike weather/currency), so the app no longer writes places
    // results there at all - it only still reads it, as a fallback.
    suspend fun getPlaces(context: Context, city: String): PlacesCache? {
        val slug = citySlug(city)
        val path = placesPath(slug)
        val accountId = SessionManager.accountId

        val local = readFromLocalCache(context, accountId, path)
        if (isUsablePlacesCache(local)) {
            Log.d(TAG, "Local cache hit: $city (${local!!.places.size} places)")
            return local
        }

        val shared = readFromSharedCache(path)
        if (isUsablePlacesCache(shared)) {
            Log.d(TAG, "Shared cache hit: $city (${shared!!.places.size} places)")
            writeToLocalCache(context, accountId, path, shared)
            return shared
        }

        val fresh = fetchFromApi(city, slug)
        if (fresh != null) {
            writeToLocalCache(context, accountId, path, fresh)
            return fresh
        }

        val stale = chooseStalePlacesFallback(local, shared)
        if (stale != null) {
            Log.d(TAG, "Returning stale cache for $city after API failure")
        }
        return stale
    }

    // Same local -> shared -> live order as getPlaces, keyed by coordinates
    // instead of a city name.
    suspend fun getPlacesByCoords(context: Context, lat: Double, lon: Double): PlacesCache? {
        val slug = coordSlug(lat, lon)
        val path = placesPath(slug)
        val accountId = SessionManager.accountId

        val local = readFromLocalCache(context, accountId, path)
        if (isUsablePlacesCache(local)) {
            Log.d(TAG, "Local cache hit: $slug (${local!!.places.size} places)")
            return local
        }

        val shared = readFromSharedCache(path)
        if (isUsablePlacesCache(shared)) {
            Log.d(TAG, "Shared cache hit: $slug (${shared!!.places.size} places)")
            writeToLocalCache(context, accountId, path, shared)
            return shared
        }

        val stale = chooseStalePlacesFallback(local, shared)

        val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
        if (key.isBlank()) return stale

        if (!UsageGuardian.isAllowed(GuardedApi.LOCATIONIQ, NEARBY_REQUESTS)) {
            Log.w(TAG, "LocationIQ budget used up -- skipping live fetch for $slug")
            return stale
        }
        UsageGuardian.recordUse(GuardedApi.LOCATIONIQ, NEARBY_REQUESTS)

        val places = withContext(Dispatchers.IO) { fetchNearby(lat, lon, key) }
        if (places != null) {
            val fresh = PlacesCache(
                city           = slug,
                fetchedDateUtc = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString(),
                places         = places,
            )
            writeToLocalCache(context, accountId, path, fresh)
            return fresh
        }

        return stale
    }

    suspend fun reverseGeocode(lat: Double, lon: Double, key: String): String? =
        withContext(Dispatchers.IO) {
            try {
                val url = "https://us1.locationiq.com/v1/reverse" +
                          "?key=$key&lat=$lat&lon=$lon&format=json"
                if (!UsageGuardian.isAllowed(GuardedApi.LOCATIONIQ)) return@withContext null
                UsageGuardian.recordUse(GuardedApi.LOCATIONIQ)
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

    private fun coordSlug(lat: Double, lon: Double) =
        "%.2f_%.2f".format(lat, lon).replace('-', 'n').replace('.', 'd')


    // Read-only: nothing in this file writes to the shared cache any more
    // (PR 12 commit 4). GitHubCacheRepository.readJson already returns null
    // on a network/HTTP failure, which is what lets getPlaces/getPlacesByCoords
    // fall through to a live LocationIQ fetch when the shared cache is
    // unreachable (PR 12 commit 3).
    private suspend fun readFromSharedCache(path: String): PlacesCache? {
        val json = GitHubCacheRepository.readJson(path) ?: return null
        return PlacesCacheSerializer.fromJson(json).also {
            if (it == null) Log.w(TAG, "Cache parse error for $path")
        }
    }

    private suspend fun readFromLocalCache(context: Context, accountId: String, path: String): PlacesCache? =
        withContext(Dispatchers.IO) { PlacesCacheLocalRepository(context).get(accountId, path) }

    private suspend fun writeToLocalCache(context: Context, accountId: String, path: String, data: PlacesCache) =
        withContext(Dispatchers.IO) { PlacesCacheLocalRepository(context).put(accountId, path, data) }


    private suspend fun fetchFromApi(city: String, slug: String): PlacesCache? =
        withContext(Dispatchers.IO) {
            val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
            if (key.isBlank()) {
                Log.w(TAG, "No LocationIQ key available")
                return@withContext null
            }

            if (!UsageGuardian.isAllowed(GuardedApi.LOCATIONIQ, REFRESH_REQUESTS)) {
                Log.w(TAG, "LocationIQ budget used up -- skipping live fetch for $city")
                return@withContext null
            }
            UsageGuardian.recordUse(GuardedApi.LOCATIONIQ, REFRESH_REQUESTS)

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

    suspend fun geocodeCity(city: String): Pair<Double, Double>? {
        val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
        if (key.isBlank()) return null
        if (!UsageGuardian.isAllowed(GuardedApi.LOCATIONIQ)) return null
        UsageGuardian.recordUse(GuardedApi.LOCATIONIQ)
        return withContext(Dispatchers.IO) { geocodeCityWithKey(city, key) }
    }

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

    private fun fetchNearby(lat: Double, lon: Double, key: String): List<ExplorePlace>? {
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
}
