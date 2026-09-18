package com.waypoint.app.core.auth

import android.content.Context
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity

/**
 * Whether this device can currently do biometric auth at all - check this
 * before offering the Settings toggle or attempting BiometricAuthenticator.authenticate,
 * since the underlying BiometricPrompt call throws/no-ops ungracefully
 * otherwise (e.g. an emulator with no fingerprint/face enrolled).
 */
enum class BiometricAvailability {
    AVAILABLE,
    NO_HARDWARE,
    NONE_ENROLLED,
    UNAVAILABLE,
}

/**
 * Thin wrapper around androidx.biometric's BiometricPrompt/BiometricManager.
 * Deliberately checks BIOMETRIC_WEAK rather than BIOMETRIC_STRONG - face
 * unlock on many devices is classified as WEAK (it doesn't always use
 * depth-sensing hardware), and STRONG-only would silently exclude it on
 * exactly the devices where "facial recognition" is what's enrolled.
 */
object BiometricAuthenticator {

    fun availability(context: Context): BiometricAvailability {
        val manager = BiometricManager.from(context)
        return when (manager.canAuthenticate(BIOMETRIC_WEAK)) {
            BiometricManager.BIOMETRIC_SUCCESS -> BiometricAvailability.AVAILABLE
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE,
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> BiometricAvailability.NO_HARDWARE
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> BiometricAvailability.NONE_ENROLLED
            else -> BiometricAvailability.UNAVAILABLE
        }
    }

    /**
     * Shows the system biometric prompt (whatever the device has enrolled -
     * face, fingerprint, or both). [onError] fires both for real errors and
     * for a user-initiated cancel (errorCode ERROR_NEGATIVE_BUTTON/
     * ERROR_USER_CANCELED) - callers that want to distinguish those can
     * inspect [errorCode], but for this app's use (an unlock gate) both
     * cases mean the same thing: stay locked, let the user retry.
     */
    fun authenticate(
        activity: FragmentActivity,
        title: String,
        subtitle: String,
        negativeButtonText: String,
        onSuccess: () -> Unit,
        onError: (errorCode: Int, message: CharSequence) -> Unit,
    ) {
        val executor = ContextCompat.getMainExecutor(activity)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                onSuccess()
            }

            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                onError(errorCode, errString)
            }

            // onAuthenticationFailed (a single failed scan, e.g. unrecognised
            // face) is intentionally not overridden - the system prompt
            // handles that UI itself and keeps waiting for another attempt.
        }

        val promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle(title)
            .setSubtitle(subtitle)
            .setAllowedAuthenticators(BIOMETRIC_WEAK)
            .setNegativeButtonText(negativeButtonText)
            .build()

        BiometricPrompt(activity, executor, callback).authenticate(promptInfo)
    }
}
