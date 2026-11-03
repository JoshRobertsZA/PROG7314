// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `android.util.Log` for use in this file
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.content.SharedPreferences` for use in this file
import android.content.SharedPreferences
import android.util.Log
// imports `com.waypoint.app.BuildConfig` for use in this file
import com.waypoint.app.BuildConfig
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.withContext` for use in this file
import kotlinx.coroutines.withContext
// imports `okhttp3.Request` for use in this file
import okhttp3.Request
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.ZoneOffset` for use in this file
import java.time.ZoneOffset
// imports `java.time.format.DateTimeFormatter` for use in this file
import java.time.format.DateTimeFormatter

// declares enum class `GuardedApi` with a primary constructor taking 3 parameters (`slug`, `dailyLimit`, `monthlyLimit`); the shared third party APIs whose free quota the team has to protect
enum class GuardedApi(val slug: String, val dailyLimit: Int?, val monthlyLimit: Int?) {
    // enum entry `LOCATIONIQ`: the free plan allows 5,000 requests per day
    LOCATIONIQ("locationiq", 5_000, null),
    // enum entry `AIRLABS`: assumed free plan of 1,000 requests per month, check this against the AirLabs dashboard
    AIRLABS("airlabs", null, 1_000),
// closes the enum class `GuardedApi`
}

// declares data class `UsageSnapshot` with a primary constructor taking 3 parameters (`api`, `dailyCount`, `monthlyCount`); how much of an API's quota has been used so far
data class UsageSnapshot(
    // continues the statement started above: `val api: GuardedApi,`
    val api: GuardedApi,
    // continues the statement started above: `val dailyCount: Int,`
    val dailyCount: Int,
    // continues the statement started above: `val monthlyCount: Int,`
    val monthlyCount: Int,
// ends the argument list and opens the body that holds the computed values
) {
    // declares read-only property `limit`, the limit that applies to this API: the daily one when it has one, otherwise the monthly one
    val limit: Int get() = api.dailyLimit ?: api.monthlyLimit ?: 0
    // declares read-only property `used`, the count that is measured against `limit`
    val used: Int get() = if (api.dailyLimit != null) dailyCount else monthlyCount
    // declares read-only property `fraction`, how much of the limit is used, from 0 to 1
    val fraction: Float get() = if (limit == 0) 0f else (used.toFloat() / limit).coerceIn(0f, 1f)
// closes the body of `UsageSnapshot`
}

// declares object `UsageGuardian` and opens its body
object UsageGuardian {

    // declares private const read-only property `TAG`, initialised to the string literal "UsageGuardian"
    private const val TAG = "UsageGuardian"

    // declares private const read-only property `SAFETY_FRACTION`, initialised to 0.9; calls are blocked at 90 percent of a limit because CounterAPI reads can lag behind the real usage
    private const val SAFETY_FRACTION = 0.9
    // declares private const read-only property `SNAPSHOT_TTL_MS`, initialised to 60_000L; how long a usage read is reused before CounterAPI is asked again
    private const val SNAPSHOT_TTL_MS = 60_000L
    // declares private read-only property `snapshots`, holding the last usage read per API and when it was read, so the guardian does not call CounterAPI before every request
    private val snapshots = java.util.concurrent.ConcurrentHashMap<GuardedApi, Pair<Long, UsageSnapshot>>()

    // declares function `isWithinBudget` taking 2 parameters (`snapshot`, `needed`), returning `Boolean`; true when `needed` more requests still fit under the safe part of every limit the API has
    fun isWithinBudget(snapshot: UsageSnapshot, needed: Int = 1): Boolean {
        // declares read-only property `dailyLimit`, initialised to `snapshot.api.dailyLimit`
        val dailyLimit = snapshot.api.dailyLimit
        // declares read-only property `monthlyLimit`, initialised to `snapshot.api.monthlyLimit`
        val monthlyLimit = snapshot.api.monthlyLimit
        // declares read-only property `dailyOk`, true when there is no daily limit or the daily count plus `needed` stays under the safe limit
        val dailyOk = dailyLimit == null || snapshot.dailyCount + needed <= (dailyLimit * SAFETY_FRACTION).toInt()
        // declares read-only property `monthlyOk`, true when there is no monthly limit or the monthly count plus `needed` stays under the safe limit
        val monthlyOk = monthlyLimit == null || snapshot.monthlyCount + needed <= (monthlyLimit * SAFETY_FRACTION).toInt()
        // returns `dailyOk && monthlyOk` from the current function
        return dailyOk && monthlyOk
    // closes the function `isWithinBudget`
    }

    // declares suspend function `isAllowed` taking 2 parameters (`api`, `needed`), returning `Boolean`; the check callers make before spending quota, which allows the call when usage cannot be read
    suspend fun isAllowed(api: GuardedApi, needed: Int = 1): Boolean {
        // declares read-only property `snapshot`, initialised with the result of calling `currentSnapshot(…)`, returning true from the function when usage cannot be read
        val snapshot = currentSnapshot(api) ?: return true
        // declares read-only property `allowed`, initialised with the result of calling `isWithinBudget(…)`
        val allowed = isWithinBudget(snapshot, needed)
        // `if` statement: executes `Log.w(...)` when `!allowed` is true
        if (!allowed) Log.w(TAG, "${api.slug} budget used up: daily=${snapshot.dailyCount}, monthly=${snapshot.monthlyCount}")
        // returns `allowed` from the current function
        return allowed
    // closes the function `isAllowed`
    }

    // declares private suspend function `currentSnapshot` taking 1 parameter (`api`), returning `UsageSnapshot?`; reuses a read from the last minute, otherwise reads CounterAPI again
    private suspend fun currentSnapshot(api: GuardedApi): UsageSnapshot? {
        // declares read-only property `cached`, initialised to `snapshots[api]`
        val cached = snapshots[api]
        // `if` statement: executes `return cached.second` when the cached read is still fresh
        if (cached != null && System.currentTimeMillis() - cached.first < SNAPSHOT_TTL_MS) return cached.second
        // declares read-only property `fresh`, initialised with the result of calling `readCounts(…)`, returning null from the function when the read fails
        val fresh = readCounts(api) ?: return null
        // assigns `snapshots[api]` the value `Pair(System.currentTimeMillis(), fresh)`
        snapshots[api] = Pair(System.currentTimeMillis(), fresh)
        // returns `fresh` from the current function
        return fresh
    // closes the function `currentSnapshot`
    }

    // declares private property `prefs` holding the phone's saved window baselines; null until `init` has run
    private var prefs: SharedPreferences? = null

    // declares function `init` taking one parameter (`context`); called once from the Application so the guardian can remember where each day or month started
    fun init(context: Context) {
        // opens the private preferences file the baselines are saved in
        prefs = context.applicationContext.getSharedPreferences("usage_guardian_prefs", Context.MODE_PRIVATE)
    // closes the function `init`
    }

    // declares function `counterName` taking one parameter (`api`), returning `String`; one fixed counter per API that is created once in the CounterAPI dashboard, for example `wp-airlabs`
    fun counterName(api: GuardedApi): String = "wp-${api.slug}"

    // declares function `windowKey` taking 2 parameters (`api`, `date`), returning `String`; names the current window, the UTC day for an API with a daily limit and the UTC month otherwise
    fun windowKey(api: GuardedApi, date: LocalDate = LocalDate.now(ZoneOffset.UTC)): String =
        // continues the statement started above: the day as `20261103` or the month as `202611`
        if (api.dailyLimit != null) date.format(DateTimeFormatter.BASIC_ISO_DATE) else "%04d%02d".format(date.year, date.monthValue)

    // declares function `windowedUsage` taking 2 parameters (`total`, `baseline`), returning `Int`; how much was used since the window started, never below 0
    fun windowedUsage(total: Int, baseline: Int): Int = (total - baseline).coerceAtLeast(0)

    // declares private function `usedInWindow` taking 2 parameters (`api`, `total`), returning `Int`; compares the counter's running total with the total this phone saw when the current day or month began
    private fun usedInWindow(api: GuardedApi, total: Int): Int {
        // declares read-only property `store`, returning 0 from the function when `init` has not run
        val store = prefs ?: return 0
        // declares read-only property `key`, the saved baseline for this API and this window
        val key = "baseline_${api.slug}_${windowKey(api)}"
        // `if` statement: the block below runs the first time this window is seen, so today's total becomes the starting point
        if (!store.contains(key)) {
            // saves the total as the baseline for this window
            store.edit().putInt(key, total).apply()
            // returns 0 because nothing has been counted in this window yet
            return 0
        // closes the if block
        }
        // returns the usage since the baseline
        return windowedUsage(total, store.getInt(key, total))
    // closes the function `usedInWindow`
    }

    // declares function `parseCount` taking 1 parameter (`body`), returning `Int?`; reads the counter value from a CounterAPI response, trying the field names it may use, and returns null when none is found
    fun parseCount(body: String): Int? {
        // declares read-only property `json`, initialised with a `try` that returns null when `body` is not valid JSON
        val json = try { JSONObject(body) } catch (e: Exception) { return null }
        // declares read-only property `data`, the nested `data` object when there is one, otherwise the top level object
        val data = json.optJSONObject("data") ?: json
        // `for` loop: `field` takes each candidate field name in turn
        for (field in listOf("up_count", "count", "value")) {
            // `if` statement: the block below runs when `data.has(field)` is true
            if (data.has(field)) {
                // declares read-only property `value`, initialised with the result of calling `data.optInt(…)`, using -1 when it is not a number
                val value = data.optInt(field, -1)
                // `if` statement: executes `return value` when `value >= 0` is true
                if (value >= 0) return value
            // closes the if block
            }
        // closes the for loop
        }
        // returns `null` from the current function because no known field was found
        return null
    // closes the function `parseCount`
    }

    // declares suspend function `readCounts` taking 1 parameter (`api`), returning `UsageSnapshot?`; null means the usage could not be read, so callers should not block the request
    suspend fun readCounts(api: GuardedApi): UsageSnapshot? {
        // declares read-only property `token`, initialised with the result of calling `RemoteSecrets.get(…)`
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        // declares read-only property `workspace`, initialised with the result of calling `RemoteSecrets.get(…)`
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        // `if` statement: the block below runs when `token.isBlank() || workspace.isBlank()` is true
        if (token.isBlank() || workspace.isBlank()) {
            // calls `d` on `Log` with arguments `(TAG, "CounterAPI not configured -- cannot read usage")`
            Log.d(TAG, "CounterAPI not configured -- cannot read usage")
            // returns `null` from the current function
            return null
        // closes the if block
        }

        // declares read-only property `total`, the running total of the API's counter, returning null from the function when the read fails
        val total = getCount(workspace, token, counterName(api)) ?: return null
        // declares read-only property `used`, how much of that total was used in the current day or month
        val used = usedInWindow(api, total)
        // declares read-only property `daily`, the usage when the API has a daily limit
        val daily = if (api.dailyLimit != null) used else 0
        // declares read-only property `monthly`, the usage when the API has a monthly limit
        val monthly = if (api.monthlyLimit != null) used else 0
        // returns a `UsageSnapshot` with the daily and monthly counts from the current function
        return UsageSnapshot(api, daily, monthly)
    // closes the function `readCounts`
    }

    // declares suspend function `recordUse` taking 2 parameters (`api`, `count`); adds `count` to the counters the API has limits for, after real calls to that API
    suspend fun recordUse(api: GuardedApi, count: Int = 1) {
        // declares read-only property `cached`, initialised to `snapshots[api]`
        val cached = snapshots[api]
        // `if` statement: the block below runs when a usage read is cached, so decisions in the next minute already include this use
        if (cached != null) {
            // assigns `snapshots[api]` the cached read with `count` added to both counts
            snapshots[api] = Pair(cached.first, cached.second.copy(
                // continues the statement started above: `dailyCount = cached.second.dailyCount + count,`
                dailyCount = cached.second.dailyCount + count,
                // continues the statement started above: `monthlyCount = cached.second.monthlyCount + count,`
                monthlyCount = cached.second.monthlyCount + count,
            // closes the multi-line argument list started above
            ))
        // closes the if block
        }

        // declares read-only property `token`, initialised with the result of calling `RemoteSecrets.get(…)`
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        // declares read-only property `workspace`, initialised with the result of calling `RemoteSecrets.get(…)`
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        // `if` statement: executes `return` when `token.isBlank() || workspace.isBlank()` is true
        if (token.isBlank() || workspace.isBlank()) return

        // `repeat` loop: runs the block below `count` times, one CounterAPI increment per request made to the guarded API
        repeat(count) {
            // calls `increment` on the API's single running counter
            increment(workspace, token, counterName(api))
        // closes the repeat block
        }
    // closes the function `recordUse`
    }

    // declares private suspend function `getCount` taking 3 parameters (`workspace`, `token`, `name`), returning `Int?`; returns the counter's running total, or null when the counter is missing (HTTP 404) or the read fails
    private suspend fun getCount(workspace: String, token: String, name: String): Int? = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `("https://api.counterapi.dev/v2/$workspace/$name")`
                .url("https://api.counterapi.dev/v2/$workspace/$name")
                // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $token")`
                .header("Authorization", "Bearer $token")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // calls `use` on the response so it is always closed, and opens a lambda / block
            HttpClient.instance.newCall(req).execute().use { resp ->
                // `if` statement: the block below runs when the counter does not exist, which means it has to be created in the CounterAPI dashboard
                if (resp.code == 404) {
                    // logs how to fix it
                    Log.w(TAG, "CounterAPI counter $name not found, create it in the dashboard")
                    // returns null so the guardian fails open
                    return@use null
                // closes the if block
                }
                // `if` statement: the block below runs when `!resp.isSuccessful` is true
                if (!resp.isSuccessful) {
                    // calls `w` on `Log` with arguments `(TAG, "CounterAPI read HTTP ${resp.code} for $name")`
                    Log.w(TAG, "CounterAPI read HTTP ${resp.code} for $name")
                    // expression: `return@use null`
                    return@use null
                // closes the if block
                }
                // expression: `parseCount(resp.body?.string() ?: "")`
                parseCount(resp.body?.string() ?: "")
            // closes the lambda passed to `use`
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "CounterAPI read failed for $name: ${e.message}")`
            Log.w(TAG, "CounterAPI read failed for $name: ${e.message}")
            // expression: `null`
            null
        // closes the catch block
        }
    // closes the block
    }

    // declares private suspend function `increment` taking 3 parameters (`workspace`, `token`, `name`); calls the CounterAPI `/up` endpoint and only logs the outcome
    private suspend fun increment(workspace: String, token: String, name: String) = withContext(Dispatchers.IO) {
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // declares read-only property `req`, initialised with the result of calling `Request.Builder(…)`
            val req = Request.Builder()
                // chained call `.url` on the previous result with arguments `("https://api.counterapi.dev/v2/$workspace/$name/up")`
                .url("https://api.counterapi.dev/v2/$workspace/$name/up")
                // chained call `.header` on the previous result with arguments `("Authorization", "Bearer $token")`
                .header("Authorization", "Bearer $token")
                // chained call `.get` on the previous result with arguments `().build()`
                .get().build()
            // calls `use` on the response so it is always closed, and logs the HTTP code
            HttpClient.instance.newCall(req).execute().use { resp ->
                // calls `d` on `Log` with arguments `(TAG, "CounterAPI up $name -> HTTP ${resp.code}")`
                Log.d(TAG, "CounterAPI up $name -> HTTP ${resp.code}")
            // closes the lambda passed to `use`
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "CounterAPI up failed for $name: ${e.message}")`
            Log.w(TAG, "CounterAPI up failed for $name: ${e.message}")
        // closes the catch block
        }
    // closes the block
    }
// closes the object `UsageGuardian`
}
