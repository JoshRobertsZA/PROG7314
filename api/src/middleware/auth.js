/**
 * Express middleware for every user route. Checks the `Authorization: Bearer <Firebase ID token>`
 * header with the Firebase Admin SDK and puts the decoded token on `req.user`; routes take the
 * user id from `req.user.uid`, never from the request body or query. Responds 401 if the token
 * is missing, invalid or expired.
 */
const { admin } = require('../firebase');

async function requireAuth(req, res, next) {
  const authHeader = req.headers['authorization'];
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({ error: 'Missing or invalid Authorization header' });
  }

  const idToken = authHeader.split('Bearer ')[1];

  try {
    const decoded = await admin.auth().verifyIdToken(idToken);
    req.user = decoded;
    next();
  } catch (err) {
    console.error('Token verification failed:', err.message);
    return res.status(401).json({ error: 'Invalid or expired token' });
  }
}

module.exports = { requireAuth };
