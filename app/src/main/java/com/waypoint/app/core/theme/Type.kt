// declares that this file belongs to the package `com.waypoint.app.core.theme`
package com.waypoint.app.core.theme

// imports `androidx.compose.material3.Typography` for use in this file
import androidx.compose.material3.Typography
// imports `androidx.compose.ui.text.ExperimentalTextApi` for use in this file
import androidx.compose.ui.text.ExperimentalTextApi
// imports `androidx.compose.ui.text.TextStyle` for use in this file
import androidx.compose.ui.text.TextStyle
// imports `androidx.compose.ui.text.font.Font` for use in this file
import androidx.compose.ui.text.font.Font
// imports `androidx.compose.ui.text.font.FontFamily` for use in this file
import androidx.compose.ui.text.font.FontFamily
// imports `androidx.compose.ui.text.font.FontVariation` for use in this file
import androidx.compose.ui.text.font.FontVariation
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R

// annotation `@OptIn` with arguments `(ExperimentalTextApi::class)` applied to the declaration that follows
@OptIn(ExperimentalTextApi::class)
// declares read-only property `RubikFontFamily`, initialised with the result of calling `FontFamily(…)`
val RubikFontFamily = FontFamily(
    // continues the statement started above: `Font(`
    Font(
        // continues the statement started above: `resId = R.font.rubik,`
        resId = R.font.rubik,
        // continues the statement started above: `weight = FontWeight.Normal,`
        weight = FontWeight.Normal,
        // continues the statement started above: `variationSettings = FontVariation.Settings(FontVariation.we…`
        variationSettings = FontVariation.Settings(FontVariation.weight(400)),
    // closes the multi-line argument list started above
    ),
    // continues the statement started above: `Font(`
    Font(
        // continues the statement started above: `resId = R.font.rubik,`
        resId = R.font.rubik,
        // continues the statement started above: `weight = FontWeight.Medium,`
        weight = FontWeight.Medium,
        // continues the statement started above: `variationSettings = FontVariation.Settings(FontVariation.we…`
        variationSettings = FontVariation.Settings(FontVariation.weight(500)),
    // closes the multi-line argument list started above
    ),
    // continues the statement started above: `Font(`
    Font(
        // continues the statement started above: `resId = R.font.rubik,`
        resId = R.font.rubik,
        // continues the statement started above: `weight = FontWeight.SemiBold,`
        weight = FontWeight.SemiBold,
        // continues the statement started above: `variationSettings = FontVariation.Settings(FontVariation.we…`
        variationSettings = FontVariation.Settings(FontVariation.weight(600)),
    // closes the multi-line argument list started above
    ),
    // continues the statement started above: `Font(`
    Font(
        // continues the statement started above: `resId = R.font.rubik,`
        resId = R.font.rubik,
        // continues the statement started above: `weight = FontWeight.Bold,`
        weight = FontWeight.Bold,
        // continues the statement started above: `variationSettings = FontVariation.Settings(FontVariation.we…`
        variationSettings = FontVariation.Settings(FontVariation.weight(700)),
    // closes the multi-line argument list started above
    ),
// closes the multi-line argument list started above
)

// declares read-only property `WaypointTypography`, initialised with the result of calling `Typography(…)`
val WaypointTypography = Typography(
    // continues the statement started above: `displayLarge = TextStyle(fontFamily = RubikFontFamily, font…`
    displayLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `displayMedium = TextStyle(fontFamily = RubikFontFamily, fon…`
    displayMedium = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `displaySmall = TextStyle(fontFamily = RubikFontFamily, font…`
    displaySmall = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `headlineLarge = TextStyle(fontFamily = RubikFontFamily, fon…`
    headlineLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `headlineMedium = TextStyle(fontFamily = RubikFontFamily, fo…`
    headlineMedium = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `headlineSmall = TextStyle(fontFamily = RubikFontFamily, fon…`
    headlineSmall = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `titleLarge = TextStyle(fontFamily = RubikFontFamily, fontWe…`
    titleLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `titleMedium = TextStyle(fontFamily = RubikFontFamily, fontS…`
    titleMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize17, fontWeight = FontWeight.Normal),
    // continues the statement started above: `titleSmall = TextStyle(fontFamily = RubikFontFamily, fontSi…`
    titleSmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize15, fontWeight = FontWeight.Normal),
    // continues the statement started above: `bodyLarge = TextStyle(fontFamily = RubikFontFamily, fontWei…`
    bodyLarge = TextStyle(fontFamily = RubikFontFamily, fontWeight = FontWeight.Normal),
    // continues the statement started above: `bodyMedium = TextStyle(fontFamily = RubikFontFamily, fontSi…`
    bodyMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize14, fontWeight = FontWeight.Normal),
    // continues the statement started above: `bodySmall = TextStyle(fontFamily = RubikFontFamily, fontSiz…`
    bodySmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize13, fontWeight = FontWeight.Normal),
    // continues the statement started above: `labelLarge = TextStyle(fontFamily = RubikFontFamily, fontSi…`
    labelLarge = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize12, fontWeight = FontWeight.Normal),
    // continues the statement started above: `labelMedium = TextStyle(fontFamily = RubikFontFamily, fontS…`
    labelMedium = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize11, fontWeight = FontWeight.Normal),
    // continues the statement started above: `labelSmall = TextStyle(fontFamily = RubikFontFamily, fontSi…`
    labelSmall = TextStyle(fontFamily = RubikFontFamily, fontSize = TextSize10, fontWeight = FontWeight.Normal),
// closes the multi-line argument list started above
)
