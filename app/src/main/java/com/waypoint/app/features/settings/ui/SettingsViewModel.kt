// declares that this file belongs to the package `com.waypoint.app.features.settings.ui`
package com.waypoint.app.features.settings.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.auth.AuthRepository
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.network.WaypointApiRepository
// imports `com.waypoint.app.features.newtrip.data.TripRepository` for use in this file
import com.waypoint.app.features.newtrip.data.TripRepository
import com.waypoint.app.core.sync.OutboxRepository
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch
// imports `java.time.LocalDate` for use in this file
import java.time.LocalDate

// declares class `SettingsViewModel` with a primary constructor taking 1 parameter (`app`), inheriting from `AndroidViewModel(app)` and opens its body
class SettingsViewModel(app: Application) : AndroidViewModel(app) {

    // declares private read-only property `repo`, initialised with the result of calling `TripRepository(…)`
    private val repo = TripRepository(app)
    private val outbox = OutboxRepository(app)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(SettingsUiState())
    // declares read-only property `uiState` of type `StateFlow<SettingsUiState>`, initialised to `_uiState`
    val uiState: StateFlow<SettingsUiState> = _uiState

    // initialiser block: runs when an instance of the class is constructed
    init {
        // calls `loadTripCounts` with arguments `()`
        loadTripCounts()
    // closes the init block
    }

    // declares function `loadTripCounts` taking no parameters and opens its body
    fun loadTripCounts() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true) }`
            _uiState.update { it.copy(isLoading = true) }

            // declares read-only property `trips`, initialised with the result of calling `repo.getTripsForAccount(…)`
            val trips       = repo.getTripsForAccount(SessionManager.accountId)
            // declares read-only property `currentYear`, initialised with the result of calling `LocalDate.now(…)`
            val currentYear = LocalDate.now().year.toString()
            val pending     = outbox.count()

            // opens a block after `_uiState.update`
            _uiState.update {
                // calls `copy` on `it` with an argument list that continues on the following lines
                it.copy(
                    // continues the statement started above: `isLoading = false,`
                    isLoading       = false,
                    // continues the statement started above: `plannedThisYear = trips.count { trip -> trip.startDate.star…`
                    plannedThisYear = trips.count { trip -> trip.startDate.startsWith(currentYear) },
                    // continues the statement started above: `totalTrips = trips.size,`
                    totalTrips      = trips.size,
                    pendingChangeCount = pending,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the function `loadTripCounts`
    }

    // Lets the settings screen's "send test notification" button prove push
    // actually works end to end, rather than just trusting the local
    // toggle and permission state.
    fun sendTestNotification() {
        viewModelScope.launch {
            _uiState.update { it.copy(testNotificationState = TestNotificationState.SENDING) }
            val idToken = AuthRepository.getIdToken()
            val ok = idToken != null && WaypointApiRepository.sendTestNotification(idToken)
            _uiState.update {
                it.copy(testNotificationState = if (ok) TestNotificationState.SENT else TestNotificationState.FAILED)
            }
        }
    }
// closes the class `SettingsViewModel`
}
