package com.waypoint.app.features.alltrips.ui

import androidx.compose.ui.graphics.Color
import com.waypoint.app.core.db.TripEntity
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointPlaceAccent4
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.WaypointTripRange
import com.waypoint.app.core.theme.White
import java.time.LocalDate

enum class TripFilter { ALL, UPCOMING, ONGOING, PAST }

enum class TripStatus { ONGOING, UPCOMING, COMPLETED }

/** Badge content, resolved to a localized string by [tripBadgeLabel]. */
sealed class TripBadge {
    object Tomorrow : TripBadge()
    data class InDays(val days: Long) : TripBadge()
    object Ongoing : TripBadge()
    object Completed : TripBadge()
}

@androidx.compose.runtime.Composable
fun tripBadgeLabel(badge: TripBadge): String = when (badge) {
    TripBadge.Tomorrow      -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_tomorrow)
    is TripBadge.InDays     -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_in_days, badge.days)
    TripBadge.Ongoing       -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_ongoing)
    TripBadge.Completed     -> androidx.compose.ui.res.stringResource(com.waypoint.app.R.string.trip_badge_completed)
}

data class TripRow(
    val id: String,
    val name: String,
    val destination: String,
    val dates: String,
    val status: TripStatus,
    val badge: TripBadge,
    val badgeColor: Color,
    val badgeTextColor: Color,
    val titleColor: Color,
    val thumbColor: Color,
    val thumbAlpha: Float,
    val photoUrl: String? = null,
)

data class AllTripsUiState(
    val trips: List<TripRow> = emptyList(),
    val filter: TripFilter = TripFilter.ALL,
    val searchQuery: String = "",
    val isLoading: Boolean = false,
) {
    val displayed: List<TripRow>
        get() {
            val byFilter = when (filter) {
                TripFilter.ALL      -> trips
                TripFilter.UPCOMING -> trips.filter { it.status == TripStatus.UPCOMING }
                TripFilter.ONGOING  -> trips.filter { it.status == TripStatus.ONGOING }
                TripFilter.PAST     -> trips.filter { it.status == TripStatus.COMPLETED }
            }
            return if (searchQuery.isBlank()) byFilter
            else byFilter.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                    it.destination.contains(searchQuery, ignoreCase = true)
            }
        }
}

private val THUMB_COLORS = listOf(
    WaypointPlaceAccent1,
    WaypointPlaceAccent2,
    WaypointPlaceAccent3,
    WaypointPlaceAccent4,
)

fun TripEntity.toStatus(today: LocalDate = LocalDate.now()): TripStatus {
    val start = runCatching { LocalDate.parse(startDate) }.getOrNull() ?: return TripStatus.UPCOMING
    val end   = runCatching { LocalDate.parse(endDate) }.getOrNull()   ?: return TripStatus.UPCOMING
    return when {
        end.isBefore(today)                            -> TripStatus.COMPLETED
        !start.isAfter(today) && !end.isBefore(today) -> TripStatus.ONGOING
        else                                           -> TripStatus.UPCOMING
    }
}

fun TripEntity.toRow(index: Int, today: LocalDate = LocalDate.now()): TripRow {
    val status = toStatus(today)
    val start  = runCatching { LocalDate.parse(startDate) }.getOrNull()
    val end    = runCatching { LocalDate.parse(endDate) }.getOrNull()

    val badge = when (status) {
        TripStatus.UPCOMING  -> {
            val days = start?.let { today.until(it, java.time.temporal.ChronoUnit.DAYS) } ?: 0L
            if (days <= 1L) TripBadge.Tomorrow else TripBadge.InDays(days)
        }
        TripStatus.ONGOING   -> TripBadge.Ongoing
        TripStatus.COMPLETED -> TripBadge.Completed
    }

    val dateStr = if (start != null && end != null) {
        val fmt = java.time.format.DateTimeFormatter.ofPattern("MMM d")
        val endFmt = java.time.format.DateTimeFormatter.ofPattern("MMM d, yyyy")
        "${start.format(fmt)} - ${end.format(endFmt)}"
    } else "$startDate - $endDate"

    return TripRow(
        id            = id,
        name          = name,
        destination   = destination ?: "",
        photoUrl      = destPhotoUrl,
        dates         = dateStr,
        status        = status,
        badge         = badge,
        badgeColor    = when (status) {
            TripStatus.UPCOMING  -> WaypointTerracotta
            TripStatus.ONGOING   -> WaypointTripRange
            TripStatus.COMPLETED -> WaypointBorderSoft
        },
        badgeTextColor = when (status) {
            TripStatus.COMPLETED -> WaypointTextPrimary
            else                 -> White
        },
        titleColor    = if (status == TripStatus.COMPLETED) WaypointTextMuted else WaypointTextPrimary,
        thumbColor    = THUMB_COLORS[index % THUMB_COLORS.size],
        thumbAlpha    = if (status == TripStatus.COMPLETED) 0.55f else 1f,
    )
}
