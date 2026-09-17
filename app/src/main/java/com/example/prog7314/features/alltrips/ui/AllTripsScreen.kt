package com.example.prog7314.features.alltrips.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.CardSurface
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.common.StatusBadge
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusCard
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointPlaceAccent1
import com.example.prog7314.core.theme.WaypointPlaceAccent2
import com.example.prog7314.core.theme.WaypointPlaceAccent3
import com.example.prog7314.core.theme.WaypointPlaceAccent4
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White

private data class TripCard(
    val name: String,
    val badgeText: String,
    val badgeColor: Color,
    val badgeTextColor: Color,
    val titleColor: Color,
    val destination: String,
    val dates: String,
    val thumbColor: Color,
    val thumbAlpha: Float,
)

/**
 * All trips screen. Minimal skeleton whose only job is to display the
 * screen and let the user navigate back - the search box, filter chips,
 * and every trip card are still static/mock content, not wired up yet.
 *
 * TODO: wire up the search box (live filtering as the user types), the
 * All/Upcoming/Ongoing/Past filter chips (toggle selected state + filter
 * the list), and tapping a trip card (navigate to that trip's itinerary),
 * and replace mock trip content with real data, once the backend (trip
 * storage) is wired up on its own branch.
 */
@Composable
fun AllTripsScreen(
    onBackClick: () -> Unit,
    onNewTripClick: () -> Unit = {},

    modifier: Modifier = Modifier,
) {
    val trips = listOf(
        TripCard("Cape Town Getaway", "In 8 days", WaypointTerracotta, White, WaypointTextPrimary, "Cape Town, South Africa", "Aug 2 – Aug 9, 2026", WaypointPlaceAccent4, 1f),
        TripCard("Nairobi Street Food Tour", "Ongoing", com.example.prog7314.core.theme.WaypointTripRange, White, WaypointTextPrimary, "Nairobi, Kenya", "Jul 22 – Jul 27, 2026", WaypointPlaceAccent2, 1f),
        TripCard("Zanzibar Honeymoon", "In 51 days", WaypointTerracotta, White, WaypointTextPrimary, "Zanzibar, Tanzania", "Sep 14 – Sep 21, 2026", WaypointPlaceAccent3, 1f),
        TripCard("Lisbon Long Weekend", "Completed", WaypointBorderSoft, WaypointTextPrimary, WaypointTextMuted, "Lisbon, Portugal", "Jun 10 – Jun 13, 2026", WaypointPlaceAccent1, 0.55f),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // TopBar: back button, centered title
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.all_trips_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = stringResource(R.string.all_trips_title),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // SearchBox: real, usable text field, even though there's no
        // filtering logic behind it yet.
        var searchText by remember { mutableStateOf("") }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .background(WaypointCard, RoundedCornerShape(RadiusButton))
                .padding(horizontal = 14.dp, vertical = 12.dp),
        ) {
            if (searchText.isEmpty()) {
                Text(stringResource(R.string.all_trips_search_hint), color = WaypointTextMuted, fontSize = 12.sp)
            }
            BasicTextField(
                value = searchText,
                onValueChange = { searchText = it },
                textStyle = androidx.compose.ui.text.TextStyle(color = WaypointTextPrimary, fontSize = 12.sp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
        }

        // FilterRow: "All" is the selected/active chip by default
        Row(modifier = Modifier.padding(top = 16.dp)) {
            val filters = listOf(
                stringResource(R.string.all_trips_filter_all) to true,
                stringResource(R.string.all_trips_filter_upcoming) to false,
                stringResource(R.string.all_trips_filter_ongoing) to false,
                stringResource(R.string.all_trips_filter_past) to false,
            )
            filters.forEachIndexed { index, (label, selected) ->
                FilterChip(label = label, selected = selected, modifier = Modifier.padding(start = if (index == 0) 0.dp else 8.dp))
            }
        }

        // TripList
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp)) {
            trips.forEachIndexed { index, trip ->
                CardSurface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = if (index == 0) 0.dp else 12.dp)
                        .clickable(onClick = {}),
                    cornerRadius = RadiusCard,
                ) {
                    Row(
                        modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 14.dp, bottom = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ThumbnailBlock(
                            accentColor = trip.thumbColor,
                            size = 56.dp,
                            cornerRadius = RadiusButton,
                            modifier = Modifier.alpha(trip.thumbAlpha),
                        )
                        Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = trip.name,
                                    color = trip.titleColor,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.weight(1f),
                                )
                                StatusBadge(
                                    text = trip.badgeText,
                                    fillColor = trip.badgeColor,
                                    textColor = trip.badgeTextColor,
                                    cornerRadius = 20.dp,
                                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                                )
                            }
                            Text(trip.destination, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                            Text(trip.dates, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                        }
                    }
                }
            }
        }

        // New trip button
        AppButtonFilled(
            text     = stringResource(R.string.home_new_trip),
            onClick  = onNewTripClick,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
        )
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(RadiusCard)
    var chipModifier = modifier
        .clickable(onClick = {})
        .background(if (selected) WaypointTerracotta else WaypointCard, shape)
    if (!selected) {
        chipModifier = chipModifier.border(1.dp, WaypointBorderSoft, shape)
    }
    Box(modifier = chipModifier.padding(horizontal = 14.dp, vertical = 7.dp)) {
        Text(
            text = label,
            color = if (selected) White else WaypointTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
