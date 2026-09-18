package com.waypoint.app.features.notifications.ui

import android.app.Application
import android.text.format.DateUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.waypoint.app.core.db.SessionManager
import com.waypoint.app.core.notifications.NotificationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotificationHistoryUiState(
    val isLoading: Boolean = true,
    val items: List<NotificationItem> = emptyList(),
)

/** Backs the history modal opened from the Profile bell. Account-scoped. */
class NotificationHistoryViewModel(app: Application) : AndroidViewModel(app) {

    private val repo = NotificationRepository(app)

    private val _uiState = MutableStateFlow(NotificationHistoryUiState())
    val uiState: StateFlow<NotificationHistoryUiState> = _uiState

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val now = System.currentTimeMillis()
            val rows = repo.getForAccount(SessionManager.accountId).map { e ->
                NotificationItem(
                    title = e.title,
                    body = e.body,
                    timestamp = DateUtils.getRelativeTimeSpanString(
                        e.createdAtMs, now, DateUtils.MINUTE_IN_MILLIS,
                    ).toString(),
                )
            }
            _uiState.update { it.copy(isLoading = false, items = rows) }
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repo.clearForAccount(SessionManager.accountId)
            _uiState.update { it.copy(items = emptyList()) }
        }
    }
}
