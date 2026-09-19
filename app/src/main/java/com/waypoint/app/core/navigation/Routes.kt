// declares that this file belongs to the package `com.waypoint.app.core.navigation`
package com.waypoint.app.core.navigation

// declares object `Routes` and opens its body
object Routes {
    // declares const read-only property `Main`, initialised to the string literal "main"
    const val Main = "main"
    // declares const read-only property `Splash`, initialised to the string literal "splash"
    const val Splash = "splash"
    // declares const read-only property `Welcome`, initialised to the string literal "welcome"
    const val Welcome = "welcome"
    // declares const read-only property `Home`, initialised to the string literal "home"
    const val Home = "home"
    // declares const read-only property `NewTrip`, initialised to the string literal "new_trip"
    const val NewTrip = "new_trip"
    // declares const read-only property `TripCalendar`, initialised to the string literal "trip_calendar/{tripId}"
    const val TripCalendar = "trip_calendar/{tripId}"
    // declares function `tripCalendar` taking 1 parameter (`tripId`); its body is the expression `"trip_calendar/$tripId"`
    fun tripCalendar(tripId: String) = "trip_calendar/$tripId"
    // declares const read-only property `AllTrips`, initialised to the string literal "all_trips"
    const val AllTrips = "all_trips"
    // edit_itinerary/{tripId}/{selectedDates} — selectedDates is pipe-separated ISO dates, e.g. "2024-12-23|2024-12-28"
    const val EditItinerary = "edit_itinerary/{tripId}/{selectedDates}"
    fun editItinerary(tripId: String, selectedDates: String) = "edit_itinerary/$tripId/$selectedDates"
    // declares const read-only property `ViewItinerary`, initialised to the string literal "view_itinerary/{tripId}"
    const val ViewItinerary = "view_itinerary/{tripId}"
    // declares function `viewItinerary` taking 1 parameter (`tripId`); its body is the expression `"view_itinerary/$tripId"`
    fun viewItinerary(tripId: String) = "view_itinerary/$tripId"
    // declares const read-only property `Explore`, initialised to the string literal "explore"
    const val Explore = "explore"
    // declares const read-only property `Settings`, initialised to the string literal "settings"
    const val Settings = "settings"
    // declares const read-only property `Notifications`, initialised to the string literal "notifications"
    const val Notifications = "notifications"
    // declares const read-only property `PlaceDetail`, initialised to the string literal "place_detail"
    const val PlaceDetail = "place_detail"
    // declares const read-only property `PlacePicker`, initialised to the string literal "place_picker/{tripId}/{dayId}/{categor…
    const val PlacePicker = "place_picker/{tripId}/{dayId}/{category}"
    // declares function `placePicker` taking 3 parameters (`tripId`, `dayId`, `category`); its body is the expression `"place_picker/$tripId/$dayId/$category"`
    fun placePicker(tripId: String, dayId: String, category: String) = "place_picker/$tripId/$dayId/$category"
    // declares const read-only property `PlaceDetailPicker`, initialised to the string literal "place_detail_picker/{tripId}/{dayId}/{…
    const val PlaceDetailPicker = "place_detail_picker/{tripId}/{dayId}/{category}/{placeId}"
    // declares function `placeDetailPicker` taking 4 parameters (`tripId`, `dayId`, `category`, `placeId`); its body is the expression `"place_detail_picker/$tripId/$dayId/$categor…`
    fun placeDetailPicker(tripId: String, dayId: String, category: String, placeId: String) = "place_detail_picker/$tripId/$dayId/$category/$placeId"
// closes the object `Routes`
}
