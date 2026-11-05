/**
 * /itinerary routes: CRUD for days, flights, lodging, car rentals and places. Every route checks that
 * the parent trip belongs to the caller before reading or writing, and validates input (400) before
 * anything is stored.
 */
const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');
const { isValidId, isValidName, isValidDateString, isValidDateOrder, isValidLat, isValidLng } = require('../validation');

async function verifyTripOwnership(tripId, uid) {
  const trip = await db.collection('trips').doc(tripId).get();
  if (!trip.exists) throw Object.assign(new Error('Trip not found'), { status: 404 });
  if (trip.data().accountId !== uid) throw Object.assign(new Error('Forbidden'), { status: 403 });
  return trip;
}

// Shared by every itinerary POST route: create() is atomic (unlike the
// read-then-set this replaces), so a client-supplied id collision can't
// silently overwrite another user's document. On a collision, ownerCheck
// decides whether this is the same owner retrying a dropped response
// (safe to treat as success) or someone else's id (409). Ownership for
// itinerary docs runs through the parent trip, not a direct accountId
// field, hence the callback instead of a flat equality check.
async function createOrConflict(ref, data, ownerCheck) {
  try {
    await ref.create(data);
    return { created: true };
  } catch (createErr) {
    // gRPC status 6 = ALREADY_EXISTS; anything else is a real failure.
    if (createErr.code !== 6) throw createErr;
    const existing = await ref.get();
    if (existing.exists && await ownerCheck(existing.data())) {
      return { created: false, existing: existing.data() };
    }
    throw Object.assign(new Error('Id already in use'), { status: 409 });
  }
}

// Resolves ownership for itinerary docs that store `tripId` directly
// (days, lodging, cars).
function ownerOfTrip(uid) {
  return async (data) => {
    const trip = await db.collection('trips').doc(data.tripId).get();
    return trip.exists && trip.data().accountId === uid;
  };
}

// Resolves ownership for itinerary docs that only store `dayId` (flights,
// places) by following day -> trip.
function ownerOfDay(uid) {
  return async (data) => {
    const day = await db.collection('itinerary_days').doc(data.dayId).get();
    if (!day.exists) return false;
    const trip = await db.collection('trips').doc(day.data().tripId).get();
    return trip.exists && trip.data().accountId === uid;
  };
}

// Builds a PUT update object from only the named fields, so a client can
// never use a PUT body to rewrite tripId/dayId (which would move a record
// onto a trip/day it doesn't own) or any other field we don't expect.
function pickFields(body, fields) {
  const updates = {};
  for (const field of fields) {
    if (Object.prototype.hasOwnProperty.call(body, field)) updates[field] = body[field];
  }
  return updates;
}


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

