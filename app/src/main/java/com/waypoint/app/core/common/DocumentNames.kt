// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.net.Uri` for use in this file
import android.net.Uri
// imports `android.provider.OpenableColumns` for use in this file
import android.provider.OpenableColumns

// declares object `DocumentNames` and opens its body
object DocumentNames {
    // declares function `displayName` taking 2 parameters (`context`, `uriString`), returning `String` and opens its body
    fun displayName(context: Context, uriString: String): String {
        // declares read-only property `uri`, initialised with the result of calling `Uri.parse(…)`
        val uri = Uri.parse(uriString)
        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // calls `query` on `context.contentResolver` with arguments `(uri, arrayOf(OpenableColumns.DISPLAY_NAME), …)`, then chains `.?.use { c ->`
            context.contentResolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { c ->
                // continues the statement started above: `val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)`
                val idx = c.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                // `if` statement: the block below runs when `idx >= 0 && c.moveToFirst() && !c.isNull(idx)` is true
                if (idx >= 0 && c.moveToFirst() && !c.isNull(idx)) {
                    // declares read-only property `name`, initialised with the result of calling `c.getString(…)`
                    val name = c.getString(idx)
                    // `if` statement: executes `return name` when `name.isNotBlank()` is true
                    if (name.isNotBlank()) return name
                // closes the if block
                }
            // closes the block
            }
        // expression: `} catch (_: Exception) { }`
        } catch (_: Exception) {  }
        // declares read-only property `seg`, initialised to `uri.lastPathSegment ?: uriString`
        val seg = uri.lastPathSegment ?: uriString
        // returns `seg.substringAfterLast('/').ifBlank { seg }` from the current function
        return seg.substringAfterLast('/').ifBlank { seg }
    // closes the function `displayName`
    }
// closes the object `DocumentNames`
}
