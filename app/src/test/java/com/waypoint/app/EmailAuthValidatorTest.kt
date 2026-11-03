package com.waypoint.app

import com.waypoint.app.core.auth.EmailAuthValidator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class EmailAuthValidatorTest {

    @Test
    fun validateSignIn_blankEmail_requiresEmail() {
        assertEquals(R.string.auth_error_email_required, EmailAuthValidator.validateSignIn("", "password123"))
    }

    @Test
    fun validateSignIn_malformedEmail_isInvalid() {
        assertEquals(R.string.auth_error_invalid_email, EmailAuthValidator.validateSignIn("not-an-email", "password123"))
    }

    @Test
    fun validateSignIn_blankPassword_requiresPassword() {
        assertEquals(R.string.auth_error_password_required, EmailAuthValidator.validateSignIn("a@b.com", ""))
    }

    @Test
    fun validateSignIn_validFields_passes() {
        assertNull(EmailAuthValidator.validateSignIn("a@b.com", "password123"))
    }

    @Test
    fun validateSignUp_blankDisplayName_requiresName() {
        val error = EmailAuthValidator.validateSignUp("", "a@b.com", "password123", "password123")
        assertEquals(R.string.auth_error_display_name_required, error)
    }

    @Test
    fun validateSignUp_shortPassword_isTooShort() {
        val error = EmailAuthValidator.validateSignUp("Jane", "a@b.com", "short1", "short1")
        assertEquals(R.string.auth_error_password_too_short, error)
    }

    @Test
    fun validateSignUp_mismatchedConfirmPassword_failsMatchCheck() {
        val error = EmailAuthValidator.validateSignUp("Jane", "a@b.com", "password123", "password456")
        assertEquals(R.string.auth_error_passwords_dont_match, error)
    }

    @Test
    fun validateSignUp_validFields_passes() {
        assertNull(EmailAuthValidator.validateSignUp("Jane", "a@b.com", "password123", "password123"))
    }

    @Test
    fun validateReset_blankEmail_requiresEmail() {
        assertEquals(R.string.auth_error_email_required, EmailAuthValidator.validateReset(""))
    }

    @Test
    fun validateReset_validEmail_passes() {
        assertNull(EmailAuthValidator.validateReset("a@b.com"))
    }
}
