package com.waypoint.app.core.common

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

/**
 * Resolves the human-readable file name of a content:// document picked
 * with the system file picker. Falls back to the last path segment, which
 * is what the UI used to show ("document:1000094955") when the provider
 * doesn't expose DISPLAY_NAME.
 */
object DocumentNames {
    fun displayName(context: Context, uriString: String): String {
        val uri = Uri.parse(uriString)
        try {
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (idx >= 0 && c.moveToFirst() && !c.isNull(idx)) {
                    val name = c.getString(idx)
                    if (name.isNotBlank()) return name
                }
            }
        } catch (_: Exception) { /* provider gone or no permission */ }
        val seg = uri.lastPathSegment ?: uriString
        return seg.substringAfterLast('/').ifBlank { seg }
    }
}
