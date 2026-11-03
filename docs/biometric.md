# Biometric unlock

Waypoint can lock itself behind the phone's fingerprint or face unlock. The switch is in Profile under Preferences, and it is off by default.

## What counts as unlocking

The prompt accepts a fingerprint or face scan (`BIOMETRIC_WEAK`) or the phone's PIN, pattern or password (`DEVICE_CREDENTIAL`). The two are always allowed together, so a person with no biometrics enrolled can still use the lock as long as the phone has a screen lock. Android does not allow a negative button on a prompt that accepts the device credential, so the prompt has no Cancel button. Pressing back or tapping outside the prompt ends it, and the lock screen stays up.

The switch is disabled, with an explanation, when the phone has no hardware or no screen lock at all (`BiometricAuthenticator.availability`).

## When the lock appears

The lock is armed in two places in `MainActivity`, both through `armBiometricLockIfNeeded()`:

- **Cold start.** When the splash screen finishes and the user is signed in.
- **Resume.** When the app goes to the background (`Lifecycle.Event.ON_STOP`) while the user is signed in. The overlay is already showing when the user comes back.

The lock is only armed when the setting is on and unlocking is available. Signed-out users never see it. The overlay opens the prompt by itself as soon as it appears, and the Unlock button opens it again after a failure.

## When unlocking fails

`classifyBiometricError` in `BiometricErrors.kt` groups the prompt's error codes, and each group has its own message in English, isiZulu and isiXhosa:

| Failure | Message tells the user |
|---|---|
| Lockout | Too many attempts, try again in 30 seconds |
| Permanent lockout | Biometrics are locked, use the phone's PIN, pattern or password |
| Cancelled | Unlock cancelled, tap Unlock to try again |
| No hardware | Biometrics are not available on this device |
| No screen lock | Set a screen lock in the phone settings |
| Anything else | Could not unlock, try again |

The message appears on the lock screen. When the switch in Profile fails to turn on, the same message appears as a toast.

## Tests

`BiometricErrorsTest` covers the error grouping, and `TranslationParityTest` checks that the new strings exist in all three languages.
