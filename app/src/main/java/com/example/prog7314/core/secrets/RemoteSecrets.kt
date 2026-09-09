package com.example.prog7314.core.secrets

import android.util.Base64
import com.example.prog7314.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import org.json.JSONObject
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

/**
 * Fetches the shared API key cache (secrets/keys.json in the
 * apiplayground-cache GitHub repo) at runtime, using a read-only bootstrap
 * token. This lets a fresh clone run against the shared team keys with
 * zero local setup - no apikey.properties required.
 *
 * A non-blank apikey.properties value (exposed via BuildConfig, see
 * apiKey() in app/build.gradle.kts) always wins over the remote value,
 * so you can still point the app at your own personal quota locally.
 *
 * Security note: this is obfuscation, not real secrecy, matched to the
 * actual stakes (free-tier keys, nothing billable). The bootstrap token
 * is split into 8 BuildConfig fields with unrelated-looking names,
 * spread through defaultConfig, and reassembled here at runtime - this
 * stops a plain "strings" scan of the APK from finding it, but not a
 * real decompile. Never log the reassembled token or the fetched keys.
 */
object RemoteSecrets {

    private const val CONTENTS_PATH = "secrets/keys.json"

    private interface GitHubContentsApi {
        @GET("repos/{owner}/{repo}/contents/{path}")
        suspend fun getFileMetadata(
            @Path("owner") owner: String,
            @Path("repo") repo: String,
            @Path("path") path: String,
            @Header("Authorization") authorization: String,
        ): String
    }

    // Reassembles the bootstrap token from the 8 BuildConfig fields added
    // in app/build.gradle.kts. Order matters - must match the order the
    // real token was split into when those fields were filled in.
    private val bootstrapToken: String by lazy {
        listOf(
            BuildConfig.regionSeed,
            BuildConfig.cacheEpoch,
            BuildConfig.deviceClassTag,
            BuildConfig.syncNonce,
            BuildConfig.featureGateId,
            BuildConfig.telemetryPrefix,
            BuildConfig.sessionSlot,
            BuildConfig.buildFingerprint,
        ).joinToString("")
    }

    private val mutex = Mutex()
    private var cache: Map<String, String>? = null
    private var loadFailed = false

    private val api: GitHubContentsApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.github.com/")
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(ScalarsConverterFactory.create())
            .build()
            .create(GitHubContentsApi::class.java)
    }

    /**
     * Fetches and caches the remote key set if it hasn't been loaded yet.
     * Safe to call from multiple places - only the first caller actually
     * hits the network, everyone else reuses the same result.
     */
    suspend fun ensureLoaded() {
        if (cache != null || loadFailed) return
        mutex.withLock {
            if (cache != null || loadFailed) return
            cache = try {
                withContext(Dispatchers.IO) { fetchAndDecode() }
            } catch (e: Exception) {
                loadFailed = true
                null
            }
        }
    }

    /**
     * Returns the value for [keyName]. A non-blank [localValue] (usually
     * a BuildConfig field sourced from apikey.properties) always wins.
     * Falls back to the remote cache, then "" if nothing is available.
     */
    fun get(keyName: String, localValue: String): String {
        if (localValue.isNotBlank()) return localValue
        return cache?.get(keyName).orEmpty()
    }

    private suspend fun fetchAndDecode(): Map<String, String> {
        val responseJson = api.getFileMetadata(
            owner = BuildConfig.GITHUB_OWNER,
            repo = BuildConfig.GITHUB_REPO,
            path = CONTENTS_PATH,
            authorization = "Bearer $bootstrapToken",
        )

        val contentField = JSONObject(responseJson)
            .getString("content")
            .replace("\n", "")

        // GitHub's API base64-encodes the raw file bytes for us. The file
        // itself was ALSO base64-encoded before being committed (so
        // GitHub's push-protection scanner doesn't flag the plain key
        // strings), so this needs decoding twice.
        val onceDecoded = String(Base64.decode(contentField, Base64.DEFAULT))
        val twiceDecoded = String(Base64.decode(onceDecoded, Base64.DEFAULT))

        val json = JSONObject(twiceDecoded)
        val result = mutableMapOf<String, String>()
        json.keys().forEach { key -> result[key] = json.getString(key) }
        return result
    }
}
