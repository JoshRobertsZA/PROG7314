package com.waypoint.app.features.register.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.style.TextAlign
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
 * Register screen. Layout mirrors LoginScreen so the two stay visually and
 * behaviourally consistent. [onGoogleSignUpClick] drives the same Firebase
 * Google Sign-In flow as Login/Welcome - Firebase treats sign-up and
 * sign-in as the same call, it just creates the account the first time.
 * Source: Waypoint Figma node 64:23, "02 Waypoint — Register".
 */
@Composable
fun RegisterScreen(
    onGoogleSignUpClick: () -> Unit,
    onLogInClick: () -> Unit,
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
                    text = stringResource(R.string.register_logo_glyph),
                    color = White,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // BrandCol
            Text(
                text = stringResource(R.string.register_brand_name),
                color = WaypointTerracotta,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.register_tagline),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 4.dp),
            )

            // DecoBlock (wrap_content height here, unlike LoginScreen's
            // fixed 72dp - matches the source XML exactly)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .background(WaypointTerracotta, RoundedCornerShape(RadiusDeco))
                    .padding(vertical = 14.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(R.string.register_deco_text),
                    color = WaypointDecoText,
                    fontSize = 13.sp,
                )
            }

            // HeadingCol
            Text(
                text = stringResource(R.string.register_heading),
                color = WaypointTextPrimary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 24.dp),
            )
            Text(
                text = stringResource(R.string.register_subtitle),
                color = WaypointTextMuted,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 6.dp)
                    .width(280.dp),
            )

            // GoogleSSOButton
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
                    .clickable(enabled = !isLoading, onClick = onGoogleSignUpClick)
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
                        text = stringResource(R.string.register_google_glyph),
                        color = WaypointGoogleBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                    )
                }
                Text(
                    text = if (isLoading) {
                        stringResource(R.string.auth_signing_in)
                    } else {
                        stringResource(R.string.register_google_cta)
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
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp).width(280.dp),
                )
            }

            Text(
                text = stringResource(R.string.register_disclaimer),
                color = WaypointTextMuted,
                fontSize = 10.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .width(300.dp)
                    .alpha(0.85f),
            )
        }

        // FooterRow, pinned to the bottom of the screen
        Row(
            modifier = Modifier.align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = stringResource(R.string.register_footer_prompt),
                color = WaypointTextMuted,
                fontSize = 12.sp,
            )
            Text(
                text = stringResource(R.string.register_footer_cta),
                color = WaypointTerracotta,
                fontSize = 12.sp,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .clickable(onClick = onLogInClick),
            )
        }
    }
}
