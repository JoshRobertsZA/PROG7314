// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `androidx.test.platform.app.InstrumentationRegistry` for use in this file
import androidx.test.platform.app.InstrumentationRegistry
// imports `androidx.test.ext.junit.runners.AndroidJUnit4` for use in this file
import androidx.test.ext.junit.runners.AndroidJUnit4

// imports `org.junit.Test` for use in this file
import org.junit.Test
// imports `org.junit.runner.RunWith` for use in this file
import org.junit.runner.RunWith

// imports `org.junit.Assert.*` for use in this file
import org.junit.Assert.*

// continues the statement started above: `@RunWith(AndroidJUnit4::class)`
@RunWith(AndroidJUnit4::class)
// declares class `ExampleInstrumentedTest` and opens its body
class ExampleInstrumentedTest {
    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `useAppContext` taking no parameters and opens its body
    fun useAppContext() {
        // declares read-only property `appContext`, initialised with the result of calling `InstrumentationRegistry.getInstrumentation(…)`
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        // calls `assertEquals` with arguments `("com.waypoint.app", appContext.packageName)`
        assertEquals("com.waypoint.app", appContext.packageName)
    // closes the function `useAppContext`
    }
// closes the class `ExampleInstrumentedTest`
}