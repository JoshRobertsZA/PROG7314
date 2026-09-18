package com.waypoint.app.features.placepicker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Place detail picker — stub; full implementation in commit 4.
 * Shows Wikipedia summary, weather, map link, and "+ Add to itinerary" for a
 * specific place selected from [PlacePickerScreen].
 */
@Composable
fun PlaceDetailPickerScreen(
    tripId: String,
    dayId: String,
    category: String,
    placeId: String,
    onBackClick: () -> Unit,
    onPlaceAdded: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO (commit 4): replace stub with real implementation
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Place detail – $placeId (coming in commit 4)")
    }
}
