# Play Console: Data safety answers

Use these answers in Play Console > App content > Data safety.

## Overview questions

| Question | Answer |
|---|---|
| Does the app collect or share user data? | Yes |
| Is all data encrypted in transit? | Yes (HTTPS) |
| Can users request data deletion? | Yes, in app (Profile > Delete account) |
| Account creation methods | Email and password, Google sign-in |
| Privacy policy URL | https://st10438409-emeris.github.io/waypoint-privacy/ |

## Data types collected

| Category | Data type | Collected | Shared | Purpose | Optional |
|---|---|---|---|---|---|
| Personal info | Email address | Yes | No | Account management | No |
| Personal info | Name | Yes | No | Account management, app functionality | No |
| Location | Approximate location | Yes | Yes (LocationIQ, OpenWeatherMap) | App functionality | Yes |
| Location | Precise location | Yes | Yes (LocationIQ) | App functionality | Yes |
| App activity | Other user-generated content (trips, itinerary, flights, lodging) | Yes | No | App functionality | No |
| Photos and videos | Profile photo link | Yes | No | Account management | Yes |
| Device or other IDs | FCM device token | Yes | No | App functionality (notifications) | No |

Notes for the form:
- Location is processed in the moment to fetch nearby places. Third parties receive coordinates to return results. Mark as shared for app functionality (service providers acting on our behalf may be declared as processing, check the current Play wording).
- Firebase Authentication and Cloud Messaging are Google services acting as our processors.
- Flight numbers, city names and currency codes sent to AirLabs, Wikipedia and ExchangeRate-API are not personal data.

## Data types NOT collected

Financial info, health and fitness, messages, audio, files, contacts, calendar, web browsing history, advertising ID, crash logs, diagnostics, analytics.

## Permissions declared

| Permission | Why |
|---|---|
| INTERNET | Sync trips and call APIs |
| ACCESS_NETWORK_STATE | Detect offline mode |
| ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION | Nearby places in Explore (runtime prompt, optional) |
| POST_NOTIFICATIONS | Trip reminders (runtime prompt on Android 13+) |

## Open items for the team

- Privacy policy is hosted at https://st10438409-emeris.github.io/waypoint-privacy/ (source repo: https://github.com/ST10438409-Emeris/waypoint-privacy).
- Confirm the AirLabs monthly limit before store submission.
