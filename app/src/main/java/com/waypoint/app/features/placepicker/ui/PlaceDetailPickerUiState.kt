package com.waypoint.app.features.placepicker.ui

import com.waypoint.app.core.cache.WeatherCache
import android.graphics.Bitmap
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository.WikipediaSummary

/** State of the place detail screen. */
data class PlaceDetailPickerUiState(
    val placeName    : String  = "",
    val placeType    : String  = "",
    val placeEmoji   : String  = "",
    val lat          : Double  = 0.0,
    val lon          : Double  = 0.0,
    val distanceKm   : String  = "",

    val weather      : WeatherCache?      = null,
    val wikiSummary  : WikipediaSummary?  = null,
    val isLoadingWiki: Boolean            = true,
    val photoBitmap  : Bitmap?            = null,
    val isLoadingWeather: Boolean         = true,

    val isAdding     : Boolean = false,
    val isAdded      : Boolean = false,
    // True when the place is missing from the cache, so the screen shows an error
    val notFound     : Boolean = false,
)
