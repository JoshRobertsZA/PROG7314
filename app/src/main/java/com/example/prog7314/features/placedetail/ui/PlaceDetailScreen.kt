package com.example.prog7314.features.placedetail.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointCard
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.White

/**
 * Place Detail screen (Figma node 380:12). The tap-through target from
 * the "Nearby Places" list (Figma node 408:12), which doesn't exist yet -
 * reached only from the debug scratch hub for now (see MainScreen.kt).
 * Frontend skeleton only: static/mock content for a single hardcoded
 * place ("Table Mountain"), no networking wired up yet.
 *
 * TODO: replace the mock place name/category/weather/hours/description
 * with real LocationIQ/OpenWeatherMap/Wikipedia data once the backend is
 * wired up, and wire "Open in Maps" (deep link to a maps app) and
 * "+ Add to itinerary" (attach this place to a trip) once those flows
 * exist. Wire this screen into the real Nearby Places list once that
 * screen (node 408:12) is built.
 */
@Composable
fun PlaceDetailScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize().background(WaypointCream)) {
        Image(
            painter = painterResource(R.drawable.img_place_detail_hero),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .align(Alignment.TopStart),
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp)
                .verticalScroll(rememberScrollState())
                .background(WaypointCream, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = 28.dp),
        ) {
            // Mock content for now - see the TODO above.
            Text("Table Mountain", color = WaypointTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            Text(
                text = "Landmark · 2.1 km away",
                color = WaypointTextMuted,
                fontSize = 11.sp,
                modifier = Modifier.padding(top = 4.dp),
            )

            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                InfoCard(value = "16°C · Windy", label = "At this location", modifier = Modifier.weight(1f))
                InfoCard(value = "Open now", label = "08:00 – 18:00", modifier = Modifier.weight(1f).padding(start = 8.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth().padding(top = 22.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(R.string.place_detail_about_header),
                    color = WaypointTextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.weight(1f),
                )
                Text(
                    text = stringResource(R.string.place_detail_wikipedia_attribution),
                    color = WaypointTextMuted,
                    fontSize = 10.sp,
                )
            }
            Text(
                text = "Table Mountain is a flat-topped mountain forming a prominent landmark overlooking the city of Cape Town. It is a significant tourist attraction, with a rotating aerial cableway carrying visitors to the summit.",
                color = WaypointTextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp),
            )

            Text(
                text = stringResource(R.string.place_detail_location_header),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 22.dp),
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(160.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow)),
            ) {
                Image(
                    painter = painterResource(R.drawable.img_place_detail_map),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize().background(WaypointCard, RoundedCornerShape(RadiusRow)),
                )
                // TODO: not wired to a real maps deep link yet
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(12.dp)
                        .background(White.copy(alpha = 0.95f), RoundedCornerShape(RadiusRow))
                        .clickable(onClick = {})
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text = stringResource(R.string.place_detail_open_in_maps),
                        color = WaypointTerracotta,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            // TODO: not wired to a real "attach to trip" flow yet
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 22.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                    .clickable(onClick = {})
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.place_detail_add_to_itinerary),
                    color = White,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }

        CircleIconButton(
            onClick = onBackClick,
            fillColor = White.copy(alpha = 0.9f),
            borderColor = null,
            modifier = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 18.dp, top = 8.dp),
        ) {
            Text(
                text = stringResource(R.string.place_detail_back_glyph),
                color = WaypointTextPrimary,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun InfoCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(WaypointCard, RoundedCornerShape(RadiusButton))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(value, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(label, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
    }
}
