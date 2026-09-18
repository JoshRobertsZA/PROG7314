package com.waypoint.app.features.home.ui

import android.Manifest
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.common.AppButtonFilled
import com.waypoint.app.core.common.AppButtonOutline
import com.waypoint.app.core.common.OfflineHeaderIndicator
import com.waypoint.app.core.common.RowSurface
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.connectivity.rememberIsOnline
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusHero
import com.waypoint.app.core.theme.RadiusRow
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointDecoText
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.WaypointTripBadgeText
import com.waypoint.app.core.theme.WaypointTripLabel
import com.waypoint.app.core.theme.White
import com.waypoint.app.features.currencyexchange.ui.CurrencyExchangeModal

/**
 * Home screen. Wired up to [HomeViewModel] for live weather + currency data.
 * Weather widget: tap -> city search (Wikipedia OpenSearch) -> OpenWeatherMap fetch.
 * Currency widget: tap -> CurrencyExchangeModal with real ExchangeRate-API rate.
 * Location is requested on first launch and updates continuously in the background.
 */
fun placeTypeEmoji(type: String): String = when (type) {
    "restaurant" -> "🍴"
    "cafe"       -> "☕"
    "hotel"      -> "🏨"
    "pub"        -> "🍺"
    "cinema"     -> "🎬"
    "park"       -> "🌳"
    else         -> "📍"
}

