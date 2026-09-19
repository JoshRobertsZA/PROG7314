// declares that this file belongs to the package `com.waypoint.app.features.home.ui`
package com.waypoint.app.features.home.ui

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.content.Intent` for use in this file
import android.content.Intent
// imports `android.net.Uri` for use in this file
import android.net.Uri
// imports `androidx.activity.compose.rememberLauncherForActivityResult` for use in this file
import androidx.activity.compose.rememberLauncherForActivityResult
// imports `androidx.activity.result.contract.ActivityResultContracts` for use in this file
import androidx.activity.result.contract.ActivityResultContracts
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxHeight` for use in this file
import androidx.compose.foundation.layout.fillMaxHeight
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.statusBars` for use in this file
import androidx.compose.foundation.layout.statusBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
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
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.rememberCoroutineScope` for use in this file
import androidx.compose.runtime.rememberCoroutineScope
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.compose.ui.window.Dialog` for use in this file
import androidx.compose.ui.window.Dialog
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.AppButtonFilled` for use in this file
import com.waypoint.app.core.common.AppButtonFilled
// imports `com.waypoint.app.core.common.AppButtonOutline` for use in this file
import com.waypoint.app.core.common.AppButtonOutline
// imports `com.waypoint.app.core.common.TabHeader` for use in this file
import com.waypoint.app.core.common.TabHeader
// imports `com.waypoint.app.core.common.RowSurface` for use in this file
import com.waypoint.app.core.common.RowSurface
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.ThumbnailBlock
// imports `com.waypoint.app.core.connectivity.rememberIsOnline` for use in this file
import com.waypoint.app.core.connectivity.rememberIsOnline
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusHero` for use in this file
import com.waypoint.app.core.theme.RadiusHero
// imports `com.waypoint.app.core.theme.RadiusRow` for use in this file
import com.waypoint.app.core.theme.RadiusRow
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointDecoText` for use in this file
import com.waypoint.app.core.theme.WaypointDecoText
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent1` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent1
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent2` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent2
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent3` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent3
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.WaypointTripBadgeText` for use in this file
import com.waypoint.app.core.theme.WaypointTripBadgeText
// imports `com.waypoint.app.core.theme.WaypointTripLabel` for use in this file
import com.waypoint.app.core.theme.WaypointTripLabel
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `com.waypoint.app.features.currencyexchange.ui.CurrencyExchangeModal` for use in this file
import com.waypoint.app.features.currencyexchange.ui.CurrencyExchangeModal
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// declares function `placeTypeEmoji` taking 1 parameter (`type`), returning `String`; its body is the expression `when (type) {`
fun placeTypeEmoji(type: String): String = when (type) {
    // lambda `"restaurant" -> "🍴"`
    "restaurant" -> "🍴"
    // lambda `"cafe" -> "☕"`
    "cafe"       -> "☕"
    // lambda `"hotel" -> "🏨"`
    "hotel"      -> "🏨"
    // lambda `"pub" -> "🍺"`
    "pub"        -> "🍺"
    // lambda `"cinema" -> "🎬"`
    "cinema"     -> "🎬"
    // lambda `"park" -> "🌳"`
    "park"       -> "🌳"
    // `else` branch of the `when`: evaluates `"📍"`
    else         -> "📍"
// closes the block
}

