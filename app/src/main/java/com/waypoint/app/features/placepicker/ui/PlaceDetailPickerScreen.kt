// declares that this file belongs to the package `com.waypoint.app.features.placepicker.ui`
package com.waypoint.app.features.placepicker.ui

// imports `android.content.Intent` for use in this file
import android.content.Intent
// imports `android.net.Uri` for use in this file
import android.net.Uri
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
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
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
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
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
// imports `androidx.compose.ui.graphics.asImageBitmap` for use in this file
import androidx.compose.ui.graphics.asImageBitmap
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextOverflow` for use in this file
import androidx.compose.ui.text.style.TextOverflow
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.ThumbnailBlock
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusRow` for use in this file
import com.waypoint.app.core.theme.RadiusRow
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream
// imports `com.waypoint.app.core.theme.WaypointPlaceAccent2` for use in this file
import com.waypoint.app.core.theme.WaypointPlaceAccent2
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
// expression: `fun PlaceDetailPickerScreen(`
fun PlaceDetailPickerScreen(
    // continues the statement started above: `tripId : String,`
    tripId      : String,
    // continues the statement started above: `dayId : String,`
    dayId       : String,
    // continues the statement started above: `category : String,`
    category    : String,
    // continues the statement started above: `placeId : String,`
    placeId     : String,
    // continues the statement started above: `onBackClick : () -> Unit,`
    onBackClick : () -> Unit,
    // continues the statement started above: `onPlaceAdded: () -> Unit,`
    onPlaceAdded: () -> Unit,
    // continues the statement started above: `modifier : Modifier = Modifier,`
    modifier    : Modifier = Modifier,
    // continues the statement started above: `viewModel : PlaceDetailPickerViewModel = viewModel(),`
    viewModel   : PlaceDetailPickerViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `state`, delegated to `viewModel.uiState.collectAsState()`
    val state   by viewModel.uiState.collectAsState()
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current

    // calls `LaunchedEffect` with arguments `(state.isAdded)` and opens a trailing lambda / block
    LaunchedEffect(state.isAdded) {
        // `if` statement: executes `onPlaceAdded()` when `state.isAdded` is true
        if (state.isAdded) onPlaceAdded()
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `Box` with arguments `(modifier = modifier.fillMaxSize().background…)` and opens a trailing lambda / block
    Box(modifier = modifier.fillMaxSize().background(WaypointCream)) {

        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier         = Modifier
                // continues the statement started above: `.fillMaxWidth()`
                .fillMaxWidth()
                // continues the statement started above: `.height(220.dp)`
                .height(220.dp)
                // continues the statement started above: `.background(WaypointPlaceAccent2),`
                .background(WaypointPlaceAccent2),
            // continues the statement started above: `contentAlignment = Alignment.Center,`
            contentAlignment = Alignment.Center,
        // ends the argument list started above and opens the block that follows
        ) {
            // declares read-only property `bitmap`, initialised with the result of calling `if(…)`
            val bitmap = if (!state.isLoadingWiki) state.photoBitmap else null
            // declares read-only property `emoji`, initialised to `state.placeEmoji.ifEmpty { "📍" }`
            val emoji  = state.placeEmoji.ifEmpty { "📍" }
            // `if` statement: the block below runs when `bitmap != null` is true
            if (bitmap != null) {
                // calls `Image` with an argument list that continues on the following lines
                Image(
                    // continues the statement started above: `bitmap = bitmap.asImageBitmap(),`
                    bitmap             = bitmap.asImageBitmap(),
                    // continues the statement started above: `contentDescription = state.placeName,`
                    contentDescription = state.placeName,
                    // continues the statement started above: `contentScale = ContentScale.Crop,`
                    contentScale       = ContentScale.Crop,
                    // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
                    modifier           = Modifier.fillMaxSize(),
                // closes the multi-line argument list started above
                )
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // calls `Text` with arguments `(text = emoji, fontSize = 72.sp)`
                Text(text = emoji, fontSize = 72.sp)
            // closes the else branch
            }
        // closes the block
        }

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
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = state.placeName.ifBlank { "…" },`
                text       = state.placeName.ifBlank { "…" },
                // continues the statement started above: `color = WaypointTextPrimary,`
                color      = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 22.sp,`
                fontSize   = 22.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `maxLines = 2,`
                maxLines   = 2,
                // continues the statement started above: `overflow = TextOverflow.Ellipsis,`
                overflow   = TextOverflow.Ellipsis,
            // closes the multi-line argument list started above
            )
            // declares read-only property `subtitle`, initialised with the result of calling `listOfNotNull(…)`
            val subtitle = listOfNotNull(
                // continues the statement started above: `state.placeType.replaceFirstChar { it.uppercase() }.ifBlank…`
                state.placeType.replaceFirstChar { it.uppercase() }.ifBlank { null },
                // continues the statement started above: `state.distanceKm.ifBlank { null },`
                state.distanceKm.ifBlank { null },
            // continues the statement started above: `).joinToString(" · ")`
            ).joinToString(" · ")
            // `if` statement: the block below runs when `subtitle.isNotBlank()` is true
            if (subtitle.isNotBlank()) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = subtitle,`
                    text     = subtitle,
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color    = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 11.sp,`
                    fontSize = 11.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 4.dp),`
                    modifier = Modifier.padding(top = 4.dp),
                // closes the multi-line argument list started above
                )
            // closes the if block
            }

            // calls `Spacer` with arguments `(Modifier.height(20.dp))`
            Spacer(Modifier.height(20.dp))
            // `if` statement: the block below runs when `state.isLoadingWeather` is true
            if (state.isLoadingWeather) {
                // calls `CircularProgressIndicator` with an argument list that continues on the following lines
                CircularProgressIndicator(
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color    = WaypointTerracotta,
                    // continues the statement started above: `modifier = Modifier.height(32.dp).width(32.dp),`
                    modifier = Modifier.height(32.dp).width(32.dp),
                // closes the multi-line argument list started above
                )
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // declares read-only property `w`, initialised to `state.weather`
                val w = state.weather
                // `if` statement: the block below runs when `w != null` is true
                if (w != null) {
                    // calls `InfoCard` with an argument list that continues on the following lines
                    InfoCard(
                        // continues the statement started above: `value = "${"%.0f".format(w.tempC)}°C · ${w.description}",`
                        value    = "${"%.0f".format(w.tempC)}°C · ${w.description}",
                        // continues the statement started above: `label = stringResource(R.string.place_at_location),`
                        label    = stringResource(R.string.place_at_location),
                        // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                        modifier = Modifier.fillMaxWidth(),
                    // closes the multi-line argument list started above
                    )
                // closes the if block
                }
            // closes the else branch
            }

            // calls `Spacer` with arguments `(Modifier.height(22.dp))`
            Spacer(Modifier.height(22.dp))
            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                modifier          = Modifier.fillMaxWidth(),
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.place_detail_about_header),`
                    text       = stringResource(R.string.place_detail_about_header),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color      = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize   = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier   = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.place_detail_wikipedia_attri…`
                    text     = stringResource(R.string.place_detail_wikipedia_attribution),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color    = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // `if` statement: the block below runs when `state.isLoadingWiki` is true
            if (state.isLoadingWiki) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.common_loading),`
                    text     = stringResource(R.string.common_loading),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color    = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 12.sp,`
                    fontSize = 12.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                    modifier = Modifier.padding(top = 8.dp),
                // closes the multi-line argument list started above
                )
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // declares read-only property `extract`, initialised to `state.wikiSummary?.extract`
                val extract = state.wikiSummary?.extract
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = extract ?: "No Wikipedia article found for this plac…`
                    text     = extract ?: "No Wikipedia article found for this place.",
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color    = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 12.sp,`
                    fontSize = 12.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                    modifier = Modifier.padding(top = 8.dp),
                    // continues the statement started above: `maxLines = 8,`
                    maxLines = 8,
                    // continues the statement started above: `overflow = TextOverflow.Ellipsis,`
                    overflow = TextOverflow.Ellipsis,
                // closes the multi-line argument list started above
                )
            // closes the else branch
            }

            // calls `Spacer` with arguments `(Modifier.height(22.dp))`
            Spacer(Modifier.height(22.dp))
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.place_detail_location_header…`
                text       = stringResource(R.string.place_detail_location_header),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color      = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize   = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
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
                    // continues the statement started above: `.height(80.dp)`
                    .height(80.dp)
                    // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(RadiusRow)),`
                    .background(WaypointCard, RoundedCornerShape(RadiusRow)),
                // continues the statement started above: `contentAlignment = Alignment.BottomStart,`
                contentAlignment = Alignment.BottomStart,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.padding(12.dp)`
                        .padding(12.dp)
                        // continues the statement started above: `.background(White.copy(alpha = 0.95f), RoundedCornerShape(R…`
                        .background(White.copy(alpha = 0.95f), RoundedCornerShape(RadiusRow))
                        // continues the statement started above: `.clickable {`
                        .clickable {
                            // `if` statement: the block below runs when `state.lat != 0.0 || state.lon != 0.0` is true
                            if (state.lat != 0.0 || state.lon != 0.0) {
                                // declares read-only property `uri`, initialised with the result of calling `Uri.parse(…)`
                                val uri = Uri.parse(
                                    // continues the statement started above: `"https://www.google.com/maps/dir/?api=1" +`
                                    "https://www.google.com/maps/dir/?api=1" +
                                    // continues the statement started above: `"&destination=${state.lat},${state.lon}" +`
                                    "&destination=${state.lat},${state.lon}" +
                                    // continues the statement started above: `"&destination_place_id=${Uri.encode(state.placeName)}"`
                                    "&destination_place_id=${Uri.encode(state.placeName)}"
                                // closes the multi-line argument list started above
                                )
                                // calls `startActivity` on `context` with arguments `(Intent(Intent.ACTION_VIEW, uri))`
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            // closes the if block
                            }
                        // closes the block
                        }
                        // expression: `.padding(horizontal = 16.dp, vertical = 8.dp),`
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.place_detail_open_in_maps),`
                        text       = stringResource(R.string.place_detail_open_in_maps),
                        // continues the statement started above: `color = WaypointTerracotta,`
                        color      = WaypointTerracotta,
                        // continues the statement started above: `fontSize = 11.sp,`
                        fontSize   = 11.sp,
                        // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                        fontWeight = FontWeight.SemiBold,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }

            // calls `Spacer` with arguments `(Modifier.height(22.dp))`
            Spacer(Modifier.height(22.dp))
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.background(`
                    .background(
                        // continues the statement started above: `if (state.isAdding) WaypointTerracotta.copy(alpha = 0.6f) e…`
                        if (state.isAdding) WaypointTerracotta.copy(alpha = 0.6f) else WaypointTerracotta,
                        // continues the statement started above: `RoundedCornerShape(RadiusButton),`
                        RoundedCornerShape(RadiusButton),
                    // closes the multi-line argument list started above
                    )
                    // continues the statement started above: `.clickable(enabled = !state.isAdding && !state.isAdded) {`
                    .clickable(enabled = !state.isAdding && !state.isAdded) {
                        // calls `onAddToItinerary` on `viewModel` with arguments `()`
                        viewModel.onAddToItinerary()
                    // closes the block
                    }
                    // expression: `.padding(vertical = 14.dp),`
                    .padding(vertical = 14.dp),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // `if` statement: the block below runs when `state.isAdding` is true
                if (state.isAdding) {
                    // calls `CircularProgressIndicator` with arguments `(color = White, modifier = Modifier.height(20…)`
                    CircularProgressIndicator(color = White, modifier = Modifier.height(20.dp).width(20.dp))
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.place_detail_add_to_itinerar…`
                        text       = stringResource(R.string.place_detail_add_to_itinerary),
                        // continues the statement started above: `color = White,`
                        color      = White,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize   = 13.sp,
                        // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                        fontWeight = FontWeight.SemiBold,
                    // closes the multi-line argument list started above
                    )
                // closes the else branch
                }
            // closes the block
            }
        // closes the block
        }

        // calls `CircleIconButton` with an argument list that continues on the following lines
        CircleIconButton(
            // continues the statement started above: `onClick = onBackClick,`
            onClick     = onBackClick,
            // continues the statement started above: `fillColor = White.copy(alpha = 0.9f),`
            fillColor   = White.copy(alpha = 0.9f),
            // continues the statement started above: `borderColor = null,`
            borderColor = null,
            // continues the statement started above: `modifier = Modifier`
            modifier    = Modifier
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
                text       = stringResource(R.string.place_detail_back_glyph),
                // continues the statement started above: `color = WaypointTextPrimary,`
                color      = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 15.sp,`
                fontSize   = 15.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the lambda passed to `Box`
    }
// closes the block
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