@Composable
fun HomeScreen(
    onNewTripClick: () -> Unit,
    onViewAllTripsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val state by homeViewModel.uiState.collectAsState()
    val isOnline by rememberIsOnline()
    val context = LocalContext.current

    var showCitySearch by remember { mutableStateOf(false) }
    var showCurrencyModal by remember { mutableStateOf(false) }

    // Request location permission on first composition
    val locationPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        // location updates start automatically in HomeViewModel.init
    }
    LaunchedEffect(Unit) {
        locationPermissionLauncher.launch(Manifest.permission.ACCESS_COARSE_LOCATION)
    }

    // City search dialog
    if (showCitySearch) {
        CitySearchDialog(
            onCitySelected = { city ->
                homeViewModel.selectCity(city)
                showCitySearch = false
            },
            onDismiss = { showCitySearch = false },
        )
    }

    // Currency exchange modal
    if (showCurrencyModal) {
        Dialog(onDismissRequest = { showCurrencyModal = false }) {
            val currencyState = state.currency
            CurrencyExchangeModal(
                onSaveClick = { showCurrencyModal = false },
                fromCode = state.selectedFromCurrency,
                rate = if (currencyState is CurrencyState.Success) currencyState.data.rate else null,
                isLoading = currencyState is CurrencyState.Loading,
                onFromCodeChanged = { code ->
                    homeViewModel.selectFromCurrency(code)
                },
            )
        }
    }

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
            // Header: brand name + settings
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.login_brand_name),
                    color = WaypointTerracotta,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Persistent "no internet" badge (Figma "Offline Mode",
                    // node 332:424) - appears once the first-drop OfflineDialog
                    // is dismissed and connectivity is still down.
                    OfflineHeaderIndicator(
                        isOnline = isOnline,
                        iconSize = 28.dp,
                        modifier = Modifier.padding(end = 12.dp),
                    )
                    Icon(
                        painter = painterResource(R.drawable.ic_settings),
                        contentDescription = stringResource(R.string.home_settings_cd),
                        tint = WaypointTextPrimary,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable(onClick = onSettingsClick),
                    )
                }
            }

            Text(
                text = stringResource(R.string.login_tagline),
                color = WaypointTextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 22.dp),
            )

            // Upcoming trip card (mock content until trips feature is built)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusHero))
                    .padding(18.dp),
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                    ) {
                        Text(
                            text = stringResource(R.string.home_upcoming_trip_label),
                            color = WaypointTripLabel,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                        )
                        Box(
                            modifier = Modifier
                                .background(WaypointCard, RoundedCornerShape(20.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp),
                        ) {
                            Text(
                                text = "In 8 days",
                                color = WaypointTripBadgeText,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    }
                    Text(
                        text = "Cape Town Getaway",
                        color = White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 10.dp),
                    )
                    Text(
                        text = "Cape Town, South Africa · Aug 2 – Aug 9",
                        color = WaypointDecoText,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 4.dp),
                    )
                }
            }

            // Trip action buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 22.dp),
            ) {
                AppButtonOutline(
                    text = stringResource(R.string.home_view_all_trips),
                    onClick = onViewAllTripsClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                )
                AppButtonFilled(
                    text = stringResource(R.string.home_new_trip),
                    onClick = onNewTripClick,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                        .padding(start = 12.dp),
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp),
                )
            }

            // Widgets row: Weather (left) + Currency (right)
            // The entire surface of each widget is tappable.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
            ) {
                // Weather widget
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        .clickable { showCitySearch = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    when (val ws = state.weather) {
                        is WeatherState.Loading -> {
                            CircularProgressIndicator(
                                color = WaypointTerracotta,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = state.selectedCity,
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        is WeatherState.Success -> {
                            Text(
                                text = "${ws.data.tempC.toInt()}°C · ${ws.data.description}",
                                color = WaypointTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = ws.data.displayName,
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        is WeatherState.Error -> {
                            Text(
                                text = "Weather unavailable",
                                color = WaypointTextMuted,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = state.selectedCity,
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        else -> {
                            Text(
                                text = "Tap to set city",
                                color = WaypointTextMuted,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }

                // Currency widget
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .background(WaypointCard, RoundedCornerShape(RadiusButton))
                        .clickable { showCurrencyModal = true }
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    when (val cs = state.currency) {
                        is CurrencyState.Loading -> {
                            CircularProgressIndicator(
                                color = WaypointTerracotta,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(16.dp),
                            )
                            Text(
                                text = "Fetching rate...",
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        is CurrencyState.Success -> {
                            Text(
                                text = "1 ${cs.data.fromCode} = ${"%.2f".format(cs.data.rate)} ZAR",
                                color = WaypointTextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                            )
                            Text(
                                text = "Live rate",
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        is CurrencyState.Error -> {
                            Text(
                                text = "Rate unavailable",
                                color = WaypointTextMuted,
                                fontSize = 12.sp,
                            )
                            Text(
                                text = "Tap to retry",
                                color = WaypointTextMuted,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(top = 2.dp),
                            )
                        }
                        else -> {
                            Text(
                                text = "1 USD = – ZAR",
                                color = WaypointTextMuted,
                                fontSize = 12.sp,
                            )
                        }
                    }
                }
            }

            // Nearby places header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Text(
                    text = stringResource(R.string.home_nearby_places_title),
                    color = WaypointTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = stringResource(R.string.home_nearby_source),
                    color = WaypointTextMuted,
                    fontSize = 10.sp,
                )
            }

            // Nearby places list — live from LocationIQ via HomeViewModel
            val nearbyAccents = listOf(WaypointPlaceAccent1, WaypointPlaceAccent2, WaypointPlaceAccent3)
            Column(modifier = Modifier.padding(top = 10.dp)) {
                when (val ns = state.nearbyPlaces) {
                    is NearbyState.Success -> {
                        ns.places.forEachIndexed { index, place ->
                            val accent = nearbyAccents[index % nearbyAccents.size]
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
                                    modifier = Modifier.padding(start = 10.dp, top = 8.dp, end = 14.dp, bottom = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                ) {
                                    ThumbnailBlock(accentColor = accent, cornerRadius = RadiusThumbnail, label = placeTypeEmoji(place.type))
                                    Column(modifier = Modifier.padding(start = 12.dp)) {
                                        Text(
                                            text = place.name,
                                            color = WaypointTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                        )
                                        Text(
                                            text = "${place.type.replaceFirstChar { it.uppercase() }} · ${"%.1f".format(place.distanceMetres / 1000.0)} km away",
                                            color = WaypointTextMuted,
                                            fontSize = 11.sp,
                                            modifier = Modifier.padding(top = 3.dp),
                                        )
                                    }
                                }
                            }
                        }
                    }
                    is NearbyState.Loading -> {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            CircularProgressIndicator(
                                color = WaypointTerracotta,
                                strokeWidth = 2.dp,
                                modifier = Modifier.size(18.dp),
                            )
                            Text(
                                text = "Finding places near you...",
                                color = WaypointTextMuted,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 10.dp),
                            )
                        }
                    }
                    is NearbyState.Error -> {
                        Text(
                            text = "Could not load nearby places. Check your connection.",
                            color = WaypointTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                        )
                    }
                    else -> {
                        Text(
                            text = "Allow location access to see places near you.",
                            color = WaypointTextMuted,
                            fontSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 14.dp),
                        )
                    }
                }
            }

        }
    }
}
