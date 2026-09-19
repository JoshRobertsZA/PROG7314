// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `androidx.work.CoroutineWorker` for use in this file
import androidx.work.CoroutineWorker
// imports `androidx.work.WorkerParameters` for use in this file
import androidx.work.WorkerParameters
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.auth.AuthRepository` for use in this file
import com.waypoint.app.core.auth.AuthRepository
// imports `com.waypoint.app.core.db.TripEntity` for use in this file
import com.waypoint.app.core.db.TripEntity
// imports `com.waypoint.app.core.network.AirLabsRepository` for use in this file
import com.waypoint.app.core.network.AirLabsRepository
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.features.edititinerary.data.FlightEntity` for use in this file
import com.waypoint.app.features.edititinerary.data.FlightEntity
// imports `com.waypoint.app.features.edititinerary.data.ItineraryRepository` for use in this file
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
// imports `com.waypoint.app.features.home.data.WeatherRepository` for use in this file
import com.waypoint.app.features.home.data.WeatherRepository
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate
// imports `java.time.LocalTime` for use in this file
import java.time.LocalTime
// imports `java.time.temporal.ChronoUnit` for use in this file
import java.time.temporal.ChronoUnit

// declares class `TripReminderWorker` with a primary constructor taking 2 parameters (`context`, `params`), inheriting from `CoroutineWorker(context, params)` and opens its body
class TripReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    // declares private read-only property `ctx`, initialised to `applicationContext`
    private val ctx = applicationContext
    // declares private read-only property `trips`, initialised with the result of calling `TripRepository(…)`
    private val trips = TripRepository(ctx)
    // declares private read-only property `itinerary`, initialised with the result of calling `ItineraryRepository(…)`
    private val itinerary = ItineraryRepository(ctx)
    // declares private read-only property `log`, initialised with the result of calling `ReminderLogRepository(…)`
    private val log = ReminderLogRepository(ctx)

    // declares override suspend function `doWork` taking no parameters, returning `Result` and opens its body
    override suspend fun doWork(): Result {
        // declares read-only property `accountId`, initialised to `AuthRepository.currentUser?.uid ?: return Re…`
        val accountId = AuthRepository.currentUser?.uid ?: return Result.success()
        // `if` statement: executes `return Result.success()` when `!NotificationPreferences.isEnabled(ctx)` is true
        if (!NotificationPreferences.isEnabled(ctx)) return Result.success()

        // declares read-only property `now`, initialised with the result of calling `LocalTime.now(…)`
        val now   = LocalTime.now()
        // declares read-only property `today`, initialised with the result of calling `LocalDate.now(…)`
        val today = LocalDate.now()
        // declares read-only property `allTrips`, initialised with the result of calling `trips.getTripsForAccount(…)`
        val allTrips = trips.getTripsForAccount(accountId)

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // `when` expression on the value of `now.hour`: the first matching branch below runs
            when (now.hour) {
                // `when` branch: when the subject matches `MORNING_HOUR`, evaluates `allTrips.forEach { runMorningRules(it, …`
                MORNING_HOUR -> allTrips.forEach { runMorningRules(it, today) }
                // `when` branch: when the subject matches `EVENING_HOUR`, evaluates `allTrips.forEach { runEveningRules(it, …`
                EVENING_HOUR -> allTrips.forEach { runEveningRules(it, today) }
            // closes the when block
            }
        // `catch` block: handles a thrown `Exception` bound to `e`
        } catch (e: Exception) {
            // calls `w` on `Log` with arguments `(TAG, "Reminder pass failed", e)`
            Log.w(TAG, "Reminder pass failed", e)
            // returns `Result.retry()` from the current function
            return Result.retry()
        // closes the catch block
        }
        // returns `Result.success()` from the current function
        return Result.success()
    // closes the function `doWork`
    }


    // declares private suspend function `runMorningRules` taking 2 parameters (`trip`, `today`) and opens its body
    private suspend fun runMorningRules(trip: TripEntity, today: LocalDate) {
        // declares read-only property `start`, initialised with the result of calling `LocalDate.parse(…)`
        val start = LocalDate.parse(trip.startDate)
        // declares read-only property `end`, initialised with the result of calling `LocalDate.parse(…)`
        val end   = LocalDate.parse(trip.endDate)
        // declares read-only property `daysUntil`, initialised with the result of calling `ChronoUnit.DAYS.between(…)`
        val daysUntil = ChronoUnit.DAYS.between(today, start).toInt()
        // declares read-only property `place`, initialised to `trip.destination ?: trip.name`
        val place = trip.destination ?: trip.name

        // `if` statement: the block below runs when `daysUntil in COUNTDOWN_DAYS` is true
        if (daysUntil in COUNTDOWN_DAYS) {
            // calls `sendOnce` with arguments `("countdown:${trip.id}:$daysUntil")` and opens a trailing lambda / block
            sendOnce("countdown:${trip.id}:$daysUntil") {
                // declares read-only property `hasPlan`, initialised with the result of calling `itinerary.getSelectedDays(…)`
                val hasPlan = itinerary.getSelectedDays(trip.id).isNotEmpty()
                // `if` statement: the block below runs when `daysUntil == 5 && !hasPlan` is true
                if (daysUntil == 5 && !hasPlan) {
                    // calls `getString` on `ctx` with arguments `(R.string.reminder_no_plan_title, trip.name)`
                    ctx.getString(R.string.reminder_no_plan_title, trip.name) to
                        // calls `getString` on `ctx` with arguments `(R.string.reminder_no_plan_body, place)`
                        ctx.getString(R.string.reminder_no_plan_body, place)
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `getString` on `ctx` with arguments `(R.string.reminder_countdown_title, daysUntil)`
                    ctx.getString(R.string.reminder_countdown_title, daysUntil) to
                        // calls `getString` on `ctx` with arguments `(R.string.reminder_countdown_body, trip.name,…)`
                        ctx.getString(R.string.reminder_countdown_body, trip.name, place)
                // closes the else branch
                }
            // closes the lambda passed to `sendOnce`
            }
        // closes the if block
        }

        // `if` statement: the block below runs when `daysUntil == 0` is true
        if (daysUntil == 0) {
            // calls `sendOnce` with arguments `("start:${trip.id}")` and opens a trailing lambda / block
            sendOnce("start:${trip.id}") {
                // calls `getString` on `ctx` with arguments `(R.string.reminder_start_title, trip.name)`
                ctx.getString(R.string.reminder_start_title, trip.name) to
                    // calls `getString` on `ctx` with arguments `(R.string.reminder_start_body, place)`
                    ctx.getString(R.string.reminder_start_body, place)
            // closes the lambda passed to `sendOnce`
            }
        // closes the if block
        }

        // `if` statement: the block below runs when `!today.isBefore(start) && !today.isAfter(end)` is true
        if (!today.isBefore(start) && !today.isAfter(end)) {
            // declares read-only property `dayNumber`, initialised with the result of calling `ChronoUnit.DAYS.between(…)`
            val dayNumber = ChronoUnit.DAYS.between(start, today).toInt() + 1
            // declares read-only property `day`, initialised with the result of calling `itinerary.getSelectedDaysWithIds(…)`
            val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == today }

            // calls `sendOnce` with arguments `("digest:${trip.id}:$today")` and opens a trailing lambda / block
            sendOnce("digest:${trip.id}:$today") {
                // declares read-only property `parts`, initialised with the result of calling `mutableListOf(…)`
                val parts = mutableListOf<String>()
                // `if` statement: the block below runs when `day != null` is true
                if (day != null) {
                    // declares read-only property `places`, initialised with the result of calling `itinerary.getPlacesForDay(…)`
                    val places = itinerary.getPlacesForDay(day.id).map { it.name }
                    // `if` statement: executes `parts += ctx.getString(R.string.reminder_dig…` when `places.isNotEmpty()` is true
                    if (places.isNotEmpty()) parts += ctx.getString(R.string.reminder_digest_places, places.joinToString(", "))
                // closes the if block
                }
                // `if` statement: executes `parts += ctx.getString(R.string.reminder_dig…` when `itinerary.getLodgingsForTrip(trip.id).any { it.fromDate == today }` is true
                if (itinerary.getLodgingsForTrip(trip.id).any { it.fromDate == today }) parts += ctx.getString(R.string.reminder_digest_lodging)
                // `if` statement: executes `parts += ctx.getString(R.string.reminder_dig…` when `itinerary.getCarRentalsForTrip(trip.id).any { it.fromDate == today }` is true
                if (itinerary.getCarRentalsForTrip(trip.id).any { it.fromDate == today }) parts += ctx.getString(R.string.reminder_digest_car)

                // declares read-only property `body`, initialised with the result of calling `if(…)`
                val body = if (parts.isEmpty()) ctx.getString(R.string.reminder_digest_empty, dayNumber, place)
                           // statement: `else parts.joinToString(" ")`
                           else parts.joinToString(" ")
                // calls `getString` on `ctx` with arguments `(R.string.reminder_digest_title, dayNumber, t…)`
                ctx.getString(R.string.reminder_digest_title, dayNumber, trip.name) to body
            // closes the lambda passed to `sendOnce`
            }

            // `if` statement: the block below runs when `day != null` is true
            if (day != null) {
                // calls `getFlightsForDays` on `itinerary` with arguments `(listOf(day.id))`, then chains `.forEach { flight ->`
                itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                    // continues the statement started above: `val time = resolveDepartureTime(flight)`
                    val time = resolveDepartureTime(flight)
                    // `if` statement: executes `return@forEach` when `time != null && time < EARLY_CUTOFF` is true
                    if (time != null && time < EARLY_CUTOFF) return@forEach
                    // calls `sendOnce` with arguments `("flight:${flight.id}")` and opens a trailing lambda / block
                    sendOnce("flight:${flight.id}") {
                        // declares read-only property `number`, initialised to `flight.flightNumber?.takeIf { it.isNotBlank(…`
                        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                        // declares read-only property `body`, initialised with the result of calling `if(…)`
                        val body = if (time != null) ctx.getString(R.string.reminder_flight_today_body, number, time.toString())
                                   // statement: `else ctx.getString(R.string.reminder_flight_today_body_no_time, …`
                                   else ctx.getString(R.string.reminder_flight_today_body_no_time, number)
                        // calls `getString` on `ctx` with arguments `(R.string.reminder_flight_title)`
                        ctx.getString(R.string.reminder_flight_title) to body
                    // closes the lambda passed to `sendOnce`
                    }
                // closes the block
                }
            // closes the if block
            }
        // closes the if block
        }

        // `if` statement: the block below runs when `today == end.plusDays(1)` is true
        if (today == end.plusDays(1)) {
            // calls `sendOnce` with arguments `("ended:${trip.id}")` and opens a trailing lambda / block
            sendOnce("ended:${trip.id}") {
                // calls `getString` on `ctx` with arguments `(R.string.reminder_ended_title, trip.name)`
                ctx.getString(R.string.reminder_ended_title, trip.name) to
                    // calls `getString` on `ctx` with arguments `(R.string.reminder_ended_body)`
                    ctx.getString(R.string.reminder_ended_body)
            // closes the lambda passed to `sendOnce`
            }
        // closes the if block
        }
    // closes the function `runMorningRules`
    }


    // declares private suspend function `runEveningRules` taking 2 parameters (`trip`, `today`) and opens its body
    private suspend fun runEveningRules(trip: TripEntity, today: LocalDate) {
        // declares read-only property `start`, initialised with the result of calling `LocalDate.parse(…)`
        val start = LocalDate.parse(trip.startDate)
        // declares read-only property `end`, initialised with the result of calling `LocalDate.parse(…)`
        val end   = LocalDate.parse(trip.endDate)
        // declares read-only property `tomorrow`, initialised with the result of calling `today.plusDays(…)`
        val tomorrow = today.plusDays(1)
        // `if` statement: executes `return` when `tomorrow.isBefore(start) || tomorrow.isAfter(end)` is true
        if (tomorrow.isBefore(start) || tomorrow.isAfter(end)) return

        // declares read-only property `day`, initialised with the result of calling `itinerary.getSelectedDaysWithIds(…)`
        val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == tomorrow }
        // `if` statement: the block below runs when `day != null` is true
        if (day != null) {
            // calls `getFlightsForDays` on `itinerary` with arguments `(listOf(day.id))`, then chains `.forEach { flight ->`
            itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                // continues the statement started above: `val time = resolveDepartureTime(flight) ?: return@forEach`
                val time = resolveDepartureTime(flight) ?: return@forEach
                // `if` statement: executes `return@forEach` when `time >= EARLY_CUTOFF` is true
                if (time >= EARLY_CUTOFF) return@forEach
                // calls `sendOnce` with arguments `("flight:${flight.id}")` and opens a trailing lambda / block
                sendOnce("flight:${flight.id}") {
                    // declares read-only property `number`, initialised to `flight.flightNumber?.takeIf { it.isNotBlank(…`
                    val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                    // calls `getString` on `ctx` with arguments `(R.string.reminder_flight_title)`
                    ctx.getString(R.string.reminder_flight_title) to
                        // calls `getString` on `ctx` with arguments `(R.string.reminder_flight_tomorrow_body, numb…)`
                        ctx.getString(R.string.reminder_flight_tomorrow_body, number, time.toString())
                // closes the lambda passed to `sendOnce`
                }
            // closes the block
            }
        // closes the if block
        }

        // declares read-only property `destination`, initialised to `trip.destination ?: return`
        val destination = trip.destination ?: return
        // calls `sendOnce` with arguments `("weather:${trip.id}:$tomorrow")` and opens a trailing lambda / block
        sendOnce("weather:${trip.id}:$tomorrow") {
            // declares read-only property `weather`, initialised with the result of calling `WeatherRepository.getWeather(…)`
            val weather = WeatherRepository.getWeather(destination) ?: return@sendOnce null
            // declares read-only property `desc`, initialised with the result of calling `weather.description.lowercase(…)`
            val desc = weather.description.lowercase()
            // declares read-only property `bad`, initialised to `BAD_WEATHER.any { desc.contains(it) } || wea…`
            val bad = BAD_WEATHER.any { desc.contains(it) } || weather.tempC >= HOT_C || weather.tempC <= COLD_C
            // `if` statement: executes `return@sendOnce null` when `!bad` is true
            if (!bad) return@sendOnce null
            // calls `getString` on `ctx` with arguments `(R.string.reminder_weather_title, weather.dis…)`
            ctx.getString(R.string.reminder_weather_title, weather.displayName) to
                // calls `getString` on `ctx` with arguments `(R.string.reminder_weather_body, weather.desc…)`
                ctx.getString(R.string.reminder_weather_body, weather.description, weather.tempC.toInt())
        // closes the lambda passed to `sendOnce`
        }
    // closes the function `runEveningRules`
    }


    // declares private suspend function `resolveDepartureTime` taking 1 parameter (`flight`), returning `LocalTime?` and opens its body
    private suspend fun resolveDepartureTime(flight: FlightEntity): LocalTime? {
        // expression: `flight.departureTime?.let { return runCatching { LocalTime.parse…`
        flight.departureTime?.let { return runCatching { LocalTime.parse(it) }.getOrNull() }
        // declares read-only property `number`, initialised to `flight.flightNumber?.takeIf { it.isNotBlank(…`
        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: return null
        // calls `ensureLoaded` on `RemoteSecrets` with arguments `()`
        RemoteSecrets.ensureLoaded()
        // declares read-only property `looked`, initialised with the result of calling `AirLabsRepository.lookupDepartureTime(…)`
        val looked = AirLabsRepository.lookupDepartureTime(number)
        // if AirLabs returned nothing (unknown / fake flight number) store the sentinel so this flight is never looked up again
        if (looked == null) {
            itinerary.updateFlightDepartureTime(flight.id, AirLabsRepository.AIRLABS_MISS)
            return null
        }
        // calls `updateFlightDepartureTime` on `itinerary` with arguments `(flight.id, looked)`
        itinerary.updateFlightDepartureTime(flight.id, looked)
        // returns `runCatching { LocalTime.parse(looked) }.getOrNull()` from the current function
        return runCatching { LocalTime.parse(looked) }.getOrNull()
    // closes the function `resolveDepartureTime`
    }

    // declares private suspend function `sendOnce` taking 3 parameters (`key`, `build`, `String>?`) and opens its body
    private suspend fun sendOnce(key: String, build: suspend () -> Pair<String, String>?) {
        // `if` statement: executes `return` when `log.wasSent(key)` is true
        if (log.wasSent(key)) return
        // calls `val` with arguments `(title, body)`
        val (title, body) = build() ?: return
        // calls `show` on `PushNotifier` with arguments `(ctx, title, body)`
        PushNotifier.show(ctx, title, body)
        // calls `markSent` on `log` with arguments `(key)`
        log.markSent(key)
    // closes the function `sendOnce`
    }

    // declares the companion object holding members shared by all instances of the enclosing class
    private companion object {
        // declares const read-only property `TAG`, initialised to the string literal "TripReminderWorker"
        const val TAG = "TripReminderWorker"
        // declares const read-only property `MORNING_HOUR`, initialised to the number 7
        const val MORNING_HOUR = 7
        // declares const read-only property `EVENING_HOUR`, initialised to the number 19
        const val EVENING_HOUR = 19
        // declares read-only property `COUNTDOWN_DAYS`, initialised with the result of calling `setOf(…)`
        val COUNTDOWN_DAYS = setOf(30, 10, 5, 1)
        // declares read-only property `EARLY_CUTOFF` of type `LocalTime`, initialised with the result of calling `LocalTime.of(…)`
        val EARLY_CUTOFF: LocalTime = LocalTime.of(9, 0)
        // declares read-only property `BAD_WEATHER`, initialised with the result of calling `listOf(…)`
        val BAD_WEATHER = listOf("rain", "storm", "thunder", "snow", "drizzle", "hail")
        // declares const read-only property `HOT_C`, initialised to the number 35.0
        const val HOT_C = 35.0
        // declares const read-only property `COLD_C`, initialised to the number 3.0
        const val COLD_C = 3.0
    // closes the companion object
    }
// closes the class `TripReminderWorker`
}
