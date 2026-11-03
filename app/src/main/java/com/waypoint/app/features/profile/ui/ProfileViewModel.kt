package com.waypoint.app.features.profile.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.R
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.WaypointApiRepository
import com.waypoint.app.features.newtrip.data.TripRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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

    // password is used for an email/password account; googleIdToken for a
    // Google-linked one (Firebase requires a fresh credential to delete the
    // user, not just an existing session). A GitHub-only account has
    // neither available here, so it falls through to the reauth-required
    // error - re-authenticating a GitHub user needs its own Activity-based
    // flow, which is out of scope for this PR.
    //
    // Order matters for the partial-failure case: re-authenticate, delete
    // the cloud data, delete the Firebase user, then clear the local
    // session - in that order, so a failure at any step never leaves the
    // user signed out with their data still intact server-side, or signed
    // in with their data already gone.
    fun deleteAccount(password: String?, googleIdToken: String?, onSuccess: () -> Unit) {
        if (_deleteAccountUiState.value.isDeleting) return

        val providers = AuthRepository.currentSignInProviderIds
        val canReauth = (providers.contains("google.com") && googleIdToken != null) ||
            (providers.contains("password") && password != null)
        if (!canReauth) {
            _deleteAccountUiState.update {
                it.copy(errorMessage = getApplication<Application>().getString(R.string.profile_delete_reauth_required))
            }
            return
        }

        viewModelScope.launch {
            _deleteAccountUiState.update { it.copy(isDeleting = true, errorMessage = null) }
            try {
                if (providers.contains("google.com") && googleIdToken != null) {
                    AuthRepository.reauthenticateWithGoogleIdToken(googleIdToken)
                } else {
                    AuthRepository.reauthenticateWithEmail(password!!)
                }

                val idToken = AuthRepository.getIdToken(forceRefresh = true) ?: error("Could not get an auth token")
                if (!WaypointApiRepository.deleteAccount(idToken)) error("Could not delete cloud data")

                AuthRepository.deleteCurrentUser()
                SessionManager.signOut()
                _deleteAccountUiState.update { AccountDeletionUiState() }
                onSuccess()
            } catch (e: Exception) {
                Log.w(TAG, "Account deletion failed", e)
                _deleteAccountUiState.update { it.copy(isDeleting = false, errorMessage = getApplication<Application>().getString(R.string.profile_delete_error)) }
            }
        }
    }

    private companion object {
        const val TAG = "ProfileViewModel"
    }
}
