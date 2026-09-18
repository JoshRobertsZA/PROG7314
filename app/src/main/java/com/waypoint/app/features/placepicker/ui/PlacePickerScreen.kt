package com.waypoint.app.features.placepicker.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Place picker — stub; full implementation in commit 3.
 * Reads the trip's cached LocationIQ places and filters by [category].
 */
@Composable
fun PlacePickerScreen(
    tripId: String,
    dayId: String,
    category: String,
    onBackClick: () -> Unit,
    onPlaceSelected: (placeId: String) -> Unit,
    modifier: Modifier = Modifier,
) {
    // TODO (commit 3): replace stub with real implementation
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text("Place picker – $category (coming in commit 3)")
    }
}
