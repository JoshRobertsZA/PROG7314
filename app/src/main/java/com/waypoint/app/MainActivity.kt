package com.waypoint.app

import android.util.Log
import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.SystemBarStyle
import androidx.compose.foundation.isSystemInDarkTheme
import com.waypoint.app.core.theme.ThemePreferences
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.MutableState
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
import com.waypoint.app.core.auth.SessionEvents
import com.waypoint.app.core.auth.AuthViewModel
import com.waypoint.app.core.auth.BiometricAuthenticator
import com.waypoint.app.core.auth.BiometricFailure
import com.waypoint.app.core.auth.biometricFailureMessage
import com.waypoint.app.core.auth.classifyBiometricError
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
import com.waypoint.app.core.sync.SyncScheduler
import com.waypoint.app.core.theme.WaypointTheme
import com.waypoint.app.R
import com.waypoint.app.features.edititinerary.ui.EditItineraryScreen
import com.waypoint.app.features.edititinerary.ui.EditItineraryViewModel
import com.waypoint.app.features.placepicker.ui.PlaceDetailPickerScreen
import com.waypoint.app.features.placepicker.ui.PlacePickerScreen
import com.waypoint.app.features.newtrip.ui.NewTripScreen
import com.waypoint.app.features.notifications.ui.NotificationsScreen
import com.waypoint.app.features.splash.ui.SplashScreen
import com.waypoint.app.features.tripcalendar.ui.TripCalendarScreen
import com.waypoint.app.features.viewitinerary.ui.ViewItineraryScreen
import com.waypoint.app.features.welcome.WelcomeScreen
import com.waypoint.app.features.auth.ui.EmailAuthScreen
import com.waypoint.app.features.auth.ui.EmailAuthSubmission
import com.waypoint.app.features.profile.ui.DeleteAccountDialog
import com.waypoint.app.features.profile.ui.EditProfileScreen
import com.waypoint.app.features.profile.ui.ProfileViewModel
import com.waypoint.app.core.db.SessionManager
import kotlinx.coroutines.launch

/**
 * Single activity hosting the Compose navigation graph. Installs the splash screen, asks for the
 * notification permission, and opens the trip a tapped notification points to (via the `tripId`
 * extra).
 */
class MainActivity : AppCompatActivity() {

    private val notificationPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
            if (granted) WelcomeNotifier.notifyIfNeeded(applicationContext)
        }

    // Set from a push's "tripId" data extra (see PushNotifier.show, which
    // puts the whole data payload into the launch intent's extras) - read
    // once on cold start and again on every onNewIntent, so a tap works
    // whether the app was dead or already running.
    private val pendingTripId = mutableStateOf<String?>(null)

    private fun requestNotificationPermissionIfNeeded() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && !PushNotifier.hasPermission(this)) {
            notificationPermission.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        pendingTripId.value = intent.getStringExtra("tripId")
        Log.d(TAG, "onNewIntent: opened from a notification for trip ${pendingTripId.value}")
    }

    // Lifecycle logging: shows in Logcat (tag "MainActivity") when the app is opened, backgrounded and
    // reopened, which is also when the biometric lock is armed and shown.
    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart: app visible")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume: app in the foreground")
    }

    override fun onPause() {
        Log.d(TAG, "onPause: app leaving the foreground")
        super.onPause()
    }

    override fun onStop() {
        Log.d(TAG, "onStop: app in the background (biometric lock armed if enabled)")
        super.onStop()
    }

    override fun onDestroy() {
        Log.d(TAG, "onDestroy: activity destroyed (finishing=$isFinishing)")
        super.onDestroy()
    }

    private companion object {
        const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(TAG, "onCreate: ${if (savedInstanceState == null) "cold start" else "recreated (e.g. language change)"}")
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition { false }
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        requestNotificationPermissionIfNeeded()
        lifecycleScope.launch { PushTokenManager.fetchToken() }

        lifecycleScope.launch { RemoteSecrets.ensureLoaded() }

        // Cold start from a tapped push: the extra is already on the intent
        // that created this Activity. A warm-start tap instead goes through
        // onNewIntent above.
        pendingTripId.value = intent?.getStringExtra("tripId")

        setContent {
            val darkTheme = ThemePreferences.override ?: isSystemInDarkTheme()
            // runs again whenever `darkTheme` changes so the status and navigation bar icons match the chosen theme
            DisposableEffect(darkTheme) {
                enableEdgeToEdge(
                    statusBarStyle = SystemBarStyle.auto(android.graphics.Color.TRANSPARENT, android.graphics.Color.TRANSPARENT) { darkTheme },
                    navigationBarStyle = SystemBarStyle.auto(
                        android.graphics.Color.argb(0xe6, 0xFF, 0xFF, 0xFF),
                        android.graphics.Color.argb(0x80, 0x1b, 0x1b, 0x1b),
                    ) { darkTheme },
                )
                // returns an empty clean-up because nothing needs undoing
                onDispose {}
            }
            WaypointTheme(darkTheme = darkTheme) {
                WaypointNavHost(
                    pendingTripId = pendingTripId,
                    onRequestNotificationPermission = ::requestNotificationPermissionIfNeeded,
                )
            }
        }
    }
}

