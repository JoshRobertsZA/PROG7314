package com.waypoint.app.features.viewitinerary.ui

import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun ViewItineraryScreen(
    tripId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ViewItineraryViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    // Detail overlay — shown when a place card is tapped
    state.selectedPlace?.let { place ->
        ViewPlaceDetailOverlay(
            place      = place,
            onBack     = viewModel::onPlaceDismissed,
        )
        return
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
        // Top bar
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(
                onClick  = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Text(
                    text       = stringResource(R.string.view_itinerary_back_glyph),
                    color      = WaypointTerracotta,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
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

        // Day scroller
        if (state.days.isNotEmpty()) {
            ViewDayScroller(
                days           = state.days,
                activeDayIndex = state.activeDayIndex,
                onDaySelected  = viewModel::onDaySelected,
            )
        }

        // Flights
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_flights), topPadding = 20.dp)
        if (state.flightsForActiveDay.isEmpty()) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_flights))
        } else {
            state.flightsForActiveDay.forEach { f ->
                ViewFlightCard(flight = f)
            }
        }

        // Lodging
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_lodging), topPadding = 20.dp)
        val lodging = state.lodging
        if (lodging == null) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_lodging))
        } else {
            ViewDocCard(
                accentColor = WaypointPlaceAccent2,
                title       = "Lodging document",
                subtitle    = "${lodging.fromDate} – ${lodging.toDate}",
                pdfUri      = lodging.pdfUri,
            )
        }

        // Car rental
        ViewSectionHeader(title = stringResource(R.string.edit_itinerary_header_car), topPadding = 20.dp)
        val car = state.carRental
        if (car == null) {
            ViewEmptyPlaceholder(label = stringResource(R.string.view_itinerary_no_car))
        } else {
            ViewDocCard(
                accentColor = WaypointPlaceAccent3,
                title       = "Car rental document",
                subtitle    = "${car.fromDate} – ${car.toDate}",
                pdfUri      = car.pdfUri,
            )
        }

        // Places by category
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

// ── Day scroller ──────────────────────────────────────────────────────────────

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

// ── Flight card (read-only) ───────────────────────────────────────────────────

@Composable
private fun ViewFlightCard(flight: ViewFlightItem) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail)
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                val label = flight.flightNumber?.takeIf { it.isNotBlank() } ?: "Flight"
                Text(label, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(
                    text     = pdfLabel(flight.pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
            }
        }
    }
}

// ── Generic doc card (read-only) ─────────────────────────────────────────────

@Composable
private fun ViewDocCard(
    accentColor: Color,
    title: String,
    subtitle: String,
    pdfUri: String,
) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(accentColor = accentColor, size = 44.dp, cornerRadius = RadiusThumbnail)
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(title, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                Text(
                    text     = pdfLabel(pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
            }
        }
    }
}

// ── Place card (read-only, tappable → Google Maps) ───────────────────────────

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
                    text     = "Tap for details",
                    color    = WaypointTerracotta,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}

// ── Place detail overlay ──────────────────────────────────────────────────────

@Composable
private fun ViewPlaceDetailOverlay(
    place: ViewPlaceItem,
    onBack: () -> Unit,
) {
    val context   = LocalContext.current
    val hasCoords = place.lat != null && place.lng != null
    val emoji     = categoryEmoji(place.category)

    // Live Wikipedia fetch for description + bitmap
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

            // ── Hero ──────────────────────────────────────────────────────────
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

                // Back button
                CircleIconButton(
                    onClick  = onBack,
                    modifier = Modifier.align(Alignment.TopStart).padding(16.dp),
                ) {
                    Text(text = "←", color = WaypointTerracotta, fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }

            // ── Body ─────────────────────────────────────────────────────────
            Column(modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp)) {

                // Category chip
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

                // Place name
                Text(
                    text       = place.name,
                    color      = WaypointTextPrimary,
                    fontSize   = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier   = Modifier.padding(top = 12.dp),
                )

                // User note
                if (!place.note.isNullOrBlank()) {
                    Text(
                        text     = place.note,
                        color    = WaypointTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 6.dp),
                    )
                }

                // Wikipedia description
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
                        text     = "No description available.",
                        color    = WaypointTextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(top = 16.dp),
                    )
                }
            }
        }

        // ── Open in Maps pinned to bottom ──────────────────────────────────
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
                        text       = "Open in Maps",
                        color      = androidx.compose.ui.graphics.Color.White,
                        fontSize   = 15.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }
        }
    }
}

// ── Category emoji ────────────────────────────────────────────────────────────

private fun categoryEmoji(category: String): String = when (category) {
    "RESTAURANTS" -> "🍽️"
    "PARKS"       -> "🌳"
    "PUBS"        -> "🍺"
    "CINEMAS"     -> "🎬"
    "HOTELS"      -> "🏨"
    else          -> "📍"
}

// ── Section header (label only, no chip) ─────────────────────────────────────

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

// ── Empty state ───────────────────────────────────────────────────────────────

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

// ── Helper ────────────────────────────────────────────────────────────────────

private fun pdfLabel(uri: String): String {
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    return decoded.substringAfterLast('/').ifEmpty { decoded }
}
