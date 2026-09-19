// declares that this file belongs to the package `com.waypoint.app.features.notifications.ui`
package com.waypoint.app.features.notifications.ui

// imports `android.app.Application` for use in this file
import android.app.Application
// imports `android.text.format.DateUtils` for use in this file
import android.text.format.DateUtils
// imports `androidx.lifecycle.AndroidViewModel` for use in this file
import androidx.lifecycle.AndroidViewModel
// imports `androidx.lifecycle.viewModelScope` for use in this file
import androidx.lifecycle.viewModelScope
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `com.waypoint.app.core.notifications.NotificationRepository` for use in this file
import com.waypoint.app.core.notifications.NotificationRepository
// imports `kotlinx.coroutines.flow.MutableStateFlow` for use in this file
import kotlinx.coroutines.flow.MutableStateFlow
// imports `kotlinx.coroutines.flow.StateFlow` for use in this file
import kotlinx.coroutines.flow.StateFlow
// imports `kotlinx.coroutines.flow.update` for use in this file
import kotlinx.coroutines.flow.update
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// expression: `data class NotificationHistoryUiState(`
data class NotificationHistoryUiState(
    // continues the statement started above: `val isLoading: Boolean = true,`
    val isLoading: Boolean = true,
    // continues the statement started above: `val items: List<NotificationItem> = emptyList(),`
    val items: List<NotificationItem> = emptyList(),
// closes the multi-line argument list started above
) {
    // true when there are notifications the user has not yet cleared
    val hasUnread: Boolean get() = items.isNotEmpty()
}

// declares class `NotificationHistoryViewModel` with a primary constructor taking 1 parameter (`app`), inheriting from `AndroidViewModel(app)` and opens its body
class NotificationHistoryViewModel(app: Application) : AndroidViewModel(app) {

    // declares private read-only property `repo`, initialised with the result of calling `NotificationRepository(…)`
    private val repo = NotificationRepository(app)

    // declares private read-only property `_uiState`, initialised with the result of calling `MutableStateFlow(…)`
    private val _uiState = MutableStateFlow(NotificationHistoryUiState())
    // declares read-only property `uiState` of type `StateFlow<NotificationHistoryUiState>`, initialised to `_uiState`
    val uiState: StateFlow<NotificationHistoryUiState> = _uiState

    // declares function `load` taking no parameters and opens its body
    fun load() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // expression: `_uiState.update { it.copy(isLoading = true) }`
            _uiState.update { it.copy(isLoading = true) }
            // declares read-only property `now`, initialised with the result of calling `System.currentTimeMillis(…)`
            val now = System.currentTimeMillis()
            // declares read-only property `rows`, initialised with the result of calling `repo.getForAccount(…)`
            val rows = repo.getForAccount(SessionManager.accountId).map { e ->
                // continues the statement started above: `NotificationItem(`
                NotificationItem(
                    // continues the statement started above: `title = e.title,`
                    title = e.title,
                    // continues the statement started above: `body = e.body,`
                    body = e.body,
                    // continues the statement started above: `timestamp = DateUtils.getRelativeTimeSpanString(`
                    timestamp = DateUtils.getRelativeTimeSpanString(
                        // continues the statement started above: `e.createdAtMs, now, DateUtils.MINUTE_IN_MILLIS,`
                        e.createdAtMs, now, DateUtils.MINUTE_IN_MILLIS,
                    // continues the statement started above: `).toString(),`
                    ).toString(),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // expression: `_uiState.update { it.copy(isLoading = false, items = rows) }`
            _uiState.update { it.copy(isLoading = false, items = rows) }
        // closes the block
        }
    // closes the function `load`
    }

    // declares function `clearAll` taking no parameters and opens its body
    fun clearAll() {
        // opens a block after `viewModelScope.launch`
        viewModelScope.launch {
            // calls `clearForAccount` on `repo` with arguments `(SessionManager.accountId)`
            repo.clearForAccount(SessionManager.accountId)
            // expression: `_uiState.update { it.copy(items = emptyList()) }`
            _uiState.update { it.copy(items = emptyList()) }
        // closes the block
        }
    // closes the function `clearAll`
    }
// closes the class `NotificationHistoryViewModel`
}
