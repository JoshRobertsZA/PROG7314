// declares that this file belongs to the package `com.waypoint.app.features.viewitinerary.ui`
package com.waypoint.app.features.viewitinerary.ui

// imports `android.content.Intent` for use in this file
import android.content.Intent
// imports `android.widget.Toast` for use in this file
import android.widget.Toast
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `coil.compose.AsyncImage` for use in this file
import coil.compose.AsyncImage
// imports `android.graphics.Bitmap` for use in this file
import android.graphics.Bitmap
// imports `androidx.compose.foundation.Image` for use in this file
import androidx.compose.foundation.Image
// imports `androidx.compose.ui.graphics.asImageBitmap` for use in this file
import androidx.compose.ui.graphics.asImageBitmap
// imports `coil.compose.SubcomposeAsyncImage` for use in this file
import coil.compose.SubcomposeAsyncImage
// imports `coil.request.ImageRequest` for use in this file
import coil.request.ImageRequest
// imports `androidx.compose.foundation.layout.statusBarsPadding` for use in this file
import androidx.compose.foundation.layout.statusBarsPadding
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.ui.text.style.TextOverflow` for use in this file
import androidx.compose.ui.text.style.TextOverflow
// imports `com.waypoint.app.core.theme.RadiusHero` for use in this file
import com.waypoint.app.core.theme.RadiusHero
// imports `com.waypoint.app.core.theme.WaypointDecoText` for use in this file
import com.waypoint.app.core.theme.WaypointDecoText
// imports `android.net.Uri` for use in this file
import android.net.Uri
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.BoxWithConstraints` for use in this file
import androidx.compose.foundation.layout.BoxWithConstraints
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
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.lazy.LazyRow` for use in this file
import androidx.compose.foundation.lazy.LazyRow
// imports `androidx.compose.foundation.lazy.itemsIndexed` for use in this file
import androidx.compose.foundation.lazy.itemsIndexed
// imports `androidx.compose.foundation.lazy.rememberLazyListState` for use in this file
import androidx.compose.foundation.lazy.rememberLazyListState
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
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.Dp` for use in this file
import androidx.compose.ui.unit.Dp
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.CardSurface` for use in this file
import com.waypoint.app.core.common.CardSurface
// imports `com.waypoint.app.core.common.CircleIconButton` for use in this file
import com.waypoint.app.core.common.CircleIconButton
// imports `com.waypoint.app.core.common.RowSurface` for use in this file
import com.waypoint.app.core.common.RowSurface
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import com.waypoint.app.core.common.ThumbnailBlock
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
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
// imports `com.waypoint.app.core.theme.WaypointTripBadgeText` for use in this file
import com.waypoint.app.core.theme.WaypointTripBadgeText
// imports `com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository` for use in this file
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.material3.AlertDialog` for use in this file
import androidx.compose.material3.AlertDialog
// imports `androidx.compose.material3.TextButton` for use in this file
import androidx.compose.material3.TextButton
// imports `androidx.compose.material3.Divider` for use in this file
import androidx.compose.material3.HorizontalDivider

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun ViewItineraryScreen(`
fun ViewItineraryScreen(
    // continues the statement started above: `tripId: String,`
    tripId: String,
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: ViewItineraryViewModel = viewModel(),`
    viewModel: ViewItineraryViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `state`, delegated to `viewModel.uiState.collectAsState()`
    val state by viewModel.uiState.collectAsState()

    // expression: `state.selectedPlace?.let { place ->`
    state.selectedPlace?.let { place ->
        // continues the statement started above: `ViewPlaceDetailOverlay(`
        ViewPlaceDetailOverlay(
            // continues the statement started above: `place = place,`
            place      = place,
            // continues the statement started above: `onBack = viewModel::onPlaceDismissed,`
            onBack     = viewModel::onPlaceDismissed,
        // closes the multi-line argument list started above
        )
        // returns from the current function with no value
        return
    // closes the block
    }

    // show the flight status modal when the state is Loading, Success or Error
    val flightStatus = state.flightStatus
    if (flightStatus !is FlightStatusState.Idle) {
        FlightStatusModal(
            status    = flightStatus,
            onDismiss = viewModel::onFlightStatusDismissed,
        )
    }

    // `if` statement: the block below runs when `state.isLoading` is true
    if (state.isLoading) {
        // calls `Box` with arguments `(modifier = modifier.fillMaxSize(), contentAl…)` and opens a trailing lambda / block
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            // calls `CircularProgressIndicator` with arguments `(color = WaypointTerracotta)`
            CircularProgressIndicator(color = WaypointTerracotta)
        // closes the lambda passed to `Box`
        }
        // returns from the current function with no value
        return
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
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
            .windowInsetsPadding(WindowInsets.systemBars)
            // continues the statement started above: `.padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = …`
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp)
            // continues the statement started above: `.verticalScroll(rememberScrollState()),`
            .verticalScroll(rememberScrollState()),
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
                    // continues the statement started above: `text = stringResource(R.string.view_itinerary_back_glyph),`
                    text       = stringResource(R.string.view_itinerary_back_glyph),
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
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                modifier            = Modifier.align(Alignment.Center),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.view_itinerary_title),`
                    text       = stringResource(R.string.view_itinerary_title),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color      = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 16.sp,`
                    fontSize   = 16.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // `if` statement: the block below runs when `state.days.isNotEmpty()` is true
                if (state.days.isNotEmpty()) {
                    // declares read-only property `first`, initialised with the result of calling `state.days.first(…)`
                    val first    = state.days.first().date
                    // declares read-only property `last`, initialised with the result of calling `state.days.last(…)`
                    val last     = state.days.last().date
                    // declares read-only property `subtitle`, initialised with the result of calling `if(…)`
                    val subtitle = if (first == last) "$first" else "$first – $last"
                    // calls `Text` with arguments `(text = subtitle, color = WaypointTextMuted, …)`
                    Text(text = subtitle, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                // closes the if block
                }
            // closes the block
            }
        // closes the lambda passed to `Box`
        }

        // calls `Spacer` with arguments `(Modifier.height(20.dp))`
        Spacer(Modifier.height(20.dp))

        // `if` statement: the block below runs when `state.days.isNotEmpty()` is true
        if (state.days.isNotEmpty()) {
            // calls `ViewDayScroller` with an argument list that continues on the following lines
            ViewDayScroller(
                // continues the statement started above: `days = state.days,`
                days           = state.days,
                // continues the statement started above: `activeDayIndex = state.activeDayIndex,`
                activeDayIndex = state.activeDayIndex,
                // continues the statement started above: `onDaySelected = viewModel::onDaySelected,`
                onDaySelected  = viewModel::onDaySelected,
            // closes the multi-line argument list started above
            )
        // closes the if block
        }

        // calls `ViewSectionHeader` with arguments `(title = stringResource(R.string.edit_itinera…)`
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_flights), topPadding = 20.dp)
        // `if` statement: the block below runs when `state.flightsForActiveDay.isEmpty()` is true
        if (state.flightsForActiveDay.isEmpty()) {
            // calls `ViewEmptyPlaceholder` with arguments `(label = stringResource(R.string.view_itinera…)`
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_flights))
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `state.flightsForActiveDay.forEach { f ->`
            state.flightsForActiveDay.forEach { f ->
                // continues the statement started above: `ViewFlightCard(flight = f, onStatusClick = …)`
                ViewFlightCard(
                    flight        = f,
                    onStatusClick = {
                        val fn = f.flightNumber?.takeIf { it.isNotBlank() }
                        if (fn != null) viewModel.onFlightStatusTap(fn)
                    },
                )
            // closes the block
            }
        // closes the else branch
        }

        // calls `ViewSectionHeader` with arguments `(title = stringResource(R.string.edit_itinera…)`
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_lodging), topPadding = 20.dp)
        // declares read-only property `lodgings`, initialised to `state.lodgingForActiveDay`
        val lodgings = state.lodgingForActiveDay
        // `if` statement: the block below runs when `lodgings.isEmpty()` is true
        if (lodgings.isEmpty()) {
            // calls `ViewEmptyPlaceholder` with arguments `(label = stringResource(R.string.view_itinera…)`
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_lodging))
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `lodgings.forEach { lodging ->`
            lodgings.forEach { lodging ->
                // continues the statement started above: `ViewDocCard(`
                ViewDocCard(
                    // continues the statement started above: `accentColor = WaypointPlaceAccent2,`
                    accentColor = WaypointPlaceAccent2,
                    // continues the statement started above: `title = lodging.docName ?: stringResource(R.string.edit_iti…`
                    title       = lodging.docName ?: stringResource(R.string.edit_itinerary_lodging_doc),
                    // continues the statement started above: `subtitle = "${lodging.fromDate} – ${lodging.toDate}",`
                    subtitle    = "${lodging.fromDate} – ${lodging.toDate}",
                    // continues the statement started above: `pdfUri = lodging.pdfUri,`
                    pdfUri      = lodging.pdfUri,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // calls `ViewSectionHeader` with arguments `(title = stringResource(R.string.edit_itinera…)`
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_car), topPadding = 20.dp)
        // declares read-only property `cars`, initialised to `state.carRentalsForActiveDay`
        val cars = state.carRentalsForActiveDay
        // `if` statement: the block below runs when `cars.isEmpty()` is true
        if (cars.isEmpty()) {
            // calls `ViewEmptyPlaceholder` with arguments `(label = stringResource(R.string.view_itinera…)`
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_car))
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `cars.forEach { car ->`
            cars.forEach { car ->
                // continues the statement started above: `ViewDocCard(`
                ViewDocCard(
                    // continues the statement started above: `accentColor = WaypointPlaceAccent3,`
                    accentColor = WaypointPlaceAccent3,
                    // continues the statement started above: `title = car.docName ?: stringResource(R.string.edit_itinera…`
                    title       = car.docName ?: stringResource(R.string.edit_itinerary_car_doc),
                    // continues the statement started above: `subtitle = "${car.fromDate} – ${car.toDate}",`
                    subtitle    = "${car.fromDate} – ${car.toDate}",
                    // continues the statement started above: `pdfUri = car.pdfUri,`
                    pdfUri      = car.pdfUri,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // calls `listOf` with an argument list that continues on the following lines
        listOf(
            // continues the statement started above: `"RESTAURANTS" to stringResource(R.string.edit_itinerary_hea…`
            "RESTAURANTS" to stringResource(R.string.edit_itinerary_header_food),
            // continues the statement started above: `"PARKS" to stringResource(R.string.edit_itinerary_header_pa…`
            "PARKS"       to stringResource(R.string.edit_itinerary_header_parks),
            // continues the statement started above: `"PUBS" to stringResource(R.string.edit_itinerary_header_pub…`
            "PUBS"    to stringResource(R.string.edit_itinerary_header_pubs),
            // continues the statement started above: `"CINEMAS" to stringResource(R.string.edit_itinerary_header_…`
            "CINEMAS" to stringResource(R.string.edit_itinerary_header_cinemas),
        // continues the statement started above: `).forEach { (key, header) ->`
        ).forEach { (key, header) ->
            // continues the statement started above: `val items = state.placesForActiveDay[key].orEmpty()`
            val items = state.placesForActiveDay[key].orEmpty()
            // calls `ViewSectionHeader` with arguments `(title = header, topPadding = 20.dp)`
            ViewSectionHeader(title = header, topPadding = 20.dp)
            // `if` statement: the block below runs when `items.isEmpty()` is true
            if (items.isEmpty()) {
                // calls `ViewEmptyPlaceholder` with arguments `(label = stringResource(R.string.view_itinera…)`
                ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_places))
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // lambda `items.forEach { place -> ViewPlaceCard(place = place, …`
                items.forEach { place -> ViewPlaceCard(place = place, onTap = { viewModel.onPlaceSelected(place) }) }
            // closes the else branch
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun ViewDayScroller(`
private fun ViewDayScroller(
    // continues the statement started above: `days: List<ViewDayItem>,`
    days: List<ViewDayItem>,
    // continues the statement started above: `activeDayIndex: Int,`
    activeDayIndex: Int,
    // continues the statement started above: `onDaySelected: (Int) -> Unit,`
    onDaySelected: (Int) -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `BoxWithConstraints` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // declares read-only property `cardWidth`, initialised to `(maxWidth - 16.dp) / 3`
        val cardWidth = (maxWidth - 16.dp) / 3
        // calls `LazyRow` with an argument list that continues on the following lines
        LazyRow(
            // continues the statement started above: `state = rememberLazyListState(),`
            state                 = rememberLazyListState(),
            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
            modifier              = Modifier.fillMaxWidth(),
            // continues the statement started above: `horizontalArrangement = Arrangement.spacedBy(8.dp),`
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `itemsIndexed` with arguments `(days)`
            itemsIndexed(days) { index, day ->
                // continues the statement started above: `val isActive = index == activeDayIndex`
                val isActive = index == activeDayIndex
                // calls `CardSurface` with an argument list that continues on the following lines
                CardSurface(
                    // continues the statement started above: `modifier = Modifier.width(cardWidth).clickable { onDaySelec…`
                    modifier     = Modifier.width(cardWidth).clickable { onDaySelected(index) },
                    // continues the statement started above: `cornerRadius = RadiusButton,`
                    cornerRadius = RadiusButton,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier`
                        modifier            = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.background(if (isActive) WaypointTerracotta.copy(alpha = 0…`
                            .background(if (isActive) WaypointTerracotta.copy(alpha = 0.08f) else Color.Transparent)
                            // continues the statement started above: `.padding(vertical = 12.dp),`
                            .padding(vertical = 12.dp),
                        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                        horizontalAlignment = Alignment.CenterHorizontally,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.dayOfWeek.name.take(3).lowercase().replaceF…`
                            text     = day.date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() },
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextM…`
                            color    = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            // continues the statement started above: `fontSize = 9.sp,`
                            fontSize = 9.sp,
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.dayOfMonth.toString(),`
                            text       = day.date.dayOfMonth.toString(),
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextP…`
                            color      = if (isActive) WaypointTerracotta else WaypointTextPrimary,
                            // continues the statement started above: `fontSize = 20.sp,`
                            fontSize   = 20.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                            modifier   = Modifier.padding(top = 2.dp),
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.month.name.lowercase().replaceFirstChar { i…`
                            text     = day.date.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }.take(3),
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextM…`
                            color    = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            // continues the statement started above: `fontSize = 9.sp,`
                            fontSize = 9.sp,
                            // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                            modifier = Modifier.padding(top = 2.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the block
                }
            // closes the block
            }
        // closes the block
        }
    // closes the lambda passed to `BoxWithConstraints`
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ViewFlightCard` taking 2 parameters (`flight`, `onStatusClick`) and opens its body
private fun ViewFlightCard(flight: ViewFlightItem, onStatusClick: () -> Unit) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // calls `RowSurface` with an argument list that continues on the following lines
    RowSurface(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = 8.dp)`
            .padding(top = 8.dp)
            // continues the statement started above: `.clickable { openPdf(context, flight.pdfUri) },`
            .clickable { openPdf(context, flight.pdfUri) },
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 10.dp, end…`
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // shows the airline logo if a carrier code can be derived from the flight number, falling back to the accent block
            val logoUrl = airlineLogoUrl(flight.flightNumber)
            if (logoUrl != null) {
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(context).data(logoUrl).crossfade(true).build(),
                    contentDescription = flight.flightNumber,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(RadiusThumbnail))
                        .background(androidx.compose.ui.graphics.Color.White),
                    loading = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                    error   = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                )
            } else {
                ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail)
            }
            // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                // declares read-only property `label`, initialised to `flight.flightNumber?.takeIf { it.isNotBlank(…`
                val label = flight.flightNumber?.takeIf { it.isNotBlank() } ?: stringResource(R.string.reminder_flight_unnamed)
                // calls `Text` with arguments `(label, color = WaypointTextPrimary, fontSize…)` and a clickable modifier so tapping the number opens live status
                Text(
                    text       = label,
                    color      = WaypointTerracotta,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.clickable(enabled = flight.flightNumber?.isNotBlank() == true) { onStatusClick() },
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = flight.departureTime?.let { stringResource(R.string.…`
                    text     = flight.departureTime?.let { stringResource(R.string.edit_itinerary_flight_departs, it) }
                               // continues the statement started above: `?: stringResource(R.string.view_itinerary_flight_no_time),`
                               ?: stringResource(R.string.view_itinerary_flight_no_time),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color    = WaypointTextMuted,
                    // continues the statement started above: `fontSize = 11.sp,`
                    fontSize = 11.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 3.dp),`
                    modifier = Modifier.padding(top = 3.dp),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = flight.docName ?: pdfLabel(flight.pdfUri),`
                    text     = flight.docName ?: pdfLabel(flight.pdfUri),
                    // continues the statement started above: `color = WaypointTripBadgeText,`
                    color    = WaypointTripBadgeText,
                    // continues the statement started above: `fontSize = 9.sp,`
                    fontSize = 9.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 3.dp).alpha(0.8f),`
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `Column`
            }
        // closes the block
        }
    // closes the block
    }
// closes the function `ViewFlightCard`
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun ViewDocCard(`
private fun ViewDocCard(
    // continues the statement started above: `accentColor: Color,`
    accentColor: Color,
    // continues the statement started above: `title: String,`
    title: String,
    // continues the statement started above: `subtitle: String,`
    subtitle: String,
    // continues the statement started above: `pdfUri: String,`
    pdfUri: String,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // calls `RowSurface` with an argument list that continues on the following lines
    RowSurface(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = 8.dp)`
            .padding(top = 8.dp)
            // continues the statement started above: `.clickable { openPdf(context, pdfUri) },`
            .clickable { openPdf(context, pdfUri) },
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 10.dp, end…`
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `ThumbnailBlock` with arguments `(accentColor = accentColor, size = 44.dp, cor…)`
            ThumbnailBlock(accentColor = accentColor, size = 44.dp, cornerRadius = RadiusThumbnail)
            // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                // calls `Text` with arguments `(title, color = WaypointTextPrimary, fontSize…)`
                Text(title, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                // calls `Text` with arguments `(subtitle, color = WaypointTextMuted, fontSiz…)`
                Text(subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
            // closes the lambda passed to `Column`
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ViewPlaceCard` taking 2 parameters (`place`, `onTap`) and opens its body
private fun ViewPlaceCard(place: ViewPlaceItem, onTap: () -> Unit) {
    // calls `RowSurface` with an argument list that continues on the following lines
    RowSurface(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = 8.dp)`
            .padding(top = 8.dp)
            // continues the statement started above: `.clickable { onTap() },`
            .clickable { onTap() },
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 10.dp, end…`
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // `if` statement: the block below runs when `!place.photoUrl.isNullOrBlank()` is true
            if (!place.photoUrl.isNullOrBlank()) {
                // declares read-only property `ctx`, initialised to `LocalContext.current`
                val ctx = LocalContext.current
                // calls `SubcomposeAsyncImage` with an argument list that continues on the following lines
                SubcomposeAsyncImage(
                    // continues the statement started above: `model = ImageRequest.Builder(ctx).data(place.photoUrl).cros…`
                    model              = ImageRequest.Builder(ctx).data(place.photoUrl).crossfade(true).build(),
                    // continues the statement started above: `contentDescription = place.name,`
                    contentDescription = place.name,
                    // continues the statement started above: `contentScale = ContentScale.Crop,`
                    contentScale       = ContentScale.Crop,
                    // continues the statement started above: `modifier = Modifier`
                    modifier           = Modifier
                        // continues the statement started above: `.size(44.dp)`
                        .size(44.dp)
                        // continues the statement started above: `.clip(RoundedCornerShape(RadiusThumbnail)),`
                        .clip(RoundedCornerShape(RadiusThumbnail)),
                    // continues the statement started above: `error = {`
                    error = {
                        // calls `ThumbnailBlock` with an argument list that continues on the following lines
                        ThumbnailBlock(
                            // continues the statement started above: `accentColor = WaypointPlaceAccent1,`
                            accentColor  = WaypointPlaceAccent1,
                            // continues the statement started above: `label = categoryEmoji(place.category),`
                            label        = categoryEmoji(place.category),
                            // continues the statement started above: `size = 44.dp,`
                            size         = 44.dp,
                            // continues the statement started above: `cornerRadius = RadiusThumbnail,`
                            cornerRadius = RadiusThumbnail,
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    },
                // closes the multi-line argument list started above
                )
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // calls `ThumbnailBlock` with an argument list that continues on the following lines
                ThumbnailBlock(
                    // continues the statement started above: `accentColor = WaypointPlaceAccent1,`
                    accentColor  = WaypointPlaceAccent1,
                    // continues the statement started above: `label = categoryEmoji(place.category),`
                    label        = categoryEmoji(place.category),
                    // continues the statement started above: `size = 44.dp,`
                    size         = 44.dp,
                    // continues the statement started above: `cornerRadius = RadiusThumbnail,`
                    cornerRadius = RadiusThumbnail,
                // closes the multi-line argument list started above
                )
            // closes the else branch
            }
            // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                // calls `Text` with arguments `(place.name, color = WaypointTextPrimary, fon…)`
                Text(place.name, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                // `if` statement: the block below runs when `!place.note.isNullOrBlank()` is true
                if (!place.note.isNullOrBlank()) {
                    // calls `Text` with arguments `(place.note, color = WaypointTextMuted, fontS…)`
                    Text(place.note, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                // closes the if block
                }
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.view_itinerary_tap_details),`
                    text     = stringResource(R.string.view_itinerary_tap_details),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color    = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
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
// closes the function `ViewPlaceCard`
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun ViewPlaceDetailOverlay(`
private fun ViewPlaceDetailOverlay(
    // continues the statement started above: `place: ViewPlaceItem,`
    place: ViewPlaceItem,
    // continues the statement started above: `onBack: () -> Unit,`
    onBack: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context   = LocalContext.current
    // declares read-only property `hasCoords`, initialised to `place.lat != null && place.lng != null`
    val hasCoords = place.lat != null && place.lng != null
    // declares read-only property `emoji`, initialised with the result of calling `categoryEmoji(…)`
    val emoji     = categoryEmoji(place.category)

    // declares mutable property `wiki`, delegated to `remember { mutableStateOf<WikipediaPlac…`
    var wiki by remember { mutableStateOf<WikipediaPlaceRepository.WikipediaSummary?>(null) }
    // declares mutable property `wikiLoading`, delegated to `remember { mutableStateOf(true) }`
    var wikiLoading by remember { mutableStateOf(true) }
    // declares mutable property `photoBitmap`, delegated to `remember { mutableStateOf<Bitmap?>(null…`
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    // calls `LaunchedEffect` with arguments `(place.id)` and opens a trailing lambda / block
    LaunchedEffect(place.id) {
        // assigns `wiki` the value `WikipediaPlaceRepository.getSummary(place.name)`
        wiki = WikipediaPlaceRepository.getSummary(place.name)
        // declares read-only property `urlToLoad`, initialised to `place.photoUrl?.takeIf { it.isNotBlank() }`
        val urlToLoad = place.photoUrl?.takeIf { it.isNotBlank() }
            // expression: `?: wiki?.thumbnailUrl?.takeIf { it.isNotBlank() }`
            ?: wiki?.thumbnailUrl?.takeIf { it.isNotBlank() }
        // `if` statement: the block below runs when `urlToLoad != null` is true
        if (urlToLoad != null) {
            // assigns `photoBitmap` the value `WikipediaPlaceRepository.downloadBitmap(urlToLoad)`
            photoBitmap = WikipediaPlaceRepository.downloadBitmap(urlToLoad)
        // closes the if block
        }
        // assigns `wikiLoading` the value `false`
        wikiLoading = false
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars),`
            .windowInsetsPadding(WindowInsets.systemBars),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.verticalScroll(rememberScrollState())`
                .verticalScroll(rememberScrollState())
                // continues the statement started above: `.padding(bottom = 96.dp),`
                .padding(bottom = 96.dp),
        // ends the argument list started above and opens the block that follows
        ) {

            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier         = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.height(280.dp)`
                    .height(280.dp)
                    // continues the statement started above: `.background(WaypointPlaceAccent2),`
                    .background(WaypointPlaceAccent2),
                // continues the statement started above: `contentAlignment = Alignment.Center,`
                contentAlignment = Alignment.Center,
            // ends the argument list started above and opens the block that follows
            ) {
                // `if` statement: the block below runs when `photoBitmap != null` is true
                if (photoBitmap != null) {
                    // calls `Image` with an argument list that continues on the following lines
                    Image(
                        // continues the statement started above: `bitmap = photoBitmap!!.asImageBitmap(),`
                        bitmap             = photoBitmap!!.asImageBitmap(),
                        // continues the statement started above: `contentDescription = place.name,`
                        contentDescription = place.name,
                        // continues the statement started above: `contentScale = ContentScale.Crop,`
                        contentScale       = ContentScale.Crop,
                        // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
                        modifier           = Modifier.fillMaxSize(),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                } else {
                    // calls `Text` with arguments `(text = emoji, fontSize = 88.sp)`
                    Text(text = emoji, fontSize = 88.sp)
                // closes the else branch
                }

                // calls `CircleIconButton` with an argument list that continues on the following lines
                CircleIconButton(
                    // continues the statement started above: `onClick = onBack,`
                    onClick  = onBack,
                    // continues the statement started above: `modifier = Modifier.align(Alignment.TopStart).padding(16.dp…`
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with arguments `(text = "←", color = WaypointTerracotta, font…)`
                    Text(text = "←", color = WaypointTerracotta, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                // closes the block
                }
            // closes the block
            }

            // calls `Column` with arguments `(modifier = Modifier.padding(horizontal = 22.…)` and opens a trailing lambda / block
            Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp)) {

                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(20.dp))`
                        .background(WaypointCard, RoundedCornerShape(20.dp))
                        // continues the statement started above: `.padding(horizontal = 12.dp, vertical = 5.dp),`
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = "$emoji ${place.category.lowercase().replaceFirstCha…`
                        text       = "$emoji  ${place.category.lowercase().replaceFirstChar { it.uppercaseChar() }}",
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color      = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 11.sp,`
                        fontSize   = 11.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }

                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = place.name,`
                    text       = place.name,
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color      = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 24.sp,`
                    fontSize   = 24.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                    // continues the statement started above: `modifier = Modifier.padding(top = 12.dp),`
                    modifier   = Modifier.padding(top = 12.dp),
                // closes the multi-line argument list started above
                )

                // `if` statement: the block below runs when `!place.note.isNullOrBlank()` is true
                if (!place.note.isNullOrBlank()) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = place.note,`
                        text     = place.note,
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color    = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `modifier = Modifier.padding(top = 6.dp),`
                        modifier = Modifier.padding(top = 6.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the if block
                }

                // declares read-only property `description`, initialised to `wiki?.extract?.takeIf { it.isNotBlank() }`
                val description = wiki?.extract?.takeIf { it.isNotBlank() }
                // `if` statement: the block below runs when `description != null` is true
                if (description != null) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = description,`
                        text       = description,
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color      = WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 14.sp,`
                        fontSize   = 14.sp,
                        // continues the statement started above: `lineHeight = 21.sp,`
                        lineHeight = 21.sp,
                        // continues the statement started above: `maxLines = 8,`
                        maxLines   = 8,
                        // continues the statement started above: `overflow = TextOverflow.Ellipsis,`
                        overflow   = TextOverflow.Ellipsis,
                        // continues the statement started above: `modifier = Modifier.padding(top = 16.dp),`
                        modifier   = Modifier.padding(top = 16.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the previous branch and opens an `else if` branch that runs when `!wikiLoading` is true
                } else if (!wikiLoading) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.view_itinerary_no_descriptio…`
                        text     = stringResource(R.string.view_itinerary_no_description),
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color    = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize = 13.sp,
                        // continues the statement started above: `modifier = Modifier.padding(top = 16.dp),`
                        modifier = Modifier.padding(top = 16.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the else-if branch
                }
            // closes the lambda passed to `Column`
            }
        // closes the block
        }

        // `if` statement: the block below runs when `hasCoords` is true
        if (hasCoords) {
            // calls `Box` with an argument list that continues on the following lines
            Box(
                // continues the statement started above: `modifier = Modifier`
                modifier         = Modifier
                    // continues the statement started above: `.align(Alignment.BottomCenter)`
                    .align(Alignment.BottomCenter)
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.background(WaypointCream)`
                    .background(WaypointCream)
                    // continues the statement started above: `.padding(horizontal = 22.dp, vertical = 16.dp),`
                    .padding(horizontal = 22.dp, vertical = 16.dp),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.background(WaypointTerracotta, RoundedCornerShape(RadiusBu…`
                        .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                        // continues the statement started above: `.clickable {`
                        .clickable {
                            // declares read-only property `uri`, initialised with the result of calling `Uri.parse(…)`
                            val uri = Uri.parse(
                                // continues the statement started above: `"https://www.google.com/maps/dir/?api=1" +`
                                "https://www.google.com/maps/dir/?api=1" +
                                // continues the statement started above: `"&destination=${place.lat},${place.lng}" +`
                                "&destination=${place.lat},${place.lng}" +
                                // continues the statement started above: `"&destination_place_id=${Uri.encode(place.name)}"`
                                "&destination_place_id=${Uri.encode(place.name)}"
                            // closes the multi-line argument list started above
                            )
                            // calls `startActivity` on `context` with arguments `(Intent(Intent.ACTION_VIEW, uri))`
                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                        // closes the block
                        }
                        // expression: `.padding(vertical = 16.dp),`
                        .padding(vertical = 16.dp),
                    // continues the statement started above: `contentAlignment = Alignment.Center,`
                    contentAlignment = Alignment.Center,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = stringResource(R.string.view_itinerary_open_maps),`
                        text       = stringResource(R.string.view_itinerary_open_maps),
                        // continues the statement started above: `color = androidx.compose.ui.graphics.Color.White,`
                        color      = androidx.compose.ui.graphics.Color.White,
                        // continues the statement started above: `fontSize = 15.sp,`
                        fontSize   = 15.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the block
            }
        // closes the if block
        }
    // closes the block
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `FlightStatusModal` showing live AirLabs status in an AlertDialog
private fun FlightStatusModal(
    // continues the statement started above: `status: FlightStatusState,`
    status: FlightStatusState,
    // continues the statement started above: `onDismiss: () -> Unit,`
    onDismiss: () -> Unit,
) {
    // builds the dialog title text from the current state
    val title = when (status) {
        is FlightStatusState.Loading -> "Fetching flight status…"
        is FlightStatusState.Success -> status.result.flightIata
        is FlightStatusState.Error   -> "Error"
        else                         -> ""
    }
    AlertDialog(
        onDismissRequest   = onDismiss,
        containerColor     = WaypointCard,
        title              = { Text(title, color = WaypointTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text               = {
            when (status) {
                is FlightStatusState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = WaypointTerracotta, modifier = Modifier.size(36.dp))
                    }
                }
                is FlightStatusState.Error -> {
                    Text(status.message, color = WaypointTextMuted, fontSize = 13.sp)
                }
                is FlightStatusState.Success -> {
                    val r = status.result
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // status badge
                        val (badgeColor, badgeLabel) = when (r.status.lowercase()) {
                            "active"    -> Pair(WaypointTerracotta,     "In Flight")
                            "landed"    -> Pair(WaypointPlaceAccent1,   "Landed")
                            "scheduled" -> Pair(WaypointPlaceAccent2,   "Scheduled")
                            "cancelled" -> Pair(WaypointTextMuted,      "Cancelled")
                            else        -> Pair(WaypointTextMuted,      r.status.replaceFirstChar { it.uppercaseChar() })
                        }
                        Box(
                            modifier         = Modifier
                                .background(badgeColor.copy(alpha = 0.15f), androidx.compose.foundation.shape.RoundedCornerShape(20.dp))
                                .padding(horizontal = 12.dp, vertical = 4.dp),
                        ) {
                            Text(badgeLabel, color = badgeColor, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Spacer(Modifier.height(12.dp))
                        HorizontalDivider(color = WaypointTextMuted.copy(alpha = 0.15f))
                        Spacer(Modifier.height(12.dp))
                        // departure row
                        if (r.depIata.isNotBlank()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Departure", color = WaypointTextMuted, fontSize = 11.sp)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(r.depIata, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    if (r.depTime.isNotBlank()) Text(r.depTime, color = WaypointTextMuted, fontSize = 11.sp)
                                    if (r.depTerminal.isNotBlank()) Text("Terminal ${r.depTerminal}", color = WaypointTextMuted, fontSize = 10.sp)
                                    if (r.depGate.isNotBlank())     Text("Gate ${r.depGate}", color = WaypointTextMuted, fontSize = 10.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        // arrival row
                        if (r.arrIata.isNotBlank()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Arrival", color = WaypointTextMuted, fontSize = 11.sp)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(r.arrIata, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    if (r.arrTime.isNotBlank()) Text(r.arrTime, color = WaypointTextMuted, fontSize = 11.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        // delay and duration
                        if (r.depDelayedMin > 0) {
                            Text("Delayed ${r.depDelayedMin} min", color = WaypointTerracotta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                        }
                        if (r.durationMin > 0) {
                            Text("Duration: ${r.durationMin / 60}h ${r.durationMin % 60}m", color = WaypointTextMuted, fontSize = 11.sp)
                        }
                    }
                }
                else -> {}
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = WaypointTerracotta, fontWeight = FontWeight.Bold)
            }
        },
    )
}


// declares private function `airlineLogoUrl` that derives a carrier logo URL from the flight number, or null if the code can't be determined
private fun airlineLogoUrl(flightNumber: String?): String? {
    // strip spaces, uppercase, take the first two characters (IATA carrier codes are always two characters)
    val code = flightNumber?.replace(" ", "")?.uppercase()?.take(2)?.takeIf { it.length == 2 } ?: return null
    return "https://pics.avs.io/100/100/$code.png"
}


// declares private function `categoryEmoji` taking 1 parameter (`category`), returning `String`; its body is the expression `when (category) {`
private fun categoryEmoji(category: String): String = when (category) {
    // lambda `"RESTAURANTS" -> "🍽️"`
    "RESTAURANTS" -> "🍽️"
    // lambda `"PARKS" -> "🌳"`
    "PARKS"       -> "🌳"
    // lambda `"PUBS" -> "🍺"`
    "PUBS"        -> "🍺"
    // lambda `"CINEMAS" -> "🎬"`
    "CINEMAS"     -> "🎬"
    // lambda `"HOTELS" -> "🏨"`
    "HOTELS"      -> "🏨"
    // `else` branch of the `when`: evaluates `"📍"`
    else          -> "📍"
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ViewSectionHeader` taking 2 parameters (`title`, `topPadding`) and opens its body
private fun ViewSectionHeader(title: String, topPadding: Dp = 0.dp) {
    // calls `Text` with an argument list that continues on the following lines
    Text(
        // continues the statement started above: `text = title,`
        text       = title,
        // continues the statement started above: `color = WaypointTextPrimary,`
        color      = WaypointTextPrimary,
        // continues the statement started above: `fontSize = 14.sp,`
        fontSize   = 14.sp,
        // continues the statement started above: `fontWeight = FontWeight.Bold,`
        fontWeight = FontWeight.Bold,
        // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = topPadding…`
        modifier   = Modifier.fillMaxWidth().padding(top = topPadding),
    // closes the multi-line argument list started above
    )
// closes the function `ViewSectionHeader`
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `ViewEmptyPlaceholder` taking 1 parameter (`label`) and opens its body
private fun ViewEmptyPlaceholder(label: String) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = Modifier`
        modifier         = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = 8.dp)`
            .padding(top = 8.dp)
            // continues the statement started above: `.background(WaypointCard, androidx.compose.foundation.shape…`
            .background(WaypointCard, androidx.compose.foundation.shape.RoundedCornerShape(com.waypoint.app.core.theme.RadiusRow))
            // continues the statement started above: `.padding(vertical = 16.dp),`
            .padding(vertical = 16.dp),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(text = label, color = WaypointTextMuted, fon…)`
        Text(text = label, color = WaypointTextMuted, fontSize = 12.sp)
    // closes the block
    }
// closes the function `ViewEmptyPlaceholder`
}


// declares private function `openPdf` taking 2 parameters (`context`, `pdfUri`) and opens its body
private fun openPdf(context: android.content.Context, pdfUri: String) {
    // declares read-only property `intent`, initialised with the result of calling `Intent(…)` and opens a lambda / block
    val intent = Intent(Intent.ACTION_VIEW).apply {
        // calls `setDataAndType` with arguments `(Uri.parse(pdfUri), "application/pdf")`
        setDataAndType(Uri.parse(pdfUri), "application/pdf")
        // calls `addFlags` with arguments `(Intent.FLAG_GRANT_READ_URI_PERMISSION)`
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    // closes the lambda assigned to `intent`
    }
    // `try` block: exceptions thrown inside are handled by the `catch` below
    try {
        // calls `startActivity` on `context` with arguments `(Intent.createChooser(intent, null))`
        context.startActivity(Intent.createChooser(intent, null))
    // `catch` block: handles a thrown `Exception` bound to `e`
    } catch (e: Exception) {
        // calls `makeText` on `Toast` with arguments `(context, R.string.view_itinerary_no_pdf_view…)`, then chains `.show()`
        Toast.makeText(context, R.string.view_itinerary_no_pdf_viewer, Toast.LENGTH_SHORT).show()
    // closes the catch block
    }
// closes the function `openPdf`
}

// declares private function `pdfLabel` taking 1 parameter (`uri`), returning `String` and opens its body
private fun pdfLabel(uri: String): String {
    // declares read-only property `decoded`, initialised with the result of calling `Uri.parse(…)`
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    // returns `decoded.substringAfterLast('/').ifEmpty { decoded }` from the current function
    return decoded.substringAfterLast('/').ifEmpty { decoded }
// closes the function `pdfLabel`
}
