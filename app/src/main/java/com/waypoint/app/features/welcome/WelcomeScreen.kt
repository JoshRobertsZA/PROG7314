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
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.ScrollState
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.waypoint.app.R
import com.waypoint.app.core.theme.RadiusButton
import com.waypoint.app.core.theme.RadiusCard
import com.waypoint.app.core.theme.WaypointGoogleBlue
import com.waypoint.app.core.theme.WaypointGoogleText
import com.waypoint.app.core.theme.WelcomeCardScrim
import com.waypoint.app.core.theme.WelcomeGradientBottom
import com.waypoint.app.core.theme.WelcomeGradientDayBottom
import com.waypoint.app.core.theme.WelcomeGradientDayLowerMid
import com.waypoint.app.core.theme.WelcomeGradientDayMid
import com.waypoint.app.core.theme.WelcomeGradientDayTop
import com.waypoint.app.core.theme.WelcomeGradientDayUpperMid
import com.waypoint.app.core.theme.WelcomeGradientDuskBottom
import com.waypoint.app.core.theme.WelcomeGradientDuskLowerMid
import com.waypoint.app.core.theme.WelcomeGradientDuskMid
import com.waypoint.app.core.theme.WelcomeGradientDuskTop
import com.waypoint.app.core.theme.WelcomeGradientDuskUpperMid
import com.waypoint.app.core.theme.WelcomeGradientMid
import com.waypoint.app.core.theme.WelcomeGradientLowerMid
import com.waypoint.app.core.theme.WelcomeGradientNightBottom
import com.waypoint.app.core.theme.WelcomeGradientNightLowerMid
import com.waypoint.app.core.theme.WelcomeGradientNightMid
import com.waypoint.app.core.theme.WelcomeGradientNightTop
import com.waypoint.app.core.theme.WelcomeGradientNightUpperMid
import com.waypoint.app.core.theme.WelcomeGradientTop
import com.waypoint.app.core.theme.WelcomeGradientUpperMid
import com.waypoint.app.core.theme.White
import com.waypoint.app.core.theme.WaypointTerracotta
import androidx.compose.ui.graphics.lerp

/**
 * Welcome / account-setup screen. [onGoogleContinueClick] drives the same
 * Firebase Google Sign-In flow as Login/Register (see AuthViewModel).
 *
 * Source: Waypoint Figma node 363:20, "Account Setup (No Biometric) —
 * Animation 1". Sits ahead of Login/Register in the nav graph; the single
 * "Continue with Google" CTA is the one entry point into the app from
 * here (see MainActivity's NavHost).
 *
 * No real destination photos exist in this codebase yet, so the carousel
 * cards use flat color placeholders instead of the photographic cards
 * from Figma — swap DestinationCard's background for an Image/painter
 * once real assets are added.
 */
/** Entrance stagger - each section starts this many ms after the previous one. */
private const val StaggerStepMs = 90
private const val EnterDurationMs = 450

/** One gradient stop set for a time-of-day phase ("Animation 1-4" in Figma). */
private data class SkyPhase(
    val top: Color,
    val upperMid: Color,
    val mid: Color,
    val lowerMid: Color,
    val bottom: Color,
)

private val skyPhases = listOf(
    SkyPhase(WelcomeGradientTop, WelcomeGradientUpperMid, WelcomeGradientMid, WelcomeGradientLowerMid, WelcomeGradientBottom), // morning
    SkyPhase(WelcomeGradientDayTop, WelcomeGradientDayUpperMid, WelcomeGradientDayMid, WelcomeGradientDayLowerMid, WelcomeGradientDayBottom), // day
    SkyPhase(WelcomeGradientDuskTop, WelcomeGradientDuskUpperMid, WelcomeGradientDuskMid, WelcomeGradientDuskLowerMid, WelcomeGradientDuskBottom), // dusk
    SkyPhase(WelcomeGradientNightTop, WelcomeGradientNightUpperMid, WelcomeGradientNightMid, WelcomeGradientNightLowerMid, WelcomeGradientNightBottom), // night
)

/** Total time to cycle through all four phases once, before looping. */
private const val SkyCycleDurationMs = 24_000

