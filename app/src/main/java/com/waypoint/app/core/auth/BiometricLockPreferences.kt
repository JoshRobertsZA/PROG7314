package com.waypoint.app.core.auth

import android.content.Context

/**
 * Whether the user has opted into the biometric app-unlock gate (the
 * "Biometric login" toggle in Settings). Plain SharedPreferences rather
 * than a DataStore/Room dependency, consistent with this project's other
 * simple local flags - see AppLanguage/WaypointDbHelper for the same
 * pattern at larger scale.
 */
object BiometricLockPreferences {

    private const val PREFS_NAME = "biometric_lock_prefs"
    private const val KEY_ENABLED = "enabled"

    fun isEnabled(context: Context): Boolean =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_ENABLED, false)

    fun setEnabled(context: Context, enabled: Boolean) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ENABLED, enabled)
            .apply()
    }
}
