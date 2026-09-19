// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `com.waypoint.app.features.home.data.WeatherRepository` for use in this file
import com.waypoint.app.features.home.data.WeatherRepository
// imports `org.junit.Assert.assertEquals` for use in this file
import org.junit.Assert.assertEquals
// imports `org.junit.Test` for use in this file
import org.junit.Test

// declares class `WeatherRepositoryTest` and opens its body
class WeatherRepositoryTest {

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `citySlug_convertsSpacesToUnderscoresAndLowercases` taking no parameters and opens its body
    fun citySlug_convertsSpacesToUnderscoresAndLowercases() {
        // declares read-only property `slug`, initialised with the result of calling `WeatherRepository.citySlug(…)`
        val slug = WeatherRepository.citySlug("Cape Town")
        // calls `assertEquals` with arguments `("cape_town", slug)`
        assertEquals("cape_town", slug)
    // closes the function `citySlug_convertsSpacesToUnderscoresAndLowercases`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `citySlug_trimsWhitespace` taking no parameters and opens its body
    fun citySlug_trimsWhitespace() {
        // declares read-only property `slug`, initialised with the result of calling `WeatherRepository.citySlug(…)`
        val slug = WeatherRepository.citySlug("  Durban  ")
        // calls `assertEquals` with arguments `("durban", slug)`
        assertEquals("durban", slug)
    // closes the function `citySlug_trimsWhitespace`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `citySlug_removesSpecialCharacters` taking no parameters and opens its body
    fun citySlug_removesSpecialCharacters() {
        // declares read-only property `slug`, initialised with the result of calling `WeatherRepository.citySlug(…)`
        val slug = WeatherRepository.citySlug("Port Elizabeth / Gqeberha")
        // calls `assertEquals` with arguments `("port_elizabeth_gqeberha", slug)`
        assertEquals("port_elizabeth_gqeberha", slug)
    // closes the function `citySlug_removesSpecialCharacters`
    }

    // annotation `@Test` applied to the declaration that follows
    @Test
    // declares function `citySlug_handlesSingleWordCity` taking no parameters and opens its body
    fun citySlug_handlesSingleWordCity() {
        // declares read-only property `slug`, initialised with the result of calling `WeatherRepository.citySlug(…)`
        val slug = WeatherRepository.citySlug("JOHANNESBURG")
        // calls `assertEquals` with arguments `("johannesburg", slug)`
        assertEquals("johannesburg", slug)
    // closes the function `citySlug_handlesSingleWordCity`
    }
// closes the class `WeatherRepositoryTest`
}
