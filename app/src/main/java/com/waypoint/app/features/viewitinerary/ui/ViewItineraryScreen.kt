package com.waypoint.app.features.viewitinerary.ui

import com.waypoint.app.core.theme.White
import android.content.Intent
import android.widget.Toast
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import coil.compose.AsyncImage
import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.ui.graphics.asImageBitmap
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import com.waypoint.app.core.theme.RadiusHero
import com.waypoint.app.core.theme.WaypointDecoText
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.common.CardSurface
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.common.RowSurface
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointPlaceAccent4
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.WaypointTripBadgeText
import com.waypoint.app.features.placepicker.data.WikipediaPlaceRepository
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource

/**
 * Read-only view of a trip's itinerary, day by day, with live flight status and links to open booking
 * PDFs.
 */
@Composable
fun ViewItineraryScreen(
    tripId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewItineraryViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    state.selectedPlace?.let { place ->
        ViewPlaceDetailOverlay(
            place      = place,
            onBack     = viewModel::onPlaceDismissed,
        )
        return
    }

    val flightStatus = state.flightStatus
    if (flightStatus !is FlightStatusState.Idle) {
        FlightStatusModal(
            status    = flightStatus,
            onDismiss = viewModel::onFlightStatusDismissed,
        )
    }

    if (state.isLoading) {
        Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = WaypointTerracotta)
        }
        return
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(
                onClick  = onBackClick,
                // Read out by TalkBack
                contentDescription = stringResource(R.string.view_itinerary_back_cd),
                modifier = Modifier.align(Alignment.CenterStart),
                size = 40.dp,
                fillColor = WaypointTerracotta,
                borderColor = null,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_back_arrow),
                    contentDescription = null,
                    tint = WaypointCard,
                    modifier = Modifier.size(18.dp),
                )
            }
            Column(
                modifier            = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text       = stringResource(R.string.view_itinerary_title),
                    color      = WaypointTextPrimary,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (state.days.isNotEmpty()) {
                    val first    = state.days.first().date
                    val last     = state.days.last().date
                    val subtitle = if (first == last) "$first" else "$first – $last"
                    Text(text = subtitle, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 2.dp))
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        if (state.days.isNotEmpty()) {
            ViewDayScroller(
                days           = state.days,
                activeDayIndex = state.activeDayIndex,
                onDaySelected  = viewModel::onDaySelected,
            )
        }

        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_flights), topPadding = 20.dp)
        if (state.flightsForActiveDay.isEmpty()) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_flights))
        } else {
            state.flightsForActiveDay.forEach { f ->
                ViewFlightCard(
                    flight        = f,
                    onStatusClick = {
                        val fn = f.flightNumber?.takeIf { it.isNotBlank() }
                        if (fn != null) viewModel.onFlightStatusTap(fn)
                    },
                )
            }
        }

        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_lodging), topPadding = 20.dp)
        val lodgings = state.lodgingForActiveDay
        if (lodgings.isEmpty()) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_lodging))
        } else {
            lodgings.forEach { lodging ->
                ViewDocCard(
                    accentColor = WaypointPlaceAccent2,
                    title       = lodging.docName ?: stringResource(R.string.edit_itinerary_lodging_doc),
                    subtitle    = "${lodging.fromDate} – ${lodging.toDate}",
                    pdfUri      = lodging.pdfUri,
                )
            }
        }

        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_car), topPadding = 20.dp)
        val cars = state.carRentalsForActiveDay
        if (cars.isEmpty()) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_car))
        } else {
            cars.forEach { car ->
                ViewDocCard(
                    accentColor = WaypointPlaceAccent3,
                    title       = car.docName ?: stringResource(R.string.edit_itinerary_car_doc),
                    subtitle    = "${car.fromDate} – ${car.toDate}",
                    pdfUri      = car.pdfUri,
                )
            }
        }

        listOf(
            "RESTAURANTS" to stringResource(R.string.edit_itinerary_header_food),
            "PARKS"       to stringResource(R.string.edit_itinerary_header_parks),
            "PUBS"    to stringResource(R.string.edit_itinerary_header_pubs),
            "CINEMAS" to stringResource(R.string.edit_itinerary_header_cinemas),
        ).forEach { (key, header) ->
            val items = state.placesForActiveDay[key].orEmpty()
            ViewSectionHeader(title = header, topPadding = 20.dp)
            if (items.isEmpty()) {
                ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_places))
            } else {
                items.forEach { place -> ViewPlaceCard(place = place, onTap = { viewModel.onPlaceSelected(place) }) }
            }
        }
    }
}


