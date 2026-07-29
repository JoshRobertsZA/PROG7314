package com.example.prog7314.core.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

/**
 * Single light color scheme, ported from the values/waypoint_* colors
 * that the XML source actually maps onto backgrounds/text/surfaces.
 *
 * The source has no real dark mode: values-night/themes.xml is an empty
 * stub (Base.Theme.PROG7314 override with no items) and every layout
 * references @color/waypoint_* directly rather than a theme attribute, so
 * nothing would actually change in dark mode today. A dark ColorScheme is
 * intentionally NOT invented here - that gap is carried forward as-is,
 * not silently fixed.
 */
private val WaypointColorScheme = lightColorScheme(
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

@Composable
fun WaypointTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = WaypointColorScheme,
        typography = WaypointTypography,
        content = content,
    )
}
