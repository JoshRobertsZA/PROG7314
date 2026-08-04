package com.example.prog7314.core.navigation

/**
 * Route constants for the single flat NavHost hosted in MainActivity.
 * Covers exactly the navigation graph the XML/Intent version had - see
 * MainActivity.kt (pre-migration) and each screen's own Intent calls:
 *
 *   Main -> Login, TripCalendar, ViewItinerary, EditItinerary, AllTrips,
 *           Settings, NearbyPlaces (all temporary scratch nav)
 *   Login -> Register, Home
 *   Home -> NewTrip
 *   NewTrip / TripCalendar / EditItinerary / ViewItinerary / AllTrips /
 *   NearbyPlaces / Settings -> back (finish()/popBackStack only)
 *
 * No authenticated-shell/bottom-nav nesting exists in the source, so this
 * is intentionally one flat graph, not a nested NavHost.
 */
object Routes {
    const val Main = "main"
    const val Login = "login"
    const val Register = "register"
    const val Home = "home"
    const val NewTrip = "new_trip"
    const val TripCalendar = "trip_calendar"
    const val AllTrips = "all_trips"
    const val EditItinerary = "edit_itinerary"
    const val ViewItinerary = "view_itinerary"
    const val NearbyPlaces = "nearby_places"
    const val Settings = "settings"
}
