package com.waypoint.app

import org.junit.Assert.assertTrue
import org.junit.Test
import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory

// Makes sure isiZulu and isiXhosa have exactly the same translatable keys as English.
// Fails if a key is missing (would show English in that language) or extra (stale key).

class TranslationParityTest {

    // Unit tests run from the app/ module folder, so this path points at app/src/main/res
    private val resDir = File("src/main/res")

     // Reads a strings.xml file and returns every translatable key, prefixed with its tag
     // (e.g. "string/biometric_lock_title", "plurals/day_count").
     // Keys marked translatable="false" (glyphs, ids) are skipped.

    private fun translatableKeys(folder: String): Set<String> {
        val file = File(resDir, "$folder/strings.xml")
        assertTrue("Missing ${file.path}", file.exists())
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(file)
        val keys = mutableSetOf<String>()
        // Check normal strings, plurals and arrays
        for (tag in listOf("string", "plurals", "string-array")) {
            val nodes = doc.getElementsByTagName(tag)
            for (i in 0 until nodes.length) {
                val el = nodes.item(i) as Element
                if (el.getAttribute("translatable") != "false") {
                    keys += "$tag/${el.getAttribute("name")}"
                }
            }
        }
        return keys
    }

    // Compares one language folder against English and lists any differences in the failure message.
    private fun assertMatchesEnglish(folder: String) {
        val en = translatableKeys("values")
        val other = translatableKeys(folder)
        val missing = en - other   // in English but not translated
        val extra = other - en     // translated but no longer in English
        assertTrue("$folder is missing keys: $missing", missing.isEmpty())
        assertTrue("$folder has keys English doesn't: $extra", extra.isEmpty())
    }

    @Test
    fun zulu_keysMatchEnglish() = assertMatchesEnglish("values-zu")

    @Test
    fun xhosa_keysMatchEnglish() = assertMatchesEnglish("values-xh")
}