// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.network.GuardedApi` for use in this file
import com.waypoint.app.core.network.GuardedApi
// imports `com.waypoint.app.core.network.UsageGuardian` for use in this file
import com.waypoint.app.core.network.UsageGuardian
// imports `com.waypoint.app.core.network.UsageSnapshot` for use in this file
import com.waypoint.app.core.network.UsageSnapshot
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Assert.assertFalse` for use in this file
import org.junit.Assert.assertFalse
// imports `org.junit.Assert.assertNull` for use in this file
import org.junit.Assert.assertNull
// imports `org.junit.Assert.assertTrue` for use in this file
import org.junit.Assert.assertTrue
// imports `org.junit.Test` for use in this file
import org.junit.Test
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// declares class `UsageGuardianTest` and opens its body; covers the budget thresholds, counter names, response parsing and the meter maths
class UsageGuardianTest {

    // declares private function `liq` building a LocationIQ snapshot with the given daily count
    private fun liq(daily: Int) = UsageSnapshot(GuardedApi.LOCATIONIQ, daily, daily)

    // declares private function `air` building an AirLabs snapshot with the given monthly count
    private fun air(monthly: Int) = UsageSnapshot(GuardedApi.AIRLABS, monthly, monthly)

    // marks the next function as a unit test
    @Test
    // LocationIQ is allowed right up to 90% of 5,000 (4,500) and refused after that
    fun locationiq_blocksPastNinetyPercentOfDailyLimit() {
        // 4,499 used plus 1 needed lands exactly on 4,500, so it is still allowed
        assertTrue(UsageGuardian.isWithinBudget(liq(4_499)))
        // 4,500 used plus 1 needed would reach 4,501, so it is refused
        assertFalse(UsageGuardian.isWithinBudget(liq(4_500)))
    }

    // marks the next function as a unit test
    @Test
    // AirLabs is allowed right up to 90% of 1,000 (900) per month and refused after that
    fun airlabs_blocksPastNinetyPercentOfMonthlyLimit() {
        // 899 used plus 1 needed lands exactly on 900, so it is still allowed
        assertTrue(UsageGuardian.isWithinBudget(air(899)))
        // 900 used plus 1 needed would reach 901, so it is refused
        assertFalse(UsageGuardian.isWithinBudget(air(900)))
    }

    // marks the next function as a unit test
    @Test
    // a request that costs several calls is refused when the whole cost does not fit, even if one call would
    fun multiCallRequest_needsTheWholeCostToFit() {
        // 4,494 used plus 6 needed is exactly 4,500, so a nearby search is allowed
        assertTrue(UsageGuardian.isWithinBudget(liq(4_494), needed = 6))
        // 4,495 used plus 6 needed is 4,501, so it is refused
        assertFalse(UsageGuardian.isWithinBudget(liq(4_495), needed = 6))
    }

    // marks the next function as a unit test
    @Test
    // an API only looks at the limit it has: LocationIQ ignores the monthly count and AirLabs ignores the daily count
    fun onlyTheLimitThatExistsIsChecked() {
        // LocationIQ has no monthly limit, so a huge monthly count does not block it
        assertTrue(UsageGuardian.isWithinBudget(UsageSnapshot(GuardedApi.LOCATIONIQ, 0, 999_999)))
        // AirLabs has no daily limit, so a huge daily count does not block it
        assertTrue(UsageGuardian.isWithinBudget(UsageSnapshot(GuardedApi.AIRLABS, 999_999, 0)))
    }

    // marks the next function as a unit test
    @Test
    // each API has one fixed counter, and the window key rolls over per day for LocationIQ and per month for AirLabs
    fun counterNamesAreFixed_andWindowsRollOver() {
        // a fixed date used for all the checks
        val date = LocalDate.of(2026, 3, 7)
        // the counters are the fixed names created in the dashboard
        assertEquals("wp-locationiq", UsageGuardian.counterName(GuardedApi.LOCATIONIQ))
        // same for AirLabs
        assertEquals("wp-airlabs", UsageGuardian.counterName(GuardedApi.AIRLABS))
        // LocationIQ's window is the day
        assertEquals("20260307", UsageGuardian.windowKey(GuardedApi.LOCATIONIQ, date))
        // AirLabs' window is the month
        assertEquals("202603", UsageGuardian.windowKey(GuardedApi.AIRLABS, date))
        // the next day is a new LocationIQ window
        assertFalse(UsageGuardian.windowKey(GuardedApi.LOCATIONIQ, date) == UsageGuardian.windowKey(GuardedApi.LOCATIONIQ, date.plusDays(1)))
        // the next day is still the same AirLabs window
        assertEquals(UsageGuardian.windowKey(GuardedApi.AIRLABS, date), UsageGuardian.windowKey(GuardedApi.AIRLABS, date.plusDays(1)))
    }

    // marks the next function as a unit test
    @Test
    // usage in a window is the running total minus the total at the start of the window, and never negative
    fun windowedUsage_isTotalMinusBaseline() {
        // 1,250 now against a baseline of 1,000 means 250 used
        assertEquals(250, UsageGuardian.windowedUsage(1_250, 1_000))
        // a counter that was reset below the baseline counts as 0
        assertEquals(0, UsageGuardian.windowedUsage(10, 1_000))
    }

    // marks the next function as a unit test
    @Test
    // the response parser accepts the shapes CounterAPI might return and returns null for anything else, which makes the guardian fail open
    fun parseCount_isTolerant() {
        // count nested under `data.up_count`
        assertEquals(42, UsageGuardian.parseCount("""{"data":{"up_count":42}}"""))
        // count at the top level
        assertEquals(7, UsageGuardian.parseCount("""{"count":7}"""))
        // count under `value`
        assertEquals(3, UsageGuardian.parseCount("""{"value":3}"""))
        // unknown shape gives null
        assertNull(UsageGuardian.parseCount("""{"hello":"world"}"""))
        // not JSON at all gives null
        assertNull(UsageGuardian.parseCount("not json"))
    }

    // marks the next function as a unit test
    @Test
    // the meter uses the daily count for LocationIQ, the monthly count for AirLabs, and never goes past 1
    fun snapshot_fractionMatchesTheApplicableLimit() {
        // 2,500 of 5,000 is half
        assertEquals(0.5f, liq(2_500).fraction, 0.0001f)
        // 250 of 1,000 is a quarter
        assertEquals(0.25f, air(250).fraction, 0.0001f)
        // going over the limit is capped at full
        assertEquals(1f, liq(9_000).fraction, 0.0001f)
        // the limit shown matches the API
        assertEquals(5_000, liq(0).limit)
        // the used value follows the right counter
        assertEquals(250, UsageSnapshot(GuardedApi.AIRLABS, 5, 250).used)
    }
// closes the class `UsageGuardianTest`
}
