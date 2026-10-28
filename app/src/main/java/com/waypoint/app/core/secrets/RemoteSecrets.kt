package com.waypoint.app.core.secrets

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.network.HttpClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject

// Was a direct GitHub Contents API fetch using a token chunked across 8
// BuildConfig fields and embedded in every APK (the PR 1 audit's main
// finding) - now fetches through this app's own API instead. See
// api/src/routes/config.js for the two endpoints this calls.
object RemoteSecrets {

    private const val TAG = "RemoteSecrets"
    private const val PREFS_NAME = "remote_secrets_cache"
    private const val PREFS_KEY_KEYS_JSON = "cached_keys_json"
    private const val PREFS_KEY_WEB_CLIENT_ID = "cached_web_client_id"

    private val BASE_URL get() = BuildConfig.WAYPOINT_API_BASE_URL.trimEnd('/')

    private lateinit var appContext: Context
    private val mutex = Mutex()
    private var cache: Map<String, String>? = null
    private var loadFailed = false

    // Called once from WaypointApp.onCreate() - needed before any of the
    // functions below can touch SharedPreferences for the local cache.
    fun init(context: Context) {
        appContext = context.applicationContext
    }

    // FIREBASE_WEB_CLIENT_ID is not actually a secret - Google's own
    // Sign-In docs describe the web client id as a public identifier meant
    // to be embedded in app config, unlike the quota-bound third-party API
    // keys below. It also has to be available before the user has signed
    // in at all (GoogleSignInHelper needs it to start the sign-in flow in
    // the first place), so it's served by its own unauthenticated endpoint
    // rather than folded into the auth-gated bulk fetch in ensureLoaded().
    suspend fun ensureWebClientIdLoaded(): String = withContext(Dispatchers.IO) {
        val cachedLocal = prefs().getString(PREFS_KEY_WEB_CLIENT_ID, null)
        try {
            val req = Request.Builder().url("$BASE_URL/config/web-client-id").get().build()
            val resp = HttpClient.instance.newCall(req).execute()
            if (!resp.isSuccessful) {
                Log.w(TAG, "GET /config/web-client-id HTTP ${resp.code}")
                return@withContext cachedLocal.orEmpty()
            }
            val value = JSONObject(resp.body?.string() ?: "{}").optString("FIREBASE_WEB_CLIENT_ID")
            if (value.isNotBlank()) {
                prefs().edit().putString(PREFS_KEY_WEB_CLIENT_ID, value).apply()
                return@withContext value
            }
            cachedLocal.orEmpty()
        } catch (e: Exception) {
            Log.w(TAG, "web-client-id fetch failed: ${e.message}")
            cachedLocal.orEmpty()
        }
    }

    // The quota-bound third-party keys (LocationIQ, AirLabs, OpenWeather,
    // ExchangeRate, CounterAPI). Requires a signed-in user, since
    // GET /config/keys is authenticated - this is why it's a separate call
    // from ensureWebClientIdLoaded() rather than one combined fetch.
    suspend fun ensureLoaded() {
        if (cache != null || loadFailed) return
        mutex.withLock {
            if (cache != null || loadFailed) return

            val idToken = AuthRepository.getIdToken()
            if (idToken == null) {
                // Not signed in yet (or the token refresh failed) - fall
                // back to whatever was cached from a previous successful
                // fetch, if any, rather than leaving every key blank.
                cache = loadFromPrefs()
                return
            }

            cache = try {
                withContext(Dispatchers.IO) { fetchFromApi(idToken) }.also { result ->
                    saveToPrefs(result)
                    if (BuildConfig.DEBUG) Log.d(TAG, "Remote secrets loaded: ${result.keys}")
                }
            } catch (e: Exception) {
                if (BuildConfig.DEBUG) {
                    Log.w(TAG, "Remote secrets fetch failed: ${e.javaClass.simpleName}: ${e.message}")
                }
                // Network/API down - fall back to the local cache so the
                // app still mostly works offline after at least one
                // successful fetch, rather than failing closed every time.
                val fromPrefs = loadFromPrefs()
                if (fromPrefs == null) loadFailed = true
                fromPrefs
            }
        }
    }

    fun get(keyName: String, localValue: String): String {
        if (localValue.isNotBlank()) return localValue
        return cache?.get(keyName).orEmpty()
    }

    private fun fetchFromApi(idToken: String): Map<String, String> {
        val req = Request.Builder()
            .url("$BASE_URL/config/keys")
            .header("Authorization", "Bearer $idToken")
            .get()
            .build()
        val resp = HttpClient.instance.newCall(req).execute()
        if (!resp.isSuccessful) throw java.io.IOException("GET /config/keys HTTP ${resp.code}")
        val json = JSONObject(resp.body?.string() ?: "{}")
        val result = mutableMapOf<String, String>()
        json.keys().forEach { key -> result[key] = json.getString(key) }
        return result
    }

    private fun prefs(): SharedPreferences =
        appContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private fun loadFromPrefs(): Map<String, String>? {
        val raw = prefs().getString(PREFS_KEY_KEYS_JSON, null) ?: return null
        return try {
            val json = JSONObject(raw)
            val result = mutableMapOf<String, String>()
            json.keys().forEach { key -> result[key] = json.getString(key) }
            result
        } catch (e: Exception) {
            null
        }
    }

    private fun saveToPrefs(map: Map<String, String>) {
        val json = JSONObject()
        map.forEach { (key, value) -> json.put(key, value) }
        prefs().edit().putString(PREFS_KEY_KEYS_JSON, json.toString()).apply()
    }
}
