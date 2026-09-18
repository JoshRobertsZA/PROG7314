package com.waypoint.app.core.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import com.waypoint.app.R

/**
 * Registers the single notification channel the app posts to. Safe to call
 * repeatedly - createNotificationChannel is a no-op once the id exists.
 *
 * The channel id lives in strings.xml (non-translatable) because the manifest
 * <meta-data> for FCM's default channel has to reference the same value.
 */
object NotificationChannels {

    fun defaultChannelId(context: Context): String =
        context.getString(R.string.notification_channel_id)

    fun ensureCreated(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            defaultChannelId(context),
            context.getString(R.string.notification_channel_name),
            NotificationManager.IMPORTANCE_DEFAULT,
        ).apply {
            description = context.getString(R.string.notification_channel_description)
        }

        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.createNotificationChannel(channel)
    }
}
