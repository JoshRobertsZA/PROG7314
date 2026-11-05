package com.waypoint.app.core.settings

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

/** Every user preference the app keeps. Fields marked "synced" follow the user to other phones via the API. */
data class Settings(
    /** null = follow the phone's dark mode. Synced. */
    val darkMode: Boolean? = null,
    /** Device-specific: each phone enrols its own fingerprint, so this is never synced. */
    val biometricLock: Boolean = false,
    /** Pushes from the API when a trip changes on another device. Synced. */
    val notifyTripUpdates: Boolean = true,
    /** Countdown, flight, weather and daily-plan reminders. Synced. */
    val notifyReminders: Boolean = true,
    /** Currency the converter converts into by default. Synced. */
    val defaultCurrency: String = DEFAULT_CURRENCY,
    /** When the last successful two-way sync finished. */
    val lastSyncAtMs: Long? = null,
    /** When a synced field last changed on this phone; decides which copy wins against the API's. */
    val updatedAtMs: Long = 0L,
) {
    companion object {
        const val DEFAULT_CURRENCY = "ZAR"
    }
}

private val Context.settingsDataStore: DataStore<Preferences> by preferencesDataStore(name = "waypoint_settings")

/**
 * Single source of truth for settings, persisted with Jetpack DataStore.
 * The in-memory [state] is authoritative while the process runs, so reads are
 * synchronous and screens update immediately; writes are persisted in order.
 */
object AppSettings {
    private const val TAG = "AppSettings"

    private val KEY_DARK = booleanPreferencesKey("dark_mode")
    private val KEY_BIOMETRIC = booleanPreferencesKey("biometric_lock")
    private val KEY_NOTIFY_TRIP_UPDATES = booleanPreferencesKey("notify_trip_updates")
    private val KEY_NOTIFY_REMINDERS = booleanPreferencesKey("notify_reminders")
    private val KEY_CURRENCY = stringPreferencesKey("default_currency")
    private val KEY_LAST_SYNC = longPreferencesKey("last_sync_at")
    private val KEY_UPDATED = longPreferencesKey("updated_at")
    private val KEY_MIGRATED = booleanPreferencesKey("migrated_legacy_prefs")

    private lateinit var store: DataStore<Preferences>
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val writeLock = Mutex()

    private val _state = MutableStateFlow(Settings())
    val state: StateFlow<Settings> = _state.asStateFlow()
    val current: Settings get() = _state.value

    /** Compose-observable copy of [Settings.darkMode], so the theme recomposes the moment it changes. */
    var darkModeOverride: Boolean? by mutableStateOf(null)
        private set

    /** Called after a synced field changes locally, so it can be uploaded. Set by [SettingsSync]. */
    internal var onSyncedChange: (() -> Unit)? = null

    fun init(context: Context) {
        if (::store.isInitialized) return
        val appContext = context.applicationContext
        store = appContext.settingsDataStore
        // A few bytes on disk; reading it before the first frame avoids a light-to-dark flash on launch.
        val prefs = runBlocking { store.data.first() }
        publish(if (prefs[KEY_MIGRATED] == true) prefs.toSettings() else migrateLegacy(appContext, prefs))
    }

    fun setDarkMode(dark: Boolean?) = update(synced = true) { it.copy(darkMode = dark) }
    fun setBiometricLock(enabled: Boolean) = update(synced = false) { it.copy(biometricLock = enabled) }
    fun setNotifyTripUpdates(enabled: Boolean) = update(synced = true) { it.copy(notifyTripUpdates = enabled) }
    fun setNotifyReminders(enabled: Boolean) = update(synced = true) { it.copy(notifyReminders = enabled) }
    fun setDefaultCurrency(code: String) = update(synced = true) { it.copy(defaultCurrency = code) }

    /** The app language lives in AppCompatDelegate; this only marks settings as changed so it is uploaded. */
    fun markLanguageChanged() = update(synced = true) { it }

    fun recordSync(atMs: Long) = update(synced = false) { it.copy(lastSyncAtMs = atMs) }

    /**
     * Keeps this phone's preferences but forgets they belonged to the signed-out account, so they
     * are never uploaded to whoever signs in next; that account's own saved settings win instead.
     */
    fun onSignedOut() = update(synced = false) { it.copy(updatedAtMs = 0L, lastSyncAtMs = null) }

    /** Applies the API's copy without treating it as a local change (so it isn't uploaded straight back). */
    fun applyRemote(remote: Settings) = update(synced = false) {
        it.copy(
            darkMode = remote.darkMode,
            notifyTripUpdates = remote.notifyTripUpdates,
            notifyReminders = remote.notifyReminders,
            defaultCurrency = remote.defaultCurrency,
            updatedAtMs = remote.updatedAtMs,
        )
    }

    private fun update(synced: Boolean, transform: (Settings) -> Settings) {
        val next = transform(current).let { if (synced) it.copy(updatedAtMs = System.currentTimeMillis()) else it }
        publish(next)
        scope.launch {
            // Writes the latest state, so overlapping updates can't land out of order.
            writeLock.withLock { store.edit { it.writeAll(current) } }
        }
        if (synced) onSyncedChange?.invoke()
    }

    private fun publish(settings: Settings) {
        _state.value = settings
        darkModeOverride = settings.darkMode
    }

    private fun Preferences.toSettings() = Settings(
        darkMode = this[KEY_DARK],
        biometricLock = this[KEY_BIOMETRIC] ?: false,
        notifyTripUpdates = this[KEY_NOTIFY_TRIP_UPDATES] ?: true,
        notifyReminders = this[KEY_NOTIFY_REMINDERS] ?: true,
        defaultCurrency = this[KEY_CURRENCY] ?: Settings.DEFAULT_CURRENCY,
        lastSyncAtMs = this[KEY_LAST_SYNC],
        updatedAtMs = this[KEY_UPDATED] ?: 0L,
    )

    private fun MutablePreferences.writeAll(s: Settings) {
        if (s.darkMode != null) this[KEY_DARK] = s.darkMode else remove(KEY_DARK)
        this[KEY_BIOMETRIC] = s.biometricLock
        this[KEY_NOTIFY_TRIP_UPDATES] = s.notifyTripUpdates
        this[KEY_NOTIFY_REMINDERS] = s.notifyReminders
        this[KEY_CURRENCY] = s.defaultCurrency
        if (s.lastSyncAtMs != null) this[KEY_LAST_SYNC] = s.lastSyncAtMs else remove(KEY_LAST_SYNC)
        this[KEY_UPDATED] = s.updatedAtMs
        this[KEY_MIGRATED] = true
    }

    /** One-off copy of the values earlier versions kept in SharedPreferences. */
    private fun migrateLegacy(context: Context, prefs: Preferences): Settings {
        val theme = context.getSharedPreferences("waypoint_theme", Context.MODE_PRIVATE)
        val biometric = context.getSharedPreferences("biometric_lock_prefs", Context.MODE_PRIVATE)
        val notifications = context.getSharedPreferences("waypoint_notifications", Context.MODE_PRIVATE)
        val notificationsOn = notifications.getBoolean("enabled", true)
        val migrated = prefs.toSettings().copy(
            darkMode = if (theme.contains("dark")) theme.getBoolean("dark", false) else null,
            biometricLock = biometric.getBoolean("enabled", false),
            notifyTripUpdates = notificationsOn,
            notifyReminders = notificationsOn,
        )
        runBlocking { store.edit { it.writeAll(migrated) } }
        listOf(theme, biometric, notifications).forEach { it.edit().clear().apply() }
        Log.i(TAG, "Migrated legacy SharedPreferences into DataStore")
        return migrated
    }
}
