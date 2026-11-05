# Commit plan: `fix/part3-app-code`

Branch: `fix/part3-app-code` (from `dev`). Run each block in order from the repo root in Git Bash.
Every changed file appears in exactly one commit. Files that were deleted are staged with `git add` too.
Do not commit the `audit/` folder.

Paths below use these short forms:
- `APP` = `app/src/main/java/com/waypoint/app`
- `RES` = `app/src/main/res`

```bash
APP=app/src/main/java/com/waypoint/app
RES=app/src/main/res
```

---

## 1. build: add DataStore and raise minSdk to 26

**Description:** Adds `androidx.datastore:datastore-preferences` for the new settings store. Raises `minSdk` from 24 to 26 because the app uses `java.time` for trip dates, which crashes on Android 7 (lint reported about 200 NewApi errors). The README already states API 26+.

**Files:**
- `gradle/libs.versions.toml`
- `app/build.gradle.kts`

```bash
git add gradle/libs.versions.toml app/build.gradle.kts
git commit -m "build: add DataStore and raise minSdk to 26" -m "Adds androidx.datastore:datastore-preferences for the settings store. Raises minSdk from 24 to 26 because java.time is used for trip dates and crashes on Android 7 (about 200 lint NewApi errors). Matches the API 26+ stated in the README."
```

## 2. fix(i18n): register locale config and keep the chosen language on Android 12 and below

**Description:** Points the manifest at `locales_config.xml`, so Android 13+ lists English, isiZulu and isiXhosa in the system's per-app language settings. Declares AppCompat's `AppLocalesMetadataHolderService` with `autoStoreLocales`, so the language picked in Settings survives a restart on Android 12 and below.

**Files:**
- `app/src/main/AndroidManifest.xml`

```bash
git add app/src/main/AndroidManifest.xml
git commit -m "fix(i18n): register locale config and keep chosen language after restart" -m "Points the manifest at locales_config.xml so Android 13+ shows the app's languages in system settings. Declares AppLocalesMetadataHolderService with autoStoreLocales so the language chosen in Settings is kept after a restart on Android 12 and below."
```

## 3. feat(i18n): add strings for settings, sync and session messages

**Description:** Adds English, isiZulu and isiXhosa strings for the two notification switches, home currency, sync section, session-expired message, waiting-to-sync icon and the reminders notification channel. Translates the language picker's sub-labels, which were hard-coded English. Turns "N days to go!" into a plural so 1 day reads correctly, uses a real ellipsis, and removes two strings that are no longer used.

**Files:**
- `app/src/main/res/values/strings.xml`
- `app/src/main/res/values-zu/strings.xml`
- `app/src/main/res/values-xh/strings.xml`

```bash
git add $RES/values/strings.xml $RES/values-zu/strings.xml $RES/values-xh/strings.xml
git commit -m "feat(i18n): add strings for settings, sync and session messages" -m "Adds English, isiZulu and isiXhosa text for the notification switches, home currency, sync section, session-expired message, waiting-to-sync icon and reminders channel. Translates the language picker sub-labels. Makes the trip countdown a plural so '1 day to go' reads correctly, uses a real ellipsis, and removes two unused strings."
```

## 4. feat(settings): store preferences in DataStore and sync them through the API

**Description:** Adds `AppSettings`, a single settings store backed by Jetpack DataStore, which migrates the old SharedPreferences values once. Adds `SettingsSync`, which uploads changes to `PUT /account/settings` and applies newer copies from `GET /account/settings` (newest `updatedAtMs` wins), so theme, language, notification and currency choices follow the user to another phone. Biometric unlock stays per device. `ThemePreferences` and `BiometricLockPreferences` become thin wrappers over `AppSettings`.

**Files:**
- `app/src/main/java/com/waypoint/app/core/settings/AppSettings.kt` (new)
- `app/src/main/java/com/waypoint/app/core/settings/SettingsSync.kt` (new)
- `app/src/main/java/com/waypoint/app/core/theme/ThemePreferences.kt`
- `app/src/main/java/com/waypoint/app/core/auth/BiometricLockPreferences.kt`

