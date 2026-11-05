package com.waypoint.app.core.theme

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

/** Tracks whether the dark theme is active, so the colour getters below return the right shade. */
object WaypointPalette {
    var dark by mutableStateOf(false)
}

val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

val WaypointCream: Color get() = if (WaypointPalette.dark) Color(0xFF1E1713) else Color(0xFFF2E9D3)
val WaypointTerracotta = Color(0xFFC1673D)
val WaypointTextPrimary: Color get() = if (WaypointPalette.dark) Color(0xFFF2E9D3) else Color(0xFF473021)
val WaypointTextMuted: Color get() = if (WaypointPalette.dark) Color(0xFFB8A58F) else Color(0xFF8C7866)
val WaypointBorder = Color(0xFFDADCDF)
val WaypointGoogleBlue = Color(0xFF4285F4)
val WaypointGoogleText = Color(0xFF3C3D43)
val WaypointDecoText = Color(0xFFFFEBDE)

val WaypointCard: Color get() = if (WaypointPalette.dark) Color(0xFF2A211B) else Color(0xFFFCF8F0)
val WaypointBorderSoft: Color get() = if (WaypointPalette.dark) Color(0xFF4A3B2F) else Color(0xFFD9CCB5)
val WaypointTripBadgeText: Color get() = if (WaypointPalette.dark) Color(0xFFE8A07A) else Color(0xFFA9522E)
val WaypointPlaceAccent1 = Color(0xFFF1BD8A)
val WaypointPlaceAccent2 = Color(0xFFC88C5A)
val WaypointPlaceAccent3 = Color(0xFFE39D6B)

val WaypointPlaceAccent4 = Color(0xFFA57F5B)

val WaypointDayMuted = Color(0xFFB5AEA5)
val WaypointTripRange = Color(0xFF5B7FA5)

val WaypointTripLabel = Color(0xD9FFEBDE)

val WaypointNavActivePill = Color(0x1FC1673D)

val WelcomeGradientTop = Color(0xFFFFD9A0)
val WelcomeGradientUpperMid = Color(0xFFFFB199)
val WelcomeGradientMid = Color(0xFFE8848C)
val WelcomeGradientLowerMid = Color(0xFF7C6B96)
val WelcomeGradientBottom = Color(0xFF4B3B62)

val WelcomeGradientDayTop = Color(0xFF8ED2FF)
val WelcomeGradientDayUpperMid = Color(0xFF79B8E8)
val WelcomeGradientDayMid = Color(0xFFC98FC0)
val WelcomeGradientDayLowerMid = Color(0xFF7C6B96)
val WelcomeGradientDayBottom = Color(0xFF4B3B62)

val WelcomeGradientDuskTop = Color(0xFFFF9A56)
val WelcomeGradientDuskUpperMid = Color(0xFFE8637C)
val WelcomeGradientDuskMid = Color(0xFF8C5B96)
val WelcomeGradientDuskLowerMid = Color(0xFF6B5589)
val WelcomeGradientDuskBottom = Color(0xFF4B3B62)

val WelcomeGradientNightTop = Color(0xFF2B2F5E)
val WelcomeGradientNightUpperMid = Color(0xFF34366B)
val WelcomeGradientNightMid = Color(0xFF3D3568)
val WelcomeGradientNightLowerMid = Color(0xFF453460)
val WelcomeGradientNightBottom = Color(0xFF4B3B62)

val WelcomeCardScrim = Color(0xBF000000)

/** Top-to-bottom scrim over the welcome photo, so white text stays readable. */
val WelcomePhotoScrim = listOf(Color(0xCC0F172A), Color(0x66090D16), Color(0xEB070A10), Color(0xFA04060A))

/** GitHub's brand charcoal, used only for the "Continue with GitHub" button. */
val WaypointGitHubButton = Color(0xFF24292E)

/** Error text shown on the dark welcome card. */
val WelcomeErrorText = Color(0xFFFF8A8A)

val WaypointRadioBorderUnselected: Color get() = if (WaypointPalette.dark) Color(0xFF5A483A) else Color(0xFFE6DCC4)

val WaypointOfflineTitle: Color get() = if (WaypointPalette.dark) Color(0xFFF2E9D3) else Color(0xFF261F1A)
val WaypointModalScrim = Color(0x80000000)

val WaypointNotifRowBorder: Color get() = if (WaypointPalette.dark) Color(0xFF5A4535) else Color(0xFFE8C9A0)

val WaypointLogoutBorder: Color get() = if (WaypointPalette.dark) Color(0xFF7A4A3A) else Color(0xFFE8B4A0)