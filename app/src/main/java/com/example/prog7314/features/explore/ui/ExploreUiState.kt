package com.example.prog7314.features.explore.ui

import com.example.prog7314.core.cache.ExplorePlace
import com.example.prog7314.core.cache.PlacesCache

/** Filter categories shown in the Explore screen chip row. */
enum class ExploreFilter {
    ALL, RESTAURANTS, CAFES, ATTRACTIONS, ENTERTAINMENT;
}

sealed interface PlacesState {
    data object Idle    : PlacesState
    data object Loading : PlacesState
    data class  Success(val cache: PlacesCache) : PlacesState
    data object Error   : PlacesState
}

data class ExploreUiState(
    val selectedCity  : String        = "Cape Town",
    val placesState   : PlacesState   = PlacesState.Idle,
    val activeFilter  : ExploreFilter = ExploreFilter.ALL,
    /** Subset of the cache's places list after applying [activeFilter]. */
    val visiblePlaces : List<ExplorePlace> = emptyList(),
)
