package com.waypoint.app.features.placepicker.ui

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImagePainter
import androidx.compose.ui.layout.ContentScale
import com.waypoint.app.R
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusRow
import com.waypoint.app.core.theme.WaypointCard
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

@Composable
fun PlaceDetailPickerScreen(
    tripId      : String,
    dayId       : String,
    category    : String,
    placeId     : String,
    onBackClick : () -> Unit,
    onPlaceAdded: () -> Unit,
    modifier    : Modifier = Modifier,
    viewModel   : PlaceDetailPickerViewModel = viewModel(),
) {
    val state   by viewModel.uiState.collectAsState()
    val context = LocalContext.current

    // Pop back once the place has been saved
    LaunchedEffect(state.isAdded) {
        if (state.isAdded) onPlaceAdded()
    }

    Box(modifier = modifier.fillMaxSize().background(WaypointCream)) {

        // ── Hero: Wikipedia thumbnail if available, else emoji placeholder ──
        Box(
            modifier         = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .background(WaypointPlaceAccent2),
            contentAlignment = Alignment.Center,
        ) {
            val thumb = if (!state.isLoadingWiki) state.wikiSummary?.thumbnailUrl else null
            val emoji = state.placeEmoji.ifEmpty { "📍" }
            if (!thumb.isNullOrBlank()) {
                // Show Wikipedia thumbnail; fall back to emoji on load failure
                val ctx = LocalContext.current
                SubcomposeAsyncImage(
                    model              = ImageRequest.Builder(ctx).data(thumb).crossfade(true).build(),
                    contentDescription = state.placeName,
                    contentScale       = ContentScale.Crop,
                    modifier           = Modifier.fillMaxSize(),
                    loading = {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = emoji, fontSize = 72.sp)
                        }
                    },
                    error = {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(text = emoji, fontSize = 72.sp)
                        }
                    },
                )
            } else {
                // No thumbnail (or still loading wiki) — show category emoji
                Text(text = emoji, fontSize = 72.sp)
            }
        }

        // ── Scrollable body ──────────────────────────────────────────────────
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 190.dp)
                .verticalScroll(rememberScrollState())
                .background(WaypointCream, RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp))
                .padding(start = 22.dp, top = 24.dp, end = 22.dp, bottom = 28.dp),
        ) {
            // Place name + subtitle
            Text(
                text       = state.placeName.ifBlank { "…" },
                color      = WaypointTextPrimary,
                fontSize   = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines   = 2,
                overflow   = TextOverflow.Ellipsis,
            )
            val subtitle = listOfNotNull(
                state.placeType.replaceFirstChar { it.uppercase() }.ifBlank { null },
                state.distanceKm.ifBlank { null },
            ).joinToString(" · ")
            if (subtitle.isNotBlank()) {
                Text(
                    text     = subtitle,
                    color    = WaypointTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            // Weather info card
            Spacer(Modifier.height(20.dp))
            if (state.isLoadingWeather) {
                CircularProgressIndicator(
                    color    = WaypointTerracotta,
                    modifier = Modifier.height(32.dp).width(32.dp),
                )
            } else {
                val w = state.weather
                if (w != null) {
                    InfoCard(
                        value    = "${"%.0f".format(w.tempC)}°C · ${w.description}",
                        label    = "At this location",
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            // Wikipedia About section
            Spacer(Modifier.height(22.dp))
            Row(
                modifier          = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text       = stringResource(R.string.place_detail_about_header),
                    color      = WaypointTextPrimary,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier   = Modifier.weight(1f),
                )
                Text(
                    text  = stringResource(R.string.place_detail_wikipedia_attribution),
                    color = WaypointTextMuted,
                    fontSize = 10.sp,
                )
            }
            if (state.isLoadingWiki) {
                Text(
                    text     = "Loading…",
                    color    = WaypointTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                )
            } else {
                val extract = state.wikiSummary?.extract
                Text(
                    text     = extract ?: "No Wikipedia article found for this place.",
                    color    = WaypointTextMuted,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 8.dp),
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis,
                )
            }

            // Location / Open in Maps
            Spacer(Modifier.height(22.dp))
            Text(
                text       = stringResource(R.string.place_detail_location_header),
                color      = WaypointTextPrimary,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
                    .height(80.dp)
                    .background(WaypointCard, RoundedCornerShape(RadiusRow)),
                contentAlignment = Alignment.BottomStart,
            ) {
                Box(
                    modifier = Modifier
                        .padding(12.dp)
                        .background(White.copy(alpha = 0.95f), RoundedCornerShape(RadiusRow))
                        .clickable {
                            if (state.lat != 0.0 || state.lon != 0.0) {
                                val uri = Uri.parse(
                                    "https://www.google.com/maps/dir/?api=1" +
                                    "&destination=${state.lat},${state.lon}" +
                                    "&destination_place_id=${Uri.encode(state.placeName)}"
                                )
                                context.startActivity(Intent(Intent.ACTION_VIEW, uri))
                            }
                        }
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                ) {
                    Text(
                        text       = stringResource(R.string.place_detail_open_in_maps),
                        color      = WaypointTerracotta,
                        fontSize   = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }

            // Add to itinerary
            Spacer(Modifier.height(22.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (state.isAdding) WaypointTerracotta.copy(alpha = 0.6f) else WaypointTerracotta,
                        RoundedCornerShape(RadiusButton),
                    )
                    .clickable(enabled = !state.isAdding && !state.isAdded) {
                        viewModel.onAddToItinerary()
                    }
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                if (state.isAdding) {
                    CircularProgressIndicator(color = White, modifier = Modifier.height(20.dp).width(20.dp))
                } else {
                    Text(
                        text       = stringResource(R.string.place_detail_add_to_itinerary),
                        color      = White,
                        fontSize   = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
            }
        }

        // ── Back button overlaid on hero ─────────────────────────────────────
        CircleIconButton(
            onClick     = onBackClick,
            fillColor   = White.copy(alpha = 0.9f),
            borderColor = null,
            modifier    = Modifier
                .align(Alignment.TopStart)
                .windowInsetsPadding(WindowInsets.statusBars)
                .padding(start = 18.dp, top = 8.dp),
        ) {
            Text(
                text       = stringResource(R.string.place_detail_back_glyph),
                color      = WaypointTextPrimary,
                fontSize   = 15.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

// ── Info card ─────────────────────────────────────────────────────────────────

@Composable
private fun InfoCard(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .background(WaypointCard, RoundedCornerShape(RadiusButton))
            .padding(horizontal = 14.dp, vertical = 12.dp),
    ) {
        Text(value, color = WaypointTextPrimary, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
        Text(label, color = WaypointTextMuted, fontSize = 10.sp, modifier = Modifier.padding(top = 4.dp))
    }
}
