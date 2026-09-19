const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');

const COLLECTION = 'counters';

// GET /counter/:key — get current count (no auth required, public read)
router.get('/:key', async (req, res) => {
  try {
    const doc = await db.collection(COLLECTION).doc(req.params.key).get();
    const count = doc.exists ? doc.data().count : 0;
    res.json({ key: req.params.key, count });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /counter/:key/increment — increment and return new count (auth required)
router.post('/:key/increment', requireAuth, async (req, res) => {
  try {
    const ref = db.collection(COLLECTION).doc(req.params.key);
    // Firestore transaction ensures atomicity (no double-counts)
    const newCount = await db.runTransaction(async (t) => {
      const doc = await t.get(ref);
      const current = doc.exists ? doc.data().count : 0;
      const next = current + 1;
      t.set(ref, { count: next, updatedAtMs: Date.now() }, { merge: true });
      return next;
    });
    res.json({ key: req.params.key, count: newCount });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

// POST /counter/:key/reset — reset to zero (auth required)
router.post('/:key/reset', requireAuth, async (req, res) => {
  try {
    await db.collection(COLLECTION).doc(req.params.key).set({ count: 0, updatedAtMs: Date.now() });
    res.json({ key: req.params.key, count: 0 });
  } catch (err) {
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
