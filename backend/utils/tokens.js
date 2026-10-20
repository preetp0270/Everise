const crypto = require('crypto');
const jwt = require('jsonwebtoken');
const { env } = require('../config/env');

/**
 * Create short-lived access token (JWT).
 * Payload: { sub: userId, type: 'access' }
 */
function signAccessToken(userId) {
  return jwt.sign(
    { sub: userId, type: 'access' },
    env.jwtAccessSecret,
    { expiresIn: env.accessTokenTtl }
  );
}

/**
 * Verify access token. Throws on invalid/expired.
 */
function verifyAccessToken(token) {
  const payload = jwt.verify(token, env.jwtAccessSecret);
  if (payload.type !== 'access') {
    throw new Error('Invalid token type');
  }
  return payload;
}

/**
 * Create opaque refresh token (random bytes) — store only hash in DB.
 */
function generateRefreshToken() {
  return crypto.randomBytes(48).toString('base64url');
}

/**
 * Hash refresh token for storage (SHA-256).
 */
function hashRefreshToken(token) {
  return crypto.createHash('sha256').update(token).digest('hex');
}

/**
 * Parse TTL string like "15m" or "30d" to milliseconds.
 */
function ttlToMs(ttl) {
  const match = String(ttl).match(/^(\d+)([smhd])$/i);
  if (!match) {
    // fallback 30 days
    return 30 * 24 * 60 * 60 * 1000;
  }
  const n = parseInt(match[1], 10);
  const unit = match[2].toLowerCase();
  const map = { s: 1000, m: 60_000, h: 3_600_000, d: 86_400_000 };
  return n * (map[unit] || 86_400_000);
}

function refreshExpiresAt() {
  return new Date(Date.now() + ttlToMs(env.refreshTokenTtl));
}

module.exports = {
  signAccessToken,
  verifyAccessToken,
  generateRefreshToken,
  hashRefreshToken,
  ttlToMs,
  refreshExpiresAt,
};
