package com.example.prog7314.core.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.prog7314.core.cache.DeviceLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

/**
 * Wraps FusedLocationProviderClient as a cold Flow<DeviceLocation>.
 *
 * Behaviour:
 *   - Emits the last-known position immediately (if permission granted and
 *     a fix is cached on-device).
 *   - Continues emitting every UPDATE_INTERVAL_MS or MIN_DISTANCE_M,
 *     whichever comes first, until the collector cancels.
 *   - Closes silently if location permission has not been granted; the
 *     caller is responsible for requesting permission before collecting.
 */
object LocationProvider {

    private const val TAG = "LocationProvider"
    private const val UPDATE_INTERVAL_MS = 30_000L  // 30 seconds
    private const val MIN_DISTANCE_M = 50f           // metres between updates

    fun locationFlow(context: Context): Flow<DeviceLocation> = callbackFlow {
        if (!hasPermission(context)) {
            Log.d(TAG, "Location permission not granted — flow closed immediately")
            close()
            return@callbackFlow
        }

        val client: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        // Emit whatever the device already has cached — no network round-trip.
        try {
            client.lastLocation.addOnSuccessListener { loc ->
                if (loc != null) trySend(DeviceLocation(loc.latitude, loc.longitude))
            }
        } catch (e: SecurityException) {
            Log.w(TAG, "lastLocation SecurityException: ${e.message}")
        }

        val request = LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            UPDATE_INTERVAL_MS,
        )
            .setMinUpdateDistanceMeters(MIN_DISTANCE_M)
            .build()

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { loc ->
                    trySend(DeviceLocation(loc.latitude, loc.longitude))
                }
            }
        }

        try {
            client.requestLocationUpdates(request, callback, Looper.getMainLooper())
        } catch (e: SecurityException) {
            Log.w(TAG, "requestLocationUpdates SecurityException: ${e.message}")
            close()
            return@callbackFlow
        }

        awaitClose { client.removeLocationUpdates(callback) }
    }

    /** True if the app holds at least coarse location permission. */
    fun hasPermission(context: Context): Boolean =
        ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION,
        ) == PackageManager.PERMISSION_GRANTED
}
