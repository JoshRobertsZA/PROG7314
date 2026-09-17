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
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.prog7314.core.common.OfflineDialog
import com.example.prog7314.core.connectivity.rememberIsOnline
import com.example.prog7314.core.navigation.MainNavShell
import com.example.prog7314.core.navigation.Routes
import com.example.prog7314.core.secrets.RemoteSecrets
import com.example.prog7314.core.theme.WaypointTheme
import com.example.prog7314.features.alltrips.ui.AllTripsScreen
import com.example.prog7314.features.edititinerary.ui.EditItineraryScreen
import com.example.prog7314.features.explore.ui.ExploreScreen
import com.example.prog7314.features.explore.ui.ExploreViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.prog7314.features.login.ui.LoginScreen
import com.example.prog7314.features.main.ui.MainScreen
import com.example.prog7314.features.newtrip.ui.NewTripScreen
import com.example.prog7314.features.notifications.ui.NotificationsScreen
import com.example.prog7314.features.placedetail.ui.PlaceDetailScreen
import com.example.prog7314.features.register.ui.RegisterScreen
import com.example.prog7314.features.settings.ui.SettingsScreen
import com.example.prog7314.features.tripcalendar.ui.TripCalendarScreen
import com.example.prog7314.features.viewitinerary.ui.ViewItineraryScreen
import com.example.prog7314.features.welcome.WelcomeScreen
import kotlinx.coroutines.launch

/**
 * The app's only Activity. Hosts a single flat NavHost (see
 * core/navigation/Routes.kt) covering every screen - replaces the old
 * per-screen Activity + Intent navigation entirely.
 */
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Kick off the shared-key fetch as early as possible, in the
        // background. Screens that need a key call RemoteSecrets.get(),
        // which falls back to "" until this finishes (or if it fails) -
        // see RemoteSecrets.kt for the local-override behaviour.
        lifecycleScope.launch { RemoteSecrets.ensureLoaded() }

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
                onGoToTripCalendarClick = { navController.navigate(Routes.tripCalendar("__scratch__")) },
                onGoToViewItineraryClick = { navController.navigate(Routes.viewItinerary("__scratch__")) },
                onGoToEditItineraryClick = { navController.navigate(Routes.editItinerary("__scratch__")) },
                onGoToAllTripsClick = { navController.navigate(Routes.AllTrips) },
                onGoToSettingsClick = { navController.navigate(Routes.Settings) },
                onGoToExploreClick = { navController.navigate(Routes.Explore) },
                onGoToHomeClick = { navController.navigate(Routes.Home) },
                onGoToNotificationsClick = { navController.navigate(Routes.Notifications) },
                onGoToPlaceDetailClick = { navController.navigate(Routes.PlaceDetail) },
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
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
            )
        }
        composable(Routes.NewTrip) {
            NewTripScreen(
                onCloseClick = { navController.popBackStack() },
                onSaveSuccess = {
                    navController.navigate(Routes.AllTrips) {
                        popUpTo(Routes.NewTrip) { inclusive = true }
                    }
                },
            )
        }
        composable(
            route = Routes.TripCalendar,
            arguments = listOf(androidx.navigation.navArgument("tripId") { type = androidx.navigation.NavType.StringType }),
        ) {
            TripCalendarScreen(
                onBackClick = { navController.popBackStack() },
                onEditItineraryClick = { tripId -> navController.navigate(Routes.editItinerary(tripId)) },
                onViewItineraryClick = { tripId -> navController.navigate(Routes.viewItinerary(tripId)) },
            )
        }
        composable(Routes.AllTrips) {
            AllTripsScreen(
                onBackClick    = { navController.popBackStack() },
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
            )
        }
        composable(
            route = Routes.EditItinerary,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
        ) {
            EditItineraryScreen(
                tripId = it.arguments?.getString("tripId") ?: "",
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(
            route = Routes.ViewItinerary,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
        ) {
            ViewItineraryScreen(
                tripId = it.arguments?.getString("tripId") ?: "",
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Routes.Explore) {
            // ExploreScreen (the Explore tab root) has no back arrow of its
            // own, matching Figma - system back still pops this off the
            // stack when reached from the debug scratch hub.
            ExploreScreen(exploreViewModel = viewModel<ExploreViewModel>())
        }
        composable(Routes.Settings) {
            // SettingsScreen (the Profile tab root) has no back arrow of its
            // own, matching Figma - system back still pops this off the
            // stack when reached from the debug scratch hub.
            SettingsScreen()
        }
        composable(Routes.PlaceDetail) {
            PlaceDetailScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.Notifications) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }
    }

    if (!isOnline && !offlineDialogDismissed) {
        OfflineDialog(onDismissRequest = { offlineDialogDismissed = true })
    }

}
