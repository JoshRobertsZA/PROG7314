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

        // declares read-only property `daily`, initialised with the result of calling `getCount(…)`, returning null from the function when the read fails
        val daily = getCount(workspace, token, dailyCounterName(api)) ?: return null
        // declares read-only property `monthly`, initialised with the result of calling `getCount(…)`, returning null from the function when the read fails
        val monthly = getCount(workspace, token, monthlyCounterName(api)) ?: return null
        // returns a `UsageSnapshot` with the daily and monthly counts from the current function
        return UsageSnapshot(api, daily, monthly)
    // closes the function `readCounts`
    }

    // declares suspend function `recordUse` taking 1 parameter (`api`); adds one to both the daily and the monthly counter after a real call to that API
    suspend fun recordUse(api: GuardedApi) {
        // declares read-only property `token`, initialised with the result of calling `RemoteSecrets.get(…)`
        val token = RemoteSecrets.get("COUNTERAPI_API_KEY", BuildConfig.COUNTERAPI_API_KEY)
        // declares read-only property `workspace`, initialised with the result of calling `RemoteSecrets.get(…)`
        val workspace = RemoteSecrets.get("COUNTERAPI_WORKSPACE", BuildConfig.COUNTERAPI_WORKSPACE)
        // `if` statement: executes `return` when `token.isBlank() || workspace.isBlank()` is true
        if (token.isBlank() || workspace.isBlank()) return

        // calls `increment` with arguments `(workspace, token, dailyCounterName(api))`
        increment(workspace, token, dailyCounterName(api))
        // calls `increment` with arguments `(workspace, token, monthlyCounterName(api))`
        increment(workspace, token, monthlyCounterName(api))
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
