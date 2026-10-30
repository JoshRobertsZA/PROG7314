// declares that this file belongs to the package `com.waypoint.app.features.explore.data`
package com.waypoint.app.features.explore.data

// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.cache.GitHubCacheRepository` for use in this file
import com.waypoint.app.core.cache.GitHubCacheRepository
// imports `com.waypoint.app.core.cache.PlacesCache` for use in this file
import com.waypoint.app.core.cache.PlacesCache
import com.waypoint.app.core.cache.PlacesCacheLocalRepository
import com.waypoint.app.core.cache.PlacesCacheSerializer
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.core.network.HttpClient` for use in this file
import com.waypoint.app.core.network.HttpClient
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONArray` for use in this file
import org.json.JSONArray
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject
// imports `java.net.URLEncoder` for use in this file
import java.net.URLEncoder
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.ZoneOffset` for use in this file
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

// declares object `LocationIQRepository` and opens its body
object LocationIQRepository {

    // declares private const read-only property `TAG`, initialised to the string literal "LocationIQRepo"
    private const val TAG = "LocationIQRepo"
    // declares private const read-only property `NEARBY_RADIUS_METRES`, initialised to the number 15000
    private const val NEARBY_RADIUS_METRES = 15000

    // declares function `citySlug` taking 1 parameter (`city`), returning `String`; its body is the expression ``
    fun citySlug(city: String): String =
        // continues the statement started above: `city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")`
        city.trim().lowercase().replace(Regex("[^a-z0-9]+"), "_")

    // declares function `placesPath` taking 1 parameter (`citySlug`); its body is the expression `"places_${citySlug}"`
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
            incrementCounter()
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

        val places = withContext(Dispatchers.IO) { fetchNearby(lat, lon, key) }
        if (places != null) {
            val fresh = PlacesCache(
                city           = slug,
                fetchedDateUtc = java.time.LocalDate.now(java.time.ZoneOffset.UTC).toString(),
                places         = places,
            )
            writeToLocalCache(context, accountId, path, fresh)
            incrementCounter()
            return fresh
        }

        return stale
    }

    // declares suspend function `reverseGeocode` taking 3 parameters (`lat`, `lon`, `key`), returning `String?`; its body is the expression ``
    suspend fun reverseGeocode(lat: Double, lon: Double, key: String): String? =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // declares read-only property `url`, initialised to the string literal "https://us1.locationiq.com/v1/reverse"…
                val url = "https://us1.locationiq.com/v1/reverse" +
                          // continues the statement started above: `"?key=$key&lat=$lat&lon=$lon&format=json"`
                          "?key=$key&lat=$lat&lon=$lon&format=json"
                // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
                val req = Request.Builder().url(url).get().build()
                // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
                HttpClient.instance.newCall(req).execute().use { resp ->
                    // continues the statement started above: `if (!resp.isSuccessful) return@withContext null`
                    if (!resp.isSuccessful) return@withContext null
                    // declares read-only property `obj`, initialised with the result of calling `JSONObject(…)`
                    val obj  = JSONObject(resp.body?.string() ?: return@withContext null)
                    // declares read-only property `addr`, initialised with the result of calling `obj.optJSONObject(…)`
                    val addr = obj.optJSONObject("address") ?: return@withContext null
                    // declares read-only property `suburb`, initialised with the result of calling `addr.optString(…)`
                    val suburb = addr.optString("suburb").ifBlank { null }
                    // declares read-only property `city`, initialised with the result of calling `addr.optString(…)` and opens a lambda / block
                    val city   = addr.optString("city").ifBlank {
                        // calls `optString` on `addr` with arguments `("town")`, then chains `.ifBlank` and opens a trailing lambda / block
                        addr.optString("town").ifBlank {
                            // calls `optString` on `addr` with arguments `("county")`, then chains `.ifBlank { null }`
                            addr.optString("county").ifBlank { null }
                        // closes the lambda passed to `ifBlank`
                        }
                    // closes the lambda assigned to `city`
                    }
                    // calls `listOfNotNull` with arguments `(suburb, city)`, then chains `.joinToString(", ")`, `.ifBlank { null }`
                    listOfNotNull(suburb, city).joinToString(", ").ifBlank { null }
                // closes the block
                }
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "Reverse geocode failed: ${e.message}")`
                Log.w(TAG, "Reverse geocode failed: ${e.message}")
                // expression: `null`
                null
            // closes the catch block
            }
        // closes the block
        }

    // declares private function `coordSlug` taking 2 parameters (`lat`, `lon`); its body is the expression ``
    private fun coordSlug(lat: Double, lon: Double) =
        // continues the statement started above: `"%.2f_%.2f".format(lat, lon).replace('-', 'n').replace('.',…`
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


    // declares private suspend function `fetchFromApi` taking 2 parameters (`city`, `slug`), returning `PlacesCache?`; its body is the expression ``
    private suspend fun fetchFromApi(city: String, slug: String): PlacesCache? =
        // continues the statement started above: `withContext(Dispatchers.IO) {`
        withContext(Dispatchers.IO) {
            // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
            val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
            // `if` statement: the block below runs when `key.isBlank()` is true
            if (key.isBlank()) {
                // calls `w` on `Log` with arguments `(TAG, "No LocationIQ key available")`
                Log.w(TAG, "No LocationIQ key available")
                // expression: `return@withContext null`
                return@withContext null
            // closes the if block
            }

            // declares read-only property `coords`, initialised with the result of calling `geocodeCityWithKey(…)`
            val coords = geocodeCityWithKey(city, key) ?: return@withContext null
            // calls `val` with arguments `(lat, lon)`
            val (lat, lon) = coords

            // declares read-only property `places`, initialised with the result of calling `withContext(…)`
            val places = withContext(Dispatchers.IO) { fetchNearby(lat, lon, key) }
            // `if` statement: executes `return@withContext null` when `places == null` is true
            if (places == null) return@withContext null

            // calls `PlacesCache` with an argument list that continues on the following lines
            PlacesCache(
                // continues the statement started above: `city = slug,`
                city           = slug,
                // continues the statement started above: `fetchedDateUtc = LocalDate.now(ZoneOffset.UTC).toString(),`
                fetchedDateUtc = LocalDate.now(ZoneOffset.UTC).toString(),
                // continues the statement started above: `places = places,`
                places         = places,
            // closes the multi-line argument list started above
            )
        // closes the block
        }

    // declares suspend function `geocodeCity` taking 1 parameter (`city`), returning `Pair<Double, Double>?` and opens its body
    suspend fun geocodeCity(city: String): Pair<Double, Double>? {
        // declares read-only property `key`, initialised with the result of calling `RemoteSecrets.get(…)`
        val key = RemoteSecrets.get("LOCATIONIQ_API_KEY", BuildConfig.LOCATIONIQ_API_KEY)
        // `if` statement: executes `return null` when `key.isBlank()` is true
        if (key.isBlank()) return null
        // returns `withContext(Dispatchers.IO) { geocodeCityWithKey(city, key) }` from the current function
        return withContext(Dispatchers.IO) { geocodeCityWithKey(city, key) }
    // closes the function `geocodeCity`
    }

    // declares private function `geocodeCityWithKey` taking 2 parameters (`city`, `key`), returning `Pair<Double, Double>?` and opens its body
    private fun geocodeCityWithKey(city: String, key: String): Pair<Double, Double>? {
        // returns `try {` from the current function
        return try {
            // declares read-only property `encoded`, initialised with the result of calling `URLEncoder.encode(…)`
            val encoded = URLEncoder.encode(city.trim(), "UTF-8")
            // declares read-only property `url`, initialised to the string literal "https://us1.locationiq.com/v1/search" +
            val url = "https://us1.locationiq.com/v1/search" +
                      // continues the statement started above: `"?key=$key&q=$encoded&format=json&limit=1"`
                      "?key=$key&q=$encoded&format=json&limit=1"
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder().url(url).get().build()
            // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
            HttpClient.instance.newCall(req).execute().use { resp ->
                // continues the statement started above: `if (!resp.isSuccessful) {`
                if (!resp.isSuccessful) {
                    // calls `w` on `Log` with arguments `(TAG, "Geocode HTTP ${resp.code} for $city")`
                    Log.w(TAG, "Geocode HTTP ${resp.code} for $city")
                    // returns `null` from the current function
                    return null
                // closes the block
                }
                // declares read-only property `arr`, initialised with the result of calling `JSONArray(…)`
                val arr = JSONArray(resp.body?.string() ?: return null)
                // `if` statement: executes `return null` when `arr.length() == 0` is true
                if (arr.length() == 0) return null
                // declares read-only property `first`, initialised with the result of calling `arr.getJSONObject(…)`
                val first = arr.getJSONObject(0)
                // calls `Pair` with arguments `(first.getString("lat").toDouble(), first.get…)`
                Pair(first.getString("lat").toDouble(), first.getString("lon").toDouble())
            // closes the block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Geocode failed for $city: ${e.message}")`
            Log.w(TAG, "Geocode failed for $city: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the function `geocodeCityWithKey`
    }

    // declares private function `fetchNearby` taking 3 parameters (`lat`, `lon`, `key`), returning `List<ExplorePlace>?` and opens its body
    private fun fetchNearby(lat: Double, lon: Double, key: String): List<ExplorePlace>? {
        // declares read-only property `tags`, initialised with the result of calling `listOf(…)`
        val tags = listOf("restaurant", "cafe", "hotel", "pub", "cinema", "park")

        // declares read-only property `seen`, initialised with the result of calling `mutableSetOf(…)`
        val seen    = mutableSetOf<String>()
        // declares read-only property `results`, initialised with the result of calling `mutableListOf(…)`
        val results = mutableListOf<ExplorePlace>()
        // declares mutable property `anySucceeded`, initialised to false
        var anySucceeded = false

        // `for` loop with header `(index, tag) in tags.withIndex()`
        for ((index, tag) in tags.withIndex()) {
            // `if` statement: executes `Thread.sleep(400)` when `index > 0` is true
            if (index > 0) Thread.sleep(400)
            // `try` block: exceptions thrown inside are handled by the `catch` below
            try {
                // declares read-only property `url`, initialised to the string literal "https://us1.locationiq.com/v1/nearby" +
                val url = "https://us1.locationiq.com/v1/nearby" +
                          // continues the statement started above: `"?key=$key" +`
                          "?key=$key" +
                          // continues the statement started above: `"&lat=$lat" +`
                          "&lat=$lat" +
                          // continues the statement started above: `"&lon=$lon" +`
                          "&lon=$lon" +
                          // continues the statement started above: `"&tag=$tag" +`
                          "&tag=$tag" +
                          // continues the statement started above: `"&radius=$NEARBY_RADIUS_METRES" +`
                          "&radius=$NEARBY_RADIUS_METRES" +
                          // continues the statement started above: `"&format=json"`
                          "&format=json"
                // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
                val req = Request.Builder().url(url).get().build()
                // calls `Call` on `HttpClient.instance` with arguments `(req)`, then chains `.execute()`, `.use { resp ->`
                HttpClient.instance.newCall(req).execute().use { resp ->
                    // continues the statement started above: `if (!resp.isSuccessful) {`
                    if (!resp.isSuccessful) {
                        // calls `w` on `Log` with arguments `(TAG, "Nearby[$tag] HTTP ${resp.code}")`
                        Log.w(TAG, "Nearby[$tag] HTTP ${resp.code}")
                        // expression: `return@use`
                        return@use
                    // closes the block
                    }
                    // assigns `anySucceeded` the value `true`
                    anySucceeded = true
                    // declares read-only property `body`, initialised to `resp.body?.string() ?: return@use`
                    val body = resp.body?.string() ?: return@use
                    // declares read-only property `arr`, initialised with the result of calling `JSONArray(…)`
                    val arr  = JSONArray(body)
                    // calls `d` on `Log` with arguments `(TAG, "Nearby[$tag] returned ${arr.length()} …)`
                    Log.d(TAG, "Nearby[$tag] returned ${arr.length()} results")
                    // `for` loop: iterates over `0 until arr.length()`, binding each element to `i`
                    for (i in 0 until arr.length()) {
                        // `try` block: exceptions thrown inside are handled by the `catch` below
                        try {
                            // declares read-only property `p`, initialised with the result of calling `arr.getJSONObject(…)`
                            val p       = arr.getJSONObject(i)
                            // declares read-only property `rawName`, initialised with the result of calling `p.optString(…)`
                            val rawName = p.optString("name").ifBlank { p.optString("display_name", "") }
                            // `if` statement: executes `continue` when `rawName.isBlank()` is true
                            if (rawName.isBlank()) continue
                            // declares read-only property `id`, initialised with the result of calling `p.optString(…)`
                            val id = p.optString("place_id", "${tag}_$i")
                            // `if` statement: executes `continue` when `!seen.add(id)` is true
                            if (!seen.add(id)) continue
                            // calls `add` on `results` with an argument list that continues on the following lines
                            results.add(
                                // continues the statement started above: `ExplorePlace(`
                                ExplorePlace(
                                    // continues the statement started above: `id = id,`
                                    id             = id,
                                    // continues the statement started above: `name = rawName.lines().first().trim(),`
                                    name           = rawName.lines().first().trim(),
                                    // continues the statement started above: `type = p.optString("type", tag),`
                                    type           = p.optString("type", tag),
                                    // continues the statement started above: `category = p.optString("class", ""),`
                                    category       = p.optString("class", ""),
                                    // continues the statement started above: `lat = p.getString("lat").toDouble(),`
                                    lat            = p.getString("lat").toDouble(),
                                    // continues the statement started above: `lon = p.getString("lon").toDouble(),`
                                    lon            = p.getString("lon").toDouble(),
                                    // continues the statement started above: `displayAddress = p.optString("display_name", ""),`
                                    displayAddress = p.optString("display_name", ""),
                                    // continues the statement started above: `distanceMetres = p.optInt("distance", 0),`
                                    distanceMetres = p.optInt("distance", 0),
                                // closes the multi-line argument list started above
                                )
                            // closes the multi-line argument list started above
                            )
                        // expression: `} catch (_: Exception) { }`
                        } catch (_: Exception) { }
                    // closes the for loop
                    }
                // closes the block
                }
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // calls `w` on `Log` with arguments `(TAG, "Nearby[$tag] failed: ${e.message}")`
                Log.w(TAG, "Nearby[$tag] failed: ${e.message}")
            // closes the catch block
            }
        // closes the for loop
        }

        // `if` statement: executes `return null` when `!anySucceeded && results.isEmpty()` is true
        if (!anySucceeded && results.isEmpty()) return null
        // returns `results.sortedBy { it.distanceMetres }` from the current function
        return results.sortedBy { it.distanceMetres }
    // closes the function `fetchNearby`
    }


    // declares private suspend function `incrementCounter` taking no parameters; its body is the expression `withContext(Dispatchers.IO) {`
    private suspend fun incrementCounter() = withContext(Dispatchers.IO) {
        // declares read-only property `token`, initialised with the result of calling `RemoteSecrets.get(…)`
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        // declares read-only property `workspace`, initialised with the result of calling `RemoteSecrets.get(…)`
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        // declares read-only property `counter`, initialised with the result of calling `RemoteSecrets.get(…)`
        val counter = RemoteSecrets.get("COUNTERAPI_PLACES_SLUG", BuildConfig.COUNTERAPI_PLACES_SLUG)

        // `if` statement: the block below runs when `token.isBlank() || workspace.isBlank() || counter.isBlank()` is true
        if (token.isBlank() || workspace.isBlank() || counter.isBlank()) {
            // calls `d` on `Log` with arguments `(TAG, "CounterAPI not configured -- skipping …)`
            Log.d(TAG, "CounterAPI not configured -- skipping increment")
            // expression: `return@withContext`
            return@withContext
        // closes the if block
        }

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `url`, initialised to the string literal "https://api.counterapi.dev/v2/$workspa…
            val url = "https://api.counterapi.dev/v2/$workspace/$counter/up"
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `(url)`
                .url(url)
                // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $token")`
                .header("Authorization", "Bearer $token")
                // chained call `.get` on the previous result
                .get()
                // chained call `.build` on the previous result
                .build()
            // declares read-only property `resp`, initialised with the result of calling `HttpClient.instance.newCall(…)`
            val resp = HttpClient.instance.newCall(req).execute()
            // lambda `Log.d(TAG, "CounterAPI increm… -> HTTP ${resp.code}")`
            Log.d(TAG, "CounterAPI increment -> HTTP ${resp.code}")
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "CounterAPI increment failed: ${e.messa…)`
            Log.w(TAG, "CounterAPI increment failed: ${e.message}")
        // closes the catch block
        }
    // closes the block
    }
// closes the object `LocationIQRepository`
}
