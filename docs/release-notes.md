# Waypoint 1.0.0 (Final POE)

Waypoint is our travel planner for Android. You can plan a trip day by day, keep track of your flights, get reminders before you leave, and keep using the app without signal. Everything syncs to our own REST API on Google Cloud Run, which stores the data in Firestore.

This is our final POE release for PROG7314 and includes everything we've added since the Part 2 prototype.

**Version:** 1.0.0 (version code 1)  
**Requires:** Android 8.0 (API 26) or newer  
**Languages:** English, isiZulu and isiXhosa  
**API:** https://prog7314-git-287180570190.africa-south1.run.app ([Swagger docs](https://prog7314-git-287180570190.africa-south1.run.app/docs))  
**Demo video:** https://youtu.be/2Ox3HgyETtA  
**More detail:** see the [README](https://github.com/JoshRobertsZA/PROG7314#readme)

---

## What's new

### Our new user-defined features

**Feature 4: Smart trip reminders**
The app keeps an eye on your trips and lets you know when something matters: a countdown before you leave, a nudge if a trip has no plan yet, a summary of each day's plans, a heads-up the night before an early flight, and a warning if bad weather is on the way. Tapping a reminder takes you straight to that trip.

**Feature 5: Live flight tracking**
Enter a flight number and the app looks it up for your travel date, showing the status, route, departure time, terminal and gate. The free flight API has a small monthly limit, so the app keeps count of how many lookups we've used and shows the last result it saved once we get close.

### POE requirements
- Fingerprint or face unlock (with the phone PIN as a backup), which you can turn on in Settings after signing in
- Offline mode: changes are saved on the phone first and sync automatically when you're back online, including changes made on your other devices
- Push notifications from our API when a trip is added on another phone, plus the trip reminders, each with its own switch in Settings
- The whole app in English, isiZulu and isiXhosa
- Our own app icon, splash screen and a ready-to-go Play Store listing

### Also added
- Sign in with email and password (with forgot password), as well as Google and GitHub
- Settings now follow you to a new phone: theme, language, home currency and notification choices
- Edit your profile or delete your account and all its data
- Lodging, car rentals and places can now be edited, not just added and removed
- You can type a trip's destination while offline, and the app fills in its location and photo once you're back online
- New API endpoints for settings, devices and account deletion, with validation and checks that you can only reach your own data

## Improvements
- The currency converter works between any two of 21 currencies
- Dark mode
- The app wakes the API up when it opens, so the first request after a quiet spell isn't as slow
- Release builds are signed by GitHub Actions using encrypted secrets, so our upload key never sits in the repo
- Better logging and code documentation
- 150 Android tests and 58 API tests, run automatically on every push

## Fixes
- Changes now sync straight away instead of only after reconnecting or logging in
- Your language choice is remembered after restarting on older Android versions
- Signing out fully signs you out of Google too
- Turning on notifications in Settings now asks for permission on Android 13+
- The Swagger docs page loads again (one endpoint description was breaking it)
- Tuesday and Thursday show correctly on the trip calendar
- Removed leftover test screens and unused code

---

## Downloads

- `waypoint-1.0.0.apk`: install this one on an Android phone
- `waypoint-1.0.0.aab`: the bundle we'd upload to Google Play
- `waypoint-1.0.0-mapping.txt`: the R8 mapping file, for reading crash reports from the release build

---

## How we got here

All work went through pull requests:

- Project set-up and first screens: #2 to #13
- Moving to Jetpack Compose and navigation: #14, #15, #21, #22
- Home, Explore and trip planning: #17 to #19, #25, #26, #28 to #33, #36 to #38, #41
- Sign-in and security: #34, #39, #44, #55, #62, #64, #69
- REST API: #16, #48, #50, #52
- Offline sync: #27, #53, #54, #57, #83
- Notifications: #42, #56
- Languages: #24, #35, #58
- Features 4 and 5, currency and API limits: #60, #61, #63, #75
- UI work: #43, #45, #46, #49, #59, #65
- Testing and CI: #40, #66, #67, #70
- Play Store and releases: #68, #72, #78, #81
- Final clean-up and docs: #47, #51, #71, #73, #74, #76, #77, #79, #80, #82, #84

**Full changelog:** https://github.com/JoshRobertsZA/PROG7314/commits/v1.0.0-final-poe
