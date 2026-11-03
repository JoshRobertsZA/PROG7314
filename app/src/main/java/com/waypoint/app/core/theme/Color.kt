// declares that this file belongs to the package `com.waypoint.app.core.theme`
package com.waypoint.app.core.theme

// imports `androidx.compose.ui.graphics.Color` for use in this file
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

// declares the object `WaypointPalette`; holds whether the dark palette is active, as Compose state so anything that reads a theme colour redraws when it changes
object WaypointPalette {
    // declares mutable property `dark`, true while the dark palette is used; set by `WaypointTheme`
    var dark by mutableStateOf(false)
// closes the object `WaypointPalette`
}

// declares read-only property `Black`, initialised with the result of calling `Color(…)`
val Black = Color(0xFF000000)
// declares read-only property `White`, initialised with the result of calling `Color(…)`
val White = Color(0xFFFFFFFF)

// declares read-only property `WaypointCream`; a light colour in light mode and a dark one in dark mode
val WaypointCream: Color get() = if (WaypointPalette.dark) Color(0xFF1E1713) else Color(0xFFF2E9D3)
// declares read-only property `WaypointTerracotta`, initialised with the result of calling `Color(…)`
val WaypointTerracotta = Color(0xFFC1673D)
// declares read-only property `WaypointTextPrimary`; a light colour in light mode and a dark one in dark mode
val WaypointTextPrimary: Color get() = if (WaypointPalette.dark) Color(0xFFF2E9D3) else Color(0xFF473021)
// declares read-only property `WaypointTextMuted`; a light colour in light mode and a dark one in dark mode
val WaypointTextMuted: Color get() = if (WaypointPalette.dark) Color(0xFFB8A58F) else Color(0xFF8C7866)
// declares read-only property `WaypointBorder`, initialised with the result of calling `Color(…)`
val WaypointBorder = Color(0xFFDADCDF)
// declares read-only property `WaypointGoogleBlue`, initialised with the result of calling `Color(…)`
val WaypointGoogleBlue = Color(0xFF4285F4)
// declares read-only property `WaypointGoogleText`, initialised with the result of calling `Color(…)`
val WaypointGoogleText = Color(0xFF3C3D43)
// declares read-only property `WaypointDecoText`, initialised with the result of calling `Color(…)`
val WaypointDecoText = Color(0xFFFFEBDE)

// declares read-only property `WaypointCard`; a light colour in light mode and a dark one in dark mode
val WaypointCard: Color get() = if (WaypointPalette.dark) Color(0xFF2A211B) else Color(0xFFFCF8F0)
// declares read-only property `WaypointBorderSoft`; a light colour in light mode and a dark one in dark mode
val WaypointBorderSoft: Color get() = if (WaypointPalette.dark) Color(0xFF4A3B2F) else Color(0xFFD9CCB5)
// declares read-only property `WaypointTripBadgeText`; the dark terracotta used on cards in light mode and a lighter peach in dark mode so it stays readable
val WaypointTripBadgeText: Color get() = if (WaypointPalette.dark) Color(0xFFE8A07A) else Color(0xFFA9522E)
// declares read-only property `WaypointPlaceAccent1`, initialised with the result of calling `Color(…)`
val WaypointPlaceAccent1 = Color(0xFFF1BD8A)
// declares read-only property `WaypointPlaceAccent2`, initialised with the result of calling `Color(…)`
val WaypointPlaceAccent2 = Color(0xFFC88C5A)
// declares read-only property `WaypointPlaceAccent3`, initialised with the result of calling `Color(…)`
val WaypointPlaceAccent3 = Color(0xFFE39D6B)

// declares read-only property `WaypointPlaceAccent4`, initialised with the result of calling `Color(…)`
val WaypointPlaceAccent4 = Color(0xFFA57F5B)

// declares read-only property `WaypointDayMuted`, initialised with the result of calling `Color(…)`
val WaypointDayMuted = Color(0xFFB5AEA5)
// declares read-only property `WaypointTripRange`, initialised with the result of calling `Color(…)`
val WaypointTripRange = Color(0xFF5B7FA5)

// declares read-only property `WaypointTripLabel`, initialised with the result of calling `Color(…)`
val WaypointTripLabel = Color(0xD9FFEBDE)

