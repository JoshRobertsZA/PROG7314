package com.example.prog7314.features.nearbyplaces.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusCard
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointPlaceAccent1
import com.example.prog7314.core.theme.WaypointPlaceAccent2
import com.example.prog7314.core.theme.WaypointPlaceAccent3
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White

private data class NearbyPlace(val name: String, val subtitle: String, val accent: Color)

/**
 * Nearby places search screen. Minimal skeleton whose only job is to
 * display the screen and let the user go back - the town input, search
 * button, filter chips, and results list are still static/mock content,
 * not wired up yet.
 *
 * TODO: wire up the town field + search button (look up the entered town
 * via LocationIQ geocoding, then fetch nearby places), the filter chips
 * (filter the results list by type), and tapping a result row (once
 * there's a destination for place details), once the backend (LocationIQ,
 * Wikipedia REST summary API) is wired up on its own branch.
 */
@Composable
fun NearbyPlacesScreen(
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val places = listOf(
        NearbyPlace("Table Mountain", "Landmark · 2.1 km away", WaypointPlaceAccent1),
        NearbyPlace("V&A Waterfront", "Shopping · 3.4 km away", WaypointPlaceAccent2),
        NearbyPlace("Camps Bay Beach", "Beach · 4.8 km away", WaypointPlaceAccent3),
        NearbyPlace("The Test Kitchen", "Restaurant · 1.6 km away", com.example.prog7314.core.theme.WaypointPlaceAccent4),
        NearbyPlace("Truth Coffee", "Cafe · 0.9 km away", WaypointPlaceAccent2),
        NearbyPlace("Kirstenbosch Gardens", "Attraction · 6.2 km away", WaypointPlaceAccent3),
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
                    text = stringResource(R.string.nearby_places_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text = stringResource(R.string.nearby_places_title),
                color = WaypointTextPrimary,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center),
            )
        }

        // SearchRow: real EditText prefilled with a mock town, plus a
        // search button. No lookup logic behind it yet.
        var town by remember { mutableStateOf("Cape Town") }
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .background(WaypointCard, RoundedCornerShape(RadiusButton))
                    .padding(horizontal = 14.dp, vertical = 13.dp),
            ) {
                BasicTextField(
                    value = town,
                    onValueChange = { town = it },
                    textStyle = androidx.compose.ui.text.TextStyle(color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
            Box(
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(48.dp)
                    .clickable(onClick = {})
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusButton)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.nearby_places_search_glyph),
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Text(
            text = stringResource(R.string.nearby_places_helper),
            color = WaypointTextMuted,
            fontSize = 10.sp,
            modifier = Modifier.padding(top = 16.dp).alpha(0.8f),
        )

        // FilterRow: "All" is the selected/active chip by default; tapping
        // the others doesn't do anything yet.
        FlowRow(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            val filters = listOf(
                stringResource(R.string.nearby_places_filter_all) to true,
                stringResource(R.string.nearby_places_filter_restaurants) to false,
                stringResource(R.string.nearby_places_filter_cafes) to false,
                stringResource(R.string.nearby_places_filter_attractions) to false,
                stringResource(R.string.nearby_places_filter_entertainment) to false,
                stringResource(R.string.nearby_places_filter_hotels) to false,
            )
            filters.forEach { (label, selected) ->
                NearbyFilterChip(label, selected, modifier = Modifier.padding(end = 8.dp, bottom = 8.dp))
            }
        }

        // ResultsHeader
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            Text(
                text = "Results near Cape Town",
                color = WaypointTextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.weight(1f),
            )
            Text(stringResource(R.string.nearby_places_attribution), color = WaypointTextMuted, fontSize = 10.sp)
        }

        // ResultsList: mock places cycling through the shared thumbnail
        // accent colors, no real place imagery from these APIs.
        Column(modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
            places.forEachIndexed { index, place ->
                RowSurface(modifier = Modifier.fillMaxWidth().padding(top = if (index == 0) 0.dp else 8.dp)) {
                    Row(
                        modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 14.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        ThumbnailBlock(accentColor = place.accent)
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(place.name, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            Text(place.subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun NearbyFilterChip(label: String, selected: Boolean, modifier: Modifier = Modifier) {
    val shape = RoundedCornerShape(RadiusCard)
    var chipModifier = modifier
        .clickable(onClick = {})
        .background(if (selected) WaypointTerracotta else WaypointCard, shape)
    if (!selected) {
        chipModifier = chipModifier.border(1.dp, WaypointBorderSoft, shape)
    }
    Box(modifier = chipModifier.padding(horizontal = 13.dp, vertical = 7.dp)) {
        Text(
            text = label,
            color = if (selected) White else WaypointTextMuted,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}
