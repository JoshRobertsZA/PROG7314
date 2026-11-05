package com.waypoint.app

import com.waypoint.app.features.home.data.WeatherRepository
import org.junit.Assert.assertEquals
import org.junit.Test

class WeatherRepositoryTest {

    @Test
    fun citySlug_convertsSpacesToUnderscoresAndLowercases() {
        val slug = WeatherRepository.citySlug("Cape Town")
        assertEquals("cape_town", slug)
    }

    @Test
    fun citySlug_trimsWhitespace() {
        val slug = WeatherRepository.citySlug("  Durban  ")
        assertEquals("durban", slug)
    }

    @Test
    fun citySlug_removesSpecialCharacters() {
        val slug = WeatherRepository.citySlug("Port Elizabeth / Gqeberha")
        assertEquals("port_elizabeth_gqeberha", slug)
    }

    @Test
    fun citySlug_handlesSingleWordCity() {
        val slug = WeatherRepository.citySlug("JOHANNESBURG")
        assertEquals("johannesburg", slug)
    }
}