@Composable
private fun ViewDayScroller(
    days: List<ViewDayItem>,
    activeDayIndex: Int,
    onDaySelected: (Int) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth = (maxWidth - 16.dp) / 3
        LazyRow(
            state                 = rememberLazyListState(),
            modifier              = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(days) { index, day ->
                val isActive = index == activeDayIndex
                CardSurface(
                    modifier     = Modifier.width(cardWidth).clickable { onDaySelected(index) },
                    cornerRadius = RadiusButton,
                ) {
                    Column(
                        modifier            = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) WaypointTerracotta.copy(alpha = 0.08f) else Color.Transparent)
                            .padding(vertical = 12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text     = day.date.dayOfWeek.name.take(3).lowercase().replaceFirstChar { it.uppercaseChar() },
                            color    = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            fontSize = 9.sp,
                        )
                        Text(
                            text       = day.date.dayOfMonth.toString(),
                            color      = if (isActive) WaypointTerracotta else WaypointTextPrimary,
                            fontSize   = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier   = Modifier.padding(top = 2.dp),
                        )
                        Text(
                            text     = day.date.month.name.lowercase().replaceFirstChar { it.uppercaseChar() }.take(3),
                            color    = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            fontSize = 9.sp,
                            modifier = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun ViewFlightCard(flight: ViewFlightItem, onStatusClick: () -> Unit) {
    val context = LocalContext.current
    RowSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { openPdf(context, flight.pdfUri) },
    ) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val logoUrl = airlineLogoUrl(flight.flightNumber)
            if (logoUrl != null) {
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(context).data(logoUrl).crossfade(true).build(),
                    contentDescription = flight.flightNumber,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(RadiusThumbnail))
                        .background(White),
                    loading = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                    error   = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                )
            } else {
                ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail)
            }
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                val label = flight.flightNumber?.takeIf { it.isNotBlank() } ?: stringResource(R.string.reminder_flight_unnamed)
                Text(
                    text       = label,
                    color      = WaypointTerracotta,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.clickable(enabled = flight.flightNumber?.isNotBlank() == true) { onStatusClick() },
                )
                val timeText = flight.arrivalTime?.let { "Arrives $it" }
                    ?: flight.departureTime?.let { stringResource(R.string.edit_itinerary_flight_departs, it) }
                    ?: stringResource(R.string.view_itinerary_flight_no_time)
                Text(
                    text     = timeText,
                    color    = WaypointTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 3.dp),
                )
                Text(
                    text     = flight.docName ?: pdfLabel(flight.pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
            }
        }
    }
}


@Composable
private fun ViewDocCard(
    accentColor: Color,
    title: String,
    subtitle: String,
    pdfUri: String,
) {
    val context = LocalContext.current
    RowSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { openPdf(context, pdfUri) },
    ) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(accentColor = accentColor, size = 44.dp, cornerRadius = RadiusThumbnail)
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(title, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
            }
        }
    }
}


