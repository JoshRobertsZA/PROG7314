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
 * The authenticated shell: owns the shared BottomNavigationBar and a
 * nested NavHost for the four tabs (Figma section "Core Navigation" -
 * Home 47:30, Trips 60:2, Explore 62:2, Profile 281:20). Reached via the
 * top-level Routes.Home destination (see MainActivity.kt).
 *
 * Uses Scaffold instead of a hand-rolled Column+weight(1f) split: a
 * manual Column let BottomNavigationBar clip the bottom of every tab's
 * scrollable content. Scaffold measures the bottomBar first and hands
 * NavHost the exact remaining space via innerPadding.
 * contentWindowInsets is zeroed out here since each tab screen already
 * handles its own top (status bar) inset, and BottomNavigationBar handles
 * the bottom (navigation bar) inset itself.
 */
@Composable
fun MainNavShell(
    onNewTripClick: () -> Unit,
    onTripClick: (tripId: String) -> Unit,
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
                )
            }
            composable(NavTab.TRIPS.route) {
                AllTripsScreen(
                    onBackClick    = goHome,
                    onNewTripClick = onNewTripClick,
                    onTripClick    = onTripClick,
                )
            }
            composable(NavTab.EXPLORE.route) { ExploreScreen(exploreViewModel = exploreViewModel) }
            composable(NavTab.PROFILE.route) { SettingsScreen() }
        }
    }
}
