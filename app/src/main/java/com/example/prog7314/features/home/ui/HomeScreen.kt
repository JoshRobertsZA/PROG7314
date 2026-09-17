package com.example.prog7314.features.home.ui

import android.Manifest
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.AppButtonOutline
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusHero
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.RadiusThumbnail
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointDecoText
import com.example.prog7314.core.theme.WaypointPlaceAccent1
import com.example.prog7314.core.theme.WaypointPlaceAccent2
import com.example.prog7314.core.theme.WaypointPlaceAccent3
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripBadgeText
import com.example.prog7314.core.theme.WaypointTripLabel
import com.example.prog7314.core.theme.White
import com.example.prog7314.features.currencyexchange.ui.CurrencyExchangeModal

/**
 * Home screen. Wired up to [HomeViewModel] for live weather + currency data.
 * Weather widget: tap -> city search (Wikipedia OpenSearch) -> OpenWeatherMap fetch.
 * Currency widget: tap -> CurrencyExchangeModal with real ExchangeRate-API rate.
 * Location is requested on first launch and updates continuously in the background.
 */
@Composable
fun HomeScreen(
    onNewTripClick: () -> Unit,
    onViewAllTripsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    modifier: Modifier = Modifier,
    homeViewModel: HomeViewModel = viewModel(),
) {
    val state by homeViewModel.uiState.collectAsState()

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
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.home_settings_cd),
                    tint = WaypointTextPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = onSettingsClick),
                )
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

            // Nearby places list (mock until LocationIQ feature branch)
            val places = listOf(
                Triple("Table Mountain", "Landmark · 2.1 km away", WaypointPlaceAccent1),
                Triple("V&A Waterfront", "Shopping · 3.4 km away", WaypointPlaceAccent2),
                Triple("Camps Bay Beach", "Beach · 4.8 km away", WaypointPlaceAccent3),
            )
            Column(modifier = Modifier.padding(top = 10.dp)) {
                places.forEachIndexed { index, (name, subtitle, accent) ->
                    RowSurface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = if (index == 0) 0.dp else 8.dp),
                    ) {
                        Row(
                            modifier = Modifier.padding(start = 10.dp, top = 8.dp, end = 14.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            ThumbnailBlock(accentColor = accent, cornerRadius = RadiusThumbnail)
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(name, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                            }
                        }
                    }
                }
            }

            // Map CTA banner
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp, bottom = 4.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow))
                    .clickable(onClick = {})
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.home_map_cta),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.home_map_cta_arrow),
                    color = WaypointTerracotta,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    }
}
