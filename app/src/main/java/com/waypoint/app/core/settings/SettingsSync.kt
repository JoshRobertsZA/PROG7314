package com.waypoint.app.core.settings

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.network.WaypointApiRepository.SettingsPayload
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/** What a settings sync should do after comparing the phone's copy with the API's. */
internal enum class SettingsSyncAction { APPLY_REMOTE, UPLOAD_LOCAL, NONE }

/** Last write wins: the copy with the newer updatedAtMs is kept; equal means already in step. */
internal fun resolveSettingsConflict(localUpdatedAtMs: Long, remoteUpdatedAtMs: Long): SettingsSyncAction = when {
    remoteUpdatedAtMs > localUpdatedAtMs -> SettingsSyncAction.APPLY_REMOTE
    localUpdatedAtMs > remoteUpdatedAtMs -> SettingsSyncAction.UPLOAD_LOCAL
    else -> SettingsSyncAction.NONE
}

/**
 * Keeps the synced settings in [AppSettings] and the API's copy in step, so a
 * user's preferences follow them to another phone. Whichever side changed most
 * recently (updatedAtMs) wins, the same rule the trip sync uses.
 */
object SettingsSync {
    private const val TAG = "SettingsSync"
    private const val PUSH_DEBOUNCE_MS = 1_500L

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private var pendingPush: Job? = null

    fun init() {
        AppSettings.onSyncedChange = ::schedulePush
    }

    /** Waits briefly so flicking a switch on and off sends one request, not several. */
    private fun schedulePush() {
        pendingPush?.cancel()
        pendingPush = scope.launch {
            delay(PUSH_DEBOUNCE_MS)
            val token = AuthRepository.getIdToken() ?: return@launch
            // If this fails (e.g. offline), the next SyncWorker pull sees the local copy is newer and re-sends it.
            WaypointApiRepository.putSettings(token, AppSettings.current.toPayload())
        }
    }

    /** Called by SyncWorker on every sync, including straight after sign-in. */
    suspend fun pull(context: Context, idToken: String) {
        val remote = WaypointApiRepository.getSettings(idToken) ?: return
        val local = AppSettings.current
        when (resolveSettingsConflict(local.updatedAtMs, remote.updatedAtMs)) {
            SettingsSyncAction.APPLY_REMOTE -> {
                Log.i(TAG, "Applying settings from the API (remote is newer)")
                AppSettings.applyRemote(remote.toSettings(local))
                remote.language?.let { applyLanguage(it) }
            }
            SettingsSyncAction.UPLOAD_LOCAL -> {
                Log.i(TAG, "Uploading settings (this phone's copy is newer)")
                WaypointApiRepository.putSettings(idToken, local.toPayload())
            }
            SettingsSyncAction.NONE -> Unit
        }
    }

    private suspend fun applyLanguage(tag: String) {
        if (AppLanguage.entries.none { it.localeTag == tag } || AppLanguage.current().localeTag == tag) return
        withContext(Dispatchers.Main) {
            AppCompatDelegate.setApplicationLocales(LocaleListCompat.forLanguageTags(tag))
        }
    }

    private fun Settings.toPayload() = SettingsPayload(
        darkMode = darkMode,
        notifyTripUpdates = notifyTripUpdates,
        notifyReminders = notifyReminders,
        defaultCurrency = defaultCurrency,
        language = AppLanguage.current().localeTag,
        updatedAtMs = updatedAtMs,
    )

    private fun SettingsPayload.toSettings(local: Settings) = local.copy(
        darkMode = darkMode,
        notifyTripUpdates = notifyTripUpdates,
        notifyReminders = notifyReminders,
        defaultCurrency = defaultCurrency,
        updatedAtMs = updatedAtMs,
    )
}
