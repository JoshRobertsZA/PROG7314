package com.waypoint.app.features.welcome

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.EaseOutCubic
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.auth.AuthProvider
import com.waypoint.app.core.theme.RadiusCard
import com.waypoint.app.core.theme.WaypointGoogleText
import com.waypoint.app.core.theme.WaypointTerracotta
import com.waypoint.app.core.theme.WelcomeCardScrim
import com.waypoint.app.core.theme.White

private const val StaggerStepMs = 90
private const val EnterDurationMs = 450

private const val CarouselPxPerSecond = 40f
private val DestinationCardWidth = 120.dp
private val DestinationCardSpacing = 10.dp

@Composable
private fun CinematicHeroBackground(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    val heroScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(12_000, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "hero_scale",
    )

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.welcome_bg),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .scale(heroScale),
        )

        // Vignette & dark bottom scrim overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xCC0F172A),
                            Color(0x66090D16),
                            Color(0xEB070A10),
                            Color(0xFA04060A),
                        ),
                    ),
                ),
        )
    }
}

@Composable
fun WelcomeScreen(
    onGoogleContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
    onGitHubContinueClick: (() -> Unit)? = null,
    isLoading: Boolean = false,
    loadingProvider: AuthProvider? = null,
    errorMessage: String? = null,
) {
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        CinematicHeroBackground()

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 20.dp, top = 36.dp, end = 20.dp, bottom = 20.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                // Top Logo & App Title
                EnterSection(visible = contentVisible, delayMs = 0) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Box(contentAlignment = Alignment.Center) {
                            Box(
                                modifier = Modifier
                                    .size(88.dp)
                                    .blur(radius = 32.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                                    .background(WaypointTerracotta.copy(alpha = 0.35f), CircleShape),
                            )
                            PulsingPinBadge()
                        }
                        Text(
                            text = "WAYPOINT",
                            color = White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 3.sp,
                            modifier = Modifier.padding(top = 8.dp),
                        )
                    }
                }

                // Main Heading & Subtitle
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = stringResource(R.string.welcome_heading),
                            color = White,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        Text(
                            text = stringResource(R.string.welcome_subtitle),
                            color = White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp),
                        )
                    }
                }

                // Image Slider Carousel (Placed ABOVE offline sync / feature highlights)
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 2) {
                    Column(
                        modifier = Modifier.padding(top = 36.dp),
                        horizontalAlignment = Alignment.Start,
                    ) {
                        Text(
                            text = stringResource(R.string.welcome_destinations_heading),
                            color = White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp),
                        )

                        val carouselScrollState = rememberScrollState()
                        val density = LocalDensity.current
                        val singleLapWidthPx = remember(density) {
                            with(density) {
                                val cardWidthPx = DestinationCardWidth.toPx()
                                val spacingPx = DestinationCardSpacing.toPx()
                                (destinations.size * cardWidthPx + (destinations.size - 1) * spacingPx).toInt()
                            }
                        }
                        AutoSlide(carouselScrollState, singleLapWidthPx)

                        Row(
                            modifier = Modifier.horizontalScroll(carouselScrollState, enabled = false),
                            horizontalArrangement = Arrangement.spacedBy(DestinationCardSpacing),
                        ) {
                            (destinations + destinations).forEach { destination ->
                                DestinationCard(destination)
                            }
                        }
                    }
                }

                // Feature Highlights Row (Placed directly BELOW the image slider)
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 3) {
                    FeatureHighlightsRow(modifier = Modifier.padding(top = 18.dp))
                }
            }

            // Bottom Frosted Glass Card (Google + GitHub SSO)
            EnterSection(
                visible = contentVisible,
                delayMs = StaggerStepMs * 4,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth(),
            ) {
                FrostedGlassContainerCard(
                    onGoogleContinueClick = onGoogleContinueClick,
                    onGitHubContinueClick = onGitHubContinueClick,
                    isLoading = isLoading,
                    loadingProvider = loadingProvider,
                    errorMessage = errorMessage,
                )
            }
        }
    }
}

@Composable
private fun EnterSection(
    visible: Boolean,
    delayMs: Int,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        modifier = modifier,
        enter = fadeIn(
            animationSpec = tween(EnterDurationMs, delayMillis = delayMs, easing = EaseOutCubic),
        ) + slideInVertically(
            animationSpec = tween(EnterDurationMs, delayMillis = delayMs, easing = EaseOutCubic),
            initialOffsetY = { it / 4 },
        ),
    ) {
        content()
    }
}

