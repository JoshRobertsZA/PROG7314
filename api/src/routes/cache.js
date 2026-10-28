const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');

const COLLECTION = 'cache';

// The three cache kinds the app actually writes (weather/currency/places),
// each keyed by a simple identifier (city slug, currency code). This also
// closes off path traversal / arbitrary-document access through the :path
// param, same reasoning as the counter key allowlist in PR 1.
const PATH_PATTERN = /^(weather|currency|places)_[a-zA-Z0-9_]{1,64}$/;

router.get('/:path', requireAuth, async (req, res) => {
  if (!PATH_PATTERN.test(req.params.path)) {
    return res.status(400).json({ error: 'Invalid cache path' });
  }
  try {
    const doc = await db.collection(COLLECTION).doc(req.params.path).get();
    if (!doc.exists) return res.status(404).json({ error: 'Not cached yet' });
    res.json(doc.data());
  } catch (err) {
    console.error(`GET /cache/${req.params.path} error:`, err);
    res.status(500).json({ error: err.message });
  }
});

router.put('/:path', requireAuth, async (req, res) => {
  if (!PATH_PATTERN.test(req.params.path)) {
    return res.status(400).json({ error: 'Invalid cache path' });
  }
  try {
    // Shared cache, same as it was on GitHub - whatever the caller sends
    // becomes the new value verbatim, no field whitelist. Any authenticated
    // user can refresh any city's/currency's cache entry; that was already
    // true of the GitHub-backed version and isn't something this PR
    // changes, only moves behind auth instead of a hardcoded token.
    await db.collection(COLLECTION).doc(req.params.path).set(req.body);
    res.json({ path: req.params.path, ...req.body });
  } catch (err) {
    console.error(`PUT /cache/${req.params.path} error:`, err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
