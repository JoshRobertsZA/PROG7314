package com.waypoint.app.core.notifications

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.waypoint.app.R
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.db.TripEntity
import com.waypoint.app.core.network.AirLabsRepository
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.edititinerary.data.FlightEntity
import com.waypoint.app.features.edititinerary.data.ItineraryRepository
import com.waypoint.app.features.home.data.WeatherRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import java.time.LocalDate
import java.time.LocalTime
import java.time.temporal.ChronoUnit

/**
 * Evaluates every scheduled-reminder rule against the signed-in account's
 * trips. Runs hourly (see TripReminderScheduler); rules are gated by the
 * current hour and de-duplicated through ReminderLogRepository so each one
 * fires exactly once per trip/day.
 *
 * Morning window (07:00-07:59):
 *  1. Countdown        30 / 10 / 5 / 1 days before start.
 *  2. No plan yet      replaces the 5-day countdown when the trip has no itinerary days.
 *  3. Starts today     on the start date.
 *  4. Daily digest     each active-trip day: places, lodging check-in, car pick-up.
 *  5. Flight today     departures at/after 09:00 (or unknown time) on that day.
 *  6. Trip ended       the morning after the end date.
 *
 * Evening window (19:00-19:59):
 *  7. Early flight     tomorrow's departures before 09:00.
 *  8. Weather warning  rain / storms / extreme temps at the destination before an active day.
 *
 * Uses the persisted Firebase user rather than SessionManager because the
 * worker can run while no Activity has restored the in-memory session.
 */
class TripReminderWorker(context: Context, params: WorkerParameters) : CoroutineWorker(context, params) {

    private val ctx = applicationContext
    private val trips = TripRepository(ctx)
    private val itinerary = ItineraryRepository(ctx)
    private val log = ReminderLogRepository(ctx)

    override suspend fun doWork(): Result {
        val accountId = AuthRepository.currentUser?.uid ?: return Result.success()
        if (!NotificationPreferences.isEnabled(ctx)) return Result.success()

        val now   = LocalTime.now()
        val today = LocalDate.now()
        val allTrips = trips.getTripsForAccount(accountId)

        try {
            when (now.hour) {
                MORNING_HOUR -> allTrips.forEach { runMorningRules(it, today) }
                EVENING_HOUR -> allTrips.forEach { runEveningRules(it, today) }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Reminder pass failed", e)
            return Result.retry()
        }
        return Result.success()
    }

    // ── Morning ───────────────────────────────────────────────────────────────

    private suspend fun runMorningRules(trip: TripEntity, today: LocalDate) {
        val start = LocalDate.parse(trip.startDate)
        val end   = LocalDate.parse(trip.endDate)
        val daysUntil = ChronoUnit.DAYS.between(today, start).toInt()
        val place = trip.destination ?: trip.name

        // 1 + 2. Countdown / no plan yet
        if (daysUntil in COUNTDOWN_DAYS) {
            sendOnce("countdown:${trip.id}:$daysUntil") {
                val hasPlan = itinerary.getSelectedDays(trip.id).isNotEmpty()
                if (daysUntil == 5 && !hasPlan) {
                    ctx.getString(R.string.reminder_no_plan_title, trip.name) to
                        ctx.getString(R.string.reminder_no_plan_body, place)
                } else {
                    ctx.getString(R.string.reminder_countdown_title, daysUntil) to
                        ctx.getString(R.string.reminder_countdown_body, trip.name, place)
                }
            }
        }

        // 3. Starts today
        if (daysUntil == 0) {
            sendOnce("start:${trip.id}") {
                ctx.getString(R.string.reminder_start_title, trip.name) to
                    ctx.getString(R.string.reminder_start_body, place)
            }
        }

        // 4 + 5. Active day: digest and same-day flights
        if (!today.isBefore(start) && !today.isAfter(end)) {
            val dayNumber = ChronoUnit.DAYS.between(start, today).toInt() + 1
            val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == today }

            sendOnce("digest:${trip.id}:$today") {
                val parts = mutableListOf<String>()
                if (day != null) {
                    val places = itinerary.getPlacesForDay(day.id).map { it.name }
                    if (places.isNotEmpty()) parts += ctx.getString(R.string.reminder_digest_places, places.joinToString(", "))
                }
                itinerary.getLodgingForTrip(trip.id)?.takeIf { it.fromDate == today }
                    ?.let { parts += ctx.getString(R.string.reminder_digest_lodging) }
                itinerary.getCarRentalForTrip(trip.id)?.takeIf { it.fromDate == today }
                    ?.let { parts += ctx.getString(R.string.reminder_digest_car) }

                val body = if (parts.isEmpty()) ctx.getString(R.string.reminder_digest_empty, dayNumber, place)
                           else parts.joinToString(" ")
                ctx.getString(R.string.reminder_digest_title, dayNumber, trip.name) to body
            }

            if (day != null) {
                itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                    val time = resolveDepartureTime(flight)
                    // Early flights were announced last night (rule 7); skip them here.
                    if (time != null && time < EARLY_CUTOFF) return@forEach
                    sendOnce("flight:${flight.id}") {
                        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                        val body = if (time != null) ctx.getString(R.string.reminder_flight_today_body, number, time.toString())
                                   else ctx.getString(R.string.reminder_flight_today_body_no_time, number)
                        ctx.getString(R.string.reminder_flight_title) to body
                    }
                }
            }
        }

