// declares const `admin`, initialised with the result of calling `require(…)`
const admin = require('firebase-admin');

// statement: `let app;`
let app;

// declares function `initFirebase` taking no parameters and opens its body
function initFirebase() {
  // `if` statement: executes `return app;` when `app` is true
  if (app) return app;

  // declares const `serviceAccount`, initialised to `process.env.FIREBASE_SERVICE_ACCOUNT`
  const serviceAccount = process.env.FIREBASE_SERVICE_ACCOUNT
    // statement: `? JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT)`
    ? JSON.parse(process.env.FIREBASE_SERVICE_ACCOUNT)
    // statement: `: undefined;`
    : undefined;

  // assigns `app` the value `admin.initializeApp(`
  app = admin.initializeApp(
    // continues the statement started above: `serviceAccount`
    serviceAccount
      // continues the statement started above: `? { credential: admin.credential.cert(serviceAccount) }`
      ? { credential: admin.credential.cert(serviceAccount) }
      // continues the statement started above: `: { credential: admin.credential.applicationDefault() }`
      : { credential: admin.credential.applicationDefault() }
  // closes the multi-line argument list started above
  );

  // returns `app` from the current function
  return app;
// closes the function `initFirebase`
}

// calls `initFirebase` with arguments `()`
initFirebase();

// declares const `db`, initialised with the result of calling `admin.firestore(…)`
const db = admin.firestore();

// assigns `module.exports` the value `{ admin, db }`
module.exports = { admin, db };
