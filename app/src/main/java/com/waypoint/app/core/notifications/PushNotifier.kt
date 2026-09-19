// declares that this file belongs to the package `com.waypoint.app.core.notifications`
package com.waypoint.app.core.notifications

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.app.PendingIntent` for use in this file
import android.app.PendingIntent
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.content.Intent` for use in this file
import android.content.Intent
// imports `android.content.pm.PackageManager` for use in this file
import android.content.pm.PackageManager
// imports `android.os.Build` for use in this file
import android.os.Build
// imports `androidx.core.app.NotificationCompat` for use in this file
import androidx.core.app.NotificationCompat
// imports `androidx.core.app.NotificationManagerCompat` for use in this file
import androidx.core.app.NotificationManagerCompat
// imports `androidx.core.content.ContextCompat` for use in this file
import androidx.core.content.ContextCompat
// imports `com.waypoint.app.MainActivity` for use in this file
import com.waypoint.app.MainActivity
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.db.SessionManager` for use in this file
import com.waypoint.app.core.db.SessionManager
// imports `kotlinx.coroutines.CoroutineScope` for use in this file
import kotlinx.coroutines.CoroutineScope
// imports `kotlinx.coroutines.Dispatchers` for use in this file
import kotlinx.coroutines.Dispatchers
// imports `kotlinx.coroutines.SupervisorJob` for use in this file
import kotlinx.coroutines.SupervisorJob
// imports `kotlinx.coroutines.launch` for use in this file
import kotlinx.coroutines.launch

// declares object `PushNotifier` and opens its body
object PushNotifier {

    // declares private read-only property `ioScope`, initialised with the result of calling `CoroutineScope(…)`
    private val ioScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    // declares function `show` taking 4 parameters (`context`, `title`, `body`, `extras`) and opens its body
    fun show(context: Context, title: String, body: String, extras: Map<String, String> = emptyMap()) {
        // `if` statement: executes `return` when `!NotificationPreferences.isEnabled(context)` is true
        if (!NotificationPreferences.isEnabled(context)) return
        // `if` statement: executes `return` when `!hasPermission(context)` is true
        if (!hasPermission(context)) return

        // declares read-only property `launch`, initialised with the result of calling `Intent(…)` and opens a lambda / block
        val launch = Intent(context, MainActivity::class.java).apply {
            // assigns `flags` the value `Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTI…`
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
            // lambda `extras.forEach { (k, v) -> putExtra(k, v) }`
            extras.forEach { (k, v) -> putExtra(k, v) }
        // closes the lambda assigned to `launch`
        }
        // declares read-only property `pending`, initialised with the result of calling `PendingIntent.getActivity(…)`
        val pending = PendingIntent.getActivity(
            // continues the statement started above: `context,`
            context,
             // continues the statement started above: `System.currentTimeMillis().toInt(),`
             System.currentTimeMillis().toInt(),
            // continues the statement started above: `launch,`
            launch,
            // continues the statement started above: `PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMM…`
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
        // closes the multi-line argument list started above
        )

        // declares read-only property `notification`, initialised with the result of calling `NotificationCompat.Builder(…)`
        val notification = NotificationCompat.Builder(context, NotificationChannels.defaultChannelId(context))
            // chained call `.setSmallIcon` on the previous result with arguments `(R.drawable.ic_bell)`
            .setSmallIcon(R.drawable.ic_bell)
            // chained call `.setContentTitle` on the previous result with arguments `(title)`
            .setContentTitle(title)
            // chained call `.setContentText` on the previous result with arguments `(body)`
            .setContentText(body)
            // chained call `.setStyle` on the previous result with arguments `(NotificationCompat.BigTextStyle().bigTe…)`
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            // chained call `.setPriority` on the previous result with arguments `(NotificationCompat.PRIORITY_DEFAULT)`
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            // chained call `.setAutoCancel` on the previous result with arguments `(true)`
            .setAutoCancel(true)
            // chained call `.setContentIntent` on the previous result with arguments `(pending)`
            .setContentIntent(pending)
            // chained call `.build` on the previous result
            .build()

        // calls `from` on `NotificationManagerCompat` with arguments `(context)`, then chains `.notify(System.currentTimeMill…`
        NotificationManagerCompat.from(context).notify(System.currentTimeMillis().toInt(), notification)

        // `if` statement: the block below runs when `SessionManager.isSignedIn` is true
        if (SessionManager.isSignedIn) {
            // declares read-only property `accountId`, initialised to `SessionManager.accountId`
            val accountId = SessionManager.accountId
            // expression: `ioScope.launch { NotificationRepository(context).record(accountI…`
            ioScope.launch { NotificationRepository(context).record(accountId, title, body) }
        // closes the if block
        }
    // closes the function `show`
    }

    // declares function `hasPermission` taking 1 parameter (`context`), returning `Boolean`; its body is the expression ``
    fun hasPermission(context: Context): Boolean =
        // continues the statement started above: `Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||`
        Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
            // continues the statement started above: `ContextCompat.checkSelfPermission(context, Manifest.permiss…`
            ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) ==
            // continues the statement started above: `PackageManager.PERMISSION_GRANTED`
            PackageManager.PERMISSION_GRANTED
// closes the object `PushNotifier`
}
