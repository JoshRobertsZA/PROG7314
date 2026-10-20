// declares const `{ admin }`, initialised with the result of calling `require(…)`
const { admin } = require('../firebase');

// declares async function `requireAuth` taking 3 parameters (`req`, `res`, `next`) and opens its body
async function requireAuth(req, res, next) {
  // declares const `authHeader`, initialised to `req.headers['authorization']`
  const authHeader = req.headers['authorization'];
  // `if` statement: the block below runs when `!authHeader || !authHeader.startsWith('Bearer ')` is true
  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    // returns `res.status(401).json({ error: 'Missing or invalid Authorization heade…` from the current function
    return res.status(401).json({ error: 'Missing or invalid Authorization header' });
  // closes the if block
  }

  // declares const `idToken`, initialised with the result of calling `authHeader.split(…)`
  const idToken = authHeader.split('Bearer ')[1];

  // `try` block: exceptions thrown inside are handled by the `catch` below
  try {
    // declares const `decoded`, initialised to the awaited result of `admin.auth().verifyIdToken(idToken)`
    const decoded = await admin.auth().verifyIdToken(idToken);
    // assigns `req.user` the value `decoded`
    req.user = decoded;
    // calls `next` with arguments `()`
    next();
  // `catch` block: handles a thrown `exception` bound to `err`
  } catch (err) {
    // calls `error` on `console` with arguments `('Token verification failed:', err.message)`
    console.error('Token verification failed:', err.message);
    // returns `res.status(401).json({ error: 'Invalid or expired token' })` from the current function
    return res.status(401).json({ error: 'Invalid or expired token' });
  // closes the catch block
  }
// closes the function `requireAuth`
}

// assigns `module.exports` the value `{ requireAuth }`
module.exports = { requireAuth };
