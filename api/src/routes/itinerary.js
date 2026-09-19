const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');

// Helper: verify a trip belongs to the auth user
async function verifyTripOwnership(tripId, uid) {
  const trip = await db.collection('trips').doc(tripId).get();
  if (!trip.exists) throw Object.assign(new Error('Trip not found'), { status: 404 });
  if (trip.data().accountId !== uid) throw Object.assign(new Error('Forbidden'), { status: 403 });
  return trip;
}

// ─── DAYS ───────────────────────────────────────────────────────────────────

// GET /itinerary/days/:tripId
router.get('/days/:tripId', requireAuth, async (req, res) => {
  try {
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    const snap = await db.collection('itinerary_days')
      .where('tripId', '==', req.params.tripId)
      .orderBy('date', 'asc')
      .get();
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// POST /itinerary/days
router.post('/days', requireAuth, async (req, res) => {
  try {
    const { id, tripId, date } = req.body;
    if (!id || !tripId || !date) return res.status(400).json({ error: 'id, tripId, date required' });
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, date };
    await db.collection('itinerary_days').doc(id).set(data);
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// DELETE /itinerary/days/:id
router.delete('/days/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_days').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// ─── FLIGHTS ─────────────────────────────────────────────────────────────────

// GET /itinerary/flights/:dayId
router.get('/flights/:dayId', requireAuth, async (req, res) => {
  try {
    const day = await db.collection('itinerary_days').doc(req.params.dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const snap = await db.collection('itinerary_flights')
      .where('dayId', '==', req.params.dayId)
      .orderBy('createdAtMs', 'asc')
      .get();
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// POST /itinerary/flights
router.post('/flights', requireAuth, async (req, res) => {
  try {
    const { id, dayId, flightNumber, pdfUri, departureTime, docName } = req.body;
    if (!id || !dayId) return res.status(400).json({ error: 'id and dayId required' });
    const day = await db.collection('itinerary_days').doc(dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const data = { dayId, flightNumber: flightNumber ?? null, pdfUri: pdfUri ?? null, departureTime: departureTime ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    await db.collection('itinerary_flights').doc(id).set(data);
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// DELETE /itinerary/flights/:id
router.delete('/flights/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_flights').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Flight not found' });
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// ─── LODGING ──────────────────────────────────────────────────────────────────

// GET /itinerary/lodging/:tripId
router.get('/lodging/:tripId', requireAuth, async (req, res) => {
  try {
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    const snap = await db.collection('itinerary_lodging')
      .where('tripId', '==', req.params.tripId)
      .orderBy('createdAtMs', 'asc')
      .get();
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// POST /itinerary/lodging
router.post('/lodging', requireAuth, async (req, res) => {
  try {
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    await db.collection('itinerary_lodging').doc(id).set(data);
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// DELETE /itinerary/lodging/:id
router.delete('/lodging/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_lodging').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Lodging not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// ─── CAR RENTALS ─────────────────────────────────────────────────────────────

// GET /itinerary/cars/:tripId
router.get('/cars/:tripId', requireAuth, async (req, res) => {
  try {
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    const snap = await db.collection('itinerary_cars')
      .where('tripId', '==', req.params.tripId)
      .orderBy('createdAtMs', 'asc')
      .get();
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// POST /itinerary/cars
router.post('/cars', requireAuth, async (req, res) => {
  try {
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    await db.collection('itinerary_cars').doc(id).set(data);
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// DELETE /itinerary/cars/:id
router.delete('/cars/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_cars').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Car rental not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// ─── PLACES ──────────────────────────────────────────────────────────────────

// GET /itinerary/places/:dayId
router.get('/places/:dayId', requireAuth, async (req, res) => {
  try {
    const day = await db.collection('itinerary_days').doc(req.params.dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const snap = await db.collection('itinerary_places')
      .where('dayId', '==', req.params.dayId)
      .orderBy('createdAtMs', 'asc')
      .get();
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// POST /itinerary/places
router.post('/places', requireAuth, async (req, res) => {
  try {
    const { id, dayId, name, category, lat, lng, note, photoUrl } = req.body;
    if (!id || !dayId || !name) return res.status(400).json({ error: 'id, dayId, name required' });
    const day = await db.collection('itinerary_days').doc(dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const data = { dayId, name, category: category ?? null, lat: lat ?? null, lng: lng ?? null, note: note ?? null, photoUrl: photoUrl ?? null, createdAtMs: Date.now() };
    await db.collection('itinerary_places').doc(id).set(data);
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// PUT /itinerary/places/:id
router.put('/places/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_places').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Place not found' });
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    await ref.update(req.body);
    res.json({ id: req.params.id, ...req.body });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// DELETE /itinerary/places/:id
router.delete('/places/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_places').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Place not found' });
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

module.exports = router;