// annotation `@OptIn` with arguments `(ExperimentalMaterial3Api::class)` applied to the declaration that follows
@OptIn(ExperimentalMaterial3Api::class)
// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun HomeScreen(`
fun HomeScreen(
    // continues the statement started above: `onNewTripClick: () -> Unit,`
    onNewTripClick: () -> Unit,
    // continues the statement started above: `onViewAllTripsClick: () -> Unit,`
    onViewAllTripsClick: () -> Unit,
    // continues the statement started above: `onSettingsClick: () -> Unit,`
    onSettingsClick: () -> Unit,
    // continues the statement started above: `onTripClick: (tripId: String) -> Unit = {},`
    onTripClick: (tripId: String) -> Unit = {},
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `homeViewModel: HomeViewModel = viewModel(),`
    homeViewModel: HomeViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `state`, delegated to `homeViewModel.uiState.collectAsState()`
    val state by homeViewModel.uiState.collectAsState()
    // declares read-only property `isOnline`, delegated to `rememberIsOnline()`
    val isOnline by rememberIsOnline()
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares read-only property `coroutineScope`, initialised with the result of calling `rememberCoroutineScope(…)`
    val coroutineScope = rememberCoroutineScope()

    // declares mutable property `showCitySearch`, delegated to `remember { mutableStateOf(false) }`
    var showCitySearch by remember { mutableStateOf(false) }
    // declares mutable property `showCurrencyModal`, delegated to `remember { mutableStateOf(false) }`
    var showCurrencyModal by remember { mutableStateOf(false) }
    // declares mutable property `isRefreshing`, delegated to `remember { mutableStateOf(false) }`
    var isRefreshing by remember { mutableStateOf(false) }

    // declares read-only property `locationPermissionLauncher`, initialised with the result of calling `rememberLauncherForActivityResult(…)`
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        // continues the statement started above: `contract = ActivityResultContracts.RequestPermission(),`
        contract = ActivityResultContracts.RequestPermission(),
    // continues the statement started above: `) { granted ->`
    ) { granted ->
    // closes the block
    }
    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // calls `launch` on `locationPermissionLauncher` with arguments `(Manifest.permission.ACCESS_COARSE_LOCATION)`
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    // closes the lambda passed to `LaunchedEffect`
    }

    // `if` statement: the block below runs when `showCitySearch` is true
    if (showCitySearch) {
        // calls `CitySearchDialog` with an argument list that continues on the following lines
        CitySearchDialog(
            // continues the statement started above: `onCitySelected = { city ->`
            onCitySelected = { city ->
                // continues the statement started above: `homeViewModel.selectCity(city)`
                homeViewModel.selectCity(city)
                // continues the statement started above: `showCitySearch = false`
                showCitySearch = false
            // closes the block
            },
            // continues the statement started above: `onDismiss = { showCitySearch = false },`
            onDismiss = { showCitySearch = false },
        // closes the multi-line argument list started above
        )
    // closes the if block
    }

    // `if` statement: the block below runs when `showCurrencyModal` is true
    if (showCurrencyModal) {
        // calls `Dialog` with arguments `(onDismissRequest = { showCurrencyModal = fal…)` and opens a trailing lambda / block
        Dialog(onDismissRequest = { showCurrencyModal = false }) {
            // declares read-only property `currencyState`, initialised to `state.currency`
            val currencyState = state.currency
            // calls `CurrencyExchangeModal` with an argument list that continues on the following lines
            CurrencyExchangeModal(
                // continues the statement started above: `onSaveClick = { showCurrencyModal = false },`
                onSaveClick = { showCurrencyModal = false },
                // continues the statement started above: `fromCode = state.selectedFromCurrency,`
                fromCode = state.selectedFromCurrency,
                // continues the statement started above: `rate = if (currencyState is CurrencyState.Success) currency…`
                rate = if (currencyState is CurrencyState.Success) currencyState.data.rate else null,
                // continues the statement started above: `isLoading = currencyState is CurrencyState.Loading,`
                isLoading = currencyState is CurrencyState.Loading,
                // continues the statement started above: `onFromCodeChanged = { code ->`
                onFromCodeChanged = { code ->
                    // continues the statement started above: `homeViewModel.selectFromCurrency(code)`
                    homeViewModel.selectFromCurrency(code)
                // closes the block
                },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Dialog`
        }
    // closes the if block
    }

    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.statusBars)`
            .windowInsetsPadding(WindowInsets.statusBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp),`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `PullToRefreshBox` with an argument list that continues on the following lines
        PullToRefreshBox(
            // continues the statement started above: `isRefreshing = isRefreshing,`
            isRefreshing = isRefreshing,
            // continues the statement started above: `onRefresh = {`
            onRefresh = {
                // opens a block after `coroutineScope.launch`
                coroutineScope.launch {
                    // assigns `isRefreshing` the value `true`
                    isRefreshing = true
                    // calls `refresh` on `homeViewModel` with arguments `()`
                    homeViewModel.refresh()
                    // assigns `isRefreshing` the value `false`
                    isRefreshing = false
                // closes the block
                }
            // closes the block
            },
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // chained call `.weight` on the previous result with arguments `(1f)`
                .weight(1f)
                // expression: `.fillMaxWidth(),`
                .fillMaxWidth(),
        // ends the argument list started above and opens the block that follows
        ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.verticalScroll(rememberScrollState()),`
                .verticalScroll(rememberScrollState()),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `TabHeader` with arguments `(onAvatarClick = onSettingsClick)`
            TabHeader(onAvatarClick = onSettingsClick)

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.brand_tagline),`
                text = stringResource(R.string.brand_tagline),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 14.sp,`
                fontSize = 14.sp,
                // continues the statement started above: `modifier = Modifier.padding(top = 22.dp),`
                modifier = Modifier.padding(top = 22.dp),
            // closes the multi-line argument list started above
            )

            // declares read-only property `featuredTrip`, initialised to `state.upcomingTrip`
            val featuredTrip = state.upcomingTrip
            // `if` statement: the block below runs when `featuredTrip != null` is true
            if (featuredTrip != null) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.padding(top = 22.dp)`
                        .padding(top = 22.dp)
                        // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusHe…`
                        .background(WaypointTerracotta, RoundedCornerShape(RadiusHero))
                        // continues the statement started above: `.clickable { onTripClick(featuredTrip.id) }`
                        .clickable { onTripClick(featuredTrip.id) }
                        // continues the statement started above: `.padding(18.dp),`
                        .padding(18.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // opens a block after `Column`
                    Column {
                        // calls `Row` with an argument list that continues on the following lines
                        Row(
                            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                            modifier = Modifier.fillMaxWidth(),
                            // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
                            horizontalArrangement = Arrangement.SpaceBetween,
                        // ends the argument list started above and opens the block that follows
                        ) {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(`
                                text = stringResource(
                                    // continues the statement started above: `if (featuredTrip.status == com.waypoint.app.features.alltri…`
                                    if (featuredTrip.status == com.waypoint.app.features.alltrips.ui.TripStatus.ONGOING)
                                        // continues the statement started above: `R.string.home_ongoing_trip_label`
                                        R.string.home_ongoing_trip_label
                                    // continues the statement started above: `else`
                                    else
                                        // continues the statement started above: `R.string.home_upcoming_trip_label`
                                        R.string.home_upcoming_trip_label
                                // closes the multi-line argument list started above
                                ),
                                // continues the statement started above: `color = WaypointTripLabel,`
                                color = WaypointTripLabel,
                                // continues the statement started above: `fontSize = 11.sp,`
                                fontSize = 11.sp,
                                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                fontWeight = FontWeight.Bold,
                            // closes the multi-line argument list started above
                            )
                            // calls `Box` with an argument list that continues on the following lines
                            Box(
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(20.dp))`
                                    .background(WaypointCard, RoundedCornerShape(20.dp))
                                    // continues the statement started above: `.padding(horizontal = 10.dp, vertical = 4.dp),`
                                    .padding(horizontal = 10.dp, vertical = 4.dp),
                            // ends the argument list started above and opens the block that follows
                            ) {
                                // calls `Text` with an argument list that continues on the following lines
                                Text(
                                    // continues the statement started above: `text = com.waypoint.app.features.alltrips.ui.tripBadgeLabel…`
                                    text = com.waypoint.app.features.alltrips.ui.tripBadgeLabel(featuredTrip.badge),
                                    // continues the statement started above: `color = WaypointTripBadgeText,`
                                    color = WaypointTripBadgeText,
                                    // continues the statement started above: `fontSize = 10.sp,`
                                    fontSize = 10.sp,
                                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                    fontWeight = FontWeight.Bold,
                                // closes the multi-line argument list started above
                                )
                            // closes the block
                            }
                        // closes the block
                        }
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = featuredTrip.name,`
                            text = featuredTrip.name,
                            // continues the statement started above: `color = White,`
                            color = White,
                            // continues the statement started above: `fontSize = 20.sp,`
                            fontSize = 20.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `modifier = Modifier.padding(top = 10.dp),`
                            modifier = Modifier.padding(top = 10.dp),
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = "${featuredTrip.destination} · ${featuredTrip.dates}…`
                            text = "${featuredTrip.destination} · ${featuredTrip.dates}",
                            // continues the statement started above: `color = WaypointDecoText,`
                            color = WaypointDecoText,
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
                            modifier = Modifier.padding(top = 4.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the block
                }
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.padding(top = 22.dp)`
                        .padding(top = 22.dp)
                        // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusHero))`
                        .background(WaypointCard, RoundedCornerShape(RadiusHero))
                        // continues the statement started above: `.padding(18.dp),`
                        .padding(18.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.home_no_trips),`
                        text = stringResource(R.string.home_no_trips),
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the else branch
            }

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 22.dp),`
                    .padding(top = 22.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `AppButtonOutline` with an argument list that continues on the following lines
                AppButtonOutline(
                    // continues the statement started above: `text = stringResource(R.string.home_view_all_trips),`
                    text = stringResource(R.string.home_view_all_trips),
                    // continues the statement started above: `onClick = onViewAllTripsClick,`
                    onClick = onViewAllTripsClick,
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                    // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
                // closes the multi-line argument list started above
                )
                // calls `AppButtonFilled` with an argument list that continues on the following lines
                AppButtonFilled(
                    // continues the statement started above: `text = stringResource(R.string.home_new_trip),`
                    text = stringResource(R.string.home_new_trip),
                    // continues the statement started above: `onClick = onNewTripClick,`
                    onClick = onNewTripClick,
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.weight(1f)`
                        .weight(1f)
                        // continues the statement started above: `.fillMaxHeight()`
                        .fillMaxHeight()
                        // continues the statement started above: `.padding(start = 12.dp),`
                        .padding(start = 12.dp),
                    // continues the statement started above: `contentPadding = androidx.compose.foundation.layout.Padding…`
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(vertical = 12.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 22.dp),`
                    .padding(top = 22.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Column` with an argument list that continues on the following lines
                Column(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.weight(1f)`
                        .weight(1f)
                        // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        // continues the statement started above: `.clickable { showCitySearch = true }`
                        .clickable { showCitySearch = true }
                        // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // `when` expression on the value of `val ws = state.weather`: the first matching branch below runs
                    when (val ws = state.weather) {
                        // `when` branch `is WeatherState.Loading`: opens a block
                        is WeatherState.Loading -> {
                            // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                            CircularProgressIndicator(
                                // continues the statement started above: `color = WaypointTerracotta,`
                                color = WaypointTerracotta,
                                // continues the statement started above: `strokeWidth = 2.dp,`
                                strokeWidth = 2.dp,
                                // continues the statement started above: `modifier = Modifier.size(16.dp),`
                                modifier = Modifier.size(16.dp),
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = state.selectedCity,`
                                text = state.selectedCity,
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `when` branch `is WeatherState.Success`: opens a block
                        is WeatherState.Success -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = "${ws.data.tempC.toInt()}°C · ${ws.data.description}…`
                                text = "${ws.data.tempC.toInt()}°C · ${ws.data.description}",
                                // continues the statement started above: `color = WaypointTextPrimary,`
                                color = WaypointTextPrimary,
                                // continues the statement started above: `fontSize = 13.sp,`
                                fontSize = 13.sp,
                                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                fontWeight = FontWeight.Bold,
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = ws.data.displayName,`
                                text = ws.data.displayName,
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `when` branch `is WeatherState.Error`: opens a block
                        is WeatherState.Error -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_weather_unavailable),`
                                text = stringResource(R.string.home_weather_unavailable),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = state.selectedCity,`
                                text = state.selectedCity,
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `else` branch of the `when`: opens a block
                        else -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_tap_set_city),`
                                text = stringResource(R.string.home_tap_set_city),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                            // closes the multi-line argument list started above
                            )
                        // closes the when else-branch
                        }
                    // closes the when block
                    }
                // closes the block
                }

                // calls `Column` with an argument list that continues on the following lines
                Column(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.weight(1f)`
                        .weight(1f)
                        // continues the statement started above: `.padding(start = 10.dp)`
                        .padding(start = 10.dp)
                        // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        // continues the statement started above: `.clickable { showCurrencyModal = true }`
                        .clickable { showCurrencyModal = true }
                        // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // `when` expression on the value of `val cs = state.currency`: the first matching branch below runs
                    when (val cs = state.currency) {
                        // `when` branch `is CurrencyState.Loading`: opens a block
                        is CurrencyState.Loading -> {
                            // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                            CircularProgressIndicator(
                                // continues the statement started above: `color = WaypointTerracotta,`
                                color = WaypointTerracotta,
                                // continues the statement started above: `strokeWidth = 2.dp,`
                                strokeWidth = 2.dp,
                                // continues the statement started above: `modifier = Modifier.size(16.dp),`
                                modifier = Modifier.size(16.dp),
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_fetching_rate),`
                                text = stringResource(R.string.home_fetching_rate),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `when` branch `is CurrencyState.Success`: opens a block
                        is CurrencyState.Success -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = "1 ${cs.data.fromCode} = ${"%.2f".format(cs.data.rat…`
                                text = "1 ${cs.data.fromCode} = ${"%.2f".format(cs.data.rate)} ZAR",
                                // continues the statement started above: `color = WaypointTextPrimary,`
                                color = WaypointTextPrimary,
                                // continues the statement started above: `fontSize = 13.sp,`
                                fontSize = 13.sp,
                                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                fontWeight = FontWeight.Bold,
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_live_rate),`
                                text = stringResource(R.string.home_live_rate),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `when` branch `is CurrencyState.Error`: opens a block
                        is CurrencyState.Error -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_rate_unavailable),`
                                text = stringResource(R.string.home_rate_unavailable),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_tap_retry),`
                                text = stringResource(R.string.home_tap_retry),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 10.sp,`
                                fontSize = 10.sp,
                                // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                                modifier = Modifier.padding(top = 2.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the when branch
                        }
                        // `else` branch of the `when`: opens a block
                        else -> {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = "1 USD = – ZAR",`
                                text = "1 USD = – ZAR",
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                            // closes the multi-line argument list started above
                            )
                        // closes the when else-branch
                        }
                    // closes the when block
                    }
                // closes the block
                }
            // closes the block
            }

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 22.dp),`
                    .padding(top = 22.dp),
                // continues the statement started above: `horizontalArrangement = Arrangement.SpaceBetween,`
                horizontalArrangement = Arrangement.SpaceBetween,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.home_nearby_places_title),`
                    text = stringResource(R.string.home_nearby_places_title),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.home_nearby_source),`
                    text = stringResource(R.string.home_nearby_source),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // declares read-only property `nearbyAccents`, initialised with the result of calling `listOf(…)`
            val nearbyAccents = listOf(WaypointPlaceAccent1, WaypointPlaceAccent2, WaypointPlaceAccent3)
            // calls `Column` with arguments `(modifier = Modifier.padding(top = 10.dp))` and opens a trailing lambda / block
            Column(modifier = Modifier.padding(top = 10.dp)) {
                // `when` expression on the value of `val ns = state.nearbyPlaces`: the first matching branch below runs
                when (val ns = state.nearbyPlaces) {
                    // `when` branch `is NearbyState.Success`: opens a block
                    is NearbyState.Success -> {
                        // expression: `ns.places.forEachIndexed { index, place ->`
                        ns.places.forEachIndexed { index, place ->
                            // continues the statement started above: `val accent = nearbyAccents[index % nearbyAccents.size]`
                            val accent = nearbyAccents[index % nearbyAccents.size]
                            // calls `RowSurface` with an argument list that continues on the following lines
                            RowSurface(
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.fillMaxWidth()`
                                    .fillMaxWidth()
                                    // continues the statement started above: `.padding(top = if (index == 0) 0.dp else 8.dp)`
                                    .padding(top = if (index == 0) 0.dp else 8.dp)
                                    // continues the statement started above: `.clickable {`
                                    .clickable {
                                        // declares read-only property `uri`, initialised with the result of calling `Uri.parse(…)`
                                        val uri = Uri.parse(
                                            // continues the statement started above: `"https://www.google.com/maps/dir/?api=1" +`
                                            "https://www.google.com/maps/dir/?api=1" +
                                            // continues the statement started above: `"&destination=${place.lat},${place.lon}" +`
                                            "&destination=${place.lat},${place.lon}" +
                                            // continues the statement started above: `"&destination_place_id=${Uri.encode(place.name)}"`
                                            "&destination_place_id=${Uri.encode(place.name)}"
                                        // closes the multi-line argument list started above
                                        )
                                        // calls `startActivity` on `context` with arguments `(Intent(Intent.ACTION_VIEW, uri))`
                                        context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                                    // closes the block
                                    },
                            // ends the argument list started above and opens the block that follows
                            ) {
                                // calls `Row` with an argument list that continues on the following lines
                                Row(
                                    // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 8.dp, end …`
                                    modifier = Modifier.padding(start = 10.dp, top = 8.dp, end = 14.dp, bottom = 8.dp),
                                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                                    verticalAlignment = Alignment.CenterVertically,
                                // ends the argument list started above and opens the block that follows
                                ) {
                                    // calls `ThumbnailBlock` with arguments `(accentColor = accent, cornerRadius = RadiusT…)`
                                    ThumbnailBlock(accentColor = accent, cornerRadius = RadiusThumbnail, label = placeTypeEmoji(place.type))
                                    // calls `Column` with arguments `(modifier = Modifier.padding(start = 12.dp))` and opens a trailing lambda / block
                                    Column(modifier = Modifier.padding(start = 12.dp)) {
                                        // calls `Text` with an argument list that continues on the following lines
                                        Text(
                                            // continues the statement started above: `text = place.name,`
                                            text = place.name,
                                            // continues the statement started above: `color = WaypointTextPrimary,`
                                            color = WaypointTextPrimary,
                                            // continues the statement started above: `fontSize = 13.sp,`
                                            fontSize = 13.sp,
                                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                            fontWeight = FontWeight.Bold,
                                        // closes the multi-line argument list started above
                                        )
                                        // calls `Text` with an argument list that continues on the following lines
                                        Text(
                                            // continues the statement started above: `text = "${place.type.replaceFirstChar { it.uppercase() }} ·…`
                                            text = "${place.type.replaceFirstChar { it.uppercase() }} · ${"%.1f".format(place.distanceMetres / 1000.0)} km away",
                                            // continues the statement started above: `color = WaypointTextMuted,`
                                            color = WaypointTextMuted,
                                            // continues the statement started above: `fontSize = 11.sp,`
                                            fontSize = 11.sp,
                                            // continues the statement started above: `modifier = Modifier.padding(top = 3.dp),`
                                            modifier = Modifier.padding(top = 3.dp),
                                        // closes the multi-line argument list started above
                                        )
                                    // closes the lambda passed to `Column`
                                    }
                                // closes the block
                                }
                            // closes the block
                            }
                        // closes the block
                        }
                    // closes the when branch
                    }
                    // `when` branch `is NearbyState.Loading`: opens a block
                    is NearbyState.Loading -> {
                        // calls `Row` with an argument list that continues on the following lines
                        Row(
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.fillMaxWidth()`
                                .fillMaxWidth()
                                // continues the statement started above: `.padding(vertical = 14.dp),`
                                .padding(vertical = 14.dp),
                            // continues the statement started above: `horizontalArrangement = Arrangement.Center,`
                            horizontalArrangement = Arrangement.Center,
                            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                            verticalAlignment = Alignment.CenterVertically,
                        // ends the argument list started above and opens the block that follows
                        ) {
                            // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                            CircularProgressIndicator(
                                // continues the statement started above: `color = WaypointTerracotta,`
                                color = WaypointTerracotta,
                                // continues the statement started above: `strokeWidth = 2.dp,`
                                strokeWidth = 2.dp,
                                // continues the statement started above: `modifier = Modifier.size(18.dp),`
                                modifier = Modifier.size(18.dp),
                            // closes the multi-line argument list started above
                            )
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.home_finding_places),`
                                text = stringResource(R.string.home_finding_places),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                                // continues the statement started above: `modifier = Modifier.padding(start = 10.dp),`
                                modifier = Modifier.padding(start = 10.dp),
                            // closes the multi-line argument list started above
                            )
                        // closes the block
                        }
                    // closes the when branch
                    }
                    // `when` branch `is NearbyState.Error`: opens a block
                    is NearbyState.Error -> {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.home_places_error),`
                            text = stringResource(R.string.home_places_error),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.fillMaxWidth()`
                                .fillMaxWidth()
                                // continues the statement started above: `.padding(vertical = 14.dp),`
                                .padding(vertical = 14.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the when branch
                    }
                    // `else` branch of the `when`: opens a block
                    else -> {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.home_places_permission),`
                            text = stringResource(R.string.home_places_permission),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.fillMaxWidth()`
                                .fillMaxWidth()
                                // continues the statement started above: `.padding(vertical = 14.dp),`
                                .padding(vertical = 14.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the when else-branch
                    }
                // closes the when block
                }
            // closes the lambda passed to `Column`
            }

        // closes the block
        }
        // closes the block
        }
    // closes the block
    }
// closes the block
}
