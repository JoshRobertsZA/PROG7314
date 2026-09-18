package com.waypoint.app.features.explore.ui

import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache

/** Filter categories shown in the Explore screen chip row. */
enum class ExploreFilter {
    ALL, RESTAURANTS, CAFES, HOTELS, PARKS, PUBS, CINEMAS;
}

sealed interface PlacesState {
    data object Idle    : PlacesState
    data object Loading : PlacesState
    data class  Success(val cache: PlacesCache) : PlacesState
    data object Error   : PlacesState
}

data class ExploreUiState(
    val locationLabel : String        = "Locating...",
    val placesState   : PlacesState   = PlacesState.Idle,
    val activeFilter  : ExploreFilter = ExploreFilter.ALL,
    /** Subset of the cache's places list after applying [activeFilter]. */
    val visiblePlaces : List<ExplorePlace> = emptyList(),
)
