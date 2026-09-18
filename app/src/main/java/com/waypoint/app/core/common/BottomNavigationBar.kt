package com.waypoint.app.core.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.WaypointBorderSoft
import com.waypoint.app.core.theme.WaypointNavActivePill
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted

/**
 * The four tabs of the app's shared bottom navigation (Figma node 47:60),
 * in display order.
 */
enum class NavTab(val route: String, val labelRes: Int, val iconRes: Int) {
    HOME("shell_home", R.string.home_nav_home, R.drawable.ic_nav_home),
    TRIPS("shell_trips", R.string.home_nav_trips, R.drawable.ic_nav_trips),
    EXPLORE("shell_explore", R.string.home_nav_explore, R.drawable.ic_nav_explore),
    PROFILE("shell_profile", R.string.home_nav_profile, R.drawable.ic_nav_profile),
}

/**
 * Shared bottom navigation bar. Lives in one place so it doesn't
 * disappear on navigation or get duplicated per screen the way the old
 * inline copy in HomeScreen did.
 */
@Composable
fun BottomNavigationBar(
    selectedTab: NavTab,
    onTabSelected: (NavTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(WaypointBorderSoft))
        Row(modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 28.dp)) {
            NavTab.entries.forEach { tab ->
                BottomNavItem(
                    tab = tab,
                    active = tab == selectedTab,
                    onClick = { onTabSelected(tab) },
                    modifier = Modifier.weight(1f),
                )
            }
        }
    }
}

@Composable
private fun BottomNavItem(tab: NavTab, active: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .background(
                color = if (active) WaypointNavActivePill else Color.Transparent,
                shape = RoundedCornerShape(RadiusButton),
            )
            .padding(vertical = 6.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Icon(
            painter = painterResource(tab.iconRes),
            contentDescription = null,
            tint = if (active) WaypointTerracotta else WaypointTextMuted,
            modifier = Modifier.size(25.dp),
        )
        Text(
            text = stringResource(tab.labelRes),
            color = if (active) WaypointTerracotta else WaypointTextMuted,
            fontSize = 13.sp,
            fontWeight = if (active) FontWeight.SemiBold else FontWeight.Normal,
            modifier = Modifier.padding(top = 6.dp),
        )
    }
}
