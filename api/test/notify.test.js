jest.mock('../src/firebase');

const request = require('supertest');
const app = require('../src/index');
const { firebaseMock, authedUid, authHeader } = require('./helpers');

beforeEach(() => {
  firebaseMock.__reset();
  authedUid('user-1');
});

describe('POST /notify/test', () => {
  test('requires auth', async () => {
    firebaseMock.admin.auth().verifyIdToken.mockReset();
    const res = await request(app).post('/notify/test');
    expect(res.status).toBe(401);
  });

  test('sends only to the caller\'s own registered devices', async () => {
    firebaseMock.__seed('devices', 'token-mine', { accountId: 'user-1' });
    firebaseMock.__seed('devices', 'token-not-mine', { accountId: 'someone-else' });
    firebaseMock.admin.messaging().sendEachForMulticast.mockResolvedValue({
      successCount: 1,
      failureCount: 0,
      responses: [{ success: true }],
    });

    const res = await request(app).post('/notify/test').set(authHeader());

    expect(res.status).toBe(200);
    expect(firebaseMock.admin.messaging().sendEachForMulticast).toHaveBeenCalledWith(
      expect.objectContaining({ tokens: ['token-mine'] }),
    );
  });

  test('reports zero sends and does not call FCM when there are no registered devices', async () => {
    const res = await request(app).post('/notify/test').set(authHeader());
    expect(res.status).toBe(200);
    expect(res.body).toEqual({ successCount: 0, failureCount: 0, pruned: 0 });
    expect(firebaseMock.admin.messaging().sendEachForMulticast).not.toHaveBeenCalled();
  });

  test('prunes a device token FCM reports as unregistered', async () => {
    firebaseMock.__seed('devices', 'stale-token', { accountId: 'user-1' });
    firebaseMock.admin.messaging().sendEachForMulticast.mockResolvedValue({
      successCount: 0,
      failureCount: 1,
      responses: [{ success: false, error: { code: 'messaging/registration-token-not-registered' } }],
    });

    const res = await request(app).post('/notify/test').set(authHeader());

    expect(res.body.pruned).toBe(1);
    expect((await firebaseMock.db.collection('devices').doc('stale-token').get()).exists).toBe(false);
  });

  test('keeps a device token for a failure that is not an unregistered-token error', async () => {
    firebaseMock.__seed('devices', 'temp-fail-token', { accountId: 'user-1' });
    firebaseMock.admin.messaging().sendEachForMulticast.mockResolvedValue({
      successCount: 0,
      failureCount: 1,
      responses: [{ success: false, error: { code: 'messaging/internal-error' } }],
    });

    const res = await request(app).post('/notify/test').set(authHeader());

    expect(res.body.pruned).toBe(0);
    expect((await firebaseMock.db.collection('devices').doc('temp-fail-token').get()).exists).toBe(true);
  });
});
