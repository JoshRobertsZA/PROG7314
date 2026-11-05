package com.waypoint.app.core.notifications

import android.Manifest
import android.annotation.SuppressLint
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
 * Builds and shows every notification the app posts, on the channel for its type, and records it in
 * the history.
 */
object PushNotifier {

    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    /**
     * Shows a notification of [type] unless the user switched that type off in Settings.
     * A `tripId` in [extras] makes a tap open that trip (handled in MainActivity).
     */
    // hasPermission() is checked at the top of this function; lint can't follow that.
    @SuppressLint("MissingPermission")
    fun show(
        context: Context,
        type: NotificationType,
        title: String,
        body: String,
        extras: Map<String, String> = emptyMap(),
    ) {
        if (!NotificationPreferences.isEnabled(type)) return
        if (!hasPermission(context)) return

        val launch = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            extras.forEach { (k, v) -> putExtra(k, v) }
        }
        val pending = PendingIntent.getActivity(
            context,
             System.currentTimeMillis().toInt(),
            launch,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        )

        val notification = NotificationCompat.Builder(context, NotificationChannels.channelId(context, type))
            .setSmallIcon(R.drawable.ic_bell)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setAutoCancel(true)
            .setContentIntent(pending)
            .build()

        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)

        if (SessionManager.isSignedIn) {
            val accountId = SessionManager.accountId
            ioScope.launch { NotificationRepository(context).record(accountId, title, body) }
        }
    }

    fun hasPermission(context: Context): Boolean =
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            PackageManager.PERMISSION_GRANTED
}
