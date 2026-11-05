package com.waypoint.app.core.notifications

import android.util.Log
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.core.network.WaypointApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

// Re-registers this device's FCM token with the server when it changes
// while the user is already signed in (a token rotation). The initial
// post-login registration happens in AuthViewModel.applySignedInUser
// instead, which already has a freshly fetched ID token on hand and
// doesn't need to ask AuthRepository for one again.
object DeviceRegistrar {

    private const val TAG = "DeviceRegistrar"
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // Called from FirebaseMessagingService.onNewToken, which isn't a
    // suspend function - fire-and-forget is the only option there.
    fun registerIfSignedIn() {
        if (!AuthRepository.isSignedIn) return
        ioScope.launch {
            val token = PushTokenManager.currentToken ?: return@launch
            val idToken = AuthRepository.getIdToken() ?: return@launch
            val ok = WaypointApiRepository.registerDevice(idToken, token, AppLanguage.current().localeTag)
            if (!ok) Log.w(TAG, "Device re-registration failed")
        }
    }
}
