const express = require('express');
const router = express.Router();
const { requireAuth } = require('../middleware/auth');

// The third-party API keys the app needs (LocationIQ, AirLabs, OpenWeather,
// ExchangeRate, CounterAPI, the Firebase web client id). These live as
// Cloud Run environment variables / Secret Manager on this server, never in
// the app's BuildConfig - the whole point of this route is that a client
// no longer needs its own copy of any of them, authenticated or not.
const KEY_ENV_VARS = [
  'LOCATIONIQ_API_KEY',
  'EXCHANGERATE_API_KEY',
  'COUNTERAPI_API_KEY',
  'COUNTERAPI_WORKSPACE',
  'COUNTERAPI_PLACES_SLUG',
  'OPENWEATHER_API_KEY',
  'FIREBASE_WEB_CLIENT_ID',
  'AIRLABS_API_KEY',
];

router.get('/keys', requireAuth, (req, res) => {
  const keys = {};
  for (const name of KEY_ENV_VARS) {
    // Always present with an empty-string fallback rather than omitted, so
    // the client's existing RemoteSecrets.get(keyName, local) ?: "" shape
    // doesn't need a null check added just for this.
    keys[name] = process.env[name] || '';
  }
  res.json(keys);
});

module.exports = router;
