package com.waypoint.app.features.currencyexchange.ui

import java.util.Locale
import kotlin.math.abs

/** Converts [amount] at [rate]. */
fun convertAmount(amount: Double, rate: Double): Double = amount * rate

/** Formats a converted amount, showing more decimals for very small values so they don't round to zero. */
fun formatCurrencyAmount(value: Double): String {
    val decimals = when {
        value == 0.0 || abs(value) >= 1.0 -> 2
        abs(value) >= 0.01 -> 4
        else -> 6
    }
    return String.format(Locale.US, "%.${decimals}f", value)
}

// Cleans what the user types in the amount field. Phones with a comma decimal keyboard
// (common in South Africa) type "," so it becomes ".", and only one separator is kept.
fun sanitizeAmountInput(raw: String): String {
    val sb = StringBuilder()
    var seenSeparator = false
    for (c in raw) {
        when {
            c.isDigit() -> sb.append(c)
            (c == '.' || c == ',') && !seenSeparator -> {
                seenSeparator = true
                sb.append('.')
            }
        }
    }
    return sb.toString()
}
