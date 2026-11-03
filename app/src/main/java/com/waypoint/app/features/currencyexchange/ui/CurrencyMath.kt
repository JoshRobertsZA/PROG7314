// declares that this file belongs to the package `com.waypoint.app.features.currencyexchange.ui`
package com.waypoint.app.features.currencyexchange.ui

// imports `java.util.Locale` for use in this file
import java.util.Locale
// imports `kotlin.math.abs` for use in this file
import kotlin.math.abs

// declares function `convertAmount` taking 2 parameters (`amount`, `rate`), returning `Double`; its body is the expression `amount * rate`
fun convertAmount(amount: Double, rate: Double): Double = amount * rate

// declares function `formatCurrencyAmount` taking 1 parameter (`value`), returning `String`; shows more decimals for small values so rates like JPY to USD do not display as 0.00
fun formatCurrencyAmount(value: Double): String {
    // declares read-only property `decimals`, initialised with a `when` that picks 2, 4 or 6 decimal places from the size of `value`
    val decimals = when {
        // `when` branch: whole-number sized values and zero use 2 decimals
        value == 0.0 || abs(value) >= 1.0 -> 2
        // `when` branch: values from 0.01 up to 1 use 4 decimals
        abs(value) >= 0.01 -> 4
        // `else` branch of the `when`: very small values use 6 decimals
        else -> 6
    // closes the when block
    }
    // returns `String.format(Locale.US, "%.${decimals}f", value)` from the current function, using a fixed locale so the decimal separator matches the amount input
    return String.format(Locale.US, "%.${decimals}f", value)
// closes the function `formatCurrencyAmount`
}
