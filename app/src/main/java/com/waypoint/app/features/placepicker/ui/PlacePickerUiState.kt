package com.waypoint.app.features.placepicker.ui

import com.waypoint.app.core.cache.ExplorePlace

/** Loading state of the place list. */
sealed interface PickerLoadState {
    data object Loading : PickerLoadState
    data class  Success(val places: List<ExplorePlace>) : PickerLoadState
    data object Empty   : PickerLoadState
    data class  Error(val message: String) : PickerLoadState
}

/** State of the place picker. */
data class PlacePickerUiState(
    val loadState     : PickerLoadState = PickerLoadState.Loading,
    val categoryLabel : String          = "",
)
