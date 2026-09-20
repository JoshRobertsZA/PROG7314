// declares that this file belongs to the package `com.waypoint.app`
package com.waypoint.app

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.app.Activity` for use in this file
import android.app.Activity
// imports `android.os.Build` for use in this file
import android.os.Build
// imports `android.os.Bundle` for use in this file
import android.os.Bundle
// imports `androidx.activity.result.contract.ActivityResultContracts` for use in this file
import androidx.activity.result.contract.ActivityResultContracts
// imports `androidx.activity.compose.setContent` for use in this file
import androidx.activity.compose.setContent
// imports `androidx.activity.enableEdgeToEdge` for use in this file
import androidx.activity.enableEdgeToEdge
// imports `androidx.appcompat.app.AppCompatActivity` for use in this file
import androidx.appcompat.app.AppCompatActivity
// imports `androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen` for use in this file
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
// imports `androidx.compose.animation.AnimatedVisibility` for use in this file
import androidx.compose.animation.AnimatedVisibility
// imports `androidx.compose.animation.core.tween` for use in this file
import androidx.compose.animation.core.tween
// imports `androidx.compose.animation.fadeIn` for use in this file
import androidx.compose.animation.fadeIn
// imports `androidx.compose.animation.fadeOut` for use in this file
import androidx.compose.animation.fadeOut
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.DisposableEffect` for use in this file
import androidx.compose.runtime.DisposableEffect
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.collectAsState` for use in this file
import androidx.compose.runtime.collectAsState
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.fragment.app.FragmentActivity` for use in this file
import androidx.fragment.app.FragmentActivity
// imports `androidx.lifecycle.Lifecycle` for use in this file
import androidx.lifecycle.Lifecycle
// imports `androidx.lifecycle.LifecycleEventObserver` for use in this file
import androidx.lifecycle.LifecycleEventObserver
// imports `androidx.lifecycle.compose.LocalLifecycleOwner` for use in this file
import androidx.lifecycle.compose.LocalLifecycleOwner
// imports `androidx.lifecycle.lifecycleScope` for use in this file
import androidx.lifecycle.lifecycleScope
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `androidx.navigation.NavHostController` for use in this file
import androidx.navigation.NavHostController
// imports `androidx.navigation.compose.NavHost` for use in this file
import androidx.navigation.compose.NavHost
// imports `androidx.navigation.compose.composable` for use in this file
import androidx.navigation.compose.composable
// imports `androidx.navigation.NavType` for use in this file
import androidx.navigation.NavType
// imports `androidx.navigation.navArgument` for use in this file
import androidx.navigation.navArgument
// imports `androidx.navigation.compose.rememberNavController` for use in this file
import androidx.navigation.compose.rememberNavController
// imports `com.waypoint.app.core.auth.AuthRepository` for use in this file
import com.waypoint.app.core.auth.AuthRepository
// imports `com.waypoint.app.core.auth.AuthViewModel` for use in this file
import com.waypoint.app.core.auth.AuthViewModel
// imports `com.waypoint.app.core.auth.BiometricAuthenticator` for use in this file
import com.waypoint.app.core.auth.BiometricAuthenticator
// imports `com.waypoint.app.core.auth.BiometricAvailability` for use in this file
import com.waypoint.app.core.auth.BiometricAvailability
// imports `com.waypoint.app.core.auth.BiometricLockOverlay` for use in this file
import com.waypoint.app.core.auth.BiometricLockOverlay
// imports `com.waypoint.app.core.auth.BiometricLockPreferences` for use in this file
import com.waypoint.app.core.auth.BiometricLockPreferences
// imports `com.waypoint.app.core.common.OfflineDialog` for use in this file
import com.waypoint.app.core.common.OfflineDialog
// imports `com.waypoint.app.core.connectivity.rememberIsOnline` for use in this file
import com.waypoint.app.core.connectivity.rememberIsOnline
// imports `com.waypoint.app.core.navigation.MainNavShell` for use in this file
import com.waypoint.app.core.navigation.MainNavShell
// imports `com.waypoint.app.core.navigation.Routes` for use in this file
import com.waypoint.app.core.navigation.Routes
// imports `com.waypoint.app.core.notifications.PushNotifier` for use in this file
import com.waypoint.app.core.notifications.PushNotifier
// imports `com.waypoint.app.core.notifications.PushTokenManager` for use in this file
import com.waypoint.app.core.notifications.PushTokenManager
// imports `com.waypoint.app.core.notifications.WelcomeNotifier` for use in this file
import com.waypoint.app.core.notifications.WelcomeNotifier
// imports `com.waypoint.app.core.secrets.RemoteSecrets` for use in this file
import com.waypoint.app.core.secrets.RemoteSecrets
// imports `com.waypoint.app.core.theme.WaypointTheme` for use in this file
import com.waypoint.app.core.theme.WaypointTheme
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.features.alltrips.ui.AllTripsScreen` for use in this file
import com.waypoint.app.features.alltrips.ui.AllTripsScreen
// imports `com.waypoint.app.features.edititinerary.ui.EditItineraryScreen` for use in this file
import com.waypoint.app.features.edititinerary.ui.EditItineraryScreen
// imports `com.waypoint.app.features.edititinerary.ui.EditItineraryViewModel` for use in this file
import com.waypoint.app.features.edititinerary.ui.EditItineraryViewModel
// imports `com.waypoint.app.features.placepicker.ui.PlaceDetailPickerScreen` for use in this file
import com.waypoint.app.features.placepicker.ui.PlaceDetailPickerScreen
// imports `com.waypoint.app.features.placepicker.ui.PlacePickerScreen` for use in this file
import com.waypoint.app.features.placepicker.ui.PlacePickerScreen
// imports `com.waypoint.app.features.explore.ui.ExploreScreen` for use in this file
import com.waypoint.app.features.explore.ui.ExploreScreen
// imports `com.waypoint.app.features.explore.ui.ExploreViewModel` for use in this file
import com.waypoint.app.features.explore.ui.ExploreViewModel
// imports `com.waypoint.app.features.main.ui.MainScreen` for use in this file
import com.waypoint.app.features.main.ui.MainScreen
// imports `com.waypoint.app.features.newtrip.ui.NewTripScreen` for use in this file
import com.waypoint.app.features.newtrip.ui.NewTripScreen
// imports `com.waypoint.app.features.notifications.ui.NotificationsScreen` for use in this file
import com.waypoint.app.features.notifications.ui.NotificationsScreen
// imports `com.waypoint.app.features.placedetail.ui.PlaceDetailScreen` for use in this file
import com.waypoint.app.features.placedetail.ui.PlaceDetailScreen
// imports `com.waypoint.app.features.settings.ui.SettingsScreen` for use in this file
import com.waypoint.app.features.settings.ui.SettingsScreen
// imports `com.waypoint.app.features.splash.ui.SplashScreen` for use in this file
import com.waypoint.app.features.splash.ui.SplashScreen
// imports `com.waypoint.app.features.tripcalendar.ui.TripCalendarScreen` for use in this file
import com.waypoint.app.features.tripcalendar.ui.TripCalendarScreen
// imports `com.waypoint.app.features.viewitinerary.ui.ViewItineraryScreen` for use in this file
import com.waypoint.app.features.viewitinerary.ui.ViewItineraryScreen
// imports `com.waypoint.app.features.welcome.WelcomeScreen` for use in this file
import com.waypoint.app.features.welcome.WelcomeScreen
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// declares class `MainActivity`, inheriting from `AppCompatActivity()` and opens its body
class MainActivity : AppCompatActivity() {

    // declares private read-only property `notificationPermission`
    private val notificationPermission =
        // continues the statement started above: `registerForActivityResult(ActivityResultContracts.RequestPe…`
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            // continues the statement started above: `if (granted) WelcomeNotifier.notifyIfNeeded(applicationCont…`
            if (granted) WelcomeNotifier.notifyIfNeeded(applicationContext)
        // closes the block
        }

    // declares override function `onCreate` taking 1 parameter (`savedInstanceState`) and opens its body
    override fun onCreate(savedInstanceState: Bundle?) {
        // declares read-only property `splashScreen`, initialised with the result of calling `installSplashScreen(…)`
        val splashScreen = installSplashScreen()
        // expression: `splashScreen.setKeepOnScreenCondition { false }`
        splashScreen.setKeepOnScreenCondition { false }
        // calls `onCreate` on `super` with arguments `(savedInstanceState)`
        super.onCreate(savedInstanceState)
        // calls `enableEdgeToEdge` with arguments `()`
        enableEdgeToEdge()

        // `if` statement: the block below runs when `Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PushNotifie…` is true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PushNotifier.hasPermission(this)) {
            // calls `launch` on `notificationPermission` with arguments `(Manifest.permission.POST_NOTIFICATIONS)`
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        // closes the if block
        }
        // expression: `lifecycleScope.launch { PushTokenManager.fetchToken() }`
        lifecycleScope.launch { PushTokenManager.fetchToken() }

        // expression: `lifecycleScope.launch { RemoteSecrets.ensureLoaded() }`
        lifecycleScope.launch { RemoteSecrets.ensureLoaded() }

        // opens a block after `setContent`
        setContent {
            // opens a block after `WaypointTheme`
            WaypointTheme {
                // calls `WaypointNavHost` with arguments `()`
                WaypointNavHost()
            // closes the block
            }
        // closes the block
        }
    // closes the function `onCreate`
    }
// closes the class `MainActivity`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `WaypointNavHost` taking 1 parameter (`navController`) and opens its body
private fun WaypointNavHost(navController: NavHostController = rememberNavController()) {
    // declares read-only property `isOnline`, delegated to `rememberIsOnline()`
    val isOnline by rememberIsOnline()
    // declares mutable property `offlineDialogDismissed`, delegated to `remember { mutableStateOf(false) }`
    var offlineDialogDismissed by remember { mutableStateOf(false) }

    // calls `LaunchedEffect` with arguments `(isOnline)` and opens a trailing lambda / block
    LaunchedEffect(isOnline) {
        // `if` statement: executes `offlineDialogDismissed = false` when `isOnline` is true
        if (isOnline) offlineDialogDismissed = false
    // closes the lambda passed to `LaunchedEffect`
    }

    // declares read-only property `authViewModel` of type `AuthViewModel`, initialised with the result of calling `viewModel(…)`
    val authViewModel: AuthViewModel = viewModel()
    // declares read-only property `authUiState`, delegated to `authViewModel.uiState.collectAsState()`
    val authUiState by authViewModel.uiState.collectAsState()
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares read-only property `activity`, initialised to `context as FragmentActivity`
    val activity = context as FragmentActivity

    // declares mutable property `biometricLockActive`, delegated to `remember { mutableStateOf(false) }`
    var biometricLockActive by remember { mutableStateOf(false) }

    // declares function `armBiometricLockIfNeeded` taking no parameters and opens its body
    fun armBiometricLockIfNeeded() {
        // calls `if` with an argument list that continues on the following lines
        if (BiometricLockPreferences.isEnabled(context) &&
            // continues the statement started above: `BiometricAuthenticator.availability(context) == BiometricAv…`
            BiometricAuthenticator.availability(context) == BiometricAvailability.AVAILABLE
        // ends the argument list started above and opens the block that follows
        ) {
            // assigns `biometricLockActive` the value `true`
            biometricLockActive = true
        // closes the block
        }
    // closes the function `armBiometricLockIfNeeded`
    }

    // declares mutable property `sessionRestored`, delegated to `remember { mutableStateOf(false) }`
    var sessionRestored by remember { mutableStateOf(false) }
    // declares mutable property `splashAnimationDone`, delegated to `remember { mutableStateOf(false) }`
    var splashAnimationDone by remember { mutableStateOf(false) }
    // declares mutable property `hasLeftSplash`, delegated to `remember { mutableStateOf(false) }`
    var hasLeftSplash by remember { mutableStateOf(false) }

    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // calls `restoreSessionIfSignedIn` on `authViewModel` with arguments `()`
        authViewModel.restoreSessionIfSignedIn()
        // assigns `sessionRestored` the value `true`
        sessionRestored = true
    // closes the lambda passed to `LaunchedEffect`
    }

    // calls `LaunchedEffect` with arguments `(sessionRestored, splashAnimationDone)` and opens a trailing lambda / block
    LaunchedEffect(sessionRestored, splashAnimationDone) {
        // `if` statement: the block below runs when `sessionRestored && splashAnimationDone && !hasLeftSplash` is true
        if (sessionRestored && splashAnimationDone && !hasLeftSplash) {
            // assigns `hasLeftSplash` the value `true`
            hasLeftSplash = true
            // `if` statement: the block below runs when `AuthRepository.isSignedIn` is true
            if (AuthRepository.isSignedIn) {
                // calls `armBiometricLockIfNeeded` with arguments `()`
                armBiometricLockIfNeeded()
                // calls `navigate` on `navController` with arguments `(Routes.Home)` and opens a trailing lambda / block
                navController.navigate(Routes.Home) {
                    // calls `popUpTo` with arguments `(Routes.Splash)`
                    popUpTo(Routes.Splash) { inclusive = true }
                // closes the lambda passed to `navigate`
                }
            // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
            } else {
                // calls `navigate` on `navController` with arguments `(Routes.Welcome)` and opens a trailing lambda / block
                navController.navigate(Routes.Welcome) {
                    // calls `popUpTo` with arguments `(Routes.Splash)`
                    popUpTo(Routes.Splash) { inclusive = true }
                // closes the lambda passed to `navigate`
                }
            // closes the else branch
            }
        // closes the if block
        }
    // closes the lambda passed to `LaunchedEffect`
    }

    // declares read-only property `lifecycleOwner`, initialised to `LocalLifecycleOwner.current`
    val lifecycleOwner = LocalLifecycleOwner.current
    // calls `DisposableEffect` with arguments `(lifecycleOwner)` and opens a trailing lambda / block
    DisposableEffect(lifecycleOwner) {
        // declares read-only property `observer`, initialised to a lambda / arrow function
        val observer = LifecycleEventObserver { _, event ->
            // continues the statement started above: `if (event == Lifecycle.Event.ON_STOP && AuthRepository.isSi…`
            if (event == Lifecycle.Event.ON_STOP && AuthRepository.isSignedIn) {
                // calls `armBiometricLockIfNeeded` with arguments `()`
                armBiometricLockIfNeeded()
            // closes the block
            }
        // closes the block
        }
        // calls `addObserver` on `lifecycleOwner.lifecycle` with arguments `(observer)`
        lifecycleOwner.lifecycle.addObserver(observer)
        // expression: `onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }`
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    // closes the lambda passed to `DisposableEffect`
    }

    // calls `NavHost` with an argument list that continues on the following lines
    NavHost(
        // continues the statement started above: `navController = navController,`
        navController = navController,
        // continues the statement started above: `startDestination = Routes.Splash,`
        startDestination = Routes.Splash,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `composable` with arguments `(Routes.Splash)` and opens a trailing lambda / block
        composable(Routes.Splash) {
            // calls `SplashScreen` with arguments `(onFinished = { splashAnimationDone = true })`
            SplashScreen(onFinished = { splashAnimationDone = true })
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.Main)` and opens a trailing lambda / block
        composable(Routes.Main) {
            // calls `MainScreen` with an argument list that continues on the following lines
            MainScreen(
                // continues the statement started above: `onGoToWelcomeClick = { navController.navigate(Routes.Welcom…`
                onGoToWelcomeClick = { navController.navigate(Routes.Welcome) },
                // continues the statement started above: `onGoToTripCalendarClick = { navController.navigate(Routes.t…`
                onGoToTripCalendarClick = { navController.navigate(Routes.tripCalendar("__scratch__")) },
                // continues the statement started above: `onGoToViewItineraryClick = { navController.navigate(Routes.…`
                onGoToViewItineraryClick = { navController.navigate(Routes.viewItinerary("__scratch__")) },
                // continues the statement started above: `onGoToEditItineraryClick = { navController.navigate(Routes.…`
                onGoToEditItineraryClick = { navController.navigate(Routes.editItinerary("__scratch__")) },
                // continues the statement started above: `onGoToAllTripsClick = { navController.navigate(Routes.AllTr…`
                onGoToAllTripsClick = { navController.navigate(Routes.AllTrips) },
                // continues the statement started above: `onGoToSettingsClick = { navController.navigate(Routes.Setti…`
                onGoToSettingsClick = { navController.navigate(Routes.Settings) },
                // continues the statement started above: `onGoToExploreClick = { navController.navigate(Routes.Explor…`
                onGoToExploreClick = { navController.navigate(Routes.Explore) },
                // continues the statement started above: `onGoToHomeClick = { navController.navigate(Routes.Home) },`
                onGoToHomeClick = { navController.navigate(Routes.Home) },
                // continues the statement started above: `onGoToNotificationsClick = { navController.navigate(Routes.…`
                onGoToNotificationsClick = { navController.navigate(Routes.Notifications) },
                // continues the statement started above: `onGoToPlaceDetailClick = { navController.navigate(Routes.Pl…`
                onGoToPlaceDetailClick = { navController.navigate(Routes.PlaceDetail) },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.Welcome)` and opens a trailing lambda / block
        composable(Routes.Welcome) {
            // calls `WelcomeScreen` with an argument list that continues on the following lines
            WelcomeScreen(
                // continues the statement started above: `onGoogleContinueClick = {`
                onGoogleContinueClick = {
                    // calls `signInWithGoogle` on `authViewModel` with arguments `(context)` and opens a trailing lambda / block
                    authViewModel.signInWithGoogle(context) {
                        // calls `navigate` on `navController` with arguments `(Routes.Home)` and opens a trailing lambda / block
                        navController.navigate(Routes.Home) {
                            // calls `popUpTo` with arguments `(Routes.Welcome)`
                            popUpTo(Routes.Welcome) { inclusive = true }
                        // closes the lambda passed to `navigate`
                        }
                    // closes the lambda passed to `signInWithGoogle`
                    }
                // closes the block
                },
                // continues the statement started above: `onGitHubContinueClick = {`
                onGitHubContinueClick = {
                    // calls `signInWithGitHub` on `authViewModel` with arguments `(context as Activity)` and opens a trailing lambda / block
                    authViewModel.signInWithGitHub(context as Activity) {
                        // calls `navigate` on `navController` with arguments `(Routes.Home)` and opens a trailing lambda / block
                        navController.navigate(Routes.Home) {
                            // calls `popUpTo` with arguments `(Routes.Welcome)`
                            popUpTo(Routes.Welcome) { inclusive = true }
                        // closes the lambda passed to `navigate`
                        }
                    // closes the lambda passed to `signInWithGitHub`
                    }
                // closes the block
                },
                // continues the statement started above: `isLoading = authUiState.isLoading,`
                isLoading = authUiState.isLoading,
                // continues the statement started above: `loadingProvider = authUiState.loadingProvider,`
                loadingProvider = authUiState.loadingProvider,
                // continues the statement started above: `errorMessage = authUiState.errorMessage,`
                errorMessage = authUiState.errorMessage,
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.Home)` and opens a trailing lambda / block
        composable(Routes.Home) {
            // calls `MainNavShell` with an argument list that continues on the following lines
            MainNavShell(
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
                onNotificationsClick = { navController.navigate(Routes.Notifications) },
                onLogoutClick = {
                    // calls `signOut` on `authViewModel` with arguments `()`
                    authViewModel.signOut()
                    // calls `navigate` on `navController` with arguments `(Routes.Welcome)` and opens a trailing lambda / block
                    navController.navigate(Routes.Welcome) {
                        // calls `popUpTo` with arguments `(0)`
                        popUpTo(0) { inclusive = true }
                    // closes the lambda passed to `navigate`
                    }
                // closes the block
                },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.NewTrip)` and opens a trailing lambda / block
        composable(Routes.NewTrip) {
            // calls `NewTripScreen` with an argument list that continues on the following lines
            NewTripScreen(
                // continues the statement started above: `onCloseClick = { navController.popBackStack() },`
                onCloseClick = { navController.popBackStack() },
                // continues the statement started above: `onSaveSuccess = {`
                onSaveSuccess = {
                    // calls `popBackStack` on `navController` with arguments `(route = Routes.Home, inclusive = false)`
                    navController.popBackStack(route = Routes.Home, inclusive = false)
                // closes the block
                },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with an argument list that continues on the following lines
        composable(
            // continues the statement started above: `route = Routes.TripCalendar,`
            route = Routes.TripCalendar,
            // continues the statement started above: `arguments = listOf(`
            arguments = listOf(
                // continues the statement started above: `navArgument("tripId") { type = NavType.StringType },`
                navArgument("tripId") { type = NavType.StringType },
                // continues the statement started above: `navArgument("selectedDates") { type = NavType.StringType; d…`
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            // closes the multi-line argument list started above
            ),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `TripCalendarScreen` with an argument list that continues on the following lines
            TripCalendarScreen(
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick = { navController.popBackStack() },
                // continues the statement started above: `onEditItineraryClick = { tripId, selectedDates -> navContro…`
                onEditItineraryClick = { tripId, selectedDates -> navController.navigate(Routes.editItinerary(tripId, selectedDates)) },
                // continues the statement started above: `onViewItineraryClick = { tripId -> navController.navigate(R…`
                onViewItineraryClick = { tripId -> navController.navigate(Routes.viewItinerary(tripId)) },
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // calls `composable` with arguments `(Routes.AllTrips)` and opens a trailing lambda / block
        composable(Routes.AllTrips) {
            // calls `AllTripsScreen` with an argument list that continues on the following lines
            AllTripsScreen(
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick    = { navController.popBackStack() },
                // continues the statement started above: `onNewTripClick = { navController.navigate(Routes.NewTrip) },`
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
                // continues the statement started above: `onTripClick = { tripId -> navController.navigate(Routes.tri…`
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with an argument list that continues on the following lines
        composable(
            // continues the statement started above: `route = Routes.EditItinerary,`
            route = Routes.EditItinerary,
            // continues the statement started above: `arguments = listOf(`
            arguments = listOf(
                // continues the statement started above: `navArgument("tripId") { type = NavType.StringType },`
                navArgument("tripId") { type = NavType.StringType },
                // continues the statement started above: `navArgument("selectedDates") { type = NavType.StringType; d…`
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            // closes the multi-line argument list started above
            ),
        // continues the statement started above: `) { backStackEntry ->`
        ) { backStackEntry ->
            // continues the statement started above: `val tripId = backStackEntry.arguments?.getString("tripId") …`
            val tripId = backStackEntry.arguments?.getString("tripId") ?: ""
            // declares read-only property `editVm` of type `EditItineraryViewModel`, initialised with the result of calling `viewModel(…)`
            val editVm: EditItineraryViewModel = viewModel()
            // calls `EditItineraryScreen` with an argument list that continues on the following lines
            EditItineraryScreen(
                // continues the statement started above: `tripId = tripId,`
                tripId = tripId,
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick = { navController.popBackStack() },
                // continues the statement started above: `onAddPlaceClick = { category ->`
                onAddPlaceClick = { category ->
                    // continues the statement started above: `val dayId = editVm.activeDayId ?: return@EditItineraryScreen`
                    val dayId = editVm.activeDayId ?: return@EditItineraryScreen
                    // continues the statement started above: `navController.navigate(Routes.placePicker(tripId, dayId, ca…`
                    navController.navigate(Routes.placePicker(tripId, dayId, category))
                // closes the block
                },
                // continues the statement started above: `viewModel = editVm,`
                viewModel = editVm,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // calls `composable` with an argument list that continues on the following lines
        composable(
            // continues the statement started above: `route = Routes.ViewItinerary,`
            route = Routes.ViewItinerary,
            // continues the statement started above: `arguments = listOf(`
            arguments = listOf(
                // continues the statement started above: `navArgument("tripId") { type = NavType.StringType },`
                navArgument("tripId") { type = NavType.StringType },
                // continues the statement started above: `navArgument("selectedDates") { type = NavType.StringType; d…`
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            // closes the multi-line argument list started above
            ),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `ViewItineraryScreen` with an argument list that continues on the following lines
            ViewItineraryScreen(
                // continues the statement started above: `tripId = it.arguments?.getString("tripId") ?: "",`
                tripId = it.arguments?.getString("tripId") ?: "",
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick = { navController.popBackStack() },
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // calls `composable` with arguments `(Routes.Explore)` and opens a trailing lambda / block
        composable(Routes.Explore) {
            // calls `ExploreScreen` with arguments `(exploreViewModel = viewModel<ExploreViewMode…)`
            ExploreScreen(exploreViewModel = viewModel<ExploreViewModel>())
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.Settings)` and opens a trailing lambda / block
        composable(Routes.Settings) {
            // calls `SettingsScreen` with an argument list that continues on the following lines
            SettingsScreen(
                // continues the statement started above: `onLogoutClick = {`
                onLogoutClick = {
                    // calls `signOut` on `authViewModel` with arguments `()`
                    authViewModel.signOut()
                    // calls `navigate` on `navController` with arguments `(Routes.Welcome)` and opens a trailing lambda / block
                    navController.navigate(Routes.Welcome) {
                        // calls `popUpTo` with arguments `(0)`
                        popUpTo(0) { inclusive = true }
                    // closes the lambda passed to `navigate`
                    }
                // closes the block
                },
            // closes the multi-line argument list started above
            )
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.PlaceDetail)` and opens a trailing lambda / block
        composable(Routes.PlaceDetail) {
            // calls `PlaceDetailScreen` with arguments `(onBackClick = { navController.popBackStack()…)`
            PlaceDetailScreen(onBackClick = { navController.popBackStack() })
        // closes the lambda passed to `composable`
        }
        // calls `composable` with arguments `(Routes.Notifications)` and opens a trailing lambda / block
        composable(Routes.Notifications) {
            // calls `NotificationsScreen` with arguments `(onBackClick = { navController.popBackStack()…)`
            NotificationsScreen(onBackClick = { navController.popBackStack() })
        // closes the lambda passed to `composable`
        }
        // calls `composable` with an argument list that continues on the following lines
        composable(
            // continues the statement started above: `route = Routes.PlacePicker,`
            route = Routes.PlacePicker,
            // continues the statement started above: `arguments = listOf(`
            arguments = listOf(
                // continues the statement started above: `navArgument("tripId") { type = NavType.StringType },`
                navArgument("tripId")   { type = NavType.StringType },
                // continues the statement started above: `navArgument("dayId") { type = NavType.StringType },`
                navArgument("dayId")    { type = NavType.StringType },
                // continues the statement started above: `navArgument("category") { type = NavType.StringType },`
                navArgument("category") { type = NavType.StringType },
            // closes the multi-line argument list started above
            ),
        // continues the statement started above: `) { backStackEntry ->`
        ) { backStackEntry ->
            // continues the statement started above: `val args = backStackEntry.arguments!!`
            val args = backStackEntry.arguments!!
            // declares read-only property `tripId`, initialised with the result of calling `args.getString(…)`
            val tripId   = args.getString("tripId")   ?: ""
            // declares read-only property `dayId`, initialised with the result of calling `args.getString(…)`
            val dayId    = args.getString("dayId")    ?: ""
            // declares read-only property `category`, initialised with the result of calling `args.getString(…)`
            val category = args.getString("category") ?: ""
            // calls `PlacePickerScreen` with an argument list that continues on the following lines
            PlacePickerScreen(
                // continues the statement started above: `tripId = tripId,`
                tripId          = tripId,
                // continues the statement started above: `dayId = dayId,`
                dayId           = dayId,
                // continues the statement started above: `category = category,`
                category        = category,
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick     = { navController.popBackStack() },
                // continues the statement started above: `onPlaceSelected = { placeId ->`
                onPlaceSelected = { placeId ->
                    // continues the statement started above: `navController.navigate(Routes.placeDetailPicker(tripId, day…`
                    navController.navigate(Routes.placeDetailPicker(tripId, dayId, category, placeId))
                // closes the block
                },
            // closes the multi-line argument list started above
            )
        // closes the block
        }
        // calls `composable` with an argument list that continues on the following lines
        composable(
            // continues the statement started above: `route = Routes.PlaceDetailPicker,`
            route = Routes.PlaceDetailPicker,
            // continues the statement started above: `arguments = listOf(`
            arguments = listOf(
                // continues the statement started above: `navArgument("tripId") { type = NavType.StringType },`
                navArgument("tripId")   { type = NavType.StringType },
                // continues the statement started above: `navArgument("dayId") { type = NavType.StringType },`
                navArgument("dayId")    { type = NavType.StringType },
                // continues the statement started above: `navArgument("category") { type = NavType.StringType },`
                navArgument("category") { type = NavType.StringType },
                // continues the statement started above: `navArgument("placeId") { type = NavType.StringType },`
                navArgument("placeId")  { type = NavType.StringType },
            // closes the multi-line argument list started above
            ),
        // continues the statement started above: `) { backStackEntry ->`
        ) { backStackEntry ->
            // continues the statement started above: `val args = backStackEntry.arguments!!`
            val args = backStackEntry.arguments!!
            // calls `PlaceDetailPickerScreen` with an argument list that continues on the following lines
            PlaceDetailPickerScreen(
                // continues the statement started above: `tripId = args.getString("tripId") ?: "",`
                tripId      = args.getString("tripId")   ?: "",
                // continues the statement started above: `dayId = args.getString("dayId") ?: "",`
                dayId       = args.getString("dayId")    ?: "",
                // continues the statement started above: `category = args.getString("category") ?: "",`
                category    = args.getString("category") ?: "",
                // continues the statement started above: `placeId = args.getString("placeId") ?: "",`
                placeId     = args.getString("placeId")  ?: "",
                // continues the statement started above: `onBackClick = { navController.popBackStack() },`
                onBackClick = { navController.popBackStack() },
                // continues the statement started above: `onPlaceAdded = { navController.popBackStack(); navControlle…`
                onPlaceAdded = { navController.popBackStack(); navController.popBackStack() },
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the block
    }

    // `if` statement: the block below runs when `!isOnline && !offlineDialogDismissed` is true
    if (!isOnline && !offlineDialogDismissed) {
        // calls `OfflineDialog` with arguments `(onDismissRequest = { offlineDialogDismissed …)`
        OfflineDialog(onDismissRequest = { offlineDialogDismissed = true })
    // closes the if block
    }

    // calls `AnimatedVisibility` with an argument list that continues on the following lines
    AnimatedVisibility(
        // continues the statement started above: `visible = biometricLockActive,`
        visible = biometricLockActive,
        // continues the statement started above: `enter = fadeIn(animationSpec = tween(250)),`
        enter = fadeIn(animationSpec = tween(250)),
        // continues the statement started above: `exit = fadeOut(animationSpec = tween(250)),`
        exit = fadeOut(animationSpec = tween(250)),
    // ends the argument list started above and opens the block that follows
    ) {
        // declares read-only property `promptTitle`, initialised with the result of calling `stringResource(…)`
        val promptTitle = stringResource(R.string.biometric_lock_prompt_title)
        // declares read-only property `promptSubtitle`, initialised with the result of calling `stringResource(…)`
        val promptSubtitle = stringResource(R.string.biometric_lock_prompt_subtitle)
        // declares read-only property `cancelText`, initialised with the result of calling `stringResource(…)`
        val cancelText = stringResource(R.string.biometric_lock_cancel)
        // calls `BiometricLockOverlay` with an argument list that continues on the following lines
        BiometricLockOverlay(
            // continues the statement started above: `onUnlockClick = {`
            onUnlockClick = {
                // calls `authenticate` on `BiometricAuthenticator` with an argument list that continues on the following lines
                BiometricAuthenticator.authenticate(
                    // continues the statement started above: `activity = activity,`
                    activity = activity,
                    // continues the statement started above: `title = promptTitle,`
                    title = promptTitle,
                    // continues the statement started above: `subtitle = promptSubtitle,`
                    subtitle = promptSubtitle,
                    // continues the statement started above: `negativeButtonText = cancelText,`
                    negativeButtonText = cancelText,
                    // continues the statement started above: `onSuccess = { biometricLockActive = false },`
                    onSuccess = { biometricLockActive = false },
                    // continues the statement started above: `onError = { _, _ -> },`
                    onError = { _, _ ->  },
                // closes the multi-line argument list started above
                )
            // closes the block
            },
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `WaypointNavHost`
}
