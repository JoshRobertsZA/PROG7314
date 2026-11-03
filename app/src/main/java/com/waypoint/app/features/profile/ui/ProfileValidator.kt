package com.waypoint.app.features.profile.ui

import com.waypoint.app.R

// Pure, no Android Context dependency beyond the R class, so PR 9 commit 7
// can unit test this directly - same reasoning as EmailAuthValidator.
object ProfileValidator {

    private const val MAX_NAME_LENGTH = 60

    fun validateDisplayName(name: String): Int? {
        if (name.isBlank()) return R.string.profile_edit_error_name_required
        if (name.trim().length > MAX_NAME_LENGTH) return R.string.profile_edit_error_name_too_long
        return null
    }
}
