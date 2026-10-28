// declares const `express`, initialised with the result of calling `require(…)`
const express = require('express');
// declares const `router`, initialised with the result of calling `express.Router(…)`
const router = express.Router();
// declares const `{ db }`, initialised with the result of calling `require(…)`
const { db } = require('../firebase');
// declares const `{ requireAuth }`, initialised with the result of calling `require(…)`
const { requireAuth } = require('../middleware/auth');
const { isValidId, isValidName, isValidDateString, isValidDateOrder } = require('../validation');
const { sendToUser } = require('../push');

// declares const `COLLECTION`, initialised to the string literal 'trips'
const COLLECTION = 'trips';

// Firestore batch writes cap out at 500 operations, so a trip with enough
// itinerary children needs more than one batch to delete them all.
async function deleteDocsInBatches(docs) {
  for (let i = 0; i < docs.length; i += 500) {
    const batch = db.batch();
    for (const doc of docs.slice(i, i + 500)) batch.delete(doc.ref);
    await batch.commit();
  }
}

async function deleteByQuery(query) {
  const snap = await query.get();
  await deleteDocsInBatches(snap.docs);
}

// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `snapshot`, initialised to the awaited result of `db.collection(COLLECTION)`
    const snapshot = await db.collection(COLLECTION)
      // chained call `.where(…)` on the result of the previous line with arguments `('accountId', '==', req.user.uid)`
      .where('accountId', '==', req.user.uid)
      // chained call `.orderBy(…)` on the result of the previous line with arguments `('createdAtMs', 'desc')`
      .orderBy('createdAtMs', 'desc')
      // chained call `.get(…)` on the result of the previous line with arguments `()`
      .get();

    // declares const `trips`, initialised with the result of calling `snapshot.docs.map(…)`
    const trips = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    // calls `json` on `res` with arguments `(trips)`
    res.json(trips);
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('GET /trips error:', err)`
    console.error('GET /trips error:', err);
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `doc`, initialised to the awaited result of `db.collection(COLLECTION).doc(req.param…`
    const doc = await db.collection(COLLECTION).doc(req.params.id).get();
    // `if` statement: executes `return res.status(404).json({ error: 'Trip n…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    // declares const `data`, initialised with the result of calling `doc.data(…)`
    const data = doc.data();
    // `if` statement: executes `return res.status(403).json({ error: 'Forbid…` when `data.accountId !== req.user.uid` is true
    if (data.accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });
    // calls `json` on `res` with arguments `({ id: doc.id, ...data })`
    res.json({ id: doc.id, ...data });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('GET /trips/:id error:', err)`
    console.error('GET /trips/:id error:', err);
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/', requireAuth, async (req, res) => {
  try {
    const { id, name, startDate, endDate, destination, destLat, destLng, destPhotoUrl, createdAtMs, updatedAtMs } = req.body;
    if (!id || !name) return res.status(400).json({ error: 'id and name are required' });
    if (!isValidId(id)) return res.status(400).json({ error: 'id must be a UUID' });
    if (!isValidName(name)) return res.status(400).json({ error: 'name must be 1-200 characters' });
    if (startDate != null && !isValidDateString(startDate)) {
      return res.status(400).json({ error: 'startDate must be a valid YYYY-MM-DD date' });
    }
    if (endDate != null && !isValidDateString(endDate)) {
      return res.status(400).json({ error: 'endDate must be a valid YYYY-MM-DD date' });
    }
    if (!isValidDateOrder(startDate, endDate)) {
      return res.status(400).json({ error: 'startDate must not be after endDate' });
    }

    const tripData = {
      accountId: req.user.uid,
      name,
      startDate: startDate ?? null,
      endDate: endDate ?? null,
      destination: destination ?? null,
      destLat: destLat ?? null,
      destLng: destLng ?? null,
      destPhotoUrl: destPhotoUrl ?? null,
      createdAtMs: createdAtMs ?? Date.now(),
      updatedAtMs: updatedAtMs ?? Date.now(),
    };

    const docRef = db.collection(COLLECTION).doc(id);
    try {
      // create() is atomic and rejects if a document already exists at this id,
      // unlike set() which would silently overwrite it (including one owned by
      // another user). See:
      // https://googleapis.dev/nodejs/firestore/latest/DocumentReference.html#create
      await docRef.create(tripData);
    } catch (createErr) {
      // gRPC status 6 = ALREADY_EXISTS, per the Firestore client's documented
      // create() contract (@google-cloud/firestore, pinned to 7.11.6 in
      // package-lock.json). Anything else is a real failure, not an id clash.
      if (createErr.code !== 6) throw createErr;

      const existing = await docRef.get();
      if (existing.exists && existing.data().accountId === req.user.uid) {
        // The same user retrying a create whose response was lost in transit
        // would otherwise see a 409 for a trip they already own - treat it as
        // success so retries are safe.
        return res.status(200).json({ id: existing.id, ...existing.data() });
      }
      // Covers both another user's trip and a document that was deleted
      // between create() and this get() - either way the id is unusable
      // right now, and the client may retry with a different id.
      return res.status(409).json({ error: 'Trip id already in use' });
    }

    res.status(201).json({ id, ...tripData });

    // Fire-and-forget: a push failure must never fail trip creation itself,
    // and the client already has its response. The optional X-Device-Token
    // header is this device's own FCM token (if it has one registered), so
    // it doesn't get notified about a trip it just created itself - only
    // the user's other devices do.
    const deviceToken = req.headers['x-device-token'];
    sendToUser(req.user.uid, { type: 'trip_created', tripId: id }, { excludeToken: deviceToken })
      .catch(err => console.error('push on trip create failed:', err.message));
  } catch (err) {
    console.error('POST /trips error:', err);
    res.status(500).json({ error: err.message });
  }
});

// calls `put` on `router`; its first argument is a callback whose body follows
router.put('/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection(COLLECTION).doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Trip n…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    // `if` statement: executes `return res.status(403).json({ error: 'Forbid…` when `doc.data().accountId !== req.user.uid` is true
    if (doc.data().accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });

    // declares const `updates`, initialised to a lambda / arrow function
    const updates = { ...req.body, updatedAtMs: Date.now() };
    // statement: `delete updates.accountId;`
    delete updates.accountId;

    if (updates.name != null && !isValidName(updates.name)) {
      return res.status(400).json({ error: 'name must be 1-200 characters' });
    }
    if (updates.startDate != null && !isValidDateString(updates.startDate)) {
      return res.status(400).json({ error: 'startDate must be a valid YYYY-MM-DD date' });
    }
    if (updates.endDate != null && !isValidDateString(updates.endDate)) {
      return res.status(400).json({ error: 'endDate must be a valid YYYY-MM-DD date' });
    }
    // Order must hold for the dates the trip will have after this update,
    // not just the ones present in the request body - a PUT that only
    // changes endDate still has to respect the existing startDate.
    const finalStart = updates.startDate !== undefined ? updates.startDate : doc.data().startDate;
    const finalEnd = updates.endDate !== undefined ? updates.endDate : doc.data().endDate;
    if (!isValidDateOrder(finalStart, finalEnd)) {
      return res.status(400).json({ error: 'startDate must not be after endDate' });
    }

    // awaits the call to `update` on `ref` with arguments `(updates)`
    await ref.update(updates);
    // calls `json` on `res` with arguments `({ id: req.params.id, ...updates })`
    res.json({ id: req.params.id, ...updates });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('PUT /trips/:id error:', err)`
    console.error('PUT /trips/:id error:', err);
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `put`
});

// calls `delete` on `router`; its first argument is a callback whose body follows
router.delete('/:id', requireAuth, async (req, res) => {
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection(COLLECTION).doc(req.params.id);
    // declares const `doc`, initialised to the awaited result of `ref.get()`
    const doc = await ref.get();
    // `if` statement: executes `return res.status(404).json({ error: 'Trip n…` when `!doc.exists` is true
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    // `if` statement: executes `return res.status(403).json({ error: 'Forbid…` when `doc.data().accountId !== req.user.uid` is true
    if (doc.data().accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });

    // Itinerary data lives in root collections keyed by tripId (lodging,
    // cars) or by dayId one level further down (flights, places), not as
    // Firestore subcollections - so cascading means querying each one
    // explicitly rather than a recursive delete. Days are resolved first
    // since flights/places are found via their dayId, not tripId.
    const tripId = req.params.id;
    const daysSnap = await db.collection('itinerary_days').where('tripId', '==', tripId).get();
    for (const dayDoc of daysSnap.docs) {
      await deleteByQuery(db.collection('itinerary_flights').where('dayId', '==', dayDoc.id));
      await deleteByQuery(db.collection('itinerary_places').where('dayId', '==', dayDoc.id));
    }
    await deleteByQuery(db.collection('itinerary_lodging').where('tripId', '==', tripId));
    await deleteByQuery(db.collection('itinerary_cars').where('tripId', '==', tripId));
    await deleteDocsInBatches(daysSnap.docs);

    // The trip itself is deleted last, after every child is confirmed
    // gone, so a failure partway through cascading never leaves the trip
    // deleted with orphaned children still referencing it.
    await ref.delete();
    // calls `json` on `res` with arguments `({ deleted: req.params.id })`
    res.json({ deleted: req.params.id });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('DELETE /trips/:id error:', err)`
    console.error('DELETE /trips/:id error:', err);
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `delete`
});

// assigns `module.exports` the value `router`
module.exports = router;
