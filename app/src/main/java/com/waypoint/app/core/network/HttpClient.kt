package com.waypoint.app.core.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Shared OkHttpClient for the app. One instance reuses the connection pool
 * across every API call (weather, currency, Wikipedia, GitHub cache).
 */
object HttpClient {
    /** Wikimedia (API and image CDN) rejects generic UAs; send an identifying one everywhere. */
    const val USER_AGENT = "WaypointApp/1.0 (Android; prog7314@iie.ac.za)"

    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                chain.proceed(chain.request().newBuilder().header("User-Agent", USER_AGENT).build())
            }
            .build()
    }
}
