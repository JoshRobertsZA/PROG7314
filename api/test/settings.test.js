jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { sendToUser } = require('../src/push');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

const validSettings = (overrides = {}) => ({
  darkMode: true,
  notifyTripUpdates: false,
  notifyReminders: true,
  defaultCurrency: 'EUR',
  language: 'zu',
  updatedAtMs: 1_000,
  ...overrides,
});

beforeEach(() => {
  firebaseMock.__reset();
  jest.clearAllMocks();
  authedUid('user-1');
});

describe('GET /account/settings', () => {
  test('returns defaults with updatedAtMs 0 when nothing has been saved', async () => {
    const res = await request(app).get('/account/settings').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body).toEqual({
      darkMode: null,
      notifyTripUpdates: true,
      notifyReminders: true,
      defaultCurrency: 'ZAR',
      language: 'en',
      updatedAtMs: 0,
    });
  });

  test("returns only the caller's own document", async () => {
    firebaseMock.__seed('settings', 'someone-else', validSettings({ defaultCurrency: 'GBP' }));
    firebaseMock.__seed('settings', 'user-1', validSettings());

    const res = await request(app).get('/account/settings').set(authHeader());
    expect(res.body.defaultCurrency).toBe('EUR');
  });

  test('requires auth', async () => {
    expect((await request(app).get('/account/settings')).status).toBe(401);
  });
});

describe('PUT /account/settings', () => {
  test('saves the settings under the uid from the verified token', async () => {
    const res = await request(app).put('/account/settings').set(authHeader()).send(validSettings());
    expect(res.status).toBe(200);
    expect(res.body.applied).toBe(true);

    const stored = (await firebaseMock.db.collection('settings').doc('user-1').get()).data();
    expect(stored).toEqual(validSettings());
  });

  test('keeps the stored copy when the incoming one is older (last write wins)', async () => {
    firebaseMock.__seed('settings', 'user-1', validSettings({ updatedAtMs: 5_000, defaultCurrency: 'USD' }));

    const res = await request(app)
      .put('/account/settings')
      .set(authHeader())
      .send(validSettings({ updatedAtMs: 4_000, defaultCurrency: 'JPY' }));

    expect(res.status).toBe(200);
    expect(res.body.applied).toBe(false);
    expect(res.body.defaultCurrency).toBe('USD');
    expect((await firebaseMock.db.collection('settings').doc('user-1').get()).data().defaultCurrency).toBe('USD');
  });

  test.each([
    ['an unknown language', { language: 'fr' }, /language/],
    ['a lowercase currency', { defaultCurrency: 'zar' }, /defaultCurrency/],
    ['a non-boolean switch', { notifyReminders: 'yes' }, /notifyReminders/],
    ['a negative timestamp', { updatedAtMs: -1 }, /updatedAtMs/],
    ['darkMode as a string', { darkMode: 'dark' }, /darkMode/],
  ])('rejects %s with 400', async (_label, override, message) => {
    const res = await request(app).put('/account/settings').set(authHeader()).send(validSettings(override));
    expect(res.status).toBe(400);
    expect(res.body.error).toMatch(message);
  });

  test('requires auth', async () => {
    expect((await request(app).put('/account/settings').send(validSettings())).status).toBe(401);
  });
});

describe('DELETE /account', () => {
  test("also removes the caller's saved settings", async () => {
    firebaseMock.__seed('settings', 'user-1', validSettings());
    await request(app).delete('/account').set(authHeader());
    expect((await firebaseMock.db.collection('settings').doc('user-1').get()).exists).toBe(false);
  });
});

describe('push respects the trip-updates setting', () => {
  beforeEach(() => {
    firebaseMock.__seed('devices', 'token-1', { accountId: 'user-1' });
    firebaseMock.admin.messaging().sendEachForMulticast.mockResolvedValue({
      successCount: 1,
      failureCount: 0,
      responses: [{ success: true }],
    });
  });

  test('sends nothing when the user turned trip updates off', async () => {
    firebaseMock.__seed('settings', 'user-1', validSettings({ notifyTripUpdates: false }));
    const result = await sendToUser('user-1', { type: 'trip_created' }, { setting: 'notifyTripUpdates' });
    expect(result.skipped).toBe(true);
    expect(firebaseMock.admin.messaging().sendEachForMulticast).not.toHaveBeenCalled();
  });

  test('sends when the user has no saved settings (on by default)', async () => {
    const result = await sendToUser('user-1', { type: 'trip_created' }, { setting: 'notifyTripUpdates' });
    expect(result.successCount).toBe(1);
    expect(firebaseMock.admin.messaging().sendEachForMulticast).toHaveBeenCalledTimes(1);
  });
});