```bash
git add $APP/core/settings/AppSettings.kt $APP/core/settings/SettingsSync.kt $APP/core/theme/ThemePreferences.kt $APP/core/auth/BiometricLockPreferences.kt
git commit -m "feat(settings): store preferences in DataStore and sync them through the API" -m "Adds AppSettings, one settings store on Jetpack DataStore that migrates the old SharedPreferences values once. Adds SettingsSync, which uploads changes to PUT /account/settings and applies newer copies from GET /account/settings (newest updatedAtMs wins) so preferences follow the user to another phone. Biometric unlock stays per device. ThemePreferences and BiometricLockPreferences now wrap AppSettings."
```

## 5. feat(network): wake the API on start, 30 s timeout and end the session on 401

**Description:** Raises the OkHttp read timeout from 10 s to 30 s because Cloud Run cold starts can be slower than that. Calls `/health` when the app starts so the server is awake by the time the user needs it. A 401 from our own API on a token-bearing request now raises `SessionEvents.unauthorized`, which MainActivity uses to sign the user out (wired in commit 12). Adds the `getSettings`/`putSettings` calls used by `SettingsSync` and removes `sendTestNotification`, which the app never called.

**Files:**
- `app/src/main/java/com/waypoint/app/core/network/HttpClient.kt`
- `app/src/main/java/com/waypoint/app/core/auth/SessionEvents.kt` (new)
- `app/src/main/java/com/waypoint/app/core/network/WaypointApiRepository.kt`
- `app/src/main/java/com/waypoint/app/WaypointApp.kt`

```bash
git add $APP/core/network/HttpClient.kt $APP/core/auth/SessionEvents.kt $APP/core/network/WaypointApiRepository.kt $APP/WaypointApp.kt
git commit -m "feat(network): wake the API on start, use a 30s timeout and end the session on 401" -m "Read timeout goes from 10s to 30s for Cloud Run cold starts. The app calls /health on start to wake the server. A 401 from the Waypoint API on an authenticated request raises SessionEvents.unauthorized so the app can sign the user out. Adds getSettings/putSettings and removes the unused sendTestNotification client call. WaypointApp initialises AppSettings, SettingsSync and the periodic sync."
```

## 6. fix(auth): clear Credential Manager state on sign-out

**Description:** `AuthRepository.signOut(context)` now also calls `CredentialManager.clearCredentialState`, so the next Google sign-in shows the account picker instead of reusing the old account. It also stops the signed-out user's settings being uploaded to the next account that signs in. Account deletion goes through the same sign-out.

**Files:**
- `app/src/main/java/com/waypoint/app/core/auth/AuthRepository.kt`
- `app/src/main/java/com/waypoint/app/core/auth/AuthViewModel.kt`
- `app/src/main/java/com/waypoint/app/features/profile/ui/ProfileViewModel.kt`

```bash
git add $APP/core/auth/AuthRepository.kt $APP/core/auth/AuthViewModel.kt $APP/features/profile/ui/ProfileViewModel.kt
git commit -m "fix(auth): clear Credential Manager state on sign-out" -m "signOut now clears Credential Manager as well as Firebase, so the next Google sign-in shows the account picker. It also detaches local settings from the signed-out account so they are never uploaded to the next user. Account deletion uses the same sign-out."
```

## 7. fix(sync): upload on every save, pull after pushing and add a periodic job

**Description:** Saving a trip or itinerary item queued it in the outbox but never scheduled an upload, so changes only reached the API on reconnect, login or opening All Trips. `OutboxRepository.enqueue` now schedules the sync itself. `ExistingWorkPolicy.KEEP` became `APPEND_OR_REPLACE`, so a change saved while a sync is running is not dropped. After uploading, `SyncWorker` pulls trips and their itineraries from the API and records the last sync time. A 15-minute periodic job is the backup.

