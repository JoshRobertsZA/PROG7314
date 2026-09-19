// declares that this file belongs to the package `com.waypoint.app.features.welcome`
package com.waypoint.app.features.welcome

// imports `androidx.annotation.DrawableRes` for use in this file
import androidx.annotation.DrawableRes
// imports `androidx.compose.animation.AnimatedVisibility` for use in this file
import androidx.compose.animation.AnimatedVisibility
// imports `androidx.compose.animation.core.EaseInOut` for use in this file
import androidx.compose.animation.core.EaseInOut
// imports `androidx.compose.animation.core.EaseOutCubic` for use in this file
import androidx.compose.animation.core.EaseOutCubic
// imports `androidx.compose.animation.core.LinearEasing` for use in this file
import androidx.compose.animation.core.LinearEasing
// imports `androidx.compose.animation.core.RepeatMode` for use in this file
import androidx.compose.animation.core.RepeatMode
// imports `androidx.compose.animation.core.animateFloat` for use in this file
import androidx.compose.animation.core.animateFloat
// imports `androidx.compose.animation.core.infiniteRepeatable` for use in this file
import androidx.compose.animation.core.infiniteRepeatable
// imports `androidx.compose.animation.core.rememberInfiniteTransition` for use in this file
import androidx.compose.animation.core.rememberInfiniteTransition
// imports `androidx.compose.animation.core.tween` for use in this file
import androidx.compose.animation.core.tween
// imports `androidx.compose.animation.fadeIn` for use in this file
import androidx.compose.animation.fadeIn
// imports `androidx.compose.animation.slideInVertically` for use in this file
import androidx.compose.animation.slideInVertically
// imports `androidx.compose.foundation.Image` for use in this file
import androidx.compose.foundation.Image
// imports `androidx.compose.foundation.ScrollState` for use in this file
import androidx.compose.foundation.ScrollState
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.border` for use in this file
import androidx.compose.foundation.border
// imports `androidx.compose.foundation.clickable` for use in this file
import androidx.compose.foundation.clickable
// imports `androidx.compose.foundation.horizontalScroll` for use in this file
import androidx.compose.foundation.horizontalScroll
// imports `androidx.compose.foundation.layout.Arrangement` for use in this file
import androidx.compose.foundation.layout.Arrangement
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.Column` for use in this file
import androidx.compose.foundation.layout.Column
// imports `androidx.compose.foundation.layout.Row` for use in this file
import androidx.compose.foundation.layout.Row
// imports `androidx.compose.foundation.layout.Spacer` for use in this file
import androidx.compose.foundation.layout.Spacer
// imports `androidx.compose.foundation.layout.WindowInsets` for use in this file
import androidx.compose.foundation.layout.WindowInsets
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.fillMaxWidth` for use in this file
import androidx.compose.foundation.layout.fillMaxWidth
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.padding` for use in this file
import androidx.compose.foundation.layout.padding
// imports `androidx.compose.foundation.layout.size` for use in this file
import androidx.compose.foundation.layout.size
// imports `androidx.compose.foundation.layout.systemBars` for use in this file
import androidx.compose.foundation.layout.systemBars
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.foundation.layout.windowInsetsPadding` for use in this file
import androidx.compose.foundation.layout.windowInsetsPadding
// imports `androidx.compose.foundation.rememberScrollState` for use in this file
import androidx.compose.foundation.rememberScrollState
// imports `androidx.compose.foundation.shape.CircleShape` for use in this file
import androidx.compose.foundation.shape.CircleShape
// imports `androidx.compose.foundation.shape.RoundedCornerShape` for use in this file
import androidx.compose.foundation.shape.RoundedCornerShape
// imports `androidx.compose.material3.Icon` for use in this file
import androidx.compose.material3.Icon
// imports `androidx.compose.material3.Text` for use in this file
import androidx.compose.material3.Text
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.BlurredEdgeTreatment` for use in this file
import androidx.compose.ui.draw.BlurredEdgeTreatment
// imports `androidx.compose.ui.draw.alpha` for use in this file
import androidx.compose.ui.draw.alpha
// imports `androidx.compose.ui.draw.blur` for use in this file
import androidx.compose.ui.draw.blur
// imports `androidx.compose.ui.draw.clip` for use in this file
import androidx.compose.ui.draw.clip
// imports `androidx.compose.ui.draw.scale` for use in this file
import androidx.compose.ui.draw.scale
// imports `androidx.compose.ui.draw.shadow` for use in this file
import androidx.compose.ui.draw.shadow
// imports `androidx.compose.ui.graphics.Brush` for use in this file
import androidx.compose.ui.graphics.Brush
// imports `androidx.compose.ui.graphics.Color` for use in this file
import androidx.compose.ui.graphics.Color
// imports `androidx.compose.ui.layout.ContentScale` for use in this file
import androidx.compose.ui.layout.ContentScale
// imports `androidx.compose.ui.platform.LocalDensity` for use in this file
import androidx.compose.ui.platform.LocalDensity
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.res.stringResource` for use in this file
import androidx.compose.ui.res.stringResource
// imports `androidx.compose.ui.text.font.FontWeight` for use in this file
import androidx.compose.ui.text.font.FontWeight
// imports `androidx.compose.ui.text.style.TextAlign` for use in this file
import androidx.compose.ui.text.style.TextAlign
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `androidx.compose.ui.unit.sp` for use in this file
import androidx.compose.ui.unit.sp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `com.waypoint.app.core.auth.AuthProvider` for use in this file
import com.waypoint.app.core.auth.AuthProvider
// imports `com.waypoint.app.core.theme.RadiusCard` for use in this file
import com.waypoint.app.core.theme.RadiusCard
// imports `com.waypoint.app.core.theme.WaypointGoogleText` for use in this file
import com.waypoint.app.core.theme.WaypointGoogleText
// imports `com.waypoint.app.core.theme.WaypointTerracotta` for use in this file
import com.waypoint.app.core.theme.WaypointTerracotta
// imports `com.waypoint.app.core.theme.WelcomeCardScrim` for use in this file
import com.waypoint.app.core.theme.WelcomeCardScrim
// imports `com.waypoint.app.core.theme.White` for use in this file
import com.waypoint.app.core.theme.White

// declares private const read-only property `StaggerStepMs`, initialised to the number 90
private const val StaggerStepMs = 90
// declares private const read-only property `EnterDurationMs`, initialised to the number 450
private const val EnterDurationMs = 450

// declares private const read-only property `CarouselPxPerSecond`, initialised to the number 40f
private const val CarouselPxPerSecond = 40f
// declares private read-only property `DestinationCardWidth`, initialised to the number 120.dp
private val DestinationCardWidth = 120.dp
// declares private read-only property `DestinationCardSpacing`, initialised to the number 10.dp
private val DestinationCardSpacing = 10.dp

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `CinematicHeroBackground` taking 1 parameter (`modifier`) and opens its body
private fun CinematicHeroBackground(modifier: Modifier = Modifier) {
    // declares read-only property `infiniteTransition`, initialised with the result of calling `rememberInfiniteTransition(…)`
    val infiniteTransition = rememberInfiniteTransition(label = "ken_burns")
    // declares read-only property `heroScale`, delegated to `infiniteTransition.animateFloat(`
    val heroScale by infiniteTransition.animateFloat(
        // continues the statement started above: `initialValue = 1.0f,`
        initialValue = 1.0f,
        // continues the statement started above: `targetValue = 1.08f,`
        targetValue = 1.08f,
        // continues the statement started above: `animationSpec = infiniteRepeatable(`
        animationSpec = infiniteRepeatable(
            // continues the statement started above: `animation = tween(12_000, easing = EaseInOut),`
            animation = tween(12_000, easing = EaseInOut),
            // continues the statement started above: `repeatMode = RepeatMode.Reverse,`
            repeatMode = RepeatMode.Reverse,
        // closes the multi-line argument list started above
        ),
        // continues the statement started above: `label = "hero_scale",`
        label = "hero_scale",
    // closes the multi-line argument list started above
    )

    // calls `Box` with arguments `(modifier = modifier.fillMaxSize())` and opens a trailing lambda / block
    Box(modifier = modifier.fillMaxSize()) {
        // calls `Image` with an argument list that continues on the following lines
        Image(
            // continues the statement started above: `painter = painterResource(R.drawable.welcome_bg),`
            painter = painterResource(R.drawable.welcome_bg),
            // continues the statement started above: `contentDescription = null,`
            contentDescription = null,
            // continues the statement started above: `contentScale = ContentScale.Crop,`
            contentScale = ContentScale.Crop,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.scale(heroScale),`
                .scale(heroScale),
        // closes the multi-line argument list started above
        )

        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.background(`
                .background(
                    // continues the statement started above: `Brush.verticalGradient(`
                    Brush.verticalGradient(
                        // continues the statement started above: `colors = listOf(`
                        colors = listOf(
                            // continues the statement started above: `Color(0xCC0F172A),`
                            Color(0xCC0F172A),
                            // continues the statement started above: `Color(0x66090D16),`
                            Color(0x66090D16),
                            // continues the statement started above: `Color(0xEB070A10),`
                            Color(0xEB070A10),
                            // continues the statement started above: `Color(0xFA04060A),`
                            Color(0xFA04060A),
                        // closes the multi-line argument list started above
                        ),
                    // closes the multi-line argument list started above
                    ),
                // closes the multi-line argument list started above
                ),
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the function `CinematicHeroBackground`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `fun WelcomeScreen(`
fun WelcomeScreen(
    // continues the statement started above: `onGoogleContinueClick: () -> Unit,`
    onGoogleContinueClick: () -> Unit,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `onGitHubContinueClick: (() -> Unit)? = null,`
    onGitHubContinueClick: (() -> Unit)? = null,
    // continues the statement started above: `isLoading: Boolean = false,`
    isLoading: Boolean = false,
    // continues the statement started above: `loadingProvider: AuthProvider? = null,`
    loadingProvider: AuthProvider? = null,
    // continues the statement started above: `errorMessage: String? = null,`
    errorMessage: String? = null,
// ends the argument list started above and opens the block that follows
) {
    // declares mutable property `contentVisible`, delegated to `remember { mutableStateOf(false) }`
    var contentVisible by remember { mutableStateOf(false) }
    // calls `LaunchedEffect` with arguments `(Unit)`
    LaunchedEffect(Unit) { contentVisible = true }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier.fillMaxSize(),`
        modifier = modifier.fillMaxSize(),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `CinematicHeroBackground` with arguments `()`
        CinematicHeroBackground()

        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.windowInsetsPadding(WindowInsets.systemBars)`
                .windowInsetsPadding(WindowInsets.systemBars)
                // continues the statement started above: `.padding(start = 20.dp, top = 36.dp, end = 20.dp, bottom = …`
                .padding(start = 20.dp, top = 36.dp, end = 20.dp, bottom = 20.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Column` with an argument list that continues on the following lines
            Column(
                // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                modifier = Modifier.fillMaxWidth(),
                // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                horizontalAlignment = Alignment.CenterHorizontally,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `EnterSection` with arguments `(visible = contentVisible, delayMs = 0)` and opens a trailing lambda / block
                EnterSection(visible = contentVisible, delayMs = 0) {
                    // calls `Column` with arguments `(horizontalAlignment = Alignment.CenterHorizo…)` and opens a trailing lambda / block
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        // calls `Box` with arguments `(contentAlignment = Alignment.Center)` and opens a trailing lambda / block
                        Box(contentAlignment = Alignment.Center) {
                            // calls `Box` with an argument list that continues on the following lines
                            Box(
                                // continues the statement started above: `modifier = Modifier`
                                modifier = Modifier
                                    // continues the statement started above: `.size(88.dp)`
                                    .size(88.dp)
                                    // continues the statement started above: `.blur(radius = 32.dp, edgeTreatment = BlurredEdgeTreatment.…`
                                    .blur(radius = 32.dp, edgeTreatment = BlurredEdgeTreatment.Unbounded)
                                    // continues the statement started above: `.background(WaypointTerracotta.copy(alpha = 0.35f), CircleS…`
                                    .background(WaypointTerracotta.copy(alpha = 0.35f), CircleShape),
                            // closes the multi-line argument list started above
                            )
                            // calls `PulsingPinBadge` with arguments `()`
                            PulsingPinBadge()
                        // closes the lambda passed to `Box`
                        }
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = "WAYPOINT",`
                            text = "WAYPOINT",
                            // continues the statement started above: `color = White.copy(alpha = 0.9f),`
                            color = White.copy(alpha = 0.9f),
                            // continues the statement started above: `fontSize = 12.sp,`
                            fontSize = 12.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `letterSpacing = 3.sp,`
                            letterSpacing = 3.sp,
                            // continues the statement started above: `modifier = Modifier.padding(top = 8.dp),`
                            modifier = Modifier.padding(top = 8.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the lambda passed to `Column`
                    }
                // closes the lambda passed to `EnterSection`
                }

                // calls `EnterSection` with arguments `(visible = contentVisible, delayMs = StaggerS…)` and opens a trailing lambda / block
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs) {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
                        modifier = Modifier.fillMaxWidth(),
                        // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
                        horizontalAlignment = Alignment.CenterHorizontally,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.welcome_heading),`
                            text = stringResource(R.string.welcome_heading),
                            // continues the statement started above: `color = White,`
                            color = White,
                            // continues the statement started above: `fontSize = 26.sp,`
                            fontSize = 26.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `textAlign = TextAlign.Center,`
                            textAlign = TextAlign.Center,
                            // continues the statement started above: `modifier = Modifier.padding(top = 16.dp),`
                            modifier = Modifier.padding(top = 16.dp),
                        // closes the multi-line argument list started above
                        )
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.welcome_subtitle),`
                            text = stringResource(R.string.welcome_subtitle),
                            // continues the statement started above: `color = White.copy(alpha = 0.85f),`
                            color = White.copy(alpha = 0.85f),
                            // continues the statement started above: `fontSize = 13.sp,`
                            fontSize = 13.sp,
                            // continues the statement started above: `textAlign = TextAlign.Center,`
                            textAlign = TextAlign.Center,
                            // continues the statement started above: `modifier = Modifier.padding(top = 6.dp, start = 8.dp, end =…`
                            modifier = Modifier.padding(top = 6.dp, start = 8.dp, end = 8.dp),
                        // closes the multi-line argument list started above
                        )
                    // closes the block
                    }
                // closes the lambda passed to `EnterSection`
                }

                // calls `EnterSection` with arguments `(visible = contentVisible, delayMs = StaggerS…)` and opens a trailing lambda / block
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 2) {
                    // calls `Column` with an argument list that continues on the following lines
                    Column(
                        // continues the statement started above: `modifier = Modifier.padding(top = 36.dp),`
                        modifier = Modifier.padding(top = 36.dp),
                        // continues the statement started above: `horizontalAlignment = Alignment.Start,`
                        horizontalAlignment = Alignment.Start,
                    // ends the argument list started above and opens the block that follows
                    ) {
                        // calls `Text` with an argument list that continues on the following lines
                        Text(
                            // continues the statement started above: `text = stringResource(R.string.welcome_destinations_heading…`
                            text = stringResource(R.string.welcome_destinations_heading),
                            // continues the statement started above: `color = White,`
                            color = White,
                            // continues the statement started above: `fontSize = 14.sp,`
                            fontSize = 14.sp,
                            // continues the statement started above: `fontWeight = FontWeight.Bold,`
                            fontWeight = FontWeight.Bold,
                            // continues the statement started above: `modifier = Modifier.padding(bottom = 8.dp),`
                            modifier = Modifier.padding(bottom = 8.dp),
                        // closes the multi-line argument list started above
                        )

                        // declares read-only property `carouselScrollState`, initialised with the result of calling `rememberScrollState(…)`
                        val carouselScrollState = rememberScrollState()
                        // declares read-only property `density`, initialised to `LocalDensity.current`
                        val density = LocalDensity.current
                        // declares read-only property `singleLapWidthPx`, initialised with the result of calling `remember(…)` and opens a lambda / block
                        val singleLapWidthPx = remember(density) {
                            // calls `with` with arguments `(density)` and opens a trailing lambda / block
                            with(density) {
                                // declares read-only property `cardWidthPx`, initialised with the result of calling `DestinationCardWidth.toPx(…)`
                                val cardWidthPx = DestinationCardWidth.toPx()
                                // declares read-only property `spacingPx`, initialised with the result of calling `DestinationCardSpacing.toPx(…)`
                                val spacingPx = DestinationCardSpacing.toPx()
                                // statement: `(destinations.size * cardWidthPx + (destinations.size - 1) * spa…`
                                (destinations.size * cardWidthPx + (destinations.size - 1) * spacingPx).toInt()
                            // closes the lambda passed to `with`
                            }
                        // closes the lambda assigned to `singleLapWidthPx`
                        }
                        // calls `AutoSlide` with arguments `(carouselScrollState, singleLapWidthPx)`
                        AutoSlide(carouselScrollState, singleLapWidthPx)

                        // calls `Row` with an argument list that continues on the following lines
                        Row(
                            // continues the statement started above: `modifier = Modifier.horizontalScroll(carouselScrollState, e…`
                            modifier = Modifier.horizontalScroll(carouselScrollState, enabled = false),
                            // continues the statement started above: `horizontalArrangement = Arrangement.spacedBy(DestinationCar…`
                            horizontalArrangement = Arrangement.spacedBy(DestinationCardSpacing),
                        // ends the argument list started above and opens the block that follows
                        ) {
                            // expression: `(destinations + destinations).forEach { destination ->`
                            (destinations + destinations).forEach { destination ->
                                // continues the statement started above: `DestinationCard(destination)`
                                DestinationCard(destination)
                            // closes the block
                            }
                        // closes the block
                        }
                    // closes the block
                    }
                // closes the lambda passed to `EnterSection`
                }

                // calls `EnterSection` with arguments `(visible = contentVisible, delayMs = StaggerS…)` and opens a trailing lambda / block
                EnterSection(visible = contentVisible, delayMs = StaggerStepMs * 3) {
                    // calls `FeatureHighlightsRow` with arguments `(modifier = Modifier.padding(top = 18.dp))`
                    FeatureHighlightsRow(modifier = Modifier.padding(top = 18.dp))
                // closes the lambda passed to `EnterSection`
                }
            // closes the block
            }

            // calls `EnterSection` with an argument list that continues on the following lines
            EnterSection(
                // continues the statement started above: `visible = contentVisible,`
                visible = contentVisible,
                // continues the statement started above: `delayMs = StaggerStepMs * 4,`
                delayMs = StaggerStepMs * 4,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.align(Alignment.BottomCenter)`
                    .align(Alignment.BottomCenter)
                    // continues the statement started above: `.fillMaxWidth(),`
                    .fillMaxWidth(),
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `FrostedGlassContainerCard` with an argument list that continues on the following lines
                FrostedGlassContainerCard(
                    // continues the statement started above: `onGoogleContinueClick = onGoogleContinueClick,`
                    onGoogleContinueClick = onGoogleContinueClick,
                    // continues the statement started above: `onGitHubContinueClick = onGitHubContinueClick,`
                    onGitHubContinueClick = onGitHubContinueClick,
                    // continues the statement started above: `isLoading = isLoading,`
                    isLoading = isLoading,
                    // continues the statement started above: `loadingProvider = loadingProvider,`
                    loadingProvider = loadingProvider,
                    // continues the statement started above: `errorMessage = errorMessage,`
                    errorMessage = errorMessage,
                // closes the multi-line argument list started above
                )
            // closes the block
            }
        // closes the block
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun EnterSection(`
private fun EnterSection(
    // continues the statement started above: `visible: Boolean,`
    visible: Boolean,
    // continues the statement started above: `delayMs: Int,`
    delayMs: Int,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
    // continues the statement started above: `content: @Composable () -> Unit,`
    content: @Composable () -> Unit,
// ends the argument list started above and opens the block that follows
) {
    // calls `AnimatedVisibility` with an argument list that continues on the following lines
    AnimatedVisibility(
        // continues the statement started above: `visible = visible,`
        visible = visible,
        // continues the statement started above: `modifier = modifier,`
        modifier = modifier,
        // continues the statement started above: `enter = fadeIn(`
        enter = fadeIn(
            // continues the statement started above: `animationSpec = tween(EnterDurationMs, delayMillis = delayM…`
            animationSpec = tween(EnterDurationMs, delayMillis = delayMs, easing = EaseOutCubic),
        // continues the statement started above: `) + slideInVertically(`
        ) + slideInVertically(
            // continues the statement started above: `animationSpec = tween(EnterDurationMs, delayMillis = delayM…`
            animationSpec = tween(EnterDurationMs, delayMillis = delayMs, easing = EaseOutCubic),
            // continues the statement started above: `initialOffsetY = { it / 4 },`
            initialOffsetY = { it / 4 },
        // closes the multi-line argument list started above
        ),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `content` with arguments `()`
        content()
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `AutoSlide` taking 2 parameters (`scrollState`, `oneLapPx`) and opens its body
private fun AutoSlide(scrollState: ScrollState, oneLapPx: Int) {
    // calls `LaunchedEffect` with arguments `(oneLapPx)` and opens a trailing lambda / block
    LaunchedEffect(oneLapPx) {
        // `if` statement: executes `return@LaunchedEffect` when `oneLapPx <= 0` is true
        if (oneLapPx <= 0) return@LaunchedEffect
        // declares read-only property `durationMs`, initialised to `(oneLapPx / CarouselPxPerSecond * 1000)`
        val durationMs = (oneLapPx / CarouselPxPerSecond * 1000)
            // chained call `.toInt` on the previous result
            .toInt()
            // chained call `.coerceAtLeast` on the previous result with arguments `(1)`
            .coerceAtLeast(1)
        // calls `scrollTo` on `scrollState` with arguments `(0)`
        scrollState.scrollTo(0)
        // `while` loop: repeats the block below as long as `true` is true
        while (true) {
            // calls `animateScrollTo` on `scrollState` with arguments `(oneLapPx, animationSpec = tween(durationMs, …)`
            scrollState.animateScrollTo(oneLapPx, animationSpec = tween(durationMs, easing = LinearEasing))
            // calls `scrollTo` on `scrollState` with arguments `(0)`
            scrollState.scrollTo(0)
        // closes the while loop
        }
    // closes the lambda passed to `LaunchedEffect`
    }
// closes the function `AutoSlide`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `FeatureHighlightsRow` taking 1 parameter (`modifier`) and opens its body
private fun FeatureHighlightsRow(modifier: Modifier = Modifier) {
    // calls `Row` with an argument list that continues on the following lines
    Row(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.clip(RoundedCornerShape(20.dp))`
            .clip(RoundedCornerShape(20.dp))
            // continues the statement started above: `.background(White.copy(alpha = 0.12f))`
            .background(White.copy(alpha = 0.12f))
            // continues the statement started above: `.border(1.dp, White.copy(alpha = 0.22f), RoundedCornerShape…`
            .border(1.dp, White.copy(alpha = 0.22f), RoundedCornerShape(20.dp))
            // continues the statement started above: `.padding(vertical = 10.dp, horizontal = 14.dp),`
            .padding(vertical = 10.dp, horizontal = 14.dp),
        // continues the statement started above: `horizontalArrangement = Arrangement.SpaceEvenly,`
        horizontalArrangement = Arrangement.SpaceEvenly,
        // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
        verticalAlignment = Alignment.CenterVertically,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `listOf` with an argument list that continues on the following lines
        listOf(
            // continues the statement started above: `"Offline Sync",`
            "Offline Sync",
            // continues the statement started above: `"Itineraries",`
            "Itineraries",
            // continues the statement started above: `"Weather",`
            "Weather",
        // continues the statement started above: `).forEachIndexed { index, feature ->`
        ).forEachIndexed { index, feature ->
            // continues the statement started above: `if (index > 0) {`
            if (index > 0) {
                // calls `Box` with an argument list that continues on the following lines
                Box(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.height(14.dp)`
                        .height(14.dp)
                        // continues the statement started above: `.width(1.dp)`
                        .width(1.dp)
                        // continues the statement started above: `.background(White.copy(alpha = 0.25f)),`
                        .background(White.copy(alpha = 0.25f)),
                // closes the multi-line argument list started above
                )
            // closes the block
            }
            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = feature,`
                text = feature,
                // continues the statement started above: `color = White,`
                color = White,
                // continues the statement started above: `fontSize = 11.sp,`
                fontSize = 11.sp,
                // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                fontWeight = FontWeight.SemiBold,
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the block
    }
// closes the function `FeatureHighlightsRow`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun FrostedGlassContainerCard(`
private fun FrostedGlassContainerCard(
    // continues the statement started above: `onGoogleContinueClick: () -> Unit,`
    onGoogleContinueClick: () -> Unit,
    // continues the statement started above: `onGitHubContinueClick: (() -> Unit)?,`
    onGitHubContinueClick: (() -> Unit)?,
    // continues the statement started above: `isLoading: Boolean,`
    isLoading: Boolean,
    // continues the statement started above: `loadingProvider: AuthProvider?,`
    loadingProvider: AuthProvider?,
    // continues the statement started above: `errorMessage: String?,`
    errorMessage: String?,
    // continues the statement started above: `modifier: Modifier = Modifier,`
    modifier: Modifier = Modifier,
// ends the argument list started above and opens the block that follows
) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.fillMaxWidth()`
            .fillMaxWidth()
            // continues the statement started above: `.shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp…`
            .shadow(elevation = 16.dp, shape = RoundedCornerShape(28.dp))
            // continues the statement started above: `.clip(RoundedCornerShape(28.dp))`
            .clip(RoundedCornerShape(28.dp))
            // continues the statement started above: `.background(`
            .background(
                // continues the statement started above: `Brush.verticalGradient(`
                Brush.verticalGradient(
                    // continues the statement started above: `colors = listOf(`
                    colors = listOf(
                        // continues the statement started above: `White.copy(alpha = 0.22f),`
                        White.copy(alpha = 0.22f),
                        // continues the statement started above: `White.copy(alpha = 0.12f),`
                        White.copy(alpha = 0.12f),
                    // closes the multi-line argument list started above
                    ),
                // closes the multi-line argument list started above
                ),
            // closes the multi-line argument list started above
            )
            // continues the statement started above: `.border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(…`
            .border(1.dp, White.copy(alpha = 0.3f), RoundedCornerShape(28.dp))
            // continues the statement started above: `.padding(18.dp),`
            .padding(18.dp),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Column` with an argument list that continues on the following lines
        Column(
            // continues the statement started above: `modifier = Modifier.fillMaxWidth(),`
            modifier = Modifier.fillMaxWidth(),
            // continues the statement started above: `horizontalAlignment = Alignment.CenterHorizontally,`
            horizontalAlignment = Alignment.CenterHorizontally,
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `Row` with an argument list that continues on the following lines
            Row(
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxWidth()`
                    .fillMaxWidth()
                    // continues the statement started above: `.height(50.dp)`
                    .height(50.dp)
                    // continues the statement started above: `.clip(RoundedCornerShape(16.dp))`
                    .clip(RoundedCornerShape(16.dp))
                    // continues the statement started above: `.background(White)`
                    .background(White)
                    // continues the statement started above: `.clickable(enabled = !isLoading, onClick = onGoogleContinue…`
                    .clickable(enabled = !isLoading, onClick = onGoogleContinueClick),
                // continues the statement started above: `horizontalArrangement = Arrangement.Center,`
                horizontalArrangement = Arrangement.Center,
                // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                verticalAlignment = Alignment.CenterVertically,
            // ends the argument list started above and opens the block that follows
            ) {
                // calls `Icon` with an argument list that continues on the following lines
                Icon(
                    // continues the statement started above: `painter = painterResource(R.drawable.ic_google),`
                    painter = painterResource(R.drawable.ic_google),
                    // continues the statement started above: `contentDescription = null,`
                    contentDescription = null,
                    // continues the statement started above: `tint = Color.Unspecified,`
                    tint = Color.Unspecified,
                    // continues the statement started above: `modifier = Modifier.size(20.dp),`
                    modifier = Modifier.size(20.dp),
                // closes the multi-line argument list started above
                )
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = if (isLoading && loadingProvider == AuthProvider.GOO…`
                    text = if (isLoading && loadingProvider == AuthProvider.GOOGLE) {
                        // calls `stringResource` with arguments `(R.string.auth_signing_in)`
                        stringResource(R.string.auth_signing_in)
                    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                    } else {
                        // calls `stringResource` with arguments `(R.string.welcome_google_cta)`
                        stringResource(R.string.welcome_google_cta)
                    // closes the else branch
                    },
                    // continues the statement started above: `color = WaypointGoogleText,`
                    color = WaypointGoogleText,
                    // continues the statement started above: `fontSize = 14.sp,`
                    fontSize = 14.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `modifier = Modifier.padding(start = 10.dp),`
                    modifier = Modifier.padding(start = 10.dp),
                // closes the multi-line argument list started above
                )
            // closes the block
            }

            // `if` statement: the block below runs when `onGitHubContinueClick != null` is true
            if (onGitHubContinueClick != null) {
                // calls `Spacer` with arguments `(modifier = Modifier.height(10.dp))`
                Spacer(modifier = Modifier.height(10.dp))
                // calls `Row` with an argument list that continues on the following lines
                Row(
                    // continues the statement started above: `modifier = Modifier`
                    modifier = Modifier
                        // continues the statement started above: `.fillMaxWidth()`
                        .fillMaxWidth()
                        // continues the statement started above: `.height(50.dp)`
                        .height(50.dp)
                        // continues the statement started above: `.clip(RoundedCornerShape(16.dp))`
                        .clip(RoundedCornerShape(16.dp))
                        // continues the statement started above: `.background(Color(0xFF24292E))`
                        .background(Color(0xFF24292E))
                        // continues the statement started above: `.border(1.dp, White.copy(alpha = 0.2f), RoundedCornerShape(…`
                        .border(1.dp, White.copy(alpha = 0.2f), RoundedCornerShape(16.dp))
                        // continues the statement started above: `.clickable(enabled = !isLoading, onClick = onGitHubContinue…`
                        .clickable(enabled = !isLoading, onClick = onGitHubContinueClick),
                    // continues the statement started above: `horizontalArrangement = Arrangement.Center,`
                    horizontalArrangement = Arrangement.Center,
                    // continues the statement started above: `verticalAlignment = Alignment.CenterVertically,`
                    verticalAlignment = Alignment.CenterVertically,
                // ends the argument list started above and opens the block that follows
                ) {
                    // calls `Icon` with an argument list that continues on the following lines
                    Icon(
                        // continues the statement started above: `painter = painterResource(R.drawable.ic_github),`
                        painter = painterResource(R.drawable.ic_github),
                        // continues the statement started above: `contentDescription = null,`
                        contentDescription = null,
                        // continues the statement started above: `tint = White,`
                        tint = White,
                        // continues the statement started above: `modifier = Modifier.size(20.dp),`
                        modifier = Modifier.size(20.dp),
                    // closes the multi-line argument list started above
                    )
                    // calls `Text` with an argument list that continues on the following lines
                    Text(
                        // continues the statement started above: `text = if (isLoading && loadingProvider == AuthProvider.GIT…`
                        text = if (isLoading && loadingProvider == AuthProvider.GITHUB) {
                            // calls `stringResource` with arguments `(R.string.auth_signing_in)`
                            stringResource(R.string.auth_signing_in)
                        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
                        } else {
                            // calls `stringResource` with arguments `(R.string.welcome_github_cta)`
                            stringResource(R.string.welcome_github_cta)
                        // closes the else branch
                        },
                        // continues the statement started above: `color = White,`
                        color = White,
                        // continues the statement started above: `fontSize = 14.sp,`
                        fontSize = 14.sp,
                        // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                        fontWeight = FontWeight.SemiBold,
                        // continues the statement started above: `modifier = Modifier.padding(start = 10.dp),`
                        modifier = Modifier.padding(start = 10.dp),
                    // closes the multi-line argument list started above
                    )
                // closes the block
                }
            // closes the if block
            }

            // `if` statement: the block below runs when `errorMessage != null` is true
            if (errorMessage != null) {
                // calls `Text` with an argument list that continues on the following lines
                Text(
                    // continues the statement started above: `text = errorMessage,`
                    text = errorMessage,
                    // continues the statement started above: `color = Color(0xFFFF8A8A),`
                    color = Color(0xFFFF8A8A),
                    // continues the statement started above: `fontSize = 12.sp,`
                    fontSize = 12.sp,
                    // continues the statement started above: `fontWeight = FontWeight.SemiBold,`
                    fontWeight = FontWeight.SemiBold,
                    // continues the statement started above: `textAlign = TextAlign.Center,`
                    textAlign = TextAlign.Center,
                    // continues the statement started above: `modifier = Modifier.padding(top = 10.dp),`
                    modifier = Modifier.padding(top = 10.dp),
                // closes the multi-line argument list started above
                )
            // closes the if block
            }

            // calls `Text` with an argument list that continues on the following lines
            Text(
                // continues the statement started above: `text = stringResource(R.string.welcome_disclaimer),`
                text = stringResource(R.string.welcome_disclaimer),
                // continues the statement started above: `color = White.copy(alpha = 0.8f),`
                color = White.copy(alpha = 0.8f),
                // continues the statement started above: `fontSize = 11.sp,`
                fontSize = 11.sp,
                // continues the statement started above: `textAlign = TextAlign.Center,`
                textAlign = TextAlign.Center,
                // continues the statement started above: `modifier = Modifier.padding(top = 10.dp),`
                modifier = Modifier.padding(top = 10.dp),
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the block
    }
// closes the block
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `PulsingPinBadge` taking 1 parameter (`modifier`) and opens its body
private fun PulsingPinBadge(modifier: Modifier = Modifier) {
    // declares read-only property `infiniteTransition`, initialised with the result of calling `rememberInfiniteTransition(…)`
    val infiniteTransition = rememberInfiniteTransition(label = "pin_pulse")
    // declares read-only property `pulseScale`, delegated to `infiniteTransition.animateFloat(`
    val pulseScale by infiniteTransition.animateFloat(
        // continues the statement started above: `initialValue = 0.92f,`
        initialValue = 0.92f,
        // continues the statement started above: `targetValue = 1f,`
        targetValue = 1f,
        // continues the statement started above: `animationSpec = infiniteRepeatable(`
        animationSpec = infiniteRepeatable(
            // continues the statement started above: `animation = tween(1200, easing = EaseInOut),`
            animation = tween(1200, easing = EaseInOut),
            // continues the statement started above: `repeatMode = RepeatMode.Reverse,`
            repeatMode = RepeatMode.Reverse,
        // closes the multi-line argument list started above
        ),
        // continues the statement started above: `label = "pin_pulse_scale",`
        label = "pin_pulse_scale",
    // closes the multi-line argument list started above
    )
    // declares read-only property `pulseAlpha`, delegated to `infiniteTransition.animateFloat(`
    val pulseAlpha by infiniteTransition.animateFloat(
        // continues the statement started above: `initialValue = 0.12f,`
        initialValue = 0.12f,
        // continues the statement started above: `targetValue = 0.25f,`
        targetValue = 0.25f,
        // continues the statement started above: `animationSpec = infiniteRepeatable(`
        animationSpec = infiniteRepeatable(
            // continues the statement started above: `animation = tween(1200, easing = EaseInOut),`
            animation = tween(1200, easing = EaseInOut),
            // continues the statement started above: `repeatMode = RepeatMode.Reverse,`
            repeatMode = RepeatMode.Reverse,
        // closes the multi-line argument list started above
        ),
        // continues the statement started above: `label = "pin_pulse_alpha",`
        label = "pin_pulse_alpha",
    // closes the multi-line argument list started above
    )

    // calls `Box` with arguments `(modifier = modifier.size(56.dp), contentAlig…)` and opens a trailing lambda / block
    Box(modifier = modifier.size(56.dp), contentAlignment = Alignment.Center) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.size(42.dp)`
                .size(42.dp)
                // continues the statement started above: `.scale(pulseScale)`
                .scale(pulseScale)
                // continues the statement started above: `.alpha(pulseAlpha)`
                .alpha(pulseAlpha)
                // continues the statement started above: `.background(WaypointTerracotta, CircleShape),`
                .background(WaypointTerracotta, CircleShape),
        // closes the multi-line argument list started above
        )
        // calls `Icon` with an argument list that continues on the following lines
        Icon(
            // continues the statement started above: `painter = painterResource(R.drawable.ic_waypoint_pin),`
            painter = painterResource(R.drawable.ic_waypoint_pin),
            // continues the statement started above: `contentDescription = stringResource(R.string.welcome_pin_cd…`
            contentDescription = stringResource(R.string.welcome_pin_cd),
            // continues the statement started above: `tint = Color.Unspecified,`
            tint = Color.Unspecified,
            // continues the statement started above: `modifier = Modifier.size(width = 40.dp, height = 50.dp),`
            modifier = Modifier.size(width = 40.dp, height = 50.dp),
        // closes the multi-line argument list started above
        )
    // closes the lambda passed to `Box`
    }
// closes the function `PulsingPinBadge`
}

// expression: `private data class WelcomeDestination(`
private data class WelcomeDestination(
    // continues the statement started above: `val name: String,`
    val name: String,
    // continues the statement started above: `@param:DrawableRes val photoRes: Int,`
    @param:DrawableRes val photoRes: Int,
// closes the multi-line argument list started above
)

// declares private read-only property `destinations`, initialised with the result of calling `listOf(…)`
private val destinations = listOf(
    // continues the statement started above: `WelcomeDestination("Cape Town", R.drawable.dest_capetown),`
    WelcomeDestination("Cape Town", R.drawable.dest_capetown),
    // continues the statement started above: `WelcomeDestination("Bali", R.drawable.dest_bali),`
    WelcomeDestination("Bali", R.drawable.dest_bali),
    // continues the statement started above: `WelcomeDestination("Paris", R.drawable.dest_paris),`
    WelcomeDestination("Paris", R.drawable.dest_paris),
    // continues the statement started above: `WelcomeDestination("Tokyo", R.drawable.dest_tokyo),`
    WelcomeDestination("Tokyo", R.drawable.dest_tokyo),
    // continues the statement started above: `WelcomeDestination("Zanzibar", R.drawable.dest_zanzibar),`
    WelcomeDestination("Zanzibar", R.drawable.dest_zanzibar),
// closes the multi-line argument list started above
)

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares private function `DestinationCard` taking 2 parameters (`destination`, `modifier`) and opens its body
private fun DestinationCard(destination: WelcomeDestination, modifier: Modifier = Modifier) {
    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = modifier`
        modifier = modifier
            // continues the statement started above: `.width(DestinationCardWidth)`
            .width(DestinationCardWidth)
            // continues the statement started above: `.height(130.dp)`
            .height(130.dp)
            // continues the statement started above: `.shadow(elevation = 8.dp, shape = RoundedCornerShape(Radius…`
            .shadow(elevation = 8.dp, shape = RoundedCornerShape(RadiusCard))
            // continues the statement started above: `.clip(RoundedCornerShape(RadiusCard)),`
            .clip(RoundedCornerShape(RadiusCard)),
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Image` with an argument list that continues on the following lines
        Image(
            // continues the statement started above: `painter = painterResource(destination.photoRes),`
            painter = painterResource(destination.photoRes),
            // continues the statement started above: `contentDescription = destination.name,`
            contentDescription = destination.name,
            // continues the statement started above: `contentScale = ContentScale.Crop,`
            contentScale = ContentScale.Crop,
            // continues the statement started above: `modifier = Modifier.fillMaxSize(),`
            modifier = Modifier.fillMaxSize(),
        // closes the multi-line argument list started above
        )
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.fillMaxSize()`
                .fillMaxSize()
                // continues the statement started above: `.background(`
                .background(
                    // continues the statement started above: `Brush.verticalGradient(`
                    Brush.verticalGradient(
                        // continues the statement started above: `colors = listOf(Color.Transparent, WelcomeCardScrim),`
                        colors = listOf(Color.Transparent, WelcomeCardScrim),
                    // closes the multi-line argument list started above
                    ),
                // closes the multi-line argument list started above
                ),
        // closes the multi-line argument list started above
        )
        // calls `Text` with an argument list that continues on the following lines
        Text(
            // continues the statement started above: `text = destination.name,`
            text = destination.name,
            // continues the statement started above: `color = White,`
            color = White,
            // continues the statement started above: `fontSize = 13.sp,`
            fontSize = 13.sp,
            // continues the statement started above: `fontWeight = FontWeight.Bold,`
            fontWeight = FontWeight.Bold,
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.align(Alignment.BottomStart)`
                .align(Alignment.BottomStart)
                // continues the statement started above: `.padding(10.dp),`
                .padding(10.dp),
        // closes the multi-line argument list started above
        )
    // closes the block
    }
// closes the function `DestinationCard`
}
