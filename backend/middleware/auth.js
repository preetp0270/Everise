const { verifyAccessToken } = require('../utils/tokens');
const { unauthorized } = require('../utils/errors');

/**
 * Require valid Bearer access token.
 * Sets req.userId from token subject.
 */
function requireAuth(req, res, next) {
  try {
    const header = req.headers.authorization;
    if (!header || !header.startsWith('Bearer ')) {
      throw unauthorized('Missing or invalid Authorization header');
    }
    const token = header.slice(7).trim();
    if (!token) {
      throw unauthorized('Missing access token');
    }
    const payload = verifyAccessToken(token);
    req.userId = payload.sub;
    next();
  } catch (err) {
    if (err.name === 'TokenExpiredError') {
      return next(unauthorized('Access token expired'));
    }
    if (err.name === 'JsonWebTokenError') {
      return next(unauthorized('Invalid access token'));
    }
    next(err.status ? err : unauthorized(err.message || 'Unauthorized'));
  }
}

module.exports = { requireAuth };
