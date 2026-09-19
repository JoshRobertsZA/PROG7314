// declares that this file belongs to the package `com.waypoint.app.features.splash.ui`
package com.waypoint.app.features.splash.ui

// imports `androidx.compose.animation.core.CubicBezierEasing` for use in this file
import androidx.compose.animation.core.CubicBezierEasing
// imports `androidx.compose.animation.core.Easing` for use in this file
import androidx.compose.animation.core.Easing
// imports `androidx.compose.foundation.Image` for use in this file
import androidx.compose.foundation.Image
// imports `androidx.compose.foundation.background` for use in this file
import androidx.compose.foundation.background
// imports `androidx.compose.foundation.layout.Box` for use in this file
import androidx.compose.foundation.layout.Box
// imports `androidx.compose.foundation.layout.fillMaxSize` for use in this file
import androidx.compose.foundation.layout.fillMaxSize
// imports `androidx.compose.foundation.layout.height` for use in this file
import androidx.compose.foundation.layout.height
// imports `androidx.compose.foundation.layout.width` for use in this file
import androidx.compose.foundation.layout.width
// imports `androidx.compose.runtime.Composable` for use in this file
import androidx.compose.runtime.Composable
// imports `androidx.compose.runtime.LaunchedEffect` for use in this file
import androidx.compose.runtime.LaunchedEffect
// imports `androidx.compose.runtime.getValue` for use in this file
import androidx.compose.runtime.getValue
// imports `androidx.compose.runtime.mutableFloatStateOf` for use in this file
import androidx.compose.runtime.mutableFloatStateOf
// imports `androidx.compose.runtime.mutableStateOf` for use in this file
import androidx.compose.runtime.mutableStateOf
// imports `androidx.compose.runtime.remember` for use in this file
import androidx.compose.runtime.remember
// imports `androidx.compose.runtime.setValue` for use in this file
import androidx.compose.runtime.setValue
// imports `androidx.compose.runtime.withFrameNanos` for use in this file
import androidx.compose.runtime.withFrameNanos
// imports `androidx.compose.ui.Alignment` for use in this file
import androidx.compose.ui.Alignment
// imports `androidx.compose.ui.Modifier` for use in this file
import androidx.compose.ui.Modifier
// imports `androidx.compose.ui.draw.drawWithContent` for use in this file
import androidx.compose.ui.draw.drawWithContent
// imports `androidx.compose.ui.graphics.TransformOrigin` for use in this file
import androidx.compose.ui.graphics.TransformOrigin
// imports `androidx.compose.ui.graphics.drawscope.clipRect` for use in this file
import androidx.compose.ui.graphics.drawscope.clipRect
// imports `androidx.compose.ui.graphics.graphicsLayer` for use in this file
import androidx.compose.ui.graphics.graphicsLayer
// imports `androidx.compose.ui.res.colorResource` for use in this file
import androidx.compose.ui.res.colorResource
// imports `androidx.compose.ui.res.painterResource` for use in this file
import androidx.compose.ui.res.painterResource
// imports `androidx.compose.ui.unit.dp` for use in this file
import androidx.compose.ui.unit.dp
// imports `com.waypoint.app.R` for use in this file
import com.waypoint.app.R
// imports `kotlinx.coroutines.isActive` for use in this file
import kotlinx.coroutines.isActive

// declares private read-only property `PinEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val PinEasing: Easing = CubicBezierEasing(0.2f, 0.8f, 0.2f, 1f)
// declares private read-only property `DotEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val DotEasing: Easing = CubicBezierEasing(0.3f, 1.6f, 0.5f, 1f)
// declares private read-only property `ShadowEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val ShadowEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
// declares private read-only property `WipeEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val WipeEasing: Easing = CubicBezierEasing(0.6f, 0f, 0.2f, 1f)
// declares private read-only property `HaloInEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val HaloInEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)
// declares private read-only property `BreatheEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val BreatheEasing: Easing = CubicBezierEasing(0.42f, 0f, 0.58f, 1f)
// declares private read-only property `PingEasing` of type `Easing`, initialised with the result of calling `CubicBezierEasing(…)`
private val PingEasing: Easing = CubicBezierEasing(0f, 0f, 0.58f, 1f)

