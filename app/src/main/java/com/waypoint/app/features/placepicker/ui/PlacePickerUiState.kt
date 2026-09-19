// declares that this file belongs to the package `com.waypoint.app.features.placepicker.ui`
package com.waypoint.app.features.placepicker.ui

// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace

// declares sealed interface `PickerLoadState` and opens its body
sealed interface PickerLoadState {
    // expression: `data object Loading : PickerLoadState`
    data object Loading : PickerLoadState
    // declares data class `Success` with a primary constructor taking 1 parameter (`places`), inheriting from `PickerLoadState`
    data class  Success(val places: List<ExplorePlace>) : PickerLoadState
    // expression: `data object Empty : PickerLoadState`
    data object Empty   : PickerLoadState
    // declares data class `Error` with a primary constructor taking 1 parameter (`message`), inheriting from `PickerLoadState`
    data class  Error(val message: String) : PickerLoadState
// closes the interface `PickerLoadState`
}

// expression: `data class PlacePickerUiState(`
data class PlacePickerUiState(
    // continues the statement started above: `val loadState : PickerLoadState = PickerLoadState.Loading,`
    val loadState     : PickerLoadState = PickerLoadState.Loading,
    // continues the statement started above: `val categoryLabel : String = "",`
    val categoryLabel : String          = "",
// closes the multi-line argument list started above
)
