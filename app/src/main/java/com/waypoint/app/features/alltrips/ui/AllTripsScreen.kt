// declares that this file belongs to the package `com.waypoint.app.features.alltrips.ui`
package com.waypoint.app.features.alltrips.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.statusBars` for use in this file
import androidx.compose.foundation.layout.statusBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.text.BasicTextField` for use in this file
import androidx.compose.foundation.text.BasicTextField
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.ExperimentalMaterial3Api` for use in this file
import androidx.compose.material3.ExperimentalMaterial3Api
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.material3.pulltorefresh.PullToRefreshBox` for use in this file
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.AppButtonFilled` for use in this file
import com.waypoint.app.core.common.AppButtonFilled
// imports `com.waypoint.app.core.common.CardSurface` for use in this file
import com.waypoint.app.core.common.CardSurface
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.common.StatusBadge` for use in this file
import com.waypoint.app.core.common.StatusBadge
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.ThumbnailBlock
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `coil.compose.AsyncImage` for use in this file
import coil.compose.AsyncImage
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusCard` for use in this file
import com.waypoint.app.core.theme.RadiusCard
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource

// annotation `@OptIn` with arguments `(ExperimentalMaterial3Api::class)` applied to the declaration that follows
@OptIn(ExperimentalMaterial3Api::class)
// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun AllTripsScreen(`
fun AllTripsScreen(
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `onNewTripClick: () -> Unit = {},`
    onNewTripClick: () -> Unit = {},
    // continues the statement started above: `onTripClick: (tripId: String) -> Unit = {},`
    onTripClick: (tripId: String) -> Unit = {},
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: AllTripsViewModel = viewModel(),`
    viewModel: AllTripsViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `uiState`, delegated to `viewModel.uiState.collectAsState()`
    val uiState by viewModel.uiState.collectAsState()

    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.loadTrips() }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.statusBars),`
            .windowInsetsPadding(WindowInsets.statusBars),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `PullToRefreshBox` with an argument list that continues on the following lines
        PullToRefreshBox(
            // continues the statement started above: `isRefreshing = uiState.isLoading,`
            isRefreshing = uiState.isLoading,
            // continues the statement started above: `onRefresh = { viewModel.loadTrips() },`
            onRefresh = { viewModel.loadTrips() },
            // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
            modifier = Modifier.fillMaxSize(),
        // ends the argument list started above and opens the block that follows
        ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = …`
                .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 96.dp)
                // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                .verticalScroll(rememberScrollState()),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
            Box(modifier = Modifier.fillMaxWidth()) {
                // calls `CircleIconButton` with arguments `(onClick = onBackClick, modifier = Modifier.a…)` and opens a trailing lambda / block
                CircleIconButton(
                    onClick = onBackClick,
                    modifier = Modifier.align(Alignment.CenterStart),
                    size = 40.dp,
                    fillColor = WaypointTerracotta,
                    borderColor = null,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_back_arrow),
                        contentDescription = null,
                        tint = WaypointCard,
                        modifier = Modifier.size(18.dp),
                    )
                }
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.all_trips_title),`
                    text = stringResource(R.string.all_trips_title),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 17.sp,`
                    fontSize = 17.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                    modifier = Modifier.align(Alignment.Center),
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `Box`
            }

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 16.dp)`
                    .padding(top = 16.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
                    .padding(horizontal = 14.dp, vertical = 12.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // `if` statement: the block below runs when `uiState.searchQuery.isEmpty()` is true
                if (uiState.searchQuery.isEmpty()) {
                    // calls `Text` with arguments `(stringResource(R.string.all_trips_search_hin…)`
                    Text(stringResource(R.string.all_trips_search_hint), color = WaypointTextMuted, fontSize = 12.sp)
                // closes the if block
                }
                // calls `BasicTextField` with an argument list that continues on the following lines
                BasicTextField(
                    // continues the statement started above: `value = uiState.searchQuery,`
                    value = uiState.searchQuery,
                    // continues the statement started above: `onValueChange = { viewModel.onSearchQueryChanged(it) },`
                    onValueChange = { viewModel.onSearchQueryChanged(it) },
                    // continues the statement started above: `textStyle = androidx.compose.ui.text.TextStyle(color = Wayp…`
                    textStyle = androidx.compose.ui.text.TextStyle(color = WaypointTextPrimary, fontSize = 12.sp),
                    // continues the statement started above: `singleLine = true,`
                    singleLine = true,
                    // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                    modifier = Modifier.fillMaxWidth(),
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `Row` with arguments `(modifier = Modifier.padding(top = 16.dp))` and opens a trailing lambda / block
            Row(modifier = Modifier.padding(top = 16.dp)) {
                // declares read-only property `filterLabels`, initialised with the result of calling `listOf(…)`
                val filterLabels = listOf(
                    // continues the statement started above: `TripFilter.ALL to stringResource(R.string.all_trips_filter_…`
                    TripFilter.ALL      to stringResource(R.string.all_trips_filter_all),
                    // continues the statement started above: `TripFilter.UPCOMING to stringResource(R.string.all_trips_fi…`
                    TripFilter.UPCOMING to stringResource(R.string.all_trips_filter_upcoming),
                    // continues the statement started above: `TripFilter.ONGOING to stringResource(R.string.all_trips_fil…`
                    TripFilter.ONGOING  to stringResource(R.string.all_trips_filter_ongoing),
                    // continues the statement started above: `TripFilter.PAST to stringResource(R.string.all_trips_filter…`
                    TripFilter.PAST     to stringResource(R.string.all_trips_filter_past),
                // closes the multi-line argument list started above
                )
                // expression: `filterLabels.forEachIndexed { index, (filter, label) ->`
                filterLabels.forEachIndexed { index, (filter, label) ->
                    // continues the statement started above: `FilterChip(`
                    FilterChip(
                        // continues the statement started above: `label = label,`
                        label    = label,
                        // continues the statement started above: `selected = uiState.filter == filter,`
                        selected = uiState.filter == filter,
                        // continues the statement started above: `onClick = { viewModel.onFilterSelected(filter) },`
                        onClick  = { viewModel.onFilterSelected(filter) },
                        // continues the statement started above: `modifier = Modifier.padding(start = if (index == 0) 0.dp el…`
                        modifier = Modifier.padding(start = if (index == 0) 0.dp else 8.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the lambda passed to `Row`
            }

            // `if` statement: the block below runs when `uiState.isLoading` is true
            if (uiState.isLoading) {
                // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
                Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                    // calls `CircularProgressIndicator` with arguments `(color = WaypointTerracotta)`
                    CircularProgressIndicator(color = WaypointTerracotta)
                // closes the lambda passed to `Box`
                }
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // declares read-only property `displayed`, initialised to `uiState.displayed`
                val displayed = uiState.displayed
                // `if` statement: the block below runs when `displayed.isEmpty()` is true
                if (displayed.isEmpty()) {
                    // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
                    Box(modifier = Modifier.fillMaxWidth().padding(top = 40.dp), contentAlignment = Alignment.Center) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.all_trips_empty),`
                            text = stringResource(R.string.all_trips_empty),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 13.sp,`
                            fontSize = 13.sp,
                        // closes the multi-line argument list started above
                        )
                    // closes the lambda passed to `Box`
                    }
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `Column` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
                    Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 4.dp)) {
                        // expression: `displayed.forEachIndexed { index, trip ->`
                        displayed.forEachIndexed { index, trip ->
                            // continues the statement started above: `CardSurface(`
                            CardSurface(
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.fillMaxWidth()`
                                    .fillMaxWidth()
                                    // continues the statement started above: `.padding(top = if (index == 0) 0.dp else 12.dp)`
                                    .padding(top = if (index == 0) 0.dp else 12.dp)
                                    // continues the statement started above: `.clickable { onTripClick(trip.id) },`
                                    .clickable { onTripClick(trip.id) },
                                // continues the statement started above: `cornerRadius = RadiusCard,`
                                cornerRadius = RadiusCard,
                            // ends the argument list started above and opens the block that follows
                            ) {
                                // calls `Row` with an argument list that continues on the following lines
                                Row(
                                    // continues the statement started above: `modifier = Modifier.padding(start = 12.dp, top = 12.dp, end…`
                                    modifier = Modifier.padding(start = 12.dp, top = 12.dp, end = 14.dp, bottom = 12.dp),
                                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                                    verticalAlignment = Alignment.CenterVertically,
                                // ends the argument list started above and opens the block that follows
                                ) {
                                    // `if` statement: the block below runs when `trip.photoUrl != null` is true
                                    if (trip.photoUrl != null) {
                                        // declares mutable property `failed`, delegated to `remember(trip.photoUrl) { mutableStateO…`
                                        var failed by remember(trip.photoUrl) { mutableStateOf(false) }
                                        // `if` statement: the block below runs when `failed` is true
                                        if (failed) {
                                            // calls `ThumbnailBlock` with an argument list that continues on the following lines
                                            ThumbnailBlock(
                                                // continues the statement started above: `accentColor = trip.thumbColor,`
                                                accentColor = trip.thumbColor,
                                                // continues the statement started above: `size = 56.dp,`
                                                size = 56.dp,
                                                // continues the statement started above: `cornerRadius = RadiusButton,`
                                                cornerRadius = RadiusButton,
                                                // continues the statement started above: `modifier = Modifier.alpha(trip.thumbAlpha),`
                                                modifier = Modifier.alpha(trip.thumbAlpha),
                                            // closes the multi-line argument list started above
                                            )
                                        // expression: `} else AsyncImage(`
                                        } else AsyncImage(
                                            // continues the statement started above: `model = trip.photoUrl,`
                                            model = trip.photoUrl,
                                            // continues the statement started above: `contentDescription = trip.destination,`
                                            contentDescription = trip.destination,
                                            // continues the statement started above: `contentScale = ContentScale.Crop,`
                                            contentScale = ContentScale.Crop,
                                            // continues the statement started above: `onError = { failed = true },`
                                            onError = { failed = true },
                                            // continues the statement started above: `modifier = Modifier`
                                            modifier = Modifier
                                                // chained call `.size` on the previous result with arguments `(56.dp)`
                                                .size(56.dp)
                                                // chained call `.clip` on the previous result with arguments `(RoundedCornerShape(RadiusButton))`
                                                .clip(RoundedCornerShape(RadiusButton))
                                                // expression: `.alpha(trip.thumbAlpha),`
                                                .alpha(trip.thumbAlpha),
                                        // closes the multi-line argument list started above
                                        )
                                    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                                    } else {
                                        // calls `ThumbnailBlock` with an argument list that continues on the following lines
                                        ThumbnailBlock(
                                            // continues the statement started above: `accentColor = trip.thumbColor,`
                                            accentColor = trip.thumbColor,
                                            // continues the statement started above: `size = 56.dp,`
                                            size = 56.dp,
                                            // continues the statement started above: `cornerRadius = RadiusButton,`
                                            cornerRadius = RadiusButton,
                                            // continues the statement started above: `modifier = Modifier.alpha(trip.thumbAlpha),`
                                            modifier = Modifier.alpha(trip.thumbAlpha),
                                        // closes the multi-line argument list started above
                                        )
                                    // closes the else branch
                                    }
                                    // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
                                    Column(modifier = Modifier.weight(1f).padding(start = 12.dp)) {
                                        // calls `Row` with arguments `(verticalAlignment = Alignment.CenterVertical…)` and opens a trailing lambda / block
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            // calls `Text` with an argument list that continues on the following lines
                                            Text(
                                                // continues the statement started above: `text = trip.name,`
                                                text = trip.name,
                                                // continues the statement started above: `color = trip.titleColor,`
                                                color = trip.titleColor,
                                                // continues the statement started above: `fontSize = 14.sp,`
                                                fontSize = 14.sp,
                                                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                                fontWeight = FontWeight.Bold,
                                                // continues the statement started above: `modifier = Modifier.weight(1f),`
                                                modifier = Modifier.weight(1f),
                                            // closes the multi-line argument list started above
                                            )
                                            // calls `StatusBadge` with an argument list that continues on the following lines
                                            StatusBadge(
                                                // continues the statement started above: `text = tripBadgeLabel(trip.badge),`
                                                text = tripBadgeLabel(trip.badge),
                                                // continues the statement started above: `fillColor = trip.badgeColor,`
                                                fillColor = trip.badgeColor,
                                                // continues the statement started above: `textColor = trip.badgeTextColor,`
                                                textColor = trip.badgeTextColor,
                                                // continues the statement started above: `cornerRadius = 20.dp,`
                                                cornerRadius = 20.dp,
                                                // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 4.dp),
                                            // closes the multi-line argument list started above
                                            )
                                        // closes the lambda passed to `Row`
                                        }
                                        // `if` statement: the block below runs when `trip.destination.isNotBlank()` is true
                                        if (trip.destination.isNotBlank()) {
                                            // calls `Text` with arguments `(trip.destination, color = WaypointTextMuted,…)`
                                            Text(trip.destination, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                        // closes the if block
                                        }
                                        // calls `Text` with arguments `(trip.dates, color = WaypointTextMuted, fontS…)`
                                        Text(trip.dates, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 4.dp))
                                    // closes the lambda passed to `Column`
                                    }
                                // closes the block
                                }
                            // closes the block
                            }
                        // closes the block
                        }
                    // closes the lambda passed to `Column`
                    }
                // closes the else branch
                }
            // closes the else branch
            }

        // closes the block
        }
        // closes the block
        }

        // calls `AppButtonFilled` with an argument list that continues on the following lines
        AppButtonFilled(
            // continues the statement started above: `text = stringResource(R.string.home_new_trip),`
            text    = stringResource(R.string.home_new_trip),
            // continues the statement started above: `onClick = onNewTripClick,`
            onClick = onNewTripClick,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.align(Alignment.BottomCenter)`
                .align(Alignment.BottomCenter)
                // continues the statement started above: `.padding(start = 22.dp, end = 22.dp, bottom = 16.dp),`
                .padding(start = 22.dp, end = 22.dp, bottom = 16.dp),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun FilterChip(`
private fun FilterChip(
    // continues the statement started above: `label: String,`
    label: String,
    // continues the statement started above: `selected: Boolean,`
    selected: Boolean,
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `shape`, initialised with the result of calling `RoundedCornerShape(…)`
    val shape = RoundedCornerShape(RadiusCard)
    // declares mutable property `chipModifier`, initialised to `modifier`
    var chipModifier = modifier
        // chained call `.clickable` on the previous result with arguments `(onClick = onClick)`
        .clickable(onClick = onClick)
        // chained call `.background` on the previous result with arguments `(if (selected) WaypointTerracotta else W…)`
        .background(if (selected) WaypointTerracotta else WaypointCard, shape)
    // `if` statement: the block below runs when `!selected` is true
    if (!selected) {
        // assigns `chipModifier` the value `chipModifier.border(1.dp, WaypointBorderSoft, sha…`
        chipModifier = chipModifier.border(1.dp, WaypointBorderSoft, shape)
    // closes the if block
    }
    // calls `Box` with arguments `(modifier = chipModifier.padding(horizontal =…)` and opens a trailing lambda / block
    Box(modifier = chipModifier.padding(horizontal = 14.dp, vertical = 7.dp)) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = label,`
            text = label,
            // continues the statement started above: `color = if (selected) White else WaypointTextMuted,`
            color = if (selected) White else WaypointTextMuted,
            // continues the statement started above: `fontSize = 11.sp,`
            fontSize = 11.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the block
}
