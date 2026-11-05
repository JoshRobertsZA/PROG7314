/**
 * Waypoint REST API entry point: JSON parsing, request logging, Swagger UI, the route modules and a
 * JSON error handler. Runs on Google Cloud Run (port from $PORT) and locally on 8080.
 */
require('dotenv').config();
const express = require('express');
const cors = require('cors');
const swaggerUi = require('swagger-ui-express');

const { openapiSpec } = require('./openapi');
const tripsRouter = require('./routes/trips');
const itineraryRouter = require('./routes/itinerary');
const configRouter = require('./routes/config');
const cacheRouter = require('./routes/cache');
const devicesRouter = require('./routes/devices');
const notifyRouter = require('./routes/notify');
const accountRouter = require('./routes/account');

const app = express();
const PORT = process.env.PORT || 8080;

app.use(cors());
app.use(express.json());

// Every request gets one log line with the method, path, status and actual
// time taken, written on 'finish' so the status code and duration are both
// known by the time it's logged (not available yet at request start).
app.use((req, res, next) => {
  const startedAtMs = Date.now();
  res.on('finish', () => {
    console.log(`${req.method} ${req.originalUrl} ${res.statusCode} ${Date.now() - startedAtMs}ms`);
  });
  next();
});

app.get('/health', (req, res) => res.json({ status: 'ok', service: 'waypoint-api' }));

app.get('/openapi.json', (req, res) => res.json(openapiSpec));
app.use('/docs', swaggerUi.serve, swaggerUi.setup(openapiSpec, {
  customSiteTitle: 'Waypoint API docs',
  swaggerOptions: { persistAuthorization: true, docExpansion: 'none' },
}));

app.use('/trips', tripsRouter);
app.use('/itinerary', itineraryRouter);
app.use('/config', configRouter);
app.use('/cache', cacheRouter);
app.use('/devices', devicesRouter);
app.use('/notify', notifyRouter);
app.use('/account', accountRouter);

app.use((req, res) => res.status(404).json({ error: 'Not found' }));

// Express only recognises a 4-argument function as error-handling
// middleware. This is the last resort for anything a route's own
// try/catch didn't reach (a thrown error in other middleware, a malformed
// JSON body rejected by express.json(), a bug that escapes a try block).
// Errors route handlers throw deliberately (verifyTripOwnership, the
// create()/ALREADY_EXISTS handling, validation.js) already carry a safe
// err.status and err.message; anything without a status is unexpected,
// logged in full server-side, and never has its raw message sent back.
app.use((err, req, res, next) => {
  console.error(`${req.method} ${req.originalUrl} error:`, err);
  const status = err.status || 500;
  const message = err.status ? err.message : 'Internal server error';
  res.status(status).json({ error: message });
});

// Only binds a real port when this file is run directly (node src/index.js),
// not when it's require()'d - tests import the app and drive it through
// supertest, which starts its own ephemeral listener per test.
if (require.main === module) {
  app.listen(PORT, () => {
    console.log(`Waypoint API running on port ${PORT}`);
  });
}

module.exports = app;
