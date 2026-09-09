package com.example.prog7314.features.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.prog7314.R
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.theme.WaypointCream

/**
 * Entry screen. Frontend skeleton only: static/mock content, no real
 * home/first screen decided yet.
 *
 * TODO: btnGoToLogin/etc. are all temporary scratch navigation for
 * testing individual screens, bypassing the real login/home flow. Remove
 * once every screen has a real nav path into it from elsewhere in the
 * app (see the TODOs on each individual button below, carried over
 * verbatim from MainActivity.kt).
 */
@Composable
fun MainScreen(
    onGoToLoginClick: () -> Unit,
    onGoToTripCalendarClick: () -> Unit,
    onGoToViewItineraryClick: () -> Unit,
    onGoToEditItineraryClick: () -> Unit,
    onGoToAllTripsClick: () -> Unit,
    onGoToSettingsClick: () -> Unit,
    onGoToNearbyPlacesClick: () -> Unit,
    onGoToHomeClick: () -> Unit,
    onGoToWelcomeClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Hello World!")

        // Temporary: navigate to the login screen until the real home/first
        // screen for the app is decided.
        AppButtonFilled(text = "Go to Login", onClick = onGoToLoginClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: jump straight to the trip calendar screen for testing,
        // bypassing login/home. Remove once there's a real nav path to it.
        AppButtonFilled(text = "Go to Trip Calendar", onClick = onGoToTripCalendarClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: jump straight to the view itinerary screen for
        // testing, bypassing login/home/calendar. Remove once there's a
        // real nav path into it.
        AppButtonFilled(text = "Go to View Itinerary", onClick = onGoToViewItineraryClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the edit itinerary screen for
        // testing until it's wired into the app's real navigation.
        AppButtonFilled(text = "Go to Edit Itinerary", onClick = onGoToEditItineraryClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the all trips screen for testing
        // until it's wired into the app's real navigation.
        AppButtonFilled(text = "Go to All Trips", onClick = onGoToAllTripsClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the settings screen for testing
        // until it's wired into the app's real navigation.
        AppButtonFilled(text = "Go to Settings", onClick = onGoToSettingsClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the nearby places screen for
        // testing until it's wired into the app's real navigation.
        AppButtonFilled(text = "Go to Nearby Places", onClick = onGoToNearbyPlacesClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the welcome screen for testing
        // until it's wired into the app's real navigation.
        AppButtonFilled(text = "Go to Welcome", onClick = onGoToWelcomeClick, modifier = Modifier.padding(top = 16.dp))

        // Temporary: navigate directly to the home screen for testing
        // (bypassing login) so the shared bottom nav is reachable without
        // going through the login stub. Remove once there's a real nav
        // path into it.
        AppButtonFilled(text = stringResource(R.string.main_go_to_home), onClick = onGoToHomeClick, modifier = Modifier.padding(top = 16.dp))
    }
}
