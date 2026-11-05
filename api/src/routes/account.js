const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');
const { deleteDocsInBatches, deleteByQuery } = require('./trips');
const { validateSettings, SETTINGS_DEFAULTS } = require('../validation');

const SETTINGS_COLLECTION = 'settings';

// Deletes every Firestore document this account owns: each trip and its
// itinerary children (same cascade as DELETE /trips/:id, run once per
// trip), then device tokens and saved settings, so a lingering registration doesn't keep
// pushing to an account that no longer exists. Does not delete the
// Firebase Auth user itself - the client does that separately, after
// this succeeds, so a failed re-authentication never
// leaves the user signed out with their data already gone.
router.delete('/', requireAuth, async (req, res) => {
  try {
    const uid = req.user.uid;

    const tripsSnap = await db.collection('trips').where('accountId', '==', uid).get();
    for (const tripDoc of tripsSnap.docs) {
      const tripId = tripDoc.id;
      const daysSnap = await db.collection('itinerary_days').where('tripId', '==', tripId).get();
      for (const dayDoc of daysSnap.docs) {
        await deleteByQuery(db.collection('itinerary_flights').where('dayId', '==', dayDoc.id));
        await deleteByQuery(db.collection('itinerary_places').where('dayId', '==', dayDoc.id));
      }
      await deleteByQuery(db.collection('itinerary_lodging').where('tripId', '==', tripId));
      await deleteByQuery(db.collection('itinerary_cars').where('tripId', '==', tripId));
      await deleteDocsInBatches(daysSnap.docs);
    }
    await deleteDocsInBatches(tripsSnap.docs);

    const devicesSnap = await db.collection('devices').where('accountId', '==', uid).get();
    await deleteDocsInBatches(devicesSnap.docs);

    await db.collection(SETTINGS_COLLECTION).doc(uid).delete();

    res.json({ deleted: uid });
  } catch (err) {
    console.error('DELETE /account error:', err);
    res.status(500).json({ error: 'Could not delete the account' });
  }
});

// The caller's synced settings. One document per user, keyed by the uid
// from the verified token, so nobody can read or write another user's.
// Returns the defaults with updatedAtMs 0 when nothing has been saved yet.
router.get('/settings', requireAuth, async (req, res) => {
  try {
    const doc = await db.collection(SETTINGS_COLLECTION).doc(req.user.uid).get();
    res.json(doc.exists ? { ...SETTINGS_DEFAULTS, ...doc.data() } : { ...SETTINGS_DEFAULTS });
  } catch (err) {
    console.error('GET /account/settings error:', err);
    res.status(500).json({ error: 'Could not load settings' });
  }
});

// Saves the caller's settings. Last write wins by updatedAtMs: an older copy
// (e.g. a phone that was offline) is ignored and the stored, newer one is
// returned so the client can catch up.
router.put('/settings', requireAuth, async (req, res) => {
  try {
    const { settings, error } = validateSettings(req.body);
    if (error) return res.status(400).json({ error });

    const ref = db.collection(SETTINGS_COLLECTION).doc(req.user.uid);
    const existing = await ref.get();
    if (existing.exists && (existing.data().updatedAtMs ?? 0) > settings.updatedAtMs) {
      return res.json({ ...SETTINGS_DEFAULTS, ...existing.data(), applied: false });
    }

    await ref.set(settings);
    console.log(`PUT /account/settings saved for ${req.user.uid}`);
    res.json({ ...settings, applied: true });
  } catch (err) {
    console.error('PUT /account/settings error:', err);
    res.status(500).json({ error: 'Could not save settings' });
  }
});

module.exports = router;
