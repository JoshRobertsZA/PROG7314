// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager

// declares object `WelcomeNotifier` and opens its body
object WelcomeNotifier {

    // declares private mutable property `shownThisProcess`, initialised to false
    @Volatile private var shownThisProcess = false

    // declares function `notifyIfNeeded` taking 1 parameter (`context`) and opens its body
    fun notifyIfNeeded(context: Context) {
        // `if` statement: executes `return` when `shownThisProcess` is true
        if (shownThisProcess) return
        // `if` statement: executes `return` when `!SessionManager.isSignedIn` is true
        if (!SessionManager.isSignedIn) return
        // `if` statement: executes `return` when `!PushNotifier.hasPermission(context)` is true
        if (!PushNotifier.hasPermission(context)) return

        // declares read-only property `firstName`, initialised with the result of calling `SessionManager.displayName.substringBefore(…)`
        val firstName = SessionManager.displayName.substringBefore(' ').ifBlank { null }
        // declares read-only property `title`, initialised with the result of calling `if(…)` and opens a lambda / block
        val title = if (firstName != null) {
            // calls `getString` on `context` with arguments `(R.string.notification_welcome_title_named, f…)`
            context.getString(R.string.notification_welcome_title_named, firstName)
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // calls `getString` on `context` with arguments `(R.string.notification_welcome_title)`
            context.getString(R.string.notification_welcome_title)
        // closes the else branch
        }
        // calls `show` on `PushNotifier` with arguments `(context, title, context.getString(R.string.n…)`
        PushNotifier.show(context, title, context.getString(R.string.notification_welcome_body))
        // assigns `shownThisProcess` the value `true`
        shownThisProcess = true
    // closes the function `notifyIfNeeded`
    }

    // declares function `reset` taking no parameters and opens its body
    fun reset() { shownThisProcess = false }
// closes the object `WelcomeNotifier`
}
