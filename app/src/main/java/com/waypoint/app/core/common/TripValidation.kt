package com.waypoint.app.core.common

import java.time.LocalDate

//Shared validation rules for trips and flights, so every screen checks input the same way.

object TripValidation {

    // Longest trip name allowed.
    const val TRIP_NAME_MAX = 40

    // Airline code (2 characters, not both digits) + 1–4 digit number + optional letter, e.g. BA2490, 4Z123, U2123A
    private val FLIGHT_NUMBER_REGEX = Regex("^([A-Z][A-Z0-9]|[0-9][A-Z])[0-9]{1,4}[A-Z]?$")

    // A trip name must have at least one character and be no longer than TRIP_NAME_MAX.
    fun isTripNameValid(name: String): Boolean = name.trim().length in 1..TRIP_NAME_MAX

    // Both dates must be picked and the end date can't be before the start date.
    fun isDateOrderValid(start: LocalDate?, end: LocalDate?): Boolean =
        start != null && end != null && !end.isBefore(start)

    // Removes spaces and uppercases, so "ba 2490" becomes "BA2490".
    fun normaliseFlightNumber(input: String): String = input.replace(" ", "").uppercase()

    // True when the flight number matches the IATA format after normalising.
    fun isFlightNumberValid(input: String): Boolean =
        FLIGHT_NUMBER_REGEX.matches(normaliseFlightNumber(input))
}