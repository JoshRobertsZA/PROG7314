package com.example.prog7314.features.home.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.common.AppButtonOutline
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusHero
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointBorderSoft
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

/**
 * Home screen. Frontend skeleton only: static/mock content matching the
 * Waypoint Figma design (node 47:30), no networking wired up yet.
 *
 * TODO: replace mock trip/weather/currency/nearby-places content with real
 * data once the backend (LocationIQ, OpenWeatherMap, ExchangeRate-API) is
 * wired up on its own branch.
 * TODO: wire up bottomNav (navTrips/navMap/navProfile), btnSettings, and
 * the map CTA banner once those destinations exist. onNewTripClick is
 * wired below; "View all" is still unwired pending the all-trips
 * destination.
 */
@Composable
fun HomeScreen(
    onNewTripClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
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
                // TODO: not wired to a Settings screen yet
                Icon(
                    painter = painterResource(R.drawable.ic_settings),
                    contentDescription = stringResource(R.string.home_settings_cd),
                    tint = WaypointTextPrimary,
                    modifier = Modifier
                        .size(24.dp)
                        .clickable(onClick = {}),
                )
            }

            Text(
                text = stringResource(R.string.login_tagline),
                color = WaypointTextMuted,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 22.dp),
            )

            // UpcomingTrip card - trip name, dates and "In 8 days" badge are
            // mock content until the real upcoming-trip data source exists.
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

            // TripActions - neither button navigates anywhere yet except
            // New trip.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .padding(top = 22.dp),
            ) {
                AppButtonOutline(
                    text = stringResource(R.string.home_view_all_trips),
                    onClick = {},
                    modifier = Modifier.weight(1f).fillMaxHeight(),
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

            // Widgets: Weather + Currency - mock values, will be bound to
            // OpenWeatherMap / ExchangeRate-API on the backend branch.
            Row(modifier = Modifier.fillMaxWidth().padding(top = 22.dp)) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .background(WaypointCard, RoundedCornerShape(com.example.prog7314.core.theme.RadiusButton))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    Text("19°C · Windy", color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Cape Town", color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                }
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                        .background(WaypointCard, RoundedCornerShape(com.example.prog7314.core.theme.RadiusButton))
                        .padding(horizontal = 14.dp, vertical = 12.dp),
                ) {
                    Text("1 USD = 18.20 ZAR", color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Text("Live rate", color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                }
            }

            // NearbyHeaderRow
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

            // NearbyPlacesList - three hardcoded rows for layout purposes;
            // the real list will come from LocationIQ and should replace
            // these rows rather than append to them.
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
                            ThumbnailBlock(accentColor = accent, cornerRadius = com.example.prog7314.core.theme.RadiusThumbnail)
                            Column(modifier = Modifier.padding(start = 12.dp)) {
                                Text(name, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                Text(subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                            }
                        }
                    }
                }
            }

            // MapCTABanner - not wired to a Map screen yet.
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

        // NavDivider
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(WaypointBorderSoft))

        // Nav: bottom navigation bar. Only "Home" reflects the active
        // state; Trips, Map and Profile are styled inactive placeholders
        // and don't navigate anywhere - those screens don't exist yet.
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 28.dp),
        ) {
            NavItem(label = stringResource(R.string.home_nav_home), active = true, modifier = Modifier.weight(1f))
            NavItem(label = stringResource(R.string.home_nav_trips), active = false, modifier = Modifier.weight(1f))
            NavItem(label = stringResource(R.string.home_nav_map), active = false, modifier = Modifier.weight(1f))
            NavItem(label = stringResource(R.string.home_nav_profile), active = false, modifier = Modifier.weight(1f))
        }
    }
}

@Composable
private fun NavItem(label: String, active: Boolean, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.clickable(onClick = {}),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(6.dp)
                .background(if (active) WaypointTerracotta else WaypointTextMuted, CircleShape),
        )
        Text(
            text = label,
            color = if (active) WaypointTerracotta else WaypointTextMuted,
            fontSize = 10.sp,
            fontWeight = if (active) FontWeight.Bold else FontWeight.Normal,
            modifier = Modifier.padding(top = 4.dp),
        )
    }
}
