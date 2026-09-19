// declares that this file belongs to the package `com.waypoint.app.features.edititinerary.ui`
package com.waypoint.app.features.edititinerary.ui

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
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.BoxWithConstraints` for use in this file
import androidx.compose.foundation.layout.BoxWithConstraints
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.PaddingValues` for use in this file
import androidx.compose.foundation.layout.PaddingValues
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
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.foundation.text.BasicTextField` for use in this file
import androidx.compose.foundation.text.BasicTextField
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
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `android.app.TimePickerDialog` for use in this file
import android.app.TimePickerDialog
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.TextStyle` for use in this file
import androidx.compose.ui.text.TextStyle
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
// imports `com.waypoint.app.core.common.StatusBadge` for use in this file
import com.waypoint.app.core.common.StatusBadge
// imports `com.waypoint.app.core.common.ThumbnailBlock` for use in this file
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
import com.waypoint.app.core.common.ThumbnailBlock
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.RadiusChip` for use in this file
import com.waypoint.app.core.theme.RadiusChip
// imports `com.waypoint.app.core.theme.RadiusThumbnail` for use in this file
import com.waypoint.app.core.theme.RadiusThumbnail
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
// imports `com.waypoint.app.core.theme.WaypointCard` for use in this file
import com.waypoint.app.core.theme.WaypointCard
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted
// imports `com.waypoint.app.core.theme.WaypointTextPrimary` for use in this file
import com.waypoint.app.core.theme.WaypointTextPrimary
// imports `com.waypoint.app.core.theme.WaypointTripBadgeText` for use in this file
import com.waypoint.app.core.theme.WaypointTripBadgeText

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun EditItineraryScreen(`
fun EditItineraryScreen(
    // continues the statement started above: `tripId: String,`
    tripId: String,
    // continues the statement started above: `onBackClick: () -> Unit,`
    onBackClick: () -> Unit,
    // continues the statement started above: `onAddPlaceClick: (category: String) -> Unit = {},`
    onAddPlaceClick: (category: String) -> Unit = {},
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `viewModel: EditItineraryViewModel = viewModel(),`
    viewModel: EditItineraryViewModel = viewModel(),
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares read-only property `state`, delegated to `viewModel.uiState.collectAsState()`
    val state by viewModel.uiState.collectAsState()

    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { viewModel.refreshPlacesForActiveDay() }

    // declares read-only property `pdfLauncher`, initialised with the result of calling `rememberLauncherForActivityResult(…)`
    val pdfLauncher = rememberLauncherForActivityResult(
        // continues the statement started above: `contract = ActivityResultContracts.OpenDocument(),`
        contract = ActivityResultContracts.OpenDocument(),
    // continues the statement started above: `) { uri: Uri? ->`
    ) { uri: Uri? ->
        // continues the statement started above: `if (uri == null) {`
        if (uri == null) {
            // calls `onPickerDismissed` on `viewModel` with arguments `()`
            viewModel.onPickerDismissed()
            // expression: `return@rememberLauncherForActivityResult`
            return@rememberLauncherForActivityResult
        // closes the block
        }
        // opens a block after `runCatching`
        runCatching {
            // calls `takePersistableUriPermission` on `context.contentResolver` with an argument list that continues on the following lines
            context.contentResolver.takePersistableUriPermission(
                // continues the statement started above: `uri,`
                uri,
                // continues the statement started above: `android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,`
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // declares read-only property `uriString`, initialised with the result of calling `uri.toString(…)`
        val uriString = uri.toString()
        // `when` expression on the value of `state.pendingUploadType`: the first matching branch below runs
        when (state.pendingUploadType) {
            // `when` branch: when the subject matches `ItineraryUploadType.FLIGHT`, evaluates `viewModel.onFlightPdfPicked(uriString)`
            ItineraryUploadType.FLIGHT     -> viewModel.onFlightPdfPicked(uriString)
            // `when` branch: when the subject matches `ItineraryUploadType.LODGING`, evaluates `viewModel.onLodgingPdfPicked(uriString)`
            ItineraryUploadType.LODGING    -> viewModel.onLodgingPdfPicked(uriString)
            // `when` branch: when the subject matches `ItineraryUploadType.CAR_RENTAL`, evaluates `viewModel.onCarRentalPdfPicked(uriStrin…`
            ItineraryUploadType.CAR_RENTAL -> viewModel.onCarRentalPdfPicked(uriString)
            // `when` branch: when the subject matches `null`, evaluates `viewModel.onPickerDismissed()`
            null                           -> viewModel.onPickerDismissed()
        // closes the when block
        }
    // closes the block
    }

    // calls `LaunchedEffect` with arguments `(state.pendingUploadType)` and opens a trailing lambda / block
    LaunchedEffect(state.pendingUploadType) {
        // `if` statement: the block below runs when `state.pendingUploadType != null` is true
        if (state.pendingUploadType != null) {
            // calls `launch` on `pdfLauncher` with arguments `(arrayOf("application/pdf"))`
            pdfLauncher.launch(arrayOf("application/pdf"))
        // closes the if block
        }
    // closes the lambda passed to `LaunchedEffect`
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
                onClick = onBackClick,
                // continues the statement started above: `modifier = Modifier.align(Alignment.CenterStart),`
                modifier = Modifier.align(Alignment.CenterStart),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.edit_itinerary_back_glyph),`
                    text = stringResource(R.string.edit_itinerary_back_glyph),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 15.sp,`
                    fontSize = 15.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier.align(Alignment.Center),`
                modifier = Modifier.align(Alignment.Center),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = stringResource(R.string.edit_itinerary_title),`
                    text = stringResource(R.string.edit_itinerary_title),
                    // continues the statement started above: `color = WaypointTextPrimary,`
                    color = WaypointTextPrimary,
                    // continues the statement started above: `fontSize = 16.sp,`
                    fontSize = 16.sp,
                    // continues the statement started above: `fontWeight = FontWeight.Bold,`
                    fontWeight = FontWeight.Bold,
                // closes the multi-line argument list started above
                )
                // `if` statement: the block below runs when `state.days.isNotEmpty()` is true
                if (state.days.isNotEmpty()) {
                    // declares read-only property `first`, initialised with the result of calling `state.days.first(…)`
                    val first = state.days.first().date
                    // declares read-only property `last`, initialised with the result of calling `state.days.last(…)`
                    val last  = state.days.last().date
                    // declares read-only property `subtitle`, initialised with the result of calling `if(…)`
                    val subtitle = if (first == last) first.toString()
                                   // expression: `else "${first} – ${last}"`
                                   else "${first} – ${last}"
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = subtitle,`
                        text = subtitle,
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 10.sp,`
                        fontSize = 10.sp,
                        // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                        modifier = Modifier.padding(top = 2.dp),
                    // closes the multi-line argument list started above
                    )
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
            // calls `DayScroller` with an argument list that continues on the following lines
            DayScroller(
                // continues the statement started above: `days = state.days,`
                days = state.days,
                // continues the statement started above: `activeDayIndex = state.activeDayIndex,`
                activeDayIndex = state.activeDayIndex,
                // continues the statement started above: `onDaySelected = viewModel::onDaySelected,`
                onDaySelected = viewModel::onDaySelected,
            // closes the multi-line argument list started above
            )
        // closes the if block
        }

        // calls `SectionHeader` with an argument list that continues on the following lines
        SectionHeader(
            // continues the statement started above: `title = stringResource(R.string.edit_itinerary_header_fligh…`
            title     = stringResource(R.string.edit_itinerary_header_flights),
            // continues the statement started above: `chipLabel = stringResource(R.string.edit_itinerary_upload_c…`
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            // continues the statement started above: `onChipClick = viewModel::onUploadFlightClick,`
            onChipClick = viewModel::onUploadFlightClick,
            // continues the statement started above: `topPadding = 20.dp,`
            topPadding  = 20.dp,
        // closes the multi-line argument list started above
        )

        // `if` statement: the block below runs when `state.flightsForActiveDay.isEmpty()` is true
        if (state.flightsForActiveDay.isEmpty()) {
            // calls `EmptyDocPlaceholder` with arguments `()`
            EmptyDocPlaceholder()
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `state.flightsForActiveDay.forEach { flight ->`
            state.flightsForActiveDay.forEach { flight ->
                // continues the statement started above: `FlightCard(`
                FlightCard(
                    // continues the statement started above: `flight = flight,`
                    flight         = flight,
                    // continues the statement started above: `onReplaceClick = viewModel::onUploadFlightClick,`
                    onReplaceClick = viewModel::onUploadFlightClick,
                    // continues the statement started above: `onDeleteClick = { viewModel.onDeleteFlight(flight.id) },`
                    onDeleteClick  = { viewModel.onDeleteFlight(flight.id) },
                    // continues the statement started above: `onNumberChanged = { num -> viewModel.onFlightNumberChanged(…`
                    onNumberChanged = { num -> viewModel.onFlightNumberChanged(flight.id, num) },
                    // continues the statement started above: `onTimeChanged = { time -> viewModel.onFlightDepartureTimeCh…`
                    onTimeChanged  = { time -> viewModel.onFlightDepartureTimeChanged(flight.id, time) },
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // calls `SectionHeader` with an argument list that continues on the following lines
        SectionHeader(
            // continues the statement started above: `title = stringResource(R.string.edit_itinerary_header_lodgi…`
            title     = stringResource(R.string.edit_itinerary_header_lodging),
            // continues the statement started above: `chipLabel = stringResource(R.string.edit_itinerary_upload_c…`
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            // continues the statement started above: `onChipClick = viewModel::onUploadLodgingClick,`
            onChipClick = viewModel::onUploadLodgingClick,
            // continues the statement started above: `topPadding = 20.dp,`
            topPadding  = 20.dp,
        // closes the multi-line argument list started above
        )

        // declares read-only property `lodgings`, initialised to `state.lodgingForActiveDay`
        val lodgings = state.lodgingForActiveDay
        // `if` statement: the block below runs when `lodgings.isEmpty()` is true
        if (lodgings.isEmpty()) {
            // calls `EmptyDocPlaceholder` with arguments `()`
            EmptyDocPlaceholder()
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `lodgings.forEach { lodging ->`
            lodgings.forEach { lodging ->
                // continues the statement started above: `DocCard(`
                DocCard(
                    // continues the statement started above: `accentColor = WaypointPlaceAccent2,`
                    accentColor   = WaypointPlaceAccent2,
                    // continues the statement started above: `title = lodging.docName ?: stringResource(R.string.edit_iti…`
                    title         = lodging.docName ?: stringResource(R.string.edit_itinerary_lodging_doc),
                    // continues the statement started above: `subtitle = "${lodging.fromDate} – ${lodging.toDate}",`
                    subtitle      = "${lodging.fromDate} – ${lodging.toDate}",
                    // continues the statement started above: `pdfUri = lodging.pdfUri,`
                    pdfUri        = lodging.pdfUri,
                    // continues the statement started above: `onDeleteClick = { viewModel.onDeleteLodging(lodging.id) },`
                    onDeleteClick = { viewModel.onDeleteLodging(lodging.id) },
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // calls `SectionHeader` with an argument list that continues on the following lines
        SectionHeader(
            // continues the statement started above: `title = stringResource(R.string.edit_itinerary_header_car),`
            title     = stringResource(R.string.edit_itinerary_header_car),
            // continues the statement started above: `chipLabel = stringResource(R.string.edit_itinerary_upload_c…`
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            // continues the statement started above: `onChipClick = viewModel::onUploadCarRentalClick,`
            onChipClick = viewModel::onUploadCarRentalClick,
            // continues the statement started above: `topPadding = 20.dp,`
            topPadding  = 20.dp,
        // closes the multi-line argument list started above
        )

        // declares read-only property `cars`, initialised to `state.carRentalsForActiveDay`
        val cars = state.carRentalsForActiveDay
        // `if` statement: the block below runs when `cars.isEmpty()` is true
        if (cars.isEmpty()) {
            // calls `EmptyDocPlaceholder` with arguments `()`
            EmptyDocPlaceholder()
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `cars.forEach { car ->`
            cars.forEach { car ->
                // continues the statement started above: `DocCard(`
                DocCard(
                    // continues the statement started above: `accentColor = WaypointPlaceAccent3,`
                    accentColor   = WaypointPlaceAccent3,
                    // continues the statement started above: `title = car.docName ?: stringResource(R.string.edit_itinera…`
                    title         = car.docName ?: stringResource(R.string.edit_itinerary_car_doc),
                    // continues the statement started above: `subtitle = "${car.fromDate} – ${car.toDate}",`
                    subtitle      = "${car.fromDate} – ${car.toDate}",
                    // continues the statement started above: `pdfUri = car.pdfUri,`
                    pdfUri        = car.pdfUri,
                    // continues the statement started above: `onDeleteClick = { viewModel.onDeleteCarRental(car.id) },`
                    onDeleteClick = { viewModel.onDeleteCarRental(car.id) },
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // declares read-only property `foodItems`, initialised to `state.placesForActiveDay["RESTAURANTS"].orEm…`
        val foodItems = state.placesForActiveDay["RESTAURANTS"].orEmpty()
        // calls `SectionHeader` with an argument list that continues on the following lines
        SectionHeader(
            // continues the statement started above: `title = stringResource(R.string.edit_itinerary_header_food),`
            title     = stringResource(R.string.edit_itinerary_header_food),
            // continues the statement started above: `chipLabel = stringResource(R.string.edit_itinerary_add_chip…`
            chipLabel = stringResource(R.string.edit_itinerary_add_chip),
            // continues the statement started above: `onChipClick = {`
            onChipClick = {
                // declares read-only property `dayId`, initialised with the result of calling `state.days.getOrNull(…)`
                val dayId = state.days.getOrNull(state.activeDayIndex)?.dayId
                // `if` statement: executes `onAddPlaceClick("RESTAURANTS")` when `dayId != null` is true
                if (dayId != null) onAddPlaceClick("RESTAURANTS")
            // closes the block
            },
            // continues the statement started above: `topPadding = 20.dp,`
            topPadding  = 20.dp,
        // closes the multi-line argument list started above
        )
        // `if` statement: the block below runs when `foodItems.isEmpty()` is true
        if (foodItems.isEmpty()) {
            // calls `EmptyDocPlaceholder` with arguments `(label = stringResource(R.string.edit_itinera…)`
            EmptyDocPlaceholder(label = stringResource(R.string.edit_itinerary_no_places))
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // expression: `foodItems.forEach { place ->`
            foodItems.forEach { place ->
                // continues the statement started above: `PlaceCard(`
                PlaceCard(
                    // continues the statement started above: `place = place,`
                    place         = place,
                    // continues the statement started above: `onDeleteClick = { viewModel.onDeletePlace(place.id) },`
                    onDeleteClick = { viewModel.onDeletePlace(place.id) },
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the else branch
        }

        // declares read-only property `placeCategories`, initialised with the result of calling `listOf(…)`
        val placeCategories = listOf(
            // continues the statement started above: `"PARKS" to stringResource(R.string.edit_itinerary_header_pa…`
            "PARKS"   to stringResource(R.string.edit_itinerary_header_parks),
            // continues the statement started above: `"PUBS" to stringResource(R.string.edit_itinerary_header_pub…`
            "PUBS"    to stringResource(R.string.edit_itinerary_header_pubs),
            // continues the statement started above: `"CINEMAS" to stringResource(R.string.edit_itinerary_header_…`
            "CINEMAS" to stringResource(R.string.edit_itinerary_header_cinemas),
        // closes the multi-line argument list started above
        )
        // expression: `placeCategories.forEach { (categoryKey, header) ->`
        placeCategories.forEach { (categoryKey, header) ->
            // continues the statement started above: `val items = state.placesForActiveDay[categoryKey].orEmpty()`
            val items = state.placesForActiveDay[categoryKey].orEmpty()
            // calls `SectionHeader` with an argument list that continues on the following lines
            SectionHeader(
                // continues the statement started above: `title = header,`
                title       = header,
                // continues the statement started above: `chipLabel = stringResource(R.string.edit_itinerary_add_chip…`
                chipLabel   = stringResource(R.string.edit_itinerary_add_chip),
                // continues the statement started above: `onChipClick = {`
                onChipClick = {
                    // declares read-only property `dayId`, initialised with the result of calling `state.days.getOrNull(…)`
                    val dayId = state.days.getOrNull(state.activeDayIndex)?.dayId
                    // `if` statement: executes `onAddPlaceClick(categoryKey)` when `dayId != null` is true
                    if (dayId != null) onAddPlaceClick(categoryKey)
                // closes the block
                },
                // continues the statement started above: `topPadding = 20.dp,`
                topPadding  = 20.dp,
            // closes the multi-line argument list started above
            )
            // `if` statement: the block below runs when `items.isEmpty()` is true
            if (items.isEmpty()) {
                // calls `EmptyDocPlaceholder` with arguments `(label = stringResource(R.string.edit_itinera…)`
                EmptyDocPlaceholder(label = stringResource(R.string.edit_itinerary_no_places))
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // expression: `items.forEach { place ->`
                items.forEach { place ->
                    // continues the statement started above: `PlaceCard(`
                    PlaceCard(
                        // continues the statement started above: `place = place,`
                        place        = place,
                        // continues the statement started above: `onDeleteClick = { viewModel.onDeletePlace(place.id) },`
                        onDeleteClick = { viewModel.onDeletePlace(place.id) },
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
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
// expression: `private fun DayScroller(`
private fun DayScroller(
    // continues the statement started above: `days: List<DayItem>,`
    days: List<DayItem>,
    // continues the statement started above: `activeDayIndex: Int,`
    activeDayIndex: Int,
    // continues the statement started above: `onDaySelected: (Int) -> Unit,`
    onDaySelected: (Int) -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `BoxWithConstraints` with arguments `(modifier = Modifier.fillMaxWidth())` and opens a trailing lambda / block
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        // declares read-only property `cardWidth` of type `Dp`, initialised to `(maxWidth - 16.dp) / 3`
        val cardWidth: Dp  = (maxWidth - 16.dp) / 3
        // declares read-only property `listState`, initialised with the result of calling `rememberLazyListState(…)`
        val listState      = rememberLazyListState()

        // calls `LazyRow` with an argument list that continues on the following lines
        LazyRow(
            // continues the statement started above: `state = listState,`
            state            = listState,
            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
            modifier         = Modifier.fillMaxWidth(),
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
                    // continues the statement started above: `modifier = Modifier`
                    modifier      = Modifier
                        // continues the statement started above: `.width(cardWidth)`
                        .width(cardWidth)
                        // continues the statement started above: `.clickable { onDaySelected(index) },`
                        .clickable { onDaySelected(index) },
                    // continues the statement started above: `cornerRadius = RadiusButton,`
                    cornerRadius  = RadiusButton,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier`
                        modifier             = Modifier
                            // continues the statement started above: `.fillMaxWidth()`
                            .fillMaxWidth()
                            // continues the statement started above: `.background(if (isActive) WaypointTerracotta.copy(alpha = 0…`
                            .background(if (isActive) WaypointTerracotta.copy(alpha = 0.08f) else Color.Transparent)
                            // continues the statement started above: `.padding(vertical = 12.dp),`
                            .padding(vertical = 12.dp),
                        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                        horizontalAlignment  = Alignment.CenterHorizontally,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.dayOfWeek.name.take(3)`
                            text      = day.date.dayOfWeek.name.take(3)
                                // continues the statement started above: `.lowercase()`
                                .lowercase()
                                // continues the statement started above: `.replaceFirstChar { it.uppercaseChar() },`
                                .replaceFirstChar { it.uppercaseChar() },
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextM…`
                            color     = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            // continues the statement started above: `fontSize = 9.sp,`
                            fontSize  = 9.sp,
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.dayOfMonth.toString(),`
                            text      = day.date.dayOfMonth.toString(),
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextP…`
                            color     = if (isActive) WaypointTerracotta else WaypointTextPrimary,
                            // continues the statement started above: `fontSize = 20.sp,`
                            fontSize  = 20.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                            modifier  = Modifier.padding(top = 2.dp),
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = day.date.month.name`
                            text      = day.date.month.name
                                // continues the statement started above: `.lowercase()`
                                .lowercase()
                                // continues the statement started above: `.replaceFirstChar { it.uppercaseChar() }`
                                .replaceFirstChar { it.uppercaseChar() }
                                // continues the statement started above: `.take(3),`
                                .take(3),
                            // continues the statement started above: `color = if (isActive) WaypointTerracotta else WaypointTextM…`
                            color     = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            // continues the statement started above: `fontSize = 9.sp,`
                            fontSize  = 9.sp,
                            // continues the statement started above: `modifier = Modifier.padding(top = 2.dp),`
                            modifier  = Modifier.padding(top = 2.dp),
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
// expression: `private fun FlightCard(`
private fun FlightCard(
    // continues the statement started above: `flight: FlightItem,`
    flight: FlightItem,
    // continues the statement started above: `onReplaceClick: () -> Unit,`
    onReplaceClick: () -> Unit,
    // continues the statement started above: `onDeleteClick: () -> Unit,`
    onDeleteClick: () -> Unit,
    // continues the statement started above: `onNumberChanged: (String) -> Unit,`
    onNumberChanged: (String) -> Unit,
    // continues the statement started above: `onTimeChanged: (String) -> Unit,`
    onTimeChanged: (String) -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares read-only property `openTimePicker` and opens a lambda / block
    val openTimePicker = {
        // calls `val` with arguments `(h, m)`, then chains `.departureTime`, `.?.split(":")`, `.?.map { it.toInt() } ?: listO…`
        val (h, m) = flight.departureTime?.split(":")?.map { it.toInt() } ?: listOf(9, 0)
        // lambda `TimePickerDialog(context, { _… -> onTimeChanged("%02d:%02d".for…`
        TimePickerDialog(context, { _, hour, minute -> onTimeChanged("%02d:%02d".format(hour, minute)) }, h, m, true).show()
    // closes the lambda assigned to `openTimePicker`
    }
    // calls `RowSurface` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 10.dp, end…`
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // shows airline logo if a carrier code can be derived, falling back to the accent block
            val logoUrl = airlineLogoUrl(flight.flightNumber)
            if (logoUrl != null) {
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(LocalContext.current).data(logoUrl).crossfade(true).build(),
                    contentDescription = flight.flightNumber,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier.size(44.dp).clip(RoundedCornerShape(RadiusThumbnail)).background(Color.White),
                    loading = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                    error   = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                )
            } else {
                ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail)
            }
            // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                // calls `BasicTextField` with an argument list that continues on the following lines
                BasicTextField(
                    // continues the statement started above: `value = flight.flightNumber,`
                    value         = flight.flightNumber,
                    // continues the statement started above: `onValueChange = onNumberChanged,`
                    onValueChange = onNumberChanged,
                    // continues the statement started above: `textStyle = TextStyle(`
                    textStyle     = TextStyle(
                        // continues the statement started above: `color = WaypointTextPrimary,`
                        color      = WaypointTextPrimary,
                        // continues the statement started above: `fontSize = 13.sp,`
                        fontSize   = 13.sp,
                        // continues the statement started above: `fontWeight = FontWeight.Bold,`
                        fontWeight = FontWeight.Bold,
                    // closes the multi-line argument list started above
                    ),
                    // continues the statement started above: `decorationBox = { inner ->`
                    decorationBox = { inner ->
                        // continues the statement started above: `if (flight.flightNumber.isEmpty()) {`
                        if (flight.flightNumber.isEmpty()) {
                            // calls `Text` with an argument list that continues on the following lines
                            Text(
                                // continues the statement started above: `text = stringResource(R.string.edit_itinerary_flight_number…`
                                text  = stringResource(R.string.edit_itinerary_flight_number_hint),
                                // continues the statement started above: `color = WaypointTextMuted,`
                                color = WaypointTextMuted,
                                // continues the statement started above: `fontSize = 13.sp,`
                                fontSize = 13.sp,
                            // closes the multi-line argument list started above
                            )
                        // closes the block
                        }
                        // calls `inner` with arguments `()`
                        inner()
                    // closes the block
                    },
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = flight.docName ?: pdfFileName(flight.pdfUri),`
                    text     = flight.docName ?: pdfFileName(flight.pdfUri),
                    // continues the statement started above: `color = WaypointTripBadgeText,`
                    color    = WaypointTripBadgeText,
                    // continues the statement started above: `fontSize = 9.sp,`
                    fontSize = 9.sp,
                    // continues the statement started above: `modifier = Modifier.padding(top = 3.dp).alpha(0.8f),`
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = flight.departureTime?.let { stringResource(R.string.…`
                    text     = flight.departureTime?.let { stringResource(R.string.edit_itinerary_flight_departs, it) }
                               // continues the statement started above: `?: stringResource(R.string.edit_itinerary_flight_set_time),`
                               ?: stringResource(R.string.edit_itinerary_flight_set_time),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color    = WaypointTerracotta,
                    // continues the statement started above: `fontSize = 10.sp,`
                    fontSize = 10.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `modifier = Modifier.padding(top = 3.dp).clickable(onClick =…`
                    modifier = Modifier.padding(top = 3.dp).clickable(onClick = openTimePicker),
                // closes the multi-line argument list started above
                )
            // closes the lambda passed to `Column`
            }
            // opens a block after `Row`
            Row {
                // calls `IconActionButton` with an argument list that continues on the following lines
                IconActionButton(
                    // continues the statement started above: `glyph = stringResource(R.string.edit_itinerary_undo_glyph),`
                    glyph              = stringResource(R.string.edit_itinerary_undo_glyph),
                    // continues the statement started above: `contentDescription = stringResource(R.string.edit_itinerary…`
                    contentDescription = stringResource(R.string.edit_itinerary_replace_flight_doc_cd),
                    // continues the statement started above: `color = WaypointTerracotta,`
                    color              = WaypointTerracotta,
                    // continues the statement started above: `onClick = onReplaceClick,`
                    onClick            = onReplaceClick,
                // closes the multi-line argument list started above
                )
                // calls `IconActionButton` with an argument list that continues on the following lines
                IconActionButton(
                    // continues the statement started above: `glyph = stringResource(R.string.edit_itinerary_close_glyph),`
                    glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                    // continues the statement started above: `contentDescription = stringResource(R.string.edit_itinerary…`
                    contentDescription = stringResource(R.string.edit_itinerary_remove_flight_cd),
                    // continues the statement started above: `color = WaypointTextMuted,`
                    color              = WaypointTextMuted,
                    // continues the statement started above: `onClick = onDeleteClick,`
                    onClick            = onDeleteClick,
                    // continues the statement started above: `modifier = Modifier.padding(start = 6.dp),`
                    modifier           = Modifier.padding(start = 6.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the lambda passed to `RowSurface`
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun DocCard(`
private fun DocCard(
    // continues the statement started above: `accentColor: Color,`
    accentColor: Color,
    // continues the statement started above: `title: String,`
    title: String,
    // continues the statement started above: `subtitle: String,`
    subtitle: String,
    // continues the statement started above: `pdfUri: String,`
    pdfUri: String,
    // continues the statement started above: `onDeleteClick: () -> Unit,`
    onDeleteClick: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `RowSurface` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
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
            // calls `IconActionButton` with an argument list that continues on the following lines
            IconActionButton(
                // continues the statement started above: `glyph = stringResource(R.string.edit_itinerary_close_glyph),`
                glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                // continues the statement started above: `contentDescription = stringResource(R.string.edit_itinerary…`
                contentDescription = stringResource(R.string.edit_itinerary_remove_doc_cd),
                // continues the statement started above: `color = WaypointTextMuted,`
                color              = WaypointTextMuted,
                // continues the statement started above: `onClick = onDeleteClick,`
                onClick            = onDeleteClick,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the lambda passed to `RowSurface`
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun EmptyDocPlaceholder(`
private fun EmptyDocPlaceholder(
    // continues the statement started above: `label: String = stringResource(R.string.edit_itinerary_no_p…`
    label: String = stringResource(R.string.edit_itinerary_no_pdf_uploaded),
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.padding(top = 8.dp)`
            .padding(top = 8.dp)
            // continues the statement started above: `.background(WaypointCard, RoundedCornerShape(com.waypoint.a…`
            .background(WaypointCard, RoundedCornerShape(com.waypoint.app.core.theme.RadiusRow))
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
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun PlaceCard(`
private fun PlaceCard(
    // continues the statement started above: `place: PlaceItem,`
    place: PlaceItem,
    // continues the statement started above: `onDeleteClick: () -> Unit,`
    onDeleteClick: () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `RowSurface` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        // calls `Row` with an argument list that continues on the following lines
        Row(
            // continues the statement started above: `modifier = Modifier.padding(start = 10.dp, top = 10.dp, end…`
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
            verticalAlignment = Alignment.CenterVertically,
        // ends the argument list started above and opens the block that follows
        ) {
            // show place photo if available, otherwise fall back to accent block
            val placePhotoUrl = place.photoUrl
            if (placePhotoUrl != null) {
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(LocalContext.current).data(placePhotoUrl).crossfade(true).build(),
                    contentDescription = place.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.size(44.dp).clip(RoundedCornerShape(RadiusThumbnail)),
                    loading = { ThumbnailBlock(accentColor = WaypointPlaceAccent1, size = 44.dp, cornerRadius = RadiusThumbnail) },
                    error   = { ThumbnailBlock(accentColor = WaypointPlaceAccent1, size = 44.dp, cornerRadius = RadiusThumbnail) },
                )
            } else {
                ThumbnailBlock(
                    accentColor  = WaypointPlaceAccent1,
                    size         = 44.dp,
                    cornerRadius = RadiusThumbnail,
                )
            }
            // calls `Column` with arguments `(modifier = Modifier.weight(1f).padding(start…)` and opens a trailing lambda / block
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
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
                // `if` statement: the block below runs when `!place.note.isNullOrBlank()` is true
                if (!place.note.isNullOrBlank()) {
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = place.note,`
                        text     = place.note,
                        // continues the statement started above: `color = WaypointTextMuted,`
                        color    = WaypointTextMuted,
                        // continues the statement started above: `fontSize = 11.sp,`
                        fontSize = 11.sp,
                        // continues the statement started above: `modifier = Modifier.padding(top = 3.dp),`
                        modifier = Modifier.padding(top = 3.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the if block
                }
            // closes the lambda passed to `Column`
            }
            // calls `IconActionButton` with an argument list that continues on the following lines
            IconActionButton(
                // continues the statement started above: `glyph = stringResource(R.string.edit_itinerary_close_glyph),`
                glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                // continues the statement started above: `contentDescription = stringResource(R.string.edit_itinerary…`
                contentDescription = stringResource(R.string.edit_itinerary_remove_place_cd),
                // continues the statement started above: `color = WaypointTextMuted,`
                color              = WaypointTextMuted,
                // continues the statement started above: `onClick = onDeleteClick,`
                onClick            = onDeleteClick,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the lambda passed to `RowSurface`
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun SectionHeader(`
private fun SectionHeader(
    // continues the statement started above: `title: String,`
    title: String,
    // continues the statement started above: `chipLabel: String,`
    chipLabel: String,
    // continues the statement started above: `onChipClick: () -> Unit,`
    onChipClick: () -> Unit,
    // continues the statement started above: `topPadding: Dp = 0.dp,`
    topPadding: Dp = 0.dp,
// ends the argument list started above and opens the block that follows
) {
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = Modifier.fillMaxWidth().padding(top = topPadding…`
        modifier          = Modifier.fillMaxWidth().padding(top = topPadding),
        // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
        verticalAlignment = Alignment.CenterVertically,
    // ends the argument list started above and opens the block that follows
    ) {
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
            // continues the statement started above: `modifier = Modifier.weight(1f),`
            modifier   = Modifier.weight(1f),
        // closes the multi-line argument list started above
        )
        // calls `StatusBadge` with an argument list that continues on the following lines
        StatusBadge(
            // continues the statement started above: `text = chipLabel,`
            text             = chipLabel,
            // continues the statement started above: `fillColor = null,`
            fillColor        = null,
            // continues the statement started above: `borderColor = WaypointTerracotta,`
            borderColor      = WaypointTerracotta,
            // continues the statement started above: `textColor = WaypointTerracotta,`
            textColor        = WaypointTerracotta,
            // continues the statement started above: `cornerRadius = RadiusChip,`
            cornerRadius     = RadiusChip,
            // continues the statement started above: `contentPadding = PaddingValues(horizontal = 10.dp, vertical…`
            contentPadding   = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            // continues the statement started above: `modifier = Modifier.clickable(onClick = onChipClick),`
            modifier         = Modifier.clickable(onClick = onChipClick),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the block
}


// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun IconActionButton(`
private fun IconActionButton(
    // continues the statement started above: `glyph: String,`
    glyph: String,
    // continues the statement started above: `contentDescription: String,`
    contentDescription: String,
    // continues the statement started above: `color: Color,`
    color: Color,
    // continues the statement started above: `onClick: () -> Unit,`
    onClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier         = modifier
            // continues the statement started above: `.size(28.dp)`
            .size(28.dp)
            // continues the statement started above: `.clickable(onClick = onClick),`
            .clickable(onClick = onClick),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `(text = glyph, color = color, fontSize = 12.s…)`
        Text(text = glyph, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    // closes the block
    }
// closes the block
}


// derives a carrier logo URL from the flight number prefix, or null if the code can't be determined
private fun airlineLogoUrl(flightNumber: String): String? {
    val code = flightNumber.replace(" ", "").uppercase().take(2).takeIf { it.length == 2 } ?: return null
    return "https://pics.avs.io/100/100/$code.png"
}

// declares private function `pdfFileName` taking 1 parameter (`uri`), returning `String` and opens its body
private fun pdfFileName(uri: String): String {
    // declares read-only property `decoded`, initialised with the result of calling `Uri.parse(…)`
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    // returns `decoded.substringAfterLast('/').ifEmpty { decoded }` from the current function
    return decoded.substringAfterLast('/').ifEmpty { decoded }
// closes the function `pdfFileName`
}