**Files:**
- `app/src/main/java/com/waypoint/app/core/sync/OutboxRepository.kt`
- `app/src/main/java/com/waypoint/app/core/sync/SyncScheduler.kt`
- `app/src/main/java/com/waypoint/app/core/sync/SyncWorker.kt`

```bash
git add $APP/core/sync/OutboxRepository.kt $APP/core/sync/SyncScheduler.kt $APP/core/sync/SyncWorker.kt
git commit -m "fix(sync): upload on every save, pull after pushing and add a periodic job" -m "Local saves queued an outbox entry but never scheduled an upload; enqueue now schedules the sync. KEEP became APPEND_OR_REPLACE so a change saved mid-sync is not dropped. SyncWorker now pulls trips and itineraries after uploading and records the last sync time. Adds a 15-minute periodic sync as a backup."
```

## 8. feat(notifications): separate trip updates from trip reminders

**Description:** Splits notifications into two types, each with its own Android channel and its own Settings switch: Trip updates (pushed by the API) and Trip reminders (scheduled on the phone). Tapping a reminder now opens the trip it is about.

**Files:**
- `app/src/main/java/com/waypoint/app/core/notifications/NotificationChannels.kt`
- `app/src/main/java/com/waypoint/app/core/notifications/NotificationPreferences.kt`
- `app/src/main/java/com/waypoint/app/core/notifications/PushNotifier.kt`
- `app/src/main/java/com/waypoint/app/core/notifications/TripReminderWorker.kt`
- `app/src/main/java/com/waypoint/app/core/notifications/WaypointMessagingService.kt`
- `app/src/main/java/com/waypoint/app/core/notifications/WelcomeNotifier.kt`

```bash
git add $APP/core/notifications/NotificationChannels.kt $APP/core/notifications/NotificationPreferences.kt $APP/core/notifications/PushNotifier.kt $APP/core/notifications/TripReminderWorker.kt $APP/core/notifications/WaypointMessagingService.kt $APP/core/notifications/WelcomeNotifier.kt
git commit -m "feat(notifications): separate trip updates from trip reminders" -m "Two notification types, each with its own Android channel and Settings switch: trip updates pushed by the API, and reminders scheduled on the phone. Tapping a reminder opens its trip. The trip countdown uses the new plural string."
```

## 9. feat(settings): add notification switches, home currency and sync now

**Description:** The Settings screen now has separate Trip updates and Trip reminders switches, a Home currency picker that immediately retargets the converter on Home, and a Sync section showing the last sync time, an offline message, or a spinner, plus a Sync now button. It also stops showing the placeholder "Alex Carter" name when a profile is blank. Changing the language marks settings for upload. `MainNavShell` now passes the notification-permission request to the Settings tab, which never received it before. The language option row is reused for the currency picker.

**Files:**
- `app/src/main/java/com/waypoint/app/features/settings/ui/SettingsScreen.kt`
- `app/src/main/java/com/waypoint/app/features/settings/ui/SettingsUiState.kt`
- `app/src/main/java/com/waypoint/app/features/settings/ui/SettingsViewModel.kt`
- `app/src/main/java/com/waypoint/app/features/settings/ui/LanguageModal.kt`
- `app/src/main/java/com/waypoint/app/features/settings/ui/CurrencyModal.kt` (new)
- `app/src/main/java/com/waypoint/app/features/currencyexchange/ui/CurrencyExchangeModal.kt`
- `app/src/main/java/com/waypoint/app/features/home/ui/HomeViewModel.kt`
- `app/src/main/java/com/waypoint/app/core/navigation/MainNavShell.kt`

