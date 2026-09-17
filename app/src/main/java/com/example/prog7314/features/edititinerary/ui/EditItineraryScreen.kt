package com.example.prog7314.features.edititinerary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.CardSurface
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.StatusBadge
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointPlaceAccent1
import com.example.prog7314.core.theme.WaypointPlaceAccent2
import com.example.prog7314.core.theme.WaypointPlaceAccent3
import com.example.prog7314.core.theme.WaypointPlaceAccent4
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripBadgeText

private data class DayWeather(val day: String, val temp: String, val condition: String)
private data class BookingRow(val title: String, val subtitle: String, val docLabel: String?, val thumb: Color)

/**
 * Edit itinerary screen. Minimal skeleton whose only job is to display
 * the screen and let the user navigate back - the weather chips, every
 * booking/place row, and the upload/add/undo/edit/remove controls are
 * still static/mock content, not wired up yet.
 *
 * TODO: wire up the "+ Upload"/"+ Add" chips, the per-row undo (replace),
 * edit, and remove icon buttons, and the "+ Add entertainment"
 * placeholder, and replace mock weather/flight/lodging/car/restaurant/
 * attraction content with real data, once the backend (booking storage,
 * OpenWeatherMap) is wired up on its own branch.
 */
@Composable
fun EditItineraryScreen(
    tripId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val weather = listOf(
        DayWeather("Aug 3", "22°C", "Sunny"),
        DayWeather("Aug 4", "20°C", "Cloudy"),
        DayWeather("Aug 5", "18°C", "Rain"),
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // TopBar: back button, title + subtitle (centered)
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                Text(
                    text = stringResource(R.string.edit_itinerary_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.edit_itinerary_title), color = WaypointTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(
                    text = "Cape Town Getaway · Aug 3 – Aug 5",
                    color = WaypointTextMuted,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(top = 2.dp),
                )
            }
        }

        // WeatherRow: one chip per itinerary day
        Row(modifier = Modifier.fillMaxWidth().padding(top = 20.dp)) {
            weather.forEachIndexed { index, day ->
                CardSurface(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = if (index == 0) 0.dp else 8.dp),
                    cornerRadius = com.example.prog7314.core.theme.RadiusButton,
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 10.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(day.day, color = WaypointTextMuted, fontSize = 10.sp)
                        Text(day.temp, color = WaypointTextPrimary, fontSize = 15.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 3.dp))
                        Text(day.condition, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 3.dp))
                    }
                }
            }
        }

        SectionHeader(stringResource(R.string.edit_itinerary_header_flights), stringResource(R.string.edit_itinerary_upload_chip))
        BookingRowCard(
            BookingRow("CPT → JNB · SA 123", "Aug 3 · Departs 09:40", "boarding-pass.pdf", WaypointPlaceAccent4),
            editCd = stringResource(R.string.edit_itinerary_replace_flight_doc_cd),
            editGlyph = stringResource(R.string.edit_itinerary_undo_glyph),
            removeCd = stringResource(R.string.edit_itinerary_remove_flight_cd),
        )

        SectionHeader(stringResource(R.string.edit_itinerary_header_lodging), stringResource(R.string.edit_itinerary_upload_chip), topPadding = 20.dp)
        BookingRowCard(
            BookingRow("Test Valley Boutique Hotel", "Check-in Aug 3 · 2 nights", "hotel-booking.pdf", WaypointPlaceAccent2),
            editCd = stringResource(R.string.edit_itinerary_replace_lodging_doc_cd),
            editGlyph = stringResource(R.string.edit_itinerary_undo_glyph),
            removeCd = stringResource(R.string.edit_itinerary_remove_lodging_cd),
        )

        SectionHeader(stringResource(R.string.edit_itinerary_header_car), stringResource(R.string.edit_itinerary_upload_chip), topPadding = 20.dp)
        BookingRowCard(
            BookingRow("Compact Car · Avis", "Aug 3 – Aug 5 pickup", "rental-agreement.pdf", WaypointPlaceAccent3),
            editCd = stringResource(R.string.edit_itinerary_replace_car_doc_cd),
            editGlyph = stringResource(R.string.edit_itinerary_undo_glyph),
            removeCd = stringResource(R.string.edit_itinerary_remove_car_cd),
        )

        SectionHeader(stringResource(R.string.edit_itinerary_header_food), stringResource(R.string.edit_itinerary_add_chip), topPadding = 20.dp)
        Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            val foodRows = listOf(
                BookingRow("The Test Kitchen", "Restaurant · Aug 3 · 19:00", null, WaypointPlaceAccent1),
                BookingRow("Truth Coffee", "Cafe · Aug 4 · 09:00", null, WaypointPlaceAccent2),
            )
            foodRows.forEachIndexed { index, row ->
                BookingRowCard(
                    row,
                    editCd = stringResource(R.string.edit_itinerary_edit_place_cd),
                    editGlyph = stringResource(R.string.edit_itinerary_edit_glyph),
                    removeCd = stringResource(R.string.edit_itinerary_remove_place_cd),
                    topPadding = if (index == 0) 0.dp else 8.dp,
                )
            }
        }

        SectionHeader(stringResource(R.string.edit_itinerary_header_attractions), stringResource(R.string.edit_itinerary_add_chip), topPadding = 20.dp)
        BookingRowCard(
            BookingRow("Table Mountain Cableway", "Attraction · Aug 4 · 11:00", null, WaypointPlaceAccent3),
            editCd = stringResource(R.string.edit_itinerary_edit_place_cd),
            editGlyph = stringResource(R.string.edit_itinerary_edit_glyph),
            removeCd = stringResource(R.string.edit_itinerary_remove_place_cd),
        )

        SectionHeader(stringResource(R.string.edit_itinerary_header_entertainment), stringResource(R.string.edit_itinerary_add_chip), topPadding = 20.dp)

        // AddPlaceholder: no entertainment mock-booked for this trip yet;
        // dashed outline button that will eventually open the add flow.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
                .clickable(onClick = {})
                .background(Color.Transparent, RoundedCornerShape(RadiusRow))
                .padding(vertical = 14.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.edit_itinerary_add_entertainment_placeholder),
                color = WaypointTerracotta,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, chipLabel: String, topPadding: androidx.compose.ui.unit.Dp = 0.dp) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(top = topPadding),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(title, color = WaypointTextPrimary, fontSize = 14.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
        StatusBadge(
            text = chipLabel,
            fillColor = null,
            borderColor = WaypointTerracotta,
            textColor = WaypointTerracotta,
            cornerRadius = com.example.prog7314.core.theme.RadiusChip,
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 10.dp, vertical = 5.dp),
            modifier = Modifier.clickable(onClick = {}),
        )
    }
}

@Composable
private fun BookingRowCard(
    row: BookingRow,
    editCd: String,
    editGlyph: String,
    removeCd: String,
    topPadding: androidx.compose.ui.unit.Dp = 8.dp,
) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = topPadding)) {
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = 12.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(accentColor = row.thumb, size = 44.dp, cornerRadius = com.example.prog7314.core.theme.RadiusThumbnail)
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(row.title, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(row.subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
                if (row.docLabel != null) {
                    Text(row.docLabel, color = WaypointTripBadgeText, fontSize = 9.sp, modifier = Modifier.padding(top = 3.dp).alpha(0.8f))
                }
            }
            Row {
                IconActionButton(editGlyph, editCd, WaypointTerracotta)
                IconActionButton(
                    stringResource(R.string.edit_itinerary_close_glyph),
                    removeCd,
                    WaypointTextMuted,
                    modifier = Modifier.padding(start = 6.dp),
                )
            }
        }
    }
}

@Composable
private fun IconActionButton(glyph: String, contentDescription: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(28.dp)
            .clickable(onClick = {}),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = glyph, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
