package com.waypoint.app.features.edititinerary.ui

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.common.CardSurface
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.common.RowSurface
import com.waypoint.app.core.common.StatusBadge
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusChip
import com.waypoint.app.core.theme.RadiusThumbnail
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointPlaceAccent4
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.WaypointTripBadgeText

@Composable
fun EditItineraryScreen(
    tripId: String,
    onBackClick: () -> Unit,
    onAddPlaceClick: (category: String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: EditItineraryViewModel = viewModel(),
) {
    val context = LocalContext.current
    val state by viewModel.uiState.collectAsState()

    // PDF picker launcher
    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri == null) {
            viewModel.onPickerDismissed()
            return@rememberLauncherForActivityResult
        }
        // Take persistable permission so we can re-read the URI after a restart
        runCatching {
            context.contentResolver.takePersistableUriPermission(
                uri,
                android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION,
            )
        }
        val uriString = uri.toString()
        when (state.pendingUploadType) {
            ItineraryUploadType.FLIGHT     -> viewModel.onFlightPdfPicked(uriString)
            ItineraryUploadType.LODGING    -> viewModel.onLodgingPdfPicked(uriString)
            ItineraryUploadType.CAR_RENTAL -> viewModel.onCarRentalPdfPicked(uriString)
            null                           -> viewModel.onPickerDismissed()
        }
    }

    // Fire picker when ViewModel requests it
    LaunchedEffect(state.pendingUploadType) {
        if (state.pendingUploadType != null) {
            pdfLauncher.launch(arrayOf("application/pdf"))
        }
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
        // ── Top bar ──────────────────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Text(
                    text = stringResource(R.string.edit_itinerary_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(
                modifier = Modifier.align(Alignment.Center),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = stringResource(R.string.edit_itinerary_title),
                    color = WaypointTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (state.days.isNotEmpty()) {
                    val first = state.days.first().date
                    val last  = state.days.last().date
                    val subtitle = if (first == last) first.toString()
                                   else "${first} – ${last}"
                    Text(
                        text = subtitle,
                        color = WaypointTextMuted,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        // ── Day scroller ─────────────────────────────────────────────────────
        if (state.days.isNotEmpty()) {
            DayScroller(
                days = state.days,
                activeDayIndex = state.activeDayIndex,
                onDaySelected = viewModel::onDaySelected,
            )
        }

        // ── Flights ──────────────────────────────────────────────────────────
        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_flights),
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            onChipClick = viewModel::onUploadFlightClick,
            topPadding  = 20.dp,
        )

        if (state.flightsForActiveDay.isEmpty()) {
            EmptyDocPlaceholder()
        } else {
            state.flightsForActiveDay.forEach { flight ->
                FlightCard(
                    flight         = flight,
                    onReplaceClick = viewModel::onUploadFlightClick,
                    onDeleteClick  = { viewModel.onDeleteFlight(flight.id) },
                    onNumberChanged = { num -> viewModel.onFlightNumberChanged(flight.id, num) },
                )
            }
        }

        // ── Lodging ───────────────────────────────────────────────────────────
        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_lodging),
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            onChipClick = viewModel::onUploadLodgingClick,
            topPadding  = 20.dp,
        )

        val lodging = state.lodging
        if (lodging == null) {
            EmptyDocPlaceholder()
        } else {
            DocCard(
                accentColor    = WaypointPlaceAccent2,
                title          = "Lodging document",
                subtitle       = "${lodging.fromDate} – ${lodging.toDate}",
                pdfUri         = lodging.pdfUri,
                onReplaceClick = viewModel::onUploadLodgingClick,
                onDeleteClick  = viewModel::onDeleteLodging,
            )
        }

        // ── Car rental ────────────────────────────────────────────────────────
        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_car),
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            onChipClick = viewModel::onUploadCarRentalClick,
            topPadding  = 20.dp,
        )

        val car = state.carRental
        if (car == null) {
            EmptyDocPlaceholder()
        } else {
            DocCard(
                accentColor    = WaypointPlaceAccent3,
                title          = "Car rental document",
                subtitle       = "${car.fromDate} – ${car.toDate}",
                pdfUri         = car.pdfUri,
                onReplaceClick = viewModel::onUploadCarRentalClick,
                onDeleteClick  = viewModel::onDeleteCarRental,
            )
        }

        // ── Food (mock - commit 4/5) ──────────────────────────────────────────
        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_food),
            chipLabel = stringResource(R.string.edit_itinerary_add_chip),
            onChipClick = {},
            topPadding  = 20.dp,
        )
        EmptyDocPlaceholder(label = "Restaurants and cafes coming soon")

        // ── Places: Hotels / Parks / Pubs / Cinemas ─────────────────────────
        val placeCategories = listOf(
            "PARKS"   to stringResource(R.string.edit_itinerary_header_parks),
            "PUBS"    to stringResource(R.string.edit_itinerary_header_pubs),
            "CINEMAS" to stringResource(R.string.edit_itinerary_header_cinemas),
        )
        placeCategories.forEach { (categoryKey, header) ->
            val items = state.placesForActiveDay[categoryKey].orEmpty()
            SectionHeader(
                title       = header,
                chipLabel   = stringResource(R.string.edit_itinerary_add_chip),
                onChipClick = {
                    val dayId = state.days.getOrNull(state.activeDayIndex)?.dayId
                    if (dayId != null) onAddPlaceClick(categoryKey)
                },
                topPadding  = 20.dp,
            )
            if (items.isEmpty()) {
                EmptyDocPlaceholder(label = stringResource(R.string.edit_itinerary_no_places))
            } else {
                items.forEach { place ->
                    PlaceCard(
                        place        = place,
                        onDeleteClick = { viewModel.onDeletePlace(place.id) },
                    )
                }
            }
        }
    }
}

