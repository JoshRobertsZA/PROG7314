const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');

const COLLECTION = 'devices';

function isValidToken(token) {
  return typeof token === 'string' && token.trim().length > 0 && token.length <= 4096;
}

function isValidLocale(locale) {
  return typeof locale === 'string' && locale.trim().length > 0 && locale.length <= 20;
}

// The FCM token itself is the document id, so registering the same token
// twice (a refresh on the same device, or a re-login) is a plain overwrite
// rather than a duplicate row - that's the "deduplicate by token" the plan
// asks for. If a token moves to a different account (device reset, shared
// device), this correctly reassigns it rather than leaving a stale owner.
router.post('/', requireAuth, async (req, res) => {
  try {
    const { token, locale } = req.body;
    if (!isValidToken(token)) return res.status(400).json({ error: 'token is required' });
    if (locale != null && !isValidLocale(locale)) {
      return res.status(400).json({ error: 'locale must be 1-20 characters' });
    }

    const deviceData = {
      accountId: req.user.uid,
      token,
      locale: locale ?? null,
      updatedAtMs: Date.now(),
    };

    await db.collection(COLLECTION).doc(token).set(deviceData);
    res.status(200).json(deviceData);
  } catch (err) {
    console.error('POST /devices error:', err);
    res.status(500).json({ error: err.message });
  }
});

router.delete('/:token', requireAuth, async (req, res) => {
  try {
    const ref = db.collection(COLLECTION).doc(req.params.token);
    const doc = await ref.get();
    // Unregistering an already-gone or already-reassigned token is a no-op,
    // not an error - logout should never fail because of device bookkeeping.
    if (!doc.exists) return res.json({ deleted: req.params.token });
    if (doc.data().accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });

    await ref.delete();
    res.json({ deleted: req.params.token });
  } catch (err) {
    console.error('DELETE /devices/:token error:', err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
