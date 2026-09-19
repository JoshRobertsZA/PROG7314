// declares that this file belongs to the package `com.waypoint.app.core.theme`
package com.waypoint.app.core.theme

// imports `androidx.compose.material3.MaterialTheme` for use in this file
import androidx.compose.material3.MaterialTheme
// imports `androidx.compose.material3.lightColorScheme` for use in this file
import androidx.compose.material3.lightColorScheme
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable

// declares private read-only property `WaypointColorScheme`, initialised with the result of calling `lightColorScheme(…)`
private val WaypointColorScheme = lightColorScheme(
    // continues the statement started above: `primary = WaypointTerracotta,`
    primary = WaypointTerracotta,
    // continues the statement started above: `onPrimary = White,`
    onPrimary = White,
    // continues the statement started above: `background = WaypointCream,`
    background = WaypointCream,
    // continues the statement started above: `onBackground = WaypointTextPrimary,`
    onBackground = WaypointTextPrimary,
    // continues the statement started above: `surface = WaypointCard,`
    surface = WaypointCard,
    // continues the statement started above: `onSurface = WaypointTextPrimary,`
    onSurface = WaypointTextPrimary,
    // continues the statement started above: `surfaceVariant = WaypointCard,`
    surfaceVariant = WaypointCard,
    // continues the statement started above: `onSurfaceVariant = WaypointTextMuted,`
    onSurfaceVariant = WaypointTextMuted,
    // continues the statement started above: `outline = WaypointBorderSoft,`
    outline = WaypointBorderSoft,
// closes the multi-line argument list started above
)

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `WaypointTheme` taking 1 parameter (`content`) and opens its body
fun WaypointTheme(content: @Composable () -> Unit) {
    // calls `MaterialTheme` with an argument list that continues on the following lines
    MaterialTheme(
        // continues the statement started above: `colorScheme = WaypointColorScheme,`
        colorScheme = WaypointColorScheme,
        // continues the statement started above: `typography = WaypointTypography,`
        typography = WaypointTypography,
        // continues the statement started above: `content = content,`
        content = content,
    // closes the multi-line argument list started above
    )
// closes the function `WaypointTheme`
}
