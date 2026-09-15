package com.example.prog7314.core.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prog7314.core.common.BottomNavigationBar
import com.example.prog7314.core.common.NavTab
import com.example.prog7314.features.alltrips.ui.AllTripsScreen
import com.example.prog7314.features.home.ui.HomeScreen
import com.example.prog7314.features.nearbyplaces.ui.NearbyPlacesScreen
import com.example.prog7314.features.settings.ui.SettingsScreen

/**
 * The authenticated shell: owns the shared BottomNavigationBar and a
 * nested NavHost for the four tabs (Figma section "Core Navigation" -
 * Home 47:30, Trips 60:2, Explore 62:2, Profile 281:20). Reached via the
 * top-level Routes.Home destination (see MainActivity.kt).
 *
 * TODO: Trips/Explore/Profile reuse AllTripsScreen/NearbyPlacesScreen/
 * SettingsScreen as-is; still need the tab-root header (brand + bell +
 * avatar, no back arrow) that Figma uses for every tab root - see
 * AllTripsScreen.kt (still back-button-styled) and SettingsScreen.kt
 * (missing the Profile tab's avatar/stats/currency/biometric content).
 */
@Composable
fun MainNavShell(
    onNewTripClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedTab = NavTab.entries.firstOrNull { it.route == backStackEntry?.destination?.route } ?: NavTab.HOME
    val goHome = { tabNavController.navigate(NavTab.HOME.route) { launchSingleTop = true } }

    Column(modifier = modifier.fillMaxSize()) {
        NavHost(
            navController = tabNavController,
            startDestination = NavTab.HOME.route,
            modifier = Modifier.weight(1f),
        ) {
            composable(NavTab.HOME.route) {
                HomeScreen(
                    onNewTripClick = onNewTripClick,
                    onViewAllTripsClick = { tabNavController.navigate(NavTab.TRIPS.route) { launchSingleTop = true } },
                    onSettingsClick = { tabNavController.navigate(NavTab.PROFILE.route) { launchSingleTop = true } },
                )
            }
            composable(NavTab.TRIPS.route) { AllTripsScreen(onBackClick = goHome) }
            composable(NavTab.EXPLORE.route) { NearbyPlacesScreen(onBackClick = goHome) }
            composable(NavTab.PROFILE.route) { SettingsScreen(onBackClick = goHome) }
        }
        BottomNavigationBar(
            selectedTab = selectedTab,
            onTabSelected = { tab ->
                if (tab != selectedTab) {
                    tabNavController.navigate(tab.route) { launchSingleTop = true }
                }
            },
        )
    }
}
