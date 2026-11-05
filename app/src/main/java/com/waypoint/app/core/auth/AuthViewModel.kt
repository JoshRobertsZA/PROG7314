package com.waypoint.app.core.auth

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.BuildConfig
import com.waypoint.app.R
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.core.locale.AppLanguage
import com.waypoint.app.core.notifications.PushTokenManager
import com.waypoint.app.core.notifications.WelcomeNotifier
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.core.sync.SyncScheduler
import com.waypoint.app.features.newtrip.data.TripRepository
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.FirebaseUser
import com.waypoint.app.features.auth.ui.EmailAuthSubmission
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

/** The social sign-in button that is currently loading, so only that button shows a spinner. */
enum class AuthProvider {
    GOOGLE, GITHUB
}

/** State of the welcome screen's Google and GitHub sign-in buttons. */
data class AuthUiState(
    val isLoading: Boolean = false,
    val loadingProvider: AuthProvider? = null,
    val errorMessage: String? = null,
)

/** State of the email sign-in, sign-up and password-reset form. */
data class EmailAuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val resetEmailSent: Boolean = false,
)

/**
 * Runs every sign-in flow, maps Firebase errors to friendly messages, restores the session on launch,
 * and after sign-in registers the device for push and pulls the user's trips.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val tripRepository = TripRepository(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    private val _emailAuthUiState = MutableStateFlow(EmailAuthUiState())
    val emailAuthUiState: StateFlow<EmailAuthUiState> = _emailAuthUiState.asStateFlow()

    suspend fun restoreSessionIfSignedIn() {
        AuthRepository.currentUser?.let { applySignedInUser(it); return }
        try {
            AuthRepository.recoverPendingGitHubSignIn()?.let { applySignedInUser(it) }
        } catch (e: Exception) {
            Log.w(TAG, "No recoverable GitHub sign-in", e)
        }
    }

    fun signInWithGoogle(context: Context, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadingProvider = AuthProvider.GOOGLE, errorMessage = null) }
            try {
                RemoteSecrets.ensureLoaded()
                val idToken = fetchGoogleIdToken(context)
                val user = AuthRepository.signInWithGoogleIdToken(idToken)
                applySignedInUser(user)
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
                onSuccess()
            } catch (e: GetCredentialCancellationException) {
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
            } catch (e: Exception) {
                Log.w(TAG, "Google sign-in failed", e)
                _uiState.update {
                    it.copy(isLoading = false, loadingProvider = null, errorMessage = "Couldn't sign in with Google. Please try again.")
                }
            }
        }
    }

    fun signInWithGitHub(activity: Activity, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, loadingProvider = AuthProvider.GITHUB, errorMessage = null) }
            try {
                val user = AuthRepository.signInWithGitHub(activity)
                applySignedInUser(user)
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
                onSuccess()
            } catch (e: FirebaseAuthWebException) {
                _uiState.update { it.copy(isLoading = false, loadingProvider = null) }
            } catch (e: Exception) {
                Log.w(TAG, "GitHub sign-in failed", e)
                _uiState.update {
                    it.copy(isLoading = false, loadingProvider = null, errorMessage = "Couldn't sign in with GitHub. Please try again.")
                }
            }
        }
    }

    fun submitEmailAuth(submission: EmailAuthSubmission, onSuccess: () -> Unit) {
        if (_emailAuthUiState.value.isLoading) return
        when (submission) {
            is EmailAuthSubmission.SignUp -> signUpWithEmail(submission, onSuccess)
            is EmailAuthSubmission.SignIn -> signInWithEmail(submission, onSuccess)
            is EmailAuthSubmission.Reset -> sendPasswordReset(submission)
        }
    }

    private fun signUpWithEmail(submission: EmailAuthSubmission.SignUp, onSuccess: () -> Unit) {
        val validationError = EmailAuthValidator.validateSignUp(
            submission.displayName, submission.email, submission.password, submission.confirmPassword,
        )
        if (validationError != null) {
            _emailAuthUiState.update { it.copy(errorMessage = getApplication<Application>().getString(validationError)) }
            return
        }
        viewModelScope.launch {
            _emailAuthUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = AuthRepository.signUpWithEmail(submission.email.trim(), submission.password, submission.displayName.trim())
                applySignedInUser(user)
                _emailAuthUiState.update { EmailAuthUiState() }
                onSuccess()
            } catch (e: Exception) {
                Log.w(TAG, "Email sign-up failed", e)
                _emailAuthUiState.update { it.copy(isLoading = false, errorMessage = getApplication<Application>().getString(mapEmailAuthError(e))) }
            }
        }
    }

    private fun signInWithEmail(submission: EmailAuthSubmission.SignIn, onSuccess: () -> Unit) {
        val validationError = EmailAuthValidator.validateSignIn(submission.email, submission.password)
        if (validationError != null) {
            _emailAuthUiState.update { it.copy(errorMessage = getApplication<Application>().getString(validationError)) }
            return
        }
        viewModelScope.launch {
            _emailAuthUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val user = AuthRepository.signInWithEmail(submission.email.trim(), submission.password)
                applySignedInUser(user)
                _emailAuthUiState.update { EmailAuthUiState() }
                onSuccess()
            } catch (e: Exception) {
                Log.w(TAG, "Email sign-in failed", e)
                _emailAuthUiState.update { it.copy(isLoading = false, errorMessage = getApplication<Application>().getString(mapEmailAuthError(e))) }
            }
        }
    }

    private fun sendPasswordReset(submission: EmailAuthSubmission.Reset) {
        val validationError = EmailAuthValidator.validateReset(submission.email)
        if (validationError != null) {
            _emailAuthUiState.update { it.copy(errorMessage = getApplication<Application>().getString(validationError)) }
            return
        }
        viewModelScope.launch {
            _emailAuthUiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                AuthRepository.sendPasswordResetEmail(submission.email.trim())
                _emailAuthUiState.update { it.copy(isLoading = false, resetEmailSent = true) }
            } catch (e: Exception) {
                Log.w(TAG, "Password reset email failed", e)
                _emailAuthUiState.update { it.copy(isLoading = false, errorMessage = getApplication<Application>().getString(mapEmailAuthError(e))) }
            }
        }
    }

    // FirebaseAuthWeakPasswordException is a subtype of
    // FirebaseAuthInvalidCredentialsException, so it has to be checked first.
    // No branch distinguishes "wrong password" from "malformed email" on
    // sign-in - both throw FirebaseAuthInvalidCredentialsException - so that
    // case falls back to the wrong-password message, which is the more
    // common cause and was already validated client-side for format.
    private fun mapEmailAuthError(e: Exception): Int = when (e) {
        is FirebaseAuthWeakPasswordException -> R.string.auth_error_password_too_short
        is FirebaseAuthUserCollisionException -> R.string.auth_error_email_in_use
        is FirebaseAuthInvalidUserException -> R.string.auth_error_user_not_found
        is FirebaseAuthInvalidCredentialsException -> R.string.auth_error_wrong_password
        is FirebaseNetworkException -> R.string.auth_error_network
        else -> R.string.auth_error_generic
    }

    private suspend fun fetchGoogleIdToken(context: Context): String =
        try {
            GoogleSignInHelper.requestIdToken(context, filterByAuthorizedAccounts = true)
        } catch (e: NoCredentialException) {
            GoogleSignInHelper.requestIdToken(context, filterByAuthorizedAccounts = false)
        }

    private fun applySignedInUser(user: FirebaseUser) {
        SessionManager.signIn(
            id = user.uid,
            email = user.email.orEmpty(),
            displayName = user.displayName.orEmpty(),
            photoUrl = user.photoUrl?.toString().orEmpty(),
        )
        WelcomeNotifier.notifyIfNeeded(getApplication())
        viewModelScope.launch(Dispatchers.IO) {
            tripRepository.upsertAccount(
                AccountEntity(
                    id = user.uid,
                    email = user.email.orEmpty(),
                    displayName = user.displayName.orEmpty(),
                    photoUrl = user.photoUrl?.toString().orEmpty(),
                    lastLoginMs = System.currentTimeMillis(),
                )
            )
        }
        viewModelScope.launch(Dispatchers.IO) {
            val token = runCatching { user.getIdToken(false).await()?.token }.getOrNull()
            if (BuildConfig.DEBUG && token != null) Log.d(SWAGGER_TAG, token)
            if (token != null) {
                tripRepository.pullAndMergeTrips(token, user.uid)
                // Register this device for push now, reusing the ID token
                // already fetched above rather than asking for another one.
                val fcmToken = PushTokenManager.currentToken ?: PushTokenManager.fetchToken()
                if (fcmToken != null) {
                    WaypointApiRepository.registerDevice(token, fcmToken, AppLanguage.current().localeTag)
                }
            }
            // Drains anything queued from a previous session (app killed
            // before it synced) and lets WorkManager's own network
            // constraint hold it until connectivity is actually available.
            SyncScheduler.enqueue(getApplication())
        }
    }

    fun signOut() {
        // Captured before AuthRepository.signOut() clears the current user -
        // a FirebaseUser instance can still hand back its (cached, not
        // force-refreshed) ID token afterwards, but AuthRepository.getIdToken()
        // goes through auth.currentUser, which would already be null by then.
        val userBeforeSignOut = AuthRepository.currentUser
        val deviceToken = PushTokenManager.currentToken
        if (userBeforeSignOut != null && deviceToken != null) {
            // Fire-and-forget: logout must not wait on a network call, and
            // the UI has already navigated away by the time this completes.
            viewModelScope.launch(Dispatchers.IO) {
                val idToken = runCatching { userBeforeSignOut.getIdToken(false).await()?.token }.getOrNull()
                if (idToken != null) WaypointApiRepository.unregisterDevice(idToken, deviceToken)
            }
        }
        AuthRepository.signOut(getApplication())
        SessionManager.signOut()
        WelcomeNotifier.reset()
    }

    private companion object {
        const val TAG = "AuthViewModel"
        const val SWAGGER_TAG = "SWAGGER_TOKEN"
    }
}
