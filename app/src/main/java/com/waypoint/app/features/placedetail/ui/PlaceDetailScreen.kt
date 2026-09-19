// declares that this file belongs to the package `com.waypoint.app.features.placedetail.ui`
package com.waypoint.app.features.placedetail.ui

// imports `androidx.compose.foundation.Image` for use in this file
import androidx.compose.foundation.Image
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
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
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusRow` for use in this file
import com.waypoint.app.core.theme.RadiusRow
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

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `PlaceDetailScreen` taking 1 parameter (`onBackClick`) and opens its body
fun PlaceDetailScreen(onBackClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Box` with arguments `(modifier = modifier.fillMaxSize().background…)` and opens a trailing lambda / block
    Box(modifier = modifier.fillMaxSize().background(WaypointCream)) {
        // calls `Image` with an argument list that continues on the following lines
        Image(
            // continues the statement started above: `painter = painterResource(R.drawable.img_place_detail_hero),`
            painter = painterResource(R.drawable.img_place_detail_hero),
            // continues the statement started above: `contentDescription = null,`
            contentDescription = null,
            // continues the statement started above: `contentScale = ContentScale.Crop,`
            contentScale = ContentScale.Crop,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.height(220.dp)`
                .height(220.dp)
                // continues the statement started above: `.align(Alignment.TopStart),`
                .align(Alignment.TopStart),
        // closes the multi-line argument list started above
        )

        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.padding(top = 190.dp)`
                .padding(top = 190.dp)
                // continues the statement started above: `.verticalScroll(rememberScrollState())`
                .verticalScroll(rememberScrollState())
                // continues the statement started above: `.background(WaypointCream, RoundedCornerShape(topStart = 28…`
                .background(WaypointCream, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                // continues the statement started above: `.padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = …`
                .padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = 28.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with arguments `("Table Mountain", color = WaypointTextPrimar…)`
            Text("Table Mountain", color = WaypointTextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Bold)
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "Landmark · 2.1 km away",`
                text = "Landmark · 2.1 km away",
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 11.sp,`
                fontSize = 11.sp,
                // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
                modifier = Modifier.padding(top = 4.dp),
            // closes the multi-line argument list started above
            )

            // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
            Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
                // calls `InfoCard` with arguments `(value = "16°C · Windy", label = "At this loc…)`
                InfoCard(value = "16°C · Windy", label = "At this location", modifier = Modifier.weight(1f))
                // calls `InfoCard` with arguments `(value = "Open now", label = "08:00 – 18:00",…)`
                InfoCard(value = "Open now", label = "08:00 – 18:00", modifier = Modifier.weight(1f).padding(start = 8.dp))
            // closes the lambda passed to `Row`
            }

            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = 22.dp),`
                modifier = Modifier.fillMaxWidth().padding(top = 22.dp),
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.place_detail_about_header),`
                    text = stringResource(R.string.place_detail_about_header),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.place_detail_wikipedia_attri…`
                    text = stringResource(R.string.place_detail_wikipedia_attribution),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = "Table Mountain is a flat-topped mountain forming a …`
                text = "Table Mountain is a flat-topped mountain forming a prominent landmark overlooking the city of Cape Town. It is a significant tourist attraction, with a rotating aerial cableway carrying visitors to the summit.",
                // continues the statement started above: `color = WaypointTextMuted,`
                color = WaypointTextMuted,
                // continues the statement started above: `fontSize = 12.sp,`
                fontSize = 12.sp,
                // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                modifier = Modifier.padding(top = 8.dp),
            // closes the multi-line argument list started above
            )

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.place_detail_location_header…`
                text = stringResource(R.string.place_detail_location_header),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
                // continues the statement started above: `modifier = Modifier.padding(top = 22.dp),`
                modifier = Modifier.padding(top = 22.dp),
            // closes the multi-line argument list started above
            )
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 12.dp)`
                    .padding(top = 12.dp)
                    // continues the statement started above: `.height(160.dp)`
                    .height(160.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusRow)),`
                    .background(WaypointCard, RoundedCornerShape(RadiusRow)),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Image` with an argument list that continues on the following lines
                Image(
                    // continues the statement started above: `painter = painterResource(R.drawable.img_place_detail_map),`
                    painter = painterResource(R.drawable.img_place_detail_map),
                    // continues the statement started above: `contentDescription = null,`
                    contentDescription = null,
                    // continues the statement started above: `contentScale = ContentScale.Crop,`
                    contentScale = ContentScale.Crop,
                    // continues the statement started above: `modifier = Modifier.fillMaxSize().background(WaypointCard, …`
                    modifier = Modifier.fillMaxSize().background(WaypointCard, RoundedCornerShape(RadiusRow)),
                // closes the multi-line argument list started above
                )
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.align(Alignment.BottomStart)`
                        .align(Alignment.BottomStart)
                        // continues the statement started above: `.padding(12.dp)`
                        .padding(12.dp)
                        // continues the statement started above: `.background(White.copy(alpha = 0.95f), RoundedCornerShape(R…`
                        .background(White.copy(alpha = 0.95f), RoundedCornerShape(RadiusRow))
                        // continues the statement started above: `.clickable(onClick = {})`
                        .clickable(onClick = {})
                        // continues the statement started above: `.padding(horizontal = 16.dp, vertical = 8.dp),`
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.place_detail_open_in_maps),`
                        text = stringResource(R.string.place_detail_open_in_maps),
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color = WaypointTerracotta,
                        // continues the statement started above: `fontSize = 11.sp,`
                        fontSize = 11.sp,
                        // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                        fontWeight = FontWeight.SemiBold,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.padding(top = 22.dp)`
                    .padding(top = 22.dp)
                    // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusBu…`
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                    // continues the statement started above: `.clickable(onClick = {})`
                    .clickable(onClick = {})
                    // continues the statement started above: `.padding(vertical = 14.dp),`
                    .padding(vertical = 14.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.place_detail_add_to_itinerar…`
                    text = stringResource(R.string.place_detail_add_to_itinerary),
                    // continues the statement started above: `color = White,`
                    color = White,
                    // continues the statement started above: `fontSize = 13.sp,`
                    fontSize = 13.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }

        // calls `CircleIconButton` with an argument list that continues on the following lines
        CircleIconButton(
            // continues the statement started above: `onClick = onBackClick,`
            onClick = onBackClick,
            // continues the statement started above: `fillColor = White.copy(alpha = 0.9f),`
            fillColor = White.copy(alpha = 0.9f),
            // continues the statement started above: `borderColor = null,`
            borderColor = null,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.align(Alignment.TopStart)`
                .align(Alignment.TopStart)
                // continues the statement started above: `.windowInsetsPadding(WindowInsets.statusBars)`
                .windowInsetsPadding(WindowInsets.statusBars)
                // continues the statement started above: `.padding(start = 18.dp, top = 8.dp),`
                .padding(start = 18.dp, top = 8.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.place_detail_back_glyph),`
                text = stringResource(R.string.place_detail_back_glyph),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the lambda passed to `Box`
    }
// closes the function `PlaceDetailScreen`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `InfoCard` taking 3 parameters (`value`, `label`, `modifier`) and opens its body
private fun InfoCard(value: String, label: String, modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusButton))`
            .background(WaypointCard, RoundedCornerShape(RadiusButton))
            // continues the statement started above: `.padding(horizontal = 14.dp, vertical = 12.dp),`
            .padding(horizontal = 14.dp, vertical = 12.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(value, color = WaypointTextPrimary, fontSize…)`
        Text(value, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        // calls `Text` with arguments `(label, color = WaypointTextMuted, fontSize =…)`
        Text(label, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
    // closes the block
    }
// closes the function `InfoCard`
}
