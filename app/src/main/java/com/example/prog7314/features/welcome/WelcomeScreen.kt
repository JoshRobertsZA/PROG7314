package com.example.prog7314.features.welcome

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.prog7314.R
import com.example.prog7314.core.theme.RadiusButton
import com.example.prog7314.core.theme.RadiusCard
import com.example.prog7314.core.theme.WaypointGoogleBlue
import com.example.prog7314.core.theme.WaypointGoogleText
import com.example.prog7314.core.theme.WelcomeCardAccent1
import com.example.prog7314.core.theme.WelcomeCardAccent2
import com.example.prog7314.core.theme.WelcomeCardAccent3
import com.example.prog7314.core.theme.WelcomeCardAccent4
import com.example.prog7314.core.theme.WelcomeCardScrim
import com.example.prog7314.core.theme.WelcomeGradientBottom
import com.example.prog7314.core.theme.WelcomeGradientMid
import com.example.prog7314.core.theme.WelcomeGradientLowerMid
import com.example.prog7314.core.theme.WelcomeGradientTop
import com.example.prog7314.core.theme.WelcomeGradientUpperMid
import com.example.prog7314.core.theme.WelcomePinCircleBg
import com.example.prog7314.core.theme.White

/**
 * Welcome / account-setup screen. Frontend skeleton only, no auth logic
 * wired up yet — mirrors the click-stub pattern used by LoginScreen and
 * RegisterScreen.
 *
 * Source: Waypoint Figma node 363:20, "Account Setup (No Biometric) —
 * Animation 1". Sits ahead of Login/Register in the nav graph; the single
 * "Continue with Google" CTA is the one entry point into the app from
 * here (see MainActivity's NavHost for how it currently routes to Home).
 *
 * No real destination photos exist in this codebase yet, so the carousel
 * cards use flat color placeholders instead of the photographic cards
 * from Figma — swap DestinationCard's background for an Image/painter
 * once real assets are added.
 */
@Composable
fun WelcomeScreen(
    onGoogleContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        WelcomeGradientTop,
                        WelcomeGradientUpperMid,
                        WelcomeGradientMid,
                        WelcomeGradientLowerMid,
                        WelcomeGradientBottom,
                    ),
                ),
            )
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 28.dp, top = 64.dp, end = 28.dp, bottom = 32.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Pin badge
            Box(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .size(64.dp)
                    .background(WelcomePinCircleBg, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_location_pin),
                    contentDescription = stringResource(R.string.welcome_pin_cd),
                    modifier = Modifier.size(24.dp),
                )
            }

            Text(
                text = stringResource(R.string.welcome_heading),
                color = White,
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.welcome_subtitle),
                color = White,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 8.dp),
            )

            Text(
                text = stringResource(R.string.welcome_destinations_heading),
                color = White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 32.dp),
            )

            Row(
                modifier = Modifier
                    .padding(top = 12.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                destinations.forEach { destination ->
                    DestinationCard(destination)
                }
            }
        }

        // Google CTA + terms, pinned to the bottom of the screen
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(RadiusButton))
                    .background(White)
                    .clickable(onClick = onGoogleContinueClick),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(White, CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = stringResource(R.string.login_google_glyph),
                        color = WaypointGoogleBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = stringResource(R.string.welcome_google_cta),
                    color = WaypointGoogleText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }

            Text(
                text = stringResource(R.string.welcome_disclaimer),
                color = White,
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp),
            )
        }
    }
}

private data class WelcomeDestination(
    val name: String,
    val accentStart: Color,
    val accentEnd: Color,
)

private val destinations = listOf(
    WelcomeDestination("Cape Town", WelcomeCardAccent1, WelcomeCardAccent2),
    WelcomeDestination("Bali", WelcomeCardAccent3, WelcomeCardAccent4),
    WelcomeDestination("Paris", WelcomeCardAccent1, WelcomeCardAccent2),
    WelcomeDestination("Tokyo", WelcomeCardAccent3, WelcomeCardAccent4),
    WelcomeDestination("Zanzibar", WelcomeCardAccent1, WelcomeCardAccent2),
)

@Composable
private fun DestinationCard(destination: WelcomeDestination, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(130.dp)
            .height(150.dp)
            .clip(RoundedCornerShape(RadiusCard))
            .background(
                Brush.verticalGradient(
                    colors = listOf(destination.accentEnd, destination.accentStart),
                ),
            ),
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, WelcomeCardScrim),
                    ),
                ),
        )
        Text(
            text = destination.name,
            color = White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
        )
    }
}
