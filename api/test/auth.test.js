jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => firebaseMock.__reset());

// GET /trips is just a convenient stand-in for "any route behind
// requireAuth" - none of these cases are specific to trips.
describe('requireAuth middleware', () => {
  test('rejects a request with no Authorization header', async () => {
    const res = await request(app).get('/trips');
    expect(res.status).toBe(401);
  });

  test('rejects an Authorization header without a Bearer prefix', async () => {
    const res = await request(app).get('/trips').set('Authorization', 'faketoken');
    expect(res.status).toBe(401);
  });

  test('rejects a token verifyIdToken rejects as invalid or expired', async () => {
    firebaseMock.admin.auth().verifyIdToken.mockRejectedValue(new Error('invalid token'));
    const res = await request(app).get('/trips').set(authHeader());
    expect(res.status).toBe(401);
  });

  test('sets req.user and calls through once verifyIdToken resolves', async () => {
    authedUid('user-1');
    const res = await request(app).get('/trips').set(authHeader());
    expect(res.status).toBe(200);
  });
});
