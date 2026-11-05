package com.waypoint.app.features.edititinerary.ui

import com.waypoint.app.core.theme.White
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
import androidx.compose.runtime.setValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import android.app.TimePickerDialog
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
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.layout.ContentScale
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
import androidx.compose.material3.Icon
import androidx.compose.ui.res.painterResource
import com.waypoint.app.core.common.TripValidation

/**
 * Builds a trip's itinerary day by day: flights per day, lodging and car rentals across days, and
 * places by category. Every item can be added, edited and removed.
 */
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
    var editingLodging by remember { mutableStateOf<LodgingItem?>(null) }
    var editingCarRental by remember { mutableStateOf<CarRentalItem?>(null) }
    var editingPlace by remember { mutableStateOf<PlaceItem?>(null) }

    editingLodging?.let { lodging ->
        BookingDatesDialog(
            title = stringResource(R.string.edit_itinerary_edit_lodging_title),
            days = state.days,
            initialFrom = lodging.fromDate,
            initialTo = lodging.toDate,
            onSave = { from, to -> viewModel.onUpdateLodgingDates(lodging.id, from, to) },
            onDismiss = { editingLodging = null },
        )
    }
    editingCarRental?.let { car ->
        BookingDatesDialog(
            title = stringResource(R.string.edit_itinerary_edit_car_title),
            days = state.days,
            initialFrom = car.fromDate,
            initialTo = car.toDate,
            onSave = { from, to -> viewModel.onUpdateCarRentalDates(car.id, from, to) },
            onDismiss = { editingCarRental = null },
        )
    }
    editingPlace?.let { place ->
        PlaceNoteDialog(
            placeName = place.name,
            initialNote = place.note,
            onSave = { note -> viewModel.onUpdatePlaceNote(place.id, note) },
            onDismiss = { editingPlace = null },
        )
    }

    LaunchedEffect(Unit) { viewModel.refreshPlacesForActiveDay() }

    val pdfLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument(),
    ) { uri: Uri? ->
        if (uri == null) {
            viewModel.onPickerDismissed()
            return@rememberLauncherForActivityResult
        }
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
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(
                onClick = onBackClick,
                // Read out by TalkBack
                contentDescription = stringResource(R.string.edit_itinerary_back_cd),
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

        if (state.days.isNotEmpty()) {
            DayScroller(
                days = state.days,
                activeDayIndex = state.activeDayIndex,
                onDaySelected = viewModel::onDaySelected,
            )
        }

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
                    onTimeChanged  = { time -> viewModel.onFlightDepartureTimeChanged(flight.id, time) },
                )
            }
        }

        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_lodging),
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            onChipClick = viewModel::onUploadLodgingClick,
            topPadding  = 20.dp,
        )

        val lodgings = state.lodgingForActiveDay
        if (lodgings.isEmpty()) {
            EmptyDocPlaceholder()
        } else {
            lodgings.forEach { lodging ->
                DocCard(
                    accentColor   = WaypointPlaceAccent2,
                    title         = lodging.docName ?: stringResource(R.string.edit_itinerary_lodging_doc),
                    subtitle      = "${lodging.fromDate} – ${lodging.toDate}",
                    pdfUri        = lodging.pdfUri,
                    onDeleteClick = { viewModel.onDeleteLodging(lodging.id) },
                    onEditClick   = { editingLodging = lodging },
                    editContentDescription = stringResource(R.string.edit_itinerary_edit_dates_cd),
                )
            }
        }

        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_car),
            chipLabel = stringResource(R.string.edit_itinerary_upload_chip),
            onChipClick = viewModel::onUploadCarRentalClick,
            topPadding  = 20.dp,
        )

        val cars = state.carRentalsForActiveDay
        if (cars.isEmpty()) {
            EmptyDocPlaceholder()
        } else {
            cars.forEach { car ->
                DocCard(
                    accentColor   = WaypointPlaceAccent3,
                    title         = car.docName ?: stringResource(R.string.edit_itinerary_car_doc),
                    subtitle      = "${car.fromDate} – ${car.toDate}",
                    pdfUri        = car.pdfUri,
                    onDeleteClick = { viewModel.onDeleteCarRental(car.id) },
                    onEditClick   = { editingCarRental = car },
                    editContentDescription = stringResource(R.string.edit_itinerary_edit_dates_cd),
                )
            }
        }

        val foodItems = state.placesForActiveDay["RESTAURANTS"].orEmpty()
        SectionHeader(
            title     = stringResource(R.string.edit_itinerary_header_food),
            chipLabel = stringResource(R.string.edit_itinerary_add_chip),
            onChipClick = {
                val dayId = state.days.getOrNull(state.activeDayIndex)?.dayId
                if (dayId != null) onAddPlaceClick("RESTAURANTS")
            },
            topPadding  = 20.dp,
        )
        if (foodItems.isEmpty()) {
            EmptyDocPlaceholder(label = stringResource(R.string.edit_itinerary_no_places))
        } else {
            foodItems.forEach { place ->
                PlaceCard(
                    place         = place,
                    onDeleteClick = { viewModel.onDeletePlace(place.id) },
                    onEditClick   = { editingPlace = place },
                )
            }
        }

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
                        onEditClick   = { editingPlace = place },
                    )
                }
            }
        }
    }
}


