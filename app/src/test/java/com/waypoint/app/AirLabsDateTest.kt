package com.waypoint.app

import com.waypoint.app.core.network.AirLabsRepository
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test

class AirLabsDateTest {

    private fun schedule(vararg depTimes: String): JSONArray {
        val list = JSONArray()
        depTimes.forEach { list.put(JSONObject().put("flight_iata", "BA123").put("dep_time", it)) }
        return list
    }

    @Test
    fun pickScheduleEntry_noDate_returnsFirstEntry() {
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15", "2026-11-05 09:15")
        // checks that no date keeps the old behaviour of returning the first entry
        assertEquals("2026-11-03 09:15", AirLabsRepository.pickScheduleEntry(list, null)!!.getString("dep_time"))
    }

    @Test
    fun pickScheduleEntry_blankDate_returnsFirstEntry() {
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15")
        // checks that a blank date is treated the same as no date
        assertEquals("2026-11-03 09:15", AirLabsRepository.pickScheduleEntry(list, "  ")!!.getString("dep_time"))
    }

    @Test
    fun pickScheduleEntry_matchingDate_returnsThatEntry() {
        val list = schedule("2026-11-03 09:15", "2026-11-04 10:30", "2026-11-05 11:45")
        // checks that the entry for the requested date is returned, not the first one
        assertEquals("2026-11-05 11:45", AirLabsRepository.pickScheduleEntry(list, "2026-11-05")!!.getString("dep_time"))
    }

    @Test
    fun pickScheduleEntry_dateNotInList_returnsNull() {
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15")
        // checks that a date with no entry returns null instead of another day's flight
        assertNull(AirLabsRepository.pickScheduleEntry(list, "2026-12-25"))
    }

    @Test
    fun pickScheduleEntry_emptyList_returnsNull() {
        // checks that an empty schedule returns null with or without a date
        assertNull(AirLabsRepository.pickScheduleEntry(JSONArray(), null))
        // checks the same with a date
        assertNull(AirLabsRepository.pickScheduleEntry(JSONArray(), "2026-11-03"))
    }

    @Test
    fun pickScheduleEntry_dateWithSpaces_isTrimmed() {
        val list = schedule("2026-11-03 09:15", "2026-11-04 10:30")
        // checks that spaces around the date do not stop it matching
        assertEquals("2026-11-04 10:30", AirLabsRepository.pickScheduleEntry(list, " 2026-11-04 ")!!.getString("dep_time"))
    }

    @Test
    fun pickScheduleEntry_similarDate_doesNotMatch() {
        val list = schedule("2026-11-30 09:15")
        // checks that the 3rd does not match the 30th just because the text starts the same way
        assertNull(AirLabsRepository.pickScheduleEntry(list, "2026-11-03"))
    }

    @Test
    fun cacheKey_noDate_returnsNormalisedFlightNumber() {
        // checks that spaces are removed and letters are upper cased when there is no date
        assertEquals("BA123", AirLabsRepository.cacheKey("ba 123", null))
    }

    @Test
    fun cacheKey_withDate_appendsDate() {
        // checks that the date is added after the flight number
        assertEquals("BA123|2026-11-03", AirLabsRepository.cacheKey("BA123", "2026-11-03"))
    }

    @Test
    fun cacheKey_blankDate_sameAsNoDate() {
        // checks that a blank date gives the same key as no date
        assertEquals(AirLabsRepository.cacheKey("BA123", null), AirLabsRepository.cacheKey("BA123", "  "))
    }

    @Test
    fun cacheKey_sameFlightDifferentDays_differs() {
        // checks that the same flight on two days gets two different keys, so one day's status is never shown for the other
        assertNotEquals(
            AirLabsRepository.cacheKey("BA123", "2026-11-03"),
            AirLabsRepository.cacheKey("BA123", "2026-11-04"),
        )
    }

    @Test
    fun cacheKey_spacingAndCase_doNotChangeKey() {
        // checks that typing the flight number differently still hits the same cache entry
        assertEquals(
            AirLabsRepository.cacheKey("BA123", "2026-11-03"),
            AirLabsRepository.cacheKey("ba 123", "2026-11-03"),
        )
    }
}
