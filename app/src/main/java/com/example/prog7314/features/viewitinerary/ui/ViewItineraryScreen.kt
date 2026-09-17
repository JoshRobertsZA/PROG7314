package com.example.prog7314.features.viewitinerary.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.common.CardSurface
import com.example.prog7314.core.common.CircleIconButton
import com.example.prog7314.core.common.RowSurface
import com.example.prog7314.core.common.StatusBadge
import com.example.prog7314.core.common.ThumbnailBlock
import com.example.prog7314.core.theme.RadiusRow
import com.example.prog7314.core.theme.WaypointBorderSoft
import com.example.prog7314.core.theme.WaypointCream
import com.example.prog7314.core.theme.WaypointPlaceAccent1
import com.example.prog7314.core.theme.WaypointPlaceAccent2
import com.example.prog7314.core.theme.WaypointPlaceAccent3
import com.example.prog7314.core.theme.WaypointPlaceAccent4
import com.example.prog7314.core.theme.WaypointTerracotta
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.core.theme.WaypointTextPrimary
import com.example.prog7314.core.theme.WaypointTripBadgeText
import androidx.compose.ui.graphics.Color as ComposeColor

private data class DayWeather(val day: String, val temp: String, val condition: String)
private data class BookingRow(val title: String, val subtitle: String, val thumb: ComposeColor, val trailing: String, val trailingIsPdf: Boolean)

/**
 * View itinerary screen. Minimal skeleton whose only job is to display
 * the screen and let the user navigate back - the weather chips and
 * every booking/place row are still static/mock content, not wired up
 * yet.
 *
 * TODO: replace mock weather/flight/lodging/car/restaurant/attraction
 * content with real data, and wire up the "View PDF" chips, once the
 * backend (booking storage, OpenWeatherMap) is wired up on its own
 * branch.
 */
@Composable
fun ViewItineraryScreen(
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
                    text = stringResource(R.string.itinerary_back_glyph),
                    color = WaypointTerracotta,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Column(modifier = Modifier.align(Alignment.Center), horizontalAlignment = Alignment.CenterHorizontally) {
                Text(stringResource(R.string.itinerary_title), color = WaypointTextPrimary, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    modifier = Modifier.weight(1f).padding(start = if (index == 0) 0.dp else 8.dp),
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

        SectionHeader(stringResource(R.string.itinerary_header_flights))
        BookingRowCard(BookingRow("CPT → JNB · SA 123", "Aug 3 · Departs 09:40", WaypointPlaceAccent1, stringResource(R.string.itinerary_view_pdf), trailingIsPdf = true))

        SectionHeader(stringResource(R.string.itinerary_header_lodging), topPadding = 20.dp)
        BookingRowCard(BookingRow("Test Valley Boutique Hotel", "Check-in Aug 3 · 2 nights", WaypointPlaceAccent2, stringResource(R.string.itinerary_view_pdf), trailingIsPdf = true))

        SectionHeader(stringResource(R.string.itinerary_header_car), topPadding = 20.dp)
        BookingRowCard(BookingRow("Compact Car · Avis", "Aug 3 – Aug 5 pickup", WaypointPlaceAccent3, stringResource(R.string.itinerary_view_pdf), trailingIsPdf = true))

        SectionHeader(stringResource(R.string.itinerary_header_food), topPadding = 20.dp)
        Column(modifier = Modifier.fillMaxWidth().padding(top = 8.dp)) {
            val foodRows = listOf(
                BookingRow("The Test Kitchen", "Restaurant · Aug 3", WaypointPlaceAccent4, "19:00", trailingIsPdf = false),
                BookingRow("Truth Coffee", "Cafe · Aug 4", WaypointPlaceAccent2, "09:00", trailingIsPdf = false),
            )
            foodRows.forEachIndexed { index, row ->
                BookingRowCard(row, topPadding = if (index == 0) 0.dp else 8.dp)
            }
        }

        SectionHeader(stringResource(R.string.itinerary_header_attractions), topPadding = 20.dp)
        BookingRowCard(BookingRow("Table Mountain Cableway", "Attraction · Aug 4", WaypointPlaceAccent3, "11:00", trailingIsPdf = false))

        SectionHeader(stringResource(R.string.itinerary_header_entertainment), topPadding = 20.dp)

        // EmptyState: no entertainment mock-booked for this trip - dashed
        // outline box, matching the source's dashed bg_itinerary_empty_state.
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, bottom = 4.dp)
                .dashedBorder(WaypointBorderSoft, RadiusRow)
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(R.string.itinerary_empty_entertainment),
                color = WaypointTextMuted,
                fontSize = 12.sp,
            )
        }
    }
}

@Composable
private fun SectionHeader(title: String, topPadding: Dp = 0.dp) {
    Text(
        text = title,
        color = WaypointTextPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        modifier = Modifier.fillMaxWidth().padding(top = topPadding),
    )
}

@Composable
private fun BookingRowCard(row: BookingRow, topPadding: Dp = 8.dp) {
    RowSurface(modifier = Modifier.fillMaxWidth().padding(top = topPadding)) {
        Row(
            modifier = Modifier.padding(start = 10.dp, top = 10.dp, end = if (row.trailingIsPdf) 12.dp else 14.dp, bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(accentColor = row.thumb, size = 44.dp, cornerRadius = com.example.prog7314.core.theme.RadiusThumbnail)
            Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                Text(row.title, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text(row.subtitle, color = WaypointTextMuted, fontSize = 11.sp, modifier = Modifier.padding(top = 3.dp))
            }
            if (row.trailingIsPdf) {
                StatusBadge(
                    text = row.trailing,
                    fillColor = null,
                    borderColor = WaypointTerracotta,
                    textColor = WaypointTerracotta,
                    cornerRadius = com.example.prog7314.core.theme.RadiusChip,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 9.dp, vertical = 5.dp),
                    modifier = Modifier.clickable(onClick = {}),
                )
            } else {
                Text(row.trailing, color = WaypointTripBadgeText, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

private fun Modifier.dashedBorder(color: ComposeColor, radius: Dp): Modifier = this.drawBehind {
    val stroke = Stroke(
        width = 1.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(6.dp.toPx(), 4.dp.toPx()), 0f),
    )
    drawRoundRect(
        color = color,
        size = Size(size.width, size.height),
        cornerRadius = CornerRadius(radius.toPx(), radius.toPx()),
        style = stroke,
    )
}
