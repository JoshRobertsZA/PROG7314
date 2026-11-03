package com.waypoint.app

import com.waypoint.app.core.common.TripValidation
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class TripValidationTest {

    @Test
    fun isTripNameValid_blankOrWhitespaceOnly_isInvalid() {
        assertFalse(TripValidation.isTripNameValid(""))
        assertFalse(TripValidation.isTripNameValid("   "))
    }

    @Test
    fun isTripNameValid_withinLimit_isValid() {
        assertTrue(TripValidation.isTripNameValid("Cape Town trip"))
    }

    @Test
    fun isTripNameValid_atMaxLength_isValid() {
        assertTrue(TripValidation.isTripNameValid("a".repeat(TripValidation.TRIP_NAME_MAX)))
    }

    @Test
    fun isTripNameValid_overMaxLength_isInvalid() {
        assertFalse(TripValidation.isTripNameValid("a".repeat(TripValidation.TRIP_NAME_MAX + 1)))
    }

    @Test
    fun isTripNameValid_surroundingWhitespace_isTrimmedBeforeChecking() {
        // "  " + 40 a's + "  " is over the limit once trimmed to just the a's... actually trimmed length equals the max, so it's valid.
        assertTrue(TripValidation.isTripNameValid("  " + "a".repeat(TripValidation.TRIP_NAME_MAX) + "  "))
    }

    @Test
    fun isDateOrderValid_nullDates_isInvalid() {
        assertFalse(TripValidation.isDateOrderValid(null, null))
        assertFalse(TripValidation.isDateOrderValid(LocalDate.of(2026, 12, 1), null))
        assertFalse(TripValidation.isDateOrderValid(null, LocalDate.of(2026, 12, 1)))
    }

    @Test
    fun isDateOrderValid_endBeforeStart_isInvalid() {
        assertFalse(
            TripValidation.isDateOrderValid(LocalDate.of(2026, 12, 10), LocalDate.of(2026, 12, 1)),
        )
    }

    @Test
    fun isDateOrderValid_endEqualsStart_isValid() {
        val day = LocalDate.of(2026, 12, 1)
        assertTrue(TripValidation.isDateOrderValid(day, day))
    }

    @Test
    fun isDateOrderValid_endAfterStart_isValid() {
        assertTrue(
            TripValidation.isDateOrderValid(LocalDate.of(2026, 12, 1), LocalDate.of(2026, 12, 10)),
        )
    }

    @Test
    fun normaliseFlightNumber_removesSpacesAndUppercases() {
        assertEquals("BA2490", TripValidation.normaliseFlightNumber("ba 2490"))
        assertEquals("BA2490", TripValidation.normaliseFlightNumber(" b a 2 4 9 0 "))
    }

    @Test
    fun isFlightNumberValid_standardTwoLetterCode_isValid() {
        assertTrue(TripValidation.isFlightNumberValid("BA2490"))
    }

    @Test
    fun isFlightNumberValid_lowercaseAndSpaced_isValid() {
        assertTrue(TripValidation.isFlightNumberValid("ba 2490"))
    }

    @Test
    fun isFlightNumberValid_letterDigitCode_isValid() {
        assertTrue(TripValidation.isFlightNumberValid("4Z123"))
    }

    @Test
    fun isFlightNumberValid_digitLetterCode_isValid() {
        assertTrue(TripValidation.isFlightNumberValid("U2123A"))
    }

    @Test
    fun isFlightNumberValid_missingNumber_isInvalid() {
        assertFalse(TripValidation.isFlightNumberValid("BA"))
    }

    @Test
    fun isFlightNumberValid_tooManyDigits_isInvalid() {
        assertFalse(TripValidation.isFlightNumberValid("BA12345"))
    }

    @Test
    fun isFlightNumberValid_bothCodeCharsDigits_isInvalid() {
        assertFalse(TripValidation.isFlightNumberValid("12490"))
    }

    @Test
    fun isFlightNumberValid_blank_isInvalid() {
        assertFalse(TripValidation.isFlightNumberValid(""))
    }
}