// declares private const read-only property `PinDelay`, initialised to the number 0f
private const val PinDelay = 0f
// declares private const read-only property `PinDuration`, initialised to the number 600f
private const val PinDuration = 600f
// declares private const read-only property `DotDelay`, initialised to the number 400f
private const val DotDelay = 400f
// declares private const read-only property `DotDuration`, initialised to the number 450f
private const val DotDuration = 450f
// declares private const read-only property `ShadowDelay`, initialised to the number 0f
private const val ShadowDelay = 0f
// declares private const read-only property `ShadowDuration`, initialised to the number 600f
private const val ShadowDuration = 600f
// declares private const read-only property `WipeDelay`, initialised to the number 700f
private const val WipeDelay = 700f
// declares private const read-only property `WipeDuration`, initialised to the number 700f
private const val WipeDuration = 700f
// declares private const read-only property `HaloDelay`, initialised to the number 300f
private const val HaloDelay = 300f
// declares private const read-only property `HaloDuration`, initialised to the number 600f
private const val HaloDuration = 600f
// declares private const read-only property `BreatheStart`, initialised to `HaloDelay + HaloDuration`
private const val BreatheStart = HaloDelay + HaloDuration
// declares private const read-only property `BreathePeriod`, initialised to the number 2400f
private const val BreathePeriod = 2400f
// declares private const read-only property `PingDelay`, initialised to the number 1100f
private const val PingDelay = 1100f
// declares private const read-only property `PingPeriod`, initialised to the number 2400f
private const val PingPeriod = 2400f

// declares private const read-only property `EntranceDoneAt`, initialised to `WipeDelay + WipeDuration`
private const val EntranceDoneAt = WipeDelay + WipeDuration
// declares private const read-only property `MinSplashDurationMs`, initialised to `EntranceDoneAt + 400f`
private const val MinSplashDurationMs = EntranceDoneAt + 400f

// declares private function `lerp` taking 3 parameters (`start`, `stop`, `fraction`); its body is the expression `start + (stop - start) * fraction`
private fun lerp(start: Float, stop: Float, fraction: Float) = start + (stop - start) * fraction

