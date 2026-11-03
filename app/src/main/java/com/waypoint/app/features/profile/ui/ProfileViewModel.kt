package com.waypoint.app.features.profile.ui

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.R
import com.waypoint.app.core.notifications.WelcomeNotifier
import com.waypoint.app.core.db.WaypointDbHelper
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.auth.GoogleSignInHelper
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Pure, so PR 9 commit 7 can test every provider combination directly
// without mocking FirebaseAuth - same reasoning as the validators.
internal fun canReauthenticate(providerIds: List<String>): Boolean =
    providerIds.contains("password") || providerIds.contains("google.com")

data class ProfileUiState(
    val isSaving: Boolean = false,
    val errorMessage: String? = null,
)

data class AccountDeletionUiState(
    val isDeleting: Boolean = false,
    val errorMessage: String? = null,
)

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val tripRepository = TripRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    private val _deleteAccountUiState = MutableStateFlow(AccountDeletionUiState())
    val deleteAccountUiState: StateFlow<AccountDeletionUiState> = _deleteAccountUiState.asStateFlow()

    fun saveDisplayName(name: String, onSuccess: () -> Unit) {
        if (_uiState.value.isSaving) return
        val validationError = ProfileValidator.validateDisplayName(name)
        if (validationError != null) {
            _uiState.update { it.copy(errorMessage = getApplication<Application>().getString(validationError)) }
            return
        }
        val trimmedName = name.trim()
        viewModelScope.launch {
            _uiState.update { it.copy(isSaving = true, errorMessage = null) }
            try {
                AuthRepository.updateDisplayName(trimmedName)
                SessionManager.displayName = trimmedName
                tripRepository.upsertAccount(
                    AccountEntity(
                        id = SessionManager.accountId,
                        email = SessionManager.email,
                        displayName = trimmedName,
                        photoUrl = SessionManager.photoUrl,
                        lastLoginMs = System.currentTimeMillis(),
                    ),
                )
                _uiState.update { ProfileUiState() }
                onSuccess()
            } catch (e: Exception) {
                Log.w(TAG, "Saving display name failed", e)
                _uiState.update { it.copy(isSaving = false, errorMessage = getApplication<Application>().getString(R.string.profile_edit_save_error)) }
            }
        }
    }

    // A GitHub-only account has neither a password nor a Google credential
    // to reauthenticate with here, so the delete dialog uses this to decide
    // whether to show a working confirm action at all - proper GitHub
    // re-auth needs its own Activity-based flow, out of scope for this PR.
    fun canDeleteAccount(): Boolean = canReauthenticate(AuthRepository.currentSignInProviderIds)

    fun deleteAccountWithPassword(password: String, onSuccess: () -> Unit) {
        if (_deleteAccountUiState.value.isDeleting) return
        viewModelScope.launch {
            _deleteAccountUiState.update { it.copy(isDeleting = true, errorMessage = null) }
            try {
                AuthRepository.reauthenticateWithEmail(password)
                finishAccountDeletion(onSuccess)
            } catch (e: Exception) {
                onAccountDeletionFailed(e)
            }
        }
    }

    fun deleteAccountWithGoogle(context: Context, onSuccess: () -> Unit) {
        if (_deleteAccountUiState.value.isDeleting) return
        viewModelScope.launch {
            _deleteAccountUiState.update { it.copy(isDeleting = true, errorMessage = null) }
            try {
                val idToken = GoogleSignInHelper.requestIdToken(context, filterByAuthorizedAccounts = true)
                AuthRepository.reauthenticateWithGoogleIdToken(idToken)
                finishAccountDeletion(onSuccess)
            } catch (e: Exception) {
                onAccountDeletionFailed(e)
            }
        }
    }

    // Order matters for the partial-failure case: the caller has already
    // re-authenticated by the time this runs, then cloud data, then the
    // Firebase user, then the local session - in that order, so a failure
    // at any step never leaves the user signed out with their data still
    // intact server-side, or signed in with their data already gone.
    private suspend fun finishAccountDeletion(onSuccess: () -> Unit) {
        val idToken = AuthRepository.getIdToken(forceRefresh = true) ?: error("Could not get an auth token")
        if (!WaypointApiRepository.deleteAccount(idToken)) error("Could not delete cloud data")

        val deletedAccountId = SessionManager.accountId
        AuthRepository.deleteCurrentUser()
        // the privacy policy promises deletion, so the copy on this phone goes too
        withContext(Dispatchers.IO) {
            WaypointDbHelper.getInstance(getApplication<Application>()).deleteAccountData(deletedAccountId)
        }
        WelcomeNotifier.reset()
        SessionManager.signOut()
        _deleteAccountUiState.update { AccountDeletionUiState() }
        onSuccess()
    }

    private fun onAccountDeletionFailed(e: Exception) {
        Log.w(TAG, "Account deletion failed", e)
        _deleteAccountUiState.update {
            it.copy(isDeleting = false, errorMessage = getApplication<Application>().getString(R.string.profile_delete_error))
        }
    }

    private companion object {
        const val TAG = "ProfileViewModel"
    }
}
