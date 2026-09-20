// declares that this file belongs to the package `com.waypoint.app.core.navigation`
package com.waypoint.app.core.navigation

// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.navigationBars` for use in this file
import androidx.compose.foundation.layout.navigationBars
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.material3.Scaffold` for use in this file
import androidx.compose.material3.Scaffold
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.navigation.compose.NavHost` for use in this file
import androidx.navigation.compose.NavHost
// imports `androidx.navigation.compose.composable` for use in this file
import androidx.navigation.compose.composable
// imports `androidx.navigation.compose.currentBackStackEntryAsState` for use in this file
import androidx.navigation.compose.currentBackStackEntryAsState
// imports `androidx.navigation.compose.rememberNavController` for use in this file
import androidx.navigation.compose.rememberNavController
// imports `com.waypoint.app.core.common.BottomNavigationBar` for use in this file
import com.waypoint.app.core.common.BottomNavigationBar
// imports `com.waypoint.app.core.common.NavTab` for use in this file
import com.waypoint.app.core.common.NavTab
// imports `androidx.lifecycle.viewmodel.compose.viewModel` for use in this file
import androidx.lifecycle.viewmodel.compose.viewModel
// imports `com.waypoint.app.features.alltrips.ui.AllTripsScreen` for use in this file
import com.waypoint.app.features.alltrips.ui.AllTripsScreen
// imports `com.waypoint.app.features.home.ui.HomeViewModel` for use in this file
import com.waypoint.app.features.home.ui.HomeViewModel
// imports `com.waypoint.app.features.explore.ui.ExploreViewModel` for use in this file
import com.waypoint.app.features.explore.ui.ExploreViewModel
// imports `com.waypoint.app.features.explore.ui.ExploreScreen` for use in this file
import com.waypoint.app.features.explore.ui.ExploreScreen
// imports `com.waypoint.app.features.home.ui.HomeScreen` for use in this file
import com.waypoint.app.features.home.ui.HomeScreen
// imports `com.waypoint.app.features.settings.ui.SettingsScreen` for use in this file
import com.waypoint.app.features.settings.ui.SettingsScreen

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun MainNavShell(`
fun MainNavShell(
    onNewTripClick: () -> Unit,
    onTripClick: (tripId: String) -> Unit,
    onLogoutClick: () -> Unit,
    onNotificationsClick: () -> Unit = {},
    modifier: Modifier = Modifier,
) {
    // declares read-only property `tabNavController`, initialised with the result of calling `rememberNavController(…)`
    val tabNavController = rememberNavController()
    // declares read-only property `homeViewModel` of type `HomeViewModel`, initialised with the result of calling `viewModel(…)`
    val homeViewModel: HomeViewModel = viewModel()
    // declares read-only property `exploreViewModel` of type `ExploreViewModel`, initialised with the result of calling `viewModel(…)`
    val exploreViewModel: ExploreViewModel = viewModel()
    // declares read-only property `backStackEntry`, delegated to `tabNavController.currentBackStackEntryA…`
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    // declares read-only property `selectedTab`, initialised to `NavTab.entries.firstOrNull { it.route == bac…`
    val selectedTab = NavTab.entries.firstOrNull { it.route == backStackEntry?.destination?.route } ?: NavTab.HOME
    // declares read-only property `goHome`, initialised to a lambda / arrow function
    val goHome = { tabNavController.navigate(NavTab.HOME.route) { launchSingleTop = true } }

    // calls `Scaffold` with an argument list that continues on the following lines
    Scaffold(
        // continues the statement started above: `modifier = modifier.fillMaxSize(),`
        modifier = modifier.fillMaxSize(),
        // continues the statement started above: `contentWindowInsets = WindowInsets(0, 0, 0, 0),`
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        // continues the statement started above: `bottomBar = {`
        bottomBar = {
            // calls `BottomNavigationBar` with an argument list that continues on the following lines
            BottomNavigationBar(
                // continues the statement started above: `selectedTab = selectedTab,`
                selectedTab = selectedTab,
                // continues the statement started above: `onTabSelected = { tab ->`
                onTabSelected = { tab ->
                    // continues the statement started above: `if (tab != selectedTab) {`
                    if (tab != selectedTab) {
                        // calls `navigate` on `tabNavController` with arguments `(tab.route)`
                        tabNavController.navigate(tab.route) { launchSingleTop = true }
                    // closes the block
                    }
                // closes the block
                },
                // continues the statement started above: `modifier = Modifier.windowInsetsPadding(WindowInsets.naviga…`
                modifier = Modifier.windowInsetsPadding(WindowInsets.navigationBars),
            // closes the multi-line argument list started above
            )
        // closes the block
        },
    // continues the statement started above: `) { innerPadding ->`
    ) { innerPadding ->
        // continues the statement started above: `NavHost(`
        NavHost(
            // continues the statement started above: `navController = tabNavController,`
            navController = tabNavController,
            // continues the statement started above: `startDestination = NavTab.HOME.route,`
            startDestination = NavTab.HOME.route,
            // continues the statement started above: `modifier = Modifier.padding(innerPadding),`
            modifier = Modifier.padding(innerPadding),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `composable` with arguments `(NavTab.HOME.route)` and opens a trailing lambda / block
            composable(NavTab.HOME.route) {
                // calls `HomeScreen` with an argument list that continues on the following lines
                HomeScreen(
                    // continues the statement started above: `homeViewModel = homeViewModel,`
                    homeViewModel = homeViewModel,
                    // continues the statement started above: `onNewTripClick = onNewTripClick,`
                    onNewTripClick = onNewTripClick,
                    // continues the statement started above: `onViewAllTripsClick = {`
                    onViewAllTripsClick = {
                        // calls `navigate` on `tabNavController` with arguments `(NavTab.TRIPS.route)`
                        tabNavController.navigate(NavTab.TRIPS.route) { launchSingleTop = true }
                    // closes the block
                    },
                    // continues the statement started above: `onSettingsClick = {`
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
                )
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}
