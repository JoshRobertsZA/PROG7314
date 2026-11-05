package com.waypoint.app.core.auth

import com.waypoint.app.core.settings.AppSettings

/** Whether the app asks for a fingerprint/face/PIN when reopened. Stored per device in [AppSettings]. */
object BiometricLockPreferences {
    fun isEnabled(): Boolean = AppSettings.current.biometricLock

    fun setEnabled(enabled: Boolean) = AppSettings.setBiometricLock(enabled)
}
