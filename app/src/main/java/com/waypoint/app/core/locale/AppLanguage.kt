// declares that this file belongs to the package `com.waypoint.app.core.locale`
package com.waypoint.app.core.locale

// imports `androidx.appcompat.app.AppCompatDelegate` for use in this file
import androidx.appcompat.app.AppCompatDelegate

// declares enum class `AppLanguage` with a primary constructor taking 2 parameters (`displayName`, `localeTag`) and opens its body
enum class AppLanguage(val displayName: String, val localeTag: String) {
    // calls `ENGLISH` with arguments `("English", "en")`
    ENGLISH("English", "en"),
    // continues the statement started above: `ZULU("isiZulu", "zu"),`
    ZULU("isiZulu", "zu"),
    // continues the statement started above: `XHOSA("isiXhosa", "xh"),`
    XHOSA("isiXhosa", "xh"),
    // continues the statement started above: `;`
    ;

    // declares the companion object holding members shared by all instances of the enclosing class
    companion object {
        // declares function `current` taking no parameters, returning `AppLanguage` and opens its body
        fun current(): AppLanguage {
            // declares read-only property `tag`, initialised with the result of calling `AppCompatDelegate.getApplicationLocales(…)`
            val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            // returns `entries.firstOrNull { tag.startsWith(it.localeTag) } ?: ENGLISH` from the current function
            return entries.firstOrNull { tag.startsWith(it.localeTag) } ?: ENGLISH
        // closes the function `current`
        }
    // closes the companion object
    }
// closes the class `AppLanguage`
}