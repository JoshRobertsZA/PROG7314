// declares that this file belongs to the package `com.waypoint.app.features.alltrips.ui`
package com.waypoint.app.features.alltrips.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// declares class `AllTripsViewModel` with a primary constructor taking 1 parameter (`app`), inheriting from `AndroidViewModel(app)` and opens its body
class AllTripsViewModel(app: Application) : AndroidViewModel(app) {

    // declares private read-only property `repo`, initialised with the result of calling `TripRepository(…)`
    private val repo = TripRepository(app)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(AllTripsUiState(isLoading = true))
    // declares read-only property `uiState` of type `StateFlow<AllTripsUiState>`, initialised to `_uiState`
    val uiState: StateFlow<AllTripsUiState> = _uiState

    // initialiser block: runs when an instance of the class is constructed
    init {
        // calls `loadTrips` with arguments `()`
        loadTrips()
    // closes the init block
    }

    // declares function `loadTrips` taking no parameters and opens its body
    fun loadTrips() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true) }`
            _uiState.update { it.copy(isLoading = true) }
            // declares read-only property `accountId`, initialised to `SessionManager.accountId`
            val accountId = SessionManager.accountId
            // declares read-only property `today`, initialised with the result of calling `LocalDate.now(…)`
            val today     = LocalDate.now()
            // declares read-only property `entities`, initialised with the result of calling `repo.getTripsForAccount(…)`
            val entities  = repo.getTripsForAccount(accountId)

            // declares read-only property `rows`, initialised to `entities`
            val rows = entities
                // chained call `.mapIndexed` on the previous result with an inline lambda (`index, entity ->`) that evaluates `entity.toRow(index, today)`
                .mapIndexed { index, entity -> entity.toRow(index, today) }
                // expression: `.sortedWith(`
                .sortedWith(
                    // continues the statement started above: `compareBy<TripRow> { it.status.ordinal }`
                    compareBy<TripRow> { it.status.ordinal }
                        // continues the statement started above: `.thenByDescending { it.dates }`
                        .thenByDescending { it.dates }
                // closes the multi-line argument list started above
                )

            // expression: `_uiState.update { it.copy(trips = rows, isLoading = false) }`
            _uiState.update { it.copy(trips = rows, isLoading = false) }
        // closes the block
        }
    // closes the function `loadTrips`
    }

    // declares function `onFilterSelected` taking 1 parameter (`filter`) and opens its body
    fun onFilterSelected(filter: TripFilter) {
        // expression: `_uiState.update { it.copy(filter = filter) }`
        _uiState.update { it.copy(filter = filter) }
    // closes the function `onFilterSelected`
    }

    // declares function `onSearchQueryChanged` taking 1 parameter (`query`) and opens its body
    fun onSearchQueryChanged(query: String) {
        // expression: `_uiState.update { it.copy(searchQuery = query) }`
        _uiState.update { it.copy(searchQuery = query) }
    // closes the function `onSearchQueryChanged`
    }
// closes the class `AllTripsViewModel`
}
