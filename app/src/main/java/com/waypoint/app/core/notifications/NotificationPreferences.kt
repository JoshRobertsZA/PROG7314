package com.waypoint.app.core.notifications

import com.waypoint.app.core.settings.AppSettings

/** The two kinds of notification the user can switch on and off separately in Settings. */
enum class NotificationType {
    /** Pushed by the API, e.g. a trip created on another device. Also used for the welcome message. */
    TRIP_UPDATES,

    /** Scheduled on the phone: countdowns, flight times, weather warnings, daily plans. */
    REMINDERS,
}

object NotificationPreferences {
    fun isEnabled(type: NotificationType): Boolean = when (type) {
        NotificationType.TRIP_UPDATES -> AppSettings.current.notifyTripUpdates
        NotificationType.REMINDERS -> AppSettings.current.notifyReminders
    }
}
