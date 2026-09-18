package com.waypoint.app.core.navigation

/**
 * Route constants for the top-level flat NavHost hosted in MainActivity:
 *
 *   Main -> TripCalendar, ViewItinerary, EditItinerary, AllTrips,
 *           Settings, Explore, Home (all temporary scratch nav)
 *   Welcome -> Home (Google/GitHub SSO handles both sign-in and sign-up -
 *           there's no separate Login/Register flow)
 *   Home -> renders MainNavShell, which owns its own nested NavHost for
 *           the Home/Trips/Explore/Profile tabs (see MainNavShell.kt) -
 *           NewTrip is pushed on the top-level controller from inside it,
 *           covering the shell entirely.
 *   NewTrip / TripCalendar / EditItinerary / ViewItinerary / AllTrips /
 *   Explore / Settings / PlaceDetail -> back (finish()/popBackStack only)
 */
object Routes {
    const val Main = "main"
    const val Welcome = "welcome"
    const val Home = "home"
    const val NewTrip = "new_trip"
    const val TripCalendar = "trip_calendar/{tripId}"
    fun tripCalendar(tripId: String) = "trip_calendar/$tripId"
    const val AllTrips = "all_trips"
    const val EditItinerary = "edit_itinerary/{tripId}"
    fun editItinerary(tripId: String) = "edit_itinerary/$tripId"
    const val ViewItinerary = "view_itinerary/{tripId}"
    fun viewItinerary(tripId: String) = "view_itinerary/$tripId"
    const val Explore = "explore"
    const val Settings = "settings"
    const val Notifications = "notifications"
    const val PlaceDetail = "place_detail"
    const val PlacePicker = "place_picker/{tripId}/{dayId}/{category}"
    fun placePicker(tripId: String, dayId: String, category: String) = "place_picker/$tripId/$dayId/$category"
    const val PlaceDetailPicker = "place_detail_picker/{tripId}/{dayId}/{category}/{placeId}"
    fun placeDetailPicker(tripId: String, dayId: String, category: String, placeId: String) = "place_detail_picker/$tripId/$dayId/$category/$placeId"
}
