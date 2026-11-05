package com.waypoint.app.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.waypoint.app.R

/**
 * One Android notification channel per [NotificationType], so the phone's own
 * notification settings list "Trip updates" and "Trip reminders" separately.
 */
object NotificationChannels {

    fun channelId(context: Context, type: NotificationType): String = when (type) {
        NotificationType.TRIP_UPDATES -> context.getString(R.string.notification_channel_id)
        NotificationType.REMINDERS -> context.getString(R.string.notification_channel_reminders_id)
    }

    /** Created at app start; re-creating an existing channel only refreshes its translated name. */
    fun ensureCreated(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannels(
            listOf(
                NotificationChannel(
                    channelId(context, NotificationType.TRIP_UPDATES),
                    context.getString(R.string.notification_channel_name),
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply { description = context.getString(R.string.notification_channel_description) },
                NotificationChannel(
                    channelId(context, NotificationType.REMINDERS),
                    context.getString(R.string.notification_channel_reminders_name),
                    NotificationManager.IMPORTANCE_DEFAULT,
                ).apply { description = context.getString(R.string.notification_channel_reminders_description) },
            ),
        )
    }
}
