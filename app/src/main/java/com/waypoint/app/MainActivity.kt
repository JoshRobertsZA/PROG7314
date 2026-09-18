package com.waypoint.app

import android.Manifest
import android.app.Activity
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import androidx.navigation.compose.rememberNavController
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.auth.AuthViewModel
import com.waypoint.app.core.auth.BiometricAuthenticator
import com.waypoint.app.core.auth.BiometricAvailability
import com.waypoint.app.core.auth.BiometricLockOverlay
import com.waypoint.app.core.auth.BiometricLockPreferences
import com.waypoint.app.core.common.OfflineDialog
import com.waypoint.app.core.connectivity.rememberIsOnline
import com.waypoint.app.core.navigation.MainNavShell
import com.waypoint.app.core.navigation.Routes
import com.waypoint.app.core.notifications.PushNotifier
import com.waypoint.app.core.notifications.PushTokenManager
import com.waypoint.app.core.notifications.WelcomeNotifier
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.core.theme.WaypointTheme
import com.waypoint.app.R
import com.waypoint.app.features.alltrips.ui.AllTripsScreen
import com.waypoint.app.features.edititinerary.ui.EditItineraryScreen
import com.waypoint.app.features.edititinerary.ui.EditItineraryViewModel
import com.waypoint.app.features.placepicker.ui.PlaceDetailPickerScreen
import com.waypoint.app.features.placepicker.ui.PlacePickerScreen
import com.waypoint.app.features.explore.ui.ExploreScreen
import com.waypoint.app.features.explore.ui.ExploreViewModel
import com.waypoint.app.features.main.ui.MainScreen
import com.waypoint.app.features.newtrip.ui.NewTripScreen
import com.waypoint.app.features.notifications.ui.NotificationsScreen
import com.waypoint.app.features.placedetail.ui.PlaceDetailScreen
import com.waypoint.app.features.settings.ui.SettingsScreen
import com.waypoint.app.features.tripcalendar.ui.TripCalendarScreen
import com.waypoint.app.features.viewitinerary.ui.ViewItineraryScreen
import com.waypoint.app.features.welcome.WelcomeScreen
import kotlinx.coroutines.launch

/**
 * The app's only Activity. Hosts a single flat NavHost (see
 * core/navigation/Routes.kt) covering every screen - replaces the old
 * per-screen Activity + Intent navigation entirely.
 */
class MainActivity : AppCompatActivity() {

