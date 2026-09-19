// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.content.SharedPreferences` for use in this file
import android.content.SharedPreferences

// declares object `NotificationPreferences` and opens its body
object NotificationPreferences {

    // declares private const read-only property `PREFS`, initialised to the string literal "waypoint_notifications"
    private const val PREFS = "waypoint_notifications"
    // declares private const read-only property `KEY_ENABLED`, initialised to the string literal "enabled"
    private const val KEY_ENABLED = "enabled"

    // declares private function `prefs` taking 1 parameter (`context`), returning `SharedPreferences`; its body is the expression ``
    private fun prefs(context: Context): SharedPreferences =
        // continues the statement started above: `context.applicationContext.getSharedPreferences(PREFS, Cont…`
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)

    // declares function `isEnabled` taking 1 parameter (`context`), returning `Boolean`; its body is the expression `prefs(context).getBoolean(KEY_ENABLED, true)`
    fun isEnabled(context: Context): Boolean = prefs(context).getBoolean(KEY_ENABLED, true)

    // declares function `setEnabled` taking 2 parameters (`context`, `enabled`) and opens its body
    fun setEnabled(context: Context, enabled: Boolean) {
        // calls `prefs` with arguments `(context)`, then chains `.edit()`, `.putBoolean(KEY_ENABLED, enabl…`, `.apply()`
        prefs(context).edit().putBoolean(KEY_ENABLED, enabled).apply()
    // closes the function `setEnabled`
    }
// closes the object `NotificationPreferences`
}
