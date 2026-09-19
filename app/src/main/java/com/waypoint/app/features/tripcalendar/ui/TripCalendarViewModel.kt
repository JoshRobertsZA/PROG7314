// declares that this file belongs to the package `com.waypoint.app.features.tripcalendar.ui`
package com.waypoint.app.features.tripcalendar.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.SavedStateHandle` for use in this file
import androidx.lifecycle.SavedStateHandle
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
// imports `java.time.YearMonth` for use in this file
import java.time.YearMonth
// imports `java.time.format.DateTimeFormatter` for use in this file
import java.time.format.DateTimeFormatter
// imports `java.time.temporal.ChronoUnit` for use in this file
import java.time.temporal.ChronoUnit
// imports `com.waypoint.app.features.edititinerary.data.ItineraryRepository` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryRepository

// expression: `class TripCalendarViewModel(`
class TripCalendarViewModel(
    // continues the statement started above: `app: Application,`
    app: Application,
    // continues the statement started above: `savedStateHandle: SavedStateHandle,`
    savedStateHandle: SavedStateHandle,
// continues the statement started above: `) : AndroidViewModel(app) {`
) : AndroidViewModel(app) {

    // declares private read-only property `tripId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    private val tripId: String = checkNotNull(savedStateHandle["tripId"])
    // declares private read-only property `repo`, initialised with the result of calling `TripRepository(…)`
    private val repo = TripRepository(app)
    // declares private read-only property `itineraryRepo`, initialised with the result of calling `ItineraryRepository(…)`
    private val itineraryRepo = ItineraryRepository(app)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(TripCalendarUiState())
    // declares read-only property `uiState` of type `StateFlow<TripCalendarUiState>`, initialised to `_uiState`
    val uiState: StateFlow<TripCalendarUiState> = _uiState

    // declares sealed interface `ItineraryNavTarget` and opens its body
    sealed interface ItineraryNavTarget {
        // declares data class `EditItinerary` with a primary constructor taking 1 parameter (`tripId`), inheriting from `ItineraryNavTarget`
        data class EditItinerary(val tripId: String, val selectedDates: String) : ItineraryNavTarget
        // declares data class `ViewItinerary` with a primary constructor taking 1 parameter (`tripId`), inheriting from `ItineraryNavTarget`
        data class ViewItinerary(val tripId: String) : ItineraryNavTarget
    // closes the interface `ItineraryNavTarget`
    }

    // declares private read-only property `_navTarget`, initialised with the result of calling `MutableStateFlow(…)`
    private val _navTarget = MutableStateFlow<ItineraryNavTarget?>(null)
    // declares read-only property `navTarget` of type `StateFlow<ItineraryNavTarget?>`, initialised to `_navTarget`
    val navTarget: StateFlow<ItineraryNavTarget?> = _navTarget

    // declares private read-only property `shortFmt`, initialised with the result of calling `DateTimeFormatter.ofPattern(…)`
    private val shortFmt = DateTimeFormatter.ofPattern("MMM d")
    // declares private read-only property `longFmt`, initialised with the result of calling `DateTimeFormatter.ofPattern(…)`
    private val longFmt  = DateTimeFormatter.ofPattern("MMM d, yyyy")

    // expression: `init { loadTrip() }`
    init { loadTrip() }

    // declares function `loadTrip` taking no parameters and opens its body
    fun loadTrip() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `trips`, initialised with the result of calling `repo.getTripsForAccount(…)`
            val trips = repo.getTripsForAccount(SessionManager.accountId)
            // declares read-only property `trip`, initialised to `trips.firstOrNull { it.id == tripId }`
            val trip  = trips.firstOrNull { it.id == tripId }
            // `if` statement: executes `{ _uiState.update { it.copy(isLoading = fals…` when `trip == null` is true
            if (trip == null) { _uiState.update { it.copy(isLoading = false) }; return@launch }

            // declares read-only property `start`, initialised to `runCatching { LocalDate.parse(trip.startDate…`
            val start = runCatching { LocalDate.parse(trip.startDate) }.getOrNull()
            // declares read-only property `end`, initialised to `runCatching { LocalDate.parse(trip.endDate) …`
            val end   = runCatching { LocalDate.parse(trip.endDate) }.getOrNull()
            // declares read-only property `nights`, initialised with the result of calling `if(…)`
            val nights = if (start != null && end != null)
                // calls `between` on `ChronoUnit.DAYS` with arguments `(start, end)`, then chains `.toInt() else 0`
                ChronoUnit.DAYS.between(start, end).toInt() else 0
            // declares read-only property `rangeLabel`, initialised with the result of calling `if(…)`
            val rangeLabel = if (start != null && end != null)
                // expression: `"${start.format(shortFmt)} - ${end.format(longFmt)}" else ""`
                "${start.format(shortFmt)} - ${end.format(longFmt)}" else ""
            // declares read-only property `dayCount`, initialised with the result of calling `if(…)`
            val dayCount = if (start != null && end != null) (ChronoUnit.DAYS.between(start, end) + 1).toInt() else 0

            // declares read-only property `dayIds`, initialised with the result of calling `itineraryRepo.getSelectedDaysWithIds(…)`
            val storedDays = itineraryRepo.getSelectedDaysWithIds(trip.id)
            val dayIds   = storedDays.map { d -> d.id }
            val selectedDayDates = storedDays.map { d -> d.date }.toSet()
            // declares read-only property `flights`, initialised with the result of calling `itineraryRepo.getFlightsForDays(…)`
            val flights  = itineraryRepo.getFlightsForDays(dayIds).size
            // declares read-only property `stays`, initialised with the result of calling `itineraryRepo.getLodgingsForTrip(…)`
            val stays    = itineraryRepo.getLodgingsForTrip(trip.id).size
            // declares read-only property `rentals`, initialised with the result of calling `itineraryRepo.getCarRentalsForTrip(…)`
            val rentals  = itineraryRepo.getCarRentalsForTrip(trip.id).size

            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isLoading = false,`
                    isLoading      = false,
                    // continues the statement started above: `tripId = trip.id,`
                    tripId         = trip.id,
                    // continues the statement started above: `tripName = trip.name,`
                    tripName       = trip.name,
                    // continues the statement started above: `destination = trip.destination,`
                    destination    = trip.destination,
                    // continues the statement started above: `destLat = trip.destLat,`
                    destLat        = trip.destLat,
                    // continues the statement started above: `destLng = trip.destLng,`
                    destLng        = trip.destLng,
                    // continues the statement started above: `destPhotoUrl = trip.destPhotoUrl,`
                    destPhotoUrl   = trip.destPhotoUrl,
                    // continues the statement started above: `nightCount = nights,`
                    nightCount     = nights,
                    // continues the statement started above: `flightCount = flights,`
                    flightCount    = flights,
                    // continues the statement started above: `stayCount = stays,`
                    stayCount      = stays,
                    // continues the statement started above: `rentalCount = rentals,`
                    rentalCount    = rentals,
                    // continues the statement started above: `startDate = start,`
                    startDate      = start,
                    // continues the statement started above: `endDate = end,`
                    endDate        = end,
                    // continues the statement started above: `displayMonth = start?.let { d -> YearMonth.from(d) } ?: Yea…`
                    displayMonth   = start?.let { d -> YearMonth.from(d) } ?: YearMonth.now(),
                    // continues the statement started above: `dateRangeLabel = rangeLabel,`
                    dateRangeLabel = rangeLabel,
                    // continues the statement started above: `dayCount = dayCount,`
                    dayCount       = dayCount,
                    // continues the statement started above: `selectedDays = selectedDayDates,`
                    selectedDays   = selectedDayDates,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `loadTrip`
    }


    // declares function `onPrevMonth` taking no parameters and opens its body
    fun onPrevMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.minusMonths(1)) } }
    // declares function `onNextMonth` taking no parameters and opens its body
    fun onNextMonth() { _uiState.update { it.copy(displayMonth = it.displayMonth.plusMonths(1)) } }


    // declares function `onShowNameDialog` taking no parameters and opens its body
    fun onShowNameDialog() {
        // expression: `_uiState.update { it.copy(showNameDialog = true, nameInput = it.…`
        _uiState.update { it.copy(showNameDialog = true, nameInput = it.tripName) }
    // closes the function `onShowNameDialog`
    }

    // declares function `onNameInputChanged` taking 1 parameter (`value`) and opens its body
    fun onNameInputChanged(value: String) {
        // expression: `_uiState.update { it.copy(nameInput = value) }`
        _uiState.update { it.copy(nameInput = value) }
    // closes the function `onNameInputChanged`
    }

    // declares function `onDismissNameDialog` taking no parameters and opens its body
    fun onDismissNameDialog() {
        // expression: `_uiState.update { it.copy(showNameDialog = false) }`
        _uiState.update { it.copy(showNameDialog = false) }
    // closes the function `onDismissNameDialog`
    }

    // declares function `onConfirmNameEdit` taking no parameters and opens its body
    fun onConfirmNameEdit() {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // declares read-only property `newName`, initialised with the result of calling `state.nameInput.trim(…)`
        val newName = state.nameInput.trim().ifBlank { return }
        // expression: `_uiState.update { it.copy(showNameDialog = false, tripName = new…`
        _uiState.update { it.copy(showNameDialog = false, tripName = newName) }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `updateTripDetails` on `repo` with an argument list that continues on the following lines
            repo.updateTripDetails(
                // continues the statement started above: `id = tripId,`
                id          = tripId,
                // continues the statement started above: `name = newName,`
                name        = newName,
                // continues the statement started above: `destination = state.destination,`
                destination = state.destination,
                // continues the statement started above: `destLat = state.destLat,`
                destLat     = state.destLat,
                // continues the statement started above: `destLng = state.destLng,`
                destLng     = state.destLng,
                // continues the statement started above: `destPhotoUrl = state.destPhotoUrl,`
                destPhotoUrl = state.destPhotoUrl,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `onConfirmNameEdit`
    }


    // declares function `onShowDestSearch` taking no parameters and opens its body
    fun onShowDestSearch() { _uiState.update { it.copy(showDestSearch = true) } }
    // declares function `onDismissDestSearch` taking no parameters and opens its body
    fun onDismissDestSearch() { _uiState.update { it.copy(showDestSearch = false) } }


    // declares function `onDayToggled` taking 1 parameter (`date`) and opens its body
    fun onDayToggled(date: LocalDate) {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // declares read-only property `start`, initialised to `state.startDate ?: return`
        val start = state.startDate ?: return
        // declares read-only property `end`, initialised to `state.endDate ?: return`
        val end   = state.endDate   ?: return
        // `if` statement: executes `return` when `date.isBefore(start) || date.isAfter(end)` is true
        if (date.isBefore(start) || date.isAfter(end)) return
        // declares read-only property `current`, initialised to `state.selectedDays`
        val current = state.selectedDays
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with arguments `(selectedDays = if (date in current) current …)`
            it.copy(selectedDays = if (date in current) current - date else current + date)
        // closes the block
        }
    // closes the function `onDayToggled`
    }


    // declares function `onEditItineraryClick` taking no parameters and opens its body
    fun onEditItineraryClick() {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // `if` statement: the block below runs when `state.selectedDays.isEmpty()` is true
        if (state.selectedDays.isEmpty()) {
            // expression: `_uiState.update { it.copy(showNoDaysError = true) }`
            _uiState.update { it.copy(showNoDaysError = true) }
            // returns from the current function with no value
            return
        // closes the if block
        }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `replaceSelectedDays` on `itineraryRepo` with arguments `(state.tripId, state.selectedDays)`
            itineraryRepo.replaceSelectedDays(state.tripId, state.selectedDays)
            val selectedDates = state.selectedDays.joinToString("|") { it.toString() }
            _navTarget.value = ItineraryNavTarget.EditItinerary(state.tripId, selectedDates)
        // closes the block
        }
    // closes the function `onEditItineraryClick`
    }

    // declares function `onViewItineraryClick` taking no parameters and opens its body
    fun onViewItineraryClick() {
        // declares read-only property `state`, initialised to `_uiState.value`
        val state = _uiState.value
        // `if` statement: the block below runs when `state.selectedDays.isEmpty()` is true
        if (state.selectedDays.isEmpty()) {
            // expression: `_uiState.update { it.copy(showNoDaysError = true) }`
            _uiState.update { it.copy(showNoDaysError = true) }
            // returns from the current function with no value
            return
        // closes the if block
        }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `replaceSelectedDays` on `itineraryRepo` with arguments `(state.tripId, state.selectedDays)`
            itineraryRepo.replaceSelectedDays(state.tripId, state.selectedDays)
            // assigns `_navTarget.value` the value `ItineraryNavTarget.ViewItinerary(state.tripId)`
            _navTarget.value = ItineraryNavTarget.ViewItinerary(state.tripId)
        // closes the block
        }
    // closes the function `onViewItineraryClick`
    }


    // declares function `onDeleteClick` taking no parameters and opens its body
    fun onDeleteClick()   { _uiState.update { it.copy(showDeleteConfirm = true) } }
    // declares function `onDeleteDismiss` taking no parameters and opens its body
    fun onDeleteDismiss() { _uiState.update { it.copy(showDeleteConfirm = false) } }

    // declares function `onDeleteConfirm` taking no parameters and opens its body
    fun onDeleteConfirm() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `deleteTrip` on `repo` with arguments `(tripId, SessionManager.accountId)`
            repo.deleteTrip(tripId, SessionManager.accountId)
            // expression: `_uiState.update { it.copy(showDeleteConfirm = false, deleted = t…`
            _uiState.update { it.copy(showDeleteConfirm = false, deleted = true) }
        // closes the block
        }
    // closes the function `onDeleteConfirm`
    }


    // declares function `onEditDatesClick` taking no parameters and opens its body
    fun onEditDatesClick() {
        // opens a block after `_uiState.update`
        _uiState.update {
            // calls `copy` on `it` with an argument list that continues on the following lines
            it.copy(showEditDates = true, editStart = null, editEnd = null,
                    // continues the statement started above: `editMonth = it.startDate?.let { d -> YearMonth.from(d) } ?:…`
                    editMonth = it.startDate?.let { d -> YearMonth.from(d) } ?: YearMonth.now())
        // closes the block
        }
    // closes the function `onEditDatesClick`
    }
    // declares function `onEditDatesDismiss` taking no parameters and opens its body
    fun onEditDatesDismiss() { _uiState.update { it.copy(showEditDates = false) } }
    // declares function `onEditPrevMonth` taking no parameters and opens its body
    fun onEditPrevMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.minusMonths(1)) } }
    // declares function `onEditNextMonth` taking no parameters and opens its body
    fun onEditNextMonth()    { _uiState.update { it.copy(editMonth = it.editMonth.plusMonths(1)) } }

    // declares function `onEditDayTapped` taking 1 parameter (`date`) and opens its body
    fun onEditDayTapped(date: LocalDate) {
        // expression: `_uiState.update { s ->`
        _uiState.update { s ->
            // continues the statement started above: `when {`
            when {
                // lambda `s.editStart == null || s.edit… -> s.copy(editStart = date, edit…`
                s.editStart == null || s.editEnd != null -> s.copy(editStart = date, editEnd = null)
                // lambda `date < s.editStart -> s.copy(editStart = date, edit…`
                date < s.editStart                       -> s.copy(editStart = date, editEnd = s.editStart)
                // `else` branch of the `when`: evaluates `s.copy(editEnd = date)`
                else                                     -> s.copy(editEnd = date)
            // closes the block
            }
        // closes the block
        }
    // closes the function `onEditDayTapped`
    }

    // declares function `onEditDatesSave` taking no parameters and opens its body
    fun onEditDatesSave() {
        // declares read-only property `s`, initialised to `_uiState.value`
        val s = _uiState.value
        // declares read-only property `start`, initialised to `s.editStart ?: return`
        val start = s.editStart ?: return
        // declares read-only property `end`, initialised to `s.editEnd ?: start`
        val end   = s.editEnd ?: start
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `updateTripDates` on `repo` with arguments `(tripId, start.toString(), end.toString())`
            repo.updateTripDates(tripId, start.toString(), end.toString())
            // calls `removeDaysOutside` on `itineraryRepo` with arguments `(tripId, start, end)`
            itineraryRepo.removeDaysOutside(tripId, start, end)
            // expression: `_uiState.update { it.copy(showEditDates = false) }`
            _uiState.update { it.copy(showEditDates = false) }
            // calls `loadTrip` with arguments `()`
            loadTrip()
        // closes the block
        }
    // closes the function `onEditDatesSave`
    }

    // declares function `onNavConsumed` taking no parameters and opens its body
    fun onNavConsumed() { _navTarget.value = null }

    // declares function `onNoDaysErrorShown` taking no parameters and opens its body
    fun onNoDaysErrorShown() { _uiState.update { it.copy(showNoDaysError = false) } }

    // declares function `onDestinationSelected` taking 1 parameter (`name`) and opens its body
    fun onDestinationSelected(name: String) {
        // expression: `_uiState.update { it.copy(showDestSearch = false, destination = …`
        _uiState.update { it.copy(showDestSearch = false, destination = name, isGeocodingDest = true) }
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `coords`, initialised with the result of calling `LocationIQRepository.geocodeCity(…)`
            val coords = LocationIQRepository.geocodeCity(name)
            // declares read-only property `photo`, initialised with the result of calling `WikipediaCitySearch.thumbnailUrl(…)`
            val photo  = WikipediaCitySearch.thumbnailUrl(name)
            // declares read-only property `state`, initialised to `_uiState.value`
            val state  = _uiState.value
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isGeocodingDest = false,`
                    isGeocodingDest = false,
                    // continues the statement started above: `destLat = coords?.first,`
                    destLat         = coords?.first,
                    // continues the statement started above: `destLng = coords?.second,`
                    destLng         = coords?.second,
                    // continues the statement started above: `destPhotoUrl = photo,`
                    destPhotoUrl    = photo,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `updateTripDetails` on `repo` with an argument list that continues on the following lines
            repo.updateTripDetails(
                // continues the statement started above: `id = tripId,`
                id          = tripId,
                // continues the statement started above: `name = state.tripName,`
                name        = state.tripName,
                // continues the statement started above: `destination = name,`
                destination = name,
                // continues the statement started above: `destLat = coords?.first,`
                destLat     = coords?.first,
                // continues the statement started above: `destLng = coords?.second,`
                destLng     = coords?.second,
                // continues the statement started above: `destPhotoUrl = photo,`
                destPhotoUrl = photo,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the function `onDestinationSelected`
    }
// closes the block
}
