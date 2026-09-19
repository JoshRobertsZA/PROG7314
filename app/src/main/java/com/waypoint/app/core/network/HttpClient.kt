// declares that this file belongs to the package `com.waypoint.app.core.network`
package com.waypoint.app.core.network

// imports `okhttp3.OkHttpClient` for use in this file
import okhttp3.OkHttpClient
// imports `java.util.concurrent.TimeUnit` for use in this file
import java.util.concurrent.TimeUnit

// declares object `HttpClient` and opens its body
object HttpClient {
    // declares const read-only property `USER_AGENT`, initialised to the string literal "WaypointApp/1.0 (Android; prog7314@iie…
    const val USER_AGENT = "WaypointApp/1.0 (Android; prog7314@iie.ac.za)"

    // declares read-only property `instance` of type `OkHttpClient`, delegated to `lazy` and opens a lambda / block
    val instance: OkHttpClient by lazy {
        // calls `Builder` on `OkHttpClient` with arguments `()`
        OkHttpClient.Builder()
            // chained call `.connectTimeout` on the previous result with arguments `(10, TimeUnit.SECONDS)`
            .connectTimeout(10, TimeUnit.SECONDS)
            // chained call `.readTimeout` on the previous result with arguments `(10, TimeUnit.SECONDS)`
            .readTimeout(10, TimeUnit.SECONDS)
            // expression: `.addInterceptor { chain ->`
            .addInterceptor { chain ->
                // continues the statement started above: `chain.proceed(chain.request().newBuilder().header("User-Age…`
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build())
            // closes the block
            }
            // chained call `.build` on the previous result
            .build()
    // closes the lambda assigned to `instance`
    }
// closes the object `HttpClient`
}