@Composable
private fun AutoSlide(scrollState: ScrollState, oneLapPx: Int) {
    LaunchedEffect(oneLapPx) {
        if (oneLapPx <= 0) return@LaunchedEffect
        val durationMs = (oneLapPx / CarouselPxPerSecond * 1000)
            .toInt()
            .coerceAtLeast(1)
        scrollState.scrollTo(0)
        while (true) {
            scrollState.animateScrollTo(oneLapPx, animationSpec = tween(durationMs, easing = LinearEasing))
            scrollState.scrollTo(0)
        }
    }
}

@Composable
private fun FeatureHighlightsRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(White.copy(alpha = 0.12f))
            .border(1.dp, White.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
            .padding(vertical = 10.dp, horizontal = 14.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        listOf(
            "Offline Sync",
            "Itineraries",
            "Weather",
        ).forEachIndexed { index, feature ->
            if (index > 0) {
                Box(
                    modifier = Modifier
                        .height(14.dp)
                        .width(1.dp)
                        .background(White.copy(alpha = 0.25f)),
                )
            }
            Text(
                text = feature,
                color = White,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

@Composable
private fun FrostedGlassContainerCard(
    onGoogleContinueClick: () -> Unit,
    onGitHubContinueClick: (() -> Unit)?,
    isLoading: Boolean,
    loadingProvider: AuthProvider?,
    errorMessage: String?,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp))
            .clip(RoundedCornerShape(28.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        White.copy(alpha = 0.22f),
                        White.copy(alpha = 0.12f),
                    ),
                ),
            )
            .border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(28.dp))
            .padding(18.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Google SSO Button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(White)
                    .clickable(enabled = !isLoading, onClick = onGoogleContinueClick),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_google),
                    contentDescription = null,
                    tint = Color.Unspecified,
                    modifier = Modifier.size(20.dp),
                )
                Text(
                    text = if (isLoading && loadingProvider == AuthProvider.GOOGLE) {
                        stringResource(R.string.auth_signing_in)
                    } else {
                        stringResource(R.string.welcome_google_cta)
                    },
                    color = WaypointGoogleText,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }

            if (onGitHubContinueClick != null) {
                Spacer(modifier = Modifier.height(10.dp))
                // GitHub SSO Button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF24292E))
                        .border(1.dp, White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        .clickable(enabled = !isLoading, onClick = onGitHubContinueClick),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_github),
                        contentDescription = null,
                        tint = White,
                        modifier = Modifier.size(20.dp),
                    )
                    Text(
                        text = if (isLoading && loadingProvider == AuthProvider.GITHUB) {
                            stringResource(R.string.auth_signing_in)
                        } else {
                            stringResource(R.string.welcome_github_cta)
                        },
                        color = White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 10.dp),
                    )
                }
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = Color(0xFFFF8A8A),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 10.dp),
                )
            }

            Text(
                text = stringResource(R.string.welcome_disclaimer),
                color = White.copy(alpha = 0.8f),
                fontSize = 11.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 10.dp),
            )
        }
    }
}

@Composable
private fun PulsingPinBadge(modifier: Modifier = Modifier) {
    val infiniteTransition = rememberInfiniteTransition(label = "pin_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pin_pulse_scale",
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.12f,
        targetValue = 0.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = EaseInOut),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "pin_pulse_alpha",
    )

    Box(modifier = modifier.size(56.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .scale(pulseScale)
                .alpha(pulseAlpha)
                .background(WaypointTerracotta, CircleShape),
        )
        Icon(
            painter = painterResource(R.drawable.ic_waypoint_pin),
            contentDescription = stringResource(R.string.welcome_pin_cd),
            tint = Color.Unspecified,
            modifier = Modifier.size(width = 40.dp, height = 50.dp),
        )
    }
}

private data class WelcomeDestination(
    val name: String,
    @param:DrawableRes val photoRes: Int,
)

private val destinations = listOf(
    WelcomeDestination("Cape Town", R.drawable.dest_capetown),
    WelcomeDestination("Bali", R.drawable.dest_bali),
    WelcomeDestination("Paris", R.drawable.dest_paris),
    WelcomeDestination("Tokyo", R.drawable.dest_tokyo),
    WelcomeDestination("Zanzibar", R.drawable.dest_zanzibar),
)

@Composable
private fun DestinationCard(destination: WelcomeDestination, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .width(DestinationCardWidth)
            .height(130.dp)
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusCard))
            .clip(RoundedCornerShape(RadiusCard)),
    ) {
        Image(
            painter = painterResource(destination.photoRes),
            contentDescription = destination.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize(),
        )
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
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(10.dp),
        )
    }
}