@Composable
private fun ViewPlaceCard(place: ViewPlaceItem, onTap: () -> Unit) {
    RowSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .clickable { onTap() },
    ) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (!place.photoUrl.isNullOrBlank()) {
                val ctx = LocalContext.current
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(ctx).data(place.photoUrl).crossfade(true).build(),
                    contentDescription = place.name,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(RadiusThumbnail)),
                    error = {
                        ThumbnailBlock(
                            accentColor  = WaypointPlaceAccent1,
                            label        = categoryEmoji(place.category),
                            size         = 44.dp,
                            cornerRadius = RadiusThumbnail,
                        )
                    },
                )
            } else {
                ThumbnailBlock(
                    accentColor  = WaypointPlaceAccent1,
                    label        = categoryEmoji(place.category),
                    size         = 44.dp,
                    cornerRadius = RadiusThumbnail,
                )
            }
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(place.name, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                if (!place.note.isNullOrBlank()) {
                    Text(place.note, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                }
                Text(
                    text     = stringResource(R.string.view_itinerary_tap_details),
                    color    = WaypointTerracotta,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}


@Composable
private fun ViewPlaceDetailOverlay(
    place: ViewPlaceItem,
    onBack: () -> Unit,
) {
    val context   = LocalContext.current
    val hasCoords = place.lat != null && place.lng != null
    val emoji     = categoryEmoji(place.category)

    var wiki by remember { mutableStateOf<WikipediaPlaceRepository.WikipediaSummary?>(null) }
    var wikiLoading by remember { mutableStateOf(true) }
    var photoBitmap by remember { mutableStateOf<Bitmap?>(null) }
    LaunchedEffect(place.id) {
        wiki = WikipediaPlaceRepository.getSummary(place.name)
        val urlToLoad = place.photoUrl?.takeIf { it.isNotBlank() }
            ?: wiki?.thumbnailUrl?.takeIf { it.isNotBlank() }
        if (urlToLoad != null) {
            photoBitmap = WikipediaPlaceRepository.downloadBitmap(urlToLoad)
        }
        wikiLoading = false
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(bottom = 96.dp),
        ) {

            Box(
                modifier         = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
                    .background(WaypointPlaceAccent2),
                contentAlignment = Alignment.Center,
            ) {
                if (photoBitmap != null) {
                    Image(
                        bitmap             = photoBitmap!!.asImageBitmap(),
                        contentDescription = place.name,
                        contentScale       = ContentScale.Crop,
                        modifier           = Modifier.fillMaxSize(),
                    )
                } else {
                    Text(text = emoji, fontSize = 88.sp)
                }

                CircleIconButton(
                    onClick  = onBack,
                    // Read out by TalkBack
                    contentDescription = stringResource(R.string.view_itinerary_back_cd),
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
                ) {
                    // Arrow glyph from strings.xml instead of hardcoded
                    Text(text = stringResource(R.string.view_itinerary_back_glyph), color = WaypointTerracotta, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp)) {

                Box(
                    modifier = Modifier
                        .background(WaypointCard, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 5.dp),
                ) {
                    Text(
                        text       = "$emoji  ${place.category.lowercase().replaceFirstChar { it.uppercaseChar() }}",
                        color      = WaypointTextMuted,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }

                Text(
                    text       = place.name,
                    color      = WaypointTextPrimary,
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(top = 12.dp),
                )

                if (!place.note.isNullOrBlank()) {
                    Text(
                        text     = place.note,
                        color    = WaypointTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }

                val description = wiki?.extract?.takeIf { it.isNotBlank() }
                if (description != null) {
                    Text(
                        text       = description,
                        color      = WaypointTextPrimary,
                        fontSize   = 14.sp,
                        lineHeight = 21.sp,
                        maxLines   = 8,
                        overflow   = TextOverflow.Ellipsis,
                        modifier   = Modifier.padding(top = 16.dp),
                    )
                } else if (!wikiLoading) {
                    Text(
                        text     = stringResource(R.string.view_itinerary_no_description),
                        color    = WaypointTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }

        if (hasCoords) {
            Box(
                modifier         = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(WaypointCream)
                    .padding(horizontal = 22.dp, vertical = 16.dp),
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(WaypointTerracotta, RoundedCornerShape(RadiusButton))
                        .clickable {
                            val uri = Uri.parse(
                                "https://www.google.com/maps/dir/?api=1" +
                                "&destination=${place.lat},${place.lng}" +
                                "&destination_place_id=${Uri.encode(place.name)}"
                            )
                            context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                        }
                        .padding(vertical = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text       = stringResource(R.string.view_itinerary_open_maps),
                        color      = White,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}


// Shows the live flight status for a flight in the itinerary.
// All visible text comes from strings.xml so the modal follows the selected language.
@Composable
private fun FlightStatusModal(
    status: FlightStatusState,
    onDismiss: () -> Unit,
) {
    // Title changes with the lookup state: loading message, flight number, or error
    val title = when (status) {
        is FlightStatusState.Loading -> stringResource(R.string.flight_status_loading)
        is FlightStatusState.Success -> status.result.flightIata
        is FlightStatusState.Error   -> stringResource(R.string.flight_status_error_title)
        else                         -> ""
    }
    AlertDialog(
        onDismissRequest   = onDismiss,
        containerColor     = WaypointCard,
        title              = { Text(title, color = WaypointTextPrimary, fontWeight = FontWeight.Bold, fontSize = 16.sp) },
        text               = {
            when (status) {
                // Spinner while AirLabs is being called
                is FlightStatusState.Loading -> {
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = WaypointTerracotta, modifier = Modifier.size(36.dp))
                    }
                }
                // Error message comes from the repository
                is FlightStatusState.Error -> {
                    Text(status.message, color = WaypointTextMuted, fontSize = 13.sp)
                }
                is FlightStatusState.Success -> {
                    val r = status.result
                    Column(modifier = Modifier.fillMaxWidth()) {
                        // Map the API status to a badge colour and a translated label
                        // Unknown statuses fall back to the raw API value
                        val (badgeColor, badgeLabel) = when (r.status.lowercase()) {
                            "active"    -> Pair(WaypointTerracotta,     stringResource(R.string.flight_status_in_flight))
                            "landed"    -> Pair(WaypointPlaceAccent1,   stringResource(R.string.flight_status_landed))
                            "scheduled" -> Pair(WaypointPlaceAccent2,   stringResource(R.string.flight_status_scheduled))
                            "cancelled" -> Pair(WaypointTextMuted,      stringResource(R.string.flight_status_cancelled))
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
                        // Departure row, with terminal and gate only shown when the API returns them
                        if (r.depIata.isNotBlank()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(R.string.flight_status_departure), color = WaypointTextMuted, fontSize = 11.sp)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(r.depIata, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    if (r.depTime.isNotBlank()) Text(r.depTime, color = WaypointTextMuted, fontSize = 11.sp)
                                    if (r.depTerminal.isNotBlank()) Text(stringResource(R.string.flight_status_terminal, r.depTerminal), color = WaypointTextMuted, fontSize = 10.sp)
                                    if (r.depGate.isNotBlank())     Text(stringResource(R.string.flight_status_gate, r.depGate), color = WaypointTextMuted, fontSize = 10.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        // Arrival row
                        if (r.arrIata.isNotBlank()) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text(stringResource(R.string.flight_status_arrival), color = WaypointTextMuted, fontSize = 11.sp)
                                Column(horizontalAlignment = Alignment.End) {
                                    Text(r.arrIata, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                    if (r.arrTime.isNotBlank()) Text(r.arrTime, color = WaypointTextMuted, fontSize = 11.sp)
                                }
                            }
                            Spacer(Modifier.height(8.dp))
                        }
                        // Delay warning, only when the flight is actually delayed
                        if (r.depDelayedMin > 0) {
                            Text(stringResource(R.string.flight_status_delayed, r.depDelayedMin), color = WaypointTerracotta, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Spacer(Modifier.height(4.dp))
                        }
                        // Duration split into hours and minutes
                        if (r.durationMin > 0) {
                            Text(stringResource(R.string.flight_status_duration, r.durationMin / 60, r.durationMin % 60), color = WaypointTextMuted, fontSize = 11.sp)
                        }
                    }
                }
                else -> {}
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(R.string.common_close), color = WaypointTerracotta, fontWeight = FontWeight.Bold)
            }
        },
    )
}


private fun airlineLogoUrl(flightNumber: String?): String? {
    val code = flightNumber?.replace(" ", "")?.uppercase()?.take(2)?.takeIf { it.length == 2 } ?: return null
    return "https://pics.avs.io/100/100/$code.png"
}


private fun categoryEmoji(category: String): String = when (category) {
    "RESTAURANTS" -> "🍽️"
    "PARKS"       -> "🌳"
    "PUBS"        -> "🍺"
    "CINEMAS"     -> "🎬"
    "HOTELS"      -> "🏨"
    else          -> "📍"
}


@Composable
private fun ViewSectionHeader(title: String, topPadding: Dp = 0.dp) {
    Text(
        text       = title,
        color      = WaypointTextPrimary,
        fontSize   = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier   = Modifier.fillMaxWidth().padding(top = topPadding),
    )
}


@Composable
private fun ViewEmptyPlaceholder(label: String) {
    Box(
        modifier         = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(WaypointCard, androidx.compose.foundation.shape.RoundedCornerShape(com.waypoint.app.core.theme.RadiusRow))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = WaypointTextMuted, fontSize = 12.sp)
    }
}


private fun openPdf(context: android.content.Context, pdfUri: String) {
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(Uri.parse(pdfUri), "application/pdf")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    }
    try {
        context.startActivity(Intent.createChooser(intent, null))
    } catch (e: Exception) {
        Toast.makeText(context, R.string.view_itinerary_no_pdf_viewer, Toast.LENGTH_SHORT).show()
    }
}

private fun pdfLabel(uri: String): String {
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    return decoded.substringAfterLast('/').ifEmpty { decoded }
}
