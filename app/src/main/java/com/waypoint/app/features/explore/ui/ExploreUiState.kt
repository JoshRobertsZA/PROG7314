package com.waypoint.app.features.explore.ui

import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.cache.PlacesCache

/** Category chips on the Explore tab. */
enum class ExploreFilter {
    ALL, RESTAURANTS, CAFES, HOTELS, PARKS, PUBS, CINEMAS;
}

/** Loading state of the nearby places list. */
sealed interface PlacesState {
    data object Idle    : PlacesState
    data object Loading : PlacesState
    data class  Success(val cache: PlacesCache) : PlacesState
    data object Error   : PlacesState
}

/** State of the Explore tab. */
data class ExploreUiState(
    val locationLabel : String        = "Locating...",
    val placesState   : PlacesState   = PlacesState.Idle,
    val activeFilter  : ExploreFilter = ExploreFilter.ALL,
    val visiblePlaces : List<ExplorePlace> = emptyList(),
)
