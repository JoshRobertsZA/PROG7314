package com.waypoint.app

import com.waypoint.app.core.network.shouldEndSession
import com.waypoint.app.core.settings.Settings
import com.waypoint.app.core.settings.SettingsSyncAction
import com.waypoint.app.core.settings.resolveSettingsConflict
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/** Covers how settings reconcile with the API and when an API response ends the session. */
class SettingsSyncRulesTest {

    @Test
    fun newerCopyOnTheServerIsApplied() {
        assertEquals(SettingsSyncAction.APPLY_REMOTE, resolveSettingsConflict(localUpdatedAtMs = 1_000, remoteUpdatedAtMs = 2_000))
    }

    @Test
    fun newerCopyOnThePhoneIsUploaded() {
        assertEquals(SettingsSyncAction.UPLOAD_LOCAL, resolveSettingsConflict(localUpdatedAtMs = 3_000, remoteUpdatedAtMs = 2_000))
    }

    @Test
    fun aServerWithNothingSavedReceivesThePhonesChanges() {
        // GET /account/settings reports updatedAtMs 0 until the first PUT.
        assertEquals(SettingsSyncAction.UPLOAD_LOCAL, resolveSettingsConflict(localUpdatedAtMs = 1, remoteUpdatedAtMs = 0))
    }

    @Test
    fun identicalTimestampsNeedNoRequest() {
        assertEquals(SettingsSyncAction.NONE, resolveSettingsConflict(localUpdatedAtMs = 5_000, remoteUpdatedAtMs = 5_000))
    }

    @Test
    fun defaultsMatchTheApisDefaults() {
        val defaults = Settings()
        assertNull(defaults.darkMode)
        assertTrue(defaults.notifyTripUpdates)
        assertTrue(defaults.notifyReminders)
        assertFalse(defaults.biometricLock)
        assertEquals("ZAR", defaults.defaultCurrency)
        assertEquals(0L, defaults.updatedAtMs)
    }

    @Test
    fun unauthorisedFromOurApiEndsTheSession() {
        assertTrue(shouldEndSession(401, "api.example.app", "api.example.app", hadBearerToken = true))
    }

    @Test
    fun otherFailuresOrOtherHostsDoNotEndTheSession() {
        // A third-party API rejecting its key says nothing about the user's sign-in.
        assertFalse(shouldEndSession(401, "api.weather.example", "api.example.app", hadBearerToken = true))
        // Public routes such as /health carry no token.
        assertFalse(shouldEndSession(401, "api.example.app", "api.example.app", hadBearerToken = false))
        assertFalse(shouldEndSession(403, "api.example.app", "api.example.app", hadBearerToken = true))
        assertFalse(shouldEndSession(500, "api.example.app", "api.example.app", hadBearerToken = true))
        assertFalse(shouldEndSession(401, "api.example.app", null, hadBearerToken = true))
    }
}
