const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');
const { deleteDocsInBatches, deleteByQuery } = require('./trips');

// Deletes every Firestore document this account owns: each trip and its
// itinerary children (same cascade as DELETE /trips/:id, run once per
// trip), then device tokens so a lingering registration doesn't keep
// pushing to an account that no longer exists. Does not delete the
// Firebase Auth user itself - the client does that separately (PR 9
// commit 4), after this succeeds, so a failed re-authentication never
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

    res.json({ deleted: uid });
  } catch (err) {
    console.error('DELETE /account error:', err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
