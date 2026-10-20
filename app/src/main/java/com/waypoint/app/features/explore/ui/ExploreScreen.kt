// declares that this file belongs to the package `com.waypoint.app.features.explore.ui`
package com.waypoint.app.features.explore.ui

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.content.Intent` for use in this file
import android.content.Intent
// imports `android.content.pm.PackageManager` for use in this file
import android.content.pm.PackageManager
// imports `android.net.Uri` for use in this file
import android.net.Uri
// imports `androidx.activity.compose.rememberLauncherForActivityResult` for use in this file
import androidx.activity.compose.rememberLauncherForActivityResult
// imports `androidx.activity.result.contract.ActivityResultContracts` for use in this file
import androidx.activity.result.contract.ActivityResultContracts
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
// imports `androidx.compose.foundation.layout.FlowRow` for use in this file
import androidx.compose.foundation.layout.FlowRow
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
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
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.core.content.ContextCompat` for use in this file
import androidx.core.content.ContextCompat
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.common.RowSurface` for use in this file
import com.waypoint.app.core.common.RowSurface
// imports `com.waypoint.app.core.common.TabHeader` for use in this file
import com.waypoint.app.core.common.TabHeader
// imports `com.waypoint.app.core.common.PlaceThumbnail` for use in this file
import com.waypoint.app.core.common.PlaceThumbnail
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.ThumbnailBlock
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
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White
// imports `com.waypoint.app.features.home.ui.CitySearchDialog` for use in this file
import com.waypoint.app.features.home.ui.CitySearchDialog

// declares private read-only property `accentCycle`, initialised with the result of calling `listOf(…)`
private val accentCycle = listOf(
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

// annotation `@OptIn` with arguments `(ExperimentalMaterial3Api::class)` applied to the declaration that follows
@OptIn(ExperimentalMaterial3Api::class)
// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun ExploreScreen(`
fun ExploreScreen(
    // continues the statement started above: `exploreViewModel: ExploreViewModel,`
    exploreViewModel: ExploreViewModel,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `onNotificationsClick: () -> Unit = {},`
    onNotificationsClick: () -> Unit = {},
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `state`, delegated to `exploreViewModel.uiState.collectAsState…`
    val state by exploreViewModel.uiState.collectAsState()
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares mutable property `showCitySearch`, delegated to `remember { mutableStateOf(false) }`
    var showCitySearch by remember { mutableStateOf(false) }

    // declares read-only property `permissionLauncher`, initialised with the result of calling `rememberLauncherForActivityResult(…)`
    val permissionLauncher = rememberLauncherForActivityResult(
        // continues the statement started above: `ActivityResultContracts.RequestMultiplePermissions()`
        ActivityResultContracts.RequestMultiplePermissions()
    // continues the statement started above: `) { perms ->`
    ) { perms ->
        // continues the statement started above: `val granted = perms[Manifest.permission.ACCESS_FINE_LOCATIO…`
        val granted = perms[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                      // continues the statement started above: `perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true`
                      perms[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        // `if` statement: executes `exploreViewModel.retry()` when `granted` is true
        if (granted) exploreViewModel.retry()
    // closes the block
    }

    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // declares read-only property `fine`, initialised with the result of calling `ContextCompat.checkSelfPermission(…)`
        val fine = ContextCompat.checkSelfPermission(
            // continues the statement started above: `context, Manifest.permission.ACCESS_FINE_LOCATION`
            context, Manifest.permission.ACCESS_FINE_LOCATION
        // continues the statement started above: `) == PackageManager.PERMISSION_GRANTED`
        ) == PackageManager.PERMISSION_GRANTED
        // declares read-only property `coarse`, initialised with the result of calling `ContextCompat.checkSelfPermission(…)`
        val coarse = ContextCompat.checkSelfPermission(
            // continues the statement started above: `context, Manifest.permission.ACCESS_COARSE_LOCATION`
            context, Manifest.permission.ACCESS_COARSE_LOCATION
        // continues the statement started above: `) == PackageManager.PERMISSION_GRANTED`
        ) == PackageManager.PERMISSION_GRANTED
        // `if` statement: the block below runs when `!fine && !coarse` is true
        if (!fine && !coarse) {
            // calls `launch` on `permissionLauncher` with an argument list that continues on the following lines
            permissionLauncher.launch(
                // continues the statement started above: `arrayOf(`
                arrayOf(
                    // continues the statement started above: `Manifest.permission.ACCESS_FINE_LOCATION,`
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    // continues the statement started above: `Manifest.permission.ACCESS_COARSE_LOCATION,`
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                // closes the multi-line argument list started above
                )
            // closes the multi-line argument list started above
            )
        // closes the if block
        }
    // closes the lambda passed to `LaunchedEffect`
    }

    // `if` statement: the block below runs when `showCitySearch` is true
    if (showCitySearch) {
        // calls `CitySearchDialog` with an argument list that continues on the following lines
        CitySearchDialog(
            // continues the statement started above: `onCitySelected = { city ->`
            onCitySelected = { city ->
                // continues the statement started above: `showCitySearch = false`
                showCitySearch = false
                // continues the statement started above: `exploreViewModel.searchCity(city)`
                exploreViewModel.searchCity(city)
            // closes the block
            },
            // continues the statement started above: `onDismiss = { showCitySearch = false },`
            onDismiss = { showCitySearch = false },
        // closes the multi-line argument list started above
        )
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
            // continues the statement started above: `isRefreshing = state.placesState is PlacesState.Loading,`
            isRefreshing = state.placesState is PlacesState.Loading,
            // continues the statement started above: `onRefresh = { exploreViewModel.retry() },`
            onRefresh = { exploreViewModel.retry() },
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.weight(1f)`
                .weight(1f)
                // continues the statement started above: `.fillMaxWidth(),`
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
            // calls `TabHeader` with arguments `(onBellClick = onNotificationsClick)`
            TabHeader(onBellClick = onNotificationsClick)

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
                    // continues the statement started above: `.clickable { showCitySearch = true }`
                    .clickable { showCitySearch = true }
                    // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 13.dp),`
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Row` with arguments `(verticalAlignment = Alignment.CenterVertical…)` and opens a trailing lambda / block
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = state.locationLabel,`
                        text = state.locationLabel,
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color = WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                        // continues the statement started above: `modifier = Modifier.weight(1f),`
                        modifier = Modifier.weight(1f),
                    // closes the multi-line argument list started above
                    )
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.explore_search_glyph),`
                        text = stringResource(R.string.explore_search_glyph),
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color = WaypointTerracotta,
                        // continues the statement started above: `fontSize = 14.sp,`
                        fontSize = 14.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    )
                // closes the lambda passed to `Row`
                }
            // closes the block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.explore_helper),`
                text = stringResource(R.string.explore_helper),
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 10.sp,`
                fontSize = 10.sp,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.padding(top = 6.dp)`
                    .padding(top = 6.dp)
                    // continues the statement started above: `.alpha(0.8f),`
                    .alpha(0.8f),
            // closes the multi-line argument list started above
            )

            // calls `FlowRow` with an argument list that continues on the following lines
            FlowRow(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 16.dp),`
                    .padding(top = 16.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `listOf` with an argument list that continues on the following lines
                listOf(
                    // continues the statement started above: `stringResource(R.string.explore_filter_all) to ExploreFilte…`
                    stringResource(R.string.explore_filter_all)           to ExploreFilter.ALL,
                    // continues the statement started above: `stringResource(R.string.explore_filter_restaurants) to Expl…`
                    stringResource(R.string.explore_filter_restaurants)   to ExploreFilter.RESTAURANTS,
                    // continues the statement started above: `stringResource(R.string.explore_filter_cafes) to ExploreFil…`
                    stringResource(R.string.explore_filter_cafes)         to ExploreFilter.CAFES,
                    // continues the statement started above: `stringResource(R.string.explore_filter_hotels) to ExploreFi…`
                    stringResource(R.string.explore_filter_hotels)        to ExploreFilter.HOTELS,
                    // continues the statement started above: `stringResource(R.string.explore_filter_parks) to ExploreFil…`
                    stringResource(R.string.explore_filter_parks)         to ExploreFilter.PARKS,
                    // continues the statement started above: `stringResource(R.string.explore_filter_pubs) to ExploreFilt…`
                    stringResource(R.string.explore_filter_pubs)          to ExploreFilter.PUBS,
                    // continues the statement started above: `stringResource(R.string.explore_filter_cinemas) to ExploreF…`
                    stringResource(R.string.explore_filter_cinemas)       to ExploreFilter.CINEMAS,
                // continues the statement started above: `).forEach { (label, filter) ->`
                ).forEach { (label, filter) ->
                    // continues the statement started above: `ExploreFilterChip(`
                    ExploreFilterChip(
                        // continues the statement started above: `label = label,`
                        label    = label,
                        // continues the statement started above: `selected = state.activeFilter == filter,`
                        selected = state.activeFilter == filter,
                        // continues the statement started above: `onClick = { exploreViewModel.setFilter(filter) },`
                        onClick  = { exploreViewModel.setFilter(filter) },
                        // continues the statement started above: `modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),`
                        modifier = Modifier.padding(end = 8.dp, bottom = 8.dp),
                    // closes the multi-line argument list started above
                    )
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
                    // continues the statement started above: `.padding(top = 8.dp),`
                    .padding(top = 8.dp),
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = "Results near ${state.locationLabel}",`
                    text = "Results near ${state.locationLabel}",
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.explore_attribution),`
                    text = stringResource(R.string.explore_attribution),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // calls `Spacer` with arguments `(modifier = Modifier.height(16.dp))`
            Spacer(modifier = Modifier.height(16.dp))

            // `when` expression on the value of `val ps = state.placesState`: the first matching branch below runs
            when (val ps = state.placesState) {
                // `when` branch: when the subject matches `PlacesState.Idle`, evaluates `Unit`
                PlacesState.Idle -> Unit

                // `when` branch `PlacesState.Loading`: opens a block
                PlacesState.Loading -> {
                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(vertical = 32.dp),`
                            .padding(vertical = 32.dp),
                        // continues the statement started above: `contentAlignment = Alignment.Center,`
                        contentAlignment = Alignment.Center,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `CircularProgressIndicator` with arguments `(color = WaypointTerracotta)`
                        CircularProgressIndicator(color = WaypointTerracotta)
                    // closes the block
                    }
                // closes the when branch
                }

                // `when` branch `is PlacesState.Success`: opens a block
                is PlacesState.Success -> {
                    // `if` statement: the block below runs when `state.visiblePlaces.isEmpty()` is true
                    if (state.visiblePlaces.isEmpty()) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.explore_no_places),`
                            text = stringResource(R.string.explore_no_places),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `modifier = Modifier.padding(vertical = 16.dp),`
                            modifier = Modifier.padding(vertical = 16.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                    } else {
                        // calls `PlacesList` with arguments `(places = state.visiblePlaces)`
                        PlacesList(places = state.visiblePlaces)
                    // closes the else branch
                    }
                // closes the when branch
                }

                // `when` branch `PlacesState.Error`: opens a block
                PlacesState.Error -> {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier`
                        modifier = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(vertical = 16.dp),`
                            .padding(vertical = 16.dp),
                        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                        horizontalAlignment = Alignment.CenterHorizontally,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.explore_load_error),`
                            text = stringResource(R.string.explore_load_error),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                        // closes the multi-line argument list started above
                        )
                        // calls `Spacer` with arguments `(modifier = Modifier.height(12.dp))`
                        Spacer(modifier = Modifier.height(12.dp))
                        // calls `Box` with an argument list that continues on the following lines
                        Box(
                            // continues the statement started above: `modifier = Modifier`
                            modifier = Modifier
                                // continues the statement started above: `.clickable { exploreViewModel.retry() }`
                                .clickable { exploreViewModel.retry() }
                                // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusBu…`
                                .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                                // continues the statement started above: `.padding(horizontal = 20.dp, vertical = 10.dp),`
                                .padding(horizontal = 20.dp, vertical = 10.dp),
                        // ends the argument list started above and opens the block that follows
                        ) {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.common_try_again),`
                                text = stringResource(R.string.common_try_again),
                                // continues the statement started above: `color = White,`
                                color = White,
                                // continues the statement started above: `fontSize = 12.sp,`
                                fontSize = 12.sp,
                                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                                fontWeight = FontWeight.Bold,
                            // closes the multi-line argument list started above
                            )
                        // closes the block
                        }
                    // closes the block
                    }
                // closes the when branch
                }
            // closes the when block
            }

            // calls `Spacer` with arguments `(modifier = Modifier.height(16.dp))`
            Spacer(modifier = Modifier.height(16.dp))
        // closes the block
        }
        // closes the block
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `PlacesList` taking 1 parameter (`places`) and opens its body
private fun PlacesList(places: List<ExplorePlace>) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // calls `Column` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
    Column(modifier = Modifier.fillMaxWidth()) {
        // expression: `places.forEachIndexed { index, place ->`
        places.forEachIndexed { index, place ->
            // continues the statement started above: `RowSurface(`
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
                    // continues the statement started above: `modifier = Modifier.padding(`
                    modifier = Modifier.padding(
                        // continues the statement started above: `start = 10.dp,`
                        start  = 10.dp,
                        // continues the statement started above: `top = 10.dp,`
                        top    = 10.dp,
                        // continues the statement started above: `end = 14.dp,`
                        end    = 14.dp,
                        // continues the statement started above: `bottom = 10.dp,`
                        bottom = 10.dp,
                    // closes the multi-line argument list started above
                    ),
                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                    verticalAlignment = Alignment.CenterVertically,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `PlaceThumbnail` with an argument list that continues on the following lines
                    PlaceThumbnail(
                        // continues the statement started above: `type = place.type,`
                        type        = place.type,
                        // continues the statement started above: `accentColor = accentCycle[index % accentCycle.size],`
                        accentColor = accentCycle[index % accentCycle.size],
                        // continues the statement started above: `label = placeTypeEmoji(place.type),`
                        label       = placeTypeEmoji(place.type),
                    // closes the multi-line argument list started above
                    )
                    // calls `Column` with arguments `(modifier = Modifier.padding(start = 12.dp))` and opens a trailing lambda / block
                    Column(modifier = Modifier.padding(start = 12.dp)) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = place.name,`
                            text       = place.name,
                            // continues the statement started above: `color = WaypointTextPrimary,`
                            color      = WaypointTextPrimary,
                            // continues the statement started above: `fontSize = 13.sp,`
                            fontSize   = 13.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = placeSubtitle(place),`
                            text     = placeSubtitle(place),
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color    = WaypointTextMuted,
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
    // closes the lambda passed to `Column`
    }
// closes the function `PlacesList`
}

// declares private function `placeSubtitle` taking 1 parameter (`place`), returning `String` and opens its body
private fun placeSubtitle(place: ExplorePlace): String {

    // declares read-only property `kind`, initialised to `place.type`
    val kind = place.type
        // chained call `.replace` on the previous result with arguments `("_", " ")`
        .replace("_", " ")
        // chained call `.replaceFirstChar` on the previous result with an inline lambda that evaluates `it.uppercase()`
        .replaceFirstChar { it.uppercase() }
        // chained call `.ifBlank` on the previous result with an inline lambda that evaluates `place.category.replaceFirstChar { it.up…`
        .ifBlank { place.category.replaceFirstChar { it.uppercase() } }
        // chained call `.ifBlank` on the previous result with an inline lambda that evaluates `"Place"`
        .ifBlank { "Place" }
    // returns `if (place.distanceMetres > 0) {` from the current function
    return if (place.distanceMetres > 0) {
        // declares read-only property `km`, initialised to `place.distanceMetres / 1000.0`
        val km = place.distanceMetres / 1000.0
        // expression: `"$kind · ${"%.1f".format(km)} km away"`
        "$kind · ${"%.1f".format(km)} km away"
    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
    } else {
        // expression: `kind`
        kind
    // closes the else branch
    }
// closes the function `placeSubtitle`
}

// declares private function `placeTypeEmoji` taking 1 parameter (`type`), returning `String`; its body is the expression `when (type) {`
private fun placeTypeEmoji(type: String): String = when (type) {
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

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun ExploreFilterChip(`
private fun ExploreFilterChip(
    // continues the statement started above: `label : String,`
    label    : String,
    // continues the statement started above: `selected : Boolean,`
    selected : Boolean,
    // continues the statement started above: `onClick : () -> Unit,`
    onClick  : () -> Unit,
    // continues the statement started above: `modifier : Modifier = Modifier,`
    modifier : Modifier = Modifier,
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
    Box(modifier = chipModifier.padding(horizontal = 13.dp, vertical = 7.dp)) {
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = label,`
            text       = label,
            // continues the statement started above: `color = if (selected) White else WaypointTextMuted,`
            color      = if (selected) White else WaypointTextMuted,
            // continues the statement started above: `fontSize = 11.sp,`
            fontSize   = 11.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the block
}
