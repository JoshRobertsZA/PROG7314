const express = require('express');
const router = express.Router();
const { db } = require('../firebase');
const { requireAuth } = require('../middleware/auth');

const COLLECTION = 'trips';

// GET /trips — all trips for the authenticated user
router.get('/', requireAuth, async (req, res) => {
  try {
    const snapshot = await db.collection(COLLECTION)
      .where('accountId', '==', req.user.uid)
      .orderBy('createdAtMs', 'desc')
      .get();

    const trips = snapshot.docs.map(doc => ({ id: doc.id, ...doc.data() }));
    res.json(trips);
  } catch (err) {
    console.error('GET /trips error:', err);
    res.status(500).json({ error: err.message });
  }
});

// GET /trips/:id — single trip (must belong to auth user)
router.get('/:id', requireAuth, async (req, res) => {
  try {
    const doc = await db.collection(COLLECTION).doc(req.params.id).get();
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    const data = doc.data();
    if (data.accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });
    res.json({ id: doc.id, ...data });
  } catch (err) {
    console.error('GET /trips/:id error:', err);
    res.status(500).json({ error: err.message });
  }
});

// POST /trips — create a trip
router.post('/', requireAuth, async (req, res) => {
  try {
    const { id, name, startDate, endDate, destination, destLat, destLng, destPhotoUrl, createdAtMs, updatedAtMs } = req.body;
    if (!id || !name) return res.status(400).json({ error: 'id and name are required' });

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

    await db.collection(COLLECTION).doc(id).set(tripData);
    res.status(201).json({ id, ...tripData });
  } catch (err) {
    console.error('POST /trips error:', err);
    res.status(500).json({ error: err.message });
  }
});

// PUT /trips/:id — update a trip
router.put('/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection(COLLECTION).doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    if (doc.data().accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });

    const updates = { ...req.body, updatedAtMs: Date.now() };
    delete updates.accountId; // never allow overwriting ownership
    await ref.update(updates);
    res.json({ id: req.params.id, ...updates });
  } catch (err) {
    console.error('PUT /trips/:id error:', err);
    res.status(500).json({ error: err.message });
  }
});

// DELETE /trips/:id
router.delete('/:id', requireAuth, async (req, res) => {
  try {
    const ref = db.collection(COLLECTION).doc(req.params.id);
    const doc = await ref.get();
    if (!doc.exists) return res.status(404).json({ error: 'Trip not found' });
    if (doc.data().accountId !== req.user.uid) return res.status(403).json({ error: 'Forbidden' });
    await ref.delete();
    res.json({ deleted: req.params.id });
  } catch (err) {
    console.error('DELETE /trips/:id error:', err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
