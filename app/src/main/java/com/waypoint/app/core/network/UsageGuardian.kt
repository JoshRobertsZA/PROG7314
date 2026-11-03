// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `android.util.Log` for use in this file
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
// closes the multi-line argument list started above
)

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

    // declares function `dailyCounterName` taking 2 parameters (`api`, `date`), returning `String`; one counter per API per UTC day, for example `wp-airlabs-d-20261103`
    fun dailyCounterName(api: GuardedApi, date: LocalDate = LocalDate.now(ZoneOffset.UTC)): String =
        // continues the statement started above: `"wp-${api.slug}-d-${date.format(DateTimeFormatter.BASIC_ISO_DATE)}"`
        "wp-${api.slug}-d-${date.format(DateTimeFormatter.BASIC_ISO_DATE)}"

    // declares function `monthlyCounterName` taking 2 parameters (`api`, `date`), returning `String`; one counter per API per UTC month, for example `wp-airlabs-m-202611`
    fun monthlyCounterName(api: GuardedApi, date: LocalDate = LocalDate.now(ZoneOffset.UTC)): String =
        // continues the statement started above: `"wp-${api.slug}-m-%04d%02d".format(date.year, date.monthValue)`
        "wp-${api.slug}-m-%04d%02d".format(date.year, date.monthValue)

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

        // declares read-only property `daily`, read only when the API has a daily limit, returning null from the function when the read fails
        val daily = if (api.dailyLimit != null) getCount(workspace, token, dailyCounterName(api)) ?: return null else 0
        // declares read-only property `monthly`, read only when the API has a monthly limit, returning null from the function when the read fails
        val monthly = if (api.monthlyLimit != null) getCount(workspace, token, monthlyCounterName(api)) ?: return null else 0
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
            // `if` statement: executes the daily increment when the API has a daily limit
            if (api.dailyLimit != null) increment(workspace, token, dailyCounterName(api))
            // `if` statement: executes the monthly increment when the API has a monthly limit
            if (api.monthlyLimit != null) increment(workspace, token, monthlyCounterName(api))
        // closes the repeat block
        }
    // closes the function `recordUse`
    }

    // declares private suspend function `getCount` taking 3 parameters (`workspace`, `token`, `name`), returning `Int?`; a counter that does not exist yet (HTTP 404) counts as 0 and any other failure returns null
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
                // `if` statement: executes `return@use 0` when `resp.code == 404` is true
                if (resp.code == 404) return@use 0
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
