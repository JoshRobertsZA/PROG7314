package com.waypoint.app.features.login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusDeco
import com.waypoint.app.core.theme.WaypointBorder
import com.waypoint.app.core.theme.WaypointCream
import com.waypoint.app.core.theme.WaypointDecoText
import com.waypoint.app.core.theme.WaypointGoogleBlue
import com.waypoint.app.core.theme.WaypointGoogleText
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WaypointTextMuted
import com.waypoint.app.core.theme.WaypointTextPrimary
import com.waypoint.app.core.theme.White

/**
 * Login screen. [onGoogleSignInClick] should kick off Firebase Google
 * Sign-In (see AuthViewModel) and only invoke its own success callback
 * once Firebase confirms the session - the caller owns navigation.
 */
@Composable
fun LoginScreen(
    onGoogleSignInClick: () -> Unit,
    onCreateAccountClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WaypointCream)
            .windowInsetsPadding(WindowInsets.systemBars)
            .padding(start = 28.dp, top = 64.dp, end = 28.dp, bottom = 36.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // LogoMark
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .background(WaypointTerracotta, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.login_logo_glyph),
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // BrandCol
            Text(
                text = stringResource(R.string.login_brand_name),
                color = WaypointTerracotta,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.login_tagline),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp),
            )

            // HeadingCol
            Text(
                text = stringResource(R.string.login_welcome_back),
                color = WaypointTextPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.login_subtitle),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(280.dp),
            )

            // GoogleSSOButton
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable(enabled = !isLoading, onClick = onGoogleSignInClick)
                    .background(White, RoundedCornerShape(RadiusButton))
                    .border(1.dp, WaypointBorder, RoundedCornerShape(RadiusButton))
                    .padding(vertical = 14.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier
                        .size(20.dp)
                        .background(White, CircleShape)
                        .border(1.dp, WaypointBorder, CircleShape),
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
                    text = if (isLoading) {
                        stringResource(R.string.auth_signing_in)
                    } else {
                        stringResource(R.string.login_google_cta)
                    },
                    color = WaypointGoogleText,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = WaypointTerracotta,
                    fontSize = 12.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp).width(280.dp),
                )
            }

            Text(
                text = stringResource(R.string.login_disclaimer),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(300.dp)
                    .alpha(0.85f),
            )

            // DecoBlock
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .height(72.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusDeco)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.login_deco_text),
                    color = WaypointDecoText,
                    fontSize = 13.sp,
                )
            }
        }

        // FooterRow, pinned to the bottom of the screen
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.login_footer_prompt),
                color = WaypointTextMuted,
                fontSize = 12.sp,
            )
            Text(
                text = stringResource(R.string.login_footer_cta),
                color = WaypointTerracotta,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(onClick = onCreateAccountClick),
            )
        }
    }
}
