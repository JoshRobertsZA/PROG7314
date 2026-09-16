package com.example.prog7314.core.locale

import androidx.appcompat.app.AppCompatDelegate

/**
 * The languages offered in LanguageModal, each paired with its real
 * BCP-47/ISO locale tag. Backs both the modal's radio rows and the
 * actual runtime locale switch (see [apply] and [current]).
 *
 * Locale tags: "en" (English), "zu" (isiZulu), "xh" (isiXhosa).
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