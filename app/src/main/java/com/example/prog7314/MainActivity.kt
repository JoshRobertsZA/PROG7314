package com.example.prog7314

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.prog7314.core.common.OfflineDialog
import com.example.prog7314.core.connectivity.rememberIsOnline
import com.example.prog7314.core.navigation.MainNavShell
import com.example.prog7314.core.navigation.Routes
import com.example.prog7314.core.theme.WaypointTheme
import com.example.prog7314.features.alltrips.ui.AllTripsScreen
import com.example.prog7314.features.edititinerary.ui.EditItineraryScreen
import com.example.prog7314.features.login.ui.LoginScreen
import com.example.prog7314.features.main.ui.MainScreen
import com.example.prog7314.features.nearbyplaces.ui.NearbyPlacesScreen
import com.example.prog7314.features.newtrip.ui.NewTripScreen
import com.example.prog7314.features.notifications.ui.NotificationsScreen
import com.example.prog7314.features.register.ui.RegisterScreen
import com.example.prog7314.features.settings.ui.SettingsScreen
import com.example.prog7314.features.tripcalendar.ui.TripCalendarScreen
import com.example.prog7314.features.viewitinerary.ui.ViewItineraryScreen
import com.example.prog7314.features.welcome.WelcomeScreen

/**
 * The app's only Activity. Hosts a single flat NavHost (see
 * core/navigation/Routes.kt) covering every screen - replaces the old
 * per-screen Activity + Intent navigation entirely.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            WaypointTheme {
                WaypointNavHost()
            }
        }
    }
}

@Composable
private fun WaypointNavHost(navController: NavHostController = rememberNavController()) {
    val isOnline by rememberIsOnline()
    var offlineDialogDismissed by remember { mutableStateOf(false) }

    // Reset dismissal once back online, so the dialog can show again the
    // next time connectivity actually drops, rather than being
    // permanently silenced after the first dismiss.
    LaunchedEffect(isOnline) {
        if (isOnline) offlineDialogDismissed = false
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Login,
    ) {
        composable(Routes.Main) {
            MainScreen(
                onGoToWelcomeClick = { navController.navigate(Routes.Welcome) },
                onGoToLoginClick = { navController.navigate(Routes.Login) },
                onGoToTripCalendarClick = { navController.navigate(Routes.TripCalendar) },
                onGoToViewItineraryClick = { navController.navigate(Routes.ViewItinerary) },
                onGoToEditItineraryClick = { navController.navigate(Routes.EditItinerary) },
                onGoToAllTripsClick = { navController.navigate(Routes.AllTrips) },
                onGoToSettingsClick = { navController.navigate(Routes.Settings) },
                onGoToNearbyPlacesClick = { navController.navigate(Routes.NearbyPlaces) },
                onGoToHomeClick = { navController.navigate(Routes.Home) },
                onGoToNotificationsClick = { navController.navigate(Routes.Notifications) },
            )
        }
        composable(Routes.Welcome) {
            // TODO: onGoogleContinueClick currently navigates straight to
            // Home as a placeholder, same as Login's stub. Replace with a
            // real Google Sign-In flow (and only navigate to Home on
            // success) once auth is implemented.
            WelcomeScreen(
                onGoogleContinueClick = { navController.navigate(Routes.Home) },
            )
        }
        composable(Routes.Login) {
            LoginScreen(
                // TODO: onGoogleSignInClick currently navigates straight to
                // Home as a placeholder. Replace with a real Google Sign-In
                // flow (and only navigate to Home on success) once auth is
                // implemented.
                onGoogleSignInClick = { navController.navigate(Routes.Home) },
                onCreateAccountClick = { navController.navigate(Routes.Register) },
            )
        }
        composable(Routes.Register) {
            RegisterScreen(
                // Google sign-up is still a stub; log in is real navigation
                // back to Login.
                onGoogleSignUpClick = {},
                onLogInClick = { navController.popBackStack() },
            )
        }
        composable(Routes.Home) {
            MainNavShell(
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
            )
        }
        composable(Routes.NewTrip) {
            NewTripScreen(onCloseClick = { navController.popBackStack() })
        }
        composable(Routes.TripCalendar) {
            TripCalendarScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.AllTrips) {
            AllTripsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.EditItinerary) {
            EditItineraryScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.ViewItinerary) {
            ViewItineraryScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.NearbyPlaces) {
            NearbyPlacesScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.Settings) {
            SettingsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.Notifications) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }
    }

    if (!isOnline && !offlineDialogDismissed) {
        OfflineDialog(onDismissRequest = { offlineDialogDismissed = true })
    }
}