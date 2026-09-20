// declares const `express`, initialised with the result of calling `require(…)`
const express = require('express');
// declares const `router`, initialised with the result of calling `express.Router(…)`
const router = express.Router();
// declares const `{ db }`, initialised with the result of calling `require(…)`
const { db } = require('../firebase');
// declares const `{ requireAuth }`, initialised with the result of calling `require(…)`
const { requireAuth } = require('../middleware/auth');

// declares const `COLLECTION`, initialised to the string literal 'trips'
const COLLECTION = 'trips';

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
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `{ id, name, startDate, endDate, destina…`, initialised to `req.body`
    const { id, name, startDate, endDate, destination, destLat, destLng, destPhotoUrl, createdAtMs, updatedAtMs } = req.body;
    // `if` statement: executes `return res.status(400).json({ error: 'id and…` when `!id || !name` is true
    if (!id || !name) return res.status(400).json({ error: 'id and name are required' });

    // declares const `tripData`,  and opens a multi-line initialiser
    const tripData = {
      // expression: `accountId: req.user.uid,`
      accountId: req.user.uid,
      // continues the statement started above: `name,`
      name,
      // continues the statement started above: `startDate: startDate ?? null,`
      startDate: startDate ?? null,
      // continues the statement started above: `endDate: endDate ?? null,`
      endDate: endDate ?? null,
      // continues the statement started above: `destination: destination ?? null,`
      destination: destination ?? null,
      // continues the statement started above: `destLat: destLat ?? null,`
      destLat: destLat ?? null,
      // continues the statement started above: `destLng: destLng ?? null,`
      destLng: destLng ?? null,
      // continues the statement started above: `destPhotoUrl: destPhotoUrl ?? null,`
      destPhotoUrl: destPhotoUrl ?? null,
      // continues the statement started above: `createdAtMs: createdAtMs ?? Date.now(),`
      createdAtMs: createdAtMs ?? Date.now(),
      // continues the statement started above: `updatedAtMs: updatedAtMs ?? Date.now(),`
      updatedAtMs: updatedAtMs ?? Date.now(),
    // closes the initialiser of `tripData`
    };

    // awaits the call to `collection` on `db` with arguments `(COLLECTION)`, then chains `.doc(id)`, `.set(tripData)`
    await db.collection(COLLECTION).doc(id).set(tripData);
    // calls `status` on `res` with arguments `(201)`, then chains `.json({ id, ...tripData })`
    res.status(201).json({ id, ...tripData });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('POST /trips error:', err)`
    console.error('POST /trips error:', err);
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
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
    // awaits the call to `delete` on `ref` with arguments `()`
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
