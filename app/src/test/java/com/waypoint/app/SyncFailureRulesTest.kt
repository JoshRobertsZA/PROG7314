// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.sync.isPermanentFailure` for use in this file
import com.waypoint.app.core.sync.isPermanentFailure
// imports `org.junit.Assert.assertFalse` for use in this file
import org.junit.Assert.assertFalse
// imports `org.junit.Assert.assertTrue` for use in this file
import org.junit.Assert.assertTrue
// imports `org.junit.Test` for use in this file
import org.junit.Test

// Covers which HTTP results make SyncWorker drop an outbox entry and which make it retry.
class SyncFailureRulesTest {

    // annotation `@Test` applied to the function that follows
    @Test
    // the server rejected the change itself, so sending it again will not help
    fun rejectedChangesAreDropped() {
        // 400 bad request
        assertTrue(isPermanentFailure(400))
        // 403 forbidden
        assertTrue(isPermanentFailure(403))
        // 404 not found
        assertTrue(isPermanentFailure(404))
        // 409 conflict, for example a create that already went through
        assertTrue(isPermanentFailure(409))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // these 4xx results are temporary, so the change must stay in the outbox
    fun expiredTokenTimeoutAndRateLimitAreRetried() {
        // 401 expired sign-in token, fixed by the next token refresh
        assertFalse(isPermanentFailure(401))
        // 408 request timeout
        assertFalse(isPermanentFailure(408))
        // 429 too many requests
        assertFalse(isPermanentFailure(429))
    // closes the function
    }

    // annotation `@Test` applied to the function that follows
    @Test
    // server errors and a missing connection are not the change's fault, so retry
    fun serverAndNetworkFailuresAreRetried() {
        // 500 internal server error
        assertFalse(isPermanentFailure(500))
        // 503 service unavailable
        assertFalse(isPermanentFailure(503))
        // 0 means the request never reached the server
        assertFalse(isPermanentFailure(0))
    // closes the function
    }
// closes the class
}
