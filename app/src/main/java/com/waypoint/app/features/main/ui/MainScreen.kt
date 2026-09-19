// declares that this file belongs to the package `com.waypoint.app.features.main.ui`
package com.waypoint.app.features.main.ui

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.common.AppButtonFilled` for use in this file
import com.waypoint.app.core.common.AppButtonFilled
// imports `com.waypoint.app.core.theme.WaypointCream` for use in this file
import com.waypoint.app.core.theme.WaypointCream

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun MainScreen(`
fun MainScreen(
    // continues the statement started above: `onGoToTripCalendarClick: () -> Unit,`
    onGoToTripCalendarClick: () -> Unit,
    // continues the statement started above: `onGoToViewItineraryClick: () -> Unit,`
    onGoToViewItineraryClick: () -> Unit,
    // continues the statement started above: `onGoToEditItineraryClick: () -> Unit,`
    onGoToEditItineraryClick: () -> Unit,
    // continues the statement started above: `onGoToAllTripsClick: () -> Unit,`
    onGoToAllTripsClick: () -> Unit,
    // continues the statement started above: `onGoToSettingsClick: () -> Unit,`
    onGoToSettingsClick: () -> Unit,
    // continues the statement started above: `onGoToExploreClick: () -> Unit,`
    onGoToExploreClick: () -> Unit,
    // continues the statement started above: `onGoToHomeClick: () -> Unit,`
    onGoToHomeClick: () -> Unit,
    // continues the statement started above: `onGoToWelcomeClick: () -> Unit,`
    onGoToWelcomeClick: () -> Unit,
    // continues the statement started above: `onGoToNotificationsClick: () -> Unit,`
    onGoToNotificationsClick: () -> Unit,
    // continues the statement started above: `onGoToPlaceDetailClick: () -> Unit,`
    onGoToPlaceDetailClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(WaypointCream)`
            .background(WaypointCream)
            // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
            .windowInsetsPadding(WindowInsets.systemBars)
            // continues the statement started above: `.padding(16.dp),`
            .padding(16.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
        // continues the statement started above: `verticalArrangement = Arrangement.Center,`
        verticalArrangement = Arrangement.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Text` with arguments `("Hello World!")`
        Text("Hello World!")

        // calls `AppButtonFilled` with arguments `(text = "Go to Trip Calendar", onClick = onGo…)`
        AppButtonFilled(text = "Go to Trip Calendar", onClick = onGoToTripCalendarClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to View Itinerary", onClick = onG…)`
        AppButtonFilled(text = "Go to View Itinerary", onClick = onGoToViewItineraryClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to Edit Itinerary", onClick = onG…)`
        AppButtonFilled(text = "Go to Edit Itinerary", onClick = onGoToEditItineraryClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to All Trips", onClick = onGoToAl…)`
        AppButtonFilled(text = "Go to All Trips", onClick = onGoToAllTripsClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to Settings", onClick = onGoToSet…)`
        AppButtonFilled(text = "Go to Settings", onClick = onGoToSettingsClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to Explore", onClick = onGoToExpl…)`
        AppButtonFilled(text = "Go to Explore", onClick = onGoToExploreClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to Welcome", onClick = onGoToWelc…)`
        AppButtonFilled(text = "Go to Welcome", onClick = onGoToWelcomeClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = stringResource(R.string.main_go_to_ho…)`
        AppButtonFilled(text = stringResource(R.string.main_go_to_home), onClick = onGoToHomeClick, modifier = Modifier.padding(top = 16.dp))
        // calls `AppButtonFilled` with arguments `(text = "Go to Notifications", onClick = onGo…)`
        AppButtonFilled(text = "Go to Notifications", onClick = onGoToNotificationsClick, modifier = Modifier.padding(top = 16.dp))

        // calls `AppButtonFilled` with arguments `(text = "Go to Place Detail", onClick = onGoT…)`
        AppButtonFilled(text = "Go to Place Detail", onClick = onGoToPlaceDetailClick, modifier = Modifier.padding(top = 16.dp))
    // closes the block
    }
// closes the block
}