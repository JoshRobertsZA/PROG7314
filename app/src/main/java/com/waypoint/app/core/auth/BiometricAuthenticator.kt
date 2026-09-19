// declares that this file belongs to the package `com.waypoint.app.core.auth`
package com.waypoint.app.core.auth

// imports `android.content.Context` for use in this file
import android.content.Context
// imports `androidx.biometric.BiometricManager` for use in this file
import androidx.biometric.BiometricManager
// imports `androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK` for use in this file
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
// imports `androidx.biometric.BiometricPrompt` for use in this file
import androidx.biometric.BiometricPrompt
// imports `androidx.core.content.ContextCompat` for use in this file
import androidx.core.content.ContextCompat
// imports `androidx.fragment.app.FragmentActivity` for use in this file
import androidx.fragment.app.FragmentActivity

// declares enum class `BiometricAvailability` and opens its body
enum class BiometricAvailability {
    // expression: `AVAILABLE,`
    AVAILABLE,
    // continues the statement started above: `NO_HARDWARE,`
    NO_HARDWARE,
    // continues the statement started above: `NONE_ENROLLED,`
    NONE_ENROLLED,
    // continues the statement started above: `UNAVAILABLE,`
    UNAVAILABLE,
// closes the class `BiometricAvailability`
}

// declares object `BiometricAuthenticator` and opens its body
object BiometricAuthenticator {

    // declares function `availability` taking 1 parameter (`context`), returning `BiometricAvailability` and opens its body
    fun availability(context: Context): BiometricAvailability {
        // declares read-only property `manager`, initialised with the result of calling `BiometricManager.from(…)`
        val manager = BiometricManager.from(context)
        // returns `when (manager.canAuthenticate(BIOMETRIC_WEAK)) {` from the current function
        return when (manager.canAuthenticate(BIOMETRIC_WEAK)) {
            // lambda `BiometricManager.BIOMETRIC_SU… -> BiometricAvailability.AVAILAB…`
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.AVAILABLE
            // expression: `BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,`
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            // continues the statement started above: `BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Biometri…`
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailability.NO_HARDWARE
            // lambda `BiometricManager.BIOMETRIC_ER… -> BiometricAvailability.NONE_EN…`
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailability.NONE_ENROLLED
            // `else` branch of the `when`: evaluates `BiometricAvailability.UNAVAILABLE`
            else -> BiometricAvailability.UNAVAILABLE
        // closes the block
        }
    // closes the function `availability`
    }

    // expression: `fun authenticate(`
    fun authenticate(
        // continues the statement started above: `activity: FragmentActivity,`
        activity: FragmentActivity,
        // continues the statement started above: `title: String,`
        title: String,
        // continues the statement started above: `subtitle: String,`
        subtitle: String,
        // continues the statement started above: `negativeButtonText: String,`
        negativeButtonText: String,
        // continues the statement started above: `onSuccess: () -> Unit,`
        onSuccess: () -> Unit,
        // continues the statement started above: `onError: (errorCode: Int, message: CharSequence) -> Unit,`
        onError: (errorCode: Int, message: CharSequence) -> Unit,
    // ends the argument list started above and opens the block that follows
    ) {
        // declares read-only property `executor`, initialised with the result of calling `ContextCompat.getMainExecutor(…)`
        val executor = ContextCompat.getMainExecutor(activity)
        // declares read-only property `callback`, initialised to `object : BiometricPrompt.AuthenticationCallb…` and opens a lambda / block
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            // declares override function `onAuthenticationSucceeded` taking 1 parameter (`result`) and opens its body
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                // calls `onSuccess` with arguments `()`
                onSuccess()
            // closes the function `onAuthenticationSucceeded`
            }

            // declares override function `onAuthenticationError` taking 2 parameters (`errorCode`, `errString`) and opens its body
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                // calls `onError` with arguments `(errorCode, errString)`
                onError(errorCode, errString)
            // closes the function `onAuthenticationError`
            }

        // closes the lambda assigned to `callback`
        }

        // declares read-only property `promptInfo`, initialised with the result of calling `BiometricPrompt.PromptInfo.Builder(…)`
        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            // chained call `.setTitle` on the previous result with arguments `(title)`
            .setTitle(title)
            // chained call `.setSubtitle` on the previous result with arguments `(subtitle)`
            .setSubtitle(subtitle)
            // chained call `.setAllowedAuthenticators` on the previous result with arguments `(BIOMETRIC_WEAK)`
            .setAllowedAuthenticators(BIOMETRIC_WEAK)
            // chained call `.setNegativeButtonText` on the previous result with arguments `(negativeButtonText)`
            .setNegativeButtonText(negativeButtonText)
            // chained call `.build` on the previous result
            .build()

        // calls `BiometricPrompt` with arguments `(activity, executor, callback)`, then chains `.authenticate(promptInfo)`
        BiometricPrompt(activity, executor, callback).authenticate(promptInfo)
    // closes the block
    }
// closes the object `BiometricAuthenticator`
}
