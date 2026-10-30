package com.waypoint.app.core.auth

import com.waypoint.app.R

// Pure validation, no Android Context dependency beyond the R class (just
// compile-time ints), so PR 8 commit 6 can unit test this directly without
// Robolectric - same reasoning as isUsablePlacesCache in LocationIQRepository.
object EmailAuthValidator {

    private val EMAIL_PATTERN = Regex("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")
    private const val MIN_PASSWORD_LENGTH = 8

    fun validateSignIn(email: String, password: String): Int? {
        emailError(email)?.let { return it }
        if (password.isBlank()) return R.string.auth_error_password_required
        return null
    }

    fun validateSignUp(displayName: String, email: String, password: String, confirmPassword: String): Int? {
        if (displayName.isBlank()) return R.string.auth_error_display_name_required
        emailError(email)?.let { return it }
        if (password.isBlank()) return R.string.auth_error_password_required
        if (password.length < MIN_PASSWORD_LENGTH) return R.string.auth_error_password_too_short
        if (password != confirmPassword) return R.string.auth_error_passwords_dont_match
        return null
    }

    fun validateReset(email: String): Int? = emailError(email)

    private fun emailError(email: String): Int? {
        if (email.isBlank()) return R.string.auth_error_email_required
        if (!EMAIL_PATTERN.matches(email.trim())) return R.string.auth_error_invalid_email
        return null
    }
}
