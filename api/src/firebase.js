const admin = require('firebase-admin');

let app;

function initFirebase() {
  if (app) return app;

  // In production: set FIREBASE_SERVICE_ACCOUNT env var to the full JSON string
  // In local dev: set GOOGLE_APPLICATION_CREDENTIALS to the path of your key file
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
