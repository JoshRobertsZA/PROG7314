package com.waypoint.app.core.auth

import android.util.Log
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * App-wide session signals raised outside the UI layer (e.g. by the HTTP client)
 * and handled by the navigation host, which owns sign-out and routing.
 */
object SessionEvents {
    private const val TAG = "SessionEvents"

    private val _unauthorized = MutableSharedFlow<Unit>(
        extraBufferCapacity = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    /** Emits when the Waypoint API rejected the user's token with a 401. */
    val unauthorized: SharedFlow<Unit> = _unauthorized.asSharedFlow()

    fun notifyUnauthorized() {
        Log.w(TAG, "API returned 401 - ending the session")
        _unauthorized.tryEmit(Unit)
    }
}
