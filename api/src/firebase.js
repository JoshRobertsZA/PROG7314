/**
 * Initialises the Firebase Admin SDK once and exports it with the Firestore client.
 * On Cloud Run the service account comes from the FIREBASE_SERVICE_ACCOUNT secret; locally it
 * falls back to Application Default Credentials. Tests replace this module with __mocks__/firebase.js.
 */
const admin = require('firebase-admin');

let app;

function initFirebase() {
  if (app) return app;

  const serviceAccount = process.env.FIREBASE_SERVICE_ACCOUNT
    ? JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT)
    : undefined;

  app = admin.initializeApp(
    serviceAccount
      ? { credential: admin.credential.cert(serviceAccount) }
      : { credential: admin.credential.applicationDefault() }
  );

  return app;
}

initFirebase();

const db = admin.firestore();

module.exports = { admin, db };
