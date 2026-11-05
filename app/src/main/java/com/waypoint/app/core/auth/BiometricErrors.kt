package com.waypoint.app.core.auth

import androidx.annotation.StringRes
import androidx.biometric.BiometricPrompt
import com.waypoint.app.R

// The reasons the unlock prompt can fail, grouped so each one gets a clear message.
enum class BiometricFailure {
    LOCKOUT,
    LOCKOUT_PERMANENT,
    CANCELLED,
    NO_HARDWARE,
    NONE_ENROLLED,
    OTHER,
}

// Maps the error code from BiometricPrompt to a failure. Pure, so it can be unit tested.
fun classifyBiometricError(errorCode: Int): BiometricFailure = when (errorCode) {
    BiometricPrompt.ERROR_LOCKOUT -> BiometricFailure.LOCKOUT
    BiometricPrompt.ERROR_LOCKOUT_PERMANENT -> BiometricFailure.LOCKOUT_PERMANENT
    BiometricPrompt.ERROR_USER_CANCELED,
    BiometricPrompt.ERROR_CANCELED,
    BiometricPrompt.ERROR_NEGATIVE_BUTTON -> BiometricFailure.CANCELLED
    BiometricPrompt.ERROR_NO_BIOMETRICS,
    BiometricPrompt.ERROR_NO_DEVICE_CREDENTIAL -> BiometricFailure.NONE_ENROLLED
    BiometricPrompt.ERROR_HW_NOT_PRESENT,
    BiometricPrompt.ERROR_HW_UNAVAILABLE -> BiometricFailure.NO_HARDWARE
    else -> BiometricFailure.OTHER
}

/** Maps a biometric failure to the message shown on the lock screen. */
@StringRes
fun biometricFailureMessage(failure: BiometricFailure): Int = when (failure) {
    BiometricFailure.LOCKOUT -> R.string.biometric_error_lockout
    BiometricFailure.LOCKOUT_PERMANENT -> R.string.biometric_error_lockout_permanent
    BiometricFailure.CANCELLED -> R.string.biometric_error_cancelled
    BiometricFailure.NO_HARDWARE -> R.string.biometric_error_no_hardware
    BiometricFailure.NONE_ENROLLED -> R.string.biometric_error_none_enrolled
    BiometricFailure.OTHER -> R.string.biometric_error_generic
}
