package com.waypoint.app.core.network

import com.waypoint.app.BuildConfig
import com.waypoint.app.core.auth.SessionEvents
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * A 401 on a token-bearing call to our own API means the session is no longer valid.
 * 401s from third-party APIs (weather, flights...) are about their keys, not the user.
 */
internal fun shouldEndSession(status: Int, requestHost: String, apiHost: String?, hadBearerToken: Boolean): Boolean =
    status == 401 && hadBearerToken && apiHost != null && requestHost == apiHost

/** The single OkHttp client every network call in the app goes through. */
object HttpClient {
    const val USER_AGENT = "WaypointApp/1.0 (Android; prog7314@iie.ac.za)"

    private val apiHost: String? by lazy { BuildConfig.WAYPOINT_API_BASE_URL.toHttpUrlOrNull()?.host }

    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            // Cloud Run scales to zero, so the first request after idle can take well over 10 s.
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder().header("User-Agent", USER_AGENT).build()
                val response = chain.proceed(request)
                if (shouldEndSession(response.code, request.url.host, apiHost, request.header("Authorization") != null)) {
                    SessionEvents.notifyUnauthorized()
                }
                response
            }
            .build()
    }
}
