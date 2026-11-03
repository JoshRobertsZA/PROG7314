jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => {
  firebaseMock.__reset();
  authedUid('user-1');
});

const VALID_ID = 'a1b2c3d4-e5f6-4789-a012-b3c4d5e6f789';
const OTHER_ID = 'b2c3d4e5-f6a7-4890-b123-c4d5e6f7a890';

function validTrip(overrides = {}) {
  return {
    id: VALID_ID,
    name: 'Cape Town trip',
    startDate: '2026-12-01',
    endDate: '2026-12-10',
    ...overrides,
  };
}

describe('GET /trips', () => {
  test('only returns the caller\'s own trips, newest first', async () => {
    firebaseMock.__seed('trips', 'a', { accountId: 'user-1', name: 'Older', createdAtMs: 1 });
    firebaseMock.__seed('trips', 'b', { accountId: 'user-1', name: 'Newer', createdAtMs: 2 });
    firebaseMock.__seed('trips', 'c', { accountId: 'someone-else', name: 'Not mine', createdAtMs: 3 });

    const res = await request(app).get('/trips').set(authHeader());

    expect(res.status).toBe(200);
    expect(res.body.map((t) => t.id)).toEqual(['b', 'a']);
  });
});

describe('GET /trips/:id', () => {
  test('404s for a trip that does not exist', async () => {
    const res = await request(app).get(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(404);
  });

  test('403s for a trip owned by a different account', async () => {
    firebaseMock.__seed('trips', VALID_ID, { accountId: 'someone-else', name: 'Not mine' });
    const res = await request(app).get(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(403);
  });

  test('returns the trip for its owner', async () => {
    firebaseMock.__seed('trips', VALID_ID, { accountId: 'user-1', name: 'Mine' });
    const res = await request(app).get(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.name).toBe('Mine');
  });
});

describe('POST /trips', () => {
  test('rejects a missing name', async () => {
    const res = await request(app).post('/trips').set(authHeader()).send({ id: VALID_ID });
    expect(res.status).toBe(400);
  });

  test('rejects a non-UUID id', async () => {
    const res = await request(app).post('/trips').set(authHeader()).send(validTrip({ id: 'not-a-uuid' }));
    expect(res.status).toBe(400);
  });

  test('rejects startDate after endDate', async () => {
    const res = await request(app)
      .post('/trips')
      .set(authHeader())
      .send(validTrip({ startDate: '2026-12-10', endDate: '2026-12-01' }));
    expect(res.status).toBe(400);
  });

  test('creates a trip and returns 201', async () => {
    const res = await request(app).post('/trips').set(authHeader()).send(validTrip());
    expect(res.status).toBe(201);
    expect(res.body.id).toBe(VALID_ID);
  });

  test('a retry with the same id by the same owner is idempotent (200, not 409)', async () => {
    await request(app).post('/trips').set(authHeader()).send(validTrip());
    const res = await request(app).post('/trips').set(authHeader()).send(validTrip());
    expect(res.status).toBe(200);
  });

  test('an id already used by a different account is rejected with 409', async () => {
    firebaseMock.__seed('trips', VALID_ID, { accountId: 'someone-else', name: 'Theirs' });
    const res = await request(app).post('/trips').set(authHeader()).send(validTrip());
    expect(res.status).toBe(409);
  });
});

describe('PUT /trips/:id', () => {
  beforeEach(() => {
    firebaseMock.__seed('trips', VALID_ID, {
      accountId: 'user-1',
      name: 'Original',
      startDate: '2026-12-01',
      endDate: '2026-12-10',
    });
  });

  test('404s for a trip that does not exist', async () => {
    const res = await request(app).put(`/trips/${OTHER_ID}`).set(authHeader()).send({ name: 'X' });
    expect(res.status).toBe(404);
  });

  test('403s for a trip owned by a different account', async () => {
    firebaseMock.__seed('trips', OTHER_ID, { accountId: 'someone-else', name: 'Theirs' });
    const res = await request(app).put(`/trips/${OTHER_ID}`).set(authHeader()).send({ name: 'X' });
    expect(res.status).toBe(403);
  });

  test('cannot reassign accountId through the request body', async () => {
    const res = await request(app)
      .put(`/trips/${VALID_ID}`)
      .set(authHeader())
      .send({ accountId: 'someone-else' });
    expect(res.status).toBe(200);
    expect(res.body.accountId).toBeUndefined();
  });

  test('rejects an update that would put endDate before the existing startDate', async () => {
    const res = await request(app)
      .put(`/trips/${VALID_ID}`)
      .set(authHeader())
      .send({ endDate: '2026-11-01' });
    expect(res.status).toBe(400);
  });

  test('updates the name', async () => {
    const res = await request(app).put(`/trips/${VALID_ID}`).set(authHeader()).send({ name: 'Renamed' });
    expect(res.status).toBe(200);
    expect(res.body.name).toBe('Renamed');
  });
});

describe('DELETE /trips/:id', () => {
  test('404s for a trip that does not exist', async () => {
    const res = await request(app).delete(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(404);
  });

  test('403s for a trip owned by a different account', async () => {
    firebaseMock.__seed('trips', VALID_ID, { accountId: 'someone-else', name: 'Theirs' });
    const res = await request(app).delete(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(403);
  });

  test('deletes the trip and cascades to its itinerary children', async () => {
    firebaseMock.__seed('trips', VALID_ID, { accountId: 'user-1', name: 'Mine' });
    firebaseMock.__seed('itinerary_days', 'day-1', { tripId: VALID_ID, date: '2026-12-01' });
    firebaseMock.__seed('itinerary_flights', 'flight-1', { dayId: 'day-1' });
    firebaseMock.__seed('itinerary_lodging', 'lodging-1', { tripId: VALID_ID });

    const res = await request(app).delete(`/trips/${VALID_ID}`).set(authHeader());
    expect(res.status).toBe(200);

    expect((await firebaseMock.db.collection('trips').doc(VALID_ID).get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_days').doc('day-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_flights').doc('flight-1').get()).exists).toBe(false);
    expect((await firebaseMock.db.collection('itinerary_lodging').doc('lodging-1').get()).exists).toBe(false);
  });
});