// declares read-only property `WaypointNavActivePill`, initialised with the result of calling `Color(…)`
val WaypointNavActivePill = Color(0x1FC1673D)

// declares read-only property `WelcomeGradientTop`, initialised with the result of calling `Color(…)`
val WelcomeGradientTop = Color(0xFFFFD9A0)
// declares read-only property `WelcomeGradientUpperMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientUpperMid = Color(0xFFFFB199)
// declares read-only property `WelcomeGradientMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientMid = Color(0xFFE8848C)
// declares read-only property `WelcomeGradientLowerMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientLowerMid = Color(0xFF7C6B96)
// declares read-only property `WelcomeGradientBottom`, initialised with the result of calling `Color(…)`
val WelcomeGradientBottom = Color(0xFF4B3B62)

// declares read-only property `WelcomeGradientDayTop`, initialised with the result of calling `Color(…)`
val WelcomeGradientDayTop = Color(0xFF8ED2FF)
// declares read-only property `WelcomeGradientDayUpperMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDayUpperMid = Color(0xFF79B8E8)
// declares read-only property `WelcomeGradientDayMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDayMid = Color(0xFFC98FC0)
// declares read-only property `WelcomeGradientDayLowerMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDayLowerMid = Color(0xFF7C6B96)
// declares read-only property `WelcomeGradientDayBottom`, initialised with the result of calling `Color(…)`
val WelcomeGradientDayBottom = Color(0xFF4B3B62)

// declares read-only property `WelcomeGradientDuskTop`, initialised with the result of calling `Color(…)`
val WelcomeGradientDuskTop = Color(0xFFFF9A56)
// declares read-only property `WelcomeGradientDuskUpperMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDuskUpperMid = Color(0xFFE8637C)
// declares read-only property `WelcomeGradientDuskMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDuskMid = Color(0xFF8C5B96)
// declares read-only property `WelcomeGradientDuskLowerMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientDuskLowerMid = Color(0xFF6B5589)
// declares read-only property `WelcomeGradientDuskBottom`, initialised with the result of calling `Color(…)`
val WelcomeGradientDuskBottom = Color(0xFF4B3B62)

// declares read-only property `WelcomeGradientNightTop`, initialised with the result of calling `Color(…)`
val WelcomeGradientNightTop = Color(0xFF2B2F5E)
// declares read-only property `WelcomeGradientNightUpperMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientNightUpperMid = Color(0xFF34366B)
// declares read-only property `WelcomeGradientNightMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientNightMid = Color(0xFF3D3568)
// declares read-only property `WelcomeGradientNightLowerMid`, initialised with the result of calling `Color(…)`
val WelcomeGradientNightLowerMid = Color(0xFF453460)
// declares read-only property `WelcomeGradientNightBottom`, initialised with the result of calling `Color(…)`
val WelcomeGradientNightBottom = Color(0xFF4B3B62)

// declares read-only property `WelcomeCardScrim`, initialised with the result of calling `Color(…)`
val WelcomeCardScrim = Color(0xBF000000)

// declares read-only property `WaypointRadioBorderUnselected`; a light colour in light mode and a dark one in dark mode
val WaypointRadioBorderUnselected: Color get() = if (WaypointPalette.dark) Color(0xFF5A483A) else Color(0xFFE6DCC4)

// declares read-only property `WaypointOfflineTitle`; a light colour in light mode and a dark one in dark mode
val WaypointOfflineTitle: Color get() = if (WaypointPalette.dark) Color(0xFFF2E9D3) else Color(0xFF261F1A)
// declares read-only property `WaypointModalScrim`, initialised with the result of calling `Color(…)`
val WaypointModalScrim = Color(0x80000000)

// declares read-only property `WaypointNotifRowBorder`; a light colour in light mode and a dark one in dark mode
val WaypointNotifRowBorder: Color get() = if (WaypointPalette.dark) Color(0xFF5A4535) else Color(0xFFE8C9A0)

// declares read-only property `WaypointLogoutBorder`; a light colour in light mode and a dark one in dark mode
val WaypointLogoutBorder: Color get() = if (WaypointPalette.dark) Color(0xFF7A4A3A) else Color(0xFFE8B4A0)