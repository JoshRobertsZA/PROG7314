// declares const `express`, initialised with the result of calling `require(…)`
const express = require('express');
// declares const `router`, initialised with the result of calling `express.Router(…)`
const router = express.Router();
// declares const `{ db }`, initialised with the result of calling `require(…)`
const { db } = require('../firebase');
// declares const `{ requireAuth }`, initialised with the result of calling `require(…)`
const { requireAuth } = require('../middleware/auth');

// declares const `COLLECTION`, initialised to the string literal 'counters'
const COLLECTION = 'counters';

// Counters are global (shared across every user), not per-user, so there is
// no ownership check to add here - the gap is that any caller can read or
// create an arbitrary counter doc. Allowlist the key shape instead of a
// fixed list, since PR 14 (api-usage-guardian) introduces new counter keys
// later and nothing in the app calls this route's keys yet to anchor a
// literal list on.
const KEY_PATTERN = /^[a-z][a-z0-9_-]{0,63}$/;

// calls `get` on `router`; its first argument is a callback whose body follows
router.get('/:key', async (req, res) => {
  if (!KEY_PATTERN.test(req.params.key)) {
    return res.status(400).json({ error: 'Invalid counter key' });
  }
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `doc`, initialised to the awaited result of `db.collection(COLLECTION).doc(req.param…`
    const doc = await db.collection(COLLECTION).doc(req.params.key).get();
    // declares const `count`, initialised to `doc.exists ? doc.data().count : 0`
    const count = doc.exists ? doc.data().count : 0;
    // calls `json` on `res` with arguments `({ key: req.params.key, count })`
    res.json({ key: req.params.key, count });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `get`
});

// calls `post` on `router`; its first argument is a callback whose body follows
router.post('/:key/increment', requireAuth, async (req, res) => {
  if (!KEY_PATTERN.test(req.params.key)) {
    return res.status(400).json({ error: 'Invalid counter key' });
  }
  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `ref`, initialised with the result of calling `db.collection(…)`
    const ref = db.collection(COLLECTION).doc(req.params.key);
    // declares const `newCount`, initialised to the awaited result of `db.runTransaction(async (t) =>` and opens a multi-line initialiser
    const newCount = await db.runTransaction(async (t) => {
      // declares const `doc`, initialised to the awaited result of `t.get(ref)`
      const doc = await t.get(ref);
      // declares const `current`, initialised to `doc.exists ? doc.data().count : 0`
      const current = doc.exists ? doc.data().count : 0;
      // declares const `next`, initialised to `current + 1`
      const next = current + 1;
      // calls `set` on `t` with arguments `(ref, { count: next, updatedAtMs: Date.now() …)`
      t.set(ref, { count: next, updatedAtMs: Date.now() }, { merge: true });
      // returns `next` from the current function
      return next;
    // closes the initialiser of `newCount`
    });
    // calls `json` on `res` with arguments `({ key: req.params.key, count: newCount })`
    res.json({ key: req.params.key, count: newCount });
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `status` on `res` with arguments `(500)`, then chains `.json({ error: err.message })`
    res.status(500).json({ error: err.message });
  // closes the catch block
  }
// closes the callback passed to `post`
});

// assigns `module.exports` the value `router`
module.exports = router;
