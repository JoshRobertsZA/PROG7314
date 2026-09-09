package com.example.prog7314.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.prog7314.core.common.BottomNavigationBar
import com.example.prog7314.core.common.NavTab
import com.example.prog7314.core.theme.WaypointTextMuted
import com.example.prog7314.features.explore.ui.ExploreScreen
import com.example.prog7314.features.home.ui.HomeScreen
import com.example.prog7314.features.settings.ui.SettingsScreen

/**
 * The authenticated shell: owns the shared BottomNavigationBar and a
 * nested NavHost for the four tabs (Figma section "Core Navigation" -
 * Home 47:30, Trips 60:2, Explore 62:2, Profile 281:20). Reached via the
 * top-level Routes.Home destination (see MainActivity.kt).
 *
 * TODO: Trips still renders placeholder content until AllTripsScreen.kt
 * gets its tab-root header fix (it still has the back-button-styled
 * header from before the shell existed). Home, Explore, and Profile are
 * now real Figma-accurate tab-root content.
 */
@Composable
fun MainNavShell(
    onNewTripClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tabNavController = rememberNavController()
    val backStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedTab = NavTab.entries.firstOrNull { it.route == backStackEntry?.destination?.route } ?: NavTab.HOME

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
            composable(NavTab.TRIPS.route) { TabPlaceholder(NavTab.TRIPS) }
            composable(NavTab.EXPLORE.route) { ExploreScreen() }
            composable(NavTab.PROFILE.route) { SettingsScreen() }
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

@Composable
private fun TabPlaceholder(tab: NavTab, modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "${stringResource(tab.labelRes)} - coming soon", color = WaypointTextMuted)
    }
}
