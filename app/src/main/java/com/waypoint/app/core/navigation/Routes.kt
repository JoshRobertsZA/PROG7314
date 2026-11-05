package com.waypoint.app.core.navigation

/** Navigation routes for the top-level NavHost, with helpers that fill in route arguments. */
object Routes {
    const val Splash = "splash"
    const val Welcome = "welcome"
    const val EmailAuth = "email_auth"
    const val EditProfile = "edit_profile"
    const val Home = "home"
    const val NewTrip = "new_trip"
    const val TripCalendar = "trip_calendar/{tripId}"
    fun tripCalendar(tripId: String) = "trip_calendar/$tripId"
    const val EditItinerary = "edit_itinerary/{tripId}?selectedDates={selectedDates}"
    fun editItinerary(tripId: String, selectedDates: String = "") =
        if (selectedDates.isEmpty()) "edit_itinerary/$tripId"
        else "edit_itinerary/$tripId?selectedDates=$selectedDates"
    const val ViewItinerary = "view_itinerary/{tripId}"
    fun viewItinerary(tripId: String) = "view_itinerary/$tripId"
    const val Notifications = "notifications"
    const val PlacePicker = "place_picker/{tripId}/{dayId}/{category}"
    fun placePicker(tripId: String, dayId: String, category: String) = "place_picker/$tripId/$dayId/$category"
    const val PlaceDetailPicker = "place_detail_picker/{tripId}/{dayId}/{category}/{placeId}"
    fun placeDetailPicker(tripId: String, dayId: String, category: String, placeId: String) = "place_detail_picker/$tripId/$dayId/$category/$placeId"
}
