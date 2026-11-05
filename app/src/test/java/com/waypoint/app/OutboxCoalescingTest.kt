package com.waypoint.app

import com.waypoint.app.core.sync.OutboxEntityType
import com.waypoint.app.core.sync.OutboxEntry
import com.waypoint.app.core.sync.OutboxOperation
import com.waypoint.app.core.sync.coalesce
import com.waypoint.app.core.sync.mergeJson
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

// Covers the outbox coalescing rules (OutboxRepository.coalesce/mergeJson)
// and peekAll's parent-before-child ordering (OutboxEntityType.syncDepth).
// coalesce()/mergeJson() are top-level functions, not OutboxRepository
// members, specifically so they're callable here without constructing
// OutboxRepository (its constructor needs a real Context) - this project's
// unit tests run on the plain JVM with no Robolectric.
//
// Not covered: coalesce()'s "unexpected transition" fallback branch (a
// DELETE followed by another op - not a real case today, see the comment
// on coalesce() itself) logs via android.util.Log, which isn't mocked in
// this test setup and would throw. Exercising only the five real,
// documented transitions avoids that without needing to change the
// project's test configuration for this one edge case.
class OutboxCoalescingTest {

    private fun entry(
        op: OutboxOperation,
        payload: String?,
        entityType: OutboxEntityType = OutboxEntityType.TRIP,
    ) = OutboxEntry(
        id = "outbox-1",
        entityType = entityType,
        entityId = "entity-1",
        operation = op,
        payloadJson = payload,
        createdAtMs = 0L,
        attempts = 0,
        lastError = null,
    )

    @Test
    fun nothingQueued_returnsNewOperationUnchanged() {
        val result = coalesce(null, OutboxOperation.CREATE, """{"name":"Trip"}""")
        assertEquals(OutboxOperation.CREATE to """{"name":"Trip"}""", result)
    }

    @Test
    fun createThenUpdate_mergesIntoASingleCreate() {
        val existing = entry(OutboxOperation.CREATE, """{"id":"t1","name":"Old"}""")
        val result = coalesce(existing, OutboxOperation.UPDATE, """{"name":"New"}""")

        assertEquals(OutboxOperation.CREATE, result?.first)
        val merged = JSONObject(result!!.second!!)
        assertEquals("t1", merged.getString("id"))
        assertEquals("New", merged.getString("name"))
    }

    @Test
    fun createThenDelete_cancelsOutToNothing() {
        val existing = entry(OutboxOperation.CREATE, """{"id":"t1"}""")
        val result = coalesce(existing, OutboxOperation.DELETE, null)
        assertNull("A create that never synced, then deleted, should leave nothing queued", result)
    }

    @Test
    fun updateThenUpdate_mergesRatherThanReplacing() {
        // This is the scenario that actually matters: setting a flight's
        // number, then separately setting its departure time, must not
        // lose the first field. (This exact bug existed in an earlier
        // version of coalesce() before it was fixed to merge here too.)
        val existing = entry(OutboxOperation.UPDATE, """{"flightNumber":"NH204"}""")
        val result = coalesce(existing, OutboxOperation.UPDATE, """{"departureTime":"09:45"}""")

        assertEquals(OutboxOperation.UPDATE, result?.first)
        val merged = JSONObject(result!!.second!!)
        assertEquals("NH204", merged.getString("flightNumber"))
        assertEquals("09:45", merged.getString("departureTime"))
    }

    @Test
    fun updateThenUpdate_sameFieldTwice_latestValueWins() {
        val existing = entry(OutboxOperation.UPDATE, """{"flightNumber":"AB111"}""")
        val result = coalesce(existing, OutboxOperation.UPDATE, """{"flightNumber":"AB222"}""")

        val merged = JSONObject(result!!.second!!)
        assertEquals("AB222", merged.getString("flightNumber"))
    }

    @Test
    fun updateThenDelete_becomesACleanDelete() {
        val existing = entry(OutboxOperation.UPDATE, """{"name":"New"}""")
        val result = coalesce(existing, OutboxOperation.DELETE, null)
        assertEquals(OutboxOperation.DELETE to null, result)
    }

    @Test
    fun mergeJson_nullUpdates_keepsBaseUnchanged() {
        assertEquals("""{"a":1}""", mergeJson("""{"a":1}""", null))
    }

    @Test
    fun mergeJson_nullBase_returnsUpdatesAsIs() {
        assertEquals("""{"a":1}""", mergeJson(null, """{"a":1}"""))
    }

    @Test
    fun syncDepth_ordersParentsBeforeChildren() {
        // peekAll() sorts by this - a day must never be sent before its
        // trip, nor a flight/place before its day.
        assertEquals(0, OutboxEntityType.TRIP.syncDepth)
        assertEquals(1, OutboxEntityType.DAY.syncDepth)
        assertEquals(2, OutboxEntityType.FLIGHT.syncDepth)
        assertEquals(2, OutboxEntityType.LODGING.syncDepth)
        assertEquals(2, OutboxEntityType.CAR.syncDepth)
        assertEquals(2, OutboxEntityType.PLACE.syncDepth)

        val unordered = listOf(OutboxEntityType.PLACE, OutboxEntityType.TRIP, OutboxEntityType.DAY, OutboxEntityType.FLIGHT)
        val sorted = unordered.sortedBy { it.syncDepth }
        assertEquals(listOf(OutboxEntityType.TRIP, OutboxEntityType.DAY, OutboxEntityType.PLACE, OutboxEntityType.FLIGHT), sorted)
    }
}