// declares private function `progressOf` taking 4 parameters (`elapsed`, `delay`, `duration`, `easing`), returning `Float` and opens its body
private fun progressOf(elapsed: Float, delay: Float, duration: Float, easing: Easing): Float {
    // declares read-only property `raw`, initialised to `((elapsed - delay) / duration).coerceIn(0f, …`
    val raw = ((elapsed - delay) / duration).coerceIn(0f, 1f)
    // returns `easing.transform(raw)` from the current function
    return easing.transform(raw)
// closes the function `progressOf`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// declares function `SplashScreen` taking 1 parameter (`onFinished`) and opens its body
fun SplashScreen(onFinished: () -> Unit) {
    // declares mutable property `elapsed`, delegated to `remember { mutableFloatStateOf(0f) }`
    var elapsed by remember { mutableFloatStateOf(0f) }
    // declares mutable property `hasFinished`, delegated to `remember { mutableStateOf(false) }`
    var hasFinished by remember { mutableStateOf(false) }

    // calls `LaunchedEffect` with arguments `(Unit)` and opens a trailing lambda / block
    LaunchedEffect(Unit) {
        // declares read-only property `startFrame`, initialised with the result of calling `withFrameMillisCompat(…)`
        val startFrame = withFrameMillisCompat()
        // `while` loop: repeats the block below as long as `isActive` is true
        while (isActive) {
            // declares read-only property `now`, initialised with the result of calling `withFrameMillisCompat(…)`
            val now = withFrameMillisCompat()
            // assigns `elapsed` the value `(now - startFrame).toFloat()`
            elapsed = (now - startFrame).toFloat()
            // `if` statement: the block below runs when `elapsed >= MinSplashDurationMs && !hasFinished` is true
            if (elapsed >= MinSplashDurationMs && !hasFinished) {
                // assigns `hasFinished` the value `true`
                hasFinished = true
                // calls `onFinished` with arguments `()`
                onFinished()
            // closes the if block
            }
        // closes the while loop
        }
    // closes the lambda passed to `LaunchedEffect`
    }

    // declares read-only property `pinP`, initialised with the result of calling `progressOf(…)`
    val pinP = progressOf(elapsed, PinDelay, PinDuration, PinEasing)
    // declares read-only property `pinAlpha`, initialised to `pinP`
    val pinAlpha = pinP
    // declares read-only property `pinScale`, initialised with the result of calling `lerp(…)`
    val pinScale = lerp(0.4f, 1f, pinP)

    // declares read-only property `dotP`, initialised with the result of calling `progressOf(…)`
    val dotP = progressOf(elapsed, DotDelay, DotDuration, DotEasing)

    // declares read-only property `shadowP`, initialised with the result of calling `progressOf(…)`
    val shadowP = progressOf(elapsed, ShadowDelay, ShadowDuration, ShadowEasing)

    // declares read-only property `wipeP`, initialised with the result of calling `progressOf(…)`
    val wipeP = progressOf(elapsed, WipeDelay, WipeDuration, WipeEasing)

    // declares read-only property `haloAlpha` of type `Float`
    val haloAlpha: Float
    // declares read-only property `haloScale` of type `Float`
    val haloScale: Float
    // `if` statement: the block below runs when `elapsed < BreatheStart` is true
    if (elapsed < BreatheStart) {
        // declares read-only property `haloP`, initialised with the result of calling `progressOf(…)`
        val haloP = progressOf(elapsed, HaloDelay, HaloDuration, HaloInEasing)
        // assigns `haloAlpha` the value `lerp(0f, 0.25f, haloP)`
        haloAlpha = lerp(0f, 0.25f, haloP)
        // assigns `haloScale` the value `lerp(0.5f, 1f, haloP)`
        haloScale = lerp(0.5f, 1f, haloP)
    // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
    } else {
        // declares read-only property `cycle`, initialised to `((elapsed - BreatheStart) % BreathePeriod) /…`
        val cycle = ((elapsed - BreatheStart) % BreathePeriod) / BreathePeriod
        // declares read-only property `triangle`, initialised with the result of calling `if(…)`
        val triangle = if (cycle < 0.5f) cycle / 0.5f else (1f - cycle) / 0.5f
        // declares read-only property `breatheT`, initialised with the result of calling `BreatheEasing.transform(…)`
        val breatheT = BreatheEasing.transform(triangle)
        // assigns `haloScale` the value `lerp(1f, 0.92f, breatheT)`
        haloScale = lerp(1f, 0.92f, breatheT)
        // assigns `haloAlpha` the value `lerp(0.25f, 0.14f, breatheT)`
        haloAlpha = lerp(0.25f, 0.14f, breatheT)
    // closes the else branch
    }

    // declares mutable property `pingAlpha`, initialised to the number 0f
    var pingAlpha = 0f
    // declares mutable property `pingScale`, initialised to the number 0.4f
    var pingScale = 0.4f
    // `if` statement: the block below runs when `elapsed >= PingDelay` is true
    if (elapsed >= PingDelay) {
        // declares read-only property `cycleMs`, initialised to `(elapsed - PingDelay) % PingPeriod`
        val cycleMs = (elapsed - PingDelay) % PingPeriod
        // declares read-only property `cycleT`, initialised to `cycleMs / PingPeriod`
        val cycleT = cycleMs / PingPeriod
        // `if` statement: the block below runs when `cycleT <= 0.7f` is true
        if (cycleT <= 0.7f) {
            // declares read-only property `local`, initialised with the result of calling `PingEasing.transform(…)`
            val local = PingEasing.transform(cycleT / 0.7f)
            // assigns `pingScale` the value `lerp(0.4f, 1.35f, local)`
            pingScale = lerp(0.4f, 1.35f, local)
            // assigns `pingAlpha` the value `lerp(0.5f, 0f, local)`
            pingAlpha = lerp(0.5f, 0f, local)
        // closes the previous branch and opens the `else` branch, which runs when none of the conditions above matched
        } else {
            // assigns `pingScale` the value `1.35f`
            pingScale = 1.35f
            // assigns `pingAlpha` the value `0f`
            pingAlpha = 0f
        // closes the else branch
        }
    // closes the if block
    }

    // calls `Box` with an argument list that continues on the following lines
    Box(
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.background(colorResource(R.color.waypoint_splash_bg)),`
            .background(colorResource(R.color.waypoint_splash_bg)),
        // continues the statement started above: `contentAlignment = Alignment.Center,`
        contentAlignment = Alignment.Center,
    // ends the argument list started above and opens the block that follows
    ) {
        // calls `Box` with an argument list that continues on the following lines
        Box(
            // continues the statement started above: `modifier = Modifier`
            modifier = Modifier
                // continues the statement started above: `.width(240.dp)`
                .width(240.dp)
                // continues the statement started above: `.height(320.dp),`
                .height(320.dp),
        // ends the argument list started above and opens the block that follows
        ) {
            // calls `SplashLayer` with an argument list that continues on the following lines
            SplashLayer(
                // continues the statement started above: `drawable = R.drawable.ic_splash_shadow,`
                drawable = R.drawable.ic_splash_shadow,
                // continues the statement started above: `alpha = 1f,`
                alpha = 1f,
                // continues the statement started above: `scale = shadowP,`
                scale = shadowP,
                // continues the statement started above: `pivotFraction = TransformOrigin(0.5f, 225.5f / 320f),`
                pivotFraction = TransformOrigin(0.5f, 225.5f / 320f),
            // closes the multi-line argument list started above
            )
            // calls `SplashLayer` with an argument list that continues on the following lines
            SplashLayer(
                // continues the statement started above: `drawable = R.drawable.ic_splash_ping,`
                drawable = R.drawable.ic_splash_ping,
                // continues the statement started above: `alpha = pingAlpha,`
                alpha = pingAlpha,
                // continues the statement started above: `scale = pingScale,`
                scale = pingScale,
                // continues the statement started above: `pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),`
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            // closes the multi-line argument list started above
            )
            // calls `SplashLayer` with an argument list that continues on the following lines
            SplashLayer(
                // continues the statement started above: `drawable = R.drawable.ic_splash_halo,`
                drawable = R.drawable.ic_splash_halo,
                // continues the statement started above: `alpha = haloAlpha,`
                alpha = haloAlpha,
                // continues the statement started above: `scale = haloScale,`
                scale = haloScale,
                // continues the statement started above: `pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),`
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            // closes the multi-line argument list started above
            )
            // calls `SplashLayer` with an argument list that continues on the following lines
            SplashLayer(
                // continues the statement started above: `drawable = R.drawable.ic_splash_pin,`
                drawable = R.drawable.ic_splash_pin,
                // continues the statement started above: `alpha = pinAlpha,`
                alpha = pinAlpha,
                // continues the statement started above: `scale = pinScale,`
                scale = pinScale,
                // continues the statement started above: `pivotFraction = TransformOrigin(0.5f, 202.5f / 320f),`
                pivotFraction = TransformOrigin(0.5f, 202.5f / 320f),
            // closes the multi-line argument list started above
            )
            // calls `SplashLayer` with an argument list that continues on the following lines
            SplashLayer(
                // continues the statement started above: `drawable = R.drawable.ic_splash_dot,`
                drawable = R.drawable.ic_splash_dot,
                // continues the statement started above: `alpha = pinAlpha,`
                alpha = pinAlpha,
                // continues the statement started above: `scale = dotP,`
                scale = dotP,
                // continues the statement started above: `pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),`
                pivotFraction = TransformOrigin(0.5f, 82.5f / 320f),
            // closes the multi-line argument list started above
            )
            // calls `Image` with an argument list that continues on the following lines
            Image(
                // continues the statement started above: `painter = painterResource(R.drawable.ic_splash_wordmark),`
                painter = painterResource(R.drawable.ic_splash_wordmark),
                // continues the statement started above: `contentDescription = null,`
                contentDescription = null,
                // continues the statement started above: `modifier = Modifier`
                modifier = Modifier
                    // continues the statement started above: `.fillMaxSize()`
                    .fillMaxSize()
                    // continues the statement started above: `.drawWithContent {`
                    .drawWithContent {
                        // declares read-only property `revealRight`, initialised to `size.width * wipeP`
                        val revealRight = size.width * wipeP
                        // calls `clipRect` with arguments `(left = 0f, top = 0f, right = revealRight, bo…)` and opens a trailing lambda / block
                        clipRect(left = 0f, top = 0f, right = revealRight, bottom = size.height) {
                            // statement: `this@drawWithContent.drawContent()`
                            this@drawWithContent.drawContent()
                        // closes the lambda passed to `clipRect`
                        }
                    // closes the block
                    },
            // closes the multi-line argument list started above
            )
        // closes the block
        }
    // closes the block
    }
// closes the function `SplashScreen`
}

// annotation `@Composable` applied to the declaration that follows
@Composable
// expression: `private fun SplashLayer(`
private fun SplashLayer(
    // continues the statement started above: `drawable: Int,`
    drawable: Int,
    // continues the statement started above: `alpha: Float,`
    alpha: Float,
    // continues the statement started above: `scale: Float,`
    scale: Float,
    // continues the statement started above: `pivotFraction: TransformOrigin,`
    pivotFraction: TransformOrigin,
// ends the argument list started above and opens the block that follows
) {
    // calls `Image` with an argument list that continues on the following lines
    Image(
        // continues the statement started above: `painter = painterResource(drawable),`
        painter = painterResource(drawable),
        // continues the statement started above: `contentDescription = null,`
        contentDescription = null,
        // continues the statement started above: `modifier = Modifier`
        modifier = Modifier
            // continues the statement started above: `.fillMaxSize()`
            .fillMaxSize()
            // continues the statement started above: `.graphicsLayer {`
            .graphicsLayer {
                // assigns `this.alpha` the value `alpha.coerceIn(0f, 1f)`
                this.alpha = alpha.coerceIn(0f, 1f)
                // assigns `this.scaleX` the value `scale`
                this.scaleX = scale
                // assigns `this.scaleY` the value `scale`
                this.scaleY = scale
                // assigns `this.transformOrigin` the value `pivotFraction`
                this.transformOrigin = pivotFraction
            // closes the block
            },
    // closes the multi-line argument list started above
    )
// closes the block
}

// declares private suspend function `withFrameMillisCompat` taking no parameters, returning `Long` and opens its body
private suspend fun withFrameMillisCompat(): Long {
    // declares mutable property `result`, initialised to the number 0L
    var result = 0L
    // expression: `withFrameNanos { result = it / 1_000_000L }`
    withFrameNanos { result = it / 1_000_000L }
    // returns `result` from the current function
    return result
// closes the function `withFrameMillisCompat`
}
