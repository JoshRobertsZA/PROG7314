// declares that this file belongs to the package `com.waypoint.app.core.location`
package com.waypoint.app.core.location

// imports `android.Manifest` for use in this file
import android.Manifest
// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.content.pm.PackageManager` for use in this file
import android.content.pm.PackageManager
// imports `android.os.Looper` for use in this file
import android.os.Looper
// imports `android.util.Log` for use in this file
import android.util.Log
// imports `androidx.core.content.ContextCompat` for use in this file
import androidx.core.content.ContextCompat
// imports `com.waypoint.app.core.cache.DeviceLocation` for use in this file
import com.waypoint.app.core.cache.DeviceLocation
// imports `com.google.android.gms.location.FusedLocationProviderClient` for use in this file
import com.google.android.gms.location.FusedLocationProviderClient
// imports `com.google.android.gms.location.LocationCallback` for use in this file
import com.google.android.gms.location.LocationCallback
// imports `com.google.android.gms.location.LocationRequest` for use in this file
import com.google.android.gms.location.LocationRequest
// imports `com.google.android.gms.location.LocationResult` for use in this file
import com.google.android.gms.location.LocationResult
// imports `com.google.android.gms.location.LocationServices` for use in this file
import com.google.android.gms.location.LocationServices
// imports `com.google.android.gms.location.Priority` for use in this file
import com.google.android.gms.location.Priority
// imports `kotlinx.coroutines.channels.awaitClose` for use in this file
import kotlinx.coroutines.channels.awaitClose
// imports `kotlinx.coroutines.flow.Flow` for use in this file
import kotlinx.coroutines.flow.Flow
// imports `kotlinx.coroutines.flow.callbackFlow` for use in this file
import kotlinx.coroutines.flow.callbackFlow

// declares object `LocationProvider` and opens its body
object LocationProvider {

    // declares private const read-only property `TAG`, initialised to the string literal "LocationProvider"
    private const val TAG = "LocationProvider"
    // declares private const read-only property `UPDATE_INTERVAL_MS`, initialised to the number 30_000L
    private const val UPDATE_INTERVAL_MS = 30_000L
    // declares private const read-only property `MIN_DISTANCE_M`, initialised to the number 50f
    private const val MIN_DISTANCE_M = 50f

    // declares function `locationFlow` taking 1 parameter (`context`), returning `Flow<DeviceLocation>`; its body is the expression `callbackFlow {`
    fun locationFlow(context: Context): Flow<DeviceLocation> = callbackFlow {
        // `if` statement: the block below runs when `!hasPermission(context)` is true
        if (!hasPermission(context)) {
            // calls `d` on `Log` with arguments `(TAG, "Location permission not granted — flow…)`
            Log.d(TAG, "Location permission not granted — flow closed immediately")
            // calls `close` with arguments `()`
            close()
            // expression: `return@callbackFlow`
            return@callbackFlow
        // closes the if block
        }

        // declares read-only property `client` of type `FusedLocationProviderClient`
        val client: FusedLocationProviderClient =
            // continues the statement started above: `LocationServices.getFusedLocationProviderClient(context)`
            LocationServices.getFusedLocationProviderClient(context)

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // expression: `client.lastLocation.addOnSuccessListener { loc ->`
            client.lastLocation.addOnSuccessListener { loc ->
                // continues the statement started above: `if (loc != null) trySend(DeviceLocation(loc.latitude, loc.l…`
                if (loc != null) trySend(DeviceLocation(loc.latitude, loc.longitude))
            // closes the block
            }
        // `catch` block: handles a thrown `SecurityException` bound to `e`
        } catch (e: SecurityException) {
            // calls `w` on `Log` with arguments `(TAG, "lastLocation SecurityException: ${e.me…)`
            Log.w(TAG, "lastLocation SecurityException: ${e.message}")
        // closes the catch block
        }

        // declares read-only property `request`, initialised with the result of calling `LocationRequest.Builder(…)`
        val request = LocationRequest.Builder(
            // continues the statement started above: `Priority.PRIORITY_BALANCED_POWER_ACCURACY,`
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            // continues the statement started above: `UPDATE_INTERVAL_MS,`
            UPDATE_INTERVAL_MS,
        // closes the multi-line argument list started above
        )
            // chained call `.setMinUpdateDistanceMeters` on the previous result with arguments `(MIN_DISTANCE_M)`
            .setMinUpdateDistanceMeters(MIN_DISTANCE_M)
            // chained call `.build` on the previous result
            .build()

        // declares read-only property `callback`, initialised to `object : LocationCallback()` and opens a lambda / block
        val callback = object : LocationCallback() {
            // declares override function `onLocationResult` taking 1 parameter (`result`) and opens its body
            override fun onLocationResult(result: LocationResult) {
                // expression: `result.lastLocation?.let { loc ->`
                result.lastLocation?.let { loc ->
                    // continues the statement started above: `trySend(DeviceLocation(loc.latitude, loc.longitude))`
                    trySend(DeviceLocation(loc.latitude, loc.longitude))
                // closes the block
                }
            // closes the function `onLocationResult`
            }
        // closes the lambda assigned to `callback`
        }

        // `try` block: exceptions thrown inside are handled by the `catch` below
        try {
            // calls `requestLocationUpdates` on `client` with arguments `(request, callback, Looper.getMainLooper())`
            client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        // `catch` block: handles a thrown `SecurityException` bound to `e`
        } catch (e: SecurityException) {
            // calls `w` on `Log` with arguments `(TAG, "requestLocationUpdates SecurityExcepti…)`
            Log.w(TAG, "requestLocationUpdates SecurityException: ${e.message}")
            // calls `close` with arguments `()`
            close()
            // expression: `return@callbackFlow`
            return@callbackFlow
        // closes the catch block
        }

        // expression: `awaitClose { client.removeLocationUpdates(callback) }`
        awaitClose { client.removeLocationUpdates(callback) }
    // closes the block
    }

    // declares function `hasPermission` taking 1 parameter (`context`), returning `Boolean`; its body is the expression ``
    fun hasPermission(context: Context): Boolean =
        // continues the statement started above: `ContextCompat.checkSelfPermission(`
        ContextCompat.checkSelfPermission(
            // continues the statement started above: `context,`
            context,
            // continues the statement started above: `Manifest.permission.ACCESS_COARSE_LOCATION,`
            Manifest.permission.ACCESS_COARSE_LOCATION,
        // continues the statement started above: `) == PackageManager.PERMISSION_GRANTED`
        ) == PackageManager.PERMISSION_GRANTED
// closes the object `LocationProvider`
}