@Composable
private fun rememberAnimatedSkyGradient(): Brush {
    val infiniteTransition = rememberInfiniteTransition(label = "sky_cycle")
    val phaseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = skyPhases.size.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(SkyCycleDurationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart,
        ),
        label = "sky_phase_progress",
    )

    val phaseIndex = phaseProgress.toInt().coerceIn(0, skyPhases.size - 1)
    val nextIndex = (phaseIndex + 1) % skyPhases.size
    val fraction = phaseProgress - phaseIndex

    val from = skyPhases[phaseIndex]
    val to = skyPhases[nextIndex]

    return Brush.verticalGradient(
        colors = listOf(
            lerp(from.top, to.top, fraction),
            lerp(from.upperMid, to.upperMid, fraction),
            lerp(from.mid, to.mid, fraction),
            lerp(from.lowerMid, to.lowerMid, fraction),
            lerp(from.bottom, to.bottom, fraction),
        ),
    )
}

@Composable
fun WelcomeScreen(
    onGoogleContinueClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    errorMessage: String? = null,
) {
    // Drives the staggered entrance below - starts false so every section is
    // off-screen/invisible on first composition, then flips true one frame
    // later so AnimatedVisibility actually animates in rather than snapping.
    var contentVisible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) { contentVisible = true }

    val skyGradient = rememberAnimatedSkyGradient()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(skyGradient),
    ) {
        DotGridOverlay(modifier = Modifier.fillMaxSize())

        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .padding(start = 28.dp, top = 64.dp, end = 28.dp, bottom = 32.dp),
        ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            EnterSection(visible = contentVisible, delayMs = 0) {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    // Soft ambient glow behind the logo - purely decorative
                    // texture, sits underneath PulsingPinBadge's own halo.
                    // BlurredEdgeTreatment.Unbounded matters here: the
                    // default (Rectangle) clips the blur to this Box's own
                    // 100dp square, which shows up as a hard-edged square
                    // instead of a glow that actually fades to nothing.
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .blur(radius = 32.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                            .background(WaypointTerracotta.copy(alpha = 0.3f), CircleShape),
                    )
                    PulsingPinBadge()
                }
            }

            EnterSection(visible = contentVisible, delayMs = StaggerStepMs) {
                Column {
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
                }
            }

            EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 2) {
                Column {
                    Text(
                        text = stringResource(R.string.welcome_destinations_heading),
                        color = White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 32.dp),
                    )

                    val carouselScrollState = rememberScrollState()
                    val density = LocalDensity.current
                    // Width of exactly one lap of destinations, in px - the
                    // point AutoSlide instantly rewinds to 0 at. The second
                    // (looped) copy of the list below is what makes that
                    // rewind invisible: the pixels on screen right before
                    // and right after the jump are identical.
                    val singleLapWidthPx = remember(density) {
                        with(density) {
                            val cardWidthPx = DestinationCardWidth.toPx()
                            val spacingPx = DestinationCardSpacing.toPx()
                            (destinations.size * cardWidthPx + (destinations.size - 1) * spacingPx).toInt()
                        }
                    }
                    AutoSlide(carouselScrollState, singleLapWidthPx)

                    Row(
                        modifier = Modifier
                            .padding(top = 12.dp)
                            // enabled = false: this carousel is a purely
                            // ambient, always-moving visual, not something
                            // the user drives - a manual swipe would fight
                            // AutoSlide's own animateScrollTo calls.
                            .horizontalScroll(carouselScrollState, enabled = false),
                        horizontalArrangement = Arrangement.spacedBy(DestinationCardSpacing),
                    ) {
                        // Looped twice back-to-back so AutoSlide can wrap
                        // from the end of the first copy straight into the
                        // start of the second, seamlessly, instead of
                        // reversing direction at the true end of the list.
                        (destinations + destinations).forEach { destination ->
                            DestinationCard(destination)
                        }
                    }
                }
            }

            EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 3) {
                ValuePropRow(modifier = Modifier.padding(top = 40.dp))
            }
        }

        // Google CTA + terms, pinned to the bottom of the screen
        EnterSection(
            visible = contentVisible,
            delayMs = StaggerStepMs * 4,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth(),
        ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .clip(RoundedCornerShape(RadiusButton))
                    .background(White)
                    .clickable(enabled = !isLoading, onClick = onGoogleContinueClick),
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
                    text = if (isLoading) {
                        stringResource(R.string.auth_signing_in)
                    } else {
                        stringResource(R.string.welcome_google_cta)
                    },
                    color = WaypointGoogleText,
                    fontSize = 15.sp,
                    modifier = Modifier.padding(start = 10.dp),
                )
            }

            if (errorMessage != null) {
                Text(
                    text = errorMessage,
                    color = White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(top = 12.dp),
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
    }
}

