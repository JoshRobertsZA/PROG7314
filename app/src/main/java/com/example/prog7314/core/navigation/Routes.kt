package com.example.prog7314.core.navigation

/**
 * Route constants for the top-level flat NavHost hosted in MainActivity:
 *
 *   Main -> Login, TripCalendar, ViewItinerary, EditItinerary, AllTrips,
 *           Settings, Explore, Home (all temporary scratch nav)
 *   Login -> Register, Home
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
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val NewTrip = "new_trip"
    const val TripCalendar = "trip_calendar"
    const val AllTrips = "all_trips"
    const val EditItinerary = "edit_itinerary"
    const val ViewItinerary = "view_itinerary"
    const val Explore = "explore"
    const val Settings = "settings"
    const val Notifications = "notifications"
    const val PlaceDetail = "place_detail"
}
