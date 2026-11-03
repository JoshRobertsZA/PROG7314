// Jest manual mock for ../firebase. Activated per test file with
// jest.mock('../firebase') (or a relative path to this module), so routes
// that do `const { db } = require('../firebase')` get this in-memory
// Firestore instead of a real project - no service account or network
// needed to run the test suite.
//
// Covers exactly the Firestore surface routes/*.js and push.js actually
// call: collection().doc().get/set/create/update/delete, collection()
// .where().orderBy().get(), db.batch(), db.runTransaction(). Extend this
// (not individual tests) if a route starts using something new.

let store = {};

function matchOp(actual, op, expected) {
  if (op !== '==') throw new Error(`Mock Firestore only supports '==' queries, got '${op}'`);
  return actual === expected;
}

class MockDocRef {
  constructor(collectionName, id) {
    this.collectionName = collectionName;
    this.id = id;
  }

  async get() {
    const data = store[this.collectionName]?.[this.id];
    return { exists: data !== undefined, id: this.id, data: () => data };
  }

  async set(data, opts = {}) {
    store[this.collectionName] = store[this.collectionName] || {};
    const existing = store[this.collectionName][this.id];
    store[this.collectionName][this.id] = opts.merge && existing ? { ...existing, ...data } : data;
  }

  // Matches the real create()'s contract: throws with .code = 6
  // (ALREADY_EXISTS) if a document already exists at this id - the same
  // signal trips.js and itinerary.js branch on.
  async create(data) {
    store[this.collectionName] = store[this.collectionName] || {};
    if (store[this.collectionName][this.id] !== undefined) {
      const err = new Error('ALREADY_EXISTS');
      err.code = 6;
      throw err;
    }
    store[this.collectionName][this.id] = data;
  }

  async update(data) {
    store[this.collectionName] = store[this.collectionName] || {};
    const existing = store[this.collectionName][this.id];
    store[this.collectionName][this.id] = { ...existing, ...data };
  }

  async delete() {
    if (store[this.collectionName]) delete store[this.collectionName][this.id];
  }
}

class MockQuery {
  constructor(collectionName, filters = [], order = null) {
    this.collectionName = collectionName;
    this.filters = filters;
    this.order = order;
  }

  where(field, op, value) {
    return new MockQuery(this.collectionName, [...this.filters, { field, op, value }], this.order);
  }

  orderBy(field, direction = 'asc') {
    return new MockQuery(this.collectionName, this.filters, { field, direction });
  }

  async get() {
    const coll = store[this.collectionName] || {};
    let entries = Object.entries(coll);
    for (const f of this.filters) {
      entries = entries.filter(([, data]) => matchOp(data[f.field], f.op, f.value));
    }
    if (this.order) {
      const { field, direction } = this.order;
      entries.sort((a, b) => {
        const av = a[1][field];
        const bv = b[1][field];
        const cmp = av < bv ? -1 : av > bv ? 1 : 0;
        return direction === 'desc' ? -cmp : cmp;
      });
    }
    const docs = entries.map(([id, data]) => ({
      id,
      data: () => data,
      ref: new MockDocRef(this.collectionName, id),
    }));
    return { docs, empty: docs.length === 0 };
  }
}

class MockCollectionRef extends MockQuery {
  constructor(name) {
    super(name);
  }

  doc(id) {
    return new MockDocRef(this.collectionName, id);
  }
}

const db = {
  collection: (name) => new MockCollectionRef(name),

  batch: () => {
    const ops = [];
    return {
      delete: (ref) => ops.push({ type: 'delete', ref }),
      set: (ref, data, opts) => ops.push({ type: 'set', ref, data, opts }),
      commit: async () => {
        for (const op of ops) {
          if (op.type === 'delete') await op.ref.delete();
          else await op.ref.set(op.data, op.opts);
        }
      },
    };
  },

  // Real Firestore defers a transaction's writes until the function
  // returns and buffers reads against a snapshot; this mock just runs the
  // callback's reads/writes immediately against the shared store, which is
  // fine for route logic that reads a doc once and writes it back (every
  // caller in this codebase), but would not catch a true concurrent-write
  // race the way the real SDK's optimistic locking does.
  runTransaction: async (fn) => {
    const t = {
      get: (ref) => ref.get(),
      set: (ref, data, opts) => ref.set(data, opts),
      update: (ref, data) => ref.update(data),
      delete: (ref) => ref.delete(),
    };
    return fn(t);
  },
};

const verifyIdToken = jest.fn();
const sendEachForMulticast = jest.fn().mockResolvedValue({ successCount: 0, failureCount: 0, responses: [] });

const admin = {
  auth: () => ({ verifyIdToken }),
  messaging: () => ({ sendEachForMulticast }),
};

// Call in beforeEach so one test's fixture data and mock return values
// never leak into the next.
function __reset() {
  store = {};
  verifyIdToken.mockReset();
  sendEachForMulticast.mockReset();
  sendEachForMulticast.mockResolvedValue({ successCount: 0, failureCount: 0, responses: [] });
}

// Test setup helper: seeds a document directly into the store, bypassing
// create()'s ALREADY_EXISTS check.
function __seed(collectionName, id, data) {
  store[collectionName] = store[collectionName] || {};
  store[collectionName][id] = data;
}

module.exports = { db, admin, __reset, __seed };