        // 6. Ended yesterday
        if (today == end.plusDays(1)) {
            sendOnce("ended:${trip.id}") {
                ctx.getString(R.string.reminder_ended_title, trip.name) to
                    ctx.getString(R.string.reminder_ended_body)
            }
        }
    }

    // ── Evening ───────────────────────────────────────────────────────────────

    private suspend fun runEveningRules(trip: TripEntity, today: LocalDate) {
        val start = LocalDate.parse(trip.startDate)
        val end   = LocalDate.parse(trip.endDate)
        val tomorrow = today.plusDays(1)
        if (tomorrow.isBefore(start) || tomorrow.isAfter(end)) return

        // 7. Early flights tomorrow
        val day = itinerary.getSelectedDaysWithIds(trip.id).firstOrNull { it.date == tomorrow }
        if (day != null) {
            itinerary.getFlightsForDays(listOf(day.id)).forEach { flight ->
                val time = resolveDepartureTime(flight) ?: return@forEach
                if (time >= EARLY_CUTOFF) return@forEach
                sendOnce("flight:${flight.id}") {
                    val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: ctx.getString(R.string.reminder_flight_unnamed)
                    ctx.getString(R.string.reminder_flight_title) to
                        ctx.getString(R.string.reminder_flight_tomorrow_body, number, time.toString())
                }
            }
        }

        // 8. Weather warning for tomorrow
        val destination = trip.destination ?: return
        sendOnce("weather:${trip.id}:$tomorrow") {
            val weather = WeatherRepository.getWeather(destination) ?: return@sendOnce null
            val desc = weather.description.lowercase()
            val bad = BAD_WEATHER.any { desc.contains(it) } || weather.tempC >= HOT_C || weather.tempC <= COLD_C
            if (!bad) return@sendOnce null
            ctx.getString(R.string.reminder_weather_title, weather.displayName) to
                ctx.getString(R.string.reminder_weather_body, weather.description, weather.tempC.toInt())
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    /**
     * User-entered time wins. Otherwise, if there's a flight number, ask
     * AirLabs once and persist the answer so later runs don't hit the API.
     */
    private suspend fun resolveDepartureTime(flight: FlightEntity): LocalTime? {
        flight.departureTime?.let { return runCatching { LocalTime.parse(it) }.getOrNull() }
        val number = flight.flightNumber?.takeIf { it.isNotBlank() } ?: return null
        RemoteSecrets.ensureLoaded()
        val looked = AirLabsRepository.lookupDepartureTime(number) ?: return null
        itinerary.updateFlightDepartureTime(flight.id, looked)
        return runCatching { LocalTime.parse(looked) }.getOrNull()
    }

    /** Runs [build] only if [key] hasn't fired; a null result means "nothing to send" and is not logged. */
    private suspend fun sendOnce(key: String, build: suspend () -> Pair<String, String>?) {
        if (log.wasSent(key)) return
        val (title, body) = build() ?: return
        PushNotifier.show(ctx, title, body)
        log.markSent(key)
    }

    private companion object {
        const val TAG = "TripReminderWorker"
        const val MORNING_HOUR = 7
        const val EVENING_HOUR = 19
        val COUNTDOWN_DAYS = setOf(30, 10, 5, 1)
        val EARLY_CUTOFF: LocalTime = LocalTime.of(9, 0)
        val BAD_WEATHER = listOf("rain", "storm", "thunder", "snow", "drizzle", "hail")
        const val HOT_C = 35.0
        const val COLD_C = 3.0
    }
}
