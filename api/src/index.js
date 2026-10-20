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

// declares const `app`, initialised with the result of calling `express(…)`
const app = express();
// declares const `PORT`, initialised to `process.env.PORT || 8080`
const PORT = process.env.PORT || 8080;

// calls `use` on `app` with arguments `(cors())`
app.use(cors());
// calls `use` on `app` with arguments `(express.json())`
app.use(express.json());

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

// calls `use` on `app` with arguments `((req, res) => res.status(404).json({ error: …)`
app.use((req, res) => res.status(404).json({ error: 'Not found' }));

// calls `listen` on `app`; its first argument is a callback whose body follows
app.listen(PORT, () => {
  // calls `log` on `console` with arguments `(`Waypoint API running on port ${PORT}`)`
  console.log(`Waypoint API running on port ${PORT}`);
// closes the callback passed to `listen`
});
