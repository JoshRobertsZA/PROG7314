package com.waypoint.app.core.notifications

import android.content.Context
import com.waypoint.app.R
import com.waypoint.app.core.db.SessionManager

/**
 * Posts a welcome notification once per app session after sign-in, which also confirms notifications
 * are working.
 */
object WelcomeNotifier {

    @Volatile private var shownThisProcess = false

    fun notifyIfNeeded(context: Context) {
        if (shownThisProcess) return
        if (!SessionManager.isSignedIn) return
        if (!PushNotifier.hasPermission(context)) return

        val firstName = SessionManager.displayName.substringBefore(' ').ifBlank { null }
        val title = if (firstName != null) {
            context.getString(R.string.notification_welcome_title_named, firstName)
        } else {
            context.getString(R.string.notification_welcome_title)
        }
        PushNotifier.show(context, NotificationType.TRIP_UPDATES, title, context.getString(R.string.notification_welcome_body))
        shownThisProcess = true
    }

    fun reset() { shownThisProcess = false }
}
