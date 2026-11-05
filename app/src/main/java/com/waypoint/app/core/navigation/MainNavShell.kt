package com.waypoint.app.core.navigation

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.waypoint.app.core.common.BottomNavigationBar
import com.waypoint.app.core.common.NavTab
import androidx.lifecycle.viewmodel.compose.viewModel
import com.waypoint.app.features.alltrips.ui.AllTripsScreen
import com.waypoint.app.features.home.ui.HomeViewModel
import com.waypoint.app.features.explore.ui.ExploreViewModel
import com.waypoint.app.features.explore.ui.ExploreScreen
import com.waypoint.app.features.home.ui.HomeScreen
import com.waypoint.app.features.settings.ui.SettingsScreen

/**
 * The signed-in part of the app: the Home, Trips, Explore and Profile tabs with the bottom bar. Tabs
 * keep their state when switching.
 */
@Composable
fun MainNavShell(
    onNewTripClick: () -> Unit,
    onTripClick: (tripId: String) -> Unit,
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    onEditProfileClick: () -> Unit = {},
    onRequestNotificationPermission: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    val tabNavController = rememberNavController()
    val homeViewModel: HomeViewModel = viewModel()
    val exploreViewModel: ExploreViewModel = viewModel()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedTab = NavTab.entries.firstOrNull { it.route == backStackEntry?.destination?.route } ?: NavTab.HOME
    val goHome = { tabNavController.navigate(NavTab.HOME.route) { launchSingleTop = true } }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = {
            BottomNavigationBar(
                selectedTab = selectedTab,
                onTabSelected = { tab ->
                    if (tab != selectedTab) {
                        tabNavController.navigate(tab.route) { launchSingleTop = true }
                    }
                },
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            )
        },
    ) { innerPadding ->
        NavHost(
            navController = tabNavController,
            startDestination = NavTab.HOME.route,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable(NavTab.HOME.route) {
                HomeScreen(
                    homeViewModel = homeViewModel,
                    onNewTripClick = onNewTripClick,
                    onViewAllTripsClick = {
                        tabNavController.navigate(NavTab.TRIPS.route) { launchSingleTop = true }
                    },
                    onSettingsClick = {
                        tabNavController.navigate(NavTab.PROFILE.route) { launchSingleTop = true }
                    },
                    onNotificationsClick = onNotificationsClick,
                    onTripClick = onTripClick,
                )
            }
            composable(NavTab.TRIPS.route) {
                AllTripsScreen(
                    onBackClick    = goHome,
                    onNewTripClick = onNewTripClick,
                    onTripClick    = onTripClick,
                )
            }
            composable(NavTab.EXPLORE.route) {
                ExploreScreen(
                    exploreViewModel = exploreViewModel,
                    onNotificationsClick = onNotificationsClick,
                )
            }
            composable(NavTab.PROFILE.route) {
                SettingsScreen(
                    onLogoutClick = onLogoutClick,
                    onNotificationsClick = onNotificationsClick,
                    onEditProfileClick = onEditProfileClick,
                    onRequestNotificationPermission = onRequestNotificationPermission,
                )
            }
        }
    }
}
