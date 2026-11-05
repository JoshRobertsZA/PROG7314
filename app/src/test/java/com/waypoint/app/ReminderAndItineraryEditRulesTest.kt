package com.waypoint.app

import com.waypoint.app.core.common.TripValidation
import com.waypoint.app.core.notifications.ReminderPass
import com.waypoint.app.core.notifications.reminderPassesDue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

/** Covers when smart reminders run and the rules for editing bookings and place notes. */
class ReminderAndItineraryEditRulesTest {

    @Test
    fun nothingRunsBeforeSevenInTheMorning() {
        assertTrue(reminderPassesDue(0).isEmpty())
        assertTrue(reminderPassesDue(6).isEmpty())
    }

    @Test
    fun morningRulesRunFromSevenNotOnlyAtSeven() {
        assertEquals(setOf(ReminderPass.MORNING), reminderPassesDue(7))
        assertEquals(setOf(ReminderPass.MORNING), reminderPassesDue(14))
    }

    @Test
    fun eveningAddsTomorrowsRulesToTheMorningOnes() {
        assertEquals(setOf(ReminderPass.MORNING, ReminderPass.EVENING), reminderPassesDue(19))
        assertEquals(setOf(ReminderPass.MORNING, ReminderPass.EVENING), reminderPassesDue(23))
    }

    private val tripDays = (0L..4L).map { LocalDate.of(2026, 12, 1).plusDays(it) }

    @Test
    fun bookingRangeMustBeInOrderAndOnItineraryDays() {
        assertTrue(TripValidation.isBookingRangeValid(tripDays[1], tripDays[3], tripDays))
        // a one-day booking is fine
        assertTrue(TripValidation.isBookingRangeValid(tripDays[2], tripDays[2], tripDays))
        // end before start
        assertFalse(TripValidation.isBookingRangeValid(tripDays[3], tripDays[1], tripDays))
        // outside the itinerary
        assertFalse(TripValidation.isBookingRangeValid(tripDays[0], LocalDate.of(2026, 12, 20), tripDays))
    }

    @Test
    fun placeNotesAreTrimmedAndBlankMeansNoNote() {
        assertEquals("Book a table", TripValidation.normalisePlaceNote("  Book a table  "))
        assertNull(TripValidation.normalisePlaceNote("   "))
    }

    @Test
    fun placeNotesHaveALengthLimit() {
        assertTrue(TripValidation.isPlaceNoteValid("a".repeat(TripValidation.PLACE_NOTE_MAX)))
        assertFalse(TripValidation.isPlaceNoteValid("a".repeat(TripValidation.PLACE_NOTE_MAX + 1)))
    }
}
