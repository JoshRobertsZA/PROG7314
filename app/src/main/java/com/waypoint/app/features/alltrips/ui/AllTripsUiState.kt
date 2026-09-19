// declares that this file belongs to the package `com.waypoint.app.features.alltrips.ui`
package com.waypoint.app.features.alltrips.ui

// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `com.waypoint.app.core.db.TripEntity` for use in this file
import com.waypoint.app.core.db.TripEntity
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent1` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent1
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent2` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent2
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent3` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent3
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent4` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent4
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.WaypointTripRange` for use in this file
import com.waypoint.app.core.theme.WaypointTripRange
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// expression: `enum class TripFilter { ALL, UPCOMING, ONGOING, PAST }`
enum class TripFilter { ALL, UPCOMING, ONGOING, PAST }

// expression: `enum class TripStatus { ONGOING, UPCOMING, COMPLETED }`
enum class TripStatus { ONGOING, UPCOMING, COMPLETED }

// declares sealed class `TripBadge` and opens its body
sealed class TripBadge {
    // statement: `object Tomorrow : TripBadge()`
    object Tomorrow : TripBadge()
    // declares data class `InDays` with a primary constructor taking 1 parameter (`days`), inheriting from `TripBadge()`
    data class InDays(val days: Long) : TripBadge()
    // statement: `object Ongoing : TripBadge()`
    object Ongoing : TripBadge()
    // statement: `object Completed : TripBadge()`
    object Completed : TripBadge()
// closes the class `TripBadge`
}

// expression: `@androidx.compose.runtime.Composable`
@androidx.compose.runtime.Composable
// declares function `tripBadgeLabel` taking 1 parameter (`badge`), returning `String`; its body is the expression `when (badge) {`
fun tripBadgeLabel(badge: TripBadge): String = when (badge) {
    // lambda `TripBadge.Tomorrow -> androidx.compose.ui.res.strin…`
    TripBadge.Tomorrow      -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_tomorrow)
    // lambda `is TripBadge.InDays -> androidx.compose.ui.res.strin…`
    is TripBadge.InDays     -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_in_days, badge.days)
    // lambda `TripBadge.Ongoing -> androidx.compose.ui.res.strin…`
    TripBadge.Ongoing       -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_ongoing)
    // lambda `TripBadge.Completed -> androidx.compose.ui.res.strin…`
    TripBadge.Completed     -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_completed)
// closes the block
}

// expression: `data class TripRow(`
data class TripRow(
    // continues the statement started above: `val id: String,`
    val id: String,
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `val destination: String,`
    val destination: String,
    // continues the statement started above: `val dates: String,`
    val dates: String,
    // continues the statement started above: `val status: TripStatus,`
    val status: TripStatus,
    // continues the statement started above: `val badge: TripBadge,`
    val badge: TripBadge,
    // continues the statement started above: `val badgeColor: Color,`
    val badgeColor: Color,
    // continues the statement started above: `val badgeTextColor: Color,`
    val badgeTextColor: Color,
    // continues the statement started above: `val titleColor: Color,`
    val titleColor: Color,
    // continues the statement started above: `val thumbColor: Color,`
    val thumbColor: Color,
    // continues the statement started above: `val thumbAlpha: Float,`
    val thumbAlpha: Float,
    // continues the statement started above: `val photoUrl: String? = null,`
    val photoUrl: String? = null,
// closes the multi-line argument list started above
)

// expression: `data class AllTripsUiState(`
data class AllTripsUiState(
    // continues the statement started above: `val trips: List<TripRow> = emptyList(),`
    val trips: List<TripRow> = emptyList(),
    // continues the statement started above: `val filter: TripFilter = TripFilter.ALL,`
    val filter: TripFilter = TripFilter.ALL,
    // continues the statement started above: `val searchQuery: String = "",`
    val searchQuery: String = "",
    // continues the statement started above: `val isLoading: Boolean = false,`
    val isLoading: Boolean = false,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `displayed` of type `List<TripRow>`
    val displayed: List<TripRow>
        // custom getter: opens the block that computes the property value
        get() {
            // declares read-only property `byFilter`, initialised with the result of calling `when(…)` and opens a lambda / block
            val byFilter = when (filter) {
                // lambda `TripFilter.ALL -> trips`
                TripFilter.ALL      -> trips
                // lambda `TripFilter.UPCOMING -> trips.filter { it.status == T…`
                TripFilter.UPCOMING -> trips.filter { it.status == TripStatus.UPCOMING }
                // lambda `TripFilter.ONGOING -> trips.filter { it.status == T…`
                TripFilter.ONGOING  -> trips.filter { it.status == TripStatus.ONGOING }
                // lambda `TripFilter.PAST -> trips.filter { it.status == T…`
                TripFilter.PAST     -> trips.filter { it.status == TripStatus.COMPLETED }
            // closes the lambda assigned to `byFilter`
            }
            // returns `if (searchQuery.isBlank()) byFilter` from the current function
            return if (searchQuery.isBlank()) byFilter
            // opens a block after `else byFilter.filter`
            else byFilter.filter {
                // calls `contains` on `it.name` with arguments `(searchQuery, ignoreCase = true)`
                it.name.contains(searchQuery, ignoreCase = true) ||
                    // continues the statement started above: `it.destination.contains(searchQuery, ignoreCase = true)`
                    it.destination.contains(searchQuery, ignoreCase = true)
            // closes the block
            }
        // closes the getter
        }
// closes the block
}

// declares private read-only property `THUMB_COLORS`, initialised with the result of calling `listOf(…)`
private val THUMB_COLORS = listOf(
    // continues the statement started above: `WaypointPlaceAccent1,`
    WaypointPlaceAccent1,
    // continues the statement started above: `WaypointPlaceAccent2,`
    WaypointPlaceAccent2,
    // continues the statement started above: `WaypointPlaceAccent3,`
    WaypointPlaceAccent3,
    // continues the statement started above: `WaypointPlaceAccent4,`
    WaypointPlaceAccent4,
// closes the multi-line argument list started above
)

// declares function `toStatus` as an extension on `TripEntity` taking 1 parameter (`today`), returning `TripStatus` and opens its body
fun TripEntity.toStatus(today: LocalDate = LocalDate.now()): TripStatus {
    // declares read-only property `start`, initialised to `runCatching { LocalDate.parse(startDate) }.g…`
    val start = runCatching { LocalDate.parse(startDate) }.getOrNull() ?: return TripStatus.UPCOMING
    // declares read-only property `end`, initialised to `runCatching { LocalDate.parse(endDate) }.get…`
    val end   = runCatching { LocalDate.parse(endDate) }.getOrNull()   ?: return TripStatus.UPCOMING
    // returns `when {` from the current function
    return when {
        // lambda `end.isBefore(today) -> TripStatus.COMPLETED`
        end.isBefore(today)                            -> TripStatus.COMPLETED
        // lambda `!start.isAfter(today) && !end… -> TripStatus.ONGOING`
        !start.isAfter(today) && !end.isBefore(today) -> TripStatus.ONGOING
        // `else` branch of the `when`: evaluates `TripStatus.UPCOMING`
        else                                           -> TripStatus.UPCOMING
    // closes the block
    }
// closes the function `toStatus`
}

// declares function `toRow` as an extension on `TripEntity` taking 2 parameters (`index`, `today`), returning `TripRow` and opens its body
fun TripEntity.toRow(index: Int, today: LocalDate = LocalDate.now()): TripRow {
    // declares read-only property `status`, initialised with the result of calling `toStatus(…)`
    val status = toStatus(today)
    // declares read-only property `start`, initialised to `runCatching { LocalDate.parse(startDate) }.g…`
    val start  = runCatching { LocalDate.parse(startDate) }.getOrNull()
    // declares read-only property `end`, initialised to `runCatching { LocalDate.parse(endDate) }.get…`
    val end    = runCatching { LocalDate.parse(endDate) }.getOrNull()

    // declares read-only property `badge`, initialised with the result of calling `when(…)` and opens a lambda / block
    val badge = when (status) {
        // lambda with parameters `TripStatus.UPCOMING`: opens its body
        TripStatus.UPCOMING  -> {
            // declares read-only property `days`, initialised to `start?.let { today.until(it, java.time.tempo…`
            val days = start?.let { today.until(it, java.time.temporal.ChronoUnit.DAYS) } ?: 0L
            // `if` statement: executes `TripBadge.Tomorrow else TripBadge.InDays(day…` when `days <= 1L` is true
            if (days <= 1L) TripBadge.Tomorrow else TripBadge.InDays(days)
        // closes the lambda body
        }
        // lambda `TripStatus.ONGOING -> TripBadge.Ongoing`
        TripStatus.ONGOING   -> TripBadge.Ongoing
        // lambda `TripStatus.COMPLETED -> TripBadge.Completed`
        TripStatus.COMPLETED -> TripBadge.Completed
    // closes the lambda assigned to `badge`
    }

    // declares read-only property `dateStr`, initialised with the result of calling `if(…)` and opens a lambda / block
    val dateStr = if (start != null && end != null) {
        // declares read-only property `fmt`, initialised with the result of calling `java.time.format.DateTimeFormatter.ofPattern(…)`
        val fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d")
        // declares read-only property `endFmt`, initialised with the result of calling `java.time.format.DateTimeFormatter.ofPattern(…)`
        val endFmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")
        // expression: `"${start.format(fmt)} - ${end.format(endFmt)}"`
        "${start.format(fmt)} - ${end.format(endFmt)}"
    // expression: `} else "$startDate - $endDate"`
    } else "$startDate - $endDate"

    // returns `TripRow(` from the current function
    return TripRow(
        // continues the statement started above: `id = id,`
        id            = id,
        // continues the statement started above: `name = name,`
        name          = name,
        // continues the statement started above: `destination = destination ?: "",`
        destination   = destination ?: "",
        // continues the statement started above: `photoUrl = destPhotoUrl,`
        photoUrl      = destPhotoUrl,
        // continues the statement started above: `dates = dateStr,`
        dates         = dateStr,
        // continues the statement started above: `status = status,`
        status        = status,
        // continues the statement started above: `badge = badge,`
        badge         = badge,
        // continues the statement started above: `badgeColor = when (status) {`
        badgeColor    = when (status) {
            // lambda `TripStatus.UPCOMING -> WaypointTerracotta`
            TripStatus.UPCOMING  -> WaypointTerracotta
            // lambda `TripStatus.ONGOING -> WaypointTripRange`
            TripStatus.ONGOING   -> WaypointTripRange
            // lambda `TripStatus.COMPLETED -> WaypointBorderSoft`
            TripStatus.COMPLETED -> WaypointBorderSoft
        // closes the block
        },
        // continues the statement started above: `badgeTextColor = when (status) {`
        badgeTextColor = when (status) {
            // lambda `TripStatus.COMPLETED -> WaypointTextPrimary`
            TripStatus.COMPLETED -> WaypointTextPrimary
            // `else` branch of the `when`: evaluates `White`
            else                 -> White
        // closes the block
        },
        // continues the statement started above: `titleColor = if (status == TripStatus.COMPLETED) WaypointTe…`
        titleColor    = if (status == TripStatus.COMPLETED) WaypointTextMuted else WaypointTextPrimary,
        // continues the statement started above: `thumbColor = THUMB_COLORS[index % THUMB_COLORS.size],`
        thumbColor    = THUMB_COLORS[index % THUMB_COLORS.size],
        // continues the statement started above: `thumbAlpha = if (status == TripStatus.COMPLETED) 0.55f else…`
        thumbAlpha    = if (status == TripStatus.COMPLETED) 0.55f else 1f,
    // closes the multi-line argument list started above
    )
// closes the function `toRow`
}
