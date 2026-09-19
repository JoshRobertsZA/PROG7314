// declares that this file belongs to the package `com.waypoint.app.features.newtrip.ui`
package com.waypoint.app.features.newtrip.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.features.explore.data.LocationIQRepository` for use in this file
import com.waypoint.app.features.explore.data.LocationIQRepository
// imports `com.waypoint.app.features.home.data.WikipediaCitySearch` for use in this file
import com.waypoint.app.features.home.data.WikipediaCitySearch
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `kotlinx.coroutines.flow.MutableSharedFlow` for use in this file
import kotlinx.coroutines.flow.MutableSharedFlow
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.SharedFlow` for use in this file
import kotlinx.coroutines.flow.SharedFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.asSharedFlow` for use in this file
import kotlinx.coroutines.flow.asSharedFlow
// imports `kotlinx.coroutines.flow.asStateFlow` for use in this file
import kotlinx.coroutines.flow.asStateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth

// declares class `NewTripViewModel` with a primary constructor taking 1 parameter (`application`), inheriting from `AndroidViewModel(application)` and opens its body
class NewTripViewModel(application: Application) : AndroidViewModel(application) {

    // declares private read-only property `repository`, initialised with the result of calling `TripRepository(…)`
    private val repository = TripRepository(application)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(NewTripUiState())
    // declares read-only property `uiState` of type `StateFlow<NewTripUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<NewTripUiState> = _uiState.asStateFlow()

    // declares private read-only property `_tripSaved`, initialised with the result of calling `MutableSharedFlow(…)`
    private val _tripSaved = MutableSharedFlow<String>(extraBufferCapacity = 1)
    // declares read-only property `tripSaved` of type `SharedFlow<String>`, initialised with the result of calling `_tripSaved.asSharedFlow(…)`
    val tripSaved: SharedFlow<String> = _tripSaved.asSharedFlow()


    // declares function `onTripNameChanged` taking 1 parameter (`name`) and opens its body
    fun onTripNameChanged(name: String) {
        // expression: `_uiState.update { it.copy(tripName = name) }`
        _uiState.update { it.copy(tripName = name) }
    // closes the function `onTripNameChanged`
    }


    // declares function `onNextMonth` taking no parameters and opens its body
    fun onNextMonth() {
        // expression: `_uiState.update { it.copy(displayMonth = it.displayMonth.plusMon…`
        _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) }
    // closes the function `onNextMonth`
    }

    // declares function `onPrevMonth` taking no parameters and opens its body
    fun onPrevMonth() {
        // declares read-only property `current`, initialised with the result of calling `YearMonth.now(…)`
        val current = YearMonth.now()
        // expression: `_uiState.update { state ->`
        _uiState.update { state ->
            // continues the statement started above: `if (state.displayMonth > current)`
            if (state.displayMonth > current)
                // calls `copy` on `state` with arguments `(displayMonth = state.displayMonth.minusMonth…)`
                state.copy(displayMonth = state.displayMonth.minusMonths(1))
            // expression: `else state`
            else state
        // closes the block
        }
    // closes the function `onPrevMonth`
    }


    // declares function `onShowYearPicker` taking no parameters and opens its body
    fun onShowYearPicker() {
        // expression: `_uiState.update { it.copy(showYearPicker = true) }`
        _uiState.update { it.copy(showYearPicker = true) }
    // closes the function `onShowYearPicker`
    }

    // declares function `onDismissYearPicker` taking no parameters and opens its body
    fun onDismissYearPicker() {
        // expression: `_uiState.update { it.copy(showYearPicker = false) }`
        _uiState.update { it.copy(showYearPicker = false) }
    // closes the function `onDismissYearPicker`
    }

    // declares function `onYearMonthPicked` taking 1 parameter (`yearMonth`) and opens its body
    fun onYearMonthPicked(yearMonth: YearMonth) {
        // expression: `_uiState.update { it.copy(displayMonth = yearMonth, showYearPick…`
        _uiState.update { it.copy(displayMonth = yearMonth, showYearPicker = false) }
    // closes the function `onYearMonthPicked`
    }


    // declares function `onDayTapped` taking 1 parameter (`tapped`) and opens its body
    fun onDayTapped(tapped: LocalDate) {
        // expression: `_uiState.update { state ->`
        _uiState.update { state ->
            // continues the statement started above: `val start = state.startDate`
            val start = state.startDate
            // declares read-only property `end`, initialised to `state.endDate`
            val end   = state.endDate

            // `when` expression: the first branch whose condition is true runs
            when {
                // `when` branch: when the subject matches `start == null`, evaluates `state.copy(startDate = tapped, endDate …`
                start == null -> state.copy(startDate = tapped, endDate = null)

                // `when` branch `end == null`: opens a block
                end == null -> {
                    // `if` statement: the block below runs when `tapped == start` is true
                    if (tapped == start) {
                        // calls `copy` on `state` with arguments `(startDate = null, endDate = null)`
                        state.copy(startDate = null, endDate = null)
                    // closes the previous branch and opens an `else if` branch that runs when `tapped < start` is true
                    } else if (tapped < start) {
                        // calls `copy` on `state` with arguments `(startDate = tapped, endDate = start)`
                        state.copy(startDate = tapped, endDate = start)
                    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                    } else {
                        // calls `copy` on `state` with arguments `(endDate = tapped)`
                        state.copy(endDate = tapped)
                    // closes the else branch
                    }
                // closes the when branch
                }

                // assigns `tapped` the value `= start || tapped == end ->`
                tapped == start || tapped == end ->
                    // continues the statement started above: `state.copy(startDate = tapped, endDate = null)`
                    state.copy(startDate = tapped, endDate = null)

                // `when` branch: when the subject matches `tapped < start`, evaluates `state.copy(startDate = tapped)`
                tapped < start -> state.copy(startDate = tapped)
                // `when` branch: when the subject matches `tapped > end`, evaluates `state.copy(endDate = tapped)`
                tapped > end   -> state.copy(endDate = tapped)
                // `else` branch of the `when`: evaluates `state.copy(startDate = tapped)`
                else           -> state.copy(startDate = tapped)
            // closes the when block
            }
        // closes the block
        }
    // closes the function `onDayTapped`
    }



    // declares function `onShowDestSearch` taking no parameters and opens its body
    fun onShowDestSearch() {
        // expression: `_uiState.update { it.copy(showDestSearch = true) }`
        _uiState.update { it.copy(showDestSearch = true) }
    // closes the function `onShowDestSearch`
    }

    // declares function `onDismissDestSearch` taking no parameters and opens its body
    fun onDismissDestSearch() {
        // expression: `_uiState.update { it.copy(showDestSearch = false) }`
        _uiState.update { it.copy(showDestSearch = false) }
    // closes the function `onDismissDestSearch`
    }

    // declares function `onDestinationSelected` taking 1 parameter (`name`) and opens its body
    fun onDestinationSelected(name: String) {
        // expression: `_uiState.update { it.copy(showDestSearch = false, destinationNam…`
        _uiState.update { it.copy(showDestSearch = false, destinationName = name, isGeocodingDest = true) }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `coords`, initialised with the result of calling `LocationIQRepository.geocodeCity(…)`
            val coords = LocationIQRepository.geocodeCity(name)
            // declares read-only property `photo`, initialised with the result of calling `WikipediaCitySearch.thumbnailUrl(…)`
            val photo  = WikipediaCitySearch.thumbnailUrl(name)
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isGeocodingDest = false,`
                    isGeocodingDest = false,
                    // continues the statement started above: `destLat = coords?.first,`
                    destLat = coords?.first,
                    // continues the statement started above: `destLng = coords?.second,`
                    destLng = coords?.second,
                    // continues the statement started above: `destPhotoUrl = photo,`
                    destPhotoUrl = photo,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `onDestinationSelected`
    }


    // declares function `saveTrip` taking no parameters and opens its body
    fun saveTrip() {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // `if` statement: executes `return` when `!state.canSave` is true
        if (!state.canSave) return

        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isSaving = true) }`
            _uiState.update { it.copy(isSaving = true) }
            // declares read-only property `id`, initialised with the result of calling `repository.insertTrip(…)`
            val id = repository.insertTrip(
                // continues the statement started above: `accountId = SessionManager.accountId,`
                accountId   = SessionManager.accountId,
                // continues the statement started above: `name = state.tripName.trim(),`
                name        = state.tripName.trim(),
                // continues the statement started above: `startDate = state.startDate!!.toString(),`
                startDate   = state.startDate!!.toString(),
                // continues the statement started above: `endDate = state.endDate!!.toString(),`
                endDate     = state.endDate!!.toString(),
                // continues the statement started above: `destination = state.destinationName.ifBlank { null },`
                destination = state.destinationName.ifBlank { null },
                // continues the statement started above: `destLat = state.destLat,`
                destLat     = state.destLat,
                // continues the statement started above: `destLng = state.destLng,`
                destLng     = state.destLng,
                // continues the statement started above: `destPhotoUrl = state.destPhotoUrl,`
                destPhotoUrl = state.destPhotoUrl,
            // closes the multi-line argument list started above
            )
            // expression: `_uiState.update { it.copy(isSaving = false) }`
            _uiState.update { it.copy(isSaving = false) }
            // calls `emit` on `_tripSaved` with arguments `(id)`
            _tripSaved.emit(id)
        // closes the block
        }
    // closes the function `saveTrip`
    }
// closes the class `NewTripViewModel`
}
