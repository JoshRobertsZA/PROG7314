// declares const `express`, initialised with the result of calling `require(…)`
const express = require('express');
// declares const `router`, initialised with the result of calling `express.Router(…)`
const router = express.Router();
// declares const `{ db }`, initialised with the result of calling `require(…)`
const { db } = require('../firebase');
// declares const `{ requireAuth }`, initialised with the result of calling `require(…)`
const { requireAuth } = require('../middleware/auth');

// declares async function `verifyTripOwnership` taking 2 parameters (`tripId`, `uid`) and opens its body
async function verifyTripOwnership(tripId, uid) {
  // declares const `trip`, initialised to the awaited result of `db.collection('trips').doc(tripId).get()`
  const trip = await db.collection('trips').doc(tripId).get();
  // `if` statement: executes `throw Object.assign(new Error('Trip not foun…` when `!trip.exists` is true
  if (!trip.exists) throw Object.assign(new Error('Trip not found'), { status: 404 });
  // `if` statement: executes `throw Object.assign(new Error('Forbidden'), …` when `trip.data().accountId !== uid` is true
  if (trip.data().accountId !== uid) throw Object.assign(new Error('Forbidden'), { status: 403 });
  // returns `trip` from the current function
  return trip;
// closes the function `verifyTripOwnership`
}


// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/days/:tripId', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // awaits the call to `verifyTripOwnership` with arguments `(req.params.tripId, req.user.uid)`
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    // declares const `snap`, initialised to the awaited result of `db.collection('itinerary_days')`
    const snap = await db.collection('itinerary_days')
      // chained call `.where(…)` on the result of the previous line with arguments `('tripId', '==', req.params.tripId)`
      .where('tripId', '==', req.params.tripId)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('date', 'asc')`
      .orderBy('date', 'asc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();
    // calls `json` on `res` with arguments `(snap.docs.map(d => ({ id: d.id, ...d.data() …)`
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/days', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, tripId, date }`, initialised to `req.body`
    const { id, tripId, date } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id, tr…` when `!id || !tripId || !date` is true
    if (!id || !tripId || !date) return res.status(400).json({ error: 'id, tripId, date required' });
    // awaits the call to `verifyTripOwnership` with arguments `(tripId, req.user.uid)`
    await verifyTripOwnership(tripId, req.user.uid);
    // declares const `data`, initialised to a lambda / arrow function
    const data = { tripId, date };
    // awaits the call to `collection` on `db` with arguments `('itinerary_days')`, then chains `.doc(id)`, `.set(data)`
    await db.collection('itinerary_days').doc(id).set(data);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...data })`
    res.status(201).json({ id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/days/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_days').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Day no…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Day not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(doc.data().tripId, req.user.uid)`
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    // awaits the call to `delete` on `ref` with arguments `()`
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});


// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/flights/:dayId', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(req…`
    const day = await db.collection('itinerary_days').doc(req.params.dayId).get();
    // `if` statement: executes `return res.status(404).json({ error: 'Day no…` when `!day.exists` is true
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // declares const `snap`, initialised to the awaited result of `db.collection('itinerary_flights')`
    const snap = await db.collection('itinerary_flights')
      // chained call `.where(…)` on the result of the previous line with arguments `('dayId', '==', req.params.dayId)`
      .where('dayId', '==', req.params.dayId)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('createdAtMs', 'asc')`
      .orderBy('createdAtMs', 'asc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();
    // calls `json` on `res` with arguments `(snap.docs.map(d => ({ id: d.id, ...d.data() …)`
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/flights', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, dayId, flightNumber, pdfUri, depa…`, initialised to `req.body`
    const { id, dayId, flightNumber, pdfUri, departureTime, docName } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id and…` when `!id || !dayId` is true
    if (!id || !dayId) return res.status(400).json({ error: 'id and dayId required' });
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(day…`
    const day = await db.collection('itinerary_days').doc(dayId).get();
    // `if` statement: executes `return res.status(404).json({ error: 'Day no…` when `!day.exists` is true
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // declares const `data`, initialised to a lambda / arrow function
    const data = { dayId, flightNumber: flightNumber ?? null, pdfUri: pdfUri ?? null, departureTime: departureTime ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    // awaits the call to `collection` on `db` with arguments `('itinerary_flights')`, then chains `.doc(id)`, `.set(data)`
    await db.collection('itinerary_flights').doc(id).set(data);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...data })`
    res.status(201).json({ id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/flights/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_flights').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Flight…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Flight not found' });
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(doc…`
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // awaits the call to `delete` on `ref` with arguments `()`
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});


// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/lodging/:tripId', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // awaits the call to `verifyTripOwnership` with arguments `(req.params.tripId, req.user.uid)`
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    // declares const `snap`, initialised to the awaited result of `db.collection('itinerary_lodging')`
    const snap = await db.collection('itinerary_lodging')
      // chained call `.where(…)` on the result of the previous line with arguments `('tripId', '==', req.params.tripId)`
      .where('tripId', '==', req.params.tripId)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('createdAtMs', 'asc')`
      .orderBy('createdAtMs', 'asc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();
    // calls `json` on `res` with arguments `(snap.docs.map(d => ({ id: d.id, ...d.data() …)`
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/lodging', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, tripId, fromDate, toDate, pdfUri,…`, initialised to `req.body`
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id and…` when `!id || !tripId` is true
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    // awaits the call to `verifyTripOwnership` with arguments `(tripId, req.user.uid)`
    await verifyTripOwnership(tripId, req.user.uid);
    // declares const `data`, initialised to a lambda / arrow function
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    // awaits the call to `collection` on `db` with arguments `('itinerary_lodging')`, then chains `.doc(id)`, `.set(data)`
    await db.collection('itinerary_lodging').doc(id).set(data);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...data })`
    res.status(201).json({ id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/lodging/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_lodging').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Lodgin…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Lodging not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(doc.data().tripId, req.user.uid)`
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    // awaits the call to `delete` on `ref` with arguments `()`
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});


// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/cars/:tripId', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // awaits the call to `verifyTripOwnership` with arguments `(req.params.tripId, req.user.uid)`
    await verifyTripOwnership(req.params.tripId, req.user.uid);
    // declares const `snap`, initialised to the awaited result of `db.collection('itinerary_cars')`
    const snap = await db.collection('itinerary_cars')
      // chained call `.where(…)` on the result of the previous line with arguments `('tripId', '==', req.params.tripId)`
      .where('tripId', '==', req.params.tripId)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('createdAtMs', 'asc')`
      .orderBy('createdAtMs', 'asc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();
    // calls `json` on `res` with arguments `(snap.docs.map(d => ({ id: d.id, ...d.data() …)`
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/cars', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, tripId, fromDate, toDate, pdfUri,…`, initialised to `req.body`
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id and…` when `!id || !tripId` is true
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    // awaits the call to `verifyTripOwnership` with arguments `(tripId, req.user.uid)`
    await verifyTripOwnership(tripId, req.user.uid);
    // declares const `data`, initialised to a lambda / arrow function
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    // awaits the call to `collection` on `db` with arguments `('itinerary_cars')`, then chains `.doc(id)`, `.set(data)`
    await db.collection('itinerary_cars').doc(id).set(data);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...data })`
    res.status(201).json({ id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/cars/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_cars').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Car re…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Car rental not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(doc.data().tripId, req.user.uid)`
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    // awaits the call to `delete` on `ref` with arguments `()`
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});


// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/places/:dayId', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(req…`
    const day = await db.collection('itinerary_days').doc(req.params.dayId).get();
    // `if` statement: executes `return res.status(404).json({ error: 'Day no…` when `!day.exists` is true
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // declares const `snap`, initialised to the awaited result of `db.collection('itinerary_places')`
    const snap = await db.collection('itinerary_places')
      // chained call `.where(…)` on the result of the previous line with arguments `('dayId', '==', req.params.dayId)`
      .where('dayId', '==', req.params.dayId)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('createdAtMs', 'asc')`
      .orderBy('createdAtMs', 'asc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();
    // calls `json` on `res` with arguments `(snap.docs.map(d => ({ id: d.id, ...d.data() …)`
    res.json(snap.docs.map(d => ({ id: d.id, ...d.data() })));
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/places', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, dayId, name, category, lat, lng, …`, initialised to `req.body`
    const { id, dayId, name, category, lat, lng, note, photoUrl } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id, da…` when `!id || !dayId || !name` is true
    if (!id || !dayId || !name) return res.status(400).json({ error: 'id, dayId, name required' });
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(day…`
    const day = await db.collection('itinerary_days').doc(dayId).get();
    // `if` statement: executes `return res.status(404).json({ error: 'Day no…` when `!day.exists` is true
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // declares const `data`, initialised to a lambda / arrow function
    const data = { dayId, name, category: category ?? null, lat: lat ?? null, lng: lng ?? null, note: note ?? null, photoUrl: photoUrl ?? null, createdAtMs: Date.now() };
    // awaits the call to `collection` on `db` with arguments `('itinerary_places')`, then chains `.doc(id)`, `.set(data)`
    await db.collection('itinerary_places').doc(id).set(data);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...data })`
    res.status(201).json({ id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// calls `put` on `router`; its first argument is a callback whose body follows
router.put('/places/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_places').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Place …` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Place not found' });
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(doc…`
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // awaits the call to `update` on `ref` with arguments `(req.body)`
    await ref.update(req.body);
    // calls `json` on `res` with arguments `({ id: req.params.id, ...req.body })`
    res.json({ id: req.params.id, ...req.body });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `put`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/places/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection('itinerary_places').doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Place …` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Place not found' });
    // declares const `day`, initialised to the awaited result of `db.collection('itinerary_days').doc(doc…`
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    // awaits the call to `verifyTripOwnership` with arguments `(day.data().tripId, req.user.uid)`
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    // awaits the call to `delete` on `ref` with arguments `()`
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(err.status || 500)`, then chains `.json({ error: err.message })`
    res.status(err.status || 500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});

// assigns `module.exports` the value `router`
module.exports = router;
