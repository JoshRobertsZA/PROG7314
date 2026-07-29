package com.example.prog7314.features.main.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.prog7314.core.common.AppButtonFilled
import com.example.prog7314.core.theme.WaypointCream

/**
 * Entry screen. In the XML source this was MainActivity's scratch nav hub
 * with buttons to jump straight to every screen for testing - explicitly
 * documented there as temporary scaffolding, not the real app flow. Only
 * its one real edge (Main -> Login) is carried into the Compose nav
 * graph; the other four "jump straight to X for testing" buttons are
 * dropped rather than ported, since they were never part of the app's
 * actual navigation and the graph this migration builds (see
 * core/navigation/Routes.kt) replaces the need for them.
 */
@Composable
fun MainScreen(
    onGoToLoginClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text("Hello World!")
        AppButtonFilled(
            text = "Go to Login",
            onClick = onGoToLoginClick,
            modifier = Modifier,
        )
    }
}
