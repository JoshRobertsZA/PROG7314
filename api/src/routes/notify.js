/**
 * /notify/test: sends a test push to every device the caller has registered. Used to demonstrate
 * notifications from Swagger UI or Postman; real pushes are sent from the route where the event happens.
 */
const express = require('express');
const router = express.Router();
const { requireAuth } = require('../middleware/auth');
const { sendToUser } = require('../push');

// Lets a signed-in user push a test notification to every device they're
// registered on, so the settings screen's "send test notification" button
// has something real to call rather than faking success client-side.
router.post('/test', requireAuth, async (req, res) => {
  try {
    const result = await sendToUser(req.user.uid, { type: 'test', sentAtMs: String(Date.now()) });
    res.json(result);
  } catch (err) {
    console.error('POST /notify/test error:', err);
    res.status(500).json({ error: err.message });
  }
});

module.exports = router;
