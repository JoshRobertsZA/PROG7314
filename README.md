# Waypoint

> A travel itinerary planner for Android — built with Kotlin, Jetpack Compose, and real-time API data.

[![Android CI](https://github.com/JoshRobertsZA/PROG7314/actions/workflows/android-ci.yml/badge.svg)](https://github.com/JoshRobertsZA/PROG7314/actions/workflows/android-ci.yml)

---

## About

Waypoint lets travellers plan and manage trips from a single app. Users can build day-by-day itineraries, track live flight status, check local weather and currency rates, discover nearby places, and receive push notifications before departure. All data syncs to the cloud via Firestore and is also stored locally in a SQLite database so the app works offline.

---

## Features

- **Google Sign-In**: Biometric lock for returning sessions
- **Trip management**: Create, edit, and delete trips with date ranges and destinations
- **Day-by-day itinerary builder**: Flight tracking (AirLabs) per leg
- **Explore nearby**: Points of interest powered by LocationIQ
- **Live weather**: Displayed for any destination via OpenWeatherMap
- **Currency converter**: Using real-time rates from ExchangeRate-API
- **Wikipedia summaries**: Used for destinations and places
- **Push notifications**: Used via Firebase Cloud Messaging with WorkManager scheduling
- **Cloud sync**: Intergrated with Firestore; offline-first with local SQLite cache
- **Multi-language support**: English (default), isiZulu, isiXhosa
- **GitHub Actions CI**: Includes automated lint, unit tests, and APK build on every push

---

## Architecture

| Layer | Technology |
|---|---|
| Language | Kotlin |
| UI | Jetpack Compose |
| Pattern | MVVM (ViewModel + StateFlow) |
| Local database | SQLite via `SQLiteOpenHelper` |
| Cloud database | Firebase Firestore |
| Authentication | Firebase Auth (Google SSO) + BiometricPrompt |
| Push notifications | Firebase Cloud Messaging + WorkManager |
| HTTP client | OkHttp |
| Async | Kotlin Coroutines |
| Build | Gradle (Kotlin DSL) |

API keys are fetched at runtime from a private GitHub repository via `core/secrets/RemoteSecrets.kt` and held in memory only; they are never stored on disk.

---

## APIs Used

| API | Purpose |
|---|---|
| [AirLabs](https://airlabs.co/) | Real-time flight status and schedule lookup by flight number |
| [OpenWeatherMap](https://openweathermap.org/api) | Current weather and forecasts for trip destinations |
| [ExchangeRate-API](https://www.exchangerate-api.com/) | Live currency exchange rates for the in-app converter |
| [LocationIQ](https://locationiq.com/) | Reverse geocoding and nearby points-of-interest search |
| [Wikipedia REST API](https://en.wikipedia.org/api/rest_v1/) | City and place summaries displayed on destination cards |
| [GitHub Contents API](https://docs.github.com/en/rest/repos/contents) | Secure remote fetch of API keys at runtime |
| [CounterAPI](https://counterapi.dev/) | Anonymous usage counter (app open events) |
| [Firebase Cloud Messaging](https://firebase.google.com/docs/cloud-messaging) | Push notifications for trip reminders |

---

## GitHub Actions CI/CD

Every push and pull request to `main` or `dev` triggers three parallel jobs:

1. **Lint** - runs `./gradlew lint` and uploads the HTML report as an artifact
2. **Unit Tests** - runs `./gradlew test` and uploads the XML test results
3. **Build** - runs `./gradlew assembleDebug` and uploads the debug APK

A Discord webhook posts a per-job status update and a final summary so the team is notified without checking GitHub. The workflow can also be triggered manually via `workflow_dispatch`.

CI configuration: [`.github/workflows/android-ci.yml`](.github/workflows/android-ci.yml)

---

## Screenshots

| Welcome | Home | Itinerary | Explore |
|---|---|---|---|
| ![Welcome](docs/screenshots/welcome.png) | ![Home](docs/screenshots/home.png) | ![Itinerary](docs/screenshots/itinerary.png) | ![Explore](docs/screenshots/explore.png) |

*(Screenshots captured on a physical Android device)*

---

## Demo Video

[Watch on YouTube](https://youtu.be/PLACEHOLDER) *(link will be updated before submission)*

The video walks through: signing in, creating a trip, adding itinerary days and flights, exploring nearby places, checking weather and currency, and receiving a push notification.

---

## AI Usage

GitHub Copilot and Claude (Anthropic) were used as coding assistants during development. AI tools helped with boilerplate generation (ViewModel scaffolding, Compose layout templates, unit test stubs), debugging OkHttp response parsing, and writing inline documentation comments across the codebase. All suggestions were reviewed, tested, and adapted before being committed. No AI-generated code was accepted without being understood and verified by the team. AI was not used for UI/UX design decisions, architecture choices, or any assessment submission content outside of code.

---

## Running the Project Locally

1. Clone the repo
   ```
   git clone <<URL>>
   ```
2. Add your `google-services.json` from the Firebase console into `app/`
3. Open the project in Android Studio Hedgehog or later
4. Run on a physical device (API 26+) or an emulator

> API keys are fetched at runtime from a private GitHub repository via the GitHub Contents API and held in memory only. No local key file is required.

---

## Team

| Name | Student Number |
|---|---|
| Dylan | ST10438409 |
| Josh | ST10265742 |
| Grace | ST10440118 |
| Marcus | ST10356369 |

IIE Emeris - Cape Town | PROG7314 / OPSC7312 | 2025
