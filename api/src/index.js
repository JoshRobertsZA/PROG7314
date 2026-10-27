// calls `require` with arguments `('dotenv')`, then chains `.config()`
require('dotenv').config();
// declares const `express`, initialised with the result of calling `require(…)`
const express = require('express');
// declares const `cors`, initialised with the result of calling `require(…)`
const cors = require('cors');
// declares const `swaggerUi`, initialised with the result of calling `require(…)`
const swaggerUi = require('swagger-ui-express');

// declares const `{ openapiSpec }`, initialised with the result of calling `require(…)`
const { openapiSpec } = require('./openapi');
// declares const `tripsRouter`, initialised with the result of calling `require(…)`
const tripsRouter = require('./routes/trips');
// declares const `itineraryRouter`, initialised with the result of calling `require(…)`
const itineraryRouter = require('./routes/itinerary');
// declares const `counterRouter`, initialised with the result of calling `require(…)`
const counterRouter = require('./routes/counter');
const configRouter = require('./routes/config');

// declares const `app`, initialised with the result of calling `express(…)`
const app = express();
// declares const `PORT`, initialised to `process.env.PORT || 8080`
const PORT = process.env.PORT || 8080;

// calls `use` on `app` with arguments `(cors())`
app.use(cors());
// calls `use` on `app` with arguments `(express.json())`
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

// calls `get` on `app` with arguments `('/health', (req, res) => res.json({ status: …)`
app.get('/health', (req, res) => res.json({ status: 'ok', service: 'waypoint-api' }));

// calls `get` on `app` with arguments `('/openapi.json', (req, res) => res.json(open…)`
app.get('/openapi.json', (req, res) => res.json(openapiSpec));
// calls `use` on `app`; its first argument is a callback whose body follows
app.use('/docs', swaggerUi.serve, swaggerUi.setup(openapiSpec, {
  // expression: `customSiteTitle: 'Waypoint API docs',`
  customSiteTitle: 'Waypoint API docs',
  // continues the statement started above: `swaggerOptions: { persistAuthorization: true, docExpansion:…`
  swaggerOptions: { persistAuthorization: true, docExpansion: 'none' },
// statement: `}));`
}));

// calls `use` on `app` with arguments `('/trips', tripsRouter)`
app.use('/trips', tripsRouter);
// calls `use` on `app` with arguments `('/itinerary', itineraryRouter)`
app.use('/itinerary', itineraryRouter);
// calls `use` on `app` with arguments `('/counter', counterRouter)`
app.use('/counter', counterRouter);
app.use('/config', configRouter);

// calls `use` on `app` with arguments `((req, res) => res.status(404).json({ error: …)`
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

// calls `listen` on `app`; its first argument is a callback whose body follows
app.listen(PORT, () => {
  // calls `log` on `console` with arguments `(`Waypoint API running on port ${PORT}`)`
  console.log(`Waypoint API running on port ${PORT}`);
// closes the callback passed to `listen`
});
