jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => firebaseMock.__reset());

// Proves the harness itself works - the real coverage lands in the
// following commits (trips, itinerary, account, counter, push).
describe('test harness', () => {
  test('GET /health responds without hitting Firestore or auth', async () => {
    const res = await request(app).get('/health');
    expect(res.status).toBe(200);
    expect(res.body.status).toBe('ok');
  });

  test('a protected route rejects a request with no Authorization header', async () => {
    const res = await request(app).get('/trips');
    expect(res.status).toBe(401);
  });

  test('a protected route accepts a request once verifyIdToken is mocked', async () => {
    authedUid('user-1');
    const res = await request(app).get('/trips').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body).toEqual([]);
  });

  test('the Firestore mock persists and reads back a seeded document', async () => {
    firebaseMock.__seed('trips', 'trip-1', { accountId: 'user-1', name: 'Cape Town' });
    authedUid('user-1');
    const res = await request(app).get('/trips/trip-1').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ id: 'trip-1', accountId: 'user-1', name: 'Cape Town' });
  });
});
