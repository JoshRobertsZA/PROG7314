package com.waypoint.app

import androidx.biometric.BiometricPrompt
import com.waypoint.app.core.auth.BiometricFailure
import com.waypoint.app.core.auth.classifyBiometricError
import org.junit.Assert.assertEquals
import org.junit.Test

class BiometricErrorsTest {

    @Test
    fun lockoutCodesAreSeparated() {
        assertEquals(BiometricFailure.LOCKOUT, classifyBiometricError(BiometricPrompt.ERROR_LOCKOUT))
        assertEquals(BiometricFailure.LOCKOUT_PERMANENT, classifyBiometricError(BiometricPrompt.ERROR_LOCKOUT_PERMANENT))
    }

    @Test
    fun cancelCodesShareOneMessage() {
        assertEquals(BiometricFailure.CANCELLED, classifyBiometricError(BiometricPrompt.ERROR_USER_CANCELED))
        assertEquals(BiometricFailure.CANCELLED, classifyBiometricError(BiometricPrompt.ERROR_CANCELED))
        assertEquals(BiometricFailure.CANCELLED, classifyBiometricError(BiometricPrompt.ERROR_NEGATIVE_BUTTON))
    }

    @Test
    fun missingSetupAndHardwareAreClassified() {
        assertEquals(BiometricFailure.NONE_ENROLLED, classifyBiometricError(BiometricPrompt.ERROR_NO_BIOMETRICS))
        assertEquals(BiometricFailure.NO_HARDWARE, classifyBiometricError(BiometricPrompt.ERROR_HW_NOT_PRESENT))
    }

    @Test
    fun unknownCodesFallBackToGeneric() {
        assertEquals(BiometricFailure.OTHER, classifyBiometricError(-1))
    }
}
