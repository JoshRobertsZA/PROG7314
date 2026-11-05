package com.waypoint.app.core.theme

import com.waypoint.app.core.settings.AppSettings

/** Light/dark choice; stored in [AppSettings]. null means follow the phone's setting. */
object ThemePreferences {
    val override: Boolean? get() = AppSettings.darkModeOverride

    fun setDark(dark: Boolean) = AppSettings.setDarkMode(dark)
}
