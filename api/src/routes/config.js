const express = require('express');
const router = express.Router();
const { requireAuth } = require('../middleware/auth');

// The third-party API keys the app needs (LocationIQ, AirLabs, OpenWeather,
// ExchangeRate, CounterAPI). These live as Cloud Run environment variables /
// Secret Manager on this server, never in the app's BuildConfig - the whole
// point of this route is that a client no longer needs its own copy of any
// of them. FIREBASE_WEB_CLIENT_ID is deliberately not in this list - see
// GET /config/web-client-id below.
const KEY_ENV_VARS = [
  'LOCATIONIQ_API_KEY',
  'EXCHANGERATE_API_KEY',
  'COUNTERAPI_API_KEY',
  'COUNTERAPI_WORKSPACE',
  'COUNTERAPI_PLACES_SLUG',
  'OPENWEATHER_API_KEY',
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

// The OAuth web client id is not actually a secret (Google's own Sign-In
// docs describe it as a public identifier meant to ship in app config), and
// the app needs it before a user has signed in at all to start the Google
// Sign-In flow in the first place - so it can't go behind requireAuth like
// the keys above.
router.get('/web-client-id', (req, res) => {
  res.json({ FIREBASE_WEB_CLIENT_ID: process.env.FIREBASE_WEB_CLIENT_ID || '' });
});

module.exports = router;
