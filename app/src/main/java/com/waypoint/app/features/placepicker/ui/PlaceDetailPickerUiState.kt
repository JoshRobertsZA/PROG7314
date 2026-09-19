// declares that this file belongs to the package `com.waypoint.app.features.placepicker.ui`
package com.waypoint.app.features.placepicker.ui

// imports `com.waypoint.app.core.cache.WeatherCache` for use in this file
import com.waypoint.app.core.cache.WeatherCache
// imports `android.graphics.Bitmap` for use in this file
import android.graphics.Bitmap
// imports `com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository.WikipediaSummary` for use in this file
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository.WikipediaSummary

// expression: `data class PlaceDetailPickerUiState(`
data class PlaceDetailPickerUiState(
    // continues the statement started above: `val placeName : String = "",`
    val placeName    : String  = "",
    // continues the statement started above: `val placeType : String = "",`
    val placeType    : String  = "",
    // continues the statement started above: `val placeEmoji : String = "",`
    val placeEmoji   : String  = "",
    // continues the statement started above: `val lat : Double = 0.0,`
    val lat          : Double  = 0.0,
    // continues the statement started above: `val lon : Double = 0.0,`
    val lon          : Double  = 0.0,
    // continues the statement started above: `val distanceKm : String = "",`
    val distanceKm   : String  = "",

    // continues the statement started above: `val weather : WeatherCache? = null,`
    val weather      : WeatherCache?      = null,
    // continues the statement started above: `val wikiSummary : WikipediaSummary? = null,`
    val wikiSummary  : WikipediaSummary?  = null,
    // continues the statement started above: `val isLoadingWiki: Boolean = true,`
    val isLoadingWiki: Boolean            = true,
    // continues the statement started above: `val photoBitmap : Bitmap? = null,`
    val photoBitmap  : Bitmap?            = null,
    // continues the statement started above: `val isLoadingWeather: Boolean = true,`
    val isLoadingWeather: Boolean         = true,

    // continues the statement started above: `val isAdding : Boolean = false,`
    val isAdding     : Boolean = false,
    // continues the statement started above: `val isAdded : Boolean = false,`
    val isAdded      : Boolean = false,
// closes the multi-line argument list started above
)
