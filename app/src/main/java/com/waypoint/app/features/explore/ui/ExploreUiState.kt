// declares that this file belongs to the package `com.waypoint.app.features.explore.ui`
package com.waypoint.app.features.explore.ui

// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.cache.PlacesCache` for use in this file
import com.waypoint.app.core.cache.PlacesCache

// declares enum class `ExploreFilter` and opens its body
enum class ExploreFilter {
    // statement: `ALL, RESTAURANTS, CAFES, HOTELS, PARKS, PUBS, CINEMAS;`
    ALL, RESTAURANTS, CAFES, HOTELS, PARKS, PUBS, CINEMAS;
// closes the class `ExploreFilter`
}

// declares sealed interface `PlacesState` and opens its body
sealed interface PlacesState {
    // expression: `data object Idle : PlacesState`
    data object Idle    : PlacesState
    // expression: `data object Loading : PlacesState`
    data object Loading : PlacesState
    // declares data class `Success` with a primary constructor taking 1 parameter (`cache`), inheriting from `PlacesState`
    data class  Success(val cache: PlacesCache) : PlacesState
    // expression: `data object Error : PlacesState`
    data object Error   : PlacesState
// closes the interface `PlacesState`
}

// expression: `data class ExploreUiState(`
data class ExploreUiState(
    // continues the statement started above: `val locationLabel : String = "Locating...",`
    val locationLabel : String        = "Locating...",
    // continues the statement started above: `val placesState : PlacesState = PlacesState.Idle,`
    val placesState   : PlacesState   = PlacesState.Idle,
    // continues the statement started above: `val activeFilter : ExploreFilter = ExploreFilter.ALL,`
    val activeFilter  : ExploreFilter = ExploreFilter.ALL,
    // continues the statement started above: `val visiblePlaces : List<ExplorePlace> = emptyList(),`
    val visiblePlaces : List<ExplorePlace> = emptyList(),
// closes the multi-line argument list started above
)