@Composable
private fun DayScroller(
    days: List<DayItem>,
    activeDayIndex: Int,
    onDaySelected: (Int) -> Unit,
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val cardWidth: Dp  = (maxWidth - 16.dp) / 3
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


@Composable
private fun FlightCard(
    flight: FlightItem,
    onReplaceClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onNumberChanged: (String) -> Unit,
    onTimeChanged: (String) -> Unit,
) {
    val context = LocalContext.current
    val openTimePicker = {
        val (h, m) = flight.departureTime?.split(":")?.map { it.toInt() } ?: listOf(9, 0)
        TimePickerDialog(context, { _, hour, minute -> onTimeChanged("%02d:%02d".format(hour, minute)) }, h, m, true).show()
    }
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val logoUrl = airlineLogoUrl(flight.flightNumber)
            if (logoUrl != null) {
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(LocalContext.current).data(logoUrl).crossfade(true).build(),
                    contentDescription = flight.flightNumber,
                    contentScale       = ContentScale.Fit,
                    modifier           = Modifier.size(44.dp).clip(RoundedCornerShape(RadiusThumbnail)).background(White),
                    loading = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                    error   = { ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail) },
                )
            } else {
                ThumbnailBlock(accentColor = WaypointPlaceAccent4, size = 44.dp, cornerRadius = RadiusThumbnail)
            }
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
                // Hint when the flight number isn't a valid format (blank is allowed)
                if (flight.flightNumber.isNotBlank() && !TripValidation.isFlightNumberValid(flight.flightNumber)) {
                    Text(
                        text = stringResource(R.string.edit_itinerary_flight_number_invalid),
                        color = WaypointTerracotta,
                        fontSize = 9.sp,
                        modifier = Modifier.padding(top = 2.dp),
                    )
                }
                Text(
                    text     = flight.docName ?: pdfFileName(flight.pdfUri),
                    color    = WaypointTripBadgeText,
                    fontSize = 9.sp,
                    modifier = Modifier.padding(top = 3.dp).alpha(0.8f),
                )
                Text(
                    text     = flight.departureTime?.let { stringResource(R.string.edit_itinerary_flight_departs, it) }
                               ?: stringResource(R.string.edit_itinerary_flight_set_time),
                    color    = WaypointTerracotta,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 3.dp).clickable(onClick = openTimePicker),
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


@Composable
private fun DocCard(
    accentColor: Color,
    title: String,
    subtitle: String,
    pdfUri: String,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
    editContentDescription: String,
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
            }
            IconActionButton(
                glyph              = stringResource(R.string.edit_itinerary_edit_glyph),
                contentDescription = editContentDescription,
                color              = WaypointTerracotta,
                onClick            = onEditClick,
            )
            IconActionButton(
                glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                contentDescription = stringResource(R.string.edit_itinerary_remove_doc_cd),
                color              = WaypointTextMuted,
                onClick            = onDeleteClick,
            )
        }
    }
}


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


@Composable
private fun PlaceCard(
    place: PlaceItem,
    onDeleteClick: () -> Unit,
    onEditClick: () -> Unit,
) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
        Row(
            modifier          = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
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
                glyph              = stringResource(R.string.edit_itinerary_edit_glyph),
                contentDescription = stringResource(R.string.edit_itinerary_edit_note_cd),
                color              = WaypointTerracotta,
                onClick            = onEditClick,
            )
            IconActionButton(
                glyph              = stringResource(R.string.edit_itinerary_close_glyph),
                contentDescription = stringResource(R.string.edit_itinerary_remove_place_cd),
                color              = WaypointTextMuted,
                onClick            = onDeleteClick,
            )
        }
    }
}


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
            // 40dp keeps the edit and remove buttons comfortably tappable side by side
            .size(40.dp)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = glyph, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}


private fun airlineLogoUrl(flightNumber: String): String? {
    val code = flightNumber.replace(" ", "").uppercase().take(2).takeIf { it.length == 2 } ?: return null
    return "https://pics.avs.io/100/100/$code.png"
}

private fun pdfFileName(uri: String): String {
    val decoded = Uri.parse(uri).lastPathSegment ?: uri
    return decoded.substringAfterLast('/').ifEmpty { decoded }
}
