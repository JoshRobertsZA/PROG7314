// declares that this file belongs to the package `com.waypoint.app.core.theme`
package com.waypoint.app.core.theme

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue

// declares the object `ThemePreferences`; remembers whether the user picked the dark or light theme in Settings, and stays on the phone's setting until they do
object ThemePreferences {

    // declares private const `PREFS`, the name of the preferences file
    private const val PREFS = "waypoint_theme"
    // declares private const `KEY_DARK`, the saved choice
    private const val KEY_DARK = "dark"

    // declares mutable property `override`, null while the app follows the phone's setting, otherwise the user's choice; Compose state so the screens switch the moment it changes
    var override: Boolean? by mutableStateOf(null)
        // keeps the setter private so only `setDark` changes it
        private set

    // declares function `init` taking one parameter (`context`); loads the saved choice once when the app starts
    fun init(context: Context) {
        // declares read-only property `prefs`, the saved preferences
        val prefs = context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        // assigns the saved choice, or null when the user has never touched the toggle
        override = if (prefs.contains(KEY_DARK)) prefs.getBoolean(KEY_DARK, false) else null
    // closes the function `init`
    }

    // declares function `setDark` taking 2 parameters (`context`, `dark`); saves the user's choice and applies it straight away
    fun setDark(context: Context, dark: Boolean) {
        // saves the choice so it survives closing the app
        context.applicationContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE).edit().putBoolean(KEY_DARK, dark).apply()
        // applies the choice to the running app
        override = dark
    // closes the function `setDark`
    }
// closes the object `ThemePreferences`
}
