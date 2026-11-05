package com.waypoint.app.core.locale

import androidx.appcompat.app.AppCompatDelegate

/**
 * The languages the app is translated into, with the locale tag passed to
 * AppCompatDelegate.setApplicationLocales().
 */
enum class AppLanguage(val displayName: String, val localeTag: String) {
    ENGLISH("English", "en"),
    ZULU("isiZulu", "zu"),
    XHOSA("isiXhosa", "xh"),
    ;

    companion object {
        fun current(): AppLanguage {
            val tag = AppCompatDelegate.getApplicationLocales().toLanguageTags()
            return entries.firstOrNull { tag.startsWith(it.localeTag) } ?: ENGLISH
        }
    }
}