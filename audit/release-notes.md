### v1.0.0: Final POE (changes since the Part 2 prototype)

#### New
- **Smart trip reminders (user-defined feature 4):** final version of the reminder engine. Checks run hourly and a few seconds after any change, can be switched off in Settings, and tapping a reminder opens its trip.
- **Live flight tracking (user-defined feature 5):** lookups by flight number and travel date, with a usage guardian that tracks the AirLabs quota, shows the remaining budget and falls back to the last saved result.
- **Offline mode with sync:** SQLite outbox, WorkManager sync with backoff, a 15-minute periodic sync, pull of other devices' changes, last-write-wins conflicts, offline and waiting-to-sync indicators, and Sync now.
- **Push notifications from real events:** FCM device registration, a push from the API when a trip is created, invalid-token pruning, two notification channels and tap-to-open.
- **Biometric unlock** with fingerprint, face or PIN, on top of sign-in.
- **Email and password sign-in** with forgot password, alongside Google and GitHub.
- **Settings sync:** settings stored in DataStore and saved to the API, with separate notification switches, home currency, dark mode and Sync now.
- **Account management:** edit profile, delete account with all cloud data.
- **Editing lodging, car rentals and places:** change booking dates and place notes.
- **isiXhosa and isiZulu** across the whole app, with a translation parity test.
- **API:** endpoints for itinerary updates, settings, devices, account deletion and the shared cache, with input validation, ownership checks and a Swagger UI.
- **Release pipeline** that builds a signed bundle and creates a GitHub Release.

#### Improved
- Currency converter between any two of 21 currencies.
- Dark theme, and every colour moved into the theme.
- The app wakes the API on start and allows 30 s for responses, for Cloud Run cold starts.
- Larger tap targets in the itinerary editor; month names follow the app language.
- About 400 lines of unused code and the unused `/counter` API routes removed.
- 150 Android and 58 API unit tests, run on every push with lint and a release build.

#### Fixed
- Saved changes were only uploaded on reconnect or login; they now sync straight away.
- The language choice was lost after a restart on Android 12 and below.
- Signing out did not clear the Google account choice in Credential Manager.
- Turning notifications on from the Settings tab never asked for the Android 13 permission.
- The app could crash on Android 7 (min SDK raised to 26).
- "1 days to go!" grammar, untranslated language labels and leftover placeholder text.
- Debug screens and unreachable routes removed.
