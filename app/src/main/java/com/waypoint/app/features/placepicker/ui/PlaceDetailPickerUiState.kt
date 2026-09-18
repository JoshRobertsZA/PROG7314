package com.waypoint.app.features.placepicker.ui

import com.waypoint.app.core.cache.WeatherCache
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository.WikipediaSummary

data class PlaceDetailPickerUiState(
    // Core place info resolved from the LocationIQ cache
    val placeName    : String  = "",
    val placeType    : String  = "",
    val placeEmoji   : String  = "",
    val lat          : Double  = 0.0,
    val lon          : Double  = 0.0,
    val distanceKm   : String  = "",

    // Async loads
    val weather      : WeatherCache?      = null,
    val wikiSummary  : WikipediaSummary?  = null,
    val isLoadingWiki: Boolean            = true,
    val isLoadingWeather: Boolean         = true,

    // Action state
    val isAdding     : Boolean = false,
    val isAdded      : Boolean = false,   // triggers pop-back in the screen
)
