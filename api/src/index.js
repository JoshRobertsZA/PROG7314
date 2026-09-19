require('dotenv').config();
const express = require('express');
const cors = require('cors');

const tripsRouter = require('./routes/trips');
const itineraryRouter = require('./routes/itinerary');
const counterRouter = require('./routes/counter');

const app = express();
const PORT = process.env.PORT || 8080;

// ── Middleware ──────────────────────────────────────────────────────────────
app.use(cors());
app.use(express.json());

// ── Routes ──────────────────────────────────────────────────────────────────
app.get('/health', (req, res) => res.json({ status: 'ok', service: 'waypoint-api' }));

app.use('/trips', tripsRouter);
app.use('/itinerary', itineraryRouter);
app.use('/counter', counterRouter);

// ── 404 catch-all ───────────────────────────────────────────────────────────
app.use((req, res) => res.status(404).json({ error: 'Not found' }));

// ── Start ────────────────────────────────────────────────────────────────────
app.listen(PORT, () => {
  console.log(`Waypoint API running on port ${PORT}`);
});
