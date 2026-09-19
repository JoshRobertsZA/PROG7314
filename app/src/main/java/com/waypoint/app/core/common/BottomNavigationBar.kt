// declares that this file belongs to the package `com.waypoint.app.core.common`
package com.waypoint.app.core.common

// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Icon` for use in this file
import androidx.compose.material3.Icon
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.theme.RadiusButton` for use in this file
import com.waypoint.app.core.theme.RadiusButton
// imports `com.waypoint.app.core.theme.WaypointBorderSoft` for use in this file
import com.waypoint.app.core.theme.WaypointBorderSoft
// imports `com.waypoint.app.core.theme.WaypointNavActivePill` for use in this file
import com.waypoint.app.core.theme.WaypointNavActivePill
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WaypointTextMuted` for use in this file
import com.waypoint.app.core.theme.WaypointTextMuted

// declares enum class `NavTab` with a primary constructor taking 3 parameters (`route`, `labelRes`, `iconRes`) and opens its body
enum class NavTab(val route: String, val labelRes: Int, val iconRes: Int) {
    // calls `HOME` with arguments `("shell_home", R.string.home_nav_home, R.draw…)`
    HOME("shell_home", R.string.home_nav_home, R.drawable.ic_nav_home),
    // continues the statement started above: `TRIPS("shell_trips", R.string.home_nav_trips, R.drawable.ic…`
    TRIPS("shell_trips", R.string.home_nav_trips, R.drawable.ic_nav_trips),
    // continues the statement started above: `EXPLORE("shell_explore", R.string.home_nav_explore, R.drawa…`
    EXPLORE("shell_explore", R.string.home_nav_explore, R.drawable.ic_nav_explore),
    // continues the statement started above: `PROFILE("shell_profile", R.string.home_nav_profile, R.drawa…`
    PROFILE("shell_profile", R.string.home_nav_profile, R.drawable.ic_nav_profile),
// closes the class `NavTab`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun BottomNavigationBar(`
fun BottomNavigationBar(
    // continues the statement started above: `selectedTab: NavTab,`
    selectedTab: NavTab,
    // continues the statement started above: `onTabSelected: (NavTab) -> Unit,`
    onTabSelected: (NavTab) -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Column` with arguments `(modifier = modifier.fillMaxWidth())` and opens a trailing lambda / block
    Column(modifier = modifier.fillMaxWidth()) {
        // calls `Box` with arguments `(modifier = Modifier.fillMaxWidth().height(1.…)`
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(WaypointBorderSoft))
        // calls `Row` with arguments `(modifier = Modifier.fillMaxWidth().padding(t…)` and opens a trailing lambda / block
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 28.dp)) {
            // expression: `NavTab.entries.forEach { tab ->`
            NavTab.entries.forEach { tab ->
                // continues the statement started above: `BottomNavItem(`
                BottomNavItem(
                    // continues the statement started above: `tab = tab,`
                    tab = tab,
                    // continues the statement started above: `active = tab == selectedTab,`
                    active = tab == selectedTab,
                    // continues the statement started above: `onClick = { onTabSelected(tab) },`
                    onClick = { onTabSelected(tab) },
                    // continues the statement started above: `modifier = Modifier.weight(1f),`
                    modifier = Modifier.weight(1f),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the lambda passed to `Row`
        }
    // closes the lambda passed to `Column`
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `BottomNavItem` taking 3 parameters (`tab`, `active`, `onClick`) and opens its body
private fun BottomNavItem(tab: NavTab, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    // calls `Column` with an argument list that continues on the following lines
    Column(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.clickable(onClick = onClick)`
            .clickable(onClick = onClick)
            // continues the statement started above: `.background(`
            .background(
                // continues the statement started above: `color = if (active) WaypointNavActivePill else Color.Transp…`
                color = if (active) WaypointNavActivePill else Color.Transparent,
                // continues the statement started above: `shape = RoundedCornerShape(RadiusButton),`
                shape = RoundedCornerShape(RadiusButton),
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.padding(vertical = 6.dp),`
            .padding(vertical = 6.dp),
        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
        horizontalAlignment = Alignment.CenterHorizontally,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Icon` with an argument list that continues on the following lines
        Icon(
            // continues the statement started above: `painter = painterResource(tab.iconRes),`
            painter = painterResource(tab.iconRes),
            // continues the statement started above: `contentDescription = null,`
            contentDescription = null,
            // continues the statement started above: `tint = if (active) WaypointTerracotta else WaypointTextMute…`
            tint = if (active) WaypointTerracotta else WaypointTextMuted,
            // continues the statement started above: `modifier = Modifier.size(25.dp),`
            modifier = Modifier.size(25.dp),
        // closes the multi-line argument list started above
        )
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = stringResource(tab.labelRes),`
            text = stringResource(tab.labelRes),
            // continues the statement started above: `color = if (active) WaypointTerracotta else WaypointTextMut…`
            color = if (active) WaypointTerracotta else WaypointTextMuted,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = if (active) FontWeight.SemiBold else FontWeigh…`
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            // continues the statement started above: `modifier = Modifier.padding(top = 6.dp),`
            modifier = Modifier.padding(top = 6.dp),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `BottomNavItem`
}
