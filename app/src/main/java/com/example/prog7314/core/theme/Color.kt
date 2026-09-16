package com.example.prog7314.core.theme

import androidx.compose.ui.graphics.Color

/**
 * Direct port of every color in res/values/colors.xml. Same names
 * (PascalCase), same hex values - not a redesign, no new tokens invented.
 */
val Black = Color(0xFF000000)
val White = Color(0xFFFFFFFF)

// Waypoint brand palette (login screen, Figma node 64:2)
val WaypointCream = Color(0xFFF2E9D3)
val WaypointTerracotta = Color(0xFFC1673D)
val WaypointTextPrimary = Color(0xFF473021)
val WaypointTextMuted = Color(0xFF8C7866)
val WaypointBorder = Color(0xFFDADCDF)
val WaypointGoogleBlue = Color(0xFF4285F4)
val WaypointGoogleText = Color(0xFF3C3D43)
val WaypointDecoText = Color(0xFFFFEBDE)

// Shared "card" surface, reused across screens (Figma waypoint cream
// #FCF8F0 fill: badges, buttons, widgets, rows, chips)
val WaypointCard = Color(0xFFFCF8F0)
val WaypointBorderSoft = Color(0xFFD9CCB5)
val WaypointTripBadgeText = Color(0xFFA9522E)
val WaypointPlaceAccent1 = Color(0xFFF1BD8A)
val WaypointPlaceAccent2 = Color(0xFFC88C5A)
val WaypointPlaceAccent3 = Color(0xFFE39D6B)

// View / edit itinerary screens (Figma nodes 58:2, 59:2)
val WaypointPlaceAccent4 = Color(0xFFA57F5B)

// Trip calendar screen (Figma node 56:2, "04 Waypoint — Trip Calendar")
val WaypointDayMuted = Color(0xFFB5AEA5)
val WaypointTripRange = Color(0xFF5B7FA5)

// Home screen (Figma node 47:30, "03 Waypoint — Home")
val WaypointTripLabel = Color(0xD9FFEBDE)

// Shared bottom nav active-tab pill (Figma node 270:37, terracotta at 12% alpha)
val WaypointNavActivePill = Color(0x1FC1673D)

// Welcome screen (Figma node 363:20)
val WelcomeGradientTop = Color(0xFFFFD9A0)
val WelcomeGradientUpperMid = Color(0xFFFFB199)
val WelcomeGradientMid = Color(0xFFE8848C)
val WelcomeGradientLowerMid = Color(0xFF7C6B96)
val WelcomeGradientBottom = Color(0xFF4B3B62)
val WelcomePinCircleBg = Color(0x26FFFFFF)
val WelcomeCardScrim = Color(0xBF000000)
val WelcomeCardAccent1 = Color(0xFFC97FA8)
val WelcomeCardAccent2 = Color(0xFF6B2F52)
val WelcomeCardAccent3 = Color(0xFF6BC9C9)
val WelcomeCardAccent4 = Color(0xFF2F6B6B)

// Language modal
// Language modal
val WaypointRadioBorderUnselected = Color(0xFFE6DCC4)
val WaypointModalScrim = Color(0x80000000) // black at 50% alpha

// Offline dialog
val WaypointOfflineTitle = Color(0xFF261F1A)

// Notifications screen
val WaypointNotifRowBorder = Color(0xFFE8C9A0)

// Profile screen log out button border (Figma node 281:47)
val WaypointLogoutBorder = Color(0xFFE8B4A0)