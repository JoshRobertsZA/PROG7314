// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.network.AirLabsRepository` for use in this file
import com.waypoint.app.core.network.AirLabsRepository
// imports `org.json.JSONArray` for use in this file
import org.json.JSONArray
// imports `org.json.JSONObject` for use in this file
import org.json.JSONObject
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Assert.assertNotEquals` for use in this file
import org.junit.Assert.assertNotEquals
// imports `org.junit.Assert.assertNull` for use in this file
import org.junit.Assert.assertNull
// imports `org.junit.Test` for use in this file
import org.junit.Test

// declares class `AirLabsDateTest` and opens its body
class AirLabsDateTest {

    // declares private function `schedule` taking a vararg of departure times, returning a `JSONArray` shaped like the AirLabs `response` list
    private fun schedule(vararg depTimes: String): JSONArray {
        // declares read-only property `list`, initialised with the result of calling `JSONArray(…)`
        val list = JSONArray()
        // expression: `depTimes.forEach { list.put(JSONObject().put(...)) }`
        depTimes.forEach { list.put(JSONObject().put("flight_iata", "BA123").put("dep_time", it)) }
        // returns `list` from the current function
        return list
    // closes the function `schedule`
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_noDate_returnsFirstEntry`
    fun pickScheduleEntry_noDate_returnsFirstEntry() {
        // declares read-only property `list`, a schedule with three days
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15", "2026-11-05 09:15")
        // checks that no date keeps the old behaviour of returning the first entry
        assertEquals("2026-11-03 09:15", AirLabsRepository.pickScheduleEntry(list, null)!!.getString("dep_time"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_blankDate_returnsFirstEntry`
    fun pickScheduleEntry_blankDate_returnsFirstEntry() {
        // declares read-only property `list`, a schedule with two days
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15")
        // checks that a blank date is treated the same as no date
        assertEquals("2026-11-03 09:15", AirLabsRepository.pickScheduleEntry(list, "  ")!!.getString("dep_time"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_matchingDate_returnsThatEntry`
    fun pickScheduleEntry_matchingDate_returnsThatEntry() {
        // declares read-only property `list`, a schedule with three days at different times
        val list = schedule("2026-11-03 09:15", "2026-11-04 10:30", "2026-11-05 11:45")
        // checks that the entry for the requested date is returned, not the first one
        assertEquals("2026-11-05 11:45", AirLabsRepository.pickScheduleEntry(list, "2026-11-05")!!.getString("dep_time"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_dateNotInList_returnsNull`
    fun pickScheduleEntry_dateNotInList_returnsNull() {
        // declares read-only property `list`, a schedule that does not include the requested day
        val list = schedule("2026-11-03 09:15", "2026-11-04 09:15")
        // checks that a date with no entry returns null instead of another day's flight
        assertNull(AirLabsRepository.pickScheduleEntry(list, "2026-12-25"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_emptyList_returnsNull`
    fun pickScheduleEntry_emptyList_returnsNull() {
        // checks that an empty schedule returns null with or without a date
        assertNull(AirLabsRepository.pickScheduleEntry(JSONArray(), null))
        // checks the same with a date
        assertNull(AirLabsRepository.pickScheduleEntry(JSONArray(), "2026-11-03"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_dateWithSpaces_isTrimmed`
    fun pickScheduleEntry_dateWithSpaces_isTrimmed() {
        // declares read-only property `list`, a schedule with two days
        val list = schedule("2026-11-03 09:15", "2026-11-04 10:30")
        // checks that spaces around the date do not stop it matching
        assertEquals("2026-11-04 10:30", AirLabsRepository.pickScheduleEntry(list, " 2026-11-04 ")!!.getString("dep_time"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `pickScheduleEntry_similarDate_doesNotMatch`
    fun pickScheduleEntry_similarDate_doesNotMatch() {
        // declares read-only property `list`, a schedule that only has the 30th
        val list = schedule("2026-11-30 09:15")
        // checks that the 3rd does not match the 30th just because the text starts the same way
        assertNull(AirLabsRepository.pickScheduleEntry(list, "2026-11-03"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `cacheKey_noDate_returnsNormalisedFlightNumber`
    fun cacheKey_noDate_returnsNormalisedFlightNumber() {
        // checks that spaces are removed and letters are upper cased when there is no date
        assertEquals("BA123", AirLabsRepository.cacheKey("ba 123", null))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `cacheKey_withDate_appendsDate`
    fun cacheKey_withDate_appendsDate() {
        // checks that the date is added after the flight number
        assertEquals("BA123|2026-11-03", AirLabsRepository.cacheKey("BA123", "2026-11-03"))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `cacheKey_blankDate_sameAsNoDate`
    fun cacheKey_blankDate_sameAsNoDate() {
        // checks that a blank date gives the same key as no date
        assertEquals(AirLabsRepository.cacheKey("BA123", null), AirLabsRepository.cacheKey("BA123", "  "))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `cacheKey_sameFlightDifferentDays_differs`
    fun cacheKey_sameFlightDifferentDays_differs() {
        // checks that the same flight on two days gets two different keys, so one day's status is never shown for the other
        assertNotEquals(
            // continues the statement started above: `AirLabsRepository.cacheKey("BA123", "2026-11-03"),`
            AirLabsRepository.cacheKey("BA123", "2026-11-03"),
            // continues the statement started above: `AirLabsRepository.cacheKey("BA123", "2026-11-04"),`
            AirLabsRepository.cacheKey("BA123", "2026-11-04"),
        // closes the multi-line argument list started above
        )
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // declares test function `cacheKey_spacingAndCase_doNotChangeKey`
    fun cacheKey_spacingAndCase_doNotChangeKey() {
        // checks that typing the flight number differently still hits the same cache entry
        assertEquals(
            // continues the statement started above: `AirLabsRepository.cacheKey("BA123", "2026-11-03"),`
            AirLabsRepository.cacheKey("BA123", "2026-11-03"),
            // continues the statement started above: `AirLabsRepository.cacheKey("ba 123", "2026-11-03"),`
            AirLabsRepository.cacheKey("ba 123", "2026-11-03"),
        // closes the multi-line argument list started above
        )
    // closes the function
    }
// closes the class `AirLabsDateTest`
}
