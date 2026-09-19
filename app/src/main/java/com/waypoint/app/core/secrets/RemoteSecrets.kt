// declares that this file belongs to the package `com.waypoint.app.core.secrets`
package com.waypoint.app.core.secrets

// imports `android.util.Base64` for use in this file
import android.util.Base64
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.sync.Mutex` for use in this file
import kotlinx.coroutines.sync.Mutex
// imports `kotlinx.coroutines.sync.withLock` for use in this file
import kotlinx.coroutines.sync.withLock
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.OkHttpClient` for use in this file
import okhttp3.OkHttpClient
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject
// imports `retrofit2.Retrofit` for use in this file
import retrofit2.Retrofit
// imports `retrofit2.converter.scalars.ScalarsConverterFactory` for use in this file
import retrofit2.converter.scalars.ScalarsConverterFactory
// imports `retrofit2.http.GET` for use in this file
import retrofit2.http.GET
// imports `retrofit2.http.Header` for use in this file
import retrofit2.http.Header
// imports `retrofit2.http.Path` for use in this file
import retrofit2.http.Path

// declares object `RemoteSecrets` and opens its body
object RemoteSecrets {

    // declares private const read-only property `CONTENTS_PATH`, initialised to the string literal "secrets/keys.json"
    private const val CONTENTS_PATH = "secrets/keys.json"
    // declares private const read-only property `TAG`, initialised to the string literal "RemoteSecrets"
    private const val TAG = "RemoteSecrets"

    // declares private interface `GitHubContentsApi` and opens its body
    private interface GitHubContentsApi {
        // annotation `@GET` with arguments `("repos/{owner}/{repo}/contents/{path}")` applied to the declaration that follows
        @GET("repos/{owner}/{repo}/contents/{path}")
        // expression: `suspend fun getFileMetadata(`
        suspend fun getFileMetadata(
            // continues the statement started above: `@Path("owner") owner: String,`
            @Path("owner") owner: String,
            // continues the statement started above: `@Path("repo") repo: String,`
            @Path("repo") repo: String,
            // continues the statement started above: `@Path("path") path: String,`
            @Path("path") path: String,
            // continues the statement started above: `@Header("Authorization") authorization: String,`
            @Header("Authorization") authorization: String,
        // continues the statement started above: `): String`
        ): String
    // closes the interface `GitHubContentsApi`
    }

    // declares private read-only property `bootstrapToken` of type `String`, delegated to `lazy` and opens a lambda / block
    private val bootstrapToken: String by lazy {
        // calls `listOf` with an argument list that continues on the following lines
        listOf(
            // continues the statement started above: `BuildConfig.regionSeed,`
            BuildConfig.regionSeed,
            // continues the statement started above: `BuildConfig.cacheEpoch,`
            BuildConfig.cacheEpoch,
            // continues the statement started above: `BuildConfig.deviceClassTag,`
            BuildConfig.deviceClassTag,
            // continues the statement started above: `BuildConfig.syncNonce,`
            BuildConfig.syncNonce,
            // continues the statement started above: `BuildConfig.featureGateId,`
            BuildConfig.featureGateId,
            // continues the statement started above: `BuildConfig.telemetryPrefix,`
            BuildConfig.telemetryPrefix,
            // continues the statement started above: `BuildConfig.sessionSlot,`
            BuildConfig.sessionSlot,
            // continues the statement started above: `BuildConfig.buildFingerprint,`
            BuildConfig.buildFingerprint,
        // continues the statement started above: `).joinToString("")`
        ).joinToString("")
    // closes the lambda assigned to `bootstrapToken`
    }

    // declares private read-only property `mutex`, initialised with the result of calling `Mutex(…)`
    private val mutex = Mutex()
    // declares private mutable property `cache` of type `Map<String, String>?`, initialised to null
    private var cache: Map<String, String>? = null
    // declares private mutable property `loadFailed`, initialised to false
    private var loadFailed = false

    // declares private read-only property `api` of type `GitHubContentsApi`, delegated to `lazy` and opens a lambda / block
    private val api: GitHubContentsApi by lazy {
        // calls `Builder` on `Retrofit` with arguments `()`
        Retrofit.Builder()
            // chained call `.baseUrl` on the previous result with arguments `("https://api.github.com/")`
            .baseUrl("https://api.github.com/")
            // chained call `.client` on the previous result with arguments `(OkHttpClient.Builder().build())`
            .client(OkHttpClient.Builder().build())
            // chained call `.addConverterFactory` on the previous result with arguments `(ScalarsConverterFactory.create())`
            .addConverterFactory(ScalarsConverterFactory.create())
            // chained call `.build` on the previous result
            .build()
            // chained call `.create` on the previous result with arguments `(GitHubContentsApi::class.java)`
            .create(GitHubContentsApi::class.java)
    // closes the lambda assigned to `api`
    }

