package com.example.prog7314.core.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle

/**
 * Material3 Typography built from the sp steps in Dimens.kt. No custom
 * font exists in the source (system default typeface throughout), so
 * FontFamily is left as the Material3 default everywhere.
 *
 * The source applies textStyle="bold" per-element, not per size-tier
 * (e.g. some 13sp text is bold, some isn't), so font weight is
 * intentionally NOT baked into these slots - screens apply
 * FontWeight.Bold directly at the call site where the XML did, same as
 * the source.
 *
 * Only the slots that map to a real observed size are overridden; every
 * other Typography slot keeps the Material3 default.
 */
val WaypointTypography = Typography(
    labelSmall = TextStyle(fontSize = TextSize10),
    labelMedium = TextStyle(fontSize = TextSize11),
    labelLarge = TextStyle(fontSize = TextSize12),
    bodySmall = TextStyle(fontSize = TextSize13),
    bodyMedium = TextStyle(fontSize = TextSize14),
    titleSmall = TextStyle(fontSize = TextSize15),
    titleMedium = TextStyle(fontSize = TextSize17),
)
