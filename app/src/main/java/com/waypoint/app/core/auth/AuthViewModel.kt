package com.waypoint.app.core.auth

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.notifications.WelcomeNotifier
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.newtrip.data.TripRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AuthUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
)

/**
 * Shared by the Welcome, Login and Register screens - all three offer the
 * same "Continue with Google" action, just with different copy around it.
 * Drives Firebase Auth via [AuthRepository] and mirrors the signed-in
 * account into the local `accounts` table via [TripRepository] so the rest
 * of the app (which keys everything off [SessionManager.accountId]) keeps
 * working exactly as it did with the placeholder account.
 */
class AuthViewModel(application: Application) : AndroidViewModel(application) {

    private val tripRepository = TripRepository(application)

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState.asStateFlow()

    /**
     * Called once on app start. If Firebase already has a signed-in user
     * (a previous session), restores [SessionManager] from it so the app
     * can skip straight past onboarding.
     */
    fun restoreSessionIfSignedIn() {
        val user = AuthRepository.currentUser ?: return
        applySignedInUser(user)
    }

    fun signInWithGoogle(context: Context, onSuccess: () -> Unit) {
        if (_uiState.value.isLoading) return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                // FIREBASE_WEB_CLIENT_ID may come from the shared remote key
                // cache rather than a local override (see apikey.properties.example),
                // which is fetched async on app start - make sure that fetch
                // has actually resolved before requesting a credential, so a
                // fast tap right after launch doesn't race an empty client id.
                RemoteSecrets.ensureLoaded()
                val idToken = fetchGoogleIdToken(context)
                val user = AuthRepository.signInWithGoogleIdToken(idToken)
                applySignedInUser(user)
                _uiState.update { it.copy(isLoading = false) }
                onSuccess()
            } catch (e: GetCredentialCancellationException) {
                // User dismissed the account picker - not an error worth showing.
                _uiState.update { it.copy(isLoading = false) }
            } catch (e: Exception) {
                Log.w(TAG, "Google sign-in failed", e)
                _uiState.update {
                    it.copy(isLoading = false, errorMessage = "Couldn't sign in with Google. Please try again.")
                }
            }
        }
    }

    /**
     * Tries the quiet "already authorized this app" flow first, and falls
     * back to the full account picker the first time a user signs in on
     * this device (Credential Manager throws [NoCredentialException] when
     * there's no prior authorization to filter to).
     */
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
    }

    fun signOut() {
        AuthRepository.signOut()
        SessionManager.signOut()
        WelcomeNotifier.reset()
    }

    private companion object {
        const val TAG = "AuthViewModel"
    }
}
