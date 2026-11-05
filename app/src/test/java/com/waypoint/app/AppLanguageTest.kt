package com.waypoint.app

import com.waypoint.app.core.locale.AppLanguage
import org.junit.Assert.assertEquals
import org.junit.Test

class AppLanguageTest {

    @Test
    fun appLanguage_verifyTagsAndNames() {
        assertEquals("en", AppLanguage.ENGLISH.localeTag)
        assertEquals("English", AppLanguage.ENGLISH.displayName)

        assertEquals("zu", AppLanguage.ZULU.localeTag)
        assertEquals("isiZulu", AppLanguage.ZULU.displayName)

        assertEquals("xh", AppLanguage.XHOSA.localeTag)
        assertEquals("isiXhosa", AppLanguage.XHOSA.displayName)
    }

    @Test
    fun appLanguage_verifyAllLanguagesPresent() {
        val languages = AppLanguage.entries.map { it.localeTag }
        assertEquals(3, languages.size)
        assertEquals(listOf("en", "zu", "xh"), languages)
    }
}
