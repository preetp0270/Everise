const argon2 = require('argon2');

/**
 * Hash password with Argon2id (recommended defaults).
 */
async function hashPassword(plain) {
  return argon2.hash(plain, {
    type: argon2.argon2id,
    memoryCost: 65536, // 64 MB
    timeCost: 3,
    parallelism: 1,
  });
}

/**
 * Verify plain password against stored hash.
 */
async function verifyPassword(hash, plain) {
  try {
    return await argon2.verify(hash, plain);
  } catch {
    return false;
  }
}

module.exports = { hashPassword, verifyPassword };
