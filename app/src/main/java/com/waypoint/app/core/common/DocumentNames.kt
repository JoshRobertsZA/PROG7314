package com.waypoint.app.core.common

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns

/** Reads the file name of a picked booking PDF from its content URI, for display on itinerary cards. */
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
        } catch (_: Exception) {  }
        val seg = uri.lastPathSegment ?: uriString
        return seg.substringAfterLast('/').ifBlank { seg }
    }
}
