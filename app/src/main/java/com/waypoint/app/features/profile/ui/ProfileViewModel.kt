package com.waypoint.app.features.profile.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.R
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.db.AccountEntity
import com.waypoint.app.core.db.SessionManager
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

class ProfileViewModel(application: Application) : AndroidViewModel(application) {

    private val tripRepository = TripRepository(application)

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

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

    private companion object {
        const val TAG = "ProfileViewModel"
    }
}
