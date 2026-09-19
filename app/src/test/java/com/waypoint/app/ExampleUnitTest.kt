// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `org.junit.Test` for use in this file
import org.junit.Test

// imports `org.junit.Assert.*` for use in this file
import org.junit.Assert.*

// continues the statement started above: `class ExampleUnitTest {`
class ExampleUnitTest {
    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `addition_isCorrect` taking no parameters and opens its body
    fun addition_isCorrect() {
        // calls `assertEquals` with arguments `(4, 2 + 2)`
        assertEquals(4, 2 + 2)
    // closes the function `addition_isCorrect`
    }
// closes the block
}