package com.waypoint.app

import com.waypoint.app.features.profile.ui.ProfileValidator
import com.waypoint.app.features.profile.ui.canReauthenticate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ProfileTest {

    @Test
    fun validateDisplayName_blank_requiresName() {
        assertEquals(R.string.profile_edit_error_name_required, ProfileValidator.validateDisplayName(""))
    }

    @Test
    fun validateDisplayName_whitespaceOnly_requiresName() {
        assertEquals(R.string.profile_edit_error_name_required, ProfileValidator.validateDisplayName("   "))
    }

    @Test
    fun validateDisplayName_tooLong_isRejected() {
        val name = "a".repeat(61)
        assertEquals(R.string.profile_edit_error_name_too_long, ProfileValidator.validateDisplayName(name))
    }

    @Test
    fun validateDisplayName_atMaxLength_passes() {
        val name = "a".repeat(60)
        assertNull(ProfileValidator.validateDisplayName(name))
    }

    @Test
    fun validateDisplayName_normalName_passes() {
        assertNull(ProfileValidator.validateDisplayName("Jane Doe"))
    }

    @Test
    fun canReauthenticate_passwordProvider_isTrue() {
        assertTrue(canReauthenticate(listOf("password")))
    }

    @Test
    fun canReauthenticate_googleProvider_isTrue() {
        assertTrue(canReauthenticate(listOf("google.com")))
    }

    @Test
    fun canReauthenticate_bothProviders_isTrue() {
        assertTrue(canReauthenticate(listOf("password", "google.com")))
    }

    @Test
    fun canReauthenticate_githubOnly_isFalse() {
        assertFalse(canReauthenticate(listOf("github.com")))
    }

    @Test
    fun canReauthenticate_noProviders_isFalse() {
        assertFalse(canReauthenticate(emptyList()))
    }
}
