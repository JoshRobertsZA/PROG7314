package com.waypoint.app.core.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.waypoint.app.R
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.db.TripEntity
import com.waypoint.app.core.network.AirLabsRepository
import com.waypoint.app.core.network.GuardedApi
import com.waypoint.app.core.network.UsageGuardian
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.edititinerary.data.FlightEntity
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import com.waypoint.app.features.home.data.WeatherRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit


internal const val MORNING_HOUR = 7
internal const val EVENING_HOUR = 19

/** The two sets of reminder rules: morning (countdowns, start, today's digest, ended) and evening (tomorrow). */
internal enum class ReminderPass { MORNING, EVENING }

/**
 * Which rule sets are due at [hour]. Rules run on every check from their hour onward (not only at
 * exactly 07:00 or 19:00), and ReminderLogRepository makes sure each reminder is still sent once.
 */
internal fun reminderPassesDue(hour: Int): Set<ReminderPass> = buildSet {
    if (hour >= MORNING_HOUR) add(ReminderPass.MORNING)
    if (hour >= EVENING_HOUR) add(ReminderPass.EVENING)
}

/**
 * The smart trip reminders. Checks every trip of the signed-in user against the morning and evening
 * rules and posts any reminder that is due and hasn't been sent yet.
 */
class TripReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val ctx = applicationContext
    private val trips = TripRepository(ctx)
    private val itinerary = ItineraryRepository(ctx)
    private val log = ReminderLogRepository(ctx)

    override suspend fun doWork(): Result {
        val accountId = AuthRepository.currentUser?.uid ?: return Result.success()
        if (!NotificationPreferences.isEnabled(NotificationType.REMINDERS)) return Result.success()

        val now   = LocalTime.now()
        val today = LocalDate.now()
        val allTrips = trips.getTripsForAccount(accountId)

        try {
            val passes = reminderPassesDue(now.hour)
            Log.d(TAG, "Reminder check at ${now.hour}h for ${allTrips.size} trips: $passes")
            if (ReminderPass.MORNING in passes) allTrips.forEach { runMorningRules(it, today) }
            if (ReminderPass.EVENING in passes) allTrips.forEach { runEveningRules(it, today) }
        } catch (e: Exception) {
            Log.w(TAG, "Reminder pass failed", e)
            return Result.retry()
        }
        return Result.success()
    }


    private suspend fun runMorningRules(trip: TripEntity, today: LocalDate) {
        val start = LocalDate.parse(trip.startDate)
        val end   = LocalDate.parse(trip.endDate)
        val daysUntil = ChronoUnit.DAYS.between(today, start).toInt()
        val place = trip.destination ?: trip.name

        if (daysUntil in COUNTDOWN_DAYS) {
            sendOnce(trip.id, "countdown:${trip.id}:$daysUntil") {
                val hasPlan = itinerary.getSelectedDays(trip.id).isNotEmpty()
                if (daysUntil == 5 && !hasPlan) {
                    ctx.getString(R.string.reminder_no_plan_title, trip.name) to
                        ctx.getString(R.string.reminder_no_plan_body, place)
                } else {
                    ctx.resources.getQuantityString(R.plurals.reminder_countdown_title, daysUntil, daysUntil) to
                        ctx.getString(R.string.reminder_countdown_body, trip.name, place)
                }
            }
        }

        if (daysUntil == 0) {
            sendOnce(trip.id, "start:${trip.id}") {
                ctx.getString(R.string.reminder_start_title, trip.name) to
                    ctx.getString(R.string.reminder_start_body, place)
            }
        }

        if (!today.isBefore(start) && !today.isAfter(end)) {
            val dayNumber = ChronoUnit.DAYS.between(start, today).toInt() + 1
            val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == today }

            sendOnce(trip.id, "digest:${trip.id}:$today") {
                val parts = mutableListOf<String>()
                if (day != null) {
                    val places = itinerary.getPlacesForDay(day.id).map { it.name }
                    if (places.isNotEmpty()) parts += ctx.getString(R.string.reminder_digest_places, places.joinToString(", "))
                }
                if (itinerary.getLodgingsForTrip(trip.id).any { it.fromDate == today }) parts += ctx.getString(R.string.reminder_digest_lodging)
                if (itinerary.getCarRentalsForTrip(trip.id).any { it.fromDate == today }) parts += ctx.getString(R.string.reminder_digest_car)

                val body = if (parts.isEmpty()) ctx.getString(R.string.reminder_digest_empty, dayNumber, place)
                           else parts.joinToString(" ")
                ctx.getString(R.string.reminder_digest_title, dayNumber, trip.name) to body
            }

            if (day != null) {
                itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                    val time = resolveDepartureTime(flight)
                    if (time != null && time < EARLY_CUTOFF) return@forEach
                    sendOnce(trip.id, "flight:${flight.id}") {
                        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                        val body = if (time != null) ctx.getString(R.string.reminder_flight_today_body, number, time.toString())
                                   else ctx.getString(R.string.reminder_flight_today_body_no_time, number)
                        ctx.getString(R.string.reminder_flight_title) to body
                    }
                }
            }
        }

        if (today == end.plusDays(1)) {
            sendOnce(trip.id, "ended:${trip.id}") {
                ctx.getString(R.string.reminder_ended_title, trip.name) to
                    ctx.getString(R.string.reminder_ended_body)
            }
        }
    }


    private suspend fun runEveningRules(trip: TripEntity, today: LocalDate) {
        val start = LocalDate.parse(trip.startDate)
        val end   = LocalDate.parse(trip.endDate)
        val tomorrow = today.plusDays(1)
        if (tomorrow.isBefore(start) || tomorrow.isAfter(end)) return

        val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == tomorrow }
        if (day != null) {
            itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                val time = resolveDepartureTime(flight) ?: return@forEach
                if (time >= EARLY_CUTOFF) return@forEach
                sendOnce(trip.id, "flight:${flight.id}") {
                    val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                    ctx.getString(R.string.reminder_flight_title) to
                        ctx.getString(R.string.reminder_flight_tomorrow_body, number, time.toString())
                }
            }
        }

        val destination = trip.destination ?: return
        sendOnce(trip.id, "weather:${trip.id}:$tomorrow") {
            val weather = WeatherRepository.getWeather(destination) ?: return@sendOnce null
            val desc = weather.description.lowercase()
            val bad = BAD_WEATHER.any { desc.contains(it) } || weather.tempC >= HOT_C || weather.tempC <= COLD_C
            if (!bad) return@sendOnce null
            ctx.getString(R.string.reminder_weather_title, weather.displayName) to
                ctx.getString(R.string.reminder_weather_body, weather.description, weather.tempC.toInt())
        }
    }


    private suspend fun resolveDepartureTime(flight: FlightEntity): LocalTime? {
        flight.departureTime?.let { return runCatching { LocalTime.parse(it) }.getOrNull() }
        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: return null
        RemoteSecrets.ensureLoaded()
        if (!UsageGuardian.isAllowed(GuardedApi.AIRLABS)) return null
        val looked = AirLabsRepository.lookupDepartureTime(number)
        if (looked == null) {
            itinerary.updateFlightDepartureTime(flight.id, AirLabsRepository.AIRLABS_MISS)
            return null
        }
        itinerary.updateFlightDepartureTime(flight.id, looked)
        return runCatching { LocalTime.parse(looked) }.getOrNull()
    }

    private suspend fun sendOnce(tripId: String, key: String, build: suspend () -> Pair<String, String>?) {
        if (log.wasSent(key)) return
        val (title, body) = build() ?: return
        PushNotifier.show(ctx, NotificationType.REMINDERS, title, body, mapOf("tripId" to tripId))
        log.markSent(key)
    }

    private companion object {
        const val TAG = "TripReminderWorker"
        val COUNTDOWN_DAYS = setOf(30, 10, 5, 1)
        val EARLY_CUTOFF: LocalTime = LocalTime.of(9, 0)
        val BAD_WEATHER = listOf("rain", "storm", "thunder", "snow", "drizzle", "hail")
        const val HOT_C = 35.0
        const val COLD_C = 3.0
    }
}
