package com.waypoint.app.features.alltrips.ui

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.common.AppButtonFilled
import com.waypoint.app.core.common.CardSurface
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.common.StatusBadge
import com.waypoint.app.core.common.ThumbnailBlock
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import coil.compose.AsyncImage
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusCard
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllTripsScreen(
    onBackClick: () -> Unit,
    onNewTripClick: () -> Unit = {},
    onTripClick: (tripId: String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AllTripsViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    // Reload every time this screen enters composition (e.g. returning after saving a new trip)
    LaunchedEffect(Unit) { viewModel.loadTrips() }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.statusBars),
    ) {
        PullToRefreshBox(
            isRefreshing = uiState.isLoading,
            onRefresh = { viewModel.loadTrips() },
            modifier = Modifier.fillMaxSize(),
        ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 96.dp)
                .verticalScroll(rememberScrollState()),
        ) {
            // TopBar
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

            // SearchBox
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            ) {
                if (uiState.searchQuery.isEmpty()) {
                    Text(stringResource(R.string.all_trips_search_hint), color = WaypointTextMuted, fontSize = 12.sp)
                }
                BasicTextField(
                    value = uiState.searchQuery,
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    textStyle = androidx.compose.ui.text.TextStyle(color = WaypointTextPrimary, fontSize = 12.sp),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            // Filter chips
            Row(modifier = Modifier.padding(top = 16.dp)) {
                val filterLabels = listOf(
                    TripFilter.ALL      to stringResource(R.string.all_trips_filter_all),
                    TripFilter.UPCOMING to stringResource(R.string.all_trips_filter_upcoming),
                    TripFilter.ONGOING  to stringResource(R.string.all_trips_filter_ongoing),
                    TripFilter.PAST     to stringResource(R.string.all_trips_filter_past),
                )
                filterLabels.forEachIndexed { index, (filter, label) ->
                    FilterChip(
                        label    = label,
                        selected = uiState.filter == filter,
                        onClick  = { viewModel.onFilterSelected(filter) },
                        modifier = Modifier.padding(start = if (index == 0) 0.dp else 8.dp),
                    )
                }
            }

            // Trip list
            if (uiState.isLoading) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = WaypointTerracotta)
                }
            } else {
                val displayed = uiState.displayed
                if (displayed.isEmpty()) {
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.all_trips_empty),
                            color = WaypointTextMuted,
                            fontSize = 13.sp,
                        )
                    }
                } else {
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp)) {
                        displayed.forEachIndexed { index, trip ->
                            CardSurface(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = if (index == 0) 0.dp else 12.dp)
                                    .clickable { onTripClick(trip.id) },
                                cornerRadius = RadiusCard,
                            ) {
                                Row(
                                    modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 14.dp, bottom = 12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    if (trip.photoUrl != null) {
                                        // Fall back to the colour block if the image fails.
                                        var failed by remember(trip.photoUrl) { mutableStateOf(false) }
                                        if (failed) {
                                            ThumbnailBlock(
                                                accentColor = trip.thumbColor,
                                                size = 56.dp,
                                                cornerRadius = RadiusButton,
                                                modifier = Modifier.alpha(trip.thumbAlpha),
                                            )
                                        } else AsyncImage(
                                            model = trip.photoUrl,
                                            contentDescription = trip.destination,
                                            contentScale = ContentScale.Crop,
                                            onError = { failed = true },
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(RadiusButton))
                                                .alpha(trip.thumbAlpha),
                                        )
                                    } else {
                                        ThumbnailBlock(
                                            accentColor = trip.thumbColor,
                                            size = 56.dp,
                                            cornerRadius = RadiusButton,
                                            modifier = Modifier.alpha(trip.thumbAlpha),
                                        )
                                    }
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
                                                text = tripBadgeLabel(trip.badge),
                                                fillColor = trip.badgeColor,
                                                textColor = trip.badgeTextColor,
                                                cornerRadius = 20.dp,
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                                            )
                                        }
                                        if (trip.destination.isNotBlank()) {
                                            Text(trip.destination, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                        }
                                        Text(trip.dates, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }

        }
        }

        // Sticky button, always visible at the bottom
        AppButtonFilled(
            text    = stringResource(R.string.home_new_trip),
            onClick = onNewTripClick,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(start = 22.dp, end = 22.dp, bottom = 16.dp),
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val shape = RoundedCornerShape(RadiusCard)
    var chipModifier = modifier
        .clickable(onClick = onClick)
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
