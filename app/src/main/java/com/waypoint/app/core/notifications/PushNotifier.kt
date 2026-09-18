package com.waypoint.app.core.notifications

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.waypoint.app.MainActivity
import com.waypoint.app.R
import com.waypoint.app.core.db.SessionManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Builds and posts a local notification. Used by WaypointMessagingService for
 * incoming FCM messages, and available to any future in-app trigger (trip
 * reminders etc.) so every notification looks the same.
 *
 * Tapping the notification opens MainActivity; any [extras] are forwarded on
 * the launch Intent so a later checkpoint can deep-link to a specific trip.
 */
object PushNotifier {

    // show() is called from non-suspend contexts (the FCM service), so the
    // history write gets its own process-lifetime IO scope.
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    fun show(context: Context, title: String, body: String, extras: Map<String, String> = emptyMap()) {
        if (!NotificationPreferences.isEnabled(context)) return   // user switched them off in Profile
        if (!hasPermission(context)) return

        val launch = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            extras.forEach { (k, v) -> putExtra(k, v) }
        }
        val pending = PendingIntent.getActivity(
            context,
            /* requestCode = */ System.currentTimeMillis().toInt(),
            launch,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, NotificationChannels.defaultChannelId(context))
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        // Unique id per notification so consecutive pushes stack instead of replacing.
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)

        // Only notifications that were actually posted end up in the history.
        if (SessionManager.isSignedIn) {
            val accountId = SessionManager.accountId
            ioScope.launch { NotificationRepository(context).record(accountId, title, body) }
        }
    }

    /** POST_NOTIFICATIONS only exists from API 33; earlier versions are implicitly granted. */
    fun hasPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
}
