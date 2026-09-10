package com.example.prog7314.core.connectivity

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

/**
 * Observes real device connectivity (not just Wi-Fi/mobile radio state -
 * requires an actual internet-validated network), backing the offline
 * banner/dialog treatment (Figma "Offline Mode").
 */
@Composable
fun rememberIsOnline(): State<Boolean> {
    val context = LocalContext.current
    val connectivityManager = remember {
        context.applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }
    val isOnline = remember { mutableStateOf(connectivityManager.isCurrentlyOnline()) }

    DisposableEffect(connectivityManager) {
        val callback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                isOnline.value = connectivityManager.isCurrentlyOnline()
            }

            override fun onLost(network: Network) {
                isOnline.value = connectivityManager.isCurrentlyOnline()
            }

            override fun onCapabilitiesChanged(network: Network, capabilities: NetworkCapabilities) {
                isOnline.value = connectivityManager.isCurrentlyOnline()
            }
        }

        connectivityManager.registerDefaultNetworkCallback(callback)
        onDispose { connectivityManager.unregisterNetworkCallback(callback) }
    }

    return isOnline
}

private fun ConnectivityManager.isCurrentlyOnline(): Boolean {
    val network = activeNetwork ?: return false
    val capabilities = getNetworkCapabilities(network) ?: return false
    return capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
            capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
}