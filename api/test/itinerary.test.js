jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => {
  firebaseMock.__reset();
  authedUid('user-1');
});

const TRIP_ID = 'a1b2c3d4-e5f6-4789-a012-b3c4d5e6f789';
const OTHER_TRIP_ID = 'b2c3d4e5-f6a7-4890-b123-c4d5e6f7a890';
const DAY_ID = 'c3d4e5f6-a7b8-4901-c234-d5e6f7a8b901';
const FLIGHT_ID = 'd4e5f6a7-b8c9-4012-d345-e6f7a8b9c012';

async function seedTrip(id = TRIP_ID, accountId = 'user-1') {
  firebaseMock.__seed('trips', id, { accountId, name: 'Trip' });
}

// days: ownership resolved directly through tripId.
describe('/itinerary/days', () => {
  test('POST 404s when the parent trip does not exist', async () => {
    const res = await request(app)
      .post('/itinerary/days')
      .set(authHeader())
      .send({ id: DAY_ID, tripId: TRIP_ID, date: '2026-12-01' });
    expect(res.status).toBe(404);
  });

  test('POST 403s when the trip belongs to a different account', async () => {
    await seedTrip(TRIP_ID, 'someone-else');
    const res = await request(app)
      .post('/itinerary/days')
      .set(authHeader())
      .send({ id: DAY_ID, tripId: TRIP_ID, date: '2026-12-01' });
    expect(res.status).toBe(403);
  });

  test('POST creates a day for the trip owner', async () => {
    await seedTrip();
    const res = await request(app)
      .post('/itinerary/days')
      .set(authHeader())
      .send({ id: DAY_ID, tripId: TRIP_ID, date: '2026-12-01' });
    expect(res.status).toBe(201);
  });

  test('GET lists only days for the given trip, in date order', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', 'd1', { tripId: TRIP_ID, date: '2026-12-02' });
    firebaseMock.__seed('itinerary_days', 'd2', { tripId: TRIP_ID, date: '2026-12-01' });
    firebaseMock.__seed('itinerary_days', 'd3', { tripId: OTHER_TRIP_ID, date: '2026-12-01' });

    const res = await request(app).get(`/itinerary/days/${TRIP_ID}`).set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.map((d) => d.id)).toEqual(['d2', 'd1']);
  });

  test('PUT cannot move a day onto a different tripId', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    const res = await request(app)
      .put(`/itinerary/days/${DAY_ID}`)
      .set(authHeader())
      .send({ tripId: OTHER_TRIP_ID, date: '2026-12-05' });
    expect(res.status).toBe(200);
    expect(res.body.tripId).toBe(TRIP_ID);
    expect(res.body.date).toBe('2026-12-05');
  });

  test('DELETE removes the day', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    const res = await request(app).delete(`/itinerary/days/${DAY_ID}`).set(authHeader());
    expect(res.status).toBe(200);
    expect((await firebaseMock.db.collection('itinerary_days').doc(DAY_ID).get()).exists).toBe(false);
  });
});

// flights: ownership resolved two hops away, through dayId -> tripId.
describe('/itinerary/flights', () => {
  test('POST 403s when the day belongs to a trip the caller does not own', async () => {
    await seedTrip(TRIP_ID, 'someone-else');
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    const res = await request(app)
      .post('/itinerary/flights')
      .set(authHeader())
      .send({ id: FLIGHT_ID, dayId: DAY_ID, flightNumber: 'SA123' });
    expect(res.status).toBe(403);
  });

  test('POST creates a flight for a day the caller owns via its trip', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    const res = await request(app)
      .post('/itinerary/flights')
      .set(authHeader())
      .send({ id: FLIGHT_ID, dayId: DAY_ID, flightNumber: 'SA123' });
    expect(res.status).toBe(201);
    expect(res.body.flightNumber).toBe('SA123');
  });

  test('a retry with the same id is idempotent (200, not 409)', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    const body = { id: FLIGHT_ID, dayId: DAY_ID, flightNumber: 'SA123' };
    await request(app).post('/itinerary/flights').set(authHeader()).send(body);
    const res = await request(app).post('/itinerary/flights').set(authHeader()).send(body);
    expect(res.status).toBe(200);
  });

  test('PUT cannot move a flight onto a different dayId', async () => {
    await seedTrip();
    firebaseMock.__seed('itinerary_days', DAY_ID, { tripId: TRIP_ID, date: '2026-12-01' });
    firebaseMock.__seed('itinerary_flights', FLIGHT_ID, { dayId: DAY_ID, flightNumber: 'SA123', createdAtMs: 1 });

    const res = await request(app)
      .put(`/itinerary/flights/${FLIGHT_ID}`)
      .set(authHeader())
      .send({ dayId: 'some-other-day', flightNumber: 'BA456' });

    expect(res.status).toBe(200);
    expect(res.body.dayId).toBe(DAY_ID);
    expect(res.body.flightNumber).toBe('BA456');
  });
});
