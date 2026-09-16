package com.example.prog7314.core.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.Alignment
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
 *
 * Uses Scaffold instead of a hand-rolled Column+weight(1f) split: a
 * manual Column let BottomNavigationBar clip the bottom of every tab's
 * scrollable content (e.g. Profile's Log out button was unreachable).
 * Scaffold measures the bottomBar first and hands NavHost the exact
 * remaining space via innerPadding, which is the well-tested way to
 * combine a bottom bar with per-tab scrollable content. contentWindowInsets
 * is zeroed out here since each tab screen already handles its own top
 * (status bar) inset, and BottomNavigationBar handles the bottom
 * (navigation bar) inset itself.
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
                    onNewTripClick = onNewTripClick,
                    onViewAllTripsClick = { tabNavController.navigate(NavTab.TRIPS.route) { launchSingleTop = true } },
                    onSettingsClick = { tabNavController.navigate(NavTab.PROFILE.route) { launchSingleTop = true } },
                )
            }
            composable(NavTab.TRIPS.route) { TabPlaceholder(NavTab.TRIPS) }
            composable(NavTab.EXPLORE.route) { ExploreScreen() }
            composable(NavTab.PROFILE.route) { SettingsScreen() }
        }
    }
}
   
@Composable
private fun TabPlaceholder(tab: NavTab, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "${tab.name} - coming soon",
            color = WaypointTextMuted,
        )
    }
}
