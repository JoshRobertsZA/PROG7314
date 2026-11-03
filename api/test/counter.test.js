jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => firebaseMock.__reset());

describe('GET /counter/:key', () => {
  test('does not require auth', async () => {
    const res = await request(app).get('/counter/places_lookups');
    expect(res.status).toBe(200);
  });

  test('rejects a key with characters outside the allowlist pattern', async () => {
    const res = await request(app).get('/counter/not valid!');
    expect(res.status).toBe(400);
  });

  test('returns 0 for a counter that has never been incremented', async () => {
    const res = await request(app).get('/counter/places_lookups');
    expect(res.body).toEqual({ key: 'places_lookups', count: 0 });
  });

  test('returns the stored count', async () => {
    firebaseMock.__seed('counters', 'places_lookups', { count: 5, updatedAtMs: 1 });
    const res = await request(app).get('/counter/places_lookups');
    expect(res.body.count).toBe(5);
  });
});

describe('POST /counter/:key/increment', () => {
  test('requires auth', async () => {
    const res = await request(app).post('/counter/places_lookups/increment');
    expect(res.status).toBe(401);
  });

  test('rejects an invalid key', async () => {
    authedUid('user-1');
    const res = await request(app).post('/counter/NOT-VALID/increment').set(authHeader());
    expect(res.status).toBe(400);
  });

  test('starts a never-seen counter at 1', async () => {
    authedUid('user-1');
    const res = await request(app).post('/counter/places_lookups/increment').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body.count).toBe(1);
  });

  test('increments an existing counter by 1 each call, shared across users', async () => {
    authedUid('user-1');
    await request(app).post('/counter/places_lookups/increment').set(authHeader());
    authedUid('user-2');
    const res = await request(app).post('/counter/places_lookups/increment').set(authHeader());
    expect(res.body.count).toBe(2);
  });
});
