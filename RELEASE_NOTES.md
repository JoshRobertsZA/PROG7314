# Release Notes

Everything below landed after the Part 2 prototype (`main` at `ab2a3a7`), on the way to
the final POE submission. Grouped by area; branch names in parentheses point back to the
commit plan if you want the exact commits.

## Innovative features

A few things here go beyond what Part 2 asked for:

- **Offline sync with a real outbox.** Writes hit SQLite first and never wait on the
  network. A background worker drains a queue of pending changes with retry and
  backoff, and collapses repeated edits to the same record into one API call instead of
  replaying every intermediate state.
- **An API usage guardian.** AirLabs and LocationIQ are both free-tier APIs with daily
  limits. The app now tracks its own usage against those limits and switches to cached
  data before it runs out, instead of just failing once the quota's gone.
- **A per-user places cache with a live fallback.** Nearby-places results are cached
  per user in SQLite, read before the shared cache, and fall back to a live LocationIQ
  call if the shared cache can't be reached — so one stale cache entry can't block
  everyone.
- **No API keys shipped in the app.** Every third-party key is fetched at runtime from
  an authenticated endpoint and cached locally. This closed a real exposure a security
  pass found (`fix/token-exposure`).
- **A test that enforces translation parity.** If a string key is missing from the
  isiZulu or isiXhosa translations, the build fails instead of the gap shipping
  unnoticed.
- **A fully automated, signed release pipeline.** Every push to `main` builds a signed,
  minified app bundle and publishes it as a GitHub Release, no manual signing step
  required. CI signs with a throwaway key, so the pipeline itself needs no secrets.

## Cloud sync and offline support

- Added a REST API for trip and itinerary CRUD, backed by Firestore — with validation,
  ownership checks, and cascade delete of itinerary children when a trip is removed
  (`feat/api-sync-endpoints`)
- Built offline-first sync: every write lands in SQLite first, then an outbox queues the
  change for the API in the background. A `SyncWorker` drains it with a network
  constraint and backoff, and now retries on `401`/`408`/`429` instead of dropping the
  entry (`feat/offline-sync`, `fix/device-test-bugs`)
- On login, the app pulls cloud trips and itinerary and merges them into SQLite with
  last-write-wins, so data comes back automatically on a new or wiped device without
  overwriting unsynced local edits

## Security hardening

- Removed the embedded GitHub token and third-party API keys from the app; they're
  fetched at runtime from authenticated REST endpoints and cached locally
  (`fix/token-exposure`)
- Stopped logging FCM device tokens, and excluded the API key cache and device state
  from Android's automatic backup (`fix/device-test-bugs`)

## Push notifications

- Added FCM device registration and server-side push via `firebase-admin`, including a
  push sent automatically when a trip is created through sync (`feat/fcm-server-push`)
- Tapping a notification now opens the related trip; the device token registers on
  login/refresh and unregisters on logout

## Authentication and profile

- Added email/password sign-up, sign-in and password reset alongside Google Sign-In,
  with input validation and clear error messages (`feat/email-signup`)
- Added an edit-profile screen and account deletion, which re-authenticates the user and
  cascades through their cloud data and sync outbox before deleting the Firebase account
  (`feat/profile-edit-delete`)
- Polished biometric lock: falls back to device credentials when biometrics aren't
  enrolled, and shows clear, localised messages on lockout or cancel
  (`fix/biometric-polish`)

## Trip and itinerary features

- Currency converter now supports any base/target pair, not a fixed one, with the rates
  cache keyed per base currency (`feat/currency-any-to-any`)
- Flight lookup now takes the itinerary day's date, so the correct day's status is shown
  and cached (`feat/flight-lookup-by-date`)
- Added a per-user local places cache in SQLite, checked before the shared cache, with a
  live LocationIQ fallback (`feat/places-cache-per-user`)

## API usage guardian

- Added daily/monthly usage tracking for AirLabs and LocationIQ, gating calls once the
  budget's used up and falling back to cached data (`feat/api-usage-guardian`)
- Added an API budget meter to the profile screen

## UI, accessibility and localisation

- Cleaned up leftover placeholder content (Hello World text, a hardcoded Table Mountain
  example) and added empty/loading/error states that were missing (`fix/ui-cleanup`)
- Added content descriptions and minimum touch target sizes
- Closed i18n gaps: biometric, calendar and flight-modal strings are now translated into
  isiZulu and isiXhosa, with a test that fails the build on a missing key
  (`fix/i18n-gaps`)
- Added a dark theme that follows the system setting, with a contrast pass across every
  screen (`feat/dark-theme`)

## Testing

- Added a Jest + Supertest suite for the API: 52 tests across 7 files, using an
  in-memory Firestore mock so no real credentials are needed to run them
  (`test/api-tests`)
- Expanded Android unit tests to 194 tests across 22 files, covering trip/itinerary
  validation, cache serialisation, biometric error mapping, sync failure rules and more
  (`test/unit-tests-expansion`)

## Release and deployment

- Prepared the Play Store release: R8 minification with keep rules, upload keystore
  signing, `versionName 1.0.0`, store listing text, privacy policy, data safety answers
  and screenshots (`chore/play-store-prep`)
- Extended CI to run the API test suite on every push, plus an unsigned release assemble
  job that catches R8/minification problems before a real signed build
  (`chore/ci-extend`)
- Added a release pipeline that builds a signed app bundle and publishes a tagged GitHub
  Release on every push to `main` (`chore/release-workflow`)
- Fixed a release-only crash (WorkManager's database constructor stripped by R8),
  caught while testing the signed build on a physical device (`fix/device-test-bugs`)
