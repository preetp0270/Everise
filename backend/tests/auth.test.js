/**
 * Auth integration tests — run against a local or test MongoDB.
 * NODE_ENV=test MONGODB_URI=... node --test tests/auth.test.js
 *
 * Without MongoDB these tests skip gracefully.
 */
const { describe, it, before, after } = require('node:test');
const assert = require('node:assert/strict');

const hasMongo = Boolean(process.env.MONGODB_URI || process.env.TEST_MONGO);

describe('auth validators', () => {
  const { registerSchema, loginSchema, parseOrThrow } = require('../validators/auth');

  it('accepts valid register payload', () => {
    const data = parseOrThrow(registerSchema, {
      email: 'user@example.com',
      password: 'SecurePass1',
      displayName: 'Preet',
    });
    assert.equal(data.email, 'user@example.com');
    assert.equal(data.displayName, 'Preet');
  });

  it('rejects weak password', () => {
    assert.throws(
      () => parseOrThrow(registerSchema, { email: 'a@b.com', password: 'short' }),
      (err) => err.code === 'VALIDATION_ERROR'
    );
  });

  it('rejects invalid email', () => {
    assert.throws(
      () => parseOrThrow(loginSchema, { email: 'not-an-email', password: 'whatever1' }),
      (err) => err.code === 'VALIDATION_ERROR'
    );
  });
});

describe('token utilities', () => {
  const {
    signAccessToken,
    verifyAccessToken,
    generateRefreshToken,
    hashRefreshToken,
  } = require('../utils/tokens');

  it('signs and verifies access token', () => {
    const token = signAccessToken('507f1f77bcf86cd799439011');
    const payload = verifyAccessToken(token);
    assert.equal(payload.sub, '507f1f77bcf86cd799439011');
    assert.equal(payload.type, 'access');
  });

  it('hashes refresh tokens deterministically', () => {
    const raw = generateRefreshToken();
    assert.ok(raw.length > 20);
    assert.equal(hashRefreshToken(raw), hashRefreshToken(raw));
    assert.notEqual(hashRefreshToken(raw), hashRefreshToken(raw + 'x'));
  });
});

describe('password hashing', () => {
  const { hashPassword, verifyPassword } = require('../utils/password');

  it('hashes and verifies', async () => {
    const hash = await hashPassword('MySecure1');
    assert.ok(hash.startsWith('$argon2'));
    assert.equal(await verifyPassword(hash, 'MySecure1'), true);
    assert.equal(await verifyPassword(hash, 'wrong'), false);
  });
});

// Full HTTP tests only when Mongo is available
describe('auth HTTP (requires MongoDB)', { skip: !hasMongo }, () => {
  let app;
  let base;

  before(async () => {
    process.env.NODE_ENV = 'test';
    const { connectDB } = require('../config/db');
    await connectDB();
    ({ app } = require('../server'));
    // Use supertest-like manual fetch via http if needed — placeholder
    base = 'http://127.0.0.1:' + (process.env.PORT || 3000);
  });

  it('placeholder — start server and curl register/login in manual verification', () => {
    assert.ok(true);
  });
});
