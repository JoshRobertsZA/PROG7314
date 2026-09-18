package com.waypoint.app.core.auth

import android.app.Activity
import android.app.Application
import android.content.Context
import android.util.Log
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.NoCredentialException
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.secrets.RemoteSecrets
import com.waypoint.app.features.newtrip.data.TripRepository
import com.google.firebase.auth.FirebaseAuthWebException
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class AuthProvider {
    GOOGLE, GITHUB
}

data class AuthUiState(
    val isLoading: Boolean = false,
    val loadingProvider: AuthProvider? = null,
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
     * can skip straight past onboarding. Also recovers an in-flight GitHub
     * sign-in if the process died mid-redirect - see
     * AuthRepository.recoverPendingGitHubSignIn.
     *
     * Suspends until any recovery is resolved, so callers that check
     * AuthRepository.isSignedIn right after awaiting this see the correct
     * result rather than racing a fire-and-forget coroutine.
     */
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
    }

    private companion object {
        const val TAG = "AuthViewModel"
    }
}
