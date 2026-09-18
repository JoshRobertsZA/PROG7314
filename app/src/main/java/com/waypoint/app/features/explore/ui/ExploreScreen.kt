package com.waypoint.app.features.explore.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.waypoint.app.R
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.common.RowSurface
import com.waypoint.app.core.common.TabHeader
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusCard
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointPlaceAccent4
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White
import com.waypoint.app.features.home.ui.CitySearchDialog

private val accentCycle = listOf(
    WaypointPlaceAccent1,
    WaypointPlaceAccent2,
    WaypointPlaceAccent3,
    WaypointPlaceAccent4,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExploreScreen(
    exploreViewModel: ExploreViewModel,
    modifier: Modifier = Modifier,
) {
    val state by exploreViewModel.uiState.collectAsState()
    val context = LocalContext.current
    var showCitySearch by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                      perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        if (granted) exploreViewModel.retry()
    }

    LaunchedEffect(Unit) {
        val fine = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val coarse = ContextCompat.checkSelfPermission(
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        if (!fine && !coarse) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                )
            )
        }
    }

    if (showCitySearch) {
        CitySearchDialog(
            onCitySelected = { city ->
                showCitySearch = false
                exploreViewModel.searchCity(city)
            },
            onDismiss = { showCitySearch = false },
        )
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.statusBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp),
    ) {
        PullToRefreshBox(
            isRefreshing = state.placesState is PlacesState.Loading,
            onRefresh = { exploreViewModel.retry() },
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
        ) {
            TabHeader()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .clickable { showCitySearch = true }
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

            FlowRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
            ) {
                listOf(
                    stringResource(R.string.explore_filter_all)           to ExploreFilter.ALL,
                    stringResource(R.string.explore_filter_restaurants)   to ExploreFilter.RESTAURANTS,
                    stringResource(R.string.explore_filter_cafes)         to ExploreFilter.CAFES,
                    stringResource(R.string.explore_filter_hotels)        to ExploreFilter.HOTELS,
                    stringResource(R.string.explore_filter_parks)         to ExploreFilter.PARKS,
                    stringResource(R.string.explore_filter_pubs)          to ExploreFilter.PUBS,
                    stringResource(R.string.explore_filter_cinemas)       to ExploreFilter.CINEMAS,
                ).forEach { (label, filter) ->
                    ExploreFilterChip(
                        label    = label,
                        selected = state.activeFilter == filter,
                        onClick  = { exploreViewModel.setFilter(filter) },
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    )
                }
            }

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
                            text = stringResource(R.string.explore_no_places),
                            color = WaypointTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(vertical = 16.dp),
                        )
                    } else {
                        PlacesList(places = state.visiblePlaces)
                    }
                }

                PlacesState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.explore_load_error),
                            color = WaypointTextMuted,
                            fontSize = 12.sp,
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .clickable { exploreViewModel.retry() }
                                .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                        ) {
                            Text(
                                text = stringResource(R.string.common_try_again),
                                color = White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        }
    }
}

@Composable
private fun PlacesList(places: List<ExplorePlace>) {
    val context = LocalContext.current
    Column(modifier = Modifier.fillMaxWidth()) {
        places.forEachIndexed { index, place ->
            RowSurface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = if (index == 0) 0.dp else 8.dp)
                    .clickable {
                        val uri = Uri.parse(
                            "https://www.google.com/maps/dir/?api=1" +
                            "&destination=${place.lat},${place.lon}" +
                            "&destination_place_id=${Uri.encode(place.name)}"
                        )
                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                    },
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
                    ThumbnailBlock(
                        accentColor = accentCycle[index % accentCycle.size],
                        label       = placeTypeEmoji(place.type),
                    )
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

private fun placeTypeEmoji(type: String): String = when (type) {
    "restaurant" -> "🍴"
    "cafe"       -> "☕"
    "hotel"      -> "🏨"
    "pub"        -> "🍺"
    "cinema"     -> "🎬"
    "park"       -> "🌳"
    else         -> "📍"
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
