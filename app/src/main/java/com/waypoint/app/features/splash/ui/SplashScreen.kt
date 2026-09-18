package com.waypoint.app.features.splash.ui

import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.waypoint.app.R
import kotlinx.coroutines.isActive

/**
 * Matches the waypoint-splash.svg design 1:1 - see that file for the
 * original CSS keyframe timings/easings this reproduces natively, since
 * Android vector drawables can't run embedded SVG CSS animation.
 *
 * Timeline (ms from first frame): pin fades/scales in at 100-800, shadow
 * scales in alongside it, the white dot bounces in at 600-1100, the
 * wordmark wipes in left-to-right at 1000-1800, then the halo settles into
 * an infinite "breathe" loop and the ping ring loops an expanding fade -
 * both starting once the entrance finishes.
 */
private val PinEasing: Easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)
private val DotEasing: Easing = CubicBezierEasing(0.3f, 1.6f, 0.5f, 1f)
private val ShadowEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
private val WipeEasing: Easing = CubicBezierEasing(0.6f, 0f, 0.2f, 1f)
private val HaloInEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
private val BreatheEasing: Easing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
private val PingEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)

private const val PinDelay = 0f
private const val PinDuration = 600f
private const val DotDelay = 400f
private const val DotDuration = 450f
private const val ShadowDelay = 0f
private const val ShadowDuration = 600f
private const val WipeDelay = 700f
private const val WipeDuration = 700f
private const val HaloDelay = 300f
private const val HaloDuration = 600f
private const val BreatheStart = HaloDelay + HaloDuration
private const val BreathePeriod = 2400f
private const val PingDelay = 1100f
private const val PingPeriod = 2400f

private const val EntranceDoneAt = WipeDelay + WipeDuration
private const val MinSplashDurationMs = EntranceDoneAt + 400f

private fun lerp(start: Float, stop: Float, fraction: Float) = start + (stop - start) * fraction

private fun progressOf(elapsed: Float, delay: Float, duration: Float, easing: Easing): Float {
    val raw = ((elapsed - delay) / duration).coerceIn(0f, 1f)
    return easing.transform(raw)
}

@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var elapsed by remember { mutableFloatStateOf(0f) }
    var hasFinished by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        val startFrame = withFrameMillisCompat()
        while (isActive) {
            val now = withFrameMillisCompat()
            elapsed = (now - startFrame).toFloat()
            if (elapsed >= MinSplashDurationMs && !hasFinished) {
                hasFinished = true
                onFinished()
            }
        }
    }

    val pinP = progressOf(elapsed, PinDelay, PinDuration, PinEasing)
    val pinAlpha = pinP
    val pinScale = lerp(0.4f, 1f, pinP)

    val dotP = progressOf(elapsed, DotDelay, DotDuration, DotEasing)

    val shadowP = progressOf(elapsed, ShadowDelay, ShadowDuration, ShadowEasing)

    val wipeP = progressOf(elapsed, WipeDelay, WipeDuration, WipeEasing)

    val haloAlpha: Float
    val haloScale: Float
    if (elapsed < BreatheStart) {
        val haloP = progressOf(elapsed, HaloDelay, HaloDuration, HaloInEasing)
        haloAlpha = lerp(0f, 0.25f, haloP)
        haloScale = lerp(0.5f, 1f, haloP)
    } else {
        val cycle = ((elapsed - BreatheStart) % BreathePeriod) / BreathePeriod
        val triangle = if (cycle < 0.5f) cycle / 0.5f else (1f - cycle) / 0.5f
        val breatheT = BreatheEasing.transform(triangle)
        haloScale = lerp(1f, 0.92f, breatheT)
        haloAlpha = lerp(0.25f, 0.14f, breatheT)
    }

    var pingAlpha = 0f
    var pingScale = 0.4f
    if (elapsed >= PingDelay) {
        val cycleMs = (elapsed - PingDelay) % PingPeriod
        val cycleT = cycleMs / PingPeriod
        if (cycleT <= 0.7f) {
            val local = PingEasing.transform(cycleT / 0.7f)
            pingScale = lerp(0.4f, 1.35f, local)
            pingAlpha = lerp(0.5f, 0f, local)
        } else {
            pingScale = 1.35f
            pingAlpha = 0f
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.waypoint_splash_bg)),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .width(240.dp)
                .height(320.dp),
        ) {
            SplashLayer(
                drawable = R.drawable.ic_splash_shadow,
                alpha = 1f,
                scale = shadowP,
                pivotFraction = TransformOrigin(0.5f, 225.5f / 320f),
            )
            SplashLayer(
                drawable = R.drawable.ic_splash_ping,
                alpha = pingAlpha,
                scale = pingScale,
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            )
            SplashLayer(
                drawable = R.drawable.ic_splash_halo,
                alpha = haloAlpha,
                scale = haloScale,
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            )
            SplashLayer(
                drawable = R.drawable.ic_splash_pin,
                alpha = pinAlpha,
                scale = pinScale,
                pivotFraction = TransformOrigin(0.5f, 202.5f / 320f),
            )
            SplashLayer(
                drawable = R.drawable.ic_splash_dot,
                alpha = pinAlpha,
                scale = dotP,
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            )
            Image(
                painter = painterResource(R.drawable.ic_splash_wordmark),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .drawWithContent {
                        val revealRight = size.width * wipeP
                        clipRect(left = 0f, top = 0f, right = revealRight, bottom = size.height) {
                            this@drawWithContent.drawContent()
                        }
                    },
            )
        }
    }
}

@Composable
private fun SplashLayer(
    drawable: Int,
    alpha: Float,
    scale: Float,
    pivotFraction: TransformOrigin,
) {
    Image(
        painter = painterResource(drawable),
        contentDescription = null,
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer {
                this.alpha = alpha.coerceIn(0f, 1f)
                this.scaleX = scale
                this.scaleY = scale
                this.transformOrigin = pivotFraction
            },
    )
}

private suspend fun withFrameMillisCompat(): Long {
    var result = 0L
    withFrameNanos { result = it / 1_000_000L }
    return result
}
