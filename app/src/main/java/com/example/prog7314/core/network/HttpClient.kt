package com.example.prog7314.core.network

import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Shared OkHttpClient for the app. One instance reuses the connection pool
 * across every API call (weather, currency, Wikipedia, GitHub cache).
 */
object HttpClient {
    val instance: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .build()
    }
}
