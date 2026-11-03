// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.features.currencyexchange.ui.sanitizeAmountInput` for use in this file
import com.waypoint.app.features.currencyexchange.ui.sanitizeAmountInput
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Test` for use in this file
import org.junit.Test

// Covers the clean-up applied to what the user types in the currency amount field.
class AmountInputTest {

    // annotation `@Test` applied to the function that follows
    @Test
    // whole numbers and numbers with a dot decimal pass through untouched
    fun plainNumbersAreUnchanged() {
        // a whole number stays as typed
        assertEquals("125", sanitizeAmountInput("125"))
        // a dot decimal stays as typed
        assertEquals("12.5", sanitizeAmountInput("12.5"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // a comma decimal keyboard types "," so it must be turned into a dot
    fun commaDecimalBecomesDot() {
        // "12,5" becomes "12.5" so it can be parsed as a number
        assertEquals("12.5", sanitizeAmountInput("12,5"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // a second separator would make the amount unparseable, so only the first is kept
    fun onlyTheFirstSeparatorIsKept() {
        // repeated dots collapse to one decimal point
        assertEquals("1.23", sanitizeAmountInput("1.2.3"))
        // repeated commas collapse to one decimal point
        assertEquals("1.23", sanitizeAmountInput("1,2,3"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // anything that is not a digit or a separator is removed
    fun lettersAndSignsAreDropped() {
        // letters and a minus sign are stripped from the middle and end
        assertEquals("10", sanitizeAmountInput("1a0-"))
        // input with no digits becomes an empty string
        assertEquals("", sanitizeAmountInput("abc"))
    // closes the function
    }
// closes the class
}
