const { db, admin } = require('./firebase');

const COLLECTION = 'devices';

// FCM reports these when a token is no longer valid (app uninstalled, token
// rotated without the server being told). Pruning them here is the only
// place that happens, since the client can't know a token went stale on
// someone else's device.
const UNREGISTERED_ERROR_CODES = new Set([
  'messaging/registration-token-not-registered',
  'messaging/invalid-registration-token',
]);

async function getTokensForUser(uid, { excludeToken } = {}) {
  const snap = await db.collection(COLLECTION).where('accountId', '==', uid).get();
  const docs = excludeToken ? snap.docs.filter(doc => doc.id !== excludeToken) : snap.docs;
  return docs.map(doc => doc.id);
}

async function pruneUnregistered(tokens, responses) {
  const toDelete = [];
  responses.forEach((resp, i) => {
    if (!resp.success && resp.error && UNREGISTERED_ERROR_CODES.has(resp.error.code)) {
      toDelete.push(tokens[i]);
    }
  });
  if (toDelete.length === 0) return 0;

  const batch = db.batch();
  for (const token of toDelete) batch.delete(db.collection(COLLECTION).doc(token));
  await batch.commit();
  return toDelete.length;
}

// Data-only payload by design: the client builds the visible text from its
// own string resources so the notification respects whatever language the
// user has chosen, rather than baking English text into the push itself.
async function sendDataMessage(tokens, data) {
  if (tokens.length === 0) return { successCount: 0, failureCount: 0, pruned: 0 };

  const stringData = Object.fromEntries(Object.entries(data).map(([key, value]) => [key, String(value)]));
  const resp = await admin.messaging().sendEachForMulticast({ tokens, data: stringData });
  const pruned = await pruneUnregistered(tokens, resp.responses);

  return { successCount: resp.successCount, failureCount: resp.failureCount, pruned };
}

// True unless the user switched this kind of notification off in the app's
// Settings (synced to the `settings` collection by PUT /account/settings).
async function isEnabledFor(uid, settingKey) {
  const doc = await db.collection('settings').doc(uid).get();
  return !(doc.exists && doc.data()[settingKey] === false);
}

// opts.setting names the user preference that gates this push (e.g.
// 'notifyTripUpdates'); when it is off nothing is sent.
async function sendToUser(uid, data, opts = {}) {
  if (opts.setting && !(await isEnabledFor(uid, opts.setting))) {
    console.log(`push skipped for ${uid}: ${opts.setting} is off`);
    return { successCount: 0, failureCount: 0, pruned: 0, skipped: true };
  }
  const tokens = await getTokensForUser(uid, opts);
  return sendDataMessage(tokens, data);
}

module.exports = { sendToUser, sendDataMessage, getTokensForUser, isEnabledFor };