@Composable
private fun WaypointNavHost(
    navController: NavHostController = rememberNavController(),
    pendingTripId: MutableState<String?> = remember { mutableStateOf(null) },
    onRequestNotificationPermission: () -> Unit = {},
) {
    val isOnline by rememberIsOnline()
    var offlineDialogDismissed by remember { mutableStateOf(false) }

    LaunchedEffect(isOnline) {
        if (isOnline) offlineDialogDismissed = false
    }

    val authViewModel: AuthViewModel = viewModel()
    val authUiState by authViewModel.uiState.collectAsState()
    val emailAuthUiState by authViewModel.emailAuthUiState.collectAsState()
    val context = LocalContext.current

    // Reconnect trigger: WorkManager's own NetworkType.CONNECTED constraint
    // already blocks the worker from running while offline, so this only
    // needs to make sure a request is queued by the time connectivity
    // returns - it doesn't need to track "was offline, now online"
    // transitions itself.
    LaunchedEffect(isOnline) {
        if (isOnline) SyncScheduler.enqueue(context)
    }

    // The API rejected our token (revoked, disabled or deleted account), so end the session.
    LaunchedEffect(Unit) {
        SessionEvents.unauthorized.collect {
            if (!AuthRepository.isSignedIn) return@collect
            authViewModel.signOut()
            android.widget.Toast.makeText(context, R.string.session_expired, android.widget.Toast.LENGTH_LONG).show()
            navController.navigate(Routes.Welcome) { popUpTo(0) { inclusive = true } }
        }
    }
    val activity = context as FragmentActivity

    var biometricLockActive by remember { mutableStateOf(false) }
    // the reason the last unlock attempt failed, shown on the lock screen
    var biometricMessage by remember { mutableStateOf<String?>(null) }

    fun armBiometricLockIfNeeded() {
        if (BiometricLockPreferences.isEnabled() &&
            BiometricAuthenticator.availability(context) == BiometricAvailability.AVAILABLE
        ) {
            biometricLockActive = true
        }
    }

    var sessionRestored by remember { mutableStateOf(false) }
    var splashAnimationDone by remember { mutableStateOf(false) }
    var hasLeftSplash by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        authViewModel.restoreSessionIfSignedIn()
        sessionRestored = true
    }

    LaunchedEffect(sessionRestored, splashAnimationDone) {
        if (sessionRestored && splashAnimationDone && !hasLeftSplash) {
            hasLeftSplash = true
            if (AuthRepository.isSignedIn) {
                armBiometricLockIfNeeded()
                navController.navigate(Routes.Home) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            } else {
                navController.navigate(Routes.Welcome) {
                    popUpTo(Routes.Splash) { inclusive = true }
                }
            }
        }
    }

    // Opens the trip a push notification was tapped for, once there's
    // somewhere to navigate from (splash has handed off to Home/Welcome)
    // and someone is actually signed in to view it. Runs again whenever
    // pendingTripId changes, which covers both a cold start (set before
    // first composition) and a warm-start tap via onNewIntent.
    LaunchedEffect(hasLeftSplash, pendingTripId.value) {
        val tripId = pendingTripId.value
        if (hasLeftSplash && AuthRepository.isSignedIn && tripId != null) {
            navController.navigate(Routes.tripCalendar(tripId))
            pendingTripId.value = null
        }
    }

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
        startDestination = Routes.Splash,
    ) {
        composable(Routes.Splash) {
            SplashScreen(onFinished = { splashAnimationDone = true })
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
                onEmailContinueClick = { navController.navigate(Routes.EmailAuth) },
                isLoading = authUiState.isLoading,
                loadingProvider = authUiState.loadingProvider,
                errorMessage = authUiState.errorMessage,
            )
        }
        composable(Routes.EmailAuth) {
            EmailAuthScreen(
                onBackClick = { navController.popBackStack() },
                onSubmit = { submission: EmailAuthSubmission ->
                    authViewModel.submitEmailAuth(submission) {
                        navController.navigate(Routes.Home) {
                            popUpTo(Routes.Welcome) { inclusive = true }
                        }
                    }
                },
                isLoading = emailAuthUiState.isLoading,
                errorMessage = emailAuthUiState.errorMessage,
                resetEmailSent = emailAuthUiState.resetEmailSent,
            )
        }
        composable(Routes.Home) {
            MainNavShell(
                onNewTripClick = { navController.navigate(Routes.NewTrip) },
                onTripClick    = { tripId -> navController.navigate(Routes.tripCalendar(tripId)) },
                onNotificationsClick = { navController.navigate(Routes.Notifications) },
                onEditProfileClick = { navController.navigate(Routes.EditProfile) },
                onRequestNotificationPermission = onRequestNotificationPermission,
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
                    navController.popBackStack(route = Routes.Home, inclusive = false)
                },
            )
        }
        composable(
            route = Routes.TripCalendar,
            arguments = listOf(
                navArgument("tripId") { type = NavType.StringType },
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            ),
        ) {
            TripCalendarScreen(
                onBackClick = { navController.popBackStack() },
                onEditItineraryClick = { tripId, selectedDates -> navController.navigate(Routes.editItinerary(tripId, selectedDates)) },
                onViewItineraryClick = { tripId -> navController.navigate(Routes.viewItinerary(tripId)) },
            )
        }
        composable(
            route = Routes.EditItinerary,
            arguments = listOf(
                navArgument("tripId") { type = NavType.StringType },
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            ),
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
            arguments = listOf(
                navArgument("tripId") { type = NavType.StringType },
                navArgument("selectedDates") { type = NavType.StringType; defaultValue = "" },
            ),
        ) {
            ViewItineraryScreen(
                tripId = it.arguments?.getString("tripId") ?: "",
                onBackClick = { navController.popBackStack() },
            )
        }
        composable(Routes.EditProfile) {
            val profileViewModel: ProfileViewModel = viewModel()
            val profileUiState by profileViewModel.uiState.collectAsState()
            val deleteAccountUiState by profileViewModel.deleteAccountUiState.collectAsState()
            var showDeleteDialog by remember { mutableStateOf(false) }

            EditProfileScreen(
                onBackClick = { navController.popBackStack() },
                onSave = { name -> profileViewModel.saveDisplayName(name) { navController.popBackStack() } },
                initialDisplayName = SessionManager.displayName,
                photoUrl = SessionManager.photoUrl,
                isSaving = profileUiState.isSaving,
                errorMessage = profileUiState.errorMessage,
                onDeleteAccountClick = { showDeleteDialog = true },
            )

            if (showDeleteDialog) {
                val requiresPassword = AuthRepository.currentSignInProviderIds.contains("password")
                DeleteAccountDialog(
                    onDismiss = { showDeleteDialog = false },
                    onConfirmWithPassword = { password ->
                        profileViewModel.deleteAccountWithPassword(password) {
                            navController.navigate(Routes.Welcome) { popUpTo(0) { inclusive = true } }
                        }
                    },
                    onConfirmWithGoogle = {
                        profileViewModel.deleteAccountWithGoogle(context) {
                            navController.navigate(Routes.Welcome) { popUpTo(0) { inclusive = true } }
                        }
                    },
                    canDelete = profileViewModel.canDeleteAccount(),
                    requiresPassword = requiresPassword,
                    isDeleting = deleteAccountUiState.isDeleting,
                    errorMessage = deleteAccountUiState.errorMessage,
                )
            }
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
        val failureMessages = BiometricFailure.values().associateWith { stringResource(biometricFailureMessage(it)) }
        BiometricLockOverlay(
            message = biometricMessage,
            onUnlockClick = {
                BiometricAuthenticator.authenticate(
                    activity = activity,
                    title = promptTitle,
                    subtitle = promptSubtitle,
                    onSuccess = {
                        biometricMessage = null
                        biometricLockActive = false
                    },
                    onError = { code, _ ->
                        biometricMessage = failureMessages[classifyBiometricError(code)]
                    },
                )
            },
        )
    }
}
