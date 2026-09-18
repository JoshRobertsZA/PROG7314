package com.waypoint.app.features.placepicker.ui

import com.waypoint.app.core.cache.ExplorePlace

sealed interface PickerLoadState {
    data object Loading : PickerLoadState
    data class  Success(val places: List<ExplorePlace>) : PickerLoadState
    data object Empty   : PickerLoadState
    data object Error   : PickerLoadState
}

data class PlacePickerUiState(
    val loadState     : PickerLoadState = PickerLoadState.Loading,
    val categoryLabel : String          = "",
)
