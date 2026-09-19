// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `android.content.Context` for use in this file
import android.content.Context

// declares object `BiometricLockPreferences` and opens its body
object BiometricLockPreferences {

    // declares private const read-only property `PREFS_NAME`, initialised to the string literal "biometric_lock_prefs"
    private const val PREFS_NAME = "biometric_lock_prefs"
    // declares private const read-only property `KEY_ENABLED`, initialised to the string literal "enabled"
    private const val KEY_ENABLED = "enabled"

    // declares function `isEnabled` taking 1 parameter (`context`), returning `Boolean`; its body is the expression ``
    fun isEnabled(context: Context): Boolean =
        // continues the statement started above: `context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVA…`
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            // chained call `.getBoolean` on the previous result with arguments `(KEY_ENABLED, false)`
            .getBoolean(KEY_ENABLED, false)

    // declares function `setEnabled` taking 2 parameters (`context`, `enabled`) and opens its body
    fun setEnabled(context: Context, enabled: Boolean) {
        // calls `getSharedPreferences` on `context` with arguments `(PREFS_NAME, Context.MODE_PRIVATE)`
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            // chained call `.edit` on the previous result
            .edit()
            // chained call `.putBoolean` on the previous result with arguments `(KEY_ENABLED, enabled)`
            .putBoolean(KEY_ENABLED, enabled)
            // chained call `.apply` on the previous result
            .apply()
    // closes the function `setEnabled`
    }
// closes the object `BiometricLockPreferences`
}