```bash
git add $APP/features/settings/ui/SettingsScreen.kt $APP/features/settings/ui/SettingsUiState.kt $APP/features/settings/ui/SettingsViewModel.kt $APP/features/settings/ui/LanguageModal.kt $APP/features/settings/ui/CurrencyModal.kt $APP/features/currencyexchange/ui/CurrencyExchangeModal.kt $APP/features/home/ui/HomeViewModel.kt $APP/core/navigation/MainNavShell.kt
git commit -m "feat(settings): add notification switches, home currency and sync now" -m "Settings gets separate trip-update and reminder switches, a home currency picker that retargets the Home converter straight away, and a sync section with last sync time, offline state and a Sync now button. Removes the placeholder name and email shown for blank profiles. The Settings tab can now ask for the notification permission. The language option row is shared with the currency picker."
```

## 10. feat(trips): show a waiting-to-sync icon on unsynced trips

**Description:** Trips with a change still in the outbox (for example made offline) show a small cloud-upload icon in All Trips. It disappears once the sync uploads the change.

**Files:**
- `app/src/main/java/com/waypoint/app/features/alltrips/ui/AllTripsScreen.kt`
- `app/src/main/java/com/waypoint/app/features/alltrips/ui/AllTripsUiState.kt`
- `app/src/main/java/com/waypoint/app/features/alltrips/ui/AllTripsViewModel.kt`
- `app/src/main/res/drawable/ic_sync_pending.xml` (new)

```bash
git add $APP/features/alltrips/ui/AllTripsScreen.kt $APP/features/alltrips/ui/AllTripsUiState.kt $APP/features/alltrips/ui/AllTripsViewModel.kt $RES/drawable/ic_sync_pending.xml
git commit -m "feat(trips): show a waiting-to-sync icon on unsynced trips" -m "Trips with a change still waiting in the outbox show a small cloud-upload icon in All Trips. The list refreshes as the sync uploads changes, so the icon disappears once the server has it."
```

## 11. style(ui): move remaining hard-coded colours and text into the theme

**Description:** Moves the Welcome screen's scrim, GitHub button and error colours into named theme colours, swaps `Color.White` for the theme's `White` in the itinerary screens, and replaces the hard-coded "Clear All" with its string resource.

**Files:**
- `app/src/main/java/com/waypoint/app/core/theme/Color.kt`
- `app/src/main/java/com/waypoint/app/features/welcome/WelcomeScreen.kt`
- `app/src/main/java/com/waypoint/app/features/edititinerary/ui/EditItineraryScreen.kt`
- `app/src/main/java/com/waypoint/app/features/viewitinerary/ui/ViewItineraryScreen.kt`
- `app/src/main/java/com/waypoint/app/features/notifications/ui/NotificationsScreen.kt`

```bash
git add $APP/core/theme/Color.kt $APP/features/welcome/WelcomeScreen.kt $APP/features/edititinerary/ui/EditItineraryScreen.kt $APP/features/viewitinerary/ui/ViewItineraryScreen.kt $APP/features/notifications/ui/NotificationsScreen.kt
git commit -m "style(ui): move remaining hard-coded colours and text into the theme" -m "Welcome screen scrim, GitHub button and error colours are now named theme colours. Itinerary screens use the theme White. 'Clear All' on the notifications screen now comes from strings.xml so it is translated."
```

## 12. refactor(nav): remove debug screens and handle session expiry

**Description:** Removes the debug `MainScreen` (hard-coded "Go to …" buttons and `__scratch__` routes that nothing navigated to), the unused `PlaceDetailScreen`, and the standalone AllTrips, Explore, Settings and PlaceDetail routes, which are tabs inside `MainNavShell` or were only reachable from the debug screen. MainActivity now listens for `SessionEvents.unauthorized`: it signs out, shows "Your session has ended" and returns to Welcome. It also passes the notification-permission request into the tabs.

**Files:**
- `app/src/main/java/com/waypoint/app/MainActivity.kt`
- `app/src/main/java/com/waypoint/app/core/navigation/Routes.kt`
- `app/src/main/java/com/waypoint/app/features/main/ui/MainScreen.kt` (deleted)
- `app/src/main/java/com/waypoint/app/features/placedetail/ui/PlaceDetailScreen.kt` (deleted)

