// declares that this file belongs to the package `com.waypoint.app.features.edititinerary.ui`
package com.waypoint.app.features.edititinerary.ui

// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// declares enum class `ItineraryUploadType` and opens its body
enum class ItineraryUploadType {
    // expression: `FLIGHT,`
    FLIGHT,
    // continues the statement started above: `LODGING,`
    LODGING,
    // continues the statement started above: `CAR_RENTAL,`
    CAR_RENTAL,
// closes the class `ItineraryUploadType`
}

// expression: `data class DayItem(`
data class DayItem(
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val date: LocalDate,`
    val date: LocalDate,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `label` of type `String`
    val label: String
        // custom getter: returns `"${date.dayOfMonth} ${`
        get() = "${date.dayOfMonth} ${
            date.month.name
                .lowercase()
                .replaceFirstChar { it.uppercaseChar() }
                .take(3)
        }"
// closes the block
}

// expression: `data class FlightItem(`
data class FlightItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val flightNumber: String,`
    val flightNumber: String,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val departureTime: String? = null,`
    val departureTime: String? = null,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class LodgingItem(`
data class LodgingItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class CarRentalItem(`
data class CarRentalItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val fromDate: LocalDate,`
    val fromDate: LocalDate,
    // continues the statement started above: `val toDate: LocalDate,`
    val toDate: LocalDate,
    // continues the statement started above: `val pdfUri: String,`
    val pdfUri: String,
    // continues the statement started above: `val docName: String? = null,`
    val docName: String? = null,
// closes the multi-line argument list started above
)


// expression: `data class PlaceItem(`
data class PlaceItem(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val dayId: String,`
    val dayId: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val category: String,`
    val category: String,
    // continues the statement started above: `val note: String?,`
    val note: String?,
// closes the multi-line argument list started above
)

// expression: `data class EditItineraryUiState(`
data class EditItineraryUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val days: List<DayItem> = emptyList(),`
    val days: List<DayItem> = emptyList(),
    // continues the statement started above: `val activeDayIndex: Int = 0,`
    val activeDayIndex: Int = 0,
    // continues the statement started above: `val flightsForActiveDay: List<FlightItem> = emptyList(),`
    val flightsForActiveDay: List<FlightItem> = emptyList(),
    // continues the statement started above: `val lodgings: List<LodgingItem> = emptyList(),`
    val lodgings: List<LodgingItem> = emptyList(),
    // continues the statement started above: `val carRentals: List<CarRentalItem> = emptyList(),`
    val carRentals: List<CarRentalItem> = emptyList(),
    // continues the statement started above: `val pendingUploadType: ItineraryUploadType? = null,`
    val pendingUploadType: ItineraryUploadType? = null,
    // continues the statement started above: `val placesForActiveDay: Map<String, List<PlaceItem>> = empt…`
    val placesForActiveDay: Map<String, List<PlaceItem>> = emptyMap(),
// ends the argument list started above and opens the block that follows
) {
    // expression: `private val activeDate: LocalDate? get() = days.getOrNull(active…`
    private val activeDate: LocalDate? get() = days.getOrNull(activeDayIndex)?.date
    // declares read-only property `lodgingForActiveDay` of type `List<LodgingItem>`
    val lodgingForActiveDay: List<LodgingItem>
        // custom getter: returns `activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && …`
        get() = activeDate?.let { d -> lodgings.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
    // declares read-only property `carRentalsForActiveDay` of type `List<CarRentalItem>`
    val carRentalsForActiveDay: List<CarRentalItem>
        // custom getter: returns `activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) &…`
        get() = activeDate?.let { d -> carRentals.filter { !d.isBefore(it.fromDate) && !d.isAfter(it.toDate) } }.orEmpty()
// closes the block
}