// ── Day scroller ──────────────────────────────────────────────────────────────

@Composable
private fun DayScroller(
    days: List<DayItem>,
    activeDayIndex: Int,
    onDaySelected: (Int) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth: Dp  = (maxWidth - 16.dp) / 3  // 2 gaps of 8dp between 3 visible cards
        val listState      = rememberLazyListState()

        LazyRow(
            state            = listState,
            modifier         = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            itemsIndexed(days) { index, day ->
                val isActive = index == activeDayIndex
                CardSurface(
                    modifier      = Modifier
                        .width(cardWidth)
                        .clickable { onDaySelected(index) },
                    cornerRadius  = RadiusButton,
                ) {
                    Column(
                        modifier             = Modifier
                            .fillMaxWidth()
                            .background(if (isActive) WaypointTerracotta.copy(alpha = 0.08f) else Color.Transparent)
                            .padding(vertical = 12.dp),
                        horizontalAlignment  = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text      = day.date.dayOfWeek.name.take(3)
                                .lowercase()
                                .replaceFirstChar { it.uppercaseChar() },
                            color     = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            fontSize  = 9.sp,
                        )
                        Text(
                            text      = day.date.dayOfMonth.toString(),
                            color     = if (isActive) WaypointTerracotta else WaypointTextPrimary,
                            fontSize  = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier  = Modifier.padding(top = 2.dp),
                        )
                        Text(
                            text      = day.date.month.name
                                .lowercase()
                                .replaceFirstChar { it.uppercaseChar() }
                                .take(3),
                            color     = if (isActive) WaypointTerracotta else WaypointTextMuted,
                            fontSize  = 9.sp,
                            modifier  = Modifier.padding(top = 2.dp),
                        )
                    }
                }
            }
        }
    }
}

// ── Flight card ───────────────────────────────────────────────────────────────

