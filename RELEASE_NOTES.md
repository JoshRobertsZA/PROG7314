# Release Notes

Everything below landed after the Part 2 prototype (`main` at `ab2a3a7`) on the way to
the final POE submission. Grouped by area; branch names are in parentheses for
traceability back to the commit plan.

## Cloud sync and offline support

- Added a REST API for trip and itinerary CRUD, backed by Firestore, with per-field
  validation, ownership checks, and cascade delete of itinerary children when a trip is
  removed (`feat/api-sync-endpoints`)
- Added offline-first sync: every write lands in SQLite first, then an outbox queues the
  change for the API in the background. A `SyncWorker` drains the outbox with a network
  constraint and exponential backoff, retrying on `401`/`408`/`429` instead of dropping
  the entry (`feat/offline-sync`, `fix/device-test-bugs`)
- On login, the app pulls cloud trips and itinerary and merges them into SQLite with
  last-write-wins, so data is restored automatically on a new or wiped device without
  overwriting unsynced local edits

## Security hardening

- Removed the embedded GitHub token and third-party API keys from the app; they're now
  fetched at runtime from authenticated REST endpoints and cached locally
  (`fix/token-exposure`)
- Stopped logging FCM device tokens, and excluded the API key cache and device state
  from Android's automatic backup (`fix/device-test-bugs`)

## Push notifications

- Added FCM device registration, server-side push via `firebase-admin`, and a push sent
  automatically when a trip is created through sync (`feat/fcm-server-push`)
- Tapping a push notification opens the related trip; the token is registered on login
  and refresh, and unregistered on logout

## Authentication and profile

- Added email/password sign-up, sign-in, and password reset alongside Google Sign-In,
  with input validation and clear error messages (`feat/email-signup`)
- Added an edit-profile screen (display name) and account deletion, which
  re-authenticates the user, cascades through the owner's cloud data and sync outbox
  before deleting the Firebase user (`feat/profile-edit-delete`)
- Added biometric lock improvements: device-credential fallback when biometrics aren't
  enrolled, and clear, localised messages for lockout and cancellation
  (`fix/biometric-polish`)

## Trip and itinerary features

- Currency converter now supports any base/target currency pair, not just a fixed one,
  with the rates cache keyed per base currency (`feat/currency-any-to-any`)
- Flight lookup now accepts a travel date, using the itinerary day's date so the correct
  day's flight status is shown and cached (`feat/flight-lookup-by-date`)
- Added a per-user local places cache in SQLite, read before the shared cache, with a
  live LocationIQ fallback if the shared cache is unreachable
  (`feat/places-cache-per-user`)

## API usage guardian

- Added daily/monthly usage tracking for AirLabs and LocationIQ, gating calls once a
  budget is used up and falling back to cached data instead of failing
  (`feat/api-usage-guardian`)
- Added an API budget meter in the profile screen

## UI, accessibility and localisation

- Removed leftover placeholder content (Hello World text, a hardcoded Table Mountain
  example) and added missing empty, loading and error states (`fix/ui-cleanup`)
- Added content descriptions and minimum touch target sizes for accessibility
- Closed i18n gaps: biometric, calendar, and flight-modal strings are now translated
  into isiZulu and isiXhosa, with an automated test failing the build if a key is
  missing from either locale (`fix/i18n-gaps`)
- Added a dark theme that follows the system setting, with a contrast pass across every
  screen (`feat/dark-theme`)

## Testing

- Added a Jest + Supertest suite for the API: 52 tests across 7 files, using an
  in-memory Firestore mock so no real credentials are needed to run them
  (`test/api-tests`)
- Expanded Android unit tests to 194 tests across 22 files, covering trip/itinerary
  validation, cache serialisation, biometric error mapping, sync failure rules, and
  more (`test/unit-tests-expansion`)

## Release and deployment

- Prepared the Play Store release: R8 minification with keep rules, upload keystore
  signing, `versionName 1.0.0`, store listing text, privacy policy, data safety
  answers, and screenshots (`chore/play-store-prep`)
- Extended CI to run the API test suite on every push and added an unsigned release
  assemble job to catch R8/minification problems before a real signed build
  (`chore/ci-extend`)
- Added a release pipeline that builds a signed app bundle and publishes a tagged
  GitHub Release automatically on every push to `main` (`chore/release-workflow`)
- Fixed a release-only crash (WorkManager's database constructor stripped by R8) found
  while testing the signed build on a physical device (`fix/device-test-bugs`)
