package com.waypoint.app.features.placepicker.ui

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
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.R
import com.waypoint.app.core.cache.ExplorePlace
import com.waypoint.app.core.common.CircleIconButton
import com.waypoint.app.core.common.RowSurface
import com.waypoint.app.core.common.ThumbnailBlock
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointPlaceAccent1
import com.waypoint.app.core.theme.WaypointPlaceAccent2
import com.waypoint.app.core.theme.WaypointPlaceAccent3
import com.waypoint.app.core.theme.WaypointPlaceAccent4
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary

private val accentCycle = listOf(
    WaypointPlaceAccent1, WaypointPlaceAccent2, WaypointPlaceAccent3, WaypointPlaceAccent4,
)

@Composable
fun PlacePickerScreen(
    tripId: String,
    dayId: String,
    category: String,
    onBackClick: () -> Unit,
    onPlaceSelected: (placeId: String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: PlacePickerViewModel = viewModel(),
) {
    val state by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 22.dp, top = 28.dp, end = 22.dp, bottom = 32.dp),
    ) {
        // ── Top bar ──────────────────────────────────────────────────────────
        Box(modifier = Modifier.fillMaxWidth()) {
            CircleIconButton(
                onClick  = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Text(
                    text       = stringResource(R.string.edit_itinerary_back_glyph),
                    color      = WaypointTerracotta,
                    fontSize   = 15.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
            Text(
                text       = state.categoryLabel,
                color      = WaypointTextPrimary,
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier   = Modifier.align(Alignment.Center),
            )
        }

        Spacer(Modifier.height(20.dp))

        // ── Content ──────────────────────────────────────────────────────────
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            when (val s = state.loadState) {
                PickerLoadState.Loading -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator(color = WaypointTerracotta)
                    }
                }

                PickerLoadState.Error -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text     = "This trip has no destination set. Edit your trip to add one.",
                            color    = WaypointTextMuted,
                            fontSize = 13.sp,
                        )
                    }
                }

                PickerLoadState.Empty -> {
                    Box(
                        modifier         = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text     = "No ${state.categoryLabel.lowercase()} found near your destination.",
                            color    = WaypointTextMuted,
                            fontSize = 13.sp,
                        )
                    }
                }

                is PickerLoadState.Success -> {
                    s.places.forEachIndexed { index, place ->
                        PlaceRow(
                            place   = place,
                            index   = index,
                            onClick = { onPlaceSelected(place.id) },
                        )
                    }
                }
            }
        }
    }
}

// ── Place row ─────────────────────────────────────────────────────────────────

@Composable
private fun PlaceRow(
    place   : ExplorePlace,
    index   : Int,
    onClick : () -> Unit,
) {
    RowSurface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = if (index == 0) 0.dp else 8.dp)
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier          = Modifier.padding(
                start  = 10.dp,
                top    = 10.dp,
                end    = 14.dp,
                bottom = 10.dp,
            ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ThumbnailBlock(
                accentColor = accentCycle[index % accentCycle.size],
                label       = placeTypeEmoji(place.type),
            )
            Column(modifier = Modifier.padding(start = 12.dp)) {
                Text(
                    text       = place.name,
                    color      = WaypointTextPrimary,
                    fontSize   = 13.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text     = placeSubtitle(place),
                    color    = WaypointTextMuted,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(top = 3.dp),
                )
            }
        }
    }
}

// ── Helpers ───────────────────────────────────────────────────────────────────

private fun placeSubtitle(place: ExplorePlace): String {
    val kind = place.type
        .replace("_", " ")
        .replaceFirstChar { it.uppercase() }
        .ifBlank { "Place" }
    return if (place.distanceMetres > 0) {
        "$kind · ${"%.1f".format(place.distanceMetres / 1000.0)} km away"
    } else {
        kind
    }
}

private fun placeTypeEmoji(type: String): String = when (type) {
    "hotel"      -> "🏨"  // 🏨
    "restaurant" -> "🍽"  // 🍽️
    "pub"    -> "🍺"  // 🍺
    "cinema" -> "🎬"  // 🎬
    "park"   -> "🌳"  // 🌳
    else     -> "📍"  // 📍
}
