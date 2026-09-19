// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.core.locale.AppLanguage` for use in this file
import com.waypoint.app.core.locale.AppLanguage
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Test` for use in this file
import org.junit.Test

// declares class `AppLanguageTest` and opens its body
class AppLanguageTest {

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `appLanguage_verifyTagsAndNames` taking no parameters and opens its body
    fun appLanguage_verifyTagsAndNames() {
        // calls `assertEquals` with arguments `("en", AppLanguage.ENGLISH.localeTag)`
        assertEquals("en", AppLanguage.ENGLISH.localeTag)
        // calls `assertEquals` with arguments `("English", AppLanguage.ENGLISH.displayName)`
        assertEquals("English", AppLanguage.ENGLISH.displayName)

        // calls `assertEquals` with arguments `("zu", AppLanguage.ZULU.localeTag)`
        assertEquals("zu", AppLanguage.ZULU.localeTag)
        // calls `assertEquals` with arguments `("isiZulu", AppLanguage.ZULU.displayName)`
        assertEquals("isiZulu", AppLanguage.ZULU.displayName)

        // calls `assertEquals` with arguments `("xh", AppLanguage.XHOSA.localeTag)`
        assertEquals("xh", AppLanguage.XHOSA.localeTag)
        // calls `assertEquals` with arguments `("isiXhosa", AppLanguage.XHOSA.displayName)`
        assertEquals("isiXhosa", AppLanguage.XHOSA.displayName)
    // closes the function `appLanguage_verifyTagsAndNames`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `appLanguage_verifyAllLanguagesPresent` taking no parameters and opens its body
    fun appLanguage_verifyAllLanguagesPresent() {
        // declares read-only property `languages`, initialised to `AppLanguage.entries.map { it.localeTag }`
        val languages = AppLanguage.entries.map { it.localeTag }
        // calls `assertEquals` with arguments `(3, languages.size)`
        assertEquals(3, languages.size)
        // calls `assertEquals` with arguments `(listOf("en", "zu", "xh"), languages)`
        assertEquals(listOf("en", "zu", "xh"), languages)
    // closes the function `appLanguage_verifyAllLanguagesPresent`
    }
// closes the class `AppLanguageTest`
}
