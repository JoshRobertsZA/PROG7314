// declares that this file belongs to the package `com.waypoint.app.core.connectivity`
package com.waypoint.app.core.connectivity

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `android.net.ConnectivityManager` for use in this file
import android.net.ConnectivityManager
// imports `android.net.Network` for use in this file
import android.net.Network
// imports `android.net.NetworkCapabilities` for use in this file
import android.net.NetworkCapabilities
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.DisposableEffect` for use in this file
import androidx.compose.runtime.DisposableEffect
// imports `androidx.compose.runtime.State` for use in this file
import androidx.compose.runtime.State
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.ui.platform.LocalContext` for use in this file
import androidx.compose.ui.platform.LocalContext

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `rememberIsOnline` taking no parameters, returning `State<Boolean>` and opens its body
fun rememberIsOnline(): State<Boolean> {
    // declares read-only property `context`, initialised to `LocalContext.current`
    val context = LocalContext.current
    // declares read-only property `connectivityManager`, initialised to `remember` and opens a lambda / block
    val connectivityManager = remember {
        // calls `getSystemService` on `context.applicationContext` with arguments `(Context.CONNECTIVITY_SERVICE)`
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    // closes the lambda assigned to `connectivityManager`
    }
    // declares read-only property `isOnline`, initialised to `remember { mutableStateOf(connectivityManage…`
    val isOnline = remember { mutableStateOf(connectivityManager.isCurrentlyOnline()) }

    // calls `DisposableEffect` with arguments `(connectivityManager)` and opens a trailing lambda / block
    DisposableEffect(connectivityManager) {
        // declares read-only property `callback`, initialised to `object : ConnectivityManager.NetworkCallback…` and opens a lambda / block
        val callback = object : ConnectivityManager.NetworkCallback() {
            // declares override function `onAvailable` taking 1 parameter (`network`) and opens its body
            override fun onAvailable(network: Network) {
                // assigns `isOnline.value` the value `connectivityManager.isCurrentlyOnline()`
                isOnline.value = connectivityManager.isCurrentlyOnline()
            // closes the function `onAvailable`
            }

            // declares override function `onLost` taking 1 parameter (`network`) and opens its body
            override fun onLost(network: Network) {
                // assigns `isOnline.value` the value `connectivityManager.isCurrentlyOnline()`
                isOnline.value = connectivityManager.isCurrentlyOnline()
            // closes the function `onLost`
            }

            // declares override function `onCapabilitiesChanged` taking 2 parameters (`network`, `capabilities`) and opens its body
            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                // assigns `isOnline.value` the value `connectivityManager.isCurrentlyOnline()`
                isOnline.value = connectivityManager.isCurrentlyOnline()
            // closes the function `onCapabilitiesChanged`
            }
        // closes the lambda assigned to `callback`
        }

        // calls `registerDefaultNetworkCallback` on `connectivityManager` with arguments `(callback)`
        connectivityManager.registerDefaultNetworkCallback(callback)
        // expression: `onDispose { connectivityManager.unregisterNetworkCallback(callba…`
        onDispose { connectivityManager.unregisterNetworkCallback(callback) }
    // closes the lambda passed to `DisposableEffect`
    }

    // returns `isOnline` from the current function
    return isOnline
// closes the function `rememberIsOnline`
}

// declares private function `isCurrentlyOnline` as an extension on `ConnectivityManager` taking no parameters, returning `Boolean` and opens its body
private fun ConnectivityManager.isCurrentlyOnline(): Boolean {
    // declares read-only property `network`, initialised to `activeNetwork ?: return false`
    val network = activeNetwork ?: return false
    // declares read-only property `capabilities`, initialised with the result of calling `getNetworkCapabilities(…)`
    val capabilities = getNetworkCapabilities(network) ?: return false
    // returns `capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNE…` from the current function
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
// closes the function `isCurrentlyOnline`
}