    // declares suspend function `ensureLoaded` taking no parameters and opens its body
    suspend fun ensureLoaded() {
        // `if` statement: executes `return` when `cache != null || loadFailed` is true
        if (cache != null || loadFailed) return
        // opens a block after `mutex.withLock`
        mutex.withLock {
            // `if` statement: executes `return` when `cache != null || loadFailed` is true
            if (cache != null || loadFailed) return
            // assigns `cache` the value `try {`
            cache = try {
                // calls `withContext` with arguments `(Dispatchers.IO)`, then chains `.also { result ->`
                withContext(Dispatchers.IO) { fetchAndDecode() }.also { result ->
                    // continues the statement started above: `if (BuildConfig.DEBUG) {`
                    if (BuildConfig.DEBUG) {
                        // calls `d` on `Log` with arguments `(TAG, "Remote secrets loaded: ${result.keys}")`
                        Log.d(TAG, "Remote secrets loaded: ${result.keys}")
                    // closes the block
                    }
                // closes the block
                }
            // `catch` block: handles a thrown `Exception` bound to `e`
            } catch (e: Exception) {
                // assigns `loadFailed` the value `true`
                loadFailed = true
                // `if` statement: the block below runs when `BuildConfig.DEBUG` is true
                if (BuildConfig.DEBUG) {
                    // calls `w` on `Log` with arguments `(TAG, "Remote secrets fetch failed: ${e.javaC…)`
                    Log.w(TAG, "Remote secrets fetch failed: ${e.javaClass.simpleName}: ${e.message}")
                // closes the if block
                }
                // expression: `null`
                null
            // closes the catch block
            }
        // closes the block
        }
    // closes the function `ensureLoaded`
    }

    // declares function `get` taking 2 parameters (`keyName`, `localValue`), returning `String` and opens its body
    fun get(keyName: String, localValue: String): String {
        // `if` statement: executes `return localValue` when `localValue.isNotBlank()` is true
        if (localValue.isNotBlank()) return localValue
        // returns `cache?.get(keyName).orEmpty()` from the current function
        return cache?.get(keyName).orEmpty()
    // closes the function `get`
    }

    // declares private suspend function `fetchAndDecode` taking no parameters, returning `Map<String, String>` and opens its body
    private suspend fun fetchAndDecode(): Map<String, String> {
        // declares read-only property `responseJson`, initialised with the result of calling `api.getFileMetadata(…)`
        val responseJson = api.getFileMetadata(
            // continues the statement started above: `owner = BuildConfig.GITHUB_OWNER,`
            owner = BuildConfig.GITHUB_OWNER,
            // continues the statement started above: `repo = BuildConfig.GITHUB_REPO,`
            repo = BuildConfig.GITHUB_REPO,
            // continues the statement started above: `path = CONTENTS_PATH,`
            path = CONTENTS_PATH,
            // continues the statement started above: `authorization = "Bearer $bootstrapToken",`
            authorization = "Bearer $bootstrapToken",
        // closes the multi-line argument list started above
        )

        // declares read-only property `contentField`, initialised with the result of calling `JSONObject(…)`
        val contentField = JSONObject(responseJson)
            // chained call `.getString` on the previous result with arguments `("content")`
            .getString("content")
            // chained call `.replace` on the previous result with arguments `("\n", "")`
            .replace("\n", "")

        // declares read-only property `onceDecoded`, initialised with the result of calling `String(…)`
        val onceDecoded = String(Base64.decode(contentField, Base64.DEFAULT))
        // declares read-only property `twiceDecoded`, initialised with the result of calling `String(…)`
        val twiceDecoded = String(Base64.decode(onceDecoded, Base64.DEFAULT))

        // declares read-only property `json`, initialised with the result of calling `JSONObject(…)`
        val json = JSONObject(twiceDecoded)
        // declares read-only property `result`, initialised with the result of calling `mutableMapOf(…)`
        val result = mutableMapOf<String, String>()
        // lambda `json.keys().forEach { key -> result[key] = json.getString(…`
        json.keys().forEach { key -> result[key] = json.getString(key) }
        // returns `result` from the current function
        return result
    // closes the function `fetchAndDecode`
    }
// closes the object `RemoteSecrets`
}