/**
 * Wraps [content] in a fade + slide-up entrance, delayed by [delayMs] so
 * sections cascade in one after another instead of all popping in at once.
 */
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

/** Auto-slide speed for the destinations carousel, in dp per second. */
private const val CarouselPxPerSecond = 40f
private val DestinationCardWidth = 130.dp
private val DestinationCardSpacing = 12.dp

/**
 * Continuously scrolls [scrollState] forward in one direction only, and
 * once it passes [oneLapPx] (the width of exactly one copy of the
 * destinations list), instantly rewinds to 0 rather than reversing
 * direction. The Row this drives renders the destinations list twice
 * back-to-back, so the pixels right before and right after that rewind are
 * identical - the loop reads as infinite rather than "bouncing back".
 *
 * User scrolling is disabled on the Row (`horizontalScroll(..., enabled =
 * false)`), so there's no gesture to fight with here.
 */
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
            // Instant, not animated - imperceptible since the content at
            // this scroll position is identical to position 0 (start of
            // the second, looped copy of the list).
            scrollState.scrollTo(0)
        }
    }
}

/** Spacing between dots in [DotGridOverlay], and their radius. */
private val DotGridSpacing = 28.dp
private val DotGridRadius = 1.2.dp

/**
 * A faint, evenly-spaced dot grid drawn across the whole screen - cheap
 * texture that breaks up what would otherwise be a completely flat
 * gradient fill, without needing any image assets.
 */
@Composable
private fun DotGridOverlay(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier) {
        val spacingPx = DotGridSpacing.toPx()
        val radiusPx = DotGridRadius.toPx()
        var y = 0f
        while (y < size.height) {
            var x = 0f
            while (x < size.width) {
                drawCircle(
                    color = White.copy(alpha = 0.08f),
                    radius = radiusPx,
                    center = Offset(x, y),
                )
                x += spacingPx
            }
            y += spacingPx
        }
    }
}

private data class ValueProp(val emoji: String, val label: String)

private val valueProps = listOf(
    ValueProp("📶", "Offline sync"),
    ValueProp("🗓️", "Smart itineraries"),
    ValueProp("⛅", "Live weather"),
)

/**
 * Three lightweight feature callouts that fill the gap between the
 * destinations carousel and the CTA button - that space used to just be
 * empty, which read as unfinished rather than intentional.
 */
@Composable
private fun ValuePropRow(modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        valueProps.forEach { prop ->
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .background(White.copy(alpha = 0.14f), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(text = prop.emoji, fontSize = 18.sp)
                }
                Text(
                    text = prop.label,
                    color = White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .padding(top = 8.dp)
                        .width(76.dp),
                )
            }
        }
    }
}

/**
 * The real Waypoint logo (waypoint.svg - a pin with a pulsing halo behind
 * it) plus its animated halo, reimplemented natively here since Android's
 * vector drawables can't run the source file's CSS @keyframes. The static
 * pin/circle artwork lives in ic_waypoint_pin.xml; the halo's scale
 * (.92 -> 1.0) and opacity (.12 -> .25) below match that file's `pulse`
 * keyframes exactly - 2.4s ease-in-out, reversing rather than restarting,
 * so a full cycle is .92 -> 1.0 -> .92, same as 0%/50%/100% in CSS.
 */
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

    Box(modifier = modifier.size(64.dp), contentAlignment = Alignment.Center) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .scale(pulseScale)
                .alpha(pulseAlpha)
                .background(WaypointTerracotta, CircleShape),
        )
        Icon(
            painter = painterResource(R.drawable.ic_waypoint_pin),
            contentDescription = stringResource(R.string.welcome_pin_cd),
            tint = Color.Unspecified,
            modifier = Modifier.size(width = 46.dp, height = 58.dp),
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
            .height(150.dp)
            // Real elevation instead of a flat color block - a shadow is
            // what actually reads as "card" rather than "colored rectangle".
            .shadow(elevation = 10.dp, shape = RoundedCornerShape(RadiusCard))
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
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(12.dp),
        )
    }
}