```bash
git add $APP/MainActivity.kt $APP/core/navigation/Routes.kt $APP/features/main/ui/MainScreen.kt $APP/features/placedetail/ui/PlaceDetailScreen.kt
git commit -m "refactor(nav): remove debug screens and handle session expiry" -m "Removes the debug MainScreen and its __scratch__ routes, the unused PlaceDetailScreen, and the AllTrips, Explore, Settings and PlaceDetail routes that are tabs in MainNavShell or were only reachable from the debug screen. On a 401 from the API, MainActivity signs out, shows 'Your session has ended' and returns to Welcome. Passes the notification permission request into the tabs."
```

## 13. test: replace the template test with settings sync and session rules

**Description:** Deletes the Android Studio `2 + 2 = 4` template test. Adds `SettingsSyncRulesTest`, which covers the last-write-wins rule for settings, defaults that match the API, and which responses end the session (only a 401 from our API on an authenticated call).

**Files:**
- `app/src/test/java/com/waypoint/app/ExampleUnitTest.kt` (deleted)
- `app/src/test/java/com/waypoint/app/SettingsSyncRulesTest.kt` (new)

```bash
git add app/src/test/java/com/waypoint/app/ExampleUnitTest.kt app/src/test/java/com/waypoint/app/SettingsSyncRulesTest.kt
git commit -m "test: replace the template test with settings sync and session rules" -m "Deletes the 2 + 2 = 4 template test. Adds SettingsSyncRulesTest: newest copy wins between phone and API, defaults match the API, and only a 401 from the Waypoint API on an authenticated call ends the session."
```

## 14. feat(api): add settings endpoints and respect the trip-updates setting

**Description:** Adds `GET /account/settings` and `PUT /account/settings`, stored one document per user in the `settings` collection and keyed by the uid from the verified Firebase token. `PUT` validates every field (400 with a clear message) and keeps whichever copy has the newer `updatedAtMs`. Trip-created pushes are skipped when the user turned Trip updates off. `DELETE /account` also deletes the settings, and its 500 response no longer exposes the raw error message. Both routes are documented in Swagger.

**Files:**
- `api/src/validation.js`
- `api/src/routes/account.js`
- `api/src/push.js`
- `api/src/routes/trips.js`
- `api/src/openapi.js`

```bash
git add api/src/validation.js api/src/routes/account.js api/src/push.js api/src/routes/trips.js api/src/openapi.js
git commit -m "feat(api): add settings endpoints and respect the trip-updates setting" -m "Adds GET and PUT /account/settings, one Firestore document per user keyed by the verified token's uid. PUT validates each field (400 with a clear message) and keeps the copy with the newer updatedAtMs. Trip-created pushes are skipped when the user turned trip updates off. DELETE /account also removes settings and no longer returns raw error messages. Documented in Swagger."
```

## 15. test(api): cover settings endpoints and the push opt-out

**Description:** 14 new Jest tests covering defaults when nothing is saved, per-user isolation, saving under the token's uid, last-write-wins, 400s for five kinds of bad input, 401 without a token, settings removed with the account, and pushes skipped or sent depending on the setting.

**Files:**
- `api/test/settings.test.js` (new)

```bash
git add api/test/settings.test.js
git commit -m "test(api): cover settings endpoints and the push opt-out" -m "Covers defaults when nothing is saved, per-user isolation, saving under the token's uid, last-write-wins, 400s for invalid input, 401 without a token, settings deleted with the account, and pushes skipped or sent depending on the trip-updates setting."
```

---

## After the last commit

```bash
git status --short
```

Only `audit/` should be left. Then push and open a PR into `dev`:

```bash
git push -u origin fix/part3-app-code
```

Redeploy the API so `/account/settings` exists on Cloud Run:

```bash
gcloud run deploy prog7314-git --source api --region africa-south1
```
