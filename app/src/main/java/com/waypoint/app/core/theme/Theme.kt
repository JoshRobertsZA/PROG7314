package com.waypoint.app.core.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember

private fun waypointColorScheme(dark: Boolean): ColorScheme =
    if (dark) {
        darkColorScheme(
            primary = WaypointTerracotta,
            onPrimary = White,
            background = WaypointCream,
            onBackground = WaypointTextPrimary,
            surface = WaypointCard,
            onSurface = WaypointTextPrimary,
            surfaceVariant = WaypointCard,
            onSurfaceVariant = WaypointTextMuted,
            outline = WaypointBorderSoft,
        )
    } else {
        lightColorScheme(
            primary = WaypointTerracotta,
            onPrimary = White,
            background = WaypointCream,
            onBackground = WaypointTextPrimary,
            surface = WaypointCard,
            onSurface = WaypointTextPrimary,
            surfaceVariant = WaypointCard,
            onSurfaceVariant = WaypointTextMuted,
            outline = WaypointBorderSoft,
        )
    }

/**
 * Applies Waypoint's own colours, Rubik typography and shapes, in light or dark depending on Settings
 * or the phone.
 */
@Composable
fun WaypointTheme(darkTheme: Boolean = ThemePreferences.override ?: isSystemInDarkTheme(), content: @Composable () -> Unit) {
    if (WaypointPalette.dark != darkTheme) WaypointPalette.dark = darkTheme
    val colorScheme = remember(darkTheme) { waypointColorScheme(darkTheme) }
    MaterialTheme(
        colorScheme = colorScheme,
        typography = WaypointTypography,
    // opens the content block of `MaterialTheme`
    ) {
        // provides the default text colour with the same structure in both modes, so switching theme redraws the screens instead of rebuilding them: cream in dark mode, unchanged in light mode
        CompositionLocalProvider(
            LocalContentColor provides if (darkTheme) WaypointTextPrimary else LocalContentColor.current,
            content = content,
        )
    }
}
