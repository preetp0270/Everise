const User = require('../models/User');
const RefreshSession = require('../models/RefreshSession');
const { hashPassword, verifyPassword } = require('../utils/password');
const {
  signAccessToken,
  generateRefreshToken,
  hashRefreshToken,
  refreshExpiresAt,
} = require('../utils/tokens');
const {
  conflict,
  unauthorized,
  notFound,
  badRequest,
} = require('../utils/errors');

/**
 * Register a new user and issue tokens.
 */
async function register({ email, password, displayName, timezone }) {
  const existing = await User.findOne({ email, deletedAt: null });
  if (existing) {
    throw conflict('An account with this email already exists');
  }

  const passwordHash = await hashPassword(password);
  const user = await User.create({
    email,
    passwordHash,
    displayName: displayName || email.split('@')[0],
    timezone: timezone || 'UTC',
  });

  const tokens = await issueTokens(user._id, 'registration');
  return { user: user.toJSON(), ...tokens };
}

/**
 * Login and issue tokens.
 */
async function login({ email, password, deviceInfo }) {
  const user = await User.findOne({ email, deletedAt: null }).select('+passwordHash');
  if (!user) {
    throw unauthorized('Invalid email or password');
  }

  const valid = await verifyPassword(user.passwordHash, password);
  if (!valid) {
    throw unauthorized('Invalid email or password');
  }

  const tokens = await issueTokens(user._id, deviceInfo || 'login');
  return { user: user.toJSON(), ...tokens };
}

/**
 * Rotate refresh token — detect reuse and revoke family if needed.
 */
async function refresh({ refreshToken, deviceInfo }) {
  const tokenHash = hashRefreshToken(refreshToken);
  const session = await RefreshSession.findOne({ tokenHash });

  if (!session) {
    throw unauthorized('Invalid refresh token');
  }

  // Reuse detection: token already revoked (was rotated away)
  if (session.revokedAt) {
    // Possible token theft — revoke all sessions for this user
    await RefreshSession.updateMany(
      { userId: session.userId, revokedAt: null },
      { $set: { revokedAt: new Date() } }
    );
    throw unauthorized('Refresh token reuse detected — all sessions revoked');
  }

  if (session.expiresAt < new Date()) {
    session.revokedAt = new Date();
    await session.save();
    throw unauthorized('Refresh token expired');
  }

  // Rotate: revoke old, create new
  const newRaw = generateRefreshToken();
  const newHash = hashRefreshToken(newRaw);
  const newExpires = refreshExpiresAt();

  session.revokedAt = new Date();
  session.replacedByHash = newHash;
  await session.save();

  await RefreshSession.create({
    userId: session.userId,
    tokenHash: newHash,
    deviceInfo: deviceInfo || session.deviceInfo,
    expiresAt: newExpires,
  });

  const accessToken = signAccessToken(session.userId.toString());
  const user = await User.findById(session.userId);
  if (!user || user.deletedAt) {
    throw unauthorized('User no longer available');
  }

  return {
    user: user.toJSON(),
    accessToken,
    refreshToken: newRaw,
    expiresAt: newExpires.toISOString(),
  };
}

/**
 * Logout current session (revoke refresh token).
 */
async function logout(refreshToken) {
  if (!refreshToken) return;
  const tokenHash = hashRefreshToken(refreshToken);
  await RefreshSession.updateOne(
    { tokenHash, revokedAt: null },
    { $set: { revokedAt: new Date() } }
  );
}

/**
 * Logout all devices for a user.
 */
async function logoutAll(userId) {
  await RefreshSession.updateMany(
    { userId, revokedAt: null },
    { $set: { revokedAt: new Date() } }
  );
}

/**
 * Change password and revoke all sessions.
 */
async function changePassword(userId, { currentPassword, newPassword }) {
  const user = await User.findById(userId).select('+passwordHash');
  if (!user || user.deletedAt) {
    throw notFound('User not found');
  }

  const valid = await verifyPassword(user.passwordHash, currentPassword);
  if (!valid) {
    throw unauthorized('Current password is incorrect');
  }

  if (currentPassword === newPassword) {
    throw badRequest('New password must be different from current password');
  }

  user.passwordHash = await hashPassword(newPassword);
  await user.save();
  await logoutAll(userId);

  return { message: 'Password changed successfully. Please sign in again.' };
}

/**
 * Soft-delete account.
 */
async function deleteAccount(userId) {
  const user = await User.findById(userId);
  if (!user || user.deletedAt) {
    throw notFound('User not found');
  }
  user.deletedAt = new Date();
  user.email = `deleted_${user._id}_${user.email}`;
  await user.save();
  await logoutAll(userId);
  return { message: 'Account deleted' };
}

/**
 * Get current user profile.
 */
async function getMe(userId) {
  const user = await User.findOne({ _id: userId, deletedAt: null });
  if (!user) throw notFound('User not found');
  return user.toJSON();
}

async function issueTokens(userId, deviceInfo) {
  const accessToken = signAccessToken(userId.toString());
  const rawRefresh = generateRefreshToken();
  const tokenHash = hashRefreshToken(rawRefresh);
  const expiresAt = refreshExpiresAt();

  await RefreshSession.create({
    userId,
    tokenHash,
    deviceInfo: deviceInfo || '',
    expiresAt,
  });

  return {
    accessToken,
    refreshToken: rawRefresh,
    expiresAt: expiresAt.toISOString(),
  };
}

module.exports = {
  register,
  login,
  refresh,
  logout,
  logoutAll,
  changePassword,
  deleteAccount,
  getMe,
};
