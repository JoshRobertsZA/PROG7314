package com.example.prog7314.features.explore.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
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
import com.example.prog7314.core.cache.ExplorePlace
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.TabHeader
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

private val accentCycle = listOf(
    WaypointPlaceAccent1,
    WaypointPlaceAccent2,
    WaypointPlaceAccent3,
    WaypointPlaceAccent4,
)

@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    modifier: Modifier = Modifier,
) {
    val state by exploreViewModel.uiState.collectAsState()


    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            TabHeader()

            // City search button — tapping opens CitySearchDialog
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = state.locationLabel,
                        color = WaypointTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.weight(1f),
                    )
                    Text(
                        text = stringResource(R.string.explore_search_glyph),
                        color = WaypointTerracotta,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Text(
                text = stringResource(R.string.explore_helper),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .alpha(0.8f),
            )

            // Filter chips
            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                listOf(
                    stringResource(R.string.explore_filter_all)           to ExploreFilter.ALL,
                    stringResource(R.string.explore_filter_restaurants)   to ExploreFilter.RESTAURANTS,
                    stringResource(R.string.explore_filter_cafes)         to ExploreFilter.CAFES,
                    stringResource(R.string.explore_filter_hotels)      to ExploreFilter.HOTELS,
                    stringResource(R.string.explore_filter_parks)       to ExploreFilter.PARKS,
                    stringResource(R.string.explore_filter_pubs)        to ExploreFilter.PUBS,
                    stringResource(R.string.explore_filter_cinemas)     to ExploreFilter.CINEMAS,
                ).forEach { (label, filter) ->
                    ExploreFilterChip(
                        label    = label,
                        selected = state.activeFilter == filter,
                        onClick  = { exploreViewModel.setFilter(filter) },
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    )
                }
            }

            // Results header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "Results near ${state.locationLabel}",
                    color = WaypointTextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.explore_attribution),
                    color = WaypointTextMuted,
                    fontSize = 10.sp,
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Results body
            when (val ps = state.placesState) {
                PlacesState.Idle -> Unit

                PlacesState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 32.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = WaypointTerracotta)
                    }
                }

                is PlacesState.Success -> {
                    if (state.visiblePlaces.isEmpty()) {
                        Text(
                            text = "No places found for this filter.",
                            color = WaypointTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    } else {
                        PlacesList(places = state.visiblePlaces)
                    }
                }

                PlacesState.Error -> {
                    Text(
                        text = "Could not load places. Check your connection and try again.",
                        color = WaypointTextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(vertical = 16.dp),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun PlacesList(places: List<ExplorePlace>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        places.forEachIndexed { index, place ->
            RowSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (index == 0) 0.dp else 8.dp),
            ) {
                Row(
                    modifier = Modifier.padding(
                        start  = 10.dp,
                        top    = 10.dp,
                        end    = 14.dp,
                        bottom = 10.dp,
                    ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ThumbnailBlock(accentColor = accentCycle[index % accentCycle.size])
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        Text(
                            text       = place.name,
                            color      = WaypointTextPrimary,
                            fontSize   = 13.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Text(
                            text     = placeSubtitle(place),
                            color    = WaypointTextMuted,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 3.dp),
                        )
                    }
                }
            }
        }
    }
}

/** Builds a readable subtitle from the LocationIQ type/category + distance. */
private fun placeSubtitle(place: ExplorePlace): String {
    val kind = place.type
        .replace("_", " ")
        .replaceFirstChar { it.uppercase() }
        .ifBlank { place.category.replaceFirstChar { it.uppercase() } }
        .ifBlank { "Place" }
    return if (place.distanceMetres > 0) {
        val km = place.distanceMetres / 1000.0
        "$kind · ${"%.1f".format(km)} km away"
    } else {
        kind
    }
}

@Composable
private fun ExploreFilterChip(
    label    : String,
    selected : Boolean,
    onClick  : () -> Unit,
    modifier : Modifier = Modifier,
) {
    val shape = RoundedCornerShape(RadiusCard)
    var chipModifier = modifier
        .clickable(onClick = onClick)
        .background(if (selected) WaypointTerracotta else WaypointCard, shape)
    if (!selected) {
        chipModifier = chipModifier.border(1.dp, WaypointBorderSoft, shape)
    }
    Box(modifier = chipModifier.padding(horizontal = 13.dp, vertical = 7.dp)) {
        Text(
            text       = label,
            color      = if (selected) White else WaypointTextMuted,
            fontSize   = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
