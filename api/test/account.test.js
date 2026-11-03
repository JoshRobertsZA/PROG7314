jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => {
  firebaseMock.__reset();
  authedUid('user-1');
});

describe('DELETE /account', () => {
  test('cascades through every trip the caller owns and their device tokens, leaving other accounts untouched', async () => {
    firebaseMock.__seed('trips', 'trip-1', { accountId: 'user-1', name: 'Mine' });
    firebaseMock.__seed('itinerary_days', 'day-1', { tripId: 'trip-1', date: '2026-12-01' });
    firebaseMock.__seed('itinerary_flights', 'flight-1', { dayId: 'day-1' });
    firebaseMock.__seed('itinerary_lodging', 'lodging-1', { tripId: 'trip-1' });
    firebaseMock.__seed('devices', 'token-1', { accountId: 'user-1' });

    firebaseMock.__seed('trips', 'trip-2', { accountId: 'someone-else', name: 'Not mine' });
    firebaseMock.__seed('devices', 'token-2', { accountId: 'someone-else' });

    const res = await request(app).delete('/account').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.deleted).toBe('user-1');

    expect((await firebaseMock.db.collection('trips').doc('trip-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_days').doc('day-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_flights').doc('flight-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_lodging').doc('lodging-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('devices').doc('token-1').get()).exists).toBe(false);

    // A different account's trip and device token must survive.
    expect((await firebaseMock.db.collection('trips').doc('trip-2').get()).exists).toBe(true);
    expect((await firebaseMock.db.collection('devices').doc('token-2').get()).exists).toBe(true);
  });

  test('succeeds as a no-op when the caller owns nothing', async () => {
    const res = await request(app).delete('/account').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.deleted).toBe('user-1');
  });

  test('requires auth', async () => {
    const res = await request(app).delete('/account');
    expect(res.status).toBe(401);
  });
});