router.post('/days', requireAuth, async (req, res) => {
  try {
    const { id, tripId, date } = req.body;
    if (!id || !tripId || !date) return res.status(400).json({ error: 'id, tripId, date required' });
    if (!isValidId(id) || !isValidId(tripId)) return res.status(400).json({ error: 'id and tripId must be UUIDs' });
    if (!isValidDateString(date)) return res.status(400).json({ error: 'date must be a valid YYYY-MM-DD date' });
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, date };
    const ref = db.collection('itinerary_days').doc(id);
    const result = await createOrConflict(ref, data, ownerOfTrip(req.user.uid));
    if (!result.created) return res.status(200).json({ id, ...result.existing });
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// Only `date` is mutable on a day; `tripId` is the parent link and must
// never move via PUT.
const DAY_FIELDS = ['date'];

router.put('/days/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_days').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    const updates = pickFields(req.body, DAY_FIELDS);
    if (updates.date != null && !isValidDateString(updates.date)) {
      return res.status(400).json({ error: 'date must be a valid YYYY-MM-DD date' });
    }
    await ref.update(updates);
    res.json({ id: req.params.id, ...doc.data(), ...updates });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

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

router.post('/flights', requireAuth, async (req, res) => {
  try {
    const { id, dayId, flightNumber, pdfUri, departureTime, docName } = req.body;
    if (!id || !dayId) return res.status(400).json({ error: 'id and dayId required' });
    if (!isValidId(id) || !isValidId(dayId)) return res.status(400).json({ error: 'id and dayId must be UUIDs' });
    const day = await db.collection('itinerary_days').doc(dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const data = { dayId, flightNumber: flightNumber ?? null, pdfUri: pdfUri ?? null, departureTime: departureTime ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    const ref = db.collection('itinerary_flights').doc(id);
    const result = await createOrConflict(ref, data, ownerOfDay(req.user.uid));
    if (!result.created) return res.status(200).json({ id, ...result.existing });
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// `dayId` is the parent link and `createdAtMs` is an immutable creation
// stamp; neither is mutable via PUT.
const FLIGHT_FIELDS = ['flightNumber', 'pdfUri', 'departureTime', 'docName'];

router.put('/flights/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_flights').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Flight not found' });
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const updates = pickFields(req.body, FLIGHT_FIELDS);
    await ref.update(updates);
    res.json({ id: req.params.id, ...doc.data(), ...updates });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

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

router.post('/lodging', requireAuth, async (req, res) => {
  try {
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    if (!isValidId(id) || !isValidId(tripId)) return res.status(400).json({ error: 'id and tripId must be UUIDs' });
    if (fromDate != null && !isValidDateString(fromDate)) {
      return res.status(400).json({ error: 'fromDate must be a valid YYYY-MM-DD date' });
    }
    if (toDate != null && !isValidDateString(toDate)) {
      return res.status(400).json({ error: 'toDate must be a valid YYYY-MM-DD date' });
    }
    if (!isValidDateOrder(fromDate, toDate)) {
      return res.status(400).json({ error: 'fromDate must not be after toDate' });
    }
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    const ref = db.collection('itinerary_lodging').doc(id);
    const result = await createOrConflict(ref, data, ownerOfTrip(req.user.uid));
    if (!result.created) return res.status(200).json({ id, ...result.existing });
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// `tripId` is the parent link and `createdAtMs` is immutable.
const LODGING_FIELDS = ['fromDate', 'toDate', 'pdfUri', 'docName'];

router.put('/lodging/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_lodging').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Lodging not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    const updates = pickFields(req.body, LODGING_FIELDS);
    if (updates.fromDate != null && !isValidDateString(updates.fromDate)) {
      return res.status(400).json({ error: 'fromDate must be a valid YYYY-MM-DD date' });
    }
    if (updates.toDate != null && !isValidDateString(updates.toDate)) {
      return res.status(400).json({ error: 'toDate must be a valid YYYY-MM-DD date' });
    }
    const finalFrom = updates.fromDate !== undefined ? updates.fromDate : doc.data().fromDate;
    const finalTo = updates.toDate !== undefined ? updates.toDate : doc.data().toDate;
    if (!isValidDateOrder(finalFrom, finalTo)) {
      return res.status(400).json({ error: 'fromDate must not be after toDate' });
    }
    await ref.update(updates);
    res.json({ id: req.params.id, ...doc.data(), ...updates });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

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

router.post('/cars', requireAuth, async (req, res) => {
  try {
    const { id, tripId, fromDate, toDate, pdfUri, docName } = req.body;
    if (!id || !tripId) return res.status(400).json({ error: 'id and tripId required' });
    if (!isValidId(id) || !isValidId(tripId)) return res.status(400).json({ error: 'id and tripId must be UUIDs' });
    if (fromDate != null && !isValidDateString(fromDate)) {
      return res.status(400).json({ error: 'fromDate must be a valid YYYY-MM-DD date' });
    }
    if (toDate != null && !isValidDateString(toDate)) {
      return res.status(400).json({ error: 'toDate must be a valid YYYY-MM-DD date' });
    }
    if (!isValidDateOrder(fromDate, toDate)) {
      return res.status(400).json({ error: 'fromDate must not be after toDate' });
    }
    await verifyTripOwnership(tripId, req.user.uid);
    const data = { tripId, fromDate: fromDate ?? null, toDate: toDate ?? null, pdfUri: pdfUri ?? null, docName: docName ?? null, createdAtMs: Date.now() };
    const ref = db.collection('itinerary_cars').doc(id);
    const result = await createOrConflict(ref, data, ownerOfTrip(req.user.uid));
    if (!result.created) return res.status(200).json({ id, ...result.existing });
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// `tripId` is the parent link and `createdAtMs` is immutable.
const CAR_FIELDS = ['fromDate', 'toDate', 'pdfUri', 'docName'];

router.put('/cars/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_cars').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Car rental not found' });
    await verifyTripOwnership(doc.data().tripId, req.user.uid);
    const updates = pickFields(req.body, CAR_FIELDS);
    if (updates.fromDate != null && !isValidDateString(updates.fromDate)) {
      return res.status(400).json({ error: 'fromDate must be a valid YYYY-MM-DD date' });
    }
    if (updates.toDate != null && !isValidDateString(updates.toDate)) {
      return res.status(400).json({ error: 'toDate must be a valid YYYY-MM-DD date' });
    }
    const finalFrom = updates.fromDate !== undefined ? updates.fromDate : doc.data().fromDate;
    const finalTo = updates.toDate !== undefined ? updates.toDate : doc.data().toDate;
    if (!isValidDateOrder(finalFrom, finalTo)) {
      return res.status(400).json({ error: 'fromDate must not be after toDate' });
    }
    await ref.update(updates);
    res.json({ id: req.params.id, ...doc.data(), ...updates });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

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

router.post('/places', requireAuth, async (req, res) => {
  try {
    const { id, dayId, name, category, lat, lng, note, photoUrl } = req.body;
    if (!id || !dayId || !name) return res.status(400).json({ error: 'id, dayId, name required' });
    if (!isValidId(id) || !isValidId(dayId)) return res.status(400).json({ error: 'id and dayId must be UUIDs' });
    if (!isValidName(name)) return res.status(400).json({ error: 'name must be 1-200 characters' });
    if (lat != null && !isValidLat(lat)) return res.status(400).json({ error: 'lat must be between -90 and 90' });
    if (lng != null && !isValidLng(lng)) return res.status(400).json({ error: 'lng must be between -180 and 180' });
    const day = await db.collection('itinerary_days').doc(dayId).get();
    if (!day.exists) return res.status(404).json({ error: 'Day not found' });
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const data = { dayId, name, category: category ?? null, lat: lat ?? null, lng: lng ?? null, note: note ?? null, photoUrl: photoUrl ?? null, createdAtMs: Date.now() };
    const ref = db.collection('itinerary_places').doc(id);
    const result = await createOrConflict(ref, data, ownerOfDay(req.user.uid));
    if (!result.created) return res.status(200).json({ id, ...result.existing });
    res.status(201).json({ id, ...data });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

// `dayId` is the parent link and `createdAtMs` is immutable; everything
// else about a place can be edited (the audit flagged this route doing
// `ref.update(req.body)` directly, which let a client rewrite `dayId` to
// move a place onto a day in a trip it doesn't own).
const PLACE_FIELDS = ['name', 'category', 'lat', 'lng', 'note', 'photoUrl'];

router.put('/places/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection('itinerary_places').doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Place not found' });
    const day = await db.collection('itinerary_days').doc(doc.data().dayId).get();
    await verifyTripOwnership(day.data().tripId, req.user.uid);
    const updates = pickFields(req.body, PLACE_FIELDS);
    if (updates.name != null && !isValidName(updates.name)) {
      return res.status(400).json({ error: 'name must be 1-200 characters' });
    }
    if (updates.lat != null && !isValidLat(updates.lat)) {
      return res.status(400).json({ error: 'lat must be between -90 and 90' });
    }
    if (updates.lng != null && !isValidLng(updates.lng)) {
      return res.status(400).json({ error: 'lng must be between -180 and 180' });
    }
    await ref.update(updates);
    res.json({ id: req.params.id, ...doc.data(), ...updates });
  } catch (err) {
    res.status(err.status || 500).json({ error: err.message });
  }
});

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