@Composable
private fun FlightCard(
    flight: FlightItem,
    onReplaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNumberChanged: (String) -> Unit,
) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(
                accentColor   = WaypointPlaceAccent4,
                size          = 44.dp,
                cornerRadius  = RadiusThumbnail,
            )
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                BasicTextField(
                    value         = flight.flightNumber,
                    onValueChange = onNumberChanged,
                    textStyle     = TextStyle(
                        color      = WaypointTextPrimary,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.Bold,
                    ),
                    decorationBox = { inner ->
                        if (flight.flightNumber.isEmpty()) {
                            Text(
                                text  = stringResource(R.string.edit_itinerary_flight_number_hint),
                                color = WaypointTextMuted,
                                fontSize = 13.sp,
                            )
                        }
                        inner()
                    },
                )
                Text(
                    text     = pdfFileName(flight.pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
            }
            Row {
                IconActionButton(
                    glyph              = stringResource(R.string.edit_itinerary_undo_glyph),
                    contentDescription = stringResource(R.string.edit_itinerary_replace_flight_doc_cd),
                    color              = WaypointTerracotta,
                    onClick            = onReplaceClick,
                )
                IconActionButton(
                    glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                    contentDescription = stringResource(R.string.edit_itinerary_remove_flight_cd),
                    color              = WaypointTextMuted,
                    onClick            = onDeleteClick,
                    modifier           = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}

// ── Generic doc card (lodging / car rental) ───────────────────────────────────

@Composable
private fun DocCard(
    accentColor: Color,
    title: String,
    subtitle: String,
    pdfUri: String,
    onReplaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
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
                    text     = pdfFileName(pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
            }
            Row {
                IconActionButton(
                    glyph              = stringResource(R.string.edit_itinerary_undo_glyph),
                    contentDescription = "Replace document",
                    color              = WaypointTerracotta,
                    onClick            = onReplaceClick,
                )
                IconActionButton(
                    glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                    contentDescription = "Remove document",
                    color              = WaypointTextMuted,
                    onClick            = onDeleteClick,
                    modifier           = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}

// ── Empty state ───────────────────────────────────────────────────────────────

@Composable
private fun EmptyDocPlaceholder(
    label: String = stringResource(R.string.edit_itinerary_no_pdf_uploaded),
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
            .background(WaypointCard, RoundedCornerShape(com.waypoint.app.core.theme.RadiusRow))
            .padding(vertical = 16.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = label, color = WaypointTextMuted, fontSize = 12.sp)
    }
}

// ── Place card ───────────────────────────────────────────────────────────────

@Composable
private fun PlaceCard(
    place: PlaceItem,
    onDeleteClick: () -> Unit,
) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(
                accentColor  = WaypointPlaceAccent1,
                size         = 44.dp,
                cornerRadius = RadiusThumbnail,
            )
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(
                    text       = place.name,
                    color      = WaypointTextPrimary,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                if (!place.note.isNullOrBlank()) {
                    Text(
                        text     = place.note,
                        color    = WaypointTextMuted,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(top = 3.dp),
                    )
                }
            }
            IconActionButton(
                glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                contentDescription = stringResource(R.string.edit_itinerary_remove_place_cd),
                color              = WaypointTextMuted,
                onClick            = onDeleteClick,
            )
        }
    }
}

// ── Section header ────────────────────────────────────────────────────────────

@Composable
private fun SectionHeader(
    title: String,
    chipLabel: String,
    onChipClick: () -> Unit,
    topPadding: Dp = 0.dp,
) {
    Row(
        modifier          = Modifier.fillMaxWidth().padding(top = topPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text       = title,
            color      = WaypointTextPrimary,
            fontSize   = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier   = Modifier.weight(1f),
        )
        StatusBadge(
            text             = chipLabel,
            fillColor        = null,
            borderColor      = WaypointTerracotta,
            textColor        = WaypointTerracotta,
            cornerRadius     = RadiusChip,
            contentPadding   = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            modifier         = Modifier.clickable(onClick = onChipClick),
        )
    }
}

// ── Icon action button ────────────────────────────────────────────────────────

@Composable
private fun IconActionButton(
    glyph: String,
    contentDescription: String,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier         = modifier
            .size(28.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = glyph, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

/** Extracts a readable filename from a content URI string for display. */
private fun pdfFileName(uri: String): String {
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    return decoded.substringAfterLast('/').ifEmpty { decoded }
}
