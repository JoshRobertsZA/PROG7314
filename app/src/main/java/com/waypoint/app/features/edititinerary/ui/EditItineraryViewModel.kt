// declares that this file belongs to the package `com.waypoint.app.features.edititinerary.ui`
package com.waypoint.app.features.edititinerary.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.SavedStateHandle` for use in this file
import androidx.lifecycle.SavedStateHandle
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.network.AirLabsRepository` for use in this file
import com.waypoint.app.core.network.AirLabsRepository
// imports `com.waypoint.app.core.common.DocumentNames` for use in this file
import com.waypoint.app.core.common.DocumentNames
// imports `com.waypoint.app.features.edititinerary.data.ItineraryRepository` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.asStateFlow` for use in this file
import kotlinx.coroutines.flow.asStateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.delay
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// expression: `class EditItineraryViewModel(`
class EditItineraryViewModel(
    // continues the statement started above: `application: Application,`
    application: Application,
    // continues the statement started above: `savedStateHandle: SavedStateHandle,`
    savedStateHandle: SavedStateHandle,
// continues the statement started above: `) : AndroidViewModel(application) {`
) : AndroidViewModel(application) {

    // declares read-only property `tripId` of type `String`, initialised with the result of calling `checkNotNull(…)`
    val tripId: String = checkNotNull(savedStateHandle["tripId"])
    // pipe-separated ISO dates from TripCalendar selection, e.g. "2024-12-23|2024-12-28"
    private val selectedDates: Set<String> = (savedStateHandle.get<String>("selectedDates") ?: "")
        .split("|").filter { it.isNotBlank() }.toSet()

    // declares private read-only property `repo`, initialised with the result of calling `ItineraryRepository(…)`
    private val repo = ItineraryRepository(application)
    // debounce jobs: one per flight id, cancelled on each new keystroke
    private val depTimeLookupJobs = HashMap<String, Job>()

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(EditItineraryUiState())
    // declares read-only property `uiState` of type `StateFlow<EditItineraryUiState>`, initialised with the result of calling `_uiState.asStateFlow(…)`
    val uiState: StateFlow<EditItineraryUiState> = _uiState.asStateFlow()

    // declares read-only property `activeDayId` of type `String?`
    val activeDayId: String?
        // continues the statement started above: `get() = _uiState.value.days.getOrNull(_uiState.value.active…`
        get() = _uiState.value.days.getOrNull(_uiState.value.activeDayIndex)?.dayId

    // initialiser block: runs when an instance of the class is constructed
    init {
        // calls `loadAll` with arguments `()`
        loadAll()
    // closes the init block
    }


    // declares private function `loadAll` taking no parameters and opens its body
    private fun loadAll() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true) }`
            _uiState.update { it.copy(isLoading = true) }

            // load all stored days then filter to only the ones selected on the calendar
            val allDayEntities = repo.getSelectedDaysWithIds(tripId)
            val dayEntities = if (selectedDates.isEmpty()) allDayEntities
                              else allDayEntities.filter { it.date.toString() in selectedDates }
            // declares read-only property `days`, initialised to a lambda / arrow function
            val days        = dayEntities.map { e -> DayItem(dayId = e.id, date = e.date) }
            // declares read-only property `lodgings`, initialised with the result of calling `loadLodgings(…)`
            val lodgings    = loadLodgings()
            // declares read-only property `cars`, initialised with the result of calling `loadCarRentals(…)`
            val cars        = loadCarRentals()

            // declares read-only property `activeIndex`, initialised to the number 0
            val activeIndex = 0
            // declares read-only property `flights`, initialised with the result of calling `if(…)`
            val flights     = if (days.isNotEmpty()) loadFlightsFor(days[activeIndex].dayId) else emptyList()

            // declares read-only property `places`, initialised with the result of calling `if(…)`
            val places = if (days.isNotEmpty()) loadPlacesFor(days[activeIndex].dayId) else emptyMap()

            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isLoading = false,`
                    isLoading           = false,
                    // continues the statement started above: `days = days,`
                    days                = days,
                    // continues the statement started above: `activeDayIndex = activeIndex,`
                    activeDayIndex      = activeIndex,
                    // continues the statement started above: `flightsForActiveDay = flights,`
                    flightsForActiveDay = flights,
                    // continues the statement started above: `lodgings = lodgings,`
                    lodgings            = lodgings,
                    // continues the statement started above: `carRentals = cars,`
                    carRentals          = cars,
                    // continues the statement started above: `placesForActiveDay = places,`
                    placesForActiveDay  = places,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `loadAll`
    }

    // declares private suspend function `loadLodgings` taking no parameters, returning `List<LodgingItem>`; its body is the expression ``
    private suspend fun loadLodgings(): List<LodgingItem> =
        // continues the statement started above: `repo.getLodgingsForTrip(tripId).map { l -> LodgingItem(l.id…`
        repo.getLodgingsForTrip(tripId).map { l -> LodgingItem(l.id, l.fromDate, l.toDate, l.pdfUri, l.docName) }

    // declares private suspend function `loadCarRentals` taking no parameters, returning `List<CarRentalItem>`; its body is the expression ``
    private suspend fun loadCarRentals(): List<CarRentalItem> =
        // continues the statement started above: `repo.getCarRentalsForTrip(tripId).map { c -> CarRentalItem(…`
        repo.getCarRentalsForTrip(tripId).map { c -> CarRentalItem(c.id, c.fromDate, c.toDate, c.pdfUri, c.docName) }

    // declares private suspend function `loadFlightsFor` taking 1 parameter (`dayId`), returning `List<FlightItem>`; its body is the expression ``
    private suspend fun loadFlightsFor(dayId: String): List<FlightItem> =
        // continues the statement started above: `repo.getFlightsForDays(listOf(dayId)).map { f ->`
        repo.getFlightsForDays(listOf(dayId)).map { f ->
            // continues the statement started above: `FlightItem(`
            FlightItem(
                // continues the statement started above: `id = f.id,`
                id           = f.id,
                // continues the statement started above: `dayId = f.dayId,`
                dayId        = f.dayId,
                // continues the statement started above: `flightNumber = f.flightNumber.orEmpty(),`
                flightNumber = f.flightNumber.orEmpty(),
                // continues the statement started above: `pdfUri = f.pdfUri,`
                pdfUri       = f.pdfUri,
                // continues the statement started above: `departureTime = f.departureTime,`
                departureTime = f.departureTime?.takeIf { it != AirLabsRepository.AIRLABS_MISS },
                // continues the statement started above: `docName = f.docName,`
                docName      = f.docName,
            // closes the multi-line argument list started above
            )
        // closes the block
        }

    // declares private suspend function `loadPlacesFor` taking 1 parameter (`dayId`), returning `Map<String, List<PlaceItem>>` and opens its body
    private suspend fun loadPlacesFor(dayId: String): Map<String, List<PlaceItem>> {
        // declares read-only property `itinCategories`, initialised with the result of calling `listOf(…)`
        val itinCategories = listOf("RESTAURANTS", "HOTELS", "PARKS", "PUBS", "CINEMAS")
        // declares read-only property `all`, initialised with the result of calling `repo.getPlacesForDay(…)`
        val all = repo.getPlacesForDay(dayId).map { p ->
            // continues the statement started above: `PlaceItem(`
            PlaceItem(
                // continues the statement started above: `id = p.id,`
                id       = p.id,
                // continues the statement started above: `dayId = p.dayId,`
                dayId    = p.dayId,
                // continues the statement started above: `name = p.name,`
                name     = p.name,
                // continues the statement started above: `category = p.category,`
                category = p.category,
                // continues the statement started above: `note = p.note,`
                note     = p.note,
                // continues the statement started above: `photoUrl = p.photoUrl,`
                photoUrl = p.photoUrl,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // returns `itinCategories.associateWith { cat ->` from the current function
        return itinCategories.associateWith { cat ->
            // continues the statement started above: `all.filter { it.category == cat }`
            all.filter { it.category == cat }
        // closes the block
        }
    // closes the function `loadPlacesFor`
    }


    // declares function `onDaySelected` taking 1 parameter (`index`) and opens its body
    fun onDaySelected(index: Int) {
        // declares read-only property `days`, initialised to `_uiState.value.days`
        val days = _uiState.value.days
        // `if` statement: executes `return` when `index < 0 || index >= days.size` is true
        if (index < 0 || index >= days.size) return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `dayId`, initialised to `days[index].dayId`
            val dayId   = days[index].dayId
            // declares read-only property `flights`, initialised with the result of calling `loadFlightsFor(…)`
            val flights = loadFlightsFor(dayId)
            // declares read-only property `places`, initialised with the result of calling `loadPlacesFor(…)`
            val places  = loadPlacesFor(dayId)
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `activeDayIndex = index,`
                    activeDayIndex      = index,
                    // continues the statement started above: `flightsForActiveDay = flights,`
                    flightsForActiveDay = flights,
                    // continues the statement started above: `placesForActiveDay = places,`
                    placesForActiveDay  = places,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `onDaySelected`
    }


    // declares function `onUploadFlightClick` taking no parameters; its body is the expression ``
    fun onUploadFlightClick() =
        // continues the statement started above: `_uiState.update { it.copy(pendingUploadType = ItineraryUplo…`
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.FLIGHT) }

    // declares function `onUploadLodgingClick` taking no parameters; its body is the expression ``
    fun onUploadLodgingClick() =
        // continues the statement started above: `_uiState.update { it.copy(pendingUploadType = ItineraryUplo…`
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.LODGING) }

    // declares function `onUploadCarRentalClick` taking no parameters; its body is the expression ``
    fun onUploadCarRentalClick() =
        // continues the statement started above: `_uiState.update { it.copy(pendingUploadType = ItineraryUplo…`
        _uiState.update { it.copy(pendingUploadType = ItineraryUploadType.CAR_RENTAL) }

    // declares function `onPickerDismissed` taking no parameters; its body is the expression ``
    fun onPickerDismissed() =
        // continues the statement started above: `_uiState.update { it.copy(pendingUploadType = null) }`
        _uiState.update { it.copy(pendingUploadType = null) }


    // declares function `onFlightPdfPicked` taking 1 parameter (`pdfUri`) and opens its body
    fun onFlightPdfPicked(pdfUri: String) {
        // declares read-only property `activeDayId`, initialised to `_uiState.value.days`
        val activeDayId = _uiState.value.days
            // chained call `.getOrNull` on the previous result with arguments `(_uiState.value.activeDayIndex)`
            .getOrNull(_uiState.value.activeDayIndex)
            // expression: `?.dayId ?: return`
            ?.dayId ?: return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `insertFlight` on `repo` with arguments `(dayId = activeDayId, pdfUri = pdfUri, docNam…)`
            repo.insertFlight(dayId = activeDayId, pdfUri = pdfUri, docName = DocumentNames.displayName(getApplication(), pdfUri))
            // declares read-only property `flights`, initialised with the result of calling `loadFlightsFor(…)`
            val flights = loadFlightsFor(activeDayId)
            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `pendingUploadType = null,`
                    pendingUploadType   = null,
                    // continues the statement started above: `flightsForActiveDay = flights,`
                    flightsForActiveDay = flights,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `onFlightPdfPicked`
    }

    // declares function `onLodgingPdfPicked` taking 1 parameter (`pdfUri`) and opens its body
    fun onLodgingPdfPicked(pdfUri: String) {
        // declares read-only property `days`, initialised to `_uiState.value.days`
        val days = _uiState.value.days
        // `if` statement: executes `return` when `days.isEmpty()` is true
        if (days.isEmpty()) return
        // declares read-only property `from`, initialised with the result of calling `days.first(…)`
        val from = days.first().date
        // declares read-only property `to`, initialised with the result of calling `days.last(…)`
        val to   = days.last().date
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `insertLodging` on `repo` with arguments `(tripId, from, to, pdfUri, DocumentNames.disp…)`
            repo.insertLodging(tripId, from, to, pdfUri, DocumentNames.displayName(getApplication(), pdfUri))
            // expression: `_uiState.update { it.copy(pendingUploadType = null, lodgings = l…`
            _uiState.update { it.copy(pendingUploadType = null, lodgings = loadLodgings()) }
        // closes the block
        }
    // closes the function `onLodgingPdfPicked`
    }

    // declares function `onCarRentalPdfPicked` taking 1 parameter (`pdfUri`) and opens its body
    fun onCarRentalPdfPicked(pdfUri: String) {
        // declares read-only property `days`, initialised to `_uiState.value.days`
        val days = _uiState.value.days
        // `if` statement: executes `return` when `days.isEmpty()` is true
        if (days.isEmpty()) return
        // declares read-only property `from`, initialised with the result of calling `days.first(…)`
        val from = days.first().date
        // declares read-only property `to`, initialised with the result of calling `days.last(…)`
        val to   = days.last().date
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `insertCarRental` on `repo` with arguments `(tripId, from, to, pdfUri, DocumentNames.disp…)`
            repo.insertCarRental(tripId, from, to, pdfUri, DocumentNames.displayName(getApplication(), pdfUri))
            // expression: `_uiState.update { it.copy(pendingUploadType = null, carRentals =…`
            _uiState.update { it.copy(pendingUploadType = null, carRentals = loadCarRentals()) }
        // closes the block
        }
    // closes the function `onCarRentalPdfPicked`
    }


    // declares function `onDeleteFlight` taking 1 parameter (`flightId`) and opens its body
    fun onDeleteFlight(flightId: String) {
        // declares read-only property `activeDayId`, initialised to `_uiState.value.days`
        val activeDayId = _uiState.value.days
            // chained call `.getOrNull` on the previous result with arguments `(_uiState.value.activeDayIndex)`
            .getOrNull(_uiState.value.activeDayIndex)
            // expression: `?.dayId ?: return`
            ?.dayId ?: return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `deleteFlight` on `repo` with arguments `(flightId)`
            repo.deleteFlight(flightId)
            // declares read-only property `flights`, initialised with the result of calling `loadFlightsFor(…)`
            val flights = loadFlightsFor(activeDayId)
            // expression: `_uiState.update { it.copy(flightsForActiveDay = flights) }`
            _uiState.update { it.copy(flightsForActiveDay = flights) }
        // closes the block
        }
    // closes the function `onDeleteFlight`
    }

    // declares function `onDeleteLodging` taking 1 parameter (`lodgingId`) and opens its body
    fun onDeleteLodging(lodgingId: String) {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `deleteLodging` on `repo` with arguments `(lodgingId)`
            repo.deleteLodging(lodgingId)
            // expression: `_uiState.update { it.copy(lodgings = loadLodgings()) }`
            _uiState.update { it.copy(lodgings = loadLodgings()) }
        // closes the block
        }
    // closes the function `onDeleteLodging`
    }

    // declares function `onDeleteCarRental` taking 1 parameter (`carRentalId`) and opens its body
    fun onDeleteCarRental(carRentalId: String) {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `deleteCarRental` on `repo` with arguments `(carRentalId)`
            repo.deleteCarRental(carRentalId)
            // expression: `_uiState.update { it.copy(carRentals = loadCarRentals()) }`
            _uiState.update { it.copy(carRentals = loadCarRentals()) }
        // closes the block
        }
    // closes the function `onDeleteCarRental`
    }


    // declares function `onFlightNumberChanged` taking 2 parameters (`flightId`, `number`) and opens its body
    fun onFlightNumberChanged(flightId: String, number: String) {
        _uiState.update { state ->
            state.copy(
                flightsForActiveDay = state.flightsForActiveDay.map { f ->
                    if (f.id == flightId) f.copy(flightNumber = number) else f
                }
            )
        }
        viewModelScope.launch { repo.updateFlightNumber(flightId, number) }
        // auto-fill departure time: debounce 1.5 s then call AirLabs
        depTimeLookupJobs[flightId]?.cancel()
        val iata = number.replace(" ", "").uppercase()
        if (iata.length >= 4) {
            depTimeLookupJobs[flightId] = viewModelScope.launch {
                delay(1_500)
                val time = AirLabsRepository.lookupDepartureTime(iata)
                if (time != null) {
                    repo.updateFlightDepartureTime(flightId, time)
                    _uiState.update { state ->
                        state.copy(
                            flightsForActiveDay = state.flightsForActiveDay.map { f ->
                                if (f.id == flightId) f.copy(departureTime = time) else f
                            }
                        )
                    }
                }
            }
        }
    }

    // declares function `onFlightDepartureTimeChanged` taking 2 parameters (`flightId`, `time`) and opens its body
    fun onFlightDepartureTimeChanged(flightId: String, time: String?) {
        // expression: `_uiState.update { state ->`
        _uiState.update { state ->
            // continues the statement started above: `state.copy(`
            state.copy(
                // continues the statement started above: `flightsForActiveDay = state.flightsForActiveDay.map { f ->`
                flightsForActiveDay = state.flightsForActiveDay.map { f ->
                    // continues the statement started above: `if (f.id == flightId) f.copy(departureTime = time) else f`
                    if (f.id == flightId) f.copy(departureTime = time) else f
                // closes the block
                }
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // expression: `viewModelScope.launch { repo.updateFlightDepartureTime(flightId,…`
        viewModelScope.launch { repo.updateFlightDepartureTime(flightId, time) }
    // closes the function `onFlightDepartureTimeChanged`
    }


    // declares function `onDeletePlace` taking 1 parameter (`placeId`) and opens its body
    fun onDeletePlace(placeId: String) {
        // declares read-only property `activeDayId`, initialised to `_uiState.value.days`
        val activeDayId = _uiState.value.days
            // chained call `.getOrNull` on the previous result with arguments `(_uiState.value.activeDayIndex)`
            .getOrNull(_uiState.value.activeDayIndex)
            // expression: `?.dayId ?: return`
            ?.dayId ?: return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `deletePlace` on `repo` with arguments `(placeId)`
            repo.deletePlace(placeId)
            // declares read-only property `places`, initialised with the result of calling `loadPlacesFor(…)`
            val places = loadPlacesFor(activeDayId)
            // expression: `_uiState.update { it.copy(placesForActiveDay = places) }`
            _uiState.update { it.copy(placesForActiveDay = places) }
        // closes the block
        }
    // closes the function `onDeletePlace`
    }


    // declares function `refreshPlacesForActiveDay` taking no parameters and opens its body
    fun refreshPlacesForActiveDay() {
        // declares read-only property `dayId`, initialised to `activeDayId ?: return`
        val dayId = activeDayId ?: return
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // declares read-only property `places`, initialised with the result of calling `loadPlacesFor(…)`
            val places = loadPlacesFor(dayId)
            // expression: `_uiState.update { it.copy(placesForActiveDay = places) }`
            _uiState.update { it.copy(placesForActiveDay = places) }
        // closes the block
        }
    // closes the function `refreshPlacesForActiveDay`
    }
// closes the block
}