    // Android 13+ needs explicit consent before any notification shows.
    // If a restored session already tried to post the welcome greeting
    // before the user answered, retry it now that we know the answer.
    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) WelcomeNotifier.notifyIfNeeded(applicationContext)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PushNotifier.hasPermission(this)) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
        // Logs the FCM token (tag WaypointFCM) for Firebase Console test sends.
        lifecycleScope.launch { PushTokenManager.fetchToken() }

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

    // Shared across Welcome and Register/Login-turned-SSO-only-flow - both
    // drive the same Firebase Google/GitHub Sign-In, see AuthViewModel.
    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()
    val context = LocalContext.current
    val activity = context as FragmentActivity

    // The biometric app-unlock gate (Settings' "Biometric login" toggle).
    // Only relevant once actually signed in - there's nothing to protect
    // before that - and only armed if the device can currently do
    // biometric auth at all, so a change in enrollment doesn't lock
    // someone out of an app they can no longer unlock.
    var biometricLockActive by remember { mutableStateOf(false) }

    fun armBiometricLockIfNeeded() {
        if (BiometricLockPreferences.isEnabled(context) &&
            BiometricAuthenticator.availability(context) == BiometricAvailability.AVAILABLE
        ) {
            biometricLockActive = true
        }
    }

    // Skip straight past onboarding if Firebase already has a session
    // from a previous launch (e.g. app was killed and reopened).
    LaunchedEffect(Unit) {
        authViewModel.restoreSessionIfSignedIn()
        if (AuthRepository.isSignedIn) {
            armBiometricLockIfNeeded()
            navController.navigate(Routes.Home) {
                popUpTo(Routes.Welcome) { inclusive = true }
            }
        }
    }

    // Re-lock every time the app is backgrounded (ON_STOP - not ON_PAUSE,
    // which also fires for transient things like a permission dialog or
    // pulling down the notification shade, which would be too aggressive).
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_STOP && AuthRepository.isSignedIn) {
                armBiometricLockIfNeeded()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    NavHost(
        navController = navController,
        startDestination = Routes.Welcome,
    ) {
        composable(Routes.Main) {
            MainScreen(
                onGoToWelcomeClick = { navController.navigate(Routes.Welcome) },
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
            WelcomeScreen(
                onGoogleContinueClick = {
                    authViewModel.signInWithGoogle(context) {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Welcome) { inclusive = true }
                        }
                    }
                },
                onGitHubContinueClick = {
                    authViewModel.signInWithGitHub(context as Activity) {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Welcome) { inclusive = true }
                        }
                    }
                },
                isLoading = authUiState.isLoading,
                loadingProvider = authUiState.loadingProvider,
                errorMessage = authUiState.errorMessage,
            )
        }
        composable(Routes.Home) {
            MainNavShell(
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate(Routes.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.NewTrip) {
            NewTripScreen(
                onCloseClick = { navController.popBackStack() },
                onSaveSuccess = {
                    // Pop back to the existing MainNavShell entry so the bottom nav is visible.
                    // Navigating to Routes.AllTrips would land on the standalone screen (no nav bar).
                    navController.popBackStack(route = Routes.Home, inclusive = false)
                },
            )
        }
        composable(
            route = Routes.TripCalendar,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType }),
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
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
            val editVm: EditItineraryViewModel = viewModel()
            EditItineraryScreen(
                tripId = tripId,
                onBackClick = { navController.popBackStack() },
                onAddPlaceClick = { category ->
                    val dayId = editVm.activeDayId ?: return@EditItineraryScreen
                    navController.navigate(Routes.placePicker(tripId, dayId, category))
                },
                viewModel = editVm,
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
            SettingsScreen(
                onLogoutClick = {
                    authViewModel.signOut()
                    navController.navigate(Routes.Welcome) {
                        popUpTo(0) { inclusive = true }
                    }
                },
            )
        }
        composable(Routes.PlaceDetail) {
            PlaceDetailScreen(onBackClick = { navController.popBackStack() })
        }
        composable(Routes.Notifications) {
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        }
        composable(
            route = Routes.PlacePicker,
            arguments = listOf(
                navArgument("tripId")   { type = NavType.StringType },
                navArgument("dayId")    { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            val tripId   = args.getString("tripId")   ?: ""
            val dayId    = args.getString("dayId")    ?: ""
            val category = args.getString("category") ?: ""
            PlacePickerScreen(
                tripId          = tripId,
                dayId           = dayId,
                category        = category,
                onBackClick     = { navController.popBackStack() },
                onPlaceSelected = { placeId ->
                    navController.navigate(Routes.placeDetailPicker(tripId, dayId, category, placeId))
                },
            )
        }
        composable(
            route = Routes.PlaceDetailPicker,
            arguments = listOf(
                navArgument("tripId")   { type = NavType.StringType },
                navArgument("dayId")    { type = NavType.StringType },
                navArgument("category") { type = NavType.StringType },
                navArgument("placeId")  { type = NavType.StringType },
            ),
        ) { backStackEntry ->
            val args = backStackEntry.arguments!!
            PlaceDetailPickerScreen(
                tripId      = args.getString("tripId")   ?: "",
                dayId       = args.getString("dayId")    ?: "",
                category    = args.getString("category") ?: "",
                placeId     = args.getString("placeId")  ?: "",
                onBackClick = { navController.popBackStack() },
                onPlaceAdded = { navController.popBackStack(); navController.popBackStack() },
            )
        }
    }

    if (!isOnline && !offlineDialogDismissed) {
        OfflineDialog(onDismissRequest = { offlineDialogDismissed = true })
    }

    AnimatedVisibility(
        visible = biometricLockActive,
        enter = fadeIn(animationSpec = tween(250)),
        exit = fadeOut(animationSpec = tween(250)),
    ) {
        val promptTitle = stringResource(R.string.biometric_lock_prompt_title)
        val promptSubtitle = stringResource(R.string.biometric_lock_prompt_subtitle)
        val cancelText = stringResource(R.string.biometric_lock_cancel)
        BiometricLockOverlay(
            onUnlockClick = {
                BiometricAuthenticator.authenticate(
                    activity = activity,
                    title = promptTitle,
                    subtitle = promptSubtitle,
                    negativeButtonText = cancelText,
                    onSuccess = { biometricLockActive = false },
                    onError = { _, _ -> /* stay locked - user can retry via the button */ },
                )
            },
        )
    }
}
