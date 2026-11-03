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
// declares function `WaypointTheme` taking 2 parameters (`darkTheme`, `content`); `darkTheme` picks the dark palette and follows the phone's dark mode setting by default
fun WaypointTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
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
        // `if` statement: in dark mode text with no colour set uses the cream text colour instead of black; light mode is unchanged
        if (darkTheme) {
            // provides the cream colour as the default content colour for everything below
            CompositionLocalProvider(LocalContentColor provides WaypointTextPrimary, content = content)
        // `else` branch: light mode keeps its existing behaviour
        } else {
            // draws the content as it was
            content()
        // closes the else block
        }
    // closes the content block
    }
// closes the function `WaypointTheme`
}
