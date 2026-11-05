package com.waypoint.app.core.network

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.waypoint.app.BuildConfig
import com.waypoint.app.core.secrets.RemoteSecrets
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.Request
import org.json.JSONObject
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/** Third-party APIs with a small free quota, and their daily or monthly limit. */
enum class GuardedApi(val slug: String, val dailyLimit: Int?, val monthlyLimit: Int?) {
    // enum entry `LOCATIONIQ`: the free plan allows 5,000 requests per day
    LOCATIONIQ("locationiq", 5_000, null),
    // enum entry `AIRLABS`: assumed free plan of 1,000 requests per month, check this against the AirLabs dashboard
    AIRLABS("airlabs", null, 1_000),
}

/** How much of one API's quota has been used, for the budget meters in Settings. */
data class UsageSnapshot(
    val api: GuardedApi,
    val dailyCount: Int,
    val monthlyCount: Int,
) {
    val limit: Int get() = api.dailyLimit ?: api.monthlyLimit ?: 0
    val used: Int get() = if (api.dailyLimit != null) dailyCount else monthlyCount
    val fraction: Float get() = if (limit == 0) 0f else (used.toFloat() / limit).coerceIn(0f, 1f)
}

/**
 * Counts calls to the quota-limited APIs (through CounterAPI, shared by all users) and blocks new
 * calls at 90% of the limit, so the app falls back to cached data instead of failing.
 */
object UsageGuardian {

    private const val TAG = "UsageGuardian"

    private const val SAFETY_FRACTION = 0.9
    private const val SNAPSHOT_TTL_MS = 60_000L
    private val snapshots = java.util.concurrent.ConcurrentHashMap<GuardedApi, Pair<Long, UsageSnapshot>>()

    fun isWithinBudget(snapshot: UsageSnapshot, needed: Int = 1): Boolean {
        val dailyLimit = snapshot.api.dailyLimit
        val monthlyLimit = snapshot.api.monthlyLimit
        val dailyOk = dailyLimit == null || snapshot.dailyCount + needed <= (dailyLimit * SAFETY_FRACTION).toInt()
        val monthlyOk = monthlyLimit == null || snapshot.monthlyCount + needed <= (monthlyLimit * SAFETY_FRACTION).toInt()
        return dailyOk && monthlyOk
    }

    suspend fun isAllowed(api: GuardedApi, needed: Int = 1): Boolean {
        val snapshot = currentSnapshot(api) ?: return true
        val allowed = isWithinBudget(snapshot, needed)
        if (!allowed) Log.w(TAG, "${api.slug} budget used up: daily=${snapshot.dailyCount}, monthly=${snapshot.monthlyCount}")
        return allowed
    }

    private suspend fun currentSnapshot(api: GuardedApi): UsageSnapshot? {
        val cached = snapshots[api]
        if (cached != null && System.currentTimeMillis() - cached.first < SNAPSHOT_TTL_MS) return cached.second
        val fresh = readCounts(api) ?: return null
        snapshots[api] = Pair(System.currentTimeMillis(), fresh)
        return fresh
    }

    private var prefs: SharedPreferences? = null

    fun init(context: Context) {
        // opens the private preferences file the baselines are saved in
        prefs = context.applicationContext.getSharedPreferences("usage_guardian_prefs", Context.MODE_PRIVATE)
    }

    fun counterName(api: GuardedApi): String = "wp-${api.slug}"

    fun windowKey(api: GuardedApi, date: LocalDate = LocalDate.now(ZoneOffset.UTC)): String =
        if (api.dailyLimit != null) date.format(DateTimeFormatter.BASIC_ISO_DATE) else "%04d%02d".format(date.year, date.monthValue)

    fun windowedUsage(total: Int, baseline: Int): Int = (total - baseline).coerceAtLeast(0)

    private fun usedInWindow(api: GuardedApi, total: Int): Int {
        val store = prefs ?: return 0
        val key = "baseline_${api.slug}_${windowKey(api)}"
        if (!store.contains(key)) {
            // saves the total as the baseline for this window
            store.edit().putInt(key, total).apply()
            // returns 0 because nothing has been counted in this window yet
            return 0
        }
        // returns the usage since the baseline
        return windowedUsage(total, store.getInt(key, total))
    }

    fun parseCount(body: String): Int? {
        val json = try { JSONObject(body) } catch (e: Exception) { return null }
        val data = json.optJSONObject("data") ?: json
        for (field in listOf("up_count", "count", "value")) {
            if (data.has(field)) {
                val value = data.optInt(field, -1)
                if (value >= 0) return value
            }
        }
        return null
    }

    suspend fun readCounts(api: GuardedApi): UsageSnapshot? {
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        if (token.isBlank() || workspace.isBlank()) {
            Log.d(TAG, "CounterAPI not configured -- cannot read usage")
            return null
        }

        val total = getCount(workspace, token, counterName(api)) ?: return null
        val used = usedInWindow(api, total)
        val daily = if (api.dailyLimit != null) used else 0
        val monthly = if (api.monthlyLimit != null) used else 0
        // returns a `UsageSnapshot` with the daily and monthly counts from the current function
        return UsageSnapshot(api, daily, monthly)
    }

    suspend fun recordUse(api: GuardedApi, count: Int = 1) {
        val cached = snapshots[api]
        if (cached != null) {
            snapshots[api] = Pair(cached.first, cached.second.copy(
                dailyCount = cached.second.dailyCount + count,
                monthlyCount = cached.second.monthlyCount + count,
            ))
        }

        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        if (token.isBlank() || workspace.isBlank()) return

        repeat(count) {
            increment(workspace, token, counterName(api))
        }
    }

    private suspend fun getCount(workspace: String, token: String, name: String): Int? = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url("https://api.counterapi.dev/v2/$workspace/$name")
                .header("Authorization", "Bearer $token")
                .get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                if (resp.code == 404) {
                    // logs how to fix it
                    Log.w(TAG, "CounterAPI counter $name not found, create it in the dashboard")
                    // returns null so the guardian fails open
                    return@use null
                }
                if (!resp.isSuccessful) {
                    Log.w(TAG, "CounterAPI read HTTP ${resp.code} for $name")
                    return@use null
                }
                parseCount(resp.body?.string() ?: "")
            }
        } catch (e: Exception) {
            Log.w(TAG, "CounterAPI read failed for $name: ${e.message}")
            null
        }
    }

    private suspend fun increment(workspace: String, token: String, name: String) = withContext(Dispatchers.IO) {
        try {
            val req = Request.Builder()
                .url("https://api.counterapi.dev/v2/$workspace/$name/up")
                .header("Authorization", "Bearer $token")
                .get().build()
            HttpClient.instance.newCall(req).execute().use { resp ->
                Log.d(TAG, "CounterAPI up $name -> HTTP ${resp.code}")
            }
        } catch (e: Exception) {
            Log.w(TAG, "CounterAPI up failed for $name: ${e.message}")
        }
    }
}
