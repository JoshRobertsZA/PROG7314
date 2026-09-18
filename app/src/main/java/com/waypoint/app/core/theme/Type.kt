package com.waypoint.app.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontVariation
import androidx.compose.ui.text.font.FontWeight
import com.waypoint.app.R

/**
 * Waypoint global font family: Rubik.
 * Bundled in app/src/main/res/font/rubik.ttf.
 * Mapped explicitly to weight 400 using FontVariation settings.
 */
@OptIn(ExperimentalTextApi::class)
val RubikFontFamily = FontFamily(
    Font(
        resId = R.font.rubik,
        weight = FontWeight.Normal,
        variationSettings = FontVariation.Settings(FontVariation.weight(400)),
    ),
    Font(
        resId = R.font.rubik,
        weight = FontWeight.Medium,
        variationSettings = FontVariation.Settings(FontVariation.weight(500)),
    ),
    Font(
        resId = R.font.rubik,
        weight = FontWeight.SemiBold,
        variationSettings = FontVariation.Settings(FontVariation.weight(600)),
    ),
    Font(
        resId = R.font.rubik,
        weight = FontWeight.Bold,
        variationSettings = FontVariation.Settings(FontVariation.weight(700)),
    ),
)

/**
 * Material3 Typography built with Rubik font family (weight 400).
 */
val WaypointTypography = Typography(
    displayLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    displayMedium = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    displaySmall = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    headlineLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    headlineMedium = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    headlineSmall = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    titleLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    titleMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize17, fontWeight = FontWeight.Normal),
    titleSmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize15, fontWeight = FontWeight.Normal),
    bodyLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    bodyMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize14, fontWeight = FontWeight.Normal),
    bodySmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize13, fontWeight = FontWeight.Normal),
    labelLarge = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize12, fontWeight = FontWeight.Normal),
    labelMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize11, fontWeight = FontWeight.Normal),
    labelSmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize10, fontWeight = FontWeight.Normal),
)
