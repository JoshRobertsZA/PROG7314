// declares that this file belongs to the package `com.waypoint.app.features.placepicker.ui`
package com.waypoint.app.features.placepicker.ui

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
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.verticalScroll` for use in this file
import androidx.compose.foundation.verticalScroll
// imports `androidx.compose.material3.CircularProgressIndicator` for use in this file
import androidx.compose.material3.CircularProgressIndicator
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
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
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.cache.ExplorePlace` for use in this file
import com.waypoint.app.core.cache.ExplorePlace
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.common.RowSurface` for use in this file
import com.waypoint.app.core.common.RowSurface
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.PlaceThumbnail
import com.waypoint.app.core.common.ThumbnailBlock
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

// declares private read-only property `accentCycle`, initialised with the result of calling `listOf(…)`
private val accentCycle = listOf(
    // continues the statement started above: `WaypointPlaceAccent1, WaypointPlaceAccent2, WaypointPlaceAc…`
    WaypointPlaceAccent1, WaypointPlaceAccent2, WaypointPlaceAccent3, WaypointPlaceAccent4,
// closes the multi-line argument list started above
)

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun PlacePickerScreen(`
fun PlacePickerScreen(
    // continues the statement started above: `tripId: String,`
    tripId: String,
    // continues the statement started above: `dayId: String,`
    dayId: String,
    // continues the statement started above: `category: String,`
    category: String,
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `onPlaceSelected: (placeId: String) -> Unit,`
    onPlaceSelected: (placeId: String) -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: PlacePickerViewModel = viewModel(),`
    viewModel: PlacePickerViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `state`, delegated to `viewModel.uiState.collectAsState()`
    val state by viewModel.uiState.collectAsState()

    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
            .windowInsetsPadding(WindowInsets.systemBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = …`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
        Box(modifier = Modifier.fillMaxWidth()) {
            // calls `CircleIconButton` with an argument list that continues on the following lines
            CircleIconButton(
                // continues the statement started above: `onClick = onBackClick,`
                onClick  = onBackClick,
                // continues the statement started above: `modifier = Modifier.align(Alignment.CenterStart),`
                modifier = Modifier.align(Alignment.CenterStart),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.edit_itinerary_back_glyph),`
                    text       = stringResource(R.string.edit_itinerary_back_glyph),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color      = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize   = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = state.categoryLabel,`
                text       = state.categoryLabel,
                // continues the statement started above: `color = WaypointTextPrimary,`
                color      = WaypointTextPrimary,
                // continues the statement started above: `fontSize = 16.sp,`
                fontSize   = 16.sp,
                // continues the statement started above: `fontWeight = FontWeight.Bold,`
                fontWeight = FontWeight.Bold,
                // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                modifier   = Modifier.align(Alignment.Center),
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `Box`
        }

        // calls `Spacer` with arguments `(Modifier.height(20.dp))`
        Spacer(Modifier.height(20.dp))

        // calls `Column` with arguments `(modifier = Modifier.verticalScroll(rememberS…)` and opens a trailing lambda / block
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            // `when` expression on the value of `val s = state.loadState`: the first matching branch below runs
            when (val s = state.loadState) {
                // `when` branch `PickerLoadState.Loading`: opens a block
                PickerLoadState.Loading -> {
                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier         = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(top = 64.dp),`
                            .padding(top = 64.dp),
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

                // `when` branch `is PickerLoadState.Error`: opens a block
                is PickerLoadState.Error -> {
                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier         = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(top = 64.dp),`
                            .padding(top = 64.dp),
                        // continues the statement started above: `contentAlignment = Alignment.Center,`
                        contentAlignment = Alignment.Center,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = s.message,`
                            text     = s.message,
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color    = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 13.sp,`
                            fontSize = 13.sp,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the when branch
                }

                // `when` branch `PickerLoadState.Empty`: opens a block
                PickerLoadState.Empty -> {
                    // calls `Box` with an argument list that continues on the following lines
                    Box(
                        // continues the statement started above: `modifier = Modifier`
                        modifier         = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.padding(top = 64.dp),`
                            .padding(top = 64.dp),
                        // continues the statement started above: `contentAlignment = Alignment.Center,`
                        contentAlignment = Alignment.Center,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = "No ${state.categoryLabel.lowercase()} found near yo…`
                            text     = "No ${state.categoryLabel.lowercase()} found near your destination.",
                            // continues the statement started above: `color = WaypointTextMuted,`
                            color    = WaypointTextMuted,
                            // continues the statement started above: `fontSize = 13.sp,`
                            fontSize = 13.sp,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the when branch
                }

                // `when` branch `is PickerLoadState.Success`: opens a block
                is PickerLoadState.Success -> {
                    // expression: `s.places.forEachIndexed { index, place ->`
                    s.places.forEachIndexed { index, place ->
                        // continues the statement started above: `PlaceRow(`
                        PlaceRow(
                            // continues the statement started above: `place = place,`
                            place   = place,
                            // continues the statement started above: `index = index,`
                            index   = index,
                            // continues the statement started above: `onClick = { onPlaceSelected(place.id) },`
                            onClick = { onPlaceSelected(place.id) },
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the when branch
                }
            // closes the when block
            }
        // closes the lambda passed to `Column`
        }
    // closes the block
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun PlaceRow(`
private fun PlaceRow(
    // continues the statement started above: `place : ExplorePlace,`
    place   : ExplorePlace,
    // continues the statement started above: `index : Int,`
    index   : Int,
    // continues the statement started above: `onClick : () -> Unit,`
    onClick : () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `RowSurface` with an argument list that continues on the following lines
    RowSurface(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = if (index == 0) 0.dp else 8.dp)`
            .padding(top = if (index == 0) 0.dp else 8.dp)
            // continues the statement started above: `.clickable(onClick = onClick),`
            .clickable(onClick = onClick),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(`
            modifier          = Modifier.padding(
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
            // calls `ThumbnailBlock` with an argument list that continues on the following lines
            PlaceThumbnail(
                type        = place.type,
                accentColor = accentCycle[index % accentCycle.size],
                label       = placeTypeEmoji(place.type),
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


// declares private function `placeSubtitle` taking 1 parameter (`place`), returning `String` and opens its body
private fun placeSubtitle(place: ExplorePlace): String {
    // declares read-only property `kind`, initialised to `place.type`
    val kind = place.type
        // chained call `.replace` on the previous result with arguments `("_", " ")`
        .replace("_", " ")
        // chained call `.replaceFirstChar` on the previous result with an inline lambda that evaluates `it.uppercase()`
        .replaceFirstChar { it.uppercase() }
        // chained call `.ifBlank` on the previous result with an inline lambda that evaluates `"Place"`
        .ifBlank { "Place" }
    // returns `if (place.distanceMetres > 0) {` from the current function
    return if (place.distanceMetres > 0) {
        // expression: `"$kind · ${"%.1f".format(place.distanceMetres / 1000.0)} km away"`
        "$kind · ${"%.1f".format(place.distanceMetres / 1000.0)} km away"
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
    // lambda `"hotel" -> "🏨"`
    "hotel"      -> "🏨"
    // lambda `"restaurant" -> "🍽"`
    "restaurant" -> "🍽"
    // lambda `"pub" -> "🍺"`
    "pub"    -> "🍺"
    // lambda `"cinema" -> "🎬"`
    "cinema" -> "🎬"
    // lambda `"park" -> "🌳"`
    "park"   -> "🌳"
    // `else` branch of the `when`: evaluates `"📍"`
    else     -> "📍"
// closes the block
}
