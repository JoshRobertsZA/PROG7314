package com.waypoint.app.core.notifications

import android.content.Context
import android.content.SharedPreferences

/**
 * The user's in-app "Notifications" switch (Profile tab). Persisted in
 * SharedPreferences so it survives process death and reinstall-free
 * restarts - defaults to ON the very first time only.
 *
 * This is separate from the OS-level POST_NOTIFICATIONS permission:
 * [PushNotifier.show] checks both, so turning this off silences every
 * notification the app would post, FCM pushes included.
 */
object NotificationPreferences {

    private const val PREFS = "waypoint_notifications"
    private const val KEY_ENABLED = "enabled"

    private fun prefs(context: Context): SharedPreferences =
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    fun isEnabled(context: Context): Boolean = prefs(context).getBoolean(KEY_ENABLED, true)

    fun setEnabled(context: Context, enabled: Boolean) {
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
    }
}
