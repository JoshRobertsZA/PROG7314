// Must require the real module path, not src/__mocks__/firebase directly -
// jest.mock('../src/firebase') in each test file only redirects requires of
// that exact path to the manual mock. Reaching into __mocks__ directly would
// load a second, separately-mocked instance with its own jest.fn()s, so
// authedUid() here would configure a verifyIdToken the routes never see.
const firebaseMock = require('../src/firebase');

// Most routes only care that verifyIdToken resolves with a uid - this
// covers that common case in one line per test instead of repeating the
// mock call everywhere.
function authedUid(uid) {
  firebaseMock.admin.auth().verifyIdToken.mockResolvedValue({ uid });
}

function authHeader(token = 'fake-token') {
  return { Authorization: `Bearer ${token}` };
}

module.exports = { firebaseMock, authedUid, authHeader };
