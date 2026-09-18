package com.waypoint.app.core.notifications

import android.content.Context
import com.waypoint.app.R
import com.waypoint.app.core.db.SessionManager

/**
 * Posts the "welcome back" notification once per app process, on whichever
 * happens first: a fresh Google sign-in, a restored Firebase session on
 * launch, or the user granting POST_NOTIFICATIONS after one of those.
 *
 * The once-per-process guard stops a restored session plus a late
 * permission grant from producing two notifications.
 */
object WelcomeNotifier {

    @Volatile private var shownThisProcess = false

    fun notifyIfNeeded(context: Context) {
        if (shownThisProcess) return
        if (!SessionManager.isSignedIn) return
        if (!PushNotifier.hasPermission(context)) return   // will retry once granted

        val firstName = SessionManager.displayName.substringBefore(' ').ifBlank { null }
        val title = if (firstName != null) {
            context.getString(R.string.notification_welcome_title_named, firstName)
        } else {
            context.getString(R.string.notification_welcome_title)
        }
        PushNotifier.show(context, title, context.getString(R.string.notification_welcome_body))
        shownThisProcess = true
    }

    /** Call on sign-out so the next sign-in in the same process greets again. */
    fun reset() { shownThisProcess = false }
}
