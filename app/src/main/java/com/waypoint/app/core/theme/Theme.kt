// declares that this file belongs to the package `com.waypoint.app.core.theme`
package com.waypoint.app.core.theme

// imports `androidx.compose.material3.MaterialTheme` for use in this file
// imports `androidx.compose.material3.ColorScheme` for use in this file
// imports `androidx.compose.foundation.isSystemInDarkTheme` for use in this file
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
// imports `androidx.compose.material3.LocalContentColor` for use in this file
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
// imports `androidx.compose.material3.darkColorScheme` for use in this file
import androidx.compose.material3.darkColorScheme
// imports `androidx.compose.material3.lightColorScheme` for use in this file
import androidx.compose.material3.lightColorScheme
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.CompositionLocalProvider` for use in this file
import androidx.compose.runtime.CompositionLocalProvider
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember

// declares function `waypointColorScheme` taking one parameter (`dark`), returning the Material colour scheme built from the current Waypoint palette
private fun waypointColorScheme(dark: Boolean): ColorScheme =
    // `if` expression: builds a dark scheme in dark mode and a light one otherwise
    if (dark) {
        // calls `darkColorScheme` with the Waypoint colours
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
        // closes the multi-line argument list started above
        )
    // `else` branch of the `if` expression
    } else {
        // calls `lightColorScheme` with the Waypoint colours
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
        // closes the multi-line argument list started above
        )
    // closes the else block
    }

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `WaypointTheme` taking 2 parameters (`darkTheme`, `content`); `darkTheme` picks the dark palette and uses the user's choice from Settings, or the phone's dark mode setting until they choose
fun WaypointTheme(darkTheme: Boolean = ThemePreferences.override ?: isSystemInDarkTheme(), content: @Composable () -> Unit) {
    // `if` statement: switches the shared palette before any child reads a colour, only when it actually changed
    if (WaypointPalette.dark != darkTheme) WaypointPalette.dark = darkTheme
    // declares read-only property `colorScheme`, rebuilt only when the mode changes
    val colorScheme = remember(darkTheme) { waypointColorScheme(darkTheme) }
    // calls `MaterialTheme` with an argument list that continues on the following lines
    MaterialTheme(
        // continues the statement started above: `colorScheme = colorScheme,`
        colorScheme = colorScheme,
        // continues the statement started above: `typography = WaypointTypography,`
        typography = WaypointTypography,
    // opens the content block of `MaterialTheme`
    ) {
        // provides the default text colour with the same structure in both modes, so switching theme redraws the screens instead of rebuilding them: cream in dark mode, unchanged in light mode
        CompositionLocalProvider(
            // continues the statement started above: the colour used by text that sets none
            LocalContentColor provides if (darkTheme) WaypointTextPrimary else LocalContentColor.current,
            // continues the statement started above: the app content
            content = content,
        // closes the multi-line argument list started above
        )
    // closes the content block
    }
// closes the function `WaypointTheme`
}